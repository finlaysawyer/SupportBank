package training.supportbank.file;

import java.util.List;

import training.supportbank.Account;
import training.supportbank.Transaction;

abstract class ImportFile {
    abstract List<Account> importFile(String fileLocation, List<Account> accountList);

    private static List<Account> createAccount(List<Account> accountList, String name, Float amount, Transaction transaction) {
        Account account = new Account(name, amount);
        accountList.add(account);
        account.addTransaction(transaction);
        return accountList;
    }

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
                accountList = createAccount(accountList, nameTo, amount, transaction);
            } else {
                nameToAccount.addToBalance(amount);
                nameToAccount.addTransaction(transaction);
            }

            if (nameFromAccount == null) {
                accountList = createAccount(accountList, nameFrom, -1 * amount, transaction);
            } else {
                nameFromAccount.removeFromBalance(amount);
                nameFromAccount.addTransaction(transaction);
            }
            
        } else {
            accountList = createAccount(accountList, nameTo, amount, transaction);
            accountList = createAccount(accountList, nameFrom, -1 * amount, transaction);
        }
        return accountList;
    }
}