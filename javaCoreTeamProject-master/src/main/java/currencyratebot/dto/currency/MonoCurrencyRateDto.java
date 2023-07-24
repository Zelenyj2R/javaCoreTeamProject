package currencyratebot.dto.currency;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonoCurrencyRateDto {
    private Integer currencyCodeA;
    private Integer currencyCodeB;
    private BigDecimal rateBuy;
    private BigDecimal rateSell;
}