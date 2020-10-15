package training.supportbank.file;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import training.supportbank.Account;
import training.supportbank.Transaction;

public class ImportCsvFile implements ImportFile {

    final Logger LOGGER = LogManager.getLogger();

    @Override
    public List<Account> importFile(String fileLocation) {
        CSVReader records = null;
        List<Account> accountList = new ArrayList<Account>();

        try {
            records = new CSVReader(new FileReader(fileLocation));
            String[] record = null;
            records.readNext();

            while ((record = records.readNext()) != null) {
                Transaction transaction = null;
                try {
                    transaction = new Transaction(record[2], record[1], record[0], record[3], Float.parseFloat(record[4]));
                } catch (NumberFormatException e) {
                    LOGGER.info("Could not import record due to incorrect formatting", e);
                    continue;
                }

                String nameTo = record[2];
                String nameFrom = record[1];
                Float balance = Float.parseFloat(record[4]);

                Account nameToAccount = null;
                Account nameFromAccount = null;

                if (!accountList.isEmpty()) {
                    for (int i = 0; i < accountList.size(); i++) {
                        if (accountList.get(i).getName().equals(nameTo)) {
                            nameToAccount = accountList.get(i);
                        }
                        if (accountList.get(i).getName().equals(nameFrom)) {
                            nameFromAccount = accountList.get(i);
                        }
                    }

                    if (nameToAccount == null) {
                        nameToAccount = new Account(nameTo, balance);
                        accountList.add(nameToAccount);
                        nameToAccount.addTransaction(transaction);
                    } else {
                        nameToAccount.addToBalance(balance);
                        nameToAccount.addTransaction(transaction);
                    }

                    if (nameFromAccount == null) {
                        nameFromAccount = new Account(nameFrom, -1 * balance);
                        accountList.add(nameFromAccount);
                        nameFromAccount.addTransaction(transaction);
                    } else {
                        nameFromAccount.removeFromBalance(balance);
                        nameFromAccount.addTransaction(transaction);
                    }
                    
                } else {
                    nameToAccount = new Account(nameTo, balance);
                    accountList.add(nameToAccount);
                    nameToAccount.addTransaction(transaction);

                    nameFromAccount = new Account(nameFrom, -1 * balance);
                    accountList.add(nameFromAccount);
                    nameFromAccount.addTransaction(transaction);
                }
            }
            records.close();

        } catch (IOException | NumberFormatException | CsvValidationException e) {
            System.out.println("Couldn't find file or encountered a formatting exception: " + e);
        }
        return accountList;
    }

}
