package main.java.view;

import main.java.controller.GameController;
import main.java.factory.BlockFactoryManager;
import main.java.model.Block;
import main.java.model.Cell;
import main.java.model.Grid;
import main.java.solver.BacktrackingSolver;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.List;

public class GridView extends JFrame {

    // ====== COSTANTI UI ======
    private static final Color COLOR_BORDER_GRAY   = new Color(180, 180, 180);   // Bordo del pannello operazioni
    private static final Color COLOR_SAVE_BUTTON   = new Color(200, 200, 200);   // Colore bottoni SAVE/LOAD
    private static final Color BUTTON_COLOR     = new Color(40, 155, 255);  // colore principale dei bottoni iniziali
    private static final Color BACKGROUND_COLOR = new Color(200, 220, 255); // sfondo finestra iniziale e pannelli
    private static final Color COLOR_SECONDARY_BUTTON  = new Color(70, 130, 180);


    private static final Font BUTTON_FONT       = new Font("SansSerif", Font.BOLD, 24);  // font per pulsanti principali
    private static final Font STATUS_FONT       = new Font("SansSerif", Font.BOLD, 18);  // font per etichetta di stato
    private static final Font FONT_LABEL_SMALL  = new Font("SansSerif", Font.BOLD, 14);  // Label MaxSoluzioni
    private static final Font FONT_LABEL_NORMAL = new Font("SansSerif", Font.PLAIN, 14); // Radio buttons
    private static final Font FONT_ARROW        = new Font("SansSerif", Font.BOLD, 60);  // Frecce Next/Prev
    private static final Font FONT_SAVELOAD     = new Font("SansSerif", Font.BOLD, 16);  // Save/Load button
    private static final Font FONT_WELCOME      = new Font("SansSerif", Font.PLAIN, 30); // “Benvenuto in...”
    private static final Font TITLE_FONT        = new Font("SansSerif", Font.BOLD, 60);  // font per il titolo "KenKen"


    private static final Dimension DIM_BUTTON_LARGE  = new Dimension(250, 80);   // Nuova partita / Carica
    private static final Dimension DIM_BUTTON_WIDE   = new Dimension(250, 35);   // Solve, Change size, Blocchi
    private static final Dimension DIM_BUTTON_SMALL  = new Dimension(120, 35);   // Reset (Values)
    private static final Dimension DIM_BUTTON_COMPACT= new Dimension(120, 40);   // Save / Load
    private static final Dimension DIM_SPINNER       = new Dimension(70, 35);    // Spinner numerico
    private static final Dimension DIM_ARROW_BUTTON  = new Dimension(90, 100);   // Frecce → ←
    private static final Dimension DIM_GRID_SIZE_BTN = new Dimension(150, 70);   // Bottoni 4x4, 5x5, 6x6, 7x7


    // =========================

    private List<int[][]> solutions = new ArrayList<>();
    private int solIdx = -1;
    private GameController controller;
    private KenKenGridPanel gridPanel;
    private int size;
    private JLabel statusLabel; // Navigazione soluzioni
    private JButton prevBtn;
    private JButton nextBtn;
    private JSpinner spinMax; // Per selezionare max soluzioni
    private JPanel gridContainer; // Contenitore della griglia
    private GridStatusObserver observer; // nuovobserver aggiunto

    public GridView() {
        super("KenKen Solve"); // Titolo della finestra

        setDefaultCloseOperation(EXIT_ON_CLOSE); // Chiude il programma alla chiusura della finestra

        /** === scelta iniziale Nuova / Carica ========================= */
        Grid initialGrid = firstDialogNewOrLoad();
        if (initialGrid == null) {          // utente vuole una nuova griglia
            Integer sel = askGridSize("Scegli la dimensione della griglia:");
            if (sel == null) System.exit(0); //Se ha chiuso senza scegliere, esce
            size       = sel;
            controller = new GameController(size, new BacktrackingSolver()); // Crea nuova partita con grandezza size
        } else {                            // caricata da file
            size       = initialGrid.getSize();
            controller = new GameController(size, new BacktrackingSolver()); // Crea il controller con stessa dimensione
            controller.getGrid().setValues(initialGrid.getValuesCopy());  //Copia i valori
            controller.getGrid().getBlocks().addAll(initialGrid.getBlocks()); //Copia i blocchi
        }

        setLayout(new BorderLayout()); // Layout principale

        //  statusLabel in alto inizzialmente vuoto
        statusLabel = new JLabel(" ");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(STATUS_FONT);
        add(statusLabel, BorderLayout.NORTH);

        gridPanel = new KenKenGridPanel(controller); // Inizializza il pannello della griglia

        // Creo contenitore della griglia
        gridContainer = new JPanel(new GridBagLayout());
        gridContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        gridContainer.add(gridPanel);

        add(gridContainer, BorderLayout.CENTER); // Aggiunge la griglia al centro
        add(buildSidePanel(), BorderLayout.EAST); // Aggiunge il pannello laterale a destra

        statusLabel = new JLabel("Griglia pronta");    // Etichetta che mostra lo stato
        add(statusLabel, BorderLayout.NORTH);                 // Aggiungila nella parte superiore della GUI

        observer = new GridStatusObserver(controller, statusLabel, prevBtn, nextBtn, gridPanel);
        controller.addObserver(observer);
        pack(); // Dimensionamento automatico della finestra
        setLocationRelativeTo(null); // Centra la finestra sullo schermo
        setVisible(true); // Rende visibile la GUI
    }

    /** ======================= finestra inizialer Nuova/Carica ====================== */

    private Grid firstDialogNewOrLoad() {

        /** Bottoni */
        JButton btnNewGrid = new JButton("+ CREA PARTITA");
        btnNewGrid.setFont(BUTTON_FONT);
        btnNewGrid.setBackground(BUTTON_COLOR);
        btnNewGrid.setForeground(Color.BLACK); // Testo
        btnNewGrid.setFocusPainted(false);
        btnNewGrid.setFocusable(false); //  Disattiva focus iniziale
        btnNewGrid.setPreferredSize(DIM_BUTTON_LARGE);

        JButton btnLoadGrid = new JButton("CARICA PARTITA");
        btnLoadGrid.setFont(BUTTON_FONT);
        btnLoadGrid.setBackground(BUTTON_COLOR);
        btnLoadGrid.setForeground(Color.BLACK); // Testo
        btnLoadGrid.setFocusPainted(false);
        btnLoadGrid.setFocusable(false); // Disattiva focus iniziale
        btnLoadGrid.setPreferredSize(DIM_BUTTON_LARGE);


        // Primo titolo piccolo "Benvenuto in..."
        JLabel welcomeLabel = new JLabel("Benvenuto in...");
        welcomeLabel.setFont(FONT_WELCOME); // Font più piccolo
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);      // Centra il testo orizzontalmente
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);          // Centra anche rispetto all'asse X
        // Titolo
        JLabel titleLabel = new JLabel("KenKen");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Pannello principale
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 30, 30, 30));


        mainPanel.add(welcomeLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(100)); // Spazio bello ampio dopo KenKen

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        buttonsPanel.setBackground(BACKGROUND_COLOR);
        buttonsPanel.add(btnNewGrid);
        buttonsPanel.add(btnLoadGrid);

        mainPanel.add(buttonsPanel);
        mainPanel.add(Box.createVerticalGlue());

        // Crea il JDialog
        JDialog dialog = new JDialog(this, "KenKen", true); // TRUE = bloccante
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);  // Chiude solo la finestra, non l'intera app
        dialog.getContentPane().add(mainPanel);
        dialog.setSize(700, 500);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);

        final Grid[] gridResult = new Grid[1];
        final boolean[] buttonClicked = {false}; // Aggiunto flag per verifiacre che il bottone è stato premuto

        btnNewGrid.addActionListener(e -> {                               // Listener per bottone "Nuova"
            gridResult[0] = null;
            buttonClicked[0] = true; //Segno che ha premuto
            dialog.dispose();
        });

        btnLoadGrid.addActionListener(e -> {       // Listener per bottone "Carica"
            JFileChooser fc = new JFileChooser();        // Crea selettore file
            if (fc.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) { // Se l'utente conferma la selezione
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fc.getSelectedFile()))) {
                    gridResult[0] = (Grid) ois.readObject();   // Tenta di leggere l'oggetto Grid dal file
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Errore caricamento: " + ex.getMessage());
                    System.exit(1);               // Esce dal programma in caso di errore
                }
            }
            buttonClicked[0] = true;             // Segna che è stato premuto
            dialog.dispose();                 // Chiude la finestra
        });

        dialog.setVisible(true);

        // Quando la finestra viene chiusa
        if (!buttonClicked[0]) { // Se NON ha premuto nessun bottone
            System.exit(0); // Esco subito
        }

        return gridResult[0];
    }


    /** Mostra un dialogo per far scegliere all'utente la dimensione della griglia */

    private Integer askGridSize(String message) {

        //  Elenco dimensioni disponibili
        Integer[] sizes = {3, 4, 5, 6}; // Se vuoi aggiungere 7, basta scrivere {3,4,5,6,7} Utilizzando un ciclo per creare i bottoni in modo dinamico

        // Titolo
        JLabel titleLabel = new JLabel("Seleziona la Dimensione della Griglia");
        titleLabel.setFont(BUTTON_FONT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);  // Allineamento orizzontale centrato
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);    // Allineamento all’interno del BoxLayout

        // Pannello bottoni dinamici
        JPanel buttonsPanel = new JPanel(new GridLayout(0, 2, 20, 20)); // 2 colonne flessibili
        buttonsPanel.setBackground(BACKGROUND_COLOR);

        final Integer[] selectedSize = {null};   // Variabile per salvare la scelta dell’utent

        for (Integer size : sizes) {   // Per ogni dimensione disponibile
            JButton btn = createGridSizeButton(size + " x " + size, size, COLOR_SECONDARY_BUTTON ); // Crea bottone
            btn.addActionListener(e -> {   // Listener per il click sul bottone
                selectedSize[0] = size;    // Salva la dimensione selezionata
                SwingUtilities.getWindowAncestor(btn).dispose(); // Chiude il dialog
            });
            buttonsPanel.add(btn);   // Aggiunge il bottone al pannello
        }

        // Pannello principale
        JPanel mainPanel = new JPanel();   // Crea contenitore principale
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(buttonsPanel);

        JDialog dialog = new JDialog(this, "Scegli Dimensione", true);
        dialog.getContentPane().add(mainPanel);
        dialog.setSize(700, 500);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        if (selectedSize[0] == null) {
            System.exit(0); // Se chiude senza scegliere, esci
        }

        return selectedSize[0];
    }

    // Metodo di supporto per creare i bottoni
    private JButton createGridSizeButton(String text, int size, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(BUTTON_FONT);
        btn.setBackground(color);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setFocusable(false);    //pre-focus
        btn.setPreferredSize(DIM_GRID_SIZE_BTN);
        return btn;
    }

    /**
     * Mostra un dialogo per scegliere una nuova dimensione della griglia.
     * Se confermata, ricrea il controller e aggiorna l’interfaccia con la nuova dimensione.
     */
    private void onChangeSize() {
        Integer newSize = askGridSize("Nuova dimensione della griglia:");
        if (newSize == null || newSize.equals(size)) return;

        size = newSize;
        controller = new GameController(size, new BacktrackingSolver());
        clearSolutions();

        remove(gridContainer); //  Rimuovo tutto il contenitore vecchio

        gridPanel = new KenKenGridPanel(controller);

        // Creo nuovo contenitore aggiornato
        gridContainer = new JPanel(new GridBagLayout());
        gridContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));


        gridContainer.add(gridPanel);

        add(gridContainer, BorderLayout.CENTER); // ➡️ Aggiungo di nuovo il contenitore completo


        revalidate();
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Cancella tutte le soluzioni trovate, azzera l'indice e aggiorna l'interfaccia disabilitando i controlli.
     */
    private void clearSolutions() {
        solutions = Collections.emptyList();   // Svuota lista soluzioni
        solIdx = -1;                            // Reset indice soluzione
        prevBtn.setEnabled(false);              // Disabilita Previous
        nextBtn.setEnabled(false);              // Disabilita Next
        statusLabel.setText(" ");               // Svuota statusLabel

        // Controlla che observer esista prima di notificare // per non avitare eccezioni se l'observer non esiste
        if (observer != null) {
            observer.clearSolutions();          // notifica solo se observer non è null
        }
    }



    /** ======================= pannello laterale ====================== */
    private JPanel buildSidePanel() {
        // ====================Creazione del pannello verticale====================
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        // ===============Bordo del pannello intitolato “OPERAZIONI”==============
        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_BORDER_GRAY, 1),
                "OPERAZIONI");
        tb.setTitleJustification(TitledBorder.CENTER);
        tb.setTitleFont(STATUS_FONT);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                tb
        ));

        /** modalità */
        // Permette di cambiare modalità
        JRadioButton rbDef = createButton("Definisci blocchi");
        JRadioButton rbIns = createButton("Inserisci numeri");

        ButtonGroup group = new ButtonGroup(); // Raggruppa i radio button
        group.add(rbDef);
        group.add(rbIns);
        // Gestione cambio modalità
        rbDef.setSelected(true);//impostato già a True
        rbDef.addActionListener(e -> gridPanel.setMode(KenKenGridPanel.Mode.DEFINE_BLOCKS));
        rbIns.addActionListener(e -> gridPanel.setMode(KenKenGridPanel.Mode.INSERT_NUMBERS));

        p.add(rbDef);
        p.add(rbIns);
        p.add(Box.createVerticalStrut(10));


        /** Controllo live */
         JCheckBox liveCheck = new JCheckBox("Controllo vincoli live", true);  // checkbox per abilitare/disabilitare il controllo live
         liveCheck.setFont(FONT_LABEL_SMALL);
         liveCheck.setOpaque(false);
         liveCheck.setAlignmentX(Component.CENTER_ALIGNMENT);
         gridPanel.setLiveCheckSupplier(liveCheck::isSelected);                  // passo al KenKenGridPanel un Supplier che legge lo stato della checkbox
         p.add(liveCheck);

         p.add(Box.createVerticalStrut(20));


        /** Max Solution */
        JPanel spinPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); //  Layout centrato perfetto
        spinPanel.setOpaque(false);

        //  Creazione della label "Max Solutions" ingrandita
        JLabel spinLabel = new JLabel("Max Solutions:");
        spinLabel.setFont(FONT_LABEL_SMALL); // Font aumentato a 16pt, in grassetto
        spinMax = new JSpinner(new SpinnerNumberModel(5, 1, 100, 1));
        spinMax.setPreferredSize(DIM_SPINNER);
        //  Ingrandisci anche il numero dentro lo spinner
        JComponent editor = spinMax.getEditor();
        JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
        textField.setFont(STATUS_FONT); // Numeri grandi e bold
        textField.setHorizontalAlignment(JTextField.CENTER);    // Numeri centrati dentro il campo

        spinPanel.add(spinLabel);
        spinPanel.add(spinMax);
        p.add(spinPanel);

        p.add(Box.createVerticalStrut(20));


        /** Solve */
        p.add(createLargeButton("SOLVE", e -> onSolve()));
        p.add(Box.createVerticalStrut(10));

        /** Next e Previus*/
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        prevBtn = createNavigationButton("←", e -> showSolution(solIdx - 1));
        nextBtn = createNavigationButton("→", e -> showSolution(solIdx + 1));
        prevBtn.setEnabled(false); //per disattivare il pulsante
        nextBtn.setEnabled(false);
        navPanel.add(prevBtn);
        navPanel.add(nextBtn);
        p.add(navPanel);

        p.add(Box.createVerticalStrut(20));

        /** Aggiungi blocco*/
        p.add(createLargeButton("AGGIUNGI BLOCCO", e -> onAddBlock()));
        p.add(Box.createVerticalStrut(10));

        /** Reset*/
        // Pulsante per resettare solo i valori (senza rimuovere blocchi)
        JPanel resetPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        resetPanel.setOpaque(false);
        JButton resetVals = createSmallButton("Reset (values)", e -> {
            controller.resetGrid();      // Azzera solo i valori numerici
            gridPanel.repaint();         // Ridisegna la griglia aggiornata
        });
        // Pulsante per resettare tutto (valori + blocchi)
        JButton resetAll = createSmallButton("Reset All", e -> {
            controller.resetGrid();                          // Azzera i valori
            controller.getGrid().getBlocks().clear();        // Rimuove tutti i blocchi
            gridPanel.repaint();                             // Aggiorna visivamente
        });
        resetPanel.add(resetVals); // Aggiunge il pulsante al pannello
        resetPanel.add(resetAll);
        p.add(resetPanel);

        p.add(Box.createVerticalStrut(20));

        /** Change Size*/
        // Pulsante per cambiare la dimensione della griglia
        p.add(createLargeButton("CHANGE SIZE", e ->onChangeSize()));

        p.add(Box.createVerticalStrut(20));


        /** Save e Load*/
        JPanel SaveLoadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        SaveLoadPanel.setOpaque(false);
        JButton save = createSaveLoadButton("Save", e -> onSave());
        JButton load = createSaveLoadButton("Load", e -> onLoad());

        SaveLoadPanel.add(save);
        SaveLoadPanel.add(load);
        p.add(SaveLoadPanel);
        p.add(Box.createVerticalStrut(10));

        return p;
    }

    //============Metodi di appoggio per la creazione dei vari Bottoni===============
    // RadioButton
    private JRadioButton createButton(String text) {
        JRadioButton rb = new JRadioButton(text);
        rb.setFont(FONT_LABEL_NORMAL);
        rb.setFocusPainted(false);
        rb.setOpaque(false);
        rb.setAlignmentX(Component.CENTER_ALIGNMENT);
        return rb;
    }

    // Bottoni grandi
    private JButton createLargeButton(String text, java.awt.event.ActionListener action) {
        JButton b = new JButton(text);
        b.setFont(BUTTON_FONT);
        b.setBackground(Color.WHITE);
        b.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        b.setFocusPainted(false);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);

        // larghezza e altezza identici per tutti
        b.setPreferredSize(DIM_BUTTON_WIDE);
        b.setMaximumSize(DIM_BUTTON_WIDE); // Fix per BoxLayout (stessa larghezza sempre)

        b.addActionListener(action);
        return b;
    }


    // Bottoni freccia grandi e solo freccia cliccabile
    private JButton createNavigationButton(String arrow, java.awt.event.ActionListener action) {
        JButton b = new JButton(arrow);
        b.setFont(FONT_ARROW); //font
        b.setFocusPainted(false);
        b.setContentAreaFilled(false); // Nessun riempimento rettangolare in modo da non vedere il bottone
        b.setBorderPainted(false);     // bordo del bottone non visibile
        b.setOpaque(false);
        b.setPreferredSize(DIM_ARROW_BUTTON); //spazio
        b.addActionListener(action);
        return b;
    }

    // Bottoni normali piccoli
    private JButton createSmallButton(String text, java.awt.event.ActionListener action) {
        JButton b = new JButton(text);
        b.setFont(FONT_LABEL_SMALL);
        b.setBackground(Color.WHITE);
        b.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        b.setFocusPainted(false);
        b.setPreferredSize(DIM_BUTTON_SMALL);
        b.addActionListener(action);
        return b;
    }

    // Bottoni rettangolari senza bordo (per SAVE/LOAD)
    private JButton createSaveLoadButton(String text, java.awt.event.ActionListener action) {
        JButton b = new JButton(text);
        b.setFont(FONT_SAVELOAD);
        b.setBackground(COLOR_SAVE_BUTTON); // Sfondo leggerissimo grigio chiaro
        b.setForeground(Color.BLACK);              // Testo nero
        b.setFocusPainted(true);                   // Nessuna evidenziazione clic
        b.setContentAreaFilled(false);                // Riempi sfondo normalmente
        b.setOpaque(true);                          // Sfondo opaco
        b.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Bordino nero sottilissimo
        b.setPreferredSize(DIM_BUTTON_COMPACT); // Dimensione più compatta
        b.addActionListener(action);

        return b;
    }


    /**
     * Metodo chiamato quando l'utente clicca su "Aggiungi Blocco".
     * Permette di creare un blocco matematico (vincolo) sulle celle selezionate.
     */
    private void onAddBlock() {
        List<Cell> cells = gridPanel.consumeSelectedCells(); // Ottiene e svuota selezione
        if (cells.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nessuna cella selezionata!");
            return;
        }

        // Controllo che le celle non siano già in uso
        if (!cellsAreAllFree(cells)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Errore: alcune celle selezionate appartengono già ad un altro blocco!"
            );
            return;
        }
        // Controllo che formino un’unica area connessa
        if (!cellsAreConnected(cells)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Errore: le celle non formano un’area continua!"
            );
            return;
        }

        // Chiede all’utente l’operatore e il target
        String op = JOptionPane.showInputDialog(this, "Operatore (+, -, *, /):");
        if (op == null) return;

        String tgt = JOptionPane.showInputDialog(this, "Risultato del blocco:");
        if (tgt == null) return;

        try {
            int target = Integer.parseInt(tgt); // Converte il target
            Block b = BlockFactoryManager.getFactory(op.trim())
                    .createBlock(target, cells);   // Crea il blocco
            controller.addBlock(b); // Aggiunge alla griglia
            gridPanel.repaint(); // Ridisegna la griglia
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Target non valido!");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
        }
    }

    /** Inserisce la prima soluzione se esiste, altrimenti mostra un messaggio. */
    private void onSolve() {
        int maxSol = (Integer) spinMax.getValue(); // prendo numero massimo soluzioni richieste

        if (maxSol < 0) {
            JOptionPane.showMessageDialog(this, "Inserire un numero ≥ 0!");
            return;
        }

        if (controller.isGridComplete()) { // se la griglia è già compeltamtne riempita
            String err = controller.validateCurrentGrid(); // verifico rispetti tutti i vincoli
            JOptionPane.showMessageDialog(this,
                    (err == null) ? "La griglia è completa e CORRETTA!"
                            : "La griglia è completa ma NON corretta:\n" + err);
            return; // esco perchè non serve cercare soluzioni
        }

        int ans = JOptionPane.showConfirmDialog( // chiedo conferma all'utente prima di lanciare il solver
                this,
                "La griglia non è completa.\nVuoi cercare " +
                        (maxSol == 0 ? "tutte le possibili" : "fino a " + maxSol) + " soluzioni?",
                "Trova soluzione?",
                JOptionPane.YES_NO_OPTION);

        if (ans != JOptionPane.YES_OPTION) return;

        solutions = controller.solvePuzzle(maxSol); // Risolve la griglia
        observer.setSolutions(solutions); // Comunica all'observer quante soluzioni abbiamo

        if (solutions.isEmpty()) {// Se non ci sono soluzioni, avviso l’utente e pulisco lo stato
            JOptionPane.showMessageDialog(this, "Nessuna soluzione trovata.");
            clearSolutions(); // per riposrtare il pannello di stato allo stato iniziale
            return;
        }

        // Mostra la prima soluzione trovata
        int[][] sol = solutions.get(0);
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                controller.setGridValue(r, c, sol[r][c]); // Applica la soluzione

        // se trovo almeno una soluzione mostro la prima
        solIdx = 0;
        showSolution(solIdx);

        JOptionPane.showMessageDialog(this, //informo l'utente delle soluzioni trovate
                "Trovate " + solutions.size() + " soluzioni (massimo richiesto: " + maxSol + ").");
    }


    /** Metodo per visualizzare una soluzione specifica (in base all'indice)*/
    private void showSolution(int idx) {
        if (idx < 0 || idx >= solutions.size()) return;  // Evita errori se l'indice è fuori dai limiti della lista

        solIdx = idx;           // Aggiorna l'indice corrente della soluzione mostrata

        int[][] sol = solutions.get(solIdx);        // Recupera la soluzione da visualizzare
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                controller.setGridValue(r, c, sol[r][c]);
            }
        }

        gridPanel.repaint();

        // Aggiorno la statusLabel dopo aver mostrato una nuova soluzione
        int totale = solutions.size();
        statusLabel.setText(
                "Soluzione " + (solIdx + 1) +
                        " di " + totale +
                        " (richieste: " + spinMax.getValue() + ")"
        );

        //  Aggiorno sempre abilitazione bottoni Previous/Next
        prevBtn.setEnabled(solIdx > 0);                   // Abilita Previous solo se non sei sulla prima
        nextBtn.setEnabled(solIdx < solutions.size() - 1); // Abilita Next solo se non sei sull'ultima
    }

    /** Metodo che gestisce il salvataggio della partita su file
     Permette all’utente di salvare su file .ser (serializzato) e ricaricarlo */
    private void onSave() {
        JFileChooser fc = new JFileChooser(); // Crea selettore file
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                controller.saveGrid(fc.getSelectedFile()); // Salva la griglia nel file scelto
                JOptionPane.showMessageDialog(this, "Partita salvata con successo!");
            } catch (Exception ex) {
                // Mostra errore se qualcosa va storto
                JOptionPane.showMessageDialog(this, "Errore durante il salvataggio: " + ex.getMessage());
            }
        }
    }
    /**
     * Apre una finestra per caricare una griglia salvata da file.
     * Se il file è valido, ricostruisce l'interfaccia e il controller con la griglia caricata.
     */
    private void onLoad() {
        JFileChooser fc = new JFileChooser();// Apro un file chooser per far selezionare all’utente il file da caricare
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return; // Se l’utente non conferma l’apertura, esco subito dal metodo

        Grid loaded;
        // Provo a leggere l’oggetto Grid salvato su file, chiudendo automaticamente lo stream
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fc.getSelectedFile()))) {
            loaded = (Grid) ois.readObject(); // Deserializzo la griglia
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore caricamento: " + ex.getMessage());
            return;
        }

        // Prelevo la dimensione della griglia caricata
        int newSize = loaded.getSize();
        if (newSize != size) { // Se la size è diversa da quella corrente, chiedo conferma
            int ans = JOptionPane.showConfirmDialog(this,
                    "Il file è " + newSize + "×" + newSize + ". Sostituire la griglia corrente?",
                    "Dimensione diversa", JOptionPane.YES_NO_OPTION);
            if (ans != JOptionPane.YES_OPTION) return; //se l'utente dice no, esco
        }

        // Ricreo un nuovo controller con la size (resettando lo stato)
        controller = new GameController(newSize, new BacktrackingSolver());
        // Ripristino i valori e i blocchi dalla griglia caricata
        controller.getGrid().setValues(loaded.getValuesCopy());
        controller.getGrid().getBlocks().addAll(loaded.getBlocks());
        size = newSize;

        // Rimuovo il vecchio contenitore
        remove(gridContainer);

        gridPanel = new KenKenGridPanel(controller);  // Creo un nuovo KenKenGridPanel basato sul nuovo controller

        // Creo nuovo contenitore centrato
        gridContainer = new JPanel(new GridBagLayout());
        gridContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));


        gridContainer.add(gridPanel);// Aggiungo il nuovo pannello della griglia al container

        add(gridContainer, BorderLayout.CENTER);

        clearSolutions();// Svuoto eventuali soluzioni precedenti e resetto l’indice
        revalidate();// informare che al stutura dei componenti è cambiata
        pack();  // attendo finestra
        setLocationRelativeTo(null);

        //Ricreo observer
        observer = new GridStatusObserver(controller, statusLabel, prevBtn, nextBtn, gridPanel);
        controller.addObserver(observer);

        JOptionPane.showMessageDialog(this, "Caricato con successo!");
    }


    // Metodi di validazione selezione blocchi
    /**
     * Verifica che tutte le celle selezionate non appartengano già a un altro blocco.
     */
    private boolean cellsAreAllFree(List<Cell> cells) {
        // Scorro tutti i blocchi esistenti nella griglia
        for (Block b : controller.getGrid().getBlocks()) {
            //e per ciascun blocco controllo ogni sua cella
            for (Cell c : b.getCells()) {
                // Se la selezione contiene questa cella, c’è sovrapposizione
                if (cells.contains(c)) {
                    return false; // sovrapposizione trovata
                }
            }
        }
        return true; // tutte libere
    }


    /**
     * Verifica che le celle selezionate formino un’area continua (adiacenti 4-direzioni).
     */
    /*
    insieme di celle selezionate in una griglia forma un’unica “area” continua,
    considerando l’adiacenza ortogonale (su, giù, sinistra, destra).
     */
    private boolean cellsAreConnected(List<Cell> list) {
        if (list.size() <= 1) { //caso base
            return true; // 0 o 1 elemento è sempre connesso
        }
        // Preparo un set di celle “da visitare”
        Set<Cell> remaining = new HashSet<>(list);
        Deque<Cell> queue = new ArrayDeque<>();
        queue.add(list.get(0)); // prendiamo la prima cella e la inseriamo nella coda
        remaining.remove(list.get(0)); //appena messe in coda le eliminiamo per non doverla risaminare

        // Direzioni: su, giù, sinistra, destra
        int[] dr = {-1, 1, 0, 0}, dc = {0, 0, -1, 1};

        // Finché ho celle in coda
        while (!queue.isEmpty()) {
            Cell cur = queue.poll(); // estraggo la prossima cella da esplorare
            // Controllo le quattro celle adiacenti
            for (int k = 0; k < 4; k++) {
                Cell next = new Cell(
                        cur.getRow() + dr[k],
                        cur.getCol() + dc[k]
                );
                // Se la cella adiacente è tra quelle da visitare
                if (remaining.remove(next)) {
                    queue.add(next); // la aggiungo alla coda
                }
            }
        }

        // Se alla fine non rimane nulla, tutte le celle erano connesse
        return remaining.isEmpty();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GridView::new);
    }
}