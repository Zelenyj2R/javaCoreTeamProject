package currencyratebot.dto.currency;

import currencyratebot.enums.Currency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NbuCurrencyRateDto {
    private Currency cc;
    private BigDecimal rate;
}
