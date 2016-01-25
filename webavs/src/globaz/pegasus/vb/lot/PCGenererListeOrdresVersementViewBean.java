package globaz.pegasus.vb.lot;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.services.CorvusServiceLocator;

public class PCGenererListeOrdresVersementViewBean extends BJadePersistentObjectViewBean {

    private String eMailAddress = null;
    private SimpleLot simpleLot = null;

    public PCGenererListeOrdresVersementViewBean() {
        simpleLot = new SimpleLot();
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public void execute() {

    }

    public String geteMailAddress() {
        return eMailAddress;
    }

    @Override
    public String getId() {
        return simpleLot.getId();
    }

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    public SimpleLot getSimpleLot() {
        return simpleLot;
    }

    @Override
    public BSpy getSpy() {
        return (simpleLot != null) && !simpleLot.isNew() ? new BSpy(simpleLot.getSpy()) : new BSpy(getSession());
    }

    @Override
    public void retrieve() throws Exception {
        simpleLot = CorvusServiceLocator.getLotService().read(simpleLot.getId());

    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    @Override
    public void setId(String newId) {
        simpleLot.setId(newId);
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub
    }

}
