package Main;
import View.StartScreen;

import javax.swing.*;

/**
 *  Classe main del progetto JPokeBattle
 *  avvia la schermata iniziale del gioco
 *
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class JPokeBattle {

    /**
     * Metodo main: punto di ingresso del programma
     * @param args (non utilizzati)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StartScreen startScreen = new StartScreen();
            startScreen.setVisible(true);
        });
    }
}
