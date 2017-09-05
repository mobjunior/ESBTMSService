/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.ekenya.tms.utilities;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
//import javax.ejb.Singleton;
//import javax.ejb.Startup;

/**
 *
 * @author julius
 */
public class Configs {

    public static String JMS_FACTORY;
    public static String databaseContextURL;
    public static String PROVIDER_URL;
    public static String SECURITY_PRINCIPAL;
    public static String SECURITY_CREDENTIALS;
    public static String LOGS_PATH;
    public static String DataSource_EBANK;
    public static int ESBTimeout;
    public static String ESB_Request_Queue;
    public static String ESB_Response_Queue;
    public static String ESB_LOG_QUEUE;
    public static String ESB_SMS_TOPIC;
    public static String TMSWSUrl;

    public void loadConfigs() {
        try {

//            RedisConfigs configs = new RedisConfigs();
//            JMS_FACTORY = configs.getConfig("JMS_FACTORY");// "java:jboss/exported/jms/RemoteConnectionFactory";//
//            System.out.println("Loading Configs" + "\n\n");
//            //System.out.println(JMS_FACTORY + "\n");
//            setJMS_FACTORY(JMS_FACTORY);
//            PROVIDER_URL = configs.getConfig("PROVIDER_URL");//"remote://localhost:4447";//
//            // System.out.println(PROVIDER_URL + "\n");
//            setPROVIDER_URL(PROVIDER_URL);
//            SECURITY_PRINCIPAL = configs.getConfig("SECURITY_PRINCIPAL");//"admin";//
//            // System.out.println(SECURITY_PRINCIPAL + "\n");
//            setSECURITY_PRINCIPAL(SECURITY_PRINCIPAL);
//            SECURITY_CREDENTIALS = configs.getConfig("SECURITY_CREDENTIALS");// "pass@123";// 
//            // System.out.println(SECURITY_CREDENTIALS + "\n");
//            setSECURITY_CREDENTIALS(SECURITY_CREDENTIALS);
//            databaseContextURL = configs.getConfig("databaseContextURL");//"java:jboss/datasources/HUDUMAgency"; //
//            // databaseContextURL = "java:jboss/datasources/HUDUMAgency";
//            // System.out.println(databaseContextURL + "\n");
//            setDatabaseContextURL(databaseContextURL);
//            ESB_Request_Queue = configs.getConfig("ESB_REQUEST_QUEUE");//"java:jboss/exported/jms/queue/ESBRequest_Queue_DS";//
//            //System.out.println(ESB_Request_Queue + "\n");
//            setESB_Request_Queue(ESB_Request_Queue);
//            ESB_Response_Queue = configs.getConfig("ESB_RESPONSE_QUEUE");// "java:jboss/exported/jms/queue/ESBChannelResponse_Queue";//
//            //System.out.println(ESB_Response_Queue + "\n");
//            setESB_Response_Queue(ESB_Response_Queue);
//            ESB_LOG_QUEUE = configs.getConfig("ESB_LOG_QUEUE");//"java:jboss/exported/jms/queue/ESBLogQueue";//
//            setESB_LOG_QUEUE(ESB_LOG_QUEUE);
//            ESB_SMS_TOPIC = configs.getConfig("ESB_SMS_TOPIC");//"java:jboss/exported/jms/topic/ESBTransaction_Topic";//
//            setESB_SMS_TOPIC(ESB_SMS_TOPIC);
            // System.out.println(ESB_SMS_TOPIC);
            //TMSWSUrl = "https://testgateway.ekenya.co.ke:8443/wrappers/hudumaUssdWrapper.php"; //configs.getConfig("TMSWSUrl");//http://10.40.45.10:4111/ESBTMSHTTPServerV1.1/TMSTOESB
            TMSWSUrl = "http://10.20.2.23:8080/ESB_Huduma-war/HudumaServlet"; //configs.getConfig("TMSWSUrl");//http://10.40.45.10:4111/ESBTMSHTTPServerV1.1/TMSTOESB
            setTMSWSUrl(TMSWSUrl);
            System.out.println("Configs Loading Completed............................" + "\n\n");
            // LOGS_PATH = properties.getProperty("LOGS_PATH");
            ESBTimeout = 30;

        } catch (Exception ex) {
            System.err.print("ERROR: Failed to load properties file.\nCause: " + ex.getMessage());
            Logger.getLogger(Props.class.getName()).log(Level.SEVERE, "ERROR: Failed to load properties file.\nCause: \n", ex);
        }
    }

    public String getDatabaseContextURL() {
        return databaseContextURL;
    }

    public void setDatabaseContextURL(String databaseContextURL) {
        this.databaseContextURL = databaseContextURL;
    }

    public String getJMS_FACTORY() {
        return JMS_FACTORY;
    }

    public void setJMS_FACTORY(String JMS_FACTORY) {
        this.JMS_FACTORY = JMS_FACTORY;
    }

    public String getPROVIDER_URL() {
        return PROVIDER_URL;
    }

    public void setPROVIDER_URL(String PROVIDER_URL) {
        this.PROVIDER_URL = PROVIDER_URL;
    }

    public String getSECURITY_PRINCIPAL() {
        return SECURITY_PRINCIPAL;
    }

    public void setSECURITY_PRINCIPAL(String SECURITY_PRINCIPAL) {
        this.SECURITY_PRINCIPAL = SECURITY_PRINCIPAL;
    }

    public String getSECURITY_CREDENTIALS() {
        return SECURITY_CREDENTIALS;
    }

    public void setSECURITY_CREDENTIALS(String SECURITY_CREDENTIALS) {
        this.SECURITY_CREDENTIALS = SECURITY_CREDENTIALS;
    }

    public String getLOGS_PATH() {
        return LOGS_PATH;
    }

    public void setLOGS_PATH(String LOGS_PATH) {
        this.LOGS_PATH = LOGS_PATH;
    }

    public String getESB_Request_Queue() {
        return ESB_Request_Queue;
    }

    public void setESB_Request_Queue(String ESB_Request_Queue) {
        this.ESB_Request_Queue = ESB_Request_Queue;
    }

    public String getESB_Response_Queue() {
        return ESB_Response_Queue;
    }

    public void setESB_Response_Queue(String ESB_Response_Queue) {
        this.ESB_Response_Queue = ESB_Response_Queue;
    }

    public String getESB_LOG_QUEUE() {
        return ESB_LOG_QUEUE;
    }

    public void setESB_LOG_QUEUE(String ESB_LOG_QUEUE) {
        this.ESB_LOG_QUEUE = ESB_LOG_QUEUE;
    }

    public String getESB_SMS_TOPIC() {
        return ESB_SMS_TOPIC;
    }

    public void setESB_SMS_TOPIC(String ESB_SMS_TOPIC) {
        this.ESB_SMS_TOPIC = ESB_SMS_TOPIC;
    }
    
     public String getTMSWSUrl() {
        return TMSWSUrl;
    }

    public void setTMSWSUrl(String TMSWSUrl) {
        this.TMSWSUrl = TMSWSUrl;
    }
    

}
