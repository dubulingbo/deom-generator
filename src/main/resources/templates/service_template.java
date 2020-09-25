package #{packageDir};

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
#{importZone}

/**
 * #{remark} Service类
 * @author #{user}
 * @since #{curTime}
 * Always believe that something wonderful is about to happen
 */
@Service
public class #{serviceName} {

    @Autowired
    private #{mapperName} mapper;

    @Transactional
    public void save(#{modelName} entity){
        mapper.add(entity);
    }

    @Transactional
    public void delete(String id){
        mapper.delete(id);
    }

    @Transactional
    public void edit(#{modelName} entity){
        mapper.update(entity);
    }

    @Transactional
    public #{modelName} get(String id){
        return mapper.get(id);
    }

    @Transactional
    public List<#{modelName}> listAll(){
        return mapper.listAll();
    }

    @Transactional
    public List<#{modelName}> select(Map<String,Object> condition){
        return mapper.select(condition);
    }
}
