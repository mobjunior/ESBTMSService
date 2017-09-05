/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.ekenya.tms.logsengine;

//import com.eclectics.esb.tms.jms.QueueWriter;
//import com.eclectics.esb.tms.utils.Configs;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SocketHandler;

/**
 *
 * @author julius
 */
public class TMSLog {

    private FileHandler fh = null;
    private String pathtologs = "C://TMS/logs/"; //"/var/log/esblogs/tmsmdb/";
    private SocketHandler sh = null;
    //for socket handler
    private String directory = "tmsmdb";

    private String filename = null;
    private String msg = null;
    private Logger logger = null;
    public HashMap<String, String> logmap;

    public TMSLog(String filename, String msg) {
        this.filename = filename;
        this.msg = msg;
    }

    public TMSLog(String filename, HashMap map) {
        this.filename = filename;
        logmap = new HashMap(map);
        if (logmap.containsKey("RawMessage")) {
            logmap.put("RawMessage", "");
        }
          //check for field2 and mask it
        if (logmap.containsKey("2")) {
            String Pan = logmap.get("2").toString();
            if (Pan.length() == 16) {
                String Maskedpart = Pan.substring(6, 12);
                Pan = Pan.replace(Maskedpart, "******");
                logmap.put("2", Pan);
            } else if (Pan.length() == 19) {
                String Maskedpart = Pan.substring(6, 15);
                Pan = Pan.replace(Maskedpart, "******");
                logmap.put("2", Pan);
            }
        }
      

        this.msg = logmap.toString();
    }

    public TMSLog(String msg) {
        this.filename = "exceptions";
        this.msg = msg;
    }

    public void logfile() {
        logger = Logger.getLogger("");
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        String daylog = format.format(new Date());
        try {
            fh = new FileHandler(createDailyDirectory() + filename + "%g.txt", 26000000, 20, true);
            fh.setFormatter(new EsbFormatter());
            logger.addHandler(fh);
            logger.setLevel(Level.FINE);
            logger.fine(msg);
            fh.close();
        } catch (Exception e) {
            System.out.println("AT ESB Class ESBLog{ log() }: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void log2() {
        logger = Logger.getLogger(filename);
        String finalmessage = "###" + directory + "$$$" + filename + "###" + msg;
        try {
            sh = new SocketHandler("192.168.114.211", 5050);
            sh.setFormatter(new EsbFormatter());
            logger.addHandler(sh);
            logger.setLevel(Level.FINE);
            logger.fine(finalmessage);
            sh.flush();
            sh.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public void log() {
//        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS");
//        String timestamp = format.format(new Date());
//        String finalmessage = "###" + directory + "$$$" + filename + "###" + timestamp + "::" + msg;
//        // QueueWriter qw = new QueueWriter("java:jboss/exported/jms/queue/ESBLogQueue");
//        QueueWriter qw = new QueueWriter(Configs.ESB_LOG_QUEUE);
//        qw.sendText(finalmessage, "");
//    }

    public String createDailyDirectory() {
        String Dailydirectory = "";
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        String daylog = format.format(new Date());
        Dailydirectory = pathtologs + daylog;
        new File(Dailydirectory).mkdir();
        return Dailydirectory + "/";
    }

}
