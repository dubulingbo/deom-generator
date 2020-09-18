package edu.dublbo.generator.utils;

import edu.dublbo.generator.common.exception.OptErrorException;
import edu.dublbo.generator.common.result.OptStatus;
import edu.dublbo.generator.common.utils.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author DubLBo
 * @since 2020-09-08 20:35
 * i believe i can i do
 */
public class DemoFileGenUtils {
    private static Logger logger = LoggerFactory.getLogger(DemoFileGenUtils.class);
    private static final String demoAuthor = "guan_exe (demo-generator)";

    private static class FileCoo {
        public int listIndex;
        public int strIndex;

        public FileCoo() {
            this.listIndex = -1;
            this.strIndex = -1;
        }
    }

    private static String getCurTimeStr() {
        Date curDate = new Date();
        return new SimpleDateFormat("yyyy年M月d日 H:m:s.S").format(curDate);
    }

    private static FileCoo findFirstRowIndex(List<String> list, String target) {
        FileCoo coo = new FileCoo();
        if (list != null && list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                String tmp = list.get(i);
                if (tmp.contains(target)) {
                    coo.listIndex = i;
                    coo.strIndex = tmp.indexOf(target);
                }
            }
        }
        return coo;
    }

    // 生成 Model 层源码
    public static List<String> generateModelDemo(String modelName, String modelRemark, String packageDir, List<String> proNames,
                                                 HashMap<String, String> proTypeMap, HashMap<String, String> proDescMap, Set<String> packageSet) {
        // 读取模板文件
        List<String> tFile;
        try {
            tFile = FileOperator.readContent(new File("./src/main/resources/templates/model_template.java"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模板文件操作错误");
        }
        if (tFile == null || tFile.size() == 0) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模板文件内容为空");
        }

        FileCoo coo;
        String tmp;
        String target;
        StringBuilder rowBuilder = new StringBuilder();
        String[] singleRowTags = {"#{packageDir}", "#{remark}", "#{user}", "#{curTime}", "#{className}"};
        List<String> singleRowValues = new ArrayList<>();
        singleRowValues.add(packageDir);
        singleRowValues.add(modelRemark);
        singleRowValues.add(demoAuthor);
        singleRowValues.add(getCurTimeStr());
        singleRowValues.add(modelName);

        logger.info(singleRowValues.toString());

        for (int var = 0; var < singleRowTags.length; var++) {
            rowBuilder.setLength(0);
            target = singleRowTags[var];
            coo = findFirstRowIndex(tFile, target);
            tmp = tFile.get(coo.listIndex);
            rowBuilder.append(tmp, 0, coo.strIndex).append(singleRowValues.get(var))
                    .append(tmp.substring(coo.strIndex + target.length()));
            tFile.set(coo.listIndex, rowBuilder.toString());
        }

        rowBuilder.setLength(0);
        for (String s : packageSet) {
            rowBuilder.append("import ").append(s).append(";\n");
        }
        coo = findFirstRowIndex(tFile, "#{importZone}");
        tFile.set(coo.listIndex, rowBuilder.toString());


        StringBuilder var3 = new StringBuilder();
        StringBuilder var4 = new StringBuilder();
        for (String key : proNames) {
            var3.append("\t// ")
                    .append(proDescMap.get(key))
                    .append("\n\tprivate ")
                    .append(proTypeMap.get(key))
                    .append(" ").append(key).append(";\n");
            var4.append("\tpublic ").append(proTypeMap.get(key)).append(" ")
                    .append(ObjectUtils.field2GetMethodName(key))
                    .append("() {\n\t\treturn ").append(key)
                    .append(";\n\t}\n\n\tpublic void ")
                    .append(ObjectUtils.field2SetMethodName(key)).append("(")
                    .append(proTypeMap.get(key)).append(" ")
                    .append(key)
                    .append(") {\n\t\tthis.")
                    .append(key)
                    .append(" = ")
                    .append(key)
                    .append(";\n\t}\n\n");
        }
        coo = findFirstRowIndex(tFile, "#{propertyZone}");
        tFile.set(coo.listIndex, var3.toString());
        coo = findFirstRowIndex(tFile, "#{getAndSetMethod}");
        tFile.set(coo.listIndex, var4.toString());

        rowBuilder.setLength(0);
        rowBuilder.append("\t@Override \n\tpublic String toString() {\n\t\treturn \"").append(modelName).append("{");
        int iMax = proNames.size() - 1;
        for (int i = 0; ; i++) {
            rowBuilder.append(proNames.get(i)).append("=\"+").append(proNames.get(i)).append("+\"");
            if (i == iMax) {
                rowBuilder.append("}\";\n\t}\n");
                break;
            }
            rowBuilder.append(", ");
        }
        coo = findFirstRowIndex(tFile, "#{toStringMethod}");
        tFile.set(coo.listIndex, rowBuilder.toString());

        return tFile;
    }

    // 生成 Mapper Interface 层内容
    public static List<String> generateMapperInterDemo(String modelName, String modelRemark, String packageDir, String packageDir2) {
        // 读取模板文件
        List<String> tFile;
        try {
            tFile = FileOperator.readContent(new File("./src/main/resources/templates/mapper_inter_template.java"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模板文件操作错误");
        }
        if (tFile == null || tFile.size() == 0) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模板文件内容为空");
        }

        FileCoo coo;
        String tmp;
        String target;
        StringBuilder rowBuilder = new StringBuilder();
        String[] singleRowTags = {"#{packageDir}", "#{importZone}", "#{remark}", "#{user}", "#{curTime}", "#{mapperName}"};
        List<String> singleRowValues = new ArrayList<>();
        singleRowValues.add(packageDir2 + ".mapper");
        singleRowValues.add(packageDir + "." + modelName);
        singleRowValues.add(modelRemark);
        singleRowValues.add(demoAuthor);
        singleRowValues.add(getCurTimeStr());
        singleRowValues.add(modelName + "Mapper");

        logger.info(singleRowValues.toString());

        for (int i = 0; i < singleRowTags.length; i++) {
            rowBuilder.setLength(0);
            target = singleRowTags[i];
            coo = findFirstRowIndex(tFile, target);
            tmp = tFile.get(coo.listIndex);
            rowBuilder.append(tmp, 0, coo.strIndex).append(singleRowValues.get(i))
                    .append(tmp.substring(coo.strIndex + target.length()));
            tFile.set(coo.listIndex, rowBuilder.toString());
        }

        // 替换 #{modelName}
        target = "#{modelName}";
        for (int i = 0; i < tFile.size(); i++) {
            tmp = tFile.get(i);
            if (tmp.contains(target)) {
                rowBuilder.setLength(0);
                int index = tmp.indexOf(target);
                rowBuilder.append(tmp, 0, index).append(modelName).append(tmp.substring(index + target.length()));
                tFile.set(i, rowBuilder.toString());
            }
        }

        return tFile;
    }

    // 生成 Mapper Xml 文件内容
    public static List<String> generateMapperXmlContent(String modelName, String tableName, String packageDir, String packageDir2, List<String> proNames, Map<String, String> colNameMap) {
        // 读取模板文件
        List<String> tFile;
        try {
            tFile = FileOperator.readContent(new File("./src/main/resources/templates/mapper_xml_template.xml"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模板文件操作错误");
        }
        if (tFile == null || tFile.size() == 0) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模板文件内容为空");
        }

        String[] singleRowTags = {"{{qualifiedMapperName}}", "{{tableName}}", "{{columnNameItems}}",
                "{{resultMapZone}}", "{{conditionZone}}", "{{aliasConditionZone}}", "{{insertColItems}}", "{{updateColItems}}"};
        List<String> singleRowValues = new ArrayList<>();
        singleRowValues.add(packageDir2 + ".mapper." + modelName + "Mapper");
        singleRowValues.add(tableName);

        // columnNameItems
        // resultMapZone
        // conditionZone
        // aliasConditionZone
        // insertColItems
        // updateColItems
        StringBuilder columnNameItems = new StringBuilder();
        StringBuilder resultMapZone = new StringBuilder();
        StringBuilder conditionZone = new StringBuilder();
        StringBuilder aliasConditionZone = new StringBuilder();
        StringBuilder insertColItems = new StringBuilder();
        StringBuilder updateColItems = new StringBuilder();
        for (int i = 0; ; i++) {
            String key = proNames.get(i);
            String tmp = String.format("#{%s}", key);
            columnNameItems.append(colNameMap.get(key));
            resultMapZone.append(String.format("<result column=\"%s\" property=\"%s\"/>", colNameMap.get(key), key));
            conditionZone.append("<if test=\"").append(key)
                    .append(" != null and ").append(key)
                    .append(" != ''\">\n\t\t\tand ").append(colNameMap.get(key))
                    .append(" = #{").append(key).append("}\n\t\t</if>");
            aliasConditionZone.append("<if test=\"").append(key)
                    .append(" != null and ").append(key)
                    .append(" != ''\">\n\t\t\tand a.").append(colNameMap.get(key))
                    .append(" = #{").append(key).append("}\n\t\t</if>");
            insertColItems.append(tmp);
            updateColItems.append(colNameMap.get(key)).append(" = ").append(tmp).append(",");
            if (i == proNames.size() - 1) {
                singleRowValues.add(columnNameItems.toString());
                singleRowValues.add(resultMapZone.toString());
                singleRowValues.add(conditionZone.toString());
                singleRowValues.add(aliasConditionZone.toString());
                singleRowValues.add(insertColItems.toString());
                singleRowValues.add(updateColItems.toString());
                break;
            }
            columnNameItems.append(",");
            resultMapZone.append("\n\t\t");
            conditionZone.append("\n\t\t");
            aliasConditionZone.append("\n\t\t");
            insertColItems.append(",");
            updateColItems.append("\n\t\t\t");
        }
        logger.info("列表（singleRowValues）的容量为：{}", singleRowValues.size());
        StringBuilder rower = new StringBuilder();
        FileCoo coo;
        for (int i = 0; i < singleRowTags.length; i++) {
            rower.setLength(0);
            coo = findFirstRowIndex(tFile, singleRowTags[i]);
            String tmp = tFile.get(coo.listIndex);
            rower.append(tmp, 0, coo.strIndex).append(singleRowValues.get(i))
                    .append(tmp.substring(coo.strIndex + singleRowTags[i].length()));
            tFile.set(coo.listIndex, rower.toString());
        }

        // 替换 {{aliasModelName}} 和 {{qualifiedModelName}}
        String s1 = "{{aliasModelName}}";
        String target = modelName.substring(0, 1).toLowerCase() + modelName.substring(1);
        for (int i = 0; i < tFile.size(); i++) {
            String tmp = tFile.get(i);
            if (tmp.contains(s1)) {
                rower.setLength(0);
                int index = tFile.get(i).indexOf(s1);
                rower.append(tmp, 0, index).append(target).append(tmp.substring(index + s1.length()));
                tFile.set(i, rower.toString());
            }
        }
        s1 = "{{qualifiedModelName}}";
        target = packageDir + "." + modelName;
        for (int i = 0; i < tFile.size(); i++) {
            String tmp = tFile.get(i);
            if (tmp.contains(s1)) {
                rower.setLength(0);
                int index = tFile.get(i).indexOf(s1);
                rower.append(tmp, 0, index).append(target).append(tmp.substring(index + s1.length()));
                tFile.set(i, rower.toString());
            }
        }

        return tFile;
    }

    // 生成 Service层代码
    public static List<String> generateServiceDemo(String modelName, String modelRemark, String packageDir, String packageDir2) {
        // 读取模板文件
        List<String> tFile;
        try {
            tFile = FileOperator.readContent(new File("./src/main/resources/templates/service_template.java"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模板文件操作错误");
        }
        if (tFile == null || tFile.size() == 0) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模板文件内容为空");
        }

        String[] singleRowTags = {"#{packageDir}", "#{importZone}", "#{remark}", "#{user}",
                "#{curTime}", "#{serviceName}", "#{mapperName}"};
        List<String> singleRowValues = new ArrayList<>();
        singleRowValues.add(packageDir2 + ".service");
        String importZone = "import " + packageDir + "." + modelName + ";\nimport " +
                packageDir2 + ".mapper." + modelName + "Mapper;";
        singleRowValues.add(importZone);
        singleRowValues.add(modelRemark);
        singleRowValues.add(demoAuthor);
        singleRowValues.add(getCurTimeStr());
        String tmp = getBusiBeanName(modelName);
        singleRowValues.add(tmp + "Service");
        singleRowValues.add(modelName + "Mapper");

        logger.info("列表（singleRowValues）的容量为：{}", singleRowValues.size());
        StringBuilder rower = new StringBuilder();
        FileCoo coo;
        for (int i = 0; i < singleRowTags.length; i++) {
            rower.setLength(0);
            coo = findFirstRowIndex(tFile, singleRowTags[i]);
            tmp = tFile.get(coo.listIndex);
            rower.append(tmp, 0, coo.strIndex).append(singleRowValues.get(i))
                    .append(tmp.substring(coo.strIndex + singleRowTags[i].length()));
            tFile.set(coo.listIndex, rower.toString());
        }

        // modelName
        String keyStr = "#{modelName}";
        for (int i = 0; i < tFile.size(); i++) {
            tmp = tFile.get(i);
            if (tmp.contains(keyStr)) {
                rower.setLength(0);
                int index = tFile.get(i).indexOf(keyStr);
                rower.append(tmp, 0, index).append(modelName).append(tmp.substring(index + keyStr.length()));
                tFile.set(i, rower.toString());
            }
        }
        return tFile;
    }

    private static String getBusiBeanName(String modelName){
        if(StringUtils.isEmpty(modelName)) {
            logger.error("model Name is EMPTY #233#");
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模型名为空");
        }
        if(modelName.length() < 5){
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模型名长度不合适");
        }
        char first = modelName.charAt(0);
        if(first == 'T' || first == 'V' || first == 'S'){
            return modelName.substring(1,2).toUpperCase() + modelName.substring(2);
        }
        return modelName.substring(0,1).toUpperCase() + modelName.substring(1);
    }




//    private static List<>

    public static void main(String[] args) {
        List<String> colNames = new ArrayList<>();
        colNames.add("id");
        colNames.add("createUser");
        colNames.add("modifyUser");
        colNames.add("deleteFlag");
        List<String> proNames = new ArrayList<>();
        proNames.add("id");
        proNames.add("create_user");
        proNames.add("modify_user");
        proNames.add("delete_flag");
        String tmp = "package #{packageDir};";
        StringBuilder sb = new StringBuilder("\n\t");


    }
}
