package globaz.hercule.db.traitement;

import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.hercule.db.CEAbstractViewBean;

public class CEAttributionRisqueViewBean extends CEAbstractViewBean {

    private String codeNoga = "";
    private String periodicite = "";

    /**
     * @return
     */
    public String getCodeNoga() {
        return codeNoga;
    }

    public FWParametersSystemCodeManager getCSNoga() {
        FWParametersSystemCodeManager mgr = new FWParametersSystemCodeManager();
        mgr.setSession(getSession());
        mgr.getListeCodes("VENOGAVAL", getSession().getIdLangue());

        return mgr;
    }

    /**
     * @return
     */
    public String getPeriodicite() {
        return periodicite;
    }

    /**
     * @param codeNoga
     */
    public void setCodeNoga(String codeNoga) {
        this.codeNoga = codeNoga;
    }

    /**
     * @param periodicite
     */
    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }
}
