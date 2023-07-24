package currencyratebot.service.request;

import currencyratebot.dto.currency.CurrencyRateDto;
import currencyratebot.service.CurrencyRateMessageService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DailyCurrencyRateRequestService implements Runnable{
    public static List<CurrencyRateDto> currencyRateDtos = new ArrayList<>();
    private static final CurrencyRateMessageService currencyRateMessageService = new CurrencyRateMessageService();

    @Override
    public void run() {
        while (true) {
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime nextDayTime = currentTime.plusDays(1).truncatedTo(ChronoUnit.DAYS).plusHours(12);
            LocalDateTime currentDayTime = currentTime.truncatedTo(ChronoUnit.DAYS).plusHours(10);
            Duration duration;
            int currentHour = currentTime.getHour();
            if (currentTime.getMinute() == 0 && currentHour == 12) {
                saveCurrencyRatesToList();
            }
            if (currentHour <= 12) {
                duration = Duration.between(currentTime, currentDayTime);
                try {
                    Thread.sleep(duration.toMillis());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                duration = Duration.between(currentTime, nextDayTime);
                try {
                    Thread.sleep(duration.toMillis());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void saveCurrencyRatesToList() {
        currencyRateDtos = currencyRateMessageService.getActualRates();
    }
}
