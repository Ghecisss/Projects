package View;

import javax.swing.*;

import Data.InputManager;
import Model.Pokemon;
import Model.Team;
import java.awt.*;

/**
 * Panel per lo scambio del pokemon in partita
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class TeamPanel extends JPanel {
    /**
     * il team del giocatore
     */
    private Team playerTeam;
    /**
     * i riquadri che mostreranno i pokemon del team selezionabili
     */
    private JButton[] teamSlots;
    /**
     * indice del pokemon selezionato
     */
    private int selectedIndex = -1;
    /**
     * pulsante per tornare al menu di azioni generale
     */
    private JButton backButton;
    /**
     * schermata di lotta attuale
     */
    private BattleScreen battleScreen;
    /**
     * manager per notificare quando viene selezionato lo switch
     */
    private InputManager inputManager;
    /**
     * booleano per segnalare se lo switch è volontario o forzato
     * a seguito della sconfitta di un pokemon
     */
    private boolean cambioForzato = false; // Indica se il cambio è forzato
    /**
     * booleano per segnalare se il panel è attualmente visibile a schermo
     */
    private boolean isShowing = false;

    /**
     * costruttore del panel del team del giocatore
     * @param team il team del giocatore
     * @param battleScreen la schermata di lotta
     * @param inputManager il manager degli observer degli input
     * @param cambioForzato il valore booleano, true quando è stato sconfitto un pokemon del giocatore
     */
    public TeamPanel(Team team, BattleScreen battleScreen, InputManager inputManager, Boolean cambioForzato) {
    	this.inputManager = inputManager;
        this.playerTeam = team;
        this.battleScreen = battleScreen;
        this.cambioForzato = cambioForzato;
        setLayout(null);
        setBounds(530, 390, 290, 170);
        setOpaque(false);

        JPanel teamPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        teamPanel.setBounds(120, 0, 100, 170);
        teamPanel.setOpaque(false);
        add(teamPanel);

        teamSlots = new JButton[3];

        for (int i = 0; i < 3; i++) {
            JButton teamSlot = new JButton();
            teamSlot.setPreferredSize(new Dimension(70, 70));
            teamSlot.setBackground(Color.BLACK);
            teamSlot.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

            if (team.getSquadra()[i] != null) {
                Pokemon p = team.getSquadra()[i];
                teamSlot.setIcon(new ImageIcon(getClass().getResource("/sprites/" + p.getNome() + ".png")));
            }

            int slotIndex = i;
            teamSlot.addActionListener(e -> {
                selectedIndex = slotIndex;
                for (JButton btn : teamSlots) {
                    btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                }
                teamSlot.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3)); // Evidenzia selezione
            });

            teamSlots[i] = teamSlot;
            teamPanel.add(teamSlot);
            
        }

        // Pulsante Switch creato con ButtonFactory
        JButton switchButton;
        if (!cambioForzato) {
            // Pulsante normale (avvia il turno)
            switchButton = ButtonFactory.createButton("Switch", 10, 50, 100, 30, e -> {
                if (selectedIndex != -1 && playerTeam.getSquadra()[selectedIndex].getStatistiche().getHp() > 0) {
                    playerTeam.switchPokemon(selectedIndex);
                    battleScreen.closeTeamPanel();
                    inputManager.notifySwitchSelezionato(); // Avvia il turno
                } else {
                    JOptionPane.showMessageDialog(this, "Seleziona un Pokémon valido!");
                }
            });
        } else {
            // Pulsante per il cambio forzato (NON avvia il turno)
            switchButton = ButtonFactory.createButton("Forza Switch", 10, 50, 100, 30, e -> {
                if (selectedIndex != -1 && playerTeam.getSquadra()[selectedIndex].getStatistiche().getHp() > 0) {
                    playerTeam.switchPokemon(selectedIndex);
                    battleScreen.closeTeamPanel();
                    //battleScreen.loadPlayer(); // Carica direttamente il nuovo Pokémon
                    inputManager.notifySwitchForzato();
                } else {
                    System.out.println(System.identityHashCode(playerTeam.getSquadra()[selectedIndex]));
                    JOptionPane.showMessageDialog(this, "Seleziona un Pokémon valido!");
                }
            });
        }
        add(switchButton);


        // Pulsante Indietro creato con ButtonFactory
        backButton = ButtonFactory.createButton("Indietro", 10, 100, 100, 30, e -> {
            battleScreen.closeTeamPanel();
            battleScreen.loadAzioniPlayer();
        });
        add(backButton);
    }

    /**
     * ritorna true se il panel è a schermo
     * @return il valore booleano is showing
     */
    public boolean isShowing() {return this.isShowing;}

    /**
     * setta il valore booleano di isShowing
     * @param isShowing
     */
    public void setIsShowing(boolean isShowing) {this.isShowing = isShowing;}
}
