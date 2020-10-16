package training.supportbank;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

import training.supportbank.file.ExportCsvFile;
import training.supportbank.file.ImportCsvFile;
import training.supportbank.file.ImportJsonFile;
import training.supportbank.file.ImportXmlFile;

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

                    //System.out.println("Name | Balance");
                    List<Account> accountList = getAccountsList(imports);

                    List<List<String>> rows = new ArrayList<List<String>>();
                    rows.add( Arrays.asList("Name", "Balance") );
                    rows.add( Arrays.asList("-------", "-------") );

                    for (Account acc : accountList) {
                        List<String> cols = new ArrayList<String>();
                        cols.add(acc.getName());
                        cols.add(new DecimalFormat("#.##").format(acc.getBalance()));
                        rows.add(cols);
                    }

                    printRows(rows);

                } else if (command.length >= 3) { // List user transactions

                    String nameTarget = String.join(" ", sliceArray(command, 1, 3));
                    System.out.println("Listing transactions for " + nameTarget + ":");
                    
                    if (getAccount(nameTarget, imports) != null) {

                        List<List<String>> rows = new ArrayList<List<String>>();
                        rows.add( Arrays.asList("Date", "Type", "Net", "Narrative") );
                        rows.add( Arrays.asList("----------", "--------", "-----", "-------------") );
                        for (Transaction trans : getAccount(nameTarget, imports).getTransactions()) {
                            rows.add(trans.getReadableTransaction(nameTarget));
                        }
                        printRows(rows);

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
                new ExportCsvFile().exportFile(getAccountsList(imports));
                System.out.println("Exported transactions.");

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
            String fullPath = "src\\main\\resources\\" + file;
            if (ext.equalsIgnoreCase("csv")) {
                accountList = new ImportCsvFile().importFile(fullPath, accountList);
            } else if (ext.equalsIgnoreCase("json")) {
                accountList = new ImportJsonFile().importFile(fullPath, accountList);
            } else if (ext.equalsIgnoreCase("xml")) {
                accountList = new ImportXmlFile().importFile(fullPath, accountList);
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

    public static Integer getHighestLen(List<List<String>> rows, Integer colIndex) {
        Integer lenLargest = 0;
        for (List<String> cols : rows) {
            Integer lenCurrent = cols.get(colIndex).length();
            if (lenCurrent > lenLargest) {
                lenLargest = lenCurrent;
            }
        }
        return lenLargest;
    }

    public static void printRows(List<List<String>> rows) {
        for (List<String> row : rows) {
            String send = "";
            Integer index = 0;
            for (String col : row) {
                send += (col + new String(new char[getHighestLen(rows, index) - col.length()]).replace("\0", " ") + " | ");
                index += 1;
            }
            System.out.print(send.substring(0, send.length() - 3) + "\n");
        }
    }
 
}