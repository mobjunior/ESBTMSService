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
public class Ministatement {

    //DatabaseConnections connections = new DatabaseConnections();
    //ClassImportantValues cl = new ClassImportantValues();
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
    String strDebitAccount = "";
    String strCreditAccount = "";
    String strPhoneNumber = "";
    String strProcCode = "";
    String strfield100 = "";

    public void Run(String IncomingMessage, String intid) {
        try {
            //380000#AGENCY#BRS51160800748#5245500003055855=18092261082506999999?#5245500003055855=18092261082506999999?#1234#1000014#~9F0206000000000000~9F0306000000000000~9F0607a0000000041010~82023800~9F36020055~9F0702ff00~9F26082a2c10871b7c93fa~9F270100~9F34031f0301~9F10120110200003640000000000000000000000ff~9F09020002~9F3303000000~9F1A020410~9F350100~95058000900000~9A03170815~5F2A0200ff~9C0100~9F370443549189~57135245500003055855d18092261082506999999f~5A085245500003055855#
            strReceivedData = IncomingMessage.split("#");
            processingcode = strReceivedData[0].toString();
            strAgencyCashManagement = strReceivedData[1];
            strDeviceid = strReceivedData[2].replace("Ù", "");
            strTrack2Data = strReceivedData[3].replace("Ù", "");
            strTrack2Data = strReceivedData[3].replace("?", "");
            strTrack2Data = strReceivedData[3].replace(";", "");
            strPinClear = strReceivedData[5].replace("Ù", "");
            //strPinClear = strPinClear.substring(0, 4);
            strAgentID = strReceivedData[6].replace("Ù", "").trim();
            //strAgentID = func.fn_RemoveNon_Numeric(strAgentID);

            switch (strAgencyCashManagement) {

                case "AGENCY":
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
                    }

                    strNarration = "MINI STATEMENT";
                    strProcCode = "MINI";
                    strAmount = "0";
                    field24 = "MM";

                    strDebitAccount = strCardNumber;//10011204000282

                    func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strfield100, strDebitAccount, strCreditAccount, "", "", strDeviceid, field24, strProcCode, strDeviceid, "", "");
                    //end test
                    //strVerifyPin = func.PIN_Verify(strDeviceid, strNarration, strProcCode, strAmount, processingcode, strPinClear, strCardNumber, strExpiryDate, strAgentID, strField35, convert.PadZeros(12, intid), strPhoneNumber, strDebitAccount, strCreditAccount, strDeviceid);
                    break;

                case "FLOAT":
                    //380000#FLOAT#BRS51160800748# # # #1000014#
                    
                    processingcode = "380000";
                    //strAccountNumber = "";
                    strNarration = "AGENT MINISTATEMENT :" + strAgentID;
                    strProcCode = "MINIFLOAT";
                    strAmount = "0";
                    strAccountNumber = func.fn_getAgentAccountNumber(strAgentID);
                    strPhoneNumber = "254708003472";//func.fn_getAgentPhonenumber2(strDeviceid);
                    strCardNumber = strAccountNumber;
                    strDebitAccount=strCardNumber;
                    //func.getESBResponse(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strProcCode, strAccountNumber, "", strDeviceid, strPhoneNumber, strDeviceid);
                    func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strfield100, strDebitAccount, strCreditAccount, "", "", strDeviceid, field24, strProcCode, strDeviceid, "", "");
                    break;
                case "COMM":
                    //380000#COMM#BRS51160800748# # # #1000014#
                    processingcode = "380000";
                    strAccountNumber = "";
                    strNarration = "AGENT COMMISSION MINISTATEMENT :" + strAgentID;
                    strProcCode = "MINICOMM";
                    strAmount = "0";
                    strAccountNumber = func.fn_getAgentCommisionAccountNumber(strAgentID);
                    strPhoneNumber = "254708003472";//func.fn_getAgentPhonenumber2(strDeviceid);
                    strCardNumber = strAccountNumber;
                    strDebitAccount=strCardNumber;
                    //func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strProcCode, strAccountNumber, "", strDeviceid, strPhoneNumber, strDeviceid);
                    func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strfield100, strDebitAccount, strCreditAccount, "", "", strDeviceid, field24, strProcCode, strDeviceid, "", "");
                    break;

                default:
                    break;
            }

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
        }
    }
}
