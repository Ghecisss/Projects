package Model;

/**
 * rappresenta il giocatore
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class Player {
    /**
     * il nome del giocatore
     */
    private String nome;
    /**
     * il team del giocatore
     */
    private Team team;
    /**
     * il numero di vittorie consecutive di un giocatore
     */
    private int vittorieConsecutive;

    /**
     * crea il giocatore, con un nome e un team di pokemon
     * @param nome il nome del giocatore
     * @param squadra un array di pokemon
     */
    public Player(String nome, Pokemon[] squadra) {
    	this.nome = nome;
    	this.team = new Team(squadra);
    	this.vittorieConsecutive = 0;

    }

    /**
     * ritorna il nome del giocatore
     * @return il nome del giocatore
     */
    public String getNome() {
        return nome;
    }

    /**
     * ritorna il team del giocatore
     * @return il team del giocatore
     */
    public Team getTeam() {
        return team;
    }

    /**
     * ritorna il numero di vittorie consecutive
     * @return il numero di vittorie consecutive
     */
    public int getVittorieConsecutive() {
        return vittorieConsecutive;
    }

    /**
     * aumenta il numero di vittorie di q
     */
    public void incrementaVittorie() {
        vittorieConsecutive++;
    }
}
