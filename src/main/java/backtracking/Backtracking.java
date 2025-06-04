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
    private final List<int[][]> solutions = new ArrayList<>();
    private int maxToFind = 1;      // quante soluzioni cercare
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

    /** Trova AL PIÙ maxSol soluzioni e le restituisce. */
    public List<int[][]> solve(int maxSol) {
        maxToFind = Math.max(1, maxSol);     // Imposta il numero massimo di soluzioni da trovare (almeno 1)
        solutions.clear();                   // Svuota la lista di soluzioni precedenti
        dfs(0);                                // avvia la ricerca
        return new ArrayList<>(solutions);     // copia difensiva
    }

    /** Trova la PRIMA soluzione; ritorna null se impossibile. */
    public int[][] solve() {
        List<int[][]> list = solve(1);
        return list.isEmpty() ? null : list.get(0);
    }


    /* ------------------------- ricorsione ------------------------- */
    private boolean dfs(int idx) {
        if (idx == free.size()) {            // griglia completa
            solutions.add(copy(work)); // Aggiungi soluzione
            return solutions.size() >= maxToFind;
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

    /* copia profonda di una matrice n×n, per poter avere la soluzione da far vedere */
    private static int[][] copy(int[][] src) {
        int n = src.length;
        int[][] dst = new int[n][n];// Crea una nuova matrice della stessa dimensione
        for (int r = 0; r < n; r++)
            System.arraycopy(src[r], 0, dst[r], 0, n);
        return dst;
    }
}
