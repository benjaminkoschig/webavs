package globaz.perseus.vb.lot;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFGenererListeOrdresVersementViewBean extends BJadePersistentObjectViewBean {

    private String eMailAddress = null;
    private String idLot = null;
    private Lot lot = null;

    public PFGenererListeOrdresVersementViewBean() {
        lot = new Lot();
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
        return lot.getId();
    }

    public String getIdLot() {
        return idLot;
    }

    public Lot getLot() {
        return lot;
    }

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return (lot != null) && !lot.isNew() ? new BSpy(lot.getSpy()) : new BSpy(getSession());
    }

    @Override
    public void retrieve() throws Exception {
        lot = PerseusServiceLocator.getLotService().read(lot.getId());
    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    @Override
    public void setId(String newId) {
        lot.setId(newId);
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub
    }

}
