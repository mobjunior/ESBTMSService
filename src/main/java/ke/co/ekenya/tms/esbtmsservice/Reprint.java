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
public class Reprint {

    Database connections;
    Props props;
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
    String processingcode = "";
    String[] strReceivedData;
    String[] strLoginStatus;
    String strStatus;
    String strUserType;
    String strTrials;
    String strLoggedin;
    String strFirstLogin = "";
    String strActiveAgent = "";

    public void Run(String IncomingMessage, String intid) {
        try {//999999#AGENCY##1000014##
            props = new Props();
            connections = new Database(props);
            strReceivedData = IncomingMessage.split("#");
            processingcode = strReceivedData[0];
            try {
                strAgentCode = strReceivedData[3].replace("Ù", "");
                strAgentCode = func.fn_RemoveNon_Numeric(strAgentCode);
                //strAgentPassword = strReceivedData[4].replace("Ù", "");
                //strAgentPassword = func.fn_RemoveNon_Numeric(strAgentPassword);

                strCardNumber = "0000000000000000";
                strDeviceid = strReceivedData[2];
                strExpiryDate = "0000";
                //strAgentID = strReceivedData[5].trim();
                //strAgentID = func.fn_RemoveNon_Numeric(strAgentID);

                //strLoginStatus = func.fn_verify_login_details(strAgentCode, strAgentPassword, strDeviceid).split("\\|");
//                strStatus = strLoginStatus[0];
//                strUserType = strLoginStatus[1];
//                strTrials = strLoginStatus[2];
//                strLoggedin = strLoginStatus[3];
//                strFirstLogin = strLoginStatus[4];
//                strActiveAgent = strLoginStatus[5];
//                switch (Integer.parseInt(strStatus)) {
//                    case 1:
//                        switch (Integer.parseInt(strUserType)) {
//                            case 11:
                strResponse = connections.ExecuteQueryStringValue("select top 1 POS_RECEIPT from tbincomingtransactions where field_3 <> '000000' and field_3 <> '999999'and field_101='"+strAgentCode+"' order by id desc", "", "POS_RECEIPT");
                // break;
//                            default:
//                                strResponse = func.strResponseHeader(strReceivedData[2]);
//                                strResponse += "Ref No    :" + func.PadZeros(12, intid) + "#";
//                                strResponse += "--------------------------------" + "#";
//                                strResponse += "   WRONG CREDENTIALS ENTERED   " + "#";
//                                strResponse += "--------------------------------" + "#";
//                                strResponse += func.strResponseFooter(strAgentID);
//                                return;
//                        }
//                }

                func.SendPOSResponse(strResponse, intid);

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("field0", "0200");
                map.put("field2", "0000000000000000");
                map.put("field3", processingcode);
                map.put("field4", strAmount);
                map.put("field7", func.anyDate("MMDDHHMMSS"));
                map.put("field11", strDeviceid);
                map.put("field32", "POS");
                map.put("field37", strField37);
                map.put("field65", strAgentID);
                map.put("field88", strNarration);
                map.put("field100", "");
                map.put("field101", strAgentCode);
                map.put("field102", strAccountNumber);
                map.put("field103", "");
                map.put("field104", "");
                map.put("POSReceipt", strResponse);

                func.spInsertPOSTransactions(map);

            } catch (Exception ex) {
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                TMSLog el = new TMSLog(sw.toString());
                el.logfile();

            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();

        }
    }
}
