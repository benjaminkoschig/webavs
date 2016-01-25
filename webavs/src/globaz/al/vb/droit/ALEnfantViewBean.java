package globaz.al.vb.droit;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.al.business.models.droit.EnfantComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean gérant le modèle représentant un enfant complet.
 * 
 * @author GMO
 * 
 */
public class ALEnfantViewBean extends BJadePersistentObjectViewBean {

    /**
     * Le modèle enfant
     */
    private EnfantComplexModel enfantComplexModel = null;
    /**
     * Le nombre de droits dans lesquels l'enfant est actif
     */
    private int nbDroitsActifs = 0;

    /**
     * Constructeur du viewBean
     */
    public ALEnfantViewBean() {
        super();
        enfantComplexModel = new EnfantComplexModel();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        throw new Exception(this.getClass() + " - Method called (add) not implemented (might be never called)");

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        throw new Exception(this.getClass() + " - Method called (add) not implemented (might be never called)");

    }

    /**
     * @return enfantComplexModel Le modèle de l'enfant utilisé
     */
    public EnfantComplexModel getEnfantComplexModel() {
        return enfantComplexModel;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return enfantComplexModel.getId();
    }

    /**
     * @return nbDroitsActifs Le nombre de droits pour lesquels l'enfant est actif
     */
    public int getNbDroitsActifs() {
        return nbDroitsActifs;
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
        return (enfantComplexModel != null) && !enfantComplexModel.isNew() ? new BSpy(enfantComplexModel.getSpy())
                : new BSpy(getSession());
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        enfantComplexModel = ALServiceLocator.getEnfantComplexModelService().read(getId());
        nbDroitsActifs = ALServiceLocator.getEnfantBusinessService().getNombreDroitsActifs(enfantComplexModel.getId());

    }

    /**
     * @param enfantComplexModel
     *            Le modèle enfant à utiliser
     */
    public void setEnfantComplexModel(EnfantComplexModel enfantComplexModel) {
        this.enfantComplexModel = enfantComplexModel;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        enfantComplexModel.setId(newId);

    }

    /**
     * @param nbDroitsActifs
     *            Le nombre de droits pour lesquels l'enfant est actif
     */
    public void setNbDroitsActifs(int nbDroitsActifs) {
        this.nbDroitsActifs = nbDroitsActifs;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        enfantComplexModel = ALServiceLocator.getEnfantComplexModelService().update(enfantComplexModel);

    }

}
