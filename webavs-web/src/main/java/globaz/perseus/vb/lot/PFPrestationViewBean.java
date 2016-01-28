/**
 * 
 */
package globaz.perseus.vb.lot;

import globaz.globall.db.BSpy;
import globaz.perseus.utils.PFUserHelper;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.models.lot.Prestation;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author BSC
 */
public class PFPrestationViewBean extends PFAbstractPrestationViewBean {

    // instance de la classe métier
    private Prestation prestation = null;

    /**
     * Constructeur simple
     */
    public PFPrestationViewBean() {
        super();
        prestation = new Prestation();
    }

    /**
     * Constructeur simple
     */
    public PFPrestationViewBean(Prestation prestation) {
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
        prestation = PerseusServiceLocator.getPrestationService().delete(prestation);
    }

    @Override
    public String getDetailAssure() {
        if (CSTypeLot.LOT_FACTURES.getCodeSystem().equals(prestation.getLot().getSimpleLot().getTypeLot())) {
            return PFUserHelper.getDetailAssure(getSession(), prestation.getFacture().getQd().getQdAnnuelle()
                    .getDossier().getDemandePrestation().getPersonneEtendue());
        } else {
            return PFUserHelper.getDetailAssure(getSession(), prestation.getDecision().getDemande().getDossier()
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
            return prestation.getFacture().getQd().getQdAnnuelle().getDossier().getDemandePrestation()
                    .getDemandePrestation().getIdTiers();
        } else {
            return prestation.getDecision().getDemande().getDossier().getDemandePrestation().getDemandePrestation()
                    .getIdTiers();
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

    public Prestation getPrestation() {
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
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        prestation = PerseusServiceLocator.getPrestationService().read(prestation.getId());
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

    public void setPrestation(Prestation prestation) {
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
