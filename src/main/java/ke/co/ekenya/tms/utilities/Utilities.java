/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.ekenya.tms.utilities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author julius
 */
public class Utilities {
   
    private final Logging logger;

    public Utilities(Logging logger) {
        this.logger = logger;
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static Date formatDate(String dateData) throws ParseException {
        DateFormat dateFormated = new SimpleDateFormat("YYYY-MM-dd");

        //java.sql.Date sqlDate = new java.sql.Date(dateFormated.parse(dateData).getDate());
        return dateFormated.parse(dateData);
    }

    public static String formatCash(String amount) {
        double preAmount = Double.parseDouble(amount )* 100;
        int intAmount =  (int) (preAmount);
        return (amount + "*" + String.format("%012d", intAmount));
    }

    public static final ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("YYYY-MM-dd");
        }
    };

    public static final ThreadLocal<SimpleDateFormat> monthDayFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("MMDD");
        }
    };

    public static final ThreadLocal<SimpleDateFormat> timeFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HHmmss");
        }
    };
    public static final ThreadLocal<SimpleDateFormat> dateTimeFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("MMddHHmmss");
        }
    };

      public static final ThreadLocal<SimpleDateFormat> dbDateTimeFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
      
    public static String toStringSOAPMessage(SOAPMessage soapMessage) throws Exception {
        Source source = soapMessage.getSOAPPart().getContent();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);
        transformer.transform(source, streamResult);

        return stringWriter.toString();
    }

    public static SOAPMessage getSoapMessageFromString(String xml) throws SOAPException, IOException {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))));
        return message;
    }

    public static Map convertNodesFromXml(String xml) throws Exception {

        InputStream is = new ByteArrayInputStream(xml.getBytes());
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(is);
        return (Map) createMap(document.getDocumentElement());
    }

    public static Object createMap(Node node) {
        Map<String, Object> map = new HashMap<String, Object>();
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            String name = currentNode.getNodeName();
            Object value = null;
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                value = createMap(currentNode);
            } else if (currentNode.getNodeType() == Node.TEXT_NODE) {
                return currentNode.getTextContent();
            }
            if (map.containsKey(name)) {
                Object object = map.get(name);
                if (object instanceof List) {
                    ((List<Object>) object).add(value);
                } else {
                    List<Object> objectList = new LinkedList<Object>();
                    objectList.add(object);
                    objectList.add(value);
                    map.put(name, objectList);
                }
            } else {
                map.put(name, value);
            }
        }
        return map;
    }

    public static int pingAPIURL(String url, int timeout) {
        // Otherwise an exception may be thrown on invalid SSL certificates:
        url = url.replaceFirst("^https", "http");

        int responseCode = 0;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("HEAD");
            responseCode = connection.getResponseCode();
            return responseCode;
        } catch (IOException exception) {
            return responseCode;
        }
    }

    public static Map<String, String> convertStringQueryToMap(String query) throws Exception {
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }

    public static Document createDOMDocument(String xml) throws Exception {
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(is);

        return document;
    }

    public static String toStringXMLMessage(Document xmlPayload) throws Exception {
        DOMSource source = new DOMSource(xmlPayload);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);
        transformer.transform(source, streamResult);

        return stringWriter.toString().replace("\n", "");
    }

    public static String logPreString() {
        return "ESBTMSService | " + Thread.currentThread().getStackTrace()[2].getClassName() + " | "
                + Thread.currentThread().getStackTrace()[2].getLineNumber() + " | "
                + Thread.currentThread().getStackTrace()[2].getMethodName() + "() | ";
    }

    public static String generateISOAmount(String origAmount) {
        double preAmount = Double.parseDouble(origAmount) * 100;
        int intAmount = (int) preAmount;
        String amount = String.format("%012d", intAmount);

        if (intAmount > 0) {
            amount = "C" + amount;
        } else {
            amount = "C000000000000" + amount.replace("-", "D");
        }
        return amount;
    } 
}
