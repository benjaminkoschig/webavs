package globaz.phenix.db.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.application.CPApplication;
import globaz.phenix.process.listes.CPProcessListeCommunicationStatistique;

public class CPListeCommunicationStatistiqueViewBean extends CPProcessListeCommunicationStatistique implements
        BIPersistentObject, FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id = null;

    // Constructeur
    public CPListeCommunicationStatistiqueViewBean() throws Exception {
        super(new BSession(CPApplication.DEFAULT_APPLICATION_PHENIX));
    }

    public void _init() {
    }

    @Override
    protected void _validate() throws Exception {
        if (BSessionUtil.compareDateFirstGreater(getSession(), getFromAnnee(), getToAnnee())) {
            this._addError(getSession().getLabel("CP_MSG_0087"));
        }
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getSession().getLabel("CP_MSG_0145"));
        }
    }

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void retrieve() throws Exception {
        setFromAnnee(Integer.toString(JACalendar.today().getYear() - 7));
        setToAnnee(Integer.toString(JACalendar.today().getYear() - 2));
    }

    @Override
    public void setId(String newId) {
        id = newId;
    }

    @Override
    public void update() throws Exception {
    }
}