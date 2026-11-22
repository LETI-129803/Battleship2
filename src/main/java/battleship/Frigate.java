package battleship;

import java.util.Objects;

/**
 * The type Frigate represents a ship with a size of 4 units.
 * It is positioned on the game board based on its bearing and initial position.
 *
 * Author: britoeabreu
 * Date: 2023-10-10
 * Time: 15:30
 */
public class Frigate extends Ship {

	private static final String SHIP_NAME = "Fragata";
	private static final int FRIGATE_SIZE = 4;

	/**
	 * Instantiates a new Frigate.
	 *
	 * @param bearing The bearing of the ship (NORTH, SOUTH, EAST, or WEST).
	 * @param pos     The initial position of the ship on the game board.
	 */
	public Frigate(Compass bearing, IPosition pos) {
		super(SHIP_NAME, bearing, pos, FRIGATE_SIZE);
		Objects.requireNonNull(bearing, "Bearing cannot be null");

		switch (bearing) {
			case NORTH, SOUTH -> {
				for (int r = 0; r < getSize(); r++) {
					getPositions().add(new Position(pos.getRow() + r, pos.getColumn()));
				}
			}
			case EAST, WEST -> {
				for (int c = 0; c < getSize(); c++) {
					getPositions().add(new Position(pos.getRow(), pos.getColumn() + c));
				}
			}
			default -> throw new IllegalArgumentException("Invalid bearing for Frigate: " + bearing);
		}
	}
}