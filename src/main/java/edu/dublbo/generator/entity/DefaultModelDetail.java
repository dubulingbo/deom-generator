package edu.dublbo.generator.entity;

import java.util.Date;

/**
 * @author DubLBo
 * @since 2020-09-07 19:57
 * i believe i can i do
 */
public enum DefaultModelDetail {
    // id，createUser，createTime，modifyUser，modifyTime，deleteFlag
    DEFAULT_01("","","id","java.lang.String","主键ID","id","VARCHAR",32,"主键ID",1,"",null,"",null,0),

    DEFAULT_02("","","createUser","java.lang.String","创建人","create_user","VARCHAR",64,"创建人",996,"",null,"",null,0),

    DEFAULT_03("","","createTime","java.util.Date","创建时间","create_time","DATETIME",null,"创建时间",997,"",null,"",null,0),

    DEFAULT_04("","","modifyUser","java.lang.String","最后更新人","modify_user","VARCHAR",64,"最后更新人",998,"",null,"",null,0),

    DEFAULT_05("","","modifyTime","java.util.Date","最后更新时间","modify_time","DATETIME",null,"最后更新时间",999,"",null,"",null,0),

    DEFAULT_06("","","deleteFlag","java.lang.Integer","是否删除（1：是，0：否）","delete_flag","SMALLINT",4,"是否删除（1：是，0：否）",1000,"",null,"",null,0);
    private String id;
    private String modelId;
    private String propertyName;
    private String propertyType;
    private String propertyDesc;
    private String columnName;
    private String columnType;
    private Integer columnTypeLength;
    private String columnDesc;
    private Integer sortNo;
    private String createUser;
    private Date createTime;
    private String modifyUser;
    private Date modifyTime;
    private Integer deleteFlag;

    DefaultModelDetail(String id, String modelId, String propertyName, String propertyType, String propertyDesc, String columnName, String columnType, Integer columnTypeLength, String columnDesc, Integer sortNo, String createUser, Date createTime, String modifyUser, Date modifyTime, Integer deleteFlag) {
        this.id = id;
        this.modelId = modelId;
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.propertyDesc = propertyDesc;
        this.columnName = columnName;
        this.columnType = columnType;
        this.columnTypeLength = columnTypeLength;
        this.columnDesc = columnDesc;
        this.sortNo = sortNo;
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

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyDesc() {
        return propertyDesc;
    }

    public void setPropertyDesc(String propertyDesc) {
        this.propertyDesc = propertyDesc;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public Integer getColumnTypeLength() {
        return columnTypeLength;
    }

    public void setColumnTypeLength(Integer columnTypeLength) {
        this.columnTypeLength = columnTypeLength;
    }

    public String getColumnDesc() {
        return columnDesc;
    }

    public void setColumnDesc(String columnDesc) {
        this.columnDesc = columnDesc;
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
