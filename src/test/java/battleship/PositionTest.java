package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Position with maximum branch and line coverage (100%).
 * Author: 106363
 */
public class PositionTest {

	private Position position;

	@BeforeEach
	void setUp() {
		position = new Position(2, 3);
	}

	@AfterEach
	void tearDown() {
		position = null;
	}

	@Test
	@DisplayName("Test numeric coordinates constructor and initial states")
	void constructorWithCoordinates() {
		Position pos = new Position(1, 1);
		assertNotNull(pos, "Failed to create Position: object is null");
		assertEquals(1, pos.getRow(), "Failed to set row: expected 1 but got " + pos.getRow());
		assertEquals(1, pos.getColumn(), "Failed to set column: expected 1 but got " + pos.getColumn());
		assertFalse(pos.isOccupied(), "New position should not be occupied");
		assertFalse(pos.isHit(), "New position should not be hit");
	}

	@Test
	@DisplayName("Test classic alphanumeric coordinates constructor")
	void constructorWithClassicCoordinates() {
		Position pos = new Position('c', 4);
		assertEquals(2, pos.getRow(), "Classic row 'c' should map to index 2");
		assertEquals(3, pos.getColumn(), "Classic column 4 should map to index 3");
		assertFalse(pos.isOccupied(), "New position should not be occupied");
		assertFalse(pos.isHit(), "New position should not be hit");

		Position posA = new Position('A', 1);
		assertEquals(0, posA.getRow());
		assertEquals(0, posA.getColumn());
	}

	@Test
	@DisplayName("Test randomPosition factory creates valid inside position")
	void testRandomPosition() {
		for (int i = 0; i < 20; i++) {
			Position randPos = Position.randomPosition();
			assertNotNull(randPos);
			assertTrue(randPos.isInside(), "Random position must be inside the board");
		}
	}

	@Test
	@DisplayName("Test getRow returns expected row index")
	void getRow() {
		assertEquals(2, position.getRow(), "Failed to get row: expected 2 but got " + position.getRow());
	}

	@Test
	@DisplayName("Test getColumn returns expected column index")
	void getColumn() {
		assertEquals(3, position.getColumn(), "Failed to get column: expected 3 but got " + position.getColumn());
	}

	@Test
	@DisplayName("Test getClassicRow converts row index to traditional letter [A-J]")
	void getClassicRow() {
		assertEquals('C', position.getClassicRow(), "Index 2 should map to classic letter 'C'");
		Position posA = new Position(0, 0);
		assertEquals('A', posA.getClassicRow(), "Index 0 should map to classic letter 'A'");
		Position posJ = new Position(9, 9);
		assertEquals('J', posJ.getClassicRow(), "Index 9 should map to classic letter 'J'");
	}

	@Test
	@DisplayName("Test getClassicColumn converts column index to traditional 1-based number")
	void getClassicColumn() {
		assertEquals(4, position.getClassicColumn(), "Index 3 should map to classic column 4");
		Position pos1 = new Position(0, 0);
		assertEquals(1, pos1.getClassicColumn(), "Index 0 should map to classic column 1");
		Position pos10 = new Position(9, 9);
		assertEquals(10, pos10.getClassicColumn(), "Index 9 should map to classic column 10");
	}

	@Test
	@DisplayName("Test isInside for valid top-left boundary position")
	void isInsideTopLeftCorner() {
		position = new Position(0, 0);
		assertTrue(position.isInside(), "Position (0,0) should be inside");
	}

	@Test
	@DisplayName("Test isInside for valid bottom-right boundary position")
	void isInsideBottomRightCorner() {
		position = new Position(Game.BOARD_SIZE - 1, Game.BOARD_SIZE - 1);
		assertTrue(position.isInside(), "Position (9,9) should be inside");
	}

	@Test
	@DisplayName("Test isInside returns false when row is negative")
	void isInsideNegativeRow() {
		position = new Position(-1, 5);
		assertFalse(position.isInside(), "Position with negative row should not be inside");
	}

	@Test
	@DisplayName("Test isInside returns false when column is negative")
	void isInsideNegativeColumn() {
		position = new Position(5, -1);
		assertFalse(position.isInside(), "Position with negative column should not be inside");
	}

	@Test
	@DisplayName("Test isInside returns false when row is equal to or greater than BOARD_SIZE")
	void isInsideRowBeyondBoard() {
		position = new Position(Game.BOARD_SIZE, 5);
		assertFalse(position.isInside(), "Position with row == BOARD_SIZE should not be inside");
		position = new Position(Game.BOARD_SIZE + 2, 5);
		assertFalse(position.isInside(), "Position with row > BOARD_SIZE should not be inside");
	}

	@Test
	@DisplayName("Test isInside returns false when column is equal to or greater than BOARD_SIZE")
	void isInsideColumnBeyondBoard() {
		position = new Position(5, Game.BOARD_SIZE);
		assertFalse(position.isInside(), "Position with column == BOARD_SIZE should not be inside");
		position = new Position(5, Game.BOARD_SIZE + 3);
		assertFalse(position.isInside(), "Position with column > BOARD_SIZE should not be inside");
	}

	@Test
	@DisplayName("Test isAdjacentTo returns true for horizontal adjacency")
	void isAdjacentToHorizontal() {
		Position right = new Position(2, 4);
		Position left = new Position(2, 2);
		assertTrue(position.isAdjacentTo(right), "Failed to detect horizontally right adjacent position");
		assertTrue(position.isAdjacentTo(left), "Failed to detect horizontally left adjacent position");
	}

	@Test
	@DisplayName("Test isAdjacentTo returns true for vertical adjacency")
	void isAdjacentToVertical() {
		Position down = new Position(3, 3);
		Position up = new Position(1, 3);
		assertTrue(position.isAdjacentTo(down), "Failed to detect vertically down adjacent position");
		assertTrue(position.isAdjacentTo(up), "Failed to detect vertically up adjacent position");
	}

	@Test
	@DisplayName("Test isAdjacentTo returns true for diagonal adjacency")
	void isAdjacentToDiagonal() {
		Position downRight = new Position(3, 4);
		Position downLeft = new Position(3, 2);
		Position upRight = new Position(1, 4);
		Position upLeft = new Position(1, 2);
		assertTrue(position.isAdjacentTo(downRight), "Failed to detect diagonal down-right adjacent position");
		assertTrue(position.isAdjacentTo(downLeft), "Failed to detect diagonal down-left adjacent position");
		assertTrue(position.isAdjacentTo(upRight), "Failed to detect diagonal up-right adjacent position");
		assertTrue(position.isAdjacentTo(upLeft), "Failed to detect diagonal up-left adjacent position");
	}

	@Test
	@DisplayName("Test isAdjacentTo returns false for non-adjacent positions")
	void isAdjacentToNonAdjacent() {
		Position distantRow = new Position(5, 3);
		Position distantCol = new Position(2, 6);
		Position distantBoth = new Position(5, 6);
		assertFalse(position.isAdjacentTo(distantRow), "Distant row position incorrectly identified as adjacent");
		assertFalse(position.isAdjacentTo(distantCol), "Distant column position incorrectly identified as adjacent");
		assertFalse(position.isAdjacentTo(distantBoth), "Distant both position incorrectly identified as adjacent");
	}

	@Test
	@DisplayName("Test isAdjacentTo throws NullPointerException when parameter is null")
	void isAdjacentToWithNull() {
		assertThrows(NullPointerException.class, () -> position.isAdjacentTo(null),
				"isAdjacentTo should throw NullPointerException for null input");
	}

	@Test
	@DisplayName("Test adjacentPositions returns all 8 surrounding positions for center cell")
	void testAdjacentPositionsCenter() {
		Position center = new Position(5, 5);
		List<IPosition> adj = center.adjacentPositions();
		assertEquals(8, adj.size(), "Center position should have exactly 8 adjacent positions inside board");
		assertTrue(adj.contains(new Position(4, 5))); // North
		assertTrue(adj.contains(new Position(5, 6))); // East
		assertTrue(adj.contains(new Position(6, 5))); // South
		assertTrue(adj.contains(new Position(5, 4))); // West
		assertTrue(adj.contains(new Position(6, 6))); // Southeast
		assertTrue(adj.contains(new Position(6, 4))); // Southwest
		assertTrue(adj.contains(new Position(4, 6))); // Northeast
		assertTrue(adj.contains(new Position(4, 4))); // Northwest
	}

	@Test
	@DisplayName("Test adjacentPositions filters out out-of-bounds positions on board corners")
	void testAdjacentPositionsCorners() {
		// Top-left corner (0,0) -> only (0,1), (1,0), (1,1) are inside
		Position corner00 = new Position(0, 0);
		List<IPosition> adj00 = corner00.adjacentPositions();
		assertEquals(3, adj00.size(), "Corner (0,0) should have exactly 3 valid adjacent positions");
		for (IPosition pos : adj00) {
			assertTrue(pos.isInside(), "All returned positions must be inside the board");
		}

		// Bottom-right corner (9,9)
		Position corner99 = new Position(Game.BOARD_SIZE - 1, Game.BOARD_SIZE - 1);
		List<IPosition> adj99 = corner99.adjacentPositions();
		assertEquals(3, adj99.size(), "Corner (9,9) should have exactly 3 valid adjacent positions");
	}

	@Test
	@DisplayName("Test occupy and isOccupied behavior")
	void isOccupied() {
		assertFalse(position.isOccupied(), "New position should not be occupied");
		position.occupy();
		assertTrue(position.isOccupied(), "Position should be occupied after occupy()");
	}

	@Test
	@DisplayName("Test shoot and isHit behavior")
	void isHit() {
		assertFalse(position.isHit(), "New position should not be hit");
		position.shoot();
		assertTrue(position.isHit(), "Position should be hit after shoot()");
	}

	@Test
	@DisplayName("Test equals with same instance and identical values")
	void testEqualsSameAndIdentical() {
		assertTrue(position.equals(position), "A position should be equal to itself");
		Position sameValues = new Position(2, 3);
		assertTrue(position.equals(sameValues), "Equal positions should be identified as equal");
	}

	@Test
	@DisplayName("Test equals returns false for null and different types")
	void testEqualsNullAndDifferentType() {
		assertFalse(position.equals(null), "Position should not equal null");
		assertFalse(position.equals("A string"), "Position should not equal an object of different type");
	}

	@Test
	@DisplayName("Test equals returns false for different rows or columns")
	void testEqualsDifferentCoordinates() {
		Position diffCol = new Position(2, 4);
		assertFalse(position.equals(diffCol), "Positions with different column should not be equal");

		Position diffRow = new Position(3, 3);
		assertFalse(position.equals(diffRow), "Positions with different row should not be equal");

		Position diffBoth = new Position(4, 5);
		assertFalse(position.equals(diffBoth), "Positions with different row and column should not be equal");
	}

	@Test
	@DisplayName("Test hashCode consistency and behavior when state changes")
	void testHashCode() {
		Position same = new Position(2, 3);
		assertEquals(position.hashCode(), same.hashCode(), "Hash codes must be equal for identical positions");

		Position modifiedOccupied = new Position(2, 3);
		modifiedOccupied.occupy();
		assertNotEquals(position.hashCode(), modifiedOccupied.hashCode(),
				"Hash code should change when occupation state changes");

		Position modifiedHit = new Position(2, 3);
		modifiedHit.shoot();
		assertNotEquals(position.hashCode(), modifiedHit.hashCode(),
				"Hash code should change when hit state changes");
	}

	@Test
	@DisplayName("Test toString returns traditional grid notation (e.g., C4)")
	void toStringFormat() {
		String expected = "C4";
		assertEquals(expected, position.toString(),
				"Incorrect string representation: expected '" + expected + "' but got '" + position.toString() + "'");

		Position posA1 = new Position(0, 0);
		assertEquals("A1", posA1.toString());

		Position posJ10 = new Position(9, 9);
		assertEquals("J10", posJ10.toString());
	}
}
