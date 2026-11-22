package battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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

	@Test
	@DisplayName("Verificar historico vazio")
	void testHistoricoVazio() {

		GameHistory historico = new GameHistory();

		// Limpar apenas a lista em memoria
		historico.getHistory().clear();

		assertTrue(historico.getHistory().isEmpty());
		assertEquals(1, historico.getNextMoveNumber());

		PrintStream original = System.out;
		ByteArrayOutputStream saida = new ByteArrayOutputStream();

		try {
			System.setOut(new PrintStream(saida));

			historico.printHistory();

		} finally {
			System.setOut(original);
		}

		assertTrue(saida.toString().contains(
				"Ainda não existem jogadas registadas."
		));
	}

	@Test
	@DisplayName("Verificar historico com jogadas e navios")
	void testHistoricoComJogadas() {

		GameHistory historico = new GameHistory();
		historico.getHistory().clear();

		historico.getHistory().add(Map.of(
				"moveNumber", 2,
				"shots", List.of(Map.of(
						"row", "A",
						"column", 1,
						"result", "MISS"
				))
		));

		historico.getHistory().add(Map.of(
				"moveNumber", 7,
				"shots", List.of(Map.of(
						"row", "B",
						"column", 3,
						"result", "HIT",
						"shipType", "Fragata"
				))
		));

		assertEquals(2, historico.getHistory().size());
		assertEquals(8, historico.getNextMoveNumber());

		PrintStream original = System.out;
		ByteArrayOutputStream saida = new ByteArrayOutputStream();

		try {
			System.setOut(new PrintStream(saida));

			historico.printHistory();

		} finally {
			System.setOut(original);
		}

		String resultado = saida.toString();

		assertTrue(resultado.contains("HISTÓRICO DE JOGADAS"));
		assertTrue(resultado.contains("Jogada nº 2"));
		assertTrue(resultado.contains("Jogada nº 7"));
		assertTrue(resultado.contains("Tiro: A1 - MISS"));
		assertTrue(resultado.contains("Tiro: B3 - HIT (Fragata)"));
	}


}
