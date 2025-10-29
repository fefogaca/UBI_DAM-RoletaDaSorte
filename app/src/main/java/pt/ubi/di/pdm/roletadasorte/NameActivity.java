package pt.ubi.di.pdm.roletadasorte;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

// Esta atividade é responsável por solicitar e salvar o nome do usuário.
public class NameActivity extends AppCompatActivity {

    // Componentes da interface do usuário
    private TextInputEditText inputName; // Campo para o usuário digitar o nome
    private Button btnSaveName; // Botão para salvar o nome

    // Constantes para o nome do arquivo de SharedPreferences e a chave do nome do usuário
    private static final String PREFS_NAME = "RoletaPrefs";
    private static final String NAME_KEY = "UserName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name); // Define o layout da atividade

        // Inicializa os componentes da UI
        inputName = findViewById(R.id.inputName);
        btnSaveName = findViewById(R.id.btnSaveName);

        // Configura o listener de clique para o botão "Salvar"
        btnSaveName.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim(); // Obtém o texto do campo e remove espaços em branco
            if (name.isEmpty()) {
                // Exibe uma mensagem se o campo estiver vazio
                Toast.makeText(this, "Por favor, digite seu nome!", Toast.LENGTH_SHORT).show();
            } else {
                // Salva o nome e prossegue para a próxima tela
                saveNameAndProceed(name);
            }
        });
    }

    private void saveNameAndProceed(String name) {
        // Salva o nome do usuário e inicia a WelcomeActivity
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(NAME_KEY, name);
        editor.apply();

        Intent intent = new Intent(NameActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish(); // Impede que o usuário volte para esta tela
    }
}
