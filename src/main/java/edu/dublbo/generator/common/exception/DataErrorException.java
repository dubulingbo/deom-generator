package edu.dublbo.generator.common.exception;

/**
 * @author DubLBo
 * @since 2020-09-06 11:50
 * i believe i can i do
 */
public class DataErrorException extends BaseException {
    public DataErrorException() {
    }

    public DataErrorException(String optCode, String optMsg) {
        super(optCode, optMsg);
    }
}
