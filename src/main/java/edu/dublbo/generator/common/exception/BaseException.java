package edu.dublbo.generator.common.exception;

/**
 * @author DubLBo
 * @since 2020-09-05 21:40
 * i believe i can i do
 */
public class BaseException extends RuntimeException {
    protected String optCode;
    protected String optMsg;

    public BaseException(){}

    public BaseException(String optCode, String optMsg){
        super();
        this.optCode = optCode;
        this.optMsg = optMsg;
    }

    public String getOptCode() {
        return optCode;
    }

    public void setOptCode(String optCode) {
        this.optCode = optCode;
    }

    public String getOptMsg() {
        return optMsg;
    }

    public void setOptMsg(String optMsg) {
        this.optMsg = optMsg;
    }
}
