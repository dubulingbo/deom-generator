package edu.dublbo.generator.demo.mapper;

import edu.dublbo.generator.demo.entity.DefaultModelDetail;
import edu.dublbo.generator.demo.entity.TDemoColumnType;
import edu.dublbo.generator.demo.entity.TDemoModelDetail;
import edu.dublbo.generator.demo.entity.TDemoPropertyType;

import java.util.List;
import java.util.Map;

/**
 * @author DubLBo
 * @since 2020-09-06 14:26
 * i believe i can i do
 */
public interface TDemoModelDetailMapper {
    void add(TDemoModelDetail entity);
    void update(TDemoModelDetail entity);
    TDemoModelDetail get(String id);
    void batchAdd(List<DefaultModelDetail> detailList);
    void batchUpdateDelete(Map<String, Object> condition);
    List<TDemoModelDetail> select(Map<String, Object> condition);

    void addProType(TDemoPropertyType entity);
    void addColType(TDemoColumnType entity);

    Integer select1(Map<String, Object> con);
}
