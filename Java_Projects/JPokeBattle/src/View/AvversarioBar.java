package View;

import Model.Pokemon;

/**
 * gestisce la barra degli hp del pokemon avversario
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class AvversarioBar extends PokemonBar {
    /**
     * costruttore della barra degli hp, mostra gli hp, il livello
     * e il nome del pokemon avversario
     * @param pokemon
     */
    public AvversarioBar(Pokemon pokemon) {
       super(pokemon, "/pngs/enemyBar.png");
        int panelWidth = barImage.getIconWidth();
        int panelHeight = barImage.getIconHeight();
        setBounds(560, 170, panelWidth, panelHeight);
        nameLabel.setBounds(30, 8, 100, 20);
        levelLabel.setBounds(156, 8, 50, 20);
        hpBar.setBounds(85, 30, 90, 10);
    }

}
