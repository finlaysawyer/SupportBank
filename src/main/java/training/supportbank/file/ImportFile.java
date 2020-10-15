package training.supportbank.file;

import java.util.List;

import training.supportbank.Account;
import training.supportbank.Transaction;

abstract class ImportFile {
    abstract List<Account> importFile(String fileLocation, List<Account> accountList);

    public static List<Account> processTransaction(List<Account> accountList, String nameTo, String nameFrom, String date, String narrative, Float amount) {
        Transaction transaction = new Transaction(nameTo, nameFrom, date, narrative, amount);

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
                nameToAccount = new Account(nameTo, amount);
                accountList.add(nameToAccount);
                nameToAccount.addTransaction(transaction);
            } else {
                nameToAccount.addToBalance(amount);
                nameToAccount.addTransaction(transaction);
            }

            if (nameFromAccount == null) {
                nameFromAccount = new Account(nameFrom, -1 * amount);
                accountList.add(nameFromAccount);
                nameFromAccount.addTransaction(transaction);
            } else {
                nameFromAccount.removeFromBalance(amount);
                nameFromAccount.addTransaction(transaction);
            }
            
        } else {
            nameToAccount = new Account(nameTo, amount);
            accountList.add(nameToAccount);
            nameToAccount.addTransaction(transaction);

            nameFromAccount = new Account(nameFrom, -1 * amount);
            accountList.add(nameFromAccount);
            nameFromAccount.addTransaction(transaction);
        }
        return accountList;
    }
}