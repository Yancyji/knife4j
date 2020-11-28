package com.lee.knife4j.controller;

import com.lee.knife4j.model.User;
import com.lee.knife4j.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("user")
@Api(tags = {"用户管理"}, description = "用户管理")
@RestController
public class UserController {

    @Autowired
    private UserService usersService;

    @GetMapping("findAll")
    @ApiOperation(value = "查询所有用户")
    public List<User> list() {
        return usersService.findAll();
    }

    @PostMapping("save")
    @ApiOperation(value = "保存用户")
    public User save(User user) throws InterruptedException {
        return usersService.save(user);
    }

    @PostMapping("findById")
    @ApiOperation(value = "根据id查询客户")
    public User findById(Long id) throws InterruptedException {
        return usersService.findById(id);
    }

    @PostMapping("deleteById")
    @ApiOperation(value = "删除用户")
    public void deleteById(Long id) throws InterruptedException {
        usersService.deleteById(id);
    }
}