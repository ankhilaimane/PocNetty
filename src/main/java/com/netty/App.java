package com.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class App {
  public static void main(String[] args) throws InterruptedException {

    String host = "www.google.com";
    int port = 443;
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      Bootstrap b = new Bootstrap();
      b.group(workerGroup);
      b.channel(NioSocketChannel.class);
      b.option(ChannelOption.SO_KEEPALIVE, true);
      b.handler(new ChannelInitializer<SocketChannel>() {

        @Override
        public void initChannel(SocketChannel ch) throws Exception { 
          ch.pipeline().addLast(
              new RequestDataEncoder(),
              new ResponseDataDecoder(),
              new ClientHandler());
        }
      });

      ChannelFuture f = b.connect(host, port).sync();
      //traitement
      if(f.channel().isActive()) {
       //String display= f.channel().read().toString();
       
       System.out.println("Connected to [" + host + ':' + port + ']');
       //System.out.println(display);
       
       
       
      }
      f.channel().closeFuture().sync();
    } finally {
      workerGroup.shutdownGracefully();
    }

  }
}
