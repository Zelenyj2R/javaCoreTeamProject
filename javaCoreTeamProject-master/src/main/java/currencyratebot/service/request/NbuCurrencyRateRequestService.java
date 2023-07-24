package currencyratebot.service.request;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import currencyratebot.dto.currency.CurrencyRateDto;
import currencyratebot.dto.currency.NbuCurrencyRateDto;
import currencyratebot.enums.BankName;
import currencyratebot.enums.Currency;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NbuCurrencyRateRequestService implements CurrencyRateRequestService {
    private static final String URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";

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
                List<NbuCurrencyRateDto> responseDtos = convertResponseToList(response.body());
                result = responseDtos.stream()
                        .map(dto -> new CurrencyRateDto(dto.getCc(), dto.getRate(), dto.getRate(), BankName.NBU))
                        .filter(dto -> dto.getCurrency() == Currency.USD || dto.getCurrency() == Currency.EUR)
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<NbuCurrencyRateDto> convertResponseToList(String response) {
        Type type = TypeToken.getParameterized(List.class, NbuCurrencyRateDto.class).getType();
        return new Gson().fromJson(response, type);
    }
}
