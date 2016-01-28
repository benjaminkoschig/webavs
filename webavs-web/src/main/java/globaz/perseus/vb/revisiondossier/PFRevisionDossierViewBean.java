/**
 * 
 */
package globaz.perseus.vb.revisiondossier;

import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.prestation.utils.ged.PRGedUtils;
import ch.globaz.pegasus.business.exceptions.NotImplementedException;

/**
 * @author DDE
 * 
 */
public class PFRevisionDossierViewBean extends BJadePersistentObjectViewBean {

    private String adresseMail = null;
    private String csCaisse = null;
    private String dateRevision = null;
    private String isSendToGed = null;

    public PFRevisionDossierViewBean() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        throw new NotImplementedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        throw new NotImplementedException();
    }

    public String getAdresseMail() {
        return adresseMail;
    }

    public String getCsCaisse() {
        return csCaisse;
    }

    public String getDateRevision() {
        return dateRevision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIsSendToGed() {
        return isSendToGed;
    }

    // TODO Récupération de la session user
    protected BSession getSession() {
        return BSessionUtil.getSessionFromThreadContext();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isSendToGed(String caisse) {
        if (null == caisse) {
            return false;
        } else {
            return PRGedUtils.isDocumentInGed(IPRConstantesExternes.PCF_REVISION_DOSSIER, caisse, getSession());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {

    }

    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    public void setCsCaisse(String csCaisse) {
        this.csCaisse = csCaisse;
    }

    public void setDateRevision(String dateRevision) {
        this.dateRevision = dateRevision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    public void setIsSendToGed(String isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        throw new NotImplementedException();
    }

}
