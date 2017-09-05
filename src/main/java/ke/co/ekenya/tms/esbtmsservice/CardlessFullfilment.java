/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.ekenya.tms.esbtmsservice;

import ke.co.ekenya.tms.logsengine.TMSLog;
import ke.co.ekenya.tms.utilities.Functions;
import ke.co.ekenya.tms.utilities.XMLParser;
import ke.co.ekenya.tms.utilities.DataConversions;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author julius
 */
public class CardlessFullfilment {

    // DatabaseConnections connections = new DatabaseConnections();
    // ClassImportantValues cl = new ClassImportantValues();
    Functions func = new Functions();
    XMLParser parser = new XMLParser();

    String strCardNumber = "";
    String strDeviceid = "";
    String strExpiryDate = "";
    String strAccountNumber = "";
    String strCreditAccount="";
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
    String strRemittanceCode = "";
    String strPhoneNumber = "";
    String strField100 = "";
    String processingcode = "";
    String strResponseXML = "";
    String BankSortCode = "";
    String Cardlessbankvirtualpan = "";
    DataConversions convert = new DataConversions();

    public void Run(String IncomingMessage, String intid) {

        strReceivedData = IncomingMessage.split("#");
        processingcode = strReceivedData[0].toString();

        try {
            //630000#AGENCY#BRS51160800748#123456#100#0708003472#1000014#DCB#
            strAgencyCashManagement = strReceivedData[1];
            strDeviceid = strReceivedData[2];
            //strAgentID = func.fn_RemoveNon_Numeric(strAgentID);
            strRemittanceCode = strReceivedData[3].replace("Ù", "");
            strAmount = strReceivedData[4];
            strPhoneNumber = "254"+ strReceivedData[5].substring(1,10);
            strAgentID = strReceivedData[6].trim();
            //strField100 = strReceivedData[7].toUpperCase();
            //BankSortCode = strReceivedData[8];
            //Cardlessbankvirtualpan = func.getAgentBankVirtualpan(BankSortCode);
            
            strCardNumber = func.fn_getAgentAccountNumber(strAgentID);//Cardlessbankvirtualpan;
            strCreditAccount = strCardNumber;
            switch (strAgencyCashManagement) {

                case "AGENCY":

                    if ("SameBank".equals(strField100)) {//HUDUMA cardless
                        strCardNumber = "0000000000000000";
                        strExpiryDate = "0000";
                        strNarration = "CARDLESS FULLFILMENT BY " + strPhoneNumber;
                        strField100 = "CLF";
                        func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strField100, "", "", strPhoneNumber, "", strDeviceid, "", "630000", strDeviceid, BankSortCode, strRemittanceCode);

                        //  strResponseXML = func.getESBResponse(strAgentID,strCardNumber, processingcode, "0", strDeviceid, strNarration, "CARDLESS_FULL", strAccountNumber, "", "", strPhoneNumber);
                    } else {// means acquiring cardless from another bank
                        //i.e BOA and other banks
                        strNarration = "CARDLESS FULLFILMENT BY " + strPhoneNumber;
                        //strField100 = "CLF";
                        func.getESBResponse_v1(strAgentID, strCardNumber, processingcode, strAmount, convert.PadZeros(12, intid), strNarration, strField100, "", strCreditAccount, strPhoneNumber, "", strDeviceid, "", "630000", strDeviceid, BankSortCode, strRemittanceCode);
//                        strResponse = func.strResponseHeader(strDeviceid);
//                        strResponse += "Auth ID:      " + func.PadZeros(12, intid) + "#";
//                        strResponse += "--------------------------------" + "#";
//                        strResponse += "   Cardless Fulfilment          " + "#";
//                        strResponse += "--------------------------------" + "#";
//                        strResponse += strField100 + " Not Enabled      " + "#";
//                        strResponse += "#--------------------------------#";
//                        strResponse += func.strResponseFooter(strAgentID);
//                        func.SendPOSResponse(strResponse, intid);
                        return;
                    }
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
