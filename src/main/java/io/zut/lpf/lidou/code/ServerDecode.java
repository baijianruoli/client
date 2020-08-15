package io.zut.lpf.lidou.code;

import io.zut.lpf.lidou.util.BaseResponse;
import io.zut.lpf.lidou.util.ProtostuffUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ServerDecode extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        BaseResponse deserialize = (BaseResponse) ProtostuffUtils.deserialize(bytes, BaseResponse.class);
        System.out.println("服务端回复的信息:" + deserialize);
        list.add(deserialize);
    }
}
