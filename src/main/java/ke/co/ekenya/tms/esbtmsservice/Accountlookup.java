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
public class Accountlookup {

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
    String strAgentCode = "";
    String strAgentPassword = "";
    String strField60 = "";
    String studentnumber = "";
    String studentname = "";
    String strReceivedData[];
    String strField37 = "";
    String processingcode = "";
    String strResponseXML = "";
    String strBankCode = "";
    String strRequiredFields = "";
    String strProcCode = "";

    String strBanksortcode = "";
    String Virtualpan = "";

    public void Run(String IncomingMessage, String intid) {

        try {
            //360000#LOOKUP#POSSerial#Accountno#Agentid#Bankcode#
            //360000#LOOKUP#BRS51160900603#1234568975452#1000024#SameBank#4407830052292316=21122211958886200000?#
            strReceivedData = IncomingMessage.split("#");
            processingcode = strReceivedData[0].toString();
            strAgencyCashManagement = strReceivedData[1];
            strDeviceid = strReceivedData[2];
            strAccountNumber = strReceivedData[3].replace("Ù", "");
            strAgentID = strReceivedData[4];
            strBanksortcode = strReceivedData[5];
            
            if ("SameBank".equalsIgnoreCase(strBanksortcode)) {
                strTrack2Data = strReceivedData[6].replace("Ù", "");
                strTrack2Data = strReceivedData[6].replace("?", "");
                if (strTrack2Data.contains("=")) {
                    strCardInformation = strTrack2Data.split("=");
                    strCardNumber = strCardInformation[0];
                    int strlen = strCardNumber.length();

                    if (strlen < 16) {
                        strResponse = func.strResponseHeader(strDeviceid);
                        strResponse += "--------------------------------" + "#";
                        strResponse += "INVALID PAN #";
                        strResponse += func.strResponseFooter(strAgentID);
                        func.SendPOSResponse(strResponse, intid);
                        return;
                    }
                    strExpiryDate = strCardInformation[1].substring(0, 4);
                    String[] strTrack2Data1 = strCardInformation[1].split("\\?");
                    strField35 = strCardInformation[0] + "=" + strTrack2Data1[0];
                } else if (strTrack2Data.contains("D")) {

                    strCardInformation = strTrack2Data.split("D");
                    strCardNumber = strCardInformation[0];
                    int strlen1 = strCardNumber.length();
                    if (strlen1 < 16) {
                        strResponse = func.strResponseHeader(strDeviceid);
                        strResponse += "--------------------------------" + "#";
                        strResponse += "INVALID PAN #";
                        strResponse += func.strResponseFooter(strAgentID);
                        func.SendPOSResponse(strResponse, intid);
                        return;
                    }
                    strExpiryDate = strCardInformation[1].substring(0, 4);
                    String[] strTrack2Data1 = strCardInformation[1].split("\\?");
                    strField35 = strCardInformation[0] + "=" + strTrack2Data1[0];
                }
            } else {
                Virtualpan = func.fn_getVirtualPan(strBanksortcode);
                strCardNumber = Virtualpan;
            }
            strProcCode = processingcode;
            strNarration = "ACCOUNT LOOKUP : " + strAccountNumber;
            field24 = "CC";
            strAmount = "0";
            func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strAgencyCashManagement, strAccountNumber, strAccountNumber, "", "", strDeviceid, field24, strProcCode, strDeviceid, "", "");

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
        }
    }
}
