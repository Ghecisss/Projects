package View;

import javax.swing.*;
import java.awt.*;

/**
 * Gestisce la creazione della schermata start e dei
 * suoi elementi: classifica, inizio di una nuova partita,
 * scelta del nome e del team del giocatore
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class StartScreen extends JFrame {
    /**
     * il contenitore dei vari "strati" della GUI
     */
	private JLayeredPane layeredPane;
    /**
     * il panel per inserire il nome del giocatore
     */
    private NomePlayerInput nomeInputPanel;
    /**
     * il panel per scegliere il team del giocatore
     */
    private TeamPlayerInput teamInputPanel;
    /**
     * il pulsante per iniziare una nuova partita
     */
    private JButton nuovaPartitaBtn;
    /**
     * il pulsante per aprire la classifica
     */
    private JButton classificaBtn;
    /**
     * il panel per mostrare il background
     */
    private BackGroundPanel backGroundPanel;
    /**
     * il panel che contiene la classifica
     */
    private ClassificaPanel classificaPanel;


    /**
     * costruttore della schermata di start
     */
    public StartScreen() {
        setTitle("JPokeBattle - Start");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 600));

        backGroundPanel = new BackGroundPanel("pngs/BackgroundPixel.gif");
        backGroundPanel.setBounds(0, 0, 800, 600);
        layeredPane.add(backGroundPanel, JLayeredPane.DEFAULT_LAYER);
       
        nuovaPartitaBtn = ButtonFactory.createButton("Nuova Partita", 300, 200, 100, 50, e -> apriNomeInput());
        nuovaPartitaBtn.setBounds(300, 400, 200, 50);
        layeredPane.add(nuovaPartitaBtn, JLayeredPane.PALETTE_LAYER);

        classificaBtn = ButtonFactory.createButton("Classifica", 300, 300, 100, 50, e -> loadClassifica());
        classificaBtn.setBounds(300, 460, 200, 50);
        layeredPane.add(classificaBtn, JLayeredPane.PALETTE_LAYER);

        add(layeredPane);
    }

    /**
     * apre il panel per inserire il nome del giocatore
     */
    public void apriNomeInput() {
    	nuovaPartitaBtn.setEnabled(false); //disabilita i pulsanti per sicurezza
        classificaBtn.setEnabled(false);
    	nomeInputPanel = new NomePlayerInput(e -> apriTeamInput());
    	layeredPane.add(nomeInputPanel, JLayeredPane.MODAL_LAYER);
    	layeredPane.repaint();
    }

    /**
     * apre il panel per la scelta del team del giocatore
     */
    public void apriTeamInput() {
    	String playerNome = nomeInputPanel.getPlayerName(); //salva il nome del giocatore
    	layeredPane.remove(nomeInputPanel);
    	teamInputPanel = new TeamPlayerInput(playerNome, this); //il nome viene passato al costruttore del panel
    	layeredPane.add(teamInputPanel, JLayeredPane.MODAL_LAYER);
    	
    }

    /**
     * renderizza la gif di background
     */
    private static class BackGroundPanel extends JPanel {
    	private ImageIcon backgroundGif;
    	
    	public BackGroundPanel(String imagePath) {
    		backgroundGif = new ImageIcon(getClass().getResource("/" + imagePath));
    		setOpaque(false);
    		
    	}
    	
    	@Override
    	protected void paintComponent(Graphics g) {
    		super.paintComponent(g);
    		g.drawImage(backgroundGif.getImage(), 0, 0, getWidth(), getHeight(), this);
    	}
    }

    /**
     * mostra a schermo la classifica
     */
    public void loadClassifica() {
        nuovaPartitaBtn.setEnabled(false);
        classificaBtn.setEnabled(false);
        classificaPanel = new ClassificaPanel(() -> {
            nuovaPartitaBtn.setEnabled(true); //callback quando viene chiusa la classifica per ri-abilitare i pulsanti
            classificaBtn.setEnabled(true);
        });
        layeredPane.add(classificaPanel, JLayeredPane.MODAL_LAYER);
    }
}
