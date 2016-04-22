/**
 * <p>Copyright: Copyright (c) 2016</p>
 */
package com.study.netty.client.handler;

import static org.jboss.netty.buffer.ChannelBuffers.*;
import java.util.Date;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * Description: TODO {不共享的处理器}<br/>
 *
 * @author zhanglong
 * @date: 2016年4月22日 上午10:04:46
 * @version 1.0
 * @since JDK 1.7
 * 
 * 
 * 
 *        1)动态缓冲区是一个channelbuffer增加需求的能力。当你不知道消息的长度时，它是非常有用的。
 * 
 *        2）首先，所有接收到的数据应该被累积到buf。
 *        3）然后，处理器必须检查buf是否有足够的数据，在这个例子中是4个字节，并进行实际的业务逻辑。否则，
 *        网络会叫messagereceived方法时再更多的数据到达，最终所有4字节将累积。
 * 
 */
public class NotSharableTimeClientHandler extends SimpleChannelHandler {

    private final ChannelBuffer buf = dynamicBuffer();//1)

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        ChannelBuffer msg = (ChannelBuffer) e.getMessage();
        buf.writeBytes(msg);//2)
        if(buf.readableBytes()>=4){//3)
            long currentTimeMillis = buf.readInt() * 1000L;
            System.out.println(new Date(currentTimeMillis));
            e.getChannel().close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
