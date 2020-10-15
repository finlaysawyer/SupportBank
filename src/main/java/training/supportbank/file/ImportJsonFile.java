package training.supportbank.file;

import java.io.FileReader;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import training.supportbank.Account;

public class ImportJsonFile extends ImportFile {

    final Logger LOGGER = LogManager.getLogger();

    @Override
    public List<Account> importFile(String fileLocation, List<Account> accountList) {
        try {
            JsonParser parser = new JsonParser();

            JsonArray array = parser.parse(new FileReader(fileLocation)).getAsJsonArray();

            for (int i = 0; i < array.size(); i++) {
                JsonObject object = array.get(i).getAsJsonObject();
                String nameTo = object.get("toAccount").getAsString();
                String nameFrom = object.get("fromAccount").getAsString();
                String date = object.get("date").getAsString();
                String narrative = object.get("narrative").getAsString();
                Float amount = object.get("amount").getAsFloat();

                try {
                    accountList = processTransaction(accountList, nameTo, nameFrom, date, narrative, amount);
                } catch (NumberFormatException e) {
                    LOGGER.info("Could not import record due to incorrect formatting", e);
                    continue;
                }
            }
            return accountList;
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
