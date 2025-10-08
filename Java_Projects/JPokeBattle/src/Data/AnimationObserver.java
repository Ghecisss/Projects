package Data;

import View.AnimationType;

/**
 * interfaccia degli observer delle animazioni
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public interface AnimationObserver {
	/**
	 * metodo da implementare dagli observer,
	 * definirà le operazioni da svolgere alla fine di
	 * un'animazione
	 * @param type il tipo di animazione terminata
	 */
	void onAnimationFinished(AnimationType type);
}
