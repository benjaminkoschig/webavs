package ch.globaz.pegasus.business.models.lot;

import globaz.jade.persistence.model.JadeComplexModel;

public class OrdreVersementForList extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String designationRequerant1 = null;
    private String designationRequerant2 = null;
    private String idCompteAnnexeConjoint = null;
    private String idCompteAnnexeRequerant = null;
    private String idTiersRequerant = null;
    private String montantPresation = null;
    private String numAvs = null;
    private SimpleOrdreVersement simpleOrdreVersement = null;
    private String dateDecision;
    private String dateDebut;
    private String dateFin;
    private String refPaiement;

    public String getRefPaiement() {
        return refPaiement;
    }

    public void setRefPaiement(String refPaiement) {
        this.refPaiement = refPaiement;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public OrdreVersementForList() {
        super();
        simpleOrdreVersement = new SimpleOrdreVersement();
    }

    public String getDesignationRequerant1() {
        return designationRequerant1;
    }

    public String getDesignationRequerant2() {
        return designationRequerant2;
    }

    @Override
    public String getId() {
        return null;
    }

    public String getIdCompteAnnexeConjoint() {
        return idCompteAnnexeConjoint;
    }

    public String getIdCompteAnnexeRequerant() {
        return idCompteAnnexeRequerant;
    }

    public String getIdTiersRequerant() {
        return idTiersRequerant;
    }

    public String getMontantPresation() {
        return montantPresation;
    }

    public String getNumAvs() {
        return numAvs;
    }

    public SimpleOrdreVersement getSimpleOrdreVersement() {
        return simpleOrdreVersement;
    }

    @Override
    public String getSpy() {
        return null;
    }

    public void setDesignationRequerant1(String designationRequerant1) {
        this.designationRequerant1 = designationRequerant1;
    }

    public void setDesignationRequerant2(String designationRequerant2) {
        this.designationRequerant2 = designationRequerant2;
    }

    @Override
    public void setId(String id) {
    }

    public void setIdCompteAnnexeConjoint(String idCompteAnnexeConjoint) {
        this.idCompteAnnexeConjoint = idCompteAnnexeConjoint;
    }

    public void setIdCompteAnnexeRequerant(String idCompteAnnexeRequerant) {
        this.idCompteAnnexeRequerant = idCompteAnnexeRequerant;
    }

    public void setIdTiersRequerant(String idTiersRequerant) {
        this.idTiersRequerant = idTiersRequerant;
    }

    public void setMontantPresation(String montantPresation) {
        this.montantPresation = montantPresation;
    }

    public void setNumAvs(String numAvs) {
        this.numAvs = numAvs;
    }

    public void setSimpleOrdreVersement(SimpleOrdreVersement simpleOrdreVersement) {
        this.simpleOrdreVersement = simpleOrdreVersement;
    }

    @Override
    public void setSpy(String spy) {

    }

    @Override
    public String toString() {
        return "OrdreVersementForList [designationRequerant1=" + designationRequerant1 + ", designationRequerant2="
                + designationRequerant2 + ", idCompteAnnexeConjoint=" + idCompteAnnexeConjoint
                + ", idCompteAnnexeRequerant=" + idCompteAnnexeRequerant + ", idTiersRequerant=" + idTiersRequerant
                + ", montantPresation=" + montantPresation + ", numAvs=" + numAvs + ", simpleOrdreVersement="
                + simpleOrdreVersement + "]";
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

}
