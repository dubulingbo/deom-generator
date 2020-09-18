package edu.dublbo.generator.demo.controller;

import edu.dublbo.generator.common.result.BaseResponseData;
import edu.dublbo.generator.common.result.ResponseResult;
import edu.dublbo.generator.common.result.Result;
import edu.dublbo.generator.demo.entity.TDemoModelDetail;
import edu.dublbo.generator.demo.service.DemoModelDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 模型明细管理
 *
 * @author DubLBo
 * @since 2020-09-08 14:03
 * i believe i can i do
 */
@RestController
@RequestMapping(value = "/demo/model/detail")
public class DemoModelDetailController {

    @Autowired
    private DemoModelDetailService service;

    // 获取模型下的明细列表
    @GetMapping(value = "/list")
    public Result<Map<String, Object>> list(String modelId) {
//        Map<String, Object> res = service.list(modelId);
        return ResponseResult.generateSuccessResult(service.list(modelId));
    }

    // 修改某个模型明细的内容
    @PutMapping("/{id}")
    public Result<Map<String, Object>> edit(@PathVariable String id, HttpServletRequest request) {
        TDemoModelDetail modelDetail = service.edit(id, request);
        Map<String, Object> res = new HashMap<>();
        res.put("modelDetail", modelDetail);
        return ResponseResult.generateSuccessResult(res);
    }

    // 获取某个模型明细的详情
    @GetMapping(value = "/{id}")
    public Result<Map<String, Object>> detail(@PathVariable String id) {
        TDemoModelDetail modelDetail = service.detail(id);
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("modelDetail", modelDetail);
        return ResponseResult.generateSuccessResult(res);
    }

    // 增加一条模型明细记录
    @PostMapping
    public Result<Map<String, Object>> add(@RequestBody @Validated TDemoModelDetail entity) {
        TDemoModelDetail modelDetail = service.add(entity);
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("modelDetail", modelDetail);
        return ResponseResult.generateSuccessResult(res);
    }

    // 删除一条模型明细记录
    @DeleteMapping(value = "/{id}")
    public ResponseResult<BaseResponseData> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseResult.generateSuccessResult();
    }

    // 生成源码
    @GetMapping(value = "/demo/generate")
    public void demoGenerate(String modelId){
        service.demoGenerate(modelId);
    }
}
