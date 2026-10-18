package battleship;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class I18nManagerTest {

    @Test
    void testPortugueseMessages() {
        I18nManager.setLanguage("pt");
        assertEquals("Bem-vindo a Batalha Naval!", I18nManager.getMessage("game.welcome"));
        assertEquals("Tiro na agua!", I18nManager.getMessage("game.shot.water"));
    }

    @Test
    void testEnglishMessages() {
        I18nManager.setLanguage("en");
        assertEquals("Welcome to Battleship!", I18nManager.getMessage("game.welcome"));
        assertEquals("Shot missed!", I18nManager.getMessage("game.shot.water"));
    }
}