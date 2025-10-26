package org.ds.utils.buttonsMessage;

public final class ButtonsMessageUtils {
    public static int calculateOptimalButtonsPerRow(int totalButtonsCount) {
        return switch (totalButtonsCount) {
            case 1 -> 1;
            case 2 -> 2;
            default -> Math.min(totalButtonsCount / 2, 3);
        };
    }
}
