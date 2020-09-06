
-- @author admin
-- @date 2020-08-15 10:38:48.000
-- @memo 供货公司\生产公司
drop table if exists `test01`;
create table `test01`(
	`id` varchar(32) comment '主键' ,
--	`列名1` varchar(256) comment '公司名称' ,
--	`列名2` varchar(64) comment '拼音码' ,
--	`列名3` varchar(32) comment '五笔码' ,
	`create_user` varchar(64) comment '创建人' ,
	`create_time` datetime comment '创建时间' ,
	`modify_user` varchar(64) comment '最后更新人' ,
	`modify_time` datetime comment '最后更新时间' ,
	`delete_flag` smallint(4) comment '是否删除(1是,0否)' ,
	primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='only for test';

--  增加一列
alter table test01 add `name` varchar(32) comment '这是增加的列233！' after `id`;
alter table test01 add `name02` varchar(32) comment '这是增加的列23345！' before `create_user`;