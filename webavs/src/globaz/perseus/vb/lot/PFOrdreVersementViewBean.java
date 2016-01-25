/**
 * 
 */
package globaz.perseus.vb.lot;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.utils.PFUserHelper;
import java.util.Date;
import ch.globaz.perseus.business.models.lot.OrdreVersement;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;

/**
 * 
 * @author MBO
 * 
 */
public class PFOrdreVersementViewBean extends BJadePersistentObjectViewBean {

    // instance de la classe métier
    private OrdreVersement ordreVersement = null;

    /**
     * Constructeur simple
     */
    public PFOrdreVersementViewBean() {
        super();
        ordreVersement = new OrdreVersement();
    }

    /**
     * Constructeur simple
     */
    public PFOrdreVersementViewBean(OrdreVersement ordreVersement) {
        super();
        this.ordreVersement = ordreVersement;
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

    public String getAdressePaiement() throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {
        AdresseTiersDetail detailTiers = null;
        if (!JadeStringUtil.isEmpty(ordreVersement.getTiers().getIdTiers())) {
            detailTiers = PFUserHelper.getAdressePaiementAssure(ordreVersement.getSimpleOrdreVersement()
                    .getIdTiersAdressePaiement(), ordreVersement.getSimpleOrdreVersement().getIdDomaineApplication(),
                    JadeDateUtil.getGlobazFormattedDate(new Date()));
        }

        return detailTiers != null ? JadeStringUtil.toNotNullString(detailTiers.getAdresseFormate()) : "";
    }

    public String getBeneficiaire() {
        return ordreVersement.getTiers().getDesignation1() + " " + ordreVersement.getTiers().getDesignation2();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return ordreVersement.getId();
    }

    public OrdreVersement getOrdreVersement() {
        return ordreVersement;
    }

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (ordreVersement != null) && !ordreVersement.isNew() ? new BSpy(ordreVersement.getSpy()) : new BSpy(
                getSession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        ordreVersement = PerseusServiceLocator.getOrdreVersementService().read(ordreVersement.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        ordreVersement.setId(newId);
    }

    public void setOrdreVersement(OrdreVersement ordreVersement) {
        this.ordreVersement = ordreVersement;
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
