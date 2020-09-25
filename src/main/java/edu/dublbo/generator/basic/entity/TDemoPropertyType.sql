drop table if exists `t_demo_property_type`;
create table `t_demo_property_type`(
	`id` varchar(32) comment '主键',
    `name` varchar(64) comment '名称',
    `qualified_name` varchar(128) comment '全限定名（带包路径）',
    `sort_no` int comment '排序',
    `remark` varchar(512) comment '备注',
	`create_user` varchar(64) comment '创建人',
	`create_time` datetime comment '创建时间',
	`modify_user` varchar(64) comment '最后更新人',
	`modify_time` datetime comment '最后更新时间',
	`delete_flag` smallint(4) comment '是否删除（1：是，0：否）',
	primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='属性类型表';
