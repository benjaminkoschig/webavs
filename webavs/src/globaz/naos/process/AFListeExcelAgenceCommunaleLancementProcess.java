package globaz.naos.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;

public class AFListeExcelAgenceCommunaleLancementProcess extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String date = JACalendar.todayJJsMMsAAAA();
    private java.lang.String email = new String();
    private java.lang.String idTiersAgence = new String();
    private Boolean wantCsv = new Boolean(Boolean.FALSE);

    // Constructeur
    public AFListeExcelAgenceCommunaleLancementProcess() {
        super();
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception, Exception {

        try {
            AFListeExcelAgenceCommunaleProcess process = new AFListeExcelAgenceCommunaleProcess();
            process.setSession(getSession());
            process.setForDate(getDate());
            process.setEMailAddress(getEmail());
            process.setWantCsv(getWantCsv());
            process.setParentWithCopy(this);
            if (!JadeStringUtil.isBlankOrZero(getIdTiersAgence())) {
                process.setForIdTiersAgence(getIdTiersAgence());
                BProcessLauncher.start(process);
            } else {
                TIAdministrationManager agenceManager = new TIAdministrationManager();
                agenceManager.setSession(getSession());
                agenceManager.setForGenreAdministration(CodeSystem.GENRE_ADMIN_AGENCE_COMMUNALE);
                agenceManager.orderByCodeAdministration();
                agenceManager.find(BManager.SIZE_NOLIMIT);
                if (agenceManager.size() > 0) {
                    for (int i = 0; i < agenceManager.size(); i++) {
                        process.setSession(getSession());
                        process.setForIdTiersAgence(((TIAdministrationViewBean) agenceManager.getEntity(i))
                                .getIdTiersAdministration());
                        BProcessLauncher.start(process);
                        // process.executeProcess();
                    }
                }
                process.setForIdTiersAgenceVide(new Boolean(true));
                process.setForIdTiersAgence("");
                // process.executeProcess();
                BProcessLauncher.start(process);

            }

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
        return false;
    }

    public java.lang.String getDate() {
        return date;
    }

    public java.lang.String getEmail() {
        return email;
    }

    @Override
    protected String getEMailObject() {
        // TODO Auto-generated method stub
        return null;
    }

    public java.lang.String getIdTiersAgence() {
        return idTiersAgence;
    }

    public Boolean getWantCsv() {
        return wantCsv;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setDate(java.lang.String date) {
        this.date = date;
    }

    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    public void setIdTiersAgence(java.lang.String idTiersAgence) {
        this.idTiersAgence = idTiersAgence;
    }

    public void setWantCsv(Boolean wantCsv) {
        this.wantCsv = wantCsv;
    }

}
