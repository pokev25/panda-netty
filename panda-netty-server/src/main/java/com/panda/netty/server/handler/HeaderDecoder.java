package com.panda.netty.server.handler;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.panda.netty.common.message.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class HeaderDecoder extends ByteToMessageDecoder {

	private static final Logger logger = LoggerFactory.getLogger(HeaderDecoder.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
		logger.info("server message decode");
		int readableBytes = buf.readableBytes();
		if (readableBytes < Message.HEAD_LENGTH) {
			return;
		}
		buf.markReaderIndex();
		int length = buf.readInt();
		short version = buf.readShort();
		int command = buf.readInt();
		byte[] messageIdBytes = new byte[32];
		buf.readBytes(messageIdBytes);
		String messageId = new String(messageIdBytes);
		Date createTime = new Date(buf.readLong());

		if (readableBytes < length) {
			buf.resetReaderIndex();
			return;
		}
		logger.info("server decode->length:{},readableBytes:{}", length, buf.readableBytes());
		Message<Object> message = new Message<Object>(length, version, command, messageId, createTime);
		byte[] bs = new byte[length - Message.HEAD_LENGTH];
		buf.readBytes(bs);
		message.decodeBody(bs);
		out.add(message);
	}
}
