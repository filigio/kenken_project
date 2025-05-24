package test.factory;

import main.java.factory.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test approfonditi su BlockFactoryManager per ogni operatore.
 * Verifico che il simbolo dell'operazionie si del Tipo Corretto, e verifico che se un simbolo non lo sia mi restituisca un'eccezione
 */
class BlockFactoryManagerTest {

    // Testa che per "+" venga restituita una SumBlockFactory
    @Test
    void testGetFactoryForPlusOperator() {
        BlockFactory factory = BlockFactoryManager.getFactory("+"); // Chiedo la Factory associata a "+"
        assertNotNull(factory);                                     // Verifico che NON sia null
        assertTrue(factory instanceof SumBlockFactory);             // Verifico che sia del tipo corretto
    }

    // Testa che per "-" venga restituita una SubBlockFactory
    @Test
    void testGetFactoryForMinusOperator() {
        BlockFactory factory = BlockFactoryManager.getFactory("-"); // Chiedo la Factory associata a "-"
        assertNotNull(factory);
        assertTrue(factory instanceof SubBlockFactory);
    }

    // Testa che per "*" venga restituita una MulBlockFactory
    @Test
    void testGetFactoryForMultiplicationOperator() {
        BlockFactory factory = BlockFactoryManager.getFactory("*"); // Chiedo la Factory associata a "*"
        assertNotNull(factory);
        assertTrue(factory instanceof MulBlockFactory);
    }

    // Testa che per "/" venga restituita una DivBlockFactory
    @Test
    void testGetFactoryForDivisionOperator() {
        BlockFactory factory = BlockFactoryManager.getFactory("/"); // Chiedo la Factory associata a "/"
        assertNotNull(factory);
        assertTrue(factory instanceof DivBlockFactory);
    }

    // Testa che passando un operatore NON valido, venga lanciata una eccezione
    @Test
    void testInvalidOperatorThrowsException() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            BlockFactoryManager.getFactory("?"); // Chiedo una factory per un operatore NON esistente
        });

        assertTrue(ex.getMessage().contains("Operatore non supportato")); // Verifico che il messaggio sia sensato
    }
}
