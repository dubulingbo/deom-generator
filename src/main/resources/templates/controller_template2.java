package cn.wisefly.ssd.basic.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.wisefly.framework.common.bean.BaseResponseData;
import cn.wisefly.framework.common.bean.Page;
import cn.wisefly.framework.common.bean.ResponsePacket;
import cn.wisefly.framework.dbi.utils.SnowflakeIdWorker;
import cn.wisefly.framework.web.bean.Condition;
import cn.wisefly.framework.web.utils.PageUtils;
import cn.wisefly.framework.web.utils.WebObjectUtils;
import cn.wisefly.ssd.core.utils.AuthUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cn.wisefly.ssd.basic.model.TBasicDepartment;
import cn.wisefly.ssd.basic.service.BasicDepartmentService;
/**
 * #{modelName} 控制类
 * @author admin
 * @date 2020-08-18 17:03:50.113
 **/
@RestController
@RequestMapping(value="/basic/department")
public class BasicDepartmentRest {

    @Autowired
    private BasicDepartmentService service;

    @Autowired
    private SnowflakeIdWorker idWorker;

    @PostMapping
    public ResponsePacket<TBasicDepartment> add(@Validated TBasicDepartment entity, HttpServletRequest request) {

        entity.setId(idWorker.nextStringId());
        entity.setCreateTime(new Date());
        entity.setLastTime(entity.getCreateTime());
        entity.setCreateUser(AuthUtils.getCurrentUser(request));
        entity.setLastUser(AuthUtils.getCurrentUser(request));
        entity.setDeleteFlag(0);
        service.save(entity);
        return ResponsePacket.generateSuccessPacket(entity);
    }

    @PutMapping(value="/{id}")
    public ResponsePacket<TBasicDepartment> edit(@PathVariable(value="id") String id, HttpServletRequest request){

        TBasicDepartment oll = service.get(id);
        if(oll!=null){
            // "id","departmentId","parentId","code","name","spellCode","positon","lastLevel","filingTime","shiftTime","envCategory","site","sortNo","deptAlias","locationCode","chargePerson","createUser","createTime","lastUser","lastTime","deleteFlag",
            WebObjectUtils.assignObjectField(request,oll,"departmentId","parentId","code","name","spellCode","positon","lastLevel","filingTime","shiftTime","envCategory","site","sortNo","deptAlias","locationCode","chargePerson");
            oll.setLastTime(new Date());
            oll.setLastUser(AuthUtils.getCurrentUser(request));
            service.update(oll);
        }
        return ResponsePacket.generateSuccessPacket(oll);
    }

    @GetMapping(value="/{id}")
    public ResponsePacket<TBasicDepartment> detail(@PathVariable(value="id") String id){
        TBasicDepartment entity = service.get(id);
        return ResponsePacket.generateSuccessPacket(entity);
    }

    @DeleteMapping(value="/{id}")
    public ResponsePacket<BaseResponseData> delete(@PathVariable(value="id") String id, HttpServletRequest request){

        TBasicDepartment entity = service.get(id);
        entity.setLastUser(AuthUtils.getCurrentUser(request));
        entity.setLastTime(new Date());
        entity.setDeleteFlag(1);
        service.update(entity);
        return ResponsePacket.generateSuccessPacket();
    }
}
