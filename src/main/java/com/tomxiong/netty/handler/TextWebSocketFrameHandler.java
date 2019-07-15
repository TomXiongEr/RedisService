package com.tomxiong.netty.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tomxiong.netty.bean.UserInfo;
import com.tomxiong.netty.server.GlobalConstant;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * netty来发送和接收数据都会继承SimpleChannelInboundHandler和ChannelInboundHandlerAdapter这两个抽象类
 * SimpleChannelInboundHandler在接收到数据后会自动release掉数据占用的Bytebuffer资源(自动调用Bytebuffer.release())
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>{
	private Logger loger = LogManager.getLogger();
	private final ChannelGroup group;
	
	public TextWebSocketFrameHandler(ChannelGroup group) {
		this.group = group;
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		loger.info("Event====>{}", evt);
		if(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
			ctx.pipeline().remove(HttpRequestHandler.class);
			//加入当前, 上线人员推送前端，显示用户列表中去
			Channel channel = ctx.channel();
			try{
				group.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString("",SerializerFeature.DisableCircularReferenceDetect)));
				group.add(channel);
			}catch(Exception e){
				e.printStackTrace();
			}
		}else {
			super.userEventTriggered(ctx, evt);
		}
	}
	
	/**接收客户端发送的消息时触发**/
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		Channel channel = ctx.channel();
		String token = channel.attr(GlobalConstant.CHANNEL_TOKEN_KEY).get();
		UserInfo from = GlobalConstant.onlines.get(token);
		if(from == null) {
			group.writeAndFlush("OK");
		}else {
			group.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString("",SerializerFeature.DisableCircularReferenceDetect)));
		}
	}
	
	/**
	 * 当前通道激活状态变为失去激活时触发
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		loger.info("Current channel channelInactive");
		offlines(ctx);
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		loger.info("Current channel handlerRemoved");
		offlines(ctx);
	}
	
	private void offlines(ChannelHandlerContext ctx) {
		Channel channel = ctx.channel();
		String token = channel.attr(GlobalConstant.CHANNEL_TOKEN_KEY).get();
		GlobalConstant.removeOnlines(token);
		group.remove(channel);
		ctx.close();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		loger.error("=====> {}", cause.getMessage());
		offlines(ctx);
	}
}
