package main.java.model;

import java.util.List;

/** Blocco con operatore SOTTRAZIONE (-) */
public class SubBlock extends Block {

    public SubBlock(int target, List<Cell> cells) {
        super(target, cells);
    }

    @Override
    public boolean isSatisfied(int[][] grid) {
        // Prende il primo valore dal blocco
        int firstVal = grid[cells.get(0).getRow()][cells.get(0).getCol()];
        if (firstVal == 0) return true; // Se è vuoto, lo stato è incompleto -> accettabile per ora

        int result = firstVal; // Inizializza il risultato con il primo valore

        // Sottrae in sequenza i valori delle altre celle
        for (int i = 1; i < cells.size(); i++) {
            int val = grid[cells.get(i).getRow()][cells.get(i).getCol()];
            if (val == 0) return true; // Se la cella è vuota, stato incompleto -> accettabile
            result = Math.abs(result - val); //aggiorna il risultato con la sottrazione assoluta
        }

        // Verifica se il valore assoluto del risultato è uguale al target
        return (result == target) && noRowColDuplicates(grid);
    }
}
