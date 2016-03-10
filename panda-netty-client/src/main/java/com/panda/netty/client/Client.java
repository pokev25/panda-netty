package com.panda.netty.client;

import com.panda.netty.client.common.DefaultClient;
import com.panda.netty.common.message.LoginMessage;
import com.panda.netty.common.message.Message;
import com.panda.netty.common.util.UUIDUtil;


public class Client {
	private DefaultClient defaultClient;

	public Client(String serverHost, int serverPort) {
		defaultClient = new DefaultClient(serverHost, serverPort);
	}

	public void connect() {
		defaultClient.connect();
	}

	@SuppressWarnings("rawtypes")
	public void sendMessage(Message message) {
		defaultClient.sendMessage(message);
	}

	public void close() {
		defaultClient.close();
	}

	public static void main(String[] args) {
		Client client = new Client("127.0.0.1", 9100);
		client.connect();
		LoginMessage lmBody = new LoginMessage();
		lmBody.setUserId("123");
		lmBody.setUserName("小明");
		Message<LoginMessage> msLoginMessage = new Message<LoginMessage>(UUIDUtil.create(), lmBody);
		msLoginMessage.encodeBody();

		Client client2 = new Client("127.0.0.1", 9100);
		client2.connect();
		LoginMessage lmBody2 = new LoginMessage();
		lmBody2.setUserId("456");
		lmBody2.setUserName("小红");
		Message<LoginMessage> msLoginMessage2 = new Message<LoginMessage>(UUIDUtil.create(), lmBody2);
		msLoginMessage2.encodeBody();

		Runnable r1 = new Runnable() {
			@Override
			public void run() {
				while (true) {
					client.sendMessage(msLoginMessage);
					 try {
					 Thread.sleep(5000);
					 } catch (InterruptedException e) {
					 }
				}
			}
		};

		Runnable r2 = new Runnable() {
			@Override
			public void run() {
				while (true) {
					client2.sendMessage(msLoginMessage2);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
					}
				}
			}
		};

		Thread t1 = new Thread(r1);
		Thread t2 = new Thread(r2);
		t1.start();
		t2.start();

	}
}
