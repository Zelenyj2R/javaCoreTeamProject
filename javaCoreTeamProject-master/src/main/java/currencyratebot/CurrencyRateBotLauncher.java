package currencyratebot;

import currencyratebot.controller.CurrencyRateBotController;
import currencyratebot.service.AutoNotificationService;
import currencyratebot.service.request.DailyCurrencyRateRequestService;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class CurrencyRateBotLauncher {

    public static void main(String[] args) {

        DailyCurrencyRateRequestService dailyCurrencyRateRequestService = new DailyCurrencyRateRequestService();
        dailyCurrencyRateRequestService.saveCurrencyRatesToList();
        Thread currencyRatesSaver = new Thread(dailyCurrencyRateRequestService);
        currencyRatesSaver.start();

        AutoNotificationService autoNotificationService = new AutoNotificationService();
        Thread currencyRateSender = new Thread(autoNotificationService);
        currencyRateSender.start();

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new CurrencyRateBotController());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
