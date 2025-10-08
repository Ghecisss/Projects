package View;

import Data.InputManager;
import Model.*;
import javax.swing.*;

/**
 * il menu delle mosse selezionabili dal giocatore
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class MossePanel extends JPanel {
    /**
     * l'array di pulsanti per le mosse
     */
	private JButton[] mosseButtons = new JButton[4];
    /**
     * il manager per notitificare gli observer quando viene dato un input (scelta una mossa)
     */
	private InputManager inputManager;

    /**
     * crea il menu di mosse selezionabili
     * @param pokemon il pokemon in gioco
     * @param battleScreen la schermata di lotta
     * @param inputManager il manager degli input per notificare la scelta
     */
	public MossePanel(Pokemon pokemon, BattleScreen battleScreen, InputManager inputManager) {
		setLayout(null);
        setBounds(560, 430, 280, 250); 
        setOpaque(false);

        Mossa[] mosse = pokemon.getMosseAttive().getMosse();
        for (int i = 0; i < 4; i++) {
            if (mosse[i] != null) {
                Mossa mossa = mosse[i];
                mosseButtons[i] = ButtonFactory.createButton(
                        mossa.getNomeMossa(),
                        (i % 2) * 100, (i / 2) * 40, 
                        100, 40,
                        e -> {
                        	pokemon.getMosseAttive().setMossaScelta(mossa);
                        	inputManager.notifyMossaSelezionata(pokemon.getMosseAttive().getMossaScelta());
                        });
            } else {
                mosseButtons[i] = ButtonFactory.createButton("-", (i % 2) * 100, (i / 2) * 40, 100, 40, null);
                mosseButtons[i].setEnabled(false);
            }
            add(mosseButtons[i]);
        }

        JButton indietroButton = ButtonFactory.createButton("Indietro", 60, 85, 80, 40, e -> {
        	
        battleScreen.loadAzioniPlayer();
        battleScreen.closeMossePanel();
        
        });
        
        add(indietroButton);
    }
	
}
