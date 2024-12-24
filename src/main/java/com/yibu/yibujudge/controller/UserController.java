package com.yibu.yibujudge.controller;

import cn.hutool.core.bean.BeanUtil;
import com.yibu.yibujudge.constant.UserConstant;
import com.yibu.yibujudge.model.dto.UserDTO;
import com.yibu.yibujudge.model.entity.User;
import com.yibu.yibujudge.model.response.Result;
import com.yibu.yibujudge.model.vo.UserVO;
import com.yibu.yibujudge.service.UserService;
import com.yibu.yibujudge.utils.BaseContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/captcha")
    public Result<Void> getCaptcha(@NotBlank(message = UserConstant.EMAIL_NOT_NULL) @Email(message = UserConstant.EMAIL_PARAM_ERROR) String email,
                             @RequestParam(required = false, defaultValue = UserConstant.TYPE_LOGIN) String type) {
        boolean sendCaptcha = userService.sendCaptcha(email, type);
        if (!sendCaptcha) {
            return Result.error(UserConstant.CAPTCHA_FREQUENT_ERROR);
        }
        return Result.success();
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody @Valid UserDTO userDTO) {
        String token = userService.register(userDTO.getEmail(), userDTO.getUsername(), userDTO.getPassword(), userDTO.getCaptcha());
        return Result.success(token);
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody UserDTO userDTO) {
        String token = userService.login(userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getCaptcha());
        return Result.success(token);
    }


    @GetMapping("/info/{id}")
    public Result<UserVO> info(@PathVariable("id") Long id) {
        if (id.compareTo(-1L) == 0) {
            id = BaseContext.getCurrentId();
            if (id == null) {
                return Result.refresh();
            }
        }
        User user = userService.getUser(id);
        if (user == null) {
            return Result.error(UserConstant.USER_NOT_EXIST);
        }
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        return Result.success(userVO);
    }

    @PutMapping("/update")
    public Result<Void> update(@RequestBody @Valid UserDTO userDTO) {
        Long uid = BaseContext.getCurrentId();
        if (uid == null) {
            return Result.refresh();
        }
        userService.update(uid, userDTO.getNickName(), userDTO.getPassword(), userDTO.getCaptcha());
        return Result.success();
    }

    @PostMapping("/update/avatar")
    public Result<String> updateAvatar(MultipartFile file) {
        Long uid = BaseContext.getCurrentId();
        if (uid == null) {
            return Result.refresh();
        }
        String newAvatarUrl =  userService.updateAvatar(uid, file);
        return Result.success(newAvatarUrl);
    }
}
