package com.ilib.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @describe 验证码错误异常
 * @author KYOXUE
 * @datetime2017-11-17上午9:46:46
 */
public class IncorrectCaptchaException extends AuthenticationException {
	private static final long serialVersionUID = -9063447801585281265L;

	public IncorrectCaptchaException() {
		super();
	}

	public IncorrectCaptchaException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectCaptchaException(String message) {
		super(message);
	}

	public IncorrectCaptchaException(Throwable cause) {
		super(cause);
	}
}
