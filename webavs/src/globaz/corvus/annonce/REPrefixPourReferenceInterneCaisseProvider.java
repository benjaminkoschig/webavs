package globaz.corvus.annonce;

/**
 * Interface d�finissant une classe capable de le pr�fixe � utiliser pour la r�f�rence interne de la caisse.
 * REAAL1A.YXLREI
 * Cette interface est li� aux annonces de rentes 9�me et 10�me r�vision
 * 
 * @author lga
 * 
 */
public interface REPrefixPourReferenceInterneCaisseProvider {

    /**
     * Retourne le prefix � utiliser pour la r�f�rence interne de la caisse
     * Ce prefix est concat�n� au nom de l'utilisateur dans le champs REAAL1A.YXLREI
     * 
     * @return le prefix � utiliser pour la r�f�rence interne de la caisse
     */
    public String getPrefixPourReferenceInterneCaisse();
}
