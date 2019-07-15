package com.tomxiong.netty.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.tomxiong.netty.bean.UserInfo;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author TOM XIONG
 * @description 全局变量
 * @date 2019/7/9 11:47
 */
public class GlobalConstant {

    public static final AttributeKey<String> CHANNEL_TOKEN_KEY = AttributeKey.valueOf("netty.channel.token");
    /**用来保存当前在线人员*/
    public static Map<String, UserInfo> onlines = new ConcurrentHashMap<String, UserInfo>();

    public static Map<String,Channel> onlineIP = new ConcurrentHashMap<String,Channel>();

    public static void addOnlines(String sessionId, UserInfo val) {
        onlines.put(sessionId, val);
    }

    public static void removeOnlines(String sessionId) {
        if(StringUtils.isNotBlank(sessionId) && onlines.containsKey(sessionId)){
            onlines.remove(sessionId);
        }
    }

    public static void addOnlineIP(String ip, Channel channel) {
        onlineIP.put(ip, channel);
    }

    public static void removeOnlineIP(String sessionId) {
        if(StringUtils.isNotBlank(sessionId) && onlineIP.containsKey(sessionId)){
            onlineIP.remove(sessionId);
        }
    }

    private static char[]prefix = {'A','B','C','D','E','F','G','H','J','K','L','M','N','P','Q','R','S','T','U','V','W','X','Y'};
    private static int[]imgPrefix = {1,2,3,4,5,6,7,8,9,10,11};

    public static String headImg() {
        int index = RandomUtils.nextInt(0, imgPrefix.length);
        return "/resources/img/head/"+imgPrefix[index]+".jpg";
    }

    public static String code() {
        int index = RandomUtils.nextInt(0, prefix.length);
        char prf = prefix[index];
        String len = (onlines.size()+1)+"";
        if(len.length() < 4) {
            len = StringUtils.leftPad(len, 4, '0');
        }
        return prf+len;
    }
}
