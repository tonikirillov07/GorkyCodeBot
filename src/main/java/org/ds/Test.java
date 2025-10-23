package org.ds;

import org.ds.bot.preparingSteps.responses.freeTime.FreeTimeResponse;

public class Test {
    public static void main(String[] args) {
        FreeTimeResponse freeTimeResponse = new FreeTimeResponse(true, null, "");

        System.out.println(freeTimeResponse.getClass());
    }
}
