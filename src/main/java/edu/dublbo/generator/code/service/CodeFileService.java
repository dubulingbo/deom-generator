package edu.dublbo.generator.code.service;

import edu.dublbo.generator.basic.entity.TDemoModel;
import edu.dublbo.generator.basic.entity.TDemoModelDetail;
import edu.dublbo.generator.basic.mapper.TDemoModelDetailMapper;
import edu.dublbo.generator.basic.mapper.TDemoModelMapper;
import edu.dublbo.generator.code.cto.DemoPackageCTO;
import edu.dublbo.generator.common.exception.OptErrorException;
import edu.dublbo.generator.common.result.OptStatus;
import edu.dublbo.generator.common.utils.SnowflakeIdWorker;
import edu.dublbo.generator.utils.DemoFileGenUtils;
import edu.dublbo.generator.utils.FileOperator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

@Service
public class CodeFileService {
    private static final Logger logger = LoggerFactory.getLogger(CodeFileService.class);
    @Autowired
    private SnowflakeIdWorker idWorker;
    @Autowired
    TDemoModelMapper modelMapper;
    @Autowired
    TDemoModelDetailMapper modelDetailMapper;

    @Transactional
    public Map<String, Object> loadCode(String modelId) {
        // 1. 检查模型ID的合理性
        // 2. 查询模型表，模型明细表
        // 3. 生成源码文件
        // 4. 返回结果
        TDemoModel model = modelMapper.get(modelId);
        if (model == null) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模型不存在，请刷新重试");
        }
        Map<String, Object> con = new HashMap<>();
        con.put("modelId", model.getId());
        con.put("_order", "sort_no");
        con.put("inherentFlag", "0"); // 排除固有字段
        List<TDemoModelDetail> detailList = modelDetailMapper.select(con);

        // 预处理阶段：定义一些构建的基础数据
        String modelName = model.getModelName();
        String packageDir = model.getPackageDir();
        String packageDir2 = model.getPackageDir2();
        String modelRemark = model.getRemark();
        String tableName = model.getTableName();

        if (StringUtils.isEmpty(modelName) || StringUtils.isEmpty(packageDir)
                || StringUtils.isEmpty(packageDir2)
                || StringUtils.isEmpty(tableName)) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模型的相关数据已丢失");
        }

        if (detailList == null || detailList.size() == 0) {
            //  模型中只有固有属性，则去除标记直接返回
            Map<String, Object> res = DemoFileGenUtils.generateInherentCode(modelName, modelRemark, tableName, packageDir, packageDir2);
            return res;
        }

        // 属性名列表
        List<String> proNames = new ArrayList<>();
        // 属性名对应的属性类型
        Map<String, String> proTypeMap = new HashMap<>();
        // 属性名对应的属性注释
        Map<String, String> proDescMap = new HashMap<>();
        // 属性名对应的字段名
        Map<String, String> colNameMap = new HashMap<>();
        // 属性名对应的字段的类型
        Map<String, String> colTypeMap = new HashMap<>();
        // 需要导入的包
        Set<String> packageSet = new HashSet<>();

        for (TDemoModelDetail de : detailList) {
            if (!StringUtils.isEmpty(de.getPropertyName()) && !StringUtils.isEmpty(de.getProType()) && !StringUtils.isEmpty(de.getColType())) {
                proNames.add(de.getPropertyName());
                proTypeMap.put(de.getPropertyName(), de.getProType());
                proDescMap.put(de.getPropertyName(), de.getRemark());
                colNameMap.put(de.getPropertyName(), de.getColumnName());
                // 设置字段类型的长度
                StringBuilder tmp = new StringBuilder();
                tmp.append(de.getColType());
                if (de.getColumnLength() != null && de.getColumnLength() > 0) {
                    // 设置了字段长度
                    tmp.append("(").append(de.getColumnLength()).append(")");
                } else if (de.getDefaultColLen() != null && de.getDefaultColLen() > 0) {
                    // 默认长度不为空
                    tmp.append("(").append(de.getDefaultColLen()).append(")");
                }
                colTypeMap.put(de.getPropertyName(), tmp.toString());
            }
            if (!StringUtils.isEmpty(de.getQualifiedProType()) && !de.getQualifiedProType().startsWith("java.lang")
                    && !de.getQualifiedProType().equals("java.util.Date")) {
                packageSet.add(de.getQualifiedProType());
            }
        }

        if (proNames.size() == 0) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模型明细的相关数据已丢失");
        }

        List<String> tableStructureDemo = DemoFileGenUtils.generateTableDemo(tableName, modelRemark, proNames, colNameMap, colTypeMap, proDescMap);
        List<String> entityDemo = DemoFileGenUtils.generateModelDemo(modelName, modelRemark, packageDir, proNames, proTypeMap, proDescMap, packageSet);
        List<String> mapperInterfaceDemo = DemoFileGenUtils.generateMapperInterDemo(modelName, modelRemark, packageDir, packageDir2);
        List<String> mapperXmlDemo = DemoFileGenUtils.generateMapperXmlContent(modelName, tableName, packageDir, packageDir2, proNames, colNameMap);
        List<String> serviceDemo = DemoFileGenUtils.generateServiceDemo(modelName, modelRemark, packageDir, packageDir2);
        List<String> controllerDemo = DemoFileGenUtils.generateControllerDemo(modelName, modelRemark, packageDir, packageDir2);

        Map<String, Object> res = new HashMap<>();
        res.put("tableStructureDemo", String.join("", tableStructureDemo));
        res.put("entityDemo", String.join("", entityDemo));
        res.put("mapperInterfaceDemo", String.join("", mapperInterfaceDemo));
        res.put("mapperXmlDemo", String.join("", mapperXmlDemo));
        res.put("serviceDemo", String.join("", serviceDemo));
        res.put("controllerDemo", String.join("", controllerDemo));

        return res;
    }

    public String zipDemoFile(DemoPackageCTO demoPackage) {
        TDemoModel model = modelMapper.get(demoPackage.getModelId());

        if (model == null || StringUtils.isEmpty(model.getModelName())
                || StringUtils.isEmpty(model.getPackageDir2()) || StringUtils.isEmpty(model.getPackageDir())) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模型的相关数据为空");
        }
        String proDir = System.getProperty("user.dir");
        if(StringUtils.isEmpty(proDir)){
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "获取的项目根路径为空");
        }
        String rootDir = proDir + "/tmpdemo/" + idWorker.nextStringId();
        String path = DemoFileGenUtils.generateModelZipFile(model.getModelName(), model.getPackageDir2(), rootDir, demoPackage);
        return path;

    }

    public void deleteTmpDemoFile() {
        String proDir = System.getProperty("user.dir");
        if(StringUtils.isEmpty(proDir)){
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "获取的项目根路径为空");
        }
        String rootDir = proDir + "/tmpdemo";
        FileOperator.deleteFile(new File(rootDir));
    }
}
