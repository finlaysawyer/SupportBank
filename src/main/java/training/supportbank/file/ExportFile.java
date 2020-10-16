package training.supportbank.file;

import java.util.List;

import training.supportbank.Account;

public interface ExportFile {
    public void exportFile(List<Account> accountList);
}
