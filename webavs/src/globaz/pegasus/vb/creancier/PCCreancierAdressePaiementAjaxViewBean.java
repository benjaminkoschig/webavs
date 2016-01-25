package globaz.pegasus.vb.creancier;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Iterator;
import ch.globaz.pyxis.business.model.AdressePaiementComplexModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class PCCreancierAdressePaiementAjaxViewBean extends BJadePersistentObjectViewBean implements
        FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AdressePaiementComplexModel adressePaiement = null;

    public PCCreancierAdressePaiementAjaxViewBean() {
        super();
        adressePaiement = new AdressePaiementComplexModel();
    }

    public PCCreancierAdressePaiementAjaxViewBean(AdressePaiementComplexModel adressePaiement) {
        super();
        this.adressePaiement = adressePaiement;
    }

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    public String getAdressePaiement() throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {
        AdresseTiersDetail detailTiers = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
                adressePaiement.getAvoirPaiement().getIdTiers(), Boolean.TRUE,
                adressePaiement.getAvoirPaiement().getIdApplication(), JACalendar.todayJJsMMsAAAA(),
                adressePaiement.getAvoirPaiement().getIdExterne());
        return detailTiers != null ? detailTiers.getAdresseFormate() : "";
    }

    @Override
    public String getId() {
        return adressePaiement.getId();
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return null;
    }

    @Override
    public BSpy getSpy() {
        return (adressePaiement != null) && !adressePaiement.isNew() ? new BSpy(adressePaiement.getSpy()) : new BSpy(
                (BSession) getISession());
    }

    @Override
    public boolean hasList() {
        return false;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setGetListe(boolean getListe) {
    }

    @Override
    public void setId(String newId) {
        adressePaiement.setId(newId);
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
    }

    @Override
    public void update() throws Exception {
    }

}
