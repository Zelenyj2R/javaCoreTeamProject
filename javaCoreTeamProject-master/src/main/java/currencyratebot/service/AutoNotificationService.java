package currencyratebot.service;

import currencyratebot.controller.CurrencyRateBotController;
import currencyratebot.dto.settings.UserSettingDto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static currencyratebot.controller.CurrencyRateBotController.userSettingDtoList;

public class AutoNotificationService implements Runnable {
    @Override
    public void run() {
        while (true) {
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime nextHour = currentTime.plusHours(1).truncatedTo(ChronoUnit.HOURS);
            Duration duration = Duration.between(currentTime, nextHour);
            int currentHour = currentTime.getHour();
            if (currentTime.getMinute() == 0 && currentHour >= 9 && currentHour <= 18) {
                sendNotificationByUserSetting(currentHour);
            }
            try {
                Thread.sleep(duration.toMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendNotificationByUserSetting(int currentHour) {
        for (UserSettingDto userSettingsDto : userSettingDtoList) {
            if (userSettingsDto.getNotificationTime().equals(String.valueOf(currentHour))) {
                CurrencyRateBotController currencyRateBotController = new CurrencyRateBotController();
                MenuCreationService menuCreationService = new MenuCreationService();
                SendMessage newMessage = menuCreationService.getAutoRateMenu(userSettingsDto.getChatId(), userSettingsDto, false);
                try {
                    currencyRateBotController.execute(newMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
