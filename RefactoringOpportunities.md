# Oportunidades de Refabricação (Refactoring Opportunities)

| Local (classe ou classe::método) | Nome do cheiro no código | Nome da refabricação (IntelliJ) | Número d@ alun@ |
| :--- | :--- | :--- | :--- |
| `battleship.Move::processEnemyFire` | Long Method | Extract Method | 106363 |
| `battleship.Move::printVerboseSummary` | Long Parameter List | Introduce Parameter Object | 106363 |
| `battleship.Position::isInside` | Complex Condition | Decompose Conditional | 106363 |
| `battleship.Position::isAdjacentTo` | Complex Expression | Introduce Variable | 106363 |
| `battleship.Tasks::readClassicPosition` | Complex boolean logic | Introduce Variable | 106363 |
| `battleship.GameHistory::addMove` | Redundant temporary / Data flow | Inline Variable | 106363 |
| `battleship.Move::processEnemyFire` | Duplicated string literal | Replace with Constant | 106363 |
| `battleship.Tasks::menu` | Long Method | Extract Method | 106363 |
| `battleship.Ship::buildShip` | Switch Statement / Overly Complex | Replace Conditional with Polymorphism | 129788 |
| `battleship.Fleet::addShip` | Long Method | Extract Method | 129788 |
| `battleship.Frigate::Frigate` | Redundant assignment | Inline Variable | 129788 |
| `battleship.Game::randomEnemyFire` | Duplicated Code | Extract Method | 129803 |
| `battleship.Game::fireSingleShot` | Complex Condition | Introduce Variable | 129803 |
| `battleship.Game::readEnemyFire` | Redundant Temporary Variable | Inline Variable | 129803 |
| `battleship.Game::printBoard` | Inconsistent Naming | Rename | 129803 |
| `battleship.Tasks::menu` | Magic Number | Extract Constant | 129803 |

