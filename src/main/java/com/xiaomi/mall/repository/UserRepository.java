package com.xiaomi.mall.repository;

import com.xiaomi.mall.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Long>{

    UserEntity findByUsername (String userName);

    UserEntity findByPhone (String phone);
}
