package currencyratebot.service;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import currencyratebot.controller.CurrencyRateBotController;
import currencyratebot.dto.settings.UserSettingDto;
import currencyratebot.enums.BankName;
import currencyratebot.enums.Currency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class KeyboardCreationService {

    private static final String GET_INFO_BUTTON = "Отримати інфо";
    private static final String GET_INFO_CALLBACK = "getInfo";
    private static final String SETTINGS_BUTTON = "Налаштування";
    private static final String SETTINGS_CALLBACK = "settings";
    private static final String DECIMAL_COUNT_BUTTON = "Кількість знаків після коми (";
    private static final String DECIMAL_COUNT_CALLBACK = "set_decimalCount";
    private static final String BANK_BUTTON = "Банк (";
    private static final String BANK_CALLBACK = "set_bankName";
    private static final String CURRENCY_BUTTON = "Валюти ";
    private static final String CURRENCY_CALLBACK = "set_currency";
    private static final String NOTIFICATION_TIME_BUTTON = "Час оповіщень (";
    private static final String NOTIFICATION_TIME_CALLBACK = "notificationTime";
    private static final String HOME_CALLBACK = "home";
    private static final String HOME_EMOJI_BUTTON = ":house:";
    private static final String DECIMAL_TWO_OPTION = "2 ";
    private static final String DECIMAL_TWO_CALLBACK = "set_dec_decimalCountIsTwo";
    private static final String DECIMAL_THREE_OPTION = "3 ";
    private static final String DECIMAL_THREE_CALLBACK = "set_dec_decimalCountIsThree";
    private static final String DECIMAL_FOUR_OPTION = "4 ";
    private static final String DECIMAL_FOUR_CALLBACK = "set_dec_decimalCountIsFour";
    private static final String CHECK_MARK_EMOJI = ":white_check_mark:";
    private static final String EMPTY_STRING = "";
    private static final String NBU_OPTION = "НБУ ";
    private static final String NBU_CALLBACK = "set_bank_nbu";
    private static final String PRIVAT_OPTION = "ПриватБанк ";
    private static final String PRIVAT_CALLBACK = "set_bank_privatBank";
    private static final String MONOBANK_OPTION = "monobank ";
    private static final String MONOBANK_CALLBACK = "set_bank_monobank";
    private static final String USD_OPTION = "USD ";
    private static final String USD_CALLBACK = "set_cur_usd";
    private static final String EUR_OPTION = "EUR ";
    private static final String EUR_CALLBACK = "set_cur_eur";
    private static final String OPTION_NINE = "9:00";
    private static final String OPTION_TEN = "10:00";
    private static final String OPTION_ELEVEN = "11:00";
    private static final String OPTION_TWELVE = "12:00";
    private static final String OPTION_THIRTEEN = "13:00";
    private static final String OPTION_FOURTEEN = "14:00";
    private static final String OPTION_FIFTEEN = "15:00";
    private static final String OPTION_SIXTEEN = "16:00";
    private static final String OPTION_SEVENTEEN = "17:00";
    private static final String OPTION_EIGHTEEN = "18:00";
    private static final String OPTION_OFF = "Вимкнути сповіщення";
    private static final String BACK_EMOJI_BUTTON = ":arrow_left:";
    private static final String BACK_CALLBACK = "set_back";
    private static final String RIGHT_ROUND_BRACKET = ")";
    private static final String LEFT_ROUND_BRACKET = "(";
    private static final String LEFT_SQUARE_BRACKET = "[";
    private static final String RIGHT_SQUARE_BRACKET = "]";
    private static final String NOTIFICATION_OPTION_OFF = "OFF";
    private static final String MINUTES_OPTION = ":00";
    private static final String GET_RATES_AGAIN = "Зробити повторний запит";
    private static final String GET_INFO_AGAIN_CALLBACK = "getInfoAgain";

    public InlineKeyboardMarkup getMainKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(createButton(GET_INFO_BUTTON, GET_INFO_CALLBACK));
        rowsInline.add(createButton(SETTINGS_BUTTON, SETTINGS_CALLBACK));
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
    public InlineKeyboardMarkup getErrorKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(createButton(GET_RATES_AGAIN, GET_INFO_AGAIN_CALLBACK));
        rowsInline.add(createButton(SETTINGS_BUTTON, SETTINGS_CALLBACK));
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public InlineKeyboardMarkup getSettingsKeyboard(long chatId) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(createButton(DECIMAL_COUNT_BUTTON + getUserDecimalSetting(chatId) + RIGHT_ROUND_BRACKET, DECIMAL_COUNT_CALLBACK));
        rowsInline.add(createButton(BANK_BUTTON + getUserBankSetting(chatId).name + RIGHT_ROUND_BRACKET, BANK_CALLBACK));
        rowsInline.add(createButton(CURRENCY_BUTTON + getUserCurrencySetting(chatId)
                .toString()
                .replace(LEFT_SQUARE_BRACKET, LEFT_ROUND_BRACKET)
                .replace(RIGHT_SQUARE_BRACKET, RIGHT_ROUND_BRACKET), CURRENCY_CALLBACK));
        rowsInline.add(createButton(NOTIFICATION_TIME_BUTTON + getUserNotificationSetting(chatId) + RIGHT_ROUND_BRACKET, NOTIFICATION_TIME_CALLBACK));
        rowsInline.add(createButton(EmojiParser.parseToUnicode(HOME_EMOJI_BUTTON), HOME_CALLBACK));
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }


    public InlineKeyboardMarkup getDecimalCountKeyboard(long chatId) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(createButton(DECIMAL_TWO_OPTION + checkMarkForDecimalCount(chatId, 2), DECIMAL_TWO_CALLBACK));
        rowsInline.add(createButton(DECIMAL_THREE_OPTION + checkMarkForDecimalCount(chatId, 3), DECIMAL_THREE_CALLBACK));
        rowsInline.add(createButton(DECIMAL_FOUR_OPTION + checkMarkForDecimalCount(chatId, 4), DECIMAL_FOUR_CALLBACK));
        rowsInline.add(createBackHomeButtons());
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    private String checkMarkForDecimalCount(long chatId, int decimalOption) {
        Integer userDecimalSetting = getUserDecimalSetting(chatId);
        return userDecimalSetting == decimalOption
                ? EmojiParser.parseToUnicode(CHECK_MARK_EMOJI)
                : EMPTY_STRING;
    }

    private Integer getUserDecimalSetting(long chatId) {
        return CurrencyRateBotController.userSettingDtoList.stream()
                .filter(userSettings -> userSettings.getChatId() == chatId)
                .map(UserSettingDto::getDecimalCount)
                .reduce(0, Integer::sum);
    }


    public InlineKeyboardMarkup getBankNameKeyboard(long chatId) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(createButton(NBU_OPTION + checkMarkForBankName(chatId, BankName.NBU), NBU_CALLBACK));
        rowsInline.add(createButton(PRIVAT_OPTION + checkMarkForBankName(chatId, BankName.PRIVATBANK), PRIVAT_CALLBACK));
        rowsInline.add(createButton(MONOBANK_OPTION + checkMarkForBankName(chatId, BankName.MONOBANK), MONOBANK_CALLBACK));
        rowsInline.add(createBackHomeButtons());
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    private String checkMarkForBankName(long chatId, BankName bank) {
        BankName userBankSetting = getUserBankSetting(chatId);
        return userBankSetting == bank
                ? EmojiParser.parseToUnicode(CHECK_MARK_EMOJI)
                : EMPTY_STRING;
    }

    private BankName getUserBankSetting(long chatId) {
        return CurrencyRateBotController.userSettingDtoList.stream()
                .filter(userSettings -> userSettings.getChatId() == chatId)
                .map(UserSettingDto::getBank)
                .findFirst()
                .orElse(BankName.PRIVATBANK);
    }


    public InlineKeyboardMarkup getCurrencyKeyboard(long chatId) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(createButton(USD_OPTION + checkMarkForCurrency(chatId, Currency.USD), USD_CALLBACK));
        rowsInline.add(createButton(EUR_OPTION + checkMarkForCurrency(chatId, Currency.EUR), EUR_CALLBACK));
        rowsInline.add(createBackHomeButtons());
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    private String checkMarkForCurrency(long chatId, Currency currency) {
        List<Currency> userCurrencySetting = getUserCurrencySetting(chatId);
        return userCurrencySetting.contains(currency)
                ? EmojiParser.parseToUnicode(CHECK_MARK_EMOJI)
                : EMPTY_STRING;
    }

    private List<Currency> getUserCurrencySetting(long chatId) {
        return CurrencyRateBotController.userSettingDtoList.stream()
                .filter(userSettings -> userSettings.getChatId() == chatId)
                .map(UserSettingDto::getCurrency)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }


    public ReplyKeyboardMarkup getNotificationKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(OPTION_NINE);
        row.add(OPTION_TEN);
        row.add(OPTION_ELEVEN);
        keyboard.add(row);

        row = new KeyboardRow();
        row.add(OPTION_TWELVE);
        row.add(OPTION_THIRTEEN);
        row.add(OPTION_FOURTEEN);
        keyboard.add(row);

        row = new KeyboardRow();
        row.add(OPTION_FIFTEEN);
        row.add(OPTION_SIXTEEN);
        row.add(OPTION_SEVENTEEN);
        keyboard.add(row);

        row = new KeyboardRow();
        row.add(OPTION_EIGHTEEN);
        row.add(OPTION_OFF);
        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setOneTimeKeyboard(true);
        return keyboardMarkup;
    }

    private String getUserNotificationSetting(long chatId) {
        return CurrencyRateBotController.userSettingDtoList.stream()
                .filter(userSettings -> userSettings.getChatId() == chatId)
                .map(UserSettingDto::getNotificationTime)
                .map(time -> time.equals(NOTIFICATION_OPTION_OFF) ? time + EMPTY_STRING : time + MINUTES_OPTION)
                .collect(Collectors.joining());
    }


    public InlineKeyboardMarkup getHomeKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(createButton(EmojiParser.parseToUnicode(HOME_EMOJI_BUTTON), HOME_CALLBACK));
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    private List<InlineKeyboardButton> createButton(String command, String callBack) {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton button = InlineKeyboardButton.builder()
                .text(command)
                .callbackData(callBack)
                .build();
        rowInline.add(button);
        return rowInline;
    }

    private List<InlineKeyboardButton> createBackHomeButtons() {
        String markedOptionArrow = EmojiParser.parseToUnicode(BACK_EMOJI_BUTTON);
        String markedOptionHouse = EmojiParser.parseToUnicode(HOME_EMOJI_BUTTON);
        List<InlineKeyboardButton> backButton = createButton(markedOptionArrow, BACK_CALLBACK);
        List<InlineKeyboardButton> homeButton = createButton(markedOptionHouse, HOME_CALLBACK);
        backButton.addAll(homeButton);
        return backButton;
    }
}
