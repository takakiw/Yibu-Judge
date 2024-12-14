package com.yibu.yibuJudge.cache;

import com.yibu.yibuJudge.model.entity.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class UserCacheService {

    private static final String CAPTCHA_CACHE_PREFIX = "captcha_";
    private static final long CAPTCHA_CACHE_EXPIRE_TIME = 5 * 60;

    private static final String USER_CACHE_PREFIX = "user_";
    private static final long USER_CACHE_EXPIRE_TIME = 24 * 60 * 60;

    private final RedisTemplate<String, Object> redisTemplate;

    public UserCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveCaptcha(String email, String type, String captcha) {
        redisTemplate.opsForValue().set(CAPTCHA_CACHE_PREFIX + email + "_" + type, captcha, CAPTCHA_CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    public String getCaptcha(String email, String type) {
        return (String) redisTemplate.opsForValue().get(CAPTCHA_CACHE_PREFIX + email + "_" + type);
    }


    public boolean isCaptchaFrequent(String email, String type) {
        Long expire = redisTemplate.getExpire(CAPTCHA_CACHE_PREFIX + email + "_" + type , TimeUnit.SECONDS);
        return expire != null && CAPTCHA_CACHE_EXPIRE_TIME - expire < 60;
    }

    public User getUserById(Long id) {
        return (User) redisTemplate.opsForValue().get(USER_CACHE_PREFIX + id);
    }

    public void saveUser(User user) {
        redisTemplate.opsForValue().set(USER_CACHE_PREFIX + user.getId(), user, USER_CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    public void deleteUser(Long uid) {
        redisTemplate.delete(USER_CACHE_PREFIX + uid);
    }

    public Boolean lock(String key, int time) {
        return redisTemplate.opsForValue().setIfAbsent(key, "locked", time, TimeUnit.SECONDS);
    }
    public void unlock(String key) {
        redisTemplate.delete(key);
    }
}
