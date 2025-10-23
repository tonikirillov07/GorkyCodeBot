package org.ds.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ds.bot.states.States;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class BotStateService {
    private static final Log log = LogFactory.getLog(BotStateService.class);
    private States currentState;

    public BotStateService() {
        this.currentState = States.NONE;
    }

    public void changeCurrentState(@NotNull States currentState) {
        this.currentState = currentState;

        log.info("Set bot state to %s".formatted(currentState.name()));
    }

    public States getCurrentState() {
        return currentState;
    }
}
