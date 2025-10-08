package Model;

import java.util.*;

/**
 * gestice i dati della classifica
 * delle partite giocate, utilizza il singleton pattern
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class Classifica {
    /**
     * variabile statica per l'unica istanza della classifica
     */
    private static Classifica instance;
    /**
     * i dati della classifica
     */
    private final Map<String, Integer> recordGiocatori;

    /**
     * crea la classifica usando una hashmap
     */
    private Classifica() {
        recordGiocatori = new HashMap<>();
    }

    /**
     * singleton pattern: se esiste già un'istanza di un oggetto classifica,
     * viene restituita, altrimenti viene creata
     * @return l'istanza di Classifica
     */
    public static Classifica getInstance() {
        if (instance == null) {
            instance = new Classifica();
        }
        return instance;
    }

    /**
     * aggiunge un entry nella classifica
     * @param nomeGiocatore il nome del giocatore
     * @param vittorie il numero di vittorie consecutive del giocatore
     */
    public void aggiungiPartita(String nomeGiocatore, int vittorie) {
        recordGiocatori.put(nomeGiocatore,
                Math.max(vittorie, recordGiocatori.getOrDefault(nomeGiocatore, 0)));
    }

    /**
     * ritorna una lista con la top 10 delle partite in base al numero
     * di vittorie
     * @return la lista di partite
     */
    public List<Map.Entry<String, Integer>> getTop10() {
        return recordGiocatori.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // decrescente
                .limit(10)
                .toList();
    }

    /**
     * ritorna il valore booleano per rappresentare
     * se la classifica è vuota
     * @return
     */
    public boolean isEmpty() {
        return recordGiocatori.isEmpty();
    }
}
