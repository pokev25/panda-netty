package com.panda.netty.server;

import com.panda.netty.common.message.Message;
import com.panda.netty.server.common.DefaultServer;


/**
 * 
 * @author chenlj
 * @Date 2016年3月5日 下午6:27:16
 */
public class Server {
	private DefaultServer defaultServer;

	public Server(String serverName, int serverPort) {
		defaultServer = new DefaultServer(serverName, serverPort);
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
		Server server = new Server("服务1", 9100);
		server.start();
	}
}
