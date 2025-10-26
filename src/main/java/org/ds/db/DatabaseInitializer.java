package org.ds.db;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class DatabaseInitializer {
    private static final String DATABASE_NAME = "users.db";
    private static final String DATABASE_RESOURCE_PATH = "db/" + DATABASE_NAME;

    public static @NotNull String getDbUrl() {
        return "jdbc:sqlite:" + new File(DATABASE_RESOURCE_PATH).getAbsolutePath();
    }
}
