package globaz.naos.application;

import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BApplication;
import globaz.globall.db.BSession;
import globaz.globall.format.IFormatData;
import globaz.globall.parameters.FWParameters;
import globaz.globall.parameters.FWParametersManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.affiliation.INumberGenerator;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.api.IAFParticulariteAffiliation;
import globaz.naos.api.helper.IAFAffiliationHelper;
import globaz.naos.api.helper.IAFParticulariteAffiliationHelper;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.api.OsirisDef;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import globaz.webavs.common.CommonProperties;

/**
 * Application NAOS
 * 
 * @author Emmanuel Fleury
 */
public class AFApplication extends BApplication {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String ADD_JOURDATE_FACTURATION = "addJourDateFacturation";
    public final static String ASDOCAVISMUTATION = "asDocAvisMutation";
    public final static String ASDOCBORDEREAUMUTATION = "asDocBordereauMutation";
    public final static String ASDOCCARTOTHEQUE = "asDocCartotheque";
    public final static String CREATIONPASSAGE_AUTOMATIQUE = "creationPassageAutomatique";

    public final static String DEFAULT_APPLICATION_NAOS = "NAOS";

    public final static String DEFAULT_APPLICATION_NAOS_REP = "naosRoot";
    public final static String LIBELLEPASSAGE_AUTOMATIQUE = "libellePassageCotPersAuto";
    /** Répertoire des models pour Excel */
    public static final String MODELS_EXCELML_NAOS = "model/excelml";
    public final static String MOISFFPP = "moisFFPP";
    public final static String NOUVEAU_CONTROLE_EMPLOYEUR = "nouveautControleEmployeur";
    public static final String PARAM_MODE_PERIODES_CHEVAUCHANTES = "MPERCHEV";
    public final static String PERIODICITECONTROLEEMPLOYEURCAISSE = "periodiciteControleEmployeurCaisse";
    public static final String PROPERTY_ANNUALISER_MASSE = "annualiserMasse";
    public static final String PROPERTY_GED_SERVICE_NAME = "ged.service.name";
    public static final String PROPERTY_IS_SUIVI_REV_BILAN = "isSuiviRevenuBilan";
    /**
     * Le nom de la propriété qui permet forcer une cotisation minimale pour les taux variables
     */
    public static final String PROPERTY_IS_TAUX_COTISATION_MIN = "isCotisationMinimale";
    /** Le nom de la propriété qui permet de calculer le taux par palier ou non */
    public static final String PROPERTY_IS_TAUX_PAR_PALIER = "isTauxParPalier";
    public static final String PROPERTY_MODIFIER_TAG_AMATGENEVOISE = "modifierTagAmatGenevoise";

    public static final String PROPERTY_NO_DOCUMENT_DS_RADIATION = "modelDsRadiation";
    public final static String PROPERTY_QUITTANCE_PCG_CONTROLE_NBRE_QUITTANCES = "quittancePcgControleNbre";
    public static final String PROPERTY_RESTITUTION_TAXE_CO2_NON_JUIN = "restituerTaxeCO2NonJuin";
    public static final String PROPERTY_RESTITUTION_TAXE_CO2_MONTH = "restituerTaxeCO2Mois";
    public static final String PROPERTY_ATTESTATION_RUBRIQUES_COMPLEMENTAIRES = "attestation.rubriques.complementaires";

    public final static String SYNCHROTAUX = "synchroTaux";
    // Liste des types d'affiliation interdits par la modification de la période active IDE
    public static final String IDE_LIST_TYPE_AFFILIATION_NON_MODIFIABLE = "ide.typeAffiliation.nonModifiable";
    // Liste des personnalités juridiques autorisées par la modification de la période active IDE
    public static final String IDE_LIST_PERSONNALITE_JURIDQIUE_MODIFIABLE = "ide.personnaliteJuridique.modifiable";

    public static final String PROPERTY_LIEN_REGISTRE_IDE = "ide.lienRegistre";

    /**
     * Retourne la langue associée a un tier.
     * 
     * @param tiers
     * @return la langue
     */
    public static String getISOLangueTiers(TITiers tiers) {
        if (tiers == null) {
            return "FR";
        } else if (tiers.getLangue().equalsIgnoreCase(IConstantes.CS_TIERS_LANGUE_FRANCAIS)) {
            return "FR";
        } else if (tiers.getLangue().equalsIgnoreCase(IConstantes.CS_TIERS_LANGUE_ALLEMAND)) {
            return "DE";
        } else if (tiers.getLangue().equalsIgnoreCase(IConstantes.CS_TIERS_LANGUE_ITALIEN)) {
            return "IT";
        } else {
            return "FR"; // default
        }
    }

    /**
     * PErmet la récupération d'un tiers par son identifiant
     * 
     * @param session
     *            Une session
     * @param idTiers
     *            Un identifiant de tiers
     * @return TITiers
     * @throws Exception
     */
    public static TITiers retrieveTiers(BSession session, String idTiers) throws Exception {

        if (session == null) {
            throw new Exception("Unabled to retrieve TITiers, session is null");
        }

        if (JadeStringUtil.isIntegerEmpty(idTiers)) {
            throw new Exception("Unabled to retrieve TITiers, idTiers is null or empty");
        }

        TITiers tiers = new TITiers();
        tiers.setSession(session);
        tiers.setIdTiers(idTiers);
        try {
            tiers.retrieve();
        } catch (Exception e) {
            tiers = null;
            throw new Exception("Technical Exception, Unabled to retrieve the tiers ( idTiers = " + idTiers + ")", e);
        }

        return tiers;
    }

    private IFormatData affileFormater = null;

    private BIApplication appOsiris = null;

    private String modePeriodesChevauchantes;

    private INumberGenerator noAffGenerator = null;

    private BISession sessionOsiris = null;

    /**
     * Initialise l'application.
     * 
     * @exception Exception
     *                - si l'initialisation de l'application a échouée
     */
    public AFApplication() throws Exception {
        super(AFApplication.DEFAULT_APPLICATION_NAOS);
    }

    /**
     * Constructeur du type AFApplication.
     * 
     * @param id
     *            - l'id de l'application
     * @exception Exception
     *                - si l'initialisation de l'application a échouée
     */
    public AFApplication(String id) throws Exception {
        super(id);
    }

    /**
     * Déclare les APIs de l'application.
     */
    @Override
    protected void _declareAPI() {
        // super._declareAPI();
        _addAPI(IAFAffiliation.class, IAFAffiliationHelper.class);
        _addAPI(IAFParticulariteAffiliation.class, IAFParticulariteAffiliationHelper.class);
    }

    /**
     * Initialisation de l'application.
     * 
     * @see globaz.globall.db.BApplication#_initializeApplication()
     */
    @Override
    protected void _initializeApplication() throws Exception {
        FWMenuCache cache = FWMenuCache.getInstance();
        cache.addFile("NAOSMenu.xml");
    }

    @Override
    protected void _initializeCustomActions() {
        FWAction.registerActionCustom("aries.decisioncgas.decisionCgasSearch.afficherCgasSearch",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("auriga.decisioncap.decisionCapSearch.afficherCapSearch", FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.affiliation.epuCasNonSoumis.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.rentier.export.executer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.calculRetroactif.calculRetroactif.executer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.adhesion.adhesion.afficherSelectionCotisation", FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.adhesion.adhesion.ajouterCotisation", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.affiliation.affiliation.supprimerAffiliation", FWSecureConstants.REMOVE);
        FWAction.registerLinkedAction("naos.affiliation.affiliation.rechercheIdCompteAnnexe",
                "osiris.comptes.apercuComptes.chercher");
        FWAction.registerLinkedAction("naos.affiliation.affiliation.rechercheDecisionCP",
                "phenix.principale.decision.chercher");
        FWAction.registerActionCustom("naos.affiliation.affiliation.gedafficherdossier", FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.affiliation.affiliation.rechercheAdressesTiers", FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.annonceAffilie.annonceAffilie.creer", FWSecureConstants.ADD);
        FWAction.registerActionCustom("naos.releve.apercuReleve.afficherPreSaisie", FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.releve.apercuReleve.calculer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.releve.apercuReleve.setPeriode", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.releve.apercuReleve.imprimer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.releve.apercuReleve.selectionImpression", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.calculRetroactif.calculRetroactif.generer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.cotisation.cotisation.afficherCreeException", FWSecureConstants.ADD);
        FWAction.registerActionCustom("naos.cotisation.cotisation.creeException", FWSecureConstants.ADD);
        FWAction.registerActionCustom("naos.cotisation.cotisation.afficherModifierException", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.couverture.couverture.chercherTauxAssurance", FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.couverture.couverture.chercherParamAssurance", FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.beneficiairepc.impression.imprimerQuittance", FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.beneficiairepc.impressionErreurs.imprimerErreurs", FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.lienAffiliation.lienAffiliation.affilieLienAffiliation",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.lienAffiliation.lienAffiliation.selectAffilie", FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.lienAffiliation.lienAffiliation.selectAffilieLister",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.lienAffiliation.lienAffiliation.selectAffilieModifier",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.plan.plan.selectionPlanAssurance", FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.wizard.wizard.afficherSaisieAffiliation", FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.wizard.wizard.afficherSelectionPlanCaisse", FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.wizard.wizard.afficherCreationPlanAffiliation", FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.wizard.wizard.afficherSelectionCotisation", FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.wizard.wizard.ajouterPlanAffiliation", FWSecureConstants.ADD);
        FWAction.registerActionCustom("naos.wizard.wizard.ajouterAffiliation", FWSecureConstants.ADD);
        FWAction.registerActionCustom("naos.controleEmployeur.imprimerControle", FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.controleEmployeur.imprimerlettrelibre", FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.controleEmployeur.saisieRapideReviseur", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.controleEmployeur.saisieRapideReviseur.ouvrir", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.controleEmployeur.saisieRapideReviseur.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.controleEmployeur.lettreProchainControle", FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.controleEmployeur.controleEmployeur.imprimerControle",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.controleEmployeur.controleEmployeur.imprimerlettrelibre",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.controleEmployeur.controleEmployeur.lettreProchainControle",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.controleEmployeur.attributionPts.historique", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.controleEmployeur.attributionPts.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.taxeCo2.masseTaxeCo2.calculerMasse", FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.taxeCo2.figerTaxeCo2.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.avisMutation.avisMutation.selectionImpression", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.annonceAffilie.annonceAffilie.chercher", FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.tent.export.detail", FWSecureConstants.READ);
        FWAction.registerLinkedAction("naos.affiliation.affiliation.gestionEnvois", "leo.envoi.envoi.chercher");
        FWAction.registerActionCustom("naos.masse.masseModifier.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.masse.annonceSalaires.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.affiliation.affiliation.gedafficherdossier", FWSecureConstants.READ);
        FWAction.registerActionCustom("naos.wizard.wizard.afficherSaisieAffiliation", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.annonceAffilie.impressionMutation.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.rentier.export.detail", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.beneficiairepc.journalQuittances.afficherGenerer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.beneficiairepc.journalQuittances.afficherComptabiliser",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("naos.controleLpp.controleLppAnnuel.afficher", FWSecureConstants.UPDATE);

        // Ree
        FWAction.registerActionCustom("naos.annoncesRee.annoncesRee.afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("naos.beneficiairepc.journalQuittances.afficherNom", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("naos.beneficiairepc.journalQuittances.executerGenerer", FWSecureConstants.UPDATE);
        // PO 7327
        FWAction.registerActionCustom("naos.taxeCo2.reinjectionListeExcel.afficher", FWSecureConstants.UPDATE);

        // D0050 IDE
        FWAction.registerActionCustom("naos.ide.ideAnnonce.prepareAjoutAnnonceIdeCreation", FWSecureConstants.ADD);
    }

    /**
     * @return true si cette caisse imprime le doc Avis Mutation.
     */
    public boolean asDocAvisMutation() {
        return Boolean.valueOf(this.getProperty(AFApplication.ASDOCAVISMUTATION).trim()).booleanValue();
    }

    /**
     * @return true si cette caisse imprime le doc Bordereau Mutation.
     */
    public boolean asDocBordereauMutation() {
        return Boolean.valueOf(this.getProperty(AFApplication.ASDOCBORDEREAUMUTATION).trim()).booleanValue();
    }

    /**
     * @return true si cette caisse imprime le doc Carthotheque.
     */
    public boolean asDocCartotheque() {
        return Boolean.valueOf(this.getProperty(AFApplication.ASDOCCARTOTHEQUE).trim()).booleanValue();
    }

    /**
     * Retourne le nombre de jour à ajouter lors de la création automatique du passage de facturation Date de création :
     * (20.04.2006 10:46:24)
     * 
     * @return int nombre de jour à ajouter
     */
    public int getAddJourDateFacturation() {
        return Integer.parseInt(this.getProperty(AFApplication.ADD_JOURDATE_FACTURATION, "1"));
    }

    /**
     * Renvoie le Formateur du numéro d'affilié.
     * 
     * @return le formateur
     * 
     * @throws Exception
     *             - si l'initialisation du formateur a échoué
     */
    public globaz.globall.format.IFormatData getAffileFormater() throws Exception {

        if (affileFormater == null) {
            String className = this.getProperty(CommonProperties.KEY_FORMAT_NUM_AFFILIE);
            if (!JadeStringUtil.isEmpty(className)) {
                affileFormater = (IFormatData) Class.forName(className).newInstance();
            }
        }
        return affileFormater;
    }

    /**
     * Renvoie le Formateur de l'ancien numéro d'affilié.
     * 
     * @return le formateur
     * 
     * @throws Exception
     *             - si l'initialisation du formateur a échoué
     */
    public globaz.globall.format.IFormatData getAncienAffileFormater() throws Exception {

        if (affileFormater == null) {
            String className = this.getProperty(CommonProperties.KEY_FORMAT_ANCIEN_NUM_AFFILIE);
            if (JadeStringUtil.isEmpty(className)) {
                className = this.getProperty(CommonProperties.KEY_FORMAT_NUM_AFFILIE);
            }
            if (!JadeStringUtil.isEmpty(className)) {
                affileFormater = (IFormatData) Class.forName(className).newInstance();
            }
        }
        return affileFormater;
    }

    /**
     * Retourne CAApplication (OSIRIS) ou null si inaccessible Date de création : (24.02.2003 18:41:34)
     * 
     * @return globaz.globall.api.BIApplication
     */
    public BIApplication getApplicationOsiris() {
        // Si application pas ouverte
        if (appOsiris == null) {
            try {
                appOsiris = GlobazSystem.getApplication(OsirisDef.DEFAULT_APPLICATION_OSIRIS);
            } catch (Exception e) {
                appOsiris = null;
                JadeLogger.error(this, e);
            }
        }
        return appOsiris;
    }

    /**
     * Renvoie le nombre de digits pour le numéro d'affilier.
     * 
     * @return le nombre de digit.
     */
    public int getAutoDigitAffilie() {

        String temp = this.getProperty(CommonProperties.KEY_AUTO_DIGIT_AFF).trim();
        return Integer.valueOf(temp).intValue();
    }

    /**
     * Renvoie le générateur du numéro d'affilié.
     * 
     * @return le générateur
     * 
     * @throws Exception
     *             - si l'initialisation a échoué
     */
    public INumberGenerator getGeneratorNoAff() throws Exception {
        if (noAffGenerator == null) {
            String className = this.getProperty("genNumAffilie");
            if (!JadeStringUtil.isEmpty(className)) {
                noAffGenerator = (INumberGenerator) Class.forName(className).newInstance();
            }
        }
        return noAffGenerator;
    }

    public String getLibellePassageAutomatique() {
        return this.getProperty(AFApplication.LIBELLEPASSAGE_AUTOMATIQUE, "Facturation relevés");
    }

    /**
     * Méthode qui retourne l'id du modèle pour l'impression des DS lors d'une radiation Si pas renseigné => vide, comme
     * actuellement
     * 
     * @return l'id
     */
    public String getModelDS() {
        return this.getProperty(AFApplication.PROPERTY_NO_DOCUMENT_DS_RADIATION, "").trim();
    }

    public String getModePeriodesChevauchantes(BISession session) throws Exception {
        if (modePeriodesChevauchantes == null) {
            modePeriodesChevauchantes = "standard";
            if (session != null) {
                FWParametersManager mgr = new FWParametersManager();
                mgr.setISession(session);
                mgr.setForApplication(AFApplication.DEFAULT_APPLICATION_NAOS);
                mgr.setForIdCle(AFApplication.PARAM_MODE_PERIODES_CHEVAUCHANTES);
                mgr.find();
                if (!mgr.isEmpty()) {
                    modePeriodesChevauchantes = ((FWParameters) mgr.getFirstEntity()).getValeurAlpha();
                }
            }

        }
        return modePeriodesChevauchantes;

    }

    public String getMoisFFPP() {

        String temp = this.getProperty(AFApplication.MOISFFPP);

        if (temp == null) {
            JadeLogger.warn(this, "La propriété 'naos." + AFApplication.MOISFFPP
                    + " n'as pas été définie. Le mois de décembre est pris par défaut.");
        }

        return (temp != null) ? temp.trim() : CodeSystem.MOIS_DECEMBRE;
    }

    public String getNumCaisse() {

        String temp = this.getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE).trim();
        return temp;
    }

    /**
     * Retourne une session OSIRIS ou null en cas d'execption Date de création : (24.02.2003 18:44:58)
     * 
     * @return globaz.globall.api.BISession
     */
    public BISession getSessionOsiris(BISession session) {
        // Si session pas ouverte
        if (sessionOsiris == null) {
            try {
                sessionOsiris = getApplicationOsiris().newSession(session);
            } catch (Exception e) {
                sessionOsiris = null;
                JadeLogger.error(this, e);
            }
        }
        return sessionOsiris;
    }

    public String getSynchroTaux() {

        String temp = this.getProperty(AFApplication.SYNCHROTAUX).trim();
        return temp;
    }

    /**
     * Liste des types d'affiliation interdits par la modification de la période active IDE
     * 
     * @return string
     */
    public String getListeIDETypeAffiliationNonModifiable() {
        try {
            return this.getProperty(AFApplication.IDE_LIST_TYPE_AFFILIATION_NON_MODIFIABLE).trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Liste des personnalités juridiques autorisées par la modification de la période active IDE
     * 
     * @return string
     */
    public String getListeIDEPersonnaliteJuridiqueModifiable() {
        try {
            return this.getProperty(AFApplication.IDE_LIST_PERSONNALITE_JURIDQIUE_MODIFIABLE).trim();
        } catch (Exception e) {
            return "";
        }
    }

    public String getLienRegistreIde() {

        return this.getProperty(AFApplication.PROPERTY_LIEN_REGISTRE_IDE, "").trim();

    }

    public boolean isControleNombreQuittances() {
        return Boolean.valueOf(
                this.getProperty(AFApplication.PROPERTY_QUITTANCE_PCG_CONTROLE_NBRE_QUITTANCES, "false").trim())
                .booleanValue();
    }

    public boolean isCotisationMinimale() {
        if ("true".equalsIgnoreCase(this.getProperty(AFApplication.PROPERTY_IS_TAUX_COTISATION_MIN, "false"))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCreationPassageAutomatique() {
        if ("true".equalsIgnoreCase(this.getProperty(AFApplication.CREATIONPASSAGE_AUTOMATIQUE, "false"))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isNouveauControleEmployeur() {
        return Boolean.valueOf(this.getProperty(AFApplication.NOUVEAU_CONTROLE_EMPLOYEUR, "false").trim())
                .booleanValue();
    }

    public boolean isSuiviRevenuBilan() {
        try {
            if ("true".equalsIgnoreCase(this.getProperty(AFApplication.PROPERTY_IS_SUIVI_REV_BILAN, "false"))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTauxParPalier() {
        if ("true".equalsIgnoreCase(this.getProperty(AFApplication.PROPERTY_IS_TAUX_PAR_PALIER, "false"))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Indique si la masse doit être annualiser avant la recherche du taux Inforom D0017
     * 
     * @return boolean
     */
    public boolean wantAnnualiserMasse() {
        try {
            if ("true".equalsIgnoreCase(this.getProperty(AFApplication.PROPERTY_ANNUALISER_MASSE, "false"))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

}
