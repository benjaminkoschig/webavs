/*
 * Créé le 13 fev. 07
 */
package globaz.corvus.vb.basescalcul;

import globaz.corvus.db.basescalcul.REBasesCalculDixiemeRevision;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import java.util.Iterator;

/**
 * @author bsc
 */
public class REBasesCalculDixiemeRevisionViewBean extends REAbstractBasesCalculProxyViewBean implements
        FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public REBasesCalculDixiemeRevisionViewBean() {
        super(new REBasesCalculDixiemeRevision());
    }

    @Override
    public REBasesCalculDixiemeRevision getBasesCalcul() {
        return (REBasesCalculDixiemeRevision) super.getBasesCalcul();
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasList() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Iterator iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setGetListe(boolean getListe) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        // TODO Auto-generated method stub

    }

}
