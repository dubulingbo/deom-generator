package edu.dublbo.generator.mapper;

import edu.dublbo.generator.entity.DefaultModelDetail;
import edu.dublbo.generator.entity.TDemoModelDetail;

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
    List<TDemoModelDetail> listAll();

    void batchAdd(List<DefaultModelDetail> detailList);

    void add01(DefaultModelDetail entity);

    void batchUpdateDelete(Map<String, Object> condition);
}
