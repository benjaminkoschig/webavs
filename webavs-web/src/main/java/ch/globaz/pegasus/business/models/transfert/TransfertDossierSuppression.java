package ch.globaz.pegasus.business.models.transfert;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Mod�le complexe de demande de transfert de dossier d'une d�cision de suppression. <br>
 * <br>
 * Remarque: Cr�� initialement dans la vue de g�rer des copies de document, mais les copies sont g�r�s par le mod�le de
 * d�cision header.
 * 
 * @author eco
 * 
 */
public class TransfertDossierSuppression extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleTransfertDossierSuppression simpleTransfertDossierSuppression = null;

    public TransfertDossierSuppression() {
        super();
        simpleTransfertDossierSuppression = new SimpleTransfertDossierSuppression();
    }

    @Override
    public String getId() {
        return simpleTransfertDossierSuppression.getId();
    }

    public SimpleTransfertDossierSuppression getSimpleTransfertDossierSuppression() {
        return simpleTransfertDossierSuppression;
    }

    @Override
    public String getSpy() {
        return simpleTransfertDossierSuppression.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleTransfertDossierSuppression.setId(id);
    }

    public void setSimpleTransfertDossierSuppression(SimpleTransfertDossierSuppression simpleTransfertDossierSuppression) {
        this.simpleTransfertDossierSuppression = simpleTransfertDossierSuppression;
    }

    @Override
    public void setSpy(String spy) {
        simpleTransfertDossierSuppression.setSpy(spy);
    }

}
