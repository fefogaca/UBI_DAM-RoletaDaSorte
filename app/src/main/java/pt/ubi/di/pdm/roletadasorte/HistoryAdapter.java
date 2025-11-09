package pt.ubi.di.pdm.roletadasorte;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Felipe Fogaça
 * Adapter para o RecyclerView que exibe a lista de roletas salvas no histórico
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private final List<ArrayList<Opcao>> historyList;
    private final OnHistoryItemClickListener listener;

    public interface OnHistoryItemClickListener {
        void onHistoryItemClick(ArrayList<Opcao> options);
    }

    public HistoryAdapter(List<ArrayList<Opcao>> historyList, OnHistoryItemClickListener listener) {
        this.historyList = historyList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        ArrayList<Opcao> options = historyList.get(position);
        holder.bind(options, listener);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView textHistoryTitle;
        private final TextView textHistoryOptions;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textHistoryTitle = itemView.findViewById(R.id.textHistoryTitle);
            textHistoryOptions = itemView.findViewById(R.id.textHistoryOptions);
        }

        public void bind(final ArrayList<Opcao> options, final OnHistoryItemClickListener listener) {
            textHistoryTitle.setText("Roleta com " + options.size() + " opções");

            StringBuilder optionsString = new StringBuilder();
            for (int i = 0; i < options.size(); i++) {
                optionsString.append(options.get(i).getTexto());
                if (i < options.size() - 1) {
                    optionsString.append(", ");
                }
            }
            textHistoryOptions.setText(optionsString.toString());

            itemView.setOnClickListener(v -> listener.onHistoryItemClick(options));
        }
    }
}
