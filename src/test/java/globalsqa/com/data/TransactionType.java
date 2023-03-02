package globalsqa.com.data;

public enum TransactionType {

    CREDIT("Credit"),
    DEBIT("Debit");

    private final String value;

    TransactionType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
