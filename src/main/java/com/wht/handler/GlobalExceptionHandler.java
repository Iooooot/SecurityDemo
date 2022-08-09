package com.wht.handler;

import com.wht.entity.APIException;
import com.wht.entity.ResponseEntityDemo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(APIException.class)
    public ResponseEntityDemo APIExceptionHandler(APIException e) {
        //打印异常信息
        log.error("出现了异常！ {}",e);
        return ResponseEntityDemo.failed(e.getCode(),e.getMessage());
    }

    /***
     * 所有异常的处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntityDemo exceptionHandler(Exception e){
        //打印异常信息
        log.error("出现了异常！ {}",e);
        //从异常对象中获取提示信息封装返回
        return ResponseEntityDemo.failed(e.getMessage());
    }
}
