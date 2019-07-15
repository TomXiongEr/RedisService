package com.tomxiong;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.tomxiong.netty.server.GlobalConstant;
import com.tomxiong.netty.server.NettyServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author TOM XIONG
 * @description Redis特殊操作
 * @date 2019/7/4 16:47
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableScheduling
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class RedisApplication implements CommandLineRunner {

    @Autowired
    private NettyServer nettyServer;

    public static void main(String[] args){
        SpringApplication.run(RedisApplication.class,args);
    }

    /*重新设置内置的tomcat的参数*/
    /*
    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer(){
        return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
            @Override
            public void customize(ConfigurableWebServerFactory factory) {
                factory.setPort(8081);
            }
        };
    }*/

    @Override
    public void run(String... args) throws Exception {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 9090);
        //绑定端口，开启监听
        ChannelFuture future = nettyServer.start(address);
        //针对jvm增加关闭的钩子，在jvm关闭之前，先执行定义的钩子里面的内容
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                nettyServer.destroy();
            }
        });
        //开启channel的监听器，负责监听channel是否关闭，主线程会同步等待子线程，如果channel通道关闭了子线程才会释放，
        //syncUninterruptibly让主线程等待子线程的结果
        future.channel().closeFuture().syncUninterruptibly();
    }
}
