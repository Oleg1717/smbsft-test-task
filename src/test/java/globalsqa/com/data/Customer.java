package globalsqa.com.data;

public enum Customer {

    HERMOINE_GRANGER("Hermoine Granger"),
    HARRY_POTTER("Harry Potter"),
    RON_WEASLY("Ron Weasly"),
    ALBUS_DUMBLEDORE("Albus Dumbledore"),
    NEVILLE_LONGBOTTOM("Neville Longbottom");

    private final String name;

    Customer(String name) {
        this.name = name;
    }

    public String name_() {
        return name;
    }
}
