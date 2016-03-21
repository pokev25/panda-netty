package com.panda.netty.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.panda.netty.common.util.CtxUtil;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartBeatHandler extends ChannelHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(HeartBeatHandler.class);

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			switch (e.state()) {
			case READER_IDLE:
				logger.warn("客户端{}断网，服务端关闭此连接", CtxUtil.getClientId(ctx));
				ctx.close();
				break;
			case WRITER_IDLE:
				break;
			case ALL_IDLE:
			}
		}
	}
}
