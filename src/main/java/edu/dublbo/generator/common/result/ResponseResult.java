package edu.dublbo.generator.common.result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一响应结果类
 * @author DubLBo
 * @since 2020-09-05 19:28
 * i believe i can i do
 *
 * {
 *     code:"通讯状态",
 *     msg:"通讯描述信息",
 *     data:{
 *         optCode:"操作状态",
 *         message:"操作描述信息",
 *         ...【结果数据】
 *     }
 * }
 */
public class ResponseResult<T extends BaseResponseData> extends Result<T> {
    private static final Logger logger = LoggerFactory.getLogger(ResponseResult.class);

    public ResponseResult() {
    }

    public ResponseResult(String code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 生成通讯成功、操作成功、但不带结果数据的响应结果
     * @return 统一响应结果
     */
    public static ResponseResult<BaseResponseData> generateSuccessResult(){
        ResponseResult<BaseResponseData> res = new ResponseResult<>();
        res.setCode(ResponseStatus.OK.getCode());
        res.setMsg(ResponseStatus.OK.getMsg());
        BaseResponseData data = new BaseResponseData();
        data.setOptCode(OptStatus.SUCCESS.getOptCode());
        data.setMessage(OptStatus.SUCCESS.getMessage());
        res.setData(data);
        return res;
    }

    /**
     * 生成通讯成功、操作成功、可带结果数据的访问的响应结果
     * @param data 结果数据
     * @return 基本响应结果
     */
    public static Result<Map<String, Object>> generateSuccessResult(Map<String, Object> data){
        return generateResult(OptStatus.SUCCESS.getOptCode(),OptStatus.SUCCESS.getMessage(),data);
    }

    /**
     * 生成通讯成功、可修改操作状态的、可带结果数据的响应结果
     * @param optCode 操作状态码
     * @param message 操作状态描述信息
     * @param dataMap 结果数据 Map
     * @return 基本响应结果
     */
    public static Result<Map<String, Object>> generateResult(String optCode, String message, Map<String, Object> dataMap){
        Result<Map<String, Object>> res = new Result<>();
        res.setCode(ResponseStatus.OK.getCode());
        res.setMsg(ResponseStatus.OK.getMsg());
        Map<String, Object> data = new HashMap<>();
        data.put("optCode", optCode);
        data.put("message", message);
        if(dataMap != null && dataMap.size() != 0){
             for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                 data.put(entry.getKey(), entry.getValue());
             }
        }
        res.setData(data);
//        logger.info(data.toString());
        return res;
    }

    /**
     * 根据实体类生成通讯成功、操作成功的基本响应结果
     * @param data 实体类数据
     * @param <T> 继承于统一响应结果的实体类
     * @return 统一响应结果
     */
    public static <T extends BaseResponseData> ResponseResult<T> generateSuccessResult(T data){
        return new ResponseResult<>(ResponseStatus.OK.getCode(), ResponseStatus.OK.getMsg(), data);
    }


}
