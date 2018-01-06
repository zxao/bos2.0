package com.itheima.redis.test;

import org.junit.runner.RunWith;
import redis.clients.jedis.Jedis;

public class RedisTest {
    /**
     * redis的测试
     * @param args
     */
    public static void main(String[] args) {
        //操作redis需要导入jedis的包
        //创建jedis的对象
        Jedis jedis = new Jedis("localhost");
//        jedis的set方法可以存入键值对（键和值都为字符串）
        jedis.set("age","28");
//        get方法可以通过键获取值
        String age = jedis.get("age");
        //通过setex设置字段的生命周期
        jedis.setex("password",20,"123");
        System.out.println(age);


    }

}
