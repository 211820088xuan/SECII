package org.fffd.l23o6.service.impl;

import lombok.RequiredArgsConstructor;
import org.fffd.l23o6.dao.UserDao;
import org.fffd.l23o6.exception.BizException;
import org.fffd.l23o6.exception.ErrorType;
import org.fffd.l23o6.pojo.entity.UserEntity;
import org.fffd.l23o6.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public void register(String username, String password) {
        UserEntity user = userDao.findByUsername(username);

        if (user != null) {
            throw new BizException(ErrorType.USERNAME_EXISTS);
        }

        userDao.save(UserEntity.builder().username(username).password(password).build());
    }

    @Override
    public void login(String username, String password) {
        UserEntity user = userDao.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new BizException(ErrorType.INVALID_CREDENTIAL);
        }
    }
}