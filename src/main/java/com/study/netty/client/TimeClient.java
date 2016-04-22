/**
 * <p>Copyright: Copyright (c) 2016</p>
 */
package com.study.netty.client;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.study.netty.client.handler.TimeClientHandler;
import com.study.netty.server.handler.EchoServerHandler;

/**
 * Description: TODO {时间协议客户端}<br/>
 *
 * @author zhanglong
 * @date: 2016年4月21日 下午3:31:11
 * @version 1.0
 * @since JDK 1.7
 * 
 * 
 * 
 *        1)
 *        使用NioClientSocketChannelFactory而不是NioServerSocketChannelFactory来创建客户端的Channel通道对象。
 * 
 *        2) 客户端的ClientBootstrap对应ServerBootstrap。
 * 
 *        3) 请注意，这里不存在使用“child.”前缀的配置项，客户端的SocketChannel实例不存在父级Channel对象。
 * 
 *        4) 我们应当调用connect连接方法，而不是之前的bind绑定方法。
 */
public class TimeClient {

    
    private static final int PORT = 8080;
    private static final String LOCALHOST = "127.0.0.1";

    public static void main(String[] args) {
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService workers = Executors.newCachedThreadPool();
        NioClientSocketChannelFactory factory = new NioClientSocketChannelFactory(boss, workers);
        ClientBootstrap bootstrap = new ClientBootstrap(factory);
        TimeClientHandler clientHandler = new TimeClientHandler();
        ChannelPipeline pipeline = bootstrap.getPipeline();
        pipeline.addLast("handler", clientHandler);
        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("keepAlive", true);
        bootstrap.connect(new InetSocketAddress(LOCALHOST,PORT));
    }
}
