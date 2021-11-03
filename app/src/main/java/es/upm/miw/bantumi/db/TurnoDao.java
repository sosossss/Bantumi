package es.upm.miw.bantumi.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TurnoDao {
    @Query("SELECT * FROM Turno ORDER BY winnerNumber DESC LIMIT 0, 10")
    List<TurnoDO> getAll();

    @Insert
    void insert(TurnoDO... turnoDO);

    @Delete
    void delete(TurnoDO... turnoDo);

    @Query("DELETE FROM Turno")
    void deleteAll();
}
