package org.ds.bot.commands.botCommands;

import org.ds.bot.BotInfo;
import org.ds.bot.commands.AbstractCommand;
import org.ds.bot.commands.CommandData;
import org.ds.bot.states.States;
import org.ds.db.UserRegistrationResponse;
import org.ds.db.entity.UserEntity;
import org.ds.service.BotStateService;
import org.ds.service.db.DBService;
import org.ds.service.message.KeyboardButtonsCallbacksService;
import org.ds.service.message.MessageSenderService;
import org.ds.utils.Utils;
import org.ds.utils.fileReader.FileReader;
import org.ds.utils.fileReader.files.PhotoFiles;
import org.ds.utils.fileReader.files.TextFiles;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class StartCommand extends AbstractCommand {
    private final DBService dBService;
    private final BotInfo botInfo;

    protected StartCommand(MessageSenderService messageSenderService, KeyboardButtonsCallbacksService keyboardButtonsCallbacksService, BotStateService botStateService, DBService dBService, BotInfo botInfo) {
        super(messageSenderService, keyboardButtonsCallbacksService, botStateService);
        this.dBService = dBService;
        this.botInfo = botInfo;
    }

    @Override
    public void execute(@NotNull CommandData commandData) {
        UserRegistrationResponse userRegistrationResponse = checkUserRegistration(commandData.userId());

        if (!userRegistrationResponse.usingFirstTime() && userRegistrationResponse.isGotResult())
            messageSenderService().sendTextMessage(commandData.chatId(), FileReader.read(TextFiles.WELCOME_2_TEXT));
        else if (!userRegistrationResponse.usingFirstTime() && !userRegistrationResponse.isGotResult())
            messageSenderService().sendTextMessage(commandData.chatId(), FileReader.read(TextFiles.WELCOME_3_TEXT));
        else
            messageSenderService().sendPhotoMessage(commandData.chatId(), PhotoFiles.MAIN_PHOTO,
                    FileReader.read(TextFiles.WELCOME_TEXT).formatted(commandData.username()));

        botStateService().changeCurrentState(States.REQUIRES_INTERESTS);
    }

    private @NotNull UserRegistrationResponse checkUserRegistration(Long userId) {
        boolean usingFirstTime, gotResult;

        if (dBService.existsUserByUserId(userId)) {
            UserEntity user = dBService.getUserByUserId(userId);
            if (user == null)
                throw new NullPointerException("User with id %d is null".formatted(userId));

            usingFirstTime = user.getUsingFirstTime() ||
                    Utils.getDifferenceBetweenDatesInHours(user.getLastUsingTime(), LocalDateTime.now()) >= botInfo.sayHelloInterval();

            gotResult = user.getGotResult();

            UserEntity userToUpdate = UserEntity.of(user);
            userToUpdate.setLastUsingTime(LocalDateTime.now());

            dBService.updateUser(userToUpdate);
        } else {
            usingFirstTime = true;
            gotResult = false;

            dBService.addUser(UserEntity.createNewUser(userId));
        }

        return UserRegistrationResponse.of(usingFirstTime, gotResult);
    }
}
