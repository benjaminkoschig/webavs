package globaz.pegasus.vb.parametre;

import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.ForfaitsPrimesAssuranceMaladie;
import ch.globaz.pegasus.business.models.parametre.SimpleZoneForfaits;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.PCParametreHandler;

/**
 * @author DMA
 * @date 12 nov. 2010
 */
public class PCForfaitLoyerViewBean extends BJadePersistentObjectViewBean {
    ForfaitsPrimesAssuranceMaladie forfaitPrimesAssuranceMaladie = null;

    public PCForfaitLoyerViewBean() {
        super();
        forfaitPrimesAssuranceMaladie = new ForfaitsPrimesAssuranceMaladie();
    }

    public PCForfaitLoyerViewBean(ForfaitsPrimesAssuranceMaladie forfaitPrimesAssuranceMaladie) {
        super();
        this.forfaitPrimesAssuranceMaladie = forfaitPrimesAssuranceMaladie;
    }

    @Override
    public void add() throws Exception {
        PegasusServiceLocator.getParametreServicesLocator().getForfaitsPrimesAssuranceMaladieService()
                .create(forfaitPrimesAssuranceMaladie);
    }

    @Override
    public void delete() throws Exception {
        PegasusServiceLocator.getParametreServicesLocator().getForfaitsPrimesAssuranceMaladieService()
                .delete(forfaitPrimesAssuranceMaladie);

    }

    public String getDescZone(BSession objSession) {
        SimpleZoneForfaits zf = getForfaitPrimesAssuranceMaladie().getSimpleZoneForfaits();
        return this.getDescZone(zf, objSession);
    }

    public String getDescZone(SimpleZoneForfaits simpleZoneForfaits, BSession objSession) {
        return PCParametreHandler.getDescriptionZone(simpleZoneForfaits, objSession);
    }

    /**
     * @return the forfaitPrimesAssuranceMaladie
     */
    public ForfaitsPrimesAssuranceMaladie getForfaitPrimesAssuranceMaladie() {
        return forfaitPrimesAssuranceMaladie;
    }

    @Override
    public String getId() {
        return forfaitPrimesAssuranceMaladie.getId();
    }

    public JadeAbstractModel[] getListZoneFofaits() throws ForfaitsPrimesAssuranceMaladieException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return PCParametreHandler.getListZoneFofaitsLoyer();
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
        return (forfaitPrimesAssuranceMaladie != null) && !forfaitPrimesAssuranceMaladie.isNew() ? new BSpy(
                forfaitPrimesAssuranceMaladie.getSpy()) : new BSpy(getSession());
    }

    @Override
    public void retrieve() throws Exception {
        forfaitPrimesAssuranceMaladie = PegasusServiceLocator.getParametreServicesLocator()
                .getForfaitsPrimesAssuranceMaladieService().read(forfaitPrimesAssuranceMaladie.getId());

    }

    /**
     * @param forfaitPrimesAssuranceMaladie
     *            the forfaitPrimesAssuranceMaladie to set
     */
    public void setForfaitPrimesAssuranceMaladie(ForfaitsPrimesAssuranceMaladie forfaitPrimesAssuranceMaladie) {
        this.forfaitPrimesAssuranceMaladie = forfaitPrimesAssuranceMaladie;
    }

    @Override
    public void setId(String newId) {
        forfaitPrimesAssuranceMaladie.setId(newId);

    }

    @Override
    public void update() throws Exception {
        PegasusServiceLocator.getParametreServicesLocator().getForfaitsPrimesAssuranceMaladieService()
                .update(forfaitPrimesAssuranceMaladie);

    }

}
