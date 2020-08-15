package io.zut.lpf.lidou.service;


import io.zut.lpf.lidou.util.BaseResponse;

public interface UserService {
    public BaseResponse add(String msg, int id);
    public BaseResponse delete(String msg);
}
