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

import com.study.netty.server.handler.TimeServerHandler;

/**
 * Description: TODO {时间协议服务}<br/>
 *
 * @author zhanglong
 * @date: 2016年4月21日 下午3:18:35
 * @version 1.0
 * @since JDK 1.7
 * 
 *        实现的协议是TIME协议 。这是一个与先前所介绍的不同的例子。这个例子里，服务端返回一个32位的整数消息，
 *        我们不接受请求中包含的任何数据并且当消息返回完毕后立即关闭连接。通过这个例子你将学会如何构建和发送消息，以及当完成处理后如何主动关闭连接
 */
public class TimeServer {

    public static void main(String[] args) {
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();
        NioServerSocketChannelFactory factory = new NioServerSocketChannelFactory(boss, worker);
        ServerBootstrap bootstrap = new ServerBootstrap(factory);
        TimeServerHandler timeServerHandler = new TimeServerHandler();
        ChannelPipeline pipeline = bootstrap.getPipeline();
        pipeline.addLast("handler", timeServerHandler);
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.bind(new InetSocketAddress(8080));
    }
}
