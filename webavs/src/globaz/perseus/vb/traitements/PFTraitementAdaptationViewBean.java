/**
 * 
 */
package globaz.perseus.vb.traitements;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.pegasus.business.exceptions.NotImplementedException;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFTraitementAdaptationViewBean extends BJadePersistentObjectViewBean {

    private String adresseMailAGLAU = null;
    private String adresseMailCCVD = null;
    private String mois = null;
    private String texteDecision = null;

    public PFTraitementAdaptationViewBean() {
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
     * @return the adresseMailAGLAU
     */
    public String getAdresseMailAGLAU() {
        return adresseMailAGLAU;
    }

    /**
     * @return the adresseMailCCVD
     */
    public String getAdresseMailCCVD() {
        return adresseMailCCVD;
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

    /**
     * @return the texte
     */
    public String getTexteDecision() {
        return texteDecision;
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
     * @param adresseMailAGLAU
     *            the adresseMailAGLAU to set
     */
    public void setAdresseMailAGLAU(String adresseMailAGLAU) {
        this.adresseMailAGLAU = adresseMailAGLAU;
    }

    /**
     * @param adresseMailCCVD
     *            the adresseMailCCVD to set
     */
    public void setAdresseMailCCVD(String adresseMailCCVD) {
        this.adresseMailCCVD = adresseMailCCVD;
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

    /**
     * @param texte
     *            the texte to set
     */
    public void setTexteDecision(String texte) {
        texteDecision = texte;
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
