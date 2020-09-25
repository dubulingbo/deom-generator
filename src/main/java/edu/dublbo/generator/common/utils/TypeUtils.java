package edu.dublbo.generator.common.utils;

import edu.dublbo.generator.common.exception.DataErrorException;
import edu.dublbo.generator.common.result.ResponseStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author DubLBo
 * @since 2020-09-06 20:24
 * i believe i can i do
 */
public class TypeUtils {
    private static final Logger logger = LoggerFactory.getLogger(TypeUtils.class);

    public TypeUtils() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T formatType(Class<T> clazz, String value) throws Exception {
        if (StringUtils.isEmpty(value) || clazz == null) {
            return null;
        }

        if (clazz.equals(String.class)) {
            return (T) value;
        } else if (clazz.equals(Integer.class)) {
            return (T) new Integer(value);
        } else if (clazz.equals(Long.class)) {
            return (T) new Long(value);
        } else if (clazz.equals(Float.class)) {
            return (T) new Float(value);
        } else if (clazz.equals(Double.class)) {
            return (T) new Double(value);
        } else if (clazz.equals(Date.class)) {
            if (!value.contains(":") && value.length() == 10) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return (T) sdf.parse(value);
            } else if (value.contains(":")) {
                if (value.length() == 8) {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    return (T) sdf.parse(value);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return (T) sdf.parse(value);
                }
            }
        }
        logger.error("[类型转换工具类] 转化的类型[{}] 不具备，请另外实现 #233# ", clazz.getName());
        throw new DataErrorException(ResponseStatus.DATA_ERROR.getCode(), "无法将 " + value + " 转换成 " + clazz.getName() + " #233# ");
    }
}
