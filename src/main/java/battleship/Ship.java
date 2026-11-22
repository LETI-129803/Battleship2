package battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type Ship.
 */
public abstract class Ship implements IShip {

	private static final String GALEAO = "galeao";
	private static final String FRAGATA = "fragata";
	private static final String NAU = "nau";
	private static final String CARAVELA = "caravela";
	private static final String BARCA = "barca";

	/**
	 * Create a new ship.
	 *
	 * @param shipKind the ship kind
	 * @param bearing  the bearing
	 * @param pos      the pos
	 * @return s ship
	 */
	static Ship buildShip(String shipKind, Compass bearing, Position pos) {
		assert shipKind != null;
		assert bearing != null;
		assert pos != null;

		return switch (shipKind) {
			case BARCA -> new Barge(bearing, pos);
			case CARAVELA -> new Caravel(bearing, pos);
			case NAU -> new Carrack(bearing, pos);
			case FRAGATA -> new Frigate(bearing, pos);
			case GALEAO -> new Galleon(bearing, pos);
			default -> null;
		};
	}

	private final String category;
	private final Compass bearing;
	private final IPosition pos;
	private final Integer size;

	/**
	 * The Positions occupied by the ship.
	 */
	protected List<IPosition> positions;

	/**
	 * Create ships.
	 *
	 * @param category The category of ships of interest
	 * @param bearing  The bearing of ships of interest
	 * @param pos      The position of ships of interest
	 * @param size     The number of positions occupied by the ship
	 */
	public Ship(String category, Compass bearing, IPosition pos, int size) {
		this.category = Objects.requireNonNull(category, "Ship's category must not be null");
		this.bearing = Objects.requireNonNull(bearing, "Ship's bearing must not be null");
		this.pos = Objects.requireNonNull(pos, "Ship's position must not be null");
		this.size = size;
		this.positions = new ArrayList<>();
	}

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public List<IPosition> getPositions() {
		return positions;
	}

	@Override
	public List<IPosition> getAdjacentPositions() {
		List<IPosition> adjacentPositions = new ArrayList<>();
		for (IPosition position : getPositions()) {
			for (IPosition adj : position.adjacentPositions()) {
				if (!getPositions().contains(adj) && !adjacentPositions.contains(adj)) {
					adjacentPositions.add(adj);
				}
			}
		}
		return adjacentPositions;
	}

	@Override
	public IPosition getPosition() {
		return pos;
	}

	@Override
	public Compass getBearing() {
		return bearing;
	}

	@Override
	public Integer getSize() {
		return size;
	}

	@Override
	public boolean stillFloating() {
		return getPositions().stream().anyMatch(position -> !position.isHit());
	}

	@Override
	public int getTopMostPos() {
		return getPositions().stream()
				.mapToInt(IPosition::getRow)
				.min()
				.orElseThrow();
	}

	@Override
	public int getBottomMostPos() {
		return getPositions().stream()
				.mapToInt(IPosition::getRow)
				.max()
				.orElseThrow();
	}

	@Override
	public int getLeftMostPos() {
		return getPositions().stream()
				.mapToInt(IPosition::getColumn)
				.min()
				.orElseThrow();
	}

	@Override
	public int getRightMostPos() {
		return getPositions().stream()
				.mapToInt(IPosition::getColumn)
				.max()
				.orElseThrow();
	}

	@Override
	public boolean occupies(IPosition pos) {
		assert pos != null;
		return getPositions().contains(pos);
	}

	@Override
	public boolean tooCloseTo(IShip other) {
		assert other != null;
		return other.getPositions().stream().anyMatch(this::tooCloseTo);
	}

	@Override
	public boolean tooCloseTo(IPosition pos) {
		assert pos != null;
		return getPositions().stream().anyMatch(position -> position.isAdjacentTo(pos));
	}

	@Override
	public boolean isInsideBoard() {
		if (getPositions().isEmpty()) {
			return false;
		}

		return getLeftMostPos() >= 0
				&& getRightMostPos() <= Game.BOARD_SIZE - 1
				&& getTopMostPos() >= 0
				&& getBottomMostPos() <= Game.BOARD_SIZE - 1;
	}

	@Override
	public void shoot(IPosition pos) {
		assert pos != null;
		assert pos.isInside();

		for (IPosition position : getPositions()) {
			if (position.equals(pos)) {
				position.shoot();
			}
		}
	}

	@Override
	public void sink() {
		for (IPosition position : getPositions()) {
			position.shoot();
		}
	}

	@Override
	public String toString() {
		return "[" + category + " " + bearing + " " + pos + "]";
	}
}