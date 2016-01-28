package ch.globaz.al.business.models.tarif;

/**
 * classe représentant les prestations des tarifs
 * 
 * @author PTA
 * 
 */
public class PrestationTarifModel extends PrestationTarifBaseModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Indication de capacité d'éxercer
     */
    private Boolean capableExercer = null;
    /**
     * Catégorie de résident
     * 
     * @see ch.globaz.al.business.constantes.ALCSTarif#GROUP_RESIDENT
     */
    private String categorieResident = null;
    /**
     * Date de début de validité du tarif
     */
    private String debutValidite = null;
    /**
     * Date de fin de validité du tarif
     */
    private String finValidite = null;
    /**
     * Mois de séparation pour déterminer l'échéance d'un droit
     */
    private String moisSeparation = null;
    /**
     * Type de prestation
     * 
     * @see ch.globaz.al.business.constantes.ALCSDroit#GROUP_TYPE
     */
    private String typePrestation = null;

    /**
     * @return the capableExercer
     */
    public Boolean getCapableExercer() {
        return capableExercer;
    }

    /**
     * @return the categorieResident
     */
    public String getCategorieResident() {
        return categorieResident;
    }

    /**
     * @return the debutValidite
     */
    public String getDebutValidite() {
        return debutValidite;
    }

    /**
     * @return the finValidite
     */
    public String getFinValidite() {
        return finValidite;
    }

    /**
     * @return the moisSeparation
     */
    public String getMoisSeparation() {
        return moisSeparation;
    }

    /**
     * @return the typePrestation
     */
    public String getTypePrestation() {
        return typePrestation;
    }

    /**
     * @param capableExercer
     *            the capableExercer to set
     */
    public void setCapableExercer(Boolean capableExercer) {
        this.capableExercer = capableExercer;
    }

    /**
     * @param categorieResident
     *            the categorieResident to set
     */
    public void setCategorieResident(String categorieResident) {
        this.categorieResident = categorieResident;
    }

    /**
     * @param debutValidite
     *            the debutValidite to set
     */
    public void setDebutValidite(String debutValidite) {
        this.debutValidite = debutValidite;
    }

    /**
     * @param finValidite
     *            the finValidite to set
     */
    public void setFinValidite(String finValidite) {
        this.finValidite = finValidite;
    }

    /**
     * @param moisSeparation
     *            the moisSeparation to set
     */
    public void setMoisSeparation(String moisSeparation) {
        this.moisSeparation = moisSeparation;
    }

    /**
     * @param typePrestation
     *            the typePrestation to set
     */
    public void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }
}