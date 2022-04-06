package com.gd.gd_service.config;

import com.gd.base.enums.ResultCodeEnums;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.sys.SysTalkDTO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auther: tangxl
 * @Date: 2022年2月22日17:37:31
 * @Description: 全局异常处理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = HibernateException.class)
    @ResponseBody
    public CommonResult exceptionHandler(HibernateException e){
        log.error("ERROR", "Error found: ", e);
        System.out.println("全局异常捕获>>>:"+e);
        return CommonResult.failed("数据处理异常，请联系管理员！");
    }

    /**
     * 自定义异常处理--智能聊天异常
     */
    @ExceptionHandler(TalkException.class)
    @ResponseBody
    public CommonResult handleException(TalkException e) {
        SysTalkDTO sysTalkDTO = new SysTalkDTO(1,e.getMsg());
        return CommonResult.success(sysTalkDTO);
    }

    @ExceptionHandler(value =RuntimeException.class)
    @ResponseBody
    public CommonResult runtimeExceptionHandler(RuntimeException e){
        System.out.println("全局异常捕获>>>:"+e);
        log.error("ERROR", "Error found: ", e);
        return CommonResult.failed(e.getMessage());
    }

    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public CommonResult exceptionHandler(Exception e){
        log.error("ERROR", "Error found: ", e);
        //json入参解析错误
        if (e instanceof HttpMessageNotReadableException) {
            log.info("前端入参异常:{}", e.getMessage());
            e.printStackTrace();
            return CommonResult.failed(ResultCodeEnums.param_error_json);
        }

        //POST、GET使用错误造成的异常自动处理
        if (e instanceof HttpRequestMethodNotSupportedException) {
            log.info("HTTP请求方式异常:{}", e.getMessage());
            e.printStackTrace();
            return CommonResult.failed(ResultCodeEnums.requestmethod_error);
        }
        //POST请求参数异常处理
        if (e instanceof MethodArgumentNotValidException) {
            log.info("调用方异常:{}", e.getMessage());
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            for (FieldError error : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
                log.info("参数异常:" + error.getField() + error.getDefaultMessage());
                return CommonResult.failed(ResultCodeEnums.param_error.getCode(), error.getDefaultMessage());
            }
            return CommonResult.failed();
        }
        //GET请求参数异常处理
        if (e instanceof MissingServletRequestParameterException) {
            log.info("调用方异常:{}", e.getMessage());
            return CommonResult.failed(ResultCodeEnums.param_error.getCode(),
                    "参数异常:" + ((MissingServletRequestParameterException) e).getParameterName() + "不能为空！");
        }
        e.printStackTrace();
        return CommonResult.failed();
    }
}
