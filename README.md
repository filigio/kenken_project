# kenken_project


## Descrizione
Questo progetto è un'applicazione Java con interfaccia grafica (GUI) per la creazione e risoluzione di puzzle **KenKen**.  
Gli utenti possono:

- Definire una nuova griglia di gioco scegliendo la dimensione (es. 4x4, 5x5, ecc.)
- Inserire blocchi con vincoli matematici (+, −, ×, ÷)
- Risolvere automaticamente il puzzle usando un algoritmo di **backtracking**
- Impostare un **numero massimo di soluzioni** da trovare
- **Scorrere le soluzioni trovate** con i pulsanti "→" e "←"
- Attivare o disattivare il **controllo live dei vincoli** (checkbox)
- Salvare e caricare partite da file `.ser`


## Come eseguire l'applicazione
- Java 17 o superiore
- Un IDE Java (es. IntelliJ IDEA)

### Avvio da IDE:
1. **Importa il progetto** come progetto Java.
2. Apri la classe `GridView`, che si trova nel package: main.java.view
3. Avvia la classe (`Run` o tasto destro → *Run GridView.main()*


## Test del progetto:
I test sono disponibili in una cartella separata (test/), organizzata nei seguenti package:
- test.backtracking
- test.controller
- test.factory
- test.model
Nota: Non viene usato Maven. JUnit 5 è stato aggiunto manualmente tramite IntelliJ IDEA (inserito nelle librerie del progetto)

## 👨‍💻 Autore
Progetto sviluppato da **[Felice Dardis]**  
per il corso di **[Ingegneria del Software]**