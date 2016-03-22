package com.panda.netty.client.common;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.panda.netty.client.handler.HeaderDecoder;
import com.panda.netty.client.handler.HeaderEncoder;
import com.panda.netty.client.handler.NettyClientHandler;
import com.panda.netty.common.message.Message;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class DefaultClient {

	private static final Logger logger = LoggerFactory.getLogger(DefaultClient.class);
	private String serverHost = "127.0.0.1";
	private int serverPort = 9100;
	private NettyClientHandler handler;
	private int reloginWaitSeconds = 5; // 重连等待时间(默认隔1秒重连一次)

	private EventLoopGroup workGroup;
	private Bootstrap bootstrap;
	private Channel channel; // 客户端通信通道

	public DefaultClient(String serverHost, int serverPort) {
		this.serverHost = serverHost;
		this.serverPort = serverPort;
		handler = new NettyClientHandler(this);
		workGroup = new NioEventLoopGroup();
		bootstrap = new Bootstrap();
		bootstrap.group(workGroup);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000);
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new HeaderEncoder()).addLast(new HeaderDecoder()).addLast(handler);
			}
		});
	}

	public void connect() {
		ChannelFuture future = null;
		try {
			future = bootstrap.connect(serverHost, serverPort).sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		channel = future.channel();
	}

	/**
	 * 重连
	 * 
	 * @author chenlj
	 * @Date 2016 下午4:01:46
	 */
	public void reConnect() {
		// if (channel.isActive()) {
		// return;
		// }
		// if (reloginWaitSeconds < 0) {
		// return;
		// }
		channel.eventLoop().schedule(new Runnable() {
			@Override
			public void run() {
				bootstrap.connect(serverHost, serverPort).addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						if (!future.isSuccess()) {
							logger.warn("client connect failed");
							future.channel().eventLoop().schedule(new Runnable() {
								@Override
								public void run() {
									reConnect();
								}
							}, reloginWaitSeconds, TimeUnit.SECONDS);
						} else {
							channel = future.channel();
							logger.info("client connect-->host:{},port:{}", serverHost, serverPort);
						}
					}
				});
			}
		}, reloginWaitSeconds, TimeUnit.SECONDS);
	}

	@SuppressWarnings("rawtypes")
	public void sendMessage(Message message) {
		channel.writeAndFlush(message);
	}

	public void close() {
		workGroup.shutdownGracefully();
	}

	public int getReloginWaitSeconds() {
		return reloginWaitSeconds;
	}

	public void setReloginWaitSeconds(int reloginWaitSeconds) {
		this.reloginWaitSeconds = reloginWaitSeconds;
	}

}
