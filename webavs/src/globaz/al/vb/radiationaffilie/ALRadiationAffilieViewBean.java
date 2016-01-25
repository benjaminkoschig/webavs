package globaz.al.vb.radiationaffilie;

import globaz.al.process.radiationaffilie.ALRadiationAffilieProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import java.util.Date;

public class ALRadiationAffilieViewBean extends BJadePersistentObjectViewBean {

    private String affilieDestinataire = "";
    private String affilieOrigine = "";
    private String dateDebutActivite = "";
    private String dateImpression = "";
    private String dateRadiation = "";
    private String email = "";
    private boolean printDecisions = false;
    private String reference = "";

    @Override
    public void add() throws Exception {

        if (JadeStringUtil.isBlankOrZero(email)) {
            JadeThread.logError(this.getClass().getName(),
                    "globaz.al.vb.radiationaffilie.ALRadiationAffilieViewBean.email");
        }

        if (JadeStringUtil.isBlankOrZero(getAffilieOrigine())) {
            JadeThread.logError(this.getClass().getName(),
                    "globaz.al.vb.radiationaffilie.ALRadiationAffilieViewBean.affilieOrigine");
        }

        if (!JadeDateUtil.isGlobazDate(getDateRadiation())) {
            JadeThread.logError(this.getClass().getName(),
                    "globaz.al.vb.radiationaffilie.ALRadiationAffilieViewBean.dateRadiation");
        }

        if (!JadeStringUtil.isBlankOrZero(getAffilieDestinataire())
                && !JadeDateUtil.isGlobazDate(getDateDebutActivite())) {
            JadeThread.logError(this.getClass().getName(),
                    "globaz.al.vb.radiationaffilie.ALRadiationAffilieViewBean.dateDebutActivite");
        }

        if (getPrintDecisions() && !JadeDateUtil.isGlobazDate(dateImpression)) {
            JadeThread.logError(this.getClass().getName(),
                    "globaz.al.vb.radiationaffilie.ALRadiationAffilieViewBean.dateImpression");
        }

        if (JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR) == null) {

            ALRadiationAffilieProcess process = new ALRadiationAffilieProcess();
            process.setAffilieDestinataire(getAffilieDestinataire());
            process.setAffilieOrigine(getAffilieOrigine());
            process.setDateDebutActivite(getDateDebutActivite());
            process.setDateImpression(getDateImpression());
            process.setDateRadiation(getDateRadiation());
            process.setEmail(getEmail());
            process.setPrintDecisions(getPrintDecisions());
            process.setReference(getReference());
            process.setSession(getSession());
            BProcessLauncher.start(process, false);
        }
    }

    @Override
    public void delete() throws Exception {
        // NOT IMPLEMENTED
    }

    public String getAffilieDestinataire() {
        return affilieDestinataire;
    }

    public String getAffilieOrigine() {
        return affilieOrigine;
    }

    public String getDateDebutActivite() {
        return dateDebutActivite;
    }

    public String getDateImpression() {
        return JadeStringUtil.isBlank(dateImpression) ? JadeDateUtil.getGlobazFormattedDate(new Date())
                : dateImpression;
    }

    public String getDateRadiation() {
        return dateRadiation;
    }

    public String getEmail() {
        return JadeStringUtil.isEmpty(email) ? JadeThread.currentUserEmail() : email;
    }

    @Override
    public String getId() {
        // NOT IMPLEMENTED
        return null;
    }

    public boolean getPrintDecisions() {
        return printDecisions;
    }

    public String getReference() {
        return reference;
    }

    /**
     * @return session actuelle
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        // NOT IMPLEMENTED
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        // NOT IMPLEMENTED
    }

    public void setAffilieDestinataire(String affilieDestinataire) {
        this.affilieDestinataire = affilieDestinataire;
    }

    public void setAffilieOrigine(String affilieOrigine) {
        this.affilieOrigine = affilieOrigine;
    }

    public void setDateDebutActivite(String dateDebutActivite) {
        this.dateDebutActivite = dateDebutActivite;
    }

    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    public void setDateRadiation(String dateRadiation) {
        this.dateRadiation = dateRadiation;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setId(String newId) {
        // NOT IMPLEMENTED
    }

    public void setPrintDecisions(boolean printDecisions) {
        this.printDecisions = printDecisions;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public void update() throws Exception {
        // NOT IMPLEMENTED
    }

}
