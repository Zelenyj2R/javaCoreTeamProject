package currencyratebot.enums;

public enum BankName {
    MONOBANK("monobank"),
    PRIVATBANK("ПриватБанк"),
    NBU("НБУ");

    public final String name;
    BankName(String bankName) {
        this.name = bankName;
    }
    
}
