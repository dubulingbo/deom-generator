package edu.dublbo.generator.basic.entity;

import edu.dublbo.generator.common.result.BaseResponseData;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * @author DubLBo
 * @since 2020-09-05 16:00
 * i believe i can i do
 */
public class TDemoModel extends BaseResponseData implements Serializable {
    // 主键
    private String id;

    // 模型名称（带包名）
    @NotEmpty
    private String name;
    // 说明
    @NotEmpty
    private String remark;
    // 表名称
    private String tableName;
    private Long sortNo;
    private String createUser;
    private Date createTime;
    private String modifyUser;
    private Date modifyTime;
    private Integer deleteFlag;

    // 模型名（不带包路径）
    private String modelName;
    // 父级包路径
    private String packageDir;
    // 二级包路径（用于计算其他层路径；Service，Rest，Mapper等）
    private String packageDir2;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getSortNo() {
        return sortNo;
    }

    public void setSortNo(Long sortNo) {
        this.sortNo = sortNo;
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

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getPackageDir() {
        return packageDir;
    }

    public void setPackageDir(String packageDir) {
        this.packageDir = packageDir;
    }

    public String getPackageDir2() {
        return packageDir2;
    }

    public void setPackageDir2(String packageDir2) {
        this.packageDir2 = packageDir2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TDemoModel that = (TDemoModel) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "TDemoModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                ", tableName='" + tableName + '\'' +
                ", sortNo=" + sortNo +
                ", createUser='" + createUser + '\'' +
                ", createTime=" + createTime +
                ", modifyUser='" + modifyUser + '\'' +
                ", modifyTime=" + modifyTime +
                ", deleteFlag=" + deleteFlag +
                ", modelName='" + modelName + '\'' +
                ", packageDir='" + packageDir + '\'' +
                ", packageDir2='" + packageDir2 + '\'' +
                '}';
    }
}
