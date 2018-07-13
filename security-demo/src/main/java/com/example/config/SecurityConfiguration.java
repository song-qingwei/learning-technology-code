package com.example.config;

import com.example.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.session.SessionManagementFilter;

import javax.annotation.Resource;

/**
 * @author SongQingWei
 * @date 2018年6月26日 上午10:43:18
 */
@EnableWebSecurity
// 启用方法安全设置
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String KEY = "SongQingWei.com";

	@Resource
	private UserDetailsService userDetailsService;
	
	@Resource
    private MyFilterSecurityInterceptor securityInterceptor;
	
	@Resource
    private MySecuritySessionInterceptor sessionInterceptor;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		// 设置密码加密方式
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
	
	@Bean
	public AuthenticationSuccessHandler successHandler() {
		return new MyAuthenticationSuccessHandler("/");
	}
	
	@Bean
	public AuthenticationFailureHandler failureHandler() {
		return  new MyAuthenticationFailureHandler();
	}
	
	@Bean
    public SessionManagementFilter expiredSessionFilter() {
        SessionManagementFilter sessionManagementFilter = new SessionManagementFilter(new HttpSessionSecurityContextRepository());
        sessionManagementFilter.setInvalidSessionStrategy(sessionInterceptor);
        return sessionManagementFilter;
    }

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// 防止拦截css、js、image文件
		web.ignoring().antMatchers("/css/**", "/js/**", "/images/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 在认证用户名之前认证验证码，如果验证码错误，将不执行用户名和密码的认证
		http.addFilterBefore(new KaptchaAuthenticationFilter("/login", "/login?error"), UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(securityInterceptor, FilterSecurityInterceptor.class);
		http.addFilterAfter(expiredSessionFilter(),SessionManagementFilter.class);
		http.csrf().disable()
				.authorizeRequests()
				.antMatchers("/", "/css/**", "/js/**", "/images/**", "/layer/**", "/fonts/**", "/login", "/getKaptchaImage")
				.permitAll() // 都可以访问
				// 都可以访问
				.antMatchers("/h2-console/**").permitAll()
				// 需要相应的角色才能访问
				// .antMatchers("/admin/**").hasRole("ADMIN")
				// 需要相应的角色才能访问
				// .antMatchers("/console/**").hasAnyRole("ADMIN", "USER")
				// 其他请求都需要验证权限
				.anyRequest().authenticated()
				// 基于 Form 表单登录验证
				.and().formLogin()
				// 自定义登录界面
				.loginPage("/login")
				.successHandler(successHandler())
				.failureHandler(failureHandler())
				// 启用 remember-me
				.and().rememberMe().key(KEY)
				// 处理异常，拒绝访问就重定向到 403 页面
				.and().exceptionHandling().accessDeniedPage("/403");
		http.logout().logoutUrl("/logout").logoutSuccessUrl("/login");
		// 禁用 H2 控制台的 CSRF 防护
		http.csrf().ignoringAntMatchers("/h2-console/**");
		// 禁用 H2 控制台的 CSRF 防护
		http.csrf().ignoringAntMatchers("/ajax/**");
		http.csrf().ignoringAntMatchers("/upload");
		// 允许来自同一来源的H2 控制台的请求
		http.headers().frameOptions().sameOrigin();
	}

}
