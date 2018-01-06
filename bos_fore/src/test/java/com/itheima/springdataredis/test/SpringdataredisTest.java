package com.itheima.springdataredis.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SpringdataredisTest {

    //注入RedisTemplate
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    public void redisTemplateTest(){
        //保存key和value,并设置生存时间
        redisTemplate.opsForValue().set("zxao","hjjvj",40, TimeUnit.SECONDS);
        String birthday = redisTemplate.opsForValue().get("zxao");
        System.out.println(birthday);

    }

}
