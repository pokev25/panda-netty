package com.panda.netty.common.message;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.panda.netty.common.enums.CommandEnum;
import com.panda.netty.common.util.JsonUtil;

@SuppressWarnings("rawtypes")
public class Message<T> extends Header {

    private static final Logger              logger            = LoggerFactory.getLogger(Message.class);
    // 消息注册
    private static final Map<Integer, Class> commandClassesMap = new HashMap<Integer, Class>();
    private static final Map<Class, Integer> classedCommandMap = new HashMap<Class, Integer>();
    public static final int                  HEAD_LENGTH       = 50;

    static {
        // 登录
        commandClassesMap.put(CommandEnum.LOGIN.getKey(), LoginMessage.class);
        classedCommandMap.put(LoginMessage.class, CommandEnum.LOGIN.getKey());
        // 退出
        commandClassesMap.put(CommandEnum.QUIT.getKey(), QuitMessage.class);
        classedCommandMap.put(QuitMessage.class, CommandEnum.QUIT.getKey());
    }

    // 消息体
    private T body;

    public Message(){
        super();
    }

    public Message(short version, int command, String messageId){
        super(version, command, messageId);
    }

    public Message(int length, short version, int command, String messageId, Date createTime){
        super(length, version, command, messageId, createTime);
    }

    public Message(String messageId, T body){
        super((short) 1, classedCommandMap.get(body.getClass()), messageId);
        this.body = body;
    }

    public byte[] encodeBody() {
        // 序列化方式可以选择 json protobuf
        byte[] bs = JsonUtil.toJsonString(this.body).getBytes();
        setLength(HEAD_LENGTH + bs.length);
        return bs;
    }

    @SuppressWarnings("unchecked")
    public void decodeBody(byte[] bs) {
        String bodyStr = new String(bs);
        logger.info("消息体内容:{}", bodyStr);
        this.body = (T) JsonUtil.parseObject(bodyStr, commandClassesMap.get(getCommand()));
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
