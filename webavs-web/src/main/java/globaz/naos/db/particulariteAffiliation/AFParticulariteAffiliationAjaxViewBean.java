package globaz.naos.db.particulariteAffiliation;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSpy;

import java.util.Iterator;

/**
 * Le viewBean ajax de l'entité ParticulariteAffiliation.
 *
 * @author ebko
 */
public class AFParticulariteAffiliationAjaxViewBean extends AFParticulariteAffiliation implements FWAJAXViewBeanInterface {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur d'AFParticulariteAffiliationViewBean.
     */
    public AFParticulariteAffiliationAjaxViewBean() {
        super();
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
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
    public void setGetListe(boolean b) {
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
    }

    @Override
    public BSpy getSpy() {
        return null;
    }
}
