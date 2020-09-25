package edu.dublbo.generator.basic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dublbo.generator.common.exception.OptErrorException;
import edu.dublbo.generator.common.result.OptStatus;
import edu.dublbo.generator.common.utils.RedisUtil;
import edu.dublbo.generator.basic.entity.TDemoColumnType;
import edu.dublbo.generator.basic.entity.TDemoPropertyType;
import edu.dublbo.generator.basic.mapper.TDemoPropertyTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DubLBo
 * @since 2020-09-16 15:13
 * i believe i can i do
 */
@Service
public class DemoPropertyTypeService {
    private static final Logger logger = LoggerFactory.getLogger(DemoPropertyTypeService.class);
    @Autowired
    private TDemoPropertyTypeMapper mapper;

    @Autowired
    private RedisUtil redisUtil;

    @Transactional
    public Map<String, Object> listAll() {
        // 先查找Redis缓存
        Map<String, Object> res = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        if (redisUtil.hasKey("propertyTypeList")) {
            logger.info("正在从Redis缓存中获取 属性类型列表。。。");
            // 直接返回
            Object jsonObj = redisUtil.get("propertyTypeList");
            try {
                res =  objectMapper.readValue(jsonObj.toString(), Map.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new OptErrorException(OptStatus.FAIL.getOptCode(), "Json串解析错误");
            }

        } else {

            List<TDemoPropertyType> all = mapper.listAll();
            if (all != null && all.size() != 0) { // 缓存起来
                try {
                    res.put("propertyTypeList", all);
                    String jsonStr = objectMapper.writeValueAsString(res);

                    logger.info("正在将属性类型列表存入Redis缓存。。。{}",jsonStr);
                    redisUtil.set("propertyTypeList", jsonStr, 3 * 60 * 60);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    throw new OptErrorException(OptStatus.FAIL.getOptCode(), "Map转成Json串错误");
                }

            }
        }

        return res;
    }

    @Transactional
    public Map<String, Object> listColTypes() {
        // 先查找Redis缓存
        Map<String, Object> res = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        if (redisUtil.hasKey("columnTypeList")) {
            logger.info("正在从Redis缓存中获取 字段类型列表。。。");
            // 直接返回
            Object jsonObj = redisUtil.get("columnTypeList");
            try {
                res =  objectMapper.readValue(jsonObj.toString(), Map.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new OptErrorException(OptStatus.FAIL.getOptCode(), "Json串解析错误");
            }

        } else {

            List<TDemoColumnType> all = mapper.listColTypes();
            if (all != null && all.size() != 0) { // 缓存起来
                try {
                    res.put("columnTypeList", all);
                    String jsonStr = objectMapper.writeValueAsString(res);

                    logger.info("正在将字段类型列表存入Redis缓存。。。{}",jsonStr);
                    redisUtil.set("columnTypeList", jsonStr, 3 * 60 * 60);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    throw new OptErrorException(OptStatus.FAIL.getOptCode(), "Map转成Json串错误");
                }

            }
        }

        return res;
    }
}
