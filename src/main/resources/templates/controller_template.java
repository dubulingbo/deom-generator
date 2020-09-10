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
@Api(value = "科室",tags=" 科室")
@RestController
@RequestMapping(value="/basic/department")
public class BasicDepartmentRest {

    @Autowired
    private BasicDepartmentService service;

    @Autowired
    private SnowflakeIdWorker idWorker;

    @ApiOperation(value = "增加科室")
    @ApiImplicitParams({
            // 代码生成工具根据model类生成所有字段描述注解
            @ApiImplicitParam(name="departmentId",value="科室ID",required = true,type="form",paramType="String"),
            @ApiImplicitParam(name="parentId",value="上级ID",required = true,type="form",paramType="String"),
            @ApiImplicitParam(name="code",value="编码",required = true,type="form",paramType="String"),
            @ApiImplicitParam(name="name",value="名称",required = true,type="form",paramType="String"),
            @ApiImplicitParam(name="spellCode",value="拼音码",required = true,type="form",paramType="String"),
            @ApiImplicitParam(name="positon",value="位置",required = true,type="form",paramType="String"),
            @ApiImplicitParam(name="lastLevel",value="末级",required = true,type="form",paramType="String"),
            @ApiImplicitParam(name="filingTime",value="建表时间",required = false,type="form",paramType="Date"),
            @ApiImplicitParam(name="shiftTime",value="撤档时间",required = false,type="form",paramType="Date"),
            @ApiImplicitParam(name="envCategory",value="环境类别，字典：ssd_department_env_category",required = false,type="form",paramType="String"),
            @ApiImplicitParam(name="site",value="站点，字典：ssd_department_site",required = false,type="form",paramType="String"),
            @ApiImplicitParam(name="sortNo",value="顺序",required = false,type="form",paramType="Integer"),
            @ApiImplicitParam(name="deptAlias",value="别名",required = false,type="form",paramType="String"),
            @ApiImplicitParam(name="locationCode",value="位置编码",required = false,type="form",paramType="String"),
            @ApiImplicitParam(name="chargePerson",value="部门负责人，外键，关联员工表",required = false,type="form",paramType="String"),

    })
    @PostMapping
    public ResponsePacket<TBasicDepartment> add(@Validated TBasicDepartment entity,HttpServletRequest request) {

        entity.setId(idWorker.nextStringId());
        entity.setCreateTime(new Date());
        entity.setLastTime(entity.getCreateTime());
        entity.setCreateUser(AuthUtils.getCurrentUser(request));
        entity.setLastUser(AuthUtils.getCurrentUser(request));
        entity.setDeleteFlag(0);
        service.save(entity);
        return ResponsePacket.generateSuccessPacket(entity);
    }

    @ApiOperation(value = "修改科室")
    @ApiImplicitParams({
            // 代码生成工具根据model类生成所有字段描述注解
            @ApiImplicitParam(name="id",value="主键",required = false,type="query",paramType="String"),
            @ApiImplicitParam(name="departmentId",value="科室ID",required = true,type="form",paramType="String"),
            @ApiImplicitParam(name="parentId",value="上级ID",required = true,type="form",paramType="String"),
            @ApiImplicitParam(name="code",value="编码",required = true,type="form",paramType="String"),
            @ApiImplicitParam(name="name",value="名称",required = true,type="form",paramType="String"),
            @ApiImplicitParam(name="spellCode",value="拼音码",required = true,type="form",paramType="String"),
            @ApiImplicitParam(name="positon",value="位置",required = true,type="form",paramType="String"),
            @ApiImplicitParam(name="lastLevel",value="末级",required = true,type="form",paramType="String"),
            @ApiImplicitParam(name="filingTime",value="建表时间",required = false,type="form",paramType="Date"),
            @ApiImplicitParam(name="shiftTime",value="撤档时间",required = false,type="form",paramType="Date"),
            @ApiImplicitParam(name="envCategory",value="环境类别，字典：ssd_department_env_category",required = false,type="form",paramType="String"),
            @ApiImplicitParam(name="site",value="站点，字典：ssd_department_site",required = false,type="form",paramType="String"),
            @ApiImplicitParam(name="sortNo",value="顺序",required = false,type="form",paramType="Integer"),
            @ApiImplicitParam(name="deptAlias",value="别名",required = false,type="form",paramType="String"),
            @ApiImplicitParam(name="locationCode",value="位置编码",required = false,type="form",paramType="String"),
            @ApiImplicitParam(name="chargePerson",value="部门负责人，外键，关联员工表",required = false,type="form",paramType="String"),

    })
    @PutMapping(value="/{id}")
    public ResponsePacket<TBasicDepartment> edit(@PathVariable(value="id") String id ,HttpServletRequest request){

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

    @ApiOperation(value = "获取科室详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="主键",required = false,type="query",paramType="String"),

    })
    @GetMapping(value="/{id}")
    public ResponsePacket<TBasicDepartment> detail(@PathVariable(value="id") String id){
        TBasicDepartment entity = service.get(id);
        return ResponsePacket.generateSuccessPacket(entity);
    }

    @ApiOperation(value = "删除科室")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="主键",required = false,type="query",paramType="String"),

    })
    @DeleteMapping(value="/{id}")
    public ResponsePacket<BaseResponseData> delete(@PathVariable(value="id") String id,HttpServletRequest request){

        TBasicDepartment entity = service.get(id);
        entity.setLastUser(AuthUtils.getCurrentUser(request));
        entity.setLastTime(new Date());
        entity.setDeleteFlag(1);
        service.update(entity);
        return ResponsePacket.generateSuccessPacket();
    }
}
