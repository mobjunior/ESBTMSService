///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package ke.co.ekenya.tms.utilities;
//
///**
// *
// * @author julius
// */
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import redis.clients.jedis.Jedis;
//
//public class RedisConfigs {
//
//    Jedis jedis = null;
//
//    public RedisConfigs() {
//
//    }
//
//    public void getRedisConnection() {
//        try {
//            //jedis = new Jedis("52.167.209.202", 4110);
//            jedis = new Jedis("10.40.45.10", 4110);
//            jedis.auth("the@0fwar");
//            jedis.select(2);
//
//        } catch (Exception ex) {
//            StringWriter sw = new StringWriter();
//            ex.printStackTrace(new PrintWriter(sw));
//            System.err.println(sw.toString());
//        }
//    }
//
//    public String getConfig(String keyString) {
//        if (this.jedis == null) {
//            getRedisConnection();
//        } else if (!this.jedis.isConnected()) {
//            getRedisConnection();
//        }
//        String valString = null;
//        try {
//            valString = this.jedis.get(keyString);
//        } catch (Exception ex) {
//            StringWriter sw = new StringWriter();
//            ex.printStackTrace(new PrintWriter(sw));
//            System.err.println(sw.toString());
//        } finally {
//            if (this.jedis.isConnected()) {
//                this.jedis.close();
//            }
//        }
//        return valString;
//    }
//
//    public String getConfigFromObject(String objKey, String keyString) {
//        String valString = null;
//        try {
//            if (this.jedis == null) {
//                getRedisConnection();
//            } else if (!this.jedis.isConnected()) {
//                getRedisConnection();
//            }
//            valString = this.jedis.hget(objKey, keyString);
//        } catch (Exception ex) {
//            StringWriter sw = new StringWriter();
//            ex.printStackTrace(new PrintWriter(sw));
//            System.err.println(sw.toString());
//        } finally {
//            if (this.jedis.isConnected()) {
//                this.jedis.close();
//            }
//        }
//        return valString;
//    }
//}
