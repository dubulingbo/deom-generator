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
     * @param modelName 模型名（不带包路径），建议包含前缀
     * @return 表名
     */
    public static String modelName2TableName(String modelName) {
        if (modelName == null || modelName.trim().length() == 0) {
            throw new DataErrorException(ResponseStatus.DATA_ERROR.getCode(), "模型名为空");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(modelName.substring(0,1).toLowerCase());
        for (int i = 1; i < modelName.length(); i++) {
            char target = modelName.charAt(i);
            if (target >= 'A' && target <= 'Z') {
                sb.append("_").append((char) (target + 32));
            } else {
                sb.append(target);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String s = "ASadaT1213TTBsada";
        System.out.println(DemoUtils.modelName2TableName(s));

    }

}
