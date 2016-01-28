package globaz.al.vb.dossier;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean gérant la radiation d'un dossier à une date donnée (AL0020)
 * 
 * @author PTA
 * 
 */
public class ALRadiationViewBean extends BJadePersistentObjectViewBean {
    /**
     * Modèle de base du dossier
     */
    private DossierModel dossierModel = null;
    /**
     * indique si il faut mettre à jour le nombre de jours de fin selon la date de radiation
     */
    private boolean updateNbJoursFin = false;

    /**
     * Constructeur de la classe
     */
    public ALRadiationViewBean() {
        super();
        dossierModel = new DossierModel();

    }

    @Override
    public void add() throws Exception {
        throw new Exception(this.getClass() + " - Method called (add) not implemented (might be never called)");

    }

    @Override
    public void delete() throws Exception {
        throw new Exception(this.getClass() + " - Method called (delete) not implemented (might be never called)");
    }

    /**
     * @return the dossierModel
     */
    public DossierModel getDossierModel() {
        return dossierModel;
    }

    @Override
    public String getId() {
        return dossierModel.getId();
    }

    /**
     * @return the session
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return (dossierModel != null) && !dossierModel.isNew() ? new BSpy(dossierModel.getSpy()) : new BSpy(
                getSession());
    }

    /**
     * @return valeur du critère statut NP
     */
    public Boolean isUpdateNbJoursFin() {
        return new Boolean(updateNbJoursFin);
    }

    @Override
    public void retrieve() throws Exception {

        // Lecture du modèle, appel du service read
        dossierModel = ALServiceLocator.getDossierModelService().read(dossierModel.getIdDossier());
    }

    /**
     * @param dossierModel
     *            the dossierModel to set
     */
    public void setDossierModel(DossierModel dossierModel) {
        this.dossierModel = dossierModel;
    }

    @Override
    public void setId(String newId) {
        dossierModel.setId(newId);

    }

    public void setUpdateNbJoursFin(Boolean isUpdateNbJours) {
        updateNbJoursFin = isUpdateNbJours.booleanValue();

    }

    @Override
    public void update() throws Exception {
        dossierModel = ALServiceLocator.getDossierBusinessService().radierDossier(dossierModel,
                dossierModel.getFinValidite(), updateNbJoursFin, "");
    }

}
