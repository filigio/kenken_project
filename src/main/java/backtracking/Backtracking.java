package main.java.backtracking;

import main.java.model.Grid;
import main.java.model.Cell;

import java.util.List;

public class Backtracking {
    private final Grid grid;
    private final int n; //dimensione griglia
    private final int[][] work; //matrice di lavro
    public Backtracking(Grid g) {
        this.grid = g;
        this.n =g.getSize();
        this.work = g.getValuesCopy();

    }

    public int[][] solve() {
        // Metodo ancora da implementare
        return null;
    }
}
