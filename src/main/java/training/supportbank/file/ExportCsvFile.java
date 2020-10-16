package training.supportbank.file;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.opencsv.CSVWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import training.supportbank.Account;
import training.supportbank.Transaction;

public class ExportCsvFile implements ExportFile {

    final Logger LOGGER = LogManager.getLogger();
    private static final String EXPORT_FILE = "./exported-transactions.csv";

    @Override
    public void exportFile(List<Account> accountList) {
        try {
            Writer writer = Files.newBufferedWriter(Paths.get(EXPORT_FILE));

            CSVWriter csvWriter = new CSVWriter(writer,
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
                
            String[] headerRecord = {"To", "Form", "Date", "Narrative", "Amount"};
            csvWriter.writeNext(headerRecord);

            for (Account acc : accountList) {
                for (Transaction trans : acc.getTransactions()) {
                    if (acc.getName().equals(trans.getFrom())) {
                        csvWriter.writeNext(new String[]{trans.getTo(), trans.getFrom(), trans.getDate(), trans.getNarrative(), trans.getAmount().toString()});
                    }
                }
            }
            csvWriter.close();
        } catch (IOException e) {
            LOGGER.error("Couldn't generate file or encountered a formatting exception: " + e);
        }
    }
}
