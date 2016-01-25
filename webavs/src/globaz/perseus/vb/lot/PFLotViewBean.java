package globaz.perseus.vb.lot;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * 
 * @author MBO
 * 
 */

public class PFLotViewBean extends BJadePersistentObjectViewBean {

    private Lot lot = null;

    public PFLotViewBean() {
        super();
        lot = new Lot();
    }

    public PFLotViewBean(Lot lot) {
        super();
        this.lot = lot;
    }

    @Override
    public void add() throws Exception {
        PerseusServiceLocator.getLotService().create(lot);

    }

    @Override
    public void delete() throws Exception {
        PerseusServiceLocator.getLotService().delete(lot);

    }

    @Override
    public String getId() {
        return lot.getId();
    }

    public Lot getLot() {
        return lot;
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(lot.getSpy());
    }

    @Override
    public void retrieve() throws Exception {
        lot = PerseusServiceLocator.getLotService().read(lot.getId());

    }

    @Override
    public void setId(String newId) {
        lot.setId(newId);
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    @Override
    public void update() throws Exception {
        PerseusServiceLocator.getLotService().update(lot);

    }

}
