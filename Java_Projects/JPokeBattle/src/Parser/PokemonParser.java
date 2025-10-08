package Parser;

import java.io.FileReader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Model.Mossa;
import Model.Pokemon;

import java.lang.reflect.Type;
import java.util.List;

/**
 * parser del file Pokemon.json
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class PokemonParser {
    /**
     * parsa il file json e ritorna una lista di pokemon
     * @param filePath il filepath del json
     * @return la lista di pokemon
     */
    public static List<Pokemon> parsePokemon(String filePath) {
        try {
            FileReader reader = new FileReader(filePath);
            Gson gson = new Gson();
            Type pokemonListType = new TypeToken<List<Pokemon>>() {}.getType();
            return gson.fromJson(reader, pokemonListType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    


}
