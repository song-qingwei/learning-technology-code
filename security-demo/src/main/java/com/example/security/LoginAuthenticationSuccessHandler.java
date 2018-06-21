package com.example.security;

import com.example.entity.SysUser;
import com.example.service.IUserService;
import com.example.util.JsonUtil;
import com.example.util.ResultVO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 验证成功页面, 根据url跳转
 * @author SongQingWei
 * @date 2018年06月09 15:23
 */
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private String defaultUrl;

    @Resource
    private IUserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        SysUser user = userService.getUserByUserName(userDetails.getUsername());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        ResultVO<Object> resultVO;
        if (user != null) {
            if (user.getUserStatus().equals(0)) {
                resultVO = ResultVO.createBySuccessMessage("登录成功");
            } else {
                resultVO = ResultVO.createByErrorMessage("用户被禁用");
            }
        } else {
            resultVO = ResultVO.createByErrorMessage("账号或密码错误");
        }
        out.write(JsonUtil.toString(resultVO));
    }

    public String getDefaultUrl() {
        return defaultUrl;
    }

    public void setDefaultUrl(String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }
}
