/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.ekenya.tms.esbtmsservice;

import ke.co.ekenya.tms.logsengine.TMSLog;
import ke.co.ekenya.tms.utilities.Functions;
import ke.co.ekenya.tms.utilities.XMLParser;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

/**
 *
 * @author julius
 */
public class EODReport {

    // DatabaseConnections connections = new DatabaseConnections();
    // ClassImportantValues cl = new ClassImportantValues();
    Functions func = new Functions();
    XMLParser parser = new XMLParser();

    private static String strCardNumber = "";
    private static String strDeviceid = "";
    private static String strExpiryDate = "";
    private static String strAccountNumber = "";
    private static String strResponse = "";
    private static String strAgentID = "";
    private static String strAmount = "";
    private static double amount;
    private static String field24 = "";
    private static String strTrack2Data = "";
    private static String strAgencyCashManagement = "";
    private static String[] strCardInformation;
    private static String strField35 = "";
    private static String strPinClear = "";
    private static String strVerifyPin = "";
    private static String strNarration = "";
    private static String strField37 = "";
    private static String strField39 = "";
    private static String strRemittanceCode = "";
    private static String str514narration = "";
    private static String strAgentCode = "";
    private static String strAgentPassword = "";
    private static String[] strReceivedData;
    private static String processingcode = "";
    private static String SOURCE_ID = "";

    public void Run(String IncomingMessage, String intid) {

        strReceivedData = IncomingMessage.split("#");
        processingcode = strReceivedData[0];
        try {
//            999990#AGENCY#BRS51160800748# # #1000014#"
            strAgentCode = strReceivedData[5].replace("Ù", "");
            //strAgentPassword = strReceivedData[4].replace("Ù", "");

            //strAgentPassword = func.fn_RemoveNon_Numeric(strAgentPassword);
            strCardNumber = "0000000000000000";
            strDeviceid = strReceivedData[2];

            strExpiryDate = "0000";
            strAgentID = strAgentCode;//strReceivedData[5].trim();
            strAgentID = func.fn_RemoveNon_Numeric(strAgentID);

            strNarration = "EOD Report";

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("0", "0200");
            map.put("2", "0000000000000000");
            map.put("3", processingcode);
            map.put("4", strAmount);
            map.put("7", func.anyDate("MMDDHHMMSS"));
            map.put("11", strDeviceid);
            map.put("32", "POS");
            map.put("37", strField37);
            map.put("65", strAgentID);
            map.put("88", strNarration);
            map.put("100", "");
            map.put("102", strAccountNumber);
            map.put("103", "");
            map.put("104", "");
            map.put("POSReceipt", strResponse);

            func.spInsertPOSTransactions(map);

            strResponse = func.strResponseHeader(strDeviceid);
            strResponse = strResponse + func.fn_getAgentEODTransactions(strAgentID, strDeviceid, intid);
            strResponse = strResponse + func.strResponseFooter(strDeviceid);

            func.SendPOSResponse(strResponse, intid);
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();            
        }
    }
}
