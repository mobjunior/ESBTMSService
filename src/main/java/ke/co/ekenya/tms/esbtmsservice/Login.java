/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.ekenya.tms.esbtmsservice;

import ke.co.ekenya.tms.utilities.Functions;
import ke.co.ekenya.tms.utilities.Props;
import ke.co.ekenya.tms.utilities.XMLParser;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import ke.co.ekenya.tms.logsengine.*;

/**
 *
 * @author julius
 */
public class Login {

    private Props props;
    Functions func = new Functions();
    XMLParser parser = new XMLParser();

    String strAgentCode = "";
    String strAgentPassword = "";
    String strDeviceid = "";
    String strResponse = "";
    String strField39 = "";
    String[] strLoginStatus;
    String strField37 = "";
    String processingcode = "";
    String[] strRecievedData;
    String strField48 = "";
    String strUserType = "";
    String strActiveDevice = "";
    String strTrials = "";
    String strLoggedin = "";
    String strActiveAgent = "";
    String strFirstLogin = "";

    public void Run(String IncomingMessage, String intID) {
//000000#BRS51141100009#000002#5978#
//000000#BRS51160800748#1000014#1234#
        String strRecievedData[] = IncomingMessage.split("#");
        processingcode = strRecievedData[0].toString();
        try {
            strDeviceid = strRecievedData[1].replace("Ù", "");
            strAgentCode = strRecievedData[2].replace("Ù", "");
            strAgentPassword = strRecievedData[3].replace("Ù", "");
            strLoginStatus = func.fn_verify_login_details(strAgentCode, strAgentPassword, strDeviceid).split("|");
            strFirstLogin = strLoginStatus[2];
            strActiveAgent = strLoginStatus[0];
            //strAgentCode = func.fn_RemoveNon_Numeric(strAgentCode);

            if (strActiveAgent.equals("1")) {
                if (strFirstLogin.equals("1")) {
                    strResponse = "58#";
                    strField39 = "00";
                    strField48 = "Successful";
                } else {
                    strResponse = "11#";
                    strField39 = "00";
                    strField48 = "Successful";
                }
            } else if (strActiveAgent.equals("0") && strFirstLogin.equals("0")) {
                strResponse = "57#";
                strField39 = "99";
                strField48 = "Invalid Credentials";
            } //else {
            //                strResponse = "56#";
            //                strField39 = "99";
            //                strField48 = "Terminal is blocked";
            //            }
            else {
                strResponse = "55#";
                strField39 = "99";
                strField48 = "Agent is Inactive";
            }

            HashMap<String, String> map = new HashMap<String, String>();
            String strNarration = "USER LOGIN " + strAgentCode;

            map.put("field0", "0200");
            map.put("field2", "0000000000000000");
            map.put("field3", processingcode);
            map.put("field4", "0");
            map.put("field7", func.anyDate("YYYYMMDDHH"));
            map.put("field11", "");
            map.put("field32", "POS");
            map.put("field37", strField37);
            map.put("field39", strField39);
            map.put("field41", strDeviceid);
            map.put("field48", strField48);
            map.put("field65", "");
            map.put("field88", "LOGIN");
            map.put("field100", "");
            map.put("field101", strAgentCode);
            map.put("field102", "");
            map.put("field103", "");
            map.put("field104", "");
            map.put("field105", strDeviceid);
            //map.put("ReferenceNumber", intID);
            //String strXML_Request = parser.WriteXML(map);
            //map.put("strRequesttoEconnect", strXML_Request);
            //map.put("strResponseFromEconnect", "");

            func.spInsertPOSTransactions(map);

            func.SendPOSResponse(strResponse, intID);
            //System.out.println(strField48);

        } catch (Exception ex) {
            // func.log("\nSEVERE Login() :: " + ex.getMessage() + "\n" + func.StackTraceWriter(ex), "ERROR");

            strResponse = "SYSTEM MULFUNCTION#";

            func.SendPOSResponse(strResponse, intID);

            ex.printStackTrace();
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
        }
    }
}
