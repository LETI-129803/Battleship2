package battleship;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardRendererTest {

    @Test
    void testRenderBoardWithEmptyGrid() {
        char[][] grid = new char[10][10];
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                grid[r][c] = '.';
            }
        }

        String rendered = BoardRenderer.renderBoard(grid);

        assertNotNull(rendered, "O tabuleiro renderizado não deve ser nulo.");
        assertFalse(rendered.isEmpty(), "O tabuleiro renderizado não deve estar vazio.");
        assertTrue(rendered.contains("10"), "Deve incluir a coluna 10 no cabeçalho.");
        assertTrue(rendered.contains("A"), "Deve conter a linha A.");
        assertTrue(rendered.contains("J"), "Deve conter a linha J.");
    }
}