package test.backtracking;

import main.java.backtracking.Backtracking;
import main.java.factory.BlockFactoryManager;
import main.java.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class SolverTest {
    private Grid grid;
    private Backtracking solver;

    // =============================================
    // SETUP: eseguito prima di ogni test
    // =============================================
    @BeforeEach
    void setup() {
        grid = new Grid(3);              // Crea una griglia 3x3
        solver = new Backtracking(grid); // Istanzia il solver con quella griglia
    }

    // ==============================================================
    // TEST INDIRETTI SU isSafe() — verifichiamo che non accetti duplicati
    // ==============================================================

    /**
     * Testa che il solver non permetta duplicati nella stessa riga.
     * Il blocco somma 3 tra (0,0) e (0,1), quindi dovranno essere 1 e 2.
     * Verifica che i due valori non siano uguali (no duplicati in riga).
     */
    @Test
    void testSolve_RejectsDuplicateValuesInRow() {
        grid.addBlock(BlockFactoryManager.getFactory("+").createBlock(
                3, List.of(new Cell(0, 0), new Cell(0, 1))));// Crea un blocco in riga (0,0) e (0,1) con somma = 3

        int[][] solution = solver.solve();// Prova a risolvere la griglia

        assertNotNull(solution);  // Verifica che venga trovata una soluzione
        // ma i due valori nella stessa riga devono essere diversi
        assertNotEquals(solution[0][0], solution[0][1]);
    }

    /** idem ma per le colonne*/
    @Test
    void testSolve_RejectsDuplicateValuesInColumn() {
        // Crea un blocco in colonna (0,0) e (1,0) con somma = 3
        grid.addBlock(BlockFactoryManager.getFactory("+").createBlock(
                3, List.of(new Cell(0, 0), new Cell(1, 0))));

        int[][] solution = solver.solve();

        // controlla che non ci siano duplicati in colonna
        assertNotNull(solution);
        assertNotEquals(solution[0][0], solution[1][0]);
    }

    // =============================================
    // WHITE BOX TESTING - PATH COVERAGE
    // Verifica che l’algoritmo DFS del solver percorra correttamente
    // i rami di successo (soluzioni trovate) e fallimento (nessuna soluzione). Per (+, -, *, /)
    // =============================================

    /**
     * Caso positivo: verifica che il solver trovi una soluzione valida.
     * Blocchi semplici su una 2x2 con somma 3 (es. 1+2).
     */
    @Test
    void testDfs_FindsSolution_ForValidGrid() {
        // Griglia 2x2 con blocchi semplici e risolvibili
        Grid smallGrid = new Grid(2);
        smallGrid.addBlock(BlockFactoryManager.getFactory("+")
                .createBlock(3, List.of(new Cell(0, 0), new Cell(0, 1))));
        smallGrid.addBlock(BlockFactoryManager.getFactory("+")
                .createBlock(3, List.of(new Cell(1, 0), new Cell(1, 1))));

        // Risolve la griglia
        Backtracking smallSolver = new Backtracking(smallGrid);
        int[][] solution = smallSolver.solve();

        // Verifica che esista una soluzione e che abbia un valore atteso
        assertNotNull(solution, "Dovrebbe trovare una soluzione");
        assertEquals(1, solution[0][0]); // Controllo di un valore noto nella soluzione
    }

    /**
     * Caso negativo: verifica che il solver restituisca null se non ci sono soluzioni.
     * Il target del blocco è 100 su due celle: impossibile.
     */
    @Test
    void testDfs_ReturnsNull_ForUnsolvableGrid() {
        // Aggiunge un blocco con somma irrealistica (impossibile da risolvere)
        grid.addBlock(BlockFactoryManager.getFactory("+")
                .createBlock(100, List.of(new Cell(0, 0), new Cell(0, 1))));

        // Il solver non deve trovare nessuna soluzione
        assertNull(solver.solve(),
                "Dovrebbe restituire null: nessuna soluzione possibile");
    }

    /**
     * Verifica che il solver riesca a trovare una soluzione
     * quando la griglia contiene un blocco di MOLTIPLICAZIONE (*).
     */
    @Test
    void testDfs_FindsSolution_WithMultiplicationBlock() {
        Grid grid = new Grid(2);

        // blocco (0,0),(1,0) prodotto = 2
        grid.addBlock(BlockFactoryManager.getFactory("*").createBlock(
                2, List.of(new Cell(0, 0), new Cell(1, 0)))); // 1×2 o 2×1

        // blocco (0,1),(1,1) somma = 3
        grid.addBlock(BlockFactoryManager.getFactory("+").createBlock(
                3, List.of(new Cell(0, 1), new Cell(1, 1)))); // 1+2

        Backtracking solver = new Backtracking(grid);
        int[][] sol = solver.solve();

        assertNotNull(sol, "Dovrebbe trovare una soluzione con moltiplicazione");

        // Verifica che il prodotto sia 2 e la somma 3
        assertEquals(2, sol[0][0] * sol[1][0]);
        assertEquals(3, sol[0][1] + sol[1][1]);
    }
    /**
     * Verifica che il solver risolva correttamente un blocco di SOTTRAZIONE (-),
     * che ammette l'uso del valore assoluto tra due celle.
     */
    @Test
    void testDfs_FindsSolution_WithSubtractionBlock() {
        Grid grid = new Grid(2);

        // blocco (0,0),(0,1)  differenza assoluta = 1
        grid.addBlock(BlockFactoryManager.getFactory("-").createBlock(
                1, List.of(new Cell(0, 0), new Cell(0, 1)))); // |1−2|

        // blocco (1,0),(1,1) somma = 3
        grid.addBlock(BlockFactoryManager.getFactory("+").createBlock(
                3, List.of(new Cell(1, 0), new Cell(1, 1))));

        Backtracking solver = new Backtracking(grid);
        int[][] sol = solver.solve();

        assertNotNull(sol, "Dovrebbe trovare una soluzione con sottrazione");

        // Verifica differenza assoluta
        assertEquals(1, Math.abs(sol[0][0] - sol[0][1]));
    }

    /**
     * Verifica che il solver gestisca correttamente un blocco di DIVISIONE (/),
     * che considera entrambe le direzioni (val1/val2 e val2/val1).
     */
    @Test
    void testDfs_FindsSolution_WithDivisionBlock() {
        Grid grid = new Grid(2);

        // blocco (0,0),(1,0) rapporto = 2
        grid.addBlock(BlockFactoryManager.getFactory("/").createBlock(
                2, List.of(new Cell(0, 0), new Cell(1, 0)))); // 2/1 oppure 4/2 ecc.

        // blocco (0,1),(1,1) somma = 3
        grid.addBlock(BlockFactoryManager.getFactory("+").createBlock(
                3, List.of(new Cell(0, 1), new Cell(1, 1)))); // 1+2

        Backtracking solver = new Backtracking(grid);
        int[][] sol = solver.solve();

        assertNotNull(sol, "Dovrebbe trovare una soluzione con divisione");

        int a = sol[0][0], b = sol[1][0];

        // Verifica che uno sia il doppio dell’altro
        assertTrue(
                (a / b == 2 && a % b == 0) ||
                        (b / a == 2 && b % a == 0),
                "Uno dei due valori deve essere il doppio dell'altro"
        );
    }


    // =============================================
    // BLACK BOX TESTING - PARTIZIONAMENTO IN CLASSI
    // Verifica casi d’uso dal punto di vista dell’utente, senza conoscere la logica interna
    // =============================================
    /**Verifica che il solver sia in grado di esplorare tutte le permutazioni valide di numeri in una griglia senza vincoli,
     * (ovviamtni privi di duplicati)*/
    @Test
    void testSolve_EmptyGrid_ReturnsAllSolutions() {
        // Nessun blocco = griglia senza vincoli → tutte le permutazioni sono valide
        List<int[][]> solutions = solver.solve(Integer.MAX_VALUE);

        // Deve generare molte soluzioni possibili (es. 3x3 = 9 celle → molte combinazioni)
        assertTrue(solutions.size() > 1,
                "Dovrebbe restituire tutte le soluzioni possibili");
    }

    /**Verifica che la logica di limitazione del numero di soluzioni funzioni correttamente.
     * Quindi che venga rispettato il numero massimo di soluzioni richieste*/
    @Test
    void testSolve_MaxSolutionsLimit_Respected() {
        // Aggiunge un blocco semplice alla griglia
        grid.addBlock(BlockFactoryManager.getFactory("+")
                .createBlock(6, List.of(new Cell(0, 0), new Cell(0, 1), new Cell(0, 2))));

        int maxSolutions = 2;
        List<int[][]> solutions = solver.solve(maxSolutions);

        // Il numero di soluzioni deve rispettare il limite impostato
        assertEquals(maxSolutions, solutions.size(),
                "Dovrebbe restituire esattamente " + maxSolutions + " soluzioni");
    }

    // =============================================
    // Verifica che una griglia semplice venga risolta correttamente
    // =============================================

    @Test
    void testSimpleSolve() {
        Grid g = new Grid(2);

        // blocco (0,0)-(0,1) → somma = 3
        g.addBlock(BlockFactoryManager.getFactory("+").createBlock(
                3, List.of(new Cell(0, 0), new Cell(0, 1))));

        // blocco (1,0)-(1,1) → somma = 3
        g.addBlock(BlockFactoryManager.getFactory("+").createBlock(
                3, List.of(new Cell(1, 0), new Cell(1, 1))));

        Backtracking solver = new Backtracking(g);
        int[][] sol = solver.solve();

        assertNotNull(sol, "Il solver avrebbe dovuto trovare una soluzione");
        assertEquals(1, sol[0][0]);
        assertEquals(2, sol[0][1]);
        assertEquals(2, sol[1][0]);
        assertEquals(1, sol[1][1]);
    }
}
