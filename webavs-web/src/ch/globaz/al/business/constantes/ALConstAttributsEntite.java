package ch.globaz.al.business.constantes;

/**
 * 
 * Constantes li�es aux attributs entit� de l'application (table ALATTRIB). La valeur de chaque constante correspond au
 * nom de l'attribut dans la table et non � sa valeur r�elle
 * 
 * @author GMO
 * 
 */
public interface ALConstAttributsEntite {

    /**
     * Affichage du n� salarie externe ou non
     */
    public static final String AFFICHAGE_NUMERO_SALARIE = "DISPNUMSAL";
    /**
     * Nom de l'attribut utilis� pour forcer un mode de calcul intercanto
     * 
     * @see ch.globaz.al.businessimpl.calcul.modes.CalculModeFactory
     */
    public static final String ATTRIBUT_MODE_CALC_CS = "MODECALCCS";
    /**
     * Nom de l'attribut utilis� pour forcer un mode de calcul
     * 
     * @see ch.globaz.al.businessimpl.calcul.modes.CalculModeFactory
     */
    public static final String ATTRIBUT_MODE_CALC_FORCE = "MODECALCF";

    public static final String ATTRIBUT_TARIF_CALC_FORCE = "TARIFCALC";

    /**
     * Le destinataire de l'avis d'�ch�ance
     */
    public static final String AVIS_ECHEANCE_DESTINATAIRE = "DESTAVISECH";
    /**
     * Le format de la r�cap auquel elle doit �tre sortie
     */
    public static final String FORMAT_RECAP = "RECAPFORMAT";
    /**
     * Le mode d'envoi de r�cap (courier,ebusiness)
     */
    public static final String MODE_ENVOI_RECAP = "RECAPENVOI";
    /**
     * Versement des prestations en paiement direct (au niveau affili� => pour tout l'affili�)
     */
    public static final String PAIEMENT_DIRECT = "PMTDIRECT";

}
