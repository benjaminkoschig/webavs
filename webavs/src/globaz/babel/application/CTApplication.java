package globaz.babel.application;

import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BApplication;
import globaz.jade.log.JadeLogger;
import java.util.Properties;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class CTApplication extends BApplication {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ACTION_ANNEXES_COPIES = "babel.editableDocument.choixAnnexesCopies";
    public static final String ACTION_DEFAULT_ANNEXE = "babel.annexes.documentJointDefaultAnnexes";
    public static final String ACTION_DEFAULT_COPIE = "babel.copies.documentJointDefaultCopies";
    public static final String ACTION_DOCUMENTS = "babel.cat.document";
    public static final String ACTION_EDIT_PARAGRAPHES = "babel.editableDocument.editParagraphes";
    public static final String ACTION_PARAGRAPHES = "babel.editableDocument.choixParagraphes";
    public static final String ACTION_TEXTES = "babel.cat.texte";
    public static final String ACTION_TEXTES_SAISIE = "babel.cat.texteSaisie";

    public static final String APPLICATION_BABEL_REP = "babelRoot";
    public static final String APPLICATION_PREFIX = "CT";
    public static final String DEFAULT_APPLICATION_BABEL = "BABEL";

    private static final String ID_ROLE_ADMINISTRATEUR = "role.administrateur";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CTApplication.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public CTApplication() throws Exception {
        this(DEFAULT_APPLICATION_BABEL);
    }

    /**
     * Crée une nouvelle instance de la classe CTApplication.
     * 
     * @param id
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public CTApplication(String id) throws Exception {
        super(id);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     */
    @Override
    protected void _declareAPI() {
        // TODO Auto-generated method stub
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _initializeApplication() throws Exception {
        try {
            FWMenuCache.getInstance().addFile("BABELMenu.xml");
        } catch (Exception e) {
            JadeLogger.error(this, "BABELMenu.xml non résolu : " + e.toString());
        }
    }

    @Override
    protected void _initializeCustomActions() {
        FWAction.registerActionCustom(ACTION_DOCUMENTS + ".actionCopierDocument", FWSecureConstants.ADD);
        FWAction.registerActionCustom(ACTION_ANNEXES_COPIES + ".arreterGenererDocument", FWSecureConstants.ADD);
        FWAction.registerActionCustom(ACTION_ANNEXES_COPIES + ".genererDocument", FWSecureConstants.ADD);
        FWAction.registerActionCustom(ACTION_ANNEXES_COPIES + ".allerVersEcranPrecedent", FWSecureConstants.READ);
        FWAction.registerActionCustom(ACTION_ANNEXES_COPIES + ".afficherDocument", FWSecureConstants.READ);
        FWAction.registerActionCustom(ACTION_PARAGRAPHES + ".arreterGenererDocument", FWSecureConstants.ADD);
        FWAction.registerActionCustom(ACTION_PARAGRAPHES + ".actionNiveauPrecedant", FWSecureConstants.READ);
        FWAction.registerActionCustom(ACTION_PARAGRAPHES + ".actionNiveauSuivant", FWSecureConstants.READ);
        FWAction.registerActionCustom(ACTION_PARAGRAPHES + ".actionChangeSelection", FWSecureConstants.READ);
        FWAction.registerActionCustom(ACTION_PARAGRAPHES + ".allerVersEcranSuivant", FWSecureConstants.READ);
        FWAction.registerActionCustom(ACTION_PARAGRAPHES + ".allerVersEcranPrecedent", FWSecureConstants.READ);
        FWAction.registerActionCustom(ACTION_EDIT_PARAGRAPHES + ".arreterGenererDocument", FWSecureConstants.ADD);
        FWAction.registerActionCustom(ACTION_EDIT_PARAGRAPHES + ".allerVersEcranSuivant", FWSecureConstants.READ);
        FWAction.registerActionCustom(ACTION_EDIT_PARAGRAPHES + ".allerVersEcranPrecedent", FWSecureConstants.READ);
        FWAction.registerActionCustom(ACTION_DOCUMENTS + ".actionAjouter", FWSecureConstants.ADD);
    }

    /**
     * @param properties
     *            DOCUMENT ME!
     */
    protected void _readProperties(Properties properties) {
        // TODO Auto-generated method stub
    }

    /**
     * retourne l'identifiant du rôle représentant un administrateur du système
     * 
     * @return la valeur courante de l'attribut id role administrateur
     */
    public String getIdRoleAdministrateur() {
        return getProperty(ID_ROLE_ADMINISTRATEUR);
    }
}
