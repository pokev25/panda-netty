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
import io.netty.handler.timeout.IdleStateHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.panda.netty.common.message.Message;
import com.panda.netty.common.util.StringUtil;
import com.panda.netty.common.util.UUIDUtil;
import com.panda.netty.server.channel.ChannelManager;
import com.panda.netty.server.config.ServerConfig;
import com.panda.netty.server.handler.HeaderDecoder;
import com.panda.netty.server.handler.HeaderEncoder;
import com.panda.netty.server.handler.HeartBeatHandler;
import com.panda.netty.server.handler.IpFilterFactory;
import com.panda.netty.server.handler.NettyServerHandler;

/**
 * 
 * @author chenlj
 * @Date 2016年3月5日 下午6:27:16
 */
public class DefaultServer extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(DefaultServer.class);
	private int serverPort = ServerConfig.PORT; // 默认端口
	private String serverName;
	private ChannelManager channelManager;
	private NettyServerHandler handler;
	// 假设客户端心跳时间是每10秒发送一次，那heartBeatTime可设置为30秒,即3次发送后服务端仍未收到则视为客户端断开连接
	private int heartBeatTime = ServerConfig.HEARTBEAT_TIME; // 心跳时间，单位秒
	private boolean isKickNewClientWithSameId = true; // 是否踢出新登录的同名用户，如果为false则踢出老的同名用户

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
				ch.pipeline().addLast(IpFilterFactory.createRuleBasedIpFilter()).addLast(new IdleStateHandler(heartBeatTime, heartBeatTime, heartBeatTime))
						.addLast(new HeartBeatHandler()).addLast(new HeaderDecoder()).addLast(new HeaderEncoder()).addLast(handler);
			}
		});
		initOptionSet(serverBootstrap);
	}

	private void initOptionSet(ServerBootstrap serverBootstrap) {
		serverBootstrap.option(ChannelOption.SO_BACKLOG, ServerConfig.SO_BACKLOG);
		serverBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, ServerConfig.CONNECT_TIMEOUT_MILLIS);
		serverBootstrap.option(ChannelOption.SO_TIMEOUT, ServerConfig.SO_TIMEOUT);
		serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, ServerConfig.SO_KEEPALIVE);
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

	public int getHeartBeatTime() {
		return heartBeatTime;
	}

	public void setHeartBeatTime(int heartBeatTime) {
		this.heartBeatTime = heartBeatTime;
	}

	public void setKickNewClientWithSameId(boolean isKickNewClientWithSameId) {
		this.isKickNewClientWithSameId = isKickNewClientWithSameId;
	}

	public boolean isKickNewClientWithSameId() {
		return isKickNewClientWithSameId;
	}

}
