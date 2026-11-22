package battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Tasks class with comprehensive coverage.
 * Author: 106363
 */
public class TasksTest {

	@Test
	@DisplayName("Test menuHelp prints help menu without exception")
	void testMenuHelp() {
		assertDoesNotThrow(() -> Tasks.menuHelp());
	}

	@Test
	@DisplayName("Test readPosition parses row and column integers correctly")
	void testReadPosition() {
		String input = "3 4";
		Scanner scanner = new Scanner(input);
		Position pos = Tasks.readPosition(scanner);

		assertNotNull(pos);
		assertEquals(3, pos.getRow());
		assertEquals(4, pos.getColumn());
	}

	@Test
	@DisplayName("Test readClassicPosition with compact notation (e.g. B5)")
	void testReadClassicPositionCompact() {
		String input = "B5";
		Scanner scanner = new Scanner(input);
		IPosition pos = Tasks.readClassicPosition(scanner);

		assertNotNull(pos);
		assertEquals(1, pos.getRow()); // 'B' -> row 1
		assertEquals(4, pos.getColumn()); // 5 -> column 4
	}

	@Test
	@DisplayName("Test readClassicPosition with lowercase compact notation (e.g. c2)")
	void testReadClassicPositionLowercase() {
		String input = "c2";
		Scanner scanner = new Scanner(input);
		IPosition pos = Tasks.readClassicPosition(scanner);

		assertNotNull(pos);
		assertEquals(2, pos.getRow()); // 'C' -> row 2
		assertEquals(1, pos.getColumn()); // 2 -> column 1
	}

	@Test
	@DisplayName("Test readClassicPosition with separated notation (e.g. D 7)")
	void testReadClassicPositionSeparated() {
		String input = "D 7";
		Scanner scanner = new Scanner(input);
		IPosition pos = Tasks.readClassicPosition(scanner);

		assertNotNull(pos);
		assertEquals(3, pos.getRow()); // 'D' -> row 3
		assertEquals(6, pos.getColumn()); // 7 -> column 6
	}

	@Test
	@DisplayName("Test readClassicPosition with empty input throws IllegalArgumentException")
	void testReadClassicPositionEmpty() {
		Scanner scanner = new Scanner("");
		assertThrows(IllegalArgumentException.class, () -> Tasks.readClassicPosition(scanner));
	}

	@Test
	@DisplayName("Test readClassicPosition with invalid format throws IllegalArgumentException")
	void testReadClassicPositionInvalid() {
		Scanner scanner = new Scanner("999");
		assertThrows(IllegalArgumentException.class, () -> Tasks.readClassicPosition(scanner));

		Scanner scanner2 = new Scanner("INVALID_COORDINATE");
		assertThrows(IllegalArgumentException.class, () -> Tasks.readClassicPosition(scanner2));
	}

	@Test
	@DisplayName("Test readShip creates expected ship instance from input stream")
	void testReadShip() {
		// "Fragata 2 3 N"
		String input = "Fragata 2 3 N";
		Scanner scanner = new Scanner(input);
		Ship ship = Tasks.readShip(scanner);

		assertNotNull(ship);
		assertEquals("Fragata", ship.getCategory());
		assertEquals(Compass.NORTH, ship.getBearing());
		assertEquals(2, ship.getPosition().getRow());
		assertEquals(3, ship.getPosition().getColumn());
	}

	@Test
	@DisplayName("Test menu execution with immediate desist command")
	void testMenuDesistir() {
		String simulatedInput = "desisto\n";
		InputStream originalIn = System.in;
		try {
			System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
			assertDoesNotThrow(() -> Tasks.menu());
		} finally {
			System.setIn(originalIn);
		}
	}

	@Test
	@DisplayName("Test menu execution with help and invalid commands followed by desist")
	void testMenuHelpAndUnknown() {
		String simulatedInput = "ajuda\ncomando_desconhecido\ndesisto\n";
		InputStream originalIn = System.in;
		try {
			System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
			assertDoesNotThrow(() -> Tasks.menu());
		} finally {
			System.setIn(originalIn);
		}
	}
}
