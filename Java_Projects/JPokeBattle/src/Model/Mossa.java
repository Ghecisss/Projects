package Model;

/**
 *rappresenta una mossa utilizzabile da in pokemon
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class Mossa {
	/**
	 * il nome della mossa
	 */
	private String nomeMossa;
	/**
	 * il tipo della mossa
	 */
	private Tipo tipo;
	/**
	 * la potenza della mossa
	 */
	private int potenza;
	/**
	 * la precisione della mossa
	 */
	private int precisione;
	/**
	 * la categoria della mossa
	 */
	private Categoria categoria;

	/**
	 * costruttore principale, crea una mossa in base agli attributi
	 * definiti nel json mosseImparabili
	 * @param nomeMossa il nome della mossa
	 * @param tipo il tipo della mossa
	 * @param potenza la potenza della mossa
	 * @param precisione la precisione della mossa
	 * @param categoria la categoria della mossa
	 */
	public Mossa(String nomeMossa, Tipo tipo, int potenza, int precisione, Categoria categoria) {
		this.nomeMossa = nomeMossa;
		this.tipo = tipo;
		this.potenza = potenza;
		this.precisione = precisione;
		this.categoria = categoria;
	}

	/**
	 * costruttore di deep copy delle mosse
	 * @param originale la mossa originale dal copiare
	 */
	public Mossa(Mossa originale) {
		this.nomeMossa = originale.nomeMossa;
		this.tipo = originale.tipo;
		this.potenza = originale.potenza;
		this.precisione = originale.precisione;
		this.categoria = originale.categoria;
	}

	/**
	 * ritorna il nome della mossa
	 * @return il nome della mossa
	 */
	public String getNomeMossa() {
		return this.nomeMossa;
	}

	/**
	 * ritorna il tipo della mossa
	 * @return il tipo della mossa
	 */
	public Tipo getTipo() {
		return this.tipo;
	}

	/**
	 * ritorna la potenza della mossa
	 * @return la potenza della mossa
	 */
	public int getPotenza() {
		return this.potenza;
	}

	/**
	 * ritorna la categoria della mossa
	 * @return la categoria della mossa
	 */
	public Categoria getCategoria() {
		return this.categoria;
	}

}
