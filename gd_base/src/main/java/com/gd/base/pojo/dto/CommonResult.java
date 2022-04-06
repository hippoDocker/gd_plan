package com.gd.base.pojo.dto;

import com.gd.base.enums.ErrorCode;
import com.gd.base.enums.ResultCodeEnums;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @Auther: tangxl
 * @Date: 2021年11月30日14:07:39
 * @Description: 统一返回结果
 */
@Data
@ApiModel(value = "返回结果通用实体类")
@Component
public class CommonResult<T>  implements Serializable {
    private boolean success;
    private long code;
    private String message;
    private T data;

    public CommonResult() {
    }

    /**
     * @Description:返回消息编码和数据
     * @param code
     * @param message
     * @param data
     */
    public CommonResult(boolean success,long code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * @Description: 只返回编码和消息
     * @param code
     * @param message
     */
    public CommonResult(boolean success, long code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    /**
     * 成功返回结果
     *
     * @param
     */
    public static <T> CommonResult<T> success() {
        return new CommonResult<T>(true,ResultCodeEnums.SUCCESS.getCode(), ResultCodeEnums.SUCCESS.getMessage());
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(true,ResultCodeEnums.SUCCESS.getCode(), ResultCodeEnums.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param message 提示信息
     */
    public static <T> CommonResult<T> success(String message) {
        return new CommonResult<T>(true,ResultCodeEnums.SUCCESS.getCode(),message);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<T>(true,ResultCodeEnums.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     */
    public static <T> CommonResult<T> failed(ErrorCode errorCode) {
        return new CommonResult<T>(false,errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 失败返回结果
     *
     * @param Code 状态码
     * @param message  消息
     * @return
     */
    public static <T> CommonResult<T> failed(long Code,String message) {
        return new CommonResult<T>(false,Code, message,null);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<T>(false,ResultCodeEnums.FAILED.getCode(), message, null);
    }

    /**
     * 失败返回结果
     */
    public static <T> CommonResult<T> failed() {
        return failed(ResultCodeEnums.FAILED);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> CommonResult<T> validateFailed() {
        return failed(ResultCodeEnums.VALIDATE_FAILED);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> CommonResult<T> validateFailed(String message) {
        return new CommonResult<T>(false,ResultCodeEnums.VALIDATE_FAILED.getCode(), message, null);
    }

    /**
     * 未登录返回结果
     */
    public static <T> CommonResult<T> unLogin(T data) {
        return new CommonResult<T>(false,ResultCodeEnums.UNAUTHORIZED.getCode(), ResultCodeEnums.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * 未授权返回结果
     */
    public static <T> CommonResult<T> forbidden(T data) {
        return new CommonResult<T>(false,ResultCodeEnums.FORBIDDEN.getCode(), ResultCodeEnums.FORBIDDEN.getMessage(), data);
    }

    public long getCode() {
        return code;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
