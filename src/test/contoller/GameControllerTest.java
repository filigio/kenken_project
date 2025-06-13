package test.contoller;


import main.java.controller.GameController;
import main.java.factory.BlockFactoryManager;
import main.java.model.Block;
import main.java.model.Cell;
import main.java.model.Grid;
import main.java.solver.BacktrackingSolver;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    // ==============================================================
    // Verifica che il controller rilevi correttamente
    // - duplicati in riga
    // - duplicati in colonna
    // - blocchi non soddisfatti
    //- griglie valide
    // ==============================================================
    /**
     * BLACK BOX – Classe di Equivalenza Valida
     * Verifica che una griglia completa e valida restituisca null (nessun errore).
     */
    @Test
    void testValidGrid_ReturnsNull() {
        GameController controller = new GameController(2, new BacktrackingSolver());
        Grid g = controller.getGrid();

        // imposto i valori nelle celle
        g.setValue(0, 0, 1);
        g.setValue(0, 1, 2);
        g.setValue(1, 0, 2);
        g.setValue(1, 1, 1);

        Block b1 = BlockFactoryManager.getFactory("+") //crea blocco con targhet +3 dato da 1+2
                .createBlock(3, List.of(new Cell(0, 0), new Cell(0, 1)));
        Block b2 = BlockFactoryManager.getFactory("+") // ideam ma 2+1, per non avere duplicati
                .createBlock(3, List.of(new Cell(1, 0), new Cell(1, 1)));

        controller.addBlock(b1); //aggiunge blocco 1 alla griglia
        controller.addBlock(b2); // idem per blocco 2
        // per poi vedere se è valido, cosa che deve essere
        assertNull(controller.validateCurrentGrid(), "La griglia è valida -> dovrebbe restituire null");
    }

    /**
     * Black Box (input errato)
     * Verifica che venga rilevato un duplicato nella riga.
     */
    @Test
    void testDuplicateInRow() {
        GameController controller = new GameController(2, new BacktrackingSolver());
        Grid g = controller.getGrid();

        g.setValue(0, 0, 1);
        g.setValue(0, 1, 1); // duplicato nella riga 0
        g.setValue(1, 0, 2);
        g.setValue(1, 1, 2);

        Block b = BlockFactoryManager.getFactory("+") // blocco inutile, serve solo per evitare blocco vuoto
                .createBlock(2, List.of(new Cell(0, 0), new Cell(0, 1)));
        controller.addBlock(b);

        String msg = controller.validateCurrentGrid(); // esegue la validazione
        assertNotNull(msg);   // ci aspettiamo un errore
        assertTrue(msg.contains("Duplicato 1 nella riga 1"));
    }

    /**Black Box (input errato)
     * Verifica che venga rilevato un duplicato nella colonna.
     */
    @Test
    void testDuplicateInColumn() {
        GameController controller = new GameController(2, new BacktrackingSolver());
        Grid g = controller.getGrid();

        g.setValue(0, 0, 2);
        g.setValue(1, 0, 2); // duplicato nella colonna 0
        g.setValue(0, 1, 1);
        g.setValue(1, 1, 1);

        Block b = BlockFactoryManager.getFactory("+")// blocco inutile, serve solo per evitare blocco vuoto
                .createBlock(4, List.of(new Cell(0, 0), new Cell(1, 0)));
        controller.addBlock(b);

        String msg = controller.validateCurrentGrid();// esegue la validazione
        assertNotNull(msg);
        assertTrue(msg.contains("Duplicato 2 nella colonna 1"));
    }

    /**
     * Black Box (violazione vincolo)
     * Verifica che venga rilevata una violazione nei vincoli di blocco.
     * Es: somma 5 ma la somma reale è 3.
     */
    @Test
    void testBlockNotSatisfied() {
        GameController controller = new GameController(2, new BacktrackingSolver());
        Grid g = controller.getGrid();

        g.setValue(0, 0, 1);
        g.setValue(0, 1, 2); // somma = 3
        g.setValue(1, 0, 2);
        g.setValue(1, 1, 1);

        Block wrongBlock = BlockFactoryManager.getFactory("+")
                .createBlock(5, List.of(new Cell(0, 0), new Cell(0, 1))); // blocco con target = 5 ma somma = 3
        controller.addBlock(wrongBlock);

        String msg = controller.validateCurrentGrid(); // esegue la validazione
        assertNotNull(msg);
        assertTrue(msg.contains("Il blocco con target 5 non è soddisfatto"));
    }

    // ==============================================================
    //  Test per il metodo isGridComplete() della classe GameController.
    //  Verifica se la griglia è completamente riempita (nessun 0).
    // ==============================================================
    /**
     * BLACK BOX – Classe valida
     * Scopo: verifica che una griglia piena (senza zeri) venga riconosciuta come completa.
     */
    @Test
    void testGridIsComplete_ReturnsTrue() {
        GameController controller = new GameController(2, new BacktrackingSolver());

        // Imposta manualmente tutti i valori ≠ 0
        controller.setGridValue(0, 0, 1);
        controller.setGridValue(0, 1, 2);
        controller.setGridValue(1, 0, 2);
        controller.setGridValue(1, 1, 1);

        // Ora tutte le celle sono piene -> deve restituire true
        assertTrue(controller.isGridComplete(), "Tutte le celle sono riempite → griglia completa");
    }
    /**
     * BLACK BOX – Classe incompleta
     * Scopo: verifica che una griglia con almeno una cella vuota venga segnalata come incompleta.
     */
    @Test
    void testGridWithEmptyCells_ReturnsFalse() {
        GameController controller = new GameController(2, new BacktrackingSolver());

        // Imposto almeno una cella vuota
        controller.setGridValue(0, 0, 1);
        controller.setGridValue(0, 1, 2);
        controller.setGridValue(1, 0, 0); // cella vuota
        controller.setGridValue(1, 1, 1);

        // Poiché (1,0) è 0 → griglia NON è completa
        assertFalse(controller.isGridComplete(), "C'è almeno una cella vuota → griglia incompleta");
    }

    // ==============================================================
    // Test per il metodo solvePuzzle(int maxSol) della classe GameController.
    // Verifica comportamento in base a:
    // -  completezza griglia
    //  - correttezza dei vincoli
    // -  validità della copertura dei blocchi
    // ==============================================================
    /**
     * Griglia completa e valida
     * Scopo: solvePuzzle() deve restituire la soluzione corrente,
     *        senza invocare il solver.
     */
    @Test
    void testCompleteAndCorrectGrid_ReturnsSameSolution() {
        GameController controller = new GameController(2, new BacktrackingSolver()); // crea una griglia 2x2

        // Impostati tutti i valori (griglia completa)
        controller.setGridValue(0, 0, 1);
        controller.setGridValue(0, 1, 2);
        controller.setGridValue(1, 0, 2);
        controller.setGridValue(1, 1, 1);

        // Aggiunti due blocchi di somma 3 (entrambi soddisfatti)
        Block b1 = BlockFactoryManager.getFactory("+").createBlock(
                3, List.of(new Cell(0, 0), new Cell(0, 1))); // 1+2
        Block b2 = BlockFactoryManager.getFactory("+").createBlock(
                3, List.of(new Cell(1, 0), new Cell(1, 1))); // 2+1

        controller.addBlock(b1); // aggiunge blocco 1
        controller.addBlock(b2); // e 2

        // Chiama solvePuzzle: deve restituire la griglia com’è, senza calcoli (in quanto compelta e valida)
        List<int[][]> result = controller.solvePuzzle(1);

        assertEquals(1, result.size());   // deve restituire una sola soluzione (la quale deve essere uguale a quella isnerita)
        assertEquals(1, result.get(0)[0][0]); // (0,0)  verifica valori specifici (quindi per ogni punto il valore deve essere come quello che abbimo inserito)
        assertEquals(2, result.get(0)[0][1]); // (0,1)
        assertEquals(2, result.get(0)[1][0]); // (1,0)
        assertEquals(1, result.get(0)[1][1]); // (1,1)
    }


    /**
     * Griglia completa ma NON valida
     * Scopo: il metodo deve segnalare errore e NON restituire soluzioni.
     */
    @Test
    void testCompleteButInvalidGrid_ReturnsEmptyList() {
        GameController controller = new GameController(2, new BacktrackingSolver());

        // Griglia completa, ma con duplicati (errore logico)
        controller.setGridValue(0, 0, 1);
        controller.setGridValue(0, 1, 1); // duplicato nella riga
        controller.setGridValue(1, 0, 2);
        controller.setGridValue(1, 1, 2); // duplicato nella riga

        // Aggiunge almeno un blocco per non far fallire la validazione per assenza blocchi
        Block b = BlockFactoryManager.getFactory("+").createBlock(
                2, List.of(new Cell(0, 0), new Cell(0, 1)));
        controller.addBlock(b);

        List<int[][]> result = controller.solvePuzzle(1);

        assertTrue(result.isEmpty(), "Griglia completa ma non valida -> non deve restituire soluzioni, in quanto valori duplicati tra le righe");
    }


    /**
     * Griglia incompleta con celle non coperte da blocchi
     * Scopo: il metodo deve rilevare errore di copertura e fermarsi.
     */
    @Test
    void testIncompleteGridWithCoverageError_ReturnsEmpty() {
        GameController controller = new GameController(2, new BacktrackingSolver());

        // lascio due celle senza blocchi, per simulare una griglia mal definita
        // Blocca solo la riga 0: (1,0) e (1,1) sono scoperte -> errore
        Block b = BlockFactoryManager.getFactory("+").createBlock(
                2, List.of(new Cell(0, 0), new Cell(0, 1)));
        controller.addBlock(b);

        List<int[][]> result = controller.solvePuzzle(5); //chiamiamo solvePuzzle() e richiediamo 5 soluzioni

        assertTrue(result.isEmpty(), "Celle scoperte -> solver non deve partire");
    }


    /**
     * Black box: testiamo cosa succede se la griglia è incompleta
     * White box: copriamo il ramo if (!isGridComplete()) → chiama solver.solve(...)
     *
     * Griglia incompleta ma con copertura corretta
     * Scopo: il solver viene attivato e restituisce 1+ soluzioni.
     */
    @Test
    void testIncompleteGridValid_CallsSolverAndReturnsSolutions() {
        GameController controller = new GameController(2, new BacktrackingSolver());

        // Crea blocchi validi che coprono tutte le celle
        Block b1 = BlockFactoryManager.getFactory("+").createBlock(
                3, List.of(new Cell(0, 0), new Cell(0, 1))); // somma 3
        Block b2 = BlockFactoryManager.getFactory("+").createBlock(
                3, List.of(new Cell(1, 0), new Cell(1, 1))); // somma 3
        controller.addBlock(b1);
        controller.addBlock(b2);

        // simula l’utente che ha solo definito i blocchi, ma non ha riempito le celle

        List<int[][]> result = controller.solvePuzzle(2); // richiede max 2 soluzioni

        assertFalse(result.isEmpty(), "Il solver deve trovare almeno una soluzione");
        assertTrue(result.size() <= 2, "Il numero di soluzioni deve rispettare il limite impostato");
    }

}