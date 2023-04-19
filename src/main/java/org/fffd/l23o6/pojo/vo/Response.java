package org.fffd.l23o6.pojo.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.fffd.l23o6.exception.BizException;
import org.fffd.l23o6.exception.ErrorType;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Response<T> {
    @Schema(description = "响应码", required = true)
    private int code;
    @Schema(description = "响应消息", required = true)
    private String msg;
    @Schema(description = "响应数据")
    private T data;

    @Hidden
    @JsonIgnore
    // used to define http status code
    private int httpCode;

    public static <T> Response<T> success() {
        return success(null);
    }

    public static <T> Response<T> success(T data) {
        return success(data, 200);
    }

    public static <T> Response<T> success(int httpCode) {
        return success(null, httpCode);
    }

    public static <T> Response<T> success(T data, int httpCode) {
        return new Response<>(0, "success", data, httpCode);
    }
    public static <T> Response<T> error(ErrorType type) {
        return new Response<>(type.getCode(), type.getMessage(), null, type.getHttpCode());
    }

    public static <T> Response<T> error(ErrorType type, String msg) {
        return new Response<>(type.getCode(), msg, null, type.getHttpCode());
    }

    public static <T> Response<T> error(BizException exception) {
        return new Response<>(exception.getCode(), exception.getMessage(), null, exception.getHttpCode());
    }

}