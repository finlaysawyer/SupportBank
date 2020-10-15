package training.supportbank;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import training.supportbank.file.ImportCsvFile;
import training.supportbank.file.ImportJsonFile;
import training.supportbank.file.ImportXMLNew;

public class Main {

    public static void main(String args[]) {
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
                    List<Account> accountList = getAccountsList(imports);

                    for (Account acc : accountList) {
                        System.out.println(acc.getName() + ": " + new DecimalFormat("#.##").format(acc.getBalance()));
                    }

                } else if (command.length >= 3) { // List user transactions

                    String nameTarget = String.join(" ", sliceArray(command, 1, 3));
                    System.out.println("Listing transactions for " + nameTarget + ":");
                    
                    if (getAccount(nameTarget, imports) != null) {
                        System.out.println("Date | From | To | Narrative | Amount");
                        for (Transaction trans : getAccount(nameTarget, imports).getTransactions()) {
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
                        System.out.println("File imported");
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

    public static Account getAccount(String name, List<String> imports) {
        for (Account acc : getAccountsList(imports)) {
            if (acc.getName().equals(name)) {
                return acc;
            }
        }
        return null;
    }

    public static List<Account> getAccountsList(List<String> files) {
        List<Account> accountList = new ArrayList<Account>();

        for (String file : files) {
            String ext = getExtension(file);
            String fullPath = "D:\\dev_corndel\\SupportBank\\" + file;
            if (ext.equalsIgnoreCase("csv")) {
                accountList = new ImportCsvFile().importFile(fullPath, accountList);
            } else if (ext.equalsIgnoreCase("json")) {
                accountList = new ImportJsonFile().importFile(fullPath, accountList);
            } else if (ext.equalsIgnoreCase("xml")) {
                accountList = new ImportXMLNew().importFile(fullPath, accountList);
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