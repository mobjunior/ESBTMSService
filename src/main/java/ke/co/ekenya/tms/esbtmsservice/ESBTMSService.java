/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.ekenya.tms.esbtmsservice;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import ke.co.ekenya.tms.utilities.DataConversions;
import ke.co.ekenya.tms.utilities.Functions;
import ke.co.ekenya.tms.logsengine.*;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author julius
 */
public class ESBTMSService {

    public static Queue<Map<String, String>> Logsdataqueue = new ConcurrentLinkedQueue<>();
    public static HashMap OuterHoldingQueue = new HashMap<String, HashMap<String, String>>();
    public static HashMap OuterHoldingQueue_reversals = new HashMap<String, HashMap<String, String>>();
    DataConversions convert = new DataConversions();
    Functions func = new Functions();

    String[] strReceivedData;
    String IncomingMessage = "";
    String MessageGUID = "";
    String[] Messagedata;
    int stan;
    String message = "";
    String strAgentCode = "";
    int processingcode;
    String intid = "";
    String operation = "";
    boolean Exitthread = false;
    String Field37 = "";
    String[] strRecievedData;

    public ESBTMSService(String message) {
        this.message = message;
    }

    public enum Operations {

        Cash_withdrawal(100000), Cash_Deposit(210000), Cheque_Deposit(240000), Balance(310000), Card_Activation(340000),
        Agent_Float(320000), Mini_Statement(380000), Full_statement(390000), Funds_Transfer(400000),
        Cash_Request(401000), Deposit_Request(402000), Accept_Cash(403000), Confirm_Deposit(404000),
        Request_Excess_Cash(405000), Shortage_Cash(406000), Topup(420000), Billpayments(500000),
        Bill_Presentment(510000), Loan_Repayment(530000), Cardless_Origination(620000), Cardless_Fulfilment(630000),
        Reprint(999999), ReprintRef(999998), EOD_Report(999990), Password_Change(999980), Login(000000), Link_Account(700000), Teller_Opeartions(710000),
        ReveralsRequest(720000), Customer_Information(800000), Merchant_Services(120000), Account_Lookup(360000), Customer_Phonenumber(999970);

        private final int status;

        Operations(int aOperations) {
            this.status = aOperations;
        }

        public int status() {
            return this.status;
        }

        public static Operations getStatusFor(int desired) {
            String stating = "";
            for (Operations stat : Operations.values()) {
                if (desired == new Integer(stat.status())) {
                    return stat;
                }
            }
            return null;
        }

    };

    public void ThreadFromPOS() {

        try {
            //log
            TMSLog el = new TMSLog("MessageFromPOS", "\n\n" + message + "\n\n");
            el.logfile();
            //decrypt the message from the POS
//           String POSRequest = func.decrypt(message, 2).toString();
//           System.out.println("decrypted message " + POSRequest + "\n\n");

//            if (POSRequest.trim().equalsIgnoreCase("+++") || POSRequest.trim().substring(0, 2).equalsIgnoreCase("AT")) {
//                // discard dirty characters from POS
//            } else {
//                String strRecievedData[] = message.split("#");
//
//                IncomingMessage = strRecievedData[0];
//                if (strRecievedData.length > 1) {
//                    MessageGUID = strRecievedData[1];
//                    IncomingMessage = strRecievedData[1];
//                }
            intid = func.generateCorelationID();
            IncomingMessage = message;
            Messagedata = IncomingMessage.split("#");
            processingcode = Integer.parseInt(Messagedata[0]);
            operation = Operations.getStatusFor(processingcode).toString();

            switch (operation) {
                case "Login":
                    Login login = new Login();
                    login.Run(IncomingMessage, convert.PadZeros(12, intid));
                    break;
                case "EOD_Report":
                    EODReport EOD_Report = new EODReport();
                    EOD_Report.Run(IncomingMessage, convert.PadZeros(12, intid));
                    break;
                case "Cash_withdrawal":
                    Cashwithdrawal Cash_withdrawal = new Cashwithdrawal();
                    Cash_withdrawal.Run(IncomingMessage, convert.PadZeros(12, intid));
                    break;
                case "Balance":
                    BalanceEnquiry bal = new BalanceEnquiry();
                    bal.Run(IncomingMessage, convert.PadZeros(12, intid));
                    break;
                case "Agent_Float":
                    AgentFloat agentfoat = new AgentFloat();
                    agentfoat.Run(IncomingMessage, convert.PadZeros(12, intid));
                    break;
                case "Mini_Statement":
                    Ministatement mini = new Ministatement();
                    mini.Run(IncomingMessage, convert.PadZeros(12, intid));
                    break;
                case "Funds_Transfer":
                    FundsTransfer FT = new FundsTransfer();
                    FT.Run(IncomingMessage, convert.PadZeros(12, intid));
                    break;
                case "Billpayments":
                    BillPayments billpay = new BillPayments();
                    billpay.Run(IncomingMessage, convert.PadZeros(12, intid));
                    break;
                case "Cardless_Origination":
                    CardlessOrigination cardlessOrig = new CardlessOrigination();
                    cardlessOrig.Run(IncomingMessage, convert.PadZeros(12, intid));
                    break;
                case "Reprint":
                    Reprint reprint = new Reprint();
                    reprint.Run(IncomingMessage, convert.PadZeros(12, intid));
                    break;
                case "ReprintRef":
                    ReprintRef reprintref = new ReprintRef();
                    reprintref.Run(IncomingMessage, convert.PadZeros(12, intid));
                    break;
                case "Password_Change":
                    PasswordChange password_change = new PasswordChange();
                    password_change.Run(IncomingMessage, convert.PadZeros(12, intid));
                    break;
                case "Cash_Deposit":
                    CashDeposit Cashdeposit = new CashDeposit();
                    Cashdeposit.Run(IncomingMessage, convert.PadZeros(12, intid));
                    break;
                case "Account_Lookup":
                    Accountlookup acclp = new Accountlookup();
                    acclp.Run(IncomingMessage, convert.PadZeros(12, intid));
                    break;
                case "Cardless_Fulfilment":
                    CardlessFullfilment cardless_Fullfilment = new CardlessFullfilment();
                    cardless_Fullfilment.Run(IncomingMessage, convert.PadZeros(12, intid));
                    break;
                case "Topup":
                    Topup airtime_topup = new Topup();
                    airtime_topup.Run(IncomingMessage, convert.PadZeros(12, intid));
                    break;
                case "Customer_Phonenumber":
                    CustomerPhonenumber cp = new CustomerPhonenumber();
                    cp.Run(IncomingMessage, convert.PadZeros(12, intid));
                    break;
//                    case "Bill_Presentment":
//                        Billspresentment billpr = new Billspresentment();
//                        billpr.Run(IncomingMessage, intid);
//                        break;
//                    case "Card_Activation":
//                        AccountActivation AccACT = new AccountActivation();
//                        AccACT.Run(IncomingMessage, intid);
//                        break;
                default:
                    String strResponse = "";
                    strResponse = "Transaction Code Not Defined#";
                    strResponse += "--------------------------------#";
                    // strResponse += func.strResponseFooter(strAgentCode);
                    //func.SendPOSResponse(strResponse, intid);
                    break;
            }

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            TMSLog el = new TMSLog(sw.toString());
            el.logfile();
        }

    }

    public void purgeHoldingQueue() {
        // PurgeHoldingQueue purge = new PurgeHoldingQueue();
        // purge.purgeHoldingQueue();
    }
}
