package currencyratebot.dto.currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import currencyratebot.enums.BankName;
import currencyratebot.enums.Currency;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRateDto {
    private Currency currency;
    private BigDecimal buyRate;
    private BigDecimal sellRate;
    private BankName bankName;
}