package training.supportbank;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import training.supportbank.file.ImportCsvFile;
import training.supportbank.file.ImportJsonFile;

public class Main {

    public static void main(String args[]) {

        //final Logger LOGGER = LogManager.getLogger();
        Scanner in = new Scanner(System.in);
        List<String> imports = new ArrayList<String>();

        while (true) {

            String command[] = new String[] { "" };
            
            try {
                command = in.nextLine().split(" ");
            } catch (Exception e) {
                System.out.println("No input");
            }

            if (command[0].equalsIgnoreCase("list")) {

                if (command[1].equalsIgnoreCase("all")) { // List all balances

                    System.out.println("Listing all records:");
                    List<Account> accountList = getAccountsList();

                    for (Account acc : accountList) {
                        System.out.println(acc.getName() + ": " + new DecimalFormat("#.##").format(acc.getBalance()));
                    }

                } else if (command.length >= 3) { // List user transactions

                    String nameTarget = String.join(" ", sliceArray(command, 1, 3));
                    System.out.println("Listing transactions for " + nameTarget + ":");
                    
                    if (getAccount(nameTarget) != null) {
                        System.out.println("Date | From | To | Narrative | Amount");
                        for (Transaction trans : getAccount(nameTarget).getTransactions()) {
                            System.out.println(trans.getReadableTransaction());
                        }
                    } else {
                        System.out.println("No transactions were found for " + nameTarget);
                    }

                } else {
                    System.out.println("Please specify a valid sub-command.");
                }

            } else if (command[0].equalsIgnoreCase("import")) { // Import files
            
                if (command.length >= 2) {

                    String ext = getExtension(command[1]);

                    if (ext.equalsIgnoreCase("csv") || ext.equalsIgnoreCase("json") || ext.equalsIgnoreCase("xml")) {
                        imports.add(command[1]);
                    } else {
                        System.out.println("Invalid file format");
                    }

                } else {
                    System.out.println("Please specify a valid sub-command.");
                }

            } else if (command[0].equalsIgnoreCase("export")) { // Export Transactions to File
                in.close();
                System.exit(0);

            } else if (command[0].equalsIgnoreCase("exit")) { // Exit
                in.close();
                System.exit(0);

            } else {
                System.out.println("That command does not exist.");
            }
        }
    }

    public static String[] sliceArray(String[] array, int indexFrom, int indexTo) {
        String[] slice = new String[indexTo - indexFrom];
        for (int i = 0; i < slice.length; i++) {
            slice[i] = array[indexFrom + i];
        }
        return slice;
    }

    public static Account getAccount(String name) {
        for (Account acc : getAccountsList()) {
            if (acc.getName().equals(name)) {
                return acc;
            }
        }
        return null;
    }

    public static List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<Account>();
        
        accountList = new ImportCsvFile().importFile("D:\\dev_corndel\\SupportBank\\Transactions2014.csv", accountList);
        accountList = new ImportCsvFile().importFile("D:\\dev_corndel\\SupportBank\\DodgyTransactions2015.csv", accountList);
        accountList = new ImportJsonFile().importFile("D:\\dev_corndel\\SupportBank\\Transactions2013.json", accountList);

        return accountList;
    }

    public static List<Account> getAccountsListNew(List<String> files) {

        List<Account> accountList = new ArrayList<Account>();

        for (String file : files) {
            String ext = getExtension(file);
            if (ext.equalsIgnoreCase("csv")) {
                accountList = new ImportCsvFile().importFile(file, accountList);
            } else if (ext.equalsIgnoreCase("json")) {
                accountList = new ImportJsonFile().importFile(file, accountList);
            } else if (ext.equalsIgnoreCase("xml")) {
                //accountList = new ImportXmlFile().importFile(file, accountList);
            }
        }

        return accountList;

    }

    public static String getExtension(String fileName) {
        Integer index = fileName.lastIndexOf(".");
        if (index > 0) {
            return fileName.substring(index + 1);
        }
        return "";
    }
        
}