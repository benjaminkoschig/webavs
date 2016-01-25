package ch.globaz.perseus.business.models.lot;

import globaz.jade.persistence.model.JadeComplexModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.qd.Facture;

/**
 * 
 * @author MBO
 * 
 */

public class Prestation extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Decision decision = null;
    private Facture facture = null;
    /**
     * La liste des ordres de versement n'est pas remplie automatiquement par la persistence
     */
    private List<OrdreVersement> listOrdreVersement = null;
    private Lot lot = null;
    private SimplePrestation simplePrestation = null;

    public Prestation() {
        simplePrestation = new SimplePrestation();
        decision = new Decision();
        lot = new Lot();
        facture = new Facture();
        listOrdreVersement = new ArrayList<OrdreVersement>();
    }

    /**
     * @return the decision
     */
    public Decision getDecision() {
        return decision;
    }

    /**
     * @return the facture
     */
    public Facture getFacture() {
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

    public SimplePrestation getSimplePrestation() {
        return simplePrestation;
    }

    @Override
    public String getSpy() {
        return simplePrestation.getSpy();
    }

    /**
     * @param decision
     *            the decision to set
     */
    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    /**
     * @param facture
     *            the facture to set
     */
    public void setFacture(Facture facture) {
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

    public void setSimplePrestation(SimplePrestation simplePrestation) {
        this.simplePrestation = simplePrestation;
    }

    @Override
    public void setSpy(String spy) {
        simplePrestation.setSpy(spy);

    }

}
