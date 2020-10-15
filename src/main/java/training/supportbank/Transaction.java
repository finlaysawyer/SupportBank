package training.supportbank;

import lombok.Getter;

@Getter
public class Transaction {
  private String to;
  private String from;
  private String date;
  private String narrative;
  private Float amount;

  public Transaction(String to, String from, String date, String narrative, Float amount) {
    this.to = to;
    this.from = from;
    this.date = date;
    this.narrative = narrative;
    this.amount = amount;
  }

  public String getReadableTransaction() {
    return date + " | " + from + " | " + to + " | " + narrative + " | " + amount;
  }

}