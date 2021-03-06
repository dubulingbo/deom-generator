package edu.dublbo.generator.basic.controller;

import edu.dublbo.generator.common.exception.OptErrorException;
import edu.dublbo.generator.common.result.BaseResponseData;
import edu.dublbo.generator.common.result.OptStatus;
import edu.dublbo.generator.common.result.Result;
import edu.dublbo.generator.basic.service.DemoModelService;
import edu.dublbo.generator.common.result.ResponseResult;
import edu.dublbo.generator.basic.entity.TDemoModel;
import edu.dublbo.generator.common.utils.WebObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DubLBo
 * @since 2020-09-05 19:14
 * i believe i can i do
 */
@RestController
@RequestMapping(value = "/basic/model")
public class BasicModelController {
    private static final Logger logger = LoggerFactory.getLogger(BasicModelController.class);
    private DemoModelService service;

    @Autowired
    public void setService(DemoModelService service) {
        this.service = service;
    }

    // 增加 模型
    @PostMapping
    public ResponseResult<TDemoModel> add(@RequestBody @Validated TDemoModel model) {
        TDemoModel entity = service.add(model);
        return ResponseResult.generateSuccessResult(entity);
    }

    // 删除 模型
    @DeleteMapping(value = "/{id}")
    public ResponseResult<BaseResponseData> delete(@PathVariable String id) {
        if (StringUtils.isEmpty(id)) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "请求参数为空");
        }
        TDemoModel entity = service.get(id);

        if (entity == null) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "要删除的对象不存在");
        }
        service.delete(entity);

        return ResponseResult.generateSuccessResult();
    }

    // 查询所有存在的模型
    @GetMapping("/s")
    public Result<Map<String, Object>> list(@RequestParam(value = "modelName", required = false) String modelName) {
        Map<String, Object> condition = new HashMap<>();
        logger.info("modelName : {}", modelName);
        condition.put("name", modelName == null ? null : modelName.trim());
        condition.put("_order", "modify_time");
        condition.put("_sort", "DESC");
//        logger.info("delete flag : {}", condition.get("deleteFlag") == null ? null : condition.get("deleteFlag").toString());
        List<TDemoModel> all = service.list(condition);

        Map<String, Object> records = new HashMap<>();
        records.put("records", all);
        return ResponseResult.generateSuccessResult(records);
    }

    @PutMapping(value = "/{id}")
    public ResponseResult<TDemoModel> edit(@PathVariable String id, HttpServletRequest request) {

        TDemoModel entity = service.get(id);
        if (entity == null) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "请求的对象不存在");
        }

        WebObjectUtils.assignObjectField(request, entity, "name", "remark", "tableName", "sortNo");

        service.edit(entity);
        return ResponseResult.generateSuccessResult(entity);
    }

    @GetMapping(value = "/{id}")
    public ResponseResult<TDemoModel> detail(@PathVariable String id) {
        TDemoModel model = service.get(id);
        if (model == null) {
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "要查看的记录不存在");
        }
        return ResponseResult.generateSuccessResult(model);

    }
}
