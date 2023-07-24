package currencyratebot.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import currencyratebot.dto.settings.UserSettingDto;
import currencyratebot.service.CommandResolverService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CurrencyRateBotController extends TelegramLongPollingBot {
    private static final String JSON_FILE = "src/main/resources/settings.json";
    private static final String USERNAME = "username";
    private static final String TOKEN = "token";
    private static final String SETTINGS_CALLDATA_MARKER = "set_";
    public static List<UserSettingDto> userSettingDtoList = settingsFromJson();
    private static final CommandResolverService commandResolverService = new CommandResolverService();
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("telegramBot");

    @Override
    public String getBotUsername() {
        return resourceBundle.getString(USERNAME);
    }

    @Override
    public String getBotToken() {
        return resourceBundle.getString(TOKEN);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            UserSettingDto userSettingDto = new UserSettingDto(chatId);
            if (userSettingDtoList.stream().noneMatch(item -> item.getChatId() == chatId)) {
                userSettingDtoList.add(userSettingDto);
            }
            settingsToJson();
            SendMessage message = commandResolverService.resolveMainMenuAndNotificationCommands(update.getMessage().getText(), chatId);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            String callData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callData.contains(SETTINGS_CALLDATA_MARKER)) {
                EditMessageText newMessage = commandResolverService.resolveSettingsMenuCommands(callData, chatId, messageId);
                try {
                    execute(newMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                SendMessage newMessage = commandResolverService.resolveMainMenuAndNotificationCommands(callData, chatId);
                try {
                    execute(newMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static synchronized void settingsToJson() {
        try (Writer fileWriter = new FileWriter(JSON_FILE)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(userSettingDtoList, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<UserSettingDto> settingsFromJson() {
        List<UserSettingDto> tempList = new ArrayList<>();
        try (Reader fileReader = new FileReader(JSON_FILE)) {
            Type type = TypeToken.getParameterized(List.class, UserSettingDto.class).getType();
            tempList = new Gson().fromJson(fileReader, type);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (tempList == null) {
            tempList = new ArrayList<>();
        }
        return tempList;
    }
}