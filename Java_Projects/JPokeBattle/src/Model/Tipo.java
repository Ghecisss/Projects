package Model;
import java.util.EnumSet;
import java.util.Set;

/**
 * rappresenta i tipi possibili di un pokemon o una mossa
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public enum Tipo {
    /**
     * tipo fuoco
     */
    Fuoco,
    /**
     * tipo acqua
     */
    Acqua,
    /**
     * tipo erba
     */
    Erba,
    /**
     * tipo volante
     */
    Volante,
    /**
     * tipo elettro
     */
    Elettro,
    /**
     * tipo roccia
     */
    Roccia,
    /**
     * tipo terra
     */
    Terra,
    /**
     * tipo buio
     */
    Buio,
    /**
     * tipo veleno
     */
    Veleno,
    /**
     * tipo spettro
     */
    Spettro,
    /**
     * tipo psico
     */
    Psico,
    /**
     * tipo ghiaccio
     */
    Ghiaccio,
    /**
     * tipo drago
     */
    Drago,
    /**
     * tipo lotta
     */
    Lotta,
    /**
     * tipo normale
     */
    Normale,
    /**
     * tipo coleottero
     */
    Coleottero,
    /**
     * tipo acciaio
     */
    Acciaio;
    /**
     * l'insieme di tipi a cui un tipo è debole
     */
    private Set<Tipo> deboliA;
    /**
     * l'insieme di tipi a cui un tipo resiste
     */
    private Set<Tipo> resisteA;
    /**
     * l'insieme di tipi a cui un tipo è immune
     */
    private Set<Tipo> immuneA;

    /** Metodo statico per inizializzare le interazioni dopo la creazione dell'enum */
    static {
        Fuoco.setInterazioni(Set.of(Acqua, Roccia, Terra), Set.of(Erba, Ghiaccio, Coleottero, Acciaio, Fuoco), Set.of());
        Acqua.setInterazioni(Set.of(Erba, Elettro), Set.of(Fuoco, Ghiaccio, Acciaio, Acqua), Set.of());
        Erba.setInterazioni(Set.of(Fuoco, Ghiaccio, Volante, Veleno, Coleottero), Set.of(Acqua, Terra, Roccia, Erba), Set.of());
        Volante.setInterazioni(Set.of(Elettro, Ghiaccio, Roccia), Set.of(Erba, Lotta, Coleottero), Set.of(Terra));
        Elettro.setInterazioni(Set.of(Terra), Set.of(Acciaio, Volante, Elettro), Set.of());
        Roccia.setInterazioni(Set.of(Acqua, Erba, Lotta, Terra, Acciaio), Set.of(Normale, Fuoco, Volante, Veleno), Set.of());
        Terra.setInterazioni(Set.of(Acqua, Erba, Ghiaccio), Set.of(Veleno, Roccia), Set.of(Elettro));
        Buio.setInterazioni(Set.of(Lotta, Coleottero), Set.of(Spettro, Buio), Set.of(Psico));
        Veleno.setInterazioni(Set.of(Terra, Psico), Set.of(Erba, Lotta, Veleno, Coleottero), Set.of());
        Spettro.setInterazioni(Set.of(Spettro, Buio), Set.of(Veleno, Coleottero), Set.of(Normale, Lotta));
        Psico.setInterazioni(Set.of(Buio, Coleottero, Spettro), Set.of(Lotta, Psico), Set.of());
        Ghiaccio.setInterazioni(Set.of(Lotta, Acciaio, Fuoco, Roccia), Set.of(Ghiaccio), Set.of());
        Drago.setInterazioni(Set.of(Ghiaccio, Drago), Set.of(Acqua, Fuoco, Erba, Elettro), Set.of());
        Lotta.setInterazioni(Set.of(Volante, Psico), Set.of(Roccia, Coleottero, Buio), Set.of());
        Normale.setInterazioni(Set.of(Lotta), Set.of(), Set.of(Spettro));
        Coleottero.setInterazioni(Set.of(Fuoco, Volante, Roccia), Set.of(Erba, Lotta, Terra), Set.of());
        Acciaio.setInterazioni(Set.of(Fuoco, Lotta, Terra), Set.of(Normale, Erba, Ghiaccio, Volante, Roccia, Drago, Coleottero, Psico, Acciaio), Set.of(Veleno));
    }

    /**
     * setta le interazioni fra tipi, utilizzando dei set di tipi
     * @param deboli il set dei tipi a cui il pokemon è debole
     * @param resiste il set dei tipi a cui il pokemon resiste
     * @param immune il set dei tipi a cui il pokemon è immune
     */
    private void setInterazioni(Set<Tipo> deboli, Set<Tipo> resiste, Set<Tipo> immune) {
        this.deboliA = deboli;
        this.resisteA = resiste;
        this.immuneA = immune;
    }

    /**
     * ritorna true se il tipo è debole a un altro
     * @param altro
     * @return
     */
    public boolean deboleA(Tipo altro) {
        return deboliA.contains(altro);
    }

    /**
     * ritorna true se il tipo resiste a un altro
     * @param altro
     * @return
     */
    public boolean resisteA(Tipo altro) {
        return resisteA.contains(altro);
    }

    /**
     * ritorna true se il tipo è immune a un altro
     * @param altro
     * @return
     */
    public boolean immuneA(Tipo altro) {
        return immuneA.contains(altro);
    }
}
