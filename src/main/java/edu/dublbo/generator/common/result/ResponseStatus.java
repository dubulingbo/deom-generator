package edu.dublbo.generator.common.result;

/**
 * 通讯状态枚举类
 * @author DubLBo
 * @since 2020-09-05 20:51
 * i believe i can i do
 */
public enum ResponseStatus {
    OK("200", "成功"),
    UNAUTHORIZED("401", "未授权"),
    NOT_FOUND("404", "请求未找到"),
    SERVER_ERROR("500", "系统异常"),
    DATA_ERROR("400", "请求数据存在异常"),
    METHOD_NOT_SUPPORT("405", "请求方法不支持");

    private String code;
    private String msg;


    ResponseStatus(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
