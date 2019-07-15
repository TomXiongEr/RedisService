package com.tomxiong.netty.server;

import java.util.concurrent.TimeUnit;

import com.tomxiong.netty.handler.HttpRequestHandler;
import com.tomxiong.netty.handler.NettyHeartbeatHandler;
import com.tomxiong.netty.handler.TextWebSocketFrameHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyServerInitializer extends ChannelInitializer<Channel>{
	private final ChannelGroup group;
	
	public NettyServerInitializer(ChannelGroup group) {
		this.group = group;
	}
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		//处理日志
		pipeline.addLast(new LoggingHandler(LogLevel.INFO));
		
		//处理心跳
		//IdleStateHandler可处理读超时（客户端长时间没有发送数据给服务端）、写超时（服务端长时间没有发送数据到客户端）和读写超时（客户端与服务端长时间无数据交互）三种情况
		//https://blog.csdn.net/e_wsq/article/details/53710972
		pipeline.addLast(new IdleStateHandler(0, 0, 100, TimeUnit.SECONDS));//空闲状态处理器
		pipeline.addLast(new NettyHeartbeatHandler());
		pipeline.addLast(new HttpServerCodec());
		pipeline.addLast(new ChunkedWriteHandler());
		pipeline.addLast(new HttpObjectAggregator(64 * 1024));
		pipeline.addLast(new HttpRequestHandler());
		pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
		pipeline.addLast(new TextWebSocketFrameHandler(group));
	}
}
