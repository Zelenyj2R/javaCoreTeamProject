package currencyratebot.service.request;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import currencyratebot.dto.currency.CurrencyRateDto;
import currencyratebot.dto.currency.PrivatCurrencyRateDto;
import currencyratebot.enums.BankName;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrivatCurrencyRateRequestService implements CurrencyRateRequestService {

    private static final String URL = "https://api.privatbank.ua/p24api/pubinfo?exchange&coursid=11";
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
                List<PrivatCurrencyRateDto> responseDtos = convertResponseToList(response.body());
                result = responseDtos.stream()
                        .map(dto -> new CurrencyRateDto(dto.getCcy(), dto.getBuy(), dto.getSale(), BankName.PRIVATBANK))
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<PrivatCurrencyRateDto> convertResponseToList(String response) {
        Type type = TypeToken.getParameterized(List.class, PrivatCurrencyRateDto.class).getType();
        return new Gson().fromJson(response, type);
    }
}
