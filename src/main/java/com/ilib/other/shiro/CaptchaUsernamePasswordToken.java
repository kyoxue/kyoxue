package com.ilib.other.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @describe 用户信息参数重新封装
 * @author KYOXUE
 * @datetime 2017-11-17 22:20:15
 */
public class CaptchaUsernamePasswordToken extends UsernamePasswordToken {
	private static final long serialVersionUID = 4560440260899490595L;
	private String captcha;

	public CaptchaUsernamePasswordToken(String username, String password, boolean rememberMe, String host,
			String captcha) {
		super(username, password, rememberMe, host);
		this.captcha = captcha;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
}
