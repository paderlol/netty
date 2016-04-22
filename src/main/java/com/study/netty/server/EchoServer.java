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

import com.study.netty.server.handler.EchoServerHandler;

/** 
 * Description: TODO {响应协议启动服务}<br/>
 *
 * @author zhanglong
 * @date: 2016年4月21日 下午2:36:32
 * @version 1.0
 * @since JDK 1.7
 */
public class EchoServer {

    
    public static void main(String[] args) {
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService workers = Executors.newCachedThreadPool();
        NioServerSocketChannelFactory factory = new NioServerSocketChannelFactory(boss, workers);
        ServerBootstrap bootstrap = new ServerBootstrap(factory);
        EchoServerHandler echoServerHandler = new EchoServerHandler();
        ChannelPipeline pipeline = bootstrap.getPipeline();
        pipeline.addLast("handler", echoServerHandler);
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.bind(new InetSocketAddress(8080));
    }
}
