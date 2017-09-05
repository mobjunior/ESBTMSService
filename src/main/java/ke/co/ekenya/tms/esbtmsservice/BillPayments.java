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
public class BillPayments {

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
    String strField37 = null;
    String strBillNumber = null;
    String strField100 = null;
    String strwalkingcustomerphonenumber = "";
    String strReceivedData[] = null;
    String EcgBalresponse = "";
    String[] strECGResponse;
    String strResponseMessage = "";
    String status = "";
    String customername = "";
    String custbal = "";
    String outstandingdate = "";
    String strResponseXML = "";
    String processingcode = "";
    String strPhoneNumber = "";
    String strDebitAccount = "";
    String strCreditAccount = "";
    String strField24 = "";
    String strField65 = "";
    String strField98 = "";
    String Agentbankvirtualpan = "";

    public void Run(String IncomingMessage, String intid) {

        try {
            strReceivedData = IncomingMessage.split("#");

            strAgencyCashManagement = strReceivedData[1];
            processingcode = strReceivedData[0].toString();
            strDeviceid = strReceivedData[2];

            switch (strAgencyCashManagement) {
                case "CASH":
                    //500000#CASH#BRS51160800748#180#1000014#KPLC#12345678#0708003472#
                    strAmount = strReceivedData[3];
                    strAgentID = strReceivedData[4].trim();
                    strField98 = strReceivedData[5];
                    strField100 = strReceivedData[5];
                    strBillNumber = strReceivedData[6];
                    //Agentbankvirtualpan = func.fn_getAgentBankVirtualpan(strAgentID);
                    //strCardNumber = Agentbankvirtualpan;
                    field24 = "MM";
                    strwalkingcustomerphonenumber = "254" + strReceivedData[7].substring(1, 10);
                    strNarration = "BILL PAYMENT FOR BILL NO " + strBillNumber;
                    strDebitAccount = func.fn_getAgentAccountNumber(strAgentID);
                    strCreditAccount = "";//10011204000282
                    //strDebitAccount = "";//10011204000282
                    strCardNumber=strwalkingcustomerphonenumber;

                    func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strField100, strDebitAccount, strCreditAccount, strBillNumber, strwalkingcustomerphonenumber, strDeviceid, field24, strField98, strDeviceid, "", "");

                    break;
                case "AGENCY":
                    //420000#AGENCY#BRS51160800748#5245500003055855=18092261082506999999?#5245500003055855=18092261082506999999?#1234#150#1000014#SAFARICOM#0708003472#~9F0206000000000000~9F0306000000000000~9F0607a0000000041010~82023800~9F36020075~9F0702ff00~9F2608ad067afc293ddf23~9F270100~9F34031f0301~9F10120110200003640400000000000000000000ff~9F09020002~9F3303000000~9F1A020410~9F350100~95058000900000~9A03170818~5F2A0200ff~9C0100~9F3704fef244c0~57135245500003055855d18092261082506999999f~5A085245500003055855#
                    //500000#AGENCY#BRS51160800748#5245500003055855=18092261082506999999?#5245500003055855=18092261082506999999?#1234#200#1000014#KPLC#1234567#0708003472#~9F0206000000000000~9F0306000000000000~9F0607a0000000041010~82023800~9F3602005a~9F0702ff00~9F2608f8b229d21fcce647~9F270100~9F34031f0301~9F10120110200003640000000000000000000000ff~9F09020002~9F3303000000~9F1A020410~9F350100~95058000900000~9A03170815~5F2A0200ff~9C0100~9F3704bbd83116~57135245500003055855d18092261082506999999f~5A085245500003055855#
                    strTrack2Data = strReceivedData[3].replace("Ù", "");
                    strTrack2Data = strReceivedData[3].replace("?", "");
                    strPinClear = strReceivedData[5].replace("Ù", "");
                    strPinClear = strPinClear.substring(0, 4);
                    strAmount = strReceivedData[6];
                    strAgentID = strReceivedData[7].trim();
                    //strAgentID = func.fn_RemoveNon_Numeric(strAgentID);
                    //strField98 = strReceivedData[7];
                    strField100 = strReceivedData[8];
                    strBillNumber = strReceivedData[9];
                    strwalkingcustomerphonenumber =  "254" + strReceivedData[10].substring(1, 10);;
                    strField65 = strBillNumber;
                    field24 = "MM";
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

                    strNarration = "BILL PAYMENT FOR BILL NO " + strBillNumber;
                    strPhoneNumber = strwalkingcustomerphonenumber;

                    //test
                    strCreditAccount = "";//0120035195500
                    strDebitAccount = strCardNumber;// "";//10011204000282

                    // public HashMap getESBResponse_v1(String strAgentID, String cardNumber, String processingCode, String amount, String referenceNumber, String narration, String transactionIdentifier, String strAccountNumber, String creditAccount, String strField65, String Phonenumber, String strField68, String strField24, String strField98) throws InterruptedException {
                    func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strField100, strDebitAccount, strCreditAccount, strBillNumber, strwalkingcustomerphonenumber, strDeviceid, field24, strField98, strDeviceid, "", "");

                    //;;;;;;;;;;;;;;;
                    //strVerifyPin = func.PIN_Verify(strBillNumber, strNarration, strField100, strAmount, processingcode, strPinClear, strCardNumber, strExpiryDate, strAgentID, strField35, convert.PadZeros(12, intid), strPhoneNumber, strDebitAccount, strCreditAccount, strDeviceid);
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
