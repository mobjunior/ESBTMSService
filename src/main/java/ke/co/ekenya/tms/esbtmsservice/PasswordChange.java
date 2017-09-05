/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.ekenya.tms.esbtmsservice;

import ke.co.ekenya.tms.logsengine.TMSLog;
import ke.co.ekenya.tms.utilities.Database;
import ke.co.ekenya.tms.utilities.Functions;
import ke.co.ekenya.tms.utilities.Props;
import ke.co.ekenya.tms.utilities.XMLParser;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

/**
 *
 * @author julius
 */
public class PasswordChange {

    Props props;
    Database connections;
    Functions func = new Functions();
    XMLParser parser = new XMLParser();

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
    String strField39 = "";
    String str514narration = "";
    String strPhoneNumber = "";
    String strField100 = "";
    String strBillNumber = "";
    String strRemittanceCode = "";
    String strAgentPassword = "";
    String strAgentCode = "";
    String[] strReceivedData;
    String processingcode = "";
    String strnewpassword = "";
    String strconfirmpass = "";
    String SOURCE_ID = "";

    public void Run(String IncomingMessage, String intid) {

        try {

            //999980#AGENCY#BRS51160300178#xxx#000002#5978#6035#6035#
            //999980#INITIAL#BRS51160800748# #1000014#TION#1234#1234#(null pointer)#
            strReceivedData = IncomingMessage.split("#");
            processingcode = strReceivedData[0].toString();
            strDeviceid = strReceivedData[2];
            //strAgentPassword = strReceivedData[5].replace("Ù", "");
            //strAgentPassword = func.fn_RemoveNon_Numeric(strAgentPassword);
            strAgentCode = strReceivedData[4].replace("Ù", "");
            strAgentID = strReceivedData[4].trim().replace("Ù", "");
            //strAgentID = func.fn_RemoveNon_Numeric(strAgentID);
            strnewpassword = strReceivedData[6].replace("?", "");
            strconfirmpass = strReceivedData[6].replace("?", "");
            Boolean Success = false;

            if (strAgentCode.isEmpty() || strAgentCode.toString() == null || strnewpassword.isEmpty() || strnewpassword.toString() == null || strconfirmpass.isEmpty() || strconfirmpass.toString() == null) {
                strResponse = func.strResponseHeader(strReceivedData[2]);
                strResponse += "Auth ID:      " + func.PadZeros(12, intid) + "#";
                strResponse += "--------------------------------" + "#";
                strResponse += "   Request for Password Change   " + "#";
                strResponse += "--------------------------------" + "#";
                strResponse += " `  Some paramenters missing    " + "#";
                strResponse += "    Kindly Try Again " + "#";
                strResponse += func.strResponseFooter(strAgentCode);
                func.SendPOSResponse(strResponse, intid);
                return;
            }

            //func.fn_Updateagentpassword(strAgentID, strnewpassword, strDeviceid, intid);
            if (func.fn_Updateagentpassword(strAgentID, strnewpassword, strDeviceid, intid)) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("0", "1200");
                map.put("2", "0000000000000000");
                map.put("3", processingcode);
                map.put("4", strAmount);
                map.put("7", func.anyDate("MMDDHHMMSS"));
                map.put("11", strDeviceid);
                map.put("32", SOURCE_ID);
                map.put("37", strField37);
                map.put("65", strAgentID);
                map.put("88", strNarration);
                map.put("100", "PASSCHANGE");
                map.put("102", strAccountNumber);
                map.put("103", "");
                map.put("104", "");
                map.put("POSReceipt", strResponse);
                //func.spInsertPOSTransaction(map);

                if (strReceivedData[1].equals("INITIAL")) {//firstlogin
                    strResponse = "11#";
                    strField39 = "00";
//                    strField48 = "Successful";
                } else {

                    strResponse = func.strResponseHeader(strReceivedData[2]);
                    strResponse += "Auth ID:        " + func.PadZeros(12, intid) + "#";
                    strResponse += "--------------------------------" + "#";
                    strResponse += "   Request for Password Change    " + "#";
                    strResponse += "--------------------------------" + "#";
                    strResponse += " `  Password change " + strAgentID + "#";
                    strResponse += "    Successful " + "#";
                    strResponse += func.strResponseFooter(strAgentID);

                    func.SendPOSResponse(strResponse, intid);
                }
                func.SendPOSResponse(strResponse, intid);
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();

        }
    }
}
