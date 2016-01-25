/**
 * 
 */
package ch.globaz.perseus.business.models.situationfamille;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DDE
 * 
 */
public class SimpleEnfantFamille extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csEtatCivil = null;
    private String csFormation = null;
    private String csGarde = null;
    private String csSource = null;
    private String idEnfant = null;
    private String idEnfantFamille = null;
    private String idSituationFamiliale = null;

    /**
     * @return the csFormation
     */
    public String getCsFormation() {
        return csFormation;
    }

    /**
     * @return the csGarde
     */
    public String getCsGarde() {
        return csGarde;
    }

    /**
     * @return the csSource
     */
    public String getCsSource() {
        return csSource;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idEnfantFamille;
    }

    /**
     * @return the idEnfant
     */
    public String getIdEnfant() {
        return idEnfant;
    }

    /**
     * @return the idEnfantFamille
     */
    public String getIdEnfantFamille() {
        return idEnfantFamille;
    }

    /**
     * @return the idSituationFamiliale
     */
    public String getIdSituationFamiliale() {
        return idSituationFamiliale;
    }

    /**
     * @param csFormation
     *            the csFormation to set
     */
    public void setCsFormation(String csFormation) {
        this.csFormation = csFormation;
    }

    /**
     * @param csGarde
     *            the csGarde to set
     */
    public void setCsGarde(String csGarde) {
        this.csGarde = csGarde;
    }

    /**
     * @param csSource
     *            the csSource to set
     */
    public void setCsSource(String csSource) {
        this.csSource = csSource;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idEnfantFamille = id;
    }

    /**
     * @param idEnfant
     *            the idEnfant to set
     */
    public void setIdEnfant(String idEnfant) {
        this.idEnfant = idEnfant;
    }

    /**
     * @param idEnfantFamille
     *            the idEnfantFamille to set
     */
    public void setIdEnfantFamille(String idEnfantFamille) {
        this.idEnfantFamille = idEnfantFamille;
    }

    /**
     * @param idSituationFamiliale
     *            the idSituationFamiliale to set
     */
    public void setIdSituationFamiliale(String idSituationFamiliale) {
        this.idSituationFamiliale = idSituationFamiliale;
    }

    public void setCsEtatCivil(String csEtatCivil) {
        this.csEtatCivil = csEtatCivil;
    }

    public String getCsEtatCivil() {
        return csEtatCivil;
    }

}
