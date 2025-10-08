package View;

import javax.swing.*;
import Model.*;
import Controller.*;
import Parser.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * la schermata di vittoria
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class VictoryScreen extends JFrame {
    /**
     * il giocatore attuale
     */
	private Player player;

    /**
     * crea la schermata di vittoria, incrementa il numeero
     * di vittorie consecutive del giocatore, e aggiorna la classifica
     * in caso si voglia terminare la partita e tornare alla home
     * @param player
     */
    public VictoryScreen(Player player) {
    	this.player = player;
        player.incrementaVittorie();
        setTitle("Victory!");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null); // Centra la finestra

        // Carica e scala l'immagine di background
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/pngs/victoryScreen.jpg"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel background = new JLabel(scaledIcon);
        background.setBounds(0, 0, 800, 600);

        // Pulsante Home (torna alla StartScreen)
        JButton homeButton = ButtonFactory.createButton("Home", 250, 500, 120, 50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Classifica.getInstance().aggiungiPartita(player.getNome(), player.getVittorieConsecutive());
                dispose(); // Chiude la schermata attuale

                StartScreen startScreen = new StartScreen(); // Apri StartScreen
                startScreen.setVisible(true);
            }
        });

        // Pulsante Continua (prosegue con la prossima battaglia)
        JButton continueButton = ButtonFactory.createButton("Continua", 430, 500, 120, 50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Chiude la schermata attuale
                for(int i = 0; i < 3; i++) {
                    if (player.getTeam().getSquadra()[i].getStatistiche().getHp() >0) {
                        player.getTeam().setPokemonAttivo(i);
                        break;
                    }
                }
                List<Pokemon> pokemonDisponibili = PokemonParser.parsePokemon("src/risorse/pokemon.json");
                
                // Apri la prossima schermata di battaglia (supponendo esista BattleScreen)
                Avversario avversario = new Avversario(pokemonDisponibili, player.getTeam());
                BattleController battleController = new BattleController(player, avversario);
                battleController.startBattle();

            }
        });

        // Usa un JPanel trasparente per posizionare i pulsanti sopra lo sfondo
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 800, 600);
        panel.setOpaque(false);
        panel.add(homeButton);
        panel.add(continueButton);

        // Aggiunge gli elementi al frame
        add(panel);
        add(background);

        setVisible(true);
    }
}
