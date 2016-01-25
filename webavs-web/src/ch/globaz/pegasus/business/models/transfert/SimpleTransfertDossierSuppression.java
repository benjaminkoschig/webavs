package ch.globaz.pegasus.business.models.transfert;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleTransfertDossierSuppression extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDecisionHeader = null;
    private String idNouveauDomicile = null; // TODO à enlever car obsolète par rapport à l'adresse de courrier du
    // decision header
    private String idNouvelleCaisse = null;
    private String idTransfertDossierSuppression = null;
    private String textMotifContact = null;
    private String textMotifTransfert = null;

    @Override
    public String getId() {
        return getIdTransfertDossierSuppression();
    }

    public String getIdDecisionHeader() {
        return idDecisionHeader;
    }

    public String getIdNouveauDomicile() {
        return idNouveauDomicile;
    }

    public String getIdNouvelleCaisse() {
        return idNouvelleCaisse;
    }

    public String getIdTransfertDossierSuppression() {
        return idTransfertDossierSuppression;
    }

    public String getTextMotifContact() {
        return textMotifContact;
    }

    public String getTextMotifTransfert() {
        return textMotifTransfert;
    }

    @Override
    public void setId(String id) {
        setIdTransfertDossierSuppression(id);
    }

    public void setIdDecisionHeader(String idDecisionHeader) {
        this.idDecisionHeader = idDecisionHeader;
    }

    public void setIdNouveauDomicile(String idNouveauDomicile) {
        this.idNouveauDomicile = idNouveauDomicile;
    }

    public void setIdNouvelleCaisse(String idNouvelleCaisse) {
        this.idNouvelleCaisse = idNouvelleCaisse;
    }

    public void setIdTransfertDossierSuppression(String idTransfertDossierSuppression) {
        this.idTransfertDossierSuppression = idTransfertDossierSuppression;
    }

    public void setTextMotifContact(String textMotifContact) {
        this.textMotifContact = textMotifContact;
    }

    public void setTextMotifTransfert(String textMotifTransfert) {
        this.textMotifTransfert = textMotifTransfert;
    }

}
