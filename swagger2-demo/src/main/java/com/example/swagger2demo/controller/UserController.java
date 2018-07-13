package com.example.swagger2demo.controller;

import com.example.swagger2demo.model.User;
import com.example.swagger2demo.util.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;

/**
 * @author SongQingWei
 * @date 2018年07月13 09:59
 */
@Slf4j
@RestController
@Api("swaggerDemoController相关的api")
public class UserController {

    /**
     * 创建线程安全的Map
     */
    private static Map<Integer, User> users = Collections.synchronizedMap(new HashMap<>());

    /**
     * 根据ID查询用户
     * @param id id
     * @return json
     */
    @ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
    @GetMapping(value = "/user/{id}")
    public ResponseEntity<JsonResult> getUserById (@PathVariable(value = "id") Integer id){
        log.info("根据ID查询用户, id={}", id);
        JsonResult r = new JsonResult();
        try {
            User user = users.get(id);
            r.setResult(user);
            r.setStatus("ok");
        } catch (Exception e) {
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    /**
     * 查询用户列表
     * @return json
     */
    @ApiOperation(value="获取用户列表", notes="获取用户列表")
    @GetMapping(value = "/users")
    public ResponseEntity<JsonResult> getUserList (){
        log.info("查询用户列表");
        JsonResult r = new JsonResult();
        try {
            List<User> userList = new ArrayList<>(users.values());
            r.setResult(userList);
            r.setStatus("ok");
        } catch (Exception e) {
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    /**
     * 添加用户
     * @param user user
     * @return json
     */
    @ApiOperation(value="创建用户", notes="根据User对象创建用户")
    @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    @PostMapping(value = "/user")
    public ResponseEntity<JsonResult> add (@RequestBody User user){
        log.info("添加用户, user={}", user);
        JsonResult r = new JsonResult();
        try {
            users.put(user.getId(), user);
            r.setResult(user.getId());
            r.setStatus("ok");
        } catch (Exception e) {
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("error");

            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    /**
     * 根据id删除用户
     * @param id id
     * @return json
     */
    @ApiOperation(value="删除用户", notes="根据url的id来指定删除用户")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<JsonResult> delete (@PathVariable(value = "id") Integer id){
        log.info("根据id删除用户, id={}", id);
        JsonResult r = new JsonResult();
        try {
            users.remove(id);
            r.setResult(id);
            r.setStatus("ok");
        } catch (Exception e) {
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    /**
     * 根据id修改用户信息
     * @param user user
     * @return json
     */
    @ApiOperation(value="更新信息", notes="根据url的id来指定更新用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long",paramType = "path"),
            @ApiImplicitParam(name = "user", value = "用户实体user", required = true, dataType = "User")
    })
    @PutMapping(value = "/user/{id}")
    public ResponseEntity<JsonResult> update (@PathVariable("id") Integer id, @RequestBody User user){
        log.info("根据id修改用户信息, id={}, user={}", id, user);
        JsonResult r = new JsonResult();
        try {
            User u = users.get(id);
            u.setName(user.getName());
            u.setAge(user.getAge());
            users.put(id, u);
            r.setResult(u);
            r.setStatus("ok");
        } catch (Exception e) {
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("error");

            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    /**
     * //@ApiIgnore: 使用该注解忽略这个API
     * @return String
     */
    @ApiIgnore
    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    public String  jsonTest() {
        return " hi you!";
    }
}
