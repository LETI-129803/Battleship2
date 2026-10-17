package battleship;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

/**
 * Utilitário para renderização visual do tabuleiro com tabelas ASCII.
 */
public class BoardRenderer {

    /**
     * Renderiza o tabuleiro com as dimensões e moldura gráfica formatada.
     *
     * @param map Matriz de carateres 10x10 representativa do tabuleiro.
     * @return String formatada contendo a tabela ASCII.
     */
    public static String renderBoard(char[][] map) {
        AsciiTable at = new AsciiTable();
        at.addRule();

        // Cabeçalho das colunas (1 a 10)
        at.addRow("", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        at.addRule();

        // Linhas de A a J
        for (int row = 0; row < Game.BOARD_SIZE; row++) {
            Position pos = new Position(row, 0);
            char rowLabel = pos.getClassicRow();

            String[] rowData = new String[Game.BOARD_SIZE + 1];
            rowData[0] = String.valueOf(rowLabel);

            for (int col = 0; col < Game.BOARD_SIZE; col++) {
                char symbol = (map != null && row < map.length && col < map[row].length) ? map[row][col] : ' ';
                rowData[col + 1] = String.valueOf(symbol);
            }

            at.addRow((Object[]) rowData);
            at.addRule();
        }

        at.setTextAlignment(TextAlignment.CENTER);
        return at.render();
    }
}