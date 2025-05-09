package main.java.model;

/**
 * Rappresenta una singola cella (row, col) nella griglia KenKen
 */
public class Cell {
    private final int row;
    private final int col;

    public Cell(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    @Override
    public boolean equals(Object obj) { // due celle sono uguali se hanno stesse cordinate
        if (!(obj instanceof Cell)) return false;
        Cell other = (Cell) obj;
        return this.row == other.row && this.col == other.col;
    }
}
