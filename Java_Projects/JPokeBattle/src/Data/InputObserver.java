package Data;

import Model.*;

/**
 * interfaccia da implementare per gli observer degli input
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public interface InputObserver {
	void onMossaSelezionata(Mossa mossa);
	void onSwitchSelezionato();
	void onSwitchForzato();
	void onFuga();
	void onReplaceSelezionato(int i, Mossa nuova);
}
