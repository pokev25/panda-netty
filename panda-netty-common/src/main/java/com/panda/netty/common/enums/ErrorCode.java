package com.panda.netty.common.enums;

public enum ErrorCode {
    SUCCESS(0, "成功"), ERROR_PARAM(1, "参数错误"), ERROR_REPEAT(2, "重复操作"), ACTION_NOT_EXIST(3, "接口不存在"),
    FILE_NOT_EXIST(4, "文件不存在"), UNKNOW(-1, "系统异常");

    private ErrorCode(int value, String msg){
        this.value = value;
        this.msg = msg;
    }

    /**
     * code码
     */
    private int    value;
    /**
     * code信息
     */
    private String msg;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 根据code码查找
     */
    public static ErrorCode valueOf(int value) {
        ErrorCode result = UNKNOW;
        for (ErrorCode p : ErrorCode.values()) {
            if (p.getValue() == value) {
                result = p;
            }
        }
        return result;
    }

    /**
     * 根据name查找
     */
    public static ErrorCode fromName(String name) {
        ErrorCode result = UNKNOW;
        for (ErrorCode p : ErrorCode.values()) {
            if (p.getMsg().equals(name)) {
                result = p;
            }
        }
        return result;
    }
}
