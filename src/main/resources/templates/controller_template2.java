package #{packageDir};

import java.util.Date;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
#{importZone}

/**
 * #{remark} 控制类
 * @author #{user}
 * @since #{curTime}
 **/
@RestController
@RequestMapping(value = "#{requestPath}")
public class #{controllerName} {
    @Autowired
    private #{serviceName} service;

    @PostMapping
    public #{modelName} add(@Validated #{modelName} entity, HttpServletRequest request){
        // 主键生成策略，可自定义
        String id = UUID.randomUUID().toString().replace("-","");
        // logger.info("Generate id is : {}", id);
        entity.setId(id);
        entity.setCreateTime(new Date());
        entity.setLastTime(entity.getCreateTime());
        // 这里的用户可以自定义，默认为 admin
        entity.setCreateUser("admin");
        entity.setLastUser("admin");
        entity.setDeleteFlag(0);
        service.save(entity);
        return entity;
    }

    @PutMapping(value = "/{id}")
    public #{modelName} edit(@PathVariable(value = "id") String id, @RequestBody #{modelName} modifyEntity, HttpServletRequest request) {
        #{modelName} oldEntity = service.get(id);
        if (oll != null) {
            // 这里需根据业务需求修改哪些字段，
            // 但是 id,createUser,createTime,modifyUser,modifyTime,deleteFlag 这些字段不允许用户修改
            // oldEntity 为修改前的对象，modifyEntity 为修改后的对象，该怎么做自己根据业务需求自己定制吧，这里就不生成了
            // Do what you want to do
            oldEntity.setModifyTime(new Date());
            oldEntity.setModifyUser("admin");
            // service.update(oldEntity);
        }
        return oldEntity;
    }

    @GetMapping(value = "/{id}")
    public #{modelName} detail(@PathVariable(value = "id") String id) {
        #{modelName} entity = service.get(id);
        return entity;
    }

    @DeleteMapping(value = "/{id}")
    public #{modelName} delete(@PathVariable(value = "id") String id) {
        #{modelName} entity = service.get(id);
        entity.setModifyUser("admin");
        entity.setModifyTime(new Date());
        entity.setDeleteFlag(1);
        service.update(entity);
        return entity;
    }
}