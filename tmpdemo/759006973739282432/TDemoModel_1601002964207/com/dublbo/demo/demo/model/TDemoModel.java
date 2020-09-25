package com.dublbo.demo.demo.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 模型 实体类
 * @author guan_exe (demo-generator)
 * @since 2020年9月25日 11:2:42.237
 * i believe i can i do
 */
public class TDemoModel implements Serializable {
//    private static final long serialVersionUID = #{serialNumber}L;
    // 主键
    private String id;
    // 模型名称
	private String name;
	// 模型描述
	private String remark;
	// 模型对应的表名
	private String tableName;
	// 排序
	private Integer sortNo;
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

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
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

    @Override
    public String toString() {
        return "TDemoModel{id="+id+", name="+name+", remark="+remark+", tableName="+tableName+", sortNo="+sortNo+", createUser="+createUser+", createTime="+createTime+", modifyUser="+modifyUser+", modifyTime="+modifyTime+", deleteFlag="+deleteFlag+"}";
    }
}
