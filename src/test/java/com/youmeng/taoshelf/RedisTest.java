package com.youmeng.taoshelf;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    private Logger logger = LoggerFactory.getLogger(RedisTest.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void test1() {
        String u = redisTemplate.opsForValue().get("u");
        redisTemplate.opsForValue().increment("u", 1);
        System.out.println(u);
    }

    @Test
    public void test2() {
        redisTemplate.delete("u");
    }
}
