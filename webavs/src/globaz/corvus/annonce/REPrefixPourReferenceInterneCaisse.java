package globaz.corvus.annonce;

/**
 * Définit le genre d'annonces existentes
 * 
 * @author lga
 * 
 */
public enum REPrefixPourReferenceInterneCaisse implements REPrefixPourReferenceInterneCaisseProvider {

    ANNONCE_AUGMENTATION("AUG"),
    ANNONCE_DIMINUTION("DIM"),
    ANNONCE_PONCTUELLE("PONC");

    private String prefixPourReferenceInterneCaisse;

    private REPrefixPourReferenceInterneCaisse(String prefixPourReferenceInterneCaisse) {
        this.prefixPourReferenceInterneCaisse = prefixPourReferenceInterneCaisse;
    }

    /**
     * Retourne le prefix à utiliser pour la référence interne de la caisse
     * Ce prefix est concaténé au nom de l'utilisateur dans le champs REAAL1A.YXLREI
     * 
     * @return le prefix à utiliser pour la référence interne de la caisse
     */
    @Override
    public String getPrefixPourReferenceInterneCaisse() {
        return prefixPourReferenceInterneCaisse;
    }

}