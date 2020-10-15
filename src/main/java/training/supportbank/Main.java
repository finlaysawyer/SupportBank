package training.supportbank;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;

import training.supportbank.file.ImportCsvFile;

public class Main {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        while (true) {
            String command[] = in.nextLine().split(" ");

            if (command[0].equalsIgnoreCase("list")) {
                if (command[1].equalsIgnoreCase("all")) {
                    System.out.println("Listing all records:");

                    List<Account> accountList = new ImportCsvFile().importFile("D:\\dev_corndel\\SupportBank\\Transactions2014.csv");

                    for (Account acc : accountList) {
                        System.out.println(acc.getName() + ": " + new DecimalFormat("#.##").format(acc.getBalance()));
                    }

                } else if (command.length >= 3) {
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

            } else if (command[0].equalsIgnoreCase("exit")) {
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
        List<Account> accountList = new ImportCsvFile().importFile("D:\\dev_corndel\\SupportBank\\Transactions2014.csv");

        for (Account acc : accountList) {
            if (acc.getName().equals(name)) {
                return acc;
            }
        }
        return null;
    }

}