package Controller;

import Model.*;
import Parser.*;
import java.util.List;

/**
 * calcolatore dei danni di una mossa da un pokemon
 * attaccante verso uno difensore
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class CalcolatoreDanni {
	/**
	 * calcola i danni di una mossa in base al pokemon che la utilizza
	 * e al pokemon che la subisce
	 * @param attaccante il pokemon attaccante
	 * @param difensore il pokemon che subisce
	 * @param mossa la mossa utilizzata
	 * @return il valore dei danni
	 */
	public int calcolaDanni(Pokemon attaccante, Pokemon difensore, Mossa mossa) {
		if (mossa.getCategoria() == Categoria.Fisica) {
			double danni = ((mossa.getPotenza() + attaccante.getStatistiche().getAtk() - difensore.getStatistiche().getDef()) / 3.0);
			danni = calcolaModificatore(attaccante, difensore, mossa) * danni;
			if (calcolaModificatore(attaccante, difensore, mossa) == 0) {
				return 0;
			} else {
			return Math.max(1, (int) danni);
			}
		}
		else if(mossa.getCategoria() == Categoria.Speciale) {
			double danni = ((mossa.getPotenza() + attaccante.getStatistiche().getSpeAtk() - difensore.getStatistiche().getSpeDef()) / 3.0);
			danni = calcolaModificatore(attaccante, difensore, mossa) * danni;
			if (calcolaModificatore(attaccante, difensore, mossa) == 0) {
				return 0;
			} else {
			return Math.max(1, (int) danni);
			}
		}
		else {
			int danni = 0;
			return danni;
		}
	}

	/**
	 * calcola il modificatore da applicare
	 * al valore dei danni in base alle interazioni
	 * di tipo
	 * @param attaccante il pokemon attacante
	 * @param difensore il pokemon che subisce
	 * @param mossa la mossa utilizzata
	 * @return
	 */
	public double calcolaModificatore(Pokemon attaccante, Pokemon difensore, Mossa mossa) {
	    double modificatore = 1;
	    Tipo tipoMossa = mossa.getTipo();

	    if (difensore.getTipo().immuneA(tipoMossa)) {
	    return 0;
	    }
	    if (difensore.getTipo().deboleA(tipoMossa)) { modificatore *= 2;
	    }
	    if (difensore.getTipo().resisteA(tipoMossa)) { modificatore *= 0.5;
	    
	    }
	    
	    if (difensore.getTipoSecondario() != null) {
	        if (difensore.getTipoSecondario().immuneA(tipoMossa)) { 
	        
	        return 0;
	        }
	        if (difensore.getTipoSecondario().deboleA(tipoMossa)) { modificatore *= 2;
		    
	        }
	        if (difensore.getTipoSecondario().resisteA(tipoMossa)) { modificatore *= 0.5;
	        
	        }
	    }
	    
	    if (attaccante.getTipo() == mossa.getTipo() || attaccante.getTipoSecondario() == mossa.getTipo()) {
	    	modificatore *= 1.5;
	    }

	    return modificatore;
	}
	
}
