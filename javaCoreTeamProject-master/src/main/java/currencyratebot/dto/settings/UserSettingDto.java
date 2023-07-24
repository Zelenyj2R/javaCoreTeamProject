package currencyratebot.dto.settings;

import currencyratebot.enums.BankName;
import currencyratebot.enums.Currency;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class UserSettingDto {
    private long chatId;
    private int decimalCount;
    private List<Currency> currency;
    private BankName bank;
    private String notificationTime;

    public UserSettingDto(long chatId) {
        this.chatId = chatId;
        decimalCount = 2;
        currency = new ArrayList<>(){
            {
                add(Currency.USD);
            }
        };
        bank = BankName.PRIVATBANK;
        notificationTime = "OFF";
    }

    public void setNotificationTime(String notificationTime) {
        if (notificationTime.equalsIgnoreCase("Вимкнути сповіщення")) {
            this.notificationTime = "OFF";
        } else {
            this.notificationTime = notificationTime.replace(":00", "");
        }
    }
}
