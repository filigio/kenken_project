package main.java.solver;

import main.java.backtracking.Backtracking;
import main.java.model.Grid;
import java.util.List;

/**
 * Strategia concreta che usa il backtracking per risolvere il puzzle.
 */

public class BacktrackingSolver implements SolverStrategy {

    @Override
    public List<int[][]> solve(Grid grid, int maxSolutions) {
        Backtracking backtracking = new Backtracking(grid);
        return backtracking.solve(maxSolutions);
    }
}
