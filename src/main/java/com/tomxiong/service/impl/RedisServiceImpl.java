package com.tomxiong.service.impl;

import com.tomxiong.service.RedisService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author TOM XIONG
 * @description RedisService服务实现
 * @date 2019/7/4 17:42
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Override
    @Cacheable(key="#root.methodName", cacheNames ="loadData")
    public String loadData(){
        return "redisValue";
    }

}
