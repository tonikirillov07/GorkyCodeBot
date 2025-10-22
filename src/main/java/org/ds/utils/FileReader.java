package org.ds.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ds.Main;
import org.ds.exceptions.FileReadException;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class FileReader {
    public static @NotNull String read(@NotNull String filePath) {
        try (InputStream inputStream = Main.class.getResourceAsStream(filePath)) {
            if (inputStream == null)
                throw new NullPointerException("InputStream of file %s is null".formatted(filePath));

            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            return stringBuilder.toString();
        } catch (Exception e) {
            throw new FileReadException(filePath, e);
        }
    }
}
