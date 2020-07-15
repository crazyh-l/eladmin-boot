package me.yanghao.exception.handler;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import me.yanghao.exception.BadRequestException;
import me.yanghao.exception.EntityNotFoundException;
import me.yanghao.utils.ThrowableUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityExistsException;
import java.util.Objects;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author YangHao
 * @ClassName: GlobalExceptionHandler
 * @Description: TODO
 * @date 2020/7/15 16:47
 * @Version V1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @Description 处理所有不可知的异常
     * @Date 2020/7/15 16:53
     * @param
     * @return org.springframework.http.ResponseEntity<me.yanghao.exception.handler.ApiError>
     */
    public ResponseEntity<ApiError> handlerException(Throwable e) {
        //打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return buildResponseEntity(ApiError.error(e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> badCredenialsException(BadCredentialsException e) {
        //打印堆栈信息
        String message = "坏的凭证".equals(e.getMessage()) ? "用户名或密码不正确" : e.getMessage();
        log.error(ThrowableUtil.getStackTrace(e));
        return buildResponseEntity(ApiError.error(message));
    }

    /**
     * @Description 处理自定义异常
     * @Date 2020/7/15 17:09
     * @param  
     * @return org.springframework.http.ResponseEntity<me.yanghao.exception.handler.ApiError>
     */
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ApiError> badRequestException(BadRequestException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return buildResponseEntity(ApiError.error(e.getStatus(),e.getMessage()));
    }

    /**
     * @Description 处理Entity
     * @Date 2020/7/15 17:15
     * @param
     * @return org.springframework.http.ResponseEntity<me.yanghao.exception.handler.ApiError>
     */
    @ExceptionHandler(value = EntityExistsException.class)
    public ResponseEntity<ApiError> entityExistException(EntityExistsException e) {
        //打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return buildResponseEntity(ApiError.error(e.getMessage()));
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ApiError> entityNotFoundException(EntityNotFoundException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return buildResponseEntity(ApiError.error(NOT_FOUND.value(),e.getMessage()));
    }

    /**
     * 处理所有接口数据验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        String[] str = Objects.requireNonNull(e.getBindingResult().getAllErrors().get(0).getCodes())[1].split("\\.");
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String msg = "不能为空";
        if(msg.equals(message)){
            message = str[1] + ":" + message;
        }
        return buildResponseEntity(ApiError.error(message));
    }

    /**
     * @Description 统一返回
     * @Date 2020/7/15 16:50
     * @param apiError
     * @return org.springframework.http.ResponseEntity<me.yanghao.exception.handler.ApiError>
     */
    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }

}
