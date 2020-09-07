package edu.dublbo.generator.utils;

import edu.dublbo.generator.common.exception.DataErrorException;
import edu.dublbo.generator.common.result.ResponseStatus;

/**
 * @author DubLBo
 * @since 2020-09-07 17:16
 * i believe i can i do
 */
public class DemoUtils {

    /**
     * 将模型名转化为表名
     *
     * @param modelName 带包路径的模型名
     * @return 表名
     */
    public static String modelName2TableName(String modelName) {
        if (modelName == null || modelName.trim().length() == 0) {
            throw new DataErrorException(ResponseStatus.DATA_ERROR.getCode(), "模型名为空");
        }
        int point = modelName.lastIndexOf(".");
        if (point > 0 && point < modelName.length() - 1){
            String cname = modelName.substring(point + 1);
            StringBuilder sb = new StringBuilder("t_");
            for (int i = 0; i < cname.length(); i++) {
                char target = cname.charAt(i);
                if (target >= 'A' && target <= 'Z') {
                    // 不是第一个
                    if(i != 0){
                        sb.append("_").append((char) (target + 32));
                    }else{
                        sb.append((char) (target + 32));
                    }
                } else {
                    sb.append(target);
                }
            }
            return sb.toString();
        }else{
            throw new DataErrorException(ResponseStatus.DATA_ERROR.getCode(), "模型名格式错误，不能解析");
        }
    }

//    public static void main(String[] args) {
//        String s = "asadaT1213TTBsada";
//        System.out.println(s.toUpperCase());
//        System.out.println(s.toLowerCase());
//
//    }

}
