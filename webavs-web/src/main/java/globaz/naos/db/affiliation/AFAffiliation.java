package globaz.naos.db.affiliation;

import globaz.commons.nss.NSUtil;
import globaz.draco.application.DSApplication;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.db.declaration.MajFADHelper;
import globaz.draco.db.preimpression.DSPreImpressionDeclarationViewBean;
import globaz.draco.process.DSProcessValidation;
import globaz.draco.util.DSUtil;
import globaz.framework.secure.user.FWSecureUserDetail;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.ged.AirsConstants;
import globaz.globall.api.BIApplication;
import globaz.globall.db.*;
import globaz.globall.format.IFormatData;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.shared.GlobazValueObject;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JATime;
import globaz.globall.util.JAUtil;
import globaz.hercule.application.CEApplication;
import globaz.hercule.utils.CEUtils;
import globaz.hermes.db.access.HEInfos;
import globaz.hermes.utils.HEUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.service.JadeTarget;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.process.handler.LEJournalHandler;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.lupus.db.journalisation.LUJournalViewBean;
import globaz.musca.util.FAUtil;
import globaz.naos.affiliation.INumberGenerator;
import globaz.naos.api.helper.IAFAffiliationHelper;
import globaz.naos.application.AFApplication;
import globaz.naos.db.adhesion.AFAdhesion;
import globaz.naos.db.adhesion.AFAdhesionManager;
import globaz.naos.db.annonceAffilie.AFAnnonceAffilie;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationJAssuranceManager;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.lienAffiliation.AFLienAffiliation;
import globaz.naos.db.lienAffiliation.AFLienAffiliationManager;
import globaz.naos.db.nombreAssures.AFNombreAssures;
import globaz.naos.db.nombreAssures.AFNombreAssuresManager;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliationManager;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.db.planAffiliation.AFPlanAffiliationManager;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliationManager;
import globaz.naos.properties.AFProperties;
import globaz.naos.suivi.AFSuiviLAA;
import globaz.naos.suivi.AFSuiviLPP;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDERules;
import globaz.naos.util.AFIDEUtil;
import globaz.naos.util.AFUtil;
import globaz.orion.process.EBDanPreRemplissage;
import globaz.osiris.utils.CAUtil;
import globaz.pavo.util.CIUtil;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionManager;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TICompositionTiers;
import globaz.pyxis.db.tiers.TICompositionTiersManager;
import globaz.pyxis.db.tiers.TIRole;
import globaz.pyxis.db.tiers.TIRoleManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.ICommonConstantes;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.aries.business.constantes.ARDecisionEtat;
import ch.globaz.aries.business.models.DecisionCGASSearchModel;
import ch.globaz.aries.business.services.AriesServiceLocator;
import ch.globaz.auriga.business.constantes.AUDecisionEtat;
import ch.globaz.auriga.business.models.DecisionCAPSearchModel;
import ch.globaz.auriga.business.services.AurigaServiceLocator;
import ch.globaz.orion.businessimpl.services.ServicesProviders;
import ch.globaz.xmlns.eb.dan.DANService;

public class AFAffiliation extends BEntity implements Serializable {

    private static final long serialVersionUID = 972086630099382983L;
    private static final Logger logger = LoggerFactory.getLogger(AFAffiliation.class);

    public enum Genre {
        AUTRE,
        PARITAIRE,
        PARITAIRE_ET_PERSONNEL,
        PERSONNEL
    }

    public static final String FIELDNAME_NUMERO_IDE = "MALFED";
    public static final String FIELDNAME_NUMERO_AFFILIE = "MALNAF";
    public static final String FIELDNAME_STATUT_IDE = "MATSTA";
    public static final String FIELDNAME_RAISON_SOCIALE = "MADESL";

    public static final String FIELDNAME_AFF_DDEBUT = "MADDEB";
    public static final String FIELDNAME_AFF_DFIN = "MADFIN";
    public static final String FIELDNAME_AFFILIATION_ID = "MAIAFF";
    public static final String FIELDNAME_AFFILIATION_TYPE = "MATTAF";
    public static final String FIELDNAME_TIER_ID = "HTITIE";
    public static final String FIELDNAME_MAJ_FAD = "MAJFAD";
    public static final String LIEN_AGENCE = "507007";
    public static final String LIEN_AGENCE_PRIVE = "507008";
    public static String SECURE_CODE = "SecureCode";

    private MajFADHelper majFADHelper = new MajFADHelper();

    public static final String TABLE_NAME = "AFAFFIP";

    public void setMajFADHelper(MajFADHelper majFADHelper) {
        this.majFADHelper = majFADHelper;
    }

    public MajFADHelper getMajFADHelper() {
        return majFADHelper;
    }

    /**
     * Retour l'IdCotisation de la première Cotisation qui correspond au critères de recherche.
     *
     * @param transaction
     * @param idAffiliation
     * @param type
     * @param dateDebutCP
     * @param dateFinCP
     * @return
     * @throws Exception
     */
    public static String _idCotisation(BTransaction transaction, String idAffiliation, String type, String dateDebutCP,
            String dateFinCP) throws Exception {
        try {
            AFPlanAffiliationManager planAffiliationManager = new AFPlanAffiliationManager();
            planAffiliationManager.setForAffiliationId(idAffiliation);
            planAffiliationManager.setSession(transaction.getSession());
            planAffiliationManager.find(transaction);

            for (int i = 0; i < planAffiliationManager.size(); i++) {
                AFPlanAffiliation planAffiliation = (AFPlanAffiliation) planAffiliationManager.getEntity(i);
                if (!planAffiliation.isInactif().booleanValue()) {
                    AFCotisationManager cotisationManager = new AFCotisationManager();
                    cotisationManager.setForPlanAffiliationId(planAffiliation.getPlanAffiliationId());
                    cotisationManager.setSession(transaction.getSession());
                    cotisationManager.find(transaction);

                    for (int j = 0; j < cotisationManager.size(); j++) {

                        AFCotisation cotisation = (AFCotisation) cotisationManager.getEntity(j);

                        if (cotisation.getAssurance().getTypeAssurance().equals(type)) {

                            if (!BSessionUtil.compareDateFirstGreaterOrEqual(transaction.getSession(),
                                    cotisation.getDateDebut(), dateFinCP)) {

                                if (JadeStringUtil.isBlankOrZero(cotisation.getDateFin())) {

                                    return cotisation.getCotisationId();
                                } else {
                                    if (!BSessionUtil.compareDateFirstGreater(transaction.getSession(), dateFinCP,
                                            cotisation.getDateFin())) {

                                        return cotisation.getCotisationId();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            transaction.addErrors(transaction.getSession().getLabel("980") + idAffiliation);
        }
        return "";
    }

    /**
     * retourne l'affilié en fonction du numéro d'affilié. si forParitaire et forPersonnel sont à false aucun filtre
     * n'est effectué
     *
     * @param session
     * @param numeroAffilie
     * @param forParitaire
     *            true pour filtrer sur les affiliés paritaires
     * @param forPersonnel
     *            true pour filtrer sur les affiliés personnels
     * @param moisD
     * @param moisF
     * @param annee
     * @param jourDebut
     * @param JourFin
     * @return
     */
    public static AFAffiliation returnAffiliation(BSession session, String numeroAffilie, boolean forParitaire,
            boolean forPersonnel, String periodeDebut, String periodeFin) {
        AFAffiliationManager affMgr = new AFAffiliationManager();
        try {
            affMgr.setSession(session);
            affMgr.setForAffilieNumero(numeroAffilie);
            if (forParitaire) {
                affMgr.setForTypeFacturation(AFAffiliationManager.PARITAIRE);
                affMgr.setForTypesAffParitaires();
            }
            if (forPersonnel) {
                affMgr.setForTypesAffPersonelles();
            }

            affMgr.find();
            for (int i = 0; i < affMgr.size(); i++) {
                AFAffiliation affiliation = (AFAffiliation) affMgr.getEntity(i);
                if ((affiliation.getDateDebut().equalsIgnoreCase(affiliation.getDateFin()) == false)
                        && BSessionUtil.compareDateFirstLowerOrEqual(session, affiliation.getDateDebut(), periodeDebut)) {
                    if (JadeStringUtil.isBlankOrZero(affiliation.getDateFin())
                            || BSessionUtil.compareDateFirstGreaterOrEqual(session, affiliation.getDateFin(),
                                    periodeFin)) {

                        return affiliation;
                    }
                }
            }
        } catch (Exception ex) {
        }
        return null;

    }

    private TITiersViewBean _tiers = null;
    private String accesSecurite = new String();
    // DB
    // Primary Key
    private java.lang.String affiliationId = new String();
    // Fields
    private java.lang.String affilieNumero = new String();
    private java.lang.String ancienAffilieNumero = new String();
    private java.lang.Boolean bonusMalus = new Boolean(false);
    private java.lang.String brancheEconomique = new String();
    private java.lang.String caissePartance = new String();
    private java.lang.String caisseProvenance = new String();
    private java.lang.String codeFacturation = new String();
    private java.lang.String codeNoga = new String();
    private java.lang.String dateCreation = new String();
    private java.lang.String dateDebut = new String();
    private java.lang.String dateDebutSave = new String();
    private String dateDemandeAffiliation = new String();
    private java.lang.String dateEditionFiche = new String();
    private java.lang.String dateEditionFicheM1 = new String();
    private java.lang.String dateEditionFicheM2 = new String();
    private java.lang.String dateFin = new String();
    private java.lang.String dateFinSave = new String();
    private java.lang.String datePrecDebut = new String();
    private java.lang.String datePrecFin = new String();
    private java.lang.String dateTent = new String();
    private java.lang.String declarationSalaire = new String();
    private Boolean envoiAutomatiqueAnnonceSalaires = new Boolean(false);
    private java.lang.Boolean envoiAutomatiqueLAA = new Boolean(true);
    private java.lang.Boolean envoiAutomatiqueLPP = new Boolean(true);
    private java.lang.Boolean exonerationGenerale = new Boolean(false);
    // Primary and Foreign Key
    private java.lang.String idTiers = new String();
    private java.lang.Boolean majFAD = new Boolean(false);
    private java.lang.Boolean irrecouvrable = new Boolean(false);
    private java.lang.Boolean liquidation = new Boolean(false);
    private java.lang.String masseAnnuelle = new String();
    private java.lang.String massePeriodicite = new String();
    private java.lang.String membreComite = new String();
    private java.lang.String motifCreation = new String();
    private java.lang.String motifFin = new String();
    private java.lang.String convention = new String();

    private java.lang.String numeroIDE = new String();
    private java.lang.Boolean occasionnel = new Boolean(false);

    private AFAffiliation oldAffiliation = null;
    private java.lang.String periodicite = new String();

    private java.lang.String personnaliteJuridique = new String();
    private java.lang.Boolean personnelMaison = new Boolean(false);
    private String raisonSociale = "";
    private String raisonSocialeCourt = "";
    private java.lang.Boolean releveParitaire = new Boolean(false);
    private java.lang.Boolean relevePersonnel = new Boolean(false);

    // list des suivis à exécuter
    private ArrayList<BProcess> suivisList = null;
    private java.lang.String taxeCo2Fraction = new String();

    private java.lang.String taxeCo2Taux = new String();

    private java.lang.Boolean traitement = new Boolean(false);

    private java.lang.String typeAffiliation = new String();

    private java.lang.String typeAssocie = new String();

    private String codeSUVA = "";

    private boolean wantGenerationSuiviLAALPP = true;
    // D0050 - Champ IDE
    private java.lang.Boolean ideAnnoncePassive = new Boolean(false);
    private java.lang.Boolean ideNonAnnoncante = new Boolean(false);
    private String ideStatut = "";
    private String ideRaisonSociale = "";

    // D0050 - Champ IDE non persisté
    private boolean isAnnonceIdeCreationToAdd = false;
    private boolean rulesByPass = false;

    private boolean saisieSysteme = Boolean.FALSE;

    // D0181 - avenant IDE champ activité
    private String activite = "";
    private boolean confirmerAnnonceActivite = false;

    private String warningMessageAnnonceIdeCreationNotAdded = "";

    private String idAnnonceIdeCreationLiee = "";

    /**
     * Permet de définir si une modification de l'affiliation est faite depuis l'écran ou par le système
     *
     */
    public boolean isSaisieSysteme() {
        return saisieSysteme;
    }

    public void setSaisieSysteme(boolean saisieSysteme) {
        this.saisieSysteme = saisieSysteme;
    }

    public String getIdAnnonceIdeCreationLiee() {
        return idAnnonceIdeCreationLiee;
    }

    public void setIdAnnonceIdeCreationLiee(String idAnnonceIdeCreationLiee) {
        this.idAnnonceIdeCreationLiee = idAnnonceIdeCreationLiee;
    }

    public String getWarningMessageAnnonceIdeCreationNotAdded() {
        return warningMessageAnnonceIdeCreationNotAdded;
    }

    public void setWarningMessageAnnonceIdeCreationNotAdded(String warningMessageAnnonceIdeCreationNotAdded) {
        this.warningMessageAnnonceIdeCreationNotAdded = warningMessageAnnonceIdeCreationNotAdded;
    }

    public boolean isAnnonceIdeCreationToAdd() {
        return isAnnonceIdeCreationToAdd;
    }

    // commence par souligné afin de ne pas être prise par jspSetBeanProperties
    public void _setAnnonceIdeCreationToAdd(boolean isAnnonceIdeCreationToAdd) {
        this.isAnnonceIdeCreationToAdd = isAnnonceIdeCreationToAdd;
    }

    public String giveMeFirstIdAnnonceCreationEnCoursForIdTiers() {
        return AFIDEUtil.giveMeFirstIdAnnonceCreationEnCoursForIdTiers(getSession(), getIdTiers());
    }

    public boolean isRulesByPass() {
        return rulesByPass;
    }

    public void setRulesByPass(boolean rulesByPass) {
        this.rulesByPass = rulesByPass;
    }

    public boolean getConfirmerAnnonceActivite() {
        return confirmerAnnonceActivite;
    }

    public void setConfirmerAnnonceActivite(boolean confirmerAnnonceActivite) {
        this.confirmerAnnonceActivite = confirmerAnnonceActivite;
    }

    /**
     * Constructeur de AFAffiliation
     */
    public AFAffiliation() {
        super();
        setMethodsToLoad(IAFAffiliationHelper.getterToLoad());
    }

    /**
     * Effectue des traitements après un ajout dans la BD.
     *
     * @see globaz.globall.db.BEntity#_afterAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        // Une adresse doit exister
        if (JadeStringUtil.isBlank(getTiers().getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                ICommonConstantes.CS_APPLICATION_COTISATION, getDateDebut()))) {
            _addError(transaction, getSession().getLabel("2060"));
        }
        // si aucune erreur n'est constatée dans l'insertion de l'affiliation,
        // initier les suivi LAA et LPP en fonction du choix de l'utilisateur
        if (!transaction.hasErrors() && !getSession().hasErrors()) {
            if (isWantGenerationSuiviLAALPP()) {
                if (getEnvoiAutomatiqueLAA().booleanValue()) {
                    // Contrôle LAA
                    envoiCtrlLAA();
                }
                if (getEnvoiAutomatiqueLPP().booleanValue()) {
                    // Contrôle LPP
                    this.envoiCtrlLPP();
                }
            }
        }

        // ajoute du rôle affilé dans tiers s'il n'existe pas encore
        // la date de début est celle d'affiliation et non celle du jour
        TIRoleManager rMgr = new TIRoleManager();
        rMgr.setSession(getSession());
        rMgr.setForIdTiers(getIdTiers());
        rMgr.setForRole(TIRole.CS_AFFILIE);
        rMgr.setForDateEntreDebutEtFin(getDateDebut());
        if (rMgr.getCount() == 0) {
            // aucune rôle n'existe, en créer un
            TIRole role = new TIRole();
            role.setSession(getSession());
            role.setIdTiers(getIdTiers());
            role.setRole(TIRole.CS_AFFILIE);
            role.setDebutRole(getDateDebut());
            role.add(transaction);
        }

        JATime now = new JATime(JACalendar.now());
        AFAnnonceAffilie annonce = new AFAnnonceAffilie();
        annonce.setChampModifier(CodeSystem.CHAMPS_MOD_CREATION_AFFILIE);
        annonce.setUtilisateur(getSession().getUserName());
        annonce.setAffiliationId(getAffiliationId());
        annonce.setChampAncienneDonnee(getSession().getLabel("960"));
        annonce.setDateEnregistrement(JACalendar.todayJJsMMsAAAA());
        annonce.setHeureEnregistrement(now.toStr(""));
        annonce.setSession(getSession());
        annonce.add(transaction);
        // changement GED AIRS
        if (JadeGedFacade.isInstalled()) {
            // A rajouter:
            JadeTarget target = JadeGedFacade.getInstance().getTarget("JadeGedFacade");
            if (globaz.jade.ged.adapter.airs.JadeGedAdapter.class.isAssignableFrom(target.getClass())) {

                Properties properties = new Properties();
                String classNameNumAffFormatter = JadePropertiesService.getInstance().getProperty(
                        "common.formatNumAffilie");
                IFormatData formatterNumAffilie;

                formatterNumAffilie = (IFormatData) Class.forName(classNameNumAffFormatter).newInstance();

                properties.setProperty(AirsConstants.NAFF, formatterNumAffilie.unformat(getAffilieNumero()));
                properties.setProperty(AirsConstants.NNSS, NSUtil.formatAVSUnknown(getTiers().getNumAvsActuel()));
                properties.setProperty(AirsConstants.NOM, getTiers().getDesignation1());
                properties.setProperty(AirsConstants.PRENOM, getTiers().getDesignation2());
                properties.setProperty(AirsConstants.NPA, getTiers().getLocaliteLong());
                JadeGedFacade.propagate(properties);
            }
        }
        if (!transaction.hasErrors() && !getSession().hasErrors()) {
            if (isAnnonceIdeCreationToAdd) {
                setWarningMessageAnnonceIdeCreationNotAdded(AFIDERules.controleGenerateAnnonceCreationOnAffiliation(
                        getSession(), this));
            } else {
                setWarningMessageAnnonceIdeCreationNotAdded(AFIDERules.controleGenerateAnnonceOnAffiliation(
                        getSession(), this, getOldAffiliation()));
                AFIDERules.controleGenerateAnnonceCreationCloture(getSession(), this);
            }
        }
    }

    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        // Suppression des différents plans et cotisations
        AFPlanAffiliationManager planAffManager = new AFPlanAffiliationManager();
        planAffManager.setSession(getSession());
        planAffManager.setForAffiliationId(getAffiliationId());
        planAffManager.find(transaction);

        for (int i = 0; i < planAffManager.size(); i++) {
            AFPlanAffiliation planAffiliation = (AFPlanAffiliation) planAffManager.getEntity(i);

            AFCotisationManager cotisationManager = new AFCotisationManager();
            cotisationManager.setSession(getSession());
            cotisationManager.setForPlanAffiliationId(planAffiliation.getPlanAffiliationId());
            cotisationManager.changeManagerSize(BManager.SIZE_NOLIMIT);
            cotisationManager.find(transaction);

            for (int j = 0; j < cotisationManager.size(); j++) {
                AFCotisation cotisation = (AFCotisation) cotisationManager.getEntity(j);
                // Suppression de la Cotisation
                cotisation.delete(transaction);
            } // end for
              // Suppression du Plan d'Affiliation
            planAffiliation.delete(transaction);
        } // end for

        // supprimer les annonces IDE non traitées
        AFIDEUtil.deleteTypeAnnonceEnCours(getSession(), "", this);

        if (!transaction.hasErrors()) {
            // Suppression de l'Adhesion
            AFAdhesionManager adhesionManager = new AFAdhesionManager();
            adhesionManager.setSession(getSession());
            adhesionManager.setForAffiliationId(affiliationId);
            adhesionManager.find(transaction);

            for (int j = 0; j < adhesionManager.size(); j++) {
                AFAdhesion adhesion = (AFAdhesion) adhesionManager.getEntity(j);
                adhesion.delete(transaction);
            }
            // Suppression des Lien d'affiliation
            AFLienAffiliationManager lienAffiliationManager = new AFLienAffiliationManager();
            lienAffiliationManager.setSession(getSession());
            lienAffiliationManager.setForAffiliationId(affiliationId);
            lienAffiliationManager.find(transaction);

            for (int j = 0; j < lienAffiliationManager.size(); j++) {
                AFLienAffiliation lienAfilliation = (AFLienAffiliation) lienAffiliationManager.getEntity(j);
                lienAfilliation.delete(transaction);
            }
            // Suppression des Nombre d'Assurés
            AFNombreAssuresManager nombreAssureManager = new AFNombreAssuresManager();
            nombreAssureManager.setSession(getSession());
            nombreAssureManager.setForAffiliationId(affiliationId);
            nombreAssureManager.find(transaction);

            for (int j = 0; j < nombreAssureManager.size(); j++) {
                AFNombreAssures nombreAssure = (AFNombreAssures) nombreAssureManager.getEntity(j);
                nombreAssure.delete(transaction);
            }
            // Suppression des Suivi de Caisse
            AFSuiviCaisseAffiliationManager suiviCaisseManager = new AFSuiviCaisseAffiliationManager();
            suiviCaisseManager.setSession(getSession());
            suiviCaisseManager.setForAffiliationId(affiliationId);
            suiviCaisseManager.find(transaction);
            for (int j = 0; j < suiviCaisseManager.size(); j++) {
                AFSuiviCaisseAffiliation suivi = (AFSuiviCaisseAffiliation) suiviCaisseManager.getEntity(j);
                suivi.delete(transaction);
            }

        } else {
            _addError(transaction, getSession().getLabel("1040"));
        }
    }

    /**
     * Effectue des traitements après une lecture dans la BD.
     *
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        setDateDebutSave(getDateDebut());
        setDateFinSave(getDateFin());
    }

    /**
     * Effectue des traitements après une mise à jour dans la BD.
     *
     * @see globaz.globall.db.BEntity#_afterUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {

        if(!isSaisieSysteme()) {
            // Update le boolean majFAD sur l'affiliation et ajoute si besoin la cotisation à l'assurance de majoration des frais d'admin
            getMajFADHelper().updateMajFADSansDeclaration(getSession().getCurrentThreadTransaction(), this);
        }

        if (isWantGenerationSuiviLAALPP()) {
            if (getEnvoiAutomatiqueLAA().booleanValue()) {
                // Contrôle LAA
                envoiCtrlLAA();
            }
            if (getEnvoiAutomatiqueLPP().booleanValue()) {
                // Contrôle LPP
                this.envoiCtrlLPP();
            }
        }

        AFAffiliation oldAffiliation = getOldAffiliation();

        miseAJourAdhesion(transaction, oldAffiliation);

        miseAjourCotisation(transaction, oldAffiliation);

        // Dans le cas d'une radiation de l'affiliation, on imprime
        // automatiquement une déclaration de salaire
        impressionDeclarationSalaire(transaction);
        // Pré-remplissage pour EBU
        preRemplissageDAN();
        // PO 5107 + PO 6417
        // Si il y a une date de radiation
        if (!JadeStringUtil.isBlankOrZero(getDateFin())) {
            // On supprime le suivi DS et des DS structurée pour l'année de l'ancienne date de radiation
            suppressionSuiviCategorie(transaction, DSApplication.DEFAULT_APPLICATION_DRACO,
                    ILEConstantes.CS_CATEGORIE_SUIVI_DS, getAffilieNumero(), getIdTiers(),
                    "" + (JACalendar.getYear(getDateFin()) + 1));

            // On supprime les DS de type 13 à l'état ouvert
            extourneDSType13Ouverte(transaction);

            suppressionSuiviCategorie(transaction, CEApplication.DEFAULT_APPLICATION_HERCULE,
                    ILEConstantes.CS_CATEGORIE_SUIVI_DS_STRUCTURE, getAffilieNumero(), getIdTiers(),
                    "" + (JACalendar.getYear(getDateFin()) + 1));

        }
        // Extourne des décision CAP
        extourneDecisionCAP();

        // Extourne des décision CGAS
        extourneDecisionCGAS();

        // Sauvegarde des anciennes dates d'affiliation (pour les sorties)
        if (!transaction.hasErrors()) {
            setDatePrecDebut(getDateDebut());
            setDatePrecFin(getDateFin());
        }

        if (isAnnonceIdeCreationToAdd) {
            setWarningMessageAnnonceIdeCreationNotAdded(AFIDERules.controleGenerateAnnonceCreationOnAffiliation(
                    getSession(), this));
        }

    }

    private void preRemplissageDAN() throws JAException, Exception {
        if (!getDateFin().equals(getDatePrecFin())
                && !JadeStringUtil.isEmpty(getDateFin())
                && (CodeSystem.DS_DAN.equals(getDeclarationSalaire()) || CodeSystem.DECL_SAL_MIXTE_DAN
                        .equals(getDeclarationSalaire()))
                && String.valueOf(JADate.getYear(getDateFin())).equalsIgnoreCase(
                        String.valueOf(JADate.getYear(JACalendar.todayJJsMMsAAAA())))) {
            // On test si le provider n'est pas nul
            DANService service = null;
            try {
                service = ServicesProviders.danServiceProvide(getSession());
            } catch (Exception e) {
                // Ne rien faire doit fonctionner en asynchrone
            }
            if (service != null) {
                EBDanPreRemplissage process = new EBDanPreRemplissage();
                process.setSession(getSession());
                process.setPreRemplissageForRadiation(true);
                process.setAnnee(String.valueOf(JADate.getYear(getDateFin())));
                process.setNumAffilie(affilieNumero);
                process.setEmail(getSession().getUserEMail());
                process.setDateRadiation(getDateFin());
                BProcessLauncher.start(process, false);
            }

        }
    }

    private void impressionDeclarationSalaire(BTransaction transaction) throws JAException, Exception {
        if (!getDateFin().equals(getDatePrecFin())
                && !JadeStringUtil.isEmpty(getDateFin())
                && (CodeSystem.DECL_SAL_PRE_LISTE.equals(getDeclarationSalaire()) || CodeSystem.DECL_SAL_PRE_MIXTE
                        .equals(getDeclarationSalaire()))
                && String.valueOf(JADate.getYear(getDateFin())).equalsIgnoreCase(
                        String.valueOf(JADate.getYear(JACalendar.todayJJsMMsAAAA())))) {
            BIApplication remoteApplication = GlobazServer.getCurrentSystem().getApplication("DRACO");
            BSession sessionDraco = (BSession) remoteApplication.newSession(getSession());
            DSPreImpressionDeclarationViewBean processImpression = new DSPreImpressionDeclarationViewBean();
            processImpression.setSession(sessionDraco);
            if (!JadeStringUtil.isBlank(getSession().getApplication().getProperty(
                    AFApplication.PROPERTY_NO_DOCUMENT_DS_RADIATION))) {
                processImpression.setIdDocumentDefaut(getSession().getApplication().getProperty(
                        AFApplication.PROPERTY_NO_DOCUMENT_DS_RADIATION));
                processImpression.setIdDocument(getSession().getApplication().getProperty(
                        AFApplication.PROPERTY_NO_DOCUMENT_DS_RADIATION));
            }
            processImpression.setTransaction(transaction);
            processImpression.setAnnee(String.valueOf(JADate.getYear(getDateFin())));
            processImpression.setDemarreSuivi(new Boolean(true));
            processImpression.setFromAffilies(getAffilieNumero());
            processImpression.setImprimerDeclaration(new Boolean(true));
            processImpression.setImprimerLettre(new Boolean(true));
            processImpression.setUntilAffilies(getAffilieNumero());
            processImpression.setEMailAddress(getSession().getUserEMail());
            processImpression.setAffilieTous(false);
            processImpression.setProvientEcranPreImpression(false);
            processImpression.start();
        }
    }

    private void miseAjourCotisation(BTransaction transaction, AFAffiliation oldAffiliation) throws Exception {
        // *********************************************************
        // Mise à jour de la périodicité des cotisations en
        // fonction de la périodicité de l'affiliation
        // *********************************************************
        if (!oldAffiliation.getPeriodicite().equals(getPeriodicite())
                || !oldAffiliation.getMotifFin().equals(getMotifFin())
                || !oldAffiliation.getDateDebut().equals(getDateDebut())
                || !oldAffiliation.getDateFin().equals(getDateFin())) {

            AFPlanAffiliationManager planAffManager = new AFPlanAffiliationManager();
            planAffManager.setSession(getSession());
            planAffManager.setForAffiliationId(getAffiliationId());
            planAffManager.find(transaction);

            for (int i = 0; i < planAffManager.size(); i++) {
                AFPlanAffiliation planAffiliation = (AFPlanAffiliation) planAffManager.getEntity(i);

                AFCotisationManager cotisationManager = new AFCotisationManager();
                cotisationManager.setSession(getSession());
                cotisationManager.setForPlanAffiliationId(planAffiliation.getPlanAffiliationId());
                cotisationManager.setForAnneeActive(String.valueOf(globaz.globall.util.JACalendar.today().getYear()));
                // cotisationManager.setForNotMotifFin(CodeSystem.MOTIF_FIN_EXCEPTION);
                cotisationManager.find(transaction);

                for (int j = 0; (j < cotisationManager.size()) && !transaction.hasErrors(); j++) {

                    AFCotisation cotisation = (AFCotisation) cotisationManager.getEntity(j);

                    boolean needToBeUpdated = false;
                    // Modification des dates de début et de fin si nécessaire
                    if (JadeStringUtil.isEmpty(cotisation.getAdhesionId())
                            || JadeStringUtil.isNull(cotisation.getAdhesionId())
                            || "0".equals(cotisation.getAdhesionId())) {
                        // PO 3466 - (classe méritant un refactoring profont car code illisible... mise à jour dans
                        // tout les sens)
                        if (!oldAffiliation.getDateDebut().equals(getDateDebut())) {
                            if (!CodeSystem.MOTIF_FIN_EXCEPTION.equals(cotisation.getMotifFin())
                                    || BSessionUtil.compareDateFirstLower(getSession(), cotisation.getDateDebut(),
                                            getDateDebut())) {
                                cotisation.setDateDebut(getDateDebut());
                                needToBeUpdated = true;
                            }
                        }
                        if (!oldAffiliation.getDateFin().equals(getDateFin())) {
                            if (!CodeSystem.MOTIF_FIN_EXCEPTION.equals(cotisation.getMotifFin())
                                    || exceptionAvecDateFinSuperieur(cotisation.getDateFin())) {
                                cotisation.setDateFin(getDateFin());
                                needToBeUpdated = true;
                            }
                        }
                        if (!oldAffiliation.getMotifFin().equals(getMotifFin())
                                && (!CodeSystem.MOTIF_FIN_EXCEPTION.equals(cotisation.getMotifFin()))) {
                            cotisation.setMotifFin(getMotifFin());
                            needToBeUpdated = true;
                        }

                    }

                    if (!cotisation.getPeriodicite().equals(getPeriodicite())
                            && !CodeSystem.MOTIF_FIN_EXCEPTION.equals(cotisation.getMotifFin())) {
                        if (!cotisation.getExceptionPeriodicite().booleanValue()) {
                            cotisation.setPeriodicite(getPeriodicite());
                            needToBeUpdated = true;
                        } else {
                            if (cotisation.getPeriodicite().equals(CodeSystem.PERIODICITE_ANNUELLE)
                                    && (getPeriodicite().equals(CodeSystem.PERIODICITE_TRIMESTRIELLE) || getPeriodicite()
                                            .equals(CodeSystem.PERIODICITE_MENSUELLE))) {
                                // OK
                            } else if (cotisation.getPeriodicite().equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)
                                    && getPeriodicite().equals(CodeSystem.PERIODICITE_MENSUELLE)) {
                                // OK
                            } else {
                                cotisation.setPeriodicite(getPeriodicite());
                                needToBeUpdated = true;
                            }
                        }
                    }
                    if (needToBeUpdated) {
                        cotisation.update(transaction);
                    }
                }
            }
        }
    }

    private void miseAJourAdhesion(BTransaction transaction, AFAffiliation oldAffiliation) throws Exception {
        AFAdhesionManager adhesionManager = new AFAdhesionManager();
        adhesionManager.setSession(getSession());
        adhesionManager.setForAffiliationId(getAffiliationId());
        adhesionManager.find(transaction);

        for (int i = 0; i < adhesionManager.size(); i++) {
            AFAdhesion adhesion = (AFAdhesion) adhesionManager.getEntity(i);
            adhesion.setSession(getSession());

            boolean update = false;
            boolean delete = false;

            if (!oldAffiliation.getDateDebut().equals(getDateDebut())
                    || !oldAffiliation.getDateFin().equals(getDateFin())) {

                // ************************************************
                // Affiliation D----------F
                // Adhesion D----------F
                // ***************************************************
                if (!JadeStringUtil.isBlankOrZero(getDateFin()) && !JadeStringUtil.isBlankOrZero(adhesion.getDateFin())) {

                    if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDebut(), adhesion.getDateFin())) {

                        if (adhesion.getDateDebut().equals(oldAffiliation.getDateDebut())) {
                            adhesion.setDateDebut(getDateDebut());
                            update = true;
                        } else {
                            if (BSessionUtil.compareDateFirstGreater(getSession(), getDateDebut(),
                                    adhesion.getDateDebut())) {
                                adhesion.setDateDebut(getDateDebut());
                                update = true;
                            }
                        }
                    } else {
                        delete = true;
                    }

                    if (BSessionUtil
                            .compareDateFirstGreaterOrEqual(getSession(), getDateFin(), adhesion.getDateDebut())) {

                        if (!JadeStringUtil.isBlankOrZero(oldAffiliation.getDateFin())
                                && adhesion.getDateFin().equals(oldAffiliation.getDateFin())) {
                            adhesion.setDateFin(getDateFin());
                            update = true;
                        } else {
                            if (BSessionUtil.compareDateFirstLower(getSession(), getDateFin(), adhesion.getDateFin())) {
                                adhesion.setDateFin(getDateFin());
                                update = true;
                            }
                        }
                    } else {
                        delete = true;
                    }

                    // ************************************************
                    // Affiliation D-----------------------------
                    // Adhesion D----------F
                    // ***************************************************
                } else if (JadeStringUtil.isBlankOrZero(getDateFin())
                        && !JadeStringUtil.isBlankOrZero(adhesion.getDateFin())) {

                    if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDebut(), adhesion.getDateFin())) {

                        if (adhesion.getDateDebut().equals(oldAffiliation.getDateDebut())) {
                            adhesion.setDateDebut(getDateDebut());
                            update = true;
                        } else {
                            if (BSessionUtil.compareDateFirstGreater(getSession(), getDateDebut(),
                                    adhesion.getDateDebut())) {
                                adhesion.setDateDebut(getDateDebut());
                                update = true;
                            }
                        }
                    } else {
                        delete = true;
                    }

                    if (!JadeStringUtil.isBlankOrZero(oldAffiliation.getDateFin())
                            && adhesion.getDateFin().equals(oldAffiliation.getDateFin())) {
                        adhesion.setDateFin("");
                        update = true;
                    }

                    // ************************************************
                    // Affiliation D----------F
                    // Adhesion D-----------------------------
                    // ***************************************************
                } else if (!JadeStringUtil.isBlankOrZero(getDateFin())
                        && JadeStringUtil.isBlankOrZero(adhesion.getDateFin())) {

                    if (BSessionUtil
                            .compareDateFirstGreaterOrEqual(getSession(), getDateFin(), adhesion.getDateDebut())) {

                        if (adhesion.getDateDebut().equals(oldAffiliation.getDateDebut())) {
                            adhesion.setDateDebut(getDateDebut());
                            update = true;
                        } else {
                            if (BSessionUtil.compareDateFirstGreater(getSession(), getDateDebut(),
                                    adhesion.getDateDebut())) {
                                adhesion.setDateDebut(getDateDebut());
                                update = true;
                            }
                        }
                    } else {
                        adhesion.setDateDebut(getDateFin());
                        update = true;
                    }

                    adhesion.setDateFin(getDateFin());
                    update = true;

                    // ************************************************
                    // Affiliation D-----------------------------
                    // Adhesion D-----------------------------
                    // ***************************************************
                } else {

                    if (adhesion.getDateDebut().equals(oldAffiliation.getDateDebut())) {
                        adhesion.setDateDebut(getDateDebut());
                        update = true;
                    } else {
                        if (BSessionUtil.compareDateFirstGreater(getSession(), getDateDebut(), adhesion.getDateDebut())) {
                            adhesion.setDateDebut(getDateDebut());
                            update = true;
                        }
                    }
                }
            }

            if (delete) {
                adhesion.setAllowChildDelete(true);
                adhesion.delete(transaction);

            } else if (update) {
                adhesion.update(transaction);
            }
        }
    }

    /**
     * Effectue des traitements avant un ajout dans la BD.
     *
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {

        // Incrémente de +1 le numéro
        setAffiliationId(this._incCounter(transaction, "0"));

        setDateCreation(JACalendar.todayJJsMMsAAAA());

        if (getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_NON_ACTIF)) {
            AFAffiliationManager aff = new AFAffiliationManager();
            aff.setForAffilieNumero(getAffilieNumero());
            aff.setSession(getSession());
            aff.find();
            if (aff.size() == 0) {
                setDateTent(JACalendar.todayJJsMMsAAAA());
            }
        }
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        boolean idAffiliationUtilise = false;
        String error = getSession().getLabel("3675") + "\n";
        // Test si existance dans les CI
        if (CIUtil.returnNombreEcitureByIdAffiliation(getAffiliationId(), getSession()) > 0) {
            idAffiliationUtilise = true;
            error += getSession().getLabel("3675_PAVO") + "\n";
        }
        // Test si existance dans un compte annexe
        if (CAUtil.returnCompteAnnexe(getSession(), getAffilieNumero(), "", "", "", "", "", "") != null) {
            idAffiliationUtilise = true;
            error += getSession().getLabel("3675_OSIRIS") + "\n";
        }
        // Test si existance dans la facturation
        if (FAUtil.returnNombreEnteteFacture(getSession(), "", "", getAffilieNumero(), getIdTiers()) > 0) {
            idAffiliationUtilise = true;
            error += getSession().getLabel("3675_MUSCA") + "\n";
        }
        // Test si existance dans la gestion des envois
        if (AFUtil.returnNombreEnvoi(getSession(), getAffiliationId(), getIdTiers()) > 0) {
            idAffiliationUtilise = true;
            error += getSession().getLabel("3675_LEO") + "\n";
        }
        // Test si existance ARC
        if (HEUtil.returnNombreInfo(getSession(), HEInfos.CS_NUMERO_AFFILIE, getAffilieNumero(), "", "") > 0) {
            idAffiliationUtilise = true;
            error += getSession().getLabel("3675_HERMES") + "\n";
        }

        // Test si existance dossier AF
        /*
         * if (xxxx) { idAffiliationUtilise = true; error += getSession().getLabel("3675_AF"); }
         */
        // Test si existance contrôle employeur
        if (AFUtil.isNouveauControleEmployeur(getSession())) {
            if (CEUtils.returnNombreControle(getAffiliationId(), "", "", "", false, false, getSession()) > 0) {
                idAffiliationUtilise = true;
                error += getSession().getLabel("3675_HERCULE") + "\n";
            }
        } else {
            if (AFUtil.returnNombreControle(getAffiliationId(), "", "", "", getSession()) > 0) {
                idAffiliationUtilise = true;
                error += getSession().getLabel("3675_HERCULE") + "\n";
            }

        }
        // Test si existance cot. pers.
        if (CPToolBox.returnNombreDecision(getSession(), "", getAffiliationId(), "", Boolean.FALSE) > 0) {
            idAffiliationUtilise = true;
            error += getSession().getLabel("3675_PHENIX") + "\n";
        }
        // Test si existance déclaration de salaire
        if (DSUtil.returnNombreDeclaration(getSession(), "", getAffiliationId(), "", "", "", "") > 0) {
            idAffiliationUtilise = true;
            error += getSession().getLabel("3675_DRACO") + "\n";
        }
        // Si affiliation utilisée
        if (idAffiliationUtilise) {
            _addError(transaction, error);
        }

        // Test si il y a des affiliations liées a celle ci
        AFLienAffiliationManager lienAffiliationManager = new AFLienAffiliationManager();
        lienAffiliationManager.setSession(getSession());
        lienAffiliationManager.setForAff_AffiliationId(getAffiliationId());
        lienAffiliationManager.find(transaction);

        if (lienAffiliationManager.size() >= 1) {
            _addError(transaction, getSession().getLabel("1440"));
        }

        // Test si l'affiliation est provisoire
        if (!isTraitement().booleanValue()) {
            _addError(transaction, getSession().getLabel("1050"));
        }
    }

    /**
     * Effectue des traitements avant une mise à jour dans la BD.
     *
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        // Création des annonces de mutation
        creationAnnonceAffiliation(transaction);

        if (!isRulesByPass()) {
            AFIDERules.controleGenerateAnnonceOnAffiliation(getSession(), this, getOldAffiliation());
        }
        // Lors de la radiation d'un affilié de type "Employeur" ou
        // "Indépendant - Employeur"
        // s'il possède une particularité "Fiche partielle" on affiche une
        // erreur.
        if (!JadeStringUtil.isBlankOrZero(getDateFin())) {
            if (CodeSystem.TYPE_AFFILI_EMPLOY.equals(getTypeAffiliation())
                    || CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equals(getTypeAffiliation())) {
                if (!JadeStringUtil.isBlankOrZero(getAffiliationId())) {
                    AFParticulariteAffiliationManager particulariteMana = new AFParticulariteAffiliationManager();
                    particulariteMana.setSession(getSession());
                    particulariteMana.setForAffiliationId(getAffiliationId());
                    particulariteMana.setForParticularite(CodeSystem.PARTIC_AFFILIE_FICHE_PARTIELLE);
                    particulariteMana.find();
                    if (particulariteMana.size() > 0) {
                        _addError(transaction, getSession().getLabel("PLAUSI_PARTICULARITE")
                                + getSession().getCodeLibelle(CodeSystem.PARTIC_AFFILIE_FICHE_PARTIELLE)
                                + getSession().getLabel("PLAUSI_PARTICULARITE2") + " " + getAffilieNumero());
                    }
                }
            }

            // // Annonce de radiation IDE
            //
            // // Si motif de fin <> de changement de caisse => annonce iDE de radiation
            // // sinon annonce de désenregistrement
            // if (!CodeSystem.MOTIF_FIN_CHANGEMENT_CAISSE.equalsIgnoreCase(getMotifFin())) {
            // // cas de sélection d'un cas avec statut radié/inactif et date de fin de radiation anticipée (date
            // // de fin > date du jour)
            // if (AFIDEUtil.isCasRadieAnticipe(getSession(), this)) {
            // AFIDEUtil.generateAnnonceReactivationIde(getSession(), this);
            // }
            // AFIDEUtil.generateAnnonceRadiationIde(getSession(), this);
            //
            // } else {
            // if (getOldAffiliation().getDateFin().isEmpty()) {
            // AFIDEUtil.generateAnnonceDesenregistrementIde(getSession(), this);
            // // switch (casEnregistrement) {
            // // case 1:
            // // setIdeFinAnnonceActive(getDateFin());
            // // break;
            // // case 2:
            // // setIdeDebutAnnoncePassive(getDateFin());
            // // setIdeFinAnnoncePassive("");
            // // break;
            // //
            // // default:
            // // break;
            // // }
            // }
            // }
            // } else {
            // // Réactivation annonce IDE si ancienne date de fin <> 0 (suppression radiation)
            // if (!JadeStringUtil.isBlankOrZero(getOldAffiliation().getDateFin())
            // || CodeSystem.STATUT_IDE_RADIE.equalsIgnoreCase(getIdeStatut())
            // || CodeSystem.STATUT_IDE_DEFINITIF_RADIE.equalsIgnoreCase(getIdeStatut())) {
            // AFIDEUtil.generateAnnonceReactivationIde(getSession(), this);
            // }

        }
        // *******************************************************************
        // Gestion du code noga / Si pas paritaire ou paritaire personnel
        // On le set à 0 car ne doit pas être présent pour les autres.
        // *******************************************************************
        if (_getGenre() != Genre.PARITAIRE && _getGenre() != Genre.PARITAIRE_ET_PERSONNEL) {
            setCodeSUVA("");
        }

    }

    private void creationAnnonceAffiliation(BTransaction transaction) throws Exception {
        String today = JACalendar.todayJJsMMsAAAA();
        String now = (new JATime(JACalendar.now())).toStr("");

        // *********************************************************
        // Ancienne valeur Affiliation
        // *********************************************************
        AFAffiliation oldAffiliation = new AFAffiliation();
        oldAffiliation.setAffiliationId(getAffiliationId());
        oldAffiliation.setIdTiers(getIdTiers());
        oldAffiliation.setSession(getSession());
        oldAffiliation.retrieve(transaction);
        setOldAffiliation(oldAffiliation);

        // Initialisation Annonce
        AFAnnonceAffilie annonce = new AFAnnonceAffilie();
        annonce.setUtilisateur(getSession().getUserId());
        annonce.setSession(getSession());
        annonce.setAffiliationId(oldAffiliation.getAffiliationId());
        annonce.setDateEnregistrement(today);
        annonce.setHeureEnregistrement(now);

        // Numéro Affilié
        if (!oldAffiliation.getAffilieNumero().equals(getAffilieNumero())) {

            annonce.setChampModifier(CodeSystem.CHAMPS_MOD_NUM_AFFILIE);
            annonce.setChampAncienneDonnee(oldAffiliation.getAffilieNumero());
            annonce.add(transaction);
        }
        // Numéro Affilié
        if (!oldAffiliation.getAncienAffilieNumero().equals(getAncienAffilieNumero())) {
            annonce.setChampModifier(CodeSystem.CHAMPS_MOD_NUM_AF_ANCIEN);
            annonce.setChampAncienneDonnee(oldAffiliation.getAncienAffilieNumero());
            annonce.add(transaction);
        }
        // Type Affiliation
        if (!oldAffiliation.getTypeAffiliation().equals(getTypeAffiliation())) {

            annonce.setChampModifier(CodeSystem.CHAMPS_MOD_TYPE_AFFILIE);
            annonce.setChampAncienneDonnee(oldAffiliation.getTypeAffiliation());
            annonce.add(transaction);
        }
        // Date Début
        if (!oldAffiliation.getDateDebut().equals(getDateDebut())) {

            annonce.setChampModifier(CodeSystem.CHAMPS_MOD_DATE_DEBUT);
            annonce.setChampAncienneDonnee(oldAffiliation.getDateDebut());
            annonce.add(transaction);
        }
        // Date Fin
        if (!oldAffiliation.getDateFin().equals(getDateFin())) {

            annonce.setChampModifier(CodeSystem.CHAMPS_MOD_DATE_FIN);
            annonce.setChampAncienneDonnee(oldAffiliation.getDateFin());
            annonce.add(transaction);
        }
        // Périodicité
        if (!oldAffiliation.getPeriodicite().equals(getPeriodicite())) {

            annonce.setChampModifier(CodeSystem.CHAMPS_MOD_PERIODICITE);
            annonce.setChampAncienneDonnee(oldAffiliation.getPeriodicite());
            annonce.add(transaction);
        }
        // Branche Economique
        if (!oldAffiliation.getBrancheEconomique().equals(getBrancheEconomique())) {

            annonce.setChampModifier(CodeSystem.CHAMPS_MOD_BRANCH_ECONO);
            annonce.setChampAncienneDonnee(oldAffiliation.getBrancheEconomique());
            annonce.add(transaction);
        }
        // Personnalité Juridique
        if (!oldAffiliation.getPersonnaliteJuridique().equals(getPersonnaliteJuridique())) {

            annonce.setChampModifier(CodeSystem.CHAMPS_MOD_PERSON_JURI);
            annonce.setChampAncienneDonnee(oldAffiliation.getPersonnaliteJuridique());
            annonce.add(transaction);
        }
        // Exoneration general
        if (!oldAffiliation.isExonerationGenerale().equals(isExonerationGenerale())) {

            annonce.setChampModifier(CodeSystem.CHAMPS_MOD_EXO_GENERAL);
            annonce.setChampAncienneDonnee(oldAffiliation.isExonerationGenerale().toString());
            annonce.add(transaction);
        }
        // Irrecouvrable
        if (!oldAffiliation.isIrrecouvrable().equals(isIrrecouvrable())) {

            annonce.setChampModifier(CodeSystem.CHAMPS_MOD_IRRECOU);
            annonce.setChampAncienneDonnee(oldAffiliation.isIrrecouvrable().toString());
            annonce.add(transaction);
        }
        // Occasionnel
        if (!oldAffiliation.isOccasionnel().equals(isOccasionnel())) {

            annonce.setChampModifier(CodeSystem.CHAMPS_MOD_PERSON_OCCAS);
            annonce.setChampAncienneDonnee(oldAffiliation.isOccasionnel().toString());
            annonce.add(transaction);
        }
        // Personnel de Maison
        if (!oldAffiliation.isPersonnelMaison().equals(isPersonnelMaison())) {

            annonce.setChampModifier(CodeSystem.CHAMPS_MOD_PERSON_MAISON);
            annonce.setChampAncienneDonnee(oldAffiliation.isPersonnelMaison().toString());
            annonce.add(transaction);
        }
    }

    /**
     * Cette méthode retourne une cotisation active, c'est une cotisation qui n'a pas de date de fin
     *
     * @param transaction
     * @param idAffiliation
     * @param genreCotisation
     * @param typeCotisation
     * @return
     */
    public AFCotisation _cotisationActive(globaz.globall.db.BTransaction transaction, String idAffiliation,
            String genreCotisation, String typeCotisation) {
        try {
            AFCotisationManager cotisationManager = new AFCotisationManager();
            cotisationManager.setForAffiliationId(idAffiliation);
            cotisationManager.setSession(getSession());
            cotisationManager.setForGenreAssurance(genreCotisation);
            cotisationManager.setForTypeAssurance(typeCotisation);
            cotisationManager.changeManagerSize(BManager.SIZE_NOLIMIT);
            // on veut des cotisations sans date de fin
            cotisationManager.setForDateFin("0");
            cotisationManager.find(transaction);
            for (int i = 0; i < cotisationManager.size(); i++) {
                AFCotisation cotisation = (AFCotisation) cotisationManager.getEntity(i);
                // il faut récupérer la cotisation active, elle ne doit donc pas avoir de date de fin
                if (JadeStringUtil.isEmpty(cotisation.getDateFin())) {
                    return cotisation;
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return null;
    }

    /**
     * Retourne la Cotisation qui correspond au critères de recherche.
     *
     * @param transaction
     * @param idAffiliation
     * @param genre
     * @param type
     * @param dateDebutCP
     * @param dateFinCP
     * @return
     * @throws Exception
     */
    public AFCotisation _cotisation(globaz.globall.db.BTransaction transaction, String idAffiliation, String genre,
            String type, String dateDebutCP, String dateFinCP, int test) throws Exception {
        try {
            AFCotisationManager cotisationManager = new AFCotisationManager();
            cotisationManager.setForAffiliationId(idAffiliation);
            cotisationManager.setSession(getSession());
            cotisationManager.setForGenreAssurance(genre);
            cotisationManager.setForTypeAssurance(type);
            cotisationManager.changeManagerSize(BManager.SIZE_NOLIMIT);
            cotisationManager.find(transaction);
            for (int i = 0; i < cotisationManager.size(); i++) {
                AFCotisation cotisation = (AFCotisation) cotisationManager.getEntity(i);
                // Cas 1 : Test si cotisation appartient ou comprend la
                // période transmises
                // (utilisé pour cot. pers.)
                if ((test == 1) || (test == 4)) {
                    if ((BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dateDebutCP, cotisation.getDateFin()) || JadeStringUtil
                            .isBlankOrZero(cotisation.getDateFin()))
                            && BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), dateFinCP,
                                    cotisation.getDateDebut())) {
                        // Po 9019 - Correction PO 8230
                        if ((test == 4) || !cotisation.getDateDebut().equals(cotisation.getDateFin())) {
                            return cotisation;
                        }
                    }
                } else if (test == 2) {
                    // Cas 2 : Test si cotisation supérieure à la période
                    // transmise
                    // Utilisé pour LettreAffiliation
                    if (!BSessionUtil.compareDateFirstGreater(getSession(), cotisation.getDateDebut(), dateFinCP)) {
                        if (JadeStringUtil.isEmpty(cotisation.getDateFin())) {
                            return cotisation;

                        } else {
                            if (!BSessionUtil.compareDateFirstGreater(getSession(), dateFinCP, cotisation.getDateFin())) {
                                return cotisation;
                            }
                        }
                    }
                } else if (test == 3) {
                    // Cas 3 : test pour les demandes de revenu et bilan
                    // la date de fin d'affiliation peut ne pas être
                    // renseignée
                    if ((BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dateDebutCP, cotisation.getDateFin()) || JadeStringUtil
                            .isBlankOrZero(cotisation.getDateFin()))
                            && (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), dateFinCP,
                                    cotisation.getDateDebut()) || JadeStringUtil.isBlankOrZero(dateFinCP))) {
                        return cotisation;
                    }
                }
            }
        } catch (Exception e) {
            _addError(transaction, getSession().getLabel("980") + idAffiliation);
        }
        return null;
    }

    /**
     * Retourne la Cotisation qui correspond au critères de recherche.
     *
     * @param transaction
     * @param idAffiliation
     * @param genre
     * @param type
     * @param dateDebutCP
     * @param dateFinCP
     * @return
     * @throws Exception
     */
    public AFCotisation _cotisationSansTestFinAffiliation(globaz.globall.db.BTransaction transaction,
            String idAffiliation, String genre, String type, String dateFinCP) throws Exception {
        try {
            AFCotisationManager cotisationManager = new AFCotisationManager();
            cotisationManager.setForAffiliationId(idAffiliation);
            cotisationManager.setForGenreAssurance(genre);
            cotisationManager.setForTypeAssurance(type);
            cotisationManager.setSession(getSession());
            cotisationManager.changeManagerSize(BManager.SIZE_NOLIMIT);
            cotisationManager.find(transaction);
            for (int i = 0; i < cotisationManager.size(); i++) {
                AFCotisation cotisation = (AFCotisation) cotisationManager.getEntity(i);
                if (!BSessionUtil.compareDateFirstGreater(getSession(), cotisation.getDateDebut(), dateFinCP)) {
                    if (JadeStringUtil.isEmpty(cotisation.getDateFin())) {
                        return cotisation;
                    } else {
                        if (!BSessionUtil.compareDateFirstGreater(getSession(), cotisation.getDateDebut(),
                                cotisation.getDateFin())) {
                            return cotisation;
                        }
                    }
                }
            }
        } catch (Exception e) {
            _addError(transaction, getSession().getLabel("980") + idAffiliation);
        }
        return null;
    }

    /**
     * Création d'une Affiliation.
     *
     * @param transaction
     * @param session
     * @param dateDebut
     * @param genre
     * @param idTiers
     * @param numAffilie
     * @return
     * @throws Exception
     */
    public String _creationAffiliation(BTransaction transaction, BSession session, String dateDebut, String genre,
            String idTiers, String numAffilie) throws Exception {

        AFAffiliation affiliation = new AFAffiliation();
        AFCotisation cotisation = new AFCotisation();
        BSession naosSession = new BSession("NAOS");
        session.connectSession(naosSession);

        // ---------------------tent------------------------------------------
        affiliation.setDateCreation(JACalendar.todayJJsMMsAAAA());

        if ((genre.equals(CodeSystem.TYPE_AFFILI_NON_ACTIF)) || (genre.equals(CodeSystem.TYPE_AFFILI_PROVIS))) {
            AFAffiliationManager aff = new AFAffiliationManager();
            aff.setForAffilieNumero(numAffilie);
            aff.setSession(naosSession);
            aff.find();
            if (aff.size() == 0) {
                affiliation.setDateTent(JACalendar.todayJJsMMsAAAA());
            }
        }
        // --------------------tent---------------------------------------

        affiliation.setDateDebut(dateDebut);
        affiliation.setTypeAffiliation(genre);
        affiliation.setIdTiers(idTiers);
        affiliation.setAffilieNumero(numAffilie);
        affiliation.setMotifCreation(CodeSystem.MOTIF_AFFIL_NOUVELLE_AFFILIATION);
        affiliation.setBrancheEconomique(CodeSystem.BRANCHE_ECO_PERS_VIVANT_PENSION);
        affiliation.setPeriodicite(CodeSystem.PERIODICITE_TRIMESTRIELLE);
        affiliation.setPersonnaliteJuridique(CodeSystem.PERS_JURIDIQUE_NA);

        if (genre.equals(CodeSystem.TYPE_AFFILI_PROVIS)) {
            affiliation.setTraitement(new Boolean(true));
            affiliation.setTypeAffiliation(CodeSystem.TYPE_AFFILI_NON_ACTIF);
        }
        affiliation.setSession(naosSession);
        affiliation.add(transaction);

        cotisation._creationCotisation(transaction, naosSession, affiliation.getAffiliationId(),
                affiliation.getIdTiers(), "100");

        return "0";
    }

    /**
     * Retourne l'affiliation précédente.
     *
     * @return l'affiliation précédente
     */
    public AFAffiliation _getDerniereAffiliation() {

        AFAffiliation myAff = new AFAffiliation();
        myAff.setSession(getSession());

        AFAffiliationManager manager = new AFAffiliationManager();

        manager.setSession(getSession());

        try {
            manager.setForIdTiers(getIdTiers());
            manager.find();
            String temp = new String();
            for (int i = 0; i < manager.size(); i++) {
                AFAffiliation aff = (AFAffiliation) manager.getEntity(i);

                if (aff.getDateFin().equalsIgnoreCase("")) {
                    i = manager.size();
                    myAff = aff;
                } else {
                    if (BSessionUtil.compareDateFirstGreater(getSession(), aff.getDateFin(), temp)) {
                        myAff = aff;
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        if (myAff.isNew()) {
            return null;
        } else {
            return myAff;
        }
    }

    /**
     * Cette méthode retourne la description du tiers à savoir : nom, adresse et localité
     *
     * @return Une String contenant la description du tiers
     */
    public String _getDescriptionTiers() {
        String _afficher = "";
        if (getTiers() != null) {
            try {
                TIAdresseDataSource data = getTiers().getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        ICommonConstantes.CS_APPLICATION_COTISATION,
                        JACalendar.format(JACalendar.today().toString(), JACalendar.FORMAT_DDsMMsYYYY), true);
                if (data == null) {
                    return "";
                } else {
                    Hashtable<?, ?> table = data.getData();
                    _afficher = (String) table.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D1) + " ";
                    _afficher = _afficher + (String) table.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D2) + "\n";
                    _afficher = _afficher + (String) table.get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE) + " "
                            + (String) table.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO) + "\n";
                    _afficher = _afficher + (String) table.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA) + " "
                            + (String) table.get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return _afficher;
    }

    /**
     * Retourne le genre d'affiliation paritaire ou personnel en fonction du type d'affiliation
     *
     * @return le genre d'affiliation PARITAIRE, PERSONNEL, PARITAIRE ET PERSONNEL, AUTRE
     */
    public Genre _getGenre() {
        if (CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equals(getTypeAffiliation())) {
            return Genre.PARITAIRE_ET_PERSONNEL;
        } else if (CodeSystem.TYPE_AFFILI_EMPLOY.equals(getTypeAffiliation())) {
            return Genre.PARITAIRE;
        } else if (CodeSystem.TYPE_AFFILI_EMPLOY_D_F.equals(getTypeAffiliation())) {
            return Genre.PARITAIRE;
        } else if (CodeSystem.TYPE_AFFILI_LTN.equals(getTypeAffiliation())) {
            return Genre.PARITAIRE;
        } else if (CodeSystem.TYPE_AFFILI_INDEP.equals(getTypeAffiliation())) {
            return Genre.PERSONNEL;
        } else if (CodeSystem.TYPE_AFFILI_NON_ACTIF.equals(getTypeAffiliation())) {
            return Genre.PERSONNEL;
        } else if (CodeSystem.TYPE_AFFILI_SELON_ART_1A.equals(getTypeAffiliation())) {
            return Genre.PERSONNEL;
        } else if (CodeSystem.TYPE_AFFILI_TSE.equals(getTypeAffiliation())) {
            return Genre.PERSONNEL;
        } else if (CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE.equals(getTypeAffiliation())) {
            return Genre.PERSONNEL;
        } else {
            return Genre.AUTRE;
        }
    }

    /**
     * Retourne l'affiliation précédente par rapport a la cotisation.
     *
     * @param session
     * @param currentCotisation
     * @return l'affiliation précédent
     */
    public AFAffiliation _getPreviousAffiliation(BSession session, AFCotisation currentCotisation) {
        AFAffiliation myAff = null;

        AFAffiliationManager manager = new AFAffiliationManager();
        manager.setSession(session);
        manager.setForTillDateDebut(currentCotisation.getDateDebut());
        manager.forIsTraitement(false);
        manager.setOrder(" MADDEB DESC ");

        try {
            manager.find();
            // retourner la première entity trouvé car c'est l'affiliation
            // précédente
            if (manager.getSize() > 0) {
                myAff = (AFAffiliation) manager.getFirstEntity();
            }
            return myAff;
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return null;
        }
    }

    /**
     * Retour le nom de la Table.
     *
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return AFAffiliation.TABLE_NAME;
    }

    /**
     * On vérifie que deux affiliations de même type (paritaire, personnel) portant le même numéro ne se chevauchent pas
     *
     * @param affiliation
     *            l'affiliation à vérifier
     * @return true si les affiliations se chevauchent, false sinon
     * @throws Exception
     *             en cas d'erreur
     */
    private boolean _isPeriodeChevauchante(AFAffiliation affiliation) throws Exception {
        if (affiliation == null) {
            return false;
        }
        // On ne compare pas la même affiliation
        if (getAffiliationId().equals(affiliation.getAffiliationId())) {
            return false;
        }
        String dateDebut = affiliation.getDateDebut();
        String dateFin = affiliation.getDateFin();
        // plafonner la date de fin si l'affiliation est active
        if (JadeStringUtil.isBlankOrZero(dateFin)) {
            dateFin = "31.12.2999";
        }
        String thisDateDebut = getDateDebut();
        String thisDateFin = getDateFin();
        // plafonner la date de fin si l'affiliation est active
        if (JadeStringUtil.isBlankOrZero(thisDateFin)) {
            thisDateFin = "31.12.2999";
        }
        // Date de début = date fin -> l'affiliation est inactive
        if (dateDebut.equals(dateFin) || thisDateDebut.equals(thisDateFin)) {
            return false;
        }
        // Si les périodes se chevauchent
        if (BSessionUtil.compareDateBetweenOrEqual(getSession(), thisDateDebut, thisDateFin, dateDebut)
                || BSessionUtil.compareDateBetweenOrEqual(getSession(), thisDateDebut, thisDateFin, dateFin)
                || BSessionUtil.compareDateBetweenOrEqual(getSession(), dateDebut, dateFin, thisDateDebut)
                || BSessionUtil.compareDateBetweenOrEqual(getSession(), dateDebut, dateFin, thisDateFin)) {
            // Si le no d'affilié est identique et de même type ou de type paritaire et personnel, il y a chevauchement
            if (getAffilieNumero().equals(affiliation.getAffilieNumero())
                    && (_getGenre().equals(affiliation._getGenre()) || (_getGenre()
                            .equals(Genre.PARITAIRE_ET_PERSONNEL) && !affiliation._getGenre().equals(Genre.AUTRE)))) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }

    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     *
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idTiers = statement.dbReadNumeric(AFAffiliation.FIELDNAME_TIER_ID);
        affiliationId = statement.dbReadNumeric(AFAffiliation.FIELDNAME_AFFILIATION_ID);
        affilieNumero = statement.dbReadString("MALNAF");
        dateDebut = statement.dbReadDateAMJ("MADDEB");
        dateFin = statement.dbReadDateAMJ("MADFIN");
        datePrecDebut = statement.dbReadDateAMJ("MADXDE");
        datePrecFin = statement.dbReadDateAMJ("MADXFI");
        motifFin = statement.dbReadNumeric("MATMOT");
        typeAffiliation = statement.dbReadNumeric("MATTAF");
        brancheEconomique = statement.dbReadNumeric("MATBRA");
        personnaliteJuridique = statement.dbReadNumeric("MATJUR");
        exonerationGenerale = statement.dbReadBoolean("MABEXO");
        dateEditionFiche = statement.dbReadDateAMJ("MADFIC");
        dateEditionFicheM1 = statement.dbReadNumeric("MADFI1");
        dateEditionFicheM2 = statement.dbReadNumeric("MADFI2");
        declarationSalaire = statement.dbReadNumeric("MATDEC");
        membreComite = statement.dbReadNumeric("MATMCO");
        irrecouvrable = statement.dbReadBoolean("MABIRR");
        occasionnel = statement.dbReadBoolean("MABOCC");
        personnelMaison = statement.dbReadBoolean("MABMAI");
        liquidation = statement.dbReadBoolean("MABLIQ");
        traitement = statement.dbReadBoolean("MABTRA");
        releveParitaire = statement.dbReadBoolean("MABREP");
        relevePersonnel = statement.dbReadBoolean("MABREI");
        envoiAutomatiqueAnnonceSalaires = statement.dbReadBoolean("MABEAA");
        massePeriodicite = statement.dbReadNumeric("MAMMAP", 2);
        masseAnnuelle = statement.dbReadNumeric("MAMMAA", 2);
        periodicite = statement.dbReadNumeric("MATPER");
        numeroIDE = statement.dbReadString("MALFED");
        taxeCo2Taux = statement.dbReadNumeric("MAMTCO", 5);
        taxeCo2Fraction = statement.dbReadNumeric("MAMFCO", 5);
        ancienAffilieNumero = statement.dbReadString("MALNAA");
        caisseProvenance = statement.dbReadNumeric("MAICPR");
        caissePartance = statement.dbReadNumeric("MAICPA");
        motifCreation = statement.dbReadNumeric("MATMCR");
        bonusMalus = statement.dbReadBoolean("MABBMA");
        dateTent = statement.dbReadDateAMJ("MADTEN");
        dateCreation = statement.dbReadDateAMJ("MADCRE");
        codeNoga = statement.dbReadNumeric("MATCDN");
        typeAssocie = statement.dbReadNumeric("MATTAS");
        raisonSocialeCourt = statement.dbReadString("MADESC");
        raisonSociale = statement.dbReadString("MADESL");
        dateDemandeAffiliation = statement.dbReadDateAMJ("MADDEM");
        codeFacturation = statement.dbReadNumeric("MATCFA");
        accesSecurite = statement.dbReadNumeric("MATSEC");
        codeSUVA = statement.dbReadNumeric("MATSUV");
        convention = statement.dbReadNumeric("MACONV");
        // D0050 - Cahmp IDE
        ideAnnoncePassive = statement.dbReadBoolean("MATPAS");
        ideNonAnnoncante = statement.dbReadBoolean("MATNAN");
        ideStatut = statement.dbReadNumeric("MATSTA");
        ideRaisonSociale = statement.dbReadString("MATRSO");
        // D0181 - activité
        activite = statement.dbReadString("MATACT");
        majFAD = statement.dbReadBoolean(AFAffiliation.FIELDNAME_MAJ_FAD);
    }

    /**
     * Retour l'Affiliation qui correspond au critères de recherche.
     *
     * @param transaction
     * @param noAffilie
     * @param annee
     * @param genre
     * @param mode
     *            de recherche
     * @return
     * @throws Exception
     */
    public AFAffiliation _retourAffiliation(BTransaction transaction, String noAffilie, String annee, String genre,
            int modeRecherche) throws Exception {

        AFAffiliation affiliationDemander = null;
        int nb = 0;

        try {
            int anneeControle = Integer.parseInt(annee);

            AFAffiliationManager affiliationManager = new AFAffiliationManager();
            if (modeRecherche == 1) {
                affiliationManager.setForIdTiers(noAffilie);
            } else {
                affiliationManager.setForAffilieNumero(noAffilie);
            }
            affiliationManager.setSession(getSession());
            affiliationManager.forIsTraitement(Boolean.FALSE);
            affiliationManager.find(transaction);

            for (int i = 0; i < affiliationManager.size(); i++) {
                AFAffiliation affiliation = (AFAffiliation) affiliationManager.getEntity(i);

                if (genre.equalsIgnoreCase(affiliation.getTypeAffiliation())
                        && !(affiliation.getDateDebut().equalsIgnoreCase(affiliation.getDateFin()))) {

                    int anneeDebAffiliation = JACalendar.getYear(affiliation.getDateDebut());
                    int anneeFinAffiliation = JACalendar.getYear(affiliation.getDateFin());

                    if (((anneeControle >= anneeDebAffiliation) && (anneeControle <= anneeFinAffiliation))
                            || ((anneeControle >= anneeDebAffiliation) && (anneeFinAffiliation == 0))) {

                        affiliationDemander = affiliation;
                        nb++;
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            _addError(transaction, "erreur");
        }
        if (nb > 1) {
            return null;
        } else {
            return affiliationDemander;
        }
    }

    /**
     * Retour l'Affiliation qui correspond au critères de recherche.
     *
     * @param transaction
     * @param noAffilie
     * @param dateDebutCP
     * @param dateFinCP
     * @param genre
     * @param mode
     *            de recherche
     * @return
     * @throws Exception
     */
    public AFAffiliation _retourAffiliation(BTransaction transaction, String noAffilie, String dateDebutCP,
            String dateFinCP, String genre, int modeRecherche) throws Exception {

        Vector<AFAffiliation> affTrouve = new Vector<AFAffiliation>();
        Vector<Boolean> cotiActive = new Vector<Boolean>();
        int nb = 0;

        try {
            AFAffiliationManager affiliationManager = new AFAffiliationManager();
            if (modeRecherche == 1) {
                affiliationManager.setForIdTiers(noAffilie);
            } else {
                affiliationManager.setForAffilieNumero(noAffilie);
            }

            affiliationManager.setSession(getSession());
            affiliationManager.forIsTraitement(Boolean.FALSE);
            affiliationManager.find(transaction);

            for (int i = 0; i < affiliationManager.size(); i++) {

                AFAffiliation affiliation = (AFAffiliation) affiliationManager.getEntity(i);

                if (genre.equalsIgnoreCase(affiliation.getTypeAffiliation())
                        && !(affiliation.getDateDebut().equalsIgnoreCase(affiliation.getDateFin()))) {

                    if (JadeStringUtil.isBlankOrZero(affiliation.getDateFin())) {

                        if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), dateDebutCP,
                                affiliation.getDateDebut())) {
                            nb++;
                            affTrouve.add(affiliation);
                            // Test si cotisatoin active
                            AFCotisationJAssuranceManager cotiManager = new AFCotisationJAssuranceManager();
                            cotiManager.setSession(getSession());
                            cotiManager.setForAffiliationId(affiliation.getAffiliationId());
                            cotiManager.setForAnneeActive(JACalendar.getYear(dateDebutCP) + "");
                            cotiManager.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
                            if (cotiManager.getCount() > 0) {
                                cotiActive.add(Boolean.TRUE);
                            } else {
                                cotiActive.add(Boolean.FALSE);
                            }

                        }
                    } else {
                        if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), dateDebutCP,
                                affiliation.getDateDebut())
                                && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dateFinCP,
                                        affiliation.getDateFin())) {
                            nb++;
                            affTrouve.add(affiliation);
                            // Test si cotisatoin active
                            AFCotisationJAssuranceManager cotiManager = new AFCotisationJAssuranceManager();
                            cotiManager.setSession(getSession());
                            cotiManager.setForAffiliationId(affiliation.getAffiliationId());
                            cotiManager.setForAnneeActive(JACalendar.getYear(dateDebutCP) + "");
                            cotiManager.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
                            if (cotiManager.getCount() > 0) {
                                cotiActive.add(Boolean.TRUE);
                            } else {
                                cotiActive.add(Boolean.FALSE);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            _addError(transaction, "erreur");
        }
        if (nb == 0) {
            return null;
        } else if (nb == 1) {
            return affTrouve.firstElement();
        } else {
            // Retourner la première affiliation qui a des cotisations actives
            if (cotiActive.contains(Boolean.TRUE)) {
                for (int i = 0; i < cotiActive.size(); i++) {
                    if (cotiActive.elementAt(i).equals(Boolean.TRUE)) {
                        return affTrouve.elementAt(i);
                    }
                }
            }
            // Si rien trouvé => retourner la première affiliation
            return affTrouve.firstElement();
        }
    }

    /**
     * Retour l'IdAffiliation de l'Affiliation qui correspond au critères de recherche.
     *
     * @param transaction
     * @param noAffilie
     * @param annee
     * @param genre
     * @return
     * @throws Exception
     */
    public String _retourIdAffiliation(BTransaction transaction, String noAffilie, String annee, String genre)
            throws Exception {

        String affiliationRetour = "";
        int nb = 0;

        try {
            int anneeControle = Integer.parseInt(annee);

            AFAffiliationManager affiliationManager = new AFAffiliationManager();
            affiliationManager.setForAffilieNumero(noAffilie);
            affiliationManager.setSession(getSession());
            affiliationManager.find(transaction);

            for (int i = 0; i < affiliationManager.size(); i++) {
                AFAffiliation affiliation = (AFAffiliation) affiliationManager.getEntity(i);

                if (genre.equalsIgnoreCase(affiliation.getTypeAffiliation())) {

                    int anneeDebAffiliation = JACalendar.getYear(affiliation.getDateDebut());
                    int anneeFinAffiliation = JACalendar.getYear(affiliation.getDateFin());

                    if (((anneeControle >= anneeDebAffiliation) && (anneeControle <= anneeFinAffiliation))
                            || ((anneeControle >= anneeDebAffiliation) && (anneeFinAffiliation == 0))) {

                        affiliationRetour = affiliation.getAffiliationId();
                        nb++;
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            _addError(transaction, "erreur");
        }

        if (nb > 1) {
            _addError(transaction, getSession().getLabel("970"));
            return "";
        } else {
            return affiliationRetour;
        }
    }

    /**
     * Retour l'IdAffiliation de l'Affiliation qui correspond au critères de recherche.
     *
     * @param transaction
     * @param noAffilie
     * @param dateDebutCP
     * @param dateFinCP
     * @param genre
     * @return
     * @throws Exception
     */
    public String _retourIdAffiliation(BTransaction transaction, String noAffilie, String dateDebutCP,
            String dateFinCP, String genre) throws Exception {
        String affiliationRetour = "";
        int nb = 0;

        try {
            AFAffiliationManager affiliationManager = new AFAffiliationManager();
            affiliationManager.setForAffilieNumero(noAffilie);
            affiliationManager.forIsTraitement(Boolean.FALSE);
            affiliationManager.setSession(getSession());
            affiliationManager.find(transaction);

            for (int i = 0; i < affiliationManager.size(); i++) {
                AFAffiliation affiliation = (AFAffiliation) affiliationManager.getEntity(i);

                if (genre.equalsIgnoreCase(affiliation.getTypeAffiliation())) {

                    if (JadeStringUtil.isBlankOrZero(affiliation.getDateFin())) {
                        if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), dateDebutCP,
                                affiliation.getDateDebut())) {

                            affiliationRetour = affiliation.getAffiliationId();
                            nb++;
                        }
                    } else {
                        if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), dateDebutCP,
                                affiliation.getDateDebut())
                                && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dateFinCP,
                                        affiliation.getDateFin())) {

                            affiliationRetour = affiliation.getAffiliationId();
                            nb++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            _addError(transaction, "erreur");
        }
        if (nb > 1) {
            _addError(transaction, getSession().getLabel("990"));
            return "";
        } else {
            return affiliationRetour;
        }
    }

    /**
     * Retour le nombre d'affiliation qui correspond au critères de recherche.
     *
     * @param transaction
     * @param noAffilie
     * @param annee
     * @param genre
     * @param mode
     *            de recherche
     * @return
     * @throws Exception
     */
    public int _retourNbAffiliation(BTransaction transaction, String noAffilie, String annee, String genre,
            int modeRecherche) throws Exception {
        int nb = 0;
        try {
            int anneeControle = Integer.parseInt(annee);
            AFAffiliationManager affiliationManager = new AFAffiliationManager();
            if (modeRecherche == 1) {
                affiliationManager.setForIdTiers(noAffilie);
            } else {
                affiliationManager.setForAffilieNumero(noAffilie);
            }
            affiliationManager.setSession(getSession());
            affiliationManager.find(transaction);
            for (int i = 0; i < affiliationManager.size(); i++) {
                AFAffiliation affiliation = (AFAffiliation) affiliationManager.getEntity(i);

                if (genre.equalsIgnoreCase(affiliation.getTypeAffiliation())) {

                    int anneeDebAffiliation = JACalendar.getYear(affiliation.getDateDebut());
                    int anneeFinAffiliation = JACalendar.getYear(affiliation.getDateFin());

                    if (((anneeControle >= anneeDebAffiliation) && (anneeControle <= anneeFinAffiliation))
                            || ((anneeControle >= anneeDebAffiliation) && (anneeFinAffiliation == 0))) {
                        nb++;
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            _addError(transaction, "erreur");
        }
        return nb;
    }

    /**
     * Valide le contenu de l'entité.
     *
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        boolean validationOK = true;

        String errorMessage = validationMandatory();
        if (!JadeStringUtil.isEmpty(errorMessage)) {
            _addError(statement.getTransaction(), errorMessage);
            validationOK = false;
        }

        // *********************************************************
        // Test: - Date de Début
        // - Date de Fin
        // *********************************************************
        if (validationOK) {
            validationOK &= _validationDate(statement.getTransaction());
        }

        // *******************************************************************
        // Type Affiliation
        // *******************************************************************
        // Si le type d'affiliation est vide
        if (JadeStringUtil.isIntegerEmpty(getTypeAffiliation())
                && !getBrancheEconomique().equalsIgnoreCase(CodeSystem.BRANCHE_ECO_PERS_VIVANT_PENSION)
                && !getBrancheEconomique().equalsIgnoreCase(CodeSystem.BRANCHE_ECO_PERS_SANS_ACT_LUCRA)) {
            _addError(statement.getTransaction(), getSession().getLabel("210"));
        }

        // si l'affiliation est du type fichier central (affiliation non AVS)
        // ne pas effectuer les plausibilités suivantes
        if (isAffiliationAVS()) {

            // *******************************************************************
            // Personnalité Juridique
            // *******************************************************************
            // Si la personnalité juridique est vide
            if ((JadeStringUtil.isIntegerEmpty(getPersonnaliteJuridique()))) {
                _addError(statement.getTransaction(), getSession().getLabel("230"));
            } else {

                if ((CodeSystem.TYPE_AFFILI_INDEP.equals(getTypeAffiliation())
                        || CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equals(getTypeAffiliation())
                        || CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE.equals(getTypeAffiliation()) || CodeSystem.TYPE_AFFILI_TSE
                            .equals(getTypeAffiliation()))
                        && !CodeSystem.PERS_JURIDIQUE_CODE_A.equals(getSession().getCode(getPersonnaliteJuridique()))) {
                    _addError(statement.getTransaction(), getSession().getLabel("NAOS_PERJUR_CODE_A"));
                } else

                if ((CodeSystem.TYPE_AFFILI_NON_ACTIF.equals(getTypeAffiliation()))
                        && !CodeSystem.PERS_JURIDIQUE_CODE_B.equals(getSession().getCode(getPersonnaliteJuridique()))) {
                    _addError(statement.getTransaction(), getSession().getLabel("NAOS_PERJUR_CODE_B"));
                }
            }

            // *******************************************************************
            // Profession
            // *******************************************************************
            // Si la profession est vide
            if ((JadeStringUtil.isIntegerEmpty(getBrancheEconomique()))) {
                _addError(statement.getTransaction(), getSession().getLabel("220"));
            }

            // *******************************************************************
            // Taux et fraction taxe Co2
            // *******************************************************************
            // Test du taux et de la fraction de la taxe Co2
            if (JadeStringUtil.isIntegerEmpty(getTaxeCo2Taux())) {

                // Taux pas renseigné, test si fraction est renseignée
                if (!JadeStringUtil.isIntegerEmpty(getTaxeCo2Fraction())) {
                    _addError(statement.getTransaction(), getSession().getLabel("240"));
                }
            } else {
                // Taux pas renseigné, test si fraction est renseignée
                if (JadeStringUtil.isIntegerEmpty(getTaxeCo2Fraction())) {
                    _addError(statement.getTransaction(), getSession().getLabel("950"));
                }

                // Taux renseigné, la fraction doit être plus grande ou égale au
                // taux
                BigDecimal bFraction = new BigDecimal(getTaxeCo2Fraction());
                BigDecimal bTaux = new BigDecimal(getTaxeCo2Taux());

                if (bFraction.compareTo(bTaux) == -1) {
                    _addError(statement.getTransaction(), getSession().getLabel("250"));
                }
            }

            // *******************************************************************
            // Declaration Salaire
            // *******************************************************************
            if (((JadeStringUtil.isIntegerEmpty(getDeclarationSalaire())) && (typeAffiliation
                    .equals(CodeSystem.TYPE_AFFILI_EMPLOY)))
            /* && (JadeStringUtil.isIntegerEmpty(getAvsCaisseExterne())) */) {
                _addError(statement.getTransaction(), getSession().getLabel("260"));
            }
            if (((JadeStringUtil.isIntegerEmpty(getDeclarationSalaire())) && (typeAffiliation
                    .equals(CodeSystem.TYPE_AFFILI_EMPLOY_D_F)))
            /* && (JadeStringUtil.isIntegerEmpty(getAvsCaisseExterne())) */) {
                _addError(statement.getTransaction(), getSession().getLabel("260"));
            }
            if (((JadeStringUtil.isIntegerEmpty(getDeclarationSalaire())) && (typeAffiliation
                    .equals(CodeSystem.TYPE_AFFILI_LTN)))
            /* && (JadeStringUtil.isIntegerEmpty(getAvsCaisseExterne())) */) {
                _addError(statement.getTransaction(), getSession().getLabel("260"));
            }
            if (((JadeStringUtil.isIntegerEmpty(getDeclarationSalaire())) && (typeAffiliation
                    .equals(CodeSystem.TYPE_AFFILI_INDEP_EMPLOY)))
            /* && (JadeStringUtil.isIntegerEmpty(getAvsCaisseExterne())) */) {
                _addError(statement.getTransaction(), getSession().getLabel("260"));
            }
            // effectuer les tests sur décl. salaire seulement si
            // "personnel occasionnel" non coché
            if ((!JadeStringUtil.isIntegerEmpty(getDeclarationSalaire()))
                    && (typeAffiliation.equals(CodeSystem.TYPE_AFFILI_INDEP))) {
                _addError(statement.getTransaction(), getSession().getLabel("270"));
            }

            // Modification pour la CCJU car ils ont des non-actifs avec du
            // personnels

            if ((!JadeStringUtil.isIntegerEmpty(getDeclarationSalaire()))
                    && (typeAffiliation.equals(CodeSystem.TYPE_AFFILI_NON_ACTIF))) {
                _addError(statement.getTransaction(), getSession().getLabel("280"));
            }

            if ((!JadeStringUtil.isIntegerEmpty(getDeclarationSalaire()))
                    && (typeAffiliation.equals(CodeSystem.TYPE_AFFILI_SELON_ART_1A))) {
                _addError(statement.getTransaction(), getSession().getLabel("300"));
            }
            if ((!JadeStringUtil.isIntegerEmpty(getDeclarationSalaire()))
                    && (typeAffiliation.equals(CodeSystem.TYPE_AFFILI_PROVIS))) {
                _addError(statement.getTransaction(), getSession().getLabel("310"));
            }

            // *******************************************************************
            // Exoneration Generale
            // *******************************************************************
            if (isExonerationGenerale().booleanValue()) {
                if (typeAffiliation.equals(CodeSystem.TYPE_AFFILI_INDEP)
                        || typeAffiliation.equals(CodeSystem.TYPE_AFFILI_NON_ACTIF)
                        || typeAffiliation.equals(CodeSystem.TYPE_AFFILI_INDEP)) {
                    // TODO checked ??? Test et Label
                    _addError(statement.getTransaction(), getSession().getLabel("320"));
                }
            }

            // *******************************************************************
            // Personnel Maison
            // *******************************************************************
            if (isPersonnelMaison().booleanValue()) {
                if (typeAffiliation.equals(CodeSystem.TYPE_AFFILI_INDEP) ||
                // typeAffiliation.equals(CodeSystem.TYPE_AFFILI_NON_ACTIF) ||
                        typeAffiliation.equals(CodeSystem.TYPE_AFFILI_SELON_ART_1A)) {
                    // TODO checked ??? Test et Label
                    _addError(statement.getTransaction(), getSession().getLabel("320b"));
                }
            }

            // *******************************************************************
            // Occasionnel
            // *******************************************************************
            if (isOccasionnel().booleanValue()) {
                if (typeAffiliation.equals(CodeSystem.TYPE_AFFILI_INDEP)
                        || typeAffiliation.equals(CodeSystem.TYPE_AFFILI_INDEP)) {
                    _addError(statement.getTransaction(), getSession().getLabel("320c"));
                }
            }

            // *******************************************************************
            // Periodicite
            // *******************************************************************
            if (JadeStringUtil.isIntegerEmpty(getPeriodicite())
                    && !getBrancheEconomique().equalsIgnoreCase(CodeSystem.BRANCHE_ECO_PERS_VIVANT_PENSION)
                    && !getBrancheEconomique().equalsIgnoreCase(CodeSystem.BRANCHE_ECO_PERS_SANS_ACT_LUCRA)) {

                _addError(statement.getTransaction(), getSession().getLabel("330"));
            }

            // *******************************************************************
            // Releve Paritaire
            // *******************************************************************
            if (isReleveParitaire().booleanValue()) {
                if (typeAffiliation.equals(CodeSystem.TYPE_AFFILI_INDEP)
                        || typeAffiliation.equals(CodeSystem.TYPE_AFFILI_NON_ACTIF)
                        || typeAffiliation.equals(CodeSystem.TYPE_AFFILI_SELON_ART_1A)) {

                    _addError(statement.getTransaction(), getSession().getLabel("350"));
                }
            }

        } // fin ignorer plausi pour type d'affiliation fichier central

        // *******************************************************************
        // Date Edition Fiche
        // *******************************************************************
        setDateEditionFiche(JACalendar.todayJJsMMsAAAA());
        JATime now = new JATime(JACalendar.now());
        setDateEditionFicheM1(now.toStr(""));

        // Sauvegarde des anciennes dates d'affiliation (pour les sorties)
        if (!statement.getTransaction().hasErrors()) {
            setDatePrecDebut(getDateDebutSave());
            setDatePrecFin(getDateFinSave());
        }

    }

    /**
     * Control les dates de début et de fin d'affiliation avec les autres affiliations pour le meme affilié.
     *
     * @param transaction
     *            la transaction a utiliser
     * @return true - Si les dates sont valides
     * @throws Exception
     */
    private boolean _validationDate(BTransaction transaction) throws Exception {

        boolean result = true;

        AFApplication app = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                AFApplication.DEFAULT_APPLICATION_NAOS));
        String modePeriodesChevauchantes = app.getModePeriodesChevauchantes(getSession());

        // *********************************************************
        // Test Date de Début, Date de Fin
        // *********************************************************
        AFAffiliationManager affManager = new AFAffiliationManager();
        affManager.setForAffilieNumero(getAffilieNumero());
        affManager.setForIdTiers(getIdTiers());
        affManager.setSession(getSession());
        affManager.find(transaction);

        for (int i = 0; i < affManager.size(); i++) {

            AFAffiliation affiliation = (AFAffiliation) affManager.getEntity(i);

            // Mode CCVS ---
            // TODO à activer pour tous les clients pour la version 1-8
            if ("special".equals(modePeriodesChevauchantes)) {
                if (_isPeriodeChevauchante(affiliation)) {
                    _addError(transaction, FWMessageFormat.format(getSession().getLabel("823"), getDateDebut(),
                            getDateFin(), affiliation.getDateDebut()));

                    return false;
                } else {
                    continue;
                }
            } else {
                // une affiliation ouverte et clôturée à la même date est considérée
                // comme test
                // il ne faut donc pas en tenir compte pour le test des dates
                if (JadeStringUtil.equals(affiliation.getDateDebut(), affiliation.getDateFin(), true)) {
                    continue;
                }

                // Ne pas tester l'affiliation avec elle meme
                if (!affiliation.getAffiliationId().equalsIgnoreCase(getAffiliationId())) {

                    if ((!CodeSystem.TYPE_AFFILI_EMPLOY.equals(getTypeAffiliation()) && CodeSystem.TYPE_AFFILI_NON_ACTIF
                            .equals(affiliation.getTypeAffiliation()))
                            || ((!CodeSystem.TYPE_AFFILI_NON_ACTIF.equals(getTypeAffiliation()) && CodeSystem.TYPE_AFFILI_EMPLOY
                                    .equals(affiliation.getTypeAffiliation())))) {

                        // Test si il y a déjà une affiliation sans une date de fin
                        if (JadeStringUtil.isBlankOrZero(affiliation.getDateFin())) {
                            _addError(transaction, getSession().getLabel("810"));
                            result = false;

                        } else {
                            // Test si il n'y a pas de chevauchement avec une
                            // affiliation
                            if ((BSessionUtil.compareDateFirstLowerOrEqual(transaction.getSession(), getDateDebut(),
                                    affiliation.getDateFin()))
                                    && (JadeStringUtil.isBlankOrZero(affiliation.getDateFin())
                                            || (JadeStringUtil.isBlankOrZero(getDateFin()) && (BSessionUtil
                                                    .compareDateBetweenOrEqual(getSession(), getDateDebut(),
                                                            getDateFin(), affiliation.getDateFin()))) || (BSessionUtil
                                                .compareDateBetweenOrEqual(getSession(), affiliation.getDateDebut(),
                                                        affiliation.getDateFin(), getDateFin()))))

                            {
                                _addError(transaction, FWMessageFormat.format(getSession().getLabel("822"),
                                        getDateDebut(), affiliation.getDateDebut(), affiliation.getDateFin()));
                                result = false;
                            }
                        }
                    } else {
                        if (BSessionUtil.compareDateFirstLower(transaction.getSession(), getDateDebut(), getDateFin())) {

                            // Test si il n'y a pas de chevauchement pour un
                            // nouvelle affiliation
                            // avec date de fin avec un affiliation sans date de fin
                            if (JadeStringUtil.isBlankOrZero(affiliation.getDateFin())) {

                                if (BSessionUtil.compareDateFirstGreaterOrEqual(transaction.getSession(), getDateFin(),
                                        affiliation.getDateDebut())) {
                                    if ((!CodeSystem.TYPE_AFFILI_EMPLOY.equals(getTypeAffiliation()) && CodeSystem.TYPE_AFFILI_NON_ACTIF
                                            .equals(affiliation.getTypeAffiliation()))
                                            || ((!CodeSystem.TYPE_AFFILI_NON_ACTIF.equals(getTypeAffiliation()) && CodeSystem.TYPE_AFFILI_EMPLOY
                                                    .equals(affiliation.getTypeAffiliation())))) {

                                        _addError(transaction, FWMessageFormat.format(getSession().getLabel("823"),
                                                getDateDebut(), getDateFin(), affiliation.getDateDebut()));
                                        result = false;
                                    }
                                }
                            } else {
                                if (BSessionUtil.compareDateBetweenOrEqual(transaction.getSession(),
                                        affiliation.getDateDebut(), affiliation.getDateFin(), getDateDebut())
                                        || BSessionUtil.compareDateBetweenOrEqual(transaction.getSession(),
                                                affiliation.getDateDebut(), affiliation.getDateFin(), getDateFin())) {
                                    if ((!CodeSystem.TYPE_AFFILI_EMPLOY.equals(getTypeAffiliation()) && CodeSystem.TYPE_AFFILI_NON_ACTIF
                                            .equals(affiliation.getTypeAffiliation()))
                                            || ((!CodeSystem.TYPE_AFFILI_NON_ACTIF.equals(getTypeAffiliation()) && CodeSystem.TYPE_AFFILI_EMPLOY
                                                    .equals(affiliation.getTypeAffiliation())))) {

                                        _addError(transaction, FWMessageFormat.format(getSession().getLabel("824"),
                                                getDateDebut(), getDateFin(), affiliation.getDateDebut(),
                                                affiliation.getDateFin()));

                                        result = false;
                                    }
                                } else {
                                    _addError(transaction, FWMessageFormat.format(getSession().getLabel("824"),
                                            getDateDebut(), getDateFin(), affiliation.getDateDebut(),
                                            affiliation.getDateFin()));

                                    result = false;
                                }
                            }
                        }
                    }
                }
            }
        }

        // Si l'affiliation est radiée, elle ne peut être l'objet d'un lien:
        // taxé sous
        if (!JAUtil.isDateEmpty(getDateFin())) {
            JACalendar cal = new JACalendarGregorian();
            if (this.isTaxationPrincipale(cal.addDays(getDateFin(), 1))) {
                _addError(transaction, getSession().getLabel("2070"));
            }
        }
        return result;
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     *
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MAIAFF", this._dbWriteNumeric(statement.getTransaction(), getAffiliationId(), ""));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     *
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MAIAFF",
                this._dbWriteNumeric(statement.getTransaction(), getAffiliationId(), "affiliationId"));
        statement.writeField("HTITIE", this._dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers"));
        statement.writeField("MALNAF",
                this._dbWriteString(statement.getTransaction(), getAffilieNumero(), "affilieNumero"));
        statement.writeField("MADDEB", this._dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "dateDebut"));
        statement.writeField("MADFIN", this._dbWriteDateAMJ(statement.getTransaction(), getDateFin(), "dateFin"));
        statement.writeField("MADXDE",
                this._dbWriteDateAMJ(statement.getTransaction(), getDatePrecDebut(), "datePrecDebut"));
        statement.writeField("MADXFI",
                this._dbWriteDateAMJ(statement.getTransaction(), getDatePrecFin(), "datePrecFin"));
        statement.writeField("MATMOT", this._dbWriteNumeric(statement.getTransaction(), getMotifFin(), "motifFin"));
        statement.writeField("MATTAF",
                this._dbWriteNumeric(statement.getTransaction(), getTypeAffiliation(), "typeaffiliation"));
        statement.writeField("MATMCR",
                this._dbWriteNumeric(statement.getTransaction(), getMotifCreation(), "motifCreation"));
        statement.writeField("MABBMA", this._dbWriteBoolean(statement.getTransaction(), getBonusMalus(), "bonusMalus"));
        statement.writeField("MATBRA",
                this._dbWriteNumeric(statement.getTransaction(), getBrancheEconomique(), "brancheEconomique"));
        statement.writeField("MATJUR",
                this._dbWriteNumeric(statement.getTransaction(), getPersonnaliteJuridique(), "personnaliteJuridique"));
        statement.writeField("MABEXO",
                this._dbWriteBoolean(statement.getTransaction(), isExonerationGenerale(), "exonerationGenerale"));
        statement.writeField("MADFIC",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateEditionFiche(), "dateEditionFiche"));
        statement.writeField("MADFI1",
                this._dbWriteTime(statement.getTransaction(), getDateEditionFicheM1(), "dateEditionFicheM1"));
        statement.writeField("MADFI2",
                this._dbWriteNumeric(statement.getTransaction(), getDateEditionFicheM2(), "dateEditionFicheM2"));
        statement.writeField("MATDEC",
                this._dbWriteNumeric(statement.getTransaction(), getDeclarationSalaire(), "declarationSalaire"));
        statement.writeField("MATMCO",
                this._dbWriteNumeric(statement.getTransaction(), getMembreComite(), "membreComite"));
        statement.writeField("MABIRR",
                this._dbWriteBoolean(statement.getTransaction(), isIrrecouvrable(), "irrecouvrable"));
        statement
                .writeField("MABOCC", this._dbWriteBoolean(statement.getTransaction(), isOccasionnel(), "occasionnel"));
        statement.writeField("MABMAI",
                this._dbWriteBoolean(statement.getTransaction(), isPersonnelMaison(), "personnelMaison"));
        statement
                .writeField("MABLIQ", this._dbWriteBoolean(statement.getTransaction(), isLiquidation(), "liquidation"));
        statement.writeField("MABTRA", this._dbWriteBoolean(statement.getTransaction(), isTraitement(), "traitement"));
        statement.writeField("MABREP",
                this._dbWriteBoolean(statement.getTransaction(), isReleveParitaire(), "releveParitaire"));
        statement.writeField("MABREI",
                this._dbWriteBoolean(statement.getTransaction(), isRelevePersonnel(), "relevePersonnel"));
        statement.writeField("MABEAA", this._dbWriteBoolean(statement.getTransaction(),
                envoiAutomatiqueAnnonceSalaires, "envoiAutomatiqueAnnonceSalaires"));
        statement.writeField("MAMMAP",
                this._dbWriteNumeric(statement.getTransaction(), getMassePeriodicite(), "massePeriodicite"));
        statement.writeField("MAMMAA",
                this._dbWriteNumeric(statement.getTransaction(), getMasseAnnuelle(), "masseAnnuelle"));
        statement.writeField("MATPER",
                this._dbWriteNumeric(statement.getTransaction(), getPeriodicite(), "periodicite"));
        statement.writeField("MALFED", this._dbWriteString(statement.getTransaction(), getNumeroIDE(), "numeroIDE"));
        statement.writeField("MAMTCO",
                this._dbWriteNumeric(statement.getTransaction(), getTaxeCo2Taux(), "taxeCo2Taux"));
        statement.writeField("MAMFCO",
                this._dbWriteNumeric(statement.getTransaction(), getTaxeCo2Fraction(), "taxeCo2Fraction"));
        statement.writeField("MALNAA",
                this._dbWriteString(statement.getTransaction(), getAncienAffilieNumero(), "ancienAffilieNumero"));
        statement.writeField("MAICPA",
                this._dbWriteNumeric(statement.getTransaction(), getCaissePartance(), "caissePartance"));
        statement.writeField("MADCRE",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateCreation(), "dateCreation"));
        statement.writeField("MADTEN", this._dbWriteDateAMJ(statement.getTransaction(), getDateTent(), "dateTent"));
        statement.writeField("MATCDN", this._dbWriteNumeric(statement.getTransaction(), getCodeNoga(), "codeNoga"));
        statement.writeField("MATTAS",
                this._dbWriteNumeric(statement.getTransaction(), getTypeAssocie(), "typeAssocie"));
        statement.writeField("MADESC", this._dbWriteString(statement.getTransaction(), getRaisonSocialeCourt()));
        statement.writeField("MADESL", this._dbWriteString(statement.getTransaction(), getRaisonSociale()));
        statement.writeField("MADDEM", this._dbWriteDateAMJ(statement.getTransaction(), getDateDemandeAffiliation()));
        // maj de la raison sociale en majuscule
        statement.writeField(
                "MADESM",
                this._dbWriteString(statement.getTransaction(),
                        JadeStringUtil.convertSpecialChars(getRaisonSociale().toUpperCase())));
        statement.writeField("MATCFA",
                this._dbWriteNumeric(statement.getTransaction(), getCodeFacturation(), "montantLibre"));
        // code de sécurité sur l'affiliation (utilisé dans l'affichage des CI)
        statement.writeField("MATSEC",
                this._dbWriteNumeric(statement.getTransaction(), getAccesSecurite(), "accesSecurite"));
        statement.writeField("MATSUV", this._dbWriteNumeric(statement.getTransaction(), getCodeSUVA(), "codeSUVA"));
        statement.writeField("MACONV", this._dbWriteNumeric(statement.getTransaction(), getConvention(), "convention"));

        // D0050 - Champs IDE
        statement.writeField("MATPAS",
                this._dbWriteBoolean(statement.getTransaction(), isIdeAnnoncePassive(), "ideAnnoncePassive"));
        statement.writeField("MATNAN",
                this._dbWriteBoolean(statement.getTransaction(), isIdeNonAnnoncante(), "ideNonAnnoncante"));
        statement.writeField("MATSTA", this._dbWriteNumeric(statement.getTransaction(), getIdeStatut(), "ideStatut"));
        statement.writeField("MATRSO",
                this._dbWriteString(statement.getTransaction(), getIdeRaisonSociale(), "ideRaisonSociale"));
        // D0181 - activité
        statement.writeField("MATACT", this._dbWriteString(statement.getTransaction(), getActivite(), "activite"));
        statement.writeField("MAJFAD ", this._dbWriteBoolean(statement.getTransaction(), isMajFAD(),
                BConstants.DB_TYPE_BOOLEAN_NUMERIC, "majFAD"));
    }

    /**
     * Ajoute un suivi à la liste des suivis à traiter de manière groupée
     *
     * @param process
     *            le suivi à suspendre
     */
    public void addSuivi(BProcess process) {
        suivisList.add(process);
    }

    /**
     * Créer un suivi sur le contrôle LAA si pas déjà présent
     *
     * @throws Exception
     *             si problème de création
     */
    public void envoiCtrlLAA() throws Exception {
        AFSuiviLAA suiviLAA = new AFSuiviLAA();

        if (suiviLAA.isAffiliationConcerne(this)) {
            suiviLAA.genererControle(this);
        }
    }

    /**
     * Créer un suivi sur le contrôle LPP si pas déjà présent
     *
     * @throws Exception
     *             si problème de création
     */
    public void envoiCtrlLPP() throws Exception {
        AFSuiviLPP suiviLPP = new AFSuiviLPP();

        if (suiviLPP.isAffiliationConcerne(this)) {
            suiviLPP.genererControle(this);
        }
    }

    /**
     * Créer un suivi sur le contrôle LPP si pas déjà présent avec le module de provenance défini
     *
     * @throws Exception
     *             si problème de création
     */
    public void envoiCtrlLPP(BApplication application, BProcess parent) throws Exception {
        AFSuiviLPP suiviLPP = new AFSuiviLPP();

        if (suiviLPP.isAffiliationConcerne(this, application)) {
            suiviLPP.genererControle(this, application);
        } else {
            DSProcessValidation processValidationParent = (DSProcessValidation) parent;
            processValidationParent.getMemoryLog().logMessage(getSession().getLabel("SUIVI_LPP_ERROR"),
                    FWMessage.INFORMATION, "");
            processValidationParent.setSendCompletionMail(true);
            processValidationParent.setForceEnvoiMail(true);
        }
    }

    /**
     * Test si la date de fin de l'exception doit être mise à jour
     *
     * @param dateFinException
     * @return
     * @throws Exception
     */
    private boolean exceptionAvecDateFinSuperieur(String dateFinException) throws Exception {
        return (!JadeStringUtil.isBlankOrZero(getDateFin()) && BSessionUtil.compareDateFirstGreater(getSession(),
                dateFinException, getDateFin()));
    }

    private void extourneDecisionCAP() throws Exception {
        if ((getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_CAP_EMPLOYEUR) || getTypeAffiliation().equals(
                CodeSystem.TYPE_AFFILI_CAP_INDEPENDANT))
                && !getDateFin().equals(getDatePrecFin())) {
            try {
                // initialisation du thread context et utilisation du contextjdbc
                JadeThreadContext threadContext = AFAffiliationUtil.initContext(getSession());
                JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

                DecisionCAPSearchModel decisionCAPSearchModel = new DecisionCAPSearchModel();
                decisionCAPSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                decisionCAPSearchModel.setForIdAffiliation(getAffiliationId());
                decisionCAPSearchModel.setForEtat(AUDecisionEtat.VALIDEE.getCodeSystem());

                if (JadePersistenceManager.count(decisionCAPSearchModel) >= 1) {
                    throw new Exception(getSession().getLabel("ERREUR_EXTOURNE_CAP_DECISION_EN_COURS"));
                }

                AurigaServiceLocator.getDecisionCAPService().extournerDecisionsRollback(getAffiliationId());

                if (!JadeStringUtil.isEmpty(getDateFin())) {
                    // appel du service
                    AurigaServiceLocator.getDecisionCAPService().extournerDecisions(getAffiliationId(), getDateFin());
                }

            } catch (Exception e) {
                throw new Exception(getSession().getLabel("ERREUR_EXTOURNE_CAP") + e.getMessage());
            } finally {
                // stopper l'utilisation du context
                JadeThreadActivator.stopUsingContext(Thread.currentThread());
            }
        }
    }

    // ***********************************************
    // Getter
    // ***********************************************

    private void extourneDecisionCGAS() throws Exception {
        if ((getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_CGAS_EMPLOYEUR) || getTypeAffiliation().equals(
                CodeSystem.TYPE_AFFILI_CGAS_INDEPENDANT))
                && !getDateFin().equals(getDatePrecFin())) {
            try {
                // initialisation du thread context et utilisation du contextjdbc
                JadeThreadContext threadContext = AFAffiliationUtil.initContext(getSession());
                JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

                DecisionCGASSearchModel decisionCGASSearchModel = new DecisionCGASSearchModel();
                decisionCGASSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                decisionCGASSearchModel.setForIdAffiliation(getAffiliationId());
                decisionCGASSearchModel.setForEtat(ARDecisionEtat.VALIDEE.getCodeSystem());

                if (JadePersistenceManager.count(decisionCGASSearchModel) >= 1) {
                    throw new Exception(getSession().getLabel("ERREUR_EXTOURNE_CGAS_DECISION_EN_COURS"));
                }

                AriesServiceLocator.getDecisionCGASService().extournerDecisionsRollback(getAffiliationId());

                if (!JadeStringUtil.isEmpty(getDateFin())) {
                    // appel du service
                    AriesServiceLocator.getDecisionCGASService().extournerDecisions(getAffiliationId(), getDateFin());
                }

            } catch (Exception e) {
                throw new Exception(getSession().getLabel("ERREUR_EXTOURNE_CGAS") + e.getMessage());
            } finally {
                // stopper l'utilisation du context
                JadeThreadActivator.stopUsingContext(Thread.currentThread());
            }
        }
    }

    private void extourneDSType13Ouverte(BTransaction transaction) throws JAException, Exception {
        DSDeclarationListViewBean manaDecl = new DSDeclarationListViewBean();
        manaDecl.setSession(getSession());
        manaDecl.setFromAnnee("" + (JACalendar.getYear(getDateFin()) + 1));
        manaDecl.setForAffiliationId(getAffiliationId());
        manaDecl.setForTypeDeclaration(DSDeclarationViewBean.CS_PRINCIPALE);
        manaDecl.setForEtat(DSDeclarationViewBean.CS_OUVERT);
        manaDecl.find(transaction);
        if (manaDecl.size() > 0) {
            for (int i = 0; i < manaDecl.size(); i++) {
                DSDeclarationViewBean declaration = (DSDeclarationViewBean) manaDecl.getEntity(i);
                declaration.delete(transaction);
            }
        }
    }

    /**
     * Methode utilisée par les API.
     *
     * @param params
     * @return
     * @throws Exception
     */
    public BManager find(Hashtable<?, ?> params) throws Exception {
        BManager manager = getManager();
        manager.setSession(getSession());

        if (params != null) {
            Enumeration<?> methods = params.keys();
            while (methods.hasMoreElements()) {
                String methodName = (String) methods.nextElement();
                String value = (String) params.get(methodName);
                Method m = manager.getClass().getMethod(methodName, new Class[] { String.class });
                if (m != null) {
                    m.invoke(manager, new Object[] { value });
                }
            }
        }
        manager.find();

        return manager;
    }

    public String getAccesSecurite() {
        return accesSecurite;
    }

    /**
     * Indique si l'affilié donné est valide pour les prestations AF
     *
     * @param date
     *            date à laquelle le test doit s'effectuer. Renvoie false si non rensignée
     * @param typeAllocataire
     *            type d'allocataire (salarié,non actif,paysan,collaborateur agricole, travailleur agricole)
     * @return true si l'affilié données est valide, false sinon
     * @exception exception
     *                si un erreur survient lors de la recherche
     */
    public Boolean getActifAF(String date, String typeAllocataire) throws Exception {
        return new Boolean(AFAffiliationUtil.isActifAF(this, date, typeAllocataire));
    }

    /**
     * Retourne l'affiliation à laquelle les cotisations AF sont facturées.<br>
     * Si l'affiliation donnée en paramètre contient une cotisation AF (AF facturés), cette même affilition sera
     * retournée.<br>
     * Par contre, si la cotisation AF contient l'indication qu'elle est facturé à la maison mère, le lien de type
     * "Succursale de" est recherché afin de retrouver l'affiliation maison mère.<br>
     * Dans le cas où le lien "Succursale de" n'est pas applicable, il est également possible d'utiliser le type de lien
     * "Décompte AF sous" que cette méthode tentera également d'utiliser pour rechercher l'affiliation de facturation.
     *
     * @param date
     *            la date utilisée pour la recherche.
     * @return l'affiliation à laquelle les cotisations AF sont facturées ou null si inconnu.
     * @exception exception
     *                si une erreur survient lors de la recherche.
     */
    public GlobazValueObject getAffiliationFacturationAF(String date) throws Exception {
        AFAffiliation aff = AFAffiliationUtil.getAffiliationFacturationAF(this, date);
        if (aff != null) {
            return aff.toValueObject();
        } else {
            return null;
        }
    }

    /**
     * Retourne l'affiliation à laquelle les cotisations AF sont facturées.<br>
     * Si l'affiliation donnée en paramètre contient une cotisation AF (AF facturés), cette même affilition sera
     * retournée.<br>
     * Par contre, si la cotisation AF contient l'indication qu'elle est facturé à la maison mère, le lien de type
     * "Succursale de" est recherché afin de retrouver l'affiliation maison mère.<br>
     * Dans le cas où le lien "Succursale de" n'est pas applicable, il est également possible d'utiliser le type de lien
     * "Décompte AF sous" que cette méthode tentera également d'utiliser pour rechercher l'affiliation de facturation.
     *
     * @param date
     *            la date utilisée pour la recherche.
     * @param typeAllocataire
     *            type d'allocataire (salarié,non actif,paysan,collaborateur agricole, travailleur agricole)
     * @return l'affiliation à laquelle les cotisations AF sont facturées ou null si inconnu.
     * @exception exception
     *                si une erreur survient lors de la recherche.
     */
    public GlobazValueObject getAffiliationFacturationAF(String date, String typeAllocataire) throws Exception {
        AFAffiliation aff = AFAffiliationUtil.getAffiliationFacturationAF(this, date, typeAllocataire);
        if (aff != null) {
            return aff.toValueObject();
        } else {
            return null;
        }
    }

    // ID
    public java.lang.String getAffiliationId() {
        return affiliationId;
    }

    // Header
    public java.lang.String getAffilieNumero() {
        return affilieNumero;
    }

    /**
     * Retourne une liste d'Assurances pour une Affiliation données.
     *
     * @param httpSession
     * @param IdAffiliation
     * @return
     */

    public String getAgenceCom(String IdAffiliation, String dateJour) {
        TICompositionTiersManager compositionTiersManager = new TICompositionTiersManager();
        compositionTiersManager.setForIdTiersParent(getIdTiers());

        String agenceCommunale = "";
        compositionTiersManager.setForTypeLien(AFAffiliation.LIEN_AGENCE);

        // modif DGI: si radié, prendre la date de radiation pour la recherche
        if (JadeStringUtil.isEmpty(getDateFin())) {
            compositionTiersManager.setForDateEntreDebutEtFin(dateJour);
        } else {
            compositionTiersManager.setForDateEntreDebutEtFin(getDateFin());
        }

        compositionTiersManager.setSession(getSession());

        try {
            compositionTiersManager.find();
        } catch (Exception e) {
            return "";
        }

        if (!compositionTiersManager.isEmpty()) {
            agenceCommunale = ((TICompositionTiers) compositionTiersManager.get(0)).getNomTiersEnfant();
        }

        return agenceCommunale;
    }

    public String getAgenceComNum(String IdAffiliation, String dateJour) {
        return this.getAgenceComNum(IdAffiliation, dateJour, AFAffiliation.LIEN_AGENCE);
    }

    /**
     * Retourne une liste d'Assurances pour une Affiliation données.
     *
     * @param httpSession
     * @param IdAffiliation
     * @return
     */

    public String getAgenceComNum(String IdAffiliation, String dateJour, String typeAgence) {
        TICompositionTiersManager compositionTiersManager = new TICompositionTiersManager();
        TIAdministrationViewBean admin = new TIAdministrationViewBean();
        compositionTiersManager.setForIdTiersParent(getIdTiers());

        String idTiersEnfants = "";
        String agenceComNum = "";
        compositionTiersManager.setForTypeLien(typeAgence);
        compositionTiersManager.setForDateEntreDebutEtFin(dateJour);
        compositionTiersManager.setSession(getSession());

        try {
            compositionTiersManager.find();

            if (!compositionTiersManager.isEmpty()) {
                idTiersEnfants = ((TICompositionTiers) compositionTiersManager.get(0)).getIdTiersEnfant();
                admin.setIdTiersAdministration(idTiersEnfants);
                admin.setSession(getSession());
                admin.retrieve();
                agenceComNum = admin.getCodeAdministration();
            }
        } catch (Exception e) {
            return "";
        }

        return agenceComNum;
    }

    public String getAgenceComPriveNum(String IdAffiliation, String dateJour) {
        return this.getAgenceComNum(IdAffiliation, dateJour, AFAffiliation.LIEN_AGENCE_PRIVE);
    }

    public java.lang.String getAncienAffilieNumero() {
        return ancienAffilieNumero;
    }

    public java.lang.Boolean getBonusMalus() {
        return bonusMalus;
    }

    public java.lang.String getBrancheEconomique() {
        return brancheEconomique;
    }

    public java.lang.String getCaissePartance() {
        return caissePartance;
    }

    public java.lang.String getCaisseProvenance() {
        return caisseProvenance;
    }

    /**
     * Retourne le canton défini pour les AF. Si le canton est spécifié dans la cotisation AF, celui-ci est retourné.
     * Sinon, c'est le canton de l'adresse de l'affilié qui est utilisé
     *
     * @param date
     *            la date utilisée pour la recherche
     * @return e canton défini pour les AF
     * @throws Exception
     *             si une erreur survient lors de la recherche.
     */
    public String getCantonAF(String date) throws Exception {
        return AFAffiliationUtil.getCantonAF(this, date);
    }

    public java.lang.String getCategorieNoga() throws Exception {
        if (!JadeStringUtil.isBlank(codeNoga)) {
            // code, recherche de la catégorie carrespondante
            FWParametersSystemCode child = new FWParametersSystemCode();

            child.setSession(getSession());
            child.setIdCode(codeNoga);

            child.retrieve();

            FWParametersSystemCode parent = child.getSelection();
            if (parent != null) {
                return parent.getIdCode();
            }
        }

        return "";
    }

    public java.lang.String getCodeFacturation() {
        return codeFacturation;
    }

    public java.lang.String getCodeNoga() {
        return codeNoga;
    }

    public FWParametersSystemCodeManager getCSNoga() {
        FWParametersSystemCodeManager mgr = new FWParametersSystemCodeManager();

        mgr.setSession(getSession());
        mgr.getListeCodes("VENOGAVAL", getSession().getIdLangue());

        return mgr;
    }

    public java.lang.String getDateCreation() {
        return dateCreation;
    }

    public java.lang.String getDateDebut() {
        return dateDebut;
    }

    public java.lang.String getDateDebutSave() {
        return dateDebutSave;
    }

    public String getDateDemandeAffiliation() {
        return dateDemandeAffiliation;
    }

    public java.lang.String getDateEditionFiche() {
        return dateEditionFiche;
    }

    public java.lang.String getDateEditionFicheM1() {
        return dateEditionFicheM1;
    }

    public java.lang.String getDateEditionFicheM2() {
        return dateEditionFicheM2;
    }

    public java.lang.String getDateFin() {
        return dateFin;
    }

    public java.lang.String getDateFinSave() {
        return dateFinSave;
    }

    public java.lang.String getDatePrecDebut() {
        return datePrecDebut;
    }

    public java.lang.String getDatePrecFin() {
        return datePrecFin;
    }

    public java.lang.String getDateTent() {
        return dateTent;
    }

    public java.lang.String getDeclarationSalaire() {
        return declarationSalaire;
    }

    public Boolean getEnvoiAutomatiqueAnnonceSalaires() {
        return envoiAutomatiqueAnnonceSalaires;
    }

    public java.lang.Boolean getEnvoiAutomatiqueLAA() {
        return envoiAutomatiqueLAA;
    }

    public java.lang.Boolean getEnvoiAutomatiqueLPP() {
        return envoiAutomatiqueLPP;
    }

    /**
     * Retour les "Code System" a exclure pour la sélection du "Motif de fin" d'affiliation.
     *
     * @return
     */
    public HashSet<String> getExceptMotifFin() {
        HashSet<String> except = new HashSet<String>();
        // liste des cs qui ne devront pas figurér dans la liste
        except.add(CodeSystem.MOTIF_FIN_FIN_ADHESION);
        except.add(CodeSystem.MOTIF_FIN_FIN_COUV_ASSURANCE);
        except.add(CodeSystem.MOTIF_FIN_EXCEPTION);

        return except;
    }

    public String getIdeRaisonSociale() {
        return ideRaisonSociale;
    }

    public String getIdeStatut() {
        return ideStatut;
    }

    public java.lang.String getIdTiers() {
        return idTiers;
    }

    public java.lang.Boolean isIdeAnnoncePassive() {
        return ideAnnoncePassive;
    }

    public java.lang.Boolean isIdeNonAnnoncante() {
        return ideNonAnnoncante;
    }

    /**
     * Renvoie le Manager de l'entité.
     *
     * @return
     */
    protected BManager getManager() {
        return new AFAffiliationManager();
    }

    public java.lang.String getMasseAnnuelle() {
        return JANumberFormatter.fmt(masseAnnuelle.toString(), true, false, true, 2);
    }

    public java.lang.String getMassePeriodicite() {
        return JANumberFormatter.fmt(massePeriodicite.toString(), true, false, true, 2);
    }

    public java.lang.String getMembreComite() {
        return membreComite;
    }

    public java.lang.String getMotifCreation() {
        return motifCreation;
    }

    public java.lang.String getMotifFin() {
        return motifFin;
    }

    public String getNumeroIDESansCHE() {
        return getNumeroIDE().replace("CHE", "");
    }

    public java.lang.String getNouveauAffilieNumero() {
        String result = "";

        // génération automatique
        try {
            INumberGenerator generator = ((AFApplication) getSession().getApplication()).getGeneratorNoAff();
            result = generator.generateBeforeDisplay(this);
        } catch (Exception ex) {
            // impossible de générer le no
            result = "";
        }

        return result;
    }

    public java.lang.String getNumeroIDE() {
        return numeroIDE;
    }

    // Other
    public AFAffiliation getOldAffiliation() {
        return oldAffiliation;
    }

    public java.lang.String getPeriodicite() {
        return periodicite;
    }

    public java.lang.String getPersonnaliteJuridique() {
        return personnaliteJuridique;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public String getActivite() {
        return activite;
    }

    /**
     * conversion Base64 : Une raison sociale peut contenir des caractères non autorisé en get
     * utilisé pour transiter la raison sociale vers la recherche ide
     *
     * @return la raison sociale en B64
     */
    public String getRaisonSocialeb64() {
        return new String(Base64.encodeBase64URLSafe(raisonSociale.getBytes()));
    }

    public String getRaisonSocialeCourt() {
        return raisonSocialeCourt;
    }

    public java.lang.Boolean getReleveParitaire() {
        return releveParitaire;
    }

    public List<AFSuiviCaisseAffiliation> getSuiviCaisseList() {
        try {
            return AFAffiliationUtil.retrieveSuiviCaisse(this);
        } catch (Exception e) {
            // erreur lors du retrieve, catchée car méthode appelée dans jsp
            return new ArrayList<AFSuiviCaisseAffiliation>();
        }
    }

    /**
     * Détermine si l'affiliation est l'objet d'un lien d'affiliation de type 'taxé sous': x --taxé sous--> this
     *
     * @deprecated utilier AFAffiliationUtil.getTaxationPrincipale()
     */
    @Deprecated
    public AFAffiliation getTaxationPrincipale(String date) throws Exception {
        return AFAffiliationUtil.getTaxationPrincipale(this, date);
    }

    public java.lang.String getTaxeCo2Fraction() {
        return JANumberFormatter.fmt(taxeCo2Fraction.toString(), true, false, true, 5);
    }

    public java.lang.String getTaxeCo2Taux() {
        return JANumberFormatter.fmt(taxeCo2Taux.toString(), true, false, true, 5);
    }

    /**
     * Rechercher le tiers de l'affiliastion en fonction de son ID.
     *
     * @return le tiers
     */
    public TITiersViewBean getTiers() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getIdTiers())) {
            return null;
        }

        if ((_tiers == null) || ((_tiers != null) && !getIdTiers().equals(_tiers.getIdTiers()))) {

            _tiers = new TITiersViewBean();
            _tiers.setSession(getSession());
            _tiers.setIdTiers(getIdTiers());

            try {
                _tiers.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _tiers = null;
            }
        }

        return _tiers;
    }

    /**
     * Méthode getTiersNom en fonction de son ID.
     *
     * @return le tiers
     */
    public java.lang.String getTiersNom() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getIdTiers())) {
            return null;
        }

        if ((_tiers == null) || ((_tiers != null) && !getIdTiers().equals(_tiers.getIdTiers()))) {
            _tiers = new TITiersViewBean();
            _tiers.setSession(getSession());
            _tiers.setIdTiers(getIdTiers());

            try {
                _tiers.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _tiers = null;
            }
        }

        return _tiers.getNom();
    }

    public java.lang.Boolean getTraitement() {
        return traitement;
    }

    public java.lang.String getTypeAffiliation() {
        return typeAffiliation;
    }

    public java.lang.String getTypeAssocie() {
        return typeAssocie;
    }

    /**
     * Test si l'affiliation contient une information sur la caisse LAA
     *
     * @param annee
     *            l'année concernée
     * @return true si caisse trouvée
     * @throws Exception
     *             si une erreur survient
     * @deprecated utiliser AFAffiliationUtil.hasCaissseLAA()
     */
    @Deprecated
    public boolean hasCaissseLAA(String annee) throws Exception {
        return AFAffiliationUtil.hasCaissseLAA(this, annee);
    }

    /**
     * Test si l'affiliation contient une information sur la caisse LPP
     *
     * @param annee
     *            l'année concernée
     * @return true si caisse trouvée
     * @throws Exception
     *             si une erreur survient
     * @deprecated utiliser AFAffiliationUtil.hasCaissseLPP()
     */
    @Deprecated
    public boolean hasCaissseLPP(String annee) throws Exception {
        return AFAffiliationUtil.hasCaissseLPP(this, annee);
    }

    public boolean hasDefinitiveForAnnee(int anneeFin) {
        CPDecisionManager manager = new CPDecisionManager();

        manager.setForIdAffiliation(affiliationId);
        manager.setSession(getSession());
        manager.setForAnneeDecision(String.valueOf(anneeFin));
        manager.setForExceptTypeDecision(CPDecision.CS_IMPUTATION);
        manager.setForIsActive(Boolean.TRUE);

        try {
            manager.find();
            for (int i = 0; i < manager.size(); i++) {
                CPDecision decision = (CPDecision) manager.get(i);
                if (!decision.isProvisoireMetier()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // ajouté le 16.03.09 par mka
    public boolean hasLPPetMotif() throws Exception {
        List<?> suiviCaisse = getSuiviCaisseList();
        boolean hasLPPetMotif = false;

        for (int i = 0; i < suiviCaisse.size(); i++) {

            AFSuiviCaisseAffiliation lpp = (AFSuiviCaisseAffiliation) suiviCaisse.get(i);
            if (CodeSystem.GENRE_CAISSE_LPP.equals(lpp.getGenreCaisse())) {
                if ((lpp.getMotif() != null) && !JadeStringUtil.isBlankOrZero(lpp.getMotif())) {
                    String dateDebut = lpp.getDateDebut();
                    String dateFin = lpp.getDateFin();
                    String dateDuJour = JACalendar.todayJJsMMsAAAA();

                    if (!JadeStringUtil.isBlankOrZero(dateDebut) && !JadeStringUtil.isBlankOrZero(dateFin)
                            && BSessionUtil.compareDateBetweenOrEqual(getSession(), dateDebut, dateFin, dateDuJour)) {
                        hasLPPetMotif = true;
                    } else if (!JadeStringUtil.isBlankOrZero(dateDebut) && JadeStringUtil.isBlankOrZero(dateFin)
                            && BSessionUtil.compareDateFirstGreater(getSession(), dateDuJour, dateDebut)) {
                        hasLPPetMotif = true;
                    } else {
                        hasLPPetMotif = false;
                    }
                } else {
                    hasLPPetMotif = false;
                }
            }
        }
        return hasLPPetMotif;
    }

    /**
     * indique si un user à un niveau de droit (complément codeSecure) suffisant par rapport au niveau de sécurité sur
     * l'affiliation
     *
     * @return vrai si le user à le droit >= à la sécurité de l'affiliation
     */
    public boolean hasRightAccesSecurity() {
        try {
            // recherche du complément "secureCode" du user
            BSession session = getSession();
            FWSecureUserDetail user = new FWSecureUserDetail();

            user.setSession(session);
            user.setUser(session.getUserId());
            user.setLabel(AFAffiliation.SECURE_CODE);

            user.retrieve();

            // si l'affiliation est en cours de création on affiche le select
            if (isNew()) {
                return true;
            }
            // si le SecureCode existe et a une valeur
            else if (!user.isNew() && !JadeStringUtil.isEmpty(user.getData())) {
                String userLevelSecurity = user.getData();

                if (Integer.parseInt(getAccesSecurite().substring(getAccesSecurite().length() - 1)) <= Integer
                        .parseInt(userLevelSecurity)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                // si le SecureCode n'existe pas ou n'as pas de valeur => alors
                // userLevelSecurity=0
                String userLevelSecurity = "0";
                if (Integer.parseInt(getAccesSecurite().substring(getAccesSecurite().length() - 1)) <= Integer
                        .parseInt(userLevelSecurity)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAffiliationAVS() {
        return !CodeSystem.TYPE_AFFILI_FICHIER_CENT.equals(getTypeAffiliation())
                && !CodeSystem.TYPE_AFFILI_NON_SOUMIS.equals(getTypeAffiliation()) && !traitement.booleanValue();
    }

    public java.lang.Boolean isEnvoiAutomatiqueLAA() {
        AFSuiviLAA suiviEnvoiLAA = new AFSuiviLAA();
        return new Boolean(suiviEnvoiLAA.isAlreadySent(this) != null);
    }

    public java.lang.Boolean isEnvoiAutomatiqueLPP() {
        AFSuiviLPP suiviEnvoiLPP = new AFSuiviLPP();
        return new Boolean(suiviEnvoiLPP.isAlreadySent(this) != null);
    }

    public java.lang.Boolean isExonerationGenerale() {
        return exonerationGenerale;
    }

    public java.lang.Boolean isIrrecouvrable() {
        return irrecouvrable;
    }

    /**
     * @return retourne l'état de la case a cocher et synchronise la cotisation a la majoration des frais d'admin
     */
    public java.lang.Boolean isMajFADAndUpdate() {
        return isMajFAD();
    }

    /**
     * @return true si les frais d'admin doivent être majoré
     */
    public java.lang.Boolean isMajFAD() {
        return majFAD;
    }

    public java.lang.Boolean isLiquidation() {
        return liquidation;
    }

    public java.lang.Boolean isOccasionnel() {
        return occasionnel;
    }

    public java.lang.Boolean isPersonnelMaison() {
        return personnelMaison;
    }

    public java.lang.Boolean isReleveParitaire() {
        return releveParitaire;
    }

    public java.lang.Boolean isRelevePersonnel() {
        return relevePersonnel;
    }

    /**
     * Retourne si les suivis sont retenus pour leur traitement
     *
     * @return
     */
    public boolean isSuivisSuspendu() {
        return (suivisList != null);
    }

    /**
     * Détermine si l'affiliation est l'objet d'un lien d'affiliation de type 'taxé sous': x --taxé sous--> this
     *
     * @deprecated utilier AFAffiliationUtil.isTaxationPrincipale()
     */
    @Deprecated
    public boolean isTaxationPrincipale() throws Exception {
        return AFAffiliationUtil.isTaxationPrincipale(this, null);
    }

    /**
     * Détermine si l'affiliation est l'objet d'un lien d'affiliation de type 'taxé sous': x --taxé sous--> this
     *
     * @deprecated utilier AFAffiliationUtil.isTaxationPrincipale()
     */
    @Deprecated
    public boolean isTaxationPrincipale(String date) throws Exception {
        return AFAffiliationUtil.isTaxationPrincipale(this, date);
    }

    public java.lang.Boolean isTraitement() {
        return traitement;
    }

    public boolean isWantGenerationSuiviLAALPP() {
        return wantGenerationSuiviLAALPP;
    }

    /**
     * Exécute tous les suivis retenus
     */
    public void libererSuivis() {
        Iterator<BProcess> it = suivisList.iterator();
        while (it.hasNext()) {
            (it.next()).start();
        }
        suivisList = null;
    }

    public void setAccesSecurite(String accesSecurite) {
        this.accesSecurite = accesSecurite;
    }

    public void setAffiliationId(java.lang.String newAffiliationId) {
        affiliationId = newAffiliationId;
    }

    public void setAffilieNumero(java.lang.String newAffilieNumero) {
        affilieNumero = newAffilieNumero;
    }

    public void setAncienAffilieNumero(java.lang.String newAncienAffilieNumero) {
        ancienAffilieNumero = newAncienAffilieNumero;
    }

    public void setBonusMalus(java.lang.Boolean newBonusMalus) {
        bonusMalus = newBonusMalus;
    }

    public void setBrancheEconomique(java.lang.String newBrancheEconomique) {
        brancheEconomique = newBrancheEconomique;
    }

    public void setCaissePartance(java.lang.String newCaissePartance) {
        caissePartance = newCaissePartance;
    }

    public void setCaisseProvenance(java.lang.String newCaisseProvenance) {
        caisseProvenance = newCaisseProvenance;
    }

    public void setCodeFacturation(java.lang.String codeFacturation) {
        this.codeFacturation = codeFacturation;
    }

    public void setCodeNoga(java.lang.String string) {
        codeNoga = string;
    }

    public void setDateCreation(java.lang.String newDateCreation) {
        dateCreation = newDateCreation;
    }

    public void setDateDebut(java.lang.String newDateDebut) {
        dateDebut = newDateDebut;
    }

    public void setDateDebutSave(java.lang.String string) {
        dateDebutSave = string;
    }

    public void setDateDemandeAffiliation(String string) {
        dateDemandeAffiliation = string;
    }

    public void setDateEditionFiche(java.lang.String newDateEditionFiche) {
        dateEditionFiche = newDateEditionFiche;
    }

    public void setDateEditionFicheM1(java.lang.String newDateEditionFicheM1) {
        dateEditionFicheM1 = newDateEditionFicheM1;
    }

    public void setDateEditionFicheM2(java.lang.String newDateEditionFicheM2) {
        dateEditionFicheM2 = newDateEditionFicheM2;
    }

    public void setDateFin(java.lang.String newDateFin) {
        dateFin = newDateFin;
    }

    public void setDateFinSave(java.lang.String string) {
        dateFinSave = string;
    }

    public void setDatePrecDebut(java.lang.String newDatePrecDebut) {
        datePrecDebut = newDatePrecDebut;
    }

    public void setDatePrecFin(java.lang.String newDatePrecFin) {
        datePrecFin = newDatePrecFin;
    }

    public void setDateTent(java.lang.String newDateTent) {
        dateTent = newDateTent;
    }

    public void setDeclarationSalaire(java.lang.String newDeclarationSalaire) {
        declarationSalaire = newDeclarationSalaire;
    }

    public void setEnvoiAutomatiqueAnnonceSalaires(Boolean boolean1) {
        envoiAutomatiqueAnnonceSalaires = boolean1;
    }

    public void setEnvoiAutomatiqueLAA(java.lang.Boolean boolean1) {
        envoiAutomatiqueLAA = boolean1;
    }

    public void setEnvoiAutomatiqueLPP(java.lang.Boolean boolean1) {
        envoiAutomatiqueLPP = boolean1;
    }

    public void setExonerationGenerale(java.lang.Boolean newExonerationGenerale) {
        exonerationGenerale = newExonerationGenerale;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    public void setIdeStatut(String iDE_statut) {
        ideStatut = iDE_statut;
    }

    public void setIdeRaisonSociale(String iDE_raisonSociale) {
        ideRaisonSociale = iDE_raisonSociale;
    }

    /**
     * conversion Base64 : Une raison sociale peut contenir des caractères non autorisé en get
     *
     * @param iDE_raisonSociale
     */
    public void setIdeRaisonSocialeb64(String iDE_raisonSociale) {
        ideRaisonSociale = new String(Base64.decodeBase64(iDE_raisonSociale.getBytes()));
    }

    public void setIdTiers(java.lang.String newIdTiers) {
        idTiers = newIdTiers;
    }

    public void setIdeAnnoncePassive(java.lang.Boolean _idePassif) {
        ideAnnoncePassive = _idePassif;
    }

    public void setIdeNonAnnoncante(java.lang.Boolean ideNonAnnoncante) {
        this.ideNonAnnoncante = ideNonAnnoncante;
    }

    public void setIrrecouvrable(java.lang.Boolean newIrrecouvrable) {
        irrecouvrable = newIrrecouvrable;
    }

    public void setMajFAD(java.lang.Boolean majFAD) {
        this.majFAD = majFAD;
    }

    public void setLiquidation(java.lang.Boolean newLiquidation) {
        liquidation = newLiquidation;
    }

    public void setMasseAnnuelle(java.lang.String newMasseAnnuelle) {
        masseAnnuelle = JANumberFormatter.deQuote(newMasseAnnuelle);
    }

    public void setMassePeriodicite(java.lang.String newMassePeriodicite) {
        massePeriodicite = JANumberFormatter.deQuote(newMassePeriodicite);
    }

    public void setMembreComite(java.lang.String newMembreComite) {
        membreComite = newMembreComite;
    }

    public void setMotifCreation(java.lang.String newMotifCreation) {
        motifCreation = newMotifCreation;
    }

    public void setMotifFin(java.lang.String newMotifFin) {
        motifFin = newMotifFin;
    }

    public void setNumeroIDE(java.lang.String newNumeroIDE) {
        numeroIDE = newNumeroIDE;
    }

    public void setOccasionnel(java.lang.Boolean newOccasionnel) {
        occasionnel = newOccasionnel;
    }

    public void setOldAffiliation(AFAffiliation affiliation) {
        oldAffiliation = affiliation;
    }

    public void setPeriodicite(java.lang.String newPeriodicite) {
        periodicite = newPeriodicite;
    }

    public void setPersonnaliteJuridique(java.lang.String newPersonnaliteJuridique) {
        personnaliteJuridique = newPersonnaliteJuridique;
    }

    public void setPersonnelMaison(java.lang.Boolean newPersonnelMaison) {
        personnelMaison = newPersonnelMaison;
    }

    public void setRaisonSociale(String string) {
        raisonSociale = string;
    }

    public void setRaisonSocialeCourt(String string) {
        raisonSocialeCourt = string;
    }

    public void setReleveParitaire(java.lang.Boolean newReleveParitaire) {
        releveParitaire = newReleveParitaire;
    }

    public void setRelevePersonnel(java.lang.Boolean newRelevePersonnel) {
        relevePersonnel = newRelevePersonnel;
    }

    public void setTaxeCo2Fraction(java.lang.String newTaxeCo2Fraction) {
        taxeCo2Fraction = JANumberFormatter.deQuote(newTaxeCo2Fraction);
    }

    public void setTaxeCo2Taux(java.lang.String newTaxeCo2Taux) {
        taxeCo2Taux = JANumberFormatter.deQuote(newTaxeCo2Taux);
    }

    public void setTraitement(java.lang.Boolean newTraitement) {
        traitement = newTraitement;
    }

    public void setTypeAffiliation(java.lang.String newTypeAffiliation) {
        typeAffiliation = newTypeAffiliation;
    }

    public void setTypeAssocie(java.lang.String string) {
        typeAssocie = string;
    }

    /**
     * Permet de ne pas générer le suivi LAA LPP
     * Exemple en cas de mise à jour de l'affiliation par un process (ex traitement swissdec)
     *
     * @param wantGenerationSuiviLAALPP
     */
    public void setWantGenerationSuiviLAALPP(boolean varBoolean) {
        wantGenerationSuiviLAALPP = varBoolean;
    }

    private void suppressionSuiviCategorie(BTransaction transaction, String defaultApplication, String categorieSuivi,
            String affilieNumero, String idTiers, String annee) {
        try {
            LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();

            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE, defaultApplication);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, affilieNumero);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, idTiers);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, annee);

            LUJournalListViewBean viewBean = new LUJournalListViewBean();

            viewBean.setSession(getSession());
            viewBean.setProvenance(provenanceCriteres);
            viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_CATEGORIE_GROUPE);
            viewBean.setForValeurCodeSysteme(categorieSuivi);

            viewBean.find(transaction);

            if (viewBean.size() > 0) {
                LUJournalViewBean suivi = (LUJournalViewBean) viewBean.getFirstEntity();
                String idJournalInitial = JadeStringUtil.isBlankOrZero(suivi.getIdInitial()) ? suivi
                        .getIdJournalisation() : suivi.getIdInitial();

                LEJournalHandler jHandler = new LEJournalHandler();
                jHandler.annulerEtape(idJournalInitial, getSession(), transaction);
            }
        } catch (Exception e) {
            if (categorieSuivi.equals(ILEConstantes.CS_CATEGORIE_SUIVI_DS)) {
                transaction.addErrors(transaction.getSession().getLabel("ERREUR_SUPPR_SUIVI_DS") + affilieNumero);
            } else {
                transaction
                        .addErrors(transaction.getSession().getLabel("ERREUR_SUPPR_SUIVI_DS_STRUCT") + affilieNumero);
            }

        }

    }

    /**
     * Suspens les suivis générés dans affiliation. A utiliser avec libererSuivi() qui effectuera les traitements de
     * manière groupée
     */
    public void suspendreSuivis() {
        suivisList = new ArrayList<BProcess>();
    }

    /**
     * Contrôle si les champs obligatoires sont renseignés et valides.
     *
     * @return Une String vide si les champs obligatoires sont renseignés et valides ou Une String contenant les erreurs
     * @throws Exception
     */
    public String validationMandatory() throws Exception {

        StringBuffer message = new StringBuffer();
        boolean validationOK = true;

        // Test que les champs obligatoires soit renseignés

        if (JadeStringUtil.isBlankOrZero(getDateDebut())) {
            message.append(getSession().getLabel("20") + "\n");
            validationOK = false;
        }

        // *******************************************************************
        // Numero Affilie
        // *******************************************************************
        if (validationOK) {
            // utiliser le générateur de no d'affilié si configuré
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            INumberGenerator generator = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getGeneratorNoAff();

            if (generator != null) {
                try {
                    affilieNumero = generator.generateBeforeAdd(this);
                    if (affilieFormater != null) {
                        affilieFormater.check(affilieNumero);
                    }
                } catch (Exception e) {
                    // toujours pas ok -> msg d'erreur
                    message.append(getSession().getLabel("930") + "\n");
                    validationOK = false;
                }
            } else {
                if (affilieFormater != null) {
                    try {
                        affilieFormater.check(affilieNumero);
                        affilieNumero = affilieFormater.format(affilieNumero);
                    } catch (Exception e) {
                        message.append(getSession().getLabel("930") + "\n");
                        validationOK = false;
                    }
                }
            }
        }

        // test si le numéro d'affiliation n'existe pas déjà
        AFAffiliationManager mgr = new AFAffiliationManager();

        mgr.setSession(getSession());
        mgr.setForAffilieNumero(affilieNumero);

        mgr.find();

        // recherche sur les affiliations actives
        if (mgr.size() != 0) {
            for (int i = 0; (i < mgr.size()) && validationOK; i++) {
                AFAffiliation affIdem = (AFAffiliation) mgr.getEntity(i);
                if (!affIdem.getIdTiers().equals(getIdTiers())) {
                    // une autre affiliation existe!
                    message.append(getSession().getLabel("AFFILIATION_NUM_EXISTE") + "\n");
                    validationOK = false;
                }
            }
        }

        // *******************************************************************
        // Date Debut
        // *******************************************************************
        if (validationOK) {
            // Contrôle que la date de début soit compris entre le 01.01.1990 et
            // la date du jour + 10 années
            String dateLimiteInferieur = "01.01.1900";
            String dateInitiale = JACalendar.todayJJsMMsAAAA();

            // Validité Date
            try {
                BSessionUtil.checkDateGregorian(getSession(), getDateDebut());
            } catch (Exception e) {
                message.append(getSession().getLabel("160") + "\n");
                validationOK = false;
            }

            try {
                String dateLimiteSuperieur = getSession().getApplication().getCalendar().addYears(dateInitiale, 10);

                // Test Date Debut >= Date Limite Inferieur
                if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getDateDebut(), dateLimiteInferieur)) {
                    // Test Date Debut <= Date Limite Superieur
                    if (!BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), dateLimiteSuperieur, getDateDebut())) {
                        message.append(getSession().getLabel("50") + "\n");
                        validationOK = false;
                    }
                } else {
                    message.append(getSession().getLabel("60") + "\n");
                    validationOK = false;
                }
            } catch (Exception e) {
                message.append(getSession().getLabel("940") + "\n");
                validationOK = false;
            }
        }

        // *******************************************************************
        // Date Fin
        // *******************************************************************
        if (validationOK) {
            if (JadeStringUtil.isBlankOrZero(getDateFin())) {
                // Si pas de date de fin, le motif de fin doit être vide
                if (!JadeStringUtil.isIntegerEmpty(getMotifFin())) {
                    message.append(getSession().getLabel("170") + "\n");
                    validationOK = false;
                }
            } else {
                // Validité Date
                try {
                    BSessionUtil.checkDateGregorian(getSession(), getDateFin());
                } catch (Exception e) {
                    message.append(getSession().getLabel("180") + "\n");
                    validationOK = false;
                }

                // Si le motif de fin est vide
                if ((JadeStringUtil.isIntegerEmpty(getMotifFin()))) {
                    message.append(getSession().getLabel("190") + "\n");
                    validationOK = false;
                }

                // Date Fin > Date Debut
                if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDebut(), getDateFin())) {
                    message.append(getSession().getLabel("90") + "\n");
                    validationOK = false;
                }
            }
        }

        return message.toString();
    }

    /**
     * Getter de codeSUVA
     *
     * @return the codeSUVA
     */
    public String getCodeSUVA() {
        return codeSUVA;
    }

    /**
     * Setter de codeSUVA
     *
     * @param codeSUVA the codeSUVA to set
     */
    public void setCodeSUVA(String codeSUVA) {
        this.codeSUVA = codeSUVA;
    }

    /**
     * Retourne <br>
     * - true si la date de fin est présente (l'affiliation est terminée,radiée)<br>
     * - false si l'affiliation est en cours (date de fin non renseignée).
     *
     * @return true or false
     */
    public boolean isRadie() {
        return !isEnCours();
    }

    /**
     * Retourne <br>
     * - false si la date de fin est présente (l'affiliation est terminée,radiée)<br>
     * - true si l'affiliation est en cours (date de fin non renseignée).
     *
     * @return true or false
     */
    public boolean isEnCours() {
        return JadeStringUtil.isBlankOrZero(dateFin);
    }

    /**
     * Retourne <br>
     * - true si le numéro IDE à le status définitif
     * - false sinon
     *
     * return true or false
     */
    public boolean isIdeDefinitif() {
        return CodeSystem.STATUT_IDE_DEFINITIF.equals(ideStatut);
    }

    /**
     * Retourne <br>
     * - true si le numéro IDE à le status provisoire
     * - false sinon
     *
     * return true or false
     */
    public boolean isIdeProvisoire() {
        return CodeSystem.STATUT_IDE_PROVISOIRE.equals(ideStatut);
    }

    public java.lang.String getConvention() {
        return convention;
    }

    public void setConvention(java.lang.String convention) {
        this.convention = convention;
    }

    /**
     * Permet d'appeler un service d'extension définit dans le fichier globazProduct.xml
     */
    public void callExtensionContextsAfterUpdate() {
        try {
            String extensionContexts = AFProperties.IDE_EXTENSION_CONTEXTS.getValue();

            if (JadeStringUtil.isBlankOrZero(extensionContexts)) {
                return;
            }

            List<String> listExtensionContexts = Arrays.asList(extensionContexts.split(","));

            for (BIEntityExternalService externalService : (List<BIEntityExternalService>) getExternalServices()) {
                logger.debug("context name : " + externalService.getContextName());
                if (listExtensionContexts.contains(externalService.getContextName())) {
                    externalService.afterUpdate(this);
                }
            }
        } catch (Throwable e) {
            logger.error("unable to call after update of external extensions", e);
        }
    }
}
