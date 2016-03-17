package com.panda.netty.common.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.HessianFactory;
import com.panda.netty.common.enums.CommandEnum;
import com.panda.netty.common.message.req.LoginMessage;
import com.panda.netty.common.message.req.QuitMessage;
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
        // json
        // byte[] bs = _encodeByJson(this.body);
        // hessian
        byte[] bs = _encodeByHessian2(this.body);
        setLength(HEAD_LENGTH + bs.length);
        return bs;
    }

    @SuppressWarnings("unused")
    private byte[] _encodeByJson(Object obj) {
        return JsonUtil.toJsonString(obj).getBytes();
    }

    private byte[] _encodeByHessian2(Object obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        HessianFactory factory = new HessianFactory();
        Hessian2Output out = factory.createHessian2Output(bos);
        try {
            out.startMessage();
            out.writeObject(obj);
            out.completeMessage();
            out.close();
        } catch (IOException e) {
            logger.error("消息编码失败", e);
        }
        return bos.toByteArray();
    }

    @SuppressWarnings("unchecked")
    public void decodeBody(byte[] bs) {
        // json
        // this.body = (T) _decodeByJson(bs);
        // hessian
        this.body = (T) _decodeByHessian2(bs);
        logger.info("消息体内容:{}", JsonUtil.toJsonString(this.body));
    }

    @SuppressWarnings({ "unchecked", "unused" })
    private Object _decodeByJson(byte[] bs) {
        return JsonUtil.parseObject(new String(bs), commandClassesMap.get(getCommand()));
    }

    private Object _decodeByHessian2(byte[] bs) {
        Object obj = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(bs);
        HessianFactory factory = new HessianFactory();
        Hessian2Input in = factory.createHessian2Input(bis);
        try {
            in.startMessage();
            obj = in.readObject();
            in.completeMessage();
            in.close();
            bis.close();
        } catch (IOException e) {
            logger.error("消息解码失败", e);
        }
        return obj;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public static void main(String[] args) {
        LoginMessage message = new LoginMessage();
        message.setUserId("1");
        message.setUserName("小明");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        HessianFactory factory = new HessianFactory();
        Hessian2Output out = factory.createHessian2Output(bos);
        try {
            out.startMessage();
            out.writeObject(message);
            out.completeMessage();
            out.close();
        } catch (IOException e) {
            logger.error("消息编码失败", e);
        }
        byte[] bs = bos.toByteArray();

        ByteArrayInputStream bis = new ByteArrayInputStream(bs);
        Hessian2Input in = factory.createHessian2Input(bis);
        try {
            in.startMessage();
            System.out.println(JsonUtil.toJsonString(in.readObject()));
            in.completeMessage();
            in.close();
            bis.close();
        } catch (IOException e) {
            logger.error("消息解码失败", e);
        }
    }
}
