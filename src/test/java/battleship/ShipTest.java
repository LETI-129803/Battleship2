package battleship;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Ship.
 */
@DisplayName("Testes Unitários da Classe Ship")
public class ShipTest {

    private Ship ship, shipBorder;

    @BeforeEach
    void setUp() {
        ship = new Barge(Compass.NORTH, new Position(5, 5));
        shipBorder = new Barge(Compass.NORTH, new Position(0, 0));
    }

    @AfterEach
    void tearDown() {
        ship = null;
        shipBorder = null;
    }

    @Test
    @DisplayName("Construtor inicializa corretamente atributos do navio")
    void testConstructor() {
        assertNotNull(ship, "Error: Ship instance should not be null.");
        assertEquals("Barca", ship.getCategory(), "Error: Ship category is incorrect.");
        assertEquals(Compass.NORTH, ship.getBearing(), "Error: Ship bearing is incorrect.");
        assertEquals(new Position(5, 5), ship.getPosition(), "Error: Ship initial position is incorrect.");
        assertEquals(1, ship.getSize(), "Error: Ship size is incorrect.");
        assertFalse(ship.getPositions().isEmpty(), "Error: Ship positions should not be empty.");
    }

    @Test
    @DisplayName("Construtor lança NullPointerException quando argumentos obrigatórios são nulos")
    void testConstructorNullArguments() {
        assertThrows(NullPointerException.class, () -> new Barge(null, new Position(0, 0)));
        assertThrows(NullPointerException.class, () -> new Barge(Compass.NORTH, null));
        assertThrows(NullPointerException.class, () -> new Ship(null, Compass.NORTH, new Position(0, 0), 1) {});
    }

    @Test
    @DisplayName("buildShip cria corretamente todos os tipos de navios e devolve null para tipo inválido")
    void testBuildShip() {
        Position pos = new Position(2, 2);
        assertTrue(Ship.buildShip("barca", Compass.NORTH, pos) instanceof Barge);
        assertTrue(Ship.buildShip("caravela", Compass.NORTH, pos) instanceof Caravel);
        assertTrue(Ship.buildShip("nau", Compass.NORTH, pos) instanceof Carrack);
        assertTrue(Ship.buildShip("fragata", Compass.NORTH, pos) instanceof Frigate);
        assertTrue(Ship.buildShip("galeao", Compass.NORTH, pos) instanceof Galleon);
        assertNull(Ship.buildShip("submarino", Compass.NORTH, pos));
    }

    @Test
    @DisplayName("buildShip lança AssertionError quando parâmetros são nulos")
    void testBuildShipAssertions() {
        Position pos = new Position(2, 2);
        assertThrows(AssertionError.class, () -> Ship.buildShip(null, Compass.NORTH, pos));
        assertThrows(AssertionError.class, () -> Ship.buildShip("barca", null, pos));
        assertThrows(AssertionError.class, () -> Ship.buildShip("barca", Compass.NORTH, null));
    }

    @Test
    @DisplayName("getCategory devolve a categoria do navio")
    void testGetCategory() {
        assertEquals("Barca", ship.getCategory(), "Error: Ship category should be 'Barca'.");
    }

    @Test
    @DisplayName("getPosition devolve a posição inicial do navio")
    void testGetPosition() {
        assertEquals(new Position(5, 5), ship.getPosition(), "Error: Ship position should be (5, 5).");
    }

    @Test
    @DisplayName("getSize devolve o tamanho do navio")
    void testGetSize() {
        assertEquals(1, ship.getSize(), "Error: Ship size should be 1.");
    }

    @Test
    @DisplayName("getBearing devolve a orientação do navio")
    void testGetBearing() {
        assertEquals(Compass.NORTH, ship.getBearing(), "Error: Ship bearing should be NORTH.");
    }

    @Test
    @DisplayName("getPositions devolve a lista de posições ocupadas pelo navio")
    void testGetPositions() {
        List<IPosition> positions = ship.getPositions();
        assertNotNull(positions, "Error: Ship positions should not be null.");
        assertEquals(1, positions.size(), "Error: Ship should have exactly one position.");
        assertEquals(5, positions.get(0).getRow(), "Error: Position's row should be 5.");
        assertEquals(5, positions.get(0).getColumn(), "Error: Position's column should be 5.");
    }

    @Test
    @DisplayName("stillFloating devolve true quando o navio está intacto")
    void testStillFloating1() {
        assertTrue(ship.stillFloating(), "Error: Ship should still be floating.");
    }

    @Test
    @DisplayName("stillFloating devolve false quando todas as posições foram atingidas")
    void testStillFloating2() {
        ship.getPositions().get(0).shoot();
        assertFalse(ship.stillFloating(), "Error: Ship should no longer be floating after being hit.");
    }

    @Test
    @DisplayName("stillFloating percorre várias posições quando a primeira já foi atingida")
    void testStillFloatingPartialHit() {
        Ship multiPosShip = new Frigate(Compass.NORTH, new Position(2, 2));
        multiPosShip.getPositions().get(0).shoot();
        assertTrue(multiPosShip.stillFloating(), "Error: Ship should still float if only one of four positions is hit.");
    }

    @Test
    @DisplayName("sink afunda o navio atingindo todas as suas posições")
    void testSink() {
        Ship multiPosShip = new Frigate(Compass.EAST, new Position(2, 2));
        assertTrue(multiPosShip.stillFloating());
        multiPosShip.sink();
        assertFalse(multiPosShip.stillFloating(), "Error: Ship should not be floating after sink().");
    }

    @Test
    @DisplayName("shoot marca a posição como atingida quando o tiro acerta no navio")
    void testShoot1() {
        Position target = new Position(5, 5);
        ship.shoot(target);
        assertTrue(ship.getPositions().get(0).isHit(), "Error: Position should be marked as hit.");
    }

    @Test
    @DisplayName("shoot não altera o navio quando o tiro falha")
    void testShoot2() {
        Position target = new Position(0, 0);
        ship.shoot(target);
        assertFalse(ship.getPositions().get(0).isHit(), "Error: Position should not be marked as hit for an invalid target.");
    }

    @Test
    @DisplayName("shoot lança AssertionError para posição nula ou fora do tabuleiro")
    void testShootAssertions() {
        assertThrows(AssertionError.class, () -> ship.shoot(null));
        assertThrows(AssertionError.class, () -> ship.shoot(new Position(-1, 0)));
    }

    @Test
    @DisplayName("occupies devolve true quando a posição pertence ao navio")
    void testOccupies1() {
        Position pos = new Position(5, 5);
        assertTrue(ship.occupies(pos), "Error: Ship should occupy position (5, 5).");
    }

    @Test
    @DisplayName("occupies devolve false quando a posição não pertence ao navio")
    void testOccupies2() {
        Position pos = new Position(1, 1);
        assertFalse(ship.occupies(pos), "Error: Ship should not occupy position (1, 1).");
    }

    @Test
    @DisplayName("occupies lança AssertionError quando a posição é nula")
    void testOccupiesAssertion() {
        assertThrows(AssertionError.class, () -> ship.occupies(null));
    }

    @Test
    @DisplayName("tooCloseTo(IShip) devolve true quando os navios são adjacentes")
    void testTooCloseToShip1() {
        Ship nearbyShip = new Barge(Compass.NORTH, new Position(5, 6));
        assertTrue(ship.tooCloseTo(nearbyShip), "Error: Ships should be too close.");
    }

    @Test
    @DisplayName("tooCloseTo(IShip) devolve false quando os navios estão afastados")
    void testTooCloseToShip2() {
        Ship farShip = new Barge(Compass.NORTH, new Position(9, 9));
        assertFalse(ship.tooCloseTo(farShip), "Error: Ships should not be too close.");
    }

    @Test
    @DisplayName("tooCloseTo(IShip) lança AssertionError quando o outro navio é nulo")
    void testTooCloseToShipAssertion() {
        assertThrows(AssertionError.class, () -> ship.tooCloseTo((IShip) null));
    }

    @Test
    @DisplayName("tooCloseTo(IPosition) devolve true quando a posição é adjacente ao navio")
    void testTooCloseToPosition1() {
        Position pos = new Position(5, 6);
        assertTrue(ship.tooCloseTo(pos), "Error: Ship should be too close to the given position.");
    }

    @Test
    @DisplayName("tooCloseTo(IPosition) devolve false quando a posição não é adjacente")
    void testTooCloseToPosition2() {
        Position pos = new Position(7, 7);
        assertFalse(ship.tooCloseTo(pos), "Error: Ship should not be too close to the given position.");
    }

    @Test
    @DisplayName("tooCloseTo(IPosition) lança AssertionError quando a posição é nula")
    void testTooCloseToPositionAssertion() {
        assertThrows(AssertionError.class, () -> ship.tooCloseTo((IPosition) null));
    }

    @Test
    @DisplayName("getTopMostPos, getBottomMostPos, getLeftMostPos e getRightMostPos calculam extremos corretamente")
    void testExtremePositions() {
        Ship customShip = new Ship("Custom", Compass.NORTH, new Position(5, 5), 3) {};
        customShip.getPositions().add(new Position(5, 5));
        customShip.getPositions().add(new Position(3, 7)); // menor linha (3), maior coluna (7)
        customShip.getPositions().add(new Position(8, 2)); // maior linha (8), menor coluna (2)

        assertEquals(3, customShip.getTopMostPos(), "Error: The topmost position should be 3.");
        assertEquals(8, customShip.getBottomMostPos(), "Error: The bottommost position should be 8.");
        assertEquals(2, customShip.getLeftMostPos(), "Error: The leftmost position should be 2.");
        assertEquals(7, customShip.getRightMostPos(), "Error: The rightmost position should be 7.");
    }

    @Test
    @DisplayName("getAdjacentPositions devolve posições vizinhas sem duplicados e sem posições do próprio navio")
    void testGetAdjacentPositions() {
        assertEquals(8, ship.getAdjacentPositions().size(), "Error: Adjacent positions in middle of board should be 8.");
        assertEquals(3, shipBorder.getAdjacentPositions().size(), "Error: Adjacent positions at (0,0) should be 3.");

        Ship horizontalShip = new Frigate(Compass.EAST, new Position(3, 3));
        assertEquals(14, horizontalShip.getAdjacentPositions().size(), "Error: Adjacent positions for 4-unit ship should be 14.");
    }

    @Test
    @DisplayName("isInsideBoard valida corretamente navios dentro, fora e sem posições")
    void testIsInsideBoard() {
        // Navio sem posições
        Ship emptyShip = new Ship("Empty", Compass.NORTH, new Position(0, 0), 0) {};
        assertFalse(emptyShip.isInsideBoard(), "Error: Ship with empty positions should not be inside board.");

        // Navio totalmente dentro do tabuleiro
        assertTrue(ship.isInsideBoard(), "Error: Ship at (5,5) should be inside board.");

        // Fora à esquerda (coluna < 0)
        Ship outLeft = new Barge(Compass.NORTH, new Position(5, -1));
        assertFalse(outLeft.isInsideBoard(), "Error: Ship with column < 0 should not be inside board.");

        // Fora à direita (coluna >= BOARD_SIZE)
        Ship outRight = new Barge(Compass.NORTH, new Position(5, Game.BOARD_SIZE));
        assertFalse(outRight.isInsideBoard(), "Error: Ship with column >= BOARD_SIZE should not be inside board.");

        // Fora em cima (linha < 0)
        Ship outTop = new Barge(Compass.NORTH, new Position(-1, 5));
        assertFalse(outTop.isInsideBoard(), "Error: Ship with row < 0 should not be inside board.");

        // Fora em baixo (linha >= BOARD_SIZE)
        Ship outBottom = new Barge(Compass.NORTH, new Position(Game.BOARD_SIZE, 5));
        assertFalse(outBottom.isInsideBoard(), "Error: Ship with row >= BOARD_SIZE should not be inside board.");
    }

    @Test
    @DisplayName("toString devolve representação textual formatada do navio")
    void testToString() {
        assertEquals("[Barca NORTH (5, 5)]", ship.toString(), "Error: toString format is incorrect.");
    }
}