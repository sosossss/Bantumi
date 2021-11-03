package es.upm.miw.bantumi.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.component.ComparedComponent;
import es.upm.miw.bantumi.db.TurnoDO;
import es.upm.miw.bantumi.db.TurnoDao;
import es.upm.miw.bantumi.db.TurnoDataBase;

public class LeaderboardActivity extends AppCompatActivity {

    TurnoDao turnoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        turnoDao = TurnoDataBase.getInstance(this).getTurnoDao();
        List<TurnoDO> turnolist = turnoDao.getAll();
        load(turnolist);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.setting_opcions_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcBorrarResultados:
                turnoDao = TurnoDataBase.getInstance(this).getTurnoDao();
                turnoDao.deleteAll();
                load(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void load(@Nullable List<TurnoDO> turnolist) {
        LinearLayout list = findViewById(R.id.list);
        list.removeAllViews();
        if (turnolist != null && turnolist.size() > 0) {
            for (int i = 0; i < turnolist.size(); i++) {
                ComparedComponent item = new ComparedComponent(this);
                list.addView(item);
                item.setPlayer1(turnolist.get(i).getPlayerOneName());
                item.setData1(String.valueOf(turnolist.get(i).getPlayerOneNumber()));
                item.setPlayer2(turnolist.get(i).getPlayerTwoName());
                item.setData2(String.valueOf(turnolist.get(i).getPlayerTwoNumber()));
                item.setWinnerName(turnolist.get(i).getWinnerName());
                item.setWinnerData(String.valueOf(turnolist.get(i).getWinnerNumber()));
                item.setDate(turnolist.get(i).getPlayTime());
            }
        } else {
            TextView text = new TextView(this);
            text.setText("No Hay Turnos");
            text.setTextSize(22);
            text.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            text.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = 80;
            text.setLayoutParams(params);
            list.addView(text);
        }
    }
}

