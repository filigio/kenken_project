package main.java.model;

import java.util.List;

/** Blocco con operatore SOMMA (+) */
public class SumBlock extends Block {

    public SumBlock(int target, List<Cell> cells) {// inizializza il target e le celle del blocco
        super(target, cells);
    }

    @Override
    public boolean isSatisfied(int[][] grid) {
        int sum = 0;
        boolean incomplete = false;  // Flag che indica se ci sono celle ancora vuote (== 0)

        // Somma i valori delle celle
        for (Cell c : cells) {
            int v = grid[c.getRow()][c.getCol()]; // Prende il valore dalla griglia
            if (v == 0) {
                incomplete = true;    // Se la cella è vuota, il blocco è incompleto
                continue;             // Salta al prossimo ciclo
            }
            sum += v;                 // Somma il valore
        }

        // Se ci sono celle vuote, verifica che la somma parziale sia valida (pruning)
        if (incomplete)
            return sum <= target && noDuplicates(grid); // La somma non deve superare il target
        else
            return sum == target && noDuplicates(grid); // Se completo, la somma deve essere esatta
    }
    // verifica che non ci siano duplicati in riga o colonna
    private boolean noDuplicates(int[][] grid) {
        for (int i = 0; i < cells.size(); i++)
            for (int j = i + 1; j < cells.size(); j++) {
                Cell a = cells.get(i), b = cells.get(j); // Prende due celle da confrontare
                int va = grid[a.getRow()][a.getCol()];
                int vb = grid[b.getRow()][b.getCol()];
                // Se sono sulla stessa riga o colonna e hanno lo stesso valore (diverso da 0)
                if ((a.getRow()==b.getRow()||a.getCol()==b.getCol()) && va==vb && va!=0)
                    return false; // Violazione: duplicato trovato
            }
        return true; // Nessun duplicato
    }
}
