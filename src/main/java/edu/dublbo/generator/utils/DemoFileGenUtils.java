package edu.dublbo.generator.utils;

import edu.dublbo.generator.code.cto.DemoPackageCTO;
import edu.dublbo.generator.common.exception.OptErrorException;
import edu.dublbo.generator.common.result.OptStatus;
import edu.dublbo.generator.common.utils.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author DubLBo
 * @since 2020-09-08 20:35
 * i believe i can i do
 */
public class DemoFileGenUtils {
    private static final Logger logger = LoggerFactory.getLogger(DemoFileGenUtils.class);
    private static final String demoAuthor = "guan_exe (demo-generator)";



    public static String generateModelZipFile(String modelName, String packageDir2, String rootDir, DemoPackageCTO content) {
        String zipFileName = modelName+ "_" + System.currentTimeMillis();
        String packagePath = packageToFilePath(packageDir2);
        String zipRootDir = rootDir + File.separator + zipFileName;
        String projectPath = zipRootDir + File.separator + packagePath;
        String serviceName = getBusiBeanName(modelName);
        logger.info("root dir: {}\nzip root dir: {}\nproject path: {}",rootDir,zipRootDir,projectPath);

        FileOperator.writeContent(content.getTableDemo(), projectPath + File.separator + "model", modelName + "Mysql.sql");
        FileOperator.writeContent(content.getEntityDemo(), projectPath + File.separator + "model", modelName + ".java");
        FileOperator.writeContent(content.getMapperInterDemo(), projectPath + File.separator + "mapper", modelName + "Mapper.java");
        FileOperator.writeContent(content.getMapperXmlDemo(), projectPath + File.separator + "mapper", modelName + "Mapper.xml");
        FileOperator.writeContent(content.getServiceDemo(), projectPath + File.separator + "service", serviceName + "Service.java");
        FileOperator.writeContent(content.getControllerDemo(), projectPath + File.separator + "controller", serviceName + "Controller.java");

        String zipFilepath = rootDir + File.separator + zipFileName + ".zip";
        FileOperator.toZip(new File(zipRootDir),new File(zipFilepath),true);
        return zipFilepath;
    }

    private static String packageToFilePath(String packageDir) {
        if(StringUtils.isEmpty(packageDir)){
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "操作的项目目录不存在");
        }
        return packageDir.replace(".",File.separator);
    }

    /**
     * 定义内部文件坐标类
     */
    private static class FileCoo {
        public int listIndex;
        public int strIndex;

        public FileCoo() {
            this.listIndex = -1;
            this.strIndex = -1;
        }
    }

    /**
     * 获取当前系统时间
     *
     * @return yyyy年M月d日 H:m:s.S
     */
    private static String getCurTimeStr() {
        Date curDate = new Date();
        return new SimpleDateFormat("yyyy年M月d日 H:m:s.S").format(curDate);
    }

    /**
     * 寻找第一次出现target的位置
     *
     * @param list   搜索的范围
     * @param target 搜索的目标
     * @return 首次出现目标的坐标
     */
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

    /**
     * 根据模型名获得业务类名称（service，controller）
     *
     * @param modelName 模型名
     * @return 模型的业务名
     */
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

    /**
     * 移除所有【标记的所在行】
     *
     * @param data 操作的数据
     * @param tags 标记集合
     */
    private static void removeAllRowDataTagAt(List<String> data, Set<String> tags) {
        if (tags == null || tags.size() == 0) {
            return;
        }
        for (String tag : tags) {
            FileCoo coo = findFirstRowIndex(data, tag);
            while (coo.listIndex != -1) {
                data.remove(coo.listIndex);
                coo = findFirstRowIndex(data, tag);
            }
        }
    }

    /**
     * 清空所有【标记出现的标记】
     *
     * @param data 操作的数据
     * @param tags 标记集合
     */
    private static void clearAllTagDataTagAt(List<String> data, Set<String> tags) {
        if (tags == null || tags.size() == 0) {
            return;
        }
        for (String tag : tags) {
            FileCoo coo = findFirstRowIndex(data, tag);
            while (coo.listIndex != -1 && coo.strIndex != -1) {
                String tmp = data.get(coo.listIndex);
                tmp = tmp.substring(0, coo.strIndex) + tmp.substring(coo.strIndex + tag.length());
                data.set(coo.listIndex, tmp);
                coo = findFirstRowIndex(data, tag);
            }
        }
    }

    /**
     * 读取模板文件 到 列表中
     *
     * @param filePath 文件的路径
     * @return 内容列表
     */
    private static List<String> readTemplateContent(String filePath) {
        List<String> content;
        content = FileOperator.readContent(new File(filePath));
        if (content == null || content.size() == 0) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模板文件内容为空");
        }
        return content;
    }

    /**
     * 给指定标签出现的所有地方设置新的值，标签不能重复
     *
     * @param data    操作的目标数据
     * @param newVals 标签对应的新值集合
     * @param tags    标签数据
     */
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


    // 生成默认的代码文件
    public static Map<String, Object> generateInherentCode(String modelName, String modelRemark, String tableName, String packageDir, String packageDir2) {
        Map<String, Object> res = new HashMap<>();
        List<String> newVals = new ArrayList<>();
        Set<String> removeTags = new HashSet<>();
        List<String> data;

        // table structure
        newVals.add(demoAuthor);
        newVals.add(getCurTimeStr());
        newVals.add(tableName);
        newVals.add(modelRemark);
        data = readTemplateContent(Constant.TABLE_TF_PATH);
        setNewValTagAt(data, newVals, "#{user}", "#{curTime}", "#{tableName}", "#{memo}");
        removeTags.add("#{newAddColumns}");
        removeAllRowDataTagAt(data, removeTags);
        res.put("tableStructureDemo", String.join("", data));


        // entity
        newVals.clear();
        newVals.add(packageDir);
        newVals.add(modelRemark);
        newVals.add(demoAuthor);
        newVals.add(getCurTimeStr());
        newVals.add(modelName);
        data = readTemplateContent(Constant.ENTITY_TF_PATH);
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
        newVals.clear();
        newVals.add(packageDir2 + ".mapper");
        newVals.add(packageDir + "." + modelName);
        newVals.add(modelRemark);
        newVals.add(demoAuthor);
        newVals.add(getCurTimeStr());
        newVals.add(modelName + "Mapper");
        newVals.add(modelName);
        data = readTemplateContent(Constant.MAPPER_INTER_TF_PATH);
        setNewValTagAt(data, newVals, "#{packageDir}", "#{importZone}", "#{remark}", "#{user}", "#{curTime}", "#{mapperName}", "#{modelName}");
        res.put("mapperInterfaceDemo", String.join("", data));


        // mapper xml
        newVals.clear();
        newVals.add(packageDir2 + ".mapper." + modelName + "Mapper");
        newVals.add(tableName);
        newVals.add(modelName.substring(0, 1).toLowerCase() + modelName.substring(1));
        newVals.add(packageDir + "." + modelName);
        data = readTemplateContent(Constant.MAPPER_XML_TF_PATH);
        setNewValTagAt(data, newVals, "{{qualifiedMapperName}}", "{{tableName}}", "{{aliasModelName}}", "{{qualifiedModelName}}");

        removeTags.clear();
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
        data = generateServiceDemo(modelName, modelRemark, packageDir, packageDir2);
        res.put("serviceDemo", String.join("", data));


        // controller
        data = generateControllerDemo(modelName, modelRemark, packageDir, packageDir2);
        res.put("controllerDemo", String.join("", data));

        return res;
    }


    // 生成 Table structure 的代码
    public static List<String> generateTableDemo(String tableName, String tableDesc, List<String> proNames,
                                                 Map<String, String> colNameMap, Map<String, String> colTypeMap, Map<String, String> proDescMap) {
        StringBuilder sb = new StringBuilder();
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
        List<String> singleRowValues = new ArrayList<>();
        singleRowValues.add(demoAuthor);
        singleRowValues.add(getCurTimeStr());
        singleRowValues.add(tableName);
        singleRowValues.add(sb.toString());
        singleRowValues.add(tableDesc);

        logger.info("singleRowValues : " + singleRowValues.toString());
        List<String> tFile = readTemplateContent(Constant.TABLE_TF_PATH);
        setNewValTagAt(tFile, singleRowValues, "#{user}", "#{curTime}", "#{tableName}", "#{newAddColumns}", "#{memo}");
        return tFile;
    }

    // 生成 Model 层源码
    public static List<String> generateModelDemo(String modelName, String modelRemark, String packageDir, List<String> proNames,
                                                 Map<String, String> proTypeMap, Map<String, String> proDescMap, Set<String> packageSet) {
        StringBuilder var1 = new StringBuilder();
        for (String s : packageSet) {
            var1.append("import ").append(s).append(";\n");
        }

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
                break;
            }
            var2.append("\n\t");
            var3.append("\n\n\t");
        }

        List<String> singleRowValues = new ArrayList<>();
        singleRowValues.add(packageDir);
        singleRowValues.add(var1.toString());
        singleRowValues.add(modelRemark);
        singleRowValues.add(demoAuthor);
        singleRowValues.add(getCurTimeStr());
        singleRowValues.add(modelName);
        singleRowValues.add(var2.toString());
        singleRowValues.add(var3.toString());
        singleRowValues.add(var4.toString());

        logger.info("The singleRowValues size is " + singleRowValues.size());

        List<String> tFile = readTemplateContent(Constant.ENTITY_TF_PATH);
        setNewValTagAt(tFile, singleRowValues, "#{packageDir}", "#{importZone}", "#{remark}", "#{user}", "#{curTime}", "#{className}", "#{propertyZone}", "#{getAndSetMethod}", "#{toStringZone}");
        return tFile;
    }

    // 生成 Mapper Interface 层内容
    public static List<String> generateMapperInterDemo(String modelName, String modelRemark, String packageDir, String packageDir2) {
        List<String> newVals = new ArrayList<>();
        newVals.add(packageDir2 + ".mapper");
        newVals.add(packageDir + "." + modelName);
        newVals.add(modelRemark);
        newVals.add(demoAuthor);
        newVals.add(getCurTimeStr());
        newVals.add(modelName + "Mapper");
        newVals.add(modelName);

        logger.info(newVals.toString());

        List<String> tFile = readTemplateContent(Constant.MAPPER_INTER_TF_PATH);
        setNewValTagAt(tFile, newVals, "#{packageDir}", "#{importZone}", "#{remark}", "#{user}", "#{curTime}", "#{mapperName}", "#{modelName}");
        return tFile;
    }

    // 生成 Mapper Xml 文件内容
    public static List<String> generateMapperXmlContent(String modelName, String tableName, String packageDir, String packageDir2, List<String> proNames, Map<String, String> colNameMap) {
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
                break;
            }
//            columnNameItems.append(",");
            resultMapZone.append("\n\t\t");
            conditionZone.append("\n\t\t");
            aliasConditionZone.append("\n\t\t");
//            insertColItems.append(",");
            updateColItems.append("\n\t\t\t");
        }

        List<String> newVals = new ArrayList<>();
        newVals.add(packageDir2 + ".mapper." + modelName + "Mapper");
        newVals.add(tableName);
        newVals.add(columnNameItems.toString());
        newVals.add(modelName.substring(0, 1).toLowerCase() + modelName.substring(1));
        newVals.add(packageDir + "." + modelName);
        newVals.add(resultMapZone.toString());
        newVals.add(conditionZone.toString());
        newVals.add(aliasConditionZone.toString());
        newVals.add(insertColItems.toString());
        newVals.add(updateColItems.toString());

        logger.info("列表（singleRowValues）的容量为：{}", newVals.size());

        List<String> tFile = readTemplateContent(Constant.MAPPER_XML_TF_PATH);
        String[] singleRowTags = {"{{qualifiedMapperName}}", "{{tableName}}", "{{columnNameItems}}", "{{aliasModelName}}", "{{qualifiedModelName}}",
                "{{resultMapZone}}", "{{conditionZone}}", "{{aliasConditionZone}}", "{{insertColItems}}", "{{updateColItems}}"};
        setNewValTagAt(tFile, newVals, singleRowTags);
        return tFile;
    }

    // 生成 Service层代码
    public static List<String> generateServiceDemo(String modelName, String modelRemark, String packageDir, String packageDir2) {
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
        singleRowValues.add(modelName);

        logger.info("列表（singleRowValues）的容量为：{}", singleRowValues.size());

        List<String> tFile = readTemplateContent(Constant.SERVICE_TF_PATH);
        setNewValTagAt(tFile, singleRowValues, "#{packageDir}", "#{importZone}", "#{remark}", "#{user}", "#{curTime}", "#{serviceName}", "#{mapperName}", "#{modelName}");
        return tFile;
    }

    // 生成 controller 层代码
    public static List<String> generateControllerDemo(String modelName, String modelRemark, String packageDir, String packageDir2) {
        List<String> newVals = new ArrayList<>();
        String serviceName = getBusiBeanName(modelName);
        newVals.add(packageDir2 + ".controller");
        String importZone = "import " + packageDir + "." + modelName + ";\nimport " +
                packageDir2 + ".service." + serviceName + "Service;";
        newVals.add(importZone);
        newVals.add(modelRemark);
        newVals.add(demoAuthor);
        newVals.add(getCurTimeStr());
        String[] paths = packageDir2.split("\\.");
        String requestPath = "/";
        if (paths.length > 1) {
            requestPath += (paths[paths.length - 2] + "/" + paths[paths.length - 1]);
        } else if (paths.length > 0) {
            requestPath += paths[paths.length - 1];
        }
        newVals.add(requestPath);
        newVals.add(serviceName + "Controller");
        newVals.add(serviceName + "Service");
        newVals.add(modelName);

        List<String> data = readTemplateContent(Constant.CONTROLLER_TF_PATH);
        setNewValTagAt(data, newVals, "#{packageDir}", "#{importZone}",
                "#{remark}", "#{user}", "#{curTime}", "#{requestPath}", "#{controllerName}", "#{serviceName}", "#{modelName}");
        return data;
    }

}
