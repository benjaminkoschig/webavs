package globaz.naos.db.cotisation;

import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.db.declaration.DSLigneDeclarationListViewBean;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JATime;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.api.helper.IAFCotisationHelper;
import globaz.naos.application.AFApplication;
import globaz.naos.db.adhesion.AFAdhesion;
import globaz.naos.db.adhesion.AFAdhesionManager;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.annonceAffilie.AFAnnonceAffilie;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.couverture.AFCouverture;
import globaz.naos.db.couverture.AFCouvertureManager;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.db.planAffiliation.AFPlanAffiliationListViewBean;
import globaz.naos.db.planAffiliation.AFPlanAffiliationManager;
import globaz.naos.db.planAssurance.AFPlanAssurance;
import globaz.naos.db.planAssurance.AFPlanAssuranceListViewBean;
import globaz.naos.db.planCaisse.AFPlanCaisse;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.db.tauxAssurance.AFTauxAssuranceManager;
import globaz.naos.db.tauxAssurance.AFTauxVariableUtil;
import globaz.naos.db.tauxAssurance.AFTauxVariableUtil.TauxCalcul;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFAgeRente;
import globaz.naos.util.AFIDERules;
import globaz.naos.util.AFUtil;
import globaz.osiris.api.APIRubrique;
import globaz.phenix.db.principale.CPCotisationManager;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (28.05.2002 09:11:43)
 * 
 * @author: Administrator
 */
public class AFCotisation extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final JACalendar CALENDAR = new JACalendarGregorian();

    public static final String FIELDNAME_ADHESION_ID = "MRIADH";
    public static final String FIELDNAME_ASSURANCE_ID = "MBIASS";
    public static final String FIELDNAME_COTISATION_ID = "MEICOT";
    public static final String FIELDNAME_DATE_DEB = "MEDDEB";
    public static final String FIELDNAME_DATE_FIN = "MEDFIN";
    public static final String FIELDNAME_MAISON_MERE = "MEBMER";
    public static final String FIELDNAME_PLANAFFILIATION_ID = "MUIPLA";
    public static final String FIELDNAME_PLANCAISSE_ID = "MSIPLC";
    public static final String TABLE_NAME = "AFCOTIP";
    public final static String TYPE_CONTROLE_EMPLOYEUR = "122004";
    private static final String TYPE_EXPLOITATION = "508021";

    // @BMS-ONLY
    private AFCotisation oldCotisation = null;

    /**
     * Renvoie la rubrique comptable pour la cotisation definie par l'ID donné.
     * 
     * @param idCotisation la ID de la cotisation
     * @param session la session
     * @return Le Code System de la rubrique
     */
    public static String _getRubrique(String idCotisation, BSession session) {

        try {
            AFCotisation coti = new AFCotisation();
            coti.setSession(session);
            coti.setCotisationId(idCotisation);
            coti.retrieve();
            if (!coti.isNew()) {
                return coti.getAssurance().getRubriqueId();
            } else {
                return "";
            }
        } catch (Exception e) {
            JadeLogger.error(null, e);
            return "";
        }
    }

    /**
     * Retourne une liste d'Assurances pour une Affiliation données.
     * 
     * @param httpSession
     * @param IdAffiliation
     * @return
     */
    public static Vector<String[]> getListAssuranceAffiliation(HttpSession httpSession, String IdAffiliation) {

        Vector<String[]> vList = new Vector<String[]>();
        String[] element = null;

        try {
            AFPlanAffiliationManager planAffMananger = new AFPlanAffiliationManager();
            planAffMananger.setSession((BSession) CodeSystem.getSession(httpSession));
            planAffMananger.setForAffiliationId(IdAffiliation);
            planAffMananger.find();
            for (int i = 0; i < planAffMananger.size(); i++) {
                AFPlanAffiliation planAffiliation = (AFPlanAffiliation) planAffMananger.getEntity(i);
                if (!planAffiliation.isInactif().booleanValue()) {
                    AFCotisationManager cotiManager = new AFCotisationManager();
                    cotiManager.setISession(CodeSystem.getSession(httpSession));
                    cotiManager.setForPlanAffiliationId(planAffiliation.getPlanAffiliationId());
                    cotiManager.find();

                    for (int j = 0; j < cotiManager.size(); j++) {
                        AFCotisation cotisation = (AFCotisation) cotiManager.getEntity(j);

                        element = new String[2];
                        element[0] = cotisation.getCotisationId();
                        element[1] = cotisation.getAssurance().getAssuranceLibelleCourt();
                        vList.add(element);
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(null, e);
        }
        return vList;
    }

    private AFAdhesion _adhesion = null;

    private AFAffiliation _affiliation = null;
    private AFAssurance _assurance = null;
    private AFCouverture _couverture = null;
    // private AFTauxAssurance _tauxAssurance = null;
    private AFTauxAssurance _montantAssurance = null;
    private AFPlanAffiliation _planAffiliation = null;
    private AFPlanCaisse _planCaisse = null;
    private TITiers _tiers = null;

    private java.lang.String adhesionId = new java.lang.String();

    private java.lang.String affiliationId = new java.lang.String();
    private java.lang.String anneeDecision = new java.lang.String();
    // Foreign key
    private java.lang.String assuranceId = new java.lang.String();
    private java.lang.String categorieTauxId = new java.lang.String();
    // DB
    // Primary Key
    private java.lang.String cotisationId = new java.lang.String();
    // Field
    private java.lang.String dateDebut = new java.lang.String();
    private java.lang.String dateFin = new java.lang.String();
    private java.lang.String dateFinMin = "";

    /*
     * private FWParametersSystemCode csMotifFin = null; private FWParametersSystemCode csPeriodicite = null;
     */

    private java.lang.String directe = new java.lang.String();
    private java.lang.Boolean exceptionPeriodicite = new Boolean(false);
    private Boolean inactive = new Boolean(false);
    private java.lang.Boolean maisonMere = new Boolean(false);

    private java.lang.String masseAnnuelle = new String();

    private java.lang.String masseId = new String();
    private java.lang.String montantAnnuel = new String();
    private java.lang.String montantMensuel = new String();
    private java.lang.String montantSemestriel = new String();
    private java.lang.String montantTrimestriel = new String();
    private java.lang.String motifFin = new String();
    private boolean passageValidate = true;
    private java.lang.String periodicite = new String();
    private java.lang.String planAffiliationId = new String();

    private java.lang.String planCaisseId = new String();

    private java.lang.String tauxAssuranceId = new String();

    private String traitementMoisAnnee = "";

    private java.lang.Boolean miseAjourDepuisEcran = new Boolean(true);

    /**
     * Constructeur d'AFCotisation
     */
    public AFCotisation() {
        super();
        setMethodsToLoad(IAFCotisationHelper.METHODS_TO_LOAD);
    }

    /**
     * Effectue des traitements après un ajout dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_afterAdd(BTransaction)
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {

        // si on ajoute la cotisation "Amat genevoise" et que la propertie
        // modifierTagAmatGenevoise=true modifier dans l'affiliation
        // le champ "Décl. salaires"
        if ("true".equals(getSession().getApplication().getProperty(AFApplication.PROPERTY_MODIFIER_TAG_AMATGENEVOISE))) {
            AFAssurance assurance = new AFAssurance();
            assurance = getAssurance();
            if (CodeSystem.TYPE_ASS_MATERNITE.equals(assurance.getTypeAssurance())
                    && IConstantes.CS_LOCALITE_CANTON_GENEVE.equals(assurance.getAssuranceCanton())) {
                AFAffiliation affilie = new AFAffiliation();
                affilie.setSession(getSession());
                affilie.setAffiliationId(getAffiliation().getAffiliationId());
                affilie.retrieve();
                if ((affilie != null) && !affilie.isNew()) {
                    affilie.setDeclarationSalaire(CodeSystem.DECL_SAL_AMAT_GE);
                    affilie.update(transaction);
                }
            }
        }
        super._afterAdd(transaction);
    }

    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {

        super._afterUpdate(transaction);
    }

    @Override
    public boolean _allowDelete() {

        // 1) Suppression des cotisation de type exceptions authorisée
        if (CodeSystem.MOTIF_FIN_EXCEPTION.equals(getMotifFin())) {
            return true; // suppression authorisée
        }

        // 2) pour les cotisations "régulière", il faut vérifier si la cotisation a des liens.

        if (getAssurance() != null) {
            // Spécifique CCVS selon HNA 06.04.2011
            if (CodeSystem.TYPE_ASS_AFI.equalsIgnoreCase(getAssurance().getTypeAssurance())) {
                return true;
            }
            // contrôle des cotisations personnelles
            if (CodeSystem.GENRE_ASS_PERSONNEL.equals(getAssurance().getAssuranceGenre())) {
                CPCotisationManager mgr = new CPCotisationManager();
                mgr.setSession(getSession());
                mgr.setForIdCotiAffiliation(getCotisationId());
                try {
                    if (mgr.getCount() > 0) {
                        getSession().addError(
                                "La cotisation ne peut pas être supprimée, car elle possède une décision" + " "
                                        + getAssurance().getAssuranceLibelle() + ".");

                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }
                // si on trouve un lien dans cotisation personnelle, on empêche la suppression

            }
            if (CodeSystem.GENRE_ASS_PARITAIRE.equals(getAssurance().getAssuranceGenre())) {
                int anneeDebut;
                try {
                    anneeDebut = JACalendar.getYear(getDateDebut());

                    DSDeclarationListViewBean l = new DSDeclarationListViewBean();
                    l.setSession(getSession());
                    l.setFromAnnee(String.valueOf(anneeDebut));
                    l.setForAffiliationId(getAffiliation().getAffiliationId());
                    l.find();

                    if (l.getSize() > 0) {
                        boolean notAllowedToDelete = checkDS(((DSDeclarationViewBean) l.getFirstEntity())
                                .getIdDeclaration());
                        if (notAllowedToDelete) {
                            // declaration existe - pas du suppression.
                            getSession().addError(
                                    "La cotisation ne peut pas être supprimée, car elle est liée à une déclaration.");
                            return false;
                        } else {
                            return true;
                        }

                    }

                } catch (Exception e) {
                    getSession().addError("A compléter");
                    return false;
                }
            }
        }
        // Si pas d'assurance
        return true;
    }

    /**
     * Effectue des traitements avant un ajout dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // incrémente de +1 le numéro
        setCotisationId(this._incCounter(transaction, "0"));

        if (CodeSystem.TYPE_ASS_FFPP.equalsIgnoreCase(getAssurance().getTypeAssurance())) {
            setPeriodicite(CodeSystem.PERIODICITE_ANNUELLE);
            setExceptionPeriodicite(new Boolean(true));
            setTraitementMoisAnnee(((AFApplication) getSession().getApplication()).getMoisFFPP());
        }

        // Si la périodicité est diférente qu'annuelle, alors le champs
        // moisAnnee doit être vidé
        if (!CodeSystem.PERIODICITE_ANNUELLE.equals(periodicite)) {
            traitementMoisAnnee = "";
        }

        // tests sur type d'assurance indépendantes
        _validationTypeAssurance(transaction);

        AFAnnonceAffilie annonce = new AFAnnonceAffilie();
        annonce.setChampModifier(CodeSystem.CHAMPS_MOD_CREATION_COTI);
        annonce.setUtilisateur(getSession().getUserName());
        annonce.setAffiliationId(getPlanAffiliation().getAffiliationId());
        annonce.setChampAncienneDonnee(getSession().getLabel("1060"));
        annonce.setDateEnregistrement(JACalendar.todayJJsMMsAAAA());

        JATime now = new JATime(JACalendar.now());
        annonce.setHeureEnregistrement(now.toStr(""));
        annonce.setSession(getSession());
        annonce.add(transaction);

        // test de cohérence avec les adhésions
        checkAdhesion(transaction);

        validePeriodiciteAffCoti(transaction);

        checkExceptionPeriodicite(transaction);

        fillCategorieCotisationInfoRom354Lot2(transaction);

        AFIDERules.controleGenerateAnnonceOuvertureCotisation(getSession(), this);
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {

        if (getSession().hasErrors()) {
            transaction.addErrors(getSession().getErrors().toString());
        }
    }

    /**
     * Effectue des traitements avant une mise à jour dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {

        // Si la périodicité est diférente qu'annuelle, alors le champs
        // moisAnnee doit être vidé
        if (!CodeSystem.PERIODICITE_ANNUELLE.equals(periodicite)) {
            traitementMoisAnnee = "";
        }
        // Création des annonces de mutation
        AFCotisation ancien = creationAnnonceCotisation(transaction);

        // @BMS-ONLY --> Commenté pour ne pas impacter WEB@AVS
        // oldCotisation = ancien;

        // si changement de plan caisse, on efface l'id de l'adhésion
        if (!ancien.getPlanCaisseId().equals(getPlanCaisseId())) {
            setAdhesionId("");
        }
        // test de cohérence avec les adhésions
        checkAdhesion(transaction);

        validePeriodiciteAffCoti(transaction);

        checkExceptionPeriodicite(transaction);

        fillCategorieCotisationInfoRom354Lot2(transaction);

        AFIDERules.controleGenerateAnnonceModifCotisation(getSession(), this);
    }

    private AFCotisation creationAnnonceCotisation(BTransaction transaction) throws Exception {
        AFCotisation ancien = new AFCotisation();
        ancien.setCotisationId(getCotisationId());
        ancien.setSession(getSession());
        ancien.retrieve(transaction);

        AFAnnonceAffilie annonce = new AFAnnonceAffilie();
        annonce.setSession(getSession());
        annonce.setUtilisateur(getSession().getUserName());
        annonce.setAffiliationId(getPlanAffiliation().getAffiliationId());
        annonce.setDateEnregistrement(JACalendar.todayJJsMMsAAAA());
        annonce.setHeureEnregistrement(new JATime(JACalendar.now()).toStr(""));

        if (!ancien.getDateDebut().equals(getDateDebut())) {
            annonce.setChampModifier(CodeSystem.CHAMPS_MOD_DATE_DEBUT_COTI);
            annonce.setChampAncienneDonnee(ancien.getDateDebut());
            annonce.add(transaction);
        }
        if (!ancien.getDateFin().equals(getDateFin())) {
            annonce.setChampModifier(CodeSystem.CHAMPS_MOD_DATE_FIN_COTI);
            annonce.setChampAncienneDonnee(ancien.getDateFin());
            annonce.add(transaction);
        }
        if (!ancien.getAssuranceId().equals(getAssuranceId())) {
            annonce.setChampModifier(CodeSystem.CHAMPS_MOD_ASSURANCE_COTI);
            annonce.setChampAncienneDonnee(ancien.getAssuranceId());
            annonce.add(transaction);
        }
        if (!ancien.getAnneeDecision().equals(getAnneeDecision())) {
            annonce.setChampModifier(CodeSystem.CHAMPS_MOD_ANNEE_DECIS_COTI);
            annonce.setChampAncienneDonnee(ancien.getAnneeDecision());
            annonce.add(transaction);
        }
        if (!ancien.getMontantAnnuel().equals(getMontantAnnuel())) {
            annonce.setChampModifier(CodeSystem.CHAMPS_MOD_MONT_ANNU_COTI);
            annonce.setChampAncienneDonnee(ancien.getMontantAnnuel());
            annonce.add(transaction);
        }
        if (!ancien.getMasseAnnuelle().equals(getMasseAnnuelle())) {
            if (!(JadeStringUtil.isDecimalEmpty(ancien.getMasseAnnuelle()) && JadeStringUtil
                    .isDecimalEmpty(getMasseAnnuelle()))) {
                annonce.setChampModifier(CodeSystem.CHAMPS_MOD_MASSE_ANNU_COTI);
                annonce.setChampAncienneDonnee(ancien.getMasseAnnuelle());
                annonce.add(transaction);
            }
        }
        return ancien;
    }

    /**
     * Creation d'une cotisation.
     * 
     * @param transaction la transaction a utiliser
     * @param session la session
     * @param affiliationId l'affiliation ID
     * @param idTiers le tiers ID
     * @param planId le plan ID
     * @throws Exception
     */
    public void _creationCotisation(BTransaction transaction, BSession session, String affiliationId, String idTiers,
            String planId) throws Exception {

        AFAffiliation affiliation = new AFAffiliation();
        affiliation.setIdTiers(idTiers);
        affiliation.setAffiliationId(affiliationId);
        affiliation.setSession(session);
        affiliation.retrieve(transaction);

        // Création du plan d'affiliation
        // Chercher s'il existe déja un plan d'affiliation
        AFPlanAffiliationListViewBean planAffiliationList = new AFPlanAffiliationListViewBean();
        planAffiliationList.setSession(getSession());
        planAffiliationList.setForAffiliationId(affiliationId);
        planAffiliationList.find(transaction);

        AFPlanAffiliation planAffiliation = null;
        if (planAffiliationList.size() == 0) {
            planAffiliation = new AFPlanAffiliation();
            planAffiliation.setSession(getSession());
            planAffiliation.setAffiliationId(affiliationId);
            String planAffiliationLibelle = affiliation.getAffilieNumero() + "_" + affiliation.getDateDebut();
            planAffiliation.setLibelle(planAffiliationLibelle);
            planAffiliation.add(transaction);
        } else {
            planAffiliation = (AFPlanAffiliation) planAffiliationList.getEntity(0);
        }

        AFPlanAssuranceListViewBean planAssuList = new AFPlanAssuranceListViewBean();
        planAssuList.setForPlanId(planId);
        planAssuList.setSession(session);
        planAssuList.find(transaction);

        AFCotisation cotisation = new AFCotisation();
        cotisation.setSession(session);
        cotisation.setPlanAffiliationId(planAffiliation.getPlanAffiliationId());
        cotisation.setDateDebut(affiliation.getDateDebut());
        if (!JadeStringUtil.isBlankOrZero(affiliation.getDateFin())) {
            cotisation.setDateFin(affiliation.getDateFin());
            cotisation.setMotifFin(affiliation.getMotifFin());
        }
        cotisation.setPeriodicite(affiliation.getPeriodicite());

        for (int i = 0; i < planAssuList.size(); i++) {
            cotisation.setAssuranceId(planAssuList.getAssuranceId(i));
            cotisation.add(transaction);
        }
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return AFCotisation.TABLE_NAME; // "AFCOTIP";
    };

    /**
     * Crée les cotisation pour l'affilation données en fonction du plan d'assurance selectionné. Crée aussi un plan
     * d'affiliation si il n'y en a aucun par défaut.
     * 
     * @param newAffiliationId l'ID de l'affiliation
     * @param newPlanId l'ID du plan d'assurance
     * @return l'ID du plan d'affiliation
     * @throws Exception
     */
    public String _planAutomatique(String newAffiliationId, String newPlanId) throws Exception {

        String result = "";

        try {
            // Selection de l'affiliation
            AFAffiliation affiliation = new AFAffiliation();
            affiliation.setSession(getSession());
            affiliation.setAffiliationId(newAffiliationId);
            affiliation.setSession(getSession());
            affiliation.retrieve();

            // Création du plan d'affiliation
            // Chercher s'il existe déja un plan d'affiliation
            AFPlanAffiliationListViewBean planAffiliationList = new AFPlanAffiliationListViewBean();
            planAffiliationList.setSession(getSession());
            planAffiliationList.setForAffiliationId(newAffiliationId);
            planAffiliationList.find();

            AFPlanAffiliation planAffiliation = null;
            if (planAffiliationList.size() == 0) {
                planAffiliation = new AFPlanAffiliation();
                planAffiliation.setSession(getSession());
                planAffiliation.setAffiliationId(newAffiliationId);
                String planAffiliationLibelle = affiliation.getAffilieNumero() + "_" + affiliation.getDateDebut();
                planAffiliation.setLibelle(planAffiliationLibelle);
                planAffiliation.add();
            } else {
                planAffiliation = (AFPlanAffiliation) planAffiliationList.getEntity(0);
            }
            result = planAffiliation.getPlanAffiliationId();

            // Initialisation de la nouvelle cotisation
            AFCotisation cotisation = new AFCotisation();
            cotisation.setSession(getSession());
            cotisation.setDateDebut(affiliation.getDateDebut());
            if (!JadeStringUtil.isBlankOrZero(affiliation.getDateFin())) {
                cotisation.setDateFin(affiliation.getDateFin());
                cotisation.setMotifFin(affiliation.getMotifFin());
            }
            cotisation.setPeriodicite(affiliation.getPeriodicite());
            cotisation.setPlanAffiliationId(planAffiliation.getPlanAffiliationId());

            // Pour chaque assurance du Plan d'assurance,
            // créer la cotisation correspondante
            AFPlanAssuranceListViewBean planAssList = new AFPlanAssuranceListViewBean();
            planAssList.setForPlanId(newPlanId);
            planAssList.setSession(getSession());
            planAssList.find();

            for (int i = 0; i < planAssList.size(); i++) {
                AFPlanAssurance planAss = (AFPlanAssurance) planAssList.getEntity(i);

                // Création de la nouvelle cotisation
                cotisation.setAssuranceId(planAss.getAssuranceId());
                cotisation.add();
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return result;
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        cotisationId = statement.dbReadNumeric("MEICOT");
        assuranceId = statement.dbReadNumeric("MBIASS");
        planAffiliationId = statement.dbReadNumeric("MUIPLA");
        planCaisseId = statement.dbReadNumeric("MSIPLC");
        adhesionId = statement.dbReadNumeric("MRIADH");
        tauxAssuranceId = statement.dbReadNumeric("MCITAU");
        categorieTauxId = statement.dbReadNumeric("METCAT");
        dateDebut = statement.dbReadDateAMJ("MEDDEB");
        dateFin = statement.dbReadDateAMJ("MEDFIN");
        motifFin = statement.dbReadNumeric("METMOT");
        periodicite = statement.dbReadNumeric("METPER");
        exceptionPeriodicite = statement.dbReadBoolean("METPEE");
        maisonMere = statement.dbReadBoolean("MEBMER");
        masseAnnuelle = statement.dbReadNumeric("MEMMAP", 2);
        montantTrimestriel = statement.dbReadNumeric("MEMTRI", 2);
        montantSemestriel = statement.dbReadNumeric("MEMSEM", 2);
        montantAnnuel = statement.dbReadNumeric("MEMANN", 2);
        montantMensuel = statement.dbReadNumeric("MEMMEN", 2);
        anneeDecision = statement.dbReadNumeric("MENANN"); // TODO: faudrait-il
        inactive = statement.dbReadBoolean("INACTIVE");
        // enlever le ', 2'
        // ?
        traitementMoisAnnee = statement.dbReadNumeric("METMOA"); // renseigné
        // pour les
        // périodes
        // annuells,
        // dit le
        // mois pour
        // lequel il
        // faut
        // envoyer
        // la
        // facture
        // date de fin minimum uniquement utilisée dans la vue "résumé"
        dateFinMin = statement.dbReadDateAMJ("MEDFINMIN");
    }

    /**
     * Retour l'Affiliation qui correspond au critères de recherche.
     * 
     * @param transaction
     * @param noAffilie
     * @param annee
     * @param genre
     * @return
     * @throws Exception
     */
    public AFCotisation _retourCotisation(BTransaction transaction, String idAffiliation, String annee, String genre,
            String type) throws Exception {

        AFCotisation cotisationDemander = null;
        int nb = 0;
        String dateDebutBk = "";
        String dateFinBk = "";
        String dateDebutCoti = "";
        String dateFinCoti = "";
        try {
            int anneeControle = Integer.parseInt(annee);

            AFCotisationManager cotiManager = new AFCotisationManager();
            cotiManager.setForAffiliationId(idAffiliation);
            cotiManager.setForTypeAssurance(type);
            cotiManager.setForGenreAssurance(genre);
            cotiManager.setSession(getSession());
            cotiManager.find(transaction);
            for (int i = 0; i < cotiManager.size(); i++) {
                AFCotisation cotisation = (AFCotisation) cotiManager.getEntity(i);
                // Ne pas prendre les assurances annulées
                if (!cotisation.getDateDebut().equalsIgnoreCase(cotisation.getDateFin())) {
                    // if
                    // (cotisation.getAssurance().getTypeAssurance().equals(type)
                    // && cotisation.getAssurance().getAssuranceGenre()
                    // .equals(genre)) {
                    int anneeDebCotisation = JACalendar.getYear(cotisation.getDateDebut());
                    int anneeFinCotisation = JACalendar.getYear(cotisation.getDateFin());
                    if (((anneeControle >= anneeDebCotisation) && (anneeControle <= anneeFinCotisation))

                    || ((anneeControle >= anneeDebCotisation) && (anneeFinCotisation == 0))) {
                        // PO 3799 - Garder la première assurance trouvée
                        if (anneeDebCotisation == anneeControle) {
                            dateDebutCoti = cotisation.getDateDebut();
                        } else {
                            dateDebutCoti = "01.01." + anneeControle;

                        }
                        if (anneeFinCotisation == anneeControle) {
                            dateFinCoti = cotisation.getDateFin();
                        } else {
                            dateFinCoti = "31.12." + anneeControle;
                        }
                        if (cotisationDemander == null) {
                            cotisationDemander = cotisation;
                            dateDebutBk = dateDebutCoti;
                            dateFinBk = dateFinCoti;
                            nb++;
                        } else {

                            // Test si il y a chevauchement de période
                            // 1 - dates de cotisation comprise entre dates
                            // sauvegardées => erreur
                            if (BSessionUtil.compareDateBetweenOrEqual(getSession(), dateDebutBk, dateFinBk,
                                    dateDebutCoti)
                                    || BSessionUtil.compareDateBetweenOrEqual(getSession(), dateDebutBk, dateFinBk,
                                            dateFinCoti)) {
                                nb++;
                            } else {

                                // Sauvegarde de la date de début si elle est
                                // inférieure à celle sauvegardée
                                if (BSessionUtil.compareDateFirstLower(getSession(), dateDebutCoti, dateDebutBk)) {
                                    dateDebutBk = dateDebutCoti;
                                }
                                if (BSessionUtil.compareDateFirstGreater(getSession(), dateFinCoti, dateFinBk)) {
                                    dateFinBk = dateFinCoti;
                                }
                            }
                        }
                    }
                    // }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            _addError(transaction, "erreur");
        }
        if (nb > 1) {
            _addError(transaction, getSession().getLabel("1000"));
            return null;
        } else {
            return cotisationDemander;
        }
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        AFAssurance theAssurance = getAssurance();
        String theAssLib = "";
        if (theAssurance != null) {
            theAssLib = theAssurance.getAssuranceLibelle();
        }

        if ((getInactive().booleanValue() == false) && getPassageValidate()) {

            boolean validationOK = true;

            validationOK &= _propertyMandatory(statement.getTransaction(), getAssuranceId(),
                    getSession().getLabel("520"));
            validationOK &= _propertyMandatory(statement.getTransaction(), getDateDebut(), getSession().getLabel("20"));
            validationOK &= _propertyMandatory(statement.getTransaction(), getPlanAffiliationId(), getSession()
                    .getLabel("1420"));
            if (!CodeSystem.TYPE_AFFILI_FICHIER_CENT.equals(getAffiliation().getTypeAffiliation())) {
                validationOK &= _propertyMandatory(statement.getTransaction(), getPeriodicite(),
                        getSession().getLabel("510"));
            }

            validationOK &= _checkRealDate(statement.getTransaction(), getDateDebut(), getSession().getLabel("160"));

            if (validationOK) {
                try {
                    // -----------------------------------------------------
                    // Date de Debut
                    // -----------------------------------------------------
                    String dateLimiteInf = "01.01.1900";
                    String dateInitiale = JACalendar.todayJJsMMsAAAA();
                    String dateLimiteSup = getSession().getApplication().getCalendar().addYears(dateInitiale, 10);

                    if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getDateDebut(), dateLimiteInf)) {
                        if (BSessionUtil.compareDateFirstGreater(getSession(), getDateDebut(), dateLimiteSup)) {
                            _addError(statement.getTransaction(), getSession().getLabel("50"));
                            validationOK = false;
                        }
                    } else {
                        _addError(statement.getTransaction(), getSession().getLabel("60"));
                        validationOK = false;
                    }

                    // -----------------------------------------------------
                    // Date de Fin
                    // -----------------------------------------------------
                    if (!JadeStringUtil.isIntegerEmpty(getMotifFin())
                            && getMotifFin().equals(CodeSystem.MOTIF_FIN_EXCEPTION)) {

                        if (JadeStringUtil.isBlankOrZero((getDateFin()))) {
                            _addError(statement.getTransaction(), getSession().getLabel("30") + " "
                                    + getAssurance().getAssuranceLibelle());
                        } else {
                            validationOK &= _checkRealDate(statement.getTransaction(), getDateFin(), getSession()
                                    .getLabel("180"));

                            if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDebut(), getDateFin())) {
                                _addError(statement.getTransaction(), getSession().getLabel("550") + " "
                                        + getAssurance().getAssuranceLibelle());
                                validationOK = false;
                            }
                        }

                    } else {
                        if (JadeStringUtil.isBlankOrZero(getDateFin())) {
                            // Si pas de date de fin, le motif de fin doit être
                            // vide
                            if (!JadeStringUtil.isIntegerEmpty(getMotifFin())) {
                                _addError(statement.getTransaction(), getSession().getLabel("170"));
                                validationOK = false;
                            }
                        } else {
                            // Date de fin renseignée, test de la validité, test
                            // plus grande que la date de début ou égale à la
                            // date de début moins 1 jours
                            validationOK &= _checkRealDate(statement.getTransaction(), getDateFin(), getSession()
                                    .getLabel("180"));

                            // Si le motif de fin est vide
                            if (JadeStringUtil.isIntegerEmpty(getMotifFin())) {
                                _addError(statement.getTransaction(), getSession().getLabel("190"));
                                validationOK = false;
                            }

                            if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDebut(), getDateFin())) {
                                _addError(statement.getTransaction(), getSession().getLabel("550") + " "
                                        + getAssurance().getAssuranceLibelle());
                            }
                        }
                    }

                    // ---------------------------------------------------
                    // Controle des Date avec les dates de l'affiliation
                    // ---------------------------------------------------
                    if (validationOK) {
                        validationOK = controleDateCotisationSelonDateAffiliation(statement);
                    }

                    // --------------------------------------------------------------------------------------------
                    // Controle des dates avec les dates de l'adhesion - PO 8177
                    // --------------------------------------------------------------------------------------------
                    if (validationOK) {
                        AFAdhesion adh = getAdhesion();
                        if (adh != null) {
                            validationOK = controleDateCotisationSelonDateAdhesion(statement, theAssLib, adh);
                        }
                    }

                    // --------------------------------------------------------------------------------------------
                    // Controle des dates avec les dates de la couverture si
                    // adhesion a une caisse professionnelle
                    // --------------------------------------------------------------------------------------------
                    if (validationOK) {

                        AFCouverture couverture = getCouverture();
                        if (couverture != null) {
                            validationOK = controleDateCotisationSelonDateCouverture(statement, theAssLib, couverture);
                        }
                    }
                    // ----------------------------------------------------------
                    // Controle des dates avec les dates des autres cotisations
                    // ----------------------------------------------------------
                    if (validationOK) {
                        if (!JadeStringUtil.isIntegerEmpty(getMotifFin())
                                && getMotifFin().equals(CodeSystem.MOTIF_FIN_EXCEPTION)) {
                            _validationException(statement.getTransaction());
                        } else {
                            _validationDate(statement.getTransaction());
                        }
                    }

                } catch (Exception e) {
                    JadeLogger.error(this, e);
                    validationOK = false;
                }
            }

            // Pas de montant de masse salariale si les montants de cotisations
            // personnelles sont remplis
            if (!JadeStringUtil.isIntegerEmpty(getMasseAnnuelle())
                    && (!JadeStringUtil.isIntegerEmpty(getMontantAnnuel())
                            || !JadeStringUtil.isIntegerEmpty(getMontantSemestriel())
                            || !JadeStringUtil.isIntegerEmpty(getMontantTrimestriel()) || !JadeStringUtil
                                .isIntegerEmpty(getMontantMensuel()))) {
                _addError(statement.getTransaction(), getSession().getLabel("580"));
            }
            // Si la maison mère est cochée et masse périodicité non vide
            // erreur!!
            if (maisonMere.booleanValue() && !JadeStringUtil.isIntegerEmpty(getMasseAnnuelle())) {
                _addError(statement.getTransaction(), getSession().getLabel("590"));
            }

            // Validation du taux / catéorie

            // InfoRom 354 Lot 2 autorise
            // - une cotisation FAD pers avec un taux forcé et une catégorie
            // - une assurance FAD pers avec taux par caisse et une cotisation la référençant avec une catégorie

            boolean isCotiFADPers = CodeSystem.GENRE_ASS_PERSONNEL.equalsIgnoreCase(getAssurance().getAssuranceGenre())
                    && CodeSystem.TYPE_ASS_FRAIS_ADMIN.equalsIgnoreCase(getAssurance().getTypeAssurance());

            if (!isCotiFADPers && !JadeStringUtil.isBlankOrZero(getCategorieTauxId())
                    && getAssurance().isTauxParCaisse().booleanValue()) {
                _addError(statement.getTransaction(), getSession().getLabel("2120"));
            }
            if (!isCotiFADPers && !JadeStringUtil.isBlankOrZero(getTauxAssuranceId())
                    && !JadeStringUtil.isBlankOrZero(getCategorieTauxId())) {
                _addError(statement.getTransaction(), getSession().getLabel("2130"));
            }

        }

    }

    /**
     * Control les dates de début et de fin de Cotisation pour un Plan d'affiliation et un type d'assurance.
     * 
     * @param transaction la transaction a utiliser
     * @return true - Si les dates sont valides
     * @throws Exception
     */
    private void _validationDate(BTransaction transaction) throws Exception {

        // *********************************************************
        // Test Date de Début, Date de Fin
        // *********************************************************

        AFCotisationManager cotisationList = new AFCotisationManager();
        cotisationList.setForPlanAffiliationId(getPlanAffiliationId());
        cotisationList.setForAssuranceId(getAssuranceId());
        cotisationList.setSession(getSession());
        cotisationList.find(transaction);

        for (int i = 0; i < cotisationList.size(); i++) {

            AFCotisation cotisation = (AFCotisation) cotisationList.getEntity(i);

            // il ne faut pas tenir compte d'une cotisation ouverte et clôturée
            // à la même date
            if (JadeStringUtil.equals(cotisation.getDateDebut(), cotisation.getDateFin(), true)) {
                continue;
            }

            // Ne pas tester la cotisation avec elle meme ou avec une exception
            if (!cotisation.getCotisationId().equalsIgnoreCase(getCotisationId())
                    && !CodeSystem.MOTIF_FIN_EXCEPTION.equals(cotisation.getMotifFin())) {

                if (JadeStringUtil.isBlankOrZero(getDateFin())) {

                    // Test si il y a déjà une cotisation sans une date de fin
                    if (JadeStringUtil.isBlankOrZero(cotisation.getDateFin())) {
                        _addError(transaction, getSession().getLabel("1180") + " "
                                + cotisation.getAssurance().getAssuranceLibelle());

                    } else {
                        // Test si il n'y a pas de chevauchement avec une

                        // affiliation
                        if (BSessionUtil.compareDateFirstLowerOrEqual(transaction.getSession(), getDateDebut(),
                                cotisation.getDateFin())) {

                            _addError(
                                    transaction,
                                    FWMessageFormat.format(getSession().getLabel("1190"), getDateDebut(),
                                            cotisation.getDateDebut(), cotisation.getDateFin())
                                            + " pour l'assurance " + cotisation.getAssurance().getAssuranceLibelle());
                        }
                    }
                } else {
                    if (BSessionUtil.compareDateFirstLower(transaction.getSession(), getDateDebut(), getDateFin())) {

                        // Test si il n'y a pas de chevauchement pour un
                        // nouvelle affiliation
                        // avec date de fin avec un affiliation sans date de fin
                        if (JadeStringUtil.isBlankOrZero(cotisation.getDateFin())) {

                            if (BSessionUtil.compareDateFirstGreaterOrEqual(transaction.getSession(), getDateFin(),
                                    cotisation.getDateDebut())) {

                                _addError(
                                        transaction,
                                        FWMessageFormat.format(getSession().getLabel("1200 "), getDateDebut(),
                                                getDateFin(), cotisation.getDateDebut())
                                                + " pour l'assurance "
                                                + cotisation.getAssurance().getAssuranceLibelle());
                            }

                        } else {
                            if (BSessionUtil.compareDateBetweenOrEqual(transaction.getSession(),
                                    cotisation.getDateDebut(), cotisation.getDateFin(), getDateDebut())
                                    || BSessionUtil.compareDateBetweenOrEqual(transaction.getSession(),
                                            cotisation.getDateDebut(), cotisation.getDateFin(), getDateFin())) {

                                _addError(
                                        transaction,
                                        FWMessageFormat.format(getSession().getLabel("1210"), getDateDebut(),
                                                getDateFin(), cotisation.getDateDebut(), cotisation.getDateFin())
                                                + " pour l'assurance "
                                                + cotisation.getAssurance().getAssuranceLibelle());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Validation d'une Exception sur la masse
     * 
     * @param transaction la transaction a utiliser
     * @return true - Si les dates sont valides
     * @throws Exception
     */
    private void _validationException(BTransaction transaction) throws Exception {

        // *********************************************************
        // Test Date de Début, Date de Fin
        // *********************************************************

        AFCotisationManager cotisationList = new AFCotisationManager();
        cotisationList.setForPlanAffiliationId(getPlanAffiliationId());
        cotisationList.setForAssuranceId(getAssuranceId());
        cotisationList.setSession(getSession());
        cotisationList.find(transaction);

        AFCotisation cotisationBase = null;
        boolean foundBase = false;
        boolean foundException = false;
        // Rechercher la cotisation de base à l'exception
        for (int i = 0; i < cotisationList.size(); i++) {

            AFCotisation cotisation = (AFCotisation) cotisationList.getEntity(i);

            // Ne pas tester la cotisation avec elle meme
            if (!cotisation.getCotisationId().equalsIgnoreCase(getCotisationId())) {

                if (JadeStringUtil.isBlankOrZero(cotisation.getDateFin())) {
                    if (BSessionUtil.compareDateFirstGreaterOrEqual(transaction.getSession(), getDateDebut(),
                            cotisation.getDateDebut())) {

                        foundBase = true;
                        cotisationBase = cotisation;
                    }
                } else {
                    if (BSessionUtil.compareDateFirstGreaterOrEqual(transaction.getSession(), getDateDebut(),
                            cotisation.getDateDebut())
                            && BSessionUtil.compareDateFirstLowerOrEqual(transaction.getSession(), getDateFin(),
                                    cotisation.getDateFin())) {

                        if (cotisation.getMotifFin().equals(CodeSystem.MOTIF_FIN_EXCEPTION)) {
                            foundException = true;
                            break;
                        } else {
                            foundBase = true;
                            cotisationBase = cotisation;
                        }
                    }
                }
            }
        }

        if (foundException) {
            _addError(transaction, getSession().getLabel("NAOS_EXCEPTION_REDONDANTE"));
        }

        if (foundBase && !foundException) {

            // Controler la périodicité de l'exception
            if (!getDateDebut().substring(6).equals(getDateFin().substring(6))) {
                _addError(transaction, getSession().getLabel("1640"));
            } else {
                // int nbMoisExcep = AFUtil.nbMoisPeriode(getSession(),
                // getDateDebut(), getDateFin());

                if (CodeSystem.PERIODICITE_ANNUELLE.equals(cotisationBase.getPeriodicite())
                        || CodeSystem.PERIODICITE_ANNUELLE_31_MARS.equals(cotisationBase.getPeriodicite())
                        || CodeSystem.PERIODICITE_ANNUELLE_30_JUIN.equals(cotisationBase.getPeriodicite())
                        || CodeSystem.PERIODICITE_ANNUELLE_30_SEPT.equals(cotisationBase.getPeriodicite())) {

                    String anneeExcep = getDateFin().substring(6);

                    if (BSessionUtil.compareDateFirstLowerOrEqual(transaction.getSession(),
                            cotisationBase.getDateDebut(), "01.01." + anneeExcep)) {

                        if (!getDateDebut().equals("01.01." + anneeExcep)) {
                            _addError(transaction, "La période de l'exception doit débuter le 01.01." + anneeExcep);
                        }
                    } else {
                        if (!getDateDebut().equals(cotisationBase.getDateDebut())) {
                            _addError(transaction,
                                    "La période de l'exception doit débuter le " + cotisationBase.getDateDebut());
                        }
                    }

                    if (JadeStringUtil.isBlankOrZero(cotisationBase.getDateFin())) {

                        // Si il n'y a pas de Date de Fin à la cotisation de
                        // base
                        if (!getDateFin().equals("31.12." + anneeExcep)) {
                            _addError(transaction, "La période de l'exception doit finir le 31.12." + anneeExcep);
                        }
                    } else {

                        if (BSessionUtil.compareDateFirstGreaterOrEqual(transaction.getSession(),
                                cotisationBase.getDateFin(), "31.12." + anneeExcep)) {

                            if (!getDateFin().equals("31.12." + anneeExcep)) {
                                _addError(transaction, "La période de l'exception doit finir le 31.12" + anneeExcep);
                            }
                        } else {
                            if (!getDateFin().equals(cotisationBase.getDateFin())) {
                                _addError(transaction,
                                        "La période de l'exception doit finir le  " + cotisationBase.getDateFin());
                            }
                        }
                    }
                    /*
                     * if (nbMoisExcep > 12) { _addError(transaction,
                     * "La période de l'exception ne correspond pas à celle de base" ); }
                     */

                } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equals(cotisationBase.getPeriodicite())) {

                    // String dateFinTrimestre =
                    // AFUtil.getDateEndOfTrim(getDateDebut());
                    String dateFinTrimestre = AFUtil.getDateEndOfTrim(getDateFin());
                    // String dateDebutTrimestre =
                    // AFUtil.getDateBeginingOfMonth(AFUtil.getDateEndOfPreviousMonth(2,
                    // dateFinTrimestre));
                    String dateDebutTrimestre = AFUtil.getDateBeginingOfTrim(getDateDebut());
                    // System.out.println("EX: "+dateDebutTrimestre+"-"+dateFinTrimestre);
                    if (BSessionUtil.compareDateFirstLowerOrEqual(transaction.getSession(),
                            cotisationBase.getDateDebut(), dateDebutTrimestre)) {

                        if (!getDateDebut().equals(dateDebutTrimestre)) {
                            _addError(transaction, "La période de l'exception doit débuter le " + dateDebutTrimestre);
                        }
                    } else {
                        if (!getDateDebut().equals(cotisationBase.getDateDebut())) {
                            _addError(transaction,
                                    "La période de l'exception doit débuter le " + cotisationBase.getDateDebut());
                        }
                    }

                    if (JadeStringUtil.isBlankOrZero(cotisationBase.getDateFin())) {

                        // Si il n'y a pas de Date de Fin à la cotisation de
                        // base
                        if (!getDateFin().equals(dateFinTrimestre)) {
                            _addError(transaction, "La période de l'exception doit finir le " + dateFinTrimestre);
                        }
                    } else {

                        if (BSessionUtil.compareDateFirstGreaterOrEqual(transaction.getSession(),
                                cotisationBase.getDateFin(), dateFinTrimestre)) {

                            if (!getDateFin().equals(dateFinTrimestre)) {
                                _addError(transaction, "La période de l'exception doit finir le " + dateFinTrimestre);
                            }
                        } else {
                            if (!getDateFin().equals(cotisationBase.getDateFin())) {
                                _addError(transaction,
                                        "La période de l'exception doit finir le  " + cotisationBase.getDateFin());
                            }
                        }
                    }
                    /*
                     * if (nbMoisExcep > 3) { _addError(transaction,
                     * "La période de l'exception ne correspond pas à celle de base" ); }
                     */

                } else if (CodeSystem.PERIODICITE_MENSUELLE.equals(cotisationBase.getPeriodicite())) {

                    // String dateFinMois =
                    // AFUtil.getDateEndOfMonth(getDateDebut());
                    String dateFinMois = AFUtil.getDateEndOfMonth(getDateFin());
                    // String dateDebutMois =
                    // AFUtil.getDateBeginingOfMonth(dateFinMois);
                    String dateDebutMois = AFUtil.getDateBeginingOfMonth(getDateDebut());
                    // System.out.println("EX: "+dateDebutMois+"-"+dateFinMois);

                    if (BSessionUtil.compareDateFirstLowerOrEqual(transaction.getSession(),
                            cotisationBase.getDateDebut(), dateDebutMois)) {

                        if (!getDateDebut().equals(dateDebutMois)) {
                            _addError(transaction, "La période de l'exception doit débuter le " + dateDebutMois);
                        }
                    } else {
                        if (!getDateDebut().equals(cotisationBase.getDateDebut())) {
                            _addError(transaction,
                                    "La période de l'exception doit débuter le " + cotisationBase.getDateDebut());
                        }
                    }

                    if (JadeStringUtil.isBlankOrZero(cotisationBase.getDateFin())) {

                        // Si il n'y a pas de Date de Fin à la cotisation de
                        // base
                        if (!getDateFin().equals(dateFinMois)) {
                            _addError(transaction, "La période de l'exception doit finir le " + dateFinMois);
                        }
                    } else {

                        if (BSessionUtil.compareDateFirstGreaterOrEqual(transaction.getSession(),
                                cotisationBase.getDateFin(), dateFinMois)) {

                            if (!getDateFin().equals(dateFinMois)) {
                                _addError(transaction, "La période de l'exception doit finir le " + dateFinMois);
                            }
                        } else {
                            if (!getDateFin().equals(cotisationBase.getDateFin())) {
                                _addError(transaction,
                                        "La période de l'exception doit finir le  " + cotisationBase.getDateFin());
                            }
                        }
                    }
                    /*
                     * if (nbMoisExcep > 1) { _addError(transaction,
                     * "La période de l'exception ne correspond pas à celle de base" ); }
                     */
                }
            }
        } else if (!foundBase && !foundException) {
            _addError(transaction, "L'exception ne correspond a aucunes cotisations");
        }
    }

    /**
     * Controle si un type d'assurance est autorisé.<br>
     * - AF GE IND autorisée seulement si le canton de l'adresse de domicile est GE</br> - AMAT IND autorisée si le
     * canton de l'adresse d'exploitation de l'affillié est GE
     * 
     * @param transaction la transaction a utiliser
     * @return true - Si le conditions sont passées avec succès
     * @throws Exception
     */
    private void _validationTypeAssurance(BTransaction transaction) throws Exception {
        if (getAssurance() != null) {
            // si cotisations personnelle...
            if (CodeSystem.GENRE_ASS_PERSONNEL.equals(getAssurance().getAssuranceGenre())) {
                // assurance AF personnelle
                if (CodeSystem.TYPE_ASS_COTISATION_AF.equals(getAssurance().getTypeAssurance())
                        && BSessionUtil.compareDateFirstLower(getSession(), getDateDebut(), "01.01.2013")) {
                    // Email de ALD du 07.02.2013: Ne tester que pour VD et GE
                    if (getAssurance().getAssuranceCanton().equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_GENEVE)
                            || getAssurance().getAssuranceCanton()
                                    .equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_VAUD)) {
                        // tester avec le canton de l'adresse de domicile
                        if (!transaction.hasErrors() && (getAffiliation() != null)
                                && (getAffiliation().getTiers() != null) && !transaction.hasErrors()) {
                            TIAdresseDataSource source = getAffiliation().getTiers().getAdresseAsDataSource(
                                    IConstantes.CS_AVOIR_ADRESSE_DOMICILE, IConstantes.CS_APPLICATION_DEFAUT,
                                    getAffiliation().getAffilieNumero(), getDateDebut(), true, null);
                            if ((source != null) && (source.canton_id != null)
                                    && !source.canton_id.equals(getAssurance().getAssuranceCanton())) {
                                if (!source.canton_id.equals(IConstantes.CS_LOCALITE_ETRANGER)) {

                                    _addError(
                                            transaction,
                                            getAssurance().getAssuranceLibelle() + " "
                                                    + getSession().getLabel("COTI_AF_IND_CANTON") + " "
                                                    + CodeSystem.getLibelle(getSession(), source.canton_id));
                                }
                            }
                        }
                    }
                }
                // assurance AMAT personnelle
                if (CodeSystem.TYPE_ASS_MATERNITE.equals(getAssurance().getTypeAssurance())) {
                    // tester avec le conton de l'adresse de domicile
                    if (!getAffiliation().getTypeAssocie().equalsIgnoreCase(CodeSystem.TYPE_ASSOCIATION_ASSOCIE)) {
                        if (!transaction.hasErrors() && (getAffiliation() != null)
                                && (getAffiliation().getTiers() != null) && !transaction.hasErrors()) {
                            TIAdresseDataSource source = getAffiliation().getTiers().getAdresseAsDataSource(
                                    AFCotisation.TYPE_EXPLOITATION, IConstantes.CS_APPLICATION_DEFAUT,
                                    getAffiliation().getAffilieNumero(), getDateDebut(), true, null);
                            if ((source.canton_id != null)
                                    && !source.canton_id.equals(getAssurance().getAssuranceCanton())) {
                                _addError(
                                        transaction,
                                        getAssurance().getAssuranceLibelle() + " "
                                                + getSession().getLabel("COTI_AMAT_IND_CANTON") + " "
                                                + CodeSystem.getLibelle(getSession(), source.canton_id));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MEICOT", this._dbWriteNumeric(statement.getTransaction(), getCotisationId(), ""));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MEICOT",
                this._dbWriteNumeric(statement.getTransaction(), getCotisationId(), "CotisationId"));
        statement.writeField("MBIASS",
                this._dbWriteNumeric(statement.getTransaction(), getAssuranceId(), "AssuranceId"));
        statement.writeField("MUIPLA",
                this._dbWriteNumeric(statement.getTransaction(), getPlanAffiliationId(), "planAffiliationId"));
        statement.writeField("MSIPLC",
                this._dbWriteNumeric(statement.getTransaction(), getPlanCaisseId(), "planCaisseId"));
        statement.writeField("MRIADH", this._dbWriteNumeric(statement.getTransaction(), getAdhesionId(), "adhesionId"));
        statement.writeField("MCITAU",
                this._dbWriteNumeric(statement.getTransaction(), getTauxAssuranceId(), "tauxAssuranceId"));
        statement.writeField("METCAT",
                this._dbWriteNumeric(statement.getTransaction(), getCategorieTauxId(), "categorieTauxId"));
        statement.writeField("MEDDEB", this._dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "DateDebut"));
        statement.writeField("MEDFIN", this._dbWriteDateAMJ(statement.getTransaction(), getDateFin(), "DateFin"));
        statement.writeField("METMOT", this._dbWriteNumeric(statement.getTransaction(), getMotifFin(), "MotifFin"));
        statement.writeField("METPER",
                this._dbWriteNumeric(statement.getTransaction(), getPeriodicite(), "Periodicite"));
        statement.writeField("METPEE",
                this._dbWriteBoolean(statement.getTransaction(), getExceptionPeriodicite(), "ExceptionPeriodicite"));
        statement.writeField("MEBMER", this._dbWriteBoolean(statement.getTransaction(), getMaisonMere(), "MaisonMere"));
        statement.writeField("MEMMAP",
                this._dbWriteNumeric(statement.getTransaction(), getMasseAnnuelle(), "masseAnnuelle"));
        statement.writeField("MEMTRI",
                this._dbWriteNumeric(statement.getTransaction(), getMontantTrimestriel(), "MontantTrimestriel"));
        statement.writeField("MEMSEM",
                this._dbWriteNumeric(statement.getTransaction(), getMontantSemestriel(), "MontantSemestriel"));
        statement.writeField("MEMANN",
                this._dbWriteNumeric(statement.getTransaction(), getMontantAnnuel(), "MontantAnnuel"));
        statement.writeField("MEMMEN",
                this._dbWriteNumeric(statement.getTransaction(), getMontantMensuel(), "MontantMensuel"));
        statement.writeField("MENANN",
                this._dbWriteNumeric(statement.getTransaction(), getAnneeDecision(), "AnneeDecision"));
        statement.writeField("METMOA",
                this._dbWriteNumeric(statement.getTransaction(), getTraitementMoisAnnee(), "TraitementMoisAnnee"));
        statement.writeField("INACTIVE", this._dbWriteBoolean(statement.getTransaction(), getInactive(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "inactive"));
    }

    /*
     * Inforom D0017 - Annualiser masse pour décompte 13 et 14
     */
    protected String annualisationMassePourDecompte13et14(String date, String masse, boolean wantAnnualisationMasse,
            String typeDeclaration) throws JAException, Exception {
        if (wantAnnualisationMasse
                && !JadeStringUtil.isBlankOrZero(masse)
                && (DSDeclarationViewBean.CS_PRINCIPALE.equals(typeDeclaration) || DSDeclarationViewBean.CS_BOUCLEMENT_ACOMPTE
                        .equals(typeDeclaration))) {
            String dateDebutPourAnnualisation = "";
            String dateFinPourAnnualisation = "";
            int anneeDeclaration = JACalendar.getYear(date);
            // Test si l'affiliation commence en cours de l'année de la déclaration
            if ((anneeDeclaration == JACalendar.getYear(getAffiliation().getDateDebut()))
                    && (JACalendar.getMonth(getAffiliation().getDateDebut()) != 1)) {
                dateDebutPourAnnualisation = getAffiliation().getDateDebut();
            }
            // Test si l'affiliation est radiée en cours de l'année de la déclaration
            if ((anneeDeclaration == JACalendar.getYear(getAffiliation().getDateFin()))
                    && (JACalendar.getMonth(getAffiliation().getDateFin()) != 12)) {
                dateFinPourAnnualisation = getAffiliation().getDateFin();
            }
            if (!JadeStringUtil.isBlankOrZero(dateDebutPourAnnualisation)
                    || !JadeStringUtil.isBlankOrZero(dateFinPourAnnualisation)) {
                // Cas ou seule la date de fin de l'affiliation commence en cours de l'année de la déclaration
                if (JadeStringUtil.isBlankOrZero(dateDebutPourAnnualisation)) {
                    dateDebutPourAnnualisation = "01.01." + anneeDeclaration;
                }
                // Cas ou seule la date de fin de l'affiliation commence en cours de l'année de la déclaration
                if (JadeStringUtil.isBlankOrZero(dateFinPourAnnualisation)) {
                    dateFinPourAnnualisation = "31.12." + anneeDeclaration;
                }
                // Test si la période de l'affiliation est incomplète pour l'année
                BigDecimal nbrMois = JADate.getMonth(dateFinPourAnnualisation)
                        .subtract(JADate.getMonth(dateDebutPourAnnualisation)).add(new BigDecimal("1"));
                BigDecimal masseAnnuel = new BigDecimal(JANumberFormatter.deQuote(masse));
                masseAnnuel = masseAnnuel.multiply(new BigDecimal("12"));
                masseAnnuel = masseAnnuel.divide(nbrMois, BigDecimal.ROUND_UP);
                masse = masseAnnuel.toString();
            }
        }
        return masse;
    }

    private void checkAdhesion(BTransaction transaction) throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(getPlanCaisseId()) && JadeStringUtil.isIntegerEmpty(getAdhesionId())) {
            // test si une adhésion existe pour le plan sélectionné
            AFAdhesionManager adhMgr = new AFAdhesionManager();
            adhMgr.setSession(getSession());
            adhMgr.setForAffiliationId(getAffiliation().getAffiliationId());
            adhMgr.setForPlanCaisseId(getPlanCaisseId());
            adhMgr.setForDateValeur(getDateDebut());
            adhMgr.find(transaction);
            if (adhMgr.size() == 0) {
                // créer une nouvelle adhésion
                AFAdhesion adh = new AFAdhesion();
                adh.setSession(getSession());
                adh.setAffiliationId(getAffiliation().getAffiliationId());
                adh.setDateDebut(getDateDebut());
                adh.setDateFin(getDateFin());
                adh.setPlanCaisseId(getPlanCaisseId());
                adh.setTypeAdhesion(CodeSystem.TYPE_ADHESION_CAISSE);
                adh.setAddOnlyAdhesion(true);
                adh.add(transaction);
                // maj cotissations
                setAdhesionId(adh.getAdhesionId());
            } else {
                setAdhesionId(((AFAdhesion) adhMgr.getFirstEntity()).getAdhesionId());
            }
        }
    }

    private boolean checkDS(String idDeclaration) {
        DSLigneDeclarationListViewBean decList = new DSLigneDeclarationListViewBean();
        decList.setSession(getSession());
        decList.setForAssuranceId(getAssuranceId());
        decList.setForIdDeclaration(idDeclaration);
        // num Aff
        try {
            decList.find();
        } catch (Exception e) {
            getSession().addWarning("");

            return false;
        }
        return decList.size() > 0;
    }

    private void checkExceptionPeriodicite(BTransaction transaction) throws Exception {

        AFAffiliation theAffiliation = getAffiliation();

        if ((theAffiliation == null) || theAffiliation.isNew()) {
            if (transaction.hasErrors() == false) {
                _addError(transaction, "unable to checkExceptionPeriodicite because no affiliation was founded");
            }
            return;
        }

        String periodiciteAff = theAffiliation.getPeriodicite();
        String periodiciteCoti = getPeriodicite();

        setExceptionPeriodicite(new Boolean(false));

        if (!JadeStringUtil.isEmpty(periodiciteAff) && !JadeStringUtil.isEmpty(periodiciteCoti)
                && !periodiciteAff.equalsIgnoreCase(periodiciteCoti)) {
            setExceptionPeriodicite(new Boolean(true));
        }
    }

    private boolean controleDateCotisationSelonDateAdhesion(BStatement statement, String theAssLib, AFAdhesion adhesion)
            throws Exception {
        boolean validationOK = true;
        // la date de début de la coti ne doit pas être plus petite que celle de la couverture
        if (BSessionUtil.compareDateFirstLower(getSession(), getDateDebut(), adhesion.getDateDebut())) {
            _addError(
                    statement.getTransaction(),
                    FWMessageFormat.format(getSession().getLabel("1165"), theAssLib, getDateDebut(),
                            adhesion.getDateDebut()));
            validationOK = false;
        }
        // la date de fin de la coti ne doit pas être plus grande que celle de la couverture
        if (!JadeStringUtil.isBlankOrZero(adhesion.getDateFin())) {
            if (JadeStringUtil.isBlankOrZero(getDateFin())
                    || BSessionUtil.compareDateFirstGreater(getSession(), getDateFin(), adhesion.getDateFin())) {
                _addError(statement.getTransaction(),
                        FWMessageFormat.format(getSession().getLabel("1175"), getDateFin(), adhesion.getDateFin()));
                validationOK = false;
            }
        }
        return validationOK;
    }

    private boolean controleDateCotisationSelonDateAffiliation(BStatement statement) throws Exception {
        boolean validationOK = true;
        // la date de début de la coti ne doit pas être plus petite que celle de l'affiliation
        if (BSessionUtil.compareDateFirstLower(getSession(), getDateDebut(), getAffiliation().getDateDebut())) {
            _addError(statement.getTransaction(), FWMessageFormat.format(getSession().getLabel("1220"), getDateDebut(),
                    getAffiliation().getDateDebut()));
            validationOK = false;
        }
        // la date de fin de la coti ne doit pas être plus grande que celle de l'affiliation
        if (!JadeStringUtil.isBlankOrZero(getAffiliation().getDateFin())) {
            if (JadeStringUtil.isBlankOrZero(getDateFin())
                    || BSessionUtil.compareDateFirstGreater(getSession(), getDateFin(), getAffiliation().getDateFin())) {
                _addError(statement.getTransaction(), FWMessageFormat.format(getSession().getLabel("1230"),
                        getDateFin(), getAffiliation().getDateFin()));
                validationOK = false;
            }
        }
        return validationOK;
    }

    private boolean controleDateCotisationSelonDateCouverture(BStatement statement, String theAssLib,
            AFCouverture couverture) throws Exception {
        boolean validationOK = true;
        // la date de début de la coti ne doit pas être plus petite que celle de la couverture
        if (BSessionUtil.compareDateFirstLower(getSession(), getDateDebut(), couverture.getDateDebut())) {
            _addError(
                    statement.getTransaction(),
                    FWMessageFormat.format(getSession().getLabel("1160"), theAssLib, getDateDebut(),
                            couverture.getDateDebut())
                            + " " + getAssurance().getAssuranceLibelle());
            validationOK = false;
        }
        // la date de fin de la coti ne doit pas être plus grande que celle de la couverture
        if (!JadeStringUtil.isBlankOrZero(couverture.getDateFin())) {
            if (JadeStringUtil.isBlankOrZero(getDateFin())
                    || BSessionUtil.compareDateFirstGreater(getSession(), getDateFin(), couverture.getDateFin())) {
                _addError(statement.getTransaction(),
                        FWMessageFormat.format(getSession().getLabel("1170"), getDateFin(), couverture.getDateFin())
                                + " " + getAssurance().getAssuranceLibelle());
                validationOK = false;
            }
        }
        return validationOK;
    }

    public boolean estCotisationSansMasse() {

        boolean isCotisationSansMasse = false;

        try {
            return APIRubrique.COTISATION_SANS_MASSE.equalsIgnoreCase(getAssurance().getRubriqueComptable()
                    .getNatureRubrique());
        } catch (Exception e) {
            isCotisationSansMasse = false;
        }

        return isCotisationSansMasse;
    }

    private void fillCategorieCotisationInfoRom354Lot2(BTransaction transaction) throws Exception {

        AFAffiliation theAffiliation = getAffiliation();

        if ((theAffiliation == null) || theAffiliation.isNew()) {
            if (transaction.hasErrors() == false) {
                _addError(transaction, getSession().getLabel("1100") + " -  id Cotisation: " + getCotisationId());
            }
            return;
        }

        AFAssurance theAssurance = getAssurance();

        if ((theAssurance == null) || theAssurance.isNew()) {
            _addError(transaction, "fillCategorieCotisationInfoRom354Lot2 no assurance for cotisation id "
                    + getCotisationId());
        }

        if (CodeSystem.GENRE_ASS_PERSONNEL.equalsIgnoreCase(theAssurance.getAssuranceGenre())
                && CodeSystem.TYPE_ASS_FRAIS_ADMIN.equalsIgnoreCase(theAssurance.getTypeAssurance())) {

            if (CodeSystem.TYPE_AFFILI_NON_ACTIF.equalsIgnoreCase(theAffiliation.getTypeAffiliation())) {
                setCategorieTauxId(CodeSystem.CATEGORIE_TAUX_NON_ACTIF);
            } else if (CodeSystem.TYPE_AFFILI_INDEP.equalsIgnoreCase(theAffiliation.getTypeAffiliation())
                    || CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equalsIgnoreCase(theAffiliation.getTypeAffiliation())
                    || CodeSystem.TYPE_AFFILI_TSE.equalsIgnoreCase(theAffiliation.getTypeAffiliation())
                    || CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE.equalsIgnoreCase(theAffiliation.getTypeAffiliation())) {
                setCategorieTauxId(CodeSystem.CATEGORIE_TAUX_IND_TSE);
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
                Method m = null;
                if ("changeManagerSize".equals(methodName)) {
                    manager.changeManagerSize(Integer.parseInt(value));

                } else {
                    m = manager.getClass().getMethod(methodName, new Class[] { String.class });
                    if (m != null) {
                        m.invoke(manager, new Object[] { value });
                    }
                }
            }
        }

        manager.find(BManager.SIZE_NOLIMIT);
        return manager;
    }

    /**
     * Retourne le taux en question en fonction de la date et de la masse donnée Remarques: - Lors de la recherche le
     * sexe n'est pas pris en compte - Le type "canton" n'est pas supporté
     * 
     * @param date La pour laquelle les taux doivent être valides
     * @param masse la masse à utiliser pour les taux variable null ou "0" sinon
     * @param checkTauxParPalier true si la masse ne doit pas être prise en compte si le calcul du taux s'effectue par
     *            palier
     * @return Une liste de AFTauxAssurance
     * @throws Exception
     */
    // public List getTauxList(String date) throws Exception {
    public AFTauxAssurance findTaux(String date, String masse, boolean checkTauxParPalier) throws Exception {
        if (checkTauxParPalier
                && "true".equals(getSession().getApplication().getProperty(AFApplication.PROPERTY_IS_TAUX_PAR_PALIER))) {
            return findTauxWithRecalcul(date, null, true, false, "");
        } else {
            return findTauxWithRecalcul(date, masse, true, false, "");
        }
    }

    /**
     * Retourne le taux en question en fonction de la date, de la masse et du type de déclaration donnés
     * 
     * @param date La pour laquelle les taux doivent être valides
     * @param masse la masse à utiliser pour les taux variable null ou "0" sinon
     * @param typeDeclaration le type de la déclaration
     * @return Une liste de AFTauxAssurance
     * @throws Exception
     */
    public AFTauxAssurance findTaux(String date, String masse, String typeDeclaration, boolean wantRecalcul,
            boolean wantAnnualisationMasse) throws Exception {
        if (AFCotisation.TYPE_CONTROLE_EMPLOYEUR.equals(typeDeclaration)) {
            // on recherche un taux de type ctrl employeur
            AFTauxAssuranceManager manager = new AFTauxAssuranceManager();
            manager.setSession(getSession());
            manager.setForGenreValeur(CodeSystem.GEN_VALEUR_ASS_TAUX);
            manager.setForTypeId(CodeSystem.TYPE_TAUX_CTRL_EMP);
            manager.setForIdAssurance(getAssuranceId());
            manager.setForDate(date);
            manager.setOrderByRangDebutDesc();
            manager.find();
            if (manager.size() > 0) {
                // return manager.getContainer();
                return (AFTauxAssurance) manager.getEntity(0);
            }
        }
        return findTauxWithRecalcul(date, masse, wantRecalcul, wantAnnualisationMasse, typeDeclaration);
    }

    public AFTauxAssurance findTauxFraisAdministrationPersonnel(BSession theSession, String theDate, String theMasse)
            throws Exception {

        // validation des entrées de la méthode
        StringBuffer wrongInputBuffer = new StringBuffer();

        if (theSession == null) {
            wrongInputBuffer.append("theSession is null / ");
        }

        if (!new JACalendarGregorian().isValid(theDate)) {
            wrongInputBuffer.append("theDate (" + theDate + ") is not a date / ");
        }

        if (!JadeNumericUtil.isNumeric(JANumberFormatter.deQuote(theMasse))) {
            // wrongInputBuffer.append("theMasse (" + theMasse + ") is not numeric / ");
            theMasse = "0";
        }

        if (JadeStringUtil.isBlankOrZero(getAssuranceId())) {
            wrongInputBuffer.append("this.getAssuranceId() is blank or zero / ");
        }

        if (JadeStringUtil.isBlankOrZero(getCategorieTauxId())) {
            wrongInputBuffer.append("this.getCategorieTauxId() is blank or zero / ");
        }

        AFAffiliation theAffiliation = getAffiliation();

        if ((theAffiliation == null) || theAffiliation.isNew()) {
            wrongInputBuffer.append("theAffiliation is null or new / ");
        }

        if (wrongInputBuffer.length() >= 1) {
            throw new Exception("unable to find taux for frais administration personnelle due to wrong arguments : "
                    + wrongInputBuffer.toString());
        }

        AFTauxAssurance theTauxFixeForCategorie = null;
        AFTauxAssurance theTauxVariableForCategorieIndTse = null;

        AFTauxAssuranceManager tauxFixeAssuranceManager = null;

        tauxFixeAssuranceManager = new AFTauxAssuranceManager();
        tauxFixeAssuranceManager.setSession(theSession);
        tauxFixeAssuranceManager.setForGenreValeur(CodeSystem.GEN_VALEUR_ASS_TAUX);
        tauxFixeAssuranceManager.setForTypeId(CodeSystem.TYPE_TAUX_GROUPE);
        tauxFixeAssuranceManager.setForIdAssurance(getAssuranceId());
        tauxFixeAssuranceManager.setForCategorieId(getCategorieTauxId());
        tauxFixeAssuranceManager.setForDate(theDate);
        tauxFixeAssuranceManager.setOrderByRangDebutDesc();
        tauxFixeAssuranceManager.find(BManager.SIZE_NOLIMIT);

        if (tauxFixeAssuranceManager.size() > 0) {
            theTauxFixeForCategorie = (AFTauxAssurance) tauxFixeAssuranceManager.getEntity(0);
        }

        boolean isTauxFixeForCategorieNonActif = CodeSystem.TYPE_AFFILI_NON_ACTIF.equalsIgnoreCase(theAffiliation
                .getTypeAffiliation())
                && (theTauxFixeForCategorie != null)
                && !theTauxFixeForCategorie.isNew()
                && CodeSystem.CATEGORIE_TAUX_NON_ACTIF.equalsIgnoreCase(theTauxFixeForCategorie.getCategorieId());

        boolean isTauxFixeForCategorieIndTse = (CodeSystem.TYPE_AFFILI_INDEP.equalsIgnoreCase(theAffiliation
                .getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equalsIgnoreCase(theAffiliation.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_TSE.equalsIgnoreCase(theAffiliation.getTypeAffiliation()) || CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE
                    .equalsIgnoreCase(theAffiliation.getTypeAffiliation()))
                && (theTauxFixeForCategorie != null)
                && !theTauxFixeForCategorie.isNew()
                && CodeSystem.CATEGORIE_TAUX_IND_TSE.equalsIgnoreCase(theTauxFixeForCategorie.getCategorieId());

        boolean isTauxVariableForCategorieIndTse = false;
        if (!CodeSystem.TYPE_AFFILI_NON_ACTIF.equalsIgnoreCase(theAffiliation.getTypeAffiliation())) {

            AFTauxVariableUtil tauxVarUtil = AFTauxVariableUtil.getInstance(getAssuranceId());
            TauxCalcul calcul = tauxVarUtil.getCalcul(theSession, JANumberFormatter.deQuote(theMasse), theDate);

            if (calcul != null) {
                theTauxVariableForCategorieIndTse = calcul.getTauxRang();

                isTauxVariableForCategorieIndTse = (theTauxVariableForCategorieIndTse != null)
                        && !theTauxVariableForCategorieIndTse.isNew()
                        && CodeSystem.CATEGORIE_TAUX_IND_TSE.equalsIgnoreCase(theTauxVariableForCategorieIndTse
                                .getCategorieId());
            }

        }

        if ((isTauxFixeForCategorieNonActif || isTauxFixeForCategorieIndTse) && !isTauxVariableForCategorieIndTse) {
            return theTauxFixeForCategorie;
        } else if (isTauxVariableForCategorieIndTse && !isTauxFixeForCategorieIndTse) {
            return theTauxVariableForCategorieIndTse;
        } else if (isTauxFixeForCategorieIndTse && isTauxVariableForCategorieIndTse) {
            AFTauxAssurance theTauxWhichBeginFirst = theTauxFixeForCategorie;
            AFTauxAssurance theTauxWhichBeginSecond = theTauxVariableForCategorieIndTse;
            if (BSessionUtil.compareDateFirstGreater(theSession, theTauxFixeForCategorie.getDateDebut(),
                    theTauxVariableForCategorieIndTse.getDateDebut())) {
                theTauxWhichBeginFirst = theTauxVariableForCategorieIndTse;
                theTauxWhichBeginSecond = theTauxFixeForCategorie;
            }

            if (BSessionUtil.compareDateFirstGreaterOrEqual(theSession, theDate, theTauxWhichBeginFirst.getDateDebut())
                    && BSessionUtil.compareDateFirstLower(theSession, theDate, theTauxWhichBeginSecond.getDateDebut())) {
                return theTauxWhichBeginFirst;
            } else if (BSessionUtil.compareDateFirstGreaterOrEqual(theSession, theDate,
                    theTauxWhichBeginSecond.getDateDebut())) {
                return theTauxWhichBeginSecond;
            }
        }

        // aucun taux valide trouvé
        return null;

    }

    /**
     * Retourne le taux en question en fonction de la date et de la masse donnée Remarques: - Lors de la recherche le
     * sexe n'est pas pris en compte - Le type "canton" n'est pas supporté
     * 
     * @param date La pour laquelle les taux doivent être valides
     * @param masse la masse à utiliser pour les taux variable null ou "0" sinon
     * @param ctrlEmployeur à true lors d'un contrôle d'employeur
     * @return Une liste de AFTauxAssurance
     * @throws Exception
     */
    // public List getTauxList(String date) throws Exception {
    public AFTauxAssurance findTauxWithRecalcul(String date, String masse, boolean wantRecalcul,
            boolean wantAnnualisationMasse, String typeDeclaration) throws Exception {

        if (getAssurance() != null) {

            // Session et transaction
            BSession s = getSession();
            BTransaction t = getSession().getCurrentThreadTransaction();

            // Manager pour la recherche
            AFTauxAssuranceManager manager = null;

            // Critères de recherche
            String tauxAssuranceIdCriteria = getTauxAssuranceId();

            // Critères de recherche
            String categorieTauxIdCriteria = getCategorieTauxId();
            String assuranceIdCriteria = getAssuranceId();
            String dateCriteria = date;

            boolean isCotiFADPers = CodeSystem.GENRE_ASS_PERSONNEL.equalsIgnoreCase(getAssurance().getAssuranceGenre())
                    && CodeSystem.TYPE_ASS_FRAIS_ADMIN.equalsIgnoreCase(getAssurance().getTypeAssurance());

            // Si la cotisation contient un taux forcé, on prend celui-ci
            if (!JadeStringUtil.isBlankOrZero(tauxAssuranceIdCriteria)) {
                AFTauxAssurance taux = new AFTauxAssurance();
                taux.setSession(s);
                taux.setTauxAssuranceId(tauxAssuranceIdCriteria);
                taux.retrieve(t);
                if (!taux.isNew()) {
                    /*
                     * JAVector vector = new JAVector(1); vector.insert(taux); return vector;
                     */
                    return taux;
                }
            }

            // Si la cotisation contient une catégorie,
            // on utilise cette valeur pour récupérer le bon taux
            if (!isCotiFADPers && !JadeStringUtil.isBlankOrZero(categorieTauxIdCriteria)) {
                manager = new AFTauxAssuranceManager();
                manager.setSession(s);
                manager.setForGenreValeur(CodeSystem.GEN_VALEUR_ASS_TAUX);
                manager.setForTypeId(CodeSystem.TYPE_TAUX_GROUPE);
                manager.setForIdAssurance(assuranceIdCriteria);
                manager.setForCategorieId(categorieTauxIdCriteria);
                manager.setForDate(dateCriteria);
                manager.setOrderByRangDebutDesc();
                manager.find(t);
                if (manager.size() > 0) {
                    // return manager.getContainer();
                    return (AFTauxAssurance) manager.getEntity(0);
                }
            }

            // Critères de recherche
            boolean tauxParCaisse = getAssurance().isTauxParCaisse().booleanValue();
            // Si l'assurance gère un taux par caisse,
            // on utilise la caisse pour récupérer le taux
            if (tauxParCaisse) {
                AFPlanCaisse caisse = getCaisse();
                if (caisse != null) {
                    TIAdministrationViewBean tiers = caisse.getAdministration();
                    if (tiers != null) {
                        manager = new AFTauxAssuranceManager();
                        manager.setSession(s);
                        manager.setForGenreValeur(CodeSystem.GEN_VALEUR_ASS_TAUX);
                        manager.setForTypeId(CodeSystem.TYPE_TAUX_CAISSE);
                        manager.setForIdAssurance(assuranceIdCriteria);
                        manager.setForCategorieId(tiers.getIdTiersAdministration());
                        manager.setForDate(dateCriteria);
                        manager.setOrderByRangDebutDesc();
                        manager.find(t);
                        if (manager.size() > 0) {
                            // return manager.getContainer();
                            return (AFTauxAssurance) manager.getEntity(0);
                        }
                    }
                }
            }

            // InfoRom 354 - Récupération du taux pour les frais d'administration personnel
            if (isCotiFADPers
                    && (CodeSystem.CATEGORIE_TAUX_IND_TSE.equalsIgnoreCase(categorieTauxIdCriteria) || CodeSystem.CATEGORIE_TAUX_NON_ACTIF
                            .equalsIgnoreCase(categorieTauxIdCriteria))) {

                return findTauxFraisAdministrationPersonnel(s, date, masse);
            }

            // Si aucun taux n'a été trouvé jusqu'ici,
            // on recherche le taux par défaut
            manager = new AFTauxAssuranceManager();
            manager.setSession(s);
            manager.setForGenreValeur(CodeSystem.GEN_VALEUR_ASS_TAUX);
            manager.setForTypeId(CodeSystem.TYPE_TAUX_DEFAUT);
            manager.setForIdAssurance(assuranceIdCriteria);
            manager.setForDate(dateCriteria);
            manager.setOrderByRangDebutDesc();
            manager.find(t, BManager.SIZE_USEDEFAULT);
            if (manager.size() > 0) {
                // return manager.getContainer();
                return (AFTauxAssurance) manager.getEntity(0);
            }

            // Si aucun taux n'a été trouvé jusqu'ici,
            // on recherche un montant
            manager = new AFTauxAssuranceManager();
            manager.setSession(s);
            manager.setForGenreValeur(CodeSystem.GEN_VALEUR_ASS_MONTANT);
            manager.setForIdAssurance(assuranceIdCriteria);
            manager.setForDate(dateCriteria);
            manager.setOrderByRangDebutDesc();
            manager.find(t, BManager.SIZE_USEDEFAULT);
            if (manager.size() > 0) {
                // return manager.getContainer();
                return (AFTauxAssurance) manager.getEntity(0);
            }

            // Si aucun taux n'a été trouvé jusqu'ici,
            // on recherche un taux variable
            AFTauxVariableUtil tauxVarUtil = AFTauxVariableUtil.getInstance(getAssuranceId());
            AFTauxAssurance taux = null;
            // Pour les cotisations personnelles, il n'y a pas de taux par
            // pallier possible
            // Par contre, le taux variable est possible

            // InforomD0017 - Annulisation de la masse pour la recherche du taux pour FAD
            // Anualiser si la période est incomplète et si la caisse le désire - Seulement pour les 13 et 14
            if (CodeSystem.TYPE_ASS_FRAIS_ADMIN.equalsIgnoreCase(getAssurance().getTypeAssurance())) {
                masse = annualisationMassePourDecompte13et14(date, masse, wantAnnualisationMasse, typeDeclaration);
            }

            if (getAssurance().getAssuranceGenre().equals(CodeSystem.GENRE_ASS_PERSONNEL)) {
                TauxCalcul calcul = tauxVarUtil.getCalcul(getSession(), JANumberFormatter.deQuote(masse), dateCriteria);
                if (calcul != null) {
                    taux = calcul.getTauxRang();
                }
            } else {
                if ((masse == null)
                        || (!wantRecalcul && "true".equals(getSession().getApplication().getProperty(
                                AFApplication.PROPERTY_IS_TAUX_PAR_PALIER)))) {
                    // pas de masse, recherche du taux moyen
                    taux = tauxVarUtil.getTauxMoyen(getSession(), getAffiliation().getAffiliationId(), dateCriteria);
                } else {
                    taux = tauxVarUtil.getTaux(getSession(), masse, dateCriteria);
                    if (taux != null) {
                        // si taux variable défini en forcé, le prendre en
                        // compte ici (par défaut) comme taux normal
                        taux.setTypeId(CodeSystem.TYPE_TAUX_DEFAUT);
                    }
                }
            }
            if (taux != null) {
                return taux;
            }

            /*
             * manager = new AFTauxAssuranceManager(); manager.setSession(s); manager
             * .setForGenreValeur(CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE);
             * manager.setForIdAssurance(assuranceIdCriteria); manager.setForDate(dateCriteria);
             * manager.setOrderByRangDebutDesc(); manager.find(t); if (manager.size() > 0) { return
             * manager.getContainer(); }
             */

        }

        // Aucun taux n'a été trouvé
        // return new JAVector(0);
        return null;

    }

    /**
     * Rechercher l'adhesion pour la Cotisation en fonction de son ID.
     * 
     * @return l'adhesion
     */
    public AFAdhesion getAdhesion() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getAdhesionId())) {
            return null;
        }

        if ((_adhesion == null) || _adhesion.isNew()) {

            _adhesion = new AFAdhesion();
            _adhesion.setSession(getSession());
            _adhesion.setAdhesionId(getAdhesionId());
            try {
                _adhesion.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _adhesion = null;
            }
        }
        return _adhesion;
    }

    public java.lang.String getAdhesionId() {
        return adhesionId;
    }

    /**
     * Rechercher l'affiliation pour la Cotisation en fonction du Plan d'affiliation.
     * 
     * @return l'affiliation
     */
    public AFAffiliation getAffiliation() {

        // Si pas d'identifiant => pas d'objet
        String localAffiliationId = "";

        if ((_planAffiliation == null) || _planAffiliation.isNew()) {
            getPlanAffiliation();
            if ((_planAffiliation == null) || _planAffiliation.isNew()) {
                getAdhesion();
                if ((_adhesion == null) || _adhesion.isNew()) {
                    if (JadeStringUtil.isEmpty(getAffiliationId())) {
                        return null;
                    } else {
                        localAffiliationId = getAffiliationId();
                    }
                } else {
                    localAffiliationId = _adhesion.getAffiliationId();
                }
            } else {
                localAffiliationId = _planAffiliation.getAffiliationId();
            }
        } else {
            localAffiliationId = _planAffiliation.getAffiliationId();
        }

        if ((_affiliation == null) || _affiliation.isNew()) {

            _affiliation = new AFAffiliation();
            _affiliation.setSession(getSession());
            _affiliation.setAffiliationId(localAffiliationId);
            try {
                _affiliation.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _affiliation = null;
            }
        }
        return _affiliation;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    // Other
    public java.lang.String getAffiliationId() {
        return affiliationId;
    }

    /**
     * Retourne une liste d'Assurances pour une Affiliation données.
     * 
     * @param httpSession
     * @param IdAffiliation
     * @return
     */
    public String getAffilieAF(String IdAffiliation, String dateDebut, String dateFin) {

        Vector<String[]> vList = new Vector<String[]>();

        // Ajoute un blanc
        String[] element = new String[2];
        // element[0]="";
        // element[1]="";
        // vList.add(element);
        String temp = "false";
        try {

            AFPlanAffiliationManager planAffMananger = new AFPlanAffiliationManager();
            planAffMananger.setISession(getSession());
            planAffMananger.setForAffiliationId(IdAffiliation);
            planAffMananger.find();

            if (((getAffiliation().getTypeAffiliation().equalsIgnoreCase("804001"))
                    || (getAffiliation().getTypeAffiliation().equalsIgnoreCase("804002")) || (getAffiliation()
                    .getTypeAffiliation().equalsIgnoreCase("804005") || (getAffiliation().getTypeAffiliation()
                    .equalsIgnoreCase("19150036"))))) {
                temp = "true";
            } else {

                for (int i = 0; i < planAffMananger.size(); i++) {
                    AFPlanAffiliation planAffiliation = (AFPlanAffiliation) planAffMananger.getEntity(i);
                    if (!planAffiliation.isInactif().booleanValue()) {
                        AFCotisationManager cotiManager = new AFCotisationManager();
                        cotiManager.setISession(getSession());
                        cotiManager.setForPlanAffiliationId(planAffiliation.getPlanAffiliationId());
                        cotiManager.find();

                        for (int j = 0; j < cotiManager.size(); j++) {
                            AFCotisation cotisation = (AFCotisation) cotiManager.getEntity(j);

                            element = new String[2];
                            element[0] = cotisation.getCotisationId();
                            element[1] = cotisation.getAssurance().getAssuranceLibelleCourt();
                            if (cotisation.getAssurance().getTypeAssurance().equals("812002")) {
                                if (BSessionUtil.compareDateFirstLower(getSession(), cotisation.getDateDebut(),
                                        dateDebut)) {
                                    if ((BSessionUtil.compareDateFirstGreater(getSession(), cotisation.getDateFin(),
                                            dateFin)) || (cotisation.getDateFin().equals(""))) {

                                        temp = "true";
                                    }

                                }

                            }
                            vList.add(element);
                        }
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(getSession(), e);
        }
        return temp;
    }

    public java.lang.String getAnneeDecision() {
        return anneeDecision;
    }

    /**
     * Rechercher l'assurance de la Cotisation en fonction de son ID.
     * 
     * @return la Couverture
     */
    public AFAssurance getAssurance() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getAssuranceId())) {
            return null;
        }

        if ((_assurance == null) || (!getAssuranceId().equals(_assurance.getAssuranceId()))) {

            _assurance = new AFAssurance();
            _assurance.setSession(getSession());
            _assurance.setAssuranceId(getAssuranceId());
            try {
                _assurance.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _assurance = null;
            }
        }
        return _assurance;
    }

    public java.lang.String getAssuranceId() {
        return assuranceId;
    }

    /**
     * Rechercher le Plan de Caisse de la Cotisation en fonction de son ID.
     * 
     * @return le Plan de Caisse
     */
    public AFPlanCaisse getCaisse() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getPlanCaisseId())) {
            return new AFPlanCaisse();
        }

        if (_planCaisse == null) {

            _planCaisse = new AFPlanCaisse();
            _planCaisse.setSession(getSession());
            _planCaisse.setPlanCaisseId(getPlanCaisseId());
            try {
                _planCaisse.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _planCaisse = null;
            }
        }
        return _planCaisse;
    }

    /**
     * Retourne le canton de la cotisation pour un id affiliation.
     * 
     * @param IdAffiliation
     * @return string canton
     */
    public String getCantonAffilie(String IdAffiliation) {

        // Vector vList = new Vector();

        // Ajoute un blanc
        // String[] element = new String[2];
        // element[0]="";
        // element[1]="";
        // vList.add(element);
        String canton = "";
        try {

            AFPlanAffiliationManager planAffMananger = new AFPlanAffiliationManager();
            AFAffiliation affiliation = new AFAffiliation();
            affiliation.setAffiliationId(IdAffiliation);
            affiliation.setSession(getSession());
            affiliation.retrieve();
            if (affiliation.getTypeAffiliation().equalsIgnoreCase("19150036")) {

                canton = affiliation.getTiers().getCantonDomicile();
            }
            planAffMananger.setISession(getSession());
            planAffMananger.setForAffiliationId(IdAffiliation);
            planAffMananger.find();

            for (int i = 0; i < planAffMananger.size(); i++) {
                AFPlanAffiliation planAffiliation = (AFPlanAffiliation) planAffMananger.getEntity(i);
                if (!planAffiliation.isInactif().booleanValue()) {
                    AFCotisationManager cotiManager = new AFCotisationManager();
                    cotiManager.setISession(getSession());
                    cotiManager.setForPlanAffiliationId(planAffiliation.getPlanAffiliationId());
                    cotiManager.find();
                    for (int j = 0; j < cotiManager.size(); j++) {
                        AFCotisation cotisation = (AFCotisation) cotiManager.getEntity(j);
                        if (cotisation.getAssurance().getTypeAssurance().equals("812002")) {
                            canton = cotisation.getAssurance().getAssuranceCanton();
                            canton = getSession().getCodeLibelle(canton);
                        }
                        if (JadeStringUtil.isBlank(canton)) {

                            canton = getAffiliation().getTiers().getCantonDomicile();

                        }
                    }
                }
            }

        } catch (Exception e) {
            JadeLogger.error(getSession(), e);
        }

        return canton;
    }

    public java.lang.String getCategorieTauxId() {
        return categorieTauxId;
    }

    public java.lang.String getCotisationId() {
        return cotisationId;
    }

    /**
     * Rechercher la Couverture de la Cotisation en fonction de son Assurance et de son plan de Caisse.
     * 
     * @return la Couverture
     */
    public AFCouverture getCouverture() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getAssuranceId()) || JadeStringUtil.isIntegerEmpty(getPlanCaisseId())) {
            return null;
        }

        if ((_couverture == null)
                || ((_couverture != null) && (!_couverture.getAssuranceId().equals(getAssuranceId()) || (!_couverture
                        .getPlanCaisseId().equals(getPlanCaisseId()))))) {
            AFCouvertureManager manager = new AFCouvertureManager();
            manager.setSession(getSession());
            manager.setForAssuranceId(getAssuranceId());
            manager.setForPlanCaisseId(getPlanCaisseId());

            try {
                manager.find();
                if (manager.size() == 1) {
                    _couverture = (AFCouverture) manager.getEntity(0);
                } else {
                    _couverture = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _couverture = null;
            }
        }
        return _couverture;
    }

    public java.lang.String getDateDebut() {
        return dateDebut;
    }

    public java.lang.String getDateFin() {
        return dateFin;
    }

    public java.lang.String getDateFinAffichage() {
        if (JadeStringUtil.isEmpty(dateFin)) {
            return getDateFinRentier();
        }
        return dateFin;
    }

    /**
     * @return
     */
    public String getDateFinMin() {
        return dateFinMin;
    }

    /**
     * L'assurance personnelle AC doit être radiée automatiquement en fonction de l'age de la retraite
     */
    private String getDateFinRentier() {
        if (getAssurance() != null) {
            if ((CodeSystem.TYPE_ASS_COTISATION_AC.equals(getAssurance().getTypeAssurance()) || CodeSystem.TYPE_ASS_COTISATION_AC2
                    .equals(getAssurance().getTypeAssurance()))
                    && CodeSystem.GENRE_ASS_PERSONNEL.equals(getAssurance().getAssuranceGenre())) {
                // assurance AC ou AC2 IND
                try {
                    AFAgeRente ageRente = new AFAgeRente();
                    // on recherche l'age de la rente à la date du jour pour
                    // affichage
                    ageRente.initDateRente(getSession(), JACalendar.todayJJsMMsAAAA());
                    TITiersViewBean tiers = new TITiersViewBean();
                    tiers.setSession(getSession());
                    tiers.setIdTiers(getIdTiers());
                    tiers.retrieve();
                    return "(" + ageRente.getDateRente(tiers.getDateNaissance(), tiers.getSexe()) + ")";
                } catch (Exception ex) {
                    return dateFin;
                }
            }
        }
        return dateFin;
    }

    public java.lang.String getDirecte() {
        return directe;
    }

    public java.lang.Boolean getExceptionPeriodicite() {
        return exceptionPeriodicite;
    }

    /**
     * Retour les "Code System" a exclure pour la sélection de la "Periodicité". Régle: la périodicitéd'une cotisation
     * ne peut pas être plus petit que la périodicité de l'affiliation.
     * 
     * @return les CodeSystem a exclure
     */
    public HashSet<String> getExceptPeriodicite() {
        HashSet<String> except = new HashSet<String>();

        if (getAffiliation() != null) {
            if (CodeSystem.PERIODICITE_ANNUELLE.equals(getAffiliation().getPeriodicite())) {
                except.add(CodeSystem.PERIODICITE_ANNUELLE_31_MARS);
                except.add(CodeSystem.PERIODICITE_ANNUELLE_30_JUIN);
                except.add(CodeSystem.PERIODICITE_ANNUELLE_30_SEPT);
                except.add(CodeSystem.PERIODICITE_TRIMESTRIELLE);
                except.add(CodeSystem.PERIODICITE_MENSUELLE);

            } else if (CodeSystem.PERIODICITE_ANNUELLE_31_MARS.equals(getAffiliation().getPeriodicite())) {
                except.add(CodeSystem.PERIODICITE_ANNUELLE);
                except.add(CodeSystem.PERIODICITE_ANNUELLE_30_JUIN);
                except.add(CodeSystem.PERIODICITE_ANNUELLE_30_SEPT);
                except.add(CodeSystem.PERIODICITE_TRIMESTRIELLE);
                except.add(CodeSystem.PERIODICITE_MENSUELLE);

            } else if (CodeSystem.PERIODICITE_ANNUELLE_30_JUIN.equals(getAffiliation().getPeriodicite())) {
                except.add(CodeSystem.PERIODICITE_ANNUELLE_31_MARS);
                except.add(CodeSystem.PERIODICITE_ANNUELLE);
                except.add(CodeSystem.PERIODICITE_ANNUELLE_30_SEPT);
                except.add(CodeSystem.PERIODICITE_TRIMESTRIELLE);
                except.add(CodeSystem.PERIODICITE_MENSUELLE);

            } else if (CodeSystem.PERIODICITE_ANNUELLE_30_SEPT.equals(getAffiliation().getPeriodicite())) {
                except.add(CodeSystem.PERIODICITE_ANNUELLE_31_MARS);
                except.add(CodeSystem.PERIODICITE_ANNUELLE_30_JUIN);
                except.add(CodeSystem.PERIODICITE_ANNUELLE);
                except.add(CodeSystem.PERIODICITE_TRIMESTRIELLE);
                except.add(CodeSystem.PERIODICITE_MENSUELLE);

            } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equals(getAffiliation().getPeriodicite())) {
                except.add(CodeSystem.PERIODICITE_ANNUELLE_31_MARS);
                except.add(CodeSystem.PERIODICITE_ANNUELLE_30_JUIN);
                except.add(CodeSystem.PERIODICITE_ANNUELLE_30_SEPT);
                except.add(CodeSystem.PERIODICITE_MENSUELLE);

            } else if (CodeSystem.PERIODICITE_MENSUELLE.equals(getAffiliation().getPeriodicite())) {
                except.add(CodeSystem.PERIODICITE_ANNUELLE_31_MARS);
                except.add(CodeSystem.PERIODICITE_ANNUELLE_30_JUIN);
                except.add(CodeSystem.PERIODICITE_ANNUELLE_30_SEPT);
            }
        }
        return except;
    }

    public java.lang.String getIdTiers() {

        String idTiers = null;
        if (_tiers == null) {
            getTiers();
        }
        if (_tiers != null) {
            idTiers = _tiers.getIdTiers();
        }
        return idTiers;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public String getLibelleCourtCotisationAF(String idAffiliation, String dateValidite) throws Exception {

        AFPlanAffiliationManager planAffManager = new AFPlanAffiliationManager();

        planAffManager.setISession(getSession());
        planAffManager.setForAffiliationId(idAffiliation);
        planAffManager.setForPlanActif(true);
        planAffManager.find();

        if (planAffManager.size() == 0) {
            throw new Exception(getSession().getLabel("ERREUR_PLAN_INEXISTANT") + idAffiliation);
        }

        ArrayList<String> cotisations = new ArrayList<String>();

        for (int i = 0; i < planAffManager.size(); i++) {
            AFPlanAffiliation planAffiliation = (AFPlanAffiliation) planAffManager.getEntity(i);
            AFCotisationManager cotiManager = new AFCotisationManager();
            cotiManager.setISession(getSession());
            cotiManager.setForPlanAffiliationId(planAffiliation.getPlanAffiliationId());
            cotiManager.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AF);
            cotiManager.setForDate(dateValidite);
            cotiManager.find();

            for (int j = 0; j < cotiManager.size(); j++) {
                AFCotisation crtCotisation = (AFCotisation) cotiManager.getEntity(j);
                cotisations.add(crtCotisation.getAssurance().getAssuranceLibelleCourt());
            }
        }

        if (cotisations.size() == 0) {
            String error = getSession().getLabel("ERREUR_AUCUNE_COTISATION_AF") + idAffiliation + " date : "
                    + dateValidite;
            getSession().addError(error);
            throw new Exception(error);
        } else if (cotisations.size() > 1) {
            String error = getSession().getLabel("ERREUR_PLUS_UNE_COTISATION_AF") + idAffiliation + " date : "
                    + dateValidite;
            getSession().addError(error);
            throw new Exception(error);
        } else {
            return cotisations.get(0);
        }
    }

    public java.lang.Boolean getMaisonMere() {
        return maisonMere;
    }

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    protected BManager getManager() {
        return new AFCotisationManager();
    }

    public java.lang.String getMasseAnnuelle() {

        if (JadeStringUtil.isEmpty(masseAnnuelle)) {
            return JANumberFormatter.fmt(masseAnnuelle.toString(), true, false, true, 2);
        } else {
            return JANumberFormatter.round(masseAnnuelle.toString(), 0.05, 2, JANumberFormatter.NEAR);
        }
        // return
        // JANumberFormatter.fmt(masseAnnuelle.toString(),true,false,true,2);
    }

    public java.lang.String getMasseId() {
        return masseId;
    }

    public java.lang.String getMassePeriodicite() {
        if (!JadeStringUtil.isEmpty(masseAnnuelle)) {
            double massePeriodicite = Double.parseDouble(masseAnnuelle);

            if (CodeSystem.PERIODICITE_ANNUELLE.equals(getPeriodicite())
                    || CodeSystem.PERIODICITE_ANNUELLE_31_MARS.equals(getPeriodicite())
                    || CodeSystem.PERIODICITE_ANNUELLE_30_JUIN.equals(getPeriodicite())
                    || CodeSystem.PERIODICITE_ANNUELLE_30_SEPT.equals(getPeriodicite())) {
                // Déjà le montant annuel
            } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equals(getPeriodicite())) {
                massePeriodicite = massePeriodicite / 4;
            } else if (CodeSystem.PERIODICITE_MENSUELLE.equals(getPeriodicite())) {
                massePeriodicite = massePeriodicite / 12;
            }
            FWCurrency c = new FWCurrency(massePeriodicite);
            return c.toStringFormat();

        } else {
            return "";
        }
    }

    public java.lang.String getMontant() {

        String result = "";

        if (_montantAssurance == null) {
            if (getAssurance() != null) {

                try {
                    String date = JACalendar.todayJJsMMsAAAA();
                    if (!JadeStringUtil.isBlankOrZero(getDateFin())) {
                        date = getDateFin();
                    }

                    // List tauxList = getTauxList(date);
                    AFTauxAssurance taux = findTauxWithRecalcul(date, null, true, false, "");
                    // if (tauxList.size() > 0) {
                    if (taux != null) {
                        // _montantAssurance = (AFTauxAssurance)
                        // tauxList.get(0);

                        if (CodeSystem.GEN_VALEUR_ASS_MONTANT.equals(taux.getGenreValeur())) {
                            _montantAssurance = taux;
                            result = _montantAssurance.getValeurTotal();
                        }
                    }
                } catch (Exception e) {
                    _addError(null, e.getMessage());
                    _montantAssurance = null;
                }
            }
        } else {
            result = _montantAssurance.getValeurTotal();
        }
        return result;
    }

    public java.lang.String getMontantAnnuel() {
        return JANumberFormatter.fmt(montantAnnuel.toString(), true, false, true, 2);
    }

    public java.lang.String getMontantMensuel() {
        return JANumberFormatter.fmt(montantMensuel.toString(), true, false, true, 2);
    }

    public java.lang.String getMontantPeriodicite() {
        if (CodeSystem.PERIODICITE_ANNUELLE.equals(getPeriodicite())
                || CodeSystem.PERIODICITE_ANNUELLE_31_MARS.equals(getPeriodicite())
                || CodeSystem.PERIODICITE_ANNUELLE_30_JUIN.equals(getPeriodicite())
                || CodeSystem.PERIODICITE_ANNUELLE_30_SEPT.equals(getPeriodicite())) {
            return getMontantAnnuel();
        } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equals(getPeriodicite())) {
            return getMontantTrimestriel();
        } else if (CodeSystem.PERIODICITE_MENSUELLE.equals(getPeriodicite())) {
            return getMontantMensuel();
        }
        return "";
    }

    public java.lang.String getMontantSemestriel() {
        return JANumberFormatter.fmt(montantSemestriel.toString(), true, false, true, 2);
    }

    public java.lang.String getMontantTrimestriel() {
        return JANumberFormatter.fmt(montantTrimestriel.toString(), true, false, true, 2);
    }

    public java.lang.String getMotifFin() {
        return motifFin;
    }

    public boolean getPassageValidate() {
        return passageValidate;
    }

    public java.lang.String getPeriodicite() {
        return periodicite;
    }

    public java.lang.String getPeriodiciteMontant() {

        String result = "";

        if (_montantAssurance == null) {
            getMontant();
        }
        if (_montantAssurance != null) {
            result = _montantAssurance.getPeriodiciteMontant();
        }
        return result;
    }

    /**
     * Rechercher le plan d'affiliation pour la Cotisation en fonction de son ID.
     * 
     * @return le plan d'affiliation
     */
    public AFPlanAffiliation getPlanAffiliation() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getPlanAffiliationId())) {
            return null;
        }

        if ((_planAffiliation == null) || _planAffiliation.isNew()) {

            _planAffiliation = new AFPlanAffiliation();
            _planAffiliation.setSession(getSession());
            _planAffiliation.setPlanAffiliationId(getPlanAffiliationId());
            try {
                _planAffiliation.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _planAffiliation = null;
            }
        }
        return _planAffiliation;
    }

    // public java.lang.String getTaux() {
    //
    // String result = "";
    //
    // if (_tauxAssurance == null && getAssurance() != null) {
    //
    // // Recherche du taux pour l'assurance
    // AFTauxAssuranceListViewBean tauxAssuList = new
    // AFTauxAssuranceListViewBean();
    // tauxAssuList.setSession(getSession());
    // tauxAssuList.setForIdAssurance(getAssurance().getAssuranceId());
    // tauxAssuList.setForGenreValeur(CodeSystem.GEN_VALEUR_ASS_TAUX);
    // tauxAssuList.setForDate(JACalendar.todayJJsMMsAAAA());
    // if (! JadeStringUtil.isIntegerEmpty(getDateFin())) {
    // tauxAssuList.setForDate(getDateFin());
    // }
    //
    // try {
    // tauxAssuList.find();
    //
    // if (tauxAssuList.size() > 0) {
    // _tauxAssurance = (AFTauxAssurance) tauxAssuList.getEntity(0);
    // result = _tauxAssurance.getValeurTotal();
    // }
    // } catch (Exception e) {
    // _addError(null, e.getMessage());
    // _tauxAssurance = null;
    // }
    // }
    // return result;
    // }

    // public java.lang.String getTaux(String dateValeur, String tranche) {
    //
    // String result = "";
    //
    // if (_tauxAssurance == null && getAssurance() != null) {
    //
    // // Recherche du taux pour l'assurance
    // AFTauxAssuranceListViewBean tauxAssuList = new
    // AFTauxAssuranceListViewBean();
    // tauxAssuList.setSession(getSession());
    // tauxAssuList.setForIdAssurance(getAssurance().getAssuranceId());
    // tauxAssuList.setFromTranche(tranche);
    // tauxAssuList.setForDate(dateValeur);
    // tauxAssuList.setOrderByGenreAndDateDebutDesc();
    // if (! JadeStringUtil.isIntegerEmpty(getDateFin())) {
    // tauxAssuList.setForDate(getDateFin());
    // }
    //
    // try {
    // tauxAssuList.find();
    //
    // if (tauxAssuList.size() > 0) {
    // _tauxAssurance = (AFTauxAssurance) tauxAssuList.getEntity(0);
    // result = _tauxAssurance.getValeurTotal();
    // }
    // } catch (Exception e) {
    // _addError(null, e.getMessage());
    // _tauxAssurance = null;
    // }
    // }
    // return result;
    // }

    // public java.lang.String getFractionTaux() {
    //
    // String result = "";
    //
    // if (_tauxAssurance == null) {
    // getTaux();
    // }
    // if (_tauxAssurance != null) {
    // result = _tauxAssurance.getFraction();
    // }
    // return result;
    // }

    public java.lang.String getPlanAffiliationId() {
        return planAffiliationId;
    }

    public java.lang.String getPlanCaisseId() {
        return planCaisseId;
    }

    /*
     * public FWParametersSystemCode getCsMotifFin() { if (csMotifFin == null) { // liste pas encore chargée, on la
     * charge csMotifFin = new FWParametersSystemCode(); csMotifFin.getCode(getMotifFin()); } return csMotifFin; }
     */

    /*
     * public FWParametersSystemCode getCsPeriodicite() { if (csPeriodicite == null) { // liste pas encore chargée, on
     * la charge csPeriodicite = new FWParametersSystemCode(); csPeriodicite.getCode(getPeriodicite()); } return
     * csPeriodicite; }
     */

    /**
     * Retourne la valeur du taux relatif à cette cotisation.
     * 
     * @param montant Utilisé en cas de taux variable
     * @param date La date pour laquelle le taux doit être valide
     * @return Un String représentant la valeur du taux
     */
    public String getTaux(String date, String montant) {

        try {
            // List tauxList = getTauxList(date);
            AFTauxAssurance taux;
            if (CodeSystem.GENRE_ASS_PARITAIRE.equals(getAssurance().getAssuranceGenre())) {
                taux = this.findTaux(date, montant, true);
            } else {
                // si personnel, on effectue une recherche standard pour les
                // taux variables (ajouté pour le support de l'AMAT GE)
                taux = this.findTaux(date, montant, false);
            }
            // if (tauxList.size() > 0) {
            if (taux != null) {
                // AFTauxAssurance taux = (AFTauxAssurance) tauxList.get(0);
                return taux.getValeurTotal();
                /*
                 * if (taux.getGenreValeur().equals(CodeSystem.GEN_VALEUR_ASS_TAUX )) { return taux.getValeurTotal(); }
                 * else if(taux.getGenreValeur().equals(CodeSystem. GEN_VALEUR_ASS_TAUX_VARIABLE)) { List
                 * tauxVariableList = new ArrayList(); String rang = null; String previousRang = null; String
                 * previousDateDebut = null; // Initialisation d'une liste contenant les Taux Variables. for (int i=0; i
                 * < tauxList.size() ; i++) { taux = (AFTauxAssurance) tauxList.get(i); rang = taux.getRang(); if
                 * (previousRang != null && previousRang.equals(rang)) { // On initialise la date de fin en fonction de
                 * la date de début // du Taux suivant de même rang.
                 * taux.setDateFin(AFUtil.getDatePreviousDay(previousDateDebut )); } tauxVariableList.add(taux);
                 * previousRang = rang; previousDateDebut = taux.getDateDebut(); } // end for // Recherche dans la liste
                 * du taux. for (int i=0 ; i < tauxVariableList.size() ; i++){ taux =
                 * (AFTauxAssurance)tauxVariableList.get(i); double tranche = 0.0; if (!
                 * JadeStringUtil.isEmpty(taux.getTranche())) { tranche = Double.parseDouble( JANumberFormatter.deQuote(
                 * taux.getTranche())); } if ((JadeStringUtil.isIntegerEmpty(taux.getDateFin()) || (!
                 * JadeStringUtil.isIntegerEmpty(taux.getDateFin()) &&
                 * BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), taux.getDateFin(), dateFin))) && (tranche >
                 * Double.parseDouble(montant) || tranche == 0.0)) { return taux.getValeurTotal(); } } // end for }
                 */
            } else {
                return "";
            }

        } catch (Exception e) {
            _addError(null, getMessage());
            return "";
        }
        // return "";
    }

    public java.lang.String getTauxAssuranceId() {
        return tauxAssuranceId;
    }

    /**
     * Rechercher le tiers pour la Cotisation en fonction de l'affiliation.
     * 
     * @return le tiers
     */
    public TITiers getTiers() {

        // Si pas d'identifiant => pas d'objet
        if (_tiers == null) {
            if (_affiliation == null) {
                getAffiliation();
                if (_affiliation == null) {
                    return null;
                }
            }
            _tiers = new TITiers();
            _tiers.setSession(getSession());
            _tiers.setIdTiers(_affiliation.getIdTiers());
            try {
                _tiers.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _tiers = null;
            }
        }
        return _tiers;
    }

    public String getTraitementMoisAnnee() {
        /*
         * if(CodeSystem.PERIODICITE_ANNUELLE.equals(getPeriodicite()) &&
         * JadeStringUtil.isIntegerEmpty(traitementMoisAnnee)) { // si periodicité annuelle, on retourne décembre par
         * défault return CodeSystem.MOIS_DECEMBRE; }
         */
        return traitementMoisAnnee;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void passageValidate(boolean newPassageValidate) {
        passageValidate = newPassageValidate;
    }

    public void resetLiaisons() {
        _assurance = null;
        _planAffiliation = null;
        _planCaisse = null;
        _adhesion = null;
        _affiliation = null;
        _tiers = null;

    }

    public void setAdhesionId(java.lang.String string) {
        adhesionId = string;
    }

    public void setAffiliationId(java.lang.String string) {
        affiliationId = string;
    }

    public void setAnneeDecision(java.lang.String newAnneeDecision) {
        anneeDecision = newAnneeDecision;
    }

    public void setAssuranceId(java.lang.String newAssuranceId) {
        assuranceId = newAssuranceId;
    }

    public void setCategorieTauxId(java.lang.String string) {
        categorieTauxId = string;
    }

    public void setCotisationId(java.lang.String newCotisationId) {
        cotisationId = newCotisationId;
    }

    public void setDateDebut(java.lang.String newDateDebut) {
        dateDebut = newDateDebut;
    }

    public void setDateFin(java.lang.String newDateFin) {
        if ((newDateFin != null) && !newDateFin.startsWith("(")) {
            // date autre que retraite (informative)
            dateFin = newDateFin;
        }
    }

    /**
     * @param string
     */
    public void setDateFinMin(String string) {
        dateFinMin = string;
    }

    public void setDirecte(java.lang.String newDirecte) {
        directe = newDirecte;
    }

    public void setExceptionPeriodicite(java.lang.Boolean boolean1) {
        exceptionPeriodicite = boolean1;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public void setMaisonMere(java.lang.Boolean newMaisonMere) {
        maisonMere = newMaisonMere;
    }

    public void setMasseAnnuelle(java.lang.String newMasseAnnuelle) {
        masseAnnuelle = JANumberFormatter.deQuote(newMasseAnnuelle);
    }

    public void setMasseId(java.lang.String newMasseId) {
        masseId = newMasseId;
    }

    public void setMontantAnnuel(java.lang.String newMontantAnnuel) {
        montantAnnuel = JANumberFormatter.deQuote(newMontantAnnuel);
    }

    public void setMontantMensuel(java.lang.String newMontantMensuel) {
        montantMensuel = JANumberFormatter.deQuote(newMontantMensuel);
    }

    public void setMontantSemestriel(java.lang.String newMontantSemestriel) {
        montantSemestriel = JANumberFormatter.deQuote(newMontantSemestriel);
    }

    public void setMontantTrimestriel(java.lang.String newMontantTrimestriel) {
        montantTrimestriel = JANumberFormatter.deQuote(newMontantTrimestriel);
    }

    public void setMotifFin(java.lang.String newMotifFin) {
        motifFin = newMotifFin;
    }

    public void setPeriodicite(java.lang.String newPeriodicite) {
        periodicite = newPeriodicite;
    }

    public void setPlanAffiliationId(java.lang.String string) {
        planAffiliationId = string;
    }

    public void setPlanCaisseId(java.lang.String string) {
        planCaisseId = string;
    }

    public void setTauxAssuranceId(java.lang.String string) {
        tauxAssuranceId = string;
    }

    public void setTraitementMoisAnnee(String string) {
        traitementMoisAnnee = string;
    }

    /**
     * Mettre a jour la MassePériodicité pour la périodicité de l'affiliation en fonction du la nouvelle valeur et de la
     * périodicité de la nouvelle valeur.
     * 
     * @param newMasse la nouvelle valeur
     * @param newMassePeriodicite la périodicité de la nouvelle valeur
     * @throws Exception
     */
    public void updateMasseAnnuelle(String periodiciteMontant, String newMassePeriodicite) throws Exception {

        if (!JadeStringUtil.isIntegerEmpty(JANumberFormatter.deQuote(newMassePeriodicite))) {
            try {
                double montant = Double.parseDouble(JANumberFormatter.deQuote(newMassePeriodicite));

                if (CodeSystem.PERIODICITE_MENSUELLE.equals(periodiciteMontant)) {
                    montant = montant * 12.0;
                } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equals(periodiciteMontant)) {
                    montant = montant * 4.0;
                    /*
                     * } else if(CodeSystem.PERIODICITE_SEMESTRIELLE.equals( periodiciteMontant)) { montant = montant
                     * 2.0;
                     */
                } else if (CodeSystem.PERIODICITE_ANNUELLE.equals(periodiciteMontant)) {
                    // Déjà le montant annuel
                }
                setMasseAnnuelle(Double.toString(montant));
            } catch (Exception e) {
                _addError(null, getSession().getLabel("1150"));
            }
        } else {
            setMasseAnnuelle("");
        }
    }

    /**
     * Mettre a jour la MassePériodicité pour la périodicité de l'affiliation en fonction du la nouvelle valeur et de la
     * périodicité de la nouvelle valeur et pour la date de modification donnée. Si la date est non vide, met la date de
     * fin de cette cotisation a jour avec le jour precedant la date et sauve dans la base une nouvelle cotisation
     * identique a celle-ci avec la date comme date de debut, sinon fait pareil que
     * {@link #updateMasseAnnuelle(String, String) updateMassAnnuelle}.
     * 
     * @param newMasse la nouvelle valeur
     * @param newMassePeriodicite la périodicité de la nouvelle valeur
     * @param periode la nouvelle période de la cotisation
     * @param dateModification la date a partir de laquelle la modification doit devenir active
     * @throws Exception
     */
    public void updateMasseAnnuelle(String periodiciteMontant, String newMassePeriodicite, String periode,
            String dateModification, boolean masseDifferenteDeZero) throws Exception {
        if (JAUtil.isDateEmpty(dateModification)) {
            this.retrieve();
            boolean needUpdate = false;
            if (periode != null) {
                setPeriodicite(periode);
                needUpdate = true;
            }
            if ((newMassePeriodicite != null) && (newMassePeriodicite.length() != 0)) {
                this.updateMasseAnnuelle(periodiciteMontant, newMassePeriodicite);
                needUpdate = true;
            }

            if (!getSession().hasErrors() && needUpdate) {
                this.update();
            }
        } else {
            String dateFinEventuelle = AFCotisation.CALENDAR.addDays(new JADate(dateModification), -1).toStr(".");
            if (BSessionUtil.compareDateFirstLower(getSession(), dateDebut, dateFinEventuelle)) {
                // creer une copie de cette cotisation
                AFCotisation copie = new AFCotisation();
                copie.copyDataFromEntity(this);
                copie.setDateDebut(dateModification);
                if (periode != null) {
                    copie.setPeriodicite(periode);
                }
                if ((newMassePeriodicite != null) && (newMassePeriodicite.length() != 0)) {
                    copie.updateMasseAnnuelle(periodiciteMontant, newMassePeriodicite);
                }
                // donner une fin a cette cotisation
                setDateFin(AFCotisation.CALENDAR.addDays(new JADate(dateModification), -1).toStr("."));
                if (masseDifferenteDeZero == false) {
                    setMotifFin(CodeSystem.MOTIF_FIN_SANS_PERS); // motif 'Sans
                    // personnel'
                    // si masse
                    // à zéro
                } else {
                    setMotifFin(CodeSystem.MOTIF_FIN_MASSE); // motif
                    // 'Changement
                    // de masse
                    // salariale'
                }
                // on update d'abord pour pouvoir avoir le droit de creer une
                // nouvelle cotis sans date fin
                this.update();
                // sauver la nouvelle cotisation a present que c'est possible
                if (!getSession().hasErrors()) {
                    copie.add();
                }
            } else {
                // dans le cas ou la date insérer dans la modification
                // de la masse est plus petite que la date de début de la
                // cotisation
                // on ne crée pas de copie mais on update la cotisation
                // directement
                // car la date de fin de la cotisation ne pas pas être plus
                // petite que celle de début
                if (periode != null) {
                    setPeriodicite(periode);
                }
                if ((newMassePeriodicite != null) && (newMassePeriodicite.length() != 0)) {
                    this.updateMasseAnnuelle(periodiciteMontant, newMassePeriodicite);
                }
                if (!getSession().hasErrors()) {
                    this.update();
                }
            }
        }
    }

    /**
     * Mettre a jour la Périodicité de la Cotisation.
     * 
     * @throws Exception
     */
    public void updatePeriodicite() throws Exception {
        if (!getExceptionPeriodicite().booleanValue()) {
            setPeriodicite(getAffiliation().getPeriodicite());
        }
    }

    private void validePeriodiciteAffCoti(BTransaction transaction) throws Exception {

        AFAffiliation theAffiliation = getAffiliation();
        if ((theAffiliation == null) || theAffiliation.isNew()) {
            if (transaction.hasErrors() == false) {
                _addError(transaction, getSession().getLabel("1100") + " -  id Cotisation: " + getCotisationId());
            }
            return;
        }

        String periodiciteAff = theAffiliation.getPeriodicite();
        String periodiciteCoti = getPeriodicite();

        if (CodeSystem.PERIODICITE_ANNUELLE.equalsIgnoreCase(periodiciteAff)
                && CodeSystem.PERIODICITE_ANNUELLE.equalsIgnoreCase(periodiciteCoti)) {
            return;
        } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equalsIgnoreCase(periodiciteAff)
                && (CodeSystem.PERIODICITE_ANNUELLE.equalsIgnoreCase(periodiciteCoti) || CodeSystem.PERIODICITE_TRIMESTRIELLE
                        .equalsIgnoreCase(periodiciteCoti))) {
            return;
        } else if (CodeSystem.PERIODICITE_MENSUELLE.equalsIgnoreCase(periodiciteAff)
                && (CodeSystem.PERIODICITE_ANNUELLE.equalsIgnoreCase(periodiciteCoti)
                        || CodeSystem.PERIODICITE_TRIMESTRIELLE.equalsIgnoreCase(periodiciteCoti) || CodeSystem.PERIODICITE_MENSUELLE
                            .equalsIgnoreCase(periodiciteCoti))) {
            return;
        } else if (((CodeSystem.PERIODICITE_TRIMESTRIELLE.equalsIgnoreCase(periodiciteAff) && CodeSystem.PERIODICITE_MENSUELLE
                .equalsIgnoreCase(periodiciteCoti)))) {
            return;
        } else {
            _addError(
                    transaction,
                    FWMessageFormat.format(getSession().getLabel("ERREUR_PERIODICITE_AFFCOTI"),
                            JadeCodesSystemsUtil.getCodeLibelle(getSession(), periodiciteAff)));
        }
    }

    public java.lang.Boolean getMiseAjourDepuisEcran() {
        return miseAjourDepuisEcran;
    }

    public void setMiseAjourDepuisEcran(java.lang.Boolean miseAjourDepuisEcran) {
        this.miseAjourDepuisEcran = miseAjourDepuisEcran;
    }

    // @BMS-ONLY
    public AFCotisation getOldCotisation() {
        return oldCotisation;
    }
}
