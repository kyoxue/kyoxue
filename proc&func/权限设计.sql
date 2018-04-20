use kyoxue;
/**
 权限用户资源表设计 by kyoxue 2017-11-13 22:47
 注意：关于USING BTREE的唯一索引，MYSQL高版本放前面，低版本放后面
 **/
-- 1权限功能表 前端通过功能块来限制展示区域
CREATE TABLE `r_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `rolecode` char(12) NOT NULL COMMENT '权限码',
  `remark` varchar(128) NOT NULL COMMENT '权限备注',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `enable` char(1) NOT NULL DEFAULT 'Y' COMMENT '是否有效(Y/N)',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` int(11) NOT NULL COMMENT '创建人ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` USING BTREE(`rolecode`) ,
  KEY `index_code_enable` (`enable`),
  KEY `index_code_creater` (`creater`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='权限功能表'

-- 2导航菜单表 该表包含父级子级或再往下级，数据都定义一张表，可以往下无限级。
-- 如果父和子单独列表，不好扩展，比如又需要第三级菜单，就又要建表，更新代码。而一张表只要配置数据。
CREATE TABLE `r_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `menu` varchar(32) NOT NULL COMMENT '导航名称',
  `url` varchar(256) NOT NULL COMMENT '地址',
  `remark` varchar(128) NOT NULL COMMENT '备注',
  `sort` int(11) DEFAULT NULL COMMENT '菜单排序',
  `enable` char(1) NOT NULL DEFAULT 'Y' COMMENT '是否有效(Y/N)',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` int(11) NOT NULL COMMENT '创建人ID',
  `updatetime` timestamp DEFAULT '0000-00-00 00:00:00' COMMENT '更新的时间',
  `modifier` int(11) DEFAULT NULL COMMENT '编辑人',
  `parentid` int(11) DEFAULT NULL COMMENT '父级菜单ID，外键，对应id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_menu` USING BTREE(`menu`) ,
  KEY `index_rolemenu_parentid` (`parentid`),
  KEY `index_rolemenu_enable` (`enable`),
  KEY `index_menu_sort` (sort),
  KEY `index_menu_creater` (`creater`),
  KEY `index_menu_modifier` (`modifier`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='导航菜单表'

-- 3权限表 菜单属在的权限 用户拥有的权限 也叫角色
CREATE TABLE `r_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `rolename` varchar(32) NOT NULL COMMENT '角色名称',
  `remark` varchar(128) NOT NULL COMMENT '备注',
  `enable` char(1) NOT NULL DEFAULT 'Y' COMMENT '是否有效(Y/N)',
  `groupcode` char(12) NOT NULL COMMENT '权限所在分组，用于工作流，比如请假，资源审批等，每个流对应的角色',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `createuserid` int(11) NOT NULL COMMENT '创建人ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_rolename` USING BTREE(`rolename`) ,
  KEY `index_role_rolename` (`rolename`),
  KEY `index_role_groupcode` (`groupcode`),
  KEY `index_role_createuserid` (`createuserid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='角色表'

-- 4用户账户表
CREATE TABLE `r_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `pwd` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '密码',
  `departmentid` int(11) NOT NULL COMMENT '部门ID',
  `enable` char(1) NOT NULL DEFAULT 'Y' COMMENT '是否有效(Y/N)',
  `endtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '离职时间，与enable结合使用，N的时候设置时间',
  `updatetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '操作时间，更新的时间',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` int(11) NOT NULL COMMENT '创建人ID',
  `modifier` int(11) DEFAULT NULL COMMENT '编辑人',
  `isblacklist` char(1) NOT NULL DEFAULT 'N' COMMENT '是否黑名单，N是白名单',
  `lastupdatepwdtime` timestamp DEFAULT '0000-00-00 00:00:00' COMMENT '上一次修改密码时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_username` USING BTREE(`username`) ,
  KEY `index_user_username` (`username`),
  KEY `index_user_departmentid` (`departmentid`),
  KEY `index_user_enable` (`enable`),
  KEY `index_user_endtime` (`endtime`),
  KEY `index_user_createtime` (`createtime`),
  KEY `index_user_isblacklist` (`isblacklist`),
  KEY `index_user_creater` (`creater`),
  KEY `index_user_modifier` (`modifier`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户账户表'

-- 5用户表 创建时间和用户账户表一致
CREATE TABLE `r_userdetail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(64) NOT NULL COMMENT '姓名或昵称',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `qq` varchar(16) DEFAULT NULL COMMENT 'QQ',
  `telephone` varchar(16) DEFAULT NULL COMMENT '电话',
  `mobile` varchar(16) NOT NULL COMMENT '手机',
  `age` int(4) DEFAULT NULL COMMENT '年龄',
  `sex` char(1)  NOT NULL COMMENT '性别 1男0女',
  `birth` varchar(16) DEFAULT NULL COMMENT '生日',
  `icon` varchar(64) DEFAULT NULL COMMENT '头像路径',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `updatetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间，创建或更新的时间',
  `modifier` int(11) DEFAULT NULL COMMENT '编辑人',
  `uid` int(11) NOT NULL COMMENT '用户账户外键',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_uid` USING BTREE(`uid`) ,
  KEY `index_userdetail_uid` (`uid`),
  KEY `index_userdetail_name` (`name`),
  KEY `index_userdetail_sex` (`sex`),
  KEY `index_userdetail_mobile` (`mobile`),
  KEY `index_userdetail_modifier` (`modifier`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户表'

-- 6用户账户与角色映射
CREATE TABLE `r_userrole` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `uid` int(11) NOT NULL COMMENT '人员ID',
  `rid` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  KEY `index_userrole_uid` (`uid`),
  KEY `index_userrole_rid` (`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='人员角色关联表'

-- 7导航菜单与角色映射
CREATE TABLE `r_rolemenu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `rid` int(11) NOT NULL COMMENT '角色ID',
  `mid` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`),
  KEY `index_rolemenu_rid` (`rid`),
  KEY `index_rolemenu_mid` (`mid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='角色菜单关联表'

-- 8r_user权限功能与角色映射
CREATE TABLE `r_rolecode` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `rid` int(11) NOT NULL COMMENT '角色ID',
  `cid` int(11) NOT NULL COMMENT '功能ID',
  PRIMARY KEY (`id`),
  KEY `index_rolecode_rid` (`rid`),
  KEY `index_rolecode_cid` (`cid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='角色功能关联表'

-- 部门表
CREATE TABLE `t_department`(
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `deptname` varchar(64) not null COMMENT '部门名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_deptname` USING BTREE(`deptname`) ,
  KEY `index_department_deptname` (`deptname`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='部门表'
