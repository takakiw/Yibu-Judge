package com.yibu.yibuJudge.model.dto;


import com.yibu.yibuJudge.constant.UserConstant;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    @Pattern(regexp = UserConstant.USERNAME_PATTERN, message = UserConstant.USERNAME_PATTERN_MESSAGE)
    private String username;
    @Pattern(regexp = UserConstant.PASSWORD_PATTERN, message = UserConstant.PASSWORD_PATTERN_MESSAGE)
    private String password;
    @Pattern(regexp = UserConstant.NICKNAME_PATTERN, message = UserConstant.NICKNAME_PATTERN_MESSAGE)
    private String nickName;
    @Email(message = UserConstant.EMAIL_PARAM_ERROR)
    private String email;
    @Size(min = 6, max = 6, message = UserConstant.CAPTCHA_PARAM_ERROR)
    private String captcha;
    private String avatar;
}