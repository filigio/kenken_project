package main.java.controller;

import main.java.backtracking.Backtracking;
import main.java.model.Cell;
import main.java.model.Grid;
import main.java.model.Block;
import main.java.observer.GridObserver;
import main.java.solver.SolverStrategy;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
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
    private SolverStrategy solver;
    private final List<GridObserver> observers = new ArrayList<>(); // Lista di tutti gli observer registrati che vogliono essere notificati

    // Costruttore: inizializza la griglia di dimensione specificata
    public GameController(int size, SolverStrategy solver) {
        this.grid = new Grid(size);
        this.size = size;
        this.solver = solver;
    }

    // METODI PER GESTIRE OSSERVATORI
    public void addObserver(GridObserver o) {
        observers.add(o);                        // Aggiunge un observer alla lista
    }

    public void removeObserver(GridObserver o) {
        observers.remove(o);                     // Rimuove un observer (se non serve più)
    }

    private void notifyObservers() {
        for (GridObserver o : observers) {       // Per ogni observer registrato
            o.onGridChanged();                   // chiama il suo metodo di aggiornamento
        }
    }


    // Restituisce l'oggetto Grid per accedere allo stato del gioco
    public Grid getGrid() {
        return grid;
    }

    // Imposta un valore in una cella della griglia (riga, colonna)
    public void setGridValue(int row, int col, int value) {
        grid.setValue(row, col, value);
        notifyObservers();
    }

    // Aggiunge un blocco (vincolo) alla griglia
    public void addBlock(Block b) {
        grid.addBlock(b);
        notifyObservers();
    }

    public void resetGrid() {
        grid.clear();
        notifyObservers();
    }

    /**SOLVE: */
    public List<int[][]> solvePuzzle(int maxSol) { // Metodo per risolvere il puzzle restituendo più soluzioni (fino a maxSol)
        // 1. Verifica che ogni cella sia in un solo blocco
        String coverageError = checkFullCoverage();
        if (coverageError != null) {
            JOptionPane.showMessageDialog(null, coverageError);
            return Collections.emptyList();
        }

        // 2. Se la griglia è già completa, validala
        if (isGridComplete()) {
            String err = validateCurrentGrid();
            JOptionPane.showMessageDialog(null,
                    (err == null) ? "La soluzione corrente è corretta!" : err);
            return (err == null)
                    ? Collections.singletonList(grid.getValuesCopy())
                    : Collections.emptyList();
        }

        return solver.solve(grid, maxSol); // usa la strategia
    }

    // Verifica che tutte le celle siano riempite (≠ 0)
    public boolean isGridComplete() {
        for (int[] row : grid.getValuesCopy())
            for (int val : row)
                if (val == 0) return false;
        return true;
    }

    // Verifica righe, colonne e blocchi
    public String validateCurrentGrid() {
        int[][] v = grid.getValuesCopy();

        // Righe
        for (int r = 0; r < size; r++) {
            boolean[] seen = new boolean[size + 1];
            for (int c = 0; c < size; c++) {
                int val = v[r][c];
                if (val == 0) continue;
                if (seen[val]) return "Duplicato " + val + " nella riga " + (r + 1);
                seen[val] = true;
            }
        }

        // Colonne
        for (int c = 0; c < size; c++) {
            boolean[] seen = new boolean[size + 1];
            for (int r = 0; r < size; r++) {
                int val = v[r][c];
                if (val == 0) continue;
                if (seen[val]) return "Duplicato " + val + " nella colonna " + (c + 1);
                seen[val] = true;
            }
        }

        // Blocchi
        for (Block b : grid.getBlocks()) {
            if (!b.isSatisfied(v))
                return "Il blocco con target " + b.getTarget() + " non è soddisfatto";
        }

        return null;
    }

    // Verifica che ogni cella sia in un solo blocco e dentro i limiti
    private String checkFullCoverage() {
        int[][] cov = new int[size][size];

        for (Block b : grid.getBlocks()) {
            for (Cell c : b.getCells()) {
                int r = c.getRow(), col = c.getCol();
                if (r < 0 || r >= size || col < 0 || col >= size)
                    return "Errore: la cella " + c + " è fuori dalla griglia.";
                cov[r][col]++;
                if (cov[r][col] > 1)
                    return "Errore: la cella " + c + " è presente in più blocchi.";
            }
        }

        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                if (cov[r][c] == 0)
                    return "Errore: la cella (" + r + "," + c + ") non appartiene a nessun blocco.";

        return null;
    }


    /** SAVE e LOAD */
    // Metodo per salvare lo stato della griglia su file
    public void saveGrid(File f) throws IOException {
        // Crea un ObjectOutputStream collegato al file specificato
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
            oos.writeObject(grid); // Scrive l'intera griglia (inclusi blocchi e valori)
        }
    }

    // Metodo per caricare una griglia salvata da file
    public void loadGrid(File f) throws IOException, ClassNotFoundException {
        // Crea un ObjectInputStream collegato al file specificato
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            this.grid = (Grid) ois.readObject(); // Legge la griglia salvata
            this.size = grid.getSize(); // Aggiorna la dimensione interna
            notifyObservers();                   // Notifica gli observer
        }
    }


    // Metodo extra per sviluppo: stampa la griglia in console
    public void printGridToConsole() {
        int[][] values = grid.getValuesCopy();
        System.out.println("Griglia attuale:");
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                System.out.print((values[r][c] == 0 ? "." : values[r][c]) + " ");
            }
            System.out.println();
        }
    }

}
