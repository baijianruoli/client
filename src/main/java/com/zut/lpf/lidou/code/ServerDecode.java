package com.zut.lpf.lidou.code;

import com.zut.lpf.lidou.util.BaseResponse;
import com.zut.lpf.lidou.util.ProtostuffUtils;
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
<<<<<<< HEAD

=======
        System.out.println("服务端回复的信息:" + deserialize);
>>>>>>> e7d4a3a3214942912f9320ae14ff5bd9c310acbe
        list.add(deserialize);
    }
}
