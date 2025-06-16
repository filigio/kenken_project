package test.model;

import main.java.model.SubBlock;
import main.java.model.Block;
import main.java.model.Cell;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per la logica del blocco SubBlock (sottrazione assoluta).
 * Include:
 * - un caso valido con risultato corretto
 * - un caso non valido con risultato sbagliato
 */
class SubBlockTest {

    /**
     * Caso valido – |2 - 1| = 1  soddisfa il target
     */
    @Test
    void testSubtractionSatisfied_ReturnsTrue() {
        // creando un blocco SubBlock composto da 2 celle: (0,0), (0,1) e il target di questo blocco è 1
        Block block = new SubBlock(1, List.of(new Cell(0, 0), new Cell(0, 1))); // target = 1

        int[][] grid = {
                {2, 1, 0}, // |2 - 1| = 1 ✔
                {0, 0, 0},
                {0, 0, 0}
        };

        assertTrue(block.isSatisfied(grid)); // il blocco è soddisfatto -> true
    }

    /**
     * Caso non valido – |3 - 1| = 2 != target (che è 1)
     */
    @Test
    void testSubtractionNotSatisfied_ReturnsFalse() {
        Block block = new SubBlock(1, List.of(new Cell(1, 0), new Cell(1, 1))); // target = 1

        int[][] grid = {
                {0, 0, 0},
                {3, 1, 0}, // |3 - 1| = 2 ✘
                {0, 0, 0}
        };

        assertFalse(block.isSatisfied(grid)); // la sottrazione non dà il target → false
    }
}
