package pt.ubi.di.pdm.roletadasorte;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter para o RecyclerView que exibe a lista de opções.
 * Ele gerencia como os dados (as opções) são apresentados na lista e lida com as interações,
 * como o clique no botão de remover.
 */
public class OpcoesAdapter extends RecyclerView.Adapter<OpcoesAdapter.OpcaoViewHolder> {

    private List<Opcao> opcoes; // A lista de opções a serem exibidas
    private OnItemClickListener listener; // Listener para o clique no botão de remover

    // Interface para o listener de clique
    public interface OnItemClickListener {
        void onRemoverClick(int position);
    }

    // Construtor do adapter
    public OpcoesAdapter(List<Opcao> opcoes, OnItemClickListener listener) {
        this.opcoes = opcoes;
        this.listener = listener;
    }

    // Cria um novo ViewHolder quando o RecyclerView precisa de um
    @NonNull
    @Override
    public OpcaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout do item da lista
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_opcao, parent, false);
        return new OpcaoViewHolder(view);
    }

    // Vincula os dados de uma opção a um ViewHolder
    @Override
    public void onBindViewHolder(@NonNull OpcaoViewHolder holder, int position) {
        Opcao opcao = opcoes.get(position);
        holder.textNumero.setText(String.valueOf(opcao.getNumero()));
        holder.textOpcao.setText(opcao.getTexto());

        // Configura o listener de clique para o botão de remover
        holder.btnRemover.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemoverClick(holder.getAdapterPosition());
            }
        });
    }

    // Retorna o número total de itens na lista
    @Override
    public int getItemCount() {
        return opcoes.size();
    }

    // Classe interna que representa o ViewHolder de um item da lista
    static class OpcaoViewHolder extends RecyclerView.ViewHolder {
        TextView textNumero; // O número da opção
        TextView textOpcao; // O texto da opção
        ImageButton btnRemover; // O botão para remover a opção

        public OpcaoViewHolder(@NonNull View itemView) {
            super(itemView);
            // Associa as variáveis aos componentes do layout do item
            textNumero = itemView.findViewById(R.id.textNumero);
            textOpcao = itemView.findViewById(R.id.textOpcao);
            btnRemover = itemView.findViewById(R.id.btnRemover);
        }
    }
}