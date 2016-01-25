package ch.globaz.al.business.models.tarif;

/**
 * class modèle de la catégorie tarif
 * 
 * @author jts
 * 
 */
public class CategorieTarifModel extends CategorieTarifFkModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Catégorie de tarif
     * 
     * @see ch.globaz.al.business.constantes.ALCSTarif#GROUP_CATEGORIE
     */
    private String categorieTarif = null;

    /**
     * Retourne le code système de la catégorie de tarif
     * 
     * @return the categorieTarif
     * @see ch.globaz.al.business.constantes.ALCSTarif#GROUP_CATEGORIE
     */
    public String getCategorieTarif() {
        return categorieTarif;
    }

    /**
     * @param categorieTarif
     *            the categorieTarif to set
     */
    public void setCategorieTarif(String categorieTarif) {
        this.categorieTarif = categorieTarif;
    }
}