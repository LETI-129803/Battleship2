package battleship;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GameHistoryManager {

    /**
     * Exporta o histórico das jogadas realizadas para um ficheiro JSON.
     */
    public static void saveHistoryToJson(List<IMove> moves, String filePath) throws IOException {
        if (moves == null) {
            throw new IllegalArgumentException("A lista de jogadas não pode ser nula.");
        }

        List<Map<String, Object>> historyList = new ArrayList<>();

        for (IMove move : moves) {
            Map<String, Object> moveMap = new LinkedHashMap<>();
            moveMap.put("moveNumber", move.getNumber());

            // Registar coordenadas dos tiros
            List<Map<String, Object>> shotsList = new ArrayList<>();
            for (IPosition pos : move.getShots()) {
                Map<String, Object> posMap = new LinkedHashMap<>();
                posMap.put("row", String.valueOf(pos.getClassicRow()));
                posMap.put("column", pos.getClassicColumn());
                shotsList.add(posMap);
            }
            moveMap.put("shots", shotsList);

            // Registar resultados dos tiros
            List<Map<String, Object>> resultsList = new ArrayList<>();
            for (IGame.ShotResult result : move.getShotResults()) {
                Map<String, Object> resMap = new LinkedHashMap<>();
                resMap.put("valid", result.valid());
                resMap.put("repeated", result.repeated());
                resMap.put("sunk", result.sunk());
                if (result.ship() != null) {
                    resMap.put("shipType", result.ship().getCategory());
                } else {
                    resMap.put("shipType", "Water");
                }
                resultsList.add(resMap);
            }
            moveMap.put("results", resultsList);

            historyList.add(moveMap);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(new File(filePath), historyList);
    }
}