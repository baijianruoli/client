package com.zut.lpf.lidou.code;

import com.zut.lpf.lidou.util.BaseRequest;
import com.zut.lpf.lidou.util.ProtostuffUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ServerEncode extends MessageToByteEncoder<BaseRequest> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest, ByteBuf byteBuf) throws Exception {
<<<<<<< HEAD
=======
        System.out.println("发送信息：" + baseRequest);
>>>>>>> e7d4a3a3214942912f9320ae14ff5bd9c310acbe
        byteBuf.writeBytes(ProtostuffUtils.serialize(baseRequest));
    }
}
