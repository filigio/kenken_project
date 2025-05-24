package test.factory;

import main.java.factory.*;
import main.java.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test sulla creazione diretta dei Blocchi tramite le Factory concrete.
 *
 * Con questi test, controlliamo che ogni Factory concreta:
 * Crei effettivamente il giusto tipo di blocco,
 */
class BlockFactoryTest {

    @Test
    void testSumBlockFactoryCreatesSumBlock() {
        BlockFactory factory = new SumBlockFactory(); // Istanzia la factory per la somma
        Block block = factory.createBlock(5, List.of(new Cell(0, 0), new Cell(0, 1))); // Crea un blocco di somma con target 5 e 2 celle

        assertNotNull(block); // Verifica che il blocco non sia null
        assertTrue(block instanceof SumBlock, "Deve essere una SumBlock"); // Verifica che il blocco sia effettivamente di tipo SumBlock
        assertEquals(5, block.getTarget()); // Controlla che il target del blocco sia corretto
    }

    @Test
    void testSubBlockFactoryCreatesSubBlock() {
        BlockFactory factory = new SubBlockFactory(); // Factory per la sottrazione
        Block block = factory.createBlock(2, List.of(new Cell(1, 0), new Cell(1, 1))); // Crea blocco con target 2

        assertNotNull(block); // Controllo null
        assertTrue(block instanceof SubBlock, "Deve essere una SubBlock"); // Verifica tipo SubBlock
        assertEquals(2, block.getTarget()); // Verifica correttezza target
    }

    @Test
    void testMulBlockFactoryCreatesMulBlock() {
        BlockFactory factory = new MulBlockFactory(); // Factory per la moltiplicazione
        Block block = factory.createBlock(6, List.of(new Cell(0, 0), new Cell(1, 0))); // Blocchi da moltiplicare con target 6

        assertNotNull(block); // Controllo null
        assertTrue(block instanceof MulBlock, "Deve essere una MulBlock"); // Tipo MulBlock
        assertEquals(6, block.getTarget()); // Controlla target corretto
    }

    @Test
    void testDivBlockFactoryCreatesDivBlock() {
        BlockFactory factory = new DivBlockFactory(); // Factory per la divisione
        Block block = factory.createBlock(2, List.of(new Cell(0, 1), new Cell(1, 1))); // Blocchi per divisione con risultato 2

        assertNotNull(block); // Non deve essere null
        assertTrue(block instanceof DivBlock, "Deve essere una DivBlock"); // Tipo DivBlock
        assertEquals(2, block.getTarget()); // Controllo target corretto
    }
}
