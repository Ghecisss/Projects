package Controller;

import Data.AnimationManager;
import Data.AnimationObserver;
import Data.InputManager;
import Data.InputObserver;
import View.*;
import Model.*;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.*;

/**
 * gestore del flusso della lotta, utilizza l'observer
 * pattern, implementale interfacce dell'animation observer
 * e input observer
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class BattleController implements AnimationObserver, InputObserver {
	/**
	 * il manager per gestire il flusso di operazioni da eseguire all'arrivo di una
	 * notifica per la fine di un'animazione
	 */
	private AnimationManager animationManager;
	/**
	 * il manager per gestire il flusso di operazioni da eseguire all'arrivo
	 * di una notifica di input del giocatore
	 */
	private InputManager inputManager;
	/**
	 * una variabile booleana che tiene traccia dello stato
	 * delle animazioni
	 */
	private boolean animazioneInCorso = false;
	/**
	 * una variabile che tiene traccia dello stato
	 * del turno
	 */
	private boolean turnoInCorso = false;
	/**
	 * una variabile che dice quando mettere in pausa
	 * la coda di animazioni
	 */
	private boolean inPausa = false;
	/**
	 * la coda di animazioni da eseguire
	 */
	private Queue<Runnable> codaAnimazioni = new LinkedList<>();
	/**
	 * ls schermata di lotta
	 */
	private BattleScreen battleScreen;
	/**
	 * il giocatore attuale
	 */
	private Player player;
	/**
	 * l'avversario attuale
	 */
	private Avversario avversario;
	/**
	 * il calcolatore di danni per gli attacchi durante la lotta
	 */
	private CalcolatoreDanni calcolatoreDanni;
	/**
	 * il controller delle azioni dell'avversario
	 */
	private AvversarioController avversarioController;
	/**
	 * variabile per contenere le callback da eseguire al termine di un'animazione
	 */
	private Runnable callbackAnimazione = null;

	/**
	 * crea il controller dato un giocatore e un avversario
	 * @param player il giocatore
	 * @param avversario l'avversario
	 */
	public BattleController(Player player, Avversario avversario) {
		this.animationManager = new AnimationManager();
		this.inputManager = new InputManager();
		this.animationManager.addObserver(this);
		this.inputManager.addObserver(this);
		this.player = player;
		this.avversario = avversario;
		this.calcolatoreDanni = new CalcolatoreDanni();
		this.avversarioController = new AvversarioController(avversario, player);
		this.battleScreen = new BattleScreen(player, avversario, animationManager, inputManager);
	}

	/**
	 * le operazioni da svolgere all'inizio di una lotta
	 */
	public void startBattle() {
		battleScreen.loadTextBox();
		//mostra nel textbox la scritta "comincia la battaglia"
		aggiungiAnimazione(() -> battleScreen.getTextBox().mostraTesto("Comincia la battaglia!!!"), null, AnimationType.TEXTBOX);
		//mostra nel textbox il pokemon mandato dal giocatore
		aggiungiAnimazione(() -> battleScreen.getTextBox().mostraTesto(player.getNome() + " manda\n" + player.getTeam().getPokemonAttivo().getNome()), null, AnimationType.TEXTBOX);
		//carica a schermo il pokemon del giocatore e i suoi dati, con un'animazione di entrata
        aggiungiAnimazione(() -> battleScreen.loadPlayer(), null, AnimationType.ENTRATA_PLAYER);
		//mostra nel textbox il pokemon mandato dall'avversario
		aggiungiAnimazione(() -> battleScreen.getTextBox().mostraTesto("Il nemico manda\n" + avversario.getTeam().getPokemonAttivo().getNome()), null, AnimationType.TEXTBOX);
		//carica su schermo il pokemon dell'avversario e i suoi dati, con un'animazione di entrata
        aggiungiAnimazione(() -> battleScreen.loadAvversario(), null, AnimationType.ENTRATA_AVVERSARIO);
		//inizia ad eseguire la coda di animazioni
        eseguiProssimaAnimazione(); 
	}

	/**
	 * metodo per aggiungere un animazione alla coda
	 * @param animazione l'animazione da aggiungere
	 * @param postAnimazione la funzione da chiamare alla fine dell'animazione
	 * @param type il tipo di animazione
	 */
	private void aggiungiAnimazione(Runnable animazione, Runnable postAnimazione, AnimationType type) {
	    codaAnimazioni.add(() -> {
	        animazioneInCorso = true;  // Segna che un'animazione è in corso
	        callbackAnimazione = postAnimazione;  // Memorizza la callback
	        
	        SwingUtilities.invokeLater(animazione); // Esegui l'animazione
	    });
	}

	// Quando un'animazione termina
	@Override
	public void onAnimationFinished(AnimationType type) {
	    animazioneInCorso = false;

	    if (callbackAnimazione != null) {
	        SwingUtilities.invokeLater(() -> {
	            callbackAnimazione.run();  // Esegui la callback
	            callbackAnimazione = null;  // Reset della callback
	            eseguiProssimaAnimazione(); // Passa alla prossima animazione
	        });
	    } else {
	        eseguiProssimaAnimazione(); // Se non c'è callback, passa direttamente alla prossima animazione
	    }

		if (
				codaAnimazioni.isEmpty() &&
						!turnoInCorso &&
						(battleScreen.getTeamPanel() == null || !battleScreen.getTeamPanel().isShowing()) &&
						(battleScreen.getReplacePanel() == null || !battleScreen.getReplacePanel().isShowing())
		) {
			turnoInCorso = true;
			battleScreen.loadAzioniPlayer();
		}

	}

	/**
	 * esegue la prossima animazione in coda
	 */
	private void eseguiProssimaAnimazione() {
	    if (!animazioneInCorso && !codaAnimazioni.isEmpty() && !inPausa) {
	        Runnable animazione = codaAnimazioni.poll();
	        animazione.run(); // Esegue l'animazione successiva
	    }
	}


	/**
	 * manda avanti la lotta quando il giocatore sceglie che
	 * mossa utilizzare
	 * @param mossa la mossa scelta dal giocatore
	 */
	@Override
	public void onMossaSelezionata(Mossa mossa) {
		
		battleScreen.closeAzioniPlayer(); //impedisce al giocatore di scegliere una mossa immediatamente dopo averne selezionata una
		battleScreen.closeMossePanel();
		Mossa mossaAvversario = avversarioController.scegliMossa(); //sceglie la mossa dell'avversario
		boolean switchAvversario = avversarioController.checkSwitch(); //l'avversario controlla se è conveniente switchare
		if (switchAvversario == true) {
			mossaAvversario = null;
		}
		
		eseguiTurno(mossa, false, mossaAvversario, switchAvversario);
	}

	/**
	 * manda avanti la lotta quando il giocatore sceglie
	 * di cambiare il pokemon in lotta, prosegue
	 * con la mossa scelta dall'avversario
	 */
	@Override
	public void onSwitchSelezionato() {
		battleScreen.closeAzioniPlayer(); //impedisce al giocatore di eseguire altre azioni immediatamente dopo aver selezionato lo switch
		turnoInCorso = true;
		Mossa mossaAvversario = avversarioController.scegliMossa(); //l'avversario continua il turno normalmente
		Boolean switchAvversario = avversarioController.checkSwitch();
		if (switchAvversario == true) mossaAvversario = null;
		eseguiTurno(null, true, mossaAvversario, switchAvversario);
		avversarioController.aggiornaPokemonPlayer();
	}

	/**
	 * manda avanti la lotta quando il giocatore è costretto
	 * a cambiare il pokemon in cambio, prosegue dalla scelta della mossa
	 * del giocatore
	 */
	@Override
	public void onSwitchForzato() {
		battleScreen.closeAzioniPlayer();
		avversarioController.aggiornaPokemonPlayer(); //l'avversario memorizza il nuovo pokemon attivo del giocatore
	    aggiungiAnimazione(() -> battleScreen.loadPlayer(), null, AnimationType.ENTRATA_PLAYER);
	    fineTurno();
	}

	/**
	 * va alla schermata di sconfitta quando il giocatore
	 * seleziona la fuga
	 */
	@Override
	public void onFuga() {
		battleScreen.dispose();
		new DefeatScreen(player);
	}

	/**
	 * esegue il turno dopo che gli input del giocatore sono stati dati
	 * @param mossaGiocatore la mossa scelta dal giocatore
	 * @param cambioGiocatore un booleano true se il giocatore ha deciso di switchare
	 * @param mossaAvversario la mossa scelta dall'avversario
	 * @param cambioAvversario un booleano true se l'avversario ha scelto di switchare
	 */
	 private void eseguiTurno(Mossa mossaGiocatore, boolean cambioGiocatore, Mossa mossaAvversario, boolean cambioAvversario) {
		    Pokemon pokemonGiocatore = player.getTeam().getPokemonAttivo();
		    Pokemon pokemonAvversario = avversario.getTeam().getPokemonAttivo();
		    
			//animazioni per il cambio del giocatore
		    if (cambioGiocatore) {
		    	aggiungiAnimazione(() -> battleScreen.getTextBox().mostraTesto(player.getNome() + " ritira\nil suo pokemon!"), null, AnimationType.TEXTBOX);
		        aggiungiAnimazione(() -> battleScreen.unloadPlayer(), null, AnimationType.USCITA_PLAYER);
		    }
			//animazioni per il cambio dell'avversario, e scelta del pokemon da mandare in campo dell'avversario
		    if (cambioAvversario) {
		    	aggiungiAnimazione(() -> battleScreen.getTextBox().mostraTesto("Il nemico ritira\nil suo pokemon!"), null, AnimationType.TEXTBOX);
		        aggiungiAnimazione(() -> battleScreen.unloadAvversario(), () -> {
		        	int i = avversarioController.scegliSwitch();
			        avversario.getTeam().switchPokemon(i);
			        avversarioController.aggiornaPokemonAttivo();
		        }, AnimationType.USCITA_AVVERSARIO);
		        
		    }
		    //animazioni per il cambio di giocatore e avversario
		    if (cambioGiocatore && cambioAvversario) {
		    	aggiungiAnimazione(() -> battleScreen.getTextBox().mostraTesto("Il nemico manda\n" + avversario.getTeam().getPokemonAttivo().getNome()), null, AnimationType.TEXTBOX);
		    	aggiungiAnimazione(() -> battleScreen.loadAvversario(), null, AnimationType.ENTRATA_AVVERSARIO);
		    	aggiungiAnimazione(() -> battleScreen.getTextBox().mostraTesto(player.getNome() + " manda\n" + pokemonGiocatore.getNome()), null, AnimationType.TEXTBOX);
		    	aggiungiAnimazione(() -> battleScreen.loadPlayer(), null, AnimationType.ENTRATA_PLAYER);
		    }

		    // Se il giocatore ha cambiato e l'avversario attacca
		    if (cambioGiocatore && mossaAvversario != null) {
		    	aggiungiAnimazione(() -> battleScreen.getTextBox().mostraTesto(player.getNome() + " manda\n" + pokemonGiocatore.getNome()), null, AnimationType.TEXTBOX);
		    	aggiungiAnimazione(() -> battleScreen.loadPlayer(), null, AnimationType.ENTRATA_PLAYER);
				aggiungiAnimazione(() -> battleScreen.getTextBox().mostraTesto(pokemonAvversario.getNome() + "\nusa " + mossaAvversario.getNomeMossa()), null, AnimationType.TEXTBOX);
		    	aggiungiAnimazione(() -> battleScreen.mostraAttacco(pokemonAvversario), () -> eseguiAttacco(pokemonAvversario, pokemonGiocatore, mossaAvversario), AnimationType.ATTACCO);

		        
		    }

		    // Se l'avversario ha cambiato e il giocatore attacca
		    if (cambioAvversario && mossaGiocatore != null) {
				aggiungiAnimazione(() -> battleScreen.getTextBox().mostraTesto("Il nemico manda\n" + avversario.getTeam().getPokemonAttivo().getNome()), null, AnimationType.TEXTBOX);
		    	aggiungiAnimazione(() -> battleScreen.loadAvversario(), null, AnimationType.ENTRATA_AVVERSARIO);
				aggiungiAnimazione(() -> battleScreen.getTextBox().mostraTesto(pokemonGiocatore.getNome() + "\nusa " + mossaGiocatore.getNomeMossa()), null, AnimationType.TEXTBOX);
		        aggiungiAnimazione(() -> battleScreen.mostraAttacco(pokemonGiocatore), () -> eseguiAttacco(pokemonGiocatore, avversario.getTeam().getPokemonAttivo(), mossaGiocatore), AnimationType.ATTACCO);
		    }

		    // Se entrambi attaccano
		    if (mossaGiocatore != null && mossaAvversario != null) {
				//controlla l'ordine del turno in base al pokemon più veloce
		        boolean giocatoreAttaccaPrima = pokemonGiocatore.getStatistiche().getVel() >= pokemonAvversario.getStatistiche().getVel();
				//se il giocatore attacca prima
		        if (giocatoreAttaccaPrima) {
					aggiungiAnimazione(() -> battleScreen.getTextBox().mostraTesto(pokemonGiocatore.getNome() + "\nusa " + mossaGiocatore.getNomeMossa()), null, AnimationType.TEXTBOX);
		            aggiungiAnimazione(() -> battleScreen.mostraAttacco(pokemonGiocatore), () -> eseguiAttacco(pokemonGiocatore, pokemonAvversario, mossaGiocatore), AnimationType.ATTACCO);

					aggiungiAnimazione(() -> battleScreen.getTextBox().mostraTesto(pokemonAvversario.getNome() + "\nusa " + mossaAvversario.getNomeMossa()), null, AnimationType.TEXTBOX);
		            aggiungiAnimazione(() -> battleScreen.mostraAttacco(pokemonAvversario), () -> eseguiAttacco(pokemonAvversario, pokemonGiocatore, mossaAvversario), AnimationType.ATTACCO);
				//se l'avversario attacca prima
		        } else {
					aggiungiAnimazione(() -> battleScreen.getTextBox().mostraTesto(pokemonAvversario.getNome() + "\nusa " + mossaAvversario.getNomeMossa()), null, AnimationType.TEXTBOX);
		            aggiungiAnimazione(() -> battleScreen.mostraAttacco(pokemonAvversario), () -> eseguiAttacco(pokemonAvversario, pokemonGiocatore, mossaAvversario), AnimationType.ATTACCO);

		            aggiungiAnimazione(() -> battleScreen.getTextBox().mostraTesto(pokemonGiocatore.getNome() + "\nusa " + mossaGiocatore.getNomeMossa()), null, AnimationType.TEXTBOX);
		            aggiungiAnimazione(() -> battleScreen.mostraAttacco(pokemonGiocatore), () -> eseguiAttacco(pokemonGiocatore, pokemonAvversario, mossaGiocatore), AnimationType.ATTACCO);
		        }

		    }

		    fineTurno();
		}

	/**
	 * esegue i calcoli e gli aggiornamenti grafici
	 * per un attacco
	 * @param attaccante il pokemon che esegue l'attacco
	 * @param difensore il pokemon che subisce l'attacco
	 * @param mossa la mossa usata dal pokemon attaccante
	 */
	 private void eseguiAttacco(Pokemon attaccante, Pokemon difensore, Mossa mossa) {
		    //calcola i danni
		    int danni = calcolatoreDanni.calcolaDanni(attaccante, difensore, mossa);
			//modifica gli hp del pokemon attaccato
		    int hpAttuali = difensore.getStatistiche().getHp();
		    difensore.getStatistiche().setHp(Math.max(0, hpAttuali - danni));
			//aggiornamento grafico degli hp
		    aggiornaBarraHP(difensore);
			//controlla se il pokemon attaccato è morto
		    if (difensore.getStatistiche().getHp() <= 0) {
		    	codaAnimazioni.clear();
		        gestisciKO(difensore, attaccante);
		   }
		}

	/**
	 * aggiorna graficamente le barre degli hp
	 * @param difensore il pokemon che ha subito un attacco
	 */
	private void aggiornaBarraHP(Pokemon difensore) {
		    if (difensore == player.getTeam().getPokemonAttivo()) {
		        battleScreen.getPlayerBar().aggiornaHP(difensore.getStatistiche().getHp(), difensore.getStatistiche().getMaxHp());
		    } else {
		        battleScreen.getAvversarioBar().aggiornaHP(difensore.getStatistiche().getHp(), difensore.getStatistiche().getMaxHp());
		    }
		}

		/**
		 * Gestisce la logica della sconfitta di un Pokémon
		 * @param sconfitto il pokemon che è stato sconfitto
		 * @param vincitore il pokemon vincitore
		 */
		private void gestisciKO(Pokemon sconfitto, Pokemon vincitore) {
			//se il pokemon sconfitto è del giocatore, e il giocatore non ha finito i pokemon, esegui l'animazione di uscita e forza lo switch
		    if (sconfitto == player.getTeam().getPokemonAttivo()) {
		    	aggiungiAnimazione(() -> battleScreen.unloadPlayer(), null, AnimationType.USCITA_PLAYER);
		        player.getTeam().decrementaPokemonVivi();
		        	if (player.getTeam().getPokemonVivi() > 0) {
			            battleScreen.loadTeamPanel(true);
			        } else {
						Sconfitta();
			        }
		        
		    } else {
				/*
				se il pokemon sconfitto è dell'avversario, calcola gli exp guadagnati dal pokemon del giocatore,
				esegue i controlli sulle evoluzioni e le mosse imparate dal pokemon del giocatore, e se
				l'avversario non ha finito i pokemon allora sceglie il prossimo pokemon da mandare, altrimenti
				il giocatore ha vinto
				 */
		    	battleScreen.closeAzioniPlayer();
		    	aggiungiAnimazione(() -> battleScreen.getTextBox().mostraTesto(sconfitto.getNome() + "\n è esausto!"), null, AnimationType.TEXTBOX);
		    	aggiungiAnimazione(() -> battleScreen.unloadAvversario(), null, AnimationType.USCITA_AVVERSARIO);
		        avversario.getTeam().decrementaPokemonVivi();
		        int expGuadagnati = sconfitto.getLivello() + 15;
		        Pokemon.LevelUpResult result = vincitore.ottieniExp(expGuadagnati);
		        battleScreen.getPlayerBar().aggiornaEXP(vincitore);
		        if (result.hasEvolved) {
		        	aggiungiAnimazione(() -> battleScreen.getTextBox().mostraTesto(vincitore.getNome() + "\n si è evoluto!"), () -> battleScreen.aggiornaEvo(), AnimationType.TEXTBOX);
		        }

				if (result.noReplace == false) {
					aggiungiAnimazione(() -> battleScreen.getTextBox().mostraTesto("scegli la mossa da\nsostituire!"), () -> {

							battleScreen.loadReplacePanel(
							player.getTeam().getPokemonAttivo().getMosseImparabili().get(player.getTeam().getPokemonAttivo().getLivello()/2),
							player.getTeam().getPokemonAttivo().getMosseAttive().getMosse(),
							inputManager);
							inPausa = true;

							}, AnimationType.TEXTBOX);
				}

		        if (avversario.getTeam().getPokemonVivi() > 0) {
		            int i = avversarioController.scegliSwitch();
		            avversario.getTeam().switchPokemon(i);
		            avversarioController.aggiornaPokemonAttivo();
		            aggiungiAnimazione(() -> battleScreen.getTextBox().mostraTesto("Il nemico manda\n" + avversario.getTeam().getPokemonAttivo().getNome()), null, AnimationType.TEXTBOX);
		            aggiungiAnimazione(() -> battleScreen.loadAvversario(), null, AnimationType.ENTRATA_AVVERSARIO);
		            fineTurno();
		        } else {
		            Vittoria();
		        }
		    }
		}

	/**
	 * finiti i calcoli inizia a eseguire le animazioni in coda
	 */
	private void fineTurno() {
		 turnoInCorso = false;
		 eseguiProssimaAnimazione();
		 
	 }

	/**
	 * passa alla schermata di vittoria
	 */
	private void Vittoria() {
         aggiungiAnimazione(() -> battleScreen.getTextBox().mostraTesto("Hai vinto!"), () -> {
        	 new VictoryScreen(player);
        	 battleScreen.dispose();
        	 }, AnimationType.TEXTBOX);
         eseguiProssimaAnimazione();
	 }

	/**
	 * passa alla schermata di sconfitta
	 */
	private void Sconfitta() {
         aggiungiAnimazione(() -> battleScreen.getTextBox().mostraTesto("Hai perso!"), () -> { 
        	 new DefeatScreen(player);
        	 battleScreen.dispose();
        	 }, AnimationType.TEXTBOX);
         eseguiProssimaAnimazione();
		 
	 }

	/**
	 * manda avanti la lotta quando il giocatore ha
	 * scelto se sostiuire una mossa del suo pokemon
	 * @param i l'indice della mossa sostituita
	 * @param nuova la nuova mossa
	 */
	 @Override
	public void onReplaceSelezionato(int i, Mossa nuova) {
			player.getTeam().getPokemonAttivo().getMosseAttive().replaceMossa(i, nuova);
			inPausa = false;
			animazioneInCorso = false;
			eseguiProssimaAnimazione();
	 }

}
