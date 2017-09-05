/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.ekenya.tms.processor;

import ke.co.ekenya.tms.utilities.Configs;

/**
 *
 * @author smaingi
 */
public class StartServeer {

    public static void main(String[] args) {
        try {
            new Configs().loadConfigs();
            new NettyServer().startserver();
        } catch (Exception ex) {

            ex.printStackTrace();

        }
    }

}
