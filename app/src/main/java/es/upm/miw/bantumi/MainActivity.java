package es.upm.miw.bantumi;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.upm.miw.bantumi.activity.LeaderboardActivity;
import es.upm.miw.bantumi.activity.SettingActivity;
import es.upm.miw.bantumi.db.TurnoDao;
import es.upm.miw.bantumi.db.TurnoDO;
import es.upm.miw.bantumi.entity.SettingVO;
import es.upm.miw.bantumi.entity.TurnoVO;
import es.upm.miw.bantumi.model.BantumiViewModel;
import es.upm.miw.bantumi.db.TurnoDataBase;

public class MainActivity extends AppCompatActivity {

    protected final String LOG_TAG = "MiW";
    JuegoBantumi juegoBantumi;
    BantumiViewModel bantumiVM;
    int numInicialSemillas;

    TurnoDataBase turnoDataBase;
    TurnoDao turnoDao;
    SettingVO settingVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting
        settingVO = SettingActivity.getSetting(this);

        // Instancia el ViewModel y el juego, y asigna observadores a los huecos
        numInicialSemillas = getResources().getInteger(R.integer.intNumInicialSemillas);
        bantumiVM = new ViewModelProvider(this).get(BantumiViewModel.class);
        juegoBantumi = new JuegoBantumi(bantumiVM, JuegoBantumi.Turno.turnoJ1, numInicialSemillas);
        crearObservadores();

        //Creta Database
        turnoDataBase = TurnoDataBase.getInstance(this);
        turnoDao = turnoDataBase.getTurnoDao();

        Button reset = findViewById(R.id.button);
        reset.setOnClickListener(v -> reset());
    }

    boolean haveInitialled = false;
    boolean hasChanged = false;

    /**
     * Crea y subscribe los observadores asignados a las posiciones del tablero.
     * Si se modifica el contenido del tablero, actualiza la vista.
     */
    private void crearObservadores() {
        for (int i = 0; i < JuegoBantumi.NUM_POSICIONES; i++) {
            int finalI = i;
            bantumiVM.getNumSemillas(i).observe(    // Huecos y almacenes
                    this,
                    new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            mostrarValor(finalI, juegoBantumi.getSemillas(finalI));
                            if (haveInitialled) hasChanged = true;
                        }
                    });
        }
        bantumiVM.getTurno().observe(   // Turno
                this,
                new Observer<JuegoBantumi.Turno>() {
                    @Override
                    public void onChanged(JuegoBantumi.Turno turno) {
                        marcarTurno(juegoBantumi.turnoActual());
                        if (!haveInitialled) haveInitialled = true;
                    }
                }
        );
    }

    /**
     * Indica el turno actual cambiando el color del texto
     *
     * @param turnoActual turno actual
     */
    private void marcarTurno(@NonNull JuegoBantumi.Turno turnoActual) {
        TextView tvJugador1 = findViewById(R.id.tvPlayer1);
        if (settingVO.playerOneName != null && !settingVO.playerOneName.isEmpty()) tvJugador1.setText(settingVO.playerOneName);

        TextView tvJugador2 = findViewById(R.id.tvPlayer2);
        if (settingVO.playerTwoName != null && !settingVO.playerTwoName.isEmpty()) tvJugador2.setText(settingVO.playerTwoName);

        switch (turnoActual) {
            case turnoJ1:
                tvJugador1.setTextColor(getColor(R.color.design_default_color_primary));
                tvJugador2.setTextColor(getColor(R.color.black));
                break;
            case turnoJ2:
                tvJugador1.setTextColor(getColor(R.color.black));
                tvJugador2.setTextColor(getColor(R.color.design_default_color_primary));
                break;
            default:
                tvJugador1.setTextColor(getColor(R.color.black));
                tvJugador2.setTextColor(getColor(R.color.black));
        }
    }

    /**
     * Muestra el valor <i>valor</i> en la posición <i>pos</i>
     *
     * @param pos   posición a actualizar
     * @param valor valor a mostrar
     */
    private void mostrarValor(int pos, int valor) {
        String num2digitos = String.format(Locale.getDefault(), "%02d", pos);
        // Los identificadores de los huecos tienen el formato casilla_XX
        int idBoton = getResources().getIdentifier("casilla_" + num2digitos, "id", getPackageName());
        if (0 != idBoton) {
            TextView viewHueco = findViewById(idBoton);
            viewHueco.setText(String.valueOf(valor));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // @TODO!!! resto opciones
            case R.id.opcReiniciarPartida:
                reset();
                return true;

            case R.id.opcMejoresResultados:
                Intent leaderboardIntent = new Intent(MainActivity.this, LeaderboardActivity.class);
                startActivity(leaderboardIntent);
                return true;

            case R.id.opcAjustes: // @todo Preferencias
                Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(settingIntent);
                return true;

            case R.id.opcAcercaDe:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.aboutTitle)
                        .setMessage(R.string.aboutMessage)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                return true;

            case R.id.opcGuardarPartida:
                List<TurnoVO> turno = new ArrayList<>();
                for (int i = 0; i < JuegoBantumi.NUM_POSICIONES; i++) {
                    TurnoVO data = new TurnoVO();
                    data.index = i;
                    data.data = juegoBantumi.getSemillas(i);
                    turno.add(data);
                    Log.d("test", "turno: " + i + " - " + juegoBantumi.getSemillas(i));
                }

                Gson gson = new Gson();
                String jsonSave = gson.toJson(turno);

                try {
                    FileOutputStream outStream = openFileOutput("Turno.json", MODE_PRIVATE);
                    outStream.write(jsonSave.getBytes(StandardCharsets.UTF_8));
                    outStream.close();
                    Toast.makeText(MainActivity.this, "Guardar Partida Completa", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                return true;

            case R.id.opcRecuperarPartida:
                AppAlertDialog.SuccessCallback callback = () -> {
                    try {
                        FileInputStream in = openFileInput("Turno.json");
                        InputStreamReader inputStreamReader = new InputStreamReader(in);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            sb.append(line);
                        }
                        inputStreamReader.close();

                        String jsonLoad = sb.toString();
                        Gson gsonLoad = new Gson();
                        TurnoVO[] turnoLoad = gsonLoad.fromJson(jsonLoad, TurnoVO[].class);
                        for (TurnoVO i : turnoLoad) {
                            juegoBantumi.setSemillas(i.index, i.data);
                        }
                        Toast.makeText(MainActivity.this, "Recuperar Partida Completa", Toast.LENGTH_LONG).show();
                        hasChanged = false;
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                };
                if (hasChanged) {
                    // @TODO guardar puntuación
                    new AppAlertDialog(R.string.txtDialogoResetTitulo, R.string.txtDialogoResetPregunta, callback).show(getSupportFragmentManager(), "ALERT_DIALOG");
                } else {
                    callback.onSuccess();
                }
                return true;

            default:
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.txtSinImplementar),
                        Snackbar.LENGTH_LONG
                ).show();
        }
        return true;
    }

    /**
     * Acción que se ejecuta al pulsar sobre un hueco
     *
     * @param v Vista pulsada (hueco)
     */
    public void huecoPulsado(@NonNull View v) {
        String resourceName = getResources().getResourceEntryName(v.getId()); // pXY
        int num = Integer.parseInt(resourceName.substring(resourceName.length() - 2));
        Log.i(LOG_TAG, "huecoPulsado(" + resourceName + ") num=" + num);
        switch (juegoBantumi.turnoActual()) {
            case turnoJ1:
                juegoBantumi.jugar(num);
                break;
            case turnoJ2:
                juegaComputador();
                break;
            default:    // JUEGO TERMINADO
                finJuego();
        }
        if (juegoBantumi.juegoTerminado()) {
            finJuego();
        }
    }

    /**
     * Elige una posición aleatoria del campo del jugador2 y realiza la siembra
     * Si mantiene turno -> vuelve a jugar
     */
    void juegaComputador() {
        while (juegoBantumi.turnoActual() == JuegoBantumi.Turno.turnoJ2) {
            int pos = 7 + (int) (Math.random() * 6);    // posición aleatoria
            Log.i(LOG_TAG, "juegaComputador(), pos=" + pos);
            if (juegoBantumi.getSemillas(pos) != 0 && (pos < 13)) {
                juegoBantumi.jugar(pos);
            } else {
                Log.i(LOG_TAG, "\t posición vacía");
            }
        }
    }

    /**
     * El juego ha terminado. Volver a jugar?
     */
    private void finJuego() {
        TextView tvJugador1 = findViewById(R.id.tvPlayer1);
        TextView tvJugador2 = findViewById(R.id.tvPlayer2);
        String texto = (juegoBantumi.getSemillas(6) > 6 * numInicialSemillas)
                ? "Gana " + tvJugador1.getText().toString()
                : "Gana " + tvJugador2.getText().toString();
        Snackbar.make(
                findViewById(android.R.id.content),
                texto,
                Snackbar.LENGTH_LONG
        )
                .show();

        // @TODO guardar puntuación
        new FinalAlertDialog(() -> {
            //save
            TurnoDO turnoDO = new TurnoDO(tvJugador1.getText().toString(), juegoBantumi.getSemillas(6), tvJugador2.getText().toString(), juegoBantumi.getSemillas(13));
            turnoDao.insert(turnoDO);
        }).show(getSupportFragmentManager(), "ALERT_DIALOG");

    }

    private void reset() {
        AppAlertDialog.SuccessCallback callback = () -> {
            hasChanged = false;
            haveInitialled = false;
            bantumiVM.clear();
            juegoBantumi.clear(JuegoBantumi.Turno.turnoJ1);
            crearObservadores();
        };
        if (hasChanged) {
            new AppAlertDialog(R.string.txtDialogoReinitialTitulo, R.string.txtDialogoReinitialPregunta, callback).show(getSupportFragmentManager(), "ALERT_DIALOG");
        } else {
            callback.onSuccess();
        }
    }


//    private void save(){
//        TurnoDO turnoDO=new TurnoDO();
//        turnoDO.setPlayerOneName("test1");
//        turnoDO.setPlayerTwoName("test2");
//        turnoDO.setPlayerOneNumber(juegoBantumi.getSemillas(6));
//        turnoDO.setPlayerTwoNumber(juegoBantumi.getSemillas(13));
//        TurnoDataBase
//                .getInstance(this)
//                .getTurnoDao()
//                .insert(turnoDO);
//    }

}