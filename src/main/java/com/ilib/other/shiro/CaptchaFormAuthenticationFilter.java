package com.ilib.other.shiro;

import static com.ilib.common.Constant.*;
import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ilib.common.Constant;
import com.ilib.dao.UserDao;
import com.ilib.exception.IncorrectCaptchaException;
import com.ilib.model.User;
import com.ilib.utils.DateTimeUtils;
import com.ilib.utils.IpAddress;
import com.ilib.utils.StringUtil;

/**
 * @describe权限模块，重写fliter 验证码验证
 * @author xiangcong
 * @datetime2013-12-3上午9:45:17
 */
public class CaptchaFormAuthenticationFilter extends FormAuthenticationFilter {
	private static final Logger logger = LoggerFactory.getLogger(CaptchaFormAuthenticationFilter.class);
	public static final String DEFAULT_CAPTCHA_PARAM = "captcha";

	private String captchaParam = DEFAULT_CAPTCHA_PARAM;

	@Autowired
	private UserDao userDao;

	public String getCaptchaParam() {
		return captchaParam;
	}

	public void setCaptchaParam(String captchaParam) {
		this.captchaParam = captchaParam;
	}

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}

	// 创建 Token
	protected CaptchaUsernamePasswordToken createToken(ServletRequest request, ServletResponse response) {
		String username = getUsername(request);
		String password = getPassword(request);
		String captcha = getCaptcha(request);
		boolean rememberMe = isRememberMe(request);
		String host = getHost(request);
		return new CaptchaUsernamePasswordToken(username, password, rememberMe, host, captcha);
	}

	// 验证码校验
	protected boolean doCaptchaValidate(HttpServletRequest request,CaptchaUsernamePasswordToken token) {
		String captcha = (String) request.getSession().getAttribute(
				com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		if (captcha != null && !captcha.equalsIgnoreCase(token.getCaptcha())) {
			//throw new IncorrectCaptchaException(LOGIN_ERROR_CAPTCHA_UNRIGHT);
			request.setAttribute(LOGIN_ERROR_KEY, LOGIN_ERROR_CAPTCHA_UNRIGHT);
			return false;
		}
		return true;
	}

	// 认证
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
		CaptchaUsernamePasswordToken token = createToken(request, response);
			token.setHost(request.getRemoteHost());
			//token.setRememberMe(true);
			boolean checkCode = doCaptchaValidate((HttpServletRequest) request, token);
			if (!checkCode) {
				return onLoginFailure(token, new AuthenticationException(), request, response);
			}
			Subject subject = getSubject(request, response);
			String ip = IpAddress.getIpAddr((HttpServletRequest) request);
			String url = getRomAddr((HttpServletRequest) request);
			String username = token.getUsername();
			User user = userDao.getUserByUsername(username);
			if (null == user) {
				request.setAttribute(LOGIN_ERROR_KEY, LOGIN_ERROR_UNKNOW_USER);
				return onLoginFailure(token, new AuthenticationException(), request, response);
			} else {
				String enable = StringUtil.get().getVal(user.getEnable());
				Date endtime = user.getEndtime();
				if (N.equals(enable)) {
					String disabledTime = DateTimeUtils.formatDate(endtime, DATE_TIME_FORMAT);
					String cn = "失效时间：".concat(disabledTime);
					request.setAttribute(LOGIN_ERROR_KEY, LOGIN_ERROR_DISABLED.concat(cn));
					return onLoginFailure(token, new AuthenticationException(), request, response);
				}
				String isblacklist = StringUtil.get().getVal(user.getIsblacklist());
				if (Y.equals(isblacklist)) {
					request.setAttribute(LOGIN_ERROR_KEY, LOGIN_ERROR_BLACKLIST);
					return onLoginFailure(token, new AuthenticationException(), request, response);
				}
			}
			logger.info("有用户开始登录，IP：{},用户：{},URL：{}", ip, username, url);
			// 这里拦截用户
			boolean loginSuccess = false;
			AuthenticationException ex = new AuthenticationException();
			try {
				subject.login(token);
				if (subject.isAuthenticated()) {
					subject.getSession().setAttribute(LOGIN_SESSION_USER_KEY, user);
					subject.getSession().setTimeout(60*60*1000l);
					loginSuccess = true;
					logger.info("用户：{}，登录成功！登录时间：{}",username,DateTimeUtils.getDateTime(new Date()));
				}
			} 
			//自定义REALM的时候，可以自己去写SQL来判断，然后抛出下面的异常！			
//			  catch (IncorrectCredentialsException e) {  
//				request.setAttribute(LOGIN_ERROR_KEY, LOGIN_ERROR_PASSWORD_UNRIGHT);
//		    } catch (ExcessiveAttemptsException e) {  
//		        //msg = "登录失败次数过多";  
//		    	request.setAttribute(LOGIN_ERROR_KEY, LOGIN_ERROR_TIME_OVER);
//		    	ex=e;
//		    } catch (LockedAccountException e) {  
//		        //msg = "帐号已被锁定. The account for username " + token.getPrincipal() + " was locked.";  
//		    	request.setAttribute(LOGIN_ERROR_KEY, LOGIN_ERROR_LOCKED);  
//		    	ex=e;
//		    } catch (DisabledAccountException e) {  
//		        //msg = "帐号已被禁用. The account for username " + token.getPrincipal() + " was disabled.";  
//		    	request.setAttribute(LOGIN_ERROR_KEY, LOGIN_ERROR_USER_DISABLED);  
//		    	ex=e;
//		    } catch (ExpiredCredentialsException e) {  
//		        //msg = "帐号已过期. the account for username " + token.getPrincipal() + "  was expired.";  
//		    	request.setAttribute(LOGIN_ERROR_KEY, LOGIN_ERROR_INVALID);  
//		    	ex=e;
//		    } catch (UnknownAccountException e) {  
//		        //msg = "帐号不存在. There is no user with username of " + token.getPrincipal();  
//		    	request.setAttribute(LOGIN_ERROR_KEY, LOGIN_ERROR_UNEXISTS);  
//		    	ex=e;
//		    } catch (UnauthorizedException e) {  
//		        //msg = "您没有得到相应的授权！" + e.getMessage();  
//		    	request.setAttribute(LOGIN_ERROR_KEY, LOGIN_ERROR_UNRIGHT);  
//		    } 
			catch (AuthenticationException e) {
				request.setAttribute(LOGIN_ERROR_KEY, LOGIN_ERROR_CONFIG_EXCEPTION);
				ex=e;
			}
			if (loginSuccess) {
				return this.onLoginSuccess(token, subject, request, response);
			}else{
				return onLoginFailure(token, ex, request, response);
			}
		
	}

	public String getRomAddr(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();
		return url.toString();
	}
	private String indexPage;
	public String getIndexPage() {
		return indexPage;
	}

	public void setIndexPage(String indexPage) {
		this.indexPage = indexPage;
	}

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,ServletResponse response) throws Exception {
		 SavedRequest savedRequest = WebUtils.getSavedRequest(request);
		 Session session = subject.getSession();
        if (savedRequest != null) {
            session.removeAttribute(WebUtils.SAVED_REQUEST_KEY);
        }
//		WebUtils.getAndClearSavedRequest(request);
//		WebUtils.issueRedirect(request, response, indexPage, null, false);
		WebUtils.redirectToSavedRequest(request, response, indexPage);
		return false;
	}
	
	
}
