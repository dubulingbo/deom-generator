package edu.dublbo.generator.common.result;

import java.io.Serializable;

/**
 * 操作状态信息类
 * @author DubLBo
 * @since 2020-09-05 20:34
 * i believe i can i do
 */
public class BaseResponseData implements Serializable {
    private String optCode = OptStatus.SUCCESS.getOptCode();  // 操作状态代码
    private String message = OptStatus.SUCCESS.getMessage();  // 操作状态信息


    public String getOptCode() {
        return optCode;
    }

    public void setOptCode(String optCode) {
        this.optCode = optCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
