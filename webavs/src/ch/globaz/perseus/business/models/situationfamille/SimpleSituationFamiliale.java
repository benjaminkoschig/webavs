/**
 * 
 */
package ch.globaz.perseus.business.models.situationfamille;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DDE
 * 
 */
public class SimpleSituationFamiliale extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csEtatCivilConjoint = null;
    private String csEtatCivilRequerant = null;
    private String csNiveauFormationConjoint = null;
    private String csNiveauFormationRequerant = null;
    private String csSituationActiviteRequerant = null;
    private String csTypeConjoint = null;
    private String idConjoint = null;
    private String idDemande = null;
    private String idRequerant = null;
    private String idSituationFamilliale = null;

    public String getCsEtatCivilConjoint() {
        return csEtatCivilConjoint;
    }

    public String getCsEtatCivilRequerant() {
        return csEtatCivilRequerant;
    }

    /**
     * @return the csNiveauFormationConjoint
     */
    public String getCsNiveauFormationConjoint() {
        return csNiveauFormationConjoint;
    }

    /**
     * @return the csNiveauFormationRequerant
     */
    public String getCsNiveauFormationRequerant() {
        return csNiveauFormationRequerant;
    }

    public String getCsSituationActiviteRequerant() {
        return csSituationActiviteRequerant;
    }

    /**
     * @return the csTypeConjoint
     */
    public String getCsTypeConjoint() {
        return csTypeConjoint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idSituationFamilliale;
    }

    /**
     * @return the idConjoint
     */
    public String getIdConjoint() {
        return idConjoint;
    }

    /**
     * @return the idDemande
     */
    public String getIdDemande() {
        return idDemande;
    }

    /**
     * @return the idRequerant
     */
    public String getIdRequerant() {
        return idRequerant;
    }

    /**
     * @return the idSituationFamilliale
     */
    public String getIdSituationFamilliale() {
        return idSituationFamilliale;
    }

    public void setCsEtatCivilConjoint(String csEtatCivilConjoint) {
        this.csEtatCivilConjoint = csEtatCivilConjoint;
    }

    public void setCsEtatCivilRequerant(String csEtatCivilRequerant) {
        this.csEtatCivilRequerant = csEtatCivilRequerant;
    }

    /**
     * @param csNiveauFormationConjoint
     *            the csNiveauFormationConjoint to set
     */
    public void setCsNiveauFormationConjoint(String csNiveauFormationConjoint) {
        this.csNiveauFormationConjoint = csNiveauFormationConjoint;
    }

    /**
     * @param csNiveauFormationRequerant
     *            the csNiveauFormationRequerant to set
     */
    public void setCsNiveauFormationRequerant(String csNiveauFormationRequerant) {
        this.csNiveauFormationRequerant = csNiveauFormationRequerant;
    }

    public void setCsSituationActiviteRequerant(String csSituationActiviteRequerant) {
        this.csSituationActiviteRequerant = csSituationActiviteRequerant;
    }

    /**
     * @param csTypeConjoint
     *            the csTypeConjoint to set
     */
    public void setCsTypeConjoint(String csTypeConjoint) {
        this.csTypeConjoint = csTypeConjoint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idSituationFamilliale = id;
    }

    /**
     * @param idConjoint
     *            the idConjoint to set
     */
    public void setIdConjoint(String idConjoint) {
        this.idConjoint = idConjoint;
    }

    /**
     * @param idDemande
     *            the idDemande to set
     */
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * @param idRequerant
     *            the idRequerant to set
     */
    public void setIdRequerant(String idRequerant) {
        this.idRequerant = idRequerant;
    }

    /**
     * @param idSituationFamilliale
     *            the idSituationFamilliale to set
     */
    public void setIdSituationFamilliale(String idSituationFamilliale) {
        this.idSituationFamilliale = idSituationFamilliale;
    }

}
