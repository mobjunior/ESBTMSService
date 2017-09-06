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
 * @author simon
 */
public class BalanceEnquiry {

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
    String processingcode = "";
    String strResponseXML = "";
    String[] strReceivedData;
    String strSwitchMessage = "";
    String[] strArray;
    String status = "";
    String strMessageToPOS = "";
    String strProcCode = "";
    String strPhoneNumber = "";
    String strDebitAccount = "";
    String strCreditAccount = "";
    String strfield100 = "";

    public void Run(String IncomingMessage, String intid) {

        try {

            strReceivedData = IncomingMessage.split("#");
            strAgencyCashManagement = strReceivedData[1];

            switch (strAgencyCashManagement) {

                case "AGENCY":
//310000#AGENCY#BRS51160800748#5245500003055855=18092261082506999999?#5245500003055855=18092261082506999999?#1234#1000014#~9F0206000000000000~9F0306000000000000~9F0607a0000000041010~82023800~9F36020055~9F0702ff00~9F26082a2c10871b7c93fa~9F270100~9F34031f0301~9F10120110200003640000000000000000000000ff~9F09020002~9F3303000000~9F1A020410~9F350100~95058000900000~9A03170815~5F2A0200ff~9C0100~9F370443549189~57135245500003055855d18092261082506999999f~5A085245500003055855#
                    //EMV cards look for D in strTrack2Data
                    processingcode = strReceivedData[0];
                    strDeviceid = strReceivedData[2].replace("Ù", "");
                    strTrack2Data = strReceivedData[3].replace("Ù", "");
                    strTrack2Data = strReceivedData[3].replace("?", "");
                    strTrack2Data = strReceivedData[3].replace(";", "");
                    strPinClear = strReceivedData[5].replace("Ù", "");
                    strPinClear = strPinClear.substring(0, 4);
                    strAgentID = strReceivedData[6];
                    //strfield100 = "";

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

                    strNarration = "BALANCE INQUIRY";
                    strProcCode = "BAL";
                    strAmount = "0";
                    field24 = "MM";
                    // strDebitAccount="10011206000001";
                    strDebitAccount = strCardNumber;//10011204000282

                    func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strfield100, strDebitAccount, strCreditAccount, "", "", strDeviceid, field24, strProcCode, strDeviceid, "", "");
                    //end test
                    //strVerifyPin = func.PIN_Verify(strDeviceid, strNarration, strProcCode, strAmount, processingcode, strPinClear, strCardNumber, strExpiryDate, strAgentID, strField35, convert.PadZeros(12, intid), strPhoneNumber, strDebitAccount, strCreditAccount, strDeviceid);
                    return;

                default:
                    break;
            }

        } catch (Exception ex) {
            // func.log("\nSEVERE BalanceEnquiry() :: " + ex.getMessage() + "\n" + func.StackTraceWriter(ex), "ERROR");
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
        }
    }
}
