package ch.globaz.prestation.domaine.constantes;

/**
 * Le type de rente au sens large : API, AI, Vieillesse ou Survivant
 */
public enum DomaineCodePrestation {
    /** Rente AI */
    AI,
    /** Rente API */
    API,
    /** Non valeur, utilis� lorsque le code prestation n'est pas d�fini */
    NON_DEFINI,
    /** Rente Survivant */
    SURVIVANT,
    /** Rente Vieillesse */
    VIEILLESSE,
    /** venant du module PC */
    PRESTATION_COMPLEMENTAIRE,
    /** venant du module RFM */
    REMBOURSEMENT_FRAIS_MEDICAUX
}
