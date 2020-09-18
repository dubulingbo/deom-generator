package edu.dublbo.generator.utils;

import edu.dublbo.generator.common.exception.DataErrorException;
import edu.dublbo.generator.common.result.ResponseStatus;

import java.util.List;

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

    /**
     * 生成模型明细中的下一个序号
     * @param array 已有的序号
     */
    public static Integer generateNextSortNo(List<Integer> array){
        if(array == null || array.size() == 0){
            return Constant.MODEL_DETAIL_SORTNO_MIN_POS;
        }
        int var1 = Integer.MIN_VALUE;
        int var2 = Integer.MAX_VALUE;
        for (Integer tmp : array) {
            if (tmp != null) {
                // 寻找比最小基准数 小的 最大的数
                if (tmp < Constant.MODEL_DETAIL_SORTNO_MIN_POS && tmp > var1) {
                    var1 = tmp;
                }
                // 寻找比最大基数 大的 最小的数
                if (tmp > Constant.MODEL_DETAIL_SORTNO_MAX_POS && tmp < var2) {
                    var2 = tmp;
                }
            }
        }

        // 在最小的基数范围下的数已经取完
        if (var1 + 1 == Constant.MODEL_DETAIL_SORTNO_MIN_POS) {
            return var2 == Integer.MAX_VALUE ? Constant.MODEL_DETAIL_SORTNO_MAX_POS + 1 : var2 + 1;
        }
        return var1 == Integer.MIN_VALUE ? Constant.MODEL_DETAIL_SORTNO_MIN_POS - 1 : var1 + 1;
    }

    public static void main(String[] args) {
        String s = "ASadaT1213TTBsada";
        System.out.println(DemoUtils.modelName2TableName(s));

    }

}
