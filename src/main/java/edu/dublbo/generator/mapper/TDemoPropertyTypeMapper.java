package edu.dublbo.generator.mapper;

import edu.dublbo.generator.entity.TDemoColumnType;
import edu.dublbo.generator.entity.TDemoPropertyType;

import java.util.List;

/**
 * @author DubLBo
 * @since 2020-09-16 15:22
 * i believe i can i do
 */
public interface TDemoPropertyTypeMapper {
    List<TDemoPropertyType> listAll();

    List<TDemoColumnType> listColTypes();
}
