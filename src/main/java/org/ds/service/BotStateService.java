package org.ds.service;

import org.ds.bot.states.States;
import org.springframework.stereotype.Service;

@Service
public class BotStateService {
    private States currentState = States.NONE;

    public void changeCurrentState(States currentState) {
        this.currentState = currentState;
    }

    public States getCurrentState() {
        return currentState;
    }
}
