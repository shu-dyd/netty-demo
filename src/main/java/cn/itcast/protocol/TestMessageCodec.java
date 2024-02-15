package cn.itcast.protocol;

import cn.itcast.message.LoginRequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;


public class TestMessageCodec {
    public static void main(String[] args) throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024, 12, 4, 0, 0),
                new LoggingHandler(),
                new MessageCodec());

        // 测试encode(写出去)
        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");
//        channel.writeOutbound(message);

        // 测试decode(写进来)
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null, message, buf);
//        channel.writeInbound(buf);

        // 验证是否解决拆包
        ByteBuf s1 = buf.slice(0, 100);
        ByteBuf s2 = buf.slice(100, buf.readableBytes() - 100);
        s1.retain();
        channel.writeInbound(s1);
        channel.writeInbound(s2);

    }
}