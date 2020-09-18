package edu.dublbo.generator.common.utils;

import edu.dublbo.generator.common.exception.DataErrorException;
import edu.dublbo.generator.common.result.ResponseStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * @author DubLBo
 * @since 2020-09-06 19:21
 * i believe i can i do
 */
public class WebObjectUtils extends ObjectUtils {
    private static final Logger logger = LoggerFactory.getLogger(WebObjectUtils.class);

    public WebObjectUtils() {
    }

    /**
     * 把HttpRequest中的请求参数根据字段名赋值到已经存在的对象中
     *
     * @param request    Http请求
     * @param destObj    目标对象
     * @param fieldNames 字段名
     */
    public static void assignObjectField(HttpServletRequest request, Object destObj, String... fieldNames) {
        if (destObj == null) {
            logger.debug("目标对象为空");
            return;
        } else if (fieldNames == null || fieldNames.length == 0) {
            logger.debug("变量名序列为空");
            return;
        }
        try {
            Class<?> destClass = destObj.getClass();
            Map<String, Field> fieldMap = getAllFieldMap(destClass);

            for (String key : fieldNames) {
                if (!StringUtils.isEmpty(key)) {
                    String value = request.getParameter(key);
                    String setMethodName = field2SetMethodName(key);
                    Class<?> type = fieldMap.get(key).getType();
                    Method setMethod = destClass.getMethod(setMethodName, type);
                    setMethod.invoke(destObj, TypeUtils.formatType(type, value));
                }
            }

            Set<ConstraintViolation<Object>> errorInfos = ValidateUtils.validateAll(destObj);

            if (errorInfos == null || errorInfos.size() == 0) {
                return;
            }
//            List<FieldError> fieldErrorList = new ArrayList<>();
//            StringBuilder msgBuilder = new StringBuilder();
//            for (ConstraintViolation<Object> error : errorInfos) {
//                logger.debug("校验错误信息：{}", error);
//                msgBuilder.append(error.getPropertyPath().toString()).append(error.getMessage()).append(". #233#\n");
//            }
            throw new DataErrorException(ResponseStatus.DATA_ERROR.getCode(), ValidateUtils.generateErrorMsg(errorInfos));

        } catch (DataErrorException dataErrorException) {
            throw dataErrorException;
        } catch (Exception e) {
            logger.warn("类型转换异常：{}", e.getMessage(), e);
            throw new DataErrorException(ResponseStatus.DATA_ERROR.getCode(), e.getMessage());
        }
    }
}
