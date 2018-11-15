package com.xiaomi.mall.web;

import com.xiaomi.mall.common.result.ExceptionMsg;
import com.xiaomi.mall.common.result.Response;
import com.xiaomi.mall.entity.UserEntity;
import com.xiaomi.mall.repository.UserRepository;
import com.xiaomi.mall.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lcyang
 * @Date 2018/11/10 17:24
 * @Description
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @RequestMapping(value = "/doregist",method = RequestMethod.POST)
    public String doregist(UserEntity userEntity){
        try {
            UserEntity registUser = userRepository.findByUsername(userEntity.getUsername());
            if (registUser != null){
                return "该昵称已被占用";
            }
            userEntity.setCreated(DateUtils.getCurrentTime());
            userEntity.setUpdated(DateUtils.getCurrentTime());
            userRepository.save(userEntity);
        }catch (Exception e){
            return "注册失败";
        }
        return "注册成功";
    }
}
