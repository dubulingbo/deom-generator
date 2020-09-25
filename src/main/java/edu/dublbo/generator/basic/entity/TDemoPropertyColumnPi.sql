drop table if exists `t_demo_property_column_pi`;
create table `t_demo_property_column_pi`(
	`id` varchar(32) comment '主键',
    `property_type_id` varchar(32) comment '属性类型ID',
    `column_type_id` varchar(32) comment '字段类型ID',
    `sort_no` int default 999 comment '排序',
    `remark` varchar(512) comment '备注',
	`create_user` varchar(64) comment '创建人',
	`create_time` datetime comment '创建时间',
	`modify_user` varchar(64) comment '最后更新人',
	`modify_time` datetime comment '最后更新时间',
	`delete_flag` smallint(4) comment '是否删除（1：是，0：否）',
	primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='属性、字段类型的关系表';


