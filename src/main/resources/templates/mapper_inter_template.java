package #{packageDir};

import java.util.Map;
import java.util.List;
import #{importZone};

/**
 * #{remark} Mapper接口
 * @author #{user}
 * @since #{curTime}
 * i believe i can i do
 */
public interface #{mapperName} {
    void add(#{modelName} entity);
    void delete(String id);
    void update(#{modelName} entity);
    #{modelName} get(String id);
    List<#{modelName}> listAll();
    List<#{modelName}> select(Map<String, Object> condition);
    Integer count();
}