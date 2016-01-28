package globaz.aries.vb.decisioncgas;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.util.Iterator;
import ch.globaz.aries.business.beans.decisioncgas.DecisionCGASBean;
import ch.globaz.aries.business.services.AriesServiceLocator;

public class ARDecisionCgasAjaxViewBean extends BJadePersistentObjectViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DecisionCGASBean decisionCGASBean = null;

    public ARDecisionCgasAjaxViewBean() {
        super();
        decisionCGASBean = new DecisionCGASBean();
    }

    @Override
    public void add() throws Exception {
        decisionCGASBean = AriesServiceLocator.getDecisionCGASService().create(decisionCGASBean);

    }

    @Override
    public void delete() throws Exception {
        decisionCGASBean = AriesServiceLocator.getDecisionCGASService().delete(decisionCGASBean);

    }

    public DecisionCGASBean getDecisionCGASBean() {
        return decisionCGASBean;
    }

    @Override
    public String getId() {
        return decisionCGASBean.getDecisionCGAS().getId();
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        // Méthode non-utilisée
        return null;
    }

    @Override
    public BSpy getSpy() {
        return decisionCGASBean.isNew() ? new BSpy((BSession) getISession()) : new BSpy(decisionCGASBean.getSpy());
    }

    @Override
    public boolean hasList() {
        // Méthode non-utilisée
        return false;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Iterator iterator() {
        // Méthode non-utilisée
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        decisionCGASBean = AriesServiceLocator.getDecisionCGASService().read(getDecisionCGASBean().getId());

    }

    @Override
    public void setGetListe(boolean getListe) {
        // Méthode non-utilisée

    }

    @Override
    public void setId(String newId) {
        decisionCGASBean.getDecisionCGAS().setId(newId);

    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        // Méthode non-utilisée

    }

    @Override
    public void update() throws Exception {
        decisionCGASBean = AriesServiceLocator.getDecisionCGASService().update(decisionCGASBean);

    }

}
