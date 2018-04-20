use kyoxue;
select * from t_user;
-- 运行中定义的用户关系 运行任务 执行对象
select * from  act_ru_identitylink;
select * from  act_ru_task;
select * from  act_ru_execution;
select * from  act_ru_variable;
-- 历史信息 用户组关系，资源，变量，流程定义，流程任务，流程实例
select * from  act_hi_identitylink;
select * from  act_hi_attachment;
select * from  act_hi_varinst;
select * from  act_hi_procinst;
select * from  act_hi_taskinst;
select * from  act_hi_actinst;
select * from act_hi_comment;
-- 部署和流程定义
select * from  act_re_deployment;
-- 同一个流程图，只部署一次 流程好多次。
select * from  act_re_procdef;
-- 通用表生成ID和资源
select * from  act_ge_property;
select * from  act_ge_bytearray;
select * from ACT_GE_PROPERTY where NAME_='schema.version'

select * from t_user;
select * from t_role;

select * from act_id_user;
select * from act_id_group;
select * from act_id_membership;

/**
根据权限分组类别查询下面所有的任务流程
*/
select  
from
act_re_deployment dep ,
act_re_procdef  procdef,
act_hi_procinst procinst,
acr_hi_taskinst taskinst
where 1=1
and dep.id_ = procdef.deployment_id_
and procdef.id_ = procinst.proc_def_id_
and 
and  procdef.key_='qingjia'
order by taskinst.id_


-- 历史记录流程
select aha.ACT_ID_,aha.ACT_NAME_,aha.START_TIME_,aha.END_TIME_ from 
act_hi_procinst ahp,act_hi_actinst aha,act_hi_identitylink ahi
where 1=1
and ahp.ID_=aha.PROC_INST_ID_
and ahi.PROC_INST_ID_ =aha.PROC_INST_ID_ and ahi.user_id_='我'
order by aha.ID_

-- 实时查看当前我提交的请假审批流程
select art.`NAME_`,art.TASK_DEF_KEY_,art.CREATE_TIME_ 
FROM 
	act_re_procdef arp,
	act_ru_execution are,
	act_ru_task art,
	act_ru_identitylink ari
where 
1=1
AND arp.ID_ = are.PROC_DEF_ID_
AND ari.PROC_INST_ID_ = are.PROC_INST_ID_ AND ari.USER_ID_ = '我'
AND are.id_ = art.EXECUTION_ID_
ORDER BY art.CREATE_TIME_

-- 要添加的组
select role from t_role order by rid;

select account from t_user order by uid;

-- 权限和用户对应分组信息
select r.role,u.account
from t_role r force index(index_type)
inner join t_m_user_role tmr force index(index_fk_rid,index_fk_uid)
on r.rid = tmr.rid
inner join t_user u
on tmr.uid = u.uid
where 1=1
and u.enable = 'Y'
and r.enable = 'Y'
and r.type = 'qingjia'
order by r.rid,u.uid

-- 权限加流程定义分类 不同的工作流定义不同的权限部门套

select 
role as 'role' 
from t_role force index(index_type)
where 1=1 
and enable = 'Y' 
and type = 'qingjia' 
order by rid;		

select u.account from 
 t_user u 
,t_m_user_role m force index(index_fk_uid,index_fk_rid)
,t_role r force index(index_type)
where 1=1
and u.uid = m.uid 
and m.rid = r.rid
and r.enable ='Y'
and u.enable ='Y'
and r.type = 'qingjia'
order by u.uid asc
;









/*
truncate table act_ru_identitylink;
truncate table act_ru_task;
truncate table act_ru_execution;
truncate table act_ru_variable;
truncate table act_hi_identitylink;
truncate table act_hi_attachment;
truncate table act_hi_varinst;
truncate table act_hi_procinst;
truncate table act_hi_taskinst;
truncate table act_hi_actinst;
truncate table act_hi_comment;
truncate table act_re_deployment;
truncate table act_re_procdef;
truncate table act_ge_bytearray;
truncate table act_id_group;
truncate table act_id_info;
truncate table act_id_membership;
truncate table act_id_user; 

*/
/*
生成ID的表不要删，它会以这做基础生成ID，不然又要删全部的表，重新生成了。
truncate table act_ge_property;

*/


-- CALL act_int();


DELIMITER $$
DROP PROCEDURE IF EXISTS act_int$$
CREATE
    PROCEDURE act_int()
    BEGIN
	/*执行批量操作*/
    END$$
DELIMITER ;