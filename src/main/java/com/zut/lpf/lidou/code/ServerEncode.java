package com.zut.lpf.lidou.code;

import com.zut.lpf.lidou.util.BaseRequest;
import com.zut.lpf.lidou.util.ProtostuffUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ServerEncode extends MessageToByteEncoder<BaseRequest> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(ProtostuffUtils.serialize(baseRequest));
    }
}
