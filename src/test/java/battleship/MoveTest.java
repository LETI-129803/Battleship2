package battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Move class with 100% line and branch coverage.
 * Author: 106363
 */
public class MoveTest {

	@Test
	@DisplayName("Test Move getters and toString implementation")
	void testGettersAndToString() {
		List<IPosition> shots = List.of(new Position(0, 0), new Position(0, 1));
		List<IGame.ShotResult> results = List.of(
				new IGame.ShotResult(true, false, false, null),
				new IGame.ShotResult(true, false, false, null)
		);

		Move move = new Move(1, shots, results);
		assertEquals(1, move.getNumber());
		assertEquals(2, move.getShots().size());
		assertEquals(2, move.getShotResults().size());
		assertEquals("Move{number=1, shots=2, results=2}", move.toString());
	}

	@Test
	@DisplayName("Test processEnemyFire with invalid shots")
	void testProcessEnemyFireInvalidShots() {
		List<IPosition> shots = new ArrayList<>();
		List<IGame.ShotResult> results = new ArrayList<>();

		// 3 tiros invalidos
		results.add(new IGame.ShotResult(false, false, false, null));
		results.add(new IGame.ShotResult(false, false, false, null));
		results.add(new IGame.ShotResult(false, false, false, null));

		Move move = new Move(1, shots, results);
		String json = move.processEnemyFire(false);

		assertNotNull(json);
		assertTrue(json.contains("\"validShots\" : 0"));
		assertTrue(json.contains("\"outsideShots\" : 3"));
		assertTrue(json.contains("\"repeatedShots\" : 0"));
		assertTrue(json.contains("\"missedShots\" : 0"));
	}

	@Test
	@DisplayName("Test processEnemyFire verbose mode with repeated shots only")
	void testProcessEnemyFireOnlyRepeatedVerbose() {
		List<IPosition> shots = new ArrayList<>();
		List<IGame.ShotResult> results = new ArrayList<>();

		// 1 repetido e 2 invalidos -> validShots = 0, repeatedShots = 1
		results.add(new IGame.ShotResult(true, true, false, null));
		results.add(new IGame.ShotResult(false, false, false, null));
		results.add(new IGame.ShotResult(false, false, false, null));

		Move move1 = new Move(1, shots, results);
		String json1 = move1.processEnemyFire(true);
		assertTrue(json1.contains("\"repeatedShots\" : 1"));

		// 2 repetidos -> testa plural "repetidos"
		List<IGame.ShotResult> resultsMultiple = new ArrayList<>();
		resultsMultiple.add(new IGame.ShotResult(true, true, false, null));
		resultsMultiple.add(new IGame.ShotResult(true, true, false, null));
		Move move2 = new Move(2, shots, resultsMultiple);
		String json2 = move2.processEnemyFire(true);
		assertTrue(json2.contains("\"repeatedShots\" : 2"));
	}

	@Test
	@DisplayName("Test processEnemyFire with water misses (singular and plural)")
	void testProcessEnemyFireMisses() {
		List<IPosition> shots = new ArrayList<>();
		List<IGame.ShotResult> results = new ArrayList<>();

		// 1 tiro valido na agua, 2 tiros fora
		results.add(new IGame.ShotResult(true, false, false, null));
		Move move1 = new Move(1, shots, results);
		String json1 = move1.processEnemyFire(true);
		assertTrue(json1.contains("\"missedShots\" : 1"));

		// 2 tiros validos na agua
		List<IGame.ShotResult> results2 = new ArrayList<>();
		results2.add(new IGame.ShotResult(true, false, false, null));
		results2.add(new IGame.ShotResult(true, false, false, null));
		Move move2 = new Move(2, shots, results2);
		String json2 = move2.processEnemyFire(true);
		assertTrue(json2.contains("\"missedShots\" : 2"));
	}

	@Test
	@DisplayName("Test processEnemyFire with ship hits, sunk ships and outside shots in verbose mode")
	void testProcessEnemyFireHitsAndSunkWithVerbose() {
		List<IPosition> shots = new ArrayList<>();
		List<IGame.ShotResult> results = new ArrayList<>();

		// Criar barcos para teste
		IShip caravel = new Caravel(Compass.NORTH, new Position(0, 0));
		IShip barge1 = new Barge(new Position(5, 5));
		IShip barge2 = new Barge(new Position(7, 7));

		// 1 tiro acerta na caravela (nao afunda)
		results.add(new IGame.ShotResult(true, false, false, caravel));
		// 1 tiro afunda a barca 1
		results.add(new IGame.ShotResult(true, false, true, barge1));
		// 1 tiro afunda a barca 2 (testa plural de barcos afundados do mesmo tipo)
		results.add(new IGame.ShotResult(true, false, true, barge2));

		Move move = new Move(1, shots, results);
		String json = move.processEnemyFire(true);

		assertNotNull(json);
		assertTrue(json.contains("\"validShots\" : 3"));
		assertTrue(json.contains("\"missedShots\" : 0"));
		assertTrue(json.contains("sunkBoats"));
		assertTrue(json.contains("hitsOnBoats"));
	}

	@Test
	@DisplayName("Test processEnemyFire with combined valid shots, hits, misses and repeated shots")
	void testProcessEnemyFireCombinedVerboseBranches() {
		List<IPosition> shots = new ArrayList<>();
		List<IGame.ShotResult> results = new ArrayList<>();

		IShip caravel = new Caravel(Compass.NORTH, new Position(0, 0));

		// 1 tiro acerta na caravela
		results.add(new IGame.ShotResult(true, false, false, caravel));
		// 1 tiro na agua
		results.add(new IGame.ShotResult(true, false, false, null));
		// 1 tiro repetido
		results.add(new IGame.ShotResult(true, true, false, null));

		Move move = new Move(3, shots, results);
		String json = move.processEnemyFire(true);

		assertNotNull(json);
		assertTrue(json.contains("\"validShots\" : 2"));
		assertTrue(json.contains("\"repeatedShots\" : 1"));
		assertTrue(json.contains("\"missedShots\" : 1"));
	}

	@Test
	@DisplayName("Test processEnemyFire with multiple hits on the same boat without sinking")
	void testProcessEnemyFireMultipleHitsOnBoat() {
		List<IPosition> shots = new ArrayList<>();
		List<IGame.ShotResult> results = new ArrayList<>();

		IShip frigate = new Frigate(Compass.EAST, new Position(2, 2));

		// 2 tiros no mesmo barco (testa hits > 1)
		results.add(new IGame.ShotResult(true, false, false, frigate));
		results.add(new IGame.ShotResult(true, false, false, frigate));

		Move move = new Move(4, shots, results);
		String json = move.processEnemyFire(true);

		assertNotNull(json);
		assertTrue(json.contains("\"validShots\" : 2"));
		assertTrue(json.contains("Frigate") || json.contains("Fragata"));
	}

	@Test
	@DisplayName("Test processEnemyFire with outside shots output formatting")
	void testProcessEnemyFireOutsideShotsFormatting() {
		List<IPosition> shots = new ArrayList<>();
		List<IGame.ShotResult> results = new ArrayList<>();

		// 1 tiro valido na agua, restando 2 tiros exteriores (Game.NUMBER_SHOTS = 3)
		results.add(new IGame.ShotResult(true, false, false, null));

		Move move = new Move(5, shots, results);
		String json = move.processEnemyFire(true);
		assertTrue(json.contains("\"outsideShots\" : 2"));

		// 2 tiros validos na agua, restando 1 tiro exterior (singular)
		List<IGame.ShotResult> results2 = new ArrayList<>();
		results2.add(new IGame.ShotResult(true, false, false, null));
		results2.add(new IGame.ShotResult(true, false, false, null));

		Move move2 = new Move(6, shots, results2);
		String json2 = move2.processEnemyFire(true);
		assertTrue(json2.contains("\"outsideShots\" : 1"));
	}
}
