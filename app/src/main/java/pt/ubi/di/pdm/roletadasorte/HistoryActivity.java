package pt.ubi.di.pdm.roletadasorte;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author Felipe Fogaça
 * Atividade responsável por exibir a lista de roletas giradas anteriormente
 * O usuário pode selecionar um item do histórico para carregá-lo de volta na tela principal
 */
public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerHistory;
    private TextView textEmptyHistory;
    private HistoryManager historyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // Define a ação do botão de voltar.

        recyclerHistory = findViewById(R.id.recyclerHistory);
        textEmptyHistory = findViewById(R.id.textEmptyHistory);
        historyManager = new HistoryManager(this);

        setupRecyclerView();
    }

    /**
     * Carrega os dados do histórico e configura o RecyclerView para exibir
     */
    private void setupRecyclerView() {
        LinkedList<ArrayList<Opcao>> history = historyManager.loadHistory();

        if (history.isEmpty()) {
            recyclerHistory.setVisibility(View.GONE);
            textEmptyHistory.setVisibility(View.VISIBLE);
        } else {
            recyclerHistory.setVisibility(View.VISIBLE);
            textEmptyHistory.setVisibility(View.GONE);

            recyclerHistory.setLayoutManager(new LinearLayoutManager(this));
            HistoryAdapter adapter = new HistoryAdapter(history, options -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("history_options", options);
                setResult(RESULT_OK, resultIntent);
                finish();
            });
            recyclerHistory.setAdapter(adapter);
        }
    }
}
