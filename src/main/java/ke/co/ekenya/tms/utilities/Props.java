/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.ekenya.tms.utilities;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
//import javax.ejb.Singleton;
//import javax.ejb.Startup;

/**
 *
 * @author julius
 */
//@Singleton
//@Startup
public class Props {



    //@PostConstruct
    public void onStartUpActivities() {
        new Configs().loadConfigs();  
    }


}
