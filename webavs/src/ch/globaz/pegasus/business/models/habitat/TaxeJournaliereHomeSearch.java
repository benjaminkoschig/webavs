package ch.globaz.pegasus.business.models.habitat;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;

public class TaxeJournaliereHomeSearch extends AbstractDonneeFinanciereSearchModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdVersionDroit = null;
    private String forIdEntity = null;
    private String forIdTaxeJournaliereHome = null;
    private String forNumeroVersion = null;
    private String idDroitMembreFamille = null;
    private String forDateDebut = null;
    private String forLessDateEntreeHome = null;

    /**
     * @return the forIdEntity
     */
    public String getForIdEntity() {
        return forIdEntity;
    }

    /**
     * @return the forIdTaxeJournaliereHome
     */
    public String getForIdTaxeJournaliereHome() {
        return forIdTaxeJournaliereHome;
    }

    /**
     * @return the forNumeroVersion
     */
    public String getForNumeroVersion() {
        return forNumeroVersion;
    }

    /**
     * @return the idDroitMembreFamille
     */
    public String getIdDroitMembreFamille() {
        return idDroitMembreFamille;
    }

    /**
     * @param forIdEntity
     *            the forIdEntity to set
     */
    public void setForIdEntity(String forIdEntity) {
        this.forIdEntity = forIdEntity;
    }

    /**
     * @param forIdTaxeJournaliereHome
     *            the forIdTaxeJournaliereHome to set
     */
    public void setForIdTaxeJournaliereHome(String forIdTaxeJouranliereHome) {
        forIdTaxeJournaliereHome = forIdTaxeJouranliereHome;
    }

    /**
     * @param forNumeroVersion
     *            the forNumeroVersion to set
     */
    public void setForNumeroVersion(String forNumeroVersion) {
        this.forNumeroVersion = forNumeroVersion;
    }

    /**
     * @param idDroitMembreFamille
     *            the idDroitMembreFamille to set
     */
    public void setIdDroitMembreFamille(String idDroitMembreFamille) {
        this.idDroitMembreFamille = idDroitMembreFamille;
    }

    @Override
    public Class<TaxeJournaliereHome> whichModelClass() {
        return TaxeJournaliereHome.class;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    public String getForLessDateEntreeHome() {
        return forLessDateEntreeHome;
    }

    public void setForLessDateEntreeHome(String forLessDateEntreeHome) {
        this.forLessDateEntreeHome = forLessDateEntreeHome;
    }

}
