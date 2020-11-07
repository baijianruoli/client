
# client
lidou的客户端

# lidou
基于spring boot和netty的自制rpc框架（客户端）

# 使用方法

1.服务端和客户端的Service全路径一定要一样

2.像@Autowried一样使用@Renference，自动注入代理对象（现在只能注入标记了@Controller或者@RestController类的属性）

3.在配置文件加上lidou.zookeeper.url，为zookeeper的注册地址

# 实现需求
1. jdk动态代理，获得调用方法的类全路径，方法参数，类型，和名称
2. protobuf序列化传输数据
3. netty进行服务间的通信
4. 多线程
5. 反射
6. 心跳检测
7. 自定义注解（现在客户端只要在Service上加上@Reference，自动注入代理对象，像调用本地方法一样调用远程方法）
8. 加入zookeeper注册中心，提供简单的负载均衡

# 缺少的功能
1. 加入maven中央仓库，方便使用

# 压测
2w的压力测试下，tcp响应时间为一百多毫秒，多线程可以继续改进

