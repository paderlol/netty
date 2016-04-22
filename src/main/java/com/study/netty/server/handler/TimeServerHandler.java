/**
 * <p>Copyright: Copyright (c) 2016</p>
 */
package com.study.netty.server.handler;

import static org.jboss.netty.buffer.ChannelBuffers.*;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.ChannelHandler.Sharable;

/**
 * Description: TODO {时间协议服务}<br/>
 *
 * @author zhanglong
 * @date: 2016年4月21日 下午2:48:42
 * @version 1.0
 * @since JDK 1.7
 *        服务端返回一个32位的整数消息，我们不接受请求中包含的任何数据并且当消息返回完毕后立即关闭连接。通过这个例子你将学会如何构建和发送消息，
 *        以及当完成处理后如何主动关闭连接。
 * 
 *        因为我们会忽略接收到的任何数据而只是返回消息，这应当在建立连接后就立即开始。因此这次我们不再使用messageReceived方法，
 *        取而代之的是使用channelConnected方法。
 * 
 * 
 * 
 *        1) 正如我们解释过的，channelConnected方法将在一个连接建立后立即触发。因此让我们在这个方法里完成一个代表当前时间（秒）
 *        的32位整数消息的构建工作。
 * 
 *        2) 为了发送一个消息，我们需要分配一个包含了这个消息的buffer缓冲。因为我们将要写入一个32位的整数，因此我们需要一个4字节的
 *        ChannelBuffer。ChannelBuffers是一个可以创建buffer缓冲的帮助类。除了这个buffer方法，
 *        ChannelBuffers还提供了很多和ChannelBuffer相关的实用方法。更多信息请参考API手册。
 * 
 *        另外，一个很不错的方法是使用静态的导入方式： import static
 *        org.jboss.netty.buffer.ChannelBuffers.*; ... ChannelBuffer dynamicBuf
 *        = dynamicBuffer(256); ChannelBuffer ordinaryBuf = buffer(1024);
 * 
 *        3) 像通常一样，我们需要自己构造消息。
 * 
 *        但是打住，flip在哪？过去我们在使用NIO发送消息时不是常常需要调用
 *        ByteBuffer.flip()方法吗？实际上ChannelBuffer之所以不需要这个方法是因为
 *        ChannelBuffer有两个指针；一个对应读操作，一个对应写操作。当你向一个
 *        ChannelBuffer写入数据的时候写指针的索引值便会增加，但与此同时读指针的索引值不会有任何变化。
 *        读写指针的索引值分别代表了这个消息的开始、结束位置。
 * 
 *        与之相应的是，NIO的buffer缓冲没有为我们提供如此简洁的一种方法，除非你调用它的flip方法。因此，
 *        当你忘记调用flip方法而引起发送错误时，你便会陷入困境。这样的错误不会再Netty中发生，因为我们对应不同的操作类型有不同的指针。
 *        你会发现就像你已习惯的这样过程变得更加容易—一种没有flippling的体验！
 * 
 *        另一点需要注意的是这个写方法返回了一个ChannelFuture对象。一个ChannelFuture
 *        对象代表了一个尚未发生的I/O操作。这意味着，任何已请求的操作都可能是没有被立即执行的，因为在Netty内部所有的操作都是异步的。例如，
 *        下面的代码可能会关闭一 个连接，这个操作甚至会发生在消息发送之前：
 * 
 *        Channel ch = ...; ch.write(message); ch.close();
 * 
 *        因此，你需要这个write方法返回的ChannelFuture对象，close方法需要等待写操作异步完成之后的ChannelFuture通知
 *        /监听触发。需要注意的是，关闭方法仍旧不是立即关闭一个连接，它同样也是返回了一个ChannelFuture对象。
 * 
 *        4) 在写操作完成之后我们又如何得到通知？
 *        这个只需要简单的为这个返回的ChannelFuture对象增加一个ChannelFutureListener
 *        即可。在这里我们创建了一个匿名ChannelFutureListener对象，
 *        在这个ChannelFutureListener对象内部我们处理了异步操作完成之后的关闭操作。
 * 
 *        另外，你也可以通过使用一个预定义的监听类来简化代码。 f.addListener(ChannelFutureListener.CLOSE);
 */
@Sharable
public class TimeServerHandler extends SimpleChannelHandler {

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        Channel channel = e.getChannel();
        ChannelBuffer time = buffer(4);
        time.writeInt((int) (System.currentTimeMillis() / 1000L+2208988800L));
        ChannelFuture f = channel.write(time);
        f.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                // TODO Auto-generated method stub
                Channel ch = future.getChannel();
                ch.close();
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
