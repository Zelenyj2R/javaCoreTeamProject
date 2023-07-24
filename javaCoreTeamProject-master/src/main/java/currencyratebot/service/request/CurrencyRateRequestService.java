package currencyratebot.service.request;

import currencyratebot.dto.currency.CurrencyRateDto;

import java.util.List;

public interface CurrencyRateRequestService {
    List<CurrencyRateDto> getCurrencyRates();
}
