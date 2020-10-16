package training.supportbank.file;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import training.supportbank.Account;

public class ImportCsvFile extends ImportFile {

    final Logger LOGGER = LogManager.getLogger();

    @Override
    public List<Account> importFile(String fileLocation, List<Account> accountList) {
        CSVReader records = null;

        try {
            records = new CSVReader(new FileReader(fileLocation));
            String[] record = null;
            records.readNext();

            while ((record = records.readNext()) != null) {
                try {
                    accountList = processTransaction(accountList, record[2], record[1], record[0], record[3], Float.parseFloat(record[4]));
                } catch (NumberFormatException e) {
                    LOGGER.error("Could not import record due to incorrect formatting", e);
                    continue;
                }
            }
            records.close();

        } catch (IOException | NumberFormatException | CsvValidationException e) {
            LOGGER.error("Couldn't find file or encountered a formatting exception: " + e);
        }
        return accountList;
    }

}
