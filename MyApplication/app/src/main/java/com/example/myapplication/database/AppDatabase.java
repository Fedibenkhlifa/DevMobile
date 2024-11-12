package com.example.myapplication.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.dao.CommentDao;
import com.example.myapplication.dao.RatingDao;
import com.example.myapplication.dao.ReservationDao;
import com.example.myapplication.dao.ServicePDao;
import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.Comment;
import com.example.myapplication.entity.Converters;
import com.example.myapplication.entity.Rating;
import com.example.myapplication.entity.Reservation;
import com.example.myapplication.entity.ServicePres;
import com.example.myapplication.entity.User;

@Database(entities = {User.class, Rating.class, ServicePres.class, Reservation.class, Comment.class}, version = 8, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract RatingDao ratingDao();
    public abstract ServicePDao servicePDao();
    public abstract ReservationDao reservationDao();
    public abstract CommentDao commentDao();

    // Migration de la version 7 à la version 8 : séparation de `Rating` et ajout de `Comment`
    private static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Création de la table `comment`
            database.execSQL("CREATE TABLE IF NOT EXISTS comment (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "ratedUserId INTEGER NOT NULL, " +
                    "raterUserId INTEGER NOT NULL, " +
                    "comment TEXT, " +
                    "FOREIGN KEY(ratedUserId) REFERENCES user(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY(raterUserId) REFERENCES user(id) ON DELETE CASCADE)");

            // Modification de la table `rating` pour supprimer la colonne `comment`
            database.execSQL("ALTER TABLE rating RENAME TO rating_temp");
            database.execSQL("CREATE TABLE rating (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "ratedUserId INTEGER NOT NULL, " +
                    "raterUserId INTEGER NOT NULL, " +
                    "rating INTEGER NOT NULL)");
            database.execSQL("INSERT INTO rating (id, ratedUserId, raterUserId, rating) " +
                    "SELECT id, ratedUserId, raterUserId, rating FROM rating_temp");
            database.execSQL("DROP TABLE rating_temp");
        }
    };

    public static AppDatabase getAppDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "room_test_db")
                    .addMigrations( MIGRATION_7_8)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
