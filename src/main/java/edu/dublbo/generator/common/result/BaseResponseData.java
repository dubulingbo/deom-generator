package edu.dublbo.generator.common.result;

import java.io.Serializable;

/**
 * @author DubLBo
 * @since 2020-09-05 20:34
 * i believe i can i do
 */
public class BaseResponseData implements Serializable {
    private String optCode;  // 业务代码
    private String message;       // 操作信息


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
