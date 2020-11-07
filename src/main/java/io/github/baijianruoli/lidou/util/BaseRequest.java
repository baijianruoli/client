package io.github.baijianruoli.lidou.util;

import lombok.Data;

@Data
public class BaseRequest {
    //类名
    private String className;
    //方法名
    private String methodName;
    //参数
    private Object[] parameters;
    //参数类型
    private Class<?>[] parameTypes;

    public BaseRequest(String className) {
        this.className = className;
    }

    public BaseRequest(String className, String methodName, Object[] parameters, Class<?>[] parameTypes) {
        this.className = className;
        this.methodName = methodName;
        this.parameters = parameters;
        this.parameTypes = parameTypes;
    }
}
