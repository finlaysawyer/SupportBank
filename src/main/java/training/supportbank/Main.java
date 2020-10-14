package training.supportbank;

import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.opencsv.CSVReader;

public class Main {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        while (true) {
            String command[] = in.nextLine().split(" ");

            if (command[0].equalsIgnoreCase("list")) {
                if (command[1].equalsIgnoreCase("all")) {
                    System.out.println("Listing all records:");

                    List<Account> accountList = importCSVAccounts();
            
                    for (Account acc : accountList) {
                        System.out.println(acc.getName() + ": " + new DecimalFormat("#.##").format(acc.getBalance()));
                    }
        
                } else if (command.length >= 3) {
                    String nameTarget = String.join(" ", sliceArray(command, 1, 3));
                    System.out.println("Listing transactions for " + nameTarget + ":");
                    System.out.println("Date | From | To | Narrative | Amount");

                    for (Transaction trans : getAccount(nameTarget).getTransactions()) {
                        System.out.println(trans.getReadableTransaction());
                    }
                }

            } else if (command[0].equalsIgnoreCase("exit")) {
                in.close();
                System.exit(0);
            }
        }
    }

    public static List<Account> importCSVAccounts() {
        CSVReader records = null;
        List<Account> accountList = new ArrayList<Account>();
        
        try {
            records = new CSVReader(new FileReader("D:\\dev_corndel\\SupportBank\\Transactions2014.csv"));
            String[] record = null;
            records.readNext();

            while ((record = records.readNext()) != null) {
                String nameTo = record[2];
                String nameFrom = record[1];
                Float balance = Float.parseFloat(record[4]);
                Transaction transaction = new Transaction(record[2], record[1], record[0], record[3], Float.parseFloat(record[4]));
                Account nameToAccount = null;
                Account nameFromAccount = null;
                
                if (!accountList.isEmpty()) {
                    for (int i = 0; i < accountList.size(); i++) {
                        if (accountList.get(i).getName().equals(nameTo)) { // If account already exists
                            nameToAccount = accountList.get(i);
                        }
                        if (accountList.get(i).getName().equals(nameFrom)) {
                            nameFromAccount = accountList.get(i);
                        }
                    }

                    if (nameToAccount == null) {
                        Account accountTo = new Account(nameTo, balance);
                        accountList.add(accountTo);
                        accountTo.addTransaction(transaction);
                    } else {
                        nameToAccount.addToBalance(balance);
                        nameToAccount.addTransaction(transaction);
                    }

                    if (nameFromAccount == null) {
                        Account accountFrom = new Account(nameFrom, -1 * balance);
                        accountList.add(accountFrom);
                        accountFrom.addTransaction(transaction);
                    } else {
                        nameFromAccount.removeFromBalance(balance);
                        nameFromAccount.addTransaction(transaction);
                        
                    }
                } else {
                    Account accountTo = new Account(nameTo, balance);
                    accountList.add(accountTo);
                    accountTo.addTransaction(transaction);
                    Account accountFrom = new Account(nameFrom, -1 * balance);
                    accountList.add(accountFrom);
                    accountFrom.addTransaction(transaction);
                }
            }
            records.close();

        } catch (Exception e) {
            System.out.println("Couldn't find file or encountered an error: " + e);
        }
        return accountList;
    }

    public static String[] sliceArray(String[] array, int indexFrom, int indexTo) {
        String[] slice = new String[indexTo - indexFrom];
        for (int i = 0; i < slice.length; i++) {
            slice[i] = array[indexFrom + i];
        }
        return slice;
    }

    public static Account getAccount(String name) {
        List<Account> accountList = importCSVAccounts();

        for (Account acc : accountList) {
            if (acc.getName().equals(name)) {
                return acc;
            }
        }
        return null;
    }

}