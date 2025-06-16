package test.model;

import main.java.model.MulBlock;
import main.java.model.Block;
import main.java.model.Cell;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per la logica del blocco MulBlock (moltiplicazione).
 * Include:
 * - un caso valido con prodotto esatto
 * - un caso non valido con prodotto sbagliato
 */
class MulBlockTest {

    /**
     * Caso valido – 2 × 3 = 6 -> soddisfa il target
     */
    @Test
    void testMultiplicationSatisfied_ReturnsTrue() {
        // creando un blocco MulBlock composto da 2 celle: (0,0), (0,1) e il target di questo blocco è 6
        Block block = new MulBlock(6, List.of(new Cell(0, 0), new Cell(0, 1))); // target = 6

        int[][] grid = {
                {2, 3, 0}, // 2 × 3 = 6
                {0, 0, 0},
                {0, 0, 0}
        };

        assertTrue(block.isSatisfied(grid)); // blocco soddisfatto -> true
    }

    /**
     * Caso non valido – 2 × 4 = 8 != target 6
     */
    @Test
    void testMultiplicationNotSatisfied_ReturnsFalse() {
        Block block = new MulBlock(6, List.of(new Cell(1, 0), new Cell(1, 1))); // target = 6

        int[][] grid = {
                {0, 0, 0},
                {2, 4, 0}, // 2 × 4 = 8
                {0, 0, 0}
        };

        assertFalse(block.isSatisfied(grid)); // blocco non soddisfatto -> false
    }
}
