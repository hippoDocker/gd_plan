package com.gd.gd_service.config;

import lombok.Data;

@Data
public class TalkException extends Exception {
    //错误信息
    private String msg;

    public TalkException(String msg) {
        this.msg = msg;
    }


}
