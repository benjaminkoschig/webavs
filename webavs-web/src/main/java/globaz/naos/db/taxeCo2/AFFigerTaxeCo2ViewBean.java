package globaz.naos.db.taxeCo2;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;

public class AFFigerTaxeCo2ViewBean extends AFTaxeCo2ViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de AFAffiliation
     */
    public AFFigerTaxeCo2ViewBean() {
        super();
    }

    private void _addError(String message) {
        setMessage(message);
        setMsgType(FWViewBeanInterface.ERROR);
    }

    public void _validate() {
        if (getAnneeMasse().equals(JACalendar.todayJJsMMsAAAA().substring(6))
                || (JadeStringUtil.toInt(getAnneeMasse()) < JadeStringUtil.toInt(JACalendar.todayJJsMMsAAAA()
                        .substring(6)) - 2)) {
            this._addError(getSession().getLabel("PROCESS_FIGER_ERREUR_DATE"));
        }
    }

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    @Override
    protected BManager getManager() {
        return new AFTaxeCo2Manager();
    }
}
