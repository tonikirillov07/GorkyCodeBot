package org.ds.service.message;

import com.pengrad.telegrambot.model.CallbackQuery;
import org.ds.bot.inlineKeyboard.KeyboardButton;
import org.ds.bot.inlineKeyboard.KeyboardButtonGroup;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class KeyboardButtonsCallbacksService {
    private final MessageSenderService messageSenderService;
    public List<KeyboardButton> keyboardButtons = new ArrayList<>();
    public List<KeyboardButtonGroup> keyboardButtonGroups = new ArrayList<>();

    public KeyboardButtonsCallbacksService(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    public void addKeyboardButtonGroup(KeyboardButtonGroup keyboardButtonGroup) {
        keyboardButtonGroups.add(keyboardButtonGroup);
    }

    public void addKeyboardButton(KeyboardButton keyboardButton) {
        if (hasSuchButton(keyboardButton))
            return;

        keyboardButtons.add(keyboardButton);
    }

    public void processCallbacks(CallbackQuery callbackQuery) {
        if (processButtons(callbackQuery))
            return;

        processGroups(callbackQuery);
    }

    private void processGroups(CallbackQuery callbackQuery) {
        for (KeyboardButtonGroup keyboardButtonGroup : keyboardButtonGroups) {
            Optional<KeyboardButton> keyboardButton = keyboardButtonGroup.findButtonByCallback(callbackQuery.data());
            if (keyboardButton.isEmpty())
                continue;

            keyboardButton.get().action().accept(messageSenderService);
            keyboardButtonGroups.remove(keyboardButtonGroup);

            break;
        }
    }

    private boolean processButtons(CallbackQuery callbackQuery) {
        for (KeyboardButton keyboardButton : keyboardButtons) {
            if (keyboardButton.callback().equals(callbackQuery.data())) {
                keyboardButton.action().accept(messageSenderService);
                keyboardButtons.removeIf(button -> button.callback().equals(callbackQuery.data()));

                return true;
            }
        }

        return false;
    }

    private boolean hasSuchButton(KeyboardButton keyboardButton) {
        for (KeyboardButton button : keyboardButtons) {
            if (button.callback().equals(keyboardButton.callback()))
                return true;
        }

        return false;
    }
}
