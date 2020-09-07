package edu.dublbo.generator.mapper;

import edu.dublbo.generator.entity.TDemoModel;

import java.util.List;

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

    Integer addDefaultModelDetail();
}
