package Model;

import java.util.List;
import java.util.ArrayList;
import Parser.MosseImparabiliParser;
import Parser.PokemonParser;
import Parser.StatisticheParser;

/**
 * Classe per la creazione degli oggetti rappresentanti i pokemon,
 * gestisce sia la creazione dei pokemon che il loro sviluppo.
 * Alcuni attributi dei pokemon (le statistiche e le mosse utilizzabili)
 * sono gestiti tramite le loro rispettive classi, quindi c'è un sistema di
 * oggetti innestati
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class Pokemon {
	/**
	 * il nome del pokemon
	 */
	private String nome;
	/**
	 * il livello del pokemon
	 */
	private int livello;
	/**
	 * il tipo principale del pokemon
	 */
	private Tipo tipo;
	/**
	 * il tipo secondario del pokemon
	 */
	private Tipo tipoSecondario;
	/**
	 * le statistiche del pokemon
	 */
	private Statistiche statistiche;
	/**
	 * i punti esperienza attuali del pokemon
	 */
	private int expAttuale;
	/**
	 * i punti esperienza necessari per il level up del pokemon
	 */
	private int expRichiesta;
	/**
	 * le mosse imparabili dal pokemon
	 */
	private List<Mossa> mosseImparabili;
	/**
	 * le mosse conosciute attualmente dal pokemon
	 */
	private MosseAttive mosseAttive;
	/**
	 * lo stadio evolutivo del pokemon
	 */
	private int tier;
	/**
	 * il nome dell'evoluzione del pokemon
	 */
	private String evolutionId;
	/**
	 * il livello a cui si evolve il pokemon
	 */
	private int evolutionLv;

	/**
	 * Costruttore principale, crea i pokemon tramite gli attributi specificati nel fil json,
	 * il resto degli attributi viene aggiunto dal metodo inizializza()
	 *
	 * @param nome
	 * @param livello
	 * @param tipo
	 * @param tipoSecondario
	 * @param tier lo stato evolutivo del pokemon, può essere da 1 a 3
	 * @param evolutionId il nome dell'evoluzione del pokemon
	 * @param evolutionLv il livello in cui il pokemon evolve
	 */
	public Pokemon(String nome, int livello, Tipo tipo, Tipo tipoSecondario, int tier, String evolutionId, int evolutionLv) {
		this.nome = nome;
		this.livello = livello;
		this.tipo = tipo;
		this.tipoSecondario = tipoSecondario;
		this.tier = tier;
		this.evolutionId = evolutionId;
		this.evolutionLv = evolutionLv;
	}

	/**
	 * Costruttore secondario, prende un oggetto pokemon e
	 * crea una deep copy.
	 * Chiama anche i costruttori per la deep copy delle statistiche
	 * e delle mosse attive.
	 * @param base
	 */
	public Pokemon(Pokemon base) {
		this.nome = base.nome;
		this.livello = base.livello;
		this.tipo = base.tipo;
		this.tipoSecondario = base.tipoSecondario;
		this.tier = base.tier;
		this.evolutionId = base.evolutionId;
		this.evolutionLv = base.evolutionLv;

		// Copiare oggetti annidati
		this.statistiche = new Statistiche(base.getStatistiche());
		this.mosseAttive = new MosseAttive(base.getMosseAttive());
		this.mosseImparabili = new ArrayList<>(base.getMosseImparabili()); // Copia lista
		this.expAttuale = base.expAttuale;
		this.expRichiesta = base.expRichiesta;
	}

	/**
	 * inizializza gli attributi del pokemon
	 * che non vengono specificati dal json
	 */
	public void inizializza() {
		this.expAttuale = 0;
        this.expRichiesta = 15 + (this.livello * 2);
        inizializzaStatistiche();
        inizializzaMosse();
	}

	/**
	 * inizializza l'attributo Statistiche dei pokemon,
	 * viene chiamato dal metodo inizializza().
	 * Utilizza il parser per il json delle statistiche.
	 * Aggiunge alle statistiche il modificatore del livello
	 * tramite il metodo aggiorna()
	 */
	public void inizializzaStatistiche() {
	    StatisticheParser statisticheParser = StatisticheParser.getInstance("src/risorse/statistiche.json");
	       if (statisticheParser != null) {
	          this.statistiche = statisticheParser.getStatistiche(this.nome);
	        }
	       this.statistiche.aggiorna(this.livello);
	    }

	/**
	 * Inizializza le mosse attive del pokemon
	 * utilizzando il parser per il json delle
	 * mosse imparabili da un pokemon
	 */
	public void inizializzaMosse() {
		this.mosseAttive = new MosseAttive();
		this.mosseImparabili = MosseImparabiliParser.parseMosseImparabili(this.nome, "src/risorse/mosseImparabili.json");
		for (int i = 0; i < (this.livello / 2) + 1 && i < mosseImparabili.size(); i++) {
			this.mosseAttive.addMossa(mosseImparabili.get(i));
		}
	}

	/**
	 * Classe wrapper per poter ritornare due
	 * valori boolean, hasEvolved dice se il pokemon
	 * si è evoluto, noReplace dice se non bisogna rimpiazzare
	 * una mossa attiva del pokemon
	 */
	public class LevelUpResult {
		public final boolean hasEvolved;
		public final boolean noReplace;

		public LevelUpResult(boolean hasEvolved, boolean noReplace) {
			this.hasEvolved = hasEvolved;
			this.noReplace = noReplace;
		}
	}

	/**
	 * Metodo per la gestione dei level up di un pokemon,
	 * setta i valori di hasEvolved e noReplace e li ritorna
	 * in un oggetto wrapper al metodo ottieniExp
	 * @return LevelUpResult, oggetto wrapper con i due valori booleani
	 */
	public LevelUpResult levelUp() {
		boolean hasEvolved = false;
		boolean noReplace = true;
		this.livello++;
		this.setExpAttuale(0);
		this.setExpRichiesta(15 + (this.livello * 2));
		if (this.livello % 2 == 0 && (this.livello / 2 + 1) < this.mosseImparabili.size()) {
			noReplace = this.mosseAttive.addMossa(this.mosseImparabili.get(this.livello/2));
		};
		
		if (this.livello == this.getEvolutionLv()) {
			hasEvolved = true;
			this.evolve();
		}
		this.statistiche.aggiorna(1);
		return new LevelUpResult(hasEvolved, noReplace);
		
	}

	/**
	 * Metodo per la gestione delle evoluzioni dei
	 * pokemon, il pokemon che si è evoluto prende gli
	 * attriubuti della sua evoluzione
	 */
	public void evolve() {
		String filepath = "src/risorse/pokemon.json";
		List<Pokemon> listaPokemon = PokemonParser.parsePokemon(filepath);
		for (Pokemon p : listaPokemon) {
			if (p.getNome().equals(this.getEvolutionId())) {
				this.setNome(p.getNome());
				this.setTipo(p.getTipo());
				this.setTipoSecondario(p.getTipoSecondario());
				this.setTier(p.getTier());
				this.setEvolutionId(p.getEvolutionId());
				this.setEvolutionLv(p.getEvolutionLv());
				break;
			} 
		}
	}

	/**
	 * Metodo per la gestione del sistema di ottenimento
	 * esperienza dei pokemon, ritorna il wrapper con i booleani
	 * settati dal metodo levelUp()
	 * @param expOttenuta int rappresentante i punti exp
	 * @return oggetto wrapper con i due booleani hasEvolved e noReplace
	 */
	public LevelUpResult ottieniExp(int expOttenuta) {
		LevelUpResult result = new LevelUpResult(false, true);
		int newExp = this.expAttuale + expOttenuta;
		if (newExp >= this.expRichiesta) {
			int expResidua = newExp - expRichiesta;
			result = this.levelUp();
			this.ottieniExp(expResidua);
			return result;
		} else {
			this.setExpAttuale(newExp);
		}
		
		return result;
	}

	/**
	 * Restituisce il nome del pokemon
	 * @return la stringa del nome del pokemon
	 */
	public String getNome() {
		return this.nome;
	}

	/**
	 * restituisce il livello del pokemon
	 * @return il livello del pokemon
	 */
	public int getLivello() {
		return this.livello;
	}

	/**
	 * ritorna il tipo del pokemon
	 * @return il tipo (enum)
	 */
	public Tipo getTipo() {
		return this.tipo;
	}

	/**
	 * ritorna il tipo secondario del pokemon
	 * @return il tipo secondario (enum)
	 */
	public Tipo getTipoSecondario() {
		return this.tipoSecondario;
	}

	/**
	 * restituisce le statistiche del pokemon
	 * @return le statistiche (oggetto di classe Statistiche)
	 */
	public Statistiche getStatistiche() {
		return this.statistiche;
	}

	/**
	 * ritorna la quantità attuale dei punti esperienza di un pokemon
	 * @return i punti exp attuali
	 */
	public int getExpAttuale() {
		return this.expAttuale;
	}

	/**
	 * ritorna i punti esperienza necessari ad aumentare di livello
	 * @return i punti esperienza necessari ad aumentare di livello
	 */
	public int getExpRichiesta() {
		return this.expRichiesta;
	}

	/**
	 * ritorna le mosse attive del pokemon
	 * @return le mosse attive (oggetto di classe MosseAttive) del pokemon
	 */
	public MosseAttive getMosseAttive() {
		return this.mosseAttive;
	}

	/**
	 * ritorna la lista di mosse imparabili da un pokemon
	 * @return la lista di mosse imparabili
	 */
	public List<Mossa> getMosseImparabili() {
		return this.mosseImparabili;
	}

	/**
	 * ritorna il tier del pokemon
	 * @return il tier del pokemon
	 */
	public int getTier() {
		return this.tier;
	}

	/**
	 * ritorna il nome dell'evoluzione di un pokemon
	 * @return il nome dell'evoluzione
	 */
	public String getEvolutionId() {
		return this.evolutionId;
	}

	/**
	 * ritorna il livello di evoluzione di un pokemon
	 * @return il livello di evoluzione
	 */
	public int getEvolutionLv() {
		return this.evolutionLv;
	}

	/**
	 * detta il nome di un pokemon
	 * @param nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * setta il livello di un pokemon
	 * @param livello
	 */
	public void setLivello(int livello) {
		this.livello = livello;
	}

	/**
	 * setta il tipo di un pokemn
	 * @param tipo
	 */
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	/**
	 * setta il tipo secondario di un pokemon
	 * @param tipoSecondario
	 */
	public void setTipoSecondario(Tipo tipoSecondario) {
		this.tipoSecondario = tipoSecondario;
	}

	/**
	 * setta le statistiche di un pokemon
	 * @param statistiche
	 */
	public void setStatistiche(Statistiche statistiche) {
		this.statistiche = statistiche;
	}

	/**
	 * setta l'esperienza attuale del pokemon
	 * @param expAttuale
	 */
	public void setExpAttuale(int expAttuale) {
		this.expAttuale = expAttuale;
	}

	/**
	 * setta l'exp richiesta per aumentare di livello del pokemon
	 * @param expRichiesta
	 */
	public void setExpRichiesta(int expRichiesta) {
		this.expRichiesta = expRichiesta;
	}

	/**
	 * setta le mosse attive di un pokemon
	 * @param mosseAttive
	 */
	public void setMosseAttive(MosseAttive mosseAttive) {
		this.mosseAttive = mosseAttive;
	}

	/**
	 * setta le mosse imparabili di un pokemon
	 * @param mosseImparabili
	 */
	public void setMosseImparabili(List<Mossa> mosseImparabili) {
		this.mosseImparabili = mosseImparabili;
	}

	/**
	 * setta il tier di un pokemon
	 * @param tier
	 */
	public void setTier(int tier) {
		this.tier = tier;
	}

	/**
	 * setta l'ID dell'evoluzione di un pokemon
	 * @param evolutionId
	 */
	public void setEvolutionId(String evolutionId) {
		this.evolutionId = evolutionId;
	}

	/**
	 * setta il livello di evoluzione di un pokemon
	 * @param evolutionLv
	 */
	public void setEvolutionLv(int evolutionLv) {
		this.evolutionLv = evolutionLv;
	}

}
