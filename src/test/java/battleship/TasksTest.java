
package battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários consolidados: testes da branch 129803
 * e casos adicionais da main, sem cenários duplicados.
 */
class TasksTest {

	@Test
	@DisplayName("Verificar se o menu de ajuda apresenta os comandos")
	void testMenuHelp() {

		// Guardar a saída original
		PrintStream original = System.out;

		// Capturar o texto apresentado no terminal
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		try {
			System.setOut(new PrintStream(output));

			// Executar o método a testar
			Tasks.menuHelp();

		} finally {
			// Restaurar a saída original
			System.setOut(original);
		}

		// Obter o texto apresentado
		String resultado = output.toString();

		// Verificar os comandos
		assertTrue(resultado.contains("AJUDA DO MENU"));
		assertTrue(resultado.contains("gerafrota"));
		assertTrue(resultado.contains("lefrota"));
		assertTrue(resultado.contains("historico"));
		assertTrue(resultado.contains("desisto"));
	}


	@Test
	@DisplayName("Verificar erro quando não é introduzida uma posição")
	void testReadClassicPositionVazia() {

		Scanner scanner = new Scanner("");

		IllegalArgumentException exception =
				assertThrows(IllegalArgumentException.class,
						() -> Tasks.readClassicPosition(scanner));

		assertEquals(
				"Nenhuma posição válida encontrada!",
				exception.getMessage()
		);

		scanner.close();
	}


	@Test
	@DisplayName("Verificar posição no formato A3")
	void testReadClassicPositionSemEspaco() {

		Scanner scanner = new Scanner("A3");

		IPosition posicao = Tasks.readClassicPosition(scanner);

		assertNotNull(posicao);
		assertEquals(0, posicao.getRow());
		assertEquals(2, posicao.getColumn());

		scanner.close();
	}

	@Test
	@DisplayName("Verificar posição no formato A 3")
	void testReadClassicPositionComEspaco() {

		Scanner scanner = new Scanner("A 3");

		IPosition posicao = Tasks.readClassicPosition(scanner);

		assertNotNull(posicao);
		assertEquals(0, posicao.getRow());
		assertEquals(2, posicao.getColumn());

		scanner.close();
	}


	@Test
	@DisplayName("Verificar erro para posição com formato inválido")
	void testReadClassicPositionInvalida() {

		Scanner scanner = new Scanner("3A");

		IllegalArgumentException exception =
				assertThrows(IllegalArgumentException.class,
						() -> Tasks.readClassicPosition(scanner));

		assertEquals(
				"Formato inválido. Use 'A3', 'A 3' ou similar.",
				exception.getMessage()
		);

		scanner.close();
	}

	@Test
	@DisplayName("Verificar posição com letra minúscula")
	void testReadClassicPositionMinuscula() {

		Scanner scanner = new Scanner("a3");

		IPosition posicao = Tasks.readClassicPosition(scanner);

		assertNotNull(posicao);
		assertEquals(0, posicao.getRow());
		assertEquals(2, posicao.getColumn());

		scanner.close();
	}


	@Test
	@DisplayName("Verificar leitura de uma posição válida")
	void testReadPositionValida() {

		Scanner scanner = new Scanner("3 5");

		Position posicao = Tasks.readPosition(scanner);

		assertNotNull(posicao);
		assertEquals(3, posicao.getRow());
		assertEquals(5, posicao.getColumn());

		scanner.close();
	}

	@Test
	@DisplayName("Verificar erro quando a posição contém letras")
	void testReadPositionInvalida() {

		Scanner scanner = new Scanner("A B");

		assertThrows(
				java.util.InputMismatchException.class,
				() -> Tasks.readPosition(scanner)
		);

		scanner.close();
	}


	@Test
	@DisplayName("Verificar criação de navio com tipo desconhecido")
	void testReadShipTipoInvalido() {

		Scanner scanner = new Scanner("desconhecido 3 5 n");

		Ship navio = Tasks.readShip(scanner);

		assertNull(navio);

		scanner.close();
	}


	@Test
	@DisplayName("Verificar criação de uma barca válida")
	void testReadShipBarca() {

		Scanner scanner = new Scanner("barca 4 4 n");

		Ship navio = Tasks.readShip(scanner);

		assertNotNull(navio);
		assertInstanceOf(Barge.class, navio);
		assertEquals("Barca", navio.getCategory());
		assertEquals(Compass.NORTH, navio.getBearing());
		assertEquals(4, navio.getPosition().getRow());
		assertEquals(4, navio.getPosition().getColumn());

		scanner.close();
	}


	@Test
	@DisplayName("Verificar criação de uma caravela válida")
	void testReadShipCaravela() {

		Scanner scanner = new Scanner("caravela 4 4 n");

		Ship navio = Tasks.readShip(scanner);

		assertNotNull(navio);
		assertInstanceOf(Caravel.class, navio);
		assertEquals(Compass.NORTH, navio.getBearing());
		assertEquals(4, navio.getPosition().getRow());
		assertEquals(4, navio.getPosition().getColumn());

		scanner.close();
	}


	@Test
	@DisplayName("Verificar criação de uma nau válida")
	void testReadShipNau() {

		Scanner scanner = new Scanner("nau 4 4 n");

		Ship navio = Tasks.readShip(scanner);

		assertNotNull(navio);
		assertInstanceOf(Carrack.class, navio);
		assertEquals(Compass.NORTH, navio.getBearing());
		assertEquals(4, navio.getPosition().getRow());
		assertEquals(4, navio.getPosition().getColumn());

		scanner.close();
	}

	@Test
	@DisplayName("Verificar criação de uma fragata válida")
	void testReadShipFragata() {

		Scanner scanner = new Scanner("fragata 4 4 n");

		Ship navio = Tasks.readShip(scanner);

		assertNotNull(navio);
		assertInstanceOf(Frigate.class, navio);
		assertEquals("Fragata", navio.getCategory());
		assertEquals(Compass.NORTH, navio.getBearing());
		assertEquals(4, navio.getPosition().getRow());
		assertEquals(4, navio.getPosition().getColumn());

		scanner.close();
	}


	@Test
	@DisplayName("Verificar criação de um galeão válido")
	void testReadShipGaleao() {

		Scanner scanner = new Scanner("galeao 4 4 n");

		Ship navio = Tasks.readShip(scanner);

		assertNotNull(navio);
		assertInstanceOf(Galleon.class, navio);
		assertEquals("Galeao", navio.getCategory());
		assertEquals(Compass.NORTH, navio.getBearing());
		assertEquals(4, navio.getPosition().getRow());
		assertEquals(4, navio.getPosition().getColumn());

		scanner.close();
	}


	@Test
	@DisplayName("Verificar comandos ajuda, inválido e desisto")
	void testMenuComandosBasicos() {

		InputStream entradaOriginal = System.in;
		PrintStream saidaOriginal = System.out;

		String comandos = "ajuda\ncomandoInvalido\ndesisto\n";

		ByteArrayInputStream entrada = new ByteArrayInputStream(
				comandos.getBytes(StandardCharsets.UTF_8)
		);

		ByteArrayOutputStream saida = new ByteArrayOutputStream();

		try {
			System.setIn(entrada);
			System.setOut(new PrintStream(saida));

			Tasks.menu();

		} finally {
			System.setIn(entradaOriginal);
			System.setOut(saidaOriginal);
		}

		String resultado = saida.toString(StandardCharsets.UTF_8);

		assertTrue(resultado.contains("AJUDA DO MENU"));
		assertTrue(resultado.contains("Que comando é esse???"));
		assertTrue(resultado.contains("Bons ventos!"));
	}


	@Test
	@DisplayName("Verificar comandos sem frota criada")
	void testMenuSemFrota() {

		InputStream entradaOriginal = System.in;
		PrintStream saidaOriginal = System.out;

		String comandos =
				"estado\nmapa\nrajada\ntiros\nsimula\ndesisto\n";

		ByteArrayInputStream entrada =
				new ByteArrayInputStream(
						comandos.getBytes(StandardCharsets.UTF_8));

		ByteArrayOutputStream saida =
				new ByteArrayOutputStream();

		try {
			System.setIn(entrada);
			System.setOut(new PrintStream(saida));

			Tasks.menu();

		} finally {
			System.setIn(entradaOriginal);
			System.setOut(saidaOriginal);
		}

		String resultado = saida.toString(StandardCharsets.UTF_8);

		assertTrue(resultado.contains("Bons ventos!"));
		assertFalse(resultado.contains("Que comando é esse???"));
	}

	@Test
	@DisplayName("Verificar posição com número negativo")
	void testReadClassicPositionNumeroNegativo() {

		try (Scanner scanner = new Scanner("A -3")) {

			IllegalArgumentException exception =
					assertThrows(
							IllegalArgumentException.class,
							() -> Tasks.readClassicPosition(scanner)
					);

			assertEquals(
					"Formato inválido. Use 'A3', 'A 3' ou similar.",
					exception.getMessage()
			);
		}
	}


	private String criarEntradaFrota(IFleet frota) {

		StringBuilder entrada = new StringBuilder();

		for (IShip navio : frota.getShips()) {
			entrada.append(
							navio.getCategory().toLowerCase(Locale.ROOT))
					.append(" ")
					.append(navio.getPosition().getRow())
					.append(" ")
					.append(navio.getPosition().getColumn())
					.append(" ")
					.append(navio.getBearing().getDirection())
					.append("\n");
		}

		return entrada.toString();
	}


	@Test
	@DisplayName("Verificar criação de uma frota completa")
	void testBuildFleetValida() {

		IFleet frotaOriginal = Fleet.createRandom();

		String entrada = criarEntradaFrota(frotaOriginal);

		try (Scanner scanner = new Scanner(entrada)) {

			Fleet frota = Tasks.buildFleet(scanner);

			assertNotNull(frota);

			assertEquals(
					Fleet.FLEET_SIZE,
					frota.getShips().size()
			);

			assertEquals(
					frotaOriginal.getShips().size(),
					frota.getShips().size()
			);
		}
	}


	@Test
	@DisplayName("Verificar que navio desconhecido é ignorado")
	void testBuildFleetNavioInvalido() {

		IFleet frotaOriginal = Fleet.createRandom();

		String entrada =
				"desconhecido 0 0 n\n"
						+ criarEntradaFrota(frotaOriginal);

		try (Scanner scanner = new Scanner(entrada)) {

			Fleet frota = Tasks.buildFleet(scanner);

			assertNotNull(frota);

			assertEquals(
					Fleet.FLEET_SIZE,
					frota.getShips().size()
			);

			for (IShip navio : frota.getShips()) {
				assertNotEquals(
						"desconhecido",
						navio.getCategory().toLowerCase(Locale.ROOT)
				);
			}
		}
	}


	@Test
	@DisplayName("Verificar comando gerafrota e estado")
	void testMenuGerarFrota() {

		InputStream entradaOriginal = System.in;
		PrintStream saidaOriginal = System.out;

		String comandos = "gerafrota\nestado\ndesisto\n";

		ByteArrayInputStream entrada =
				new ByteArrayInputStream(
						comandos.getBytes(StandardCharsets.UTF_8));

		ByteArrayOutputStream saida = new ByteArrayOutputStream();

		try {
			System.setIn(entrada);
			System.setOut(new PrintStream(saida));

			Tasks.menu();

		} finally {
			System.setIn(entradaOriginal);
			System.setOut(saidaOriginal);
		}

		String resultado = saida.toString(StandardCharsets.UTF_8);

		assertTrue(resultado.contains("Estado da Frota:"));
		assertTrue(resultado.contains("Bons ventos!"));
	}


	@Test
	@DisplayName("Verificar comando lefrota e estado")
	void testMenuLerFrota() {

		InputStream entradaOriginal = System.in;
		PrintStream saidaOriginal = System.out;

		IFleet frotaOriginal = Fleet.createRandom();

		String comandos =
				"lefrota\n"
						+ criarEntradaFrota(frotaOriginal)
						+ "estado\ndesisto\n";

		ByteArrayInputStream entrada =
				new ByteArrayInputStream(
						comandos.getBytes(StandardCharsets.UTF_8));

		ByteArrayOutputStream saida = new ByteArrayOutputStream();

		try {
			System.setIn(entrada);
			System.setOut(new PrintStream(saida));

			Tasks.menu();

		} finally {
			System.setIn(entradaOriginal);
			System.setOut(saidaOriginal);
		}

		String resultado = saida.toString(StandardCharsets.UTF_8);

		assertTrue(resultado.contains("Estado da Frota:"));
		assertTrue(resultado.contains("Bons ventos!"));
	}

	@Test
	@DisplayName("Verificar comando mapa com frota criada")
	void testMenuMapaComFrota() {

		InputStream entradaOriginal = System.in;
		PrintStream saidaOriginal = System.out;

		String comandos = "gerafrota\nmapa\ndesisto\n";

		ByteArrayInputStream entrada =
				new ByteArrayInputStream(
						comandos.getBytes(StandardCharsets.UTF_8));

		ByteArrayOutputStream saida = new ByteArrayOutputStream();

		try {
			System.setIn(entrada);
			System.setOut(new PrintStream(saida));

			Tasks.menu();

		} finally {
			System.setIn(entradaOriginal);
			System.setOut(saidaOriginal);
		}

		String resultado = saida.toString(StandardCharsets.UTF_8);

		assertTrue(resultado.contains("Bons ventos!"));
		assertFalse(resultado.contains("Que comando é esse???"));
	}

	@Test
	@DisplayName("Verificar comando tiros com frota criada")
	void testMenuTirosComFrota() {

		InputStream entradaOriginal = System.in;
		PrintStream saidaOriginal = System.out;

		String comandos = "gerafrota\ntiros\ndesisto\n";

		ByteArrayInputStream entrada =
				new ByteArrayInputStream(
						comandos.getBytes(StandardCharsets.UTF_8));

		ByteArrayOutputStream saida = new ByteArrayOutputStream();

		try {
			System.setIn(entrada);
			System.setOut(new PrintStream(saida));

			Tasks.menu();

		} finally {
			System.setIn(entradaOriginal);
			System.setOut(saidaOriginal);
		}

		String resultado = saida.toString(StandardCharsets.UTF_8);

		assertTrue(resultado.contains("Bons ventos!"));
		assertFalse(resultado.contains("Que comando é esse???"));
	}

	@Test
	@DisplayName("Verificar comando historico no menu")
	void testMenuHistorico() {

		InputStream entradaOriginal = System.in;
		PrintStream saidaOriginal = System.out;

		String comandos = "historico\ndesisto\n";

		ByteArrayInputStream entrada =
				new ByteArrayInputStream(
						comandos.getBytes(StandardCharsets.UTF_8));

		ByteArrayOutputStream saida = new ByteArrayOutputStream();

		try {
			System.setIn(entrada);
			System.setOut(new PrintStream(saida));

			Tasks.menu();

		} finally {
			System.setIn(entradaOriginal);
			System.setOut(saidaOriginal);
		}

		String resultado = saida.toString(StandardCharsets.UTF_8);

		assertTrue(resultado.contains("Bons ventos!"));
		assertFalse(resultado.contains("Que comando é esse???"));
	}

	@Test
	@DisplayName("Verificar rejeição de navio demasiado próximo")
	void testBuildFleetNavioDemasiadoProximo() {

		IFleet frotaOriginal = Fleet.createRandom();

		IShip primeiroNavio = frotaOriginal.getShips().get(0);

		IPosition posicaoProxima =
				((Ship) primeiroNavio).getAdjacentPositions()
						.stream()
						.filter(IPosition::isInside)
						.findFirst()
						.orElseThrow();

		String entradaOriginal = criarEntradaFrota(frotaOriginal);

		String[] linhas = entradaOriginal.split("\n", 2);

		String navioProximo =
				"barca "
						+ posicaoProxima.getRow() + " "
						+ posicaoProxima.getColumn() + " n\n";

		String entrada =
				linhas[0] + "\n"
						+ navioProximo
						+ linhas[1];

		try (Scanner scanner = new Scanner(entrada)) {

			Fleet frota = Tasks.buildFleet(scanner);

			assertNotNull(frota);
			assertEquals(Fleet.FLEET_SIZE, frota.getShips().size());

			assertFalse(frota.getShips().stream().anyMatch(
					navio -> navio.getPosition().getRow()
							== posicaoProxima.getRow()
							&& navio.getPosition().getColumn()
							== posicaoProxima.getColumn()
			));
		}
	}


	@Test
	@DisplayName("Verificar rajada com três tiros válidos")
	void testMenuRajadaValida() {

		InputStream entradaOriginal = System.in;
		PrintStream saidaOriginal = System.out;

		String comandos =
				"gerafrota\n"
						+ "rajada A1 B2 C3\n"
						+ "desisto\n";

		ByteArrayInputStream entrada =
				new ByteArrayInputStream(
						comandos.getBytes(StandardCharsets.UTF_8));

		ByteArrayOutputStream saida =
				new ByteArrayOutputStream();

		try {
			System.setIn(entrada);
			System.setOut(new PrintStream(saida));

			Tasks.menu();

		} finally {
			System.setIn(entradaOriginal);
			System.setOut(saidaOriginal);
		}

		String resultado =
				saida.toString(StandardCharsets.UTF_8);

		assertTrue(resultado.contains("Estado da Frota:"));
		assertTrue(resultado.contains("\"row\""));
		assertTrue(resultado.contains("Bons ventos!"));
	}

	@Test
	@DisplayName("Verificar erro numa rajada incompleta")
	void testMenuRajadaIncompleta() {

		InputStream entradaOriginal = System.in;

		String comandos =
				"gerafrota\n"
						+ "rajada A1 B2\n"
						+ "desisto\n";

		ByteArrayInputStream entrada =
				new ByteArrayInputStream(
						comandos.getBytes(StandardCharsets.UTF_8));

		try {
			System.setIn(entrada);

			IllegalArgumentException exception =
					assertThrows(
							IllegalArgumentException.class,
							() -> Tasks.menu()
					);

			assertEquals(
					"Você deve inserir exatamente 3 posições!",
					exception.getMessage()
			);

		} finally {
			System.setIn(entradaOriginal);
		}
	}




	// Testes complementares da main, sem repetir os cenários já testados

	@Test
	@DisplayName("Ler posição B5 no formato compacto")
	void testReadClassicPositionB5() {
		try (Scanner scanner = new Scanner("B5")) {
			IPosition posicao = Tasks.readClassicPosition(scanner);
			assertNotNull(posicao);
			assertEquals(1, posicao.getRow());
			assertEquals(4, posicao.getColumn());
		}
	}

	@Test
	@DisplayName("Ler posição D 7 no formato separado")
	void testReadClassicPositionD7() {
		try (Scanner scanner = new Scanner("D 7")) {
			IPosition posicao = Tasks.readClassicPosition(scanner);
			assertNotNull(posicao);
			assertEquals(3, posicao.getRow());
			assertEquals(6, posicao.getColumn());
		}
	}

	@Test
	@DisplayName("Rejeitar coordenadas apenas numéricas ou texto inválido")
	void testReadClassicPositionOutrosFormatosInvalidos() {
		try (Scanner scanner = new Scanner("999")) {
			assertThrows(IllegalArgumentException.class,
					() -> Tasks.readClassicPosition(scanner));
		}
		try (Scanner scanner = new Scanner("INVALID_COORDINATE")) {
			assertThrows(IllegalArgumentException.class,
					() -> Tasks.readClassicPosition(scanner));
		}
	}

	@Test
	@DisplayName("Criar fragata com orientação sul")
	void testReadShipFragataSul() {
		// O código atual de Ship.buildShip e Compass.charToCompass usa minúsculas.
		try (Scanner scanner = new Scanner("fragata 2 3 s")) {
			Ship navio = Tasks.readShip(scanner);
			assertNotNull(navio);
			assertInstanceOf(Frigate.class, navio);
			assertEquals("Fragata", navio.getCategory());
			assertEquals(Compass.SOUTH, navio.getBearing());
			assertEquals(2, navio.getPosition().getRow());
			assertEquals(3, navio.getPosition().getColumn());
		}
	}

	@Test
	@DisplayName("Sair imediatamente do menu com desisto")
	void testMenuDesistir() {
		InputStream entradaOriginal = System.in;
		PrintStream saidaOriginal = System.out;
		ByteArrayOutputStream saida = new ByteArrayOutputStream();

		try {
			System.setIn(new ByteArrayInputStream(
					"desisto\n".getBytes(StandardCharsets.UTF_8)));
			System.setOut(new PrintStream(saida));
			assertDoesNotThrow(Tasks::menu);
		} finally {
			System.setIn(entradaOriginal);
			System.setOut(saidaOriginal);
		}

		assertTrue(saida.toString(StandardCharsets.UTF_8)
				.contains("Bons ventos!"));
	}

}