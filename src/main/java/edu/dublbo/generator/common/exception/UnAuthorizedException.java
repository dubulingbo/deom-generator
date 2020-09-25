package edu.dublbo.generator.common.exception;

/**
 * @author DubLBo
 * @since 2020-09-06 13:08
 * i believe i can i do
 */
public class UnAuthorizedException extends Exception {
    private String url;
    private String method;

    public UnAuthorizedException(String message, String url, String method) {
        super(message);
        this.url = url;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
