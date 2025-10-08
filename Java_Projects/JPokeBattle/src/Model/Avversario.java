package Model;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;


/**
 * rappresenta l'avversario, cioè l'IA che sfida
 * il giocatore.
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class Avversario {
    /**
     * il team dell'avversario
     */
    private Team team;

    /**
     * costruttore per l'oggetto avversario.
     * Gli oggetti avversario vengono creati in base al team
     * del giocatore per equilibrio di gioco
     * @param listaPokemonDisponibili i pokemon che l'avversario potrebbe avere nel team
     * @param teamGiocatore i pokemon nel team del giocatore
     */
    public Avversario(List<Pokemon> listaPokemonDisponibili, Team teamGiocatore) {
        this.team = generaTeamAvversario(listaPokemonDisponibili, teamGiocatore);
    }

    /**
     * genera il team dell'avversario in modo che sia equilibrato
     * rispetto al team del giocatore
     * @param listaPokemon tutti i pokemon disponibili
     * @param teamGiocatore i pokemon del giocatore
     * @return il team dell'avversario
     */
    private Team generaTeamAvversario(List<Pokemon> listaPokemon, Team teamGiocatore) {
        Pokemon[] teamAvversario = new Pokemon[3];

        // Trova il tier massimo nel team del giocatore
        int maxTierGiocatore = 1;
        int sommaLivelli = 0;
        for (Pokemon p : teamGiocatore.getSquadra()) {
            if (p != null) {
                maxTierGiocatore = Math.max(maxTierGiocatore, p.getTier());
                sommaLivelli += p.getLivello();
            }
        }

        // Calcola la media dei livelli e imposta il range
        int mediaLivelli = (int) Math.ceil(sommaLivelli / 3.0);
        int minLivello = Math.max(5, mediaLivelli - 1);
        int maxLivello = mediaLivelli + 1;

        // Filtra i Pokémon disponibili con tier ≤ maxTierGiocatore
        List<Pokemon> pokemonValidi = new ArrayList<>();
        for (Pokemon p : listaPokemon) {
            if (p.getTier() <= maxTierGiocatore) {
                pokemonValidi.add(p);
            }
        }

        // Seleziona casualmente 3 Pokémon
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            Pokemon base = pokemonValidi.get(rand.nextInt(pokemonValidi.size()));
            base.inizializza();
            Pokemon clone = new Pokemon(base);
            clone.setLivello(rand.nextInt(maxLivello - minLivello + 1) + minLivello);
            
            teamAvversario[i] = clone;
        }
        
        for (int i = 0; i < 3; i++) {
        	teamAvversario[i].inizializza();
        }

        return new Team(teamAvversario);
    }

    /**
     * ritorna il team dell'avversario
     * @return il team dell'avversario
     */
    public Team getTeam() {
        return team;
    }
}
