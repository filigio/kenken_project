package test.model;

import main.java.model.DivBlock;
import main.java.model.Block;
import main.java.model.Cell;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per la logica del blocco DivBlock (divisione intera).
 * Include:
 * - un caso valido (divisione intera)
 * - un caso non valido (divisione non intera)
 */
class DivBlockTest {

    /**
     * Caso valido – 4 / 2 = 2 -> target rispettato
     */
    @Test
    void testDivisionSatisfied_ReturnsTrue() {
        // creando un blocco SubBlock composto da 2 celle: (0,0), (0,1) e il target di questo blocco è 2
        Block block = new DivBlock(2, List.of(new Cell(0, 0), new Cell(0, 1))); // target = 2

        int[][] grid = {
                {4, 2, 0}, // 4 / 2 = 2 ✔
                {0, 0, 0},
                {0, 0, 0}
        };

        assertTrue(block.isSatisfied(grid)); // divisione valida -> true
    }

    /**
     * Caso non valido – 5 / 2 = 2.5
     */
    @Test
    void testDivisionNotSatisfied_ReturnsFalse() {
        Block block = new DivBlock(2, List.of(new Cell(1, 0), new Cell(1, 1))); // target = 2

        int[][] grid = {
                {0, 0, 0},
                {5, 2, 0}, // 5 / 2 = 2.5
                {0, 0, 0}
        };

        assertFalse(block.isSatisfied(grid)); // divisione non intera -> false
    }
}
