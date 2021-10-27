/**
 * 
 */
package globaz.pegasus.vb.home;

import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.home.Home;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

import static globaz.externe.IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE;

/**
 * @author ECO
 * 
 */
public class PCHomeAjaxViewBean extends BJadePersistentObjectViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Home home = null;
    /**
     * L'adresse formatee du home
     */
    private String homeAdresseFormatee = null;

    /**
     * L'adresse formatee du home
     */
    private String homeAdressePaiementFormatee = null;

    /**
	 * 
	 */
    public PCHomeAjaxViewBean() {
        super();
        home = new Home();
    }

    @Override
    public void add() throws Exception {
        home = PegasusServiceLocator.getHomeService().create(home);
    }

    @Override
    public void delete() throws Exception {
        home = PegasusServiceLocator.getHomeService().delete(home);
    }

    private void generateAdresse() throws JadePersistenceException, JadeApplicationException,
            JadeApplicationServiceNotAvailableException {
        // on recherche l'adresse formatee du home
        homeAdresseFormatee = TIBusinessServiceLocator
                .getAdresseService()
                .getAdresseTiers(home.getSimpleHome().getIdTiersHome(), Boolean.TRUE, JACalendar.todayJJsMMsAAAA(),
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, AdresseService.CS_TYPE_COURRIER, null)
                .getAdresseFormate();

    }
    private void generateAdressePaiement() throws JadeApplicationException, JadePersistenceException {
        homeAdressePaiementFormatee = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(home.getSimpleHome().getIdTiersHome(), Boolean.TRUE,   TIERS_CS_DOMAINE_APPLICATION_RENTE,JACalendar.todayJJsMMsAAAA(),"").getAdresseFormate();
    }

    /**
     * @return the home
     */
    public Home getHome() {
        return home;
    }

    /**
     * @return the homeAdresseFormatee
     */
    public String getHomeAdresseFormatee() {
        return homeAdresseFormatee;
    }

    @Override
    public String getId() {
        return home.getId();
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return null;
    }

    @Override
    public BSpy getSpy() {
        return null;
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
        home = PegasusServiceLocator.getHomeService().read(home.getId());
        if(home != null && home.getSimpleHome().getIdTiersHome() != null){
            generateAdresse();
            generateAdressePaiement();
        }
    }

    @Override
    public void setGetListe(boolean getListe) {

    }

    /**
     * @param home
     *            the home to set
     */
    public void setHome(Home home) throws Exception {
        this.home = home;
        if(home != null && home.getSimpleHome().getIdTiersHome() != null){
            generateAdresse();
            generateAdressePaiement();
        }
    }

    /**
     * @param homeAdresseFormatee
     *            the homeAdresseFormatee to set
     */
    public void setHomeAdresseFormatee(String homeAdresseFormatee) {
        this.homeAdresseFormatee = homeAdresseFormatee;
    }

    @Override
    public void setId(String newId) {
        home.setId(newId);
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {

    }

    @Override
    public void update() throws Exception {
        home = PegasusServiceLocator.getHomeService().update(home);
    }

}
