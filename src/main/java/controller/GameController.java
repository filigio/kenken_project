package main.java.controller;

import main.java.backtracking.Backtracking;
import main.java.model.Grid;
import main.java.model.Block;

import java.util.Collections;
import java.util.List;

/**
 * Classe per gestire il controller logico di KenKen.
 * - inizializza una griglia di gioco vuota,
 * - permette l'inserimento di valori nelle celle,
 * - permette l'aggiunta di blocchi (vincoli),
 * - fornisce accesso alla griglia.
 */
public class GameController {
    private Grid grid;  // griglia del gioco
    private int size;   // dimensione della griglia (NxN)

    // Costruttore: inizializza la griglia di dimensione specificata
    public GameController(int size) {
        this.grid = new Grid(size);
        this.size = size;
    }

    // Restituisce l'oggetto Grid per accedere allo stato del gioco
    public Grid getGrid() {
        return grid;
    }

    // Imposta un valore in una cella della griglia (riga, colonna)
    public void setGridValue(int row, int col, int value) {
        grid.setValue(row, col, value);
    }

    // Aggiunge un blocco (vincolo) alla griglia
    public void addBlock(Block b) {
        grid.addBlock(b);
    }
    /**SOLVE: */
    public List<int[][]> solvePuzzle() {
        Backtracking solver = new Backtracking(grid);       // usa il solver
        int[][] sol = solver.solve();                        // calcola la soluzione

        return (sol == null)
                ? Collections.emptyList()
                : Collections.singletonList(sol);            // restituisce una sola soluzione
    }
}
