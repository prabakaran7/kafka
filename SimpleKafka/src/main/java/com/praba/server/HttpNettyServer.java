package com.praba.server;

import java.util.Date;

import org.apache.log4j.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import com.praba.server.util.HttpServerInitializer;
import com.praba.util.Constants;

public class HttpNettyServer implements Runnable {

	private int port;
	private static int DEFAULT_PORT;
	private static Logger logger = Logger.getLogger(HttpNettyServer.class
			.getName());
	static {
		try {
			String portString = System.getProperty(Constants.PORTString,
					Constants.DEFAULT_PORT_NO);
			DEFAULT_PORT = Integer.parseInt(portString);
		} catch (Exception e) {
			DEFAULT_PORT = 8080;
			logger.error("Invalid Port number is received. Hence defaulting to "
					+ DEFAULT_PORT);
		}
		logger.info("Setting port number to " + DEFAULT_PORT);
	}

	HttpNettyServer(int port) {
		this.port = port;
	}

	public static void main(String[] args) throws Exception {

		new HttpNettyServer(DEFAULT_PORT).run();
	}

	public void run() {
		logger.info("Starting to setup server");
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.DEBUG))
					.childHandler(new HttpServerInitializer())
					.option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture f = b.bind(port).sync();
			logger.info("Server is ready to serve.");
			logger.info("*********** http://localhost:" + port + "/ ********");
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			logger.info(new Date() + " Terminating the server.....");
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
}
