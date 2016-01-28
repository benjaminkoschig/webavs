/**
 * 
 */
package globaz.pegasus.vb.decision;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 * 
 */
public class PCDevalidationDecisionViewBean extends BJadePersistentObjectViewBean {

    private String idDecision = null;
    private String idDemandePc = null;
    private String idDossier = null;
    private String idDroit = null;
    private String idVersionDroit = null;
    private String noVersion = null;

    /**
	 * 
	 */
    public PCDevalidationDecisionViewBean() {
        super();
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public void devalideDecision() throws Exception {
        PegasusServiceLocator.getDecisionService().devalideDecisions(idDroit, idVersionDroit, noVersion);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return idVersionDroit;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDemandePc() {
        return idDemandePc;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdDroit() {
        return idDroit;
    }

    /**
     * @return the idVersionDroit
     */
    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public String getNoVersion() {
        return noVersion;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        idVersionDroit = newId;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDemandePc(String idDemandePc) {
        this.idDemandePc = idDemandePc;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    /**
     * @param idVersionDroit
     *            the idVersionDroit to set
     */
    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public void setNoVersion(String noVersion) {
        this.noVersion = noVersion;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
