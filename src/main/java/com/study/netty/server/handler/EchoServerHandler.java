/**
 * <p>Copyright: Copyright (c) 2016</p>
 */
package com.study.netty.server.handler;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.ChannelHandler.Sharable;

/**
 * Description: TODO {响应协议服务}<br/>
 *
 * @author zhanglong
 * @date: 2016年4月21日 下午2:34:56
 * @version 1.0
 * @since JDK 1.7 //
 *        一个ChannelEvent通道事件对象自身存有一个和其关联的Channel对象引用。这个返回的Channel通道对象代表了这个接收 //
 *        MessageEvent消息事件的连接（connection）。因此，
 *        我们可以通过调用这个Channel通道对象的write方法向远程节点写入返回数据
 *        
 *        
 *        
 */
@Sharable
public class EchoServerHandler extends SimpleChannelHandler {

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

        Channel channel = e.getChannel();
        channel.write(e.getMessage());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {// (4)
        e.getCause().printStackTrace();
        Channel channel = e.getChannel();
        channel.close();
    }
}
