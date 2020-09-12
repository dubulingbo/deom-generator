package edu.dublbo.generator.controller;

import edu.dublbo.generator.common.result.OptStatus;
import edu.dublbo.generator.common.result.ResponseResult;
import edu.dublbo.generator.common.result.Result;
import edu.dublbo.generator.service.AuthUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * 权限用户控制类
 *
 * @author Administrator
 * @since 2020/9/12 17:13,星期六
 * Always believe that something wonderful is about to happen.
 **/
@RestController
@RequestMapping(value = "/user")
public class AuthUserController {
    @Autowired
    private AuthUserService service;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@NotBlank String username, @NotBlank String password) {
        Map<String, String> res = service.get(username, password);
        if(res == null || res.size() == 0 || StringUtils.isBlank(res.get("token"))){
            return ResponseResult.generateResult(OptStatus.FAIL.getOptCode(), "登录失败",null);
        }
        Map<String, Object> res1 = new HashMap<>();
        res1.put("authUser",res);
        return ResponseResult.generateSuccessResult(res1);
    }
}
