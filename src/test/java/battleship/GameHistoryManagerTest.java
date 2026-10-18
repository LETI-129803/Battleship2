package battleship;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameHistoryManagerTest {

    @Test
    void testSaveHistoryToJson(@TempDir Path tempDir) throws IOException {
        File tempFile = tempDir.resolve("game_history_test.json").toFile();

        List<IMove> moves = new ArrayList<>();
        // Testar com lista vazia de jogadas
        GameHistoryManager.saveHistoryToJson(moves, tempFile.getAbsolutePath());

        assertTrue(tempFile.exists(), "O ficheiro JSON de histórico deve ser criado.");
        assertTrue(tempFile.length() > 0, "O ficheiro JSON não deve estar vazio.");
    }
}