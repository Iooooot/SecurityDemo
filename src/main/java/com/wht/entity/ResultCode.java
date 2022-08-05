package com.wht.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "操作成功"),

    FAILED(1001, "操作失败"),

    VALIDATE_FAILED(1002, "参数校验失败"),

    UNAUTHORIZED(401,"尚未登录，请先登录"),

    FORBIDDEN(403,"权限不足"),

    ERROR(5000, "未知错误");

    private int code; //状态码
    private String msg; //响应信息

}
