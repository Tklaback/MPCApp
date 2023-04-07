package com.example.mpcandroidapp.dao;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DatabaseMigration {

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE QRCode ADD COLUMN secondaryContents TEXT");
        }
    };

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Session ADD COLUMN name TEXT");
        }
    };

    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `Session_new` (`_id` TEXT PRIMARY KEY NOT NULL, `name` TEXT, `date` TEXT)");
            database.execSQL("INSERT INTO `Session_new` (`_id`, `name`, `date`) SELECT `_id`, `name`, `date` FROM `Session`");
            database.execSQL("DROP TABLE `Session`");
            database.execSQL("ALTER TABLE `Session_new` RENAME TO `Session`");
            database.execSQL("CREATE TABLE `QRCode_new` (`_id` TEXT PRIMARY KEY NOT NULL, " +
                    "`site` TEXT, `fs` TEXT, `contents` TEXT, `secondaryContents` TEXT," +
                    "`feature_nums` TEXT, `easting` TEXT, `northing` TEXT, `level` TEXT," +
                    "`depth` TEXT, `mbd` TEXT, `date` TEXT, `excavator` TEXT, `comments` TEXT," +
                    "`sessionID` TEXT, FOREIGN KEY(`sessionID`) REFERENCES " +
                    "`Session`(`_id`) ON DELETE CASCADE)");
            database.execSQL("INSERT INTO `QRCode_new` (`_id`, `site`, `fs`," +
                    " `contents`, `secondaryContents`, `feature_nums`," +
                    " `easting`, `northing`, `level`, `depth`, `mbd`," +
                    " `date`, `excavator`," +
                    " `comments`, `sessionID`) SELECT `_id`, `site`, `fs`," +
                    "`contents`, `secondaryContents`, `feature_nums`, `easting`, " +
                    "`northing`, `level`, `depth`, `mbd`,  `date`, `excavator`," +
                    " `comments`, `sessionID` FROM `QRCode`");
            database.execSQL("DROP TABLE `QRCode`");
            database.execSQL("ALTER TABLE `QRCode_new` RENAME TO `QRCode`");
        }
    };
}
