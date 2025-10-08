package View;
import Model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * la schermata di sconfitta
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class DefeatScreen extends JFrame {
    /**
     * costruttore della schermata di sonfitta
     * @param player prende il giocatore in input per aggioranre la classifica
     */
    public DefeatScreen(Player player) {
        setTitle("Game Over");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Uso il layout assoluto per gestire le posizioni
        setLocationRelativeTo(null); // Centra la finestra

        // Carica e scala l'immagine di background
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/pngs/defeatScreen.jpg"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        
        JLabel background = new JLabel(scaledIcon);
        background.setBounds(0, 0, 800, 600);
        
        // Pulsante Home
        JButton homeButton = ButtonFactory.createButton("Home", 350, 500, 100, 50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Chiude la schermata attuale

                StartScreen startScreen = new StartScreen(); // Apri StartScreen
                startScreen.setVisible(true); // Assicura che venga visualizzata
            }
        });

        // Usa un JPanel per sovrapporre il pulsante senza influenzare lo sfondo
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 800, 600);
        panel.setOpaque(false);
        panel.add(homeButton);

        // Aggiunge gli elementi al frame
        add(panel);
        add(background);

        setVisible(true);

        Classifica.getInstance().aggiungiPartita(player.getNome(), player.getVittorieConsecutive());
    }
}
