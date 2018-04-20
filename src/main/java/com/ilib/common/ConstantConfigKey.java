package com.ilib.common;
/**
 * 专门放所有t_config的ckey值
 *
 */
public interface ConstantConfigKey {
	/**
	 * 通知窗口多少秒后缩小状态
	 */
	static final String CONFIG_KEY_NOTICE_MIN_DELAY="notice_min_delay";
	/**
	 * 多少秒轮训一次最新的通知
	 */
	static final String CONFIG_KEY_NOTICE_REFLUSH_DELAY="notice_reflush_delay";
	/**
	 * 最新通知取多少小时以内发布的
	 */
	static final String CONFIG_KEY_NOTICE_TIME_BEFORE="notice_time_before";
	/**
	 * 是否初次进入页面立刻显示通知 Y/N
	 */
	static final String CONFIG_KEY_NOTICE_IMMEDIATELY="notice_immediately";
}
