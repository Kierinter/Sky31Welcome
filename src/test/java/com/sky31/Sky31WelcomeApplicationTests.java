package com.sky31;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sky31.domain.User;
import com.sky31.mapper.UserMapper;
import com.sky31.utils.md5Util;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class Sky31WelcomeApplicationTests {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void test01() {
        String name = redisTemplate.opsForValue().get("name");
        System.out.println(name);
    }

    @Test
    public void test02(){

    }

}
