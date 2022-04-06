package com.gd.base.enums;


public enum ResultCodeEnums implements ErrorCode{
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    requestmethod_error( 1, "HTTP请求方式异常"),
    param_error_json( 2, "前端提交的JSON格式有误"),
    param_error(3, "参数异常！"),;

    private long code;
    private String message;

    private ResultCodeEnums(long code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}


