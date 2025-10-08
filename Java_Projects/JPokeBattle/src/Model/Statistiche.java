package Model;

/**
 * rappresenta le statistiche di un pokemon
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class Statistiche {
	/**
	 * gli hp massimi del pokemon
	 */
	private int maxHp;
	/**
	 * gli hp attuali del pokemon
	 */
	private int hp;
	/**
	 * la statistica di attacco del pokemon
	 */
	private int atk;
	/**
	 * la statistica di difesa del pokemon
	 */
	private int def;
	/**
	 * la statistica di attacco speciale del pokemon
	 */
	private int speAtk;
	/**
	 * la statistica di difesa speciale del pokemon
	 */
	private int speDef;
	/**
	 * la velocità del pokemon
	 */
	private int vel;

	/**
	 * costruttore principale, crea un oggetto Statistiche con i valori definiti
	 * nel json statistiche
	 * @param maxHp
	 * @param hp
	 * @param atk
	 * @param def
	 * @param speAtk
	 * @param speDef
	 * @param vel
	 */
		public Statistiche(int maxHp, int hp, int atk, int def, int speAtk, int speDef, int vel) {
			this.maxHp = maxHp;
			this.hp = hp;
			this.atk = atk;
			this.def = def;
			this.speAtk = speAtk;
			this.speDef = speDef;
			this.vel = vel;
		}

	/**
	 * costruttore di deep copy
	 * @param originale l'oggetto da copiare
	 */
		public Statistiche(Statistiche originale) {
			this.maxHp = originale.maxHp;
			this.hp = originale.hp;
			this.atk = originale.atk;
			this.def = originale.def;
			this.speAtk = originale.speAtk;
			this.speDef = originale.speDef;
			this.vel = originale.vel;
		}

	/**
	 * aggiunge il modificatore del livello alle statistiche base
	 * @param lv il livello di un pokemon
	 */
	public void aggiorna(int lv) {
				this.setMaxHp(this.getMaxHp() + lv);
				this.setHp(this.getHp() + lv);
				this.setAtk(this.getAtk() + lv);
				this.setDef(this.getDef() + lv);
				this.setSpeAtk(this.getSpeAtk() + lv);
				this.setSpeDef(this.getSpeDef() + lv);
				this.setVel(this.getVel() + lv);
			}

	/**
	 * ritorna il numero massimo di hp
	 * @return gli hp massimi
	 */
		public int getMaxHp() {
			return this.maxHp;
		}

	/**
	 * ritorna gli hp attuali
	 * @return gli hp attuali
	 */
		public int getHp() {
			return this.hp;
		}

	/**
	 * ritorna l'attacco
	 * @return l'attacco
	 */
		public int getAtk() {
			return this.atk;
		}

	/**
	 * ritorna la difesa
	 * @return la difesa
	 */
		public int getDef() {
			return this.def;
		}

	/**
	 * ritorna l'attacco speciale
	 * @return l'attacco speciale
	 */
		public int getSpeAtk() {
			return this.speAtk;
		}

	/**
	 * ritorna la difesa speciale
	 * @return la difesa speciale
	 */
		public int getSpeDef() {
			return this.speDef;
		}

	/**
	 * ritorna la velocità
	 * @return la velocità
	 */
		public int getVel() {
			return this.vel;
		}

	/**
	 * setta gli hp massimi
	 * @param maxHp gli hp massimi
	 */
		public void setMaxHp(int maxHp) {
			this.maxHp = maxHp;
		}

	/**
	 * setta gli hp attuali
	 * @param hp gli hp attuali
	 */
		public void setHp (int hp) {
			this.hp = hp;
		}

	/**
	 * setta l'attacco
	 * @param atk l'attacco
	 */
		public void setAtk (int atk) {
			this.atk = atk;
		}

	/**
	 * setta la difesa
	 * @param def la difesa
	 */
		public void setDef (int def) {
			this.def = def;
		}

	/**
	 * setta l'attacco speciale
	 * @param speAtk l'attacco speciale
	 */
		public void setSpeAtk (int speAtk) {
			this.speAtk = speAtk;
		}

	/**
	 * setta la difesa speciale
	 * @param speDef la difesa speciale
	 */
		public void setSpeDef (int speDef) {
			this.speDef = speDef;
		}

	/**
	 * setta la velocità
	 * @param vel la velocità
	 */
		public void setVel(int vel) {
			this.vel = vel;
		}
}
