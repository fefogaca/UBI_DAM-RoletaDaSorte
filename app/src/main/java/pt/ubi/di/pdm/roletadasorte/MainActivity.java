package pt.ubi.di.pdm.roletadasorte;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText inputOpcao;
    private Button btnAdicionar;
    private Button btnGirar;
    private Button btnLimpar;
    private TextView textOpcaoAtual;
    private TextView textResultado;
    private TextView textContador;
    private View roletaCirculo;
    private RecyclerView recyclerOpcoes;

    private List<Opcao> listaOpcoes;
    private OpcoesAdapter adapter;
    private int contadorOpcoes = 0;
    private boolean girando = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarComponentes();
        configurarRecyclerView();
        configurarListeners();
    }

    private void inicializarComponentes() {
        inputOpcao = findViewById(R.id.inputOpcao);
        btnAdicionar = findViewById(R.id.btnAdicionar);
        btnGirar = findViewById(R.id.btnGirar);
        btnLimpar = findViewById(R.id.btnLimpar);
        textOpcaoAtual = findViewById(R.id.textOpcaoAtual);
        textResultado = findViewById(R.id.textResultado);
        textContador = findViewById(R.id.textContador);
        roletaCirculo = findViewById(R.id.roletaCirculo);
        recyclerOpcoes = findViewById(R.id.recyclerOpcoes);

        listaOpcoes = new ArrayList<>();
    }

    private void configurarRecyclerView() {
        adapter = new OpcoesAdapter(listaOpcoes, position -> removerOpcao(position));
        recyclerOpcoes.setLayoutManager(new LinearLayoutManager(this));
        recyclerOpcoes.setAdapter(adapter);
    }

    private void configurarListeners() {
        btnAdicionar.setOnClickListener(v -> adicionarOpcao());

        btnGirar.setOnClickListener(v -> girarRoleta());

        btnLimpar.setOnClickListener(v -> limparTodasOpcoes());

        inputOpcao.setOnEditorActionListener((v, actionId, event) -> {
            adicionarOpcao();
            return true;
        });
    }

    private void adicionarOpcao() {
        String texto = inputOpcao.getText().toString().trim();

        if (texto.isEmpty()) {
            Toast.makeText(this, "Digite uma opção primeiro!", Toast.LENGTH_SHORT).show();
            return;
        }

        contadorOpcoes++;
        Opcao novaOpcao = new Opcao(texto, contadorOpcoes);
        listaOpcoes.add(novaOpcao);

        adapter.notifyItemInserted(listaOpcoes.size() - 1);
        atualizarInterface();

        inputOpcao.setText("");
        inputOpcao.requestFocus();

        Toast.makeText(this, "Opção adicionada!", Toast.LENGTH_SHORT).show();
    }

    private void removerOpcao(int position) {
        if (position >= 0 && position < listaOpcoes.size()) {
            listaOpcoes.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, listaOpcoes.size());
            atualizarInterface();
            Toast.makeText(this, "Opção removida!", Toast.LENGTH_SHORT).show();
        }
    }

    private void limparTodasOpcoes() {
        if (listaOpcoes.isEmpty()) {
            Toast.makeText(this, "Não há opções para limpar!", Toast.LENGTH_SHORT).show();
            return;
        }

        int tamanho = listaOpcoes.size();
        listaOpcoes.clear();
        adapter.notifyItemRangeRemoved(0, tamanho);
        contadorOpcoes = 0;
        atualizarInterface();
        textResultado.setVisibility(View.GONE);
        Toast.makeText(this, "Todas as opções foram removidas!", Toast.LENGTH_SHORT).show();
    }

    private void girarRoleta() {
        if (girando) {
            return;
        }

        if (listaOpcoes.isEmpty()) {
            Toast.makeText(this, "Adicione opções primeiro!", Toast.LENGTH_SHORT).show();
            return;
        }

        girando = true;
        btnGirar.setEnabled(false);
        btnAdicionar.setEnabled(false);
        textResultado.setVisibility(View.GONE);

        // Animação da roleta
        Random random = new Random();
        int rotacoesCompletas = 5 + random.nextInt(3); // 5 a 7 rotações
        float grausFinais = rotacoesCompletas * 360f + random.nextInt(360);

        ObjectAnimator animator = ObjectAnimator.ofFloat(roletaCirculo, "rotation", 0f, grausFinais);
        animator.setDuration(3000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        // Animação do texto durante o giro
        Thread atualizadorTexto = new Thread(() -> {
            try {
                for (int i = 0; i < 30; i++) {
                    int indexAleatorio = random.nextInt(listaOpcoes.size());
                    String opcaoAtual = listaOpcoes.get(indexAleatorio).getTexto();

                    runOnUiThread(() -> textOpcaoAtual.setText(opcaoAtual));
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        atualizadorTexto.start();

        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                // Selecionar vencedor
                int indexVencedor = random.nextInt(listaOpcoes.size());
                Opcao vencedora = listaOpcoes.get(indexVencedor);

                textOpcaoAtual.setText(vencedora.getTexto());
                textResultado.setText("🎉 Resultado: " + vencedora.getTexto() + " 🎉");
                textResultado.setVisibility(View.VISIBLE);

                girando = false;
                btnGirar.setEnabled(true);
                btnAdicionar.setEnabled(true);

                // Resetar rotação para próximo giro
                roletaCirculo.setRotation(0f);
            }
        });

        animator.start();
    }

    private void atualizarInterface() {
        int total = listaOpcoes.size();
        textContador.setText(total + " opção" + (total != 1 ? "ões" : "") + " adicionada" + (total != 1 ? "s" : ""));

        if (total > 0) {
            btnGirar.setEnabled(true);
            textOpcaoAtual.setText("Pronto para girar!");
        } else {
            btnGirar.setEnabled(false);
            textOpcaoAtual.setText("Adicione opções");
        }
    }
}