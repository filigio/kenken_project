package test.backtracking;

import main.java.backtracking.Backtracking;
import main.java.factory.BlockFactoryManager;
import main.java.model.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class SolverTest {

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


    //implementare caso senza soluzione
}
