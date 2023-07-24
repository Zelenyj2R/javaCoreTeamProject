package currencyratebot.service;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import currencyratebot.dto.settings.UserSettingDto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;


import static java.lang.Math.toIntExact;

public class MenuCreationService {
    private static final CurrencyRateMessageService currencyRateMessageService = new CurrencyRateMessageService();
    private static final KeyboardCreationService keyboardCreationService = new KeyboardCreationService();
    private static final String UKRAINIAN_FLAG_EMOJI = ":ua:";
    private static final String DOLLAR_EMOJI = ":dollar:";
    private static final String EURO_EMOJI = ":euro:";
    private static final String WELCOME_MESSAGE = "%1$s Ласкаво просимо %1$s\nЦей бот допоможе відслідковувати актуальні курси валют\n%2$s %3$s";
    private static final String SETTINGS_MESSAGE = "Налаштування";
    private static final String DECIMAL_COUNT_MESSAGE = "Кількість знаків після коми";
    private static final String BANKNAME_MESSAGE = "Банк";
    private static final String CURRENCY_MESSAGE = "Валюти";
    private static final String NOTIFICATION_TIME_MESSAGE = "Виберіть час оповіщень";
    private static final String MAIN_MENU_MESSAGE = "Оберіть одну з наданих опцій";
    private static final String ERROR_MESSAGE = "В даний момент інформація не доступна.\nСпробуйте пізніше.";


    public SendMessage getStartMenu(long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(String.format(WELCOME_MESSAGE, EmojiParser.parseToUnicode(UKRAINIAN_FLAG_EMOJI),
                        EmojiParser.parseToUnicode(DOLLAR_EMOJI), EmojiParser.parseToUnicode(EURO_EMOJI)))
                .replyMarkup(keyboardCreationService.getMainKeyboard())
                .build();
    }

    public SendMessage getMainMenu(long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MAIN_MENU_MESSAGE)
                .replyMarkup(keyboardCreationService.getMainKeyboard())
                .build();
    }

    public SendMessage getRateMenu(long chatId, UserSettingDto userSettingsDto, boolean isNewRequestRequired) {
        String message = currencyRateMessageService.getRateResponse(userSettingsDto, isNewRequestRequired);
        InlineKeyboardMarkup keyboard;
        if (message.equals(ERROR_MESSAGE)) {
            keyboard = keyboardCreationService.getErrorKeyboard();
        } else {
            keyboard = keyboardCreationService.getMainKeyboard();
        }
        return SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .replyMarkup(keyboard)
                .build();
    }

    public SendMessage getAutoRateMenu(long chatId, UserSettingDto userSettingsDto, boolean isNewRequestRequired) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(currencyRateMessageService.getRateResponse(userSettingsDto, isNewRequestRequired))
                .replyMarkup(keyboardCreationService.getHomeKeyboard())
                .build();
    }

    public SendMessage getSettingsMenu(long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(SETTINGS_MESSAGE)
                .replyMarkup(keyboardCreationService.getSettingsKeyboard(chatId))
                .build();
    }


    public EditMessageText getDecimalCountMenu(long chatId, long messageId) {
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(toIntExact(messageId))
                .text(DECIMAL_COUNT_MESSAGE)
                .replyMarkup(keyboardCreationService.getDecimalCountKeyboard(chatId))
                .build();
    }

    public EditMessageText getBankMenu(long chatId, long messageId) {
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(toIntExact(messageId))
                .text(BANKNAME_MESSAGE)
                .replyMarkup(keyboardCreationService.getBankNameKeyboard(chatId))
                .build();
    }

    public EditMessageText getCurrencyMenu(long chatId, long messageId) {
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(toIntExact(messageId))
                .text(CURRENCY_MESSAGE)
                .replyMarkup(keyboardCreationService.getCurrencyKeyboard(chatId))
                .build();
    }

    public EditMessageText getUpdatedSettingsMenu(long chatId, long messageId) {
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(toIntExact(messageId))
                .text(SETTINGS_MESSAGE)
                .replyMarkup(keyboardCreationService.getSettingsKeyboard(chatId))
                .build();
    }

    public SendMessage getNotificationMenu(long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(NOTIFICATION_TIME_MESSAGE)
                .replyMarkup(keyboardCreationService.getNotificationKeyboard())
                .build();
    }
}
