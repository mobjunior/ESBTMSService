/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.ekenya.tms.utilities;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author julius
 */
public class XMLParser {
    
     public String WriteXML(HashMap<String, String> fields) {
        String strXML = "";
        strXML = "<?xml version= '1.0'   encoding= 'utf-8'?>"
                + "<message>"
                + "<isomsg direction='" + fields.get("direction") + "'>";
        fields.remove("direction");
        // loop thru the hashmap tp get the values and map to the new keys
        try {
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                String id = entry.getKey();
                String value = entry.getValue();
                strXML += "<field id='" + id + "' value='" + value + "' />";
            }

            strXML += " </isomsg></message>";
            //   System.out.println(strXML);
        } catch (Exception e) {
            System.out.println("Error in xmlToEC" + e);
            return null;
        }

        return strXML;
    }

    public HashMap ParseXml(String xmlString) {
        HashMap<String, String> fields = new HashMap<>();
        try {
            DocumentBuilderFactory dbf
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlString));

            Document doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName("message");

            for (int k = 0; k < nodes.getLength(); k++) {

                NodeList nodelt = doc.getElementsByTagName("field");

                // iterate the nodes
                for (int i = 0; i < nodelt.getLength(); i++) {
                    Element m = (Element) nodelt.item(i);
                    String id = m.getAttribute("id");
                    String value = m.getAttribute("value");

                    // populate the hashmap
                    fields.put(id, value);

                    //System.out.println(" id : " + id);
                    //System.out.println(" value : " + value);
                } // end for i=0, nodelt
            } // end for k=0 ,nodes
        } catch (Exception ex) {
        }
        return fields;
    }

    public HashMap XmlBreaker(String xml) {
        String Myresponse = "";
        String[] Transcationdetails;
        String cdigit;//this thing is worth while
        String msgcdigit;
        Integer cdigitLengh;
        
         HashMap<String, String> hm = new HashMap<>();
        try {

//            Transcationdetails = strXml.split("<@@>");
//
//            if (Transcationdetails.length == 1) {
//                xml = strXml;
//            } else {
//                cdigitLengh = Transcationdetails[1].length();
//                cdigit = Transcationdetails[1];
//                xml = Transcationdetails[2];
//                if (cdigitLengh != Integer.parseInt(Transcationdetails[0])) {
//                    //c length failure
//                    hm.clear();
//                    hm.put("field104", "111|checkdigit error");
//                }
//            }
//            util.log(xml, "log", "WSMSG", "RQST");
            hm.clear();
            InputStream file = new ByteArrayInputStream(xml.getBytes(Charset.defaultCharset()));
            if (!xml.contains("ERR@")) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(file);
                doc.getDocumentElement().normalize();
                NodeList nodes = doc.getElementsByTagName("*");
                Node node;
                int nodeCount = nodes.getLength();
                for (int i = 0; i < nodeCount; i++) {
                    node = nodes.item(i);
                    if (!"message".equals(node.getNodeName())) {
                        // System.out.println("Child NAME: " + node.getNodeName());
                        hm.put(node.getNodeName().replace("field", ""), node.getFirstChild().getNodeValue());
                    }
                }
            }

//            if (utilities.decrypt3des(Base64.decode(database.myDBConn.channels)).contains(hm.get("field32").toString()) && utilities.decrypt3des(Base64.decode(database.myDBConn.procs)).contains(hm.get("field3").toString())) {
//                //check digit for 3rd party applications
//                msgcdigit = hmacDigest(Base64.encode((database.myDBConn.passcode + hm.get("field11").toString()).getBytes()), hm.get("field32").toString());
//                if (msgcdigit == null ? hm.get("field33").toString().trim() == null : msgcdigit.equals(hm.get("field33").toString().trim())) {
//                    hm.put("field104", "000|External valid message");
//                } else {
//                    hm.put("field104", "111|checkdigit error");
//                }
//            } else {
//                msgcdigit = hmacDigest((xml.length() * Integer.parseInt(hm.get("field11").toString())) + "" + hm.get("field32"), hm.get("field33").toString());
//                if (msgcdigit == null ? Transcationdetails[1] != null : !msgcdigit.equals(Transcationdetails[1])) {
//                    //checkdigit failed failure
//                    hm.put("field104", "111|checkdigit error");
//                } else {
//                    hm.put("field104", "000|valid message");
//                }
//            }
            return hm;

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return hm;
    }


private HashMap ReadXML(String xmlString) {
        final HashMap<String, String> FieldsMap = new HashMap();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() {
                public void startElement(String uri, String localName, String qName,
                        Attributes attributes) throws SAXException {
                    if (qName.equalsIgnoreCase("field")) {
                        FieldsMap.put(attributes.getValue(0), attributes.getValue(1));
                        //System.out.println(attributes.getValue(0) + " : " + attributes.getValue(1));
                    }
                }
            };

            //saxParser.parse(new InputSource(new StringReader(xmlString)), handler);
            //System.out.println(xmlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FieldsMap;
    }
}
