package main.java.model;

import java.io.Serializable;
import java.util.List;

/**
 * Classe astratta che rappresenta un blocco KenKen
 */
public abstract class Block implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int target; // risultato atteso dell'operazione del blocco ()
    protected List<Cell> cells; // celle del blocco

    public Block(int target, List<Cell> cells) {
        this.target = target;
        this.cells = cells;
    }

    public int getTarget() { return target; } // il risultato atteso del blocco
    public List<Cell> getCells() { return cells; }
    public abstract boolean isSatisfied(int[][] grid); // verifica per i vincoli dei blocchi
    // da implemntare i vari vincoli per i blocchi (come +-*/)

    /**
     * Verifica che non esistano duplicati (ovviamente che non siano ≠0) su stessa riga o colonna
     * fra tutte le celle di questo blocco.
     */
    protected boolean noRowColDuplicates(int[][] grid) {
        for (int i = 0; i < cells.size(); i++) {
            for (int j = i + 1; j < cells.size(); j++) {
                Cell c1 = cells.get(i);// Prende due celle da confrontare ossia la i-esima e la j-esima
                Cell c2 = cells.get(j);
                //poi prendo i rispettivi valori effettivi della cella
                int v1 = grid[c1.getRow()][c1.getCol()];
                int v2 = grid[c2.getRow()][c2.getCol()];
                // Se sono sulla stessa riga o colonna e hanno lo stesso valore (diverso da 0)
                if ((c1.getRow() == c2.getRow() || c1.getCol() == c2.getCol()) && v1 == v2 && v1 != 0) {
                    return false;// Violazione: duplicato trovato
                }
            }
        }
        return true;
    }
}
