package currencyratebot.dto.currency;

import currencyratebot.enums.Currency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PrivatCurrencyRateDto {
    private Currency ccy;
    private Currency base_ccy;
    private BigDecimal buy;
    private BigDecimal sale;
}
