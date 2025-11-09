package pt.ubi.di.pdm.roletadasorte;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

/**
 * @author Felipe Fogaça
 * Atividade responsável por solicitar e salvar o nome do usuário.
 * Esta tela é exibida na primeira vez que o usuário abre o aplicativo.
 */
public class NameActivity extends AppCompatActivity {

    // User Inteface
    private TextInputEditText inputName;
    private Button btnSaveName;

    // SharedPreferences
    private static final String PREFS_NAME = "RoletaPrefs";
    private static final String NAME_KEY = "UserName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        inputName = findViewById(R.id.inputName);
        btnSaveName = findViewById(R.id.btnSaveName);

        btnSaveName.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Por favor, digite seu nome!", Toast.LENGTH_SHORT).show();
            } else {
                saveNameAndProceed(name);
            }
        });
    }

    /**
     * Salva o nome nas SharedPreferences e inicia a WelcomeActivity
     */
    private void saveNameAndProceed(String name) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(NAME_KEY, name);
        editor.apply();

        Intent intent = new Intent(NameActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}
