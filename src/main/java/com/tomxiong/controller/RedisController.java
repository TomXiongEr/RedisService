package com.tomxiong.controller;

import java.math.BigDecimal;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tomxiong.netty.server.GlobalConstant;
import com.tomxiong.service.RedisService;
import com.tomxiong.util.Constant;
import com.tomxiong.util.RedisUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.core.serializer.Serializer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author TOM XIONG
 * @description
 * @date 2019/7/4 17:54
 */
@Controller
@RequestMapping("/")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CacheManager cacheManager;

    @RequestMapping("index")
    public String load(){
        RedisUtil.setRedisTemplate(redisTemplate);
        for(String cacheName:cacheManager.getCacheNames()){
            System.err.println("====cacheName:"+cacheManager.getCache(cacheName).getName());
        }
        redisService.loadData();
        return  "index";
    }

    @GetMapping("hotword/{word}")
    @ResponseBody
    public String hotWord(@PathVariable("word") String word){
        Long index=RedisUtil.zReverseRank(Constant.HOT_RANKING,word);
        //元素不存在
        if(index==null){
            RedisUtil.zAdd(Constant.HOT_RANKING,word,generateScore(Constant.NUMBER_ONE));
        }else{
            //热度加1
            long originScores=BigDecimal.valueOf(RedisUtil.zScore(Constant.HOT_RANKING,word)).longValue();
            long parsedScores=parseScore(originScores)+Constant.NUMBER_ONE;
            RedisUtil.zRemove(Constant.HOT_RANKING,word);
            RedisUtil.zAdd(Constant.HOT_RANKING,word,generateScore(parsedScores));
            //RedisUtil.zIncrementScore(Constant.HOT_RANKING,word,originScores);
        }
        return HttpStatus.OK.name();
    }


    @Scheduled(cron ="0/10 0/1 * * * ?")
    public void pushHotRanking(){
        Map<String,Channel>  onlineIP=GlobalConstant.onlineIP;
        Iterator<Map.Entry<String,Channel>> entry=onlineIP.entrySet().iterator();
        while(entry.hasNext()){
            String ip=entry.next().getKey();
            Channel channel=onlineIP.get(ip);
            if(channel.isOpen()){
                channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(loadHotWord(),SerializerFeature.DisableCircularReferenceDetect)));
            }
        }
    }


    private  List<Map<String,Object>> loadHotWord(){
        Set<ZSetOperations.TypedTuple<String>>  dataSet=
                RedisUtil.zReverseRangeWithScores(Constant.HOT_RANKING,Constant.NUMBER_ZERO,Constant.NUMBER_ONE_MINUS);
        List<Map<String,Object>> tuples=new ArrayList<Map<String,Object>>();
        dataSet.forEach((typedTuple)-> {
            System.err.println("====value:"+typedTuple.getValue());
            System.err.println("====score:"+typedTuple.getScore());
            Map<String,Object> tuple=new HashMap<String,Object>(){{
                put("value",typedTuple.getValue());
                put("score",typedTuple.getScore());
            }};
            tuples.add(tuple);
        });
        return tuples;
    }

    private long generateScore(double value){
        StringBuffer stringBuffer=new StringBuffer();
        long frontNumber=10*10*10+Double.valueOf(value).intValue();
        long timestamp=System.currentTimeMillis();
        stringBuffer.append(frontNumber).append(timestamp);
        return Long.parseLong(stringBuffer.toString());
    }

    private long parseScore(long score){
        String frontNumber=String.valueOf(score).substring(Constant.NUMBER_ZERO,Constant.NUMBER_FOUR);
        return Long.parseLong(frontNumber)-1000;
    }
}