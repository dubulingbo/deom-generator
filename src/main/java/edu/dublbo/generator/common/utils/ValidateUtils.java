package edu.dublbo.generator.common.utils;

import org.hibernate.validator.HibernateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * 校验工具
 * @author DubLBo
 * @since 2020-09-06 22:07
 * i believe i can i do
 */
public class ValidateUtils {
    private static final Logger logger = LoggerFactory.getLogger(ValidateUtils.class);

    private static final Validator validatorFast = Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();
    private static final Validator validatorAll = Validation.byProvider(HibernateValidator.class).configure().failFast(false).buildValidatorFactory().getValidator();

    /**
     * 快速校验，一旦发现一个出现问题，即刻返回校验不通过及其问题列表
     * @param domain 校验对象
     * @param <T> 校验对象类型声明
     * @return 校验对象中第一个出现问题的字段的问题列表
     */
    public static <T> Set<ConstraintViolation<T>> validateFast(T domain) {
        Set<ConstraintViolation<T>> validateResult = validatorFast.validate(domain);
        if(validateResult.size() > 0){
            logger.debug(validateResult.iterator().next().getPropertyPath() + ": "+ validateResult.iterator().next().getMessage());
        }
        return validateResult;
    }

    /**
     * 校验对象的所有字段，返回全部的校验不通过的内容
     * @param domain 校验对象
     * @param <T> 校验区域类型声明
     * @return 全部问题列表
     */
    public static <T> Set<ConstraintViolation<T>> validateAll(T domain) {
        Set<ConstraintViolation<T>> validateResult = validatorAll.validate(domain);
        if(validateResult.size() > 0){
            for (ConstraintViolation<T> cv : validateResult) {
                logger.debug(cv.getPropertyPath() + ": " + cv.getMessage());
            }
        }
        return validateResult;
    }

    /**
     * 根据校验结果生成校验描述
     * @param validateResult 校验结果
     * @param <T> 校验结果中的对象类型
     * @return 校验描述信息
     */
    public static <T> String generateErrorMsg(Set<ConstraintViolation<T>> validateResult){
        if(validateResult == null || validateResult.size() == 0){
            return null;
        }

        StringBuilder msgBuilder = new StringBuilder();
        for(ConstraintViolation<T> error : validateResult){
            msgBuilder.append(error.getPropertyPath().toString()).append(error.getMessage()).append(". #233#\n");
        }
        return msgBuilder.toString();
    }

}
