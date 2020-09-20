package #{packageDir};

import java.io.Serializable;
import java.util.Date;
#{importZone}
/**
 * #{remark}实体类
 * @author #{user}
 * @since #{curTime}
 * i believe i can i do
 */
public class #{className} implements Serializable {
//    private static final long serialVersionUID = #{serialNumber}L;
    // 主键
    private String id;
    #{propertyZone}
    // 创建人
    private String createUser;
    // 创建时间
    private Date createTime;
    // 最后修改人
    private String modifyUser;
    // 最后修改时间
    private Date modifyTime;
    // 是否删除（1：是，0：否）
    private Integer deleteFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    #{getAndSetMethod}

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Override
    public String toString() {
        return "TDemoModel{id="+id+#{toStringZone}", createUser="+createUser+", createTime="+createTime+", modifyUser="+modifyUser+", modifyTime="+modifyTime+", deleteFlag="+deleteFlag+"}";
    }
}
