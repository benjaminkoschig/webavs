package ch.globaz.al.business.models.tarif;

/**
 * class mod�le de la cat�gorie tarif
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
     * Cat�gorie de tarif
     * 
     * @see ch.globaz.al.business.constantes.ALCSTarif#GROUP_CATEGORIE
     */
    private String categorieTarif = null;

    /**
     * Retourne le code syst�me de la cat�gorie de tarif
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