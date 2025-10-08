package View;

import javax.swing.*;

/**
 * usa il factory pattern per creare i label che
 * conterranno dei png
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class ImageLabelFactory {
    /**
     * crea il label per l'immagine
     * @param imagePath il filepath dell'immagine
     * @param x la x della posizione del label
     * @param y la y della posizione del label
     * @param width la larghezza del label
     * @param height l'altezza del label
     * @return il label
     */
    public static JLabel createImageLabel(String imagePath, int x, int y, int width, int height) {
        ImageIcon icon = new ImageIcon(ImageLabelFactory.class.getResource("/" + imagePath));

        JLabel label = new JLabel(icon);
        label.setBounds(x, y, width, height);
        return label;
    }
}
