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

    private List<Opcao> opcoes;
    private OnItemClickListener listener;

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
        holder.textNumero.setText(String.valueOf(opcao.getNumero()));
        holder.textOpcao.setText(opcao.getTexto());

        holder.btnRemover.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemoverClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return opcoes.size();
    }

    static class OpcaoViewHolder extends RecyclerView.ViewHolder {
        TextView textNumero;
        TextView textOpcao;
        ImageButton btnRemover;

        public OpcaoViewHolder(@NonNull View itemView) {
            super(itemView);
            textNumero = itemView.findViewById(R.id.textNumero);
            textOpcao = itemView.findViewById(R.id.textOpcao);
            btnRemover = itemView.findViewById(R.id.btnRemover);
        }
    }
}