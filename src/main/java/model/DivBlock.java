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
        if (cells.size() != 2) return false;

        // Ottiene i due valori dalla griglia
        int val1 = grid[cells.get(0).getRow()][cells.get(0).getCol()];
        int val2 = grid[cells.get(1).getRow()][cells.get(1).getCol()];

        // Verifica che entrambi i valori siano diversi da 0 e divisibili
        return (val1 != 0 && val2 != 0) &&
                ((val1 / val2 == target && val1 % val2 == 0) ||  // val1 ÷ val2 = target?
                        (val2 / val1 == target && val2 % val1 == 0));   // oppure val2 ÷ val1 = target?
    }
}
