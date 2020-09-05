package edu.dublbo.generator.common.result;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DubLBo
 * @since 2020-09-05 19:28
 * i believe i can i do
 */
public class ResponseResult<T extends BaseResponseData> extends Result<T> {

    public ResponseResult() {
    }

    public ResponseResult(String code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

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

    public static Result<Map<String, Object>> generateSuccessResult(Map<String, Object> data){
        return generateResult(OptStatus.SUCCESS.getOptCode(),OptStatus.SUCCESS.getMessage(),data);
    }

    public static Result<Map<String, Object>> generateResult(String optCode, String message, Map<String, Object> dataMap){
        Result<Map<String, Object>> res = new Result<>();
        res.setCode(ResponseStatus.OK.getCode());
        res.setMsg(ResponseStatus.OK.getMsg());
        Map<String, Object> data = new HashMap<>();
        data.put("optCode", optCode);
        data.put("message", message);
        if(dataMap != null && dataMap.size() != 0){
            data.replaceAll((k, v) -> v);
            // 相当于：
            // for (Map.Entry<String, Object> entry : data.entrySet()) {
            //     data.put(entry.getKey(), entry.getValue());
            // }
        }
        res.setData(data);
        return res;
    }

    public static <T extends BaseResponseData> ResponseResult<T> generateSuccessResult(T data){
        return new ResponseResult<>(ResponseStatus.OK.getCode(), ResponseStatus.OK.getMsg(), data);
    }


}
