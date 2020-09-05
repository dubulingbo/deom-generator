package edu.dublbo.generator.common.result;

/**
 * @author DubLBo
 * @since 2020-09-05 20:59
 * i believe i can i do
 */
public enum OptStatus {
    SUCCESS("1", "操作成功"),
    FAIL("0", "操作失败");

    private String optCode;
    private String message;

    OptStatus(String optCode, String message) {
        this.optCode = optCode;
        this.message = message;
    }

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
