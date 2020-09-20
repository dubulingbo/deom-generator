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
                    break;
                }
            }
        }
        return coo;
    }

    private static String getBusiBeanName(String modelName) {
        if (StringUtils.isEmpty(modelName)) {
            logger.error("model Name is EMPTY #233#");
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模型名为空");
        }
        if (modelName.length() < 5) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模型名长度不合适");
        }
        char first = modelName.charAt(0);
        if (first == 'T' || first == 'V' || first == 'S') {
            return modelName.substring(1, 2).toUpperCase() + modelName.substring(2);
        }
        return modelName.substring(0, 1).toUpperCase() + modelName.substring(1);
    }

    // 移除标记所在的行
    private static void removeAllRowDataTagAt(List<String> data, Set<String> tags) {
        if (tags == null || tags.size() == 0) {
            return;
        }
        for (String tag : tags) {
            FileCoo coo;
            do {
                coo = findFirstRowIndex(data, tag);
                data.remove(coo.listIndex);
            } while (coo.listIndex != -1);
        }
    }

    // 清空标记
    private static void clearAllTagDataTagAt(List<String> data, Set<String> tags) {
        if (tags == null || tags.size() == 0) {
            return;
        }
        for (String tag : tags) {
            FileCoo coo;
            do {
                coo = findFirstRowIndex(data, tag);
                String tmp = data.get(coo.listIndex);
                tmp = tmp.substring(0, coo.strIndex) + tmp.substring(coo.strIndex + tag.length());
                data.set(coo.listIndex, tmp);
            } while (coo.listIndex != -1 && coo.strIndex != -1);
        }
    }

    // 读取模板文件
    private static List<String> readTemplateContent(String filePath) {
        List<String> content;
        try {
            content = FileOperator.readContent(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模板文件操作错误");
        }
        if (content == null || content.size() == 0) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模板文件内容为空");
        }
        return content;
    }

    // 给指定标签出现的所有地方设置新的值，标签不能重复
    private static void setNewValTagAt(List<String> data, List<String> newVals, String... tags) {
        if (tags == null || tags.length == 0) {
            return;
        }
        for (int index = 0; index < tags.length; index++) {
            String target = tags[index];
            FileCoo coo = findFirstRowIndex(data, target);
            while (coo.listIndex != -1 && coo.strIndex != -1) {
                String tmp = data.get(coo.listIndex);
                tmp = tmp.substring(0, coo.strIndex) + newVals.get(index) + tmp.substring(coo.strIndex + target.length());
                data.set(coo.listIndex, tmp);
                coo = findFirstRowIndex(data, target);
            }
        }
    }


    public static Map<String, Object> generateInherentCode(String modelName, String modelRemark, String tableName, String packageDir, String packageDir2) {
        Map<String, Object> res = new HashMap<>();
        List<String> newVals = new ArrayList<>();
        Set<String> removeTags = new HashSet<>();
        List<String> data = readTemplateContent(Constant.TABLE_TF_PATH);

        // table structure
        newVals.add(demoAuthor);
        newVals.add(getCurTimeStr());
        newVals.add(tableName);
        newVals.add(tableName);
        newVals.add(modelRemark);
        setNewValTagAt(data, newVals, "#{user}", "#{curTime}", "#{tableName}", "#{tableName}", "#{memo}");

        removeTags.add("#{newAddColumns}");
        removeAllRowDataTagAt(data, removeTags);
        res.put("tableStructureDemo", String.join("", data));

        // entity
        data = readTemplateContent(Constant.ENTITY_TF_PATH);
        newVals.clear();
        newVals.add(packageDir);
        newVals.add(modelRemark);
        newVals.add(demoAuthor);
        newVals.add(getCurTimeStr());
        newVals.add(modelName);
        setNewValTagAt(data, newVals, "#{packageDir}", "#{remark}", "#{user}", "#{curTime}", "#{className}");

        removeTags.clear();
        removeTags.add("#{importZone}");
        removeTags.add("#{propertyZone}");
        removeTags.add("#{getAndSetMethod}");
        removeAllRowDataTagAt(data, removeTags);

        removeTags.clear();
        removeTags.add("#{toStringZone}");
        clearAllTagDataTagAt(data, removeTags);
        res.put("entityDemo", String.join("", data));

        // mapper interface
        data = readTemplateContent(Constant.MAPPER_INTER_TF_PATH);
        newVals.clear();
        newVals.add(packageDir2 + ".mapper." + modelName + "Mapper");
        newVals.add(packageDir + modelName);
        newVals.add(modelRemark);
        newVals.add(demoAuthor);
        newVals.add(getCurTimeStr());
        newVals.add(modelName + "Mapper");
        newVals.add(modelName);
        setNewValTagAt(data, newVals, "#{packageDir}", "#{importZone}", "#{remark}", "#{user}", "#{curTime}", "#{mapperName}", "#{modelName}");
        res.put("mapperInterfaceDemo", String.join("", data));


        // mapper xml
        data = readTemplateContent(Constant.MAPPER_XML_TF_PATH);
        newVals.clear();
        newVals.add(packageDir2 + ".mapper." + modelName);
        newVals.add(tableName);
        newVals.add(modelName.substring(0, 1).toLowerCase() + modelName.substring(1));
        newVals.add(packageDir + modelName);
        setNewValTagAt(data, newVals, "{{qualifiedMapperName}}", "{{tableName}}", "{{aliasModelName}}", "{{qualifiedModelName}}");

        removeTags.clear();
        removeTags.add("{{columnNameItems}}");
        removeTags.add("{{resultMapZone}}");
        removeTags.add("{{conditionZone}}");
        removeTags.add("{{aliasConditionZone}}");
        removeTags.add("{{updateColItems}}");
        removeAllRowDataTagAt(data, removeTags);

        removeTags.clear();
        removeTags.add("{{columnNameItems}}");
        removeTags.add("{{insertColItems}}");
        clearAllTagDataTagAt(data, removeTags);
        res.put("mapperXmlDemo", String.join("", data));

        // service
        data = readTemplateContent(Constant.SERVICE_TF_PATH);
        newVals.clear();
        String serviceName = getBusiBeanName(modelName);
        newVals.add(packageDir2 + ".service." + serviceName);
        String importZone = "import " + packageDir + "." + modelName + ";\nimport " +
                packageDir2 + ".mapper." + modelName + "Mapper;";
        newVals.add(importZone);
        newVals.add(modelRemark);
        newVals.add(demoAuthor);
        newVals.add(getCurTimeStr());
        newVals.add(serviceName);
        newVals.add(modelName + "Mapper");
        newVals.add(modelName);
        setNewValTagAt(data, newVals, "#{packageDir}", "#{importZone}",
                "#{remark}", "#{user}", "#{curTime}", "#{serviceName}", "#{mapperName}", "#{modelName}");
        res.put("serviceDemo", String.join("", data));

        return res;
    }

    // 生成 Table structure 的代码
    public static List<String> generateTableDemo(String tableName, String tableDesc, List<String> proNames,
                                                 Map<String, String> colNameMap, Map<String, String> colTypeMap, Map<String, String> proDescMap) {
        List<String> tFile = readTemplateContent("./src/main/resources/templates/table_template.sql");
        String[] singleRowTags = {"#{user}", "#{curTime}", "#{tableName}", "#{tableName}", "#{memo}", "#{newAddColumns}"};
        List<String> singleRowValues = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        FileCoo coo;
        singleRowValues.add(demoAuthor);
        singleRowValues.add(getCurTimeStr());
        singleRowValues.add(tableName);
        singleRowValues.add(tableName);
        singleRowValues.add(tableDesc);
        for (int i = 0; i < proNames.size(); i++) {
            String key = proNames.get(i);
            sb.append(colNameMap.get(key)).append(" ")
                    .append(colTypeMap.get(key)).append(" ")
                    .append("comment '").append(proDescMap.get(key)).append("',");
            if (i == proNames.size() - 1) {
                break;
            }
            sb.append("\n\t");
        }
        singleRowValues.add(sb.toString());

        logger.info("singleRowValues : " + singleRowValues.toString());

        for (int var = 0; var < singleRowTags.length; var++) {
            sb.setLength(0);
            String target = singleRowTags[var];
            coo = findFirstRowIndex(tFile, target);
            String tmp = tFile.get(coo.listIndex);
            sb.append(tmp, 0, coo.strIndex).append(singleRowValues.get(var))
                    .append(tmp.substring(coo.strIndex + target.length()));
            tFile.set(coo.listIndex, sb.toString());
        }

        return tFile;
    }

    // 生成 Model 层源码
    public static List<String> generateModelDemo(String modelName, String modelRemark, String packageDir, List<String> proNames,
                                                 Map<String, String> proTypeMap, Map<String, String> proDescMap, Set<String> packageSet) {
        List<String> tFile = readTemplateContent("./src/main/resources/templates/model_template.java");


        String[] singleRowTags = {"#{packageDir}", "#{remark}", "#{user}", "#{curTime}", "#{className}",
                "#{importZone}", "#{propertyZone}", "#{getAndSetMethod}", "#{toStringZone}"};
        List<String> singleRowValues = new ArrayList<>();
        singleRowValues.add(packageDir);
        singleRowValues.add(modelRemark);
        singleRowValues.add(demoAuthor);
        singleRowValues.add(getCurTimeStr());
        singleRowValues.add(modelName);

        StringBuilder var1 = new StringBuilder();

        for (String s : packageSet) {
            var1.append("import ").append(s).append(";\n");
        }
        singleRowValues.add(var1.toString());

        StringBuilder var2 = new StringBuilder();
        StringBuilder var3 = new StringBuilder();
        StringBuilder var4 = new StringBuilder();
        var3.append("\n\t");
        int iMax = proNames.size() - 1;
        for (int i = 0; ; i++) {
            String key = proNames.get(i);
            var2.append("// ").append(proDescMap.get(key))
                    .append("\n\tprivate ").append(proTypeMap.get(key))
                    .append(" ").append(key).append(";");
            var3.append("public ").append(proTypeMap.get(key)).append(" ")
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
                    .append(";\n\t}");
            var4.append("\", ").append(key).append("=\"").append("+").append(key).append("+");
            if (i == iMax) {
                singleRowValues.add(var2.toString());
                singleRowValues.add(var3.toString());
                singleRowValues.add(var4.toString());
                break;
            }
            var2.append("\n\t");
            var3.append("\n\n\t");
        }

        logger.info("The singleRowValues size is " + singleRowValues.size());

        for (int var = 0; var < singleRowTags.length; var++) {
            var1.setLength(0);
            String target = singleRowTags[var];
            FileCoo coo = findFirstRowIndex(tFile, target);
            String tmp = tFile.get(coo.listIndex);
            var1.append(tmp, 0, coo.strIndex).append(singleRowValues.get(var))
                    .append(tmp.substring(coo.strIndex + target.length()));
            tFile.set(coo.listIndex, var1.toString());
        }
        return tFile;
    }

    // 生成 Mapper Interface 层内容
    public static List<String> generateMapperInterDemo(String modelName, String modelRemark, String packageDir, String packageDir2) {
        List<String> tFile = readTemplateContent("./src/main/resources/templates/mapper_inter_template.java");
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
        List<String> tFile = readTemplateContent("./src/main/resources/templates/mapper_xml_template2.xml");
        String[] singleRowTags = {"{{qualifiedMapperName}}", "{{tableName}}", "{{columnNameItems}}",
                "{{resultMapZone}}", "{{conditionZone}}", "{{aliasConditionZone}}", "{{insertColItems}}", "{{updateColItems}}"};
        List<String> singleRowValues = new ArrayList<>();
        singleRowValues.add(packageDir2 + ".mapper." + modelName + "Mapper");
        singleRowValues.add(tableName);

        StringBuilder columnNameItems = new StringBuilder();
        StringBuilder resultMapZone = new StringBuilder();
        StringBuilder conditionZone = new StringBuilder();
        StringBuilder aliasConditionZone = new StringBuilder();
        StringBuilder insertColItems = new StringBuilder();
        StringBuilder updateColItems = new StringBuilder();
        for (int i = 0; i < proNames.size(); i++) {
            String key = proNames.get(i);
            String tmp = String.format("#{%s}", key);
            columnNameItems.append(colNameMap.get(key)).append(",");
            resultMapZone.append(String.format("<result column=\"%s\" property=\"%s\"/>", colNameMap.get(key), key));
            conditionZone.append("<if test=\"").append(key)
                    .append(" != null and ").append(key)
                    .append(" != ''\">\n\t\t\tand ").append(colNameMap.get(key))
                    .append(" = #{").append(key).append("}\n\t\t</if>");
            aliasConditionZone.append("<if test=\"").append(key)
                    .append(" != null and ").append(key)
                    .append(" != ''\">\n\t\t\tand a.").append(colNameMap.get(key))
                    .append(" = #{").append(key).append("}\n\t\t</if>");
            insertColItems.append(tmp).append(",");
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
//            columnNameItems.append(",");
            resultMapZone.append("\n\t\t");
            conditionZone.append("\n\t\t");
            aliasConditionZone.append("\n\t\t");
//            insertColItems.append(",");
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
        List<String> tFile = readTemplateContent("./src/main/resources/templates/service_template.java");
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
