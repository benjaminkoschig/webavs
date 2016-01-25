/**
 * 
 */
package globaz.perseus.vb.lot;

import globaz.globall.db.BSpy;
import globaz.perseus.utils.PFUserHelper;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.models.lot.PrestationRP;

/**
 * @author BSC
 */
public class PFPrestationRPViewBean extends PFAbstractPrestationViewBean {

    // instance de la classe métier
    private PrestationRP prestation = null;

    /**
     * Constructeur simple
     */
    public PFPrestationRPViewBean() {
        super();
        prestation = new PrestationRP();
    }

    /**
     * Constructeur simple
     */
    public PFPrestationRPViewBean(PrestationRP prestation) {
        super();
        this.prestation = prestation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
    }

    @Override
    public String getDetailAssure() {
        if (CSTypeLot.LOT_FACTURES_RP.getCodeSystem().equals(prestation.getLot().getSimpleLot().getTypeLot())) {
            return PFUserHelper.getDetailAssure(getSession(), prestation.getFacture().getQdRentePont().getDossier()
                    .getDemandePrestation().getPersonneEtendue());
        } else {
            return PFUserHelper.getDetailAssure(getSession(), prestation.getRentePont().getDossier()
                    .getDemandePrestation().getPersonneEtendue());
        }
    }

    @Override
    public String getEtat() {
        return getSession().getCodeLibelle(prestation.getSimplePrestation().getEtatPrestation());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return prestation.getId();
    }

    @Override
    public String getIdDecision() {
        return prestation.getSimplePrestation().getIdDecisionPcf();
    }

    @Override
    public String getIdFacture() {
        return prestation.getSimplePrestation().getIdFacture();
    }

    @Override
    public String getIdTiersRequerant() {
        if (CSTypeLot.LOT_FACTURES.getCodeSystem().equals(prestation.getLot().getSimpleLot().getTypeLot())) {
            return prestation.getFacture().getQdRentePont().getDossier().getDemandePrestation().getDemandePrestation()
                    .getIdTiers();
        } else {
            return prestation.getRentePont().getDossier().getDemandePrestation().getDemandePrestation().getIdTiers();
        }
    }

    @Override
    public String getMontant() {
        return prestation.getSimplePrestation().getMontantTotal();
    }

    @Override
    public String getPeriode() {
        return prestation.getSimplePrestation().getDateDebut() + " - " + prestation.getSimplePrestation().getDateFin();
    }

    public PrestationRP getPrestation() {
        return prestation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (prestation != null) && !prestation.isNew() ? new BSpy(prestation.getSpy()) : new BSpy(getSession());
    }

    @Override
    public String getTypeLot() {
        return prestation.getLot().getSimpleLot().getTypeLot();
    }

    @Override
    public boolean isPCFamilles() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        prestation.setId(newId);
    }

    public void setPrestation(PrestationRP prestation) {
        this.prestation = prestation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
    }

}
