-- 数据表Id顺序号
create table t_asc_seq
(
	f_tablename  varchar2(255 byte),
	f_seq        number(32,0),
	f_nodenumber number(3,0)
);
alter table t_asc_seq
 add constraint pk_asc_seq primary key (f_tablename) using index;

-- 伤损记录阅读信息
create table t_rdms_defects_readinfo
(
	id				number(18),
	f_defects_id	number(18),
	f_user_id		number(18),
	f_create_time	date
);
alter table t_rdms_defects_readinfo
 add constraint pk_rdms_defects_readinfo primary key (id) using index;
create index idx_rdms_defects_readinfo_did on t_rdms_defects_readinfo(f_defects_id);