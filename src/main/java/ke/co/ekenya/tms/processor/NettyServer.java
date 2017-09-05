/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.ekenya.tms.processor;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.codec.string.*;
import io.netty.util.CharsetUtil;

/**
 *
 * @author julius
 */
public class NettyServer {

    static final int PORT = Integer.parseInt("9078");
    // ChannelFuture f;
    static EventLoopGroup bossGroup;
    static EventLoopGroup workerGroup;
    static ChannelFuture f;

    public void startserver() {
        try {
            // Configure the server.
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG, 100)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline p = ch.pipeline();

                                // Decoders
                                // p.addLast("frameDecoder", new LineBasedFrameDecoder(80));
                                //  p.addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
                                // Encoder
                                //  p.addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));
                                //                       if (sslCtx != null) {
                                //                          p.addLast(sslCtx.newHandler(ch.alloc()));
                                //                      }
                                //p.addLast(new LoggingHandler(LogLevel.INFO));
                                p.addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));//CharsetUtil.UTF_8
                                p.addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));
                                p.addLast("myHandler1", new NettyServerHandler());
                            }
                        });

                // Start the server.
                System.out.println("Binding to port: 9078");
                f = b.bind(PORT).sync();
                f.channel().closeFuture().sync();

            } catch (Exception ex) {

                //ex.printStackTrace();

            } finally {
                // Shut down all event loops to terminate all threads.
                bossGroup.shutdownGracefully();
                 workerGroup.shutdownGracefully();
            }
        } catch (Exception ex) {

            //ex.printStackTrace();

        }
    }

    public void Stopserver() {
        try {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();

        } catch (Exception ex) {
            //ex.printStackTrace();

        } finally {
            if (!bossGroup.isShutdown()) {
                bossGroup.shutdown();
            }
            if (!workerGroup.isShutdown()) {
                workerGroup.shutdown();
            }
        }
    }
}
