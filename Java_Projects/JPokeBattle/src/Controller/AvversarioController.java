package Controller;

import Model.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * il controller delle decisioni dell'avversario
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class AvversarioController {
    /**
     * l'oggetto avversario attualmente in lotta
     */
   private Avversario avversario;
    /**
     * il giocatore attualmente in lotta
     */
   private Player player;
    /**
     * il pokemon attualmente in lotta dell'avversario
     */
   private Pokemon pokemonAttivo;
    /**
     * il pokemon attualmente in lotta del giocatore
     */
   private Pokemon pokemonPlayer;
    /**
     * il team dell'avversario attuale
     */
   private Team teamAvversario;

    /**
     * crea il controller che prenderà decisioni in base allo stato
     * dell'avversario e del giocatore
     * @param avversario l'avversario
     * @param player il giocatore
     */
   public AvversarioController(Avversario avversario, Player player) {
       this.avversario = avversario;
       this.player = player;
       this.teamAvversario = avversario.getTeam();
       this.pokemonAttivo = teamAvversario.getPokemonAttivo();
       this.pokemonPlayer = player.getTeam().getPokemonAttivo();
   }

    /**
     * metodo per scegliere la mossa che userà
     * il pokemon dell'avversario, dividendo le mosse
     * in vantaggiose, neutre e svantaggiose
     * @return la mossa scelta
     */
   public Mossa scegliMossa() {
       Random rand = new Random();
       Tipo tipoPlayer = pokemonPlayer.getTipo();
       List<Mossa> mosseVantaggiose = new ArrayList<>();
       List<Mossa> mosseNeutre = new ArrayList<>();
       
       Mossa[] mossePokemon = pokemonAttivo.getMosseAttive().getMosse();
       
       for (Mossa m : mossePokemon) {
           if (m == null) continue;
           Tipo tipoM = m.getTipo();

           if (tipoPlayer.deboleA(tipoM)) {
               mosseVantaggiose.add(m);
           } else if (!tipoPlayer.resisteA(tipoM) && !tipoPlayer.immuneA(tipoM)) {
               mosseNeutre.add(m);
           }
       }
       
       if (!mosseVantaggiose.isEmpty()) {
           return mosseVantaggiose.get(rand.nextInt(mosseVantaggiose.size()));
       }
       
       if (!mosseNeutre.isEmpty()) {
           return mosseNeutre.get(rand.nextInt(mosseNeutre.size()));
       }
       
       return mossePokemon[rand.nextInt(mossePokemon.length)];
   }

    /**
     * metodo per la logica di decisione di cambiare
     * il pokemon in partita dell'avversario
     * @return il valore booleano che dice se l'avversario vuole switchare o no
     */
   public boolean checkSwitch() {
       if (teamAvversario.getPokemonVivi() == 1) return false;

       Tipo tipoPlayer = pokemonPlayer.getTipo();
       Tipo tipoA = pokemonAttivo.getTipo();
       Pokemon[] teamA = teamAvversario.getSquadra();
       Random rand = new Random();
       int offset = 0;
       
       if (tipoPlayer.resisteA(tipoA)) {
           offset += 30;

           for (Pokemon p : teamA) {
               if (p.getStatistiche().getHp() > 0 && tipoPlayer.deboleA(p.getTipo())) {
                   offset += 40;
                   break;
               }
           }
           
           int prob = rand.nextInt(100);
           return prob + offset >= 100;
       }
       
       return false;
   }

    /**
     * logica per la decisione del pokemon dell'avversario da mandare in campo
     * @return l'indice del pokemon da mandare in campo nel team delll'avversario
     */
   public int scegliSwitch() {
	    Tipo tipoPlayer = pokemonPlayer.getTipo();
	    Pokemon[] teamA = teamAvversario.getSquadra();
	    List<Integer> forti = new ArrayList<>();
	    List<Integer> neutri = new ArrayList<>();
	    List<Integer> vivi = new ArrayList<>();
	    
	    // Costruiamo le liste di Pokémon vivi con diverse priorità
	    for (int i = 0; i < teamA.length; i++) {
	        if (teamA[i] != pokemonAttivo && teamA[i].getStatistiche().getHp() > 0) { // Escludi il Pokémon attivo e i Pokémon esausti

                vivi.add(i); // Lista di tutti i Pokémon vivi

                if (tipoPlayer.deboleA(teamA[i].getTipo())) {
                    forti.add(i);
                } else {
                    neutri.add(i);
                }
            }
	    }

	    Random rand = new Random();

	    // Priorità ai Pokémon con vantaggio di tipo
	    if (!forti.isEmpty()) {
	        return forti.get(rand.nextInt(forti.size()));
	    }
	    
	    // Se non ci sono forti, scegliere un Pokémon neutro
	    if (!neutri.isEmpty()) {
	        return neutri.get(rand.nextInt(neutri.size()));
	    }
	    
	    // Se non ci sono Pokémon forti o neutri, sceglierne uno a caso tra quelli vivi
	    if (!vivi.isEmpty()) {
	        return vivi.get(rand.nextInt(vivi.size()));
	    }

	    return -1; // Nessuno switch possibile (situazione che non dovrebbe mai accadere)
	}

    /**
     * metodo che cambia il pokemon in lotta al
     * pokemon con l'indice dato in input
     */
   public void aggiornaPokemonAttivo() {
	   this.pokemonAttivo = teamAvversario.getPokemonAttivo();
   }

    /**
     * metodo che aggiorna l'informazione
     * sul pokemon del giocatore in campo
     */
   public void aggiornaPokemonPlayer() {
	   this.pokemonPlayer = player.getTeam().getPokemonAttivo();
   }

}
