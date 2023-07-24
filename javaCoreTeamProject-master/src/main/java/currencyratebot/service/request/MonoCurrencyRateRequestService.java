package currencyratebot.service.request;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import currencyratebot.dto.currency.CurrencyRateDto;
import currencyratebot.dto.currency.MonoCurrencyRateDto;
import currencyratebot.enums.BankName;
import currencyratebot.enums.Currency;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static currencyratebot.enums.Currency.*;

public class MonoCurrencyRateRequestService implements CurrencyRateRequestService {
    private static final String URL = "https://api.monobank.ua/bank/currency";
    private static final Map<Integer, Currency> codeCurr = Map.of(
            980, UAH,
            840, USD,
            978, EUR
    );

    @Override
    public List<CurrencyRateDto> getCurrencyRates() {
        List<CurrencyRateDto> result = new ArrayList<>();
        try {
            Connection.Response response = Jsoup.connect(URL).timeout(5000).ignoreContentType(true).execute();
            int i = 0;
            while (response.statusCode() == 504 && i < 2) {
                response = Jsoup.connect(URL).timeout(5000).ignoreContentType(true).execute();
                i++;
            }
            if (response.statusCode() == 200) {
                List<MonoCurrencyRateDto> currencyRateResponses = convertResponseToList(response.body());
                result = currencyRateResponses.stream()
                        .filter(item -> codeCurr.containsKey(item.getCurrencyCodeA())
                                && codeCurr.containsKey(item.getCurrencyCodeB())
                                && item.getCurrencyCodeB().equals(980)
                        )
                        .map(item -> new CurrencyRateDto(
                                codeCurr.get(item.getCurrencyCodeA()),
                                item.getRateBuy(),
                                item.getRateSell(),
                                BankName.MONOBANK
                        ))
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<MonoCurrencyRateDto> convertResponseToList(String response) {
        Type type = TypeToken.getParameterized(List.class, MonoCurrencyRateDto.class).getType();
        return new Gson().fromJson(response, type);
    }
}
