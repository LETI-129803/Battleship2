package battleship;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitarios da classe Move.
 * Adaptados a ShotResult(boolean valid, boolean repeated, IShip ship, boolean sunk).
 */
public class MoveTest {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private static JsonNode lerJson(String json) throws Exception {
		return MAPPER.readTree(json);
	}

	private static String capturarSaida(Runnable acao) {
		PrintStream original = System.out;
		ByteArrayOutputStream saida = new ByteArrayOutputStream();
		try {
			System.setOut(new PrintStream(saida, true, StandardCharsets.UTF_8));
			acao.run();
		} finally {
			System.setOut(original);
		}
		return saida.toString(StandardCharsets.UTF_8);
	}

	@Test
	@DisplayName("Verificar getters e toString de uma jogada")
	void testGettersAndToString() {
		List<IPosition> shots = List.of(new Position(0, 0), new Position(0, 1));
		List<IGame.ShotResult> results = List.of(
				new IGame.ShotResult(true, false, null, false),
				new IGame.ShotResult(true, false, null, false)
		);

		Move move = new Move(1, shots, results);
		assertEquals(1, move.getNumber());
		assertSame(shots, move.getShots());
		assertSame(results, move.getShotResults());
		assertEquals("Move{number=1, shots=2, results=2}", move.toString());
	}

	@Test
	@DisplayName("Verificar JSON de tres tiros invalidos")
	void testProcessEnemyFireInvalidShots() throws Exception {
		List<IGame.ShotResult> results = List.of(
				new IGame.ShotResult(false, false, null, false),
				new IGame.ShotResult(false, false, null, false),
				new IGame.ShotResult(false, false, null, false)
		);

		Move move = new Move(1, new ArrayList<>(), results);
		JsonNode json = lerJson(move.processEnemyFire(false));

		assertEquals(0, json.get("validShots").asInt());
		assertEquals(3, json.get("outsideShots").asInt());
		assertEquals(0, json.get("repeatedShots").asInt());
		assertEquals(0, json.get("missedShots").asInt());
		assertTrue(json.get("sunkBoats").isEmpty());
		assertTrue(json.get("hitsOnBoats").isEmpty());
	}

	@Test
	@DisplayName("Verificar modo verbose com tiros repetidos")
	void testProcessEnemyFireOnlyRepeatedVerbose() throws Exception {
		List<IGame.ShotResult> umRepetido = List.of(
				new IGame.ShotResult(true, true, null, false),
				new IGame.ShotResult(false, false, null, false),
				new IGame.ShotResult(false, false, null, false)
		);
		Move move1 = new Move(1, List.of(), umRepetido);
		String output1 = capturarSaida(() -> move1.processEnemyFire(true));
		assertTrue(output1.contains("1 tiro repetido"));
		assertTrue(output1.contains("2 tiros exteriores"));

		List<IGame.ShotResult> doisRepetidos = List.of(
				new IGame.ShotResult(true, true, null, false),
				new IGame.ShotResult(true, true, null, false),
				new IGame.ShotResult(false, false, null, false)
		);
		Move move2 = new Move(2, List.of(), doisRepetidos);
		String output2 = capturarSaida(() -> move2.processEnemyFire(true));
		assertTrue(output2.contains("2 tiros repetidos"));
		assertEquals(2, lerJson(move2.processEnemyFire(false)).get("repeatedShots").asInt());
	}

	@Test
	@DisplayName("Verificar tiros na agua, singular e plural")
	void testProcessEnemyFireMisses() throws Exception {
		IGame.ShotResult agua = new IGame.ShotResult(true, false, null, false);

		Move move1 = new Move(1, List.of(), List.of(agua));
		String output1 = capturarSaida(() -> move1.processEnemyFire(true));
		assertTrue(output1.contains("1 tiro na água"));
		assertEquals(1, lerJson(move1.processEnemyFire(false)).get("missedShots").asInt());

		Move move2 = new Move(2, List.of(), List.of(agua, agua));
		String output2 = capturarSaida(() -> move2.processEnemyFire(true));
		assertTrue(output2.contains("2 tiros na água"));
		assertEquals(2, lerJson(move2.processEnemyFire(false)).get("missedShots").asInt());
	}

	@Test
	@DisplayName("Verificar acertos, afundamentos e JSON")
	void testProcessEnemyFireHitsAndSunkWithVerbose() throws Exception {
		IShip caravel = Ship.buildShip("caravela", Compass.NORTH, new Position(0, 0));
		IShip barge1 = Ship.buildShip("barca", Compass.NORTH, new Position(5, 5));
		IShip barge2 = Ship.buildShip("barca", Compass.NORTH, new Position(7, 7));
		assertNotNull(caravel);
		assertNotNull(barge1);
		assertNotNull(barge2);

		List<IGame.ShotResult> results = List.of(
				new IGame.ShotResult(true, false, caravel, false),
				new IGame.ShotResult(true, false, barge1, true),
				new IGame.ShotResult(true, false, barge2, true)
		);
		Move move = new Move(1, List.of(), results);
		String output = capturarSaida(() -> move.processEnemyFire(true));
		assertTrue(output.contains("ao fundo"));

		JsonNode json = lerJson(move.processEnemyFire(false));
		assertEquals(3, json.get("validShots").asInt());
		assertEquals(0, json.get("missedShots").asInt());
		assertEquals(0, json.get("outsideShots").asInt());
		assertEquals(1, json.get("sunkBoats").size());
		assertEquals(barge1.getCategory(), json.get("sunkBoats").get(0).get("type").asText());
		assertEquals(2, json.get("sunkBoats").get(0).get("count").asInt());
		assertEquals(1, json.get("hitsOnBoats").size());
		assertEquals(caravel.getCategory(), json.get("hitsOnBoats").get(0).get("type").asText());
	}

	@Test
	@DisplayName("Verificar combinacao de acerto, agua e repetido")
	void testProcessEnemyFireCombinedVerboseBranches() throws Exception {
		IShip caravel = Ship.buildShip("caravela", Compass.NORTH, new Position(0, 0));
		assertNotNull(caravel);

		List<IGame.ShotResult> results = List.of(
				new IGame.ShotResult(true, false, caravel, false),
				new IGame.ShotResult(true, false, null, false),
				new IGame.ShotResult(true, true, null, false)
		);
		Move move = new Move(3, List.of(), results);
		String output = capturarSaida(() -> move.processEnemyFire(true));
		assertTrue(output.contains("repetido"));

		JsonNode json = lerJson(move.processEnemyFire(false));
		assertEquals(2, json.get("validShots").asInt());
		assertEquals(1, json.get("repeatedShots").asInt());
		assertEquals(1, json.get("missedShots").asInt());
		assertEquals(0, json.get("outsideShots").asInt());
	}

	@Test
	@DisplayName("Verificar multiplos acertos num navio nao afundado")
	void testProcessEnemyFireMultipleHitsOnBoat() throws Exception {
		IShip frigate = Ship.buildShip("fragata", Compass.EAST, new Position(2, 2));
		assertNotNull(frigate);

		List<IGame.ShotResult> results = List.of(
				new IGame.ShotResult(true, false, frigate, false),
				new IGame.ShotResult(true, false, frigate, false)
		);
		Move move = new Move(4, List.of(), results);
		JsonNode json = lerJson(move.processEnemyFire(true));

		assertEquals(2, json.get("validShots").asInt());
		assertEquals(1, json.get("hitsOnBoats").size());
		assertEquals(frigate.getCategory(), json.get("hitsOnBoats").get(0).get("type").asText());
		assertEquals(2, json.get("hitsOnBoats").get(0).get("hits").asInt());
	}

	@Test
	@DisplayName("Verificar formatacao de tiros exteriores")
	void testProcessEnemyFireOutsideShotsFormatting() throws Exception {
		IGame.ShotResult agua = new IGame.ShotResult(true, false, null, false);

		Move move1 = new Move(5, List.of(), List.of(agua));
		String output1 = capturarSaida(() -> move1.processEnemyFire(true));
		assertTrue(output1.contains("2 tiros exteriores"));
		assertEquals(2, lerJson(move1.processEnemyFire(false)).get("outsideShots").asInt());

		Move move2 = new Move(6, List.of(), List.of(agua, agua));
		String output2 = capturarSaida(() -> move2.processEnemyFire(true));
		assertTrue(output2.contains("1 tiro exterior"));
		assertEquals(1, lerJson(move2.processEnemyFire(false)).get("outsideShots").asInt());
	}


	@Test
	@DisplayName("Verificar tres tiros exteriores em modo verbose")
	void testTresTirosExterioresVerbose() throws Exception {

		List<IGame.ShotResult> resultados = List.of(
				new IGame.ShotResult(false, false, null, false),
				new IGame.ShotResult(false, false, null, false),
				new IGame.ShotResult(false, false, null, false)
		);

		Move jogada = new Move(9, List.of(), resultados);

		String output = capturarSaida(
				() -> jogada.processEnemyFire(true)
		);

		assertTrue(output.contains("3 tiros exteriores"));

		JsonNode json = lerJson(jogada.processEnemyFire(false));

		assertEquals(0, json.get("validShots").asInt());
		assertEquals(3, json.get("outsideShots").asInt());
		assertEquals(0, json.get("repeatedShots").asInt());
		assertEquals(0, json.get("missedShots").asInt());
	}


	@Test
	@DisplayName("Verificar afundamento de uma barca")
	void testNavioAfundadoSingular() throws Exception {

		IShip barca = Ship.buildShip(
				"barca",
				Compass.NORTH,
				new Position(4, 4)
		);

		assertNotNull(barca);

		List<IGame.ShotResult> resultados = List.of(
				new IGame.ShotResult(true, false, barca, true),
				new IGame.ShotResult(true, false, null, false),
				new IGame.ShotResult(false, false, null, false)
		);

		Move jogada = new Move(10, List.of(), resultados);

		String output = capturarSaida(
				() -> jogada.processEnemyFire(true)
		);

		assertTrue(output.contains("1 Barca ao fundo"));

		JsonNode json = lerJson(jogada.processEnemyFire(false));

		assertEquals(2, json.get("validShots").asInt());
		assertEquals(1, json.get("missedShots").asInt());
		assertEquals(1, json.get("outsideShots").asInt());

		assertEquals(1, json.get("sunkBoats").size());
		assertEquals(
				"Barca",
				json.get("sunkBoats").get(0).get("type").asText()
		);
		assertEquals(
				1,
				json.get("sunkBoats").get(0).get("count").asInt()
		);
	}

}
