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
import java.io.File;
import java.io.IOException;
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

        Date curDate = new Date();
        entity.setId(idWorker.nextStringId());
        entity.setCreateUser(Constant.CURRENT_USER);
        entity.setCreateTime(curDate);
        entity.setModifyUser(Constant.CURRENT_USER);
        entity.setModifyTime(curDate);
        entity.setDeleteFlag(0);
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
        // 判断模型是否存在
        TDemoModel model = modelMapper.get(modelDetail.getModelId());
        if (model == null) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模型不存在，请刷新后重试");
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
        if(detailList == null || detailList.size() == 0){
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模型明细列表为空，请刷新重试");
        }

        // 3. 生成源码
        // 3.1 生成 Model层
        // 3.2 生成 Mapper Bean Interface 层
        // 3.3 生成 Mapper XML 层
        // 3.4 生成 Service 层
        // 3.5 生成 Controller 层
        // 3.6 生成 sql 文件
        List<String> res;
        try {
            List<String> tFile = FileOperator.readContent(new File("./src/main/resources/templates/model_template.java"));
            res = new DemoFileGenUtils().generateModelDemo(tFile, model, detailList);

        } catch (IOException e) {
            e.printStackTrace();
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模板文件操作错误");
        }
        return res;

    }
}
