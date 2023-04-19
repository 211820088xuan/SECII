package org.fffd.l23o6.controller;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fffd.l23o6.pojo.vo.Response;
import org.fffd.l23o6.pojo.vo.user.LoginRequest;
import org.fffd.l23o6.pojo.vo.user.RegisterRequest;
import org.fffd.l23o6.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("session")
    public Response<?> login(@Valid @RequestBody LoginRequest request) {
        // Throws BizException if auth failed.
        userService.login(request.getUsername(), request.getPassword());

        StpUtil.login(request.getUsername());
        return Response.success();
    }

    @PostMapping("user")
    public Response<?> register(@Valid @RequestBody RegisterRequest request) {
        // Throws BizException if register failed.
        userService.register(request.getUsername(), request.getPassword());

        return Response.success();
    }
}