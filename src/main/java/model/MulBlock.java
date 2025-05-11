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
            return prod <= target && noDuplicates(grid);
        else
            return prod == target && noDuplicates(grid); // Se completo, deve essere esatto
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
