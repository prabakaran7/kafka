package com.praba.server.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

import com.google.protobuf.Message;
import com.praba.exceptions.InvalidMessageFormatException;
import com.praba.kafka.producer.MessageProducer;
import com.praba.protobuf.converter.JsonToMessage;

public class HttpServerHandler extends SimpleChannelInboundHandler<Object> {

	private HttpRequest request;

	private final StringBuilder buf = new StringBuilder();

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof HttpRequest) {
			this.request = (HttpRequest) msg;

		}

		if (msg instanceof HttpContent) {
			HttpContent httpContent = (HttpContent) msg;

			String queryParam = httpContent.content().toString(
					CharsetUtil.UTF_8);
			if (msg instanceof LastHttpContent) {
				try {
					System.out.println(request.getUri());

					// read topic
					String uri[] = request.getUri().split("/");
					if (uri.length == 2) {
						Message message = JsonToMessage.parseJson(queryParam);
						String topic = uri[1];
						MessageProducer.publish(message.toString(), topic);
						ByteBuf content = Unpooled.copiedBuffer(
								"Message is successfully posted!!!",
								CharsetUtil.UTF_8);
						FullHttpResponse response = new DefaultFullHttpResponse(
								HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
								content);
						response.headers().set(HttpHeaders.Names.CONTENT_TYPE,
								"text/plain");
						response.headers().set(
								HttpHeaders.Names.CONTENT_LENGTH,
								content.readableBytes());
						ctx.write(response);
					} else {
						FullHttpResponse response = new DefaultFullHttpResponse(
								HttpVersion.HTTP_1_1,
								HttpResponseStatus.REQUEST_URI_TOO_LONG,
								Unpooled.copiedBuffer("Invalid uri for topic"
										+ "uri should be /{topicName}/",
										CharsetUtil.UTF_8));

						ctx.write(response);
					}
				} catch (InvalidMessageFormatException e) {
					FullHttpResponse response = new DefaultFullHttpResponse(
							HttpVersion.HTTP_1_1,
							HttpResponseStatus.BAD_REQUEST,
							Unpooled.copiedBuffer(e.getMessage(),
									CharsetUtil.UTF_8));
					ctx.write(response);
				}
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
