package ch.globaz.perseus.business.models.lot;

import globaz.jade.persistence.model.JadeComplexModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.business.models.rentepont.FactureRentePont;
import ch.globaz.perseus.business.models.rentepont.RentePont;

/**
 * 
 * @author MBO
 * 
 */

public class PrestationRP extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private FactureRentePont facture = null;
    /**
     * La liste des ordres de versement n'est pas remplie automatiquement par la persistence
     */
    private List<OrdreVersement> listOrdreVersement = null;
    private Lot lot = null;
    private RentePont rentePont = null;
    private SimplePrestation simplePrestation = null;

    public PrestationRP() {
        simplePrestation = new SimplePrestation();
        rentePont = new RentePont();
        lot = new Lot();
        facture = new FactureRentePont();
        listOrdreVersement = new ArrayList<OrdreVersement>();
    }

    /**
     * @return the facture
     */
    public FactureRentePont getFacture() {
        return facture;
    }

    @Override
    public String getId() {
        return simplePrestation.getId();
    }

    /**
     * @return the listOrdreVersement
     */
    public List<OrdreVersement> getListOrdreVersement() {
        return listOrdreVersement;
    }

    /**
     * @return the lot
     */
    public Lot getLot() {
        return lot;
    }

    /**
     * @return the rentePont
     */
    public RentePont getRentePont() {
        return rentePont;
    }

    public SimplePrestation getSimplePrestation() {
        return simplePrestation;
    }

    @Override
    public String getSpy() {
        return simplePrestation.getSpy();
    }

    /**
     * @param facture
     *            the facture to set
     */
    public void setFacture(FactureRentePont facture) {
        this.facture = facture;
    }

    @Override
    public void setId(String id) {
        simplePrestation.setId(id);
    }

    /**
     * @param listOrdreVersement
     *            the listOrdreVersement to set
     */
    public void setListOrdreVersement(List<OrdreVersement> listOrdreVersement) {
        this.listOrdreVersement = listOrdreVersement;
    }

    /**
     * @param lot
     *            the lot to set
     */
    public void setLot(Lot lot) {
        this.lot = lot;
    }

    /**
     * @param rentePont
     *            the rentePont to set
     */
    public void setRentePont(RentePont rentePont) {
        this.rentePont = rentePont;
    }

    public void setSimplePrestation(SimplePrestation simplePrestation) {
        this.simplePrestation = simplePrestation;
    }

    @Override
    public void setSpy(String spy) {
        simplePrestation.setSpy(spy);

    }

}
