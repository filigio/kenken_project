package test.backtracking;

import main.java.backtracking.Backtracking;
import main.java.model.Block;
import main.java.model.Cell;
import main.java.model.Grid;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SolverTest {
    @Test
    void testSimpleSolve() {
        Grid g = new Grid(2);

        // blocco (0,0)-(0,1) → somma = 3
        g.addBlock(Block.createBlock("+", 3,
                List.of(new Cell(0, 0), new Cell(0, 1))));

        // blocco (1,0)-(1,1) → somma = 3
        g.addBlock(Block.createBlock("+", 3,
                List.of(new Cell(1, 0), new Cell(1, 1))));

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
