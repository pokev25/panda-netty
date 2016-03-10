package com.panda.netty.client.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.panda.netty.common.util.CtxUtil;

@Sharable
public class NettyClientHandler extends ChannelHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("client channelActive-->clientId:{}", CtxUtil.getClientId(ctx));
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("client channelInactive-->clientId:{}", CtxUtil.getClientId(ctx));
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		logger.info("client channelReadComplete -->clientId:{}", CtxUtil.getClientId(ctx));
		ctx.flush();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		super.channelRead(ctx, msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.info("client exceptionCaught -->clientId:" + CtxUtil.getClientId(ctx), cause);
		ctx.close();
	}

}
