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
public class FundsTransfer {

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
    String strAccountNumber2 = "";
    String studentnumber = "";
    String studentname = "";
    String[] strReceivedData;
    String processingcode = "";
    String strDebitAccount = "";
    String strCreditAccount = "";
    String strPhoneNumber = "";
    String strProcCode = "";
    String strfield100 = "";
    String strfield24 = "";
    String beneficiaryBanksortcode = "";

    public void Run(String IncomingMessage, String intid) {

        strReceivedData = IncomingMessage.split("#");

        strAgencyCashManagement = strReceivedData[1];

        try {

            switch (strAgencyCashManagement) {

                case "AGENCY":
//400000#AGENCY#BRS51160800748#1000046#5245500003055855=18092261082506999999?#1234#150#1000014#~9F0206000000000000~9F0306000000000000~9F0607a0000000041010~82023800~9F36020094~9F0702ff00~9F2608b6c31e4776675ad2~9F270100~9F34031f0301~9F10120110200003640400000000000000000000ff~9F09020002~9F3303000000~9F1A020410~9F350100~95058000900000~9A03170819~5F2A0200ff~9C0100~9F3704f865cd82~57135245500003055855d18092261082506999999f~5A085245500003055855#
                    strReceivedData = IncomingMessage.split("#");
                    processingcode = strReceivedData[0].toString();
                    strAgencyCashManagement = strReceivedData[1];
                    strDeviceid = strReceivedData[2];
//                    strCreditAccount = "254" + strReceivedData[3].substring(1, 10);
                    strAccountNumber = strReceivedData[3];
                    strTrack2Data = strReceivedData[4].replace("Ù", "");
                    strTrack2Data = strReceivedData[4].replace("?", "");
                    strAmount = strReceivedData[6];
                    amount = Double.valueOf(strAmount);
                    strCreditAccount = strAccountNumber;//"254" + strAccountNumber.substring(1, 10);

                    strAgentID = strReceivedData[7];
                    //strAgentID = func.fn_RemoveNon_Numeric(strAgentID);
                    strPinClear = strReceivedData[5].replace("Ù", "");
                    strPinClear = strPinClear.substring(0, 4);
                    strfield24 = "MM";//strReceivedData[9];
                    //beneficiaryBanksortcode = strReceivedData[10];

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

                    //
                    //check if samebank
//                    if (beneficiaryBanksortcode.equalsIgnoreCase("SameBank")) {
//                        //pick the banksortcode from db
//                        beneficiaryBanksortcode = "";//func.fn_getBankSortcode(strCardNumber.substring(0, 6));
//                        strfield100 = "";
//                    } else {
//                        strfield100 = "";
//                    }
                    if (amount < 0) {
                        strResponse = func.strResponseHeader(strDeviceid);
                        strResponse += "--------------------------------" + "#";
                        strResponse += "Amount must be greater than Zero #";
                        strResponse += func.strResponseFooter(strAgentID);
                        func.SendPOSResponse(strResponse, intid);
                        return;
                    }

                    strProcCode = "AGFT_CC";
                    strDebitAccount = strCardNumber; //func.fn_getAgentAccountNumber(strAgentID);

                    strNarration = "FUNDS TRANSFER FROM ACCOUNT " + strDebitAccount + " TO ACCOUNT " + strCreditAccount;

                    //strDebitAccount = "";
                    func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strfield100, strDebitAccount, strCreditAccount, "", "", strDeviceid, strfield24, strProcCode, strDeviceid, beneficiaryBanksortcode, "");
                    //end test
                    //strVerifyPin = func.PIN_Verify(strDeviceid, strNarration, strProcCode, strAmount, processingcode, strPinClear, strCardNumber, strExpiryDate, strAgentID, strField35, convert.PadZeros(12, intid), strPhoneNumber, strDebitAccount, strCreditAccount, strDeviceid);

                    break;

                case "FLOAT":
                    //400000#FLOAT#BRS51160800748#1000046#5245500003055855=18092261082506999999?#1234#150#1000014#~9F0206000000000000~9F0306000000000000~9F0607a0000000041010~82023800~9F36020094~9F0702ff00~9F2608b6c31e4776675ad2~9F270100~9F34031f0301~9F10120110200003640400000000000000000000ff~9F09020002~9F3303000000~9F1A020410~9F350100~95058000900000~9A03170819~5F2A0200ff~9C0100~9F3704f865cd82~57135245500003055855d18092261082506999999f~5A085245500003055855#
                    strReceivedData = IncomingMessage.split("#");
                    processingcode = strReceivedData[0].toString();
                    strAgencyCashManagement = strReceivedData[1];
                    strDeviceid = strReceivedData[2];
//                    strCreditAccount = "254" + strReceivedData[3].substring(1, 10);
                    strAccountNumber = strReceivedData[3];
                    strTrack2Data = strReceivedData[4].replace("Ù", "");
                    strTrack2Data = strReceivedData[4].replace("?", "");
                    strAmount = strReceivedData[6];
                    amount = Double.valueOf(strAmount);
                    strCreditAccount = strAccountNumber;//"254" + strAccountNumber.substring(1, 10);

                    strAgentID = strReceivedData[7];
                    //strAgentID = func.fn_RemoveNon_Numeric(strAgentID);
                    strPinClear = strReceivedData[5].replace("Ù", "");
                    //strPinClear = strPinClear.substring(0, 4);
                    strfield24 = "MM";//strReceivedData[9];
                    //beneficiaryBanksortcode = strReceivedData[10];
                    strCardNumber = func.fn_getAgentAccountNumber(strAgentID);
                    strDebitAccount = strCardNumber;//func.fn_getAgentAccountNumber(strAgentID);

                    strNarration = "FLOAT TRANSFER FROM ACCOUNT " + strDebitAccount + " TO ACCOUNT " + strCreditAccount;
                    strProcCode = "AGFT_CM";
                    func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strfield100, strDebitAccount, strCreditAccount, "", "", strDeviceid, strfield24, strProcCode, strDeviceid, beneficiaryBanksortcode, "");

                    break;
                case "COMM":
                    //400000#FLOAT#BRS51160800748#1000046#5245500003055855=18092261082506999999?#1234#150#1000014#~9F0206000000000000~9F0306000000000000~9F0607a0000000041010~82023800~9F36020094~9F0702ff00~9F2608b6c31e4776675ad2~9F270100~9F34031f0301~9F10120110200003640400000000000000000000ff~9F09020002~9F3303000000~9F1A020410~9F350100~95058000900000~9A03170819~5F2A0200ff~9C0100~9F3704f865cd82~57135245500003055855d18092261082506999999f~5A085245500003055855#
                    strReceivedData = IncomingMessage.split("#");
                    processingcode = strReceivedData[0].toString();
                    strAgencyCashManagement = strReceivedData[1];
                    strDeviceid = strReceivedData[2];
//                    strCreditAccount = "254" + strReceivedData[3].substring(1, 10);
                    strAccountNumber = strReceivedData[3];
                    strTrack2Data = strReceivedData[4].replace("Ù", "");
                    strTrack2Data = strReceivedData[4].replace("?", "");
                    strAmount = strReceivedData[6];
                    amount = Double.valueOf(strAmount);
//                    strCreditAccount = strAccountNumber;//"254" + strAccountNumber.substring(1, 10);

                    strAgentID = strReceivedData[7];
                    //strAgentID = func.fn_RemoveNon_Numeric(strAgentID);
                    strPinClear = strReceivedData[5].replace("Ù", "");
                    //strPinClear = strPinClear.substring(0, 4);
                    strfield24 = "MM";//strReceivedData[9];
                    //beneficiaryBanksortcode = strReceivedData[10];
                    strCardNumber = func.fn_getAgentAccountNumber(strAgentID);
                    strDebitAccount = func.fn_getAgentCommisionAccountNumber(strAgentID);
                    strCreditAccount = strCardNumber;

                    strNarration = "FUNDS TRANSFER FROM ACCOUNT " + strDebitAccount + " TO ACCOUNT " + strCreditAccount;
                    strProcCode = "AGCOMM_CM";
                    func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strfield100, strDebitAccount, strCreditAccount, "", "", strDeviceid, strfield24, strProcCode, strDeviceid, beneficiaryBanksortcode, "");

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
