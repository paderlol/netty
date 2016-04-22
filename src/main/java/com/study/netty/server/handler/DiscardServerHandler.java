/**
 * <p>Copyright: Copyright (c) 2016</p>
 */
package com.study.netty.server.handler;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * Description: TODO {抛弃协议}<br/>
 *
 * @author zhanglong
 * @date: 2016年4月21日 上午10:49:48
 * @version 1.0
 * @since JDK 1.7
 *        1)ChannelPipelineCoverage注解了一种处理器类型，这个注解标示了一个处理器是否可被多个Channel通道共享（
 *        同时关联着ChannelPipeline）。DiscardServerHandler没有处理任何有状态的信息，因此这里的注解是“all”。
 * 
 *        2)DiscardServerHandler继承了SimpleChannelHandler，这也是一个ChannelHandler
 *        的实现。SimpleChannelHandler提供了多种你可以重写的事件处理方法。
 *        目前直接继承SimpleChannelHandler已经足够了，并不需要你完成一个自己的处理器接口。
 * 
 *        3)我们这里重写了messageReceived事件处理方法。这个方法由一个接收了客户端传送数据的MessageEvent事件调用。
 *        在这个例子中，我们忽略接收到的任何数据，并以此来实现一个抛弃协议（DISCARD protocol）。
 * 
 *        4)exceptionCaught
 *        事件处理方法由一个ExceptionEvent异常事件调用，这个异常事件起因于Netty的I/O异常或一个处理器实现的内部异常。多数情况下，
 *        捕捉到的异常应当被记录下来，并在这个方法中关闭这个channel通道。当然处理这种异常情况的方法实现可能因你的实际需求而有所不同，例如，
 *        在关闭这个连接之前你可能会发送一个包含了错误码的响应消息。
 * 
 * 
 */
@Sharable // (1)
public class DiscardServerHandler extends SimpleChannelHandler {// (2)

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {// (3)
        ChannelBuffer buf = (ChannelBuffer) e.getMessage();
        // TODO
        // 由于这是一个丢球协议服务，所以实际上我们无法真正的知道。你最终将收不到任何回应。为了证明它在真正的工作，让我们修改代码打印其接收到的数据。
         while (buf.readable()) {
         System.out.println((char)buf.readByte());
         }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {// (4)
        e.getCause().printStackTrace();
        Channel channel = e.getChannel();
        channel.close();
    }
}
