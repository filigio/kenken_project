package test.model;

import main.java.model.Cell;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * WHITE BOX (Branch + Statement Coverage)
 * Obiettivo: testare tutti i metodi pubblici, tutti i rami logici e tutte le istruzioni.
 */
class CellTest {

    /**
     * Verifica che getRow() e getCol() restituiscano correttamente i valori assegnati
     */
    @Test
    void testGetRowAndCol() {
        Cell cell = new Cell(2, 3); // crea una cella in (2,3)
        assertEquals(2, cell.getRow()); // verifica riga
        assertEquals(3, cell.getCol()); // verifica colonna
    }

    /**
     * Verifica che equals restituisca true se due celle hanno stesse coordinate
     */
    @Test
    void testEquals_SameCoordinates_ReturnsTrue() {
        Cell c1 = new Cell(1, 1);
        Cell c2 = new Cell(1, 1);
        assertTrue(c1.equals(c2)); // due celle con (1,1) sono uguali
    }

    /**
     * Verifica che equals restituisca false per coordinate diverse
     */
    @Test
    void testEquals_DifferentCoordinates_ReturnsFalse() {
        Cell c1 = new Cell(0, 0);
        Cell c2 = new Cell(0, 1);
        assertFalse(c1.equals(c2)); // (0,0) != (0,1) -> false
    }

    /**
     * Verifica che equals restituisca false se confrontato con oggetto di tipo diverso
     */
    @Test
    void testEquals_WithDifferentType_ReturnsFalse() {
        Cell cell = new Cell(1, 2);
        Object other = "Non è una cella";
        assertFalse(cell.equals(other)); // confronto con tipo sbagliato -> false
    }

    /**
     * Verifica che equals restituisca true se confrontato con se stessa
     */
    @Test
    void testEquals_SameObject_ReturnsTrue() {
        Cell cell = new Cell(2, 2);
        assertTrue(cell.equals(cell)); // confronto con sé stesso -> true
    }

    /**
     * Verifica che hashCode restituisca lo stesso valore per celle uguali
     */
    @Test
    void testHashCode_SameCoordinates_SameHash() {
        Cell c1 = new Cell(4, 5);
        Cell c2 = new Cell(4, 5);
        assertEquals(c1.hashCode(), c2.hashCode()); // celle uguali -> stesso hash
    }

    /**
     * Verifica che toString produca una stringa nel formato atteso
     */
    @Test
    void testToString() {
        Cell cell = new Cell(3, 2);
        assertEquals("(3,2)", cell.toString()); // verifica formato "(r,c)"
    }
}
