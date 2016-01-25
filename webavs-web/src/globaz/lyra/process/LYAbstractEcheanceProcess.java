package globaz.lyra.process;

import globaz.framework.util.FWLog;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.job.common.JadeJobAbortable;
import globaz.lyra.api.ILYEcheances;
import java.sql.SQLException;
import java.util.Arrays;
import ch.globaz.lyra.business.models.historique.LYSimpleHistorique;
import ch.globaz.lyra.business.services.LYHistoriqueEcheancesService;
import ch.globaz.lyra.business.services.LYServiceLocator;

public abstract class LYAbstractEcheanceProcess extends AbstractJadeJob implements JadeJobAbortable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String emailAddress;
    private String idEcheance;
    private String idLog;
    private String moisTraitement;

    public LYAbstractEcheanceProcess() {
        super();

        emailAddress = "";
        idEcheance = "";
        idLog = "";
        moisTraitement = "";

        setSession(BSessionUtil.getSessionFromThreadContext());
    }

    @Override
    public void abort() {
        try {
            sendCompletionMail(Arrays.asList(getEmailAddress()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void afterExecute() throws Exception {
    }

    protected abstract void beforeExecute() throws Exception;

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getIdEcheance() {
        return idEcheance;
    }

    public String getIdLog() {
        return idLog;
    }

    public String getMoisTraitement() {
        return moisTraitement;
    }

    /**
     * Défini l'application où la session doit être connectée pour pouvoir effectuer le job
     * 
     * @return un nom d'application valide, ou vide (l'application LYRA sera utilisée)
     */
    protected abstract String getSessionApplicationName();

    private final JadeContext initContext() throws Exception {

        if (!JadeStringUtil.isBlank(getSessionApplicationName())) {
            setSession(BSessionUtil.createSession(getSessionApplicationName(), getSession().getUserId()));
        }

        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(getSession().getApplicationId());
        ctxtImpl.setLanguage(getSession().getIdLangueISO());
        ctxtImpl.setUserEmail(getSession().getUserEMail());
        ctxtImpl.setUserId(getSession().getUserId());
        ctxtImpl.setUserName(getSession().getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(getSession().getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }

        return ctxtImpl;
    }

    @Override
    public void run() {
        try {
            startUsingContext();

            beforeExecute();
            runProcess();
            // this.updateHistorique();
            afterExecute();
        } catch (Exception ex) {
            getLogSession().error(this.getClass().getName(), ex.getMessage());
            abort();
        } finally {
            JadeThreadActivator.stopUsingContext(this);
        }
    }

    protected abstract void runProcess() throws Exception;

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setIdEcheance(String idEcheance) {
        this.idEcheance = idEcheance;
    }

    public void setIdLog(String idLog) {
        this.idLog = idLog;
    }

    public void setMoisTraitement(String moisTraiement) {
        moisTraitement = moisTraiement;
    }

    private void startUsingContext() throws SQLException, Exception {
        JadeThreadActivator.startUsingJdbcContext(this, initContext());
        JadeThread.storeTemporaryObject("bsession", getSession());
        JadeThread.storeTemporaryObject("executeJob", this);
    }

    /**
     * Méthode qui met à jour l'historique des échéances
     */
    private void updateHistorique() throws Exception {
        LYSimpleHistorique historique = new LYSimpleHistorique();
        historique.setIdEcheance(idEcheance);
        historique.setDateExecution(JACalendar.todayJJsMMsAAAA());
        historique.setVisaUtilisateur(getSession().getUserName());
        historique.setIdLog(idLog);

        FWLog log = new FWLog();
        log.setSession(getSession());
        log.setIdLog(idLog);
        log.retrieve();
        if (globaz.framework.util.FWMessage.INFORMATION.equals(log.getErrorLevel())) {
            historique.setCsEtat(ILYEcheances.CS_SUCCES);
        } else {
            historique.setCsEtat(ILYEcheances.CS_ERREUR);
        }

        LYHistoriqueEcheancesService historiqueEcheancesService = LYServiceLocator.getHistoriqueEcheancesService();
        historiqueEcheancesService.add(historique);
    }
}
