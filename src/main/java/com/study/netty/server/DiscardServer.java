/**
 * <p>Copyright: Copyright (c) 2016</p>
 */
package com.study.netty.server;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.study.netty.server.handler.DiscardServerHandler;

/**
 * Description: TODO {包含DiscardServerHandler处理器服务的主方法}<br/>
 *
 * @author zhanglong
 * @date: 2016年4月21日 上午10:57:15
 * @version 1.0
 * @since JDK 1.7
 * 
 *        1)ChannelFactory 是一个创建和管理Channel通道及其相关资源的工厂接口，它处理所有的I/O请求并产生相应的I/O
 *        ChannelEvent通道事件。Netty 提供了多种 ChannelFactory
 *        实现。这里我们需要实现一个服务端的例子，因此我们使用NioServerSocketChannelFactory实现。
 *        另一件需要注意的事情是这个工厂并自己不负责创建I/O线程。你应当在其构造器中指定该工厂使用的线程池，
 *        这样做的好处是你获得了更高的控制力来管理你的应用环境中使用的线程，例如一个包含了安全管理的应用服务。
 * 
 *        2)ServerBootstrap
 *        是一个设置服务的帮助类。你甚至可以在这个服务中直接设置一个Channel通道。然而请注意，这是一个繁琐的过程，大多数情况下并不需要这样做。
 * 
 *        3)这里，我们将DiscardServerHandler处理器添加至默认的ChannelPipeline通道。
 *        任何时候当服务器接收到一个新的连接，一个新的ChannelPipeline管道对象将被创建，
 *        并且所有在这里添加的ChannelHandler对象将被添加至这个新的ChannelPipeline管道对象。这很像是一种浅拷贝操作（a
 *        shallow-copy operation）；
 *        所有的Channel通道以及其对应的ChannelPipeline实例将分享相同的DiscardServerHandler实例。
 * 
 *        4)你也可以设置我们在这里指定的这个通道实现的配置参数。我们正在写的是一个TCP/IP服务，因此我们运行设定一些socket选项，
 *        例如tcpNoDelay和keepAlive。请注意我们在配置选项里添加的"child."前缀。
 *        这意味着这个配置项仅适用于我们接收到的通道实例，而不是ServerSocketChannel实例。因此，
 *        你可以这样给一个ServerSocketChannel设定参数： bootstrap.setOption("reuseAddress",
 *        true);
 * 
 *        5)我们继续。剩下要做的是绑定这个服务使用的端口并且启动这个服务。这里，我们绑定本机所有网卡（NICs,network interface
 *        cards）上的8080端口。当然，你现在也可以对应不同的绑定地址多次调用绑定操作。
 */
public class DiscardServer {

    public static void main(String[] args) {
        ExecutorService boss = Executors.newCachedThreadPool(); 
        ExecutorService works = Executors.newCachedThreadPool();
        NioServerSocketChannelFactory factory = new NioServerSocketChannelFactory(boss, works);//(1)
        ServerBootstrap bootstrap = new ServerBootstrap(factory);//(2)
        DiscardServerHandler discardServerHandler = new DiscardServerHandler();
        ChannelPipeline pipeline = bootstrap.getPipeline();
        pipeline.addLast("handler", discardServerHandler);//(3)
        //(4)
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        
        //(5)
        bootstrap.bind(new InetSocketAddress(8080));
    }

}
