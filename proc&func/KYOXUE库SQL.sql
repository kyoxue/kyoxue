use kyoxue;
/**
 KYOXUE库表设计 by kyoxue 2017-11-13 22:47
 注意：关于USING BTREE的唯一索引，MYSQL高版本放前面，低版本放后面
 **/
-- 通知表 前端通过功能块来限制展示通知
CREATE TABLE `t_notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `title` varchar(40) NOT NULL COMMENT '通知标题',
  `content` varchar(500) NOT NULL DEFAULT '暂无通知...' COMMENT '通知内容',
  `enable` char(1) NOT NULL DEFAULT 'Y' COMMENT '是否有效(Y/N)',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` int(11) NOT NULL COMMENT '创建人ID',
  PRIMARY KEY (`id`),
  KEY `index_notice_title` (`title`),
  KEY `index_notice_createtime` (`createtime`),
  KEY `index_notice_creater` (`creater`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='通知表'

-- 系统参数表 
CREATE TABLE `t_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ckey` varchar(64) NOT NULL COMMENT '配置KEY',
  `cvalue` varchar(256) COMMENT '配置信息',
  `ctype` int(11) NOT NULL COMMENT '配置类别',
  `enable` char(1) NOT NULL DEFAULT 'Y' COMMENT '是否有效(Y/N)',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` int(11) NOT NULL COMMENT '创建人ID',
  `updatetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  `modifier` int(11) COMMENT '修改人ID',
  `desc` varchar(256) COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_ckey` USING BTREE(`ckey`) ,
  KEY `index_config_ckey` (`ckey`),
  KEY `index_config_ctype` (`ctype`),
  KEY `index_config_createtime` (`createtime`),
  KEY `index_config_creater` (`creater`),
  KEY `index_config_modifier` (`modifier`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='系统参数表'

-- 参数类别表
CREATE TABLE `t_configtype` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ctypename` varchar(64) NOT NULL COMMENT '配置类别名称',
  `enable` char(1) NOT NULL DEFAULT 'Y' COMMENT '是否有效(Y/N)',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` int(11) NOT NULL COMMENT '创建人ID',
  `updatetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  `modifier` int(11) COMMENT '修改人ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_ctypename` USING BTREE(`ctypename`),
  KEY `index_configtype_creater` (`creater`),
  KEY `index_configtype_modifier` (`modifier`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='参数类别表'

-- 系统日志表
CREATE TABLE `t_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `logtype` int(11) NOT NULL COMMENT '日志操作类别',
  `logcontent` varchar(1000) NOT NULL DEFAULT 'Y' COMMENT '是否有效(Y/N)',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `uid` int(11) COMMENT '操作人ID',
  `backfeild_a` varchar(64) COMMENT '备份字段a',
  `backfeild_b` varchar(64) COMMENT '备份字段b',
  `backfeild_c` varchar(64) COMMENT '备份字段c',
  `backfeild_d` varchar(64) COMMENT '备份字段d',
  PRIMARY KEY (`id`),
  KEY `index_log_logtype` (`logtype`),
  KEY `index_log_uid` (`uid`),
  KEY `index_log_createtime` (`createtime`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='系统日志表'

-- 日志操作类别表
CREATE TABLE `t_logtype` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `logtypename` varchar(64) NOT NULL COMMENT '日志操作类别名称',
  `enable` char(1) NOT NULL DEFAULT 'Y' COMMENT '是否有效(Y/N)',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` int(11) NOT NULL COMMENT '创建人ID',
  `updatetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  `modifier` int(11) COMMENT '修改人ID',
  KEY `index_logtype_creater` (`creater`),
  KEY `index_logtype_modifier` (`modifier`),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_logtypename` USING BTREE(`logtypename`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='日志操作类别表'

-- 下拉选项表
CREATE TABLE `t_selectoption` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `otext` varchar(64) NOT NULL COMMENT '选项显示文本',
  `ovalue` varchar(128) NOT NULL COMMENT '选项值',
  `scode` varchar(64) NOT NULL COMMENT '下拉参数',
  `enable` char(1) NOT NULL DEFAULT 'Y' COMMENT '是否有效(Y/N)',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` int(11) NOT NULL COMMENT '创建人ID',
  `updatetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  `modifier` int(11) COMMENT '修改人ID',
  PRIMARY KEY (`id`),
  KEY `index_selectoption_ovalue` (`ovalue`),
  KEY `index_selectoption_otext` (`otext`),
  KEY `index_selectoption_scode` (`scode`),
  KEY `index_selectoption_creater` (`creater`),
  KEY `index_selectoption_modifier` (`modifier`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='下拉选项表'

-- 下拉表
CREATE TABLE `t_select` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `sdesc` varchar(128) NOT NULL COMMENT '下拉描述',
  `scode` varchar(64) NOT NULL COMMENT '下拉参数',
  `enable` char(1) NOT NULL DEFAULT 'Y' COMMENT '是否有效(Y/N)',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` int(11) NOT NULL COMMENT '创建人ID',
  `updatetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  `modifier` int(11) COMMENT '修改人ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_scode` USING BTREE(`scode`) ,
  KEY `index_select_scode` (`scode`),
  KEY `index_select_sdesc` (`sdesc`),
  KEY `index_select_creater` (`creater`),
  KEY `index_select_modifier` (`modifier`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='下拉表'

/** 数据初始化 **/
-- 通知窗口30秒后缩小状态
insert into t_config(ckey,cvalue,ctype,creater) values('notice_min_delay','30',1,1);
-- 1分钟轮训一次最新的通知
insert into t_config(ckey,cvalue,ctype,creater) values('notice_reflush_delay','60',1,1);
-- 最新通知取1小时以内发布的
insert into t_config(ckey,cvalue,ctype,creater) values('notice_time_before','1',1,1);
-- 是否初次进入页面立刻显示通知
insert into t_config(ckey,cvalue,ctype,creater) values('notice_immediately','Y',1,1);
-- 是否显示通知
insert into t_config(ckey,cvalue,ctype,creater) values('notice_show','Y',1,1);