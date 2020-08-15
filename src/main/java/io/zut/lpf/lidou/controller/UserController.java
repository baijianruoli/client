package io.zut.lpf.lidou.controller;

import io.zut.lpf.lidou.config.Reference;
import io.zut.lpf.lidou.service.UserService;
import io.zut.lpf.lidou.util.BaseResponse;
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
