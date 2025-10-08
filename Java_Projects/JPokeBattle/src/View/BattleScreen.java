package View;

import Data.AnimationManager;
import Data.InputManager;
import Model.*;
import javax.swing.*;

/**
 * gestisce la schermata della lotta
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class BattleScreen extends JFrame {
    /**
     * il backgrounf della schermata
     */
	private JLabel background;
    /**
     * il giocatore attuale
     */
    private Player player;
    /**
     * l'avversario attuale
     */
    private Avversario avversario;
    /**
     * la barra degli hp del pokemon del giocatore in campo
     */
    private PlayerBar playerBar;
    /**
     * la label per lo sprite del pokemon del giocatore in campo
     */
    private JLabel playerPokemonLabel;
    /**
     * la label per lo sprite pokemon dell'avversario in campo
     */
    private JLabel enemyPokemonLabel;
    /**
     * il gestore delle animazioni del pokemon del giocatore
     */
    private SpriteAnimator playerAnimation;
    /**
     * il gestore delle animazioni del pokemon dell'avversario
     */
    private SpriteAnimator avversarioAnimation;
    /**
     * la barra degli hp del pokemon dell'avversario in campo
     */
    private AvversarioBar avversarioBar;
    /**
     * il textbox per mostrare i messaggi di lotta
     */
    private TextBox textBox;
    /**
     * il panel per il menu "mosse, switch, fuga"
     */
    private AzioniPlayer azioniPlayer;
    /**
     * il panel per la scelta della mossa del pokemon del giocatore
     */
    private MossePanel mossePanel;
    /**
     * il panel per sostituire una vecchia mossa con una nuova
     */
    private ReplacePanel replacePanel;
    /**
     * il panel per mostrare la squadra del giocatore e eventualmente switchare
     */
    private TeamPanel teamPanel;
    /**
     * manager per notificare gli observer quando si conclude un'animazione
     */
    private AnimationManager animationManager;
    /**
     * manager per notificare gli observer quando il giocatore inserisce un input
     */
    private InputManager inputManager;

    /**
     * costruttore della schermata di lotta
     * @param player il giocatore
     * @param avversario l'avversario
     * @param animationManager il manager delle animazioni per le notifiche delle animazioni
     * @param inputManager il manager degli input per le notifiche dei pulsanti
     */
    public BattleScreen(Player player, Avversario avversario, AnimationManager animationManager, InputManager inputManager) {
        this.player = player;
        this.avversario = avversario;
        this.animationManager = animationManager;
        this.inputManager = inputManager;

        
        setTitle("Battle Screen");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Aggiunge lo sfondo
        background = ImageLabelFactory.createImageLabel("pngs/battleBackground.png", 0, 0, 800, 600);
        background.setLayout(null); // Permette di aggiungere elementi sopra l'immagine
        add(background);

        setVisible(true);
    }

    /**
     * aggiorna la parte grafica dell'evoluzione di un pokemon
     */
    public void aggiornaEvo() {
    	background.remove(playerPokemonLabel);
    	background.remove(playerBar);
    	playerPokemonLabel = ImageLabelFactory.createImageLabel(
                "BackSprites/" + player.getTeam().getPokemonAttivo().getNome() + ".png",
                120, 440, 120, 120 
        );
    	background.add(playerPokemonLabel);
        playerAnimation = new SpriteAnimator(playerPokemonLabel, animationManager, background);
    	this.loadPlayerBar();
    	
    	background.revalidate();
    	background.repaint();
    }

    /**
     * carica a schermo il pokemon del giocatore e la sua barra degli hp/exp
     */
    public void loadPlayer() {
    	this.loadPlayerSprite();
    	this.loadPlayerBar();
    	background.revalidate();
    	background.repaint();
    }

    /**
     * carica a schermo il pokemon dell'avversario e la sua barra degli hp
     */
    public void loadAvversario() {
    	this.loadAvversarioBar();
    	this.loadAvversarioSprite();
    	background.revalidate();
    	background.repaint();
    	
    }

    /**
     * carica lo sprite del pokemon del giocatore
     * fuori dalla schermata (per permettere l'entrata animata)
     */
    public void loadPlayerSprite() {
        playerPokemonLabel = ImageLabelFactory.createImageLabel(
                "BackSprites/" + player.getTeam().getPokemonAttivo().getNome() + ".png",
                -120, 440, 120, 120 
        );
        background.add(playerPokemonLabel);

        playerAnimation = new SpriteAnimator(playerPokemonLabel, animationManager, background);
        playerAnimation.startAnimation(AnimationType.ENTRATA_PLAYER);
    }

    /**
     * carica lo sprite del pokemon dell'avversario
     * fuori dalla schermata (per permettere l'entrata animata)
     */
    public void loadAvversarioSprite() {
        enemyPokemonLabel = ImageLabelFactory.createImageLabel(
                "Sprites/" + avversario.getTeam().getPokemonAttivo().getNome() + ".png",
                800, 265, 80, 80 
        );
        background.add(enemyPokemonLabel);

        avversarioAnimation = new SpriteAnimator(enemyPokemonLabel, animationManager, background);
        avversarioAnimation.startAnimation(AnimationType.ENTRATA_AVVERSARIO);
    }

    /**
     * carica la barra degli hp/exp del pokemon del giocatore
     */
    public void loadPlayerBar() {
    	playerBar = new PlayerBar(player.getTeam().getPokemonAttivo());
        background.add(playerBar);
    }

    /**
     * carica la barra degli hp del pokemon dell'avversario
     */
    public void loadAvversarioBar() {
    	avversarioBar = new AvversarioBar(avversario.getTeam().getPokemonAttivo());
    	background.add(avversarioBar);
    }

    /**
     * carica il menu delle azioni del giocatore
     */
    public void loadAzioniPlayer() {
    	azioniPlayer = new AzioniPlayer(player.getTeam(), this, inputManager);
    	background.add(azioniPlayer);
    	background.revalidate();
    	background.repaint();
    }

    /**
     * carica il riquadro per i messaggi di testo
     */
    public void loadTextBox() {
    	textBox = new TextBox(animationManager);
    	background.add(textBox);
    }

    /**
     * caroca il menu delle mosse selezionabili
     */
    public void loadMossePanel() {
    	mossePanel = new MossePanel(player.getTeam().getPokemonAttivo(), this, inputManager);
    	background.add(mossePanel);
    	background.revalidate();
    	background.repaint();
    }

    /**
     * carica il panel per rimpiazzare una mossa
     * @param nuovaMossa la nuova mossa imparata
     * @param mosseAttuali le mosse giò conosciute
     * @param inputManager il manager degli input per le notifiche dei pulsanti
     */
    public void loadReplacePanel(Mossa nuovaMossa, Mossa[] mosseAttuali, InputManager inputManager) {
        this.replacePanel = new ReplacePanel(nuovaMossa, mosseAttuali, inputManager);
        this.replacePanel.setIsShowing(true);
        background.add(replacePanel);
        background.revalidate();
        background.repaint();
    }

    /**
     * carica il panel per switchare pokemon
     * @param forzato se un pokemon del giocatore viene sconfitto
     *                viene aperto forzatamente
     */
    public void loadTeamPanel(Boolean forzato) {
        this.teamPanel = new TeamPanel(player.getTeam(), this, inputManager, forzato);
        this.teamPanel.setIsShowing(true);
        background.add(teamPanel);
        background.revalidate();
        background.repaint();
    }

    /**
     * chiude il panel del team del giocatore
     */
    public void closeTeamPanel() {
        this.teamPanel.setIsShowing(false);
    	background.remove(teamPanel);
    	background.revalidate();
    	background.repaint();
    }

    /**
     * chiude il menu delle azioni
     */
    public void closeAzioniPlayer() {
    	background.remove(azioniPlayer);
    	background.revalidate();
    	background.repaint();
    }

    /**
     * chiude il menu delle mosse
     */
    public void closeMossePanel() {
    	background.remove(mossePanel);
    	background.revalidate();
    	background.repaint();
    }

    /**
     * rimuove il pokemon del giocatore e la sua barra degli hp/exp dalla schermata
     */
    public void unloadPlayer() {
        this.unloadPlayerSprite();
        this.unloadPlayerBar();
        this.closeAzioniPlayer();

    }

    /**
     *  rimuove il pokemon dell'avversario e la sua barra degli hp dalla schermata
     */
    public void unloadAvversario() {
        this.unloadAvversarioSprite();
        this.unloadAvversarioBar();
        background.revalidate();
        background.repaint();
    }

    /**
     * rimuove lo sprite del pokemon del giocatore
     */
    public void unloadPlayerSprite() {
        if (playerPokemonLabel != null) {
            playerAnimation.startAnimation(AnimationType.USCITA_PLAYER);
            playerPokemonLabel = null;
        }
    }

    /**
     * rimuove lo sprite del pokemon dell'avversario
     */
    public void unloadAvversarioSprite() {
        if (enemyPokemonLabel != null) {
            avversarioAnimation.startAnimation(AnimationType.USCITA_AVVERSARIO);
            enemyPokemonLabel = null;
        }
    }

    /**
     * rimuove la barra degli hp/exp del giocatore
     */
    public void unloadPlayerBar() {
        if (playerBar != null) {
            background.remove(playerBar);
            playerBar = null;
        }
    }

    /**
     * rimuove la barra degli hp dell'avversario
     */
    public void unloadAvversarioBar() {
        if (avversarioBar != null) {
            background.remove(avversarioBar);
            avversarioBar = null;
        }
    }

    /**
     * determina quale pokemon sta attaccando e
     * avvia l'animazione di attacco
     * @param attaccante il pokemon attaccante
     */
    public void mostraAttacco(Pokemon attaccante) {
        // Determina se l'attaccante è il Pokémon del player o dell'avversario
        SpriteAnimator attaccanteAnimation = (attaccante == player.getTeam().getPokemonAttivo()) ? playerAnimation : avversarioAnimation;

        // Avvia l'animazione di attacco
        attaccanteAnimation.startAnimation(AnimationType.ATTACCO);
    }

    /**
     * ritorna il textbox della schermata
     * @return il textbox
     */
    public TextBox getTextBox() {
    	return this.textBox;
    }

    /**
     * ritorna la barra dell'avversario
     * @return la barra dell'avversario
     */
    public AvversarioBar getAvversarioBar() {
    	return this.avversarioBar;
    }

    /**
     * ritorna la barra del giocatore
     * @return la barra del giocatore
     */
    public PlayerBar getPlayerBar() {
    	return this.playerBar;
    }

    /**
     * ritorna il panel per il cambio di pokemon del giocatore
     * @return il panel per il team del giocatore
     */
    public TeamPanel getTeamPanel() {
    	return this.teamPanel;
    }

    /**
     * ritorna il panel per la sostituzione di una mossa
     * @return il panel per la sostituzione
     */
    public ReplacePanel getReplacePanel() { return this.replacePanel;}

    /**
     * ritorna il label di background
     * @return il label di background
     */
    public JLabel getBackGround() {
    	return this.background;
    }
}
