package battleship;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;

public class GameHistory {

    private static final String FILE_PATH = "data/game_history.json";

    private final List<Map<String, Object>> history = new ArrayList<>();

    private final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    // Carregar o histórico guardado ao iniciar o jogo
    public GameHistory() {
        File file = new File(FILE_PATH);

        if (file.exists()) {
            try {
                List<Map<String, Object>> savedHistory =
                        mapper.readValue(
                                file,
                                new TypeReference<List<Map<String, Object>>>() {}
                        );

                history.addAll(savedHistory);

            } catch (IOException e) {
                throw new RuntimeException(
                        "Erro ao carregar o histórico das jogadas", e
                );
            }
        }
    }

    public void addMove(int moveNumber,
                        List<Map<String, Object>> shots) {

        history.add(Map.of(
                "moveNumber", moveNumber,
                "shots", shots
        ));

        saveHistory();
    }

    private void saveHistory() {
        try {
            File file = new File(FILE_PATH);

            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }

            mapper.writeValue(file, history);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao guardar o histórico das jogadas", e
            );
        }
    }

    public List<Map<String, Object>> getHistory() {
        return history;
    }

    public int getNextMoveNumber() {
        return history.stream()
                .mapToInt(move -> ((Number) move.get("moveNumber")).intValue())
                .max()
                .orElse(0) + 1;
    }

    public void printHistory() {

        if (history.isEmpty()) {
            System.out.println("Ainda não existem jogadas registadas.");
            return;
        }

        System.out.println("\n===== HISTÓRICO DE JOGADAS =====");

        for (Map<String, Object> move : history) {

            System.out.println("\nJogada nº " + move.get("moveNumber"));

            List<Map<String, Object>> shots =
                    (List<Map<String, Object>>) move.get("shots");

            for (Map<String, Object> shot : shots) {

                System.out.print(
                        "Tiro: " + shot.get("row") + shot.get("column")
                                + " - " + shot.get("result")
                );

                if (shot.containsKey("shipType")) {
                    System.out.print(" (" + shot.get("shipType") + ")");
                }

                System.out.println();
            }
        }
    }
}