drop table if exists `t_demo_model_detail`;
create table `t_demo_model_detail`(
	`id` varchar(32) comment '主键' ,
    `model_id` varchar(32) comment '所属模型' ,
    `property_name` varchar(64) comment '属性名（符合驼峰命名）' ,
    `property_type_id` varchar(32) comment '属性类型ID' ,
    `remark` varchar(512) comment '属性说明' ,
    `column_name` varchar(64) comment '表字段名' ,
    `column_type_id` varchar(32) comment '表字段类型ID' ,
    `column_length` int comment '表字段长度' ,
    `sort_no` int comment '序号' ,
    `inherent_flag` tinyint default 0 comment '是否时固有的标记（1：不能删除，0：可删除（默认））' ,
	`create_user` varchar(64) comment '创建人' ,
	`create_time` datetime comment '创建时间' ,
	`modify_user` varchar(64) comment '最后更新人' ,
	`modify_time` datetime comment '最后更新时间' ,
	`delete_flag` smallint comment '是否删除（1是,0否）' ,
	primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模型明细表';