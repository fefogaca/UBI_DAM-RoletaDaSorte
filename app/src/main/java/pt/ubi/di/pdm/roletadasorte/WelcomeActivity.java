package pt.ubi.di.pdm.roletadasorte;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Felipe Fogaça
 * Atividade que exibe uma mensagem de boas-vindas personalizada com o nome do usuário
 * A partir daqui, o usuário pode prosseguir para a roleta ou optar por alterar o nome salvo.
 */
public class WelcomeActivity extends AppCompatActivity {

    // SharedPreferences
    private static final String PREFS_NAME = "RoletaPrefs";
    private static final String NAME_KEY = "UserName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        TextView textWelcome = findViewById(R.id.textWelcome);
        Button btnPlay = findViewById(R.id.btnPlay);
        Button btnChangeName = findViewById(R.id.btnChangeName);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String userName = prefs.getString(NAME_KEY, "Usuário"); // Usuário como fallback

        textWelcome.setText(getString(R.string.hello_user, userName));

        btnPlay.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnChangeName.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(NAME_KEY);
            editor.apply();

            Intent intent = new Intent(WelcomeActivity.this, NameActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
