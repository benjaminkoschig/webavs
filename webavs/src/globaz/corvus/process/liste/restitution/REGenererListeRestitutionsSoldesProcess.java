package globaz.corvus.process.liste.restitution;

import globaz.corvus.db.restitution.REMotifsRestitutionsSoldesManager;
import globaz.corvus.db.restitution.RERestitutionsSoldes;
import globaz.corvus.db.restitution.RERestitutionsSoldesManager;
import globaz.corvus.excel.REListeExcelRestitutionsSoldes;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import ch.globaz.corvus.process.attestationsfiscales.REGenererListeExcelRestitutionsSoldesProcess;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

public class REGenererListeRestitutionsSoldesProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateValeur;
    private String forRole;

    /**
	 * 
	 */
    public REGenererListeRestitutionsSoldesProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            JadeThreadActivator.startUsingJdbcContext(this, initContext(getSession()));

            // Restitutions
            RERestitutionsSoldesManager restitutionsManager = new RERestitutionsSoldesManager();
            restitutionsManager.setSession(getSession());
            restitutionsManager.setForRole(forRole);
            restitutionsManager.setDateValeur(dateValeur);
            restitutionsManager.setLangue(getSession().getIdLangue());
            restitutionsManager.find(BManager.SIZE_NOLIMIT);

            // Motifs de restitutions
            REMotifsRestitutionsSoldesManager motifsManager = new REMotifsRestitutionsSoldesManager();
            motifsManager.setForIdSectionIn(getForIdsSectionInForMotisContentieuxRequest(restitutionsManager));
            motifsManager.setForLangue(getSession().getIdLangue());
            motifsManager.find(BManager.SIZE_NOLIMIT);

            // Empeche l'envoi du mail du BProcess. Le mail est envoyé avec ExcelJob
            setSendCompletionMail(false);

            REGenererListeExcelRestitutionsSoldesProcess process = new REGenererListeExcelRestitutionsSoldesProcess(
                    new REListeExcelRestitutionsSoldes(getSession(), restitutionsManager, motifsManager),
                    getEMailAddress());

            process.setTransaction(getTransaction());
            process.setSession(getSession());
            process.run();

            return true;
        } finally {
            JadeThreadActivator.stopUsingContext(this);
        }
    }

    @SuppressWarnings("unchecked")
    private String getForIdsSectionInForMotisContentieuxRequest(RERestitutionsSoldesManager restitutionsManager) {

        return Joiner.on(",").join(
                Iterables.transform(restitutionsManager.getContainer(), new Function<RERestitutionsSoldes, String>() {
                    @Override
                    public String apply(RERestitutionsSoldes restit) {
                        return restit.getIdSection();
                    }
                }));
    }

    /**
     * @return the dateValeur
     */
    public String getDateValeur() {
        return dateValeur;
    }

    @Override
    protected String getEMailObject() {
        // not used
        return "";
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
     * @param dateValeur
     *            the dateValeur to set
     */
    public void setDateValeur(String dateValeur) {
        this.dateValeur = dateValeur;
    }

    /**
     * @param forRole
     *            the forRole to set
     */
    public void setForRole(String forRole) {
        this.forRole = forRole;
    }
}
