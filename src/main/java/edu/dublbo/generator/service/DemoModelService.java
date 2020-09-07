package edu.dublbo.generator.service;

import edu.dublbo.generator.common.exception.DataErrorException;
import edu.dublbo.generator.common.exception.OptErrorException;
import edu.dublbo.generator.common.result.OptStatus;
import edu.dublbo.generator.common.result.ResponseStatus;
import edu.dublbo.generator.entity.DefaultModelDetail;
import edu.dublbo.generator.entity.TDemoModel;
import edu.dublbo.generator.mapper.TDemoModelDetailMapper;
import edu.dublbo.generator.mapper.TDemoModelMapper;
import edu.dublbo.generator.utils.Constant;
import edu.dublbo.generator.utils.DemoUtils;
import edu.dublbo.generator.utils.SnowflakeIdWorker;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author DubLBo
 * @since 2020-09-05 16:56
 * i believe i can i do
 */
@Service
public class DemoModelService {
    private static final Logger logger = LoggerFactory.getLogger(DemoModelService.class);

    @Autowired
    private TDemoModelMapper mapper;
    @Autowired
    private SnowflakeIdWorker idWorker;
    @Autowired
    private TDemoModelDetailMapper modelDetailMapper;

    /**
     * 增加模型
     *
     * @param entity 模型
     * @return 模型的详细信息
     */
    @Transactional
    public TDemoModel add(TDemoModel entity) {
        // 1. 判空 name，remark
        // 2. 添加模型记录（生成表名）
        // 3. 添加默认的模型属性记录：id，createUser，createTime，modifyUser，modifyTime，deleteFlag

        if (entity == null) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "要添加的对象为空");
        }
        logger.info("模型名：{}，模型说明：{}", entity.getName(), entity.getRemark());
        if (StringUtils.isEmpty(entity.getName()) || StringUtils.isEmpty(entity.getRemark())) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "模型名或类模型说明为空");
        }


        // 添加模型表
        Date curDate = new Date();
        String id = idWorker.nextStringId();
        // 生成模型名和表名
        String modelName = entity.getName();
        if (modelName.endsWith(".")) {
            throw new DataErrorException(ResponseStatus.DATA_ERROR.getCode(), "模型名以点号结尾");
        }
        // 是否带包路径，不带包路径，使用默认包路径
        int point = modelName.lastIndexOf(".");
        if (point == -1) {
            modelName = "com.example." + modelName;
        }
        String tableName = DemoUtils.modelName2TableName(modelName);
        entity.setName(modelName);
        entity.setTableName(tableName);
        entity.setId(id);
        entity.setCreateUser(Constant.CURRENT_USER);
        entity.setCreateTime(curDate);
        entity.setModifyUser(Constant.CURRENT_USER);
        entity.setModifyTime(curDate);
        entity.setDeleteFlag(0);
        mapper.add(entity);

        // 添加模型明细表
        List<DefaultModelDetail> detailList = new ArrayList<>();
        detailList.add(DefaultModelDetail.DEFAULT_01);
        detailList.add(DefaultModelDetail.DEFAULT_02);
        detailList.add(DefaultModelDetail.DEFAULT_03);
        detailList.add(DefaultModelDetail.DEFAULT_04);
        detailList.add(DefaultModelDetail.DEFAULT_05);
        detailList.add(DefaultModelDetail.DEFAULT_06);
        for (DefaultModelDetail en : detailList) {
            en.setId(idWorker.nextStringId());
            en.setModelId(id);
            en.setCreateUser(Constant.CURRENT_USER);
            en.setCreateTime(curDate);
            en.setModifyUser(Constant.CURRENT_USER);
            en.setModifyTime(curDate);
            en.setDeleteFlag(0);
        }


        modelDetailMapper.batchAdd(detailList);


//        entity.setOptCode(OptStatus.SUCCESS.getOptCode());
//        entity.setMessage(OptStatus.SUCCESS.getMessage());
        return entity;
    }

    //更新
    public void update(TDemoModel entity) {
//        if(entity)
    }

    public TDemoModel get(String id) {

        return mapper.get(id);

    }

    @Transactional
    public void delete(TDemoModel entity) {
        // 1. 检查要删除的模型是否为空
        // 2. 删除模型下的所有模型明细
        // 3. 删除模型
        if (entity == null || StringUtils.isEmpty(entity.getId())) {
            throw new DataErrorException(ResponseStatus.DATA_ERROR.getCode(), "模型数据为空，问题不知");
        }

        Date curDate = new Date();
        Map<String, Object> condition = new HashMap<>();
        condition.put("modifyTime", curDate);
        condition.put("modifyUser", Constant.CURRENT_USER);
        condition.put("deleteFlag", 1);
        condition.put("modelId",entity.getId());
        modelDetailMapper.batchUpdateDelete(condition);


        entity.setModifyUser(Constant.CURRENT_USER);
        entity.setModifyTime(curDate);
        entity.setDeleteFlag(1);
        mapper.update(entity);
    }

    public List<TDemoModel> listAll() {
        return mapper.listAll();
    }
}