package edu.dublbo.generator.basic.dao;

import edu.dublbo.generator.common.exception.OptErrorException;
import edu.dublbo.generator.common.result.OptStatus;
import edu.dublbo.generator.basic.entity.TDemoModel;
import edu.dublbo.generator.utils.DBOperator;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * @author DubLBo
 * @since 2020-09-05 16:55
 * i believe i can i do
 */
@Repository
public class TDemoModelDao {

    // 往数据库里添加一条记录
    public void add01(TDemoModel entity) {
        try {
            // 创建数据库对象
            DBOperator db = new DBOperator();
            String sql = "insert into t_demo_model" +
                    "(id,name,remark,sort_no,create_user,create_time,modify_user,modify_time,delete_flag)" +
                    "value(?,?,?,?,?,?,?,?,?)";
            PreparedStatement stat = db.getConnection().prepareStatement(sql);
            stat.setObject(1,entity.getId());
            stat.setObject(2,entity.getName());
            stat.setObject(3,entity.getRemark());
            stat.setObject(4,entity.getSortNo());
            stat.setObject(5,entity.getCreateUser());
            stat.setObject(6,entity.getCreateTime());
            stat.setObject(7,entity.getModifyUser());
            stat.setObject(8,entity.getModifyTime());
            stat.setObject(9,entity.getDeleteFlag());
            stat.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "数据库操作错误");
        }
    }

    public void add(TDemoModel entity) {

    }

    //更新
    public void update(TDemoModel entity){
//        if(entity)
    }

}
