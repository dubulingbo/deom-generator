package edu.dublbo.generator.service;

import edu.dublbo.generator.dao.RedisUtil;
import edu.dublbo.generator.utils.SnowflakeIdWorker;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限用户Service类
 *
 * @author Administrator
 * @since 2020/9/12 17:14,星期六
 * Always believe that something wonderful is about to happen.
 **/
@Service
public class AuthUserService {

    @Autowired
    private SnowflakeIdWorker idWorker;

    @Autowired
    private RedisUtil redisUtil;

    private final String username = "admin";
    private final String password = "12345678";


    public Map<String, String> get(String username, String password) {
        Map<String, String> userMap = new HashMap<>();
        if (!StringUtils.isBlank(username) && this.username.equals(username) && this.password.equals(password)) {
            // 生成 token
            String token = RandomStringUtils.random(8, true, true)
                    + idWorker.nextStringId() + RandomStringUtils.random(6, true, false);
            userMap.put("username", this.username);
            userMap.put("password", this.password);
            userMap.put("token", token);
            redisUtil.set(username, token, 3 * 60 * 60);
        }
        return userMap;
    }

}
