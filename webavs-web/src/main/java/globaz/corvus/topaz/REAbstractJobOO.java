package globaz.corvus.topaz;

import globaz.babel.utils.BabelContainer;
import globaz.babel.utils.CatalogueText;
import globaz.corvus.application.REApplication;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.globall.db.BSessionUtil;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.smtp.JadeSmtpClient;
import java.sql.SQLException;
import java.util.List;

public abstract class REAbstractJobOO extends AbstractJadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresseEmail;
    private BabelContainer babelContainer;
    private boolean initThreadContext;

    public REAbstractJobOO(boolean initThreadContext) {
        super();

        babelContainer = new BabelContainer();
        this.initThreadContext = initThreadContext;
    }

    protected abstract List<CatalogueText> definirCataloguesDeTextes();

    protected abstract void genererDocument() throws Exception;

    public final String getAdresseEmail() {
        return adresseEmail;
    }

    public final BabelContainer getBabelContainer() {
        return babelContainer;
    }

    protected final String getTexte(CatalogueText catalogue, int niveau, int position) {
        try {
            return babelContainer.getTexte(catalogue, niveau, position);
        } catch (Exception ex) {
            getLogSession().error(getName(), ex.toString());
            throw new RETechnicalException(ex);
        }
    }

    private final JadeContext initContext() throws Exception {

        setSession(BSessionUtil.createSession(REApplication.DEFAULT_APPLICATION_CORVUS, getSession().getUserId()));

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
    public final void run() {
        try {
            if (initThreadContext) {
                startUsingContext();
            }

            if ((definirCataloguesDeTextes() != null) && (definirCataloguesDeTextes().size() > 0)) {
                for (CatalogueText unCatalogue : definirCataloguesDeTextes()) {
                    babelContainer.addCatalogueText(unCatalogue);
                }
                babelContainer.setSession(getSession());
                babelContainer.load();
            }

            genererDocument();
        } catch (Exception ex) {
            getLogSession().error(getName(), ex.toString());
            throw new RETechnicalException(ex);
        } finally {

            if (initThreadContext) {
                JadeThreadActivator.stopUsingContext(this);
            }

            if (getLogSession().hasMessages()) {
                try {
                    StringBuilder body = new StringBuilder();
                    for (JadeBusinessMessage unMessage : getLogSession().getMessages()) {
                        body.append(unMessage.getMessageId()).append("\n");
                    }
                    JadeSmtpClient.getInstance().sendMail(getAdresseEmail(), getDescription(), body.toString(), null);
                } catch (Exception ex) {
                    throw new RETechnicalException(ex);
                }
            }
        }
    }

    public final void setAdresseEmail(String adresseEmail) {
        this.adresseEmail = adresseEmail;
    }

    private void startUsingContext() throws SQLException, Exception {
        JadeThreadActivator.startUsingJdbcContext(this, initContext());
        JadeThread.storeTemporaryObject("bsession", getSession());
        JadeThread.storeTemporaryObject("executeJob", this);
    }
}
