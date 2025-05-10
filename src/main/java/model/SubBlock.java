package main.java.model;

import java.util.List;

/** Blocco con operatore SOTTRAZIONE (-) */
public class SubBlock extends Block {

    public SubBlock(int target, List<Cell> cells) {
        super(target, cells);
    }

    @Override
    public boolean isSatisfied(int[][] grid) {
        // Inizializza il risultato con il valore della prima cella
        int result = grid[cells.get(0).getRow()][cells.get(0).getCol()];

        // Sottrae in sequenza i valori delle altre celle
        for (int i = 1; i < cells.size(); i++) {
            int val = grid[cells.get(i).getRow()][cells.get(i).getCol()];
            result -= val;
        }

        // Verifica se il valore assoluto del risultato è uguale al target
        return Math.abs(result) == target;
    }
}
