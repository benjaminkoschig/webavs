package globaz.apg.api.alfa;

/**
 * <h1>Description</h1>
 * 
 * <p>
 * Helper pour le chargement d'informations n�cessaires au bouclement ALFA des caisses horlog�res.
 * </p>
 * 
 * <p>
 * Retourne un r�sum� des informations sur les prestations annonc�es (=vers�es) durant un mois et une ann�e donn�e.
 * </p>
 * 
 * @author vre
 */
public interface IAPBouclementAlfaLoader {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Charge toutes les informations n�cessaires pour le mois et l'ann�e concern�s.
     * 
     * @param mois
     *            une valeur enti�re entre 1 et 12
     * @param annee
     *            une valeur enti�re, par exemple 2000
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    IAPBouclementAlfa[] load(String mois, String annee) throws Exception;

}
