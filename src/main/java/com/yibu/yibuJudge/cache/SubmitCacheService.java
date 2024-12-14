package com.yibu.yibuJudge.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class SubmitCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public SubmitCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public final static String SUBMIT_PREFIX = "submit:";

    public final static long SUBMIT_CACHE_EXPIRE_TIME = 60 * 60 * 24L; // 最大缓存时间为一天

    // 保存提交记录到缓存, 存储还没有判题的提交
    public void saveSubmitRecord(Long submitId) {
        redisTemplate.opsForValue().set(SUBMIT_PREFIX + submitId, submitId, SUBMIT_CACHE_EXPIRE_TIME);
    }

    // 从缓存中删除提交记录， 判题完成后删除
    public void deleteSubmitRecord(Long submitId) {
        redisTemplate.delete(SUBMIT_PREFIX + submitId);
    }

    // 判断提交是否已经判题完成
    public boolean isSubmitRecordExists(Long submitId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(SUBMIT_PREFIX + submitId));
    }
}
