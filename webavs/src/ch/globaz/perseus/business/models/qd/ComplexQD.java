/**
 * 
 */
package ch.globaz.perseus.business.models.qd;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.perseus.business.models.situationfamille.MembreFamille;

/**
 * @author DDE
 * 
 */
public class ComplexQD extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private MembreFamille membreFamille = null;
    private QDAnnuelle qdAnnuelle = null;
    private SimpleQD simpleQD = null;

    public ComplexQD() {
        super();
        simpleQD = new SimpleQD();
        membreFamille = new MembreFamille();
        qdAnnuelle = new QDAnnuelle();
    }

    @Override
    public String getId() {
        return simpleQD.getId();
    }

    /**
     * @return the membreFamille
     */
    public MembreFamille getMembreFamille() {
        return membreFamille;
    }

    /**
     * @return the qdAnnuelle
     */
    public QDAnnuelle getQdAnnuelle() {
        return qdAnnuelle;
    }

    /**
     * @return the simpleQD
     */
    public SimpleQD getSimpleQD() {
        return simpleQD;
    }

    @Override
    public String getSpy() {
        return simpleQD.getSpy();
    }

    public CSTypeQD getTypeQD() {
        return CSTypeQD.getType(getSimpleQD().getCsType());
    }

    @Override
    public void setId(String id) {
        simpleQD.setId(id);
    }

    /**
     * @param membreFamille
     *            the membreFamille to set
     */
    public void setMembreFamille(MembreFamille membreFamille) {
        this.membreFamille = membreFamille;
    }

    /**
     * @param qdAnnuelle
     *            the qdAnnuelle to set
     */
    public void setQdAnnuelle(QDAnnuelle qdAnnuelle) {
        this.qdAnnuelle = qdAnnuelle;
    }

    /**
     * @param simpleQD
     *            the simpleQD to set
     */
    public void setSimpleQD(SimpleQD simpleQD) {
        this.simpleQD = simpleQD;
    }

    @Override
    public void setSpy(String spy) {
        simpleQD.setSpy(spy);
    }

    public void setTypeQD(CSTypeQD typeQD) {
        getSimpleQD().setCsType(typeQD.getCodeSystem());
    }

}
