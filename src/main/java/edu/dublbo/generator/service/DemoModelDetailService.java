package edu.dublbo.generator.service;

import edu.dublbo.generator.common.exception.OptErrorException;
import edu.dublbo.generator.common.result.OptStatus;
import edu.dublbo.generator.entity.TDemoModel;
import edu.dublbo.generator.entity.TDemoModelDetail;
import edu.dublbo.generator.mapper.TDemoModelDetailMapper;
import edu.dublbo.generator.mapper.TDemoModelMapper;
import edu.dublbo.generator.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author DubLBo
 * @since 2020-09-08 14:12
 * i believe i can i do
 */
@Service
public class DemoModelDetailService {
    private static final Logger logger = LoggerFactory.getLogger(DemoModelDetailService.class);
    @Autowired
    private TDemoModelDetailMapper mapper;
    @Autowired
    private TDemoModelMapper modelMapper;
    @Autowired
    private SnowflakeIdWorker idWorker;

    @Transactional
    public Map<String, Object> list(String id) {
        if (id == null || StringUtils.isEmpty(id)) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "参数为空");
        }
        // 检查当前模型是否存在
        TDemoModel model = modelMapper.get(id);
        if (model == null) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "请求的模型不存在");
        }

        // 获取该模型下的模型明细列表
        Map<String, Object> condition = new HashMap<>();
        condition.put("modelId", model.getId());
        condition.put("_order", "sort_no");
        List<TDemoModelDetail> modelDetailList = mapper.select(condition);

        if (modelDetailList == null || modelDetailList.size() == 0) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模型明细列表为空");
        }

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("model", model);
        res.put("detailList", modelDetailList);

        return res;
    }

    @Transactional
    public TDemoModelDetail edit(String id, HttpServletRequest request) {
        // 判断明细是否存在
        TDemoModelDetail modelDetail = mapper.get(id);
        if (modelDetail == null) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "要修改的模型明细不存在");
        }
        // 判断模型是否存在
        TDemoModel model = modelMapper.get(modelDetail.getModelId());
        if (model == null) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模型不存在，请刷新后重试");
        }

        // 判断属性名是否已存在
        Map<String, Object> con = new HashMap<>();
        con.put("propertyName", modelDetail.getPropertyName());
        List<TDemoModelDetail> list = mapper.select(con);
        if (list != null && list.size() != 0) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "属性名已存在");
        }

        // 从Http请求中取值
        WebObjectUtils.assignObjectField(request, modelDetail, "propertyName", "propertyTypeId", "remark", "columnName", "columnTypeId");
        // 修改一些必要的字段
        modelDetail.setModifyUser(Constant.CURRENT_USER);
        modelDetail.setModifyTime(new Date());
        mapper.update(modelDetail);

        return modelDetail;
    }

    @Transactional
    public TDemoModelDetail detail(String id) {
        TDemoModelDetail modelDetail = mapper.get(id);
        if (modelDetail == null) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "请求的模型明细不存在");
        }
        TDemoModel model = modelMapper.get(modelDetail.getModelId());
        if (model == null) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模型不存在，请刷新后重试");
        }

        return modelDetail;
    }

    @Transactional
    public TDemoModelDetail add(TDemoModelDetail entity) {
        TDemoModel model = modelMapper.get(entity.getModelId());
        if (model == null) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模型不存在，请刷新后重试");
        }

        // 判断属性名是否已存在
        Map<String, Object> con = new HashMap<>();
        con.put("modelId", model.getId());
        con.put("propertyName", entity.getPropertyName());
        List<TDemoModelDetail> list = mapper.select(con);
        if (list != null && list.size() != 0) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "属性名已存在");
        }

        // 生成序号
        con.clear();
        con.put("modelId", model.getId());
        List<TDemoModelDetail> details = mapper.select(con);
        if (details == null || details.size() == 0) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "系统尚未处理没有属性的模型，请自行实现 #2333#");
        }
        Set<Integer> sortNoSet = new HashSet<>();
        for (TDemoModelDetail de : details) {
            if (de.getSortNo() != null) {
                sortNoSet.add(de.getSortNo());
            }
        }
        List<Integer> sortNos = new ArrayList<>(sortNoSet);
        int sortNo = DemoUtils.generateNextSortNo(sortNos);

        // 想试试有序集合，但是有点力不从心，好像本次可以不用借助它实现也可以。。。。
//        SortedSet<Integer> sortNoSet = new TreeSet<>();
//        for(TDemoModelDetail de : details){
//            if(de.getSortNo() != null){
//                sortNoSet.add(de.getSortNo());
//            }
//        }
        logger.info("=========sortNooooo : " + sortNo);

        Date curDate = new Date();
        entity.setId(idWorker.nextStringId());
        entity.setCreateUser(Constant.CURRENT_USER);
        entity.setCreateTime(curDate);
        entity.setModifyUser(Constant.CURRENT_USER);
        entity.setModifyTime(curDate);
        entity.setDeleteFlag(0);
        entity.setInherentFlag(0);
        entity.setSortNo(sortNo);
        mapper.add(entity);

        return entity;
    }

    @Transactional
    public void delete(String id) {
        // 判断明细是否存在
        TDemoModelDetail modelDetail = mapper.get(id);
        if (modelDetail == null) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "要删除的模型明细不存在");
        }

//        // 判断模型是否存在
//        TDemoModel model = modelMapper.get(modelDetail.getModelId());
//        if (model == null) {
//            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模型不存在，请刷新后重试");
//        }
        // 判断该明细是否为固有的，即不可删除
        if (modelDetail.getInherentFlag() == 1) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "该属性为模型的固有属性，不可删除");
        }

        modelDetail.setModifyUser(Constant.CURRENT_USER);
        modelDetail.setModifyTime(new Date());
        modelDetail.setDeleteFlag(1);
        mapper.update(modelDetail);
    }

    @Transactional
    public List<String> demoGenerate(String modelId) {
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
//        con.put("_sort","ASC");
        List<TDemoModelDetail> detailList = mapper.select(con);
        if (detailList == null || detailList.size() == 0) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模型明细列表为空，请刷新重试");
        }

        // 3. 生成源码
        // 3.1 生成 Model层
        // 3.2 生成 Mapper Bean Interface 层
        // 3.3 生成 Mapper XML 层
        // 3.4 生成 Service 层
        // 3.5 生成 Controller 层
        // 3.6 生成 sql 文件

        // 预处理阶段：定义一些构建的基础数据
        String modelName = model.getModelName();
        String packageDir = model.getPackageDir();
        String packageDir2 = model.getPackageDir2();
        String modelRemark = model.getRemark();
        String tableName = model.getTableName();
        // 属性名列表
        List<String> proNames = new ArrayList<>();
        // 属性名对应的属性类型
        Map<String, String> proTypeMap = new HashMap<>();
        // 属性名对应的属性注释
        Map<String, String> proDescMap = new HashMap<>();
        // 属性名对应的字段名
        Map<String, String> colNameMap = new HashMap<>();
        // 需要导入的包
        Set<String> packageSet = new HashSet<>();

        for (TDemoModelDetail de : detailList) {
            if (!StringUtils.isEmpty(de.getPropertyName()) && !StringUtils.isEmpty(de.getProType())) {
                proNames.add(de.getPropertyName());
                proTypeMap.put(de.getPropertyName(), de.getProType());
                proDescMap.put(de.getPropertyName(), de.getRemark());
                colNameMap.put(de.getPropertyName(), de.getColumnName());
            }
            if (!StringUtils.isEmpty(de.getQualifiedProType()) && !de.getQualifiedProType().contains("java.lang")) {
                packageSet.add(de.getQualifiedProType());
            }
        }

        if (StringUtils.isEmpty(modelName) || StringUtils.isEmpty(packageDir)
                || StringUtils.isEmpty(packageDir2)
                || StringUtils.isEmpty(tableName)
                || proNames.size() == 0) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模型的相关数据已丢失");
        }

//        List<String> res = DemoFileGenUtils.generateModelDemo(modelName, modelRemark, packageDir, proNames, proTypeMap, proDescMap, packageSet);

//        List<String> res = DemoFileGenUtils.generateMapperInterDemo(modelName, modelRemark, packageDir, packageDir2);
//        List<String> res = DemoFileGenUtils.generateMapperXmlContent(modelName, tableName, packageDir, packageDir2, proNames, colNameMap);
        List<String> res = DemoFileGenUtils.generateServiceDemo(modelName, modelRemark, packageDir, packageDir2);

        return res;

    }
}
