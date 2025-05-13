package main.java.model;

import java.util.List;

/**
 * Classe astratta che rappresenta un blocco KenKen
 */
public abstract class Block {

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
     * Metodo statico per la creazione di blocchi in base all'operatore aritmetico (+, -, *, /).
     * Attualmente centralizza la logica all'interno della classe Block.
     *
     * Da sostituire successivamente con l'implementazione del vero
     *    Factory Method Pattern: definire un'interfaccia BlockFactory e
     *    delegare la creazione a sottoclassi dedicate (es. SumBlockFactory, ecc.).
     */

    public static Block createBlock(String operator, int result, List<Cell> cells) {
        switch(operator) {
            case "+":
                return new SumBlock(result, cells);  // classi da implementare per
            case "-":
                return new SubBlock(result, cells);
            case "*":
                return new MulBlock(result, cells);
            case "/":
                return new DivBlock(result, cells);
            default:
                throw new IllegalArgumentException("Operatore non valido: " + operator);
        }
    }
}
