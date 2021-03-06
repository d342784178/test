package 工具使用.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;

import java.util.List;

public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(group) // 绑定线程池
                    .channel(NioServerSocketChannel.class) // 指定使用的channel
                    .localAddress(this.port)// 绑定监听端口
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4));//指定长度
                            ch.pipeline().addLast(new StringDecoder() {//过滤前4个字节的长度标志位
                                @Override
                                protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out)
                                        throws Exception {
                                    super.decode(ctx, msg.copy(4, msg.readableBytes() - 4), out);
                                }
                            });
                            ch.pipeline().addLast(new EchoServerHandler()); // 客户端触发操作

                            ch.pipeline().addFirst(new LengthFieldPrepender(4));
                        }
                    });
            ChannelFuture cf = sb.bind().sync(); // 服务器异步创建绑定
            cf.channel().closeFuture().sync(); // 关闭服务器通道
        } finally {
            group.shutdownGracefully().sync(); // 释放线程池资源
        }
    }

    public static void main(String[] args) throws Exception {
        new EchoServer(65535).start(); // 启动
    }
}