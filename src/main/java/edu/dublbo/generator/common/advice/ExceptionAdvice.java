package edu.dublbo.generator.common.advice;

import edu.dublbo.generator.common.exception.DataErrorException;
import edu.dublbo.generator.common.exception.OptErrorException;
import edu.dublbo.generator.common.exception.UnAuthorizedException;
import edu.dublbo.generator.common.result.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一校验异常处理
 *
 * @author DubLBo
 * @since 2020-09-06 11:23
 * i believe i can i do
 */
@ControllerAdvice
public class ExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @Value(value = "${dublbo.web.response.debug}")
    private final Boolean responseDebugMsg = false;

    /**
     * 处理数据绑定异常
     *
     * @param bindException 数据绑定异常
     * @return 统一响应基本结果
     */
    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public Result<Map<String, Object>> handleValidateException(BindException bindException) {
        logger.error("数据绑定异常，{}", bindException.getMessage(), bindException);
        Result<Map<String, Object>> result = new Result<>();
        result.setCode(ResponseStatus.DATA_ERROR.getCode());
        result.setMsg(ResponseStatus.DATA_ERROR.getMsg());
        Map<String, Object> data = new HashMap<>();
        data.put("optCode", OptStatus.FAIL.getOptCode());
        StringBuilder msgBuffer = new StringBuilder();

        List<ObjectError> errorList = bindException.getAllErrors();
        if (errorList != null && errorList.size() != 0) {
            for (Object error : errorList) {
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    String field = fieldError.getField();
                    String msg = fieldError.getDefaultMessage();
                    logger.debug("field: {}, msg: {}", field, msg);
                    msgBuffer.append(field).append(msg).append(" #233# ");
                }
            }
        }
        data.put("message", msgBuffer.toString());
        result.setData(data);
        return result;
    }

    /**
     * 处理自定义数据异常
     *
     * @param dataErrorException 数据错误异常
     * @return 统一响应结果
     */
    @ResponseBody
    @ExceptionHandler(value = DataErrorException.class)
    public ResponseResult<BaseResponseData> handleDataErrorException(DataErrorException dataErrorException) {
        logger.error("数据异常，{}", dataErrorException.getOptMsg(), dataErrorException);

        ResponseResult<BaseResponseData> result = new ResponseResult<>();
        result.setCode(ResponseStatus.DATA_ERROR.getCode());
        result.setMsg(ResponseStatus.DATA_ERROR.getMsg());

        BaseResponseData baseData = new BaseResponseData();
        baseData.setOptCode(OptStatus.FAIL.getOptCode());
        baseData.setMessage(dataErrorException.getOptMsg());
        result.setData(baseData);
        return result;
    }

    /**
     * 统一处理 405 异常
     *
     * @param exception 请求方法不支持异常
     * @return 统一响应结果
     */
    @ResponseBody
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseResult<BaseResponseData> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        if (exception != null) {
            logger.error("Http 405, {}", exception.getMessage(), exception);
        }

        ResponseResult<BaseResponseData> result = new ResponseResult<>();
        result.setCode(ResponseStatus.METHOD_NOT_SUPPORT.getCode());
        String msg = exception != null ? exception.getMethod() : "";
        result.setMsg(msg + ResponseStatus.METHOD_NOT_SUPPORT.getMsg());
        return result;
    }

    /**
     * 统一处理 404 异常
     *
     * @param exception 请求未找到异常
     * @return 统一响应结果
     */
    @ResponseBody
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseResult<BaseResponseData> handleNoFoundException(NoHandlerFoundException exception) {
        if (exception != null) {
            logger.error("Http 404, {}", exception.getMessage(), exception);
        }

        ResponseResult<BaseResponseData> result = new ResponseResult<>();
        result.setCode(ResponseStatus.NOT_FOUND.getCode());
        result.setMsg(ResponseStatus.NOT_FOUND.getMsg());
        return result;
    }

    /**
     * 统一处理为授权异常
     * @param exception 未授权异常
     * @return <code>统一响应结果</code>
     */
    @ResponseBody
    @ExceptionHandler(value = UnAuthorizedException.class)
    public ResponseResult<BaseResponseData> handleUnAuthorizedException(UnAuthorizedException exception){
        ResponseResult<BaseResponseData> result = new ResponseResult<>();
        result.setCode(ResponseStatus.UNAUTHORIZED.getCode());
        result.setMsg(ResponseStatus.UNAUTHORIZED.getMsg());
        BaseResponseData data = new BaseResponseData();
        data.setOptCode(OptStatus.FAIL.getOptCode());
        if(responseDebugMsg){
            data.setMessage(exception.getMethod() + ": " + exception.getUrl() + "无权限，可能是登录超时或未登录");
        }else{
            data.setMessage("无权限访问");
        }
        result.setData(data);
        return result;
    }

    /**
     * 统一处理自定义操作错误异常
     *
     * @param exception 操作错误异常
     * @return 统一响应结果
     */
    @ResponseBody
    @ExceptionHandler(value = OptErrorException.class)
    public ResponseResult<BaseResponseData> handleOptErrorException(OptErrorException exception) {
        ResponseResult<BaseResponseData> result = new ResponseResult<>();
        result.setCode(ResponseStatus.OK.getCode());
        result.setMsg(ResponseStatus.OK.getMsg());

        BaseResponseData data = new BaseResponseData();
        data.setOptCode(exception.getOptCode());
        data.setMessage(exception.getOptMsg());
        result.setData(data);
        return result;
    }

    /**
     * 统一处理 服务器内部 异常
     *
     * @param e 异常
     * @return 统一响应结果
     */
    @ResponseBody
    @ExceptionHandler(value = Throwable.class)
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult<BaseResponseData> handle500(Throwable e) {
        if (e != null) {
            logger.error("Http 500, {}", e.getMessage(), e);
        }

        ResponseResult<BaseResponseData> result = new ResponseResult<>();
        result.setCode(ResponseStatus.SERVER_ERROR.getCode());
        result.setMsg(ResponseStatus.SERVER_ERROR.getMsg());

        BaseResponseData data = new BaseResponseData();
        data.setOptCode(OptStatus.FAIL.getOptCode());
        if (responseDebugMsg) {
            data.setMessage(e != null ? e.getMessage() + " #233# " : "");
        } else {
            data.setMessage(ResponseStatus.SERVER_ERROR.getMsg());
        }
        result.setData(data);
        return result;
    }

}
