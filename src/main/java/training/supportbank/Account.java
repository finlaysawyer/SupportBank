package training.supportbank;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class Account {
    private String name;
    private float balance;
    private List<Transaction> transactions = new ArrayList<>();

    public Account(String name, float balance) {
        this.name = name;
        this.balance = balance;
    }

    public void addToBalance(float add) {
        balance += add;
    }

    public void removeFromBalance(float remove) {
        balance -= remove;
    }

    public void addTransaction(Transaction trans) {
        transactions.add(trans);
    }
    
}
