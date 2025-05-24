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
        return condition && noRowColDuplicates(grid);
    }
}
