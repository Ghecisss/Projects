package Data;

import View.AnimationType;

import java.util.List;
import java.util.ArrayList;

/**
 * gestisce l'aggiunta, rimozione e la notifica
 * degli observer per le animazioni
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class AnimationManager {
	/**
	 * la lista di observer
	 */
	private List<AnimationObserver> observers = new ArrayList<>();

	/**
	 * aggiunge un observer alla lista
	 * @param observer
	 */
	public void addObserver(AnimationObserver observer) {
		observers.add(observer);
	}

	/**
	 * rimuove un observer
	 * @param observer
	 */
	public void removeObserver(AnimationObserver observer) {
		observers.remove(observer);
	}

	/**
	 * notifica gli observer quando un'animazione è terminata
	 * @param type il tipo di animazione terminata
	 */
	public void notifyAnimationFinished(AnimationType type) {
		for (AnimationObserver observer : observers) {
			observer.onAnimationFinished(type);
		}
	}

}
