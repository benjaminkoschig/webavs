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
public interface IAPBouclementAlfa {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    String TYPE_AMAT = "AMAT";
    String TYPE_APG = "APG";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * retourne l'id de l'affilie.
     * 
     * @return TYPE_APG, TYPE_AMAT ou null si une erreur est survenue
     */
    String getIdAffilie();

    /**
     * retourne l'id du droit.
     * 
     * @return idDroit
     * 
     */
    String getIdDroit();

    /**
     * retourne l'id du droit.
     * 
     * @return idDroit
     * 
     */
    String getIdPrestation();

    /**
     * Retourne la somme des montants bruts de toutes les prestations ACM qui ont ete versees durant le mois et l'annee
     * concernee.
     * 
     * @return la valeur courante de l'attribut AMATMontant brut ACM
     */
    String getMontantBrutACM();

    /**
     * Retourne la somme des montants des cotisations d'assurances de toutes les prestations ACM qui ont ete versees
     * durant le mois et l'annee concernee.
     * 
     * @return la valeur courante de l'attribut AMATMontant cotisations ACM
     */
    String getMontantCotisationsACM();

    /**
     * Retourne la somme des montants des impots a la source de toutes les prestations ACM qui ont ete versees durant le
     * mois et l'annee concernee.
     * 
     * @return la valeur courante de l'attribut AMATMontant cotisations ACM
     */
    String getMontantImpotsACM();

    /**
     * Retourne le nombre de jours couverts par toutes les prestations ACM qui ont ete versees durant le mois et l'annee
     * concernee.
     * 
     * @return la valeur courante de l'attribut AMATNombre jours ACM
     */
    String getNombreJoursCouvertsACM();

    /**
     * retourne soit la valeur de la constante TYPE_APG, soit la valeur de la constante TYPE_AMAT toutes deux d�finies
     * dans cette interface.
     * 
     * @return TYPE_APG, TYPE_AMAT ou null si une erreur est survenue
     */
    String getType();

}
