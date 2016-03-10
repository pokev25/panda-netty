package com.panda.netty.server.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;

import com.panda.netty.common.entity.ClientUser;

public class ChannelManager {
	private ConcurrentHashMap<String, ChannelHandlerContext> channels = new ConcurrentHashMap<String, ChannelHandlerContext>();

	// private ChannelGroup channels = new
	// DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	// netty会自动生成channelId 目前通道ID采用自己生成的

	public void addChannelContext(ChannelHandlerContext ctx, ClientUser user) {
		ctx.attr(ClientUser.CLIENT_ID).set(user.getId());
		ctx.attr(ClientUser.CLIENT_USER).set(user);
		channels.put(user.getId(), ctx);
	}

	public boolean isUserAlreadyLogin(String clientId) {
		return channels.get(clientId) != null ? true : false;
	}

	public void removeChannel(ChannelHandlerContext ctx) {
		String clientId = ctx.attr(ClientUser.CLIENT_ID).get();
		if (clientId == null) {
			return;
		}
		channels.remove(clientId, ctx);
	}

	public Channel getChannelById(String clientId) {
		ChannelHandlerContext ctx = channels.get(clientId);
		return ctx == null ? null : ctx.channel();
	}

	public ChannelHandlerContext getChannelHandlerContextByClientId(String clientId) {
		return channels.get(clientId);
	}

	public int getSize() {
		return channels.size();
	}
}
