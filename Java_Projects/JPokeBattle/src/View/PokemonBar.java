package View;

import javax.swing.*;
import Model.Pokemon;
import java.awt.*;

/**
 * superclasse per la creazione delle barre
 * degli hp dei pokemon in lotta
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class PokemonBar extends JPanel {
    /**
     * la barra per mostrare gli hp di un pokemon in lotta
     */
    protected JProgressBar hpBar;
    /**
     * il label per mostrare il nome del pokemon in lotta
     */
    protected JLabel nameLabel;
    /**
     * il label per mostrare il livello del pokemon in lotta
     */
    protected JLabel levelLabel;
    /**
     * il png della barra degli hp
     */
    protected ImageIcon barImage;

    /**
     * costruttore della barra degli hp,
     * prende un pokemon come parametro e
     * mostra i suoi dati rilevanti sulla barra
     * @param pokemon, il pokemon associato alla barra creata
     */
    public PokemonBar(Pokemon pokemon, String filepath) {
        String nomePokemon = pokemon.getNome();
        //"/pngs/enemyBar.png"
        //"/pngs/playerBar.png"
        barImage = new ImageIcon(getClass().getResource(filepath));

        int panelWidth = barImage.getIconWidth();
        int panelHeight = barImage.getIconHeight();
        setLayout(null);
        setOpaque(false);

        nameLabel = new JLabel(nomePokemon);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(Color.BLACK);
        add(nameLabel);

        levelLabel = new JLabel(String.valueOf(pokemon.getLivello()));
        levelLabel.setFont(new Font("Arial", Font.BOLD, 14));
        levelLabel.setForeground(Color.BLACK);
        add(levelLabel);

        hpBar = new JProgressBar(0, pokemon.getStatistiche().getMaxHp());
        aggiornaHP(pokemon.getStatistiche().getHp(), pokemon.getStatistiche().getMaxHp());
        hpBar.setStringPainted(false);
        add(hpBar);
    }

    /**
     * aggiunge il label per il png della barra
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(barImage.getImage(), 0, 0, getWidth(), getHeight(), this);
    }

    /**
     * aggiorna il valore rappresentato dalla barra
     * @param hpAttuale
     * @param maxHp
     */
    public void aggiornaHP(int hpAttuale, int maxHp) {
        // Aggiorna direttamente il valore della barra HP
        hpBar.setValue(hpAttuale);

        // Calcola la percentuale della barra HP
        int percentualeFinale = (hpAttuale * 100) / maxHp;

        // Aggiorna il colore della barra in base alla percentuale
        aggiornaColoreBarra(percentualeFinale);

    }

    /**aggiorna il colore della barra in base alla percentuale di hp rimanenti
     * @param percentuale
     */
    private void aggiornaColoreBarra(int percentuale) {
        // Cambia il colore della barra HP in base alla percentuale rimanente
        if (percentuale <= 10) {
            hpBar.setForeground(Color.RED);
        } else if (percentuale <= 50) {
            hpBar.setForeground(Color.YELLOW);
        } else {
            hpBar.setForeground(Color.GREEN);
        }
    }
}
