drop table if exists `t_demo_model_detail`;
create table `t_demo_model_detail`(
	`id` varchar(32) comment '主键' ,
    `model_id` varchar(32) comment '所属模型' ,
    `property_name` varchar(256) comment '属性名（符合驼峰命名）' ,
    `property_type` varchar(128) comment '属性类型' ,
    `property_desc` varchar(128) comment '属性说明' ,
    `column_name` varchar(64) comment '表字段名' ,
    `column_type` varchar(64) comment '表字段类型' ,
    `column_type_length` int comment '表字段类型长度' ,
    `column_desc` varchar(128) comment '表字段说明' ,
    `sort_no` int(11) comment '排序（可自定义）' ,
	`create_user` varchar(64) comment '创建人' ,
	`create_time` datetime comment '创建时间' ,
	`modify_user` varchar(64) comment '最后更新人' ,
	`modify_time` datetime comment '最后更新时间' ,
	`delete_flag` smallint(1) comment '是否删除（1是,0否）' ,
	primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模型明细表';