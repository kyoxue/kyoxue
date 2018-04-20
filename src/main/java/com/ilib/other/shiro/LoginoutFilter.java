package com.ilib.other.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class LoginoutFilter extends LogoutFilter {
	Logger log = LoggerFactory.getLogger(LoginoutFilter.class);
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        //覆盖注销方法意义是，在退出前做些事情。
    	//在这里执行退出系统前需要清空的数据
    	//dosomething....
    	//以下开始退出
    	Subject subject = getSubject(request, response);
        String redirectUrl = getRedirectUrl(request, response, subject);
        try {
            subject.logout();
        } catch (SessionException ise) {
           log.error("退出登录异常",ise);
        }
        issueRedirect(request, response, redirectUrl);
      //返回false表示不执行后续的过滤器，直接返回跳转到登录页面
        return false;
    }
}
