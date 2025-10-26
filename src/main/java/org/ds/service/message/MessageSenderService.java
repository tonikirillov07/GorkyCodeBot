package org.ds.service.message;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.SendResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ds.bot.inlineKeyboard.KeyboardButton;
import org.ds.utils.Utils;
import org.ds.utils.buttonsMessage.ButtonsMessageUtils;
import org.ds.utils.fileReader.FileReader;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MessageSenderService {
    private static final Log log = LogFactory.getLog(MessageSenderService.class);
    private final TelegramBot telegramBot;

    public MessageSenderService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    /**
     * Sends text message into chat
     * @param chatId current chat id
     * @param message message to send
     */
    public SendResponse sendTextMessage(@NotNull Long chatId, @NotNull String message) {
        sendChatAction(chatId, ChatAction.typing);

        SendMessage request = new SendMessage(chatId, message)
                .parseMode(ParseMode.HTML);

        SendResponse sendResponse = telegramBot.execute(request);
        Message responseMessage = sendResponse.message();

        if (sendResponse.isOk())
            log.info("Message sent successfully to chat %s from user %s. Message: %s"
                    .formatted(responseMessage.chat().username(),
                            Utils.getUsername(responseMessage.from()),
                            responseMessage.text()));
        else
            log.error("Message sending failed. Error Code: %d, Message: %s"
                    .formatted(sendResponse.errorCode(), sendResponse.description()));

        return sendResponse;
    }

    /**
     * Sends chat action like typing, sending photo, ets.
     * @param chatId current chat id
     * @param chatAction action to send
     */
    public void sendChatAction(@NotNull Long chatId, @NotNull ChatAction chatAction) {
        SendChatAction sendChatAction = new SendChatAction(chatId, chatAction);
        telegramBot.execute(sendChatAction);

        log.info("Sent chat action %s into chat %d".formatted(chatAction.name().toUpperCase(), chatId));
    }

    /**
     * Sends message with buttons
     * @param chatId chat id
     * @param message message text
     * @param keyboardButtons buttons list
     */
    public SendResponse sendButtonsMessage(@NotNull Long chatId,
                                           @NotNull String message,
                                           KeyboardButton @NotNull [] keyboardButtons) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInKeyboard = new ArrayList<>();

        int buttonsPerRow = ButtonsMessageUtils.calculateOptimalButtonsPerRow(keyboardButtons.length);
        List<InlineKeyboardButton> currentRow = new ArrayList<>();

        for (KeyboardButton button : keyboardButtons) {
            currentRow.add(new InlineKeyboardButton(button.text()).callbackData(button.callback()));

            if (currentRow.size() >= buttonsPerRow || button.equals(keyboardButtons[keyboardButtons.length - 1])) {
                rowsInKeyboard.add(currentRow);
                currentRow = new ArrayList<>();
            }
        }

        rowsInKeyboard.forEach(row -> {
            InlineKeyboardButton[] rowArray = new InlineKeyboardButton[row.size()];
            for (int i = 0; i < row.size(); i++) {
                rowArray[i] = row.get(i);
            }

            keyboardMarkup.addRow(rowArray);
        });

        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.replyMarkup(keyboardMarkup);
        sendMessage.setParseMode(ParseMode.HTML);

        return telegramBot.execute(sendMessage);
    }

    /**
     * Sends message with image from resources
     * @param chatId chat Id
     * @param photoPath path to image from resources
     * @param message text message to image
     */
    public SendResponse sendPhotoMessage(@NotNull Long chatId,
                                 @NotNull String photoPath,
                                 @NotNull String message) {
        sendChatAction(chatId, ChatAction.upload_photo);

        SendPhoto sendPhoto = new SendPhoto(chatId, FileReader.getResourceFileBytes(photoPath));
        sendPhoto.setCaption(message);
        sendPhoto.setParseMode(ParseMode.HTML);

        return telegramBot.execute(sendPhoto);
    }

    /**
     * Sends location to user chat
     * @param chatId chat id
     * @param latitude location latitude
     * @param longitude location longitude
     * @return SendResponse
     */
    public SendResponse sendLocation(Long chatId, float latitude, float longitude) {
        SendLocation sendLocation = new SendLocation(chatId, latitude, longitude);

        return telegramBot.execute(sendLocation);
    }
}
