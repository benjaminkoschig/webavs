/**
 * 
 */
package globaz.perseus.vb.rentepont;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.perseus.business.constantes.CSEtatRentePont;
import ch.globaz.perseus.business.models.rentepont.RentePont;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author MBO
 * 
 */
public class PFCreancierRentePontViewBean extends BJadePersistentObjectViewBean {

    private Boolean areEditable = null;
    private String idRentePont = null;
    private String montantRetro = null;
    private RentePont rentePont;

    public PFCreancierRentePontViewBean() {
        super();
        rentePont = new RentePont();
    }

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    /**
     * @return the areEditable
     */
    public Boolean getAreEditable() {
        return areEditable;
    }

    @Override
    public String getId() {
        return null;
    }

    /**
     * @return the idRentePont
     */
    public String getIdRentePont() {
        return idRentePont;
    }

    /**
     * @return the montantRetro
     */
    public String getMontantRetro() {
        return montantRetro;
    }

    /**
     * @return the rentePont
     */
    public RentePont getRentePont() {
        return rentePont;
    }

    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(getSession());
    }

    public void init() throws Exception {
        rentePont = PerseusServiceLocator.getRentePontService().read(idRentePont);

        montantRetro = PerseusServiceLocator.getRentePontService().calculerRetro(rentePont).toString();

        areEditable = !CSEtatRentePont.VALIDE.getCodeSystem().equals(rentePont.getSimpleRentePont().getCsEtat());
    }

    @Override
    public void retrieve() throws Exception {
        init();
    }

    /**
     * @param areEditable
     *            the areEditable to set
     */
    public void setAreEditable(Boolean areEditable) {
        this.areEditable = areEditable;
    }

    @Override
    public void setId(String newId) {

    }

    /**
     * @param idRentePont
     *            the idRentePont to set
     */
    public void setIdRentePont(String idRentePont) {
        this.idRentePont = idRentePont;
    }

    /**
     * @param montantRetro
     *            the montantRetro to set
     */
    public void setMontantRetro(String montantRetro) {
        this.montantRetro = montantRetro;
    }

    /**
     * @param rentePont
     *            the rentePont to set
     */
    public void setRentePont(RentePont rentePont) {
        this.rentePont = rentePont;
    }

    @Override
    public void update() {

    }

}
