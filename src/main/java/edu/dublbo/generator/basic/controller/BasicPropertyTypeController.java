package edu.dublbo.generator.basic.controller;

import edu.dublbo.generator.common.result.ResponseResult;
import edu.dublbo.generator.common.result.Result;

import edu.dublbo.generator.basic.service.DemoPropertyTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author DubLBo
 * @since 2020-09-16 15:17
 * i believe i can i do
 */
@RestController
@RequestMapping(value = "/basic/property/type")
public class BasicPropertyTypeController {
    private static final Logger logger = LoggerFactory.getLogger(BasicPropertyTypeController.class);

    @Autowired
    private DemoPropertyTypeService service;

    @GetMapping("/s")
    public Result<Map<String, Object>> listAll() {

//        logger.info("delete flag : {}", condition.get("deleteFlag") == null ? null : condition.get("deleteFlag").toString());

        return ResponseResult.generateSuccessResult(service.listAll());
    }

    @GetMapping(value="/colTypes")
    public Result<Map<String, Object>> listColTypes(){
        return ResponseResult.generateSuccessResult(service.listColTypes());
    }
}
