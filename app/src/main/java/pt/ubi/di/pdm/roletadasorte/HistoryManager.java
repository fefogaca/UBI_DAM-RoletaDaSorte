package pt.ubi.di.pdm.roletadasorte;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Felipe Fogaça
 * Gerencia o salvamento e carregamento do histórico de roletas
 * Utiliza um arquivo json no armazenamento interno do aplicativo para persistir os dados
 */
public class HistoryManager {

    private static final String HISTORY_FILE_NAME = "roulette_history.json";
    // Max de 3 roletas salvas
    private static final int MAX_HISTORY_SIZE = 3;

    private final Context context;
    private final Gson gson;

    public HistoryManager(Context context) {
        this.context = context;
        this.gson = new Gson(); // Gson para serialização/deserialização json
    }

    public LinkedList<ArrayList<Opcao>> loadHistory() {
        File historyFile = new File(context.getFilesDir(), HISTORY_FILE_NAME);
        if (!historyFile.exists()) {
            return new LinkedList<>();
        }

        try (FileReader reader = new FileReader(historyFile)) {
            Type type = new TypeToken<LinkedList<ArrayList<Opcao>>>() {}.getType();
            LinkedList<ArrayList<Opcao>> history = gson.fromJson(reader, type);
            return history != null ? history : new LinkedList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new LinkedList<>();
        }
    }

    public void saveHistory(LinkedList<ArrayList<Opcao>> history) {
        File historyFile = new File(context.getFilesDir(), HISTORY_FILE_NAME);
        try (FileWriter writer = new FileWriter(historyFile)) {
            gson.toJson(history, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addRouletteToHistory(ArrayList<Opcao> newRouletteOptions) {
        if (newRouletteOptions == null || newRouletteOptions.isEmpty()) {
            return;
        }

        LinkedList<ArrayList<Opcao>> history = loadHistory();

        if (!history.isEmpty() && areListsEqual(history.getFirst(), newRouletteOptions)) {
            return;
        }

        history.addFirst(newRouletteOptions);

        while (history.size() > MAX_HISTORY_SIZE) {
            history.removeLast();
        }

        saveHistory(history);
    }

    private boolean areListsEqual(List<Opcao> list1, List<Opcao> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list1.size(); i++) {
            if (!list1.get(i).getTexto().equals(list2.get(i).getTexto())) {
                return false;
            }
        }
        return true;
    }
}
