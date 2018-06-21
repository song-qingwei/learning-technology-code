package com.example.config;

import com.example.security.LoginAuthenticationSuccessHandler;
import com.example.security.MySecurityInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.annotation.Resource;

/**
 * @author SongQingWei
 * @date 2018年06月06 10:36
 */
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String KEY = "SongQingWei.com";

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private MySecurityInterceptor securityInterceptor;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new LoginAuthenticationSuccessHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 定制授权规则
        http.authorizeRequests()
                // 不拦截的请求
                .antMatchers("/", "/login", "/getImageCheckingCode").permitAll()
                // 其他地址均需要验证权限
                .anyRequest().authenticated()
                .and()
                // 基于 Form 表单登录验证
                .formLogin().loginPage("/login").usernameParameter("userName").passwordParameter("passWord")
                .defaultSuccessUrl("/").failureUrl("/login?error")
                //.successHandler(successHandler()).
                .and()
                .logout().logoutSuccessUrl("/login?logout")
                // 启用 remember me
                .and()
                .rememberMe().rememberMeParameter("rememberMe").key(KEY)
                // 处理异常，拒绝访问就重定向到 403
                .and()
                .exceptionHandling().accessDeniedPage("/403")
                .and()
                .csrf().disable();
        http.addFilterBefore(securityInterceptor, FilterSecurityInterceptor.class);
    }
}
