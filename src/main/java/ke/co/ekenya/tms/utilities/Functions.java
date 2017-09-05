/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.ekenya.tms.utilities;

import ke.co.ekenya.tms.logsengine.TMSLog;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
//import java.nio.ByteBuffer;
//import java.nio.channels.FileChannel;
//import java.nio.channels.FileLock;
//import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
//import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.ObjectMessage;
import ke.co.ekenya.tms.processor.NettyServerHandler;
import ke.co.ekenya.tms.servlet.TMSWSCall;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.PrintWriter;
//import java.util.Base64;
//import org.apache.commons.codec.*;
import java.util.concurrent.TimeUnit;
import java.util.Base64;
//import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author julius
 */
public class Functions {

    private Database database;
    private FileHandler fh = null;
    private Logger logger = null;
    private static final char DEFAULT_TRIM_WHITESPACE = ' ';
    private static Props props = new Props();
    NettyServerHandler sock = new NettyServerHandler();
    Database connections;
    //private ReadFromQueue readFromQueue;
    Util generatePINBlock = new Util();

    //private Configs configs;
    public static StringBuffer decrypt(String cipher, int key) {
        int i;
        char swap = '0';
        char[] cipherChar = cipher.toCharArray();
        StringBuffer plainText = new StringBuffer("");
        int ciphersize = cipher.length();
        System.out.println(ciphersize);
        for (i = 0; i < ciphersize; i++) {
            cipherChar[i] = (char) (cipherChar[i] + key);
            //plainText.append(cipherChar[i]);
        }
        int sentinel;
        if (ciphersize % 2 == 0) {
            sentinel = ciphersize;
        } else {
            sentinel = ciphersize - 2;
        }
        for (i = 0; i < sentinel; i++) {
            swap = cipherChar[i];
            cipherChar[i] = cipherChar[i + 1];
            cipherChar[i + 1] = swap;
            plainText.append(cipherChar[i]);
            plainText.append(cipherChar[i + 1]);
            i++;
        }
        if (ciphersize % 2 != 0) {
            plainText.append(cipherChar[i]);
        }
        return plainText;
    }
//    public StringBuffer decrypt(String cipher, int key) {
//        int i;
//        char swap = '0';
//        char[] cipherChar = cipher.toCharArray();
//        StringBuffer plainText = new StringBuffer("");
//        for (i = 0; i < cipher.length(); ++i) {
//            cipherChar[i] = (char) (cipherChar[i] + key);
//            //plainText.append(cipherChar[i]);
//        }
//        for (i = 0; i < cipher.length(); ++i) {
//            swap = cipherChar[i];
//            cipherChar[i] = cipherChar[i + 1];
//            cipherChar[i + 1] = swap;
//            plainText.append(cipherChar[i]);
//            plainText.append(cipherChar[i + 1]);
//            i++;
//        }
//
//        return plainText;
//    }

    public static String StackTraceWriter(Exception exception) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        exception.printStackTrace(printWriter);
        String s = writer.toString();
        return s;
    }
//
//    private void getDBConnection() {
//        try {
//            this.database = new Database(props);
//        } catch (Exception ex) {
//            //logging.applicationLog(Utilities.logPreString() + Utilities.logPreString()
//            //        + "Database connection failed. Review: "
//            //      + "SQL Connectivity: " + ex.getMessage(), "", "1");
//        }
//    }

    public static String trimLeftString(String string) {
        return trimLeft(string, DEFAULT_TRIM_WHITESPACE);
    }

    /**
     * Trims the character given from the given string and returns the result.
     *
     * @param string The string to trim, cannot be null.
     * @param trimChar The character to trim from the left of the given string.
     * @return A string with the given character trimmed from the string given.
     */
    public static String trimLeft(final String string, final char trimChar) {
        final int stringLength = string.length();
        int i;

        for (i = 0; i < stringLength && string.charAt(i) == trimChar; i++) {
            /* increment i until it is at the location of the first char that
             * does not match the trimChar given. */
        }

        if (i == 0) {
            return string;
        } else {
            return string.substring(i);
        }
    }

    public String anyDate(String format) {
        try {
            if ("".equals(format)) {
                // format = "yyyy-MM-dd HH:mm:ss"; // default
                format = "yyyy/MM/dd HH:mm:ss"; // default
            }
            java.util.Date today;
            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat(format);
            today = new java.util.Date();
            return (formatter.format(today));
        } catch (Exception ex) {
            // this.log("\nINFO anyDate() :: " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
        }
        return "";
    }

    public String ddmmyy() {
        //Creating Date in java with today's date.
        java.util.Date dateNow = new java.util.Date();

//converting  date into ddMMyyyy format example "14092011"
        SimpleDateFormat dateformatddMMyyyy = new SimpleDateFormat("ddMMyyyy");
        String date_to_string = dateformatddMMyyyy.format(dateNow);
        return date_to_string;
    }

    public String MMyyhhmmss() {
        //Creating Date in java with today's date.
        java.util.Date dateNow = new java.util.Date();

        //converting  date into ddMMyyyy format example "14092011"
        SimpleDateFormat dateformatddMMyyyy = new SimpleDateFormat("MMddhhmmss");
        String date_to_string = dateformatddMMyyyy.format(dateNow);
        return date_to_string;
    }

    public String DateTime() {
        java.util.Date today = new java.util.Date();

        //Date format
        DateFormat df = DateFormat.getDateTimeInstance();                              //Date and time
        String strDate = df.format(today);
        return strDate;
    }

    public String hhmmss() {
        //Creating Date in java with today's date.
        java.util.Date dateNow = new java.util.Date();

        SimpleDateFormat dateformatddMMyyyy = new SimpleDateFormat("hhmmss");
        String date_to_string = dateformatddMMyyyy.format(dateNow);
        return date_to_string;
    }

    public String yymmdd(java.util.Date date) {
        SimpleDateFormat dateformatddMMyyyy = new SimpleDateFormat("yyMMdd");
        String date_to_string = dateformatddMMyyyy.format(date);
        return date_to_string;
    }

    public String strResponseFooter(String strAgentID) throws SQLException {
        String strResponse = "";

        try {
            connections = new Database(props);
            if (strAgentID != null && !strAgentID.isEmpty()) {
                //String strLogin = "SELECT AGENTNAMES FROM TBAGENTS WHERE AGENTID ='" + strAgentID + "'";
                String strLogin = "SELECT AGENTNAMES AS AGENTNAMES FROM TBAGENTS WHERE DEVICE1ID='" + strAgentID + "'";
                String AgentName = connections.ExecuteQueryStringValue(strLogin, "", "AGENTNAMES");
                strResponse += "------------------------------" + "#";
                strResponse += "You were served by:" + stringBuilder(AgentName) + "#";
            }

            strResponse += "------------------------------" + "#";
            strResponse += "THANK YOU FOR USING HUDUMA" + "#";
            strResponse += " AGENCY HELPDESK: +254 (020)6900020" + "#";
            strResponse += " " + "#";
            strResponse += " " + "#";
            strResponse += " " + "#";
            strResponse += " " + "#";
            strResponse += " " + "#";
            strResponse += " " + "#";

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
        }
        return strResponse;
    }
//

//    //encrypt PIN function
    public String encryptPin(String username, String plainPassword, String strKey) {
        String plainString = username + plainPassword;
        // byte[] byteArray = Base64.encodeBase64(plainString.getBytes());
        byte[] byteArray = Base64.getEncoder().encode(plainString.getBytes());
        String encodedString = new String(byteArray);
        String HMAC_SHA512 = "HmacSHA512";
        String DEFAULT_ENCODING = "UTF-8";
        byte[] result = null;

        //Hash Algorithm
        try {
            SecretKeySpec keySpec = new SecretKeySpec(strKey.getBytes(DEFAULT_ENCODING), HMAC_SHA512);
            Mac mac = Mac.getInstance(HMAC_SHA512);
            mac.init(keySpec);
            result = mac.doFinal(encodedString.getBytes(DEFAULT_ENCODING));

        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
        }

        StringBuilder sb = new StringBuilder();
        for (byte b : result) {
            sb.append(String.format("%02X", b));
        }

        return sb.toString();
    }

    public String HmacHash(String password, String key, String encodingMAC) {
        //Sample HmacSHA512,HmacSHA256
        Mac enctype = null;
        String result = null;
        try {
            enctype = Mac.getInstance(encodingMAC);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), encodingMAC);
            enctype.init(keySpec);
            byte[] mac_data = enctype.doFinal(password.getBytes("UTF-8"));
            result = convertToHex(mac_data);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public String convertToHex(byte[] raw) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < raw.length; i++) {
            stringBuilder.append(Integer.toString((raw[i] & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuilder.toString();
    }

//    public void updateTrials(String username, String deviceID) {
//        String Query = "";
//        String strTrials = "";
//        String strResponse = "";
//        String strField39 = "";
//        String strField48 = "";
//        boolean Success = true;
//        try {
//
//            Query = "SELECT A.TRIALS   FROM TBAGENTDEVICELINKING A INNER JOIN ";
//            Query += " TBAGENTS B ON A.Agentcode = b.agentid WHERE A.AGENTID ='" + username + "' AND  A.DEVICEIMEI ='" + deviceID + "'";
//
//            strTrials = connections.ExecuteQueryStringValue(ECSERVER, Query, ECPASSWORD, ECUSER, ECDB, ECPORT, "", "TRIALS");
//            int trial = Integer.parseInt(strTrials) + 1;
//
//            if (trial >= 3) {
//                String strInMsg = "UPDATE TBAGENTDEVICELINKING SET TRIALS='" + trial + "' , ACTIVE ='0'  WHERE DEVICEIMEI ='" + deviceID + "'";
//                Success = connections.ExecuteUpdate(ECSERVER, strInMsg, ECPASSWORD, ECUSER, ECDB);
//            } else {
//                String strInMsg = "UPDATE TBAGENTDEVICELINKING SET TRIALS='" + trial + "'  WHERE DEVICEIMEI ='" + deviceID + "'";
//                Success = connections.ExecuteUpdate(ECSERVER, strInMsg, ECPASSWORD, ECUSER, ECDB);
//            }
//
//        } catch (Exception ex) {
//            this.log("INFO updateTrials()  :: " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
//        }
//
//    }
//    public void clearTrials(String deviceID) {
//        String strInMsg = "UPDATE TBAGENTDEVICES SET TRIALS ='0', ACTIVE ='1'  WHERE DEVICEIMEI ='" + deviceID + "'";
//        connections.ExecuteUpdate(ECSERVER, strInMsg, ECPASSWORD, ECUSER, ECDB);
//    }
    public String fn_verify_login_details(String strUsername, String strPassword, String deviceID) {
        String strStatus = "";
        String strUserType = "";
        String MyPassword = "";
        String strLogin = "";

        try {
            String joinedPwd = strPassword + strUsername;
            String pwd = Base64.getEncoder().encodeToString(joinedPwd.getBytes());
            MyPassword = this.HmacHash(pwd, "secret", "HmacSHA256"); //this.encryptPin(strUsername, strPassword, "secret");
            connections = new Database(props);
            //strLogin = "SELECT A.ACTIVE || '|' ||B.AGENTTYPE || '|' || A.TRIALS || '|' || A.LOGGEDIN || '|' || A.FIRSTLOGIN  || '|' || B.ACTIVE  AS PROFILE ";
            //strLogin += " FROM TBAGENTDEVICES A INNER JOIN TBAGENTS B ON A.agentid = b.AGENTID WHERE B.AGENTID ='" + strUsername + "' ";
            //strLogin += " AND A.PIN ='" + MyPassword.toLowerCase() + "'  AND A.DEVICEIMEI ='" + deviceID + "'";
            //strLogin = "SELECT A.ACTIVE || '|'  || A.TRIALS || '|' ||  A.FIRSTLOGIN  || '|' || B.ACTIVE || '|' || B.AGENTTYPE AS PROFILE  FROM TBAGENTDEVICELINKING A INNER JOIN ";
            //strLogin += " TBAGENTS B ON A.Agentcode = b.agentid WHERE A.AGENTID ='" + strUsername + "' AND A.PIN ='" + MyPassword.toLowerCase() + "' AND  A.DEVICEIMEI ='" + deviceID + "'";
            //strLogin = "select ACTIVE from TBAGENTDEVICES where AGENTCODE='" + strUsername + "' AND PIN='" + MyPassword.toLowerCase() + "'";
            strLogin = "SELECT ACTIVE,FIRSTLOGIN,PIN FROM TBAGENTS WHERE AGENTID = '" + strUsername + "' AND PIN = '" + MyPassword.toLowerCase() + "'";
            strStatus = connections.ExecuteQueryStringValues(strLogin, "", "", "ACTIVE", "FIRSTLOGIN");

            if (strStatus == "" || strStatus == null || strStatus.isEmpty() || strStatus == "|") {
                strStatus = "0|0";
                //updateTrials(strUsername, deviceID);
            }

            // System.out.println(strStatus + "\n\n" + strLogin);
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();

        }

        return strStatus;
    }
//    public String fn_verify_device(String deviceID) {
//        String strStatus = "";
//        String strUserType = "";
//        String MyPassword = "";
//        String strLogin = "";
//
//        try {
//
//            strLogin = "SELECT ACTIVE  AS ACTIVE  FROM TBAGENTDEVICES WHERE DEVICEIMEI ='" + deviceID + "'";
//
//            strStatus = connections.ExecuteQueryStringValue(ECSERVER, strLogin, ECPASSWORD, ECUSER, ECDB, ECPORT, "", "ACTIVE");
//
//            System.out.println(strStatus + "\n\n" + strLogin);
//        } catch (Exception ex) {
//            this.log("INFO fn_verify_device()  :: " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
//        }
//
//        return strStatus;
//    }

    public String PadZeros(int width, String value) {

        String z = "";
        for (int i = 0; i < width - value.length(); i++) {
            z += "0";
        }
        return z + value;
    }

    public String PadRightZeros(int width, String value) {

        String z = "";
        for (int i = 0; i < width - value.length(); i++) {
            z += "0";
        }
        return value + z;
    }

    public String PadSpaces(int width, String value, String bool) {

        String z = "";
        String result = "";
        for (int i = 0; i < width - value.length(); i++) {
            z += " ";
        }
        if (bool.equalsIgnoreCase("LEFT")) {
            result = z + value;
        } else if (bool.equalsIgnoreCase("RIGHT")) {
            result = value + z;
        }
        return result;
    }

    /**
     * pad to the left
     *
     * @param s - original string
     * @param len - desired len
     * @param c - padding char
     * @return padded string
     */
    public static String padleft(String s, int len, char c)
            throws Exception {
        s = s.trim();
        if (s.length() > len) {
            throw new Exception("invalid len " + s.length() + "/" + len);
        }
        StringBuffer d = new StringBuffer(len);
        int fill = len - s.length();
        while (fill-- > 0) {
            d.append(c);
        }
        d.append(s);
        return d.toString();
    }

    /**
     * pad to the right
     *
     * @param s - original string
     * @param len - desired len
     * @param c - padding char
     * @return padded string
     */
    public static String padright(String s, int len, char c) throws Exception {
        s = s.trim();
        if (s.length() > len) {
            throw new Exception("invalid len " + s.length() + "/" + len);
        }
        StringBuffer d = new StringBuffer(len);
        int fill = len - s.length();
        d.append(s);
        while (fill-- > 0) {
            d.append(c);
        }
        return d.toString();
    }

//    public boolean fn_terminal_allowed(String strTerminalSerialNumber) {
//        try {
//
//            String str_query = "select ASSIGNED from tbagentdevices where deviceIMEI ='" + strTerminalSerialNumber.trim() + "'";
//            String blnActive = connections.ExecuteQueryStringValue(ECSERVER, str_query, ECPASSWORD, ECUSER, ECDB, ECPORT, "", "ASSIGNED");
//
//            switch (blnActive) {
//                case "True":
//                case "1":
//                    return true;
//                default:
//                    return false;
//            }
//        } catch (Exception ex) {
//            this.log("Error on Function fn_terminal_allowed" + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "fn_terminal_allowed");
//            return false;
//        }
//    }
//    public String fn_getAgentLocation(String strAgentID) {
//        String strLocation = "";
//        String strLogin = "";
//        try {
//            strLogin = "SELECT ADDRESS FROM TBAGENTREGISTRATION WHERE AGENTNO ='" + strAgentID + "'";
//            strLocation = connections.ExecuteQueryStringValue(ECSERVER, strLogin, ECPASSWORD, ECUSER, ECDB, ECPORT, "", "ADDRESS");
//            return strLocation;
//        } catch (Exception ex) {
//            this.log("\nINFO fn_getAgentLocation() :: " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
//            return "";
//        }
//
//    }
//    public String getSetting(String strParam) {
//        String schoolid = "";
//        String SQL = "";
//
//        try {
//            SQL = "select ITEMVALUE FROM tbGENERALPARAMS WHERE ITEMNAME='" + strParam + "'";
//            schoolid = connections.ExecuteQueryStringValue(ECSERVER, SQL, ECPASSWORD, ECUSER, ECDB, ECPORT, "", "ITEMVALUE");
//            return schoolid;
//        } catch (Exception ex) {
//           // this.log("\nINFO getSetting() :: " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
//            return "";
//        }
//    }
    public String fn_RemoveNon_Numeric(String strExpression) {

        String strtocheck = strExpression.trim();

        char[] myChars = strtocheck.toCharArray();
        String strNumeic = "";

        try {
            // Loop through the array testing if each is a digit

            for (char ch : myChars) {
                if (Character.isDigit(ch)) {
                    strNumeic = strNumeic + ch;
                }
            }
            return strNumeic;
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
            return "";
        }
    }

    public String SendPOSResponse(String outmessage, String intid) {

        System.out.println("[SENT: Message Sent To POS                -:]" + intid + "\n" + outmessage);
        try {

            TMSLog el = new TMSLog("MessageToPOS", "Message To POS :: \n " + outmessage);
            el.logfile();
            if (sock.isConnected) {
                sock.ctxr.write((Object) outmessage);
                //sock.ctxr.close();
            } else {
                TMSLog ell = new TMSLog("MessageToPOS_NOTSENT", "Message To POS :: \n " + outmessage);
                ell.logfile();
                // this.log("Error on function SendPOSResponse could not connect to Host ", "MessageToPOS");
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
        }
        return null;
    }
//

    public String fn_getBankSortcode(String BankBin) {
        String strLogin = "";

        try {
            Database connections = new Database(props);

            strLogin = "select BANKCODE from TBBANKBIN where bin='" + BankBin + "'";

            String rsReader = connections.ExecuteQueryStringValue(strLogin, "", "BANKCODE");;
            return rsReader;
        } catch (Exception ex) {
            //this.log("\nINFO fn_getAgentAccountNumber() :: " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
            return "";
        }
    }

    public String fn_getAgentAccountNumber(String strAgentID) {
        String strLogin = "";

        try {
            connections = new Database(props);
            //strLogin = "SELECT B.ACCOUNTNO AS ACCOUNT  FROM TBAGENTS A, TBACCOUNT B, TBAGENTDEVICELINKING C WHERE A.AGENTID=C.AGENTCODE AND B.PHONENUMBER=C.DEVICEMOBILE AND C.APPROVED = 1 AND B.MEMOCODE = '201' AND B.BLOCKED='N' AND C.AGENTID ='" + strAgentID.trim() + "'";
            strLogin = "SELECT FLOATACCOUNT AS ACCOUNT  FROM TBAGENTS WHERE AGENTID = '" + strAgentID.trim() + "'";

            String rsReader = connections.ExecuteQueryStringValue(strLogin, "", "ACCOUNT");;
            return rsReader;
        } catch (Exception ex) {
            //this.log("\nINFO fn_getAgentAccountNumber() :: " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
            return "";
        }
    }

    public String fn_getAgentCommisionAccountNumber(String strAgentID) {
        String strLogin = "";

        try {
            connections = new Database(props);
            //strLogin = "SELECT B.ACCOUNTNO AS ACCOUNT  FROM TBAGENTS A, TBACCOUNT B, TBAGENTDEVICELINKING C WHERE A.AGENTID=C.AGENTCODE AND B.PHONENUMBER=C.DEVICEMOBILE AND C.APPROVED = 1 AND B.MEMOCODE = '201' AND B.BLOCKED='N' AND C.AGENTID ='" + strAgentID.trim() + "'";
            strLogin = "SELECT COMMISSIONACCOUNT AS COMMISSIONACCOUNT  FROM TBAGENTS WHERE AGENTID = '" + strAgentID + "'";

            String rsReader = connections.ExecuteQueryStringValue(strLogin, "", "COMMISSIONACCOUNT");;
            return rsReader;
        } catch (Exception ex) {
            //this.log("\nINFO fn_getAgentAccountNumber() :: " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
            return "";
        }
    }

    public String getAgentBankVirtualpan(String BankSortcode) {
        String strLogin = "";

        try {
            Database connections = new Database(props);

            strLogin = "select  VIRTUALPAN from TBBANKS  WHERE BANK_CODE ='" + BankSortcode + "'";

            String rsReader = connections.ExecuteQueryStringValue(strLogin, "", "VIRTUALPAN");;
            return rsReader;
        } catch (Exception ex) {
            //this.log("\nINFO fn_getAgentAccountNumber() :: " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
            return "";
        }
    }

    public String fn_getAgentBankVirtualpan(String strAgentID) {
        String strLogin = "";

        try {
            Database connections = new Database(props);

            strLogin = "select  A.VIRTUALPAN from TBBANKS A INNER JOIN TBAGENTDEVICES B ON A.BANK_CODE = B.BANK_CODE WHERE B.AGENTCODE ='" + strAgentID + "'";

            String rsReader = connections.ExecuteQueryStringValue(strLogin, "", "VIRTUALPAN");
            return rsReader;
        } catch (Exception ex) {
            //this.log("\nINFO fn_getAgentAccountNumber() :: " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
            return "";
        }
    }
//
//    public String getMerchantAccount(String strAgentID) {
//        String strLogin = "";
//
//        try {
//            strLogin = "SELECT ACCOUNTNUMBER FROM TBMERCHANTS WHERE AGENTID ='" + strAgentID + "'";
//            String rsReader = connections.ExecuteQueryStringValue(ECSERVER, strLogin, ECPASSWORD, ECUSER, ECDB, ECPORT, "", "ACCOUNTNUMBER");;
//            return rsReader;
//        } catch (Exception ex) {
//            this.log("\nINFO getMerchantAccount() :: " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
//            return "";
//        }
//    }
//    public int createStan() {
//        int x = 0;
//
//        String filename = COUNT_FILE;
//        File inwrite = new File(filename);
//
//        // Get a file channel for the file
//        try {
//            FileChannel channel = new RandomAccessFile(inwrite, "rw").getChannel();
//            // Use the file channel to create a lock on the file.
//            // This method blocks until it can retrieve the lock.
//            FileLock lock = channel.lock();
//            //  if(!inwrite.exists()) {
//            String s = "";
//            try {
//                int fileSize = (int) channel.size();
//                //    System.out.println("int is" + fileSize);
//                ByteBuffer bafa = ByteBuffer.allocate(fileSize);
//                int numRead = 0;
//                while (numRead >= 0) {
//                    numRead = channel.read(bafa);
//                    bafa.rewind();
//                    for (int i = 0; i < numRead; i++) {
//                        int b = (int) bafa.get();
//                        char c = (char) b;
//                        s = s + c;
//                    }
//                }
//
//                x = Integer.parseInt(s);
//                if (x > 999999) {
//                    x = 100000;
//                } else if (x < 100000) {
//                    x = 100000;
//                }
//                x = ++x;
//                String xx = String.valueOf(x);
//                byte[] yy = xx.getBytes();
//                channel.truncate(0);
//                channel.write(ByteBuffer.wrap(yy));
//                // channel.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            lock.release();
//            // Close the file
//            channel.close();
//        } catch (FileNotFoundException e) {
//            String message = "The file " + inwrite.getName() + " does not exist. So no input can be written on it";
//            System.out.println(message);
//            e.printStackTrace();
//            //log to error file
//        } catch (IOException e) {
//            System.out.println("Problem writing to the logfile " + inwrite.getName());
//
//        }
//
//        filename = "";
//        return x;
//    } // end fn createstan

    public String generateCorelationID() {
        int min = 100000000;
        int max = 999999999;
        Random randomGenerator = new Random();
        String CorelationID = "" + (randomGenerator.nextInt(max - min + 1) + min);

        int rand = 100000 + (int) (Math.random() * ((999999999 - 100000000) + 1));
        String intid = CorelationID;

        return intid;
    }

    public String generateID() {
        int min = 100000;
        int max = 999999;
        Random randomGenerator = new Random();
        String CorelationID = "" + (randomGenerator.nextInt(max - min + 1) + min);//+""+(randomGenerator.nextInt(max - min + 1) + min);

        int rand = 100000 + (int) (Math.random() * ((999999 - 100000) + 1));
        String intid = CorelationID;

        return intid;
    }

    public String padEqual(String str) {
        int length;
        int eitherLength;
        int spaces;
        String response = "";
        length = str.length();

        if (length <= 32) {
            spaces = 32 - length;
            if ((spaces % 2) == 0) {
                eitherLength = (spaces / 2) + length;
                response = PadSpaces(eitherLength, str.toUpperCase(), "LEFT");
            } else {
                eitherLength = (spaces - 1) / 2;

                eitherLength = ((spaces - 1) / 2) + length;
                response = PadSpaces(eitherLength, str.toUpperCase(), "LEFT");
            }

        } else {

        }
        return response;
    }

    public String strResponseHeader(String strTerminalid) throws SQLException {

        try {
            String strResponse = "";
            String strLogin = "";
            String strAgentName = "";
            String strAgentLocation = "";
            String strAgentNumber = "";
            String TerminalID = "";
            String str = "";
            String deviceInfo[];

            connections = new Database(props);
            // strLogin = "SELECT A.AGENTNAMES || '|' || A.LOCATION || '|' || A.AGENTID || '|' || B.DEVICEIMEI|| '|' || B.DEVICEIMEI AS DEVICEINFO  FROM TBAGENTS A INNER JOIN TBAGENTDEVICES B ON A.AGENTID = B.AGENTID ";
            // strLogin += " WHERE B.DEVICEIMEI='" + strTerminalid + "'";
            strLogin = "SELECT COALESCE(AGENTNAMES,'') + '|' + COALESCE(LOCATION,'')+ '|'+ COALESCE(DEVICE1ID,'') AS DEVICEINFO ";
            strLogin += "FROM TBAGENTS WHERE DEVICE1ID='" + strTerminalid + "'";
            str = connections.ExecuteQueryStringValue(strLogin, "", "DEVICEINFO");

            if (!str.isEmpty()) {
                deviceInfo = str.split("\\|");
                strAgentName = deviceInfo[0];
                strAgentLocation = deviceInfo[1];
                //strAgentNumber = deviceInfo[2];
                TerminalID = deviceInfo[2];
            }
            String strTime = this.anyDate("HH:mm");
            String strDate = this.anyDate("dd/MM/yyyy");
            strResponse = "      HUDUMA AGENCY       " + "#";
            strResponse += padEqual(strAgentLocation) + "#";
            strResponse += " " + "#";
            strResponse += " DATE       TIME    TERMINAL ID  " + "#";
            strResponse += strDate + " " + strTime + " " + TerminalID + "#";
            strResponse += "                               " + "#";
            return strResponse;
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
            return "";
        } finally {
            //connections.con.close();
        }
    }
//

    public String spInsertCustomerphonenumbers(String uniqueID, String phoneNumber, String accountNumber, String bankCode, String agentCode) {
        String response = "";
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        long RecordIndex = 0;
        String SP_SQL = "{CALL SP_InsertCustomerPhoneNumber(?,?,?,?,?,?)}";

        try {
            Database connections = new Database(props);

//            if (database.getDatabaseConnection() == null) {
//                database.getDatabaseConnection();
//            } else if (database.getDatabaseConnection().isClosed()) {
//                database.getDatabaseConnection();
//            }
            connection = connections.getDatabaseConnection();

            statement = connection.prepareCall(SP_SQL);
            statement.registerOutParameter(1, java.sql.Types.INTEGER);
            statement.setString(2, uniqueID);// uniqueID
            statement.setString(3, phoneNumber);// phoneNumber
            statement.setString(4, accountNumber);// accountNumber
            statement.setString(5, bankCode);// bankCode
            statement.setString(6, agentCode);// agentCode  	

            //resultSet = statement.executeQuery();
            statement.execute();
            RecordIndex = statement.getInt(1);
            response = String.valueOf(RecordIndex);

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
        } finally {
            try {
                //resultSet.close();
                // statement.close();
                connection.close();
                database.closeDatabaseConnection();
            } catch (Exception ex) {
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                TMSLog el = new TMSLog(sw.toString());
                el.logfile();
            }
        }

        return response;
    }

    //
    public String spInsertPOSTransaction(HashMap<String, String> Map) {
        String response = "";
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        long RecordIndex = 0;
        String SP_SQL = "{CALL SP_IncomingPOSTransactions(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        try {
            connections = new Database(props);

            if (database.getDatabaseConnection() == null) {
                database.getDatabaseConnection();
            } else if (database.getDatabaseConnection().isClosed()) {
                database.getDatabaseConnection();
            }
            connection = connections.getDatabaseConnection();

            statement = connection.prepareCall(SP_SQL);
            statement.registerOutParameter(1, java.sql.Types.INTEGER);
            statement.setString(2, (Map.containsKey("field0")) ? Map.get("field0") : "");// field0
            statement.setString(3, (Map.containsKey("field2")) ? Map.get("field2") : "");// field_2
            statement.setString(4, (Map.containsKey("field3")) ? Map.get("field3") : "");// field_3
            statement.setLong(5, (Map.containsKey("field4")) ? Long.parseLong(Map.get("field4")) : 0);// field_4
            statement.setString(6, (Map.containsKey("field7")) ? Map.get("field7") : "");// field_7
            statement.setString(7, (Map.containsKey("field11")) ? Map.get("field11") : "");// field_11
            statement.setString(8, (Map.containsKey("field12")) ? Map.get("field12") : "");// field_12
            statement.setString(9, (Map.containsKey("field24")) ? Map.get("field24") : "");// field_24
            statement.setString(10, (Map.containsKey("field32")) ? Map.get("field32") : "");// field_32
            statement.setString(11, (Map.containsKey("field37")) ? Map.get("field37") : "");// field_37
            statement.setString(12, (Map.containsKey("field39")) ? Map.get("field39") : "");// field_39
            statement.setString(13, (Map.containsKey("field41")) ? Map.get("field41") : "");// field_41
            statement.setString(14, (Map.containsKey("field48")) ? Map.get("field48") : "");// field_48
            statement.setString(15, (Map.containsKey("field56")) ? Map.get("field56") : "");// field_56
            statement.setString(16, (Map.containsKey("field65")) ? Map.get("field65") : "");// field_65
            statement.setString(17, (Map.containsKey("field66")) ? Map.get("field66") : "");// field_66
            statement.setString(18, (Map.containsKey("field68")) ? Map.get("field68") : "");// field_68
            statement.setString(19, (Map.containsKey("field80")) ? Map.get("field80") : "");// field_80
            statement.setString(20, (Map.containsKey("field100")) ? Map.get("field100") : "");// field_100
            statement.setString(21, (Map.containsKey("field101")) ? Map.get("field101") : "");// field_101
            statement.setString(22, (Map.containsKey("field102")) ? Map.get("field102") : "");// field_102
            statement.setString(23, (Map.containsKey("field103")) ? Map.get("field103") : "");// field_103
            statement.setString(24, (Map.containsKey("field127")) ? Map.get("field127") : "");// field_127
            statement.setString(25, (Map.containsKey("strRequesttoEconnect")) ? Map.get("strRequesttoEconnect") : "");// strRequesttoEconnect
            statement.setString(26, (Map.containsKey("strResponseFromEconnect")) ? Map.get("strResponseFromEconnect") : "");// strResponseFromEconnect
            statement.setString(27, (Map.containsKey("pos_receipt")) ? Map.get("pos_receipt") : "");// pos_receipt	

            //resultSet = statement.executeQuery();
            statement.execute();
            RecordIndex = statement.getInt(1);
            response = String.valueOf(RecordIndex);
//            while (resultSet.next()) {
//                response = resultSet.getString(1);
//            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
        } finally {
            try {
                //resultSet.close();
                //statement.close();
                connection.close();
                database.closeDatabaseConnection();
            } catch (Exception ex) {
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                TMSLog el = new TMSLog(sw.toString());
                el.logfile();
            }
        }

        return response;
    }
//
//    public String strResponseHeader2(String strAgentID) {
//
//        try {
//            String strResponse = "";
//            String strLogin = "";
//            String strAgentName = "";
//            String strAgentLocation = "";
//            String strAgentNumber = "";
//            String TerminalID = "";
//
//            strLogin = "SELECT A.AGENTNAMES,A.LOCATION,A.AGENTID,B.TERMINALID,B.DEVICEIMEI FROM TBAGENTS A INNER JOIN TBAGENTDEVICES B ON A.AGENTID = B.AGENTID ";
//            strLogin += " WHERE A.AGENTID='" + strAgentID + "'";
//            ResultSet rslt = connections.ExecuteQueryReturnString(ECSERVER, strLogin, ECPASSWORD, ECUSER, ECDB);
//
//            while (rslt.next()) {
//                strAgentName = rslt.getString("AGENTNAMES").toString();
//                strAgentLocation = rslt.getString("LOCATION").toString();
//                strAgentNumber = rslt.getString("AGENTID").toString();
//                TerminalID = rslt.getString("TERMINALID").toString();
//            }
//
//            String strTime = this.anyDate("HH:mm");
//            String strDate = this.anyDate("dd/MM/yyyy");
//            strResponse = "       CONSOLIDATED BANK                " + "#";
//            strResponse += "   " + strAgentLocation.toUpperCase() + "  BR#";
//            strResponse += " " + "#";
//            strResponse += " DATE       TIME    POS SERIAL  " + "#";
//            strResponse += strDate + " " + strTime + " " + TerminalID + "#";
//            strResponse += "                               " + "#";
//            return strResponse;
//        } catch (Exception ex) {
//            this.log("INFO strResponseHeader2() :: " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
//            return "";
//        }
//    }
//

    public int spInsertPOSTransactions(Map<String, String> Fields) {
        Connection connection = null;
        int affectedRows = 0;
        try {

            connections = new Database(props);

//            if (database.getDatabaseConnection() == null) {
//                database.getDatabaseConnection();
//            } else if (database.getDatabaseConnection().isClosed()) {
//                database.getDatabaseConnection();
//            }
//            connection = connections.getDatabaseConnection();
            String Field_0 = Fields.get("field0") == null ? "" : Fields.get("field0");
            String Field_2 = Fields.get("field2") == null ? "" : Fields.get("field2");
            String Field_3 = Fields.get("field3") == null ? "" : Fields.get("field3");
            String Field_4 = Fields.get("field4") == null ? "" : Fields.get("field4");
            String Field_7 = Fields.get("field7") == null ? "" : Fields.get("field7");
            String Field_11 = Fields.get("field11") == null ? "" : Fields.get("field11");
            String Field_32 = Fields.get("field32") == null ? "" : Fields.get("field32");
            String Field_37 = Fields.get("field37") == null ? "" : Fields.get("field37");
            String Field_39 = Fields.get("field39") == null ? "" : Fields.get("field39");
            String Field_41 = Fields.get("field41") == null ? "" : Fields.get("field41");
            String Field_48 = Fields.get("field48") == null ? "" : Fields.get("field48");
            String Field_54 = Fields.get("field54") == null ? "" : Fields.get("field54");
            String Field_65 = Fields.get("field65") == null ? "" : Fields.get("field65");
            String Field_88 = Fields.get("field88") == null ? "" : Fields.get("field88");
            String Field_100 = Fields.get("field100") == null ? "" : Fields.get("field100");
            String Field_101 = Fields.get("field101") == null ? "" : Fields.get("field101");
            String Field_102 = Fields.get("field102") == null ? "" : Fields.get("field102");
            String Field_103 = Fields.get("field103") == null ? "" : Fields.get("field103");
            String Field_104 = Fields.get("field104") == null ? "" : Fields.get("field104");
            String strDate = this.anyDate("yyyy-mm-dd");//to_char(some_date, 'yyyy-mm-dd hh24:mi:ss') 
            String strTime = this.anyDate("HH:mm:ss");
            String strchannel = "POS";
            String strRequesttoEconnect = Fields.get("strRequesttoEconnect") == null ? "" : Fields.get("strRequesttoEconnect");
            String strResponseFromEconnect = Fields.get("strResponseFromEconnect") == null ? "" : Fields.get("strResponseFromEconnect");
            String POSReceipt = Fields.get("POSReceipt") == null ? "" : Fields.get("POSReceipt");

            String sql = " INSERT INTO TBINCOMINGTRANSACTIONS (FIELD_0, FIELD_2, FIELD_3, FIELD_4,FIELD_7,FIELD_11,FIELD_32,FIELD_37,FIELD_39,FIELD_41,"
                    + "  FIELD_54,FIELD_65,FIELD_88,FIELD_100,FIELD_101,FIELD_102,FIELD_103,FIELD_104,REQUEST_TO_ECONNECT,"
                    + "  RESPONSE_FROM_ECONNECT,POS_RECEIPT,CHANNEL)"
                    + "  VALUES ('" + Field_0 + "','" + Field_2 + "','" + Field_3 + "','" + Field_4 + "','" + Field_7 + "','" + Field_11 + "','" + Field_32 + "','" + Field_37 + "','" + Field_39 + "','" + Field_41 + "','" + Field_54 + "','" + Field_65 + "',"
                    + "  '" + Field_88 + "','" + Field_100 + "','" + Field_101 + "','" + Field_102 + "','" + Field_103 + "','" + Field_104 + "','" + strRequesttoEconnect.replace("'", "\"") + "','" + strResponseFromEconnect.replace("'", "\"") + "','" + POSReceipt.replace("'", "\"") + "','" + strchannel + "')";

            affectedRows = connections.execute(sql);

        } catch (Exception ex) {
            //this.log("INFO spInsertPOSTransaction() :: " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
        }
        return affectedRows;

    }

//    public String fn_getAgentPhonenumber(String strAgentID) {
//
//        String strMobile = "";
//        String strLogin = "";
//        try {
//            strLogin = "Select DEVICEMOBILENUMBER from tbagentdevices  where AGENTID =" + strAgentID + "'";
//            strMobile = connections.ExecuteQueryStringValue(ECSERVER, strLogin, ECPASSWORD, ECUSER, ECDB, ECPORT, "", "DEVICEMOBILENUMBER");
//            return strMobile;
//        } catch (Exception ex) {
//            this.log("\nINFO fn_getAgentPhonenumber() :: " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
//            return "";
//        }
//    }
//
    public String fn_getAgentPhonenumber2(String TerminalID) {

        String strMobile = "";
        String strLogin = "";
        try {
            Database connections = new Database(props);
            strLogin = "Select DEVICEMOBILENUMBER from tbagentdevices  where DEVICEIMEI = '" + TerminalID + "'";
            strMobile = connections.ExecuteQueryStringValue(strLogin, "", "DEVICEMOBILENUMBER");
            return strMobile;
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
            return "";
        }
    }

    public String fn_getVirtualPan(String Banksortcode) {

        String strVirtualpan = "";
        String strLogin = "";
        try {
            Database connections = new Database(props);

            strLogin = "select VIRTUALPAN from TBBANKS where BANK_CODE='" + Banksortcode + "'";
            strVirtualpan = connections.ExecuteQueryStringValue(strLogin, "", "VIRTUALPAN");
            return strVirtualpan;
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
            return "";
        }
    }
//
//    //for wallet txns
//    public String fn_Verify_Mwallet_account(String strPhone) {
//
//        String strMobile = "";
//        String strLogin = "";
//        try {
//            strLogin = "Select ACCOUNTNO from   TBACCOUNT where ACCOUNTNO=  '" + strPhone + "'";
//            strMobile = connections.ExecuteQueryStringValue(ECSERVER, strLogin, ECPASSWORD, ECUSER, ECDB, ECPORT, "", "MWALLETACCOUNT");
//
//            if (strMobile.trim() != "") {
//                strMobile = "00";
//            }
//
//            return strMobile;
//        } catch (Exception ex) {
//            this.log("\nINFO fn_Verify_Mwallet_account() :: " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
//            return "";
//        }
//    }
//
//    public String Wallet_PIN_Verify(String strPhone, String strPIN) {
//
//        String response = "";
//        String strField39 = "99";
//        String strField52 = "";
//        String strField32 = "";
//        String strUsername = "";
//        String strResults = "";
//        String MyPassword = "";
//        try {
//            strUsername = "select CUSTOMERNO from TBCUSTOMERS where MWALLETACCOUNT='" + strPhone + "'";
//            strUsername = connections.ExecuteQueryStringValue(ECSERVER, strUsername, ECPASSWORD, ECUSER, ECDB, ECPORT, "", "MWALLETACCOUNT");
//
//            //MyPassword = hmacDigest(strPIN + strDob.toString().toUpperCase(), "");
//            MyPassword = encryptPin(strPIN, strUsername, "secret");
//
//            strResults = "select PHONENUMBER   from  TBCUSTOMERVALIDATION where PHONENUMBER = '" + strPhone + "' and PIN='" + MyPassword + "'";
//            strResults = connections.ExecuteQueryStringValue(ECSERVER, strResults, ECPASSWORD, ECUSER, ECDB, ECPORT, "", "MWALLETACCOUNT");
//
//            if (strResults.trim() != "") {
//                strResults = "00";
//            }
//
//            return strResults;
//        } catch (Exception ex) {
//            this.log("\nINFO fn_Verify_Mwallet_account() :: " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
//            return "";
//        }
//    }

    public static String hmacDigest(String msg, String keyString) {
        String digest = null;
        try {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacSHA512");
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(key);

            byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));

            StringBuffer hash = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            digest = hash.toString();
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
        }
        return digest;
    }

    //Get mwallet status
//    public String Get_Approval_status(String accountocheck) throws SQLException {
//        String strResponse = "";
//        String SQL, sqlQuery = "";
//        String strTotalcredit = "";
//        String strdate = "";
//        double amount = 0;
//        String strStatementPrinting = "";
//        String strTotaldebits = "";
//        int i = 1;
//        Connection dbConnection = connections.getDBConnection(ECSERVER, ECDB, ECUSER, ECPASSWORD);
//        try {
//            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "kes"));
//            String strStoredProcedure = "{call SP_APPROVAL_STATUS(?,?)}";
//            CallableStatement callableStatement = dbConnection.prepareCall(strStoredProcedure);
//
//            callableStatement.setString(1, accountocheck);
//            callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR);
//            callableStatement.executeUpdate();
//
//            String strStatus = callableStatement.getString(2);
//
//            return strStatus;
//        } catch (Exception ex) {
//            this.log("INFO :: Error on Get_Approval_status " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
//        } finally {
//            dbConnection.close();
//        }
//
//        return null;
//    }
//
//    //-----------------------
    public Boolean updateTransactionResponse(Map<String, String> map, String CORR_ID, String POS_Receipt) {

        boolean Success = false;
        try {
            connections = new Database(props);
//TBINCOMINGPOSTRANSACTIONS
            String strInMsg = "update tbincomingtransactions set POS_receipt='" + POS_Receipt + "' where field_37='" + CORR_ID + "'";
            Success = connections.ExecuteUpdate(strInMsg);
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
            //this.log("\nINFO updateTransactionResponse() :: " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");

        }
        return Success;
    }

    public Boolean fn_Updateagentpassword(String StrAgentID, String StrPassword, String strDeviceid, String intid) {

        boolean Success = false;
        try {
            connections = new Database(props);
            //tumefika hapa
            String SQL, MyPassword = "";
            String joinedPwd = StrPassword + StrAgentID;
            String pwd = Base64.getEncoder().encodeToString(joinedPwd.getBytes());
            MyPassword = this.HmacHash(pwd, "secret", "HmacSHA256");
            System.out.println("tumefika hapa");
            //MyPassword = this.encryptPin(StrAgentID, StrPassword, "secret");
            String strInMsg = "update TBAGENTS set PIN='" + MyPassword.toLowerCase() + "',firstlogin = 0 where AGENTID = '" + StrAgentID + "' AND DEVICE1ID = '" + strDeviceid + "'";
            Success = connections.ExecuteUpdate(strInMsg);

            if (Success == true) {
                //String stragentphonenumber = this.fn_getAgentPhonenumber2(strDeviceid);
                String srAgentname = this.fn_getAgentName(StrAgentID);
                String strmessage = "Dear " + srAgentname + ",  agent ID." + StrAgentID + " your new POS login password is " + StrPassword + ".Your login Id remains " + StrAgentID;
                //
                //this.SendSMS(strmessage, stragentphonenumber, intid);
            }
            return Success;
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
            //this.log("\nINFO fn_Updateagentpassword() :: " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
            return false;
        }
    }

    //Reversal
//    public String generateReversal(HashMap<String, String> ISOdetails) throws IOException, InterruptedException {
//        String response = "";
//        String strResponse = "";
//        try {
//            int attempts = Integer.parseInt(ISOdetails.get("trials"));
//            String CORR_ID = this.generateCorelationID();
//
//            ISOdetails.remove("3");
//            ISOdetails.put("3", "420000");
//            ISOdetails.remove("CorrelationID");
//            ISOdetails.put("CorrelationID", CORR_ID);
//
//            this.log("Reversal" + ISOdetails.get("CorrelationID"), "REVERSE");
//
//            boolean sentToWebLogicAgain = false;
//            HashMap ParamsFromAdapterAgain;
//            ParamsFromAdapterAgain = new HashMap();
//
//            QueueWriter queueWriter = new QueueWriter(QUEUE_REQUEST, PROVIDER_URL);
//
//            int trials = 0;
//            do {
//                sentToWebLogicAgain = queueWriter.sendObject((HashMap) ISOdetails, CORR_ID);
//                trials++;
//            } while (sentToWebLogicAgain == false & trials < 3);
//
//            if (sentToWebLogicAgain) {
//                System.out.println("[SENT: Reversal Transaction Sent to ESB   -:]" + ISOdetails.get("CorrelationID"));
//                long Start = System.currentTimeMillis();
//                long Stop = Start + (20 * 1000);
//                do {
//                    Thread.currentThread().sleep(100);
//                    ParamsFromAdapterAgain = this.getWeblogicMessageFromQueue(ISOdetails.get("CorrelationID"));
//                } while (ParamsFromAdapterAgain.isEmpty() && System.currentTimeMillis() < Stop);
//
//                if (ParamsFromAdapterAgain.isEmpty()) {
//                    attempts++;
//
//                    if (attempts <= 10) {
//                        System.out.println("[SENT: Transaction Reversed for           -:](" + attempts + ")" + ISOdetails.get("CorrelationID"));
//                        ISOdetails.remove("trials");
//                        ISOdetails.put("trials", attempts + "");
//                        generateReversal(ISOdetails);
//                    } else {
//                        strResponse = strResponseHeader(ISOdetails.get("105"));
//                        strResponse += "--------------------------------" + "#";
//                        strResponse += "Transaction request timed out #";
//                        strResponse += strResponseFooter(ISOdetails.get("105"));
//                        SendPOSResponse(strResponse, ISOdetails.get("CorrelationID"));
//                        System.out.println("[SENT: Timeout Occured..........          -:](" + attempts + ")" + ISOdetails.get("CorrelationID"));
//                        return "";
//                    }
//                } else {
//                    response = this.genHashDelimiterString(ParamsFromAdapterAgain, ISOdetails.get("CorrelationID").toString());
//                }
//            }
//        } catch (Exception ex) {
//            this.log("\nINFO fn_Updateagentpassword() :: " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
//        }
//        return response;
//    }
//    public static String GetSequenceNumber() {
//        ClassImportantValues cl = new ClassImportantValues();
//
//        String strcountFile = COUNT_FILE;
//        File file = new File(strcountFile);
//        FileLock flock = null;
//        String strPaddedNumber = "";
//        try {
//
//            if (file.exists()) {
//                FileInputStream fstream = new FileInputStream(strcountFile);
//
//                // Read the File Containing the Next Sequence.. Then
//                DataInputStream in = new DataInputStream(fstream);
//                BufferedReader br = new BufferedReader(new InputStreamReader(in));
//                String strLine = "";
//                strLine = br.readLine();
//                in.close();
//
//                int intCurrentNum = Integer.parseInt(strLine.trim());
//                intCurrentNum += 1;
//
//                strPaddedNumber = String.format("%06d", intCurrentNum);
//
//                //Now OverWrite the File
//                FileOutputStream fos = new FileOutputStream(file, false);
//                fos.write(strPaddedNumber.getBytes());
//                fos.close();
//
//            } // end if file.exists()
//            else { // create the file if it doesn't exist
//
//                file.createNewFile();
//                // write 1234 to the file to begin the counter
//                try (FileOutputStream fos = new FileOutputStream(file, false)) {
//                    // write 1234 to the file to begin the counter
//                    fos.write("1234".getBytes());
//                }
//                strPaddedNumber = String.format("%06d", 1);
//            }
//
//            return strPaddedNumber;
//
//        } catch (IOException | NumberFormatException ex) {
//            System.out.println("Error GetSequenceNumber : " + ex.getMessage() + StackTraceWriter(ex));
//            return null;
//        }
//
//    } // end fn GetSequenceNumber
//
//    public String PIN_Verify(String strReferenceNo, String strNarration, String strProcCode, String strAmount, String strProcessingCode,
//            String strClearPIN, String strPAN, String strExpiryDate, String strTerminalSerial, String strField35, String strField37, String strPhoneNumber,
//            String strDebitAccount, String strCreditAccount, String strField68) {
//        /*
//         strField35 = "6396730617000097=18075019665930001";
//         strClearPIN = "1234";
//         strPAN = "6396730617000097";
//         strExpiryDate = "1807";
//         */
//        try {
//
//            String response;
//            String STAN = "" + GetSequenceNumber();
//            String strPINBlock = generatePINBlock.DESEncrypt(strClearPIN, trimLeftString(strPAN), PIN_VERIFICATION_ENCRYPT_KEYS);
//
//            HashMap<String, String> data = new HashMap<String, String>();
//            data.put("MTI", "1200"); // Message Type
//            data.put("2", trimLeftString(strPAN)); // Primary Account Number (PAN)
//            data.put("3", "310000");//Processing code
//            data.put("4", "000000000000");//Amount
//            data.put("7", (new SimpleDateFormat("MMddHHmmss")).format(new java.util.Date())); // Valuedate 
//            data.put("11", STAN); // Transaction Unique Reference (STAN)
//            data.put("12", (new SimpleDateFormat("hhmmss")).format(new java.util.Date())); // Time, local transaction
//            data.put("13", (new SimpleDateFormat("MMdd")).format(new java.util.Date())); //Local Transaction Date
//            data.put("14", strExpiryDate);// Card Expiry date
//            data.put("15", (new SimpleDateFormat("MMdd")).format(new java.util.Date()));//Settlement date
//            data.put("22", "902");//POS Entry Mode
//            data.put("23", "001");//Card Sequence Number
//            data.put("25", "00");//POS Condition Mode
//            data.put("26", "12");//POS Entry Mode
//            data.put("28", "C00000000");//Amount, Transaction Fee
//            data.put("30", "C00000000");//Amount, Transaction Processing Fee
//            data.put("32", "639673");//Acquiring institution code(BIN)
//            data.put("35", trimLeftString(strField35));//TRACK2 DATA
//            data.put("37", strField37);//Retrieval Reference Number
//            data.put("41", "22310001");//Card Acceptor terminal ID
//            data.put("42", "CBKL           ");// Card Acceptor ID code
//            data.put("43", "CBKL BANK                KENYA  KE");// Card Acceptor terminal ID
//            data.put("49", "404");// Currency Code
//            data.put("52", strPINBlock);// PIN BLOCK
//            data.put("56", "1510");// Message reason code
//            data.put("59", PadZeros(10, STAN));// Echo Data
//            data.put("123", "21010121014C101");// POS Data Code
//
//            String requestISO = iso.CreateISO(data);
//            requestISO = requestISO.replace(strPINBlock, new String(HexString2Bytes(strPINBlock), "ISO8859_1"));
//            String header = (new DataConversions()).IntegertoASCII(requestISO.length());
//            requestISO = header + requestISO;
//
//            if (ISO8583Adaptor.isConnectedClient) {
//                ClientSocketHandler.chEv.getChannel().write(requestISO);
//                System.out.println("Message Sent to Postilion");
//                //  Mask the PAN [replace PAN,Pin Block data during logging]
//                String strnewpan = strPAN;
//                strnewpan = strnewpan.replace(strPAN.substring(6, 14), "xxxxxxxxxxxxxx");
//                String strField_35 = strField35.replace(strField35.substring(6, 32), "xxxxxxxxxxxxxxxxxxxxxxxxx");
//                data.remove("2");
//                data.remove("52");
//                data.remove("35");
//                data.put("2", strnewpan);
//                data.put("35", strField_35);
//                data.put("52", "*********************************");
//
//                // Save map details on the OuterHashMap Collection
//                data.put("88", strNarration);
//                data.remove("3");
//                data.put("3", strProcessingCode);
//                data.put("4", strAmount);
//                data.put("88", strNarration);
//                data.put("65", strReferenceNo);
//                data.put("68", strField68);
//                data.put("100", strProcCode);
//                data.put("102", strDebitAccount);
//                data.put("103", strCreditAccount);
//                data.put("104", strTerminalSerial);
//                data.put("PhoneNumber", strPhoneNumber);
//                data.put("timestamp", this.anyDate(""));
//
//                OuterHoldingQueue.put(strField37, data);
//
//            } else {
//                String strMessageToPOS = "";
//                System.out.println("Message Failed to be Sent switch");
//                String strnewpan = strPAN;
//                strnewpan = strnewpan.replace(strPAN.substring(6, 14), "xxxxxxxxxxxxxx");
//                String strField_35 = strField35.replace(strField35.substring(6, 32), "xxxxxxxxxxxxxxxxxxxxxxxxx");
//                data.remove("2");
//                data.remove("52");
//                data.remove("35");
//                data.put("2", strnewpan);
//                data.put("39", "Failed Connecting to switch");
//                data.put("52", "*********************************");
//                data.put("35", strField_35);
//                data.put("timestamp", this.anyDate(""));
//
//                strMessageToPOS += this.strResponseHeader(strTerminalSerial) + "#";
//                strMessageToPOS += "AGENT ID:  " + strTerminalSerial.toString() + "#";
//                strMessageToPOS += "TRAN NUM:  " + strField37 + "#";
//                strMessageToPOS += "--------------------------------" + "#";
//                strMessageToPOS += "                                " + "#";
//                strMessageToPOS += "        PIN VERIFICATION        " + "#";
//                strMessageToPOS += "                                " + "#";
//                strMessageToPOS += " FAILED CONNECTING TO POSTILION " + "#";
//                strMessageToPOS += " " + "#";
//                strMessageToPOS += this.strResponseFooter(strTerminalSerial.toString()) + "#";
//                SendPOSResponse(strMessageToPOS, strField37);
//            }
//            /*
//             data.put("39", "00");
//
//             if (strDebitAccount.isEmpty() || strDebitAccount == null) {
//             strDebitAccount = "20201200003";
//             }
//             data.put("88", strNarration);
//             data.remove("3");
//             data.put("3", strProcessingCode);
//             data.put("4", strAmount);
//             data.put("88", strNarration);
//             data.put("65", strReferenceNo);
//             data.put("100", strProcCode);
//             data.put("102", strDebitAccount);
//             data.put("103", strCreditAccount);
//             data.put("104", strTerminalSerial);
//             data.put("PhoneNumber", strPhoneNumber);
//             data.put("timestamp", this.anyDate(""));
//             OuterHoldingQueue.put(strField37, data);
//             getSwitchResponse(data);
//             */
//            this.log(data.toString() + "\n\n", "PinVerification");
//
//        } catch (Exception ex) {
//            this.log("Error on Function PIN_Verify() " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
//            return "";
//        }
//        return "";
//    }
//
//    public HashMap<String, String> getSwitchResponse(HashMap<String, String> Fields) throws InterruptedException {
//
//        String strResponse = "";
//        String strAccount = "";
//        String strCardno = "";
//        String strMessageToPOS = "";
//        HashMap<String, String> OriginalFieldsToSwitch = new HashMap<String, String>();
//        try {
//            if (Fields != null || !Fields.isEmpty()) {
//
//                String Field_2 = Fields.get("2") == null ? "" : Fields.get("2").toString();
//                String Field_11 = Fields.get("11") == null ? "" : Fields.get("11").toString();;
//                String Field_39 = Fields.get("39") == null ? "" : Fields.get("39").toString();
//                String Field_37 = Fields.get("37") == null ? "" : Fields.get("37").toString();
//                String Field_102 = Fields.get("102") == null ? "" : Fields.get("102").toString();
//
//                switch (Field_39) {
//                    case "000":
//                        // 1. GET ALL THE CONTENTS FROM THE ORIGINAL HASHMAP USING THE REFERENCE_NUMBER
//                        OriginalFieldsToSwitch = (HashMap<String, String>) OuterHoldingQueue.get(Field_37.trim());
//                        OuterHoldingQueue.remove(Field_37);
//
//                        //2. ADD ACCOUNT NUMBER FROM SWITCH TO ORIGINAL HASHMAP THEN SEND TO ESB
//                        OriginalFieldsToSwitch.put("102", Field_102);
//
//                        //3. SEND THE NEW HASHMAP COLLECTION TO ECONNECT
//                        String strcardNumber = OriginalFieldsToSwitch.get("2") == null ? "" : OriginalFieldsToSwitch.get("2").toString();
//                        String strProcessingCode = OriginalFieldsToSwitch.get("3") == null ? "" : OriginalFieldsToSwitch.get("3").toString();
//                        String strAmount = OriginalFieldsToSwitch.get("4") == null ? "" : OriginalFieldsToSwitch.get("4").toString();
//                        String strReferenceNumber = OriginalFieldsToSwitch.get("37") == null ? "" : OriginalFieldsToSwitch.get("37").toString();
//                        String strField65 = OriginalFieldsToSwitch.get("65") == null ? "" : OriginalFieldsToSwitch.get("65").toString();
//                        String strField68 = OriginalFieldsToSwitch.get("68") == null ? "" : OriginalFieldsToSwitch.get("68").toString();
//                        String strNarration = OriginalFieldsToSwitch.get("88") == null ? "" : OriginalFieldsToSwitch.get("88").toString();
//                        String strTransactionIdentifier = OriginalFieldsToSwitch.get("100") == null ? "" : OriginalFieldsToSwitch.get("100").toString();
//                        String strAccountNumber = OriginalFieldsToSwitch.get("102") == null ? "" : OriginalFieldsToSwitch.get("102").toString();
//                        String strCreditAccount = OriginalFieldsToSwitch.get("103") == null ? "" : OriginalFieldsToSwitch.get("103").toString();
//                        String strAgentID = OriginalFieldsToSwitch.get("104") == null ? "" : OriginalFieldsToSwitch.get("104").toString();
//                        String strPhoneNumber = OriginalFieldsToSwitch.get("PhoneNumber") == null ? "" : OriginalFieldsToSwitch.get("PhoneNumber").toString();
//
//                        this.getESBResponse(strAgentID, strcardNumber, strProcessingCode, strAmount, strReferenceNumber, strNarration, strTransactionIdentifier, strAccountNumber, strCreditAccount, strField65, strPhoneNumber, strField68);
//                        break;
//
//                    default:
//                        String ResponseDescription = getResponsePostillion(Field_39);
//                        // 1. GENERATE MESSAGE TO SEND TO POS
//                        OriginalFieldsToSwitch = (HashMap<String, String>) OuterHoldingQueue.get(Field_37.trim());
//
//                        strMessageToPOS += this.strResponseHeader(OriginalFieldsToSwitch.get("68").toString()) + "#";
//                        strMessageToPOS += "AGENT ID:  " + OriginalFieldsToSwitch.get("104").toString() + "#";
//                        strMessageToPOS += "TRAN NUM:  " + OriginalFieldsToSwitch.get("37").toString() + "#";
//                        strMessageToPOS += "--------------------------------" + "#";
//                        strMessageToPOS += "                                " + "#";
//                        strMessageToPOS += "        PIN VERIFICATION        " + "#";
//                        strMessageToPOS += "                                " + "#";
//                        strMessageToPOS += "    PIN VERIFICATION FAILED     " + "#";
//                        strMessageToPOS += padEqual(ResponseDescription) + " #";
//                        strMessageToPOS += " " + "#";
//                        strMessageToPOS += this.strResponseFooter(OriginalFieldsToSwitch.get("105").toString()) + "#";
//                        SendPOSResponse(strMessageToPOS, Field_37);
//
//                        // 2. REMOVE THE HASHMAP FROM THE OUTERMAP COLLECTION MAP
//                        OuterHoldingQueue.remove(Field_37);
//                        break;
//
//                }
//            }
//        } catch (Exception ex) {
//            this.log("Error on getSwitchResponse() " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
//        }
//        return OriginalFieldsToSwitch;
//    }
//    public void log(String content, String filename) {
//        logging.log(content, filename);
//    }
    //
    public String fn_getAgentName(String strAgentID) {

        String AGENTNAMES = "";
        String strLogin = "";

        try {
            connections = new Database(props);
            strLogin = "Select AGENTNAMES FROM TBAGENTS where AGENTID ='" + strAgentID + "'";
            AGENTNAMES = connections.ExecuteQueryStringValue(strLogin, "", "AGENTNAMES");

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
            //this.log("INFO : Function fn_getAgentName()" + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
            return "";
        }
        return AGENTNAMES;
    }
//    public Boolean GetEnabledServices(String strServiceCode) {
//
//        Boolean Strstatus = false;
//        //' 1 MEANS ENABLED 0 MEANS DISABLED
//        try {
//            switch (strServiceCode) {
//                case "LUKUPREPAID":
//                    if (Integer.parseInt(LUKUPREPAID) == 1) {
//                        Strstatus = true;
//                    } else {
//                        Strstatus = false;
//                    }
//                    break;
//                case "AZAMTV":
//                    if (Integer.parseInt(AZAMTV) == 1) {
//                        Strstatus = true;
//                    } else {
//                        Strstatus = false;
//                    }
//                    break;
//                case "DAWASCO":
//                    if (Integer.parseInt(DAWASCO) == 1) {
//                        Strstatus = true;
//                    } else {
//                        Strstatus = false;
//                    }
//                    break;
//            }
//            return Strstatus;
//        } catch (Exception ex) {
//            return false;
//        }
//    }

    public void SendSMS(String strMessage, String strPhonenumber, String intid) {
        HashMap sms = new HashMap();
        try {
            //writing to a topic
            sms.put("field65", strPhonenumber);
            sms.put("field70", strMessage);
            //call the sms queue
            Boolean sentToJboss = false;
            int trials = 0;
            do {
//                TopicWriter tr = new TopicWriter(Configs.ESB_SMS_TOPIC);//"java:jboss/exported/jms/topic/ESBTransaction_Topic"
//                sentToJboss = tr.sendObject(sms, "SMS");

                trials++;
            } while (sentToJboss == false & trials < 3);
        } catch (Exception ex) {

        }
    }

//    public void sendSMS(String strMessage, String strPhonenumber) {
//
//        strPhonenumber = "254" + strPhonenumber.substring(strPhonenumber.length() - 9);
//        String responseXml = "";
//
//        // String strXML = "<?xml version= '1.0' encoding= 'utf-8'?>\n"
//        //       + "<message>\n"
//        //     + "<authHeader sourceid='SIMULATOR' password='SIMULATOR123'/>\n"
//        //   + "<isomsg direction='request'>\n"
//        //  + "<field id='65' value='" + strPhonenumber + "' />\n"
//        // + "<field id='104' value='" + strMessage + "' />\n </isomsg>\n</message>";
//        String strXML = "<?xml version= '1.0' encoding= 'utf-8'?>\n"
//                + "<message>\n"
//                + " <field0>1200</field0>"
//                + " <field2>" + strPhonenumber + "</field2>"
//                + " <field37>001281963783</field37>"
//                + " <field32>USSD</field32>"
//                + " <field112>" + strMessage + "</field112>"
//                + " <field113>1</field113>"
//                + " </message>";
//
//        // <?xml version= "1.0"  encoding= "utf-8"?>
//        //  <message>
//        // <field0>1200</field0>
//        // <field2>254715077090</field2>
//        // <field7>2016040813</field7>
//        // <field11>495528</field11>
//        // <field12>160408133823</field12>
//        // <field13>0408</field13>
//        // <field15>0408</field15>
//        // <field17>0408</field17>
//        // <field32>UUSD</field32>
//        // <field37>082245495528</field37>
//        // <field41>FID00001</field41>
//        // <field42>606465ATM000001</field42>
//        // <field43>Consolidated Bank Of Kenya</field43>
//        // <field56>bcac5bdc-32fb-42df-8ddb-ce8f705ba8c5</field56>
//        // <field60>ONUS</field60>
//        // <field112>Your OTP Code is: 237329. This OTP is valid for the next 10 minutes.</field112>
//        // <field113>1</field113>
//        // <branch102></branch102>
////  </message> --//
//        try {
//            String url = SMS_URL;
//            URL obj = new URL(url);
//            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//            //add reuqest header
//            con.setRequestMethod("POST");
//            //con.setRequestProperty("User-Agent", USER_AGENT);
//            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//
//            String urlParameters = strXML;
//
//            // Send post request
//            con.setDoOutput(true);
//            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//            wr.writeBytes(urlParameters);
//            wr.flush();
//            wr.close();
//
//            System.out.println("[SENT: SMS Sent to ESB            -:]" + strPhonenumber);
//            this.log(strXML, "SMS_ESB_Request");
//
//            int responseCodex = con.getResponseCode();
//            System.out.println("\nSending 'POST' request to URL : " + url);
//            System.out.println("Post parameters : " + urlParameters);
//            System.out.println("Response Code : " + responseCodex);
//
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(con.getInputStream()));
//            String inputLine;
//            StringBuffer responsefromcbs = new StringBuffer();
//
//            while ((inputLine = in.readLine()) != null) {
//                responsefromcbs.append(inputLine);
//            }
//            in.close();
//
//            responseXml = responsefromcbs.toString();
//
//            System.out.println("[RECEIVED:SMS RESPONSE            -:]" + strPhonenumber);
//            this.log(responseXml, "SMS_ESB_Response");
//
//            //Break the Response XML
//        } catch (Exception e) {
//
//        }
//    }
//
//    public void sendtoESB(String strXML) {
//
//        try {
//            ESBcall url = new ESBcall();
//            String responseXml = url.connectToESBWS(strXML);
//            this.log(strXML, "ESB_Request");
//            //Break the Response XML
//            this.log(responseXml, "ESB_Response");
//
//        } catch (Exception e) {
//
//        }
//    }
    public String fn_getAgentEODTransactions(String strAgentID, String strTerminalID, String intID) throws SQLException {
        String strResponse = "";
        String SQL, sqlQuery = "";
        String strTotalcredit = "";
        String strdate = "";
        double amount = 0;
        String strStatementPrinting = "";
        String strTotaldebits = "";
        int i = 1;

        try {
            //Connection dbConnection = connections.getDatabaseConnection();
            Connection conn = null;
            connections = new Database(props);
            conn=connections.getDatabaseConnection();
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "kes"));
            String strStoredProcedure = "{call SP_DAILY_TRANSACTIONS(?,?)}";
            CallableStatement callableStatement = conn.prepareCall(strStoredProcedure);

            callableStatement.setString(1, strTerminalID);
            callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR);
            callableStatement.executeUpdate();

            String strStatus = callableStatement.getString(2);

            if (strStatus.equals("1")) {
                String query = "select transactiontype,transactioncount,amount from tbagentsummary where serialnumber='" + strTerminalID + "'";
                ResultSet rsrecordQuery = connections.ExecuteQueryReturnString(query);

                while (rsrecordQuery.next()) {
                    String strAmount = NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(rsrecordQuery.getString("AMOUNT").toString()));
                    strStatementPrinting += " ";
                    strStatementPrinting += PadSpaces(15, rsrecordQuery.getString("TRANSACTIONTYPE").toString(), "RIGHT");
                    strStatementPrinting += PadSpaces(4, rsrecordQuery.getString("TRANSACTIONCOUNT").toString(), "LEFT");
                    strStatementPrinting += PadSpaces(12, strAmount.replace("KES", ""), "LEFT");
                    strStatementPrinting += "#";
                }

            }
            strResponse += "AGENT ID:  " + strAgentID + "#";
            strResponse += "TRAN NUM:  " + intID + "#";
            strResponse += "--------------------------------" + "#";
            strResponse += "                                " + "#";
            strResponse += "                                " + "#";
            strResponse += "       EOD SUMMARY REPORT       " + "#";
            strResponse += "                                " + "#";
            strResponse += "    TRANS.       COUNT    AMOUNT" + "#";
            strResponse += "                                " + "#";
            strResponse += strStatementPrinting;
            //fetch the account Balance
            String StrMerchantFloatAccount = fn_getAgentAccountNumber(strAgentID);
            String[] MerchantAccountBalance = getBalance(StrMerchantFloatAccount, strAgentID, strTerminalID);
            String strAvailableBalance = formatter.format(Double.valueOf(MerchantAccountBalance[0])).replace("KES", "");
            String strActualBalance = formatter.format(Double.valueOf(MerchantAccountBalance[1])).replace("KES", "");
            strAvailableBalance = formatter.format(Double.valueOf(MerchantAccountBalance[0])).replace("¤", "");
            strActualBalance = formatter.format(Double.valueOf(MerchantAccountBalance[1])).replace("¤", "");

            strResponse += "                                " + "#";
            strResponse += "ACCOUNT NUM:    " + StrMerchantFloatAccount + "#";
            strResponse += "AVAIL BALANCE:  KES " + strAvailableBalance + "#";
            strResponse += "ACTUAL BALANCE: KES " + strActualBalance + "#";
            strResponse += "                                " + "#";

            return strResponse;
        } catch (Exception ex) {
            // this.log("INFO :: Error on fn_getAgentEODTransactions " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
        } finally {
            try {
                connections.closeDatabaseConnection();
            } catch (Exception ex) {
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                TMSLog el = new TMSLog(sw.toString());
                el.logfile();
            }

        }

        return null;
    }
    //Generate XML

    public String genXML(Map<String, String> ISOdetails) {
        String strXML = "<?xml version= '1.0' encoding= 'utf-8'?>\n"
                + "<message>\n"
                //+ "<authHeader sourceid='SIMULATOR' password='SIMULATOR123'/>\n"
                //+ "<isomsg direction='request'>\n"
                + "<field0>" + ISOdetails.get("0") + "</field0>\n"
                + "<field2>" + ISOdetails.get("2") + "</field2>\n"
                + "<field3>" + ISOdetails.get("3") + "</field3>\n"
                + "<field4>" + ISOdetails.get("4") + "</field4>\n"
                + "<field7>" + ISOdetails.get("7") + "</field7>\n"
                + "<field11>" + ISOdetails.get("11") + "</field11>\n"
                + "<field12>" + ISOdetails.get("12") + "</field12>\n"
                + "<field13>" + ISOdetails.get("13") + "</field13>\n"
                + "<field15>" + ISOdetails.get("15") + "</field15>\n"
                + "<field17>" + ISOdetails.get("17") + "</field17>\n"
                + "<field24>" + ISOdetails.get("24") + "</field24>\n"
                + "<field32>" + ISOdetails.get("32") + "</field32>\n"
                + "<field37>" + ISOdetails.get("37") + "</field37>\n"
                + "<field41>" + ISOdetails.get("41") + "</field41>\n"
                + "<field42>" + ISOdetails.get("42") + "</field42>\n"
                + "<field43>" + ISOdetails.get("43") + "</field43>\n"
                + "<field49>" + ISOdetails.get("49") + "</field49>\n"
                + "<field60>" + ISOdetails.get("60") + "</field60>\n"
                + "<field65>" + ISOdetails.get("65") + "</field65>\n"
                + "<field67>" + ISOdetails.get("67") + "</field67>\n"
                + "<field98>" + ISOdetails.get("98") + "</field98>\n"
                + "<field68>" + ISOdetails.get("68") + "</field68>\n"
                + "<field100>" + ISOdetails.get("100") + "</field100>\n"
                + "<field101>" + ISOdetails.get("101") + "</field101>\n"
                + "<field102>" + ISOdetails.get("102") + "</field102>\n"
                + "<field103>" + ISOdetails.get("103") + "</field103>\n"
                + "<field105>" + ISOdetails.get("105") + "</field105>\n"
                + "<orig_procode>" + ISOdetails.get("3") + "</orig_procode>\n"
                + "<branch102>" + ISOdetails.get("branch102") + "</branch102>\n"
                //orig_procode

                + "</message>\n";

        return strXML;
    }

//    public String getCustomerDetails(String strAccountNumber) throws IOException {
//        String[] strCustomerNameArray;
//        String strCustomerName = "";
//        String fname = "";
//        String mname = "";
//        String lname = "";
//        try {
//            URL url = new URL(CUSTOMER_DETAILS_URL);
//            Map<String, String> params = new LinkedHashMap<>();
//
//            params.put("username", CUSTOMER_DETAILS_USERNAME);
//            params.put("password", CUSTOMER_DETAILS_PASSWORD);
//            params.put("sourceId", CUSTOMER_DETAILS_SOURCE_ID);
//            params.put("account", strAccountNumber);
//
//            StringBuilder postData = new StringBuilder();
//            for (Map.Entry<String, String> param : params.entrySet()) {
//                if (postData.length() != 0) {
//                    postData.append('&');
//                }
//                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
//                postData.append('=');
//                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
//            }
//            String urlParameters = postData.toString();
//            URLConnection conn = url.openConnection();
//
//            conn.setDoOutput(true);
//
//            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
//
//            writer.write(urlParameters);
//            writer.flush();
//
//            String result = "";
//            String line;
//            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//
//            while ((line = reader.readLine()) != null) {
//                result += line;
//            }
//            writer.close();
//            reader.close();
//
//            JSONObject respobj = new JSONObject(result);
//            if (respobj.has("FSTNAME") || respobj.has("MIDNAME") || respobj.has("LSTNAME")) {
//                if (respobj.has("FSTNAME")) {
//                    fname = respobj.get("FSTNAME").toString().toUpperCase() + ' ';
//                }
//                if (respobj.has("MIDNAME")) {
//                    mname = respobj.get("MIDNAME").toString().toUpperCase() + ' ';
//                }
//                if (respobj.has("LSTNAME")) {
//                    lname = respobj.get("LSTNAME").toString().toUpperCase() + ' ';
//                }
//                strCustomerName = fname + mname + lname;
//            } else {
//                strCustomerName = "N/A";
//            }
//
//        } catch (Exception ex) {
//            this.log("\nINFO : Function getCustomerDetails() " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
//        }
//
//        // System.out.println(strCustomerName);
//        return strCustomerName;
//    }
    public String[] getBalance(String strAccountNo, String strAgentID, String strTerminalID) throws IOException {
        String[] strBalance = null;
        String SOURCE_ID = "";
        try {
            String referenceNumber = this.PadZeros(12, this.generateCorelationID());
            Map<String, String> params = new HashMap<>();

            params.put("field0", "0200");
            params.put("field2", strAccountNo);
            params.put("field3", "310000");
            params.put("field4", "0");
            params.put("field7", this.anyDate("MMddHHmmss"));
            params.put("field11", this.anyDate("MMddHHmmss"));
            params.put("field24", "MM");
            params.put("field32", "POS");
            params.put("field37", referenceNumber);
            params.put("field41", strTerminalID);
            params.put("field49", "KES");
            params.put("field68", "BALANCE ENQUIRY FOR ACCOUNT: " + strAccountNo);
            params.put("field102", strAccountNo);
            params.put("field103", "");
            params.put("field105", strTerminalID);
            params.put("field101", strAgentID);

            Gson gson = new Gson();
            String jsonrequest = gson.toJson(params);
            TMSWSCall tmscall = new TMSWSCall();
            String jsonresponse = tmscall.connectToESBWS(jsonrequest);
            HashMap responseMap = new Gson().fromJson(jsonresponse, new TypeToken<HashMap<String, String>>() {
            }.getType());

            boolean sentToJboss = false;

            if (responseMap.isEmpty()) {
                params.put("field39", "999");
                params.put("field48", "No response from Esb");
                // this.log("No Response " + referenceNumber + ":" + params.toString(), "ERROR");

            } else {
                strBalance = responseMap.get("field54").toString().split("\\|");
            }

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
            // this.log("INFO : Function getBalance()  " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
        }
        return strBalance;
    }

    public String getTerminalID(String terminalID) {
        String strResponse = "";
        String strQuery = "";
        String TerminalID = "";

        try {
            connections = new Database(props);
            strQuery = "SELECT TERMINALID FROM TBAGENTDEVICES WHERE DEVICEIMEI='" + terminalID + "'";
            TerminalID = connections.ExecuteQueryStringValue(strQuery, "", "TERMINALID");
            //System.out.println(strQuery);
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();

            return "";
        }
        return TerminalID;
    }

    //Get Response from ESB
    public HashMap getESBResponse(String strAgentID, String cardNumber, String processingCode, String amount, String referenceNumber, String narration, String transactionIdentifier, String strAccountNumber, String creditAccount, String strField65, String Phonenumber, String strTerminalserial) throws InterruptedException {
        String response = "";
        String strField_02 = "";
        String strField_00 = "";
        String strFieldPRD = "";
        String strProcessingCode = "";
        String strMessageToPOS = "";
        String SOURCE_ID = "";

        try {

            strField_02 = cardNumber;

            switch (processingCode) {
                case "120000": // MERCHANT PAYMENTS
                    strProcessingCode = "010000";
                    strField_00 = "1200";
                    strFieldPRD = "CHWL";
                    narration = "PAYMENT OF GOODS & SERVICES FOR " + strAccountNumber;
                    break;
                case "310000":
                    strProcessingCode = processingCode;
                    strField_00 = "0200";
                    narration = "BALANCE ENQUIRY FOR ACCOUNT " + strAccountNumber;
                    break;

                case "320000": // AGENT FLOAT (we do BI for Agent float)
                    strProcessingCode = "310000";
                    strField_00 = "1200";
                    narration = "AGENT FLOAT FOR ACCOUNT " + strAccountNumber;
                    break;
                case "380000":
                    strProcessingCode = processingCode;
                    strField_00 = "1200";
                    narration = "MINI STATEMENT FOR ACCOUNT " + strAccountNumber;
                    break;
                case "340000": // CARD ACTIVATION
                    strProcessingCode = processingCode;
                    strField_00 = "0100";
                    break;
                case "010000": // CASH WITHDRAWAL
                    strProcessingCode = processingCode;
                    strFieldPRD = "CHWL";
                    strField_00 = "1200";
                    break;
                case "500000": // BILL PAYMENTS
                    strProcessingCode = processingCode;
                    strFieldPRD = "";
                    strField_00 = "1200";
                    break;
                case "400000": // FUNDS TRANSFER
                    strProcessingCode = processingCode;
                    strFieldPRD = "AGFT";
                    strField_00 = "1200";
                    break;

                case "210000": // CASH DEPOSIT
                    strProcessingCode = processingCode;
                    strFieldPRD = "CHDP";
                    strField_00 = "1200";
                    break;
                default:
                    strField_00 = "1200";
                    strProcessingCode = processingCode;
                    break;
            }

            Map<String, String> ISOdetails = new HashMap<>();

            ISOdetails.put("0", strField_00);
            ISOdetails.put("2", strField_02);
            ISOdetails.put("3", strProcessingCode);
            ISOdetails.put("4", amount);
            ISOdetails.put("7", this.anyDate("MMddHHmmss"));
            ISOdetails.put("11", this.anyDate("MMddHHmmss"));
            ISOdetails.put("32", "POS");
            ISOdetails.put("37", referenceNumber);
            ISOdetails.put("65", strField65);
            ISOdetails.put("66", "");
            ISOdetails.put("68", narration);
            ISOdetails.put("88", narration);
            ISOdetails.put("100", transactionIdentifier);
            ISOdetails.put("101", strAgentID);
            ISOdetails.put("102", strAccountNumber);
            ISOdetails.put("103", creditAccount);
            ISOdetails.put("104", strAgentID);
            ISOdetails.put("105", strTerminalserial);
            ISOdetails.put("CorrelationID", referenceNumber);
            ISOdetails.put("PRD", strFieldPRD);

            TMSLog el = new TMSLog("ESB_Request", referenceNumber + "\n" + ISOdetails.toString() + "\n\n");
            el.logfile();
            //this.log("REQUEST :: " + referenceNumber + "\n" + ISOdetails.toString() + "\n\n", "ESB_Request");

            boolean sentToJboss = false;
            HashMap ParamsFromAdapter = new HashMap();
            //QueueWriter queueWriter = new QueueWriter(QUEUE_REQUEST, PROVIDER_URL);
            //QueueWriter queueWriter = new QueueWriter("java:jboss/exported/jms/queue/ESBRequest_Queue_DS");

            int trials = 0;
            do {
                //sentToJboss = queueWriter.sendObject((HashMap) ISOdetails, referenceNumber);
                trials++;
            } while (sentToJboss == false & trials < 3);

            if (sentToJboss) {
                System.out.println("[SENT: Transaction Sent to ESB            -:]" + referenceNumber);
                long Start = System.currentTimeMillis();
                long Stop = Start + (20 * 1000);
                do {
                    // TimeUnit.SECONDS.sleep(30);
                    //readFromQueue = new ReadFromQueue(props);
                    //do {
                    //ParamsFromAdapter = readFromQueue.readfromQueue("java:jboss/exported/jms/queue/ESBChannelResponse_Queue", referenceNumber);
                } while (ParamsFromAdapter.isEmpty() && System.currentTimeMillis() < Stop);

                if (ParamsFromAdapter.isEmpty()) {

                    //Excempt for processing code 340000
                    if (!ISOdetails.get("3").equals("340000")) {
                        ISOdetails.put("trials", "1");
                        System.out.println("[SENT: Transaction Responses Failed from ESB-:] " + referenceNumber);
                        // System.out.println("[SENT: Transaction Reversed for           -:] " + referenceNumber);
                        // this.generateReversal((HashMap<String, String>) ISOdetails);

                        //Send Failed Response to POS
                        String TransactionType = getTransactionType(ISOdetails.get("3").toString());

                        strMessageToPOS += this.strResponseHeader(getTerminalID(strTerminalserial)) + "#";
                        strMessageToPOS += "AGENT ID:  " + ISOdetails.get("101").toString() + "#";
                        strMessageToPOS += "TRAN NUM:  " + ISOdetails.get("37").toString() + "#";
                        strMessageToPOS += "--------------------------------" + "#";
                        strMessageToPOS += "                                " + "#";
                        strMessageToPOS += padEqual(TransactionType.toUpperCase()) + "#";
                        strMessageToPOS += "                                " + "#";
                        strMessageToPOS += "   NO RESPONSE FROM ESB GATEWAY " + "#";
                        strMessageToPOS += " " + "#";
                        strMessageToPOS += this.strResponseFooter(ISOdetails.get("105").toString()) + "#";
                        SendPOSResponse(strMessageToPOS, ISOdetails.get("37").toString());

                    }
                } else {
                    System.out.println("[SENT: Transaction Response Received for  -:] " + referenceNumber);
                    switch (processingCode) {
                        case "340000":// For Card Activation Return Array
                            return ParamsFromAdapter;
                        case "320000":
                            ParamsFromAdapter.remove("3");
                            ParamsFromAdapter.put("3", "320000");
                            break;
                        case "120000":
                            ParamsFromAdapter.remove("3");
                            ParamsFromAdapter.put("3", "120000");
                            break;
                        default:
                            break;
                    }

                    response = this.genHashDelimiterString(ParamsFromAdapter, referenceNumber);
                }
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
        }
        return null;
    }

    //llllllllllllllll
    public HashMap PCI_DSS_Maplogging(HashMap fields) {
        HashMap Converted = new HashMap();
        try {
            Converted = fields;
            if (Converted.containsKey("field2")) {

            }

        } catch (Exception ex) {

        }
        return Converted;
    }

    public HashMap getESBResponse_v1(String strAgentID, String cardNumber, String processingCode, String amount, String referenceNumber, String narration, String transactionIdentifier, String strAccountNumber, String creditAccount, String strField65, String Phonenumber, String strField68, String strField24, String strField98, String strField41, String beneficiaryBanksortcode, String Secretcode) throws InterruptedException {
        String response = "";
        String strField_02 = "";
        String strField_00 = "";
        String strFieldPRD = "";
        String strProcessingCode = "";
        String strMessageToPOS = "";
        String strField49 = "KES";
        String strfield100 = "";
        String strfield33 = "";
        String strfield65 = "";
        String strField66 = "";
        String strfield71 = "";

        HashMap<String, String> responseMap = new HashMap<String, String>();

        try {
            System.out.print(transactionIdentifier);
            strField_02 = cardNumber;

            switch (processingCode) {
                case "120000": // MERCHANT PAYMENTS
                    strProcessingCode = "010000";
                    strField_00 = "0200";
                    strFieldPRD = "CHWL";
                    narration = "PAYMENT OF GOODS & SERVICES FOR " + strAccountNumber;
                    break;
                case "310000":
                    strProcessingCode = processingCode;
                    strField_00 = "0200";
                    //narration = "BALANCE ENQUIRY FOR ACCOUNT " + strAccountNumber;
                    strField24 = "MM";
                    strField49 = "KES";
                    //strfield100 = "";
                    strFieldPRD = strField98;
                    break;

                case "320000": // AGENT FLOAT (we do BI for Agent float)
                    strProcessingCode = "310000";
                    strField_00 = "0200";
                    //narration = "AGENT FLOAT FOR ACCOUNT " + strAccountNumber;
                    //strfield100 = "";
                    break;
                case "380000":
                    strProcessingCode = processingCode;
                    strField_00 = "0200";
                    narration = "MINI STATEMENT FOR ACCOUNT " + strAccountNumber;
                    strField24 = "MM";
                    strField49 = "KES";
                    strFieldPRD = strField98;
                    //strfield100 = "";
                    // strField98 = "MINI";
                    break;
                case "340000": // CARD ACTIVATION
                    strProcessingCode = processingCode;
                    strField_00 = "1200";
                    //strField65 = strField65;
                    strField66 = Phonenumber;
                    narration = "ACCOUNT MOBILE ACTIVATION" + strField_02;
                    strField24 = "MM";
                    strField49 = "KES";
                    strfield100 = "";
                    break;
                case "010000": // CASH WITHDRAWAL
                    strProcessingCode = processingCode;
                    strFieldPRD = "AGENT_WD";
                    strField_00 = "0200";
                    strField24 = "MM";
                    strField49 = "KES";
                    strfield100 = "";
                    break;
                case "500000": // BILL PAYMENTS
                    strProcessingCode = processingCode;
                    //strFieldPRD = "";
                    strField_00 = "0200";
                    strField49 = "KES";
                    //strField98 = transactionIdentifier;
                    strfield100 = transactionIdentifier;
                    strField24 = "MM";
                    strField66 = Phonenumber;
                    //narration = "BILL PAYMENT FOR ACCOUNT " + strAccountNumber;
                    break;

                case "510000": // BILL presentment
                    strProcessingCode = processingCode;
                    strFieldPRD = "";
                    strField_00 = "1200";
                    strField49 = "KES";
                    strField98 = transactionIdentifier;
                    strfield100 = transactionIdentifier;
                    strField24 = "CC";
                    narration = "BILL PAYMENT FOR ACCOUNT " + strField65;
                    //strfield28 = "0";
                    break;
                case "630000": // CARDLESS FULFILMENT
                    strProcessingCode = processingCode;
                    strFieldPRD = "AGENT-CDFUL";
                    strField_00 = "0200";
                    strField49 = "KES";
                    strField98 = transactionIdentifier;
                    strfield100 = "";
                    strField24 = "MM";
                    strField66 = Secretcode;

                    break;
                case "620000": // CARDLESS Origination
                    strProcessingCode = processingCode;
                    strFieldPRD = "";
                    strField_00 = "0200";
                    strField49 = "KES";
                    strField98 = transactionIdentifier;
                    strfield100 = transactionIdentifier;
                    strField24 = "MM";
                    strField66 = Secretcode;
                    break;
                case "400000": // FUNDS TRANSFER
                    strProcessingCode = processingCode;
                    strFieldPRD = strField98;
                    strField_00 = "0200";
                    //strField98 = "FT_CC_POS";
                    //strfield100 = transactionIdentifier;
                    //strfield33 = beneficiaryBanksortcode;
                    strField24 = "MM";
                    strField49 = "KES";

                    break;

                case "420000": // Topup
                    strProcessingCode = processingCode;
                    //strFieldPRD = "";
                    //strField_00 = "0200";

                    // strField98 = transactionIdentifier;
                    //strfield100 = transactionIdentifier;
                    strfield71 = transactionIdentifier;
                    narration = "AIRTIME TOPUP FOR ACCOUNT " + strField65;

                    strField_00 = "0200";
                    strField49 = "KES";

                    break;

                case "210000": // CASH DEPOSIT
                    strProcessingCode = processingCode;
                    strFieldPRD = "AGENT_DP";
                    strField49 = "KES";
                    strField_00 = "0200";
                    strfield100 = "";
                    break;
                case "360000":
                    strProcessingCode = processingCode;
                    strField24 = "CC";
                    strfield100 = "ACCLOOKUP";
                    strField98 = "ACCLOOKUP";
                    strField_00 = "1200";
                    strFieldPRD = "ACCLOOKUP";
                    strfield100 = "ACI";//transactionIdentifier;
                    break;

                default:
                    strField_00 = "0200";
                    strProcessingCode = processingCode;
                    break;
            }

            System.out.print(strfield100);

            HashMap<String, Object> ISOdetails = new HashMap<>();
            Map<String, String> cols = new HashMap<>();

            ISOdetails.put("field0", strField_00);
            if (!strField_02.isEmpty()) {
                ISOdetails.put("field2", strField_02);
            }

            ISOdetails.put("field3", strProcessingCode);
            if (!amount.isEmpty()) {
                ISOdetails.put("field4", amount);
            } else {
                ISOdetails.put("field4", "0");
            }

            ISOdetails.put("field7", this.anyDate("YYYYMMddHH"));
            //ISOdetails.put("11", this.anyDate("MMddHHmmss"));
            ISOdetails.put("field11", this.anyDate("HHmmss"));
            //ISOdetails.put("field12", this.anyDate("HHmmss"));
            //ISOdetails.put("field13", this.anyDate("MMdd"));
            //ISOdetails.put("field15", this.anyDate("MMdd"));
            //ISOdetails.put("field17", this.anyDate("MMdd"));

            ISOdetails.put("field24", "MM");

            //ISOdetails.put("32", SOURCE_ID);
            ISOdetails.put("field32", "POS"); //FOR NOW
//            if (!strfield33.isEmpty()) {
//                ISOdetails.put("field33", strfield33);
//            }

            ISOdetails.put("field37", referenceNumber);
            ISOdetails.put("field41", strField41);
            //ISOdetails.put("field42", "606465ATM000001");
            //ISOdetails.put("field43", "HUDUMA LIFE");
            ISOdetails.put("field49", strField49);
            //ISOdetails.put("field60", "");
            if (strField65.isEmpty() != true) {
                ISOdetails.put("field65", strField65);
                ISOdetails.put("field61", strField65);
            }
            if (strField66.isEmpty() != true) {
                ISOdetails.put("field61", strField66);
            }

            //ISOdetails.put("field67", "");
            ISOdetails.put("field68", narration);
            //ISOdetails.put("field98", strField98);
            if (!strfield100.isEmpty()) {
                ISOdetails.put("field100", strfield100);
                ISOdetails.put("field71", strfield100);
            }
            if (!strfield71.isEmpty()) {
                //ISOdetails.put("field100", strfield100);
                ISOdetails.put("field71", strfield71);
            }

            ISOdetails.put("field101", strAgentID);
            if (strAccountNumber.isEmpty() != true) {
                ISOdetails.put("field102", strAccountNumber);
            }
            if (creditAccount.isEmpty() != true) {
                ISOdetails.put("field103", creditAccount);
            }
            ISOdetails.put("field105", strField41);
            // ISOdetails.put("orig_procode", strProcessingCode);
            // ISOdetails.put("branch102", "000");
            //ISOdetails.put("CorrelationID", referenceNumber);
            // ISOdetails.put("PRD", strFieldPRD);
            // System.out.println(amount);
            System.out.println("ESB_Request :: " + referenceNumber + "\n" + ISOdetails.toString() + "\n\n");
            //log on file too
            //TMSLog el = new TMSLog("MessageToESB", ISOdetails);
            // el.log();
//here we send the transaction to ESB using servlet url
//generate the hashmap into an xml

//Convert the hasmap request to json to send to the servlet.
            Gson gson = new Gson();
            String jsonrequest = gson.toJson(ISOdetails);
//            Parsexml xml = new Parsexml();
//            String xmlrequest = xml.genXMLFromHashMap(ISOdetails);

            //System.out.println("XML_Request :: " + referenceNumber + "\n" + xmlrequest + "\n\n");
            System.out.println("JSON_Request :: " + referenceNumber + "\n" + jsonrequest + "\n\n");
            //String xmlrequeststripped = xml.stripNonValidXMLCharacters(xmlrequest);

            //log to file
            TMSLog elllj = new TMSLog("MessageToESB", jsonrequest);
            elllj.logfile();
            TMSWSCall tmscall = new TMSWSCall();
            String jsonresponse = tmscall.connectToESBWS(jsonrequest);
            System.out.println("JSON_Response :: " + referenceNumber + "\n" + jsonresponse + "\n\n");
            //log to file
            TMSLog elll = new TMSLog("MessageFromESB", jsonresponse);
            elll.logfile();
//            //convert xml response into a hashmap
//            responseMap = xml.genHashMapFromXML(xmlresponse);
//            System.out.println("responseMap :: " + referenceNumber + "\n" + responseMap + "\n\n");

            //convert json response into a hashmap
            responseMap = new Gson().fromJson(jsonresponse, new TypeToken<HashMap<String, String>>() {
            }.getType());
            System.out.println("responseMap :: " + referenceNumber + "\n" + responseMap + "\n\n");

            //log to file
            TMSLog ell = new TMSLog("MessageFromESB", responseMap);
            ell.logfile();

            //log in the db. Add some fields to be recorded fro summary.
            responseMap.put("field88", strFieldPRD);//Identifies type of txn during EOD report.
            int success = spInsertPOSTransactions(responseMap);

            if (responseMap.isEmpty()) {

                //Excempt for processing code 340000
                if (!ISOdetails.get("field3").equals("340000")) {
                    ISOdetails.put("trials", "1");
                    System.out.println("[SENT: Transaction Responses Failed from ESB-:] " + referenceNumber);
                    // System.out.println("[SENT: Transaction Reversed for           -:] " + referenceNumber);
                    // this.generateReversal((HashMap<String, String>) ISOdetails);
                    //Send Failed Response to POS
                    String TransactionType = getTransactionType(ISOdetails.get("field3").toString());
                    String terminalid = strField41;
                    strMessageToPOS += this.strResponseHeader(getTerminalID(terminalid)) + "#";
                    strMessageToPOS += "AGENT ID:  " + ISOdetails.get("field98") + "#";
                    strMessageToPOS += "TRAN NUM:  " + ISOdetails.get("field37") + "#";
                    strMessageToPOS += "--------------------------------" + "#";
                    strMessageToPOS += "                                " + "#";
                    strMessageToPOS += padEqual(TransactionType.toUpperCase()) + "#";
                    strMessageToPOS += "                                " + "#";
                    strMessageToPOS += "   NO RESPONSE FROM ESB GATEWAY " + "#";
                    strMessageToPOS += " " + "#";
                    strMessageToPOS += this.strResponseFooter(terminalid) + "#";
                    SendPOSResponse(strMessageToPOS, ISOdetails.get("field37").toString());

                }
            } else {
                System.out.println("[SENT: Transaction Response Received for  -:] " + referenceNumber);

                response = this.genHashDelimiterString(responseMap, referenceNumber);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();

        }

        return null;
    }

    public HashMap getESBResponse_v2(String strAgentID, String cardNumber, String processingCode, String amount, String referenceNumber, String narration, String transactionIdentifier, String strAccountNumber, String creditAccount, String strField65, String Phonenumber, String strField68, String strField24, String strField98, String strField41, String beneficiaryBanksortcode, String Secretcode) throws InterruptedException {
        String response = "";
        String strField_02 = "";
        String strField_00 = "";
        String strFieldPRD = "";
        String strProcessingCode = "";
        String strMessageToPOS = "";
        String strField49 = "KES";
        String strfield100 = "";
        String strfield33 = "";
        String strfield65 = "";
        String strField66 = "";
        String strfield28 = "";

        HashMap<String, String> responseMap = new HashMap<String, String>();

        try {

            strField_02 = cardNumber;

            switch (processingCode) {
                case "120000": // MERCHANT PAYMENTS
                    strProcessingCode = "010000";
                    strField_00 = "1200";
                    strFieldPRD = "CHWL";
                    narration = "PAYMENT OF GOODS & SERVICES FOR " + strAccountNumber;
                    break;
                case "310000":
                    strProcessingCode = processingCode;
                    strField_00 = "1200";
                    narration = "BALANCE ENQUIRY FOR ACCOUNT " + strAccountNumber;
                    strField24 = "CC";
                    strField49 = "KES";
                    strfield100 = "CC";
                    break;

                case "320000": // AGENT FLOAT (we do BI for Agent float)
                    strProcessingCode = "310000";
                    strField_00 = "1200";
                    narration = "AGENT FLOAT FOR ACCOUNT " + strAccountNumber;
                    break;
                case "380000":
                    strProcessingCode = processingCode;
                    strField_00 = "1200";
                    narration = "MINI STATEMENT FOR ACCOUNT " + strAccountNumber;
                    strField24 = "CC";
                    strField49 = "KES";
                    strfield100 = "CC";
                    strField98 = "MINI_POS";
                    break;
                case "340000": // CARD ACTIVATION
                    strProcessingCode = processingCode;
                    strField_00 = "1200";
                    strField65 = Phonenumber;
                    strField66 = Phonenumber;
                    narration = "ACCOUNT MOBILE ACTIVATION" + strField_02;
                    strField24 = "CC";
                    strField49 = "KES";
                    strfield100 = "CC";
                    strField98 = "ACC_ACT";

                    break;
                case "010000": // CASH WITHDRAWAL
                    strProcessingCode = processingCode;
                    strFieldPRD = "AGENT_WD";
                    strField_00 = "1200";
                    strField24 = "CC";
                    strField49 = "KES";
                    strfield100 = "CC";
                    strField98 = "AGENT_WD";
                    break;
                case "500000": // BILL PAYMENTS
                    strProcessingCode = processingCode;
                    strFieldPRD = "";
                    strField_00 = "1200";
                    strField49 = "KES";
                    strField98 = transactionIdentifier;
                    strfield100 = transactionIdentifier;
                    strField24 = "CC";
                    strField66 = Phonenumber;
                    narration = "BILL PAYMENT FOR ACCOUNT " + strAccountNumber;
                    break;

                case "510000": // BILL presentment
                    strProcessingCode = processingCode;
                    strFieldPRD = "";
                    strField_00 = "1200";
                    strField49 = "KES";
                    strField98 = transactionIdentifier;
                    strfield100 = transactionIdentifier;
                    strField24 = "CC";
                    narration = "BILL PAYMENT FOR ACCOUNT " + strField65;
                    strfield28 = "100";
                    break;
                case "630000": // CARDLESS FULFILMENT
                    strProcessingCode = processingCode;
                    strFieldPRD = "";
                    strField_00 = "1200";
                    strField49 = "KES";
                    strField98 = transactionIdentifier;
                    strfield100 = transactionIdentifier;
                    strField24 = "CC";
                    strField66 = Secretcode;
                    break;
                case "400000": // FUNDS TRANSFER
                    strProcessingCode = processingCode;
                    strFieldPRD = "AGFT";
                    strField_00 = "1200";
                    strField98 = "FT_CC_POS";
                    strfield100 = "";
                    strfield33 = beneficiaryBanksortcode;

                    if (strField24.toUpperCase().contains("CC")) {

                        strField_00 = "1200";
                        strField49 = "KES";
                        strfield100 = "CC";
                    }
                    break;

                case "420000": // Topup
                    strProcessingCode = processingCode;
                    strFieldPRD = "";
                    strField_00 = "1200";
                    narration = "AIRTIME TOPUP FOR ACCOUNT " + strAccountNumber;
                    strField98 = transactionIdentifier;
                    strfield100 = transactionIdentifier;
                    if (strField24.toUpperCase().contains("CC")) {

                        strField_00 = "1200";
                        strField49 = "KES";

                    }
                    break;

                case "210000": // CASH DEPOSIT
                    strProcessingCode = processingCode;
                    strFieldPRD = "CHDP";
                    strField_00 = "1200";
                    strField98 = "AGENT_DP";
                    strField49 = "KES";
                    strfield100 = "";

                    if (strField24.toUpperCase().contains("CC")) {

                        strField_00 = "1200";
                        strField49 = "KES";
                        strfield100 = "CC";
                        strField98 = "AGENT_DP";
                    }

                    break;
                case "360000":
                    strProcessingCode = processingCode;
                    strField24 = "CC";
                    strfield100 = "ACCLOOKUP";
                    strField98 = "ACCLOOKUP";
                    strField_00 = "1200";
                    strFieldPRD = "ACCLOOKUP";
                    break;

                default:
                    strField_00 = "1200";
                    strProcessingCode = processingCode;
                    break;
            }

            HashMap<String, String> ISOdetails = new HashMap<>();
            Map<String, String> cols = new HashMap<>();

            ISOdetails.put("field0", strField_00);
            if (!strField_02.isEmpty()) {
                ISOdetails.put("field2", strField_02);
            }

            ISOdetails.put("field3", strProcessingCode);
            if (!amount.isEmpty()) {
                ISOdetails.put("field4", amount);
            } else {
                ISOdetails.put("field4", "0");
            }

            ISOdetails.put("field7", this.anyDate("YYYYMMddHH"));
            //ISOdetails.put("11", this.anyDate("MMddHHmmss"));
            ISOdetails.put("field11", this.anyDate("HHmmss"));
            ISOdetails.put("field12", referenceNumber);
            ISOdetails.put("field13", this.anyDate("MMdd"));
            ISOdetails.put("field15", this.anyDate("MMdd"));
            ISOdetails.put("field17", this.anyDate("MMdd"));

            ISOdetails.put("field24", strField24);

            //ISOdetails.put("32", SOURCE_ID);
            ISOdetails.put("field32", "POS");
            if (!strfield33.isEmpty()) {
                ISOdetails.put("field33", strfield33);
            }

            ISOdetails.put("field37", referenceNumber);
            ISOdetails.put("field41", strField41.substring(0, 8));
            ISOdetails.put("field42", "606465ATM000001");
            ISOdetails.put("field43", "HUDUMA                                     ");
            ISOdetails.put("field49", strField49);
            ISOdetails.put("field60", "ONUS");
            if (strField65.isEmpty() != true) {
                ISOdetails.put("field65", strField65);
            }
            if (strField66.isEmpty() != true) {
                ISOdetails.put("field66", strField66);
            }

            ISOdetails.put("field67", "sync");
            ISOdetails.put("field68", narration);
            ISOdetails.put("field98", strField98);
            ISOdetails.put("field100", strfield100);
            ISOdetails.put("field101", strAgentID);
            if (strAccountNumber.isEmpty() != true) {
                ISOdetails.put("field102", strAccountNumber);
            }
            if (creditAccount.isEmpty() != true) {
                ISOdetails.put("field103", creditAccount);
            }
            ISOdetails.put("field105", strField41);
            ISOdetails.put("orig_procode", strProcessingCode);
            ISOdetails.put("branch102", "000");
            ISOdetails.put("CorrelationID", referenceNumber);
            ISOdetails.put("PRD", strFieldPRD);
            // System.out.println(amount);
            System.out.println("ESB_Request :: " + referenceNumber + "\n" + ISOdetails.toString() + "\n\n");
            //log on file too
            // TMSLog el = new TMSLog("MessageToESB", ISOdetails);
            // el.log();
//here we write to the queu at this point
            Boolean sentToJboss = false;
            int trials = 0;
            do {

                //QueueWriter qr = new QueueWriter(Configs.ESB_Request_Queue);//();
                //sentToJboss = qr.sendObject(ISOdetails, referenceNumber);
                trials++;
            } while (sentToJboss == false & trials < 3);

            System.out.println("Sent status :" + sentToJboss.toString());
            if (sentToJboss) {
                long Start = System.currentTimeMillis();
                long Stop = Start + (30 * 1000);

                do {
                    TimeUnit.MILLISECONDS.sleep(200);
                    //readFromQueue = new ReadFromQueue(props);
                    //
                    //responseMap = readFromQueue.readfromQueue(Configs.ESB_Response_Queue.trim(), referenceNumber);
                    // responseMap = readFromQueue.readfromQueue("java:jboss/exported/jms/queue/ESBChannelResponse_Queue", referenceNumber);

                } while (responseMap.isEmpty() && System.currentTimeMillis() < Stop);

                System.out.println("ESB Response: \n\n" + responseMap.toString() + "\n\n");
            }
//log in the db
            // String suc = spInsertPOSTransaction(responseMap);
//log to file
            TMSLog ell = new TMSLog("MessageFromESB", responseMap);
            ell.logfile();
            if (responseMap.isEmpty()) {

                //Excempt for processing code 340000
                if (!ISOdetails.get("3").equals("340000")) {
                    ISOdetails.put("trials", "1");
                    System.out.println("[SENT: Transaction Responses Failed from ESB-:] " + referenceNumber);
                    // System.out.println("[SENT: Transaction Reversed for           -:] " + referenceNumber);
                    // this.generateReversal((HashMap<String, String>) ISOdetails);
                    //Send Failed Response to POS
                    String TransactionType = getTransactionType(ISOdetails.get("3"));
                    String terminalid = strField41;
                    strMessageToPOS += this.strResponseHeader(getTerminalID(terminalid)) + "#";
                    strMessageToPOS += "AGENT ID:  " + ISOdetails.get("98") + "#";
                    strMessageToPOS += "TRAN NUM:  " + ISOdetails.get("37") + "#";
                    strMessageToPOS += "--------------------------------" + "#";
                    strMessageToPOS += "                                " + "#";
                    strMessageToPOS += padEqual(TransactionType.toUpperCase()) + "#";
                    strMessageToPOS += "                                " + "#";
                    strMessageToPOS += "   NO RESPONSE FROM ESB GATEWAY " + "#";
                    strMessageToPOS += " " + "#";
                    strMessageToPOS += this.strResponseFooter(terminalid) + "#";
                    SendPOSResponse(strMessageToPOS, ISOdetails.get("37"));

                }
            } else {
                System.out.println("[SENT: Transaction Response Received for  -:] " + referenceNumber);

                response = this.genHashDelimiterString(responseMap, referenceNumber);
            }

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
            // this.log("Error on getESBResponse " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "getESBResponse");
        }

        return null;
    }
    //-----------------
    //--------------------
    //    public HashMap getWeblogicMessageFromQueue(String JMSCorrelationID) {
    //
    //        String url = AGENCY_ADAPTER_URL;
    //        String QUEUE = QUEUE_RESPONSE;
    //        Message msg = null;
    //        Object ResponseMessage = null;
    //        HashMap fields = new HashMap();
    //        int loops = 1;
    //        try {
    //            while (true) {
    //                if (loops > 5) {
    //                    break;
    //                }
    //
    //                DistributedWebLogicQueueBrowser distrWebLogic = new DistributedWebLogicQueueBrowser();
    //                msg = distrWebLogic.browseWebLogicQueue(JMSCorrelationID, url, QUEUE);
    //                if (msg instanceof ObjectMessage) {
    //                    ResponseMessage = ((ObjectMessage) msg).getObject();
    //                    fields = (HashMap) ResponseMessage;
    //                    this.log("RESPONSE ::  " + JMSCorrelationID + "\n" + fields.toString() + "\n\n", "ESB_Response");
    //                    break;
    //                }
    //                distrWebLogic = null;
    //                loops++;
    //            }
    //        } catch (JMSException ex) {
    //            this.log("Error getWeblogicMessageFromUDQueue() " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "error--");
    //        }
    //        return fields;
    //    }

    public String CustomerCopy() {
        String strResponse = "";
        strResponse = "********* CUSTOMER COPY ********" + "#";
        strResponse += " " + "#";
        return strResponse;
    }

    public String AgentCopy() {
        String strResponse = "";
        strResponse = "********** AGENT COPY **********" + "#";
        strResponse += "  " + "#";
        return strResponse;
    }

    public String stringBuilder(String s) {
        StringBuilder sb = new StringBuilder(s);

        int i = 0;
        while ((i = sb.indexOf(" ", i + 30)) != -1) {
            sb.replace(i, i + 1, "#");
        }
        return sb.toString();
    }

    public String genHashDelimiterString(Map<String, String> hashDelimiterdetails, String intid) throws IOException, SQLException {

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "kenya"));
        String hashDelimiterString = "";
        String agentTeller;
        String strMessage;
        String strMessage1;
        String strReasonDescription;
        String strAvailableBalance = "";
        String strActualBalance = "";
        String cardlesssmsmsg;
        String strCustomername = "";
        String[] strBalance = null;
        String strAccountNo = "";
        String strStatementPrinting = "";
        String strRRN = "";

        if (hashDelimiterdetails.get("field39").equals("00")) {
            hashDelimiterString += CustomerCopy();
        } else {
            hashDelimiterString += AgentCopy();
        }

        hashDelimiterString += this.strResponseHeader(hashDelimiterdetails.get("field105"));
        hashDelimiterString += "AGENT ID:  " + hashDelimiterdetails.get("field101") + "#";
        strRRN = hashDelimiterdetails.get("field37");
        if (hashDelimiterdetails.get("field68").contains("AGENT FLOAT")) {
            hashDelimiterdetails.put("field3", "320000");
        }
        if (hashDelimiterdetails.get("field68").contains("AIRTIME TOPUP")) {
            hashDelimiterdetails.put("field3", "420000");
        }//AGENT MINISTATEMENT

        if (hashDelimiterdetails.get("field68").contains("AGENT COMMISSION MINISTATEMENT")) {
            hashDelimiterdetails.put("field3", "380000");
        }
        if (hashDelimiterdetails.get("field68").contains("AGENT COMMISSION BAL")) {
            hashDelimiterdetails.put("field3", "310000");
        }

        //Get Processing Code
        switch (hashDelimiterdetails.get("field3")) {

            case "010000":
                //Cash Withdrawal

                hashDelimiterString += "TRAN NUM:  " + hashDelimiterdetails.get("field37") + "#";
                hashDelimiterString += "--------------------------------" + "#";
                hashDelimiterString += "                                " + "#";
                strReasonDescription = this.getResponseCode(hashDelimiterdetails.get("field39"));

                switch (hashDelimiterdetails.get("field39")) {
                    case "00":

                        try {
                            if (hashDelimiterdetails.containsKey("field102")) {
                                strAccountNo = hashDelimiterdetails.get("field102");
                                if (strAccountNo.length() == 14) {
                                    String Maskedpart = strAccountNo.substring(6, 12);
                                    strAccountNo = strAccountNo.replace(Maskedpart, "******");
                                } else {
                                    String Maskedpart = strAccountNo.substring(6, 11);
                                    strAccountNo = strAccountNo.replace(Maskedpart, "******");
                                }
                            }
                            String amountString = formatter.format(Double.valueOf(hashDelimiterdetails.get("field4"))).replace("KES", "");
                            amountString = amountString.replace("¤", "");

                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "         CASH WITHDRAWAL        " + "#";
                            hashDelimiterString += "    CASH WITHDRAWAL SUCCESSFUL  " + "#";
                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "ACCOUNT NUM:  " + strAccountNo + "#";
                            hashDelimiterString += "AMOUNT:   KES " + amountString + "#";
                            hashDelimiterString += "                                " + "#";

                        } catch (Exception ex) {
                            //this.log("INFO : Function genHashDelimiterString   " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
                        }

                        break;
                    default:
                        hashDelimiterString += "                                " + "#";
                        hashDelimiterString += "         CASH WITHDRAWAL        " + "#";
                        hashDelimiterString += "  " + stringBuilder(strReasonDescription.toString().toUpperCase()) + "#";
                        hashDelimiterString += "                                " + "#";

                        break;
                }

                hashDelimiterString += this.strResponseFooter(hashDelimiterdetails.get("field105"));
                if (hashDelimiterdetails.get("field39").equals("00")) {
                    String strReplace = hashDelimiterString.replace("CUSTOMER COPY", "AGENT COPY");
                    hashDelimiterString += "#9999#" + strReplace;
                }
                this.SendPOSResponse(strRRN + "#" + hashDelimiterString, intid);

                updateTransactionResponse(hashDelimiterdetails, intid, hashDelimiterString);
                break;

            case "120000":
                //Merchant Payments

                hashDelimiterString += "TRAN NUM:  " + hashDelimiterdetails.get("field37") + "#";
                hashDelimiterString += "--------------------------------" + "#";
                hashDelimiterString += "                                " + "#";
                strReasonDescription = this.getResponseCode(hashDelimiterdetails.get("field39"));

                switch (hashDelimiterdetails.get("field39")) {
                    case "00":

                        try {
                            strAccountNo = hashDelimiterdetails.get("field102").toString();
                            if (strAccountNo.length() == 14) {
                                String Maskedpart = strAccountNo.substring(6, 12);
                                strAccountNo = strAccountNo.replace(Maskedpart, "******");
                            } else {
                                String Maskedpart = strAccountNo.substring(6, 11);
                                strAccountNo = strAccountNo.replace(Maskedpart, "******");
                            }
                            String amountString = formatter.format(Double.valueOf(hashDelimiterdetails.get("field4"))).replace("KES", "");

                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "         MERCHANT SERVICES        " + "#";
                            hashDelimiterString += "    MERCHANT SERVICES SUCCESSFUL  " + "#";
                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "ACCOUNT NUM:  " + strAccountNo + "#";
                            hashDelimiterString += "AMOUNT:   KES " + amountString + "#";
                            hashDelimiterString += "                                " + "#";

                        } catch (Exception ex) {
                            // this.log("INFO : Function genHashDelimiterString   " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
                        }

                        break;
                    default:
                        hashDelimiterString += "                                " + "#";
                        hashDelimiterString += "        MERCHANT SERVICES       " + "#";
                        hashDelimiterString += "  " + stringBuilder(strReasonDescription.toString().toUpperCase()) + "#";
                        hashDelimiterString += "                                " + "#";

                        break;
                }

                hashDelimiterString += this.strResponseFooter(hashDelimiterdetails.get("field105"));
                if (hashDelimiterdetails.get("field39").equals("00")) {
                    String strReplace = hashDelimiterString.replace("CUSTOMER COPY", "AGENT COPY");
                    hashDelimiterString += strReplace;
                }
                this.SendPOSResponse(strRRN + "#" + hashDelimiterString, intid);

                updateTransactionResponse(hashDelimiterdetails, intid, hashDelimiterString);
                break;

            case "510000":
                // Bill presentment
                String f127 = "";
                if (hashDelimiterdetails.containsKey("field127")) {
                    f127 = hashDelimiterdetails.get("field127");
                    f127 = f127.replace("|", "#");
                }
                hashDelimiterString = "";
                //present details in field 48 separated by # after change above
                switch (hashDelimiterdetails.get("field39")) {
                    case "00":
                        hashDelimiterString = f127 + "#";
                        break;
                    default:
                        hashDelimiterString = "Failed#0";
                        break;
                }

                this.SendPOSResponse(hashDelimiterString, intid);
                updateTransactionResponse(hashDelimiterdetails, intid, hashDelimiterString);
                break;

            case "340000":
                hashDelimiterString += "TRAN NUM:  " + hashDelimiterdetails.get("field37") + "#";
                hashDelimiterString += "--------------------------------" + "#";
                hashDelimiterString += "                                " + "#";
                strReasonDescription = this.getResponseCode(hashDelimiterdetails.get("field39"));

                switch (hashDelimiterdetails.get("field39")) {
                    case "00":
                        hashDelimiterString += "                                " + "#";
                        hashDelimiterString += "         CARD ACTIVATION        " + "#";
                        hashDelimiterString += "   CARD ACTIVATION SUCCESSFUL   " + "#";
                        hashDelimiterString += "   REQUEST BEING PROCECESSED    " + "#";
                        break;
                    default:
                        hashDelimiterString += "                                " + "#";
                        hashDelimiterString += "         CARD ACTIVATION        " + "#";
                        hashDelimiterString += "                                " + "#";
                        hashDelimiterString += "      CARD ACTIVATION FAILED    " + "#";
                        hashDelimiterString += "   " + stringBuilder(strReasonDescription.toUpperCase()) + "#";
                        hashDelimiterString += "                                " + "#";

                        break;
                }
                hashDelimiterString += this.strResponseFooter(hashDelimiterdetails.get("field105"));
                this.SendPOSResponse(hashDelimiterString, intid);
                updateTransactionResponse(hashDelimiterdetails, intid, hashDelimiterString);
                break;
            case "360000":
                hashDelimiterString = "";
                //customer name in field127
                switch (hashDelimiterdetails.get("field39")) {
                    case "00":
                        if (hashDelimiterdetails.containsKey("field127")) {
                            hashDelimiterString = hashDelimiterdetails.get("field127");
                        }
                        break;
                    default:
                        hashDelimiterString = "Lookup failed";
                        break;
                }

                this.SendPOSResponse(hashDelimiterString, intid);
                updateTransactionResponse(hashDelimiterdetails, intid, hashDelimiterString);
                break;
            case "210000":
                // Cash Deposit

                hashDelimiterString += "TRAN NUM:  " + hashDelimiterdetails.get("field37") + "#";
                hashDelimiterString += "--------------------------------" + "#";
                hashDelimiterString += "                                " + "#";
                strReasonDescription = this.getResponseCode(hashDelimiterdetails.get("field39"));
                String amountString = formatter.format(Double.valueOf(hashDelimiterdetails.get("field4"))).replace("KES", "");
                amountString = amountString.replace("¤", "");

                switch (hashDelimiterdetails.get("field39")) {
                    case "00":

                        try {
                            strAccountNo = hashDelimiterdetails.get("field103").toString();
                            if (strAccountNo.length() == 14) {
                                String Maskedpart = strAccountNo.substring(6, 12);
                                strAccountNo = strAccountNo.replace(Maskedpart, "******");
                            } else {
                                String Maskedpart = strAccountNo.substring(6, 11);
                                strAccountNo = strAccountNo.replace(Maskedpart, "******");
                            }
                            String strAccountNumber = hashDelimiterdetails.get("field103").toString();
                            // String RecipientName = "ACC NAME:" + getCustomerDetails((hashDelimiterdetails.get("field103")));
                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "          CASH DEPOSIT          " + "#";
                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "     CASH DEPOSIT SUCCESSFUL    " + "#";
                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "AMOUNT:      KES " + amountString + "#";
                            hashDelimiterString += "TO ACCOUNT:      " + strAccountNo + "#";
                            //hashDelimiterString += stringBuilder(RecipientName) + "#";

                            hashDelimiterString += "                                " + "#";

                        } catch (Exception ex) {
                            //this.log("INFO : Function genHashDelimiterString   " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
                        }

                        break;

                    default:
                        hashDelimiterString += "                                " + "#";
                        hashDelimiterString += "          CASH DEPOSIT          " + "#";
                        hashDelimiterString += "                                " + "#";
                        hashDelimiterString += "        CASH DEPOSIT FAILED    " + "#";
                        hashDelimiterString += stringBuilder(strReasonDescription.toUpperCase()) + "#";
                        hashDelimiterString += "                                " + "#";

                        break;

                }
                hashDelimiterString += this.strResponseFooter(hashDelimiterdetails.get("field105"));
                if (hashDelimiterdetails.get("field39").equals("00")) {
                    String strReplace = hashDelimiterString.replace("CUSTOMER COPY", "AGENT COPY");
                    hashDelimiterString += "#9999#" + strReplace;
                }
                this.SendPOSResponse(strRRN + "#" + hashDelimiterString, intid);
                updateTransactionResponse(hashDelimiterdetails, intid, hashDelimiterString);
                break;

            case "310000":
                //Balance Inquiry

                hashDelimiterString += "TRAN NUM:  " + hashDelimiterdetails.get("field37") + "#";
                hashDelimiterString += "--------------------------------" + "#";
                hashDelimiterString += "                                " + "#";
                strReasonDescription = this.getResponseCode(hashDelimiterdetails.get("field39"));
                switch (hashDelimiterdetails.get("field39")) {
                    case "00":

                        try {
                            String strAccountNumber = "";
                            String[] strBal = hashDelimiterdetails.get("field54").split("\\|");
                            if (hashDelimiterdetails.containsKey("field102") && !hashDelimiterdetails.get("field102").isEmpty()) {
                                strAccountNumber = hashDelimiterdetails.get("field102").toString();
                                strAccountNo = strAccountNumber;
                                if (strAccountNo.length() == 14) {
                                    String Maskedpart = strAccountNo.substring(6, 12);
                                    strAccountNo = strAccountNo.replace(Maskedpart, "******");
                                } else {
                                    String Maskedpart = strAccountNo.substring(6, 11);
                                    strAccountNo = strAccountNo.replace(Maskedpart, "******");
                                }
                            }

                            strAvailableBalance = formatter.format(Double.valueOf(strBal[0])).replace("KES", "");
                            strActualBalance = formatter.format(Double.valueOf(strBal[1])).replace("KES", "");
                            strAvailableBalance = formatter.format(Double.valueOf(strBal[0])).replace("¤", "");
                            strActualBalance = formatter.format(Double.valueOf(strBal[1])).replace("¤", "");

                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "        BALANCE ENQUIRY         " + "#";
                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "ACCOUNT NUM:      " + (strAccountNo) + "#";
                            hashDelimiterString += "AVAIL BALANCE: KES " + strAvailableBalance + "#";
                            hashDelimiterString += "ACTUAL BALANCE:KES " + strActualBalance + "#";
                            hashDelimiterString += "                                " + "#";

                        } catch (Exception ex) {
                            //this.log("INFO : Function genHashDelimiterString   " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
                        }
                        break;
                    default:
                        hashDelimiterString += "                                " + "#";
                        hashDelimiterString += "        BALANCE ENQUIRY         " + "#";
                        hashDelimiterString += "                                " + "#";
                        hashDelimiterString += stringBuilder(hashDelimiterdetails.get("field48").toUpperCase()) + "#";
                        hashDelimiterString += "                                " + "#";

                        break;

                }
                hashDelimiterString += this.strResponseFooter(hashDelimiterdetails.get("field105"));
                this.SendPOSResponse(strRRN + "#" + hashDelimiterString, intid);
                updateTransactionResponse(hashDelimiterdetails, intid, hashDelimiterString);
                break;

            case "320000":
                // Agent balances
                hashDelimiterString += "TRAN NUM:  " + hashDelimiterdetails.get("field37") + "#";
                hashDelimiterString += "--------------------------------" + "#";
                hashDelimiterString += "                                " + "#";
                strReasonDescription = this.getResponseCode(hashDelimiterdetails.get("field39"));
                switch (hashDelimiterdetails.get("field39")) {
                    case "00":
                        try {
                            String[] strBal = hashDelimiterdetails.get("field54").split("\\|");
                            String strAccountNumber = hashDelimiterdetails.get("field102").toString();
                            strAvailableBalance = formatter.format(Double.valueOf(strBal[0])).replace("KES", "");
                            strActualBalance = formatter.format(Double.valueOf(strBal[1])).replace("KES", "");
                            strAvailableBalance = formatter.format(Double.valueOf(strBal[0])).replace("¤", "");
                            strActualBalance = formatter.format(Double.valueOf(strBal[1])).replace("¤", "");
                            strAccountNo = hashDelimiterdetails.get("field102").toString();
                            if (strAccountNo.length() == 14) {
                                String Maskedpart = strAccountNo.substring(6, 12);
                                strAccountNo = strAccountNo.replace(Maskedpart, "******");
                            } else {
                                String Maskedpart = strAccountNo.substring(6, 11);
                                strAccountNo = strAccountNo.replace(Maskedpart, "******");
                            }
                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "     AGENT BALANCE ENQUIRY      " + "#";
                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "ACCOUNT NUM:     " + strAccountNo + "#";
                            hashDelimiterString += "AVAIL BALANCE:  KES" + strAvailableBalance + "#";
                            hashDelimiterString += "ACTUAL BALANCE: KES" + strActualBalance + "#";
                            hashDelimiterString += "                                " + "#";

                        } catch (Exception ex) {
                            //this.log("INFO : Function genHashDelimiterString   " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
                        }
                        break;
                    default:
                        hashDelimiterString += "                                " + "#";
                        hashDelimiterString += "     AGENT BALANCE ENQUIRY      " + "#";
                        hashDelimiterString += "                                " + "#";
                        hashDelimiterString += stringBuilder(hashDelimiterdetails.get("field48")) + "#";
                        hashDelimiterString += "                                " + "#";
                        break;
                }
                hashDelimiterString += this.strResponseFooter(hashDelimiterdetails.get("field105"));
                this.SendPOSResponse(hashDelimiterString, intid);
                updateTransactionResponse(hashDelimiterdetails, intid, hashDelimiterString);
                break;

            case "380000":
                // Mini Statement
                hashDelimiterString += "TRAN NUM:  " + hashDelimiterdetails.get("field37") + "#";
                hashDelimiterString += "--------------------------------" + "#";
                hashDelimiterString += "                                " + "#";

                try {
                    if (hashDelimiterdetails.containsKey("field127") && hashDelimiterdetails.get("field127").contains("~")) {
                        String[] strStatementData = hashDelimiterdetails.get("field127").split("~");

                        for (int i = 0; i < strStatementData.length; i++) {

                            if (i > 0) {

                                // String dates = strStatementData[i].substring(0, 10);
                                //String restofdata = strStatementData[i].substring(12).trim();
                                // strStatementPrinting += dates.trim() + "  " + restofdata + "#";
                                String restofdata = strStatementData[i].trim();
                                strStatementPrinting += restofdata + "#";

                            } else {
                                strStatementPrinting += strStatementData[i].trim() + "#";
                            }

                        }
                    }

                    switch (hashDelimiterdetails.get("field39")) {
                        case "00":
                            strAccountNo = hashDelimiterdetails.get("field102");
                            if (strAccountNo.length() == 14) {
                                String Maskedpart = strAccountNo.substring(6, 12);
                                strAccountNo = strAccountNo.replace(Maskedpart, "******");
                            } else {
                                String Maskedpart = strAccountNo.substring(6, 11);
                                strAccountNo = strAccountNo.replace(Maskedpart, "******");
                            }

                            String[] strBal = hashDelimiterdetails.get("field54").split("\\|");
                            strAvailableBalance = formatter.format(Double.valueOf(strBal[0])).replace("KES", "");
                            strActualBalance = formatter.format(Double.valueOf(strBal[1])).replace("KES", "");
                            strAvailableBalance = formatter.format(Double.valueOf(strBal[0])).replace("¤", "");
                            strActualBalance = formatter.format(Double.valueOf(strBal[1])).replace("¤", "");

                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "      MINISTATEMENT ENQUIRY     " + "#";
                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "ACCOUNT NUM:     " + (strAccountNo) + "#";
                            hashDelimiterString += strStatementPrinting + "#";
                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "--------------------------------" + "#";
                            hashDelimiterString += "AVAIL BALANCE: KES " + strAvailableBalance + "#";
                            hashDelimiterString += "ACTUAL BALANCE:KES " + strActualBalance + "#";
                            break;

                        default:
                            strReasonDescription = this.getResponseCode(hashDelimiterdetails.get("field39"));
                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "      MINISTATEMENT ENQUIRY     " + "#";
                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += stringBuilder(hashDelimiterdetails.get("field48").toUpperCase()) + "#";
                            hashDelimiterString += "                                " + "#";
                            break;
                    }
                    hashDelimiterString += this.strResponseFooter(hashDelimiterdetails.get("field105"));
                    this.SendPOSResponse(strRRN + "#" + hashDelimiterString, intid);
                    updateTransactionResponse(hashDelimiterdetails, intid, hashDelimiterString);
                    break;

                } catch (Exception ex) {
                    ex.printStackTrace();
                    // this.log("INFO : Function genHashDelimiterString   " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
                }

                break;
            case "400000":
                // FUNDS TRANSFER

                hashDelimiterString += "TRAN NUM:  " + hashDelimiterdetails.get("field37") + "#";
                hashDelimiterString += "--------------------------------" + "#";
                hashDelimiterString += "                                " + "#";
                strReasonDescription = this.getResponseCode(hashDelimiterdetails.get("field39"));
                String StramountString = formatter.format(Double.valueOf(hashDelimiterdetails.get("field4"))).replace("KES", "");
                StramountString = StramountString.replace("¤", "");

                switch (hashDelimiterdetails.get("field39")) {
                    case "00":

                        try {
                            String strAccountNumber = hashDelimiterdetails.get("field103").toString();

                            strAccountNo = hashDelimiterdetails.get("field102");
                            if (strAccountNo.length() == 14) {
                                String Maskedpart = strAccountNo.substring(6, 12);
                                strAccountNo = strAccountNo.replace(Maskedpart, "******");
                            } else {
                                String Maskedpart = strAccountNo.substring(6, 11);
                                strAccountNo = strAccountNo.replace(Maskedpart, "******");
                            }
                            //  String SendersName = "ACC NAME:" + getCustomerDetails((hashDelimiterdetails.get("field102")));
                            //  String RecipientsName = "ACC NAME:" + getCustomerDetails((hashDelimiterdetails.get("field103")));

                            // String SendersName = "ACC NAME:");
                            //  String RecipientsName = "ACC NAME:" + getCustomerDetails((hashDelimiterdetails.get("field103")));
                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "          FUNDS TRANSFER          " + "#";
                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "     FUNDS TRANSFER SUCCESSFUL    " + "#";
                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "AMOUNT:       KES " + StramountString + "#";
                            hashDelimiterString += "FROM ACCOUNT:     " + strAccountNo + "#";
                            // hashDelimiterString += stringBuilder(SendersName) + "#";
                            hashDelimiterString += "TO ACCOUNT:       " + (hashDelimiterdetails.get("field103")) + "#";
                            // hashDelimiterString += stringBuilder(RecipientsName) + "#";

                            hashDelimiterString += "                                " + "#";

                        } catch (Exception ex) {
                            //this.log("INFO : Function genHashDelimiterString   " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
                        }
                        break;
                    default:
                        hashDelimiterString += "                                " + "#";
                        hashDelimiterString += "          FUNDS TRANSFER          " + "#";
                        hashDelimiterString += stringBuilder(strReasonDescription.toUpperCase()) + "#";
                        hashDelimiterString += "                                " + "#";

                }
                hashDelimiterString += this.strResponseFooter(hashDelimiterdetails.get("field105"));
                if (hashDelimiterdetails.get("field39").equals("00")) {
                    String strReplace = hashDelimiterString.replace("CUSTOMER COPY", "AGENT COPY");
                    hashDelimiterString += "#9999#" + strReplace;
                }
                this.SendPOSResponse(strRRN + "#" + hashDelimiterString, intid);
                updateTransactionResponse(hashDelimiterdetails, intid, hashDelimiterString);
                break;

            case "420000":
                // Airtime Topup
                hashDelimiterString += "TRAN NUM:  " + hashDelimiterdetails.get("field37") + "#";
                hashDelimiterString += "--------------------------------" + "#";
                hashDelimiterString += "                                " + "#";
                strReasonDescription = this.getResponseCode(hashDelimiterdetails.get("field39"));

                switch (hashDelimiterdetails.get("field39")) {
                    case "00":
                        try {
                            if (hashDelimiterdetails.containsKey("field102")) {
                                strAccountNo = hashDelimiterdetails.get("field102").toString();

                                if (strAccountNo.length() == 14) {
                                    String Maskedpart = strAccountNo.substring(6, 12);
                                    strAccountNo = strAccountNo.replace(Maskedpart, "******");
                                } else {
                                    String Maskedpart = strAccountNo.substring(6, 11);
                                    strAccountNo = strAccountNo.replace(Maskedpart, "******");
                                }
                            }
                            //String strAmount = formatter.format(Double.valueOf(hashDelimiterdetails.get("4")) / 100).replace("KES", "");
                            String strAmount = formatter.format(Double.valueOf(hashDelimiterdetails.get("field4"))).replace("KES", "");
                            strAmount = strAmount.replace("¤", "");

                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "         AIRTIME TOPUP          " + "#";
                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "    AIRTIME TOPUP SUCCESSFUL    " + "#";
                            hashDelimiterString += "ACCOUNT NUM:     " + (strAccountNo) + "#";
                            hashDelimiterString += "AMOUNT:      KES " + strAmount + "#";
                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += " PLEASE WAIT FOR A CONFIRMATION SMS" + "#";

                        } catch (Exception ex) {
                            StringWriter sw = new StringWriter();
                            ex.printStackTrace(new PrintWriter(sw));
                            TMSLog el = new TMSLog(sw.toString());
                            el.logfile();
                            // this.log("INFO : Function genHashDelimiterString   " + ex.getMessage() + "\n" + this.StackTraceWriter(ex), "ERROR");
                        }
                        break;
                    default:
                        hashDelimiterString += "                                " + "#";
                        hashDelimiterString += "         AIRTIME TOPUP          " + "#";
                        hashDelimiterString += "                                " + "#";
                        hashDelimiterString += "      AIRTIME TOPUP FAILED      " + "#";
                        hashDelimiterString += "   " + stringBuilder(strReasonDescription.toUpperCase()) + "#";
                        hashDelimiterString += "                                " + "#";
                        break;
                }

                hashDelimiterString += this.strResponseFooter(hashDelimiterdetails.get("field105"));

                if (hashDelimiterdetails.get("field39").equals("00")) {
                    String strReplace = hashDelimiterString.replace("CUSTOMER COPY", "AGENT COPY");
                    hashDelimiterString += "#9999#" + strReplace;
                }
                this.SendPOSResponse(strRRN + "#" + hashDelimiterString, intid);
                updateTransactionResponse(hashDelimiterdetails, intid, hashDelimiterString);
                break;

            case "500000":
                //Bills Payments
                strReasonDescription = this.getResponseCode(hashDelimiterdetails.get("field39"));
                hashDelimiterString += "TRAN NUM:  " + hashDelimiterdetails.get("field37") + "#";
                hashDelimiterString += "--------------------------------" + "#";
                hashDelimiterString += "                                " + "#";
                switch (hashDelimiterdetails.get("field39")) {
                    case "00":

                        // KPLC PREPAID
                        String strAmount = formatter.format(Double.valueOf(hashDelimiterdetails.get("field4"))).replace("KES", "");
                        strAmount = strAmount.replace("¤", "");
                        String strToken = "";
                        String biller = hashDelimiterdetails.get("field100");
                        switch (biller) {
                            case "KPLC": //KPLC
                                hashDelimiterString += "                                " + "#";
                                hashDelimiterString += "          BILL PAYMENT          " + "#";
                                hashDelimiterString += "     KPLC PAYMENT SUCCESSFUL    " + "#";
                                hashDelimiterString += "                                " + "#";
                                hashDelimiterString += " METER NUM:      " + hashDelimiterdetails.get("field65") + "#";
                                // hashDelimiterString += " TOKEN NUM:      " + hashDelimiterdetails.get("field48") + "#";
                                hashDelimiterString += " AMOUNT PAID:KES " + strAmount + "#";
                                hashDelimiterString += "                                " + "#";
                                break;
                            case "NWSC"://NAIROBI WATER

                                hashDelimiterString += "                                " + "#";
                                hashDelimiterString += "          BILL PAYMENT          " + "#";
                                hashDelimiterString += "   NWSC PAYMENT SUCCESSFUL   " + "#";
                                hashDelimiterString += "                                " + "#";
                                hashDelimiterString += " METER NUM:      " + hashDelimiterdetails.get("field65") + "#";
                                // hashDelimiterString += " TOKEN NUM:      " + hashDelimiterdetails.get("field48") + "#";
                                hashDelimiterString += " AMOUNT PAID:    " + strAmount + "#";
                                hashDelimiterString += "                                " + "#";
                                break;

                            default:
                                hashDelimiterString += "                                " + "#";
                                hashDelimiterString += "          BILL PAYMENT          " + "#";
                                hashDelimiterString += "   " + hashDelimiterdetails.get("field100") + " PAYMENT SUCCESSFUL   " + "#";
                                hashDelimiterString += "                                " + "#";
                                hashDelimiterString += " BILL NUM:      " + hashDelimiterdetails.get("field65") + "#";
                                // hashDelimiterString += " TOKEN NUM:      " + hashDelimiterdetails.get("field48") + "#";
                                hashDelimiterString += " AMOUNT PAID:    " + strAmount + "#";
                                hashDelimiterString += "                                " + "#";
                                hashDelimiterString += " PLEASE WAIT FOR A CONFIRMATION SMS" + "#";
                        }

                        break;
                    default:
                        hashDelimiterString += "                                " + "#";
                        hashDelimiterString += "          BILL PAYMENT          " + "#";
                        hashDelimiterString += "  " + strReasonDescription.toUpperCase() + "#";
                        hashDelimiterString += "                                " + "#";
                        break;

                }

                hashDelimiterString += this.strResponseFooter(hashDelimiterdetails.get("field105"));
                //  if (hashDelimiterdetails.get("field48").toString().trim().equalsIgnoreCase("NOPURCHASE")) {
                //    String strReplace = hashDelimiterString.replace("CUSTOMER COPY", "AGENT COPY");
                //  hashDelimiterString += strReplace;
                // }
                this.SendPOSResponse(strRRN + "#" + hashDelimiterString, intid);
                updateTransactionResponse(hashDelimiterdetails, intid, hashDelimiterString);
                break;
            case "620000":
                //Cardless Origination
                hashDelimiterString += "TRAN NUM:  " + hashDelimiterdetails.get("field37") + "#";
                hashDelimiterString += "--------------------------------" + "#";
                hashDelimiterString += "                                " + "#";
                strReasonDescription = this.getResponseCode(hashDelimiterdetails.get("field39"));
                switch (hashDelimiterdetails.get("field39")) {
                    case "00":
                        String strAmount = "";
                        String[] strBalCa = hashDelimiterdetails.get("field54").split("\\|");
                        try {
                            strAvailableBalance = formatter.format(Double.valueOf(strBalCa[0])).replace("KES", "");
                            strActualBalance = formatter.format(Double.valueOf(strBalCa[1])).replace("KES", "");
                            strAvailableBalance = formatter.format(Double.valueOf(strBalCa[0])).replace("¤", "");
                            strActualBalance = formatter.format(Double.valueOf(strBalCa[1])).replace("¤", "");
                            strAmount = formatter.format(Double.valueOf(hashDelimiterdetails.get("field4"))).replace("KES", "");
                            strAmount = strAmount.replace("¤", "");

                        } catch (Exception ex) {
                            StringWriter sw = new StringWriter();
                            ex.printStackTrace(new PrintWriter(sw));
                            TMSLog el = new TMSLog(sw.toString());
                            el.logfile();
                        }

                        hashDelimiterString += "                                " + "#";
                        hashDelimiterString += "      CARDLESS ORIGINATION      " + "#";
                        hashDelimiterString += "  CARDLESS TRANSFER SUCCESSFUL  " + "#";
                        hashDelimiterString += "                                " + "#";

                        if (hashDelimiterdetails.containsKey("field80")) {
                            //hashDelimiterString += "    Your 4 digit code is " + hashDelimiterdetails.get("80").substring(0, 4) + "#";
                            hashDelimiterString += "    Your 4 digit code is " + hashDelimiterdetails.get("field80") + "#";
                        } else {
                            hashDelimiterString += "    Missing secret code     " + "#";
                        }

                        hashDelimiterString += "Please send it to the Receipient" + "#";
                        hashDelimiterString += "--------------------------------" + "#";
                        hashDelimiterString += "Amount .      " + strAmount + "#";
                        hashDelimiterString += "--------------------------------" + "#";
                        hashDelimiterString += "AVAIL BALANCE: KES " + strAvailableBalance + "#";
                        hashDelimiterString += "ACTUAL BALANCE:KES " + strActualBalance + "#";

                        cardlesssmsmsg = "Dear Customer, Your 4 digit code is " + hashDelimiterdetails.get("field80") + ", kindly send this to the recipient";
                        break;
                    default:

                        strReasonDescription = this.getResponseCode(hashDelimiterdetails.get("field39"));
                        hashDelimiterString += "                                " + "#";
                        hashDelimiterString += "      CARDLESS ORIGINATION      " + "#";
                        hashDelimiterString += "                                " + "#";
                        hashDelimiterString += strReasonDescription.toUpperCase() + "#";
                        hashDelimiterString += hashDelimiterdetails.get("field48") + "#";
                        break;
                }

                hashDelimiterString += this.strResponseFooter(hashDelimiterdetails.get("field105"));
                this.SendPOSResponse(hashDelimiterString, intid);
                updateTransactionResponse(hashDelimiterdetails, intid, hashDelimiterString);
                break;

            case "630000":
                //Cardless Fullfillment
                try {
                    hashDelimiterString += "Ref No:      " + hashDelimiterdetails.get("field37") + "#";
                    strReasonDescription = this.getResponseCode(hashDelimiterdetails.get("field39"));
                    switch (hashDelimiterdetails.get("field39")) {
                        case "00":
                            String strAmount = "0";
                            //BOA cardless amount is in the response of field5
//                            if (hashDelimiterdetails.containsKey("field5")) {
//                                strAmount = formatter.format(Double.valueOf(hashDelimiterdetails.get("field5"))).replace("KES", "");
//                            } else {
                            //strAmount = formatter.format(Double.valueOf(hashDelimiterdetails.get("field4"))).replace("KES", "");
                            strAmount = formatter.format(Double.valueOf(hashDelimiterdetails.get("field4"))).replace("KES", "");
                            strAmount = strAmount.replace("¤", "");
                            //}

                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "      CARDLESS FULLFILMENT      " + "#";
                            hashDelimiterString += "CARDLESS FULLFILLMENT SUCCESSFUL" + "#";
                            hashDelimiterString += "AMOUNT:      " + strAmount + "#";
                            hashDelimiterString += "                                " + "#";
                            break;
                        default:
                            strReasonDescription = this.getResponseCode(hashDelimiterdetails.get("field39"));
                            hashDelimiterString += "                                " + "#";
                            hashDelimiterString += "      CARDLESS FULLFILMENT      " + "#";
                            hashDelimiterString += strReasonDescription.toUpperCase() + "#";
                            hashDelimiterString += stringBuilder(hashDelimiterdetails.get("field48").toUpperCase()) + "#";
                            hashDelimiterString += "                                " + "#";
                            break;
                    }

                    hashDelimiterString += this.strResponseFooter(hashDelimiterdetails.get("field105"));
                    this.SendPOSResponse(hashDelimiterString, intid);
                    updateTransactionResponse(hashDelimiterdetails, intid, hashDelimiterString);
                } catch (Exception ex) {
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    TMSLog el = new TMSLog(sw.toString());
                    el.logfile();
                    //this.log("\nINFO genHashDelimeterString() :: " + ex.getMessage() + "\n" + StackTraceWriter(ex), "ERROR");
                }
                break;

            default:
                hashDelimiterString = "TRANSACTION CODE NOT DEFINED#--------------------------------#";
                hashDelimiterString += "";
                this.SendPOSResponse(hashDelimiterString, intid);
                //updateTransactionResponse(hashDelimiterdetails, intid, hashDelimiterString);
                break;
        }

        return hashDelimiterString;
    }

    public String getTransactionType(String transactionCode) {
        String TransactionType = "";

        switch (transactionCode) {
            case "010000":
                TransactionType = "Cash Withdrawal";
                break;
            case "310000":
                TransactionType = "Balance Inquiry";
                break;
            case "320000":
                TransactionType = "Bill Presentment";
                break;
            case "380000":
                TransactionType = "Mini statement";
                break;
            case "400000":
                TransactionType = "Funds Transfer";
                break;
            case "420000":
                TransactionType = "Mobile Topup";
                break;
            case "500000":
                TransactionType = "Bill Payment";
                break;
        }

        return TransactionType;
    }

    //Response Code on field 39
    public String getResponseCode(String f39) {
        String responseCode = "";

        switch (f39) {
            case "00":
                responseCode = "Transaction completed successfully.";
                break;
            case "01":
                responseCode = "NO PURCHASE";
                break;
            case "26":
                responseCode = "Paid Invoice";
                break;
            case "30":
                responseCode = "Payment reference number is missing";
                break;
            case "79":
                responseCode = "Transaction failed , invalid Currency";
                break;
            case "116":
                responseCode = "Transaction failed , insufficient funds.";
                break;
            case "57":
                responseCode = "Transaction not honoured.";
                break;
            case "72":
                responseCode = "Payment reference number is incorrect";
                break;
            case "73":
                responseCode = "Cancelled Invoice";
                break;
            case "91":
                responseCode = "Issuer not Available";
                break;
            case "114":
                responseCode = "Invalid account";
                break;

            case "491":
                responseCode = "Invalid account";
                break;

            case "999":
                responseCode = "Transaction Timed Out";
                break;
            case "":
                responseCode = "Transaction failed";
                break;

        }

        return responseCode;
    }

    public String getResponsePostillion(String respcode) {
        String resp = "";
        switch (respcode) {
            case "00":
                resp = "APPROVED OR COMPLETED SUCCESSFULLY ";
                break;
            case "01":
                resp = "REFER TO CARD ISSUER ";
                break;
            case "02":
                resp = "REFER TO CARD ISSUER SPECIAL CONDITION ";
                break;
            case "03":
                resp = "INVALID MERCHANT ";
                break;
            case "04":
                resp = "PICK UP CARD ";
                break;
            case "05":
                resp = "DO NOT HONOR ";
                break;
            case "06":
                resp = "ERROR ";
                break;
            case "07":
                resp = "PICK UP CARD SPECIAL CONDITION ";
                break;
            case "08":
                resp = "HONOR WITH IDENTIFICATION ";
                break;
            case "09":
                resp = "REQUEST IN PROGRESS ";
                break;
            case "10":
                resp = "APPROVED PARTIAL ";
                break;
            case "11":
                resp = "APPROVED VIP ";
                break;
            case "12":
                resp = "INVALID TRANSACTION ";
                break;
            case "13":
                resp = "INVALID AMOUNT ";
                break;
            case "14":
                resp = "INVALID CARD NUMBER ";
                break;
            case "15":
                resp = "NO SUCH ISSUER ";
                break;
            case "16":
                resp = "APPROVED UPDATE TRACK 3 ";
                break;
            case "17":
                resp = "CUSTOMER CANCELLATION ";
                break;
            case "18":
                resp = "CUSTOMER DISPUTE ";
                break;
            case "19":
                resp = "RE ENTER TRANSACTION ";
                break;
            case "20":
                resp = "INVALID RESPONSE ";
                break;
            case "21":
                resp = "NO ACTION TAKEN ";
                break;
            case "22":
                resp = "SUSPECTED MALFUNCTION ";
                break;
            case "23":
                resp = "UNACCEPTABLE TRANSACTION FEE ";
                break;
            case "24":
                resp = "FILE UPDATE NOT SUPPORTED ";
                break;
            case "25":
                resp = "UNABLE TO LOCATE RECORD ";
                break;
            case "26":
                resp = "DUPLICATE RECORD ";
                break;
            case "27":
                resp = "FILE UPDATE FIELD EDIT ERROR ";
                break;
            case "28":
                resp = "FILE UPDATE FILE LOCKED ";
                break;
            case "29":
                resp = "FILE UPDATE FAILED ";
                break;
            case "30":
                resp = "FORMAT ERROR ";
                break;
            case "31":
                resp = "BANK NOT SUPPORTED ";
                break;
            case "32":
                resp = "COMPLETED PARTIALLY ";
                break;
            case "33":
                resp = "EXPIRED CARD PICK UP ";
                break;
            case "34":
                resp = "SUSPECTED FRAUD PICK UP ";
                break;
            case "35":
                resp = "CONTACT ACQUIRER PICK UP ";
                break;
            case "36":
                resp = "RESTRICTED CARD PICK UP ";
                break;
            case "37":
                resp = "CALL ACQUIRER SECURITY PICK UP ";
                break;
            case "38":
                resp = "PIN TRIES EXCEEDED PICK UP ";
                break;
            case "39":
                resp = "NO CREDIT ACCOUNT ";
                break;
            case "40":
                resp = "FUNCTION NOT SUPPORTED ";
                break;
            case "41":
                resp = "LOST CARD PICK UP ";
                break;
            case "42":
                resp = "NO UNIVERSAL ACCOUNT ";
                break;
            case "43":
                resp = "STOLEN CARD PICK UP ";
                break;
            case "44":
                resp = "NO INVESTMENT ACCOUNT ";
                break;
            case "45":
                resp = "ACCOUNT CLOSED ";
                break;
            case "46":
                resp = "IDENTIFICATION REQUIRED ";
                break;
            case "47":
                resp = "IDENTIFICATION CROSS CHECK REQUIRED ";
                break;
            case "48":
                resp = "TO 50 RESERVED FOR FUTURE POSTILION USE ";
                break;
            case "51":
                resp = "NOT SUFFICIENT FUNDS ";
                break;
            case "52":
                resp = "NO CHECK ACCOUNT ";
                break;
            case "53":
                resp = "NO SAVINGS ACCOUNT ";
                break;
            case "54":
                resp = "EXPIRED CARD ";
                break;
            case "55":
                resp = "INCORRECT PIN ";
                break;
            case "56":
                resp = "NO CARD RECORD ";
                break;
            case "57":
                resp = "TRANSACTION NOT PERMITTED TO CARDHOLDER ";
                break;
            case "58":
                resp = "TRANSACTION NOT PERMITTED ON TERMINAL ";
                break;
            case "59":
                resp = "SUSPECTED FRAUD ";
                break;
            case "60":
                resp = "CONTACT ACQUIRER ";
                break;
            case "61":
                resp = "EXCEEDS WITHDRAWAL LIMIT ";
                break;
            case "62":
                resp = "RESTRICTED CARD ";
                break;
            case "63":
                resp = "SECURITY VIOLATION ";
                break;
            case "64":
                resp = "ORIGINAL AMOUNT INCORRECT ";
                break;
            case "65":
                resp = "EXCEEDS WITHDRAWAL FREQUENCY ";
                break;
            case "66":
                resp = "CALL ACQUIRER SECURITY ";
                break;
            case "67":
                resp = "HARD CAPTURE ";
                break;
            case "68":
                resp = "RESPONSE RECEIVED TOO LATE ";
                break;
            case "69":
                resp = "ADVICE RECEIVED TOO LATE ";
                break;
            case "70":
                resp = "TO 74 RESERVED FOR FUTURE POSTILION USE ";
                break;
            case "75":
                resp = "PIN TRIES EXCEEDED ";
                break;
            case "76":
                resp = "RESERVED FOR FUTURE POSTILION USE ";
                break;
            case "77":
                resp = "INTERVENE BANK APPROVAL REQUIRED ";
                break;
            case "79":
                resp = "TO 89 RESERVED FOR CLIENT SPECIFIC USE ";
                break;
            case "90":
                resp = "CUT OFF IN PROGRESS ";
                break;
            case "91":
                resp = "ISSUER OR SWITCH INOPERATIVE ";
                break;
            case "92":
                resp = "ROUTING ERROR ";
                break;
            case "93":
                resp = "VIOLATION OF LAW ";
                break;
            case "94":
                resp = "DUPLICATE TRANSACTION ";
                break;
            case "95":
                resp = "RECONCILE ERROR ";
                break;
            case "96":
                resp = "SYSTEM MALFUNCTION ";
                break;
            case "98":
                resp = "EXCEEDS CASH LIMIT ";
                break;
            case "A1":
                resp = "ATC NOT INCREMENTED ";
                break;
            case "A2":
                resp = "ATC LIMIT EXCEEDED ";
                break;
            case "A3":
                resp = "ATC CONFIGURATION ERROR ";
                break;
            case "A4":
                resp = "CVR CHECK FAILURE ";
                break;
            case "A5":
                resp = "CVR CONFIGURATION ERROR ";
                break;
            case "A6":
                resp = "TVR CHECK FAILURE ";
                break;
            case "A7":
                resp = "TVR CONFIGURATION ERROR ";
                break;
            case "A8":
                resp = "TO BZ RESERVED FOR FUTURE POSTILION USE ";
                break;
            case "C":
                resp = "ZERO UNACCEPTABLE PIN ";
                break;
            case "C1":
                resp = "PIN CHANGE FAILED ";
                break;
            case "C2":
                resp = "PIN UNBLOCK FAILED ";
                break;
            case "C3":
                resp = "TO D ZERO RESERVED FOR FUTURE POSTILION USE ";
                break;
            case "D1":
                resp = "MAC ERROR ";
                break;
            case "D2":
                resp = "TO E ZERO RESERVED FOR FUTURE POSTILION USE ";
                break;
            case "E1":
                resp = "PREPAY ERROR ";
                break;
            case "E2":
                resp = "TO MZ RESERVED FOR FUTURE POSTILION USE ";
                break;
            case "N":
                resp = "ZERO TO ZZ RESERVED FOR CLIENT SPECIFIC USE ";
                break;
            default:
                resp = "DO NOT HONOUR ";
        }
        return resp;
    }

//    private static String sendMessage(java.lang.String message, java.lang.String valcode) {
//        com.esb.ESBWS_Service service = new com.esb.ESBWS_Service();
//        com.esb.ESBWS port = service.getESBWSPort();
//        return port.sendMessage(message, valcode);
//    }
}
