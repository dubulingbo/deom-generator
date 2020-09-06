-- @author #{user}
-- @date #{curTime}
-- @memo #{memo}
drop table if exists ${tableName};
create table ${tableName}(
	`id` varchar(32) comment '主键' ,
    #{newAddColumns}
	`create_user` varchar(64) comment '创建人' ,
	`create_time` datetime comment '创建时间' ,
	`modify_user` varchar(64) comment '最后更新人' ,
	`modify_time` datetime comment '最后更新时间' ,
	`delete_flag` smallint(4) comment '是否删除(1是,0否)' ,
	primary key (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='#{memo}';