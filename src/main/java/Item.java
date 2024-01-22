import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
public class Item {
    private String itemName;
    private long price;
    private int amount;
    private int ptn;
    private int qlt;
    private int time;

    public Item(String itemName, long price, int amount, int ptn, int qlt, int time) {
        this.itemName = itemName;
        this.price = price;
        this.amount = amount;
        this.ptn = ptn;
        this.qlt = qlt;
        this.time = time;
    }
}