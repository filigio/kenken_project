package main.java.model;

import java.util.List;

/** Blocco con operatore DIVISIONE (÷) */
public class DivBlock extends Block {

    public DivBlock(int target, List<Cell> cells) {
        super(target, cells);
    }

    @Override
    public boolean isSatisfied(int[][] grid) {
        // Controlla che il blocco abbia esattamente 2 celle (tipico per la divisione)
        if (cells.size() < 2) {
            int val = grid[cells.get(0).getRow()][cells.get(0).getCol()]; // Ottiene il valore dalla griglia
            if (val == 0) return true; // Se la cella è vuota, il blocco è incompleto → accettabile
            return val == target; // Altrimenti, il valore deve corrispondere esattamente al target
        }

        // Ottiene i due valori dalla griglia
        int val1 = grid[cells.get(0).getRow()][cells.get(0).getCol()];
        int val2 = grid[cells.get(1).getRow()][cells.get(1).getCol()];

        // Se uno dei due è ancora vuoto → stato incompleto → accettabile per ora
        if (val1 == 0 || val2 == 0) {
            return true;
        }

        boolean condition = false; // Flag per verificare se la divisione è corretta

        // Controlla se val1 / val2 == target (solo se val2 ≠ 0 e divisione intera)
        if (val2 != 0 && val1 % val2 == 0 && (val1 / val2) == target) {
            condition = true;
        }

        // Controlla anche la divisione inversa (val2 / val1)
        if (val1 != 0 && val2 % val1 == 0 && (val2 / val1) == target) {
            condition = true;
        }

        // Ritorna true solo se la condizione di divisione è rispettata e non ci sono duplicati
        return condition && noDuplicates(grid);
    }

    //Verifico assenza duplicati
    private boolean noDuplicates(int[][] grid) {
        for (int i = 0; i < cells.size(); i++) {
            for (int j = i + 1; j < cells.size(); j++) {
                Cell c1 = cells.get(i);// Prende due celle da confrontare
                Cell c2 = cells.get(j);
                int v1 = grid[c1.getRow()][c1.getCol()];
                int v2 = grid[c2.getRow()][c2.getCol()];
                // Se sono sulla stessa riga o colonna e hanno lo stesso valore (diverso da 0)
                if ((c1.getRow() == c2.getRow() || c1.getCol() == c2.getCol()) && v1 == v2 && v1 != 0) {
                    return false;// Violazione: duplicato trovato
                }
            }
        }
        return true;// Nessun duplicato
    }
}
