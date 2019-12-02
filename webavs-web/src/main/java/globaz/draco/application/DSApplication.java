package globaz.draco.application;

import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.api.BIApplication;
import globaz.globall.api.GlobazSystem;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.webavs.common.CommonProperties;

/**
 * Application DRACO
 *
 * @author Sébastien Chappatte
 */
public class DSApplication extends globaz.globall.db.BApplication {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public final static String AF = "af";
    public final static String AFPRES = "afPres";
    public final static String AMAT = "amat";
    public final static String ANNEE_EXERCICE_COMPLET = "anneeDonneesComptablesCompletes";
    public final static String BLOQUAGE_13 = "csBloquageDecompte13";
    public final static String CANTON = "canton";
    public final static String CANTON_DEFAUT = "cantonDefautPreimpression";
    public final static String CATEGORIE_PERSONNE = "categoriePersonne";
    public final static String CCVD_AGRIVIT = "CCVD_AGRIVIT";
    public final static String CI_CODE_BLOQUAGE = "ciCodeBloquage";
    public final static String CODE_CANTON = "default.canton.caisse.location";
    public final static String CS_DOMAINE_DECLARATION_SALAIRES = "519008";
    public final static String CS_LONGUEUR_RECEPTION = "longueurChampsReception";
    public final static String DATE_MISE_EN_PROD = "dateMiseEnprod";
    public final static String DEFAULT_APPLICATION_DRACO = "DRACO";
    public final static String DEFAULT_APPLICATION_ROOT = "dracoRoot";
    public final static String FFPP = "ffpp";
    private static final String GESTION_LPP_DANS_VALIDATION_DS = "gestionLppDansValidationDS";
    public final static String HEADER_GRIS = "headerGris";
    public final static String IS_FORCE_SUIVI_ATTEST = "forceSuAt";
    public final static String LPP = "lpp";
    public final static String MODEL_DS = "modelDS";
    public final static String MODEL_DS_SPECIFIQUE = "modelDSSpecifique";
    public final static String NO_CAISSE = "noCaisse";
    public final static String NOUVEAU_CONTROLE_EMPLOYEUR = "nouveautControleEmployeur";
    public final static String SAISONNIER = "saisonnier";
    public final static String TEXTE_CATEGORIE_PERSONNEL = "texteCategoriePersonnel";
    public final static String TEXTE_CATEGORIE_PERSONNEL_DE = "texteCategoriePersonnelDe";
    public final static String TEXTE_CATEGORIE_PERSONNEL_IT = "texteCategoriePersonnelIt";
    public final static String TRI_AGENCE_COMMUNALE = "wantTriAgenceCom";
    public final static String VALIDATION_SPY_COMPLEMENT = "validationSpyComplement";
    public final static String EXCLURE_IMPORT_LIGNE_A_ZERO = "exclure.importation.ligne.a.zero";
    public final static String COMPLEMENT_USER_VALIDATION_DS = "ValideDS";
    private IFormatData affileFormater = null;
    private BIApplication appMusca = null;
    private BIApplication appNaos = null;
    private BIApplication appOsiris = null;

    /**
     * Initialise l'application
     *
     * @exception java.lang.Exception
     *                si l'initialisation de l'application a échouée
     */
    public DSApplication() throws Exception {
        super(DSApplication.DEFAULT_APPLICATION_DRACO);
    }

    /**
     * Constructeur du type DSApplication.
     *
     * @param id
     *            l'id de l'application
     * @exception java.lang.Exception
     *                si l'initialisation de l'application a échouée
     */
    public DSApplication(String id) throws Exception {
        super(id);
    }

    /**
     * Déclare les APIs de l'application
     */
    @Override
    protected void _declareAPI() {
    }

    /**
     * Initialise l'application
     *
     * @exception java.lang.Exception
     *                si l'initialisation de l'application a échouée
     */
    @Override
    protected void _initializeApplication() throws Exception {
        try {
            FWMenuCache.getInstance().addFile("DRACOMenu.xml");
        } catch (Exception e) {
            JadeLogger.error(this, "DRACOMenu.xml non résolu : " + e.toString());
        }
    }

    @Override
    protected void _initializeCustomActions() {
        FWAction.registerActionCustom("draco.inscriptions.inscriptionsIndividuellesListe.suivantPerso",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("draco.inscriptions.inscriptionsIndividuellesListe.precedantPerso",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("draco.inscriptions.inscriptionsIndividuellesListe.ajouter15",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("draco.inscriptions.inscriptionsIndividuellesListe.reAfficherPerso",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("draco.preimpression.preImpressionDeclaration.preImprimer",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("draco.declaration.saisieMasseAutomatique.afficherApresAnnulation",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("draco.declaration.saisieMasseDateReception.afficherApresAnnulation",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("draco.declaration.saisieMasseTotaux.afficherApresAnnulation",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("draco.declaration.annulerValidation.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("draco.declaration.attestationFiscaleLtnGen.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("draco.declaration.decompteImpotLtn.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("draco.declaration.validation.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("draco.inscriptions.prerempliDeclaration.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("draco.inscriptions.valideMontantDeclarationProcess.afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("draco.preimpression.preImpression.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("draco.declaration.attestationFiscaleLtn.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("draco.declaration.saisieMasseDateReception.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("draco.declaration.saisieMasseAutomatique.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("draco.inscriptions.inscriptionsIndividuellesListe.ajouter15",
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
                // affileFormater = new CFNumAffilie();
            }
        }
        return affileFormater;
    }

    public String getAnneePlausiDecompte18() {
        return this.getProperty(DSApplication.ANNEE_EXERCICE_COMPLET, "0");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.02.2003 18:41:34)
     *
     * @return globaz.globall.api.BIApplication
     */
    public BIApplication getApplicationMusca() {
        // Si application pas ouverte
        if (appMusca == null) {
            try {
                appMusca = GlobazSystem.getApplication("MUSCA");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return appMusca;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.02.2003 18:41:34)
     *
     * @return globaz.globall.api.BIApplication
     */
    public BIApplication getApplicationNaos() {
        // Si application pas ouverte
        if (appNaos == null) {
            try {
                appNaos = GlobazSystem.getApplication("NAOS");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return appNaos;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.02.2003 18:41:34)
     *
     * @return globaz.globall.api.BIApplication
     */
    public BIApplication getApplicationOsiris() {
        // Si application pas ouverte
        if (appOsiris == null) {
            try {
                appOsiris = GlobazSystem.getApplication("OSIRIS");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return appOsiris;
    }

    public int getAutoDigitAffilie() {
        String temp = this.getProperty(CommonProperties.KEY_AUTO_DIGIT_AFF).trim();
        return Integer.valueOf(temp).intValue();

        // return getProperty(AUTODIGITAFF);
    }

    public String getCaisse() {
        return this.getProperty(DSApplication.NO_CAISSE).trim();
    }

    public String getCantonDefaut() {
        return this.getProperty(DSApplication.CANTON_DEFAUT, "");
    }

    public String getCodeCantonAF() {
        return this.getProperty(DSApplication.CODE_CANTON).trim();
    }

    public String getDateMiseEnProd() {
        return this.getProperty(DSApplication.DATE_MISE_EN_PROD).trim();
    }

    public String getHeaderGris() {
        return this.getProperty(DSApplication.HEADER_GRIS, "");
    }

    public String getListeCSBloquage13() {
        return this.getProperty(DSApplication.BLOQUAGE_13, "");
    }

    public int getLongueurReception() {
        return Integer.valueOf(this.getProperty(DSApplication.CS_LONGUEUR_RECEPTION, "18")).intValue();
    }

    public String getModelDS() {
        return this.getProperty(DSApplication.MODEL_DS);
    }

    public String getModelDSSpecifique() {
        return this.getProperty(DSApplication.MODEL_DS_SPECIFIQUE);
    }

    public int getTailleChampsAffilie() {
        String temp = this.getProperty(CommonProperties.KEY_TAILLE_CHAMPS_AFF);
        return Integer.valueOf(temp).intValue();
    }

    public String getTexteCategoriePersonnel(String langue) {
        if (!JadeStringUtil.isEmpty(this.getProperty(DSApplication.TEXTE_CATEGORIE_PERSONNEL))) {
            if ((langue != null) && langue.equals("de")
                    && !JadeStringUtil.isEmpty(this.getProperty(DSApplication.TEXTE_CATEGORIE_PERSONNEL_DE))) {
                return this.getProperty(DSApplication.TEXTE_CATEGORIE_PERSONNEL_DE);
            } else if ((langue != null) && langue.equals("it")
                    && !JadeStringUtil.isEmpty(this.getProperty(DSApplication.TEXTE_CATEGORIE_PERSONNEL_IT))) {
                return this.getProperty(DSApplication.TEXTE_CATEGORIE_PERSONNEL_IT);
            } else {
                return this.getProperty(DSApplication.TEXTE_CATEGORIE_PERSONNEL);
            }
        } else {
            return "";
        }
    }

    public String giveCodeBloquage() {
        return this.getProperty(DSApplication.CI_CODE_BLOQUAGE, "");
    }

    public boolean isAf() {
        return Boolean.valueOf(this.getProperty(DSApplication.AF, "false").trim()).booleanValue();
    }

    public boolean isAfPres() {
        return Boolean.valueOf(this.getProperty(DSApplication.AFPRES, "false").trim()).booleanValue();
    }

    public boolean isAmat() {
        return Boolean.valueOf(this.getProperty(DSApplication.AMAT, "false").trim()).booleanValue();
    }

    public boolean isCanton() {
        return Boolean.valueOf(this.getProperty(DSApplication.CANTON, "false").trim()).booleanValue();
    }

    public boolean isCategoriePersonne() {
        return Boolean.valueOf(this.getProperty(DSApplication.CATEGORIE_PERSONNE, "false").trim()).booleanValue();
    }

    public boolean isCCVDTraitement() {
        return Boolean.valueOf(this.getProperty(DSApplication.CCVD_AGRIVIT, "false").trim()).booleanValue();
    }

    public boolean isFFPP() {
        return Boolean.valueOf(this.getProperty(DSApplication.FFPP, "false").trim()).booleanValue();
    }

    public boolean isGestionLppDansValidationDS() {
        return Boolean.valueOf(this.getProperty(DSApplication.GESTION_LPP_DANS_VALIDATION_DS, "true").trim())
                .booleanValue();
    }

    public boolean isLpp() {
        return Boolean.valueOf(this.getProperty(DSApplication.LPP, "false").trim()).booleanValue();
    }

    public boolean isNouveauControleEmployeur() {
        return Boolean.valueOf(this.getProperty(DSApplication.NOUVEAU_CONTROLE_EMPLOYEUR, "false").trim())
                .booleanValue();
    }

    public boolean isSaisonnier() {
        return Boolean.valueOf(this.getProperty(DSApplication.SAISONNIER, "false").trim()).booleanValue();
    }

    public boolean wantTriAgenceComm() {
        return Boolean.valueOf(this.getProperty(DSApplication.TRI_AGENCE_COMMUNALE, "false").trim()).booleanValue();
    }

    public boolean isRemiseAZeroDAN() {
        return Boolean.valueOf(this.getProperty(DSApplication.EXCLURE_IMPORT_LIGNE_A_ZERO, "false").trim()).booleanValue();
    }
}
