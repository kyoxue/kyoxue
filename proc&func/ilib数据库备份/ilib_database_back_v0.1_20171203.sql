/*
SQLyog Enterprise - MySQL GUI v6.14
MySQL - 5.0.41-community-nt : Database - kyoxue
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `kyoxue`;

USE `kyoxue`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `act_evt_log` */

DROP TABLE IF EXISTS `act_evt_log`;

CREATE TABLE `act_evt_log` (
  `LOG_NR_` bigint(20) NOT NULL auto_increment,
  `TYPE_` varchar(64) collate utf8_bin default NULL,
  `PROC_DEF_ID_` varchar(64) collate utf8_bin default NULL,
  `PROC_INST_ID_` varchar(64) collate utf8_bin default NULL,
  `EXECUTION_ID_` varchar(64) collate utf8_bin default NULL,
  `TASK_ID_` varchar(64) collate utf8_bin default NULL,
  `TIME_STAMP_` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `USER_ID_` varchar(255) collate utf8_bin default NULL,
  `DATA_` longblob,
  `LOCK_OWNER_` varchar(255) collate utf8_bin default NULL,
  `LOCK_TIME_` timestamp NULL default NULL,
  `IS_PROCESSED_` tinyint(4) default '0',
  PRIMARY KEY  (`LOG_NR_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_evt_log` */

/*Table structure for table `act_ge_bytearray` */

DROP TABLE IF EXISTS `act_ge_bytearray`;

CREATE TABLE `act_ge_bytearray` (
  `ID_` varchar(64) collate utf8_bin NOT NULL default '',
  `REV_` int(11) default NULL,
  `NAME_` varchar(255) collate utf8_bin default NULL,
  `DEPLOYMENT_ID_` varchar(64) collate utf8_bin default NULL,
  `BYTES_` longblob,
  `GENERATED_` tinyint(4) default NULL,
  PRIMARY KEY  (`ID_`),
  KEY `ACT_FK_BYTEARR_DEPL` (`DEPLOYMENT_ID_`),
  CONSTRAINT `ACT_FK_BYTEARR_DEPL` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `act_re_deployment` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_ge_bytearray` */

/*Table structure for table `act_ge_property` */

DROP TABLE IF EXISTS `act_ge_property`;

CREATE TABLE `act_ge_property` (
  `NAME_` varchar(64) collate utf8_bin NOT NULL default '',
  `VALUE_` varchar(300) collate utf8_bin default NULL,
  `REV_` int(11) default NULL,
  PRIMARY KEY  (`NAME_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_ge_property` */

insert  into `act_ge_property`(`NAME_`,`VALUE_`,`REV_`) values ('next.dbid','557501',224),('schema.history','create(5.18.0.0)',1),('schema.version','5.18.0.0',1);

/*Table structure for table `act_hi_actinst` */

DROP TABLE IF EXISTS `act_hi_actinst`;

CREATE TABLE `act_hi_actinst` (
  `ID_` varchar(64) collate utf8_bin NOT NULL,
  `PROC_DEF_ID_` varchar(64) collate utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) collate utf8_bin NOT NULL,
  `EXECUTION_ID_` varchar(64) collate utf8_bin NOT NULL,
  `ACT_ID_` varchar(255) collate utf8_bin NOT NULL,
  `TASK_ID_` varchar(64) collate utf8_bin default NULL,
  `CALL_PROC_INST_ID_` varchar(64) collate utf8_bin default NULL,
  `ACT_NAME_` varchar(255) collate utf8_bin default NULL,
  `ACT_TYPE_` varchar(255) collate utf8_bin NOT NULL,
  `ASSIGNEE_` varchar(255) collate utf8_bin default NULL,
  `START_TIME_` datetime NOT NULL,
  `END_TIME_` datetime default NULL,
  `DURATION_` bigint(20) default NULL,
  `TENANT_ID_` varchar(255) collate utf8_bin default '',
  PRIMARY KEY  (`ID_`),
  KEY `ACT_IDX_HI_ACT_INST_START` (`START_TIME_`),
  KEY `ACT_IDX_HI_ACT_INST_END` (`END_TIME_`),
  KEY `ACT_IDX_HI_ACT_INST_PROCINST` (`PROC_INST_ID_`,`ACT_ID_`),
  KEY `ACT_IDX_HI_ACT_INST_EXEC` (`EXECUTION_ID_`,`ACT_ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_hi_actinst` */

/*Table structure for table `act_hi_attachment` */

DROP TABLE IF EXISTS `act_hi_attachment`;

CREATE TABLE `act_hi_attachment` (
  `ID_` varchar(64) collate utf8_bin NOT NULL,
  `REV_` int(11) default NULL,
  `USER_ID_` varchar(255) collate utf8_bin default NULL,
  `NAME_` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION_` varchar(4000) collate utf8_bin default NULL,
  `TYPE_` varchar(255) collate utf8_bin default NULL,
  `TASK_ID_` varchar(64) collate utf8_bin default NULL,
  `PROC_INST_ID_` varchar(64) collate utf8_bin default NULL,
  `URL_` varchar(4000) collate utf8_bin default NULL,
  `CONTENT_ID_` varchar(64) collate utf8_bin default NULL,
  `TIME_` datetime default NULL,
  PRIMARY KEY  (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_hi_attachment` */

/*Table structure for table `act_hi_comment` */

DROP TABLE IF EXISTS `act_hi_comment`;

CREATE TABLE `act_hi_comment` (
  `ID_` varchar(64) collate utf8_bin NOT NULL,
  `TYPE_` varchar(255) collate utf8_bin default NULL,
  `TIME_` datetime NOT NULL,
  `USER_ID_` varchar(255) collate utf8_bin default NULL,
  `TASK_ID_` varchar(64) collate utf8_bin default NULL,
  `PROC_INST_ID_` varchar(64) collate utf8_bin default NULL,
  `ACTION_` varchar(255) collate utf8_bin default NULL,
  `MESSAGE_` varchar(4000) collate utf8_bin default NULL,
  `FULL_MSG_` longblob,
  PRIMARY KEY  (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_hi_comment` */

/*Table structure for table `act_hi_detail` */

DROP TABLE IF EXISTS `act_hi_detail`;

CREATE TABLE `act_hi_detail` (
  `ID_` varchar(64) collate utf8_bin NOT NULL,
  `TYPE_` varchar(255) collate utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) collate utf8_bin default NULL,
  `EXECUTION_ID_` varchar(64) collate utf8_bin default NULL,
  `TASK_ID_` varchar(64) collate utf8_bin default NULL,
  `ACT_INST_ID_` varchar(64) collate utf8_bin default NULL,
  `NAME_` varchar(255) collate utf8_bin NOT NULL,
  `VAR_TYPE_` varchar(255) collate utf8_bin default NULL,
  `REV_` int(11) default NULL,
  `TIME_` datetime NOT NULL,
  `BYTEARRAY_ID_` varchar(64) collate utf8_bin default NULL,
  `DOUBLE_` double default NULL,
  `LONG_` bigint(20) default NULL,
  `TEXT_` varchar(4000) collate utf8_bin default NULL,
  `TEXT2_` varchar(4000) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID_`),
  KEY `ACT_IDX_HI_DETAIL_PROC_INST` (`PROC_INST_ID_`),
  KEY `ACT_IDX_HI_DETAIL_ACT_INST` (`ACT_INST_ID_`),
  KEY `ACT_IDX_HI_DETAIL_TIME` (`TIME_`),
  KEY `ACT_IDX_HI_DETAIL_NAME` (`NAME_`),
  KEY `ACT_IDX_HI_DETAIL_TASK_ID` (`TASK_ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_hi_detail` */

/*Table structure for table `act_hi_identitylink` */

DROP TABLE IF EXISTS `act_hi_identitylink`;

CREATE TABLE `act_hi_identitylink` (
  `ID_` varchar(64) collate utf8_bin NOT NULL default '',
  `GROUP_ID_` varchar(255) collate utf8_bin default NULL,
  `TYPE_` varchar(255) collate utf8_bin default NULL,
  `USER_ID_` varchar(255) collate utf8_bin default NULL,
  `TASK_ID_` varchar(64) collate utf8_bin default NULL,
  `PROC_INST_ID_` varchar(64) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID_`),
  KEY `ACT_IDX_HI_IDENT_LNK_USER` (`USER_ID_`),
  KEY `ACT_IDX_HI_IDENT_LNK_TASK` (`TASK_ID_`),
  KEY `ACT_IDX_HI_IDENT_LNK_PROCINST` (`PROC_INST_ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_hi_identitylink` */

/*Table structure for table `act_hi_procinst` */

DROP TABLE IF EXISTS `act_hi_procinst`;

CREATE TABLE `act_hi_procinst` (
  `ID_` varchar(64) collate utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) collate utf8_bin NOT NULL,
  `BUSINESS_KEY_` varchar(255) collate utf8_bin default NULL,
  `PROC_DEF_ID_` varchar(64) collate utf8_bin NOT NULL,
  `START_TIME_` datetime NOT NULL,
  `END_TIME_` datetime default NULL,
  `DURATION_` bigint(20) default NULL,
  `START_USER_ID_` varchar(255) collate utf8_bin default NULL,
  `START_ACT_ID_` varchar(255) collate utf8_bin default NULL,
  `END_ACT_ID_` varchar(255) collate utf8_bin default NULL,
  `SUPER_PROCESS_INSTANCE_ID_` varchar(64) collate utf8_bin default NULL,
  `DELETE_REASON_` varchar(4000) collate utf8_bin default NULL,
  `TENANT_ID_` varchar(255) collate utf8_bin default '',
  `NAME_` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID_`),
  UNIQUE KEY `PROC_INST_ID_` (`PROC_INST_ID_`),
  KEY `ACT_IDX_HI_PRO_INST_END` (`END_TIME_`),
  KEY `ACT_IDX_HI_PRO_I_BUSKEY` (`BUSINESS_KEY_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_hi_procinst` */

/*Table structure for table `act_hi_taskinst` */

DROP TABLE IF EXISTS `act_hi_taskinst`;

CREATE TABLE `act_hi_taskinst` (
  `ID_` varchar(64) collate utf8_bin NOT NULL,
  `PROC_DEF_ID_` varchar(64) collate utf8_bin default NULL,
  `TASK_DEF_KEY_` varchar(255) collate utf8_bin default NULL,
  `PROC_INST_ID_` varchar(64) collate utf8_bin default NULL,
  `EXECUTION_ID_` varchar(64) collate utf8_bin default NULL,
  `NAME_` varchar(255) collate utf8_bin default NULL,
  `PARENT_TASK_ID_` varchar(64) collate utf8_bin default NULL,
  `DESCRIPTION_` varchar(4000) collate utf8_bin default NULL,
  `OWNER_` varchar(255) collate utf8_bin default NULL,
  `ASSIGNEE_` varchar(255) collate utf8_bin default NULL,
  `START_TIME_` datetime NOT NULL,
  `CLAIM_TIME_` datetime default NULL,
  `END_TIME_` datetime default NULL,
  `DURATION_` bigint(20) default NULL,
  `DELETE_REASON_` varchar(4000) collate utf8_bin default NULL,
  `PRIORITY_` int(11) default NULL,
  `DUE_DATE_` datetime default NULL,
  `FORM_KEY_` varchar(255) collate utf8_bin default NULL,
  `CATEGORY_` varchar(255) collate utf8_bin default NULL,
  `TENANT_ID_` varchar(255) collate utf8_bin default '',
  PRIMARY KEY  (`ID_`),
  KEY `ACT_IDX_HI_TASK_INST_PROCINST` (`PROC_INST_ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_hi_taskinst` */

/*Table structure for table `act_hi_varinst` */

DROP TABLE IF EXISTS `act_hi_varinst`;

CREATE TABLE `act_hi_varinst` (
  `ID_` varchar(64) collate utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) collate utf8_bin default NULL,
  `EXECUTION_ID_` varchar(64) collate utf8_bin default NULL,
  `TASK_ID_` varchar(64) collate utf8_bin default NULL,
  `NAME_` varchar(255) collate utf8_bin NOT NULL,
  `VAR_TYPE_` varchar(100) collate utf8_bin default NULL,
  `REV_` int(11) default NULL,
  `BYTEARRAY_ID_` varchar(64) collate utf8_bin default NULL,
  `DOUBLE_` double default NULL,
  `LONG_` bigint(20) default NULL,
  `TEXT_` varchar(4000) collate utf8_bin default NULL,
  `TEXT2_` varchar(4000) collate utf8_bin default NULL,
  `CREATE_TIME_` datetime default NULL,
  `LAST_UPDATED_TIME_` datetime default NULL,
  PRIMARY KEY  (`ID_`),
  KEY `ACT_IDX_HI_PROCVAR_PROC_INST` (`PROC_INST_ID_`),
  KEY `ACT_IDX_HI_PROCVAR_NAME_TYPE` (`NAME_`,`VAR_TYPE_`),
  KEY `ACT_IDX_HI_PROCVAR_TASK_ID` (`TASK_ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_hi_varinst` */

/*Table structure for table `act_id_group` */

DROP TABLE IF EXISTS `act_id_group`;

CREATE TABLE `act_id_group` (
  `ID_` varchar(64) collate utf8_bin NOT NULL default '',
  `REV_` int(11) default NULL,
  `NAME_` varchar(255) collate utf8_bin default NULL,
  `TYPE_` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_id_group` */

/*Table structure for table `act_id_info` */

DROP TABLE IF EXISTS `act_id_info`;

CREATE TABLE `act_id_info` (
  `ID_` varchar(64) collate utf8_bin NOT NULL default '',
  `REV_` int(11) default NULL,
  `USER_ID_` varchar(64) collate utf8_bin default NULL,
  `TYPE_` varchar(64) collate utf8_bin default NULL,
  `KEY_` varchar(255) collate utf8_bin default NULL,
  `VALUE_` varchar(255) collate utf8_bin default NULL,
  `PASSWORD_` longblob,
  `PARENT_ID_` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_id_info` */

/*Table structure for table `act_id_membership` */

DROP TABLE IF EXISTS `act_id_membership`;

CREATE TABLE `act_id_membership` (
  `USER_ID_` varchar(64) collate utf8_bin NOT NULL default '',
  `GROUP_ID_` varchar(64) collate utf8_bin NOT NULL default '',
  PRIMARY KEY  (`USER_ID_`,`GROUP_ID_`),
  KEY `ACT_FK_MEMB_GROUP` (`GROUP_ID_`),
  CONSTRAINT `ACT_FK_MEMB_GROUP` FOREIGN KEY (`GROUP_ID_`) REFERENCES `act_id_group` (`ID_`),
  CONSTRAINT `ACT_FK_MEMB_USER` FOREIGN KEY (`USER_ID_`) REFERENCES `act_id_user` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_id_membership` */

/*Table structure for table `act_id_user` */

DROP TABLE IF EXISTS `act_id_user`;

CREATE TABLE `act_id_user` (
  `ID_` varchar(64) collate utf8_bin NOT NULL default '',
  `REV_` int(11) default NULL,
  `FIRST_` varchar(255) collate utf8_bin default NULL,
  `LAST_` varchar(255) collate utf8_bin default NULL,
  `EMAIL_` varchar(255) collate utf8_bin default NULL,
  `PWD_` varchar(255) collate utf8_bin default NULL,
  `PICTURE_ID_` varchar(64) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_id_user` */

/*Table structure for table `act_re_deployment` */

DROP TABLE IF EXISTS `act_re_deployment`;

CREATE TABLE `act_re_deployment` (
  `ID_` varchar(64) collate utf8_bin NOT NULL default '',
  `NAME_` varchar(255) collate utf8_bin default NULL,
  `CATEGORY_` varchar(255) collate utf8_bin default NULL,
  `TENANT_ID_` varchar(255) collate utf8_bin default '',
  `DEPLOY_TIME_` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_re_deployment` */

/*Table structure for table `act_re_model` */

DROP TABLE IF EXISTS `act_re_model`;

CREATE TABLE `act_re_model` (
  `ID_` varchar(64) collate utf8_bin NOT NULL,
  `REV_` int(11) default NULL,
  `NAME_` varchar(255) collate utf8_bin default NULL,
  `KEY_` varchar(255) collate utf8_bin default NULL,
  `CATEGORY_` varchar(255) collate utf8_bin default NULL,
  `CREATE_TIME_` timestamp NULL default NULL,
  `LAST_UPDATE_TIME_` timestamp NULL default NULL,
  `VERSION_` int(11) default NULL,
  `META_INFO_` varchar(4000) collate utf8_bin default NULL,
  `DEPLOYMENT_ID_` varchar(64) collate utf8_bin default NULL,
  `EDITOR_SOURCE_VALUE_ID_` varchar(64) collate utf8_bin default NULL,
  `EDITOR_SOURCE_EXTRA_VALUE_ID_` varchar(64) collate utf8_bin default NULL,
  `TENANT_ID_` varchar(255) collate utf8_bin default '',
  PRIMARY KEY  (`ID_`),
  KEY `ACT_FK_MODEL_SOURCE` (`EDITOR_SOURCE_VALUE_ID_`),
  KEY `ACT_FK_MODEL_SOURCE_EXTRA` (`EDITOR_SOURCE_EXTRA_VALUE_ID_`),
  KEY `ACT_FK_MODEL_DEPLOYMENT` (`DEPLOYMENT_ID_`),
  CONSTRAINT `ACT_FK_MODEL_DEPLOYMENT` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `act_re_deployment` (`ID_`),
  CONSTRAINT `ACT_FK_MODEL_SOURCE` FOREIGN KEY (`EDITOR_SOURCE_VALUE_ID_`) REFERENCES `act_ge_bytearray` (`ID_`),
  CONSTRAINT `ACT_FK_MODEL_SOURCE_EXTRA` FOREIGN KEY (`EDITOR_SOURCE_EXTRA_VALUE_ID_`) REFERENCES `act_ge_bytearray` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_re_model` */

/*Table structure for table `act_re_procdef` */

DROP TABLE IF EXISTS `act_re_procdef`;

CREATE TABLE `act_re_procdef` (
  `ID_` varchar(64) collate utf8_bin NOT NULL,
  `REV_` int(11) default NULL,
  `CATEGORY_` varchar(255) collate utf8_bin default NULL,
  `NAME_` varchar(255) collate utf8_bin default NULL,
  `KEY_` varchar(255) collate utf8_bin NOT NULL,
  `VERSION_` int(11) NOT NULL,
  `DEPLOYMENT_ID_` varchar(64) collate utf8_bin default NULL,
  `RESOURCE_NAME_` varchar(4000) collate utf8_bin default NULL,
  `DGRM_RESOURCE_NAME_` varchar(4000) collate utf8_bin default NULL,
  `DESCRIPTION_` varchar(4000) collate utf8_bin default NULL,
  `HAS_START_FORM_KEY_` tinyint(4) default NULL,
  `HAS_GRAPHICAL_NOTATION_` tinyint(4) default NULL,
  `SUSPENSION_STATE_` int(11) default NULL,
  `TENANT_ID_` varchar(255) collate utf8_bin default '',
  PRIMARY KEY  (`ID_`),
  UNIQUE KEY `ACT_UNIQ_PROCDEF` (`KEY_`,`VERSION_`,`TENANT_ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_re_procdef` */

/*Table structure for table `act_ru_event_subscr` */

DROP TABLE IF EXISTS `act_ru_event_subscr`;

CREATE TABLE `act_ru_event_subscr` (
  `ID_` varchar(64) collate utf8_bin NOT NULL,
  `REV_` int(11) default NULL,
  `EVENT_TYPE_` varchar(255) collate utf8_bin NOT NULL,
  `EVENT_NAME_` varchar(255) collate utf8_bin default NULL,
  `EXECUTION_ID_` varchar(64) collate utf8_bin default NULL,
  `PROC_INST_ID_` varchar(64) collate utf8_bin default NULL,
  `ACTIVITY_ID_` varchar(64) collate utf8_bin default NULL,
  `CONFIGURATION_` varchar(255) collate utf8_bin default NULL,
  `CREATED_` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `PROC_DEF_ID_` varchar(64) collate utf8_bin default NULL,
  `TENANT_ID_` varchar(255) collate utf8_bin default '',
  PRIMARY KEY  (`ID_`),
  KEY `ACT_IDX_EVENT_SUBSCR_CONFIG_` (`CONFIGURATION_`),
  KEY `ACT_FK_EVENT_EXEC` (`EXECUTION_ID_`),
  CONSTRAINT `ACT_FK_EVENT_EXEC` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_ru_event_subscr` */

/*Table structure for table `act_ru_execution` */

DROP TABLE IF EXISTS `act_ru_execution`;

CREATE TABLE `act_ru_execution` (
  `ID_` varchar(64) collate utf8_bin NOT NULL default '',
  `REV_` int(11) default NULL,
  `PROC_INST_ID_` varchar(64) collate utf8_bin default NULL,
  `BUSINESS_KEY_` varchar(255) collate utf8_bin default NULL,
  `PARENT_ID_` varchar(64) collate utf8_bin default NULL,
  `PROC_DEF_ID_` varchar(64) collate utf8_bin default NULL,
  `SUPER_EXEC_` varchar(64) collate utf8_bin default NULL,
  `ACT_ID_` varchar(255) collate utf8_bin default NULL,
  `IS_ACTIVE_` tinyint(4) default NULL,
  `IS_CONCURRENT_` tinyint(4) default NULL,
  `IS_SCOPE_` tinyint(4) default NULL,
  `IS_EVENT_SCOPE_` tinyint(4) default NULL,
  `SUSPENSION_STATE_` int(11) default NULL,
  `CACHED_ENT_STATE_` int(11) default NULL,
  `TENANT_ID_` varchar(255) collate utf8_bin default '',
  `NAME_` varchar(255) collate utf8_bin default NULL,
  `LOCK_TIME_` timestamp NULL default NULL,
  PRIMARY KEY  (`ID_`),
  KEY `ACT_IDX_EXEC_BUSKEY` (`BUSINESS_KEY_`),
  KEY `ACT_FK_EXE_PROCINST` (`PROC_INST_ID_`),
  KEY `ACT_FK_EXE_PARENT` (`PARENT_ID_`),
  KEY `ACT_FK_EXE_SUPER` (`SUPER_EXEC_`),
  KEY `ACT_FK_EXE_PROCDEF` (`PROC_DEF_ID_`),
  CONSTRAINT `ACT_FK_EXE_PARENT` FOREIGN KEY (`PARENT_ID_`) REFERENCES `act_ru_execution` (`ID_`),
  CONSTRAINT `ACT_FK_EXE_PROCDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`),
  CONSTRAINT `ACT_FK_EXE_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ACT_FK_EXE_SUPER` FOREIGN KEY (`SUPER_EXEC_`) REFERENCES `act_ru_execution` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_ru_execution` */

/*Table structure for table `act_ru_identitylink` */

DROP TABLE IF EXISTS `act_ru_identitylink`;

CREATE TABLE `act_ru_identitylink` (
  `ID_` varchar(64) collate utf8_bin NOT NULL default '',
  `REV_` int(11) default NULL,
  `GROUP_ID_` varchar(255) collate utf8_bin default NULL,
  `TYPE_` varchar(255) collate utf8_bin default NULL,
  `USER_ID_` varchar(255) collate utf8_bin default NULL,
  `TASK_ID_` varchar(64) collate utf8_bin default NULL,
  `PROC_INST_ID_` varchar(64) collate utf8_bin default NULL,
  `PROC_DEF_ID_` varchar(64) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID_`),
  KEY `ACT_IDX_IDENT_LNK_USER` (`USER_ID_`),
  KEY `ACT_IDX_IDENT_LNK_GROUP` (`GROUP_ID_`),
  KEY `ACT_IDX_ATHRZ_PROCEDEF` (`PROC_DEF_ID_`),
  KEY `ACT_FK_TSKASS_TASK` (`TASK_ID_`),
  KEY `ACT_FK_IDL_PROCINST` (`PROC_INST_ID_`),
  CONSTRAINT `ACT_FK_ATHRZ_PROCEDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`),
  CONSTRAINT `ACT_FK_IDL_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`),
  CONSTRAINT `ACT_FK_TSKASS_TASK` FOREIGN KEY (`TASK_ID_`) REFERENCES `act_ru_task` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_ru_identitylink` */

/*Table structure for table `act_ru_job` */

DROP TABLE IF EXISTS `act_ru_job`;

CREATE TABLE `act_ru_job` (
  `ID_` varchar(64) collate utf8_bin NOT NULL,
  `REV_` int(11) default NULL,
  `TYPE_` varchar(255) collate utf8_bin NOT NULL,
  `LOCK_EXP_TIME_` timestamp NULL default NULL,
  `LOCK_OWNER_` varchar(255) collate utf8_bin default NULL,
  `EXCLUSIVE_` tinyint(1) default NULL,
  `EXECUTION_ID_` varchar(64) collate utf8_bin default NULL,
  `PROCESS_INSTANCE_ID_` varchar(64) collate utf8_bin default NULL,
  `PROC_DEF_ID_` varchar(64) collate utf8_bin default NULL,
  `RETRIES_` int(11) default NULL,
  `EXCEPTION_STACK_ID_` varchar(64) collate utf8_bin default NULL,
  `EXCEPTION_MSG_` varchar(4000) collate utf8_bin default NULL,
  `DUEDATE_` timestamp NULL default NULL,
  `REPEAT_` varchar(255) collate utf8_bin default NULL,
  `HANDLER_TYPE_` varchar(255) collate utf8_bin default NULL,
  `HANDLER_CFG_` varchar(4000) collate utf8_bin default NULL,
  `TENANT_ID_` varchar(255) collate utf8_bin default '',
  PRIMARY KEY  (`ID_`),
  KEY `ACT_FK_JOB_EXCEPTION` (`EXCEPTION_STACK_ID_`),
  CONSTRAINT `ACT_FK_JOB_EXCEPTION` FOREIGN KEY (`EXCEPTION_STACK_ID_`) REFERENCES `act_ge_bytearray` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_ru_job` */

/*Table structure for table `act_ru_task` */

DROP TABLE IF EXISTS `act_ru_task`;

CREATE TABLE `act_ru_task` (
  `ID_` varchar(64) collate utf8_bin NOT NULL default '',
  `REV_` int(11) default NULL,
  `EXECUTION_ID_` varchar(64) collate utf8_bin default NULL,
  `PROC_INST_ID_` varchar(64) collate utf8_bin default NULL,
  `PROC_DEF_ID_` varchar(64) collate utf8_bin default NULL,
  `NAME_` varchar(255) collate utf8_bin default NULL,
  `PARENT_TASK_ID_` varchar(64) collate utf8_bin default NULL,
  `DESCRIPTION_` varchar(4000) collate utf8_bin default NULL,
  `TASK_DEF_KEY_` varchar(255) collate utf8_bin default NULL,
  `OWNER_` varchar(255) collate utf8_bin default NULL,
  `ASSIGNEE_` varchar(255) collate utf8_bin default NULL,
  `DELEGATION_` varchar(64) collate utf8_bin default NULL,
  `PRIORITY_` int(11) default NULL,
  `CREATE_TIME_` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `DUE_DATE_` datetime default NULL,
  `CATEGORY_` varchar(255) collate utf8_bin default NULL,
  `SUSPENSION_STATE_` int(11) default NULL,
  `TENANT_ID_` varchar(255) collate utf8_bin default '',
  `FORM_KEY_` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID_`),
  KEY `ACT_IDX_TASK_CREATE` (`CREATE_TIME_`),
  KEY `ACT_FK_TASK_EXE` (`EXECUTION_ID_`),
  KEY `ACT_FK_TASK_PROCINST` (`PROC_INST_ID_`),
  KEY `ACT_FK_TASK_PROCDEF` (`PROC_DEF_ID_`),
  CONSTRAINT `ACT_FK_TASK_EXE` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`),
  CONSTRAINT `ACT_FK_TASK_PROCDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`),
  CONSTRAINT `ACT_FK_TASK_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_ru_task` */

/*Table structure for table `act_ru_variable` */

DROP TABLE IF EXISTS `act_ru_variable`;

CREATE TABLE `act_ru_variable` (
  `ID_` varchar(64) collate utf8_bin NOT NULL,
  `REV_` int(11) default NULL,
  `TYPE_` varchar(255) collate utf8_bin NOT NULL,
  `NAME_` varchar(255) collate utf8_bin NOT NULL,
  `EXECUTION_ID_` varchar(64) collate utf8_bin default NULL,
  `PROC_INST_ID_` varchar(64) collate utf8_bin default NULL,
  `TASK_ID_` varchar(64) collate utf8_bin default NULL,
  `BYTEARRAY_ID_` varchar(64) collate utf8_bin default NULL,
  `DOUBLE_` double default NULL,
  `LONG_` bigint(20) default NULL,
  `TEXT_` varchar(4000) collate utf8_bin default NULL,
  `TEXT2_` varchar(4000) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID_`),
  KEY `ACT_IDX_VARIABLE_TASK_ID` (`TASK_ID_`),
  KEY `ACT_FK_VAR_EXE` (`EXECUTION_ID_`),
  KEY `ACT_FK_VAR_PROCINST` (`PROC_INST_ID_`),
  KEY `ACT_FK_VAR_BYTEARRAY` (`BYTEARRAY_ID_`),
  CONSTRAINT `ACT_FK_VAR_BYTEARRAY` FOREIGN KEY (`BYTEARRAY_ID_`) REFERENCES `act_ge_bytearray` (`ID_`),
  CONSTRAINT `ACT_FK_VAR_EXE` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`),
  CONSTRAINT `ACT_FK_VAR_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_ru_variable` */

/*Table structure for table `r_code` */

DROP TABLE IF EXISTS `r_code`;

CREATE TABLE `r_code` (
  `id` int(11) NOT NULL auto_increment COMMENT '编号ID',
  `rolecode` char(12) NOT NULL COMMENT '权限码',
  `remark` varchar(128) NOT NULL COMMENT '权限备注',
  `sort` int(11) default NULL COMMENT '排序',
  `enable` char(1) NOT NULL default 'Y' COMMENT '是否有效(Y/N)',
  `createtime` timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` int(11) NOT NULL COMMENT '创建人ID',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `uk_code` USING BTREE (`rolecode`),
  KEY `index_code_enable` (`enable`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限功能表';

/*Data for the table `r_code` */

insert  into `r_code`(`id`,`rolecode`,`remark`,`sort`,`enable`,`createtime`,`creater`) values (1,'000001','下载统计报表',1,'Y','2017-11-17 21:57:49',0),(2,'000002','删除用户',2,'Y','2017-11-17 21:57:49',0),(3,'000003','增加菜单',3,'Y','2017-11-17 21:57:49',0);

/*Table structure for table `r_menu` */

DROP TABLE IF EXISTS `r_menu`;

CREATE TABLE `r_menu` (
  `id` int(11) NOT NULL auto_increment COMMENT '编号ID',
  `menu` varchar(32) NOT NULL COMMENT '导航名称',
  `url` varchar(256) NOT NULL COMMENT '地址',
  `remark` varchar(128) NOT NULL COMMENT '备注',
  `sort` int(11) default NULL COMMENT '菜单排序',
  `enable` char(1) NOT NULL default 'Y' COMMENT '是否有效(Y/N)',
  `createtime` timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` int(11) NOT NULL COMMENT '创建人ID',
  `updatetime` timestamp NOT NULL default '0000-00-00 00:00:00' COMMENT '更新的时间',
  `modifier` int(11) default NULL COMMENT '编辑人',
  `parentid` int(11) default NULL COMMENT '父级菜单ID，外键，对应id',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `uq_menu` USING BTREE (`menu`),
  KEY `index_rolemenu_parentid` (`parentid`),
  KEY `index_rolemenu_enable` (`enable`),
  KEY `index_menu_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='导航菜单表';

/*Data for the table `r_menu` */

insert  into `r_menu`(`id`,`menu`,`url`,`remark`,`sort`,`enable`,`createtime`,`creater`,`updatetime`,`modifier`,`parentid`) values (1,'用户管理','test','',1,'Y','2017-11-18 02:46:13',0,'0000-00-00 00:00:00',NULL,NULL),(2,'下拉配置','test','',2,'Y','2017-11-18 02:46:14',0,'0000-00-00 00:00:00',NULL,NULL),(3,'系统参数','test','',3,'Y','2017-11-18 02:46:14',0,'0000-00-00 00:00:00',NULL,NULL),(4,'接口参数','test','',4,'Y','2017-11-18 02:46:15',0,'0000-00-00 00:00:00',NULL,NULL),(5,'权限管理','test','',5,'Y','2017-11-18 02:46:21',0,'0000-00-00 00:00:00',NULL,NULL),(6,'用户查询','test2','',1,'Y','2017-11-18 02:50:05',0,'0000-00-00 00:00:00',NULL,1),(7,'用户配置','test','',2,'Y','2017-11-18 02:50:06',0,'0000-00-00 00:00:00',NULL,1),(8,'下拉主参','test4','',1,'Y','2017-11-18 02:50:10',0,'0000-00-00 00:00:00',NULL,2),(9,'下拉参数','config/configurations','',2,'Y','2017-11-18 02:50:12',0,'0000-00-00 00:00:00',NULL,2),(10,'系统常量','test','',1,'Y','2017-11-18 02:50:14',0,'0000-00-00 00:00:00',NULL,3),(11,'订单接口','test','',1,'Y','2017-11-18 02:50:16',0,'0000-00-00 00:00:00',NULL,4),(12,'权限查询','test','',1,'Y','2017-11-18 02:50:19',0,'0000-00-00 00:00:00',NULL,5),(13,'test','test3','',1,'Y','2017-11-18 03:32:45',0,'0000-00-00 00:00:00',NULL,12),(14,'权限编辑','test','',2,'Y','2017-11-18 06:06:03',0,'0000-00-00 00:00:00',NULL,5);

/*Table structure for table `r_role` */

DROP TABLE IF EXISTS `r_role`;

CREATE TABLE `r_role` (
  `id` int(11) NOT NULL auto_increment COMMENT '编号',
  `rolename` varchar(32) NOT NULL COMMENT '角色名称',
  `remark` varchar(128) NOT NULL COMMENT '备注',
  `enable` char(1) NOT NULL default 'Y' COMMENT '是否有效(Y/N)',
  `groupcode` char(12) NOT NULL COMMENT '权限所在分组，用于工作流，比如请假，资源审批等，每个流对应的角色',
  `createtime` timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间',
  `createuserid` int(11) NOT NULL COMMENT '创建人ID',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `uq_rolename` USING BTREE (`rolename`),
  KEY `index_role_groupcode` (`groupcode`),
  KEY `index_role_rolename` (`rolename`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

/*Data for the table `r_role` */

insert  into `r_role`(`id`,`rolename`,`remark`,`enable`,`groupcode`,`createtime`,`createuserid`) values (1,'系统管理员','','Y','000001','2017-11-14 22:49:29',1),(2,'导航管理员','','Y','000001','2017-11-14 22:50:30',1),(3,'用户管理员','','Y','000001','2017-11-14 22:50:30',1);

/*Table structure for table `r_rolecode` */

DROP TABLE IF EXISTS `r_rolecode`;

CREATE TABLE `r_rolecode` (
  `id` int(11) NOT NULL auto_increment COMMENT '编号',
  `rid` int(11) NOT NULL COMMENT '角色ID',
  `cid` int(11) NOT NULL COMMENT '功能ID',
  PRIMARY KEY  (`id`),
  KEY `index_rolecode_rid` (`rid`),
  KEY `index_rolecode_cid` (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色功能关联表';

/*Data for the table `r_rolecode` */

insert  into `r_rolecode`(`id`,`rid`,`cid`) values (1,1,1),(2,1,2),(3,2,3);

/*Table structure for table `r_rolemenu` */

DROP TABLE IF EXISTS `r_rolemenu`;

CREATE TABLE `r_rolemenu` (
  `id` int(11) NOT NULL auto_increment COMMENT '编号',
  `rid` int(11) NOT NULL COMMENT '角色ID',
  `mid` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY  (`id`),
  KEY `index_rolemenu_rid` (`rid`),
  KEY `index_rolemenu_mid` (`mid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色菜单关联表';

/*Data for the table `r_rolemenu` */

/*Table structure for table `r_user` */

DROP TABLE IF EXISTS `r_user`;

CREATE TABLE `r_user` (
  `id` int(11) NOT NULL auto_increment COMMENT '编号',
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `pwd` varchar(32) character set utf8 collate utf8_bin NOT NULL COMMENT '密码',
  `departmentid` int(11) NOT NULL COMMENT '部门ID',
  `enable` char(1) NOT NULL default 'Y' COMMENT '是否有效(Y/N)',
  `endtime` timestamp NOT NULL default '0000-00-00 00:00:00' COMMENT '离职时间，与enable结合使用，N的时候设置时间',
  `updatetime` timestamp NOT NULL default '0000-00-00 00:00:00' COMMENT '操作时间，更新的时间',
  `createtime` timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` int(11) NOT NULL COMMENT '创建人ID',
  `modifier` int(11) default NULL COMMENT '编辑人',
  `isblacklist` char(1) NOT NULL default 'N' COMMENT '是否黑名单，N是白名单',
  `lastupdatepwdtime` timestamp NOT NULL default '0000-00-00 00:00:00' COMMENT '上一次修改密码时间',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `uq_username` USING BTREE (`username`),
  KEY `index_user_username` (`username`),
  KEY `index_user_departmentid` (`departmentid`),
  KEY `index_user_enable` (`enable`),
  KEY `index_user_endtime` (`endtime`),
  KEY `index_user_createtime` (`createtime`),
  KEY `index_user_isblacklist` (`isblacklist`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户账户表';

/*Data for the table `r_user` */

insert  into `r_user`(`id`,`username`,`pwd`,`departmentid`,`enable`,`endtime`,`updatetime`,`createtime`,`creater`,`modifier`,`isblacklist`,`lastupdatepwdtime`) values (1,'admin','admin',1,'Y','0000-00-00 00:00:00','0000-00-00 00:00:00','2017-11-14 22:32:48',1,NULL,'N','0000-00-00 00:00:00'),(2,'kyoxue','kyoxue',2,'Y','0000-00-00 00:00:00','0000-00-00 00:00:00','2017-11-14 22:46:16',1,NULL,'N','0000-00-00 00:00:00');

/*Table structure for table `r_userdetail` */

DROP TABLE IF EXISTS `r_userdetail`;

CREATE TABLE `r_userdetail` (
  `id` int(11) NOT NULL auto_increment COMMENT '编号',
  `name` varchar(64) NOT NULL COMMENT '姓名或昵称',
  `email` varchar(64) default NULL COMMENT '邮箱',
  `qq` varchar(16) default NULL COMMENT 'QQ',
  `telephone` varchar(16) default NULL COMMENT '电话',
  `mobile` varchar(16) NOT NULL COMMENT '手机',
  `age` int(4) default NULL COMMENT '年龄',
  `sex` char(1) NOT NULL COMMENT '性别 1男0女',
  `birth` varchar(16) default NULL COMMENT '生日',
  `icon` varchar(64) default NULL COMMENT '头像路径',
  `remark` varchar(256) default NULL COMMENT '备注',
  `updatetime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '操作时间，创建或更新的时间',
  `modifier` int(11) default NULL COMMENT '编辑人',
  `uid` int(11) NOT NULL COMMENT '用户账户外键',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `uq_uid` USING BTREE (`uid`),
  KEY `index_userdetail_uid` (`uid`),
  KEY `index_userdetail_name` (`name`),
  KEY `index_userdetail_sex` (`sex`),
  KEY `index_userdetail_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

/*Data for the table `r_userdetail` */

/*Table structure for table `r_userrole` */

DROP TABLE IF EXISTS `r_userrole`;

CREATE TABLE `r_userrole` (
  `id` int(11) NOT NULL auto_increment COMMENT '编号',
  `uid` int(11) NOT NULL COMMENT '人员ID',
  `rid` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY  (`id`),
  KEY `index_userrole_uid` (`uid`),
  KEY `index_userrole_rid` (`rid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='人员角色关联表';

/*Data for the table `r_userrole` */

insert  into `r_userrole`(`id`,`uid`,`rid`) values (1,1,1),(2,2,2);

/*Table structure for table `t_config` */

DROP TABLE IF EXISTS `t_config`;

CREATE TABLE `t_config` (
  `id` int(11) NOT NULL auto_increment COMMENT '编号ID',
  `ckey` varchar(64) default NULL,
  `cvalue` varchar(256) default NULL COMMENT '配置信息',
  `ctype` int(11) NOT NULL COMMENT '配置类别',
  `enable` char(1) NOT NULL default 'Y' COMMENT '是否有效(Y/N)',
  `createtime` timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` int(11) NOT NULL COMMENT '创建人ID',
  `updatetime` timestamp NOT NULL default '0000-00-00 00:00:00' COMMENT '修改时间',
  `modifier` int(11) default NULL COMMENT '修改人ID',
  `desc` varchar(256) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `uq_ckey` USING BTREE (`ckey`),
  KEY `index_config_ckey` (`ckey`),
  KEY `index_config_ctype` (`ctype`),
  KEY `index_config_createtime` (`createtime`),
  KEY `index_config_creater` (`creater`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统参数表';

/*Data for the table `t_config` */

insert  into `t_config`(`id`,`ckey`,`cvalue`,`ctype`,`enable`,`createtime`,`creater`,`updatetime`,`modifier`,`desc`) values (1,'notice_min_delay','30',1,'Y','2017-11-19 16:49:20',1,'0000-00-00 00:00:00',NULL,NULL),(2,'notice_reflush_delay','60',1,'Y','2017-11-19 16:49:20',1,'0000-00-00 00:00:00',NULL,NULL),(3,'notice_time_before','1',1,'Y','2017-11-19 16:49:20',1,'0000-00-00 00:00:00',NULL,NULL),(4,'notice_immediately','Y',1,'Y','2017-11-19 19:29:40',1,'0000-00-00 00:00:00',NULL,NULL),(5,'notice_show','N',1,'Y','2017-11-19 20:44:25',1,'0000-00-00 00:00:00',NULL,NULL);

/*Table structure for table `t_configtype` */

DROP TABLE IF EXISTS `t_configtype`;

CREATE TABLE `t_configtype` (
  `id` int(11) NOT NULL auto_increment COMMENT '编号ID',
  `ctypename` varchar(64) default NULL,
  `enable` char(1) NOT NULL default 'Y' COMMENT '是否有效(Y/N)',
  `createtime` timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` int(11) NOT NULL COMMENT '创建人ID',
  `updatetime` timestamp NOT NULL default '0000-00-00 00:00:00' COMMENT '修改时间',
  `modifier` int(11) default NULL COMMENT '修改人ID',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `uq_ctypename` USING BTREE (`ctypename`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='参数类别表';

/*Data for the table `t_configtype` */

insert  into `t_configtype`(`id`,`ctypename`,`enable`,`createtime`,`creater`,`updatetime`,`modifier`) values (1,'系统通知配置','Y','2017-11-19 16:37:02',1,'0000-00-00 00:00:00',NULL);

/*Table structure for table `t_department` */

DROP TABLE IF EXISTS `t_department`;

CREATE TABLE `t_department` (
  `id` int(11) NOT NULL auto_increment COMMENT '编号',
  `deptname` varchar(64) NOT NULL COMMENT '部门名称',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `uq_deptname` USING BTREE (`deptname`),
  KEY `index_department_deptname` (`deptname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门表';

/*Data for the table `t_department` */

insert  into `t_department`(`id`,`deptname`) values (2,'用户管理'),(1,'系统管理');

/*Table structure for table `t_log` */

DROP TABLE IF EXISTS `t_log`;

CREATE TABLE `t_log` (
  `id` int(11) NOT NULL auto_increment COMMENT '编号ID',
  `logtype` int(11) NOT NULL COMMENT '日志操作类别',
  `logcontent` varchar(1000) NOT NULL default 'Y' COMMENT '是否有效(Y/N)',
  `createtime` timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间',
  `uid` int(11) default NULL COMMENT '操作人ID',
  `backfeild_a` varchar(64) default NULL COMMENT '备份字段a',
  `backfeild_b` varchar(64) default NULL COMMENT '备份字段b',
  `backfeild_c` varchar(64) default NULL COMMENT '备份字段c',
  `backfeild_d` varchar(64) default NULL COMMENT '备份字段d',
  PRIMARY KEY  (`id`),
  KEY `index_log_logtype` (`logtype`),
  KEY `index_log_uid` (`uid`),
  KEY `index_log_createtime` (`createtime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统日志表';

/*Data for the table `t_log` */

/*Table structure for table `t_logtype` */

DROP TABLE IF EXISTS `t_logtype`;

CREATE TABLE `t_logtype` (
  `id` int(11) NOT NULL auto_increment COMMENT '编号ID',
  `logtypename` varchar(64) NOT NULL COMMENT '日志操作类别名称',
  `enable` char(1) NOT NULL default 'Y' COMMENT '是否有效(Y/N)',
  `createtime` timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` int(11) NOT NULL COMMENT '创建人ID',
  `updatetime` timestamp NOT NULL default '0000-00-00 00:00:00' COMMENT '修改时间',
  `modifier` int(11) default NULL COMMENT '修改人ID',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `uq_logtypename` USING BTREE (`logtypename`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日志操作类别表';

/*Data for the table `t_logtype` */

/*Table structure for table `t_notice` */

DROP TABLE IF EXISTS `t_notice`;

CREATE TABLE `t_notice` (
  `id` int(11) NOT NULL auto_increment COMMENT '编号ID',
  `title` varchar(40) NOT NULL COMMENT '通知标题',
  `content` varchar(500) NOT NULL default '暂无通知...' COMMENT '通知内容',
  `enable` char(1) NOT NULL default 'Y' COMMENT '是否有效(Y/N)',
  `createtime` timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` int(11) NOT NULL COMMENT '创建人ID',
  PRIMARY KEY  (`id`),
  KEY `index_notice_title` (`title`),
  KEY `index_notice_createtime` (`createtime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='通知表';

/*Data for the table `t_notice` */

insert  into `t_notice`(`id`,`title`,`content`,`enable`,`createtime`,`creater`) values (1,'关于系统维护','系统将于2017-11-20进行维护，请注意备份！','Y','2017-11-19 03:43:55',1),(2,'关于系统维护','系统将于2017-11-20切换数据源，请在用旧数据的同学，注意更换！','Y','2017-11-19 03:43:55',1),(3,'关于国庆安排','10-01到10-08按照国家规定放假，祝愿各位同学国庆快乐！','Y','2017-11-19 03:43:55',1),(4,'关于请假安排','双十一期间禁止请假，如有特殊情况请提前到人事部申请说明！','Y','2017-11-19 03:49:19',1),(5,'关于食堂卫生','请不要在冰箱里放入带有异味的食品，包括鱼类带有腥味的食品，谢谢合作！','Y','2017-11-19 15:22:30',1),(6,'关于食堂卫生','8:30分过后，请还在吃早餐的同学自觉上班，谢谢合作！','Y','2017-11-19 15:30:18',1),(7,'关于12月份旅游','请参加旅游的同学尽快把消息发给前台助理！','Y','2017-11-19 15:48:30',1),(8,'关于12月份旅游','请参加旅游的同学尽快把消息发给前台助理！','Y','2017-11-19 15:51:25',1),(9,'关于12月份旅游','请参加旅游的同学尽快把消息发给前台助理！','Y','2017-11-19 15:52:14',1),(10,'关于12月份旅游','请参加旅游的同学尽快把消息发给前台助理！','Y','2017-11-19 15:52:35',1),(11,'关于12月份旅游','请参加旅游的同学尽快把消息发给前台助理！','Y','2017-11-19 19:14:59',1),(12,'xxxxx','yyyyy','Y','2017-12-02 16:16:22',1);

/*Table structure for table `t_select` */

DROP TABLE IF EXISTS `t_select`;

CREATE TABLE `t_select` (
  `id` int(11) NOT NULL auto_increment COMMENT '编号ID',
  `sdesc` varchar(128) NOT NULL COMMENT '下拉描述',
  `scode` varchar(64) NOT NULL COMMENT '下拉参数',
  `enable` char(1) NOT NULL default 'Y' COMMENT '是否有效(Y/N)',
  `createtime` timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` int(11) NOT NULL COMMENT '创建人ID',
  `updatetime` timestamp NOT NULL default '0000-00-00 00:00:00' COMMENT '修改时间',
  `modifier` int(11) default NULL COMMENT '修改人ID',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `uq_scode` USING BTREE (`scode`),
  KEY `index_select_scode` (`scode`),
  KEY `index_select_sdesc` (`sdesc`),
  KEY `index_select_creater` (`creater`),
  KEY `index_select_modifier` (`modifier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='下拉表';

/*Data for the table `t_select` */

insert  into `t_select`(`id`,`sdesc`,`scode`,`enable`,`createtime`,`creater`,`updatetime`,`modifier`) values (1,'性别','SEX','Y','2017-11-27 23:48:16',1,'0000-00-00 00:00:00',NULL);

/*Table structure for table `t_selectoption` */

DROP TABLE IF EXISTS `t_selectoption`;

CREATE TABLE `t_selectoption` (
  `id` int(11) NOT NULL auto_increment COMMENT '编号ID',
  `otext` varchar(64) NOT NULL COMMENT '选项显示文本',
  `ovalue` varchar(128) NOT NULL COMMENT '选项值',
  `scode` varchar(64) NOT NULL COMMENT '下拉参数',
  `enable` char(1) NOT NULL default 'Y' COMMENT '是否有效(Y/N)',
  `createtime` timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` int(11) NOT NULL COMMENT '创建人ID',
  `updatetime` timestamp NOT NULL default '0000-00-00 00:00:00' COMMENT '修改时间',
  `modifier` int(11) default NULL COMMENT '修改人ID',
  PRIMARY KEY  (`id`),
  KEY `index_selectoption_ovalue` (`ovalue`),
  KEY `index_selectoption_otext` (`otext`),
  KEY `index_selectoption_scode` (`scode`),
  KEY `index_selectoption_creater` (`creater`),
  KEY `index_selectoption_modifier` (`modifier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='下拉选项表';

/*Data for the table `t_selectoption` */

insert  into `t_selectoption`(`id`,`otext`,`ovalue`,`scode`,`enable`,`createtime`,`creater`,`updatetime`,`modifier`) values (1,'男','1','SEX','Y','2017-11-27 23:49:43',1,'0000-00-00 00:00:00',NULL),(2,'女','2','SEX','Y','2017-11-27 23:49:43',1,'0000-00-00 00:00:00',NULL);

/* Procedure structure for procedure `act_int` */

/*!50003 DROP PROCEDURE IF EXISTS  `act_int` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `act_int`()
BEGIN
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
    END */$$
DELIMITER ;

/* Function  structure for function  `SP_FUNC_MENU` */

/*!50003 DROP FUNCTION IF EXISTS `SP_FUNC_MENU` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` FUNCTION `SP_FUNC_MENU`(I_ID int(11)) RETURNS mediumtext CHARSET utf8
BEGIN
	DECLARE V_IDS MediumText;
	DECLARE O_IDS MediumText;
	IF (I_ID IS NULL) THEN
		RETURN '';
	END IF;
	SET group_concat_max_len = 2000000;
	SELECT id INTO V_IDS FROM r_menu WHERE id = I_ID;
	WHILE V_IDS IS NOT NULL DO
		IF (O_IDS IS NULL) THEN
			SET O_IDS = V_IDS;
		ELSE
			SET O_IDS = CONCAT(O_IDS,',',V_IDS);
		END IF;
		SELECT GROUP_CONCAT(id) INTO V_IDS FROM r_menu force index(index_rolemenu_parentid) WHERE FIND_IN_SET(parentid,V_IDS);
	END WHILE;
	RETURN O_IDS;
END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
