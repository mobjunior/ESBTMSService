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
public class CashDeposit {

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
    String strPhoneNumber = "";
    String strDebitAccount = "";
    String strCreditAccount = "";
    String strfield100 = "";
    String Banksortcode = "";
    String Virtualpan = "";

    public void Run(String IncomingMessage, String intid) {

        try {
            //210000#MWALLET#BRS51150650004#0708003472#200#12345#

            strReceivedData = IncomingMessage.split("#");
            processingcode = strReceivedData[0];
            strAgencyCashManagement = strReceivedData[1];
            strDeviceid = strReceivedData[2];
            strAccountNumber = strReceivedData[3].replace("Ù", "");

            switch (strAgencyCashManagement) {

                case "CASH":

                    strAmount = strReceivedData[4];
                    amount = Double.valueOf(strAmount);
                    strAgentID = strReceivedData[5];
                    //field24 = strReceivedData[6];
                    strDeviceid = strReceivedData[2];

                    strProcCode = "AGENT_DP";
                    strDebitAccount = func.fn_getAgentAccountNumber(strAgentID);
                    strCreditAccount = strAccountNumber;
                    strCardNumber = strCreditAccount;
                    strNarration = "CASH DEPOSIT TO ACCOUNT : " + strCreditAccount;
                    //Banksortcode = strReceivedData[7];
                    //Virtualpan = func.fn_getVirtualPan(Banksortcode);
                    //strCardNumber = strCreditAccount;
                    //strfield100 = "";
                    //strCreditAccount = "10011204000282";
                    //s strDebitAccount = "10011204000282";
                    func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strfield100, strDebitAccount, strCreditAccount, "", "", strDeviceid, field24, strProcCode, strDeviceid, "", "");
                    return;

                case "MWALLET":

                    strAmount = strReceivedData[4];
                    amount = Double.valueOf(strAmount);
                    strAgentID = strReceivedData[5];
                    field24 = "MM";//strReceivedData[6];
                    strDeviceid = strReceivedData[2];

                    //strProcCode = "400000";
                    strDebitAccount = func.fn_getAgentAccountNumber(strAgentID);
//                    strCreditAccount = "254" + strAccountNumber.substring(1, 10);
                    strCreditAccount = strAccountNumber;
                    strCardNumber = strCreditAccount;
                    strNarration = "CASH DEPOSIT TO ACCOUNT : " + strCreditAccount;

                    strfield100 = "";

                    //for test
                    //strDebitAccount = "10011204000282";
                    //strCreditAccount = "10011204000282";
                    func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strfield100, strDebitAccount, strCreditAccount, "", "", strDeviceid, field24, strProcCode, strDeviceid, "", "");
                    return;

                case "CARDLESS":
                    strAmount = strReceivedData[6];
                    amount = Double.valueOf(strAmount);
                    strAgentID = strReceivedData[7];
                    strAgentID = func.fn_RemoveNon_Numeric(strAgentID);
                    strProcCode = "AGENT-CHDP";
                    strDebitAccount = func.fn_getAgentAccountNumber(strAgentID);
                    strCreditAccount = strAccountNumber;
                    strNarration = "CASH DEPOSIT TO ACCOUNT : " + strCreditAccount;
                    func.getESBResponse(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strProcCode, strDebitAccount, strCreditAccount, strDeviceid, strPhoneNumber, strDeviceid);

                    return;

                case "AGENCY":
                    strTrack2Data = strReceivedData[3].replace("Ù", "");
                    strTrack2Data = strReceivedData[3].replace("?", "");
                    strAmount = strReceivedData[6];
                    amount = Double.valueOf(strAmount);
                    strAgentID = strReceivedData[7];
                    strAgentID = func.fn_RemoveNon_Numeric(strAgentID);
                    strPinClear = strReceivedData[5].replace("Ù", "");
                    strPinClear = strPinClear.substring(0, 4);
                    strProcCode = "AGENT_DP";
                    field24 = strReceivedData[8];

                    if (Double.valueOf(strAmount) > 10000000) { //TRXN LIMIT
                        strResponse = func.strResponseHeader(strDeviceid);
                        strResponse += "AGENT ID:  " + strAgentID + "#";
                        strResponse += "TRAN NUM:  " + intid + "#";
                        strResponse += "--------------------------------" + "#";
                        strResponse += "                                " + "#";
                        strResponse += "        CASH DEPOSIT        " + "#";
                        strResponse += "                                " + "#";
                        strResponse += "    CASH DEPOSIT FAILED     " + "#";
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

                    if (amount < 0) {
                        strResponse = func.strResponseHeader(strDeviceid);
                        strResponse += "--------------------------------" + "#";
                        strResponse += "Amount must be greater than Zero #";
                        strResponse += func.strResponseFooter(strAgentID);
                        func.SendPOSResponse(strResponse, intid);
                        return;
                    }

                    strProcCode = "AGENT_DP";
                    strDebitAccount = func.fn_getAgentAccountNumber(strAgentID);
                    strCreditAccount = strAccountNumber;
                    strNarration = "CASH DEPOSIT TO ACCOUNT : " + strCreditAccount;

                    //test 
                    //strCreditAccount="120630366600";
                    //strCreditAccount = "0120035195500";
                    //strDebitAccount = "10011204000282";
                    func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strfield100, strDebitAccount, strCreditAccount, "", "", strDeviceid, field24, strProcCode, strDeviceid, "", "");

                    //end test
                    // strVerifyPin = func.PIN_Verify(strDeviceid, strNarration, strProcCode, strAmount, processingcode, strPinClear, strCardNumber, strExpiryDate, strAgentID, strField35, convert.PadZeros(12, intid), strPhoneNumber, strDebitAccount, strCreditAccount, strDeviceid);
                    return;
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
