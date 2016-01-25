package globaz.corvus.annonce;

/**
 * Interface définissant une classe capable de le préfixe à utiliser pour la référence interne de la caisse.
 * REAAL1A.YXLREI
 * Cette interface est lié aux annonces de rentes 9ème et 10ème révision
 * 
 * @author lga
 * 
 */
public interface REPrefixPourReferenceInterneCaisseProvider {

    /**
     * Retourne le prefix à utiliser pour la référence interne de la caisse
     * Ce prefix est concaténé au nom de l'utilisateur dans le champs REAAL1A.YXLREI
     * 
     * @return le prefix à utiliser pour la référence interne de la caisse
     */
    public String getPrefixPourReferenceInterneCaisse();
}
