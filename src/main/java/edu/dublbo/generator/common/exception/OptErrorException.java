package edu.dublbo.generator.common.exception;

/**
 * @author DubLBo
 * @since 2020-09-05 21:39
 * i believe i can i do
 */
public class OptErrorException extends BaseException {

    public OptErrorException() {}

    public OptErrorException(String optCode, String optMsg) {
        super(optCode, optMsg);
    }
}
