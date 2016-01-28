package globaz.al.vb.dossier;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.al.business.models.adi.DecompteAdiModel;
import ch.globaz.al.business.models.adi.DecompteAdiSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean gérant la liste des décomptes ADI pour un dossier pas de listViewBean, car on doit charger des données
 * dossiers et / décompte
 * 
 * @author GMO
 * 
 */
public class ALDossierAdiViewBean extends BJadePersistentObjectViewBean {

    /**
	 * 
	 */
    private DecompteAdiSearchModel decompteSearchModel = null;

    private DossierComplexModel dossierComplexModel = null;

    public ALDossierAdiViewBean() {
        super();
        dossierComplexModel = new DossierComplexModel();
        decompteSearchModel = new DecompteAdiSearchModel();
    }

    @Override
    public void add() throws Exception {
        throw new Exception(this.getClass() + " - Method called (add) not implemented (might be never called)");
    }

    @Override
    public void delete() throws Exception {
        throw new Exception(this.getClass() + " - Method called (delete) not implemented (might be never called)");
    }

    public DecompteAdiModel getDecompteAt(int idx) {
        return idx < decompteSearchModel.getSize() ? (DecompteAdiModel) decompteSearchModel.getSearchResults()[idx]
                : new DecompteAdiModel();

    }

    public DecompteAdiSearchModel getDecompteSearchModel() {
        return decompteSearchModel;
    }

    public DossierComplexModel getDossierComplexModel() {
        return dossierComplexModel;
    }

    @Override
    public String getId() {
        return dossierComplexModel.getId();
    }

    /**
     * @return session actuelle
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return (dossierComplexModel != null) && !dossierComplexModel.isNew() ? new BSpy(dossierComplexModel.getSpy())
                : new BSpy(getSession());
    }

    @Override
    public void retrieve() throws Exception {
        dossierComplexModel = ALServiceLocator.getDossierComplexModelService().read(getId());

        decompteSearchModel.setForIdDossier(getId());
        decompteSearchModel = ALServiceLocator.getDecompteAdiModelService().search(decompteSearchModel);

    }

    public void setDecompteSearchModel(DecompteAdiSearchModel decompteSearchModel) {
        this.decompteSearchModel = decompteSearchModel;
    }

    public void setDossierComplexModel(DossierComplexModel dossierComplexModel) {
        this.dossierComplexModel = dossierComplexModel;
    }

    @Override
    public void setId(String newId) {
        dossierComplexModel.setId(newId);

    }

    @Override
    public void update() throws Exception {
        throw new Exception(this.getClass() + " - Method called (update) not implemented (might be never called)");
    }

}
