package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeSimpleModel;
import ch.globaz.pegasus.business.domaine.droit.EtatDroit;

public class SimpleVersionDroit extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csEtatDroit = null;
    private String csMotif = null;
    private String dateAnnonce = null;
    private String idDecision = null;
    private String idDroit = null;
    private String idVersionDroit = null;
    private String noVersion = null;
    private String remarqueDecompte = null;

    /**
     * @return the csEtatDroit
     */
    public String getCsEtatDroit() {
        return csEtatDroit;
    }

    public String getCsMotif() {
        return csMotif;
    }

    /**
     * @return the dateAnnonce
     */
    public String getDateAnnonce() {
        return dateAnnonce;
    }

    @Override
    public String getId() {
        return idVersionDroit;
    }

    /**
     * @return the idDecision
     */
    public String getIdDecision() {
        return idDecision;
    }

    /**
     * @return the idDroit
     */
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * @return the idVersionDroit
     */
    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    /**
     * @return the noVersion
     */
    public String getNoVersion() {
        return noVersion;
    }

    /**
     * @return the remarqueDecompte
     */
    public String getRemarqueDecompte() {
        return remarqueDecompte;
    }

    /**
     * @param csEtatDroit
     *            the csEtatDroit to set
     */
    public void setCsEtatDroit(String csEtatDroit) {
        this.csEtatDroit = csEtatDroit;
    }

    public void setCsMotif(String csMotif) {
        this.csMotif = csMotif;
    }

    /**
     * @param dateAnnonce
     *            the dateAnnonce to set
     */
    public void setDateAnnonce(String dateAnnonce) {
        this.dateAnnonce = dateAnnonce;
    }

    @Override
    public void setId(String id) {
        idVersionDroit = id;
    }

    /**
     * @param idDecision
     *            the idDecision to set
     */
    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    /**
     * @param idDroit
     *            the idDroit to set
     */
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    /**
     * @param idVersionDroit
     *            the idVersionDroit to set
     */
    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    /**
     * @param noVersion
     *            the noVersion to set
     */
    public void setNoVersion(String noVersion) {
        this.noVersion = noVersion;
    }

    /**
     * @param noVersion
     *            the noVersion to set
     */
    public void setRemarqueDecompte(String remarqueDecompte) {
        this.remarqueDecompte = remarqueDecompte;
    }

    public boolean isInitial() {
        return noVersion.equals("1");
    }

    public EtatDroit getEtat() {
        return EtatDroit.fromValue(csEtatDroit);
    }

}
