package com.panda.netty.common.util;

import com.panda.netty.common.entity.ClientUser;

import io.netty.channel.ChannelHandlerContext;

public class CtxUtil {
	/**
	 * 获取clientId
	 * 
	 * @author chenlj
	 * @Date 2016 下午3:58:28
	 */
	public static String getClientId(ChannelHandlerContext ctx) {
		return ctx.attr(ClientUser.CLIENT_ID).get();
	}

	/**
	 * 获取clientUser
	 * 
	 * @author chenlj
	 * @Date 2016 下午3:59:09
	 */
	public static ClientUser getClientUser(ChannelHandlerContext ctx) {
		return ctx.attr(ClientUser.CLIENT_USER).get();
	}
}
