package globaz.perseus.vb.paiements;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFListeVerificationRentePontViewBean extends BJadePersistentObjectViewBean {

    private String moisCourant = null;

    public PFListeVerificationRentePontViewBean() throws Exception {
        super();
        moisCourant = PerseusServiceLocator.getPmtMensuelRentePontService().getDateProchainPmt();
    }

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    @Override
    public String getId() {
        return null;
    }

    /**
     * @return the moisCourant
     */
    public String getMoisCourant() {
        return moisCourant;
    }

    private BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(String newId) {
    }

    /**
     * @param moisCourant
     *            the moisCourant to set
     */
    public void setMoisCourant(String moisCourant) {
        this.moisCourant = moisCourant;
    }

    @Override
    public void update() throws Exception {
    }

}
