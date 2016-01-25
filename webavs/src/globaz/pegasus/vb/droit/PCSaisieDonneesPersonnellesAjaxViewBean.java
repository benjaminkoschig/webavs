/**
 * 
 */
package globaz.pegasus.vb.droit;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.droit.DonneesPersonnelles;
import ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author ECO
 * 
 */
public class PCSaisieDonneesPersonnellesAjaxViewBean extends BJadePersistentObjectViewBean implements
        FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private DonneesPersonnelles donneesPersonnelles = null;

    private ModificateurDroitDonneeFinanciere droit = null;

    /**
	 * 
	 */
    public PCSaisieDonneesPersonnellesAjaxViewBean() {
        super();
        donneesPersonnelles = new DonneesPersonnelles();
    }

    @Override
    public void add() throws Exception {
        throw new Exception("Method not implemented");
    }

    @Override
    public void delete() throws Exception {
        throw new Exception("Method not implemented");
    }

    /**
     * @return the donneesPersonnelles
     */
    public DonneesPersonnelles getDonneesPersonnelles() {
        return donneesPersonnelles;
    }

    /**
     * @return the droit
     */
    public ModificateurDroitDonneeFinanciere getDroit() {
        return droit;
    }

    @Override
    public String getId() {
        return donneesPersonnelles.getId();
    }

    public String getInfoRepondant() throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {
        String infos = "";
        if (!JadeStringUtil.isBlankOrZero(donneesPersonnelles.getSimpleDonneesPersonnelles().getIdTiersRepondant())) {
            TiersSimpleModel simpleTiersSimpleModel = TIBusinessServiceLocator.getTiersService().read(
                    donneesPersonnelles.getSimpleDonneesPersonnelles().getIdTiersRepondant());
            if ((simpleTiersSimpleModel != null) && !JadeStringUtil.isEmpty(simpleTiersSimpleModel.getIdTiers())) {
                infos = simpleTiersSimpleModel.getDesignation1() + " " + simpleTiersSimpleModel.getDesignation2();
            }
        }

        return infos;
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

        donneesPersonnelles = PegasusServiceLocator.getDroitService().readDonneesPersonnelles(
                donneesPersonnelles.getId());

    }

    /**
     * @param donneesPersonnelles
     *            the donneesPersonnelles to set
     */
    public void setDonneesPersonnelles(DonneesPersonnelles donneesPersonnelles) {
        this.donneesPersonnelles = donneesPersonnelles;
    }

    /**
     * @param droit
     *            the droit to set
     */
    public void setDroit(ModificateurDroitDonneeFinanciere droit) {
        this.droit = droit;
    }

    @Override
    public void setGetListe(boolean getListe) {

    }

    @Override
    public void setId(String newId) {
        donneesPersonnelles.setId(newId);
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {

    }

    @Override
    public void update() throws Exception {
        donneesPersonnelles = PegasusServiceLocator.getDroitService().updateDonneesPersonnelles(donneesPersonnelles);
    }
}
