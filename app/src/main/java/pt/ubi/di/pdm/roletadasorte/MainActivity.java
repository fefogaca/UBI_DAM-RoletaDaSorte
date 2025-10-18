package pt.ubi.di.pdm.roletadasorte;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
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
    private TextView textResultado;
    private TextView textContador;
    private RoletaView roletaView; // Usando a nova RoletaView
    private ImageView imageSeta;
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

        roletaView.setRotation(0f);
    }

    private void inicializarComponentes() {
        inputOpcao = findViewById(R.id.inputOpcao);
        btnAdicionar = findViewById(R.id.btnAdicionar);
        btnGirar = findViewById(R.id.btnGirar);
        btnLimpar = findViewById(R.id.btnLimpar);
        textResultado = findViewById(R.id.textResultado);
        textContador = findViewById(R.id.textContador);
        roletaView = findViewById(R.id.roletaView); // Inicializando a RoletaView
        imageSeta = findViewById(R.id.imageSeta);
        recyclerOpcoes = findViewById(R.id.recyclerOpcoes);

        listaOpcoes = new ArrayList<>();
    }

    private void configurarRecyclerView() {
        // Configura o RecyclerView, que é a lista rolável de opções
        adapter = new OpcoesAdapter(listaOpcoes, position -> removerOpcao(position));
        recyclerOpcoes.setLayoutManager(new LinearLayoutManager(this));
        recyclerOpcoes.setAdapter(adapter);
    }

    private void configurarListeners() {
        // Define as ações que acontecem quando o usuário clica nos botões
        btnAdicionar.setOnClickListener(v -> adicionarOpcao());

        btnGirar.setOnClickListener(v -> girarRoleta());

        btnLimpar.setOnClickListener(v -> limparTodasOpcoes());

        // Permite que o usuário adicione uma opção pressionando "Enter" no teclado
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

        if (texto.length() > 20) {
            Toast.makeText(this, "A opção deve ter no máximo 20 caracteres!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (listaOpcoes.size() >= 10) {
            Toast.makeText(this, "A roleta pode ter no máximo 10 opções!", Toast.LENGTH_SHORT).show();
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
            return; // Se já estiver girando, não faz nada
        }

        if (listaOpcoes.size() < 2) {
            Toast.makeText(this, "Adicione pelo menos duas opções para girar!", Toast.LENGTH_SHORT).show();
            return;
        }

        girando = true;
        // Desabilitar botões para evitar cliques durante a animação
        btnGirar.setEnabled(false);
        btnAdicionar.setEnabled(false);
        btnLimpar.setEnabled(false);
        textResultado.setVisibility(View.GONE);

        // Sorteia o índice da opção vencedora ANTES da animação começar
        Random random = new Random();
        int indexVencedor = random.nextInt(listaOpcoes.size());
        Opcao vencedora = listaOpcoes.get(indexVencedor);

        // Calcula o ângulo final da roleta. A roleta é dividida em fatias iguais.
        float anguloPorOpcao = 360f / listaOpcoes.size();
        // O ângulo de destino é o centro da fatia vencedora.
        // O objetivo é que este ângulo pare no topo (270 graus).
        float anguloDestino = 270 - ((indexVencedor * anguloPorOpcao) + (anguloPorOpcao / 2));

        // Adiciona várias rotações completas para um efeito mais dramático
        int rotacoesCompletas = 5 + random.nextInt(5); // Entre 5 e 9 rotações
        float grausFinais = (rotacoesCompletas * 360f) + anguloDestino;

        // Animação da roleta
        ObjectAnimator animator = ObjectAnimator.ofFloat(roletaView, "rotation", 0f, grausFinais);
        animator.setDuration(4000); // Duração da animação em milissegundos
        animator.setInterpolator(new AccelerateDecelerateInterpolator()); // Acelera e desacelera suavemente

        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                // Exibe o resultado final
                textResultado.setText("🎉 " + vencedora.getTexto() + " 🎉");
                textResultado.setVisibility(View.VISIBLE);

                // Reabilita os botões
                girando = false;
                btnGirar.setEnabled(true);
                btnAdicionar.setEnabled(true);
                btnLimpar.setEnabled(true);

                // Mantém a roleta na posição final para o usuário ver o resultado
            }
        });

        // Inicia a animação
        roletaView.setRotation(0f); // Reseta a posição da roleta antes de começar a girar
        animator.start();
    }

    private void atualizarInterface() {
        int total = listaOpcoes.size();
        String textoContador;
        if (total == 0) {
            textoContador = "Nenhuma opção adicionada";
        } else if (total == 1) {
            textoContador = "1 opção adicionada";
        } else {
            textoContador = total + " opções adicionadas";
        }
        textContador.setText(textoContador);
        roletaView.setOpcoes(listaOpcoes); // Atualiza a RoletaView

        if (total > 1) {
            btnGirar.setEnabled(true);
        } else {
            btnGirar.setEnabled(false);
        }
    }
}
