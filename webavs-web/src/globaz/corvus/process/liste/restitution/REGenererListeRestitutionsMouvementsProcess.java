package globaz.corvus.process.liste.restitution;

import globaz.corvus.db.restitution.RERestitutionsMouvementsManager;
import globaz.corvus.excel.REListeExcelRestitutionsMouvements;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import ch.globaz.corvus.process.attestationsfiscales.REGenererListeExcelRestitutionsMouvementsProcess;

public class REGenererListeRestitutionsMouvementsProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateA;
    private String dateDe;
    private String forIdExterneRubrique;
    private String forRole;

    /**
	 * 
	 */
    public REGenererListeRestitutionsMouvementsProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            JadeThreadActivator.startUsingJdbcContext(this, initContext(getSession()));

            RERestitutionsMouvementsManager manager = new RERestitutionsMouvementsManager();
            manager.setSession(getSession());
            manager.setForRole(forRole);
            manager.setDateA((new JADate(dateA)).toStrAMJ());
            manager.setDateDe((new JADate(dateDe)).toStrAMJ());
            manager.setForIdExterneRubrique(forIdExterneRubrique);
            manager.setLangue(getSession().getIdLangueISO());
            manager.find(BManager.SIZE_NOLIMIT);

            // Empeche l'envoi du mail du BProcess. Le mail est envoyé avec ExcelJob
            setSendCompletionMail(false);

            REGenererListeExcelRestitutionsMouvementsProcess process = new REGenererListeExcelRestitutionsMouvementsProcess(
                    new REListeExcelRestitutionsMouvements(getSession(), manager), getEMailAddress());

            process.setTransaction(getTransaction());
            process.setSession(getSession());
            process.run();

            return true;
        } finally {
            JadeThreadActivator.stopUsingContext(this);
        }
    }

    /**
     * @return the dateA
     */
    public String getDateA() {
        return dateA;
    }

    /**
     * @return the dateDe
     */
    public String getDateDe() {
        return dateDe;
    }

    @Override
    protected String getEMailObject() {
        // not used
        return "";
    }

    /**
     * @return the forIdRubrique
     */
    public String getForIdExterneRubrique() {
        return forIdExterneRubrique;
    }

    /**
     * @return the forRole
     */
    public String getForRole() {
        return forRole;
    }

    /**
     * Initialisation du context du thread
     * 
     * @param session
     * @return
     * @throws Exception
     */
    private final JadeContext initContext(BSession session) throws Exception {
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        return ctxtImpl;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * @param dateA
     *            the dateA to set
     */
    public void setDateA(String dateA) {
        this.dateA = dateA;
    }

    /**
     * @param dateDe
     *            the dateDe to set
     */
    public void setDateDe(String dateDe) {
        this.dateDe = dateDe;
    }

    /**
     * @param forIdRubrique
     *            the forIdRubrique to set
     */
    public void setForIdExterneRubrique(String forIdRubrique) {
        forIdExterneRubrique = forIdRubrique;
    }

    /**
     * @param forRole
     *            the forRole to set
     */
    public void setForRole(String forRole) {
        this.forRole = forRole;
    }
}
