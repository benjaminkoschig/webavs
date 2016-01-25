package globaz.vulpecula.vb;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.util.Iterator;

public abstract class PTAjaxDisplayViewBean extends BJadePersistentObjectViewBean implements FWAJAXViewBeanInterface {
    private static final long serialVersionUID = -1763793994222047597L;

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String arg0) {
    }

    @Override
    public void update() throws Exception {
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
    public void setGetListe(boolean arg0) {
    }

    @Override
    public void setListViewBean(FWViewBeanInterface arg0) {
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

}
