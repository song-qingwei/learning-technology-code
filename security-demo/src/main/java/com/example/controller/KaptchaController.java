package com.example.controller;

import java.awt.image.BufferedImage;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.util.Constants;
import com.google.code.kaptcha.Producer;

/**
 * @author SongQingWei
 * @date 2018年6月26日 下午1:47:50
 */
@Controller
public class KaptchaController {

	private static final Logger log = LoggerFactory.getLogger(KaptchaController.class);
	
	@Resource
	private Producer captchaProducer;

	@GetMapping("/getKaptchaImage")
	public void getKaptchaImage(HttpServletResponse response, HttpSession session) throws Exception {
		response.setDateHeader("Expires", 0);
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		// return a jpeg
		response.setContentType("image/jpeg");
		// create the text for the image
		String capText = captchaProducer.createText();
		// store the text in the session
		// request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
		// 验证码存入 session
		session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
		log.info("生成验证码:{}", capText);
		// create the image with the text
		BufferedImage bi = captchaProducer.createImage(capText);
		ServletOutputStream out = response.getOutputStream();
		// write the data out
		ImageIO.write(bi, "jpg", out);
		try {
			out.flush();
		} finally {
			out.close();
		}
	}
}
