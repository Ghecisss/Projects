package Model;

/**
 * Classe per la gestione delle mosse attive
 * di un pokemon
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class MosseAttive{
	/**
	 * l'array che contiene le mosse
	 */
	private Mossa[] mosseAttive;
	/**
	 * un contatore per il numero di mosse nell'array
	 */
	private int count;
	/**
	 * la mossa usata da un giocatore o un avversario in un turno di yna lotta
	 */
	private Mossa mossaScelta;

	/**
	 * Costruttore principale, crea un oggetto con
	 * un array di 4 oggetti di tipo Mossa e un int
	 * per contare quante mosse effettive si trovano
	 * nell'array
	 */
	public MosseAttive() {
		this.mosseAttive = new Mossa[4];
		this.count = 0;
	}

	/**
	 * Costruttore secondario, crea una deep copy
	 * prendendo un oggetto MosseAttive in input
	 * @param originale l'oggetto copiato
	 */
	public MosseAttive(MosseAttive originale) {
		this.mosseAttive = new Mossa[originale.mosseAttive.length];
		for (int i = 0; i < originale.mosseAttive.length; i++) {
			if (originale.mosseAttive[i] != null) {
				this.mosseAttive[i] = new Mossa(originale.mosseAttive[i]); // Presuppone costruttore copia in Mossa
			}
		}
		this.count = originale.count;
		this.mossaScelta = (originale.mossaScelta != null) ? new Mossa(originale.mossaScelta) : null; // Copia della mossa scelta
	}

	/**
	 * ritorna l'array di mosse
	 * @return l'array di oggetti di tipo Mossa
	 */
	public Mossa[] getMosse() {
		return this.mosseAttive;
	}

	/**
	 * ritorna il numero di mosse nell'array
	 * @return il numero di mosse nell'array
	 */
	public int getCount() {
		return this.count;
	}

	/**
	 * metodo per aggiungere una mossa all'array,
	 * aggiorna il numero di mosse nell'array
	 * @param mossa la mossa da aggiungere
	 * @return un valore booleano, se è falso allora l'array è pieno
	 * e bisogna rimpiazzare una delle mosse nell'array
	 */
	public boolean addMossa(Mossa mossa) {
		if (count < 4) {
			mosseAttive[count] = mossa;
			count++;
			return true;
		}
		return false;
	}

	/**
	 * metodo per rimpiazzare una mossa nell'array
	 * @param i l'indice della mossa da rimpizzare
	 * @param nuova la nuova mossa da inserire nell'array
	 */
	public void replaceMossa(int i, Mossa nuova) {
		if (i >= 0 && i < 4) {
			mosseAttive[i] = nuova;
		}
	}

	/**
	 * setta la mossa scelta, cioè la
	 * mossa che un giocatore (o l'IA) ha scelto
	 * di utilizzare
	 * @param mossaScelta
	 */
	public void setMossaScelta(Mossa mossaScelta) {
		this.mossaScelta = mossaScelta;
	}

	/**
	 * ritorna la mossa scelta dal giocatore o dall'IA
	 * @return la mossa scelta
	 */
	public Mossa getMossaScelta() {
		return this.mossaScelta;
	}

}
