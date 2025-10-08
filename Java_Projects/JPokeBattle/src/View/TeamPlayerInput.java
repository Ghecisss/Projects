package View;

import Model.*;
import Controller.*;
import Parser.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * panel per l'input del team del giocatore
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class TeamPlayerInput extends JPanel {
    /**
     * il nome del giocatore
     */
    private String playerNome;
    /**
     * la schermata di start attuale
     */
    private StartScreen startScreen;
    /**
     * il team di pokemon del giocatore rappresentato da un array di 3 pokemon
     */
    private Pokemon[] playerTeam = new Pokemon[3];
    /**
     * i riquadri che mostrano i pokemon scelti per il team, sono pulsanti che se premuti
     * cancellano la scelta del pokemon
     */
    private JButton[] teamSlots = new JButton[3];

    /**
     * crea il panel per l'input del team del giocatore nella schermata di start
     * @param playerNome il nome del giocatore
     * @param startScreen la schemata di start
     */
    public TeamPlayerInput(String playerNome, StartScreen startScreen) {
        this.playerNome = playerNome;
        this.startScreen = startScreen;

        //parsa tutti i pokemon nel json
        List<Pokemon> pokemonDisponibili = PokemonParser.parsePokemon("src/risorse/pokemon.json");

        setLayout(null);
        setBounds(100, 100, 600, 400);
        setBackground(new Color(0, 0, 0, 180));

        JLabel label = new JLabel("Seleziona il tuo team!");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setBounds(200, 20, 300, 30);
        add(label);

        //crea una colonna di 3 riquadri per i pokemon selezionati
        JPanel teamPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        teamPanel.setBounds(20, 70, 80, 240);
        teamPanel.setOpaque(false);

        for (int i = 0; i < 3; i++) {
            JButton teamSlot = new JButton();
            teamSlot.setPreferredSize(new Dimension(70, 70));
            teamSlot.setBackground(new Color(100, 100, 100, 180));
            teamSlot.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            int slotIndex = i;
            //se il riquadro viene selezionato, cancella la scelta del pokemon
            teamSlot.addActionListener(e -> {
                playerTeam[slotIndex] = null;
                teamSlot.setIcon(null);
            });

            teamSlots[i] = teamSlot;
            teamPanel.add(teamSlot);
        }

        add(teamPanel);

        //griglia di riquadri per i pokemon selezionabili
        JPanel gridPanel = new JPanel(new GridLayout(4, 3, 8, 8));
        gridPanel.setBounds(300, 70, 250, 300);
        gridPanel.setOpaque(false);

        int index = 0;

        //crea i pulsanti per selezionare i pokemon e aggiunge gli sprite per ogni pokemon
        for (Pokemon p : pokemonDisponibili) {
        	
            if (p.getTier() == 1 && index < 12) {
            	
                JPanel slot = new JPanel();
                slot.setPreferredSize(new Dimension(40, 40));
                slot.setBackground(new Color(0, 0, 0, 160));
                slot.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                slot.setLayout(new BorderLayout());

                String imagePath = "src/Sprites/" + p.getNome() + ".png";
                ImageIcon tempIcon = new ImageIcon(imagePath);
                Image scaledImage = tempIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                final ImageIcon icon = new ImageIcon(scaledImage);

                //premendo un riquadro si aggiunge un pokemon al team
                JButton button = ButtonFactory.createButton("", 0, 0, 70, 70, e -> {
                    aggiungiPokemonAlTeam(p, icon);
                });

                button.setIcon(icon);
                button.setOpaque(false);
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);

                slot.add(button, BorderLayout.CENTER);
                gridPanel.add(slot);

                index++;
            }
        }

        add(gridPanel);

        //pulsante per iniziare la lotta, funziona solo se sono stati scelti 3 pokemon
        JButton startButton = ButtonFactory.createButton("START", 20, 320, 100, 40, e -> {
            
            for (Pokemon pokemon : playerTeam) {
                if (pokemon == null) {
                    return;
                }
            }
            
            Player player = new Player(playerNome, playerTeam);

            //crea l'avversario per iniziare la lotta
            Avversario avversario = new Avversario(pokemonDisponibili, player.getTeam());
            //chiude la schermata di start
            startScreen.dispose();
            //crea un nuovo controller per la battaglia
            BattleController battleController = new BattleController(player, avversario);
            //inizia la battaglia
            battleController.startBattle();
            
        });

        add(startButton);
    }

    /**
     * aggiunge un pokemon selezionato al team del giocatore
     * @param p il pokemon da aggiungere
     * @param icon lo sprite del pokemon
     */
    private void aggiungiPokemonAlTeam(Pokemon p, ImageIcon icon) {

        for (int i = 0; i < playerTeam.length; i++) {
            p.inizializza();
            Pokemon copia = new Pokemon(p);
            if (playerTeam[i] == null) {
                playerTeam[i] = copia;
                teamSlots[i].setIcon(icon);
                return;
            }
        }
    }
    
    
}
