drop table if exists `t_demo_column_type`;
create table `t_demo_column_type`(
	`id` varchar(32) comment '主键',
    `name` varchar(64) comment '类型名称',
    `len` int comment '字段类型长度',
    `default_len` int comment '字段类型默认长度',
    `sort_no` int comment '排序',
    `remark` varchar(512) comment '备注',
	`create_user` varchar(64) comment '创建人',
	`create_time` datetime comment '创建时间',
	`modify_user` varchar(64) comment '最后更新人',
	`modify_time` datetime comment '最后更新时间',
	`delete_flag` smallint(4) comment '是否删除（1：是，0：否）',
	primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字段类型表';


