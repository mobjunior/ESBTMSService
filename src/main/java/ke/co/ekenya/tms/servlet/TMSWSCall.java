/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.ekenya.tms.servlet;

import ke.co.ekenya.tms.utilities.Configs;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author julius
 */
public class TMSWSCall {
    //Open a connection to the ESB Web Service and send request

    public String connectToESBWS(String request) throws MalformedURLException, IOException {
        // new Config().loadConfigurations();

        String response;

        String StrESBWSUrl = Configs.TMSWSUrl;
        URL urlESBWS = new URL(StrESBWSUrl);
        URLConnection conn = urlESBWS.openConnection();
        conn.setDoOutput(true);

        try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
            //System.out.println("\n\nRequest=" + request);
            String base64Request = Base64.getEncoder().encodeToString(request.getBytes());
            writer.write(base64Request);
            writer.flush();

            String base64Response = getResponseFromInputStream(conn.getInputStream());
            byte[] decoded = Base64.getDecoder().decode(base64Response);
            response = new String(decoded, "UTF8");
        } catch (Exception ex) {
            Map<String, String> ResponseDetails = new HashMap<>();
            ResponseDetails.put("48", "DISCONNECTED");
            ResponseDetails.put("48", "DISCONNECTED");
            response = ResponseDetails.toString();
        }

        return response;
    }

    //Get response from ESB Web Service
    public String getResponseFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }
}
