/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.ekenya.tms.logsengine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * @author julius
 */
public class EsbFormatter extends Formatter{
    
     @Override
    public String format(LogRecord record) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss.SSS");
        return format.format(new Date()) + "::"
                + record.getMessage() + "\n";
    }
}
