package edu.dublbo.generator.basic.entity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author DubLBo
 * @since 2020-09-07 15:59
 * i believe i can i do
 */
public class TDemoModelDetail implements Serializable {
    private static final long serialVersionUID = 238650092367164538L;
    // 主键
    private String id;
    // 所属模型ID
    @NotNull
    private String modelId;
    // 属性名称
    @NotNull
    private String propertyName;
    // 属性类型ID
    @NotNull
    private String propertyTypeId;
    // 备注，注释
    @NotNull
    private String remark;
    // 字段名，根据属性名自动生成
    @NotNull
    private String columnName;
    // 字段类型ID
    @NotNull
    private String columnTypeId;
    // 字段长度
    private Integer columnLength;
    // 排序，默认为900
    private Integer sortNo;
    // 不可删除标记
    private Integer inherentFlag;
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

    // 属性类型名
    private String proType;
    // 属性全限定类型名（带包路径）
    private String qualifiedProType;

    // 字段类型名
    private String colType;
    // 字段默认长度
    private Integer defaultColLen;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyTypeId() {
        return propertyTypeId;
    }

    public void setPropertyTypeId(String propertyTypeId) {
        this.propertyTypeId = propertyTypeId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnTypeId() {
        return columnTypeId;
    }

    public void setColumnTypeId(String columnTypeId) {
        this.columnTypeId = columnTypeId;
    }

    public Integer getColumnLength() {
        return columnLength;
    }

    public void setColumnLength(Integer columnLength) {
        this.columnLength = columnLength;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public Integer getInherentFlag() {
        return inherentFlag;
    }

    public void setInherentFlag(Integer inherentFlag) {
        this.inherentFlag = inherentFlag;
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

    public String getProType() {
        return proType;
    }

    public void setProType(String proType) {
        this.proType = proType;
    }

    public String getQualifiedProType() {
        return qualifiedProType;
    }

    public void setQualifiedProType(String qualifiedProType) {
        this.qualifiedProType = qualifiedProType;
    }

    public String getColType() {
        return colType;
    }

    public void setColType(String colType) {
        this.colType = colType;
    }

    public Integer getDefaultColLen() {
        return defaultColLen;
    }

    public void setDefaultColLen(Integer defaultColLen) {
        this.defaultColLen = defaultColLen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TDemoModelDetail that = (TDemoModelDetail) o;
        return Objects.equals(id, that.id);
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
        return "TDemoModelDetail{" +
                "id='" + id + '\'' +
                ", modelId='" + modelId + '\'' +
                ", propertyName='" + propertyName + '\'' +
                ", propertyTypeId='" + propertyTypeId + '\'' +
                ", remark='" + remark + '\'' +
                ", columnName='" + columnName + '\'' +
                ", columnTypeId='" + columnTypeId + '\'' +
                ", sortNo=" + sortNo +
                ", createUser='" + createUser + '\'' +
                ", createTime=" + createTime +
                ", modifyUser='" + modifyUser + '\'' +
                ", modifyTime=" + modifyTime +
                ", deleteFlag=" + deleteFlag +
                '}';
    }
}
