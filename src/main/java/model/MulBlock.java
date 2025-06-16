package main.java.model;

import java.util.List;

/** Blocco con operatore MOLTIPLICAZIONE (×) */
public class MulBlock extends Block {

    public MulBlock(int target, List<Cell> cells) {
        super(target, cells);
    }

    @Override
    public boolean isSatisfied(int[][] grid) {
        int prod = 1;
        boolean incomplete = false;  // Flag per celle vuote

        // Moltiplica tutti i valori delle celle nel blocco
        for (Cell c : cells) {
            int v = grid[c.getRow()][c.getCol()];
            if (v == 0) {
                incomplete = true;   // Se vuoto, il blocco è incompleto
                continue;
            }
            prod *= v;               // Moltiplica il valore
        }

        // Se incompleto, il prodotto parziale non deve superare il target
        if (incomplete)
            return prod <= target && noRowColDuplicates(grid);
        else
            return prod == target && noRowColDuplicates(grid); // Se completo, deve essere esatto
    }
}
