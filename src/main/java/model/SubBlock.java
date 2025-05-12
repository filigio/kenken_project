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
        return (result == target) && noDuplicates(grid);
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
