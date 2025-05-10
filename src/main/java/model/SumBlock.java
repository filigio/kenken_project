package main.java.model;

import java.util.List;

/** Blocco con operatore SOMMA (+) */
public class SumBlock extends Block {

    public SumBlock(int target, List<Cell> cells) {
        super(target, cells);
    }

    @Override
    public boolean isSatisfied(int[][] grid) {
        int sum = 0;

        // Somma i valori delle celle
        for (Cell c : cells) {
            sum += grid[c.getRow()][c.getCol()];
        }

        // Verifica se la somma è esattamente il target
        return sum == target;
    }
}
