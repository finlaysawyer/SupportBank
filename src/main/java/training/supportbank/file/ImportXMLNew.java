package training.supportbank.file;

import java.util.Calendar;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.text.SimpleDateFormat;

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
    
            NodeList nodeList = doc.getElementsByTagName("SupportTransaction");
    
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element field = (Element) node;

                String rawDate = field.getAttribute("Date").toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Calendar cal = Calendar.getInstance();
                cal.setTime(sdf.parse("01/01/1900"));
                cal.add(Calendar.DATE, Integer.parseInt(rawDate));
                String date = sdf.format(cal.getTime());

                String narrative = getContent(field, "Description");
                Float amount = Float.parseFloat(getContent(field, "Value"));
                String nameFrom = "";
                String nameTo = "";

                NodeList subNodeList = field.getElementsByTagName("Parties");
                
                for (int x = 0; x < subNodeList.getLength(); x++) { 
                    Node subNode = subNodeList.item(x);
                    Element subField = (Element) subNode;
                    nameFrom = getContent(subField, "From");
                    nameTo = getContent(subField, "To");
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

    public String getContent(Element field, String name) {
        return field.getElementsByTagName(name).item(0).getTextContent();
    }

}