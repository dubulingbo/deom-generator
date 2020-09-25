package com.dublbo.demo.demo.mapper;

import java.util.Map;
import java.util.List;
import com.dublbo.demo.demo.model.TDemoModel;

/**
 * 模型 Mapper接口
 * @author guan_exe (demo-generator)
 * @since 2020年9月25日 11:2:42.238
 * i believe i can i do
 */
public interface TDemoModelMapper {
    void add(TDemoModel entity);
    void delete(String id);
    void update(TDemoModel entity);
    TDemoModel get(String id);
    List<TDemoModel> listAll();
    List<TDemoModel> select(Map<String, Object> condition);
    Integer count();
}
