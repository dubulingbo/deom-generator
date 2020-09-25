package com.dublbo.demo.demo.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dublbo.demo.demo.model.TDemoModel;
import com.dublbo.demo.demo.mapper.TDemoModelMapper;

/**
 * 模型 Service类
 * @author guan_exe (demo-generator)
 * @since 2020年9月25日 11:2:42.239
 * Always believe that something wonderful is about to happen
 */
@Service
public class DemoModelService {

    @Autowired
    private TDemoModelMapper mapper;

    @Transactional
    public void save(TDemoModel entity){
        mapper.add(entity);
    }

    @Transactional
    public void delete(String id){
        mapper.delete(id);
    }

    @Transactional
    public void edit(TDemoModel entity){
        mapper.update(entity);
    }

    @Transactional
    public TDemoModel get(String id){
        return mapper.get(id);
    }

    @Transactional
    public List<TDemoModel> listAll(){
        return mapper.listAll();
    }

    @Transactional
    public List<TDemoModel> select(Map<String,Object> condition){
        return mapper.select(condition);
    }
}
