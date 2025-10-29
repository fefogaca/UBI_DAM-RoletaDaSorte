package pt.ubi.di.pdm.roletadasorte;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "RoletaPrefs";
    private static final String NAME_KEY = "UserName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(() -> {
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
        }, 1000); // 1 segundo de delay
    }
}
