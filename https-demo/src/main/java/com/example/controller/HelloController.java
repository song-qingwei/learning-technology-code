package com.example.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author SongQingWei
 * @date 2018年08月03 09:51
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping("/{name}")
    public String sayHello(@PathVariable(value = "name") String name) {
        return "Hello " + name;
    }
}
