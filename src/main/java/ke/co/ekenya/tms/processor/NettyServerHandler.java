/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.ekenya.tms.processor;

import ke.co.ekenya.tms.esbtmsservice.ESBTMSService;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ke.co.ekenya.tms.utilities.Props;
import java.text.SimpleDateFormat;

/**
 *
 * @author julius
 */
@Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    public static Boolean isConnected = false;
    public static ChannelHandlerContext ctxr;
    private static Props props = new Props();
    private static String s;

    private void sendresponse() {
        try {
            //ctx.channel().write((Object) "Mimi");
            ctxr.write("Mimi");
        } catch (Exception ex) {

            ex.printStackTrace();

        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            //ctx.write(msg);
            this.ctxr = ctx;
//        ByteBuf m = (ByteBuf) msg;
//        try {
//            s = new String(m.array(), "UTF-8");
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(NettyServerHandler.class.getName()).log(Level.SEVERE, null, ex);
//        }

            String IsoMsg = (String) msg;
            System.out.println((new SimpleDateFormat("MMMM dd,yyyy hh:mm:ss.SSS a zzzz")).format(new java.util.Date()) + "\n[RECEIVE: Incoming Message from POS TERMINAL:]");
            System.out.println("\n" + IsoMsg);
            //
            ESBTMSService EC = new ESBTMSService(IsoMsg);
            EC.ThreadFromPOS();
            // this.sendresponse();

            // ctx.write(msg);
            // String IsoMsg = (String) msg;
            //test db connection here
            //pass to the processing classes
        } catch (Exception ex) {

            ex.printStackTrace();

        }
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        isConnected = true;
        String ip = ctx.channel().remoteAddress().toString();
        System.out.println("Connected from :" + ip);

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        isConnected = false;
        String ip = ctx.channel().remoteAddress().toString();
        System.out.println("Channel Inactive :" + ip);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        //cause.printStackTrace();
        ctx.close();

    }
}
