package com.panda.netty.server.handler;

import com.panda.netty.common.message.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


@SuppressWarnings("rawtypes")
public class HeaderEncoder extends MessageToByteEncoder<Message> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf buf) throws Exception {
		buf.writeInt(message.getLength());
		buf.writeShort(message.getVersion());
		buf.writeInt(message.getCommand());
		buf.writeBytes(message.getMessageId().getBytes());
		buf.writeLong(message.getCreateTime().getTime());
		buf.writeBytes(message.encodeBody());
	}
}
