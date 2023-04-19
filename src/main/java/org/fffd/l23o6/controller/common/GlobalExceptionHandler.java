package org.fffd.l23o6.controller.common;

import cn.dev33.satoken.exception.NotLoginException;
import lombok.extern.slf4j.Slf4j;
import org.fffd.l23o6.exception.BizException;
import org.fffd.l23o6.exception.ErrorType;
import org.fffd.l23o6.pojo.vo.Response;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * global catch
     */

    @ExceptionHandler(BizException.class)
    public Response<?> handleBaseException(BizException e) {
        log.error("Biz Exception", e);
        return Response.error(e);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, HttpMediaTypeException.class})
    public Response<?> handleBadRequest(Exception e) {
        log.error("Bad Request Exception", e);
        return Response.error(ErrorType.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Invalid Argument", e);
        return Response.error(ErrorType.ILLEGAL_ARGUMENTS, e.getAllErrors().get(0).getDefaultMessage());
//        return Response.error(ResponseType.ILLEGAL_ARGUMENTS, e.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("/")));
    }

    @ExceptionHandler(NotLoginException.class)
    public Response<?> handleAuthorizeException(NotLoginException e) {
        log.error("Not Login Exception", e);
        return Response.error(ErrorType.NOT_LOGIN, e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response<?> handleMethodNotSupportException(HttpRequestMethodNotSupportedException e) {
        log.error("Method Not Support Exception", e);
        return Response.error(ErrorType.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Response<?> handleDefaultException(Exception e) {
        log.error("Global catch", e);
        return Response.error(ErrorType.UNKNOWN_ERROR);
    }
}
