package com.example.myapplication.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.myapplication.dao.RatingDao;
import com.example.myapplication.dao.ReservationDao;
import com.example.myapplication.dao.ServicePDao;
import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.Converters;
import com.example.myapplication.entity.Rating;
import com.example.myapplication.entity.Reservation;
import com.example.myapplication.entity.ServicePres;
import com.example.myapplication.entity.User;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class, Rating.class, ServicePres.class, Reservation.class}, version = 7, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract RatingDao ratingDao();
    public abstract ServicePDao servicePDao();
    public abstract ReservationDao reservationDao();

    // Migration from version 1 to version 2 to add `imageUri`
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE user ADD COLUMN imageUri TEXT");
        }
    };

    // Migration from version 2 to version 3 for modifying the `rating` table
    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE rating RENAME TO rating_temp");
            database.execSQL("CREATE TABLE rating (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "userId INTEGER NOT NULL, " +
                    "rating INTEGER NOT NULL, " +
                    "comment TEXT)");
            database.execSQL("INSERT INTO rating (id, userId, rating, comment) " +
                    "SELECT id, serviceId AS userId, rating, comment FROM rating_temp");
            database.execSQL("DROP TABLE rating_temp");
        }
    };

    // Migration from version 3 to version 4 to add `username`, `password`, `ratedUserId`, and `raterUserId`
    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE user ADD COLUMN username TEXT");
            database.execSQL("ALTER TABLE user ADD COLUMN password TEXT");

            // Rename old `rating` table and create a new one with `ratedUserId` and `raterUserId`
            database.execSQL("ALTER TABLE rating RENAME TO rating_temp");
            database.execSQL("CREATE TABLE rating (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "ratedUserId INTEGER NOT NULL, " +
                    "raterUserId INTEGER NOT NULL, " +
                    "rating INTEGER NOT NULL, " +
                    "comment TEXT)");

            // Copy data to new schema with default values for `ratedUserId` and `raterUserId`
            database.execSQL("INSERT INTO rating (id, ratedUserId, raterUserId, rating, comment) " +
                    "SELECT id, userId AS ratedUserId, -1 AS raterUserId, rating, comment FROM rating_temp");

            // Drop the temporary table
            database.execSQL("DROP TABLE rating_temp");
        }
    };

    // Migration from version 4 to version 5 as a schema verification step
    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // No schema changes, version increment only
        }
    };

    // Migration from version 5 to version 6 to add `Reservation` table
    // Migration from version 5 to version 6 to add `reservations` table
    private static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS reservations (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "userId INTEGER NOT NULL, " +
                    "serviceId INTEGER NOT NULL, " +
                    "reservationDate INTEGER, " + // Updated reservationDate to allow nulls if needed
                    "FOREIGN KEY(userId) REFERENCES user(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY(serviceId) REFERENCES servicepr(id) ON DELETE CASCADE)");
        }
    };

    private static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE reservations ADD COLUMN status TEXT DEFAULT 'non valide'");
        }
    };
    public static AppDatabase getAppDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "room_test_db")
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
