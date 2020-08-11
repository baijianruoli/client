package com.zut.lpf.lidou.handler;

import com.zut.lpf.lidou.util.BaseRequest;
import com.zut.lpf.lidou.util.BaseResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
@Data
<<<<<<< HEAD
public class ClientHandler extends ChannelInboundHandlerAdapter implements Callable {
    private ChannelHandlerContext context;

    private  Object result;
=======
public class ClientHandler  extends ChannelInboundHandlerAdapter implements Callable {
    private ChannelHandlerContext context;
    private BaseResponse result;
>>>>>>> e7d4a3a3214942912f9320ae14ff5bd9c310acbe
    private BaseRequest pars;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush(new BaseRequest("heart"));
            }
        }

    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        log.error("{}异常", cause.getMessage());
    }
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
<<<<<<< HEAD
        this.result =((BaseResponse) msg).getData();
=======
        this.result = (BaseResponse)msg;
>>>>>>> e7d4a3a3214942912f9320ae14ff5bd9c310acbe
        this.notify();
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.context = ctx;
    }

    public synchronized Object call() throws Exception {
        this.context.writeAndFlush(this.pars);
        this.wait();
        return this.result;
    }
}
