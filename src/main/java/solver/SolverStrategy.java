package main.java.solver;

import main.java.model.Grid;
import java.util.List;
/**
 * Interfaccia per strategie di risoluzione del puzzle KenKen.
 * Permette di astrarre il tipo di algoritmo usato.
 */
public interface SolverStrategy {
    List<int[][]> solve(Grid grid, int maxSolutions);  // supporta più soluzioni
}
