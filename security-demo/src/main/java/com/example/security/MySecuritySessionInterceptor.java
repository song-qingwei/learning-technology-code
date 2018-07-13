/**
 * 
 */
package com.example.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.stereotype.Component;

/**
 * @author SongQingWei
 * @date 2018年7月4日 下午1:47:18
 */
@Component
public class MySecuritySessionInterceptor implements InvalidSessionStrategy {

	private static final Logger log = LoggerFactory.getLogger(MySecuritySessionInterceptor.class);
	
	@Override
	public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.info("session失效调用InvalidSessionStrategy");
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        // 针对ajax请求处理
        if (request.getHeader("x-requested-with") != null) {
            log.info("basePath:{}"+basePath);
            response.addHeader("sessionstatus", "timeOut");
            response.addHeader("loginPath", basePath+"/");
            // 解决中文乱码
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Cache-Control", "no-cache");
            PrintWriter out = response.getWriter();
            out.print("");
            out.flush();
            out.close();
        } else {
            log.info(basePath+"/sessionTimeout");
            request.getRequestDispatcher(basePath+"/sessionTimeout").forward(request,response);

        }
	}

}
