package org.ds.service.message;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ds.bot.inlineKeyboard.KeyboardButton;
import org.ds.utils.Utils;
import org.ds.utils.fileReader.FileReader;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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
    public void sendTextMessage(@NotNull Long chatId, @NotNull String message) {
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
    public void sendButtonsMessage(@NotNull Long chatId,
                                   @NotNull String message,
                                   KeyboardButton[] keyboardButtons) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton[] inlineKeyboardButtons = Arrays.stream(keyboardButtons).map(keyboardButton -> new InlineKeyboardButton(keyboardButton.text())
                .callbackData(keyboardButton.callback())).toArray(InlineKeyboardButton[]::new);
        keyboardMarkup.addRow(inlineKeyboardButtons);

        SendMessage sendMessage = new SendMessage(chatId, message)
                .parseMode(ParseMode.HTML)
                .replyMarkup(keyboardMarkup);

        telegramBot.execute(sendMessage);
    }

    /**
     * Sends message with image from resources
     * @param chatId chat Id
     * @param photoPath path to image from resources
     * @param message text message to image
     */
    public void sendPhotoMessage(@NotNull Long chatId,
                                 @NotNull String photoPath,
                                 @NotNull String message) {
        sendChatAction(chatId, ChatAction.upload_photo);

        SendPhoto sendPhoto = new SendPhoto(chatId, FileReader.getResourceFileBytes(photoPath));
        sendPhoto.setCaption(message);
        sendPhoto.setParseMode(ParseMode.HTML);

        telegramBot.execute(sendPhoto);
    }
}
