package org.fffd.l23o6;



import io.github.lyc8503.spring.starter.incantation.pojo.CommonResponse;
import jakarta.transaction.Transactional;
import org.fffd.l23o6.controller.UserController;
import org.fffd.l23o6.pojo.vo.user.LoginRequest;
import org.fffd.l23o6.pojo.vo.user.RegisterRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = L23o6ApplicationTests.class)
@Transactional

public class UserServiceImplTest {
    @Autowired
    UserController userController;

    @Test
    public void registerSuccessTest() {
//        UserEntity user = new UserEntity();
//        user.setId(101L);
//        user.setUsername("ZhouMi_user");
//        user.setPassword("123123");
//        user.setName("ZhouMi");
//        user.setPhone("18366685609");
//        user.setIdn("370481200307206512");
//
//        String username = "ZhouMi_user";
//        String password = "123123";
//
//        userService.login(username, password);
//
//        assert username.equals(user.getUsername());
//        assert password.equals(user.getPassword());
//        UserVO userVO = new UserVO();
//        userVO.setName("ZhouMi");
//        userVO.setIdn("370481200307258312");
//        userVO.setPhone("18366685690");
        RegisterRequest request = new RegisterRequest();
        request.setName("ZhouMi");
        request.setUsername("zhoumi-user");
        request.setPhone("18366685699");
        request.setIdn("370481200608296582");
        request.setType("身份证");
        request.setPassword("123123");

        CommonResponse<?> response = userController.register(request);

        assert Objects.equals(response, CommonResponse.success());


    }

    public void loginSuccessTest() {
        RegisterRequest request = new RegisterRequest();
        request.setName("ZhouMi");
        request.setUsername("zmuser");
        request.setPhone("18366685699");
        request.setIdn("370481200608296582");
        request.setType("身份证");
        request.setPassword("123123");
        userController.register(request);


        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("zmuser");
        loginRequest.setPassword("123123");

        CommonResponse<?> login_response = userController.login(loginRequest);

        assert Objects.equals(login_response, CommonResponse.success());
    }

    public void loginFailTest() {
        RegisterRequest request = new RegisterRequest();
        request.setName("ZhouMi");
        request.setUsername("zmuser");
        request.setPhone("18366685699");
        request.setIdn("370481200608296582");
        request.setType("身份证");
        request.setPassword("123123");
        userController.register(request);


        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("zmuser");
        loginRequest.setPassword("123123456");

        CommonResponse<?> login_response = userController.login(loginRequest);

        assert !Objects.equals(login_response, CommonResponse.success());
    }

}