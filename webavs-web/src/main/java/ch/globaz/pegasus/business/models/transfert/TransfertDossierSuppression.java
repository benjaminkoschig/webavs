package ch.globaz.pegasus.business.models.transfert;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Modèle complexe de demande de transfert de dossier d'une décision de suppression. <br>
 * <br>
 * Remarque: Créé initialement dans la vue de gérer des copies de document, mais les copies sont gérés par le modèle de
 * décision header.
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
