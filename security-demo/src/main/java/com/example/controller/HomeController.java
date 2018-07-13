package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author SongQingWei
 * @date 2018年6月26日 上午11:10:31
 */
@Controller
public class HomeController {

	@RequestMapping(value = "/")
	public String index() {
		return "index";
	}
	
	@RequestMapping(value = "/login")
	public String login() {
		return "login";
	}
	
	@RequestMapping(value = "/logout")
	public String logout() {
		return "login";
	}
	
	@RequestMapping(value = "/403")
	public String error() {
		return "403";
	}

	@RequestMapping("/add")
	public String add() {
		return "add";
	}

	@RequestMapping("/update")
	public String update() {
		return "update";
	}

	@RequestMapping("/delete")
	public String delete() {
		return "delete";
	}
}
