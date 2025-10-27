package org.ds;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ds.service.message.MessageSenderService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    private static final Log log = LogFactory.getLog(Main.class);

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("org.ds");
        context.start();

        log.info("Bot started");
    }
}