package battleship;

import org.junit.jupiter.api.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Fleet.
 */
@DisplayName("Testes Unitários da Classe Fleet")
public class FleetTest {

	private Fleet fleet;

	@BeforeEach
	void setUp() {
		fleet = new Fleet();
	}

	@AfterEach
	void tearDown() {
		fleet = null;
	}

	@Test
	@DisplayName("Construtor inicializa frota com lista de navios vazia")
	void testConstructor() {
		assertNotNull(fleet, "Error: Instance of Fleet should not be null.");
		assertTrue(fleet.getShips().isEmpty(), "Error: Fleet should be initialized with empty ships list.");
	}

	@Test
	@Timeout(value = 5, unit = TimeUnit.SECONDS)
	@DisplayName("createRandom gera uma frota completa e válida com 11 navios")
	void testCreateRandom() {
		IFleet r = Fleet.createRandom();
		assertNotNull(r, "Error: createRandom should return a non-null fleet instance.");
		assertEquals(11, r.getShips().size(), "Error: createRandom should create a fleet with 11 ships.");
	}

	@Test
	@DisplayName("addShip adiciona navio válido com sucesso")
	void testAddShipValid() {
		IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
		assertTrue(fleet.addShip(ship), "Error: Valid ship should be added successfully.");
		assertEquals(1, fleet.getShips().size(), "Error: Fleet should contain one ship after addition.");
	}

	@Test
	@DisplayName("addShip rejeita navio quando o limite da frota é ultrapassado")
	void testAddShipLimitReached() {
		int added = 0;
		for (int r = 0; r < Game.BOARD_SIZE; r += 2) {
			for (int c = 0; c < Game.BOARD_SIZE; c += 2) {
				if (fleet.addShip(new Barge(Compass.NORTH, new Position(r, c)))) {
					added++;
				}
			}
		}
		assertEquals(Fleet.FLEET_SIZE + 1, added);
		IShip extraShip = new Barge(Compass.NORTH, new Position(9, 9));
		assertFalse(fleet.addShip(extraShip), "Error: Should not add ship when fleet size limit is exceeded.");
	}

	@Test
	@DisplayName("addShip rejeita navio fora dos limites do tabuleiro")
	void testAddShipOutsideBoard() {
		IShip shipOutside = new Barge(Compass.NORTH, new Position(99, 99));
		assertFalse(fleet.addShip(shipOutside), "Error: Should not add ship outside the board.");
	}

	@Test
	@DisplayName("addShip rejeita navio em risco de colisão ou adjacente a outro")
	void testAddShipCollisionRisk() {
		IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
		IShip ship2 = new Barge(Compass.NORTH, new Position(1, 2)); // Adjacente
		fleet.addShip(ship1);
		assertFalse(fleet.addShip(ship2), "Error: Should not add ship with a collision risk.");
	}

	@Test
	@DisplayName("addShip lança AssertionError quando o navio é nulo")
	void testAddShipNullAssertion() {
		assertThrows(AssertionError.class, () -> fleet.addShip(null));
	}

	@Test
	@DisplayName("getShips devolve a lista de navios da frota")
	void testGetShips() {
		assertTrue(fleet.getShips().isEmpty(), "Error: Fleet's ships list should initially be empty.");
		IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
		fleet.addShip(ship);
		assertEquals(1, fleet.getShips().size(), "Error: Fleet should have size 1 after adding a ship.");
		assertEquals(ship, fleet.getShips().get(0), "Error: Fleet's first ship should match the added ship.");
	}

	@Test
	@DisplayName("getShipsLike filtra corretamente os navios pela categoria")
	void testGetShipsLike() {
		IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
		IShip ship2 = new Caravel(Compass.NORTH, new Position(4, 4));
		fleet.addShip(ship1);
		fleet.addShip(ship2);

		List<IShip> barges = fleet.getShipsLike("Barca");
		assertEquals(1, barges.size(), "Error: There should be exactly one ship of category 'Barca'.");
		assertEquals(ship1, barges.get(0), "Error: The ship of category 'Barca' does not match.");
		assertTrue(fleet.getShipsLike("Galeao").isEmpty());
		assertThrows(AssertionError.class, () -> fleet.getShipsLike(null));
	}

	@Test
	@DisplayName("getFloatingShips devolve apenas os navios que ainda flutuam")
	void testGetFloatingShips() {
		IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
		IShip ship2 = new Caravel(Compass.NORTH, new Position(4, 4));
		fleet.addShip(ship1);
		fleet.addShip(ship2);

		List<IShip> floatingShips = fleet.getFloatingShips();
		assertEquals(2, floatingShips.size(), "Error: All ships should be floating initially.");

		ship1.sink();
		floatingShips = fleet.getFloatingShips();
		assertEquals(1, floatingShips.size(), "Error: Only one ship should be floating after sinking one.");
		assertEquals(ship2, floatingShips.get(0), "Error: The floating ship should match the expected result.");
	}

	@Test
	@DisplayName("getSunkShips devolve apenas os navios já afundados")
	void testGetSunkShips() {
		IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
		IShip ship2 = new Caravel(Compass.NORTH, new Position(4, 4));
		fleet.addShip(ship1);
		fleet.addShip(ship2);

		assertTrue(fleet.getSunkShips().isEmpty(), "Error: No ships should be sunk initially.");

		ship1.sink();
		List<IShip> sunkShips = fleet.getSunkShips();
		assertEquals(1, sunkShips.size(), "Error: Only one ship should be sunk.");
		assertEquals(ship1, sunkShips.get(0), "Error: The sunk ship should match ship1.");
	}

	@Test
	@DisplayName("shipAt devolve o navio na posição indicada ou null se estiver vazia")
	void testShipAt() {
		IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
		fleet.addShip(ship);

		assertEquals(ship, fleet.shipAt(new Position(1, 1)), "Error: Should return the correct ship at the position.");
		assertNull(fleet.shipAt(new Position(5, 5)), "Error: Should return null for empty positions in the fleet.");
		assertThrows(AssertionError.class, () -> fleet.shipAt(null));
	}

	@Test
	@DisplayName("colisionRisk deteta risco de colisão e assertiva de parâmetro nulo")
	void testColisionRisk() throws Exception {
		var method = Fleet.class.getDeclaredMethod("colisionRisk", IShip.class);
		method.setAccessible(true);

		IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
		IShip ship2 = new Barge(Compass.NORTH, new Position(1, 1));
		fleet.addShip(ship1);

		assertTrue((Boolean) method.invoke(fleet, ship2), "Error: Overlapping ships should be at collision risk.");
		assertFalse((Boolean) method.invoke(fleet, new Barge(Compass.NORTH, new Position(5, 5))),
				"Error: Ships at non-overlapping positions should not have a collision risk.");
		assertThrows(Exception.class, () -> method.invoke(fleet, (Object) null));
	}

	@Test
	@DisplayName("Métodos de impressão da frota executam sem erros e validam pré-condições")
	void testPrintMethods() {
		IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
		IShip ship2 = new Caravel(Compass.NORTH, new Position(4, 4));
		fleet.addShip(ship1);
		fleet.addShip(ship2);
		ship2.sink();

		assertDoesNotThrow(fleet::printStatus);
		assertDoesNotThrow(fleet::printAllShips);
		assertDoesNotThrow(fleet::printFloatingShips);
		assertDoesNotThrow(() -> fleet.printShipsByCategory("Barca"));
		assertDoesNotThrow(() -> fleet.printShips(fleet.getShips()));

		assertThrows(AssertionError.class, () -> fleet.printShips(null));
		assertThrows(AssertionError.class, () -> fleet.printShipsByCategory(null));
	}
}