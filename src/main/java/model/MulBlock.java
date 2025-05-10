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

        // Moltiplica tutti i valori delle celle nel blocco
        for (Cell c : cells) {
            prod *= grid[c.getRow()][c.getCol()];
        }

        // Verifica se il prodotto è uguale al target
        return prod == target;
    }
}
