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

    // Declaração dos componentes da interface do usuário
    private TextInputEditText inputOpcao; // Campo para inserir uma nova opção
    private Button btnAdicionar; // Botão para adicionar a opção
    private Button btnGirar; // Botão para girar a roleta
    private Button btnLimpar; // Botão para limpar todas as opções
    private TextView textResultado; // Texto para exibir o resultado do sorteio
    private TextView textContador; // Texto para exibir o número de opções adicionadas
    private RoletaView roletaView; // A view customizada da roleta
    private ImageView imageSeta; // A seta que indica a opção sorteada
    private RecyclerView recyclerOpcoes; // A lista de opções adicionadas

    // Variáveis para controlar o estado da roleta
    private List<Opcao> listaOpcoes; // A lista de objetos Opcao
    private OpcoesAdapter adapter; // O adaptador para o RecyclerView
    private int contadorOpcoes = 0; // Contador para o número de opções
    private boolean girando = false; // Flag para indicar se a roleta está girando

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa os componentes da UI e configura os listeners
        inicializarComponentes();
        configurarRecyclerView();
        configurarListeners();

        // Define a rotação inicial da roleta como 0
        roletaView.setRotation(0f);
    }

    // Método para associar as variáveis aos componentes do layout
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

    // Método para configurar o RecyclerView que exibe a lista de opções
    private void configurarRecyclerView() {
        // Configura o RecyclerView, que é a lista rolável de opções
        adapter = new OpcoesAdapter(listaOpcoes, position -> removerOpcao(position));
        recyclerOpcoes.setLayoutManager(new LinearLayoutManager(this));
        recyclerOpcoes.setAdapter(adapter);
    }

    // Método para configurar os listeners de clique dos botões
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

    // Método para adicionar uma nova opção à roleta
    private void adicionarOpcao() {
        String texto = inputOpcao.getText().toString().trim();

        // Validações da entrada do usuário
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

        // Adiciona a nova opção à lista
        contadorOpcoes++;
        Opcao novaOpcao = new Opcao(texto, contadorOpcoes);
        listaOpcoes.add(novaOpcao);

        // Notifica o adapter sobre a nova opção e atualiza a UI
        adapter.notifyItemInserted(listaOpcoes.size() - 1);
        atualizarInterface();

        // Limpa o campo de entrada e solicita o foco
        inputOpcao.setText("");
        inputOpcao.requestFocus();

        Toast.makeText(this, "Opção adicionada!", Toast.LENGTH_SHORT).show();
    }

    // Método para remover uma opção da roleta
    private void removerOpcao(int position) {
        if (position >= 0 && position < listaOpcoes.size()) {
            listaOpcoes.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, listaOpcoes.size());
            atualizarInterface();
            Toast.makeText(this, "Opção removida!", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para limpar todas as opções da roleta
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

    // Método para girar a roleta
    private void girarRoleta() {
        if (girando) {
            return; // Se já estiver girando, não faz nada
        }

        if (listaOpcoes.size() < 2) {
            Toast.makeText(this, "Adicione pelo menos duas opções para girar!", Toast.LENGTH_SHORT).show();
            return;
        }

        girando = true;
        // Desabilita os botões para evitar cliques durante a animação
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
                textResultado.setText(getString(R.string.roulette_result, vencedora.getTexto()));
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

    // Método para atualizar a interface do usuário com base no número de opções
    private void atualizarInterface() {
        int total = listaOpcoes.size();
        String textoContador;
        if (total == 0) {
            textoContador = getString(R.string.zero_opcoes_adicionadas);
        } else if (total == 1) {
            textoContador = getString(R.string.one_opcao_adicionada);
        } else {
            textoContador = getString(R.string.n_opcoes_adicionadas, total);
        }
        textContador.setText(textoContador);
        roletaView.setOpcoes(listaOpcoes); // Atualiza a RoletaView

        // Habilita ou desabilita o botão de girar
        if (total > 1) {
            btnGirar.setEnabled(true);
        } else {
            btnGirar.setEnabled(false);
        }
    }
}
