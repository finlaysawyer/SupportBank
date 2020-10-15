package training.supportbank.file;

import java.util.List;

import training.supportbank.Account;

public interface ImportFile {
    List<Account> importFile(String fileLocation);
}