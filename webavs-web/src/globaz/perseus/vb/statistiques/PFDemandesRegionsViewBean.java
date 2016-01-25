/**
 * 
 */
package globaz.perseus.vb.statistiques;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.pegasus.business.exceptions.NotImplementedException;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFDemandesRegionsViewBean extends BJadePersistentObjectViewBean {

    private String adresseMail = null;
    private String mois = null;

    public PFDemandesRegionsViewBean() {
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

    /**
     * @return the adresseMail
     */
    public String getAdresseMail() {
        return adresseMail;
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

    /**
     * @return the mois
     */
    public String getMois() {
        return mois;
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        mois = PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();
    }

    /**
     * @param adresseMail
     *            the adresseMail to set
     */
    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
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

    /**
     * @param mois
     *            the mois to set
     */
    public void setMois(String mois) {
        this.mois = mois;
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
