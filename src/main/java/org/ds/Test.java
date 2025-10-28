package org.ds;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Test {
    private static final Log log = LogFactory.getLog(Test.class);

    public static void main(String[] args) {
        log.info("В ответе должен быть чистый JSON-код (без дополнительных комментариев, заметок), чтобы сообщение было удобно парсить как JSON-код.".toUpperCase());
    }
}
