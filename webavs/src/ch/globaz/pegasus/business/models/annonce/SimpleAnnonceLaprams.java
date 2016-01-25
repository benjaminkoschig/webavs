/**
 * 
 */
package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleAnnonceLaprams extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codeDestinationSortie = null;
    private String codeEnfant = null;
    private String csMotifSuppression = null;
    private String csTypeHome = null;
    private String dateEnvoi = null;
    private String dateRapport = null;
    private String idAnnonceLAPRAMS = null;
    private String idDecisionHeader = null;
    private String idDroitMbrFamConjoint = null;
    private String idDroitMbrFamRequerant = null;
    private String idPcAccordee = null;
    private String idTaxeJournaliereHome = null;

    public String getCodeDestinationSortie() {
        return codeDestinationSortie;
    }

    public String getCodeEnfant() {
        return codeEnfant;
    }

    public String getCsMotifSuppression() {
        return csMotifSuppression;
    }

    public String getCsTypeHome() {
        return csTypeHome;
    }

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public String getDateRapport() {
        return dateRapport;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idAnnonceLAPRAMS;
    }

    public String getIdAnnonceLAPRAMS() {
        return idAnnonceLAPRAMS;
    }

    public String getIdDecisionHeader() {
        return idDecisionHeader;
    }

    public String getIdDroitMbrFamConjoint() {
        return idDroitMbrFamConjoint;
    }

    public String getIdDroitMbrFamRequerant() {
        return idDroitMbrFamRequerant;
    }

    public String getIdPcAccordee() {
        return idPcAccordee;
    }

    public String getIdTaxeJournaliereHome() {
        return idTaxeJournaliereHome;
    }

    public void setCodeDestinationSortie(String codeDestinationSortie) {
        this.codeDestinationSortie = codeDestinationSortie;
    }

    public void setCodeEnfant(String codeEnfant) {
        this.codeEnfant = codeEnfant;
    }

    public void setCsMotifSuppression(String csMotifSuppression) {
        this.csMotifSuppression = csMotifSuppression;
    }

    public void setCsTypeHome(String csTypeHome) {
        this.csTypeHome = csTypeHome;
    }

    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public void setDateRapport(String dateRapport) {
        this.dateRapport = dateRapport;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idAnnonceLAPRAMS = id;

    }

    public void setIdAnnonceLAPRAMS(String idAnnonceLAPRAMS) {
        this.idAnnonceLAPRAMS = idAnnonceLAPRAMS;
    }

    public void setIdDecisionHeader(String idDecisionHeader) {
        this.idDecisionHeader = idDecisionHeader;
    }

    public void setIdDroitMbrFamConjoint(String idDroitMbrFamConjoint) {
        this.idDroitMbrFamConjoint = idDroitMbrFamConjoint;
    }

    public void setIdDroitMbrFamRequerant(String idDroitMbrFamRequerant) {
        this.idDroitMbrFamRequerant = idDroitMbrFamRequerant;
    }

    public void setIdPcAccordee(String idPcAccordee) {
        this.idPcAccordee = idPcAccordee;
    }

    public void setIdTaxeJournaliereHome(String idTaxeJournaliereHome) {
        this.idTaxeJournaliereHome = idTaxeJournaliereHome;
    }

}
