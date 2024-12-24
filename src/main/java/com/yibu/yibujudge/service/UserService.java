package com.yibu.yibujudge.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.yibu.yibujudge.cache.UserCacheService;
import com.yibu.yibujudge.constant.UserConstant;
import com.yibu.yibujudge.exceptions.BaseException;
import com.yibu.yibujudge.mapper.UserMapper;
import com.yibu.yibujudge.model.entity.User;
import com.yibu.yibujudge.utils.JWTUtil;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executor;

@Service
public class UserService {

    private final JavaMailSender mailSender;
    private final Executor captchaExecutor;
    private final UserCacheService cacheService;
    private final UserMapper userMapper;
    private final OssService ossService;
    @Value("${spring.mail.username}")
    private String from;

    public UserService(JavaMailSender mailSender,
                       Executor emailExecutor,
                       UserCacheService userCacheService,
                       UserMapper userMapper,
                       OssService ossService) {
        this.mailSender = mailSender;
        this.captchaExecutor = emailExecutor;
        this.cacheService = userCacheService;
        this.userMapper = userMapper;
        this.ossService = ossService;
    }

    public boolean sendCaptcha(String email, String type) {
        if (cacheService.isCaptchaFrequent(email, type)) {
            return false;
        }
        captchaExecutor.execute(() -> {
            MimeMessage message = mailSender.createMimeMessage();
            try {
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
                message.setSubject(UserConstant.CAPTCHA_SUBJECT);
                message.setFrom(from);
                String captcha = RandomUtil.randomNumbers(6);
                cacheService.saveCaptcha(email, type, captcha);
                String text;
                switch (type) {
                    case UserConstant.TYPE_LOGIN:
                        text = UserConstant.LOGIN_TEMPLATE;
                        break;
                    case UserConstant.TYPE_REGISTER:
                        text = UserConstant.REGISTER_TEMPLATE;
                        break;
                    case UserConstant.TYPE_RESET_PASSWORD:
                        text = UserConstant.RESET_PASSWORD_TEMPLATE;
                        break;
                    default:
                        return;
                }
                message.setText(String.format(text, captcha));
                mailSender.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
        return true;
    }

    public String login(String username, String password, String email, String captcha) {
        if (StringUtils.isAllBlank(username, email)) {
            throw new BaseException(UserConstant.PARAM_ERROR);
        }
        if (username != null && password != null) {
            String passwordMd5 = DigestUtil.md5Hex(password);
            User user = userMapper.loginByUsername(username, passwordMd5);
            if (user == null) {
                throw new BaseException(UserConstant.LOGIN_ERROR);
            }
            return JWTUtil.userToJWT(user);
        } else {
            String realCaptcha = cacheService.getCaptcha(email, UserConstant.TYPE_LOGIN);
            if (StringUtils.isBlank(captcha) || !captcha.equals(realCaptcha)) {
                throw new BaseException(UserConstant.CAPTCHA_ERROR);
            }
            User user = userMapper.getUserByEmail(email);
            if (user == null) {
                throw new BaseException(UserConstant.LOGIN_ERROR);
            }
            return JWTUtil.userToJWT(user);
        }
    }

    public User getUser(Long id) {
        User user = cacheService.getUserById(id);
        if (user == null) {
            user = userMapper.getUserById(id);
            if (user != null) {
                cacheService.saveUser(user);
            }
        }
        return user;
    }

    public void update(Long uid, String nickname, String password, String captcha) {
        User user = cacheService.getUserById(uid);
        if (user == null) {
            user = userMapper.getUserById(uid);
            if (user == null) {
                throw new BaseException(UserConstant.USER_NOT_EXIST);
            }
            cacheService.saveUser(user);
        }
        Boolean lock = cacheService.lock("update" + uid, 3000);
        if (!lock) {
            throw new BaseException(UserConstant.REQUEST_FREQUENTLY);
        }
        try {
            if (StringUtils.isAllBlank(nickname, password)) {
                throw new BaseException(UserConstant.PARAM_ERROR);
            }
            User updateUser = new User();
            if (StringUtils.isNotBlank(nickname) && !nickname.equals(user.getNickName())) {
                updateUser.setNickName(nickname);
            }
            if (StringUtils.isNotBlank(password) && !password.equals(user.getPassword())) {
                String realCaptcha = cacheService.getCaptcha(user.getEmail(), UserConstant.TYPE_RESET_PASSWORD);
                if (StringUtils.isBlank(captcha) || !captcha.equals(realCaptcha)) {
                    throw new BaseException(UserConstant.CAPTCHA_ERROR);
                }
                updateUser.setPassword(password);
            }
            updateUser.setId(uid);
            updateUser.setUpdateTime(LocalDateTime.now());
            int result = userMapper.update(updateUser);
            if (result == 0) {
                throw new BaseException(UserConstant.UPDATE_ERROR);
            }
            cacheService.deleteUser(uid);
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        } finally {
            cacheService.unlock("update" + uid);
        }
    }

    public String updateAvatar(Long uid, MultipartFile file) {
        Boolean lock = cacheService.lock("avatar" + uid, 3000);
        if (!lock) {
            throw new BaseException(UserConstant.REQUEST_FREQUENTLY);
        }
        User user = cacheService.getUserById(uid);
        if (user == null) {
            user = userMapper.getUserById(uid);
            if (user == null) {
                throw new BaseException(UserConstant.USER_NOT_EXIST);
            }
            cacheService.saveUser(user);
        }
        try {
            ossService.delete(user.getAvatar());
            String newAvatarUrl = ossService.upload(file);
            user.setAvatar(newAvatarUrl);
            user.setUpdateTime(LocalDateTime.now());
            int result = userMapper.update(user);
            if (result == 0) {
                throw new BaseException(UserConstant.UPDATE_ERROR);
            }
            cacheService.deleteUser(uid);
            return newAvatarUrl;
        } catch (IOException e) {
            throw new BaseException(UserConstant.UPLOAD_ERROR);
        } finally {
            cacheService.unlock("avatar" + uid);
        }
    }

    public String register(String email, String username, String password, String captcha) {
        String realCaptcha = cacheService.getCaptcha(email, UserConstant.TYPE_REGISTER);
        if (StringUtils.isBlank(captcha) || !captcha.equals(realCaptcha)) {
            throw new BaseException(UserConstant.CAPTCHA_ERROR);
        }
        User existUserByUsername = userMapper.getUserByUserNam(username);
        if (existUserByUsername != null) {
            throw new BaseException(UserConstant.USERNAME_EXIST);
        }
        User existUser = userMapper.getUserByEmail(email);
        if (existUser != null) {
            throw new BaseException(UserConstant.EMAIL_EXIST);
        }
        Boolean lock = cacheService.lock("register" + email, 3000);
        if (!lock) {
            throw new BaseException(UserConstant.REQUEST_FREQUENTLY);
        }
        Long userId = IdUtil.getSnowflake(1, 1).nextId();
        User user = new User(
                userId,
                username,
                DigestUtil.md5Hex(password),
                UserConstant.RANDOM_NICKNAME,
                UserConstant.DEFAULT_AVATAR + userId + UserConstant.AVATAR_SUFFIX,
                email,
                0,
                0,
                UserConstant.ROLE_USER,
                UserConstant.DEFAULT_SCORE,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        int result = userMapper.insert(user);

        if (result == 0) {
            cacheService.unlock("register" + email);
            throw new BaseException(UserConstant.REGISTER_ERROR);
        }
        cacheService.unlock("register" + email);
        return JWTUtil.userToJWT(user);
    }

    public List<User> getUsers(List<Long> ids) {
        return userMapper.getUserByIds(ids);
    }
}
