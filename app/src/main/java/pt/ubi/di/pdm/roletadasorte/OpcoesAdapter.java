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
 * @author Felipe Fogaça
 * Adapter para o RecyclerView que exibe a lista de opções na tela principal
 * Ele gerencia como os dados (as opções) são apresentados na lista e lida com as interações do usuário,
 * como o clique no botão de remover uma opção
 */
public class OpcoesAdapter extends RecyclerView.Adapter<OpcoesAdapter.OpcaoViewHolder> {

    private final List<Opcao> opcoes;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onRemoverClick(int position);
    }

    public OpcoesAdapter(List<Opcao> opcoes, OnItemClickListener listener) {
        this.opcoes = opcoes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OpcaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_opcao, parent, false);
        return new OpcaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OpcaoViewHolder holder, int position) {
        Opcao opcao = opcoes.get(position);

        holder.textNumero.setText(String.valueOf(position + 1));
        holder.textOpcao.setText(opcao.getTexto());

        holder.btnRemover.setOnClickListener(v -> {
            if (listener != null) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    listener.onRemoverClick(currentPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return opcoes.size();
    }

    static class OpcaoViewHolder extends RecyclerView.ViewHolder {
        final TextView textNumero;
        final TextView textOpcao;
        final ImageButton btnRemover;

        public OpcaoViewHolder(@NonNull View itemView) {
            super(itemView);
            textNumero = itemView.findViewById(R.id.textNumero);
            textOpcao = itemView.findViewById(R.id.textOpcao);
            btnRemover = itemView.findViewById(R.id.btnRemover);
        }
    }
}