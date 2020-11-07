package io.github.baijianruoli.lidou.config;

import io.github.baijianruoli.lidou.util.BaseRequest;
import io.github.baijianruoli.lidou.code.ServerDecode;
import io.github.baijianruoli.lidou.code.ServerEncode;
import io.github.baijianruoli.lidou.handler.ClientHandler;
import io.github.baijianruoli.lidou.util.PathUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class InitRpcConfig  implements CommandLineRunner {

    private static ExecutorService executor = Executors.newFixedThreadPool(1);
    private static ClientHandler clientHandler;
    @Autowired
    private ApplicationContext applicationContext;
    @Value("${lidou.port}")
    private Integer port;
    @Value("${lidou.url}")
    private String url;
    @Autowired
    private InitRpcConfig initRpcConfig;
    @Autowired
    private ZkClient zkClient;


    public InitRpcConfig() {
    }

    public Object getBean(final Class<?> serviceClass, final Object o) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{serviceClass}, (proxy, method, args) -> {
            BaseRequest baseRequest = new BaseRequest((String)o, method.getName(), args, method.getParameterTypes());
           //负载均衡
            //获得zookeeper路径
            String path= PathUtils.addZkPath(serviceClass.getName());
            List<String> children = zkClient.getChildren(path);
            //随机算法
            path+="/"+children.get(new Random().nextInt(children.size()));
            String temp = (String)zkClient.readData(path);
            String[] split = temp.split(":");
            NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
            clientHandler = new ClientHandler();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(bossGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast(new ServerEncode());
                    pipeline.addLast(new ServerDecode());
                    pipeline.addLast(new IdleStateHandler(80L, 80L, 80L, TimeUnit.SECONDS));
                    pipeline.addLast(clientHandler);

                }
            });
            System.out.println(split[0]+"   "+split[1]);
            ChannelFuture future1 = bootstrap.connect(split[0], Integer.valueOf(split[1])).sync();
            future1.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if(channelFuture.isSuccess())
                        System.out.println("ok");
                    else
                        System.out.println("fail");
                }
            });
            clientHandler.setPars(baseRequest);
            return executor.submit(clientHandler).get();
        });
    }

    public void init() throws IllegalAccessException {
        Map<String, Object> beansWithAnnotation = this.applicationContext.getBeansWithAnnotation(Controller.class);
       for(Object bean:beansWithAnnotation.values())
       {
           Field[] fields = bean.getClass().getDeclaredFields();
          for(Field f:fields)
          {
              f.setAccessible(true);
              if (f.isAnnotationPresent(Reference.class)) {
                  Class<?> type = f.getType();
                  //获得代理对象
                  Object bean1 = this.initRpcConfig.getBean(type, type.getName());
                  //设置属性值
                  f.set(bean, bean1);
              }
          }
           }
       }

    public void run(String... args) throws Exception {
       init();

    }
}
