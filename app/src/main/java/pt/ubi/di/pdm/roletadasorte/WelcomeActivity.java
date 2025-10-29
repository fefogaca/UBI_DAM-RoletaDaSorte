package pt.ubi.di.pdm.roletadasorte;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// Esta atividade exibe uma mensagem de boas-vindas personalizada com o nome do usuário
// e um botão para iniciar a MainActivity (a roleta).
public class WelcomeActivity extends AppCompatActivity {

    // Componentes da interface do usuário
    private TextView textWelcome; // TextView para exibir a mensagem de boas-vindas
    private Button btnPlay; // Botão para iniciar o jogo da roleta

    // Constantes para o nome do arquivo de SharedPreferences e a chave do nome do usuário
    private static final String PREFS_NAME = "RoletaPrefs";
    private static final String NAME_KEY = "UserName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome); // Define o layout da atividade

        // Inicializa os componentes da UI
        textWelcome = findViewById(R.id.textWelcome);
        btnPlay = findViewById(R.id.btnPlay);
        Button btnChangeName = findViewById(R.id.btnChangeName);

        // Obtém o nome do usuário salvo nas SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String userName = prefs.getString(NAME_KEY, "Usuário"); // Pega o nome, ou "Usuário" como padrão

        // Define o texto de boas-vindas com o nome do usuário
        textWelcome.setText(getString(R.string.hello_user, userName));

        // Configura o listener de clique para o botão "Jogar"
        btnPlay.setOnClickListener(v -> {
            // Cria uma Intent para iniciar a MainActivity
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            // Não finaliza esta atividade para que o usuário possa voltar a ela se quiser
        });

        // Configura o listener de clique para o botão "Trocar Nome"
        btnChangeName.setOnClickListener(v -> {
            // Limpa o nome do usuário das SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(NAME_KEY);
            editor.apply();

            // Inicia a NameActivity para que o usuário possa digitar um novo nome
            Intent intent = new Intent(WelcomeActivity.this, NameActivity.class);
            startActivity(intent);
            finish(); // Finaliza esta atividade para que o usuário não possa voltar a ela
        });
    }
}
