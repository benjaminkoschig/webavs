/**
 *
 */
package globaz.osiris.vb.recouvrement;

import globaz.osiris.vb.CAAbstractPersistenceViewBean;

/**
 * @author SEL
 */
public class CASursisEcheanceViewBean extends CAAbstractPersistenceViewBean {

    private String idEcheance = null;
    private String idPlanRecouvrement = null;

    @Override
    public void add() throws Exception {
        throw new Exception("NOT TO USE !");
    }

    @Override
    public void delete() throws Exception {
        throw new Exception("NOT TO USE !");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.vb.CAAbstractPersistenceViewBean#getId()
     */
    @Override
    public String getId() {
        return idEcheance;
    }

    /**
     * @return the idEcheance
     */
    public String getIdEcheance() {
        return idEcheance;
    }

    /**
     * @return the idPlanRecouvrement
     */
    public String getIdPlanRecouvrement() {
        return idPlanRecouvrement;
    }

    @Override
    public void retrieve() throws Exception {
        throw new Exception("NOT TO USE !");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.vb.CAAbstractPersistenceViewBean#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        idEcheance = newId;
    }

    /**
     * @param idEcheance
     *            the idEcheance to set
     */
    public void setIdEcheance(String idEcheance) {
        this.idEcheance = idEcheance;
    }

    /**
     * @param idPlanRecouvrement
     *            the idPlanRecouvrement to set
     */
    public void setIdPlanRecouvrement(String idPlanRecouvrement) {
        this.idPlanRecouvrement = idPlanRecouvrement;
    }

    @Override
    public void update() throws Exception {
        throw new Exception("NOT TO USE !");
    }

}
