/**
 * 
 */
package com.example.security;

import com.example.model.User;
import com.example.service.IUserService;
import com.example.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * @author SongQingWei
 * @date 2018年6月27日 上午9:36:46
 */
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private static final Logger log = LoggerFactory.getLogger(MyAuthenticationSuccessHandler.class);
	
	private String defaultRedirectUrl;
	
	public MyAuthenticationSuccessHandler(String defaultRedirectUrl) {
        this.defaultRedirectUrl = defaultRedirectUrl;
    }
	
	@Resource
    private IUserService userService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal != null && principal instanceof UserDetails) {
			response.setContentType("application/json;charset=utf-8");
			
			HttpSession session = request.getSession();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.findByUserName(userDetails.getUsername());
            if (user != null) {
                log.info("登陆成功后，在HttpSession中保存当前登录用户信息");
                session.setAttribute(Constants.CURRENT_USER, user);
                log.info("修改当前用户最近登录时间");
                user.setLastLoginTime(new Date());
                userService.updateSysUserLastLoginTime(user);
            }
			
			PrintWriter out = response.getWriter();
			out.write("{\"status\":\"ok\",\"message\":\"登录成功\"}");
			out.flush();
			out.close();
		}
	}
}
