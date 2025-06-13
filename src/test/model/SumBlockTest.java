package test.model;

import main.java.model.Cell;
import main.java.model.SumBlock;
import main.java.model.Block;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Test per la logica del blocco SumBlock.
 * Obiettivo: condition coverage + analisi di frontiera sul metodo isSatisfied().
 *
 * testare il metodo isSatisfied() della classe SumBlock verificando:
 *      tutte le possibili condizioni logiche (celle vuote, complete, duplicati)
 *      e i casi limite rispetto al target (somma <, =, > target)
 */
class SumBlockTest {

    /**
     * Celle vuote, somma parziale inferiore al target
     * Somma = 2 < target 5  ritorna true
     */
    @Test
    void testPartialSumUnderTarget_ReturnsTrue() {
        // creando un blocco SumBlock composto da 3 celle: (0,0), (0,1), (0,2), e il target di questo blocco è 5
        Block block = new SumBlock(5, List.of(new Cell(0, 0), new Cell(0, 1), new Cell(0, 2)));

        int[][] grid = {
                {2, 0, 0}, // solo la prima cella è valorizzata
                {0, 0, 0},
                {0, 0, 0}
        };

        assertTrue(block.isSatisfied(grid)); // somma 2 < target → ancora valido
    }

    /**
     * Celle vuote, somma parziale superiore al target
     * Somma = 6 > target 5 -> ritorna false
     */
    @Test
    void testPartialSumOverTarget_ReturnsFalse() {
        // creando un blocco SumBlock composto da 3 celle: (0,0), (0,1), (0,2), e il target di questo blocco è 5
        Block block = new SumBlock(5, List.of(new Cell(0, 0), new Cell(0, 1), new Cell(0, 2)));

        int[][] grid = {
                {6, 0, 0}, // somma parziale 6 > target
                {0, 0, 0},
                {0, 0, 0}
        };

        assertFalse(block.isSatisfied(grid)); // valore superiore al target
    }

    /**
     * Tutte le celle piene, somma esatta
     * Somma = 3 -> target = 3 -> true
     */
    @Test
    void testFullSumEqualsTarget_ReturnsTrue() {
        Block block = new SumBlock(3, List.of(new Cell(1, 0), new Cell(1, 1)));

        int[][] grid = {
                {0, 0, 0},
                {1, 2, 0},
                {0, 0, 0}
        };

        assertTrue(block.isSatisfied(grid)); // 1+2 = 3
    }

    /**
     * Tutte le celle piene, somma sbagliata
     * Somma = 4 != target 3 -> false
     */
    @Test
    void testFullSumDifferentFromTarget_ReturnsFalse() {
        Block block = new SumBlock(3, List.of(new Cell(1, 0), new Cell(1, 1)));

        int[][] grid = {
                {0, 0, 0},
                {2, 2, 0},
                {0, 0, 0}
        };

        assertFalse(block.isSatisfied(grid)); // somma errata
    }

    /**
     * Valori corretti ma duplicati in riga
     */
    @Test
    void testValidSumButDuplicateInRow_ReturnsFalse() {
        Block block = new SumBlock(4, List.of(new Cell(0, 0), new Cell(0, 1)));

        int[][] grid = {
                {2, 2, 0}, // duplicati nella stessa riga
                {0, 0, 0},
                {0, 0, 0}
        };

        assertFalse(block.isSatisfied(grid)); // violazione KenKen: duplicati su riga
    }
}