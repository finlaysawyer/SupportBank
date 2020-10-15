package training.supportbank.file;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

import training.supportbank.Account;

public class ImportXMLNew extends ImportFile {

    final Logger LOGGER = LogManager.getLogger();

    @Override
    public List<Account> importFile(String fileLocation, List<Account> accountList) {
        
        try {
            File records = new File(fileLocation);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(records);
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
    
            NodeList nodeList = doc.getElementsByTagName("SupportTransaction");
    
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element field = (Element) node;

                String date = field.getAttribute("Date").toString();
                String narrative = field.getElementsByTagName("Description").item(0).getTextContent();
                Float amount = Float.parseFloat(field.getElementsByTagName("Value").item(0).getTextContent());
                String nameFrom = "";
                String nameTo = "";

                NodeList subNodeList = field.getElementsByTagName("Parties");
                
                for (int x = 0; x < subNodeList.getLength(); x++) { 
                    Node subNode = subNodeList.item(x);
                    Element subField = (Element) subNode;
                    nameFrom = subField.getElementsByTagName("From").item(0).getTextContent();
                    nameTo = subField.getElementsByTagName("To").item(0).getTextContent();
                }

                try {
                    accountList = processTransaction(accountList, nameTo, nameFrom, date, narrative, amount);
                } catch (NumberFormatException e) {
                    LOGGER.info("Could not import record due to incorrect formatting", e);
                    continue;
                }

            }
                
        } catch (Exception e) {
            LOGGER.info("Couldn't parse XML", e);
        }
        
        return accountList;
        
    }
}