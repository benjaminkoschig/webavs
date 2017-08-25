package globaz.hercule.application;

import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BApplication;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.webavs.common.CommonProperties;

/**
 * @author MKA
 * @since 21 mai 2010
 */
public class CEApplication extends BApplication {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Prefix de l'application */
    public static final String APPLICATION_HERCULE_PREFIX = "CE";
    /** Racine de l'application */
    public static final String APPLICATION_HERCULE_ROOT = "herculeRoot";
    /** Id de l'application */
    public static final String DEFAULT_APPLICATION_HERCULE = "HERCULE";

    /** Id de l'application affiliation naos */
    public static final String DEFAULT_APPLICATION_NAOS = "NAOS";
    /** Répertoire des models */
    public static final String MODELS_EXCELML_HERCULES = "model/excelml";
    // Properties
    private static final String NBRE_MAX_DB = "nbreMaxDB";
    private static final int NBRE_MAX_DB_DEFAUT = 1000;

    /**
     * Renvoie une instance de l'application enregistrée dans le système
     * 
     * @return l'instance de l'application, év. null
     */
    public final static CEApplication getApplicationAquila() {
        try {
            return (CEApplication) GlobazServer.getCurrentSystem().getApplication(
                    CEApplication.DEFAULT_APPLICATION_HERCULE);
        } catch (Exception e) {
            throw new Error(e.toString());
        }
    }

    // le format du numéro d'affilié
    private IFormatData affileFormater = null;
    private BIApplication appNaos = null;

    private BISession sessionNaos = null;

    /**
     * Constructeur de CEApplication
     */
    public CEApplication() throws Exception {
        super(CEApplication.DEFAULT_APPLICATION_HERCULE);
    }

    /**
     * Constructeur de CEApplication
     */
    public CEApplication(String id) throws Exception {
        super(id);
    }

    @Override
    protected void _declareAPI() {
        // TODO compléter
    }

    /**
     * @see globaz.globall.db.BApplication#_initializeApplication()
     */
    @Override
    protected void _initializeApplication() throws Exception {
        FWMenuCache cache = FWMenuCache.getInstance();
        cache.addFile("HERCULEmenu.xml");
    }

    /**
     * @see globaz.globall.db.BApplication#_initializeCustomActions()
     */
    @Override
    protected void _initializeCustomActions() {
        FWAction.registerActionCustom("hercule.controleEmployeur.imprimerControle", FWSecureConstants.READ);
        FWAction.registerActionCustom("hercule.controleEmployeur.imprimerlettrelibre", FWSecureConstants.READ);
        FWAction.registerActionCustom("hercule.controleEmployeur.saisieRapideReviseur", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("hercule.controleEmployeur.saisieRapideReviseur.ouvrir", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("hercule.controleEmployeur.saisieRapideReviseur.afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("hercule.controleEmployeur.lettreProchainControle", FWSecureConstants.READ);
        FWAction.registerActionCustom("hercule.controleEmployeur.controleEmployeur.imprimerControle",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("hercule.controleEmployeur.controleEmployeur.imprimerlettrelibre",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("hercule.controleEmployeur.controleEmployeur.lettreProchainControle",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("hercule.controleEmployeur.attributionPts.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("hercule.controleEmployeur.attributionPts.enregistrer", FWSecureConstants.UPDATE);

        // Traitements
        FWAction.registerActionCustom("hercule.traitement.rattrapageAnnuel.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("hercule.traitement.rattrapageGlobal.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("hercule.traitement.reinjection.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("hercule.traitement.rattrapageNum.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("hercule.traitement.attributionRisque.afficher", FWSecureConstants.UPDATE);

        // Saisie Rapide
        FWAction.registerActionCustom("hercule.controleEmployeur.saisieMasseReviseur.afficher",
                FWSecureConstants.UPDATE);

        // Mettre a blanc les résiveurs
        FWAction.registerActionCustom("hercule.reviseur.reviseurABlanc.afficher", FWSecureConstants.UPDATE);

        // Déclarations structurées
        FWAction.registerActionCustom("hercule.declarationStructuree.generationSuivi.afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("hercule.declarationStructuree.saisieMasseReception.afficher",
                FWSecureConstants.UPDATE);

        // Ajout ou modification d'un groupe
        FWAction.registerActionCustom("hercule.groupement.groupe.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("hercule.groupement.membre.afficher", FWSecureConstants.READ);

        // Non certifié conforme
        FWAction.registerActionCustom("hercule.noncertifiesconformes.generationSuiviNCC.afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("hercule.noncertifiesconformes.generationSuiviNCC.afficher",
                FWSecureConstants.UPDATE);
    }

    /**
     * Method getAffileFormater.
     * 
     * @return IFormatData
     * @throws Exception
     */
    public IFormatData getAffileFormater() throws Exception {

        if (affileFormater == null) {
            String className = this.getProperty(CommonProperties.KEY_FORMAT_NUM_AFFILIE);
            if (!JadeStringUtil.isBlank(className)) {
                affileFormater = (IFormatData) Class.forName(className).newInstance();
            }
        }
        return affileFormater;
    }

    /**
     * Renvoie une instance de l'application NOAS
     * 
     * @return Une instance de l'application de naos
     */
    public BIApplication getApplicationNaos() {
        // Si application pas ouverte
        if (appNaos == null) {
            try {
                appNaos = GlobazSystem.getApplication("NAOS");
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return appNaos;
    }

    /**
     * Renvoie une session de NOAS
     * 
     * @param session
     * @return
     */
    public BISession getSessionNaos(BISession session) {
        // Si session pas ouverte
        if (sessionNaos == null) {
            try {
                sessionNaos = getApplicationNaos().newSession(session);
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return sessionNaos;
    }

    public boolean hasLanguageInPagesPath() {
        return false;
    }

    public int nombreMaxListeControleAEffectuerSupporteParDB() {
        try {
            return Integer.valueOf(this.getProperty((CEApplication.NBRE_MAX_DB))).intValue();
        } catch (Exception e) {
            return CEApplication.NBRE_MAX_DB_DEFAUT;
        }

    }
}
