package io.zut.lpf.lidou.service;


import io.zut.lpf.lidou.util.BaseResponse;

public interface MessageService {
    public BaseResponse send(String msg);
    public BaseResponse get(String msg);
}
