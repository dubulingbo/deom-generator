package edu.dublbo.generator.utils;

import edu.dublbo.generator.entity.TDemoModel;
import edu.dublbo.generator.entity.TDemoModelDetail;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private String getCurTimeStr() {
        Date curDate = new Date();
        return new SimpleDateFormat("yyyy年M月d日 H:m:s.S").format(curDate);
    }

    // 生成 Model 层源码
    public List<String> generateModelDemo(List<String> tFile, TDemoModel model, List<TDemoModelDetail> detailList) {
        // 针对特殊类型需要导入的类，直接去重
        Set<String> importZone = new HashSet<>();
        for (TDemoModelDetail detail : detailList) {
            String qualifyClassName = detail.getQualifiedProType();
            // 排除 lang包下面的类型
            if (!StringUtils.isEmpty(qualifyClassName) && !qualifyClassName.contains("java.lang")) {
                importZone.add(qualifyClassName);
            }
        }

        logger.info(importZone.toString());

        // 计算所在包路径
        String packageDir = model.getName();
        packageDir = packageDir.substring(0, packageDir.lastIndexOf("."));


        FileCoo coo;
        String tmp;
        String target;
        StringBuilder rowBuilder = new StringBuilder();
        String[] singleRowTags = {"#{packageDir}", "#{remark}", "#{user}", "#{curTime}", "#{className}"};
        List<String> singleRowValues = new ArrayList<>();
        singleRowValues.add(packageDir);
        singleRowValues.add(model.getRemark());
        singleRowValues.add(demoAuthor);
        singleRowValues.add(getCurTimeStr());
        singleRowValues.add(model.getName().substring(model.getName().lastIndexOf(".") + 1));

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
        target = "#{importZone}";
        coo = findFirstRowIndex(tFile, target);
        for (String s : importZone) {
            rowBuilder.append("import ").append(s).append(";\n");
        }
        tFile.set(coo.listIndex, rowBuilder.toString());

        rowBuilder.setLength(0);
        target = "#{propertyZone}";
        coo = findFirstRowIndex(tFile, target);
        for (TDemoModelDetail de : detailList) {
            rowBuilder.append("\t// ")
                    .append(de.getRemark())
                    .append("\n\tprivate ")
                    .append(de.getProType())
                    .append(" ").append(de.getPropertyName()).append(";\n");
        }
        tFile.set(coo.listIndex, rowBuilder.toString());

        rowBuilder.setLength(0);
        target = "#{getAndSetMethod}";
        coo = findFirstRowIndex(tFile, target);
        for (TDemoModelDetail de : detailList) {
            String typeStr = de.getProType();
            rowBuilder.append("\tpublic ").append(typeStr).append(" ")
                    .append(ObjectUtils.field2GetMethodName(de.getPropertyName()))
                    .append("() {\n\t\treturn ").append(de.getPropertyName())
                    .append(";\n\t}\n\n\tpublic void ")
                    .append(ObjectUtils.field2SetMethodName(de.getPropertyName()))
                    .append("(").append(typeStr).append(" ")
                    .append(de.getPropertyName())
                    .append(") {\n\t\tthis.")
                    .append(de.getPropertyName())
                    .append(" = ")
                    .append(de.getPropertyName())
                    .append(";\n\t}\n\n");
        }
        tFile.set(coo.listIndex, rowBuilder.toString());


//return "TBasicContract[id="+id+",status="+status+",contractType="+contractType+",
// signDate="+signDate+",expireDate="+expireDate+",remark="+remark+",
// companyId="+companyId+",contractName="+contractName+",
// imageInfo="+imageInfo+",createUser="+createUser+",
// createTime="+createTime+",lastUser="+lastUser+",
// lastTime="+lastTime+",deleteFlag="+deleteFlag+"]";

        rowBuilder.setLength(0);
        target = "#{toStringMethod}";
        coo = findFirstRowIndex(tFile, target);
        rowBuilder.append("\t@Override \n\tpublic String toString() {\n\t\treturn \"")
                .append(model.getName().substring(model.getName().lastIndexOf(".") + 1))
                .append("{");
        for (TDemoModelDetail de : detailList) {
            rowBuilder.append(de.getPropertyName()).append("=\"+")
                    .append(de.getPropertyName())
                    .append("+\", ");
        }
        rowBuilder.append("}\";\n\t}\n");
        tFile.set(coo.listIndex, rowBuilder.toString());

        return tFile;

    }


    private FileCoo findFirstRowIndex(List<String> list, String target) {
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

    public void main(String[] args) {
        String tmp = "package #{packageDir};";
        String target = "#{packageDir}";
        int st = tmp.indexOf("#{packageDir}");
        System.out.println(st);
        System.out.println(tmp.substring(st + target.length() - 1));
    }
}
