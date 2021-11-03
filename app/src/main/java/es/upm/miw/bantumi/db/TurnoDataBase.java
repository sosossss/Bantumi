package es.upm.miw.bantumi.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = TurnoDO.class, version = 2, exportSchema = true)
public abstract class TurnoDataBase extends RoomDatabase {

    public abstract TurnoDao getTurnoDao();

    private static final String DB_NAME = "TurnoDatabase.db";
    private static volatile TurnoDataBase instance;

    public static synchronized TurnoDataBase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static TurnoDataBase create(final Context context) {
        return Room.databaseBuilder(context, TurnoDataBase.class, DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }
}

