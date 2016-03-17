package com.panda.netty.wsserver;

import com.panda.netty.common.message.Message;
import com.panda.netty.wsserver.common.DefaultWsServer;

/**
 * 
 * @author chenlj
 * @Date 2016年3月5日 下午6:27:16
 */
public class Server {
	private DefaultWsServer defaultServer;

	public Server(String serverName, int serverPort) {
		defaultServer = new DefaultWsServer(serverName, serverPort);
	}

	public void start() {
		defaultServer.start();
	}

	public void sendMessage(String clientId, Message<Object> message) {
		defaultServer.sendMessage(clientId, message);
	}

	public void close() {
		defaultServer.close();
	}

	public static void main(String[] args) {
		Server server = new Server("WebSocket服务", 9200);
		server.start();
	}
}
