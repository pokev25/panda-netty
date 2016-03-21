package com.panda.netty.client.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.panda.netty.client.common.DefaultClient;
import com.panda.netty.common.message.Message;
import com.panda.netty.common.util.CtxUtil;

@Sharable
public class NettyClientHandler extends ChannelHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);
    private DefaultClient       client;

    public NettyClientHandler(DefaultClient client){
        this.client = client;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("client channelActive-->clientId:{}", CtxUtil.getClientId(ctx));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("client channelInactive-->clientId:{}", CtxUtil.getClientId(ctx));
        client.reConnect();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("client channelReadComplete -->clientId:{}", CtxUtil.getClientId(ctx));
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 客户端接收服务端数据
        Message<Object> message = (Message<Object>) msg;
        Object messageBody = message.getBody();
        // if (messageBody instanceof LoginMessage) {
        // LoginMessage ms = (LoginMessage) messageBody;
        // ClientUser client = new ClientUser(ms.getUserId(), ms.getUserName(),
        // "");
        // channelManager.addChannelContext(ctx, client);
        // }
        logger.info("server channel read->messageId:{}", message.getMessageId());
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        logger.info("client disconnect -->clientId:{}", CtxUtil.getClientId(ctx));
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        logger.info("client close -->clientId:{}", CtxUtil.getClientId(ctx));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("client exceptionCaught -->clientId:" + CtxUtil.getClientId(ctx), cause);
        // ctx.close();
    }

}
