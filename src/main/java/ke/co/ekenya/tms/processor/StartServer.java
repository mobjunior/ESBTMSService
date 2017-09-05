///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package ke.co.ekenya.tms.processor;
//
//import ke.co.ekenya.tms.logsengine.TMSLog;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
////import javax.ejb.Singleton;
////import javax.ejb.Startup;
//
///**
// *
// * @author julius
// */
////@Singleton(name = "NettyTMSEJB")
////@Startup
//public class StartServer {
//
//    @PostConstruct
//
//    void init() {
//
//        try {
//            new NettyServer().startserver();
//        } catch (Exception ex) {    
//   
//            ex.printStackTrace();
//        
//        }
//
//    }
//
//    @PreDestroy
//    void destroy() {
//        try {
//            new NettyServer().Stopserver();
//        } catch (Exception ex) {          
//
//            ex.printStackTrace();
//      
//        }
//    }
//}
