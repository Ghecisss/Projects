package View;

import Data.AnimationManager;

import javax.swing.*;
import java.awt.*;

/**
 * il textbox usato per i messaggi di lotta
 * durante le partite
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class TextBox extends JPanel {
    /**
     * l'area dove vengono mostrate le scritte
     */
    private JTextArea textArea;
    /**
     * il png del textbox
     */
    private JLabel textBoxImage;
    /**
     * il timer per l'apparizione graduale delle scritte
     */
    private Timer textTimer;
    /**
     * panel a strati per mostrare sia la scritta che il png
     */
    private JLayeredPane layeredPane;
    /**
     * il manager che notifica quando un'animazione del textbox si è conclusa
     */
    private AnimationManager animationManager;

    /**
     * crea il textbox nella schermata di lotta
     * @param animationManager il manager degli observer delle animazioni
     */
    public TextBox(AnimationManager animationManager) {
    	this.animationManager = animationManager;
        setLayout(null);
        setBounds(20, 20, 400, 100);  // Impostazioni della dimensione del pannello
        setOpaque(false);

        // Creazione del JLayeredPane per gestire i livelli
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 400, 100);  // Impostazione dei bounds del pannello con strati
        add(layeredPane);

        // Aggiunta dell'immagine della textbox nello strato più basso
        textBoxImage = ImageLabelFactory.createImageLabel("pngs/textbox.png", 0, 0, 400, 100);
        textBoxImage.setBounds(0, 0, 400, 100);
        layeredPane.add(textBoxImage, JLayeredPane.DEFAULT_LAYER);

        // Aggiunta del JTextArea sopra l'immagine
        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.BOLD, 20));
        textArea.setForeground(Color.BLACK);
        textArea.setBounds(42, 10, 360, 60);  // Spazio per il testo
        textArea.setOpaque(false);  // Impostato su false per vedere l'immagine sottostante
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);  // Non permettere la modifica da parte dell'utente
        textArea.setBackground(new Color(0, 0, 0, 0));  // Rende trasparente il background del JTextArea
        layeredPane.add(textArea, JLayeredPane.PALETTE_LAYER);
        textArea.setFocusable(false);

        add(layeredPane);
    }

    /**
     * Mostra il testo con un effetto di digitazione graduale.
     * @param text Il testo da visualizzare.
     */
    public void mostraTesto(String text) {
        textArea.setText("");  // Resetta il testo
        if (textTimer != null && textTimer.isRunning()) {
            textTimer.stop();
        }

        final int[] index = {0};  
        textTimer = new Timer(25, e -> {
            if (index[0] < text.length()) {
                // Aggiunge una lettera alla volta
                textArea.setText(text.substring(0, index[0] + 1));  
                index[0]++;
            } else {
                ((Timer) e.getSource()).stop();
                animationManager.notifyAnimationFinished(AnimationType.TEXTBOX);
            }
        });
        textTimer.start();
    }
}
