package globaz.osiris.db.ordres;

import globaz.framework.bean.FWViewBeanInterface;

public class CAOrdreGroupeViewBean extends CAOrdreGroupe implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * HumanReading CS from CsTransmissionStatutExec
     * 
     * @return
     */
    public String getHRIsoCsTransmissionStatutExec() {
        return getSession().getCodeLibelle(getIsoCsTransmissionStatutExec());
    }

    public String getHRIsoCsOrdreStatutExec() {
        return getSession().getCodeLibelle(getIsoCsOrdreStatutExec());
    }

}