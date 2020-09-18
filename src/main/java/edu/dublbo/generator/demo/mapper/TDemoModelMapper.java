package edu.dublbo.generator.demo.mapper;

import edu.dublbo.generator.demo.entity.TDemoModel;

import java.util.List;
import java.util.Map;

/**
 * @author DubLBo
 * @since 2020-09-06 14:26
 * i believe i can i do
 */
public interface TDemoModelMapper {
    void add(TDemoModel entity);
    void update(TDemoModel entity);
    TDemoModel get(String id);
    List<TDemoModel> listAll();
    List<TDemoModel> select(Map<String, Object> condition);
}
