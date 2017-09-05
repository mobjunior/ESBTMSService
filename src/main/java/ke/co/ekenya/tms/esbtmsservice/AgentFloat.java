/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.ekenya.tms.esbtmsservice;

import ke.co.ekenya.tms.logsengine.TMSLog;
import ke.co.ekenya.tms.utilities.DataConversions;
import ke.co.ekenya.tms.utilities.Functions;
import ke.co.ekenya.tms.utilities.XMLParser;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author julius
 */
public class AgentFloat {

    Functions func = new Functions();
    XMLParser parser = new XMLParser();
    DataConversions convert = new DataConversions();

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
    String[] strReceivedData;
    String processingcode = "";
    String strResponseXML = "";
    String strProcCode = "";
    String strPhoneNumber = "";
    String strDebitAccount = "";

    public void Run(String IncomingMessage, String intid) {
        strReceivedData = IncomingMessage.split("#");
        try {
            //320000#AGENCY#BRS51160800748# # #1000014#
            //320000#COMMBAL#BRS51160800748# # #1000014#
            processingcode = strReceivedData[0].toString();
            strAgencyCashManagement = strReceivedData[1];
            strDeviceid = strReceivedData[2].replace("Ù", "");
            strAgentID = strReceivedData[5].replace("Ù", "").trim();

            switch (strAgencyCashManagement) {
                case "AGENCY":
                    processingcode = "310000";
                    //strAccountNumber = "";
                    strNarration = "AGENT FLOAT :" + strAgentID;
                    strProcCode = "BI";
                    strAmount = "0";
                    strAccountNumber = func.fn_getAgentAccountNumber(strAgentID);
                    strCardNumber = strAccountNumber;
                    strDebitAccount = strCardNumber;
                    strPhoneNumber = "254708003472";//func.fn_getAgentPhonenumber2(strDeviceid);
                    //func.getESBResponse(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strProcCode, strAccountNumber, "", strDeviceid, strPhoneNumber, strDeviceid);
                    func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, "", strDebitAccount, "", "", "", strDeviceid, field24, strProcCode, strDeviceid, "", "");
                    break;
                case "COMMBAL":
                    //320000#COMMBAL#BRS51160800748# # #1000014#
                    processingcode = "310000";
                    //strAccountNumber = "";
                    strNarration = "AGENT COMMISSION BAL:" + strAgentID;
                    strProcCode = "COMMBI";
                    strAmount = "0";
                    strAccountNumber = func.fn_getAgentCommisionAccountNumber(strAgentID);
                    strCardNumber = strAccountNumber;
                    strDebitAccount = strAccountNumber;//func.fn_getAgentAccountNumber(strAgentID);
                    strPhoneNumber = "254708003472";//func.fn_getAgentPhonenumber2(strDeviceid);
                    //func.getESBResponse(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strProcCode, strAccountNumber, "", strDeviceid, strPhoneNumber, strDeviceid);
                    func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, "", strDebitAccount, "", "", "", strDeviceid, field24, strProcCode, strDeviceid, "", "");
                default:
                    break;
            }

        } catch (Exception ex) {
            // func.log("\nSEVERE AgentFloat() :: " + ex.getMessage() + "\n" + func.StackTraceWriter(ex), "ERROR");
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
        }
    }
}
