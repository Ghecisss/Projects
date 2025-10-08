package View;

import Data.InputManager;
import Model.Mossa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * il panel per la sostituzione di una mossa di un pokemon
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class ReplacePanel extends JPanel {
    /**
     * la nuova mossa imparata dal pokemon
     */
    private final Mossa nuovaMossa;
    /**
     * i 5 pulsanti per la mossa nuova e le 4 mosse già conosciute
     */
    private final JButton[] mossaButtons = new JButton[5];
    /**
     * il pulsante per confermare la scelta
     */
    private final JButton confermaButton;
    /**
     * l'indice che rappresenta la scelta della mossa da sostituire
     */
    private int selectedIndex = -1;
    /**
     * booeleano che dice se il panel è attualmente visibile a schermo
     */
    private boolean isShowing = false;
    /**
     * il manager per notificare quando viene data la conferma della mossa da sostituire
     */
    private final InputManager inputManager;

    /**
     * crea il panel per sostituire una mossa del pokemon del giocatore
     * @param nuovaMossa la nuova mossa
     * @param mosseAttuali le mosse già conosciute dal pokemon
     * @param inputManager il manager degli observer degli input
     */
    public ReplacePanel(Mossa nuovaMossa, Mossa[] mosseAttuali, InputManager inputManager) {
        this.nuovaMossa = nuovaMossa;
        this.inputManager = inputManager;

        setLayout(null);
        setBounds(240, 210, 300, 200); // Dimensioni e posizione personalizzabili
        setBackground(new Color(0, 0, 0, 180));
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 3)); // <<--- questa riga


        int width = 100;
        int height = 30;
        int startX = 20;
        int startY = 20;
        int spacing = 10;

        // Crea 4 pulsanti per le mosse attuali
        for (int i = 0; i < 4; i++) {
            int index = i;
            String text = (mosseAttuali[i] != null) ? mosseAttuali[i].getNomeMossa() : "Vuoto";
            mossaButtons[i] = ButtonFactory.createButton(text, startX, startY + i * (height + spacing), width, height, e -> {
                selectedIndex = index;
                evidenziaSelezione();
            });
            add(mossaButtons[i]);
        }

        // Pulsante per mantenere tutte le mosse esistenti (non imparare la nuova)
        mossaButtons[4] = ButtonFactory.createButton(nuovaMossa.getNomeMossa(), startX + width + spacing, startY, width, height, e -> {
            selectedIndex = 4;
            evidenziaSelezione();
        });
        add(mossaButtons[4]);

        // Pulsante di conferma
        confermaButton = ButtonFactory.createButton("OK", startX + width + spacing, startY + 3 * (height + spacing), width, height, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(ReplacePanel.this, "Seleziona una mossa da sostituire o rifiuta la nuova.");
                    return;
                }

                if (selectedIndex < 4) {
                    inputManager.notifyReplace(selectedIndex, nuovaMossa); // Sostituisce la mossa selezionata
                }

                // Chiudi il pannello
                Container parent = ReplacePanel.this.getParent();
                if (parent != null) {
                    parent.remove(ReplacePanel.this);
                    parent.revalidate();
                    parent.repaint();
                }
                isShowing = false;
            }
        });
        add(confermaButton);
    }

    /**
     * cambia il colore del pulsante selezionato
     */
    private void evidenziaSelezione() {
        for (int i = 0; i < mossaButtons.length; i++) {
            if (i == selectedIndex) {
                mossaButtons[i].setBackground(Color.GREEN);
            } else {
                mossaButtons[i].setBackground(null);
            }
        }
    }

    /**
     * ritorna true se il panel è a schermo
     * @return il valore booleano isShowing
     */
    public boolean isShowing() {return this.isShowing;}

    /**
     * setta il valore booleano isShowing
     * @param isShowing
     */
    public void setIsShowing(boolean isShowing) {this.isShowing = isShowing;}
}
