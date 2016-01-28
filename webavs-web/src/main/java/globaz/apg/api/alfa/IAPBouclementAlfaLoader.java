package globaz.apg.api.alfa;

/**
 * <h1>Description</h1>
 * 
 * <p>
 * Helper pour le chargement d'informations nécessaires au bouclement ALFA des caisses horlogères.
 * </p>
 * 
 * <p>
 * Retourne un résumé des informations sur les prestations annoncées (=versées) durant un mois et une année donnée.
 * </p>
 * 
 * @author vre
 */
public interface IAPBouclementAlfaLoader {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Charge toutes les informations nécessaires pour le mois et l'année concernés.
     * 
     * @param mois
     *            une valeur entière entre 1 et 12
     * @param annee
     *            une valeur entière, par exemple 2000
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    IAPBouclementAlfa[] load(String mois, String annee) throws Exception;

}
