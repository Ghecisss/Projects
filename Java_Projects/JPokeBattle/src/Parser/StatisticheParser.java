package Parser;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Model.Statistiche;

/**
 * parser del file Statistiche.json, utilizza il singleton pattern
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class StatisticheParser {
    /**
     * la variabile statica che tiene traccia dell'unica istanza di StatisticheParser
     */
    private static StatisticheParser instance;
    /**
     * un dizionario con valori del tipo (nome statistica, valore statistica)
     */
    private Map<String, Statistiche> statisticheMap;

    /**
     * costruttore privato del parser
     * @param filePath il filepath di Statistiche.json
     */
    private StatisticheParser(String filePath) {
        this.statisticheMap = new HashMap<>();
        this.parseStatistiche(filePath);
    }

    /**
     * restituisce l'unica istanza del parser
     * @param filePath
     * @return
     */
    public static StatisticheParser getInstance(String filePath) {
        // Se l'istanza non è stata ancora creata, la creiamo
        if (instance == null) {
            instance = new StatisticheParser(filePath);
        }
        return instance;
    }

    /**
     * esegue il parsing del file
     * @param filePath il filepath di Statistiche.json
     */
    private void parseStatistiche(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            // Parsing del JSON
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonObject statisticheObject = jsonObject.getAsJsonObject("statistiche");

            // Per ogni Pokémon nel file JSON
            for (Map.Entry<String, JsonElement> entry : statisticheObject.entrySet()) {
                String nomePokemon = entry.getKey(); // Nome del Pokémon
                JsonElement values = entry.getValue(); // I valori delle statistiche

                // Creazione dell'array per le statistiche del Pokémon
                int[] statsArray = new int[7];
                for (int i = 0; i < 7; i++) {
                    statsArray[i] = values.getAsJsonArray().get(i).getAsInt();
                }

                // Creazione dell'oggetto Statistiche e aggiunta alla mappa
                Statistiche stats = new Statistiche(statsArray[0], statsArray[1], statsArray[2], 
                                                    statsArray[3], statsArray[4], statsArray[5], statsArray[6]);
                statisticheMap.put(nomePokemon, stats);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Gestione degli errori
        }
    }

    /**
     * metodo per ottenere le statistiche di un pokemon dato il suo nome
     * @param nomePokemon nome del pokemon
     * @return le statistiche del pokemon
     */
    public Statistiche getStatistiche(String nomePokemon) {
        Statistiche originali = statisticheMap.get(nomePokemon);
        if (originali == null) return null;
        return new Statistiche(originali); // Ritorna una COPIA
    }

}
