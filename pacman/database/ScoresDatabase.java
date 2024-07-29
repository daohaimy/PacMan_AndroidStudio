package com.example.pacman.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Scores.class},version = 1, exportSchema = false)
public abstract class ScoresDatabase extends RoomDatabase {

    public abstract ScoresDAO scoreDAO();
    public static volatile ScoresDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static synchronized ScoresDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (ScoresDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ScoresDatabase.class, "score_database")
                            //.fallbackToDestructiveMigration()
                            //Test
                            //.addCallback(sRoomDatabaseCallback)
                            //Test ende
                            .build();
                }
            }

        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                ScoresDAO dao = INSTANCE.scoreDAO();
                dao.deleteAll();

                Scores scores = new Scores(187, "My");
                dao.insert(scores);
                scores = new Scores(69, "Kevin");
                dao.insert(scores);
            });
        }
    };
}