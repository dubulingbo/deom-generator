package edu.dublbo.generator.common.result;

import java.io.Serializable;

/**
 * 基本响应结果类
 * @author DubLBo
 * @since 2020-09-05 20:43
 * i believe i can i do
 */
public class Result<T> implements Serializable {
    protected String code;  // 通讯代码
    protected String msg;   // 通讯状态信息
    protected T data;       //业务数据

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
