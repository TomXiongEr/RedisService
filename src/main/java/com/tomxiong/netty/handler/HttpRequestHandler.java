package com.tomxiong.netty.handler;

import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.tomxiong.netty.bean.UserInfo;
import com.tomxiong.netty.server.GlobalConstant;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	private Logger LOGGER = LogManager.getLogger();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		InetSocketAddress ipSocket = (InetSocketAddress)ctx.channel().remoteAddress();
		String clientIp = ipSocket.getAddress().getHostAddress();
		GlobalConstant.addOnlineIP(clientIp,ctx.channel());
		ctx.channel().attr(GlobalConstant.CHANNEL_TOKEN_KEY).getAndSet(clientIp);
		ctx.fireChannelRead(request.retain());
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
	private void send100ContinueExpected(ChannelHandlerContext ctx) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONFLICT);
		ctx.writeAndFlush(response);		
	}
}
