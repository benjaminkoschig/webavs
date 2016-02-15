package ch.globaz.prestation.domaine.constantes;

/**
 * Le type de rente au sens large : API, AI, Vieillesse ou Survivant
 */
public enum DomaineCodePrestation {
    /** Rente AI */
    AI(52207001),
    /** Rente API */
    API(52207002),
    /** Non valeur, utilisé lorsque le code prestation n'est pas défini */
    NON_DEFINI(52207003),
    /** Rente Survivant */
    SURVIVANT(52207004),
    /** Rente Vieillesse */
    VIEILLESSE(52207005),
    /** venant du module PC */
    PRESTATION_COMPLEMENTAIRE(52207006),
    /** venant du module RFM */
    REMBOURSEMENT_FRAIS_MEDICAUX(52207007);

    int codeSystem;

    DomaineCodePrestation(int codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * Retourne le code système correspondant au domaine code prestation
     * 
     * @return
     */
    public int getCodeSystem() {
        return codeSystem;
    }
}
