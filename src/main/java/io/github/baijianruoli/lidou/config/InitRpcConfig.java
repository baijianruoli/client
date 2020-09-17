package io.github.baijianruoli.lidou.config;

import io.github.baijianruoli.lidou.util.BaseRequest;
import io.github.baijianruoli.lidou.code.ServerDecode;
import io.github.baijianruoli.lidou.code.ServerEncode;
import io.github.baijianruoli.lidou.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class InitRpcConfig  implements CommandLineRunner {
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static ClientHandler clientHandler;
    @Autowired
    private ApplicationContext applicationContext;
    @Value("${lidou.port}")
    private Integer port;
    @Value("${lidou.url}")
    private String url;
    @Autowired
    private InitRpcConfig initRpcConfig;

    public InitRpcConfig() {
    }

    public Object getBean(final Class<?> serviceClass, final Object o) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{serviceClass}, (proxy, method, args) -> {
            BaseRequest baseRequest = new BaseRequest((String)o, method.getName(), args, method.getParameterTypes());
            clientHandler.setPars(baseRequest);
            /*System.out.println(clientHandler.getPars());*/
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
                  Object bean1 = this.initRpcConfig.getBean(type, type.getName());
                  f.set(bean, bean1);
              }
          }

           }
       }

    public void run(String... args) throws Exception {
       init();
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
        ChannelFuture localhost = bootstrap.connect(url, port).sync();
    }
}
