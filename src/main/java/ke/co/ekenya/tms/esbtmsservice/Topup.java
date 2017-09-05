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
public class Topup {

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
    String strField39 = "";
    String str514narration = "";
    String strPhoneNumber = "";
    String strField100 = "";
    String processingcode = "";
    String[] strReceivedData;
    String strBillNumber = "";
    String strwalkingcustomerphonenumber = "";
    String strDebitAccount = "";
    String strCreditAccount = "";
    String strfield98 = "";
    String Agentbankvirtualpan = "";

    public void Run(String IncomingMessage, String intid) {

        strReceivedData = IncomingMessage.split("#");
        try {

            strReceivedData = IncomingMessage.split("#");

            processingcode = strReceivedData[0].toString();
            strAgencyCashManagement = strReceivedData[1];
            strDeviceid = strReceivedData[2];

            switch (strAgencyCashManagement) {
                case "CASH":
                    //420000#CASH#BRS51141100009#50#12J6#SAFARICOM#0722382062#
                    strAmount = strReceivedData[3];
                    strAgentID = strReceivedData[4].trim();
                    strField100 = strReceivedData[5];
                    strfield98 = strReceivedData[5];
                    strwalkingcustomerphonenumber = "254" + strReceivedData[6].substring(1, 10);
                    //Agentbankvirtualpan = func.fn_getAgentBankVirtualpan(strAgentID);
                     //strCreditAccount = "254" + strReceivedData[6].substring(1, 10);
                    //strCardNumber = Agentbankvirtualpan;
                    strNarration = "AIRTIME TOPUP FOR MOBILE NO: " + strwalkingcustomerphonenumber;
                    strDebitAccount = func.fn_getAgentAccountNumber(strAgentID);
                    strCardNumber = strwalkingcustomerphonenumber ;
                    //strCreditAccount = "";//0120035195500
                    //strDebitAccount = "";//10011204000282

                    field24 = "MM";//strReceivedData[10];
                    //processingcode="500000";

                    func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strfield98, strDebitAccount, strCreditAccount, strwalkingcustomerphonenumber, strwalkingcustomerphonenumber, strDeviceid, field24, strfield98, strDeviceid, "", "");

                    //func.getESBResponse(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strField100, strDebitAccount, strCreditAccount, strBillNumber, strwalkingcustomerphonenumber, strDeviceid);
                    break;
                case "AGENCY":
                    //420000#AGENCY#BRS51160800748#5245500003055855=18092261082506999999?#5245500003055855=18092261082506999999?#1234#150#1000014#SAFARICOM#0708003472#~9F0206000000000000~9F0306000000000000~9F0607a0000000041010~82023800~9F36020075~9F0702ff00~9F2608ad067afc293ddf23~9F270100~9F34031f0301~9F10120110200003640400000000000000000000ff~9F09020002~9F3303000000~9F1A020410~9F350100~95058000900000~9A03170818~5F2A0200ff~9C0100~9F3704fef244c0~57135245500003055855d18092261082506999999f~5A085245500003055855#
                    //420000#AGENCY#BRS51160300178#1122350010954007=19062211000088700000?#3214#60#000001#Airtel#0729029116#
                    strTrack2Data = strReceivedData[3].replace("Ù", "");
                    strTrack2Data = strReceivedData[3].replace("?", "");
                    strPinClear = strReceivedData[5].replace("Ù", "");
                    strAmount = strReceivedData[6];
                    strAgentID = strReceivedData[7].trim();
                    strField100 = strReceivedData[8];
                    strfield98 = strReceivedData[8];
                    strwalkingcustomerphonenumber = "254"+  strReceivedData[9].substring(1, 10);
                    field24 = "MM";//strReceivedData[10];
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

                    strNarration = "AIRTIME TOPUP FOR MOBILE NO: " + strwalkingcustomerphonenumber;
                    strPhoneNumber = strwalkingcustomerphonenumber;

                    //test 
                    strCreditAccount = "";//strCardNumber; //"";//0120035195500
                    strDebitAccount = strCardNumber;//10011204000282
                    //processingcode="500000";
                    //func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, strPhoneNumber, strNarration, strAccountNumber, strAccountNumber, strCreditAccount, strField35, strPhoneNumber, strfield98, strField35, strfield98, strField35, strExpiryDate, strResponse);
                    //System.out.println(strField100);
                    func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strField100, strDebitAccount, strCreditAccount, strwalkingcustomerphonenumber, strwalkingcustomerphonenumber, strDeviceid, field24, strfield98, strDeviceid, "", "");

                    //end test
                    //strVerifyPin = func.PIN_Verify(strBillNumber, strNarration, strField100, strAmount, processingcode, strPinClear, strCardNumber, strExpiryDate, strAgentID, strField35, convert.PadZeros(12, intid), strPhoneNumber, strDebitAccount, strCreditAccount, strDeviceid);
                    return;

            }

        } catch (Exception ex) {

            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
        }

    }
}
