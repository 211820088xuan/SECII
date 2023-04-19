package org.fffd.l23o6.dao;

import org.fffd.l23o6.pojo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<UserEntity, Integer> {
    UserEntity findByUsername(String username);
}