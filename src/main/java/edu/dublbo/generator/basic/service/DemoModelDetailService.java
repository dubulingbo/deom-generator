package edu.dublbo.generator.basic.service;

import edu.dublbo.generator.common.exception.OptErrorException;
import edu.dublbo.generator.common.result.OptStatus;
import edu.dublbo.generator.common.utils.SnowflakeIdWorker;
import edu.dublbo.generator.common.utils.WebObjectUtils;
import edu.dublbo.generator.basic.entity.TDemoModel;
import edu.dublbo.generator.basic.entity.TDemoModelDetail;
import edu.dublbo.generator.basic.mapper.TDemoModelDetailMapper;
import edu.dublbo.generator.basic.mapper.TDemoModelMapper;
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
        con.put("modelId",model.getId());
        con.put("id",modelDetail.getId());

        Integer num = mapper.select1(con);
        if (num != null && num != 0) {
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
}
