package com.youmeng.taoshelf.util;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisUtil {

    @Bean
    public RedisTemplate<String, Integer> counter() {
        return null;
    }
}
