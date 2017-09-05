/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.ekenya.tms.esbtmsservice;

import java.util.HashMap;
import ke.co.ekenya.tms.utilities.Database;
import ke.co.ekenya.tms.utilities.Functions;
import ke.co.ekenya.tms.utilities.Props;

/**
 *
 * @author simon
 */
public class ReprintRef {
    Functions func = new Functions();
    Database connections;
    Props props;
    String strCardNumber = "";
    String strDeviceid = "";
    String strExpiryDate = "";
    String strAccountNumber = "";
    String strResponse = "";
    String strAgentID = "";
    String strAmount = "";
    double amount;
    String field24 = "";
    String strTrack2Data = "";
    String strAgencyCashManagement = "";
    String[] strCardInformation;
    String strField35 = "";
    String strPinClear = "";
    String strVerifyPin = "";
    String strNarration = "";
    String strField37 = "";
    String strRRN = "";
    String strField39 = "";
    String str514narration = "";
    String strPhoneNumber = "";
    String strField100 = "";
    String strBillNumber = "";
    String strRemittanceCode = "";
    String strAgentPassword = "";
    String strAgentCode = "";
    String processingcode = "";
    String[] strReceivedData;
    String[] strLoginStatus;
    String strStatus;
    String strUserType;
    String strTrials;
    String strLoggedin;
    String strFirstLogin = "";
    String strActiveAgent = "";
    String strActiveDevice = "";

   public void Run(String IncomingMessage, String intid) {//999999#AGENCY##1001#1234#1001#

        strReceivedData = IncomingMessage.split("#");
        processingcode = strReceivedData[0];
        try {
            //999998#AGENCY#BRS51160800748#1000014#1111111#
            
            props = new Props();
            connections = new Database(props);
            strAgentCode = strReceivedData[3].replace("Ù", "");
            strAgentCode = func.fn_RemoveNon_Numeric(strAgentCode);
//            strAgentPassword = strReceivedData[4].replace("Ù", "");
//            strAgentPassword = func.fn_RemoveNon_Numeric(strAgentPassword);

            strCardNumber = "0000000000000000";
            strDeviceid = strReceivedData[2];
            strRRN = strReceivedData[4];
            strExpiryDate = "0000";
//            strAgentID = strReceivedData[5].trim();
//            strAgentID = func.fn_RemoveNon_Numeric(strAgentID);

            strResponse = connections.ExecuteQueryStringValue("select POS_RECEIPT from tbincomingtransactions where field_3 <> '000000' and field_3 <> '999998'and field_37 ='"+strRRN+"' order by id desc", "", "POS_RECEIPT");

            if (strResponse.isEmpty() || "".equals(strResponse)) {
                strResponse = func.strResponseHeader(strReceivedData[2]);
                strResponse += "Ref No    :" + func.PadZeros(12, intid) + "#";
                strResponse += "--------------------------------" + "#";
                strResponse += "  NO TRANSACTION FOUND    " + "#";
                strResponse += "             RETRY              " + "#";
                strResponse += "--------------------------------" + "#";
                strResponse += func.strResponseFooter(strAgentCode);
            }

            func.SendPOSResponse(strResponse, intid);

            HashMap<String, String> map = new HashMap<>();
            map.put("0", "0200");
            map.put("2", "0000000000000000");
            map.put("3", processingcode);
            map.put("4", strAmount);
            map.put("7", func.anyDate("MMDDHHMMSS"));
            map.put("11", strDeviceid);
            map.put("32", "POS");
            map.put("37", strField37);
            map.put("65", "");
            map.put("88", strNarration);
            map.put("100", "");
            map.put("100", strAgentCode);
            map.put("102", strAccountNumber);
            map.put("103", "");
            map.put("104", "");
            map.put("POSReceipt", strResponse);

            func.spInsertPOSTransactions(map);

        } catch (Exception ex) {
            //func.log("\nSEVERE Reprint() :: " + ex.getMessage() + "\n" + func.StackTraceWriter(ex), "ERROR");
        }
    }
}
