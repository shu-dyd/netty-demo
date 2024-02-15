package cn.itcast.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import cn.itcast.message.Message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

@Slf4j
public class MessageCodec extends ByteToMessageCodec<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        // 魔数（我定义为4个字节）
        out.writeBytes(new byte[]{'d', 'o', 'n', 'g'});
        // 版本号（我定义为1个字节）
        out.writeByte(1);
        // 序列化方式 jdk 0, json 1
        out.writeByte(0);
        // 指令类型（我定义为1个字节）
        out.writeByte(msg.getMessageType());
        // 请求序号（我定义为4个字节）
        out.writeInt(msg.getSequenceId());
        // 无意义，对齐填充（一般要求字节数加起来是2的n次方倍）
        out.writeByte(0xff);
        // 正文长度（因为Message实现了Serializable，都具备了序列化的能力）
        // 先将Message对象序列化成字节数组（此处可以复用在其他场景）
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(msg);
        byte[] bytes = bos.toByteArray();
        // 于是长度
        out.writeInt(bytes.length);
        // 写入内容
        out.writeBytes(bytes);

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magicNum = in.readInt();
        byte version = in.readByte();
        byte serializerType = in.readByte();
        byte messageType = in.readByte();
        int sequenceId = in.readInt();
        // 跳过对齐字节
        in.readByte();
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);
        // 如果用的是jdk的序列化方式
        if(serializerType == 0){
            // 将字节数组反序列化成Message对象
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            Message message = (Message) ois.readObject();
            out.add(message);
            log.debug("{}", message);
        }
    }
}
