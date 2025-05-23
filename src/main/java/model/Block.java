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

}
