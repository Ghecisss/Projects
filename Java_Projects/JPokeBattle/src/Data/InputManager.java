package Data;

import Model.*;
import View.SpriteAnimator;

import java.util.List;
import java.util.ArrayList;

/**
 * aggiunge, rimuove e notifica gli observer degli input
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class InputManager {
	/**
	 * la lista degli observer
	 */
	private List<InputObserver> observers = new ArrayList<>();

	/**
	 * aggiunge un observer
	 * @param observer
	 */
	public void addObserver(InputObserver observer) {
		observers.add(observer);
	}

	/**
	 * rimuove un observer
	 * @param observer
	 */
	public void removeObserver(InputObserver observer) {
		observers.remove(observer);
	}

	/**
	 * notifica gli observer che è stata selezionata una mossa
	 * @param mossa
	 */
	public void notifyMossaSelezionata(Mossa mossa) {
		for (InputObserver observer : observers) {
			observer.onMossaSelezionata(mossa);
		}
	}

	/**
	 * notifica gli observer che è stato selezionato il pulsante di switch
	 */
	public void notifySwitchSelezionato() {
		for (InputObserver observer : observers) {
			observer.onSwitchSelezionato();
		}
	}

	/**
	 * notifica gli observer che è stato eseguito uno switch forzato
	 */
	public void notifySwitchForzato() {
		for (InputObserver observer : observers) {
			observer.onSwitchForzato();
		}
	}

	/**
	 * notifica gli observer che è stato selezionato il pulsante di fuga
	 */
	public void notifyFuga() {
		for (InputObserver observer : observers) {
			observer.onFuga();
		}
	}

	/**
	 * notifica gli observer che è stata rimpiazzata una mossa
	 * @param i
	 * @param nuova
	 */
	public void notifyReplace(int i, Mossa nuova) {
		for (InputObserver observer : observers) {
			observer.onReplaceSelezionato(i, nuova);
		}
	}

}
