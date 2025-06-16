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

## Configurazione librerie (Junit 5)
Per eseguire i test del progetto, è necessario configurare manualmente JUnit 5. Ecco come fare:

scaricare le librerie già fornite nella cartella "lib"

In caso di IntelliJ per configurarlo basta:
1. Vai su File > Project Structure > Modules > Dependencies
2. Clicca su + > JARs or directories
3. Seleziona **tutti i file nella cartella lib**
4. Imposta lo **Scope su Compile** 
5. Applica e chiudi

Imposta la cartella test/ come sorgente di test
1. Vai su Project
2. Clic destro sulla cartella test/ > Mark Directory as > Test Sources Root


## 👨‍💻 Autore
Progetto sviluppato da **[Felice Dardis]**  
per il corso di **[Ingegneria del Software]**

