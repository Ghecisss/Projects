package View;

import Data.AnimationManager;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * gestisce le animazioni degli sprite
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class SpriteAnimator {
	/**
	 * la label usata per gli sprite di un pokemon
	 */
	private JLabel sprite;
	/**
	 * la posizione iniziale dello sprite
	 */
	private int startX, startY;
	/**
	 * la posizione dello sprite a termine dell'animazione
	 */
	private int targetX, targetY;
	/**
	 * la velocità dell'animazione
	 */
	private int speed = 10;
	/**
	 * il manager che notifica quando un'animazione è terminata
	 */
	private AnimationManager animationManager;
	/**
	 * il tipo di animazione da eseguire
	 */
	private AnimationType animationType;
	/**
	 * la label per il background della schermata
	 */
	private JLabel background;

	/**
	 * crea il gestore delle animazioni
	 * @param sprite lo sprite da animare
	 * @param animationManager il manager degli observer delle animazioni
	 * @param background il label dove si trova lo sprite
	 */
	public SpriteAnimator(JLabel sprite, AnimationManager animationManager, JLabel background) {
		this.background = background;
		this.sprite = sprite;
		this.animationManager = animationManager;
	}

	/**
	 * inizia un'animazione, eseguendo azioni diverse in base al tipo
	 * @param type il tipo di animazione
	 */
	public void startAnimation(AnimationType type) {
		this.animationType = type;
		switch(type) {
		
		case ENTRATA_PLAYER:
            startX = -sprite.getWidth();
            targetX = 120; // Posizione finale
            muoviOrizzontale(true, false);
            break;

        case ENTRATA_AVVERSARIO:
            startX = 800; // Fuori dallo schermo a destra
            targetX = 550; // Posizione finale
            muoviOrizzontale(false, false);
            break;

        case USCITA_PLAYER:
            startX = sprite.getX();
            targetX = -sprite.getWidth();
            muoviOrizzontale(false, true);
            break;

        case USCITA_AVVERSARIO:
            startX = sprite.getX();
            targetX = 800;
            muoviOrizzontale(true, true);
            break;

        case ATTACCO:
            startY = sprite.getY();
            targetY = startY - 40; // Salto verso l'alto di 20px
            attackAnimation();
            break;
			}	
		}

	/**
	 * muove orizzontalmente gli sprite
	 * @param versoDestra true se lo sprite deve muoversi verso destra
	 * @param removeAfter true se lo sprite deve muoversi verso sinistra
	 */
	private void muoviOrizzontale(boolean versoDestra, boolean removeAfter) {
	    sprite.setLocation(startX, sprite.getY());
	    Timer timer = new Timer();
	    TimerTask task = new TimerTask() {
	        int currentX = startX;

	        @Override
	        public void run() {
	            if ((versoDestra && currentX >= targetX) || (!versoDestra && currentX <= targetX)) {
	                sprite.setLocation(targetX, sprite.getY());
	                timer.cancel();
	                animationManager.notifyAnimationFinished(animationType);

	                if (removeAfter) {
	                    SwingUtilities.invokeLater(() -> {
	                    	if (background != null) {
	                    		background.remove(sprite);
	                    		background.revalidate();
	                    		background.repaint();
	                    	}
	                    });
	                }
	                return;
	            }

	            currentX += versoDestra ? speed : -speed;
	            sprite.setLocation(currentX, sprite.getY());
	        }
	    };
	    timer.schedule(task, 0, 20);
	}

	/**
	 * animazione di attacco,
	 * è un rapido movimento verticale dello sprite
	 */
	private void attackAnimation() {
		Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            boolean goingUp = true;
            int currentY = startY;

            @Override
            public void run() {
                if (goingUp) {
                    currentY -= speed;
                    if (currentY <= targetY) goingUp = false;
                } else {
                    currentY += speed;
                    if (currentY >= startY) {
                        sprite.setLocation(sprite.getX(), startY);
                        timer.cancel();
                        animationManager.notifyAnimationFinished(animationType);
                        return;
                    }
                }
                sprite.setLocation(sprite.getX(), currentY);
            }
        };
        timer.schedule(task, 0, 40);
	}
}
