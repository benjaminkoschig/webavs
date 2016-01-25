package globaz.al.vb.dossier;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.al.business.models.dossier.DossierListComplexModel;

/**
 * ViewBean représentant une ligne des résultats de la recherche dossier (écran AL0002)
 * 
 * @author GMO
 * 
 */
public class ALDossierResultViewBean extends BJadePersistentObjectViewBean {

    private DossierListComplexModel dossierListComplexModel = null;

    /**
     * Constructeur du viewBean
     */
    public ALDossierResultViewBean() {
        super();
        dossierListComplexModel = new DossierListComplexModel();
    }

    /**
     * Constructeur du viewBean
     * 
     * @param _dossierListComplexModel
     *            Le modèle contenant de dossier complet
     */
    public ALDossierResultViewBean(DossierListComplexModel _dossierListComplexModel) {
        super();
        dossierListComplexModel = _dossierListComplexModel;
    }

    /**
     * Pas appelé actuellement selon l'architecture de l'application
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        throw new Exception(this.getClass() + " - Method called (add) not implemented (might be never called)");
    }

    /**
     * Pas appelé actuellement selon l'architecture de l'application
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        throw new Exception(this.getClass() + " - Method called (delete) not implemented (might be never called)");
    }

    /**
     * @return dossierListComplexModel Le modèle du dossier sur lequel est fait la recherche depuis l'écran AL0002
     */
    public DossierListComplexModel getDossierComplexModel() {
        return dossierListComplexModel;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return dossierListComplexModel.getId();
    }

    /**
     * @return session courante
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (dossierListComplexModel != null) && !dossierListComplexModel.isNew() ? new BSpy(
                dossierListComplexModel.getSpy()) : new BSpy(getSession());

    }

    /**
     * Pas appelé actuellement selon l'architecture de l'application
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        throw new Exception(this.getClass() + " - Method called (retrieve) not implemented (might be never called)");
    }

    /**
     * @param _dossierListComplexModel
     *            Le modèle du dossier sur lequel est faite la recherche
     */
    public void setDossierComplexModel(DossierListComplexModel _dossierListComplexModel) {
        dossierListComplexModel = _dossierListComplexModel;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        dossierListComplexModel.setId(newId);
    }

    /**
     * Pas appelé actuellement selon l'architecture de l'application
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        throw new Exception(this.getClass() + " - Method called (update) not implemented (might be never called)");
    }
}
