/**
 * 
 */
package ch.globaz.perseus.business.models.qd;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.perseus.business.models.dossier.Dossier;

/**
 * @author DDE
 * 
 */
public class QDAnnuelle extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Dossier dossier = null;
    private SimpleQDAnnuelle simpleQDAnnuelle = null;

    public QDAnnuelle() {
        super();
        dossier = new Dossier();
        simpleQDAnnuelle = new SimpleQDAnnuelle();
    }

    /**
     * @return the dossier
     */
    public Dossier getDossier() {
        return dossier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleQDAnnuelle.getId();
    }

    /**
     * @return the simpleQDAnnuelle
     */
    public SimpleQDAnnuelle getSimpleQDAnnuelle() {
        return simpleQDAnnuelle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleQDAnnuelle.getSpy();
    }

    /**
     * @param dossier
     *            the dossier to set
     */
    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleQDAnnuelle.setId(id);
    }

    /**
     * @param simpleQDAnnuelle
     *            the simpleQDAnnuelle to set
     */
    public void setSimpleQDAnnuelle(SimpleQDAnnuelle simpleQDAnnuelle) {
        this.simpleQDAnnuelle = simpleQDAnnuelle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleQDAnnuelle.setSpy(spy);
    }

}
