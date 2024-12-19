package com.yibu.yibujudge.constant;

import cn.hutool.core.util.RandomUtil;

public class UserConstant {

    public static final String EMAIL_PARAM_ERROR = "email parameter error";

    public static final String EMAIL_NOT_NULL = "email can not be null";
    
    public static final String CAPTCHA_SUBJECT = "YiBU Judge 验证码";
    public static final String CAPTCHA_FREQUENT_ERROR = "验证码发送频繁，请稍后再试";

    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9_-]{4,16}$";
    public static final String USERNAME_PATTERN_MESSAGE = "用户名只能包含字母、数字、下划线和减号，长度4-16位";
    public static final String PASSWORD_PATTERN = "^[a-zA-Z0-9]{6,16}$";
    public static final String PASSWORD_PATTERN_MESSAGE = "密码只能包含字母和数字，长度6-16位";
    public static final String NICKNAME_PATTERN = "^[a-zA-Z0-9\\u4e00-\\u9fa5]{1,16}$";
    public static final String NICKNAME_PATTERN_MESSAGE = "昵称只能包含中文、字母和数字，长度1-16位";
    public static final String CAPTCHA_PARAM_ERROR = "captcha parameter error";
    public static final String CAPTCHA_ERROR = "captcha error";
    public static final int ROLE_USER = 0;
    public static final Integer DEFAULT_SCORE = 1200;
    public static final String REGISTER_ERROR = "register error";
    public static final String USER_ID = "userId";
    public static final String EMAIL_EXIST = "email already exist";
    public static final String PARAM_ERROR = "parameter error";
    public static final String LOGIN_ERROR = "username or password error";
    public static final String USER_NOT_EXIST = "user not exist";
    public static final String DEFAULT_AVATAR = "https://api.multiavatar.com/";
    public static final String AVATAR_SUFFIX = ".svg";
    public static final String TYPE_LOGIN = "login";
    public static final String TYPE_REGISTER = "register";
    public static final String TYPE_RESET_PASSWORD = "reset";
    public static final String LOGIN_TEMPLATE = "你正在进行登录操作,你的验证码是：%s，请在5分钟内输入。如非本人操作，请忽略本邮件。";
    public static final String REGISTER_TEMPLATE = "你正在进行注册操作,你的验证码是：%s，请在5分钟内输入。如非本人操作，请忽略本邮件。";
    public static final String RESET_PASSWORD_TEMPLATE = "你正在进行密码重置操作,你的验证码是：%s，请在5分钟内输入。如非本人操作，请忽略本邮件。";
    public static final String TOKEN_ERROR = "token error";
    public static final String UPDATE_ERROR = "update error";
    public static final String RANDOM_NICKNAME = "YiBu_" + RandomUtil.randomString(5) + "_" + RandomUtil.randomNumbers(2);
    public static final String USERNAME_EXIST = "username already exist";

    public static final String UPLOAD_ERROR = "upload error";
    public static final String REQUEST_FREQUENTLY = "request frequently";
    public static final String ROLE = "role";
    public static final Integer ROLE_ADMIN = 1;
}
