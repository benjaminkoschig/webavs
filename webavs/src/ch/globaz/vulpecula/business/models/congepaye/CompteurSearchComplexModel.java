package ch.globaz.vulpecula.business.models.congepaye;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @since WebBMS 0.01.04
 */
public class CompteurSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = 8963054682124373028L;

    private String forId;
    private String forAnnee;
    private String forIdPosteTravail;
    private String forIdConvention;
    private String forAnneeDebut;
    private String forAnneeFin;
    private String montantRestantNotZero;

    public String getForAnneeDebut() {
        return forAnneeDebut;
    }

    public void setForAnneeDebut(String forAnneeDebut) {
        this.forAnneeDebut = forAnneeDebut;
    }

    public String getForAnneeFin() {
        return forAnneeFin;
    }

    public void setForAnneeFin(String forAnneeFin) {
        this.forAnneeFin = forAnneeFin;
    }

    @Override
    public Class<CompteurComplexModel> whichModelClass() {
        return CompteurComplexModel.class;
    }

    /**
     * @return the forId
     */
    public String getForId() {
        return forId;
    }

    /**
     * @param forId the forId to set
     */
    public void setForId(String forId) {
        this.forId = forId;
    }

    /**
     * @return the forAnnee
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * @return the forIdPosteTravail
     */
    public String getForIdPosteTravail() {
        return forIdPosteTravail;
    }

    /**
     * @param forAnnee the forAnnee to set
     */
    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    /**
     * @param forIdPosteTravail the forIdPosteTravail to set
     */
    public void setForIdPosteTravail(String forIdPosteTravail) {
        this.forIdPosteTravail = forIdPosteTravail;
    }

    public String getForIdConvention() {
        return forIdConvention;
    }

    public void setForIdConvention(String forIdConvention) {
        this.forIdConvention = forIdConvention;
    }

    public String getMontantRestantNotZero() {
        return montantRestantNotZero;
    }

    public void setMontantRestantNotZero() {
        montantRestantNotZero = "0";
    }
}
