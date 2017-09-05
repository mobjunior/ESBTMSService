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
public class Cashwithdrawal {

    // DatabaseConnections connections = new DatabaseConnections();
    // ClassImportantValues cl = new ClassImportantValues();
    XMLParser parser = new XMLParser();
    DataConversions convert = new DataConversions();

    Functions func = new Functions();
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
    String strRecievedData[];
    String processingcode = "";
    String strNarration = "";
    String strDebitAccount = "";
    String strCreditAccount = "";
    String strPhoneNumber = "";
    String strProcCode = "";
    String strfield100 = "";

    public void Run(String IncomingMessage, String intid) {

        try {
            //100000#AGENCY#BRS51160800748#5245500003055855=18092261082506999999?#5245500003055855=18092261082506999999?#0000#200#1000014#~9F0206000000000000~9F0306000000000000~9F0607a0000000041010~82023800~9F36020079~9F0702ff00~9F260870b7302eb3c08de1~9F270100~9F34031f0301~9F10120110200003640400000000000000000000ff~9F09020002~9F3303000000~9F1A020410~9F350100~95058000900000~9A03170818~5F2A0200ff~9C0100~9F37046aef901d~57135245500003055855d18092261082506999999f~5A085245500003055855#
            strRecievedData = IncomingMessage.split("#");
            processingcode = "010000";
            strAgencyCashManagement = strRecievedData[1];
            strDeviceid = strRecievedData[2];
            strTrack2Data = strRecievedData[3].replace("Ù", "");
            strTrack2Data = strRecievedData[3].replace("?", "");
            strTrack2Data = strRecievedData[3].replace(";", "");
            //strAgentID = func.fn_RemoveNon_Numeric(strAgentID);
            strPinClear = strRecievedData[5].replace("Ù", "");
            strPinClear = strPinClear.substring(0, 4);
            strAmount = strRecievedData[6].replace("Ù", "");

            strAgentID = strRecievedData[7].replace("Ù", "").trim();

            switch (strAgencyCashManagement) {

                case "AGENCY":
                    if (Double.valueOf(strAmount) < 0) {
                        strResponse = func.strResponseHeader(strDeviceid);
                        strResponse += "--------------------------------" + "#";
                        strResponse += "Amount must be greater than Zero #";
                        strResponse += func.strResponseFooter(strAgentID);
                        func.SendPOSResponse(strResponse, intid);
                        return;
                    }
                    if (Double.valueOf(strAmount) > 1000000) { //TRXN LIMIT
                        strResponse = func.strResponseHeader(strDeviceid);
                        strResponse += "AGENT ID:  " + strAgentID + "#";
                        strResponse += "TRAN NUM:  " + intid + "#";
                        strResponse += "--------------------------------" + "#";
                        strResponse += "                                " + "#";
                        strResponse += "        CASH WITHDRAWAL        " + "#";
                        strResponse += "                                " + "#";
                        strResponse += "    CASH WITHDRAWAL FAILED     " + "#";
                        strResponse += "TRANSACTION AMOUNT EXCEEDS LIMIT#";
                        strResponse += "                                " + "#";
                        strResponse += func.strResponseFooter(strAgentID);
                        func.SendPOSResponse(strResponse, intid);
                        return;
                    }

                    if (strTrack2Data.contains("=")) {

                        strCardInformation = strTrack2Data.split("=");
                        strCardNumber = strCardInformation[0];
                        int strlen = strCardNumber.length();

                        if (strlen < 16) {
                            strResponse = func.strResponseHeader(strDeviceid);
                            strResponse += "--------------------------------" + "#";
                            strResponse += "Invalid PAN #";
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
                            strResponse += func.strResponseFooter(strDeviceid);
                            func.SendPOSResponse(strResponse, intid);
                            return;
                        }

                        strExpiryDate = strCardInformation[1].substring(0, 4);
                        String[] strTrack2Data1 = strCardInformation[1].split("\\?");
                        strField35 = strCardInformation[0] + "=" + strTrack2Data1[0];
                    }

                    strProcCode = "AGENT_WD";
                    strCreditAccount = func.fn_getAgentAccountNumber(strAgentID);
                    strDebitAccount = strCardNumber;
                    strNarration = "CASH WITHDRAWAL FROM ACCOUNT : " + strDebitAccount;

                    //strCreditAccount = "";
                    //strDebitAccount = "";
                    func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strfield100, strDebitAccount, strCreditAccount, "", "", strDeviceid, field24, strProcCode, strDeviceid, "", "");
                    //end test
                    //strVerifyPin = func.PIN_Verify(strDeviceid, strNarration, strProcCode, strAmount, processingcode, strPinClear, strCardNumber, strExpiryDate, strAgentID, strField35, convert.PadZeros(12, intid), strPhoneNumber, strDebitAccount, strCreditAccount, strDeviceid);
                    return;
                default:
                    break;
            }// end of switch

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
        }
    }
}
