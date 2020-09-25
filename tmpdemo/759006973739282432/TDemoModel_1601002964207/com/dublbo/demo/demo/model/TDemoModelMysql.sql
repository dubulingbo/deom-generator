-- @author guan_exe (demo-generator)
-- @date 2020年9月25日 11:2:42.236
-- @desc 模型
drop table if exists t_demo_model;
create table t_demo_model(
	id varchar(32) comment '主键',
    name varchar(100) comment '模型名称',
	desc varchar(64) comment '模型描述',
	table_name varchar(32) comment '模型对应的表名',
	sort_no int comment '排序',
	create_user varchar(64) comment '创建人',
	create_time datetime comment '创建时间',
	modify_user varchar(64) comment '最后更新人',
	modify_time datetime comment '最后更新时间',
	delete_flag smallint(4) comment '是否删除（1：是，0：否）',
	primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模型 表';
