package edu.dublbo.generator.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author DubLBo
 * @since 2020-09-09 11:08
 * i believe i can i do
 */
public class TDemoColumnType implements Serializable {
    private static final long serialVersionUID = 3645089166543799430L;

    // 主键
    private String id;
    // 类型名称
    private String name;
    // 字段类型默认长度
    private Integer defaultLen;
    // 序号
    private Integer sortNo;
    // 备注
    private String remark;
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

    public TDemoColumnType() {
    }

    public TDemoColumnType(String id, String name, Integer defaultLen, Integer sortNo, String remark, String createUser, Date createTime, String modifyUser, Date modifyTime, Integer deleteFlag) {
        this.id = id;
        this.name = name;
        this.defaultLen = defaultLen;
        this.sortNo = sortNo;
        this.remark = remark;
        this.createUser = createUser;
        this.createTime = createTime;
        this.modifyUser = modifyUser;
        this.modifyTime = modifyTime;
        this.deleteFlag = deleteFlag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDefaultLen() {
        return defaultLen;
    }

    public void setDefaultLen(Integer defaultLen) {
        this.defaultLen = defaultLen;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

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
}
