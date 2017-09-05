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
public class CustomerPhonenumber {

    //999970#BRS51160300178#0729029116#1234#000897547701#
    Functions func = new Functions();
    XMLParser parser = new XMLParser();
    DataConversions convert = new DataConversions();

    String strAgencyCashManagement = "";
    String[] strCardInformation;
    String strAgentCode = "";
    String strReceivedData[];
    String processingcode = "";
    String uniqueID = "";
    String phoneNumber = "";
    String accountNumber = "";
    String bankCode = "";
    String strDeviceid = "";
    String succ = "";

    public void Run(String IncomingMessage, String intid) {

        try {
            //999970#BRS51160300178#0729029116#1234#000897547701#
            strReceivedData = IncomingMessage.split("#");
            processingcode = strReceivedData[0];
            strDeviceid = strReceivedData[1];
            phoneNumber = strReceivedData[2];
            strAgentCode = strReceivedData[3];
            uniqueID = strReceivedData[4];

            succ = func.spInsertCustomerphonenumbers(uniqueID, phoneNumber, accountNumber, bankCode, strAgentCode);

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
        }
    }
}
