drop table if exists `t_demo_model`;
create table `t_demo_model`(
	`id` varchar(32) comment '主键' ,
    `name` varchar(64) comment '模型名称（带限定名）',
    `remark` varchar(512) comment '模型说明',
    `table_name` varchar(64) comment '模型对应的表名称',
    `sort_no` bigint comment '排序',
    `model_name` varchar(64) comment '模型名称（不带限定名）',
    `package_dir` varchar(64) comment '父级包路径',
    `package_dir2` varchar(64) comment '二级包路径',
	`create_user` varchar(64) comment '创建人' ,
	`create_time` datetime comment '创建时间' ,
	`modify_user` varchar(64) comment '最后更新人' ,
	`modify_time` datetime comment '最后更新时间' ,
	`delete_flag` smallint(4) comment '是否删除(1是,0否)' ,
	primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模型（类）表';
