package com.example.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;

/**
 * @author SongQingWei
 * @date 2018年05月07 16:35
 */
@Controller
public class KungfuController {

	private final String PREFIX = "pages/";

	@Resource
	private AuthenticationManager authenticationManager;

	/**
	 * 欢迎页
	 * @return
	 */
	@GetMapping("/")
	public String index() {
		return "welcome";
	}

	/**
	 * 登陆页
	 * @return
	 */
	@GetMapping("/login")
	public String loginPage() {
		return PREFIX+"login";
	}


	/**
	 * level1页面映射
	 * @param path
	 * @return
	 */
	@GetMapping("/level1/{path}")
	public String level1(@PathVariable("path")String path) {
		return PREFIX+"level1/"+path;
	}

	/**
	 * level2页面映射
	 * @param path
	 * @return
	 */
	@GetMapping("/level2/{path}")
	public String level2(@PathVariable("path")String path) {
		return PREFIX+"level2/"+path;
	}

	/**
	 * level3页面映射
	 * @param path
	 * @return
	 */
	@GetMapping("/level3/{path}")
	public String level3(@PathVariable("path")String path) {
		return PREFIX+"level3/"+path;
	}


	@GetMapping("/403")
	public String error() {
		return "403";
	}
}
