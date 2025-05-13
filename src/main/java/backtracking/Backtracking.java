package main.java.backtracking;

import main.java.model.Block;
import main.java.model.Grid;
import main.java.model.Cell;

import java.util.ArrayList;
import java.util.List;

public class Backtracking {
    private final Grid grid;
    private final int n; //dimensione griglia
    private final int[][] work; //matrice di lavro
    private final List<Cell> free;       // celle libere (in ordine riga-colonna) che verranno esplorate dal backtrcking

    public Backtracking(Grid g) {
        this.grid = g;
        this.n =g.getSize();
        this.work = g.getValuesCopy();
        this.free = new ArrayList<>();

        //lista delle celle vuote, ossia le posizioni della griglia che devono ancora essere riempite.
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++)
                if (work[r][c] == 0)
                    free.add(new Cell(r, c));
    }
    /**
     * Trova la prima soluzione (per il momento trovaimo la rima soluzione, per vedere se funziona correttamente la calsse.
     * Successivamente implemntiamo per avere più soluzioni possibili)
     */
    public int[][] solve() {
        dfs(0);
        return work;
    }
    /* ------------------------- ricorsione ------------------------- */
    private boolean dfs(int idx) {
        if (idx == free.size()) {            // griglia completa
            return true;
        }

        Cell p = free.get(idx);
        for (int v = 1; v <= n; v++) {
            if (isSafe(p, v)) {
                work[p.getRow()][p.getCol()] = v;
                if (dfs(idx + 1)) return true;   // trovato
                work[p.getRow()][p.getCol()] = 0; // back-track
            }
        }
        return false;
    }

    /* ---------------------- vincoli riga/colonna + blocchi ---------------- */
    //verifica se è sicuro inserire il valore v nella cella p secondo le regole del KenKen.
    private boolean isSafe(Cell p, int v) {
        int r = p.getRow();
        int c = p.getCol();

        for (int i = 0; i < n; i++)          // riga / colonna
            if (work[r][i] == v || work[i][c] == v) return false;

        int old = work[r][c];              // Salva il valore originale della cella (di solito è 0)
        work[r][c] = v;                    // Inserisce temporaneamente il valore 'v' nella cella
        for (Block b : grid.getBlocks())   // Per ogni blocco della griglia
            if (!b.isSatisfied(work)) {    // controlla se è ancora valido con il nuovo valore
                work[r][c] = old;          // Se almeno un blocco è violato, ripristina la cella
                return false;              // e dice che il valore non è valido
            }
        work[r][c] = old;                  // Ripristina comunque il valore iniziale della cella

        //se entrambi i vincoli sono rispettati
        return true; //puoi inserire v in p
    }
}
