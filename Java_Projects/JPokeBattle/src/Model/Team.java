package Model;

/**
 * rappresenta un team di pokemon
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class Team {
    /**
     * l'array di pokemon nel team
     */
    private Pokemon[] squadra;
    /**
     * il pokemon attualmente in lotta
     */
    private int pokemonAttivo;
    /**
     * il numero di pokemon con hp > 0 nel team
     */
    private int pokemonVivi;

    /**
     * costruttore, crea un oggetto team a partire da un array di pokemon
     * @param team l'array di pokemon
     */
    public Team(Pokemon[] team) {
        this.squadra = team;
        this.pokemonAttivo = 0; 
        this.pokemonVivi = 3;   
    }

    /**
     * ritorna il pokemon attualmente attivo (in lotta)
     * @return il pokemon attivo
     */
    public Pokemon getPokemonAttivo() {
        return squadra[pokemonAttivo];
    }

    /**
     * cambia il pokemon attivo
     * @param index l'index del nuovo pokemon attivo
     */
    public void switchPokemon(int index) {
        if (index >= 0 && index < 3 && squadra[index].getStatistiche().getHp() > 0) {
            this.setPokemonAttivo(index);
        } else {
            System.out.println("Non puoi selezionare questo Pokémon!");
        }
    }

    /**
     * ritorna l'array di pokemon del team
     * @return l'array di pokemon
     */
    public Pokemon[] getSquadra() {
    	return this.squadra;
    }

    /**
     * ritorna il numero di pokemon ancora in vita
     * @return il numero di pokemon vivi
     */
    public int getPokemonVivi() {
    	return this.pokemonVivi;
    }

    /**
     * decrementa il numero di pokemon vivi
     */
    public void decrementaPokemonVivi() {
    	this.pokemonVivi = this.pokemonVivi - 1;
    }

    /**
     * setta il pokemon attivo
     * @param i l'indice del nuovo pokemon attivo
     */
    public void setPokemonAttivo(int i) {
    	this.pokemonAttivo = i;
    }
}
