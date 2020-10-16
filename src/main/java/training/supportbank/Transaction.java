package training.supportbank;

import lombok.Getter;
import java.util.Arrays;
import java.util.List;
import java.text.DecimalFormat;

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

  public List<String> getReadableTransaction(String name) {
    String type = null;
    String nameTarget = null;
    Float net = amount;
    if (from.equals(name)) {
      type = "Outgoing";
      net = -1 * amount;
      nameTarget = to;
    } else {
      type = "Incoming";
      nameTarget = from;
    }
    return Arrays.asList(date, type, nameTarget, new DecimalFormat("#.##").format(net), narrative);

  }

}