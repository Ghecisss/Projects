package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * il panel di input del nome del giocatore
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class NomePlayerInput extends JPanel {
    /**
     * Il campo dove scrivere il nome del giocatore
     */
    private JTextField nameField;
    /**
     * il pulsante per confermare la scelta del nome
     */
    private JButton okButton;

    /**
     * crea il panel di input del nome
     * @param onNameConfirmed esegue delle operazioni quando viene confermato il nome
     */
    public NomePlayerInput(ActionListener onNameConfirmed) {
        setLayout(null); 
        setBounds(200, 200, 400, 200);
        setBackground(new Color(0, 0, 0, 180)); 

        JLabel label = new JLabel("Inserisci il tuo nome:");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setBounds(120, 30, 200, 30);
        add(label);

        nameField = new JTextField();
        nameField.setBounds(100, 70, 200, 30);
        add(nameField);

        okButton = ButtonFactory.createButton("OK", 150, 120, 100, 40, e -> {
            String name = nameField.getText().trim();
            if (name.length() > 8) {
                name = name.substring(0, 8); 
            }
            if (!name.isEmpty()) {
                onNameConfirmed.actionPerformed(e);
                
            } else {
                JOptionPane.showMessageDialog(this, "Inserisci un nome valido!", "Errore", JOptionPane.ERROR_MESSAGE);
            }
            
            
            
        });

        add(okButton);
    }

    /**
     * ritorna il nome del giocatore
     * @return il nome del giocatore
     */
    public String getPlayerName() {
        return nameField.getText().trim();
    }
}
