package View;

import javax.swing.*;
import Model.Pokemon;
import java.awt.*;

/**
 * la barra degli hp e degli exp del pokemon
 * del giocatore
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class PlayerBar extends PokemonBar {
    /**
     * la barra dei punti esperienza esclusiva al pokemon del giocatore
     */
    private JProgressBar expBar;
    /**
     * crea la barra degli hp e degli exp del pokemon del giocatore
     */
    public PlayerBar(Pokemon pokemon) {
        super(pokemon, "/pngs/playerBar.png");
        int panelWidth = barImage.getIconWidth();
        int panelHeight = barImage.getIconHeight();
        setBounds(15, 300, panelWidth, panelHeight);
        nameLabel.setBounds(30, 8, 100, 20);
        levelLabel.setBounds(156, 8, 50, 20);
        hpBar.setBounds(88, 30, 90, 10);
        
        expBar = new JProgressBar(0, pokemon.getExpRichiesta());  
        aggiornaEXP(pokemon);
        expBar.setBounds(59, 59, 120, 8); 
        expBar.setForeground(Color.BLUE); 
        expBar.setStringPainted(false);
        add(expBar);
    }

    /**
     * aggiorna la barra dei punti esperienza
     * @param pokemon
     */
    public void aggiornaEXP(Pokemon pokemon) {
        // Aggiorna i valori della barra EXP
        expBar.setMaximum(pokemon.getExpRichiesta());
        expBar.setValue(pokemon.getExpAttuale());

        // Aggiorna il livello nella UI
        levelLabel.setText(String.valueOf(pokemon.getLivello()));
    }



}
