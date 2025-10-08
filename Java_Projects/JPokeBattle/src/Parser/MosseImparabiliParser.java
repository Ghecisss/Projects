package Parser;

import java.io.FileReader;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Model.Mossa;

/**
 * parser per il file mosseImparabili.json
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class MosseImparabiliParser {
    /**
     * parsa il file json e ritorna una lista di mosse imparabili da un pokemon
     * @param pokemonNome nome del pokemon di cui si vogliono ottenre le mosse
     * @param filePath filepath del file json
     * @return la lista di mosse imparabili del pokemon
     */
    public static List<Mossa> parseMosseImparabili(String pokemonNome, String filePath) {
        List<Mossa> mosseImparabili = null;

        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();

            // Mappa principale che contiene "pokemonMosseImparabili"
            Map<String, Map<String, List<Mossa>>> rootMap = gson.fromJson(reader, 
                new TypeToken<Map<String, Map<String, List<Mossa>>>>() {}.getType());

            // Recupera la mappa contenente le mosse dei Pokémon
            Map<String, List<Mossa>> pokemonMosseMap = rootMap.get("pokemonMosseImparabili");

            // Verifica se il Pokémon è presente nella mappa
            if (pokemonMosseMap != null && pokemonMosseMap.containsKey(pokemonNome)) {
                mosseImparabili = pokemonMosseMap.get(pokemonNome);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mosseImparabili;
    }
}
