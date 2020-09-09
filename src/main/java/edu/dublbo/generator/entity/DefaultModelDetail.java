package edu.dublbo.generator.entity;

import java.util.Date;

/**
 * @author DubLBo
 * @since 2020-09-07 19:57
 * i believe i can i do
 */
public enum DefaultModelDetail {
    // id，createUser，createTime，modifyUser，modifyTime，deleteFlag
    DEFAULT_01("id","753323286297194496","主键ID","id","753323286297194502",32,1),
    DEFAULT_02("createUser","753323286297194496","创建人","create_user","753323286297194502",64,996),
    DEFAULT_03("createTime","753323286297194498","创建时间","create_time","753323286297194512",null,997),
    DEFAULT_04("modifyUser","753323286297194496","最后更新人","modify_user","753323286297194502",64,998),
    DEFAULT_05("modifyTime","753323286297194498","最后更新时间","modify_time","753323286297194512",null,999),
    DEFAULT_06("deleteFlag","753323286297194497","是否删除（1：是，0：否）","delete_flag","753323286297194507",4,1000);
    private String id;
    private String modelId;
    private String propertyName;
    private String propertyTypeId;
    private String remark;
    private String columnName;
    private String columnTypeId;
    private Integer columnLength;
    private Integer sortNo;
    private String createUser;
    private Date createTime;
    private String modifyUser;
    private Date modifyTime;
    private Integer deleteFlag;

    DefaultModelDetail(String propertyName, String propertyTypeId, String remark, String columnName, String columnTypeId, Integer columnLength, Integer sortNo) {
        this.propertyName = propertyName;
        this.propertyTypeId = propertyTypeId;
        this.remark = remark;
        this.columnName = columnName;
        this.columnTypeId = columnTypeId;
        this.columnLength = columnLength;
        this.sortNo = sortNo;
        this.createUser = "";
    }

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
