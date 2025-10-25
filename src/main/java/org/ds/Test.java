package org.ds;

import java.time.Duration;
import java.time.LocalDateTime;

public class Test {
    public static void main(String[] args) {
        LocalDateTime localDateTime1 = LocalDateTime.now();
        LocalDateTime localDateTime2 = LocalDateTime.now().plusHours(65);

        Duration duration = Duration.between(localDateTime1, localDateTime2);
        long hoursDifference = duration.toHours();

        System.out.println(hoursDifference);
    }
}
