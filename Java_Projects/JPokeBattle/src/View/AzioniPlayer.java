package View;

import javax.swing.*;

import Data.InputManager;
import Model.*;

/**
 * gestisce il menu delle azioni che un giocatore
 * può effettuare in una partita: scegliere una mossa,
 * switchare pokemon o terminare la lotta
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class AzioniPlayer extends JPanel {
    /**
     * il team di pokemon del giocatore
     */
	private Team playerTeam;
    /**
     * la schermata di lotta su cui si trova questo panel
     */
	private BattleScreen battleScreen;
    /**
     * l'input manager per gestire le scelte del giocatore
     */
	private InputManager inputManager;

    /**
     * costruttore del menu delle azioni, i pulsanti delle mosse e
     * dello switch aprono i loro rispetivi menu, il pulsante di fuga
     * notifica il controller per terminare la battaglia
     * @param playerTeam
     * @param battleScreen
     * @param inputManager
     */
    public AzioniPlayer(Team playerTeam, BattleScreen battleScreen, InputManager inputManager) {
    	this.playerTeam = playerTeam;
    	this.battleScreen = battleScreen;
    	this.inputManager = inputManager;
    	setLayout(null); 
        setBounds(450, 430, 800, 200); 
        setOpaque(false); 

        JButton btnMosse = ButtonFactory.createButton("Mosse", 100, 20, 100, 40, e -> {
        	battleScreen.closeAzioniPlayer();
            battleScreen.loadMossePanel();
            
        });

        JButton btnSwitch = ButtonFactory.createButton("Switch", 200, 20, 100, 40, e -> {
            battleScreen.closeAzioniPlayer();
            battleScreen.loadTeamPanel(false);
        });

        JButton btnFuga = ButtonFactory.createButton("Fuga", 150, 70, 100, 40, e -> {
            inputManager.notifyFuga();
        });

        
        add(btnMosse);
        add(btnSwitch);
        add(btnFuga);
    }
}
