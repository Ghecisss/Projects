package View;

import javax.swing.JButton;
import java.awt.event.ActionListener;

/**
 * usa il factory pattern per creare dei pulsanti
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class ButtonFactory {
    /**
     * crea il pulsante
     * @param text il testo del pulsante
     * @param x valore x della posizione del pulsante
     * @param y valore y della posizione del pulsante
     * @param width larghezza del pulsante
     * @param height altezza del pulsante
     * @param action codice eseguito quando il pulsante viene attivato
     * @return il pulsante
     */
    public static JButton createButton(String text, int x, int y, int width, int height, ActionListener action) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height); 
        button.addActionListener(action);
        return button;
    }
}
