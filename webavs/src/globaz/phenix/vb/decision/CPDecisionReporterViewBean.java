package globaz.phenix.vb.decision;

import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.vb.CPAbstractPersistentObjectViewBean;

public class CPDecisionReporterViewBean extends CPAbstractPersistentObjectViewBean {

    private String anneeATraiter = "";
    private String eMailAdress = "";
    private String idPassage = "";
    private String libellePassage = "";

    @Override
    public void add() throws Exception {
        // DO NOTHING, USED TO LAUNCH PROCESS
    }

    @Override
    public void delete() throws Exception {
        // DO NOTHING, USED TO LAUNCH PROCESS
    }

    public String getAnneeATraiter() {
        return anneeATraiter;
    }

    public String getEMailAddress() {
        if (JadeStringUtil.isBlankOrZero(eMailAdress)) {
            return getSession().getUserEMail();
        } else {
            return eMailAdress;
        }
    }

    public String getIdPassage() {
        return idPassage;
    }

    public String getLibellePassage() {
        return libellePassage;
    }

    @Override
    public void retrieve() throws Exception {
        // DO NOTHING, USED TO LAUNCH PROCESS
    }

    public void setAnneeATraiter(String anneeATraiter) {
        this.anneeATraiter = anneeATraiter;
    }

    public void setEMailAddress(String mailAddress) {
        eMailAdress = mailAddress;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    public void setLibellePassage(String libellePassage) {
        this.libellePassage = libellePassage;
    }

    @Override
    public void update() throws Exception {
        // DO NOTHING, USED TO LAUNCH PROCESS
    }

}
