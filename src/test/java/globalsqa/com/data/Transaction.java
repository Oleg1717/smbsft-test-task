package globalsqa.com.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Transaction {

    private final static DateTimeFormatter DATE_FORMAT = DateTimeFormatter
            .ofPattern("MMM d, yyyy h:mm:ss a", Locale.ENGLISH);

    private final LocalDateTime dateTime;
    private final String amount;
    private final String transactionType;

    public Transaction(String amount, String transactionType) {
        this.dateTime = null;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public Transaction(String dateTime, String amount, String transactionType) {
        this.dateTime = LocalDateTime.parse(dateTime, DATE_FORMAT);
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public LocalDateTime dateTime() {
        return dateTime;
    }

    public String amount() {
        return amount;
    }

    public String transactionType() {
        return transactionType;
    }
}
