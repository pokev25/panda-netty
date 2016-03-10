package com.panda.netty.server.common;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.panda.netty.common.message.Message;
import com.panda.netty.common.util.StringUtil;
import com.panda.netty.common.util.UUIDUtil;
import com.panda.netty.server.channel.ChannelManager;
import com.panda.netty.server.handler.HeaderDecoder;
import com.panda.netty.server.handler.NettyServerHandler;

/**
 * 
 * @author chenlj
 * @Date 2016年3月5日 下午6:27:16
 */
public class DefaultServer extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(DefaultServer.class);
	private int serverPort = 9100; // 默认端口
	private String serverName;
	private ChannelManager channelManager;
	private NettyServerHandler handler;

	private EventLoopGroup bossGroup;
	private EventLoopGroup workGroup;
	private ServerBootstrap serverBootstrap;

	public DefaultServer(String serverName, int serverPort) {
		this.serverName = serverName;
		this.serverPort = serverPort;
		channelManager = new ChannelManager();
		handler = new NettyServerHandler(channelManager);
		bossGroup = new NioEventLoopGroup();
		workGroup = new NioEventLoopGroup();
		serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new HeaderDecoder()).addLast(handler);
			}
		}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
	}

	@Override
	public void run() {
		ChannelFuture channelFuture = null;
		try {
			channelFuture = serverBootstrap.bind(serverPort).sync();
			logger.info("server start --> name:{},port:{}", serverName, serverPort);
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			logger.error("[" + serverName + "] server start error", e);
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}

	public void close() {
		bossGroup.shutdownGracefully();
		workGroup.shutdownGracefully();
	}

	public void sendMessage(final String clientId, Message<Object> message) {
		if (StringUtil.isEmpty(message.getMessageId())) {
			message.setMessageId(UUIDUtil.create());
		}
		Channel channel = channelManager.getChannelById(clientId);
		if (channel != null && channel.isActive()) {
			channel.writeAndFlush(message);
		}
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public ChannelManager getChannelManager() {
		return channelManager;
	}

	public void setChannelManager(ChannelManager channelManager) {
		this.channelManager = channelManager;
	}

}
