package main.java.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**Rappresenta la griglia di gioco KenKen
 * Per gestire lo stato corrente del gioco e i vincoli dei blocchi
 */
public class Grid implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int size; // Dimensione griglia
    private final int[][] values; //la matrice dei valori correnti delle celle
    private final List<Block> blocks; //lista dei blocchi della griglia (ogni cella deve appartenere ad un blcco)

    public Grid(int size) {
        this.size = size;
        this.values = new int[size][size];
        this.blocks = new ArrayList<>();
    }

    public int getSize() { return size; } //dimensione griglia

    public int[][] getValuesCopy() { // Crea una nuova matrice di interi della stessa dimensione della griglia originale
        //facendo una copia profonda della matrice originale
        int[][] copy = new int[size][size];
        for(int r = 0; r < size; r++) {
            System.arraycopy(values[r], 0, copy[r], 0, size);
        }
        return copy;
    }
    public int getValue(int row, int col) { return values[row][col]; } //valore Corrente di una cella
    public void setValue(int row, int col, int value) { values[row][col] = value; } //imposta valore di una cella
    public void addBlock(Block block) { blocks.add(block); } //per aggiungere un vincolo o blocco alla griglia
    public List<Block> getBlocks() { return blocks; }

    /** Metodo che azzera tutti i valori della griglia di gioco */
    public void clear() {
        // Scorre ogni riga della griglia
        for(int r = 0; r < size; r++) {
            for(int c = 0; c < size; c++) {
                values[r][c] = 0; // ed imposta il valore della cella[r][c] a 0
            }
        }
    }
}