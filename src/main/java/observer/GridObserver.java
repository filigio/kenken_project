package main.java.observer;

public interface GridObserver {

    void onGridChanged();   // Metodo che verrà chiamato dal controller
    // ogni volta che la griglia viene modificata
}
