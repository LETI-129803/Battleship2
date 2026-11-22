package battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameHistory class.
 * Author: 106363
 */
public class GameHistoryTest {

	@Test
	@DisplayName("Test GameHistory initialization and getNextMoveNumber")
	void testInitializationAndNextMoveNumber() {
		GameHistory gameHistory = new GameHistory();
		assertNotNull(gameHistory.getHistory());
		int nextMove = gameHistory.getNextMoveNumber();
		assertTrue(nextMove >= 1);
	}

	@Test
	@DisplayName("Test addMove and history persistence retrieval")
	void testAddMoveAndRetrieve() {
		GameHistory gameHistory = new GameHistory();
		int initialSize = gameHistory.getHistory().size();

		List<Map<String, Object>> shots = new ArrayList<>();
		Map<String, Object> shot1 = new HashMap<>();
		shot1.put("row", "A");
		shot1.put("column", 1);
		shot1.put("result", "Agua");
		shots.add(shot1);

		Map<String, Object> shot2 = new HashMap<>();
		shot2.put("row", "B");
		shot2.put("column", 2);
		shot2.put("result", "Tiro em navio");
		shot2.put("shipType", "Fragata");
		shots.add(shot2);

		int moveNumber = gameHistory.getNextMoveNumber();
		gameHistory.addMove(moveNumber, shots);

		assertEquals(initialSize + 1, gameHistory.getHistory().size());

		// Test printHistory with moves
		assertDoesNotThrow(() -> gameHistory.printHistory());
	}

	@Test
	@DisplayName("Test printHistory when history is empty")
	void testPrintHistoryEmpty() {
		// Criar uma subclasse de teste com lista vazia para garantir o branch de vazio
		GameHistory emptyHistory = new GameHistory();
		emptyHistory.getHistory().clear();
		assertDoesNotThrow(() -> emptyHistory.printHistory());
	}
}
