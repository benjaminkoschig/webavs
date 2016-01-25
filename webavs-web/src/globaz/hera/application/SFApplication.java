package globaz.hera.application;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BApplication;
import globaz.globall.db.BSession;
import globaz.globall.shared.GlobazValueObject;
import globaz.globall.util.JAStringFormatter;
import globaz.hera.servlet.ISFActions;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.pavo.api.ICICompteIndividuel;
import globaz.pavo.api.helper.ICICompteIndividuelHelper;
import globaz.pyxis.api.ITIPersonneAvs;
import globaz.pyxis.api.helper.ITIPersonneAvsHelper;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;
import javax.servlet.http.HttpSession;

/**
 * Descpription
 * 
 * @author scr Date de création 30 août 05
 */
public class SFApplication extends BApplication {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Le préfixe de l'application */
    public static final String APPLICATION_PREFIX = "SF";

    /** Le répertoire racine de l'application */
    public static final String APPLICATION_SF_REP = "heraRoot";

    public final static String DEFAULT_APPLICATION_NAOS = "NAOS";

    /** Le nom de l'application */
    public static final String DEFAULT_APPLICATION_SF = "HERA";

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String VERSION = "SF_1-0-001";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param string
     * @param string2
     * @return
     */
    private static String buildOptions(String noAVS, String nom) {
        StringBuffer buff = new StringBuffer();
        noAVS = JadeStringUtil.change(noAVS, ".", "");
        /**/
        buff.append("<option value='");
        buff.append(JAStringFormatter.formatAVS(noAVS));
        /**/
        buff.append("' nom=\"");
        buff.append(nom);
        buff.append("\"");
        /**/
        buff.append(" noavs=\"");
        buff.append(noAVS);
        buff.append("\"");
        /**/
        buff.append(">");
        buff.append(JAStringFormatter.formatAVS(noAVS));
        buff.append(" - ");
        buff.append(nom);
        buff.append("</option>\n");

        return buff.toString();
    }

    private static TreeSet goTiersCI(String like, BSession session) {
        TreeSet set = searchTiers(like, session);
        set.addAll(searchCI(like, session));
        return set;
    }

    public static TreeSet searchCI(String like, BSession session) {
        TreeSet set = new TreeSet();
        try {
            // globaz.pavo.db.compte.CICompteIndividuelManager mgr = new
            // globaz.pavo.db.compte.CICompteIndividuelManager();
            // mgr.setSession(bsession);
            // mgr.setQuickSearch(true);
            ICICompteIndividuel cis = (ICICompteIndividuel) session.getAPIFor(ICICompteIndividuel.class);
            Hashtable criteres = new Hashtable();
            criteres.put("setFromNumeroAvs", like);

            Object[] obj = cis.find(criteres);
            for (int i = 0; i < obj.length; i++) {
                ICICompteIndividuelHelper ci = new ICICompteIndividuelHelper((GlobazValueObject) obj[i]);
                set.add(buildOptions(ci.getNumeroAvs(), ci.getNomPrenom()));
            }

        } catch (Exception ex) {
            JadeLogger.warn("SFApplication.searchCI(like,session)", ex);
        }
        // System.out.println("after build select: "+(System.currentTimeMillis()-time)+"ms");
        return set;
    }

    private static TreeSet searchTiers(String like, BSession session) {
        TreeSet set = new TreeSet();
        try {
            ITIPersonneAvs tiersAvs = (ITIPersonneAvs) session.getAPIFor(ITIPersonneAvs.class);
            Hashtable ht = new Hashtable();
            ht.put("setFromNumAvsActuel", like);
            // ht.put("setForNumAvs", like);
            Object[] tiersObj = tiersAvs.find(ht);
            for (int i = 0; i < tiersObj.length; i++) {
                ITIPersonneAvsHelper tiers = new ITIPersonneAvsHelper((GlobazValueObject) tiersObj[i]);
                set.add(buildOptions(tiers.getNumAvsActuel(), tiers.getNom()));
            }

            // setForNumAvsActuelLike

        } catch (Exception e) {
            JadeLogger.warn("SFApplication.searchTiers(like,session)", e);
        }

        return set;
    }

    public static String searchTiersCI(String like, BSession session) {
        TreeSet set = goTiersCI(like, session);
        StringBuffer buff = new StringBuffer();
        for (Iterator iter = set.iterator(); iter.hasNext();) {
            buff.append((String) iter.next());
        }

        return buff.toString();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param like
     * @param hSession
     * @return
     */
    public static String searchTiersCI(String like, HttpSession hSession) {
        return searchTiersCI(like, (BSession) ((FWController) hSession.getAttribute("objController")).getSession());
    }

    /**
     * Crée une nouvelle instance de la classe SFApplication.
     * 
     * @throws Exception
     */
    public SFApplication() throws Exception {
        super(DEFAULT_APPLICATION_SF);
    }

    /**
     * Crée une nouvelle instance de la classe SFApplication.
     * 
     * @param id
     * 
     * @throws Exception
     */
    public SFApplication(String id) throws Exception {
        super(id);
    }

    /**
     * @see globaz.globall.db.BApplication#_declareAPI()
     */
    @Override
    protected void _declareAPI() {
    }

    /**
     * DOCUMENT ME!
     * 
     * @see globaz.globall.db.BApplication#_initializeApplication()
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _initializeApplication() throws Exception {
        FWMenuCache cache = FWMenuCache.getInstance();
        cache.addFile("HERAMenu.xml");
    }

    @Override
    protected void _initializeCustomActions() {
        FWAction.registerActionCustom(ISFActions.ACTION_RELATION_FAMILIALE_REQUERANT + ".ajouterRequerant",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(ISFActions.ACTION_RELATION_FAMILIALE_REQUERANT + ".ajouterConjointRequerant",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(ISFActions.ACTION_RELATION_FAMILIALE_REQUERANT + ".selectionnerRequerant",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(ISFActions.ACTION_RELATION_FAMILIALE_REQUERANT + ".modifierMembre",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(ISFActions.ACTION_RELATION_FAMILIALE_REQUERANT + ".quitterApplication",
                FWSecureConstants.READ);
        // Cette action créé le requérant si inexistant !!!
        FWAction.registerActionCustom(ISFActions.ACTION_RELATION_FAMILIALE_REQUERANT + ".entrerApplication",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(ISFActions.ACTION_RELATION_FAMILIALE_REQUERANT + ".changerDomaineRequerant",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(ISFActions.ACTION_RELATION_FAMILIALE_REQUERANT + ".changerDomaineMF",
                FWSecureConstants.READ);

        FWAction.registerActionCustom(ISFActions.ACTION_RELATION_FAMILIALE_REQUERANT + ".afficher",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(ISFActions.ACTION_RELATION_FAMILIALE_REQUERANT + ".modifierMembre",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(ISFActions.ACTION_VUE_GLOBALE + ".actionAfficherDossierGed",
                FWSecureConstants.READ);

        FWAction.registerActionCustom(ISFActions.ACTION_APERCU_RELATION_CONJOINT + ".ajouterConjointInconnu",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(ISFActions.ACTION_APERCU_RELATION_CONJOINT + ".ajouterRelation",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(ISFActions.ACTION_APERCU_RELATION_CONJOINT + ".nouvelleRelation",
                FWSecureConstants.ADD);
        // Cette action créé le requérant si inexistant !!!
        FWAction.registerActionCustom(ISFActions.ACTION_APERCU_RELATION_CONJOINT + ".entrerApplication",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(ISFActions.ACTION_APERCU_RELATION_CONJOINT + ".rechercherSituation",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(ISFActions.ACTION_APERCU_RELATION_CONJOINT + ".afficher", FWSecureConstants.ADD);

        FWAction.registerActionCustom(ISFActions.ACTION_VUE_GLOBALE + ".afficherFamilleRequerant",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(ISFActions.ACTION_VUE_GLOBALE + ".afficherFamilleMembre", FWSecureConstants.READ);
        FWAction.registerActionCustom(ISFActions.ACTION_VUE_GLOBALE + ".afficherPeriodesVGRequerant",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(ISFActions.ACTION_VUE_GLOBALE + ".afficherPeriodesVGMembreFamille",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(ISFActions.ACTION_VUE_GLOBALE + ".initialiserProvenance",
                FWSecureConstants.UPDATE);

        // Que du standard !!!! HIIIHAAAAAAAA
        // FWAction.registerActionCustom(ISFActions.ACTION_PERIODE,
        // FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(ISFActions.ACTION_ENFANTS + ".ajouterEnfant", FWSecureConstants.ADD);
        FWAction.registerActionCustom(ISFActions.ACTION_ENFANTS + ".supprimerEnfant", FWSecureConstants.REMOVE);
    }

    /**
     * DOCUMENT ME!
     * 
     * @see globaz.globall.db.BApplication#_readProperties(java.util.Properties)
     * 
     * @param properties
     *            DOCUMENT ME!
     */
    protected void _readProperties(Properties properties) {
    }

}
