package pt.ubi.di.pdm.roletadasorte;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Felipe Fogaça
 * A tela de Splash, que serve como ponto de entrada do aplicativo.
 * Ela exibe uma tela de carregamento por um breve período e, em seguida, decide
 * para qual atividade o usuário deve ser direcionado com base na existência de um nome de usuário salvo
 */
public class SplashActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "RoletaPrefs";
    private static final String NAME_KEY = "UserName";
    private static final int SPLASH_DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            String userName = prefs.getString(NAME_KEY, null);

            Intent intent;
            if (userName != null) {
                intent = new Intent(SplashActivity.this, WelcomeActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, NameActivity.class);
            }
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }
}
