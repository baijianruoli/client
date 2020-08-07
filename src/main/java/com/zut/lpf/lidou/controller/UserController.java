package com.zut.lpf.lidou.controller;

import com.zut.lpf.lidou.config.Reference;
import com.zut.lpf.lidou.service.UserService;
import com.zut.lpf.lidou.util.BaseResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserController {

    @Reference
    private UserService userService;
    @RequestMapping("/add")
    public BaseResponse add(String msg, int id)
    {
        BaseResponse add = userService.add(msg, id);
        return add;
    }
}
