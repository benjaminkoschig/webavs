/**
 * 
 */
package globaz.al.vb.envois;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.al.business.models.envoi.EnvoiParametresSimpleModel;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * @author dhi
 * 
 */
public class ALParametresViewBean extends BJadePersistentObjectViewBean {

    private EnvoiParametresSimpleModel envoiParametres = null;

    /**
     * Default constructor
     */
    public ALParametresViewBean() {
        super();
        envoiParametres = new EnvoiParametresSimpleModel();
    }

    /**
     * Default constructor called form rcliste
     * 
     * @param _envoisParametres
     */
    public ALParametresViewBean(EnvoiParametresSimpleModel _envoisParametres) {
        super();
        envoiParametres = _envoisParametres;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        envoiParametres = ALImplServiceLocator.getEnvoiParametresSimpleModelService().create(envoiParametres);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        envoiParametres = ALImplServiceLocator.getEnvoiParametresSimpleModelService().delete(envoiParametres);
    }

    /**
     * @return the envoiParametres
     */
    public EnvoiParametresSimpleModel getEnvoiParametres() {
        return envoiParametres;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        if (envoiParametres != null) {
            return envoiParametres.getId();
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        if (envoiParametres != null) {
            return new BSpy(envoiParametres.getSpy());
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        if (envoiParametres != null) {
            envoiParametres = ALImplServiceLocator.getEnvoiParametresSimpleModelService().read(getId());
        } else {
            envoiParametres = new EnvoiParametresSimpleModel();
        }
    }

    /**
     * @param envoiParametres
     *            the envoiParametres to set
     */
    public void setEnvoiParametres(EnvoiParametresSimpleModel envoiParametres) {
        this.envoiParametres = envoiParametres;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        if (envoiParametres != null) {
            envoiParametres.setId(newId);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        envoiParametres = ALImplServiceLocator.getEnvoiParametresSimpleModelService().update(envoiParametres);
    }

}
