package battleship;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Fleet.
 */
public class Fleet implements IFleet {

	private static final int MAX_PLACEMENT_ATTEMPTS = 10_000;

	private static final String[] DEFAULT_SHIP_TYPES = {
			"galeao",                           // 1 galleon
			"fragata",                          // 1 frigate
			"nau", "nau",                       // 2 carracks
			"caravela", "caravela", "caravela", // 3 caravels
			"barca", "barca", "barca", "barca"  // 4 barges
	};

	/**
	 * Creates a randomly generated fleet containing ships of various predefined types.
	 * Each ship is assigned a random bearing and position. If a ship cannot be added
	 * due to constraints (e.g., collision or boundary issues), it will be retried.
	 *
	 * @return a fully constructed and valid fleet as an instance of IFleet
	 */
	public static IFleet createRandom() {
		IFleet randomFleet = new Fleet();
		int fleetSize = 0;
		int attempts = 0;

		while (fleetSize < DEFAULT_SHIP_TYPES.length) {
			if (attempts++ >= MAX_PLACEMENT_ATTEMPTS) {
				throw new IllegalStateException("Unable to place all ships randomly after " + MAX_PLACEMENT_ATTEMPTS + " attempts.");
			}

			Ship ship = Ship.buildShip(DEFAULT_SHIP_TYPES[fleetSize], Compass.randomBearing(), Position.randomPosition());
			if (ship != null && randomFleet.addShip(ship)) {
				fleetSize++;
			}
		}
		return randomFleet;
	}

	private final List<IShip> ships;

	/**
	 * Instantiates a new Fleet.
	 */
	public Fleet() {
		ships = new ArrayList<>();
	}

	@Override
	public List<IShip> getShips() {
		return ships;
	}

	@Override
	public boolean addShip(IShip s) {
		assert s != null;

		if (ships.size() <= FLEET_SIZE && s.isInsideBoard() && !hasCollisionRisk(s)) {
			ships.add(s);
			return true;
		}
		return false;
	}

	@Override
	public List<IShip> getShipsLike(String category) {
		assert category != null;

		return ships.stream()
				.filter(s -> s.getCategory().equals(category))
				.toList();
	}

	@Override
	public List<IShip> getFloatingShips() {
		return ships.stream()
				.filter(IShip::stillFloating)
				.toList();
	}

	@Override
	public List<IShip> getSunkShips() {
		return ships.stream()
				.filter(s -> !s.stillFloating())
				.toList();
	}

	@Override
	public IShip shipAt(IPosition pos) {
		assert pos != null;

		return ships.stream()
				.filter(ship -> ship.occupies(pos))
				.findFirst()
				.orElse(null);
	}

	private boolean hasCollisionRisk(IShip s) {
		assert s != null;

		return ships.stream().anyMatch(existingShip -> existingShip.tooCloseTo(s));
	}

	/**
	 * This operation prints all the given ships.
	 *
	 * @param ships The list of ships
	 */
	public void printShips(List<IShip> ships) {
		assert ships != null;

		ships.forEach(System.out::println);
	}

	/**
	 * This operation shows the state of a fleet.
	 */
	public void printStatus() {
		System.out.println("Estado da Frota: " + this.getFloatingShips().size() + " a flutuar, " + this.getSunkShips().size() + " afundados!");
	}

	/**
	 * This operation prints all the ships of a fleet belonging to a particular category.
	 *
	 * @param category The category of ships of interest
	 */
	public void printShipsByCategory(String category) {
		assert category != null;

		printShips(getShipsLike(category));
	}

	/**
	 * This operation prints all the ships of a fleet not yet sunk.
	 */
	public void printFloatingShips() {
		printShips(getFloatingShips());
	}

	/**
	 * This operation prints all the ships of a fleet.
	 */
	void printAllShips() {
		printShips(ships);
	}
}