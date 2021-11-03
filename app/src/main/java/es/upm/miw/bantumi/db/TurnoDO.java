package es.upm.miw.bantumi.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Calendar;
import java.util.Date;

import es.upm.miw.bantumi.util.DateConverter;


@Entity(tableName = "Turno")
public class TurnoDO {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "playerOneName")
    public String playerOneName;

    @ColumnInfo(name = "playerOneNumber")
    public int playerOneNumber;

    @ColumnInfo(name = "playerTwoName")
    public String playerTwoName;

    @ColumnInfo(name = "playerTwoNumber")
    public int playerTwoNumber;

    @ColumnInfo(name = "winnerName")
    public String winnerName;

    @ColumnInfo(name = "winnerNumber")
    public int winnerNumber;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "playTime")
    public Date playTime;

    public TurnoDO(String playerOneName, int playerOneNumber, String playerTwoName, int playerTwoNumber) {
        this.playerOneName = playerOneName;
        this.playerOneNumber = playerOneNumber;

        this.playerTwoName = playerTwoName;
        this.playerTwoNumber = playerTwoNumber;

        this.winnerName = playerOneNumber > playerTwoNumber ? playerOneName : playerTwoName;
        this.winnerNumber = Math.max(playerOneNumber, playerTwoNumber);

        this.playTime = Calendar.getInstance().getTime();
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }

    public void setPlayerOneName(String playerOneName) {
        this.playerOneName = playerOneName;
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public void setPlayerTwoName(String playerTwoName) {
        this.playerTwoName = playerTwoName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }

    public void setPlayerOneNumber(int playerOneNumber) {
        this.playerOneNumber = playerOneNumber;
    }

    public int getPlayerOneNumber() {
        return playerOneNumber;
    }

    public void setPlayerTwoNumber(int playerTwoNumber) {
        this.playerTwoNumber = playerTwoNumber;
    }

    public int getPlayerTwoNumber() {
        return playerTwoNumber;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public int getWinnerNumber() {
        return winnerNumber;
    }

    public void setWinnerNumber(int winnerNumber) {
        this.winnerNumber = winnerNumber;
    }

    public Date getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Date playTime) {
        this.playTime = playTime;
    }
}
