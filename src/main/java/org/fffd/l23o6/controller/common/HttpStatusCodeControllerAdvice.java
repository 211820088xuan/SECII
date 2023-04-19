package org.fffd.l23o6.controller.common;


import org.fffd.l23o6.pojo.vo.Response;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


@ControllerAdvice
public class HttpStatusCodeControllerAdvice implements ResponseBodyAdvice<Response<?>> {
    /**
     * Used to set http status code
     */

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return returnType.getParameterType().isAssignableFrom(Response.class);
    }

    @Override
    public Response<?> beforeBodyWrite(Response<?> body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body != null) {
            response.setStatusCode(HttpStatus.valueOf(body.getHttpCode()));
        }
        return body;
    }
}
