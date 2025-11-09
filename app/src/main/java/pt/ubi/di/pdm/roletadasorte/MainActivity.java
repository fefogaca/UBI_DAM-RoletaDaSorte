package pt.ubi.di.pdm.roletadasorte;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Felipe Fogaça
 * Esta tela gerencia a criação da roleta, a adição e remoção de opções,
 * a animação de giro e a interação com o histórico de roletas
 * O código foi documentado para fácil entendimento e mudanças futuras
 */
public class MainActivity extends AppCompatActivity {

    // User Inteface
    private TextInputEditText inputOpcao; // Inserir uma nova opção
    private Button btnAdicionar;          // Botão para adicionar a opção à lista
    private Button btnGirar;              // Botão para iniciar a animação de giro
    private Button btnLimpar;             // Botão para remover todas as opções
    private Button btnHistory;            // Botão para abrir a tela de histórico
    private TextView textResultado;       // Texto para exibir a opção sorteada
    private TextView textContador;        // Texto para exibir o número de opções na roleta.
    private RoletaView roletaView;        // A view customizada que desenha a roleta.
    private ImageView imageSeta;          // A imagem da seta que aponta para o resultado
    private RecyclerView recyclerOpcoes;  // A lista visual de opções adicionadas.

    // Estado da roleta
    private List<Opcao> listaOpcoes;      // A lista de objetos Opcao que formam a roleta
    private OpcoesAdapter adapter;        // O adaptador para o RecyclerView de opções
    private boolean girando = false;      // Flag para impedir ações enquanto a roleta gira

    // Gerenciamento do histórico
    private HistoryManager historyManager; // Gerenciador para salvar e carregar o histórico.
    private ActivityResultLauncher<Intent> historyLauncher; // Launcher para a tela de histórico.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarComponentes();
        configurarRecyclerView();
        configurarListeners();
        configurarHistory();

        // Pra roleta começar na posição padrão
        roletaView.setRotation(0f);
    }

    private void inicializarComponentes() {
        inputOpcao = findViewById(R.id.inputOpcao);
        btnAdicionar = findViewById(R.id.btnAdicionar);
        btnGirar = findViewById(R.id.btnGirar);
        btnLimpar = findViewById(R.id.btnLimpar);
        btnHistory = findViewById(R.id.btnHistory);
        textResultado = findViewById(R.id.textResultado);
        textContador = findViewById(R.id.textContador);
        roletaView = findViewById(R.id.roletaView);
        imageSeta = findViewById(R.id.imageSeta);
        recyclerOpcoes = findViewById(R.id.recyclerOpcoes);

        listaOpcoes = new ArrayList<>();
        historyManager = new HistoryManager(this);
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

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            historyLauncher.launch(intent);
        });

        // Função que permite que adicione uma opção pressionando "Enter" no teclado virtual.
        // Função a+
        inputOpcao.setOnEditorActionListener((v, actionId, event) -> {
            adicionarOpcao();
            return true;
        });
    }

    /**
     * Configura o ActivityResultLauncher para receber dados da HistoryActivity
     * Quando um item do histórico é selecionado, a roleta atual é atualizada com essas opções
     */
    private void configurarHistory() {
        historyLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Verifica se a tela de histórico retornou um resultado positivo
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // Extrai a lista de opções do Intent de resultado.
                        // Histórico com base nas Arrays
                        ArrayList<Opcao> options = (ArrayList<Opcao>) result.getData().getSerializableExtra("history_options");
                        if (options != null) {
                            loadOptions(options);
                            Toast.makeText(this, "Roleta carregada do histórico!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Carrega uma lista de opções (vinda do histórico) para a roleta principal
     * @param options A lista de opções a ser carregada
     */
    private void loadOptions(ArrayList<Opcao> options) {
        listaOpcoes.clear();
        listaOpcoes.addAll(options);
        // Notifica o adapter que todos os dados mudaram.
        adapter.notifyDataSetChanged();
        atualizarInterface();
    }

    /**
     * Adiciona uma nova opção à roleta, com base no texto inserido pelo usuário
     */
    private void adicionarOpcao() {
        String texto = inputOpcao.getText().toString().trim();

        // Ifs de validações de input
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

        // Adiciona a nova opção à lista de dados.
        Opcao novaOpcao = new Opcao(texto);
        listaOpcoes.add(novaOpcao);

        // Notifica o adapter que um novo item foi inserido para última posição.
        adapter.notifyItemInserted(listaOpcoes.size() - 1);
        atualizarInterface();

        // Limpa o campo de entrada
        inputOpcao.setText("");
        inputOpcao.requestFocus();

        Toast.makeText(this, "Opção adicionada!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Função que remove uma opção da roleta na posição especificada
     * @param position A posição do item a ser removido
     */
    private void removerOpcao(int position) {
        if (position >= 0 && position < listaOpcoes.size()) {
            listaOpcoes.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, listaOpcoes.size());
            atualizarInterface();
            Toast.makeText(this, "Opção removida!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Função que remove todas as opções da roleta
     */
    private void limparTodasOpcoes() {
        if (listaOpcoes.isEmpty()) {
            Toast.makeText(this, "Não há opções para limpar!", Toast.LENGTH_SHORT).show();
            return;
        }

        int tamanho = listaOpcoes.size();
        listaOpcoes.clear();
        adapter.notifyItemRangeRemoved(0, tamanho);
        atualizarInterface();
        textResultado.setVisibility(View.GONE);
        Toast.makeText(this, "Todas as opções foram removidas!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Inicia a animação de giro da roleta e determina o vencedor
     */
    private void girarRoleta() {
        if (girando) {
            return; // Impede múltiplos giros simultâneos.
        }
        if (listaOpcoes.size() < 2) {
            Toast.makeText(this, "Adicione pelo menos duas opções para girar!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Salva a configuração atual no histórico antes de girar pro histórico
        historyManager.addRouletteToHistory(new ArrayList<>(listaOpcoes));

        girando = true;
        // Stop nos botões para não impactar a animação
        btnGirar.setEnabled(false);
        btnAdicionar.setEnabled(false);
        btnLimpar.setEnabled(false);
        btnHistory.setEnabled(false);
        textResultado.setVisibility(View.GONE);

        // Calculo da roleta abaixo:
        // Sorteia o índice da opção vencedora antes da animação começar
        Random random = new Random();
        int indexVencedor = random.nextInt(listaOpcoes.size());
        Opcao vencedora = listaOpcoes.get(indexVencedor);

        // Calcula o ângulo final da roleta.
        float anguloPorOpcao = 360f / listaOpcoes.size();
        // O ângulo de destino é o centro da fatia vencedora e que pare no 270 graus, sob a seta
        float anguloDestino = 270 - ((indexVencedor * anguloPorOpcao) + (anguloPorOpcao / 2));

        // Adiciona várias rotações completas para um efeito de roleta entre 5 a 9 rotações
        int rotacoesCompletas = 5 + random.nextInt(5);
        float grausFinais = (rotacoesCompletas * 360f) + anguloDestino;

        // Configura e inicia a animação, que dura 4 segundos, acelerando e desacelarando suavemente
        ObjectAnimator animator = ObjectAnimator.ofFloat(roletaView, "rotation", roletaView.getRotation(), grausFinais);
        animator.setDuration(4000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                textResultado.setText(getString(R.string.roulette_result, vencedora.getTexto()));
                textResultado.setVisibility(View.VISIBLE);

                // Habilita os botões novamente
                girando = false;
                btnGirar.setEnabled(true);
                btnAdicionar.setEnabled(true);
                btnLimpar.setEnabled(true);
                btnHistory.setEnabled(true);
            }
        });

        animator.start();
    }

    /**
     * Atualiza a interface do usuário com base no número de opções
     * Isso inclui o contador de texto e o estado do botão "Girar"
     */
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

        roletaView.setOpcoes(listaOpcoes);

        // O botão de girar só é habilitado se houver 2 ou mais opções
        btnGirar.setEnabled(total > 1);
    }
}
