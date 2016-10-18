package globaz.naos.db.releve;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.draco.application.DSApplication;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.util.DSUtil;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEControleEmployeurManager;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.process.handler.LEJournalHandler;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.lupus.db.journalisation.LUJournalViewBean;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FARemarque;
import globaz.musca.external.ServicesFacturation;
import globaz.musca.util.FAUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import globaz.naos.db.assurance.AFCalculAssurance;
import globaz.naos.db.controleEmployeur.AFControleEmployeur;
import globaz.naos.db.controleEmployeur.AFControleEmployeurManager;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliationManager;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.db.processFacturation.AFProcessFacturationManager;
import globaz.naos.db.processFacturation.AFProcessFacturationViewBean;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.db.tauxAssurance.AFTauxAssuranceManager;
import globaz.naos.db.tauxAssurance.AFTauxVariableUtil;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFAgeRente;
import globaz.naos.util.AFUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CACompteur;
import globaz.osiris.utils.CAUtil;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.db.tiers.TIRole;
import globaz.pyxis.db.tiers.TITiers;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe définissant l'entité Relevé.
 * 
 * @author jts<br/>
 *         sau 18 avr. 05 13:49:52
 */
public class AFApercuReleve extends BEntity {

    private static final long serialVersionUID = -7670516076733812694L;

    // compte annexe pour décompte 14
    CACompteAnnexe _compteAnnexe = null;
    private TITiers _tiers;
    private String affiliationId = "";
    private String affilieNumero = new String();
    private String anneeReference = "";
    private List<?> apercuReleveLineFacturationList = null;
    private String collaborateur = new String();

    private List<AFApercuReleveLineFacturation> cotisationList = new ArrayList<AFApercuReleveLineFacturation>();

    // Fields
    private String dateDebut = new String();
    private String dateFin = new String();
    private String dateReception = new String();
    private boolean doCalculCotisation = false;
    private String etat = new String();

    private boolean firstCalculation = true;
    // force la période avant l'affiliation
    Boolean forcePeriode = new Boolean(false);
    // Foreign Key
    private String idEntete = new String();
    private String idExterneFacture = new String();

    private String idPassage = new String();

    // DB Table AFREVEP
    // Primary Key
    private String idReleve = new String();
    private String idSousTypeFacture = new String();
    private String idTiers = new String();
    private String interets = new String();
    private boolean montantNegatif = false;

    private String newEtat = new String();
    // plan d'affiliation si spécifié
    private String planAffiliationId = new String();

    private Boolean previsionAcompteEBU = Boolean.FALSE;
    private BISession sessionMusca = null;
    private BISession sessionOsiris = null;

    private String totalCalculer = new String();
    private String totalControl = new String();
    private String type = new String();
    private boolean wantControleCotisation = true;

    /**
     * Constructeur
     */
    public AFApercuReleve() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        // Traitement pour le suivi des relevés
        // Création d'une session LEO
        BIApplication remoteApplication = GlobazServer.getCurrentSystem().getApplication("LEO");
        BSession sessionLeo = (BSession) remoteApplication.newSession(getSession());
        JADate jaDateDebut = new JADate(dateDebut);
        JADate jaDateFin = new JADate(dateFin);
        String datePeriodeSuivi = "";
        if (jaDateDebut.getMonth() != jaDateFin.getMonth()) {
            datePeriodeSuivi += jaDateDebut.getMonth() + "-";
        }
        datePeriodeSuivi += jaDateFin.getMonth() + "." + jaDateFin.getYear();

        // recherche des suivi en fonction de la période et du libellé du plan
        // itération sur les plan différents
        String lastLibelle = null;
        if (cotisationList != null) {
            for (int cnt = 0; cnt < cotisationList.size(); cnt++) {
                AFApercuReleveLineFacturation line = cotisationList.get(cnt);

                if ((lastLibelle == null) || !line.getLibellePlan().equals(lastLibelle)) {
                    lastLibelle = line.getLibellePlan();
                    // On sette les critères qui nous permettent de retrouver
                    // l'envoi (si il en a un)
                    LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
                    // provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO,
                    // affilieNumero);
                    provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                            AFApplication.DEFAULT_APPLICATION_NAOS);
                    provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, getAffilieNumero());
                    // On lui passe la période à réclamer selon la périodicité
                    // de l'affiliation
                    provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, datePeriodeSuivi);
                    provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PLAN, lastLibelle);
                    // On recherche dans la gestion des envois s'il y a un envoi
                    // qui concerne le relevé qu'on est en train d'ajouter
                    LUJournalListViewBean viewBean = new LUJournalListViewBean();
                    viewBean.setSession(getSession());
                    viewBean.setProvenance(provenanceCriteres);
                    viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_CATEGORIE_GROUPE);
                    viewBean.setForValeurCodeSysteme(ILEConstantes.CS_CATEGORIE_SUIVI_RELEVES);
                    viewBean.setForIdSuivant("0");
                    viewBean.find();
                    for (int i = 0; i < viewBean.size(); i++) {
                        LUJournalViewBean vBean = (LUJournalViewBean) viewBean.getEntity(i);
                        // On génère la reception du document dans LEO avec la
                        // date du jour
                        LEJournalHandler journalHandler = new LEJournalHandler();
                        try {
                            if (JadeStringUtil.isEmpty(dateReception)) {
                                journalHandler.genererJournalisationReception(vBean.getIdJournalisation(),
                                        String.valueOf(JACalendar.today()), sessionLeo, transaction);
                            } else {
                                journalHandler.genererJournalisationReception(vBean.getIdJournalisation(),
                                        dateReception, sessionLeo, transaction);
                            }
                        } catch (Exception ex) {
                            // problème de paramétrage du suivi, on ignore le
                            // traitement et on log
                            JadeLogger.warn(this, ex.toString());
                        }
                    }
                }
            }
        }

        // Traitement pour le suivi des DS pour décompte final

        if (("true".equalsIgnoreCase(getSession().getApplication().getProperty("releveMiseAJourDate", "false")) && CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE
                .equalsIgnoreCase(getType()))
                || (CodeSystem.TYPE_RELEVE_DECOMP_FINAL.equals(getType()) || CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA
                        .equals(getType()))) {

            LUJournalListViewBean viewBean = new LUJournalListViewBean();
            // On sette les données nécessaires à la recherche de l'envoi
            LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, getIdTiers());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, getAffilieNumero());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                    DSApplication.DEFAULT_APPLICATION_DRACO);
            if (getDateFin().length() > 6) {
                provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, getDateFin().substring(6));
            }
            // On va regarder s'il y a déjà un envoi correspondant aux critères
            // qui nous intéressent.
            viewBean.setSession(getSession());
            viewBean.setProvenance(provenanceCriteres);
            viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_CATEGORIE_GROUPE);
            viewBean.setForValeurCodeSysteme(ILEConstantes.CS_CATEGORIE_SUIVI_DS);
            viewBean.setForIdSuivant("0");
            viewBean.find();
            // Dans le cas où il existe un envoi, on va mettre la date de
            // réception dans LEO
            for (int i = 0; i < viewBean.size(); i++) {
                LUJournalViewBean vBean = (LUJournalViewBean) viewBean.getEntity(i);

                // vBean.get
                // On génère la reception du document dans LEO avec la date du
                // jour
                LEJournalHandler journalHandler = new LEJournalHandler();
                if (JadeStringUtil.isEmpty(dateReception)) {
                    journalHandler.genererJournalisationReception(vBean.getIdJournalisation(),
                            String.valueOf(JACalendar.today()), sessionLeo, transaction);
                } else {
                    journalHandler.genererJournalisationReception(vBean.getIdJournalisation(), dateReception,
                            sessionLeo, transaction);
                }

            }
        }

    }

    /**
     * Effectue des traitements avant un ajout dans la BD.<br>
     * 
     * !! Cette methode est appellée AVANT _validate() <br>
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdReleve(this._incCounter(transaction, "0"));
        _afterRetrieve(transaction);

        // validation
        validationMandatory();

        if (getNewEtat().equals(CodeSystem.ETATS_RELEVE_FACTURER)) {
            addReleveInFacturation(transaction);
        } else {
            addReleveMontant(transaction);
        }
    }

    /**
     * Effectue des traitements avant une suppression dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        if (getEtat().equals(CodeSystem.ETATS_RELEVE_COMPTABILISER)) {
            _addError(transaction, getSession().getLabel("1680"));
        } else if (getEtat().equals(CodeSystem.ETATS_RELEVE_FACTURER)) {
            _addError(transaction, getSession().getLabel("1690"));
        } else if (getEtat().equals(CodeSystem.ETATS_RELEVE_SAISIE)) {
            deleteReleveMontant(transaction);
        }
    }

    /**
     * Effectue des traitements avant une modification dans la BD.<br>
     * 
     * !! Cette methode est appellée APRÈS _validate()
     * 
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        if (getNewEtat().equals(CodeSystem.ETATS_RELEVE_FACTURER)) {
            addReleveInFacturation(transaction);
            deleteReleveMontant(transaction);

        } else if (getNewEtat().equals(CodeSystem.ETATS_RELEVE_SAISIE)) {

            if (getEtat().equals(CodeSystem.ETATS_RELEVE_SAISIE)) {
                updateReleveMontant(transaction);
            } else {
                generationCotisationList();
                addReleveMontantWithReleveInFacturation(transaction);
            }
        }
        if (!transaction.hasErrors() && !transaction.hasWarnings()) {
            setEtat(getNewEtat());
        }
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "AFREVEP";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idReleve = statement.dbReadNumeric("MMIREL");
        idEntete = statement.dbReadNumeric("MMEBID");
        idTiers = statement.dbReadNumeric("HTITIE");
        affilieNumero = statement.dbReadString("MALNAF");
        dateDebut = statement.dbReadDateAMJ("MMDDEB");
        dateFin = statement.dbReadDateAMJ("MMDFIN");
        totalControl = statement.dbReadNumeric("MMTOTA");
        type = statement.dbReadNumeric("MMTYRE");
        etat = statement.dbReadNumeric("MMETAT");
        interets = statement.dbReadNumeric("MMINTE");
        dateReception = statement.dbReadDateAMJ("MMDREC");

        idExterneFacture = statement.dbReadString("IDEXTERNEFACTURE");

        // Année de référence pour le taux
        anneeReference = statement.dbReadNumeric("MMANTX");

    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        boolean validationOK = true;

        try {
            if (validationOK && !getNewEtat().equals(CodeSystem.ETATS_RELEVE_SAISIE)) {
                validationOK &= _propertyMandatory(statement.getTransaction(), getIdPassage(),
                        getSession().getLabel("1620"));
                if (validationOK && JadeStringUtil.isEmpty(getTotalCalculer())) {
                    _addError(statement.getTransaction(), getSession().getLabel("1590"));
                    validationOK = false;
                } else if (validationOK && !JadeStringUtil.isEmpty(getTotalCalculer())) {
                    double dTotalMontant = Double.parseDouble(JANumberFormatter.deQuote(getTotalCalculer()));
                    double dTotalControl;
                    if (JadeStringUtil.isEmpty(getTotalControl())) {
                        dTotalControl = 0.0;
                    } else {
                        dTotalControl = Double.parseDouble(JANumberFormatter.deQuote(getTotalControl()));
                    }
                    if ((dTotalControl != 0.0) && (dTotalMontant != dTotalControl)) {
                        _addError(statement.getTransaction(), getSession().getLabel("1600"));
                        validationOK = false;
                    }
                }
            }
        } catch (NumberFormatException nfe) {
            _addError(statement.getTransaction(), getSession().getLabel("1590"));
            validationOK = false;
        }

        if (isNew()) {
            if (!validationOK || statement.getTransaction().hasErrors() || statement.getTransaction().hasWarnings()) {
                setEtat(CodeSystem.ETATS_RELEVE_SAISIE);
            } else {
                setEtat(getNewEtat());
            }
        }
        // Lors de la validation ou de la facturation d'un décompte de type 13
        // ou 14
        // On met un message d'erreur si l'affilié a une particularité
        // "Code blocage - Décompte final"
        if (!JadeStringUtil.isBlankOrZero(getType())) {
            if (CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE.equals(getType())
                    || CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA.equals(getType())) {
                if (!cotisationList.isEmpty() && cotisationList.get(0) != null) {
                    AFApercuReleveLineFacturation releveLine = cotisationList.get(0);
                    if (!JadeStringUtil.isBlankOrZero(releveLine.getIdPlan())) {
                        AFPlanAffiliation planAffilie = new AFPlanAffiliation();
                        planAffilie.setSession(getSession());
                        planAffilie.setPlanAffiliationId(releveLine.getIdPlan());
                        planAffilie.retrieve();
                        if (!planAffilie.isNew()) {
                            AFParticulariteAffiliationManager particulariteMana = new AFParticulariteAffiliationManager();
                            particulariteMana.setSession(getSession());
                            particulariteMana.setForAffiliationId(planAffilie.getAffiliationId());
                            particulariteMana.setForParticularite(CodeSystem.PARTIC_AFFILIE_CODE_BLOCAGE_DECFINAL);
                            particulariteMana.find(BManager.SIZE_USEDEFAULT);
                            if (particulariteMana.size() > 0) {
                                _addError(statement.getTransaction(), getSession().getLabel("PLAUSI_PARTICULARITE")
                                        + getSession().getCodeLibelle(CodeSystem.PARTIC_AFFILIE_CODE_BLOCAGE_DECFINAL)
                                        + getSession().getLabel("PLAUSI_PARTICULARITE2") + " " + getAffilieNumero());
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
        statement.writeKey("MMIREL", this._dbWriteNumeric(statement.getTransaction(), getIdReleve(), ""));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MMIREL",
                this._dbWriteNumeric(statement.getTransaction(), getIdReleve(), "idReleve (MMIREL)"));
        statement.writeField("MMEBID", this._dbWriteNumeric(statement.getTransaction(), idEntete, "idEntete (MMEBID)"));
        statement.writeField("HTITIE",
                this._dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers (HTITIE)"));
        statement.writeField("MALNAF",
                this._dbWriteString(statement.getTransaction(), getAffilieNumero(), "affilieNumero (MALNAF)"));
        statement.writeField("MMDDEB",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "dateDebut (MMDDEB)"));
        statement.writeField("MMDFIN",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateFin(), "dateFin (MMDDEB)"));
        statement.writeField("MMTOTA",
                this._dbWriteNumeric(statement.getTransaction(), getTotalControl(), "totalControl (MMTOTA)"));
        statement.writeField("MMTYRE",
                this._dbWriteNumeric(statement.getTransaction(), getType(), "typeReleve (MMTYRE)"));
        statement.writeField("MMETAT",
                this._dbWriteNumeric(statement.getTransaction(), getEtat(), "etatReleve (MMETAT)"));
        statement.writeField("MMINTE",
                this._dbWriteNumeric(statement.getTransaction(), getInterets(), "interet (MMINTE)"));
        statement.writeField("MMDREC",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateReception(), "dateReception (MMDREC)"));
        statement.writeField("MMANTX",
                this._dbWriteNumeric(statement.getTransaction(), getAnneeReference(), "anneeReference (MMANTX)"));
    }

    public void addCotisation(AFApercuReleveLineFacturation newCotisation) throws Exception {
        // si la cotisation se trouve déjà dans la liste (même assurance, même plan), il s'agit d'un historique
        // il ne faut pas en tenir compte, voire l'adapter avec les bonne valeur

        boolean found = false;
        for (AFApercuReleveLineFacturation line : cotisationList) {
            if (line.getAssuranceId().equals(newCotisation.getAssuranceId())
                    && line.getIdPlan().equals(newCotisation.getIdPlan())) {

                /*
                 * oca - 7.01.2011 - PO4917 Adapte les date si nécessaire, afin d'avoir une cotisation qui couvre la
                 * période "globale" utile pour les relevés finaux , de manière à pouvoir annualiser corrctement la
                 * masse avant le calcul des taux variables (voir égalament findAndSetTauxVariable(), dans la partie
                 * concerant les bouclements d'accomptes)
                 * 
                 * On garde une seul coti par assurance / plan, mais on adapte les dates de manière à garder la date la
                 * plus petite de toutes et la date la plus grande de toutes.
                 */
                if (BSessionUtil.compareDateFirstLower(getSession(), newCotisation.getDebutPeriode(),
                        line.getDebutPeriode())) {
                    line.setDebutPeriode(newCotisation.getDebutPeriode());
                }
                if (BSessionUtil.compareDateFirstGreater(getSession(), newCotisation.getFinPeriode(),
                        line.getFinPeriode())) {
                    line.setFinPeriode(newCotisation.getFinPeriode());
                }

                found = true;
                break;

            }
        }
        if (!found) {
            cotisationList.add(newCotisation);
        }
    }

    public void addCotisation(int i, AFApercuReleveLineFacturation newCotisation) {
        cotisationList.add(i, newCotisation);
    }

    /**
     * Ajouter le Relevé dans la facturation (MUSCA).
     * 
     * @param transaction
     * @throws Exception
     */
    private void addReleveInFacturation(BTransaction transaction) throws Exception {
        // année facturation
        String anneeFacturation = "0";
        if (getDateFin().length() > 6) {
            anneeFacturation = getDateFin().substring(6);
        }
        // recherche affiliation
        // AFAffiliation aff = getAffiliation();
        // dernier plan traité
        String libellePlan = "";
        // dernier genre traité
        String lastRoleCoti = "";
        // en-tête facture
        FAEnteteFacture enteteReleve = null;
        String passage = "FAUX";

        for (int i = 0; i < cotisationList.size(); i++) {
            AFApercuReleveLineFacturation releveLine = cotisationList.get(i);
            String roleCoti = CodeSystem.ROLE_AFFILIE;
            if (CodeSystem.GENRE_ASS_PERSONNEL.equals(releveLine.getGenreAssurance())) {
                // assurance personnelle
                roleCoti = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(getSession().getApplication());
            }
            if (CodeSystem.GENRE_ASS_PARITAIRE.equals(releveLine.getGenreAssurance())) {
                // assurance personnelle
                roleCoti = CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(getSession().getApplication());
            }

            if ((releveLine.getMontantCalculer() != 0)
                    || ((CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE.equals(type)) || (CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA
                            .equals(type)))) {
                FAAfact facturationLine = new FAAfact();
                facturationLine.setISession(getSessionMusca(getSession()));
                facturationLine.setReferenceExterne(getIdReleve() + "/" + releveLine.getCotisationId());
                if ((enteteReleve == null) || !libellePlan.equals(releveLine.getLibellePlan())
                        || !lastRoleCoti.equals(roleCoti)) {
                    enteteReleve = getEntetefacture(transaction, roleCoti, releveLine);
                    libellePlan = releveLine.getLibellePlan();
                    lastRoleCoti = roleCoti;
                }
                facturationLine.setIdEnteteFacture(enteteReleve.getIdEntete());
                facturationLine.setIdPassage(getIdPassage());
                facturationLine.setIdModuleFacturation(releveLine.getIdModFacturation());
                facturationLine.setNonImprimable(Boolean.FALSE);
                facturationLine.setNonComptabilisable(Boolean.FALSE);
                facturationLine.setAQuittancer(Boolean.FALSE);
                if (releveLine.getNatureRubrique().equals(CodeSystem.NAT_RUBRIQUE_COTI_AVEC_MASSE)
                        || releveLine.getNatureRubrique().equals(CodeSystem.NAT_RUBRIQUE_COTI_SANS_MASSE)) {
                    facturationLine.setAnneeCotisation(anneeFacturation);
                } else {
                    facturationLine.setAnneeCotisation("");
                }
                // libellé pas défaut si pas déjà renseigné
                if (JadeStringUtil.isEmpty(releveLine.getLibelle())) {
                    String langue = releveLine.getLangue();
                    if (langue.equalsIgnoreCase(CodeSystem.LANGUE_FRANCAIS)) {
                        facturationLine.setLibelle(releveLine.getAssuranceLibelleFr());
                    } else if (langue.equalsIgnoreCase(CodeSystem.LANGUE_ALLEMAND)) {
                        facturationLine.setLibelle(releveLine.getAssuranceLibelleAl());
                    } else if (langue.equalsIgnoreCase(CodeSystem.LANGUE_ITALIEN)) {
                        facturationLine.setLibelle(releveLine.getAssuranceLibelleIt());
                    }
                } else {
                    facturationLine.setLibelle(releveLine.getLibelle());
                }
                facturationLine.setIdRubrique(releveLine.getAssuranceRubriqueId());

                // DGI décompte 14
                if (((CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE.equals(type)) || (CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA
                        .equals(type))) || (CodeSystem.TYPE_RELEVE_TAXATION_OFFICE.equals(type))) {
                    // if(false) {
                    facturationLine.setIdTypeAfact(FAAfact.CS_AFACT_TABLEAU);
                    CACompteur cnt = getCompteur(releveLine.getAssuranceRubriqueId());
                    if (releveLine.getTaux() != 100) {
                        facturationLine.setMasseInitiale(Double.toString(releveLine.getMasse()));
                        facturationLine.setTauxInitial(Double.toString(releveLine.getTaux()));
                        if (cnt != null) {
                            facturationLine.setMasseDejaFacturee(cnt.getCumulMasse());
                        } else {
                            facturationLine.setMasseDejaFacturee("0");
                        }
                        facturationLine.setTauxDejaFacture(Double.toString(releveLine.getTaux()));
                    }
                    facturationLine.setMontantInitial(Double.toString(releveLine.getMontantCalculer()));
                    if (cnt != null) {
                        facturationLine.setMontantDejaFacture(cnt.getCumulCotisation());
                    } else {
                        facturationLine.setMontantDejaFacture("0");
                    }
                    // désactiver controle à deux balles
                    facturationLine.setControleDeuxFrancs(false);
                } else {
                    facturationLine.setIdTypeAfact(FAAfact.CS_AFACT_STANDART);
                    if (releveLine.getTaux() != 100) {
                        // ne pas renseigner de masse et de taux si taux à 100%
                        facturationLine.setMasseFacture(Double.toString(releveLine.getMasse()));
                        facturationLine.setTauxFacture(Double.toString(releveLine.getTaux()));
                    }
                    facturationLine.setMontantFacture(Double.toString(releveLine.getMontantCalculer()));
                }

                if (!releveLine.isPeriodeFactuAnnuelle()) {
                    // on prend la période du relevé saisie par l'utilisateur
                    // (PO2572)
                    facturationLine.setDebutPeriode(getDateDebut());
                    facturationLine.setFinPeriode(getDateFin());
                }

                // ajout de la caisse principale
                // facturationLine.setNumCaisse(AFAffiliationUtil.getIdCaissePrincipale(aff));
                String caisseMetier = releveLine.getIdCaisse();
                if (caisseMetier == null) {
                    caisseMetier = "";
                }
                facturationLine.setNumCaisse(caisseMetier);
                facturationLine.setIdModuleFacturation(globaz.musca.external.ServicesFacturation
                        .getIdModFacturationByType(getSession(), transaction, FAModuleFacturation.CS_MODULE_RELEVE));
                facturationLine.setTypeModule(FAModuleFacturation.CS_MODULE_RELEVE);
                // renseigner si taux caché
                facturationLine.setAffichtaux(releveLine.getAfficheTaux());
                facturationLine.add(transaction);

                // ajouter le taux moyen
                if ((CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE.equals(type) || (CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA
                        .equals(type)))
                        && "true".equals(getSession().getApplication().getProperty(
                                AFApplication.PROPERTY_IS_TAUX_PAR_PALIER))
                        && CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE.equals(releveLine.getTauxGenre())) {
                    AFCalculAssurance.calculTauxMoyen(getSession(), getAffiliation().getAffiliationId(), releveLine
                            .getAssuranceId(), Double.toString(releveLine.getMasse()), releveLine.getDebutPeriode()
                            .substring(6));
                }

            }

            // Création pour V.5 d'une amende pour taxation d'office
            if ((CodeSystem.TYPE_RELEVE_TAXATION_OFFICE.equals(getType())) && passage.equals("FAUX")) {

                FAAfact facturationLine = new FAAfact();
                facturationLine.setISession(getSessionMusca(getSession()));
                facturationLine.setReferenceExterne(getIdReleve() + "/" + releveLine.getCotisationId());
                if ((enteteReleve == null) || !libellePlan.equals(releveLine.getLibellePlan())
                        || !lastRoleCoti.equals(roleCoti)) {
                    enteteReleve = getEntetefacture(transaction, roleCoti, releveLine);
                    libellePlan = releveLine.getLibellePlan();
                    lastRoleCoti = roleCoti;
                }
                facturationLine.setIdEnteteFacture(enteteReleve.getIdEntete());
                facturationLine.setIdPassage(getIdPassage());
                facturationLine.setIdModuleFacturation(releveLine.getIdModFacturation());
                facturationLine.setNonImprimable(Boolean.FALSE);
                facturationLine.setNonComptabilisable(Boolean.FALSE);
                facturationLine.setAQuittancer(Boolean.FALSE);

                AFAssuranceManager assuranceM = new AFAssuranceManager();

                assuranceM.setForTypeAssurance(CodeSystem.TYPE_ASS_TAXE_AMENDE);
                assuranceM.setSession(getSession());
                assuranceM.find();
                if (assuranceM.size() != 0) {
                    AFAssurance assurance = (AFAssurance) assuranceM.get(i);

                    AFTauxAssuranceManager tauxassuranceM = new AFTauxAssuranceManager();

                    tauxassuranceM.setForIdAssurance(assurance.getAssuranceId());
                    tauxassuranceM.setSession(getSession());
                    tauxassuranceM.find();

                    AFTauxAssurance tauxassurance = (AFTauxAssurance) tauxassuranceM.get(i);

                    // libellé pas défaut si pas déjà renseigné

                    String langue = releveLine.getLangue();
                    if (langue.equalsIgnoreCase(CodeSystem.LANGUE_FRANCAIS)) {
                        facturationLine.setLibelle(assurance.getAssuranceLibelleFr());
                    } else if (langue.equalsIgnoreCase(CodeSystem.LANGUE_ALLEMAND)) {
                        facturationLine.setLibelle(assurance.getAssuranceLibelleAl());
                    } else if (langue.equalsIgnoreCase(CodeSystem.LANGUE_ITALIEN)) {
                        facturationLine.setLibelle(assurance.getAssuranceLibelleIt());
                    }

                    facturationLine.setIdRubrique(assurance.getRubriqueId());

                    facturationLine.setDebutPeriode(getDateDebut());
                    facturationLine.setFinPeriode(getDateFin());
                    facturationLine.setMontantFacture(tauxassurance.getValeurTotal());

                    facturationLine.setIdTypeAfact(FAAfact.CS_AFACT_STANDART);
                    facturationLine
                            .setIdModuleFacturation(globaz.musca.external.ServicesFacturation
                                    .getIdModFacturationByType(getSession(), transaction,
                                            FAModuleFacturation.CS_MODULE_RELEVE));

                    facturationLine.add(transaction);
                    passage = "VRAI";
                }
            }
        }
        if (CodeSystem.TYPE_RELEVE_TAXATION_OFFICE.equals(getType())) {

            if (AFUtil.isNouveauControleEmployeur(getSession())) {

                CEControleEmployeurManager managerCE = new CEControleEmployeurManager();
                managerCE.setForAffiliationId(getAffiliation().getAffiliationId());
                managerCE.setForAnnee(JACalendar.todayJJsMMsAAAA().substring(6));
                managerCE.setSession(getSession());
                managerCE.find(BManager.SIZE_USEDEFAULT);

                if (managerCE.size() == 0) {
                    CEControleEmployeur CE = new CEControleEmployeur();
                    CE.setAffiliationId(getAffiliation().getAffiliationId());
                    CE.setNumAffilie(getAffiliation().getAffilieNumero());
                    CE.setDatePrevue(JACalendar.todayJJsMMsAAAA());
                    CE.setDateDebutControle(getDateDebut());
                    CE.setDateFinControle(getDateFin());

                    CE.setGenreControle(CodeSystem.CONTROLE_EMPLOYEUR_OBLIGATOIRE);
                    CE.setSession(getSession());
                    CE.add();
                }

            } else {

                AFControleEmployeurManager managerCE = new AFControleEmployeurManager();
                managerCE.setForAffiliationId(getAffiliation().getAffiliationId());
                managerCE.setForAnnee(JACalendar.todayJJsMMsAAAA().substring(6));
                managerCE.setSession(getSession());
                managerCE.find(BManager.SIZE_USEDEFAULT);

                if (managerCE.size() == 0) {
                    AFControleEmployeur CE = new AFControleEmployeur();
                    CE.setAffiliationId(getAffiliation().getAffiliationId());
                    CE.setNumAffilie(getAffiliation().getAffilieNumero());
                    CE.setDatePrevue(JACalendar.todayJJsMMsAAAA());
                    CE.setDateDebutControle(getDateDebut());
                    // CE.setAnnee(getDateFin().substring(6));
                    CE.setDateFinControle(getDateFin());

                    CE.setGenreControle(CodeSystem.CONTROLE_EMPLOYEUR_OBLIGATOIRE);
                    CE.setSession(getSession());
                    CE.add();
                }
            }
        }

    }

    /**
     * Ajoute les valeurs des montants saisis du Relevé dans la DB.
     * 
     * @param transaction
     * @throws Exception
     */
    public void addReleveMontant(BTransaction transaction) throws Exception {
        for (int i = 0; i < cotisationList.size(); i++) {
            AFApercuReleveLineFacturation releveLine = cotisationList.get(i);

            AFApercuReleveMontant montant = new AFApercuReleveMontant();
            montant.setSession(getSession());
            montant.setIdReleve(getIdReleve());
            montant.setAssuranceId(releveLine.getAssuranceId());
            montant.setDateDebut(releveLine.getDebutPeriode());
            montant.setMasse(releveLine.getMasseString(false));
            montant.setMontantCalculer(releveLine.getMontantCalculerString());
            montant.setCotisationId(releveLine.getCotisationId());
            montant.add(transaction);
        }
    }

    /**
     * Ajoute les montants du Relevé dans la DB avec les valeurs sauvegardées dans la facturation (MUSCA).
     * 
     * @param transaction
     * @throws Exception
     */
    private void addReleveMontantWithReleveInFacturation(BTransaction transaction) throws Exception {

        for (int i = 0; i < cotisationList.size(); i++) {
            AFApercuReleveLineFacturation releveLine = cotisationList.get(i);

            AFApercuReleveMontant montant = new AFApercuReleveMontant();
            montant.setSession(getSession());
            montant.setIdReleve(getIdReleve());

            // recherche de l'afact correspondant
            FAAfact afact = new FAAfact();
            afact.setISession(getSessionMusca(getSession()));
            afact.setAlternateKey(FAAfact.AK_INTERETSMORATOIRES);
            afact.setReferenceExterne(getIdReleve() + "/" + releveLine.getCotisationId());
            afact.retrieve(transaction);

            if (!afact.isNew()) {
                montant.setAssuranceId(releveLine.getAssuranceId());
                if (!JadeStringUtil.isEmpty(afact.getDebutPeriode())) {
                    montant.setDateDebut(afact.getDebutPeriode());
                } else {
                    montant.setDateDebut("01.01." + afact.getAnneeCotisation());
                }
                if ((CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE.equals(type))
                        || (CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA.equals(type))) {
                    montant.setMasse(afact.getMasseInitiale());
                    montant.setMontantCalculer(afact.getMontantInitial());
                } else {
                    montant.setMasse(afact.getMasseFacture());
                    montant.setMontantCalculer(afact.getMontantFacture());
                }
                montant.setCotisationId(releveLine.getCotisationId());
                montant.add(transaction);
            }
        }
    }

    protected String annualiserMasse(AFApercuReleveLineFacturation line, BigDecimal masseAnnuel) throws Exception,
            JAException {
        String massePourRechercheTaux = masseAnnuel.toString();

        boolean wantAnnualisation = ((AFApplication) getSession().getApplication()).wantAnnualiserMasse();
        // Anualiser si présiode incomplète, si la caisse le désire et seulement pour les 13 et 14
        if (wantAnnualisation
                && ((JACalendar.getMonth(line.getDebutPeriode()) != 1) || (JACalendar.getMonth(line.getFinPeriode()) != 12))) {
            if (CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE.equals(getType())
                    || CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA.equals(getType())
                    || CodeSystem.TYPE_RELEVE_TAXATION_OFFICE.equals(getType())
                    || CodeSystem.TYPE_RELEVE_DECOMP_FINAL.equals(getType())) {
                massePourRechercheTaux = CPToolBox.annualisationRevenu(line.getDebutPeriode(), line.getFinPeriode(),
                        "", masseAnnuel.toString());
            }
        }
        return massePourRechercheTaux;
    }

    public boolean autoriseDecompte13(BSession session) throws Exception {

        ArrayList<?> array = DSUtil.getCSBloquage13((BSession) getSessionDS(getSession()));
        // Si array null, on autorise car propriété absente
        if (array == null) {
            return true;
        }
        return !array.contains(getAffiliation().getDeclarationSalaire());
    }

    public void calculeCotisation() throws Exception {
        this.calculeCotisation(getApercuReleveLineFacturationList());
    }

    /**
     * Calcule et met à jour les montant dans la liste des cotisations du relevé.
     * 
     * @param viewBean
     * @param request
     */
    public void calculeCotisation(List<?> newLineList) throws Exception {

        String theIdCompteAnnexe = "";
        try {
            theIdCompteAnnexe = getCompteAnnexe().getIdCompteAnnexe();
        } catch (Exception e) {
            theIdCompteAnnexe = "";
        }

        if (CodeSystem.ETATS_RELEVE_SAISIE.equals(getEtat())) {

            double masseGeneral = 0.0;
            FWCurrency total = new FWCurrency(0.0);
            // Mettre à jour le Relevé - Etape 1 -Assurance sans référence sur
            // d'autre assurance
            for (int i = 0; i < cotisationList.size(); i++) {
                AFApercuReleveLineFacturation line = cotisationList.get(i);
                AFApercuReleveLineFacturation newLine;
                if (newLineList == null) {
                    newLine = line.cloneLine();
                    line.setMasse(0);
                } else {
                    newLine = (AFApercuReleveLineFacturation) newLineList.get(i);
                }
                // *******************************************************************
                // Assurance Paritaire
                // *******************************************************************
                if (line.getGenreAssurance().equals(CodeSystem.GENRE_ASS_PARITAIRE)) {

                    if (JadeStringUtil.isIntegerEmpty(line.getAssuranceReferenceId())) {

                        AFAssurance assu = new AFAssurance();
                        assu.setAssuranceId(line.getAssuranceId());
                        assu.setSession(getSession());
                        assu.retrieve();

                        AFCotisation coti = new AFCotisation();
                        coti.setCotisationId(line.getCotisationId());
                        coti.setSession(getSession());
                        coti.retrieve();

                        if (newLine.isMasseVide() && !CodeSystem.TYPE_CALCUL_COTISATION.equals(line.getTypeCalcul())
                                && !line.getTauxGenre().equals(CodeSystem.GEN_VALEUR_ASS_MONTANT)) {

                            // La masse n'est pas reportée automatiquement pour les assurances de type AC2
                            if (!CodeSystem.TYPE_ASS_COTISATION_AC2.equals(assu.getTypeAssurance())) {
                                newLine.setMasse(masseGeneral);
                                if (coti.getDateDebut().equals(coti.getDateFin())) {
                                    newLine.setMasse(0.0);
                                }
                            }
                        } else {
                            if (newLine.getMasse() != 0.0) {
                                masseGeneral = newLine.getMasse();
                            }
                        }

                        // pour une assurance de type TYPE_ASS_FFPP_MASSE, il est important de toujours refaire le
                        // calcul
                        // afin de tenir compte du plancher

                        // si le calcul n'est pas refait, les cas suivants posent problèmes :
                        // cas 1 : une exception survient lors de la 1ere application du plancher --> email d'erreurs et
                        // plancher non appliqué
                        // si sans changer la masse de l'assurance on valide ou facture à nouveau le relevé --> pas
                        // d'email d'erreurs et plancher non appliqué car le calcul n'est pas refait

                        // cas 2 : saisie et calcul des masses avec un type de relevé pour lequel le plancher n'est pas
                        // appliqué (par exemple : un relevé de type périodique)
                        // si on revient en arrière (<<), sélectionne un type de relevé pour lequel le plancher est
                        // appliqué (par exemple : un relevé de type décompte final) et
                        // sans changer les masses on valide ou facture le relevé --> pas d'email d'erreurs et plancher
                        // non appliqué car le calcul n'est pas refait
                        if ((newLine.getMasse() == line.getMasse())
                                && !CodeSystem.TYPE_ASS_FFPP_MASSE.equalsIgnoreCase(assu.getTypeAssurance())) {
                            // *****************************************************************
                            // Si la masse n'a pas changé, on mets à jour le
                            // MontantCalculer
                            // *****************************************************************
                            line.setMontantCalculer(newLine.getMontantCalculer());
                        } else {
                            // *****************************************************************
                            // Si la masse a changé, on recalcule
                            // MontantCalculer avec la nouvelle masse
                            // *****************************************************************
                            line.setMasse(newLine.getMasse());

                            if (CodeSystem.TYPE_ASS_FFPP_MASSE.equalsIgnoreCase(assu.getTypeAssurance())
                                    && (CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE.equalsIgnoreCase(getType()) || CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA
                                            .equalsIgnoreCase(getType()))) {
                                try {
                                    line.setMasse(AFUtil.plancheMasse(new FWCurrency(line.getMasse()),
                                            line.getAssuranceId(), getDateDebut(), getSession()).doubleValue());
                                } catch (Exception e) {
                                    JadeLogger.error(AFApercuReleve.class.getName(), e.toString());
                                    JadeSmtpClient.getInstance().sendMail(
                                            getSession().getUserEMail(),
                                            FWMessageFormat.format(
                                                    getSession().getLabel("ERREUR_PLANCHE_MASSE_EMAIL_SUBJECT"),
                                                    line.getAssuranceLibelle(getSession())),
                                            FWMessageFormat.format(
                                                    getSession().getLabel("ERREUR_PLANCHE_MASSE_EMAIL_BODY"),
                                                    getAffilieNumero(), line.getAssuranceLibelle(getSession()))
                                                    + " ("
                                                    + e.toString() + ")", null);
                                }
                            }

                            line.setMasse(AFUtil.plafonneMasse(line.getMasse(), getType(), line.getAssuranceId(),
                                    getDateDebut(), getSession(), theIdCompteAnnexe));
                            newLine.setMasse(line.getMasse());
                            if (line.getTauxGenre().equals(CodeSystem.GEN_VALEUR_ASS_TAUX)) {

                                FWCurrency montantCal = new FWCurrency((newLine.getMasse() * line.getTaux())
                                        / line.getFraction());
                                line.setMontantCalculer(montantCal.toString());
                            } else if (line.getTauxGenre().equals(CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE)) {

                                findAndSetTauxVariable(line, String.valueOf(newLine.getMasse()), masseGeneral);

                                if ("true".equals(getSession().getApplication().getProperty(
                                        AFApplication.PROPERTY_IS_TAUX_PAR_PALIER))) {

                                } else {
                                    FWCurrency montantCal = new FWCurrency((newLine.getMasse() * line.getTaux())
                                            / line.getFraction());
                                    line.setMontantCalculer(montantCal.toString());
                                }
                            } else if (line.getTauxGenre().equals(CodeSystem.GEN_VALEUR_ASS_MONTANT)) {
                                line.setMontantCalculer(line.getMasse());

                            }

                        }

                        if ((CodeSystem.TYPE_RELEVE_RECTIF.equals(getType()))
                                || (CodeSystem.TYPE_RELEVE_CONTROL_EMPL.equals(getType()))) {
                            CACompteur cnt = getCompteur(line.getAssuranceRubriqueId());

                            FWCurrency montant = new FWCurrency(line.getMontantCalculer());
                            if (cnt != null) {
                                montant.add(cnt.getCumulCotisation());
                            }

                            // if (montant.isNegative()){
                            montantNegatif = montant.isNegative();

                            // }
                        }
                        total.add(line.getMontantCalculer());

                    } // *******************************************************************
                      // Assurance Personnelle
                      // *******************************************************************
                } else {
                    if (newLine.getMasse() == line.getMasse()) {
                        // **************************************************************
                        // Si la masse n'a pas changé, on mets à jour le
                        // MontantCalculer
                        // **************************************************************
                        line.setMontantCalculer(newLine.getMontantCalculer());
                    } else {
                        // *****************************************************************
                        // Si la masse a changé,
                        // on mets à jour la masse et le MontantCalculer
                        // *****************************************************************
                        line.setMasse(newLine.getMasse());
                        line.setMasse(AFUtil.plafonneMasse(line.getMasse(), getType(), line.getAssuranceId(),
                                getDateDebut(), getSession(), theIdCompteAnnexe));
                        newLine.setMasse(line.getMasse());
                        line.setMontantCalculer(newLine.getMasse());
                    }
                    total.add(line.getMontantCalculer());
                }

            } // end for
              // Mettre à jour le Relevé - Etape 2 - Assurance référencant une
              // autre assurance
            for (int i = 0; i < cotisationList.size(); i++) {
                AFApercuReleveLineFacturation line = cotisationList.get(i);

                // *******************************************************************
                // Assurance Paritaire
                // *******************************************************************
                if (line.getGenreAssurance().equals(CodeSystem.GENRE_ASS_PARITAIRE)) {

                    if (!JadeStringUtil.isIntegerEmpty(line.getAssuranceReferenceId())) { // Execute
                        // after
                        // this
                        // loop
                        AFApercuReleveLineFacturation newLine;
                        if (newLineList == null) {
                            newLine = line.cloneLine();
                            line.setMasse(0);
                        } else {
                            newLine = (AFApercuReleveLineFacturation) newLineList.get(i);
                        }
                        for (int j = 0; j < cotisationList.size(); j++) {
                            AFApercuReleveLineFacturation tmpLine = cotisationList.get(j);
                            if (line.getAssuranceReferenceId().equals(tmpLine.getAssuranceId())) {
                                newLine.setMasse(tmpLine.getMontantCalculer());
                                newLine.setMasse(AFUtil.plafonneMasse(newLine.getMasse(), getType(),
                                        newLine.getAssuranceId(), getDateDebut(), getSession(), theIdCompteAnnexe));
                                break;
                            }
                        }

                        if (line.getTauxGenre().equals(CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE)) {
                            findAndSetTauxVariable(line, String.valueOf(newLine.getMasse()), masseGeneral);
                            newLine.setMontantCalculer(JANumberFormatter.round(
                                    ((newLine.getMasse() * line.getTaux()) / line.getFraction()), 0.05, 2,
                                    JANumberFormatter.NEAR));
                        } else if (line.getTauxGenre().equals(CodeSystem.GEN_VALEUR_ASS_MONTANT)) {
                            line.setMontantCalculer(masseGeneral);
                        }

                        if (newLine.getMasse() == line.getMasse()) {
                            // *****************************************************************
                            // Si la masse n'a pas changé, on mets à jour le
                            // MontantCalculer
                            // *****************************************************************
                            line.setMontantCalculer(newLine.getMontantCalculer());
                        } else {

                            // *****************************************************************
                            // Si la masse a changé, on recalcule
                            // MontantCalculer avec la nouvelle masse
                            // *****************************************************************
                            line.setMasse(newLine.getMasse());
                            line.setMasse(AFUtil.plafonneMasse(line.getMasse(), getType(), line.getAssuranceId(),
                                    getDateDebut(), getSession(), theIdCompteAnnexe));
                            newLine.setMasse(line.getMasse());
                            if (line.getTauxGenre().equals(CodeSystem.GEN_VALEUR_ASS_TAUX)) {

                                FWCurrency montantCal = new FWCurrency((newLine.getMasse() * line.getTaux())
                                        / line.getFraction());
                                line.setMontantCalculer(montantCal.toString());
                            }

                        }
                        total.add(line.getMontantCalculer());

                    }
                }
            } // end for
            setTotalCalculer(total.toString());
            firstCalculation(false);
        }
    }

    /**
     * Control le numéro d'affilié.
     * 
     * @return true - si le numéro est valide
     * @throws Exception
     */
    public boolean checkAffilieNumero() throws Exception {
        boolean validationOK = true;
        AFApplication affApp = (AFApplication) GlobazServer.getCurrentSystem().getApplication(
                AFApplication.DEFAULT_APPLICATION_NAOS);
        IFormatData affilieFormater = affApp.getAffileFormater();
        if (affilieFormater != null) {
            try {
                affilieFormater.check(getAffilieNumero());
                setAffilieNumero(affilieFormater.format(getAffilieNumero()));
            } catch (Exception e) {
                validationOK = false;
            }
        }
        return validationOK;
    }

    public void clearCotisationList() {
        cotisationList.clear();
    }

    /**
     * Supprimer le Relevé de la facturation (MUSCA).
     * 
     * @param transaction
     * @throws Exception
     */
    private void deleteReleveInFacturation(BTransaction transaction) throws Exception {
        // pour toutes les lignes du relevé, effacer les afacts correspondants
        ArrayList<String> listEntete = new ArrayList<String>();
        for (int i = 0; i < cotisationList.size(); i++) {
            AFApercuReleveLineFacturation releveLine = cotisationList.get(i);

            // recherche de l'afact correspondant
            FAAfact afact = new FAAfact();
            afact.setISession(getSessionMusca(getSession()));
            afact.setAlternateKey(FAAfact.AK_INTERETSMORATOIRES);
            afact.setReferenceExterne(getIdReleve() + "/" + releveLine.getCotisationId());
            afact.retrieve(transaction);

            if (!afact.isNew()) {
                // mémoriser l'id de l'entête
                if (!listEntete.contains(afact.getIdEnteteFacture())) {
                    listEntete.add(afact.getIdEnteteFacture());
                }
                afact.delete(transaction);
            }
        }
        // tenter d'effacer les entêtes
        if (!transaction.hasErrors() && !transaction.hasWarnings()) {
            for (int i = 0; i < listEntete.size(); i++) {
                FAEnteteFacture entete = new FAEnteteFacture();
                entete.setISession(getSessionMusca(getSession()));
                entete.setIdEntete(listEntete.get(i));
                entete.retrieve(transaction);
                if (!entete.isNew()) {
                    entete.delete(transaction);
                    // si encore des affacts, la transaction est erreur, la
                    // réinitialisé avant de continuer
                    if (transaction.hasErrors()) {
                        transaction.clearErrorBuffer();
                    }
                }
            }
        }
    }

    /**
     * Supprimer les montants du Relevé de la DB.
     * 
     * @param transaction
     * @throws Exception
     */
    private void deleteReleveMontant(BTransaction transaction) throws Exception {
        AFApercuReleveMontantManager manager = new AFApercuReleveMontantManager();
        manager.setSession(getSession());
        manager.setForIdReleve(getIdReleve());
        manager.find(transaction);

        for (int i = 0; i < manager.size(); i++) {
            AFApercuReleveMontant montant = (AFApercuReleveMontant) manager.get(i);
            // Pour la correction du PO 5740
            // if (!JadeStringUtil.isDecimalEmpty(montant.getMontantCalculer())) {
            montant.delete(transaction);
            // }
        }
    }

    /**
     * Attribue le taux et calcul le montant de la cotisation en fonction de la période
     * 
     * @param line
     *            la ligne du relevé
     * @param masse
     *            la masse de la période à facturer
     * @param tauxVarUtil
     *            l'utilitaire de calcul du taux variable
     * @throws Exception
     *             si une exception survient
     */
    private void findAndSetTauxVariable(AFApercuReleveLineFacturation line, String masse, double masseAnnuelTaux)
            throws Exception {
        AFTauxAssurance taux = null;
        // chargement de l'utilitaire de calcul des taux variables
        AFTauxVariableUtil tauxVarUtil = AFTauxVariableUtil.getInstance(line.getAssuranceId());
        // System.out.println(tauxVarUtil.toString(getSession(),line.getDebutPeriode()));
        AFApplication affApp = (AFApplication) GlobazServer.getCurrentSystem().getApplication(
                AFApplication.DEFAULT_APPLICATION_NAOS);
        if ("true".equals(affApp.getProperty(AFApplication.PROPERTY_IS_TAUX_PAR_PALIER))
                && !CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE.equals(getType())
                && !CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA.equals(getType())
                && !CodeSystem.TYPE_RELEVE_DECOMP_FINAL.equals(getType())) {
            // si taux var par palier et différent de bouclement acomptes ou
            // final -> taux moyen
            // TODO, à voir si cette partie est utilisé car pour un taux moyen,
            // le taux est déjà défini (génération de la liste et non pas au
            // calcul)
            taux = tauxVarUtil.getTauxMoyen(getSession(), getAffiliation().getAffiliationId(), line.getDebutPeriode());
            // attribution du taux
            double tauxVal = Double.parseDouble(JANumberFormatter.deQuote(taux.getValeurTotal()));
            line.setTaux(tauxVal);
            double tauxFrac = Double.parseDouble(JANumberFormatter.deQuote(taux.getFraction()));
            line.setFraction(tauxFrac);
            // calcul du montant de la cotisation
            FWCurrency montantCal = new FWCurrency((line.getMasse() * tauxVal) / tauxFrac);
            line.setMontantCalculer(montantCal.toString());
            // cacher le taux si autre que 1er rang
            if (!taux.isAffichageTaux()) {
                line.setAfficheTaux(new Boolean(false));
            }

        } else {
            BigDecimal nbrMois = JADate.getMonth(line.getFinPeriode())
                    .subtract(JADate.getMonth(line.getDebutPeriode())).add(new BigDecimal("1"));
            BigDecimal masseAnnuel = null;
            BigDecimal masseAnnuelPourTaux = null;
            // calcul de la masse annuelle sur la base de la masse du relevé
            AFApplication appAf = (AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS);
            if (masseAnnuelTaux != 0.0) {
                masseAnnuel = new BigDecimal(JANumberFormatter.deQuote(masse));
                masseAnnuelPourTaux = new BigDecimal(masseAnnuelTaux);

                // Si le relevé n'est pas sur l'année, on regarde si on doit annualiser
                if (nbrMois.intValue() != 12) {
                    // Pour annualiser, on doit avoir soit le type de relevé périodique ou complément
                    // Ou la propriété "annualiseMasse" à true
                    if (CodeSystem.TYPE_RELEVE_PERIODIQUE.equals(getType())
                            || CodeSystem.TYPE_RELEVE_COMPLEMENT.equals(getType()) || appAf.wantAnnualiserMasse()) {

                        masseAnnuelPourTaux = masseAnnuelPourTaux.multiply(new BigDecimal("12"));
                        masseAnnuelPourTaux = masseAnnuelPourTaux.divide(nbrMois, BigDecimal.ROUND_UP);
                    }
                }
            } else {
                masseAnnuel = new BigDecimal(JANumberFormatter.deQuote(masse));

                if (Boolean.FALSE.equals(getPrevisionAcompteEBU())) {
                    boolean wantAnnualisation = appAf.wantAnnualiserMasse();
                    if (wantAnnualisation && (nbrMois.intValue() != 12)) {
                        masseAnnuel = masseAnnuel.multiply(new BigDecimal("12"));
                        masseAnnuel = masseAnnuel.divide(nbrMois, BigDecimal.ROUND_UP);

                    }
                }
            }

            // }
            // si relevé périodique ou décompte final et que la cotisation
            // minimale est demandée, le prendre en compte
            boolean cotiMinim = false;
            if (CodeSystem.TYPE_RELEVE_PERIODIQUE.equals(getType())
                    || CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE.equals(getType())
                    || CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA.equals(getType())
                    || CodeSystem.TYPE_RELEVE_DECOMP_FINAL.equals(getType())) {
                if (appAf.isCotisationMinimale()) {
                    cotiMinim = true;
                }
            }
            BigDecimal coti = null;

            if (!CodeSystem.TYPE_TAUX_FORCE.equals(line.getTauxType())) {
                // si taux inexistant (non forcé):
                if (!JadeStringUtil.isIntegerEmpty(line.getMasseAnnuelle())
                        && CodeSystem.TYPE_RELEVE_RECTIF.equals(getType())) {
                    // si masse existent dans plan de l'affilié on utilise
                    // celle-ci
                    taux = tauxVarUtil.getTaux(getSession(), JANumberFormatter.deQuote(line.getMasseAnnuelle()),
                            line.getDebutPeriode());
                } else {
                    if (masseAnnuelTaux != 0.0) {
                        // recherche du taux en fonction de la masse
                        taux = tauxVarUtil.getTaux(getSession(), masseAnnuelPourTaux.abs().toString(),
                                line.getDebutPeriode());
                    } else {
                        // recherche du taux en fonction de la masse
                        taux = tauxVarUtil.getTaux(getSession(), masseAnnuel.abs().toString(), line.getDebutPeriode());
                    }
                }
                // calcul du montant de la cotisation
                FWCurrency cotiFW = new FWCurrency(tauxVarUtil.getMontantCotisation(masseAnnuel.toString(),
                        line.getDebutPeriode(), taux, cotiMinim));
                coti = cotiFW.getBigDecimalValue();
                // attribution du taux
                line.setTaux(Double.parseDouble(JANumberFormatter.deQuote(taux.getValeurTotal())));
                line.setFraction(Double.parseDouble(JANumberFormatter.deQuote(taux.getFraction())));
                // cacher le taux si autre que 1er rang
                if (!taux.isAffichageTaux()) {
                    line.setAfficheTaux(new Boolean(false));
                }
            } else {
                // le taux est donné (forcé)
                FWCurrency cotiFW = new FWCurrency(tauxVarUtil.getMontantCotisation(getSession(),
                        masseAnnuel.toString(), line.getDebutPeriode(), line.getTauxString(), cotiMinim));
                coti = cotiFW.getBigDecimalValue();
            }
            /*
             * Suppression suite à InforomD0017 - Annulaisation de la amsse if (nbrMois.intValue() != 12) { coti =
             * coti.multiply(nbrMois); coti = coti.divide(new BigDecimal("12"), BigDecimal.ROUND_UP); }
             */
            line.setMontantCalculer(coti.toString());

        }
    }

    public void firstCalculation(boolean b) {
        firstCalculation = b;
    }

    /**
     * Generation de la Liste de Cotisation pour ce Relevé en fonction de l'affilié et de la période.
     * 
     * @throws Exception
     */
    public void generationCotisationList() throws Exception {

        String theIdCompteAnnexe = "";
        try {
            theIdCompteAnnexe = getCompteAnnexe().getIdCompteAnnexe();
        } catch (Exception e) {
            theIdCompteAnnexe = "";
        }

        String idModuleFacturationreleve = FAUtil.getIdModuleFacuration(getSession(),
                FAModuleFacturation.CS_MODULE_RELEVE);

        AFProcessFacturationManager manager = new AFProcessFacturationManager();
        manager.setSession(getSession());
        manager.setFromDate(getDateDebut());
        manager.setToDate(getDateFin());
        manager.setForAffilieNumero(getAffilieNumero());
        manager.setForPlanAffiliationId(getPlanAffiliationId());
        if (getType().equals(CodeSystem.TYPE_RELEVE_DECOMP_FINAL)) {
            manager.filtrerPeriodicites(false);
        } else {
            manager.filtrerPeriodicites(true);
        }

        manager.find();

        clearCotisationList();
        FWCurrency totalCotisation = new FWCurrency(0.0);

        AFAgeRente ageRente = new AFAgeRente();
        if (getDateFin().length() == 7) {
            setDateFin(JadeDateUtil.getLastDateOfMonth(getDateFin()));
        }
        ageRente.initDateRente(getSession(), getDateFin());

        int nbMoisFacturer = AFUtil.nbMoisPeriode(getSession(), getDateDebut(), getDateFin());
        String anneeFacturation = getDateFin().substring(6);
        String moisFacturation = getDateFin().substring(3, 5);

        if (getType().equals(CodeSystem.TYPE_RELEVE_PERIODIQUE) || getType().equals(CodeSystem.TYPE_RELEVE_COMPLEMENT)) {
            if (nbMoisFacturer == 1) {
                // Code de facturation
                setIdSousTypeFacture("2270" + moisFacturation);

            } else if (nbMoisFacturer == 3) {

                String noTrimestre = null;
                // Code de facturation
                if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.MARS_TRIMESTRE_1)) {
                    noTrimestre = "1";
                } else if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.JUIN_TRIMESTRE_2)) {
                    noTrimestre = "2";
                } else if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.SEPTEMBRE_TRIMESTRE_3)) {
                    noTrimestre = "3";
                } else if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.ANNUEL)) {
                    noTrimestre = "4";
                }
                // regarder dans tous les if
                setIdSousTypeFacture("22704" + noTrimestre);

            } else if (nbMoisFacturer == 12) {

                // Code de facturation
                setIdSousTypeFacture("227040");
            }
        } else if ((getType().equals(CodeSystem.TYPE_RELEVE_DECOMP_FINAL))
                || (getType().equals(CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA))) {
            setIdSousTypeFacture("227013");
        } else if (getType().equals(CodeSystem.TYPE_RELEVE_CONTROL_EMPL)) {
            setIdSousTypeFacture("227017");
        } else if (getType().equals(CodeSystem.TYPE_RELEVE_RECTIF)) {
            setIdSousTypeFacture("227018");
        } else if (getType().equals(CodeSystem.TYPE_RELEVE_TAXATION_OFFICE)) {
            setIdSousTypeFacture("227030");
        } else if (getType().equals(CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE)) {
            setIdSousTypeFacture("227014");
        } else if (getType().equals(CodeSystem.TYPE_RELEVE_SALAIRE_DIFFERES)) {
            setIdSousTypeFacture("227036");
        }

        // utiliser osiris pour determiner le no de decompte (premier du groupe
        // des trois derniers chiffres)
        // d'abord verifier si on a une transaction car elle est obligatoire:

        String typeSection = "";
        if (idSousTypeFacture.equals("227030")) {
            typeSection = "30";
        } else {
            typeSection = "1";
        }

        if (!getEtat().equals(CodeSystem.ETATS_RELEVE_COMPTABILISER)
                && !getEtat().equals(CodeSystem.ETATS_RELEVE_FACTURER)) {
            // recherche du prochain numéro de facture
            BITransaction transaction = null;
            try {
                transaction = getSessionOsiris(getSession()).newTransaction();
                transaction.openTransaction();
                setIdExterneFacture(CAUtil.creerNumeroSectionUnique(getSessionOsiris(getSession()), transaction,
                        CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(getSession().getApplication()),
                        getAffilieNumero(), typeSection, anneeFacturation, getIdSousTypeFacture()));

            } finally {
                if (transaction != null) {
                    transaction.closeTransaction(); // rollback par defaut...
                }
            }
        }

        for (int i = 0; i < manager.size(); i++) {

            AFProcessFacturationViewBean donneesFacturation = (AFProcessFacturationViewBean) manager.get(i);

            // ************************************************************
            // Calcul de la periode exacte de facturation
            // ************************************************************
            // Debut : le plus grand entre Début Facturation, Début Cotisation
            // Fin : le plus petit entre Fin Facturation, Fin Cotisation,
            // Retraite, Décés

            // Date de début
            String dateEffectiveDebutPeriode = getDateDebut();

            String dateDebutCotisation = AFUtil.getDateBeginingOfMonth(donneesFacturation.getDateDebutCoti());
            if (BSessionUtil.compareDateFirstGreater(getSession(), dateDebutCotisation, dateEffectiveDebutPeriode)) {
                dateEffectiveDebutPeriode = dateDebutCotisation;
            }

            // Date de Fin
            String dateEffectiveFinPeriode = getDateFin();

            String dateFinCotisation = AFUtil.getDateEndOfMonth(donneesFacturation.getDateFinCoti());
            String dateRetraite = ageRente.getDateRente(donneesFacturation.getDateNaissance(),
                    donneesFacturation.getSexe());
            String dateDeces = AFUtil.getDateEndOfMonth(donneesFacturation.getDateDeces());

            if ((!JadeStringUtil.isIntegerEmpty(dateFinCotisation))
                    && BSessionUtil.compareDateFirstLower(getSession(), dateFinCotisation, dateEffectiveFinPeriode)) {

                dateEffectiveFinPeriode = dateFinCotisation;
            }
            if ((!JadeStringUtil.isIntegerEmpty(dateRetraite))
                    && BSessionUtil.compareDateFirstLower(getSession(), dateRetraite, dateEffectiveFinPeriode)) {
                if (donneesFacturation.getTypeAffiliation().equalsIgnoreCase("804004")) {
                    dateEffectiveFinPeriode = dateRetraite;
                }
                // l'indiquer dans les données de facturation afin de renseigne
                // le décompte
                // 19.02.2008, pas nécessaire dans la facturation des relevés
                // donneesFacturation.setIsRentier(new Boolean("true"));
            }
            if ((!JadeStringUtil.isIntegerEmpty(dateDeces))
                    && BSessionUtil.compareDateFirstLower(getSession(), dateDeces, dateEffectiveFinPeriode)) {

                dateEffectiveFinPeriode = dateDeces;
            }

            // ************************************************************
            // Calcul le nombre de mois a Facturer
            // ************************************************************
            nbMoisFacturer = AFUtil.nbMoisPeriode(getSession(), dateEffectiveDebutPeriode, dateEffectiveFinPeriode);

            if (nbMoisFacturer > 0) {

                AFApercuReleveLineFacturation line = new AFApercuReleveLineFacturation();
                line.setLangue(donneesFacturation.getLangue());
                line.setCotisationId(donneesFacturation.getCotisationId());
                line.setAssuranceId(donneesFacturation.getAssuranceId());
                line.setAssuranceReferenceId(donneesFacturation.getIdReferenceAssurance());
                line.setAssuranceLibelleAl(donneesFacturation.getAssuranceLibelleAl());
                line.setAssuranceLibelleFr(donneesFacturation.getAssuranceLibelleFr());
                line.setAssuranceLibelleIt(donneesFacturation.getAssuranceLibelleIt());
                line.setNatureRubrique(donneesFacturation.getNatureRubrique());
                line.setAssuranceRubriqueId(donneesFacturation.getAssuranceRubriqueId());
                line.setIdPlan(donneesFacturation.getIdPlanAffiliation());
                line.setLibellePlan(donneesFacturation.getLibelleFacture());

                String libelleInfoRom280 = "";
                if (!JadeStringUtil.isBlank(donneesFacturation.getLibellePlan())) {
                    libelleInfoRom280 = getSession().getLabel("PLAN_AFFILIATION") + " : "
                            + donneesFacturation.getLibellePlan() + "  ";
                }

                if (!JadeStringUtil.isBlank(donneesFacturation.getLibelleFacture())) {
                    libelleInfoRom280 = libelleInfoRom280 + getSession().getLabel("DECOMPTE") + " : "
                            + donneesFacturation.getLibelleFacture();
                }
                line.setLibelleInfoRom280(libelleInfoRom280);

                line.setDomaineCourrier(donneesFacturation.getDomaineCourrier());
                line.setDomaineRecouvrement(donneesFacturation.getDomaineRecouvrement());
                line.setDomaineRemboursement(donneesFacturation.getDomaineRemboursement());
                line.setBlocageEnvoi(donneesFacturation.getBlocageEnvoi());
                // caisse
                // si une caisse principale existe, utiliser la gestion
                // multi-caisse: prendre la caisse liée à la cotisation ou
                // celle de la caisse principale si vide
                // si la caisse principale n'existe pas, on ne renseigne rien
                // (sans gestion caisse)
                if (!JadeStringUtil.isIntegerEmpty(donneesFacturation.getIdCaissePrincipale())) {
                    if (JadeStringUtil.isIntegerEmpty(donneesFacturation.getIdCaisseAdhesion())) {
                        // aucune adhésion, utiliser la caisse principale
                        line.setIdCaisse(donneesFacturation.getIdCaissePrincipale());
                    } else {
                        line.setIdCaisse(donneesFacturation.getIdCaisseAdhesion());
                    }
                }

                // *******************************
                // PERSONNEL
                // *******************************
                if (donneesFacturation.getGenreAssurance().equals(CodeSystem.GENRE_ASS_PERSONNEL)
                        && getAffiliation().isRelevePersonnel().booleanValue()) {

                    line.setGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
                    line.setTauxGenre("0");
                    line.setTauxType("0");
                    line.setDebutPeriode(dateEffectiveDebutPeriode);
                    line.setFinPeriode(dateEffectiveFinPeriode);

                    line.setIdModFacturation(idModuleFacturationreleve);

                    this.addCotisation(line);
                }
                // *******************************
                // PARITAIRE
                // *******************************
                else if (donneesFacturation.getGenreAssurance().equals(CodeSystem.GENRE_ASS_PARITAIRE)
                        && Boolean.TRUE.equals(donneesFacturation.getDecompte13Releve())) {

                    // Recherche des taux par l'intermédiaire de la cotisation
                    AFCotisation cotisation = new AFCotisation();
                    cotisation.setSession(getSession());
                    cotisation.setCotisationId(donneesFacturation.getCotisationId());
                    cotisation.retrieve();

                    // D0165 - NE prendre que les assurance qui ont "décompte dans relevé 13" de coché

                    AFTauxAssurance tauxAssurance = null;

                    if ((!((CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE.equalsIgnoreCase(getType())) && (CodeSystem.TYPE_ASS_FFPP
                            .equalsIgnoreCase(donneesFacturation.getTypeAssurance()))))
                            && (!((CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA.equalsIgnoreCase(getType())) && (CodeSystem.TYPE_ASS_FFPP
                                    .equalsIgnoreCase(donneesFacturation.getTypeAssurance()))))
                            && (!((CodeSystem.TYPE_RELEVE_DECOMP_FINAL.equalsIgnoreCase(getType())) && (CodeSystem.TYPE_ASS_FFPP
                                    .equalsIgnoreCase(donneesFacturation.getTypeAssurance()))))) {

                        // si bouclement d'acompte ou final, on ne recherche
                        // jamais de taux moyen
                        if (CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE.equals(type)
                                || CodeSystem.TYPE_RELEVE_DECOMP_FINAL.equals(type)
                                || CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA.equals(type)) {
                            tauxAssurance = cotisation.findTauxWithRecalcul(dateEffectiveFinPeriode, "0", true, false,
                                    "");
                        } else if (CodeSystem.TYPE_RELEVE_SALAIRE_DIFFERES.equals(type)) {
                            tauxAssurance = cotisation.findTaux("31.12." + getAnneeReference(),
                                    cotisation.getMasseAnnuelle(), true);
                            line.setMasseAnnuelle(cotisation.getMasseAnnuelle());
                        } else {
                            if (JadeStringUtil.isIntegerEmpty(cotisation.getMasseAnnuelle())) {
                                // si pas de masse annuelle (facturation par
                                // relevé) on ne peut pas définir le taux
                                // variable par avance
                                tauxAssurance = cotisation.findTauxWithRecalcul(dateEffectiveFinPeriode, null, true,
                                        false, "");
                            } else {
                                // masse existante (facturation par acompte)
                                // mais relevé pour compléments/rectificatifs,
                                // on utilise la masse pour définir le taux
                                tauxAssurance = cotisation.findTaux(dateEffectiveFinPeriode,
                                        cotisation.getMasseAnnuelle(), true);
                                line.setMasseAnnuelle(cotisation.getMasseAnnuelle());
                            }
                        }

                        if (tauxAssurance != null) {
                            // ****************************************************
                            // Assurances spéciales
                            // ****************************************************
                            if (CodeSystem.TYPE_ASS_MANUELLE.equalsIgnoreCase(donneesFacturation.getTypeAssurance())
                                    && CodeSystem.GEN_VALEUR_ASS_MONTANT.equalsIgnoreCase(tauxAssurance
                                            .getGenreValeur())
                                    && CodeSystem.PERIODICITE_ANNUELLE.equalsIgnoreCase(tauxAssurance
                                            .getPeriodiciteMontant())) {
                                double myMontant = Double.valueOf(
                                        JANumberFormatter.deQuote(tauxAssurance.getValeurTotal())).doubleValue();

                                String myYear = "";
                                if (new JACalendarGregorian().isValid(getDateDebut())) {
                                    myYear = String.valueOf(JACalendar.getYear(getDateDebut()));
                                }

                                if ((myMontant != 0.0) && (myYear != null) && (myYear.length() == 4)) {

                                    line.setGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
                                    line.setTauxGenre(tauxAssurance.getGenreValeur());
                                    line.setTauxType(tauxAssurance.getTypeId());
                                    line.setDebutPeriode("01.01." + myYear);
                                    line.setFinPeriode("31.12." + myYear);
                                    line.setPeriodeFactuAnnuelle(true);
                                    line.setMontantCalculer(myMontant);
                                    line.setIdModFacturation(idModuleFacturationreleve);

                                    this.addCotisation(line);

                                    totalCotisation.add(myMontant);
                                }

                            }

                            // Assurance FFPP paritaire
                            // le montant pour un affilié est calculé ainsi: (nb
                            // d'employé qui cotisent)*(montant de la
                            // cotisation)
                            else if (CodeSystem.TYPE_ASS_FFPP.equals(donneesFacturation.getTypeAssurance())) {
                                if (!AFParticulariteAffiliation.existeParticularite(getSession(),
                                        donneesFacturation.getAffiliationId(),
                                        CodeSystem.PARTIC_AFFILIE_SANS_PERSONNEL, JACalendar.todayJJsMMsAAAA())) {
                                    addCotisationFFPP(idModuleFacturationreleve, anneeFacturation, donneesFacturation,
                                            dateEffectiveDebutPeriode, line, tauxAssurance);
                                }
                                // *****************************************************
                                // Taux Fixe ou moyen
                                // *****************************************************
                            } else if (tauxAssurance.getGenreValeur().equals(CodeSystem.GEN_VALEUR_ASS_TAUX)) {

                                // On prend en compte le valable à la fin de la
                                // période de facturation.
                                line.setGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
                                line.setTauxGenre(tauxAssurance.getGenreValeur());
                                line.setTauxType(tauxAssurance.getTypeId());
                                line.setDebutPeriode(dateEffectiveDebutPeriode);
                                line.setFinPeriode(dateEffectiveFinPeriode);
                                line.setTaux(Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance
                                        .getValeurTotal())));
                                line.setFraction(Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance
                                        .getFraction())));
                                line.setAfficheTaux(new Boolean(tauxAssurance.isAffichageTaux()));

                                line.setIdModFacturation(idModuleFacturationreleve);

                                this.addCotisation(line);

                                // *****************************************************
                                // Taux Variable
                                // *****************************************************
                            } else if (tauxAssurance.getGenreValeur().equals(CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE)) {

                                addCotisationWhithTauxvariable(idModuleFacturationreleve, dateEffectiveDebutPeriode,
                                        dateEffectiveFinPeriode, line, cotisation, tauxAssurance);

                                // *****************************************************
                                // Montant
                                // *****************************************************
                            } else if (tauxAssurance.getGenreValeur().equals(CodeSystem.GEN_VALEUR_ASS_MONTANT)) {

                                double montantMensuel = 0.0;
                                // Un seul Montant pour la période de
                                // Facturation.
                                if (tauxAssurance.getPeriodiciteMontant().equals(CodeSystem.PERIODICITE_ANNUELLE)) {
                                    montantMensuel = Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance
                                            .getValeurTotal())) / 12;
                                } else if (tauxAssurance.getPeriodiciteMontant().equals(
                                        CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
                                    montantMensuel = Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance
                                            .getValeurTotal())) / 3;
                                } else if (tauxAssurance.getPeriodiciteMontant().equals(
                                        CodeSystem.PERIODICITE_MENSUELLE)) {
                                    montantMensuel = Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance
                                            .getValeurTotal()));
                                }

                                double montant = montantMensuel * nbMoisFacturer;

                                if (montant > 0.0) {
                                    line.setGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
                                    line.setTauxGenre(tauxAssurance.getGenreValeur());
                                    line.setTauxType(tauxAssurance.getTypeId());
                                    line.setDebutPeriode(dateEffectiveDebutPeriode);
                                    line.setFinPeriode(dateEffectiveFinPeriode);

                                    line.setMasse(montant);
                                    line.setMasse(AFUtil.plafonneMasse(line.getMasse(), getType(),
                                            line.getAssuranceId(), getDateDebut(), getSession(), theIdCompteAnnexe));
                                    line.setMontantCalculer(montant);

                                    line.setIdModFacturation(idModuleFacturationreleve);

                                    this.addCotisation(line);

                                    totalCotisation.add(montant);
                                }
                            }
                        } else {
                            // aucun taux trouvé
                            if (donneesFacturation.getTypeCalcul().equals(CodeSystem.TYPE_CALCUL_COTISATION)) {
                                // cotisation paritaire (sans masse ni taux)
                                line.setGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
                                line.setTauxType("0");
                                line.setTauxGenre("0");
                                line.setTypeCalcul(donneesFacturation.getTypeCalcul());
                                line.setDebutPeriode(dateEffectiveDebutPeriode);
                                line.setFinPeriode(dateEffectiveFinPeriode);
                                line.setIdModFacturation(idModuleFacturationreleve);
                                this.addCotisation(line);
                            }

                        }
                    }
                }
            }
        }
        setTotalCalculer(totalCotisation.toString());
    }

    private void addCotisationFFPP(String idModuleFacturationreleve, String anneeFacturation,
            AFProcessFacturationViewBean donneesFacturation, String dateEffectiveDebutPeriode,
            AFApercuReleveLineFacturation line, AFTauxAssurance tauxAssurance) throws Exception {
        String nbrAssures = donneesFacturation.getNbrAssures(anneeFacturation);
        if ((nbrAssures != null) && !nbrAssures.equals("0")) {

            if ((tauxAssurance != null) && !tauxAssurance.isNew()) {
                double montant = Integer.parseInt(tauxAssurance.getValeurEmployeur()) * Double.parseDouble(nbrAssures);
                line.setGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
                line.setTauxGenre(tauxAssurance.getGenreValeur());
                line.setTauxType(tauxAssurance.getTypeId());
                // assurance annuelle
                line.setDebutPeriode("01.01." + dateEffectiveDebutPeriode.substring(6));
                line.setFinPeriode("31.12." + dateEffectiveDebutPeriode.substring(6));
                line.setPeriodeFactuAnnuelle(true);
                line.setIdModFacturation(idModuleFacturationreleve);
                line.setMontantCalculer(montant);
                // libellé pour FFPP
                String langue = donneesFacturation.getLangue();
                if (langue.equalsIgnoreCase(CodeSystem.LANGUE_FRANCAIS)) {
                    line.setLibelle(donneesFacturation.getAssuranceLibelleFr() + " " + nbrAssures + " x "
                            + tauxAssurance.getValeurEmployeur() + ".-");
                } else if (langue.equalsIgnoreCase(CodeSystem.LANGUE_ALLEMAND)) {
                    line.setLibelle(donneesFacturation.getAssuranceLibelleAl() + " " + nbrAssures + " x "
                            + tauxAssurance.getValeurEmployeur() + ".-");
                } else if (langue.equalsIgnoreCase(CodeSystem.LANGUE_ITALIEN)) {
                    line.setLibelle(donneesFacturation.getAssuranceLibelleIt() + " " + nbrAssures + " x "
                            + tauxAssurance.getValeurEmployeur() + ".-");
                }
                this.addCotisation(line);
            }
        }
    }

    private void addCotisationWhithTauxvariable(String idModuleFacturationreleve, String dateEffectiveDebutPeriode,
            String dateEffectiveFinPeriode, AFApercuReleveLineFacturation line, AFCotisation cotisation,
            AFTauxAssurance tauxAssurance) throws Exception {
        // Le bon Taux sera calculé lorsque
        // l'utilisateur aura entré la masse.
        line.setGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
        line.setTauxGenre(tauxAssurance.getGenreValeur());
        line.setDebutPeriode(dateEffectiveDebutPeriode);
        line.setFinPeriode(dateEffectiveFinPeriode);
        if (!JadeStringUtil.isBlankOrZero(cotisation.getTauxAssuranceId())) {
            // if(CodeSystem.TYPE_TAUX_FORCE.equals(tauxAssurance.getTypeId()))
            // {
            // taux variable forcé, afficher la valeur
            // du taux
            line.setTauxType(CodeSystem.TYPE_TAUX_FORCE);
            line.setTaux(Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal())));
            line.setFraction(Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getFraction())));
        } else {
            line.setTauxType(CodeSystem.TYPE_TAUX_DEFAUT);
            // si masse existante (facturation par
            // acompte) mais relevé pour
            // compléments/rectificatifs, on spécifie le
            // taux
            if (!JadeStringUtil.isIntegerEmpty(cotisation.getMasseAnnuelle())
                    && !CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE.equals(type)
                    && !CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA.equals(type)
                    && !CodeSystem.TYPE_RELEVE_DECOMP_FINAL.equals(type)) {
                line.setTaux(Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal())));
                line.setFraction(Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getFraction())));
            }
        }
        line.setIdModFacturation(idModuleFacturationreleve);

        this.addCotisation(line);
    }

    /**
     * Recherche l'affiliation concernée
     * 
     * @param transaction
     * @throws Exception
     */
    public AFAffiliation getAffiliation() throws Exception {
        if (JadeStringUtil.isEmpty(getAffilieNumero()) || JadeStringUtil.isEmpty(getIdTiers())) {
            return null;
        }
        AFAffiliationManager mgr = new AFAffiliationManager();
        mgr.setSession(getSession());
        mgr.setForAffilieNumero(getAffilieNumero());
        mgr.setForTypesAffParitaires();
        mgr.setForIdTiers(getIdTiers());
        mgr.find(BManager.SIZE_USEDEFAULT);
        if (mgr.isEmpty()) {
            return null;
        } else {
            return (AFAffiliation) mgr.getFirstEntity();
        }
    }

    public String getAffiliationId() {
        return affiliationId;
    }

    public String getAffilieNumero() {
        return affilieNumero;
    }

    public String getAnneeReference() {
        return anneeReference;
    }

    public List<?> getApercuReleveLineFacturationList() {
        return apercuReleveLineFacturationList;
    }

    public String getCollaborateur() {
        if (JadeStringUtil.isEmpty(collaborateur)) {
            collaborateur = getSpy().getUser();
        }
        return collaborateur;
    }

    /**
     * Permet de récuperer un compte annexe Date de création : (22.05.2003 10:12:15)
     * 
     * @return globaz.osiris.db.comptes.CACompteAnnexe
     */
    public CACompteAnnexe getCompteAnnexe() throws Exception {
        // Si _compteAnnexe n'est pas déjà instancié
        if (_compteAnnexe == null) {
            // Chargement du manager
            CACompteAnnexeManager manager = new CACompteAnnexeManager();
            manager.setSession(getSession());
            // manager.setForIdTiers(getAffiliation().getIdTiers());
            manager.setForIdRole(CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(
                    getSession().getApplication()));
            manager.setForIdExterneRole(getAffilieNumero());
            manager.find(BManager.SIZE_USEDEFAULT);
            if (!manager.isEmpty()) {
                _compteAnnexe = (CACompteAnnexe) manager.getEntity(0);
                /*
                 * }else{ throw new Exception(getSession().getLabel("DECL_ERREUR_LECTURE_CA")); }
                 */
            }
        }
        return _compteAnnexe;
    }

    public CACompteur getCompteur(String rubriqueId) throws Exception {
        // Chargement du compteur
        CACompteur compteur = new CACompteur();
        compteur.setSession(getSession());
        compteur.setAlternateKey(CACompteur.AK_CPTA_RUB_ANNEE);
        compteur.setAnnee(getDateFin().substring(6));
        if (getCompteAnnexe() == null) {
            return null;
        }
        compteur.setIdCompteAnnexe(getCompteAnnexe().getIdCompteAnnexe());
        compteur.setIdRubrique(rubriqueId);
        compteur.retrieve();
        if (!compteur.isNew()) {
            return compteur;
        } else {
            return null;
        }
    }

    public List<AFApercuReleveLineFacturation> getCotisationList() {
        return cotisationList;
    }

    /**
     * Generation de la Liste de Cotisation pour ce Relevé en fonction de l'affilié et de la période.
     * 
     * @throws Exception
     */
    public void getCotisationListFromFacturation() throws Exception {
        FWCurrency totalCotisation = new FWCurrency(0.0);
        for (int i = 0; i < cotisationList.size(); i++) {
            AFApercuReleveLineFacturation releveLine = cotisationList.get(i);

            // recherche de l'afact correspondant
            FAAfact afact = new FAAfact();
            afact.setISession(getSessionMusca(getSession()));
            afact.setAlternateKey(FAAfact.AK_INTERETSMORATOIRES);
            afact.setReferenceExterne(getIdReleve() + "/" + releveLine.getCotisationId());
            afact.retrieve();

            if (!afact.isNew()) {
                // System.out.println(releveLine.getAssuranceLibelleFr()+" trouvée");
                if ((CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE.equals(type))
                        || (CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA.equals(type))) {

                    // pour afficher les montants initiaux (ceux saisi par
                    // l'utilisateur)
                    /*
                     * releveLine.setMasse(afact.getMasseInitiale()); releveLine.
                     * setMontantCalculer(afact.getMontantInitial(),false);
                     * totalCotisation.add(afact.getMontantInitial());
                     */

                    // pour afficher les lignes calculées (comme pour la
                    // taxation d'office)
                    releveLine.setMasse(afact.getMasseFacture());
                    releveLine.setMontantCalculer(afact.getMontantFacture(), false);
                    totalCotisation.add(afact.getMontantFacture());
                } else {
                    releveLine.setMasse(afact.getMasseFacture());
                    releveLine.setMontantCalculer(afact.getMontantFacture(), false);
                    totalCotisation.add(afact.getMontantFacture());
                }

            } else {
                // System.out.println(releveLine.getAssuranceLibelleFr()+" non trouvée");
                cotisationList.remove(releveLine);
                i--;
            }
        }
        setTotalCalculer(totalCotisation.toString());

        FAEnteteFacture entete = new FAEnteteFacture();
        entete.setISession(getSessionMusca(getSession()));
        entete.setIdEntete(idEntete);
        entete.retrieve();
        if (!entete.isNew()) {
            idPassage = entete.getIdPassage();
            idExterneFacture = entete.getIdExterneFacture();
        }
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return
     */
    public String getDateReception() {
        return dateReception;
    }

    private FAEnteteFacture getEntetefacture(BTransaction transaction, String roleAfact,
            AFApercuReleveLineFacturation releveLine) throws Exception {
        if (JadeStringUtil.isEmpty(roleAfact)) {
            // si role vide, utiliser paritaire par défaut
            roleAfact = CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(getSession().getApplication());
        }

        // PO 4384: Recherche et incrémente l'idExterne facture si il existe un autre relevé pour l'affilié en cours de
        // facturation
        int idFacturationExt = returndExterneFacture(transaction);

        // créer une nouvelle en-tête
        FAEnteteFacture enteteFacture = new FAEnteteFacture();
        enteteFacture.setISession(getSessionMusca(getSession()));
        enteteFacture.setIdPassage(getIdPassage());
        enteteFacture.setIdRole(roleAfact);
        enteteFacture.setIdExterneRole(getAffilieNumero());

        // Ajout d'une remarque dans l'entête de facture
        // "Délai de paiement(non déterminant pour les intérêts moratoires)"
        // bz 4458 selon cu cotisations du 24.08 texte valable uniquement pour
        // les décomptes périodiques
        // ajout écgalement du type complément période
        if (CodeSystem.TYPE_RELEVE_PERIODIQUE.equals(getType()) || CodeSystem.TYPE_RELEVE_COMPLEMENT.equals(getType())) {
            FARemarque rem = new FARemarque();
            rem.setSession(getSession());
            rem.setTexte(getSession().getApplication().getLabel("NAOS_RELEVE_REMARQUE",
                    getTiers().getLangueIso().toUpperCase()));
            rem.add(transaction);
            enteteFacture.setIdRemarque(rem.getIdRemarque());
        }

        if (idFacturationExt == 0) {
            // aucune entête trouvée précédemment
            enteteFacture.setIdExterneFacture(getIdExterneFacture());
        } else {
            // incrémenter l'id externe facturation de la dernière en-tête
            // trouvée
            if (getType().equalsIgnoreCase(CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA)) {
                _addError(transaction, "Il existe déjà un décompte 13 pour cette affilié");

            }
            enteteFacture.setIdExterneFacture(String.valueOf(idFacturationExt + 1));
        }
        if (getType().equalsIgnoreCase(CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA)) {
            if (!autoriseDecompte13(getSession())) {
                _addError(transaction, getSession().getLabel("BLOQUAGE_13_ERREUR"));
            }
        }
        /*
         * DGI 27.09.2007 suppression du type de section 30 car inutile String typeSection="";
         * if(getIdSousTypeFacture().equals("227030")){ typeSection="30"; }else{ typeSection="1"; }
         * enteteFacture.setIdTypeFacture(typeSection);
         */
        enteteFacture.setIdTypeFacture("1");
        enteteFacture.setIdTiers(getIdTiers());
        enteteFacture.setIdSousType(getIdSousTypeFacture());
        enteteFacture.setIdSoumisInteretsMoratoires(getInterets());
        enteteFacture.setIdModeRecouvrement(CodeSystem.MODE_RECOUV_AUTOMATIQUE);
        enteteFacture.setDateReceptionDS(getDateReception());
        if (releveLine != null) {
            enteteFacture.setIdDomaineCourrier(releveLine.getDomaineCourrier());
            enteteFacture.setIdDomaineLSV(releveLine.getDomaineRecouvrement());
            enteteFacture.setIdDomaineRemboursement(releveLine.getDomaineRemboursement());
            enteteFacture.setNonImprimable(releveLine.getBlocageEnvoi());
            enteteFacture.setLibelle(releveLine.getLibellePlan());
        }
        enteteFacture.setReferenceFacture(getSession().getUserId());
        enteteFacture.add(transaction);

        // if(JadeStringUtil.isIntegerEmpty(idEntete)) {
        idEntete = enteteFacture.getIdEntete();
        // }

        return enteteFacture;
    }

    public String getEtat() {
        return etat;
    }

    public Boolean getForcePeriode() {
        return forcePeriode;
    }

    public String getIdEntete() {
        return idEntete;
    }

    public String getIdExterneFacture() {
        return idExterneFacture;
    }

    public String getIdPassage() {
        return idPassage;
    }

    public String getIdReleve() {
        return idReleve;
    }

    public String getIdSousTypeFacture() {
        return idSousTypeFacture;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getInterets() {
        return interets;
    }

    public String getNewEtat() {
        return newEtat;
    }

    public String getPlanAffiliationId() {
        return planAffiliationId;
    }

    public Boolean getPrevisionAcompteEBU() {
        return previsionAcompteEBU;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public BISession getSessionDS(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute("sessionDraco");
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = GlobazSystem.getApplication("DRACO").newSession(local);
            local.setAttribute("sessionDraco", remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    /**
     * Ouvrir une session dans MUSCA.
     * 
     * @param session
     * @return
     * @throws Exception
     */
    private BISession getSessionMusca(BISession session) throws Exception {
        // Ouvrir une nouvelle session
        if (sessionMusca == null) {
            BIApplication remoteApplication = GlobazSystem.getApplication("MUSCA");
            sessionMusca = remoteApplication.newSession(session);
        }
        return sessionMusca;
    }

    /**
     * Ouvrir une session dans MUSCA.
     * 
     * @param session
     * @return
     * @throws Exception
     */
    private BSession getSessionOsiris(BISession session) throws Exception {
        // Ouvrir une nouvelle session
        if (sessionOsiris == null) {
            BIApplication remoteApplication = GlobazSystem.getApplication("OSIRIS");
            sessionOsiris = remoteApplication.newSession(session);
        }
        return (BSession) sessionOsiris;
    }

    /**
     * Rechercher le tiers de l'affiliation en fonction de l'affiliation
     * 
     * @return le tiers
     */
    public TITiers getTiers() {
        // Si pas d'identifiant => pas d'objet
        if ((_tiers == null) || ((_tiers != null) && !_tiers.getIdTiers().equals(getIdTiers()))) {
            _tiers = new TITiers();
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

    public String getTotalCalculer() {
        return JANumberFormatter.fmt(totalCalculer.toString(), true, true, false, 2);
    }

    public String getTotalControl() {
        return JANumberFormatter.fmt(totalControl.toString(), true, true, false, 2);
    }

    public String getType() {
        return type;
    }

    public String giveDateReceptionInfoRom206() {
        try {
            if ("true".equalsIgnoreCase(FWFindParameter.findParameter(new BTransaction(getSession()), "0",
                    "INFOROM206", "0", "0", 0))) {
                DSDeclarationListViewBean mgrDeclaration = new DSDeclarationListViewBean();
                mgrDeclaration.setSession(getSession());
                mgrDeclaration.setLikeNumeroAffilie(getAffilieNumero());
                mgrDeclaration.setForAnnee(String.valueOf(JADate.getYear(getDateFin())));
                mgrDeclaration.setForTypeDeclaration(DSDeclarationViewBean.CS_PRINCIPALE);
                mgrDeclaration.setForSelectionTri("1");
                mgrDeclaration.find(BManager.SIZE_USEDEFAULT);

                if (mgrDeclaration.size() == 1) {
                    DSDeclarationViewBean entityDeclaration = (DSDeclarationViewBean) mgrDeclaration.getFirstEntity();
                    if ((entityDeclaration != null)
                            && !JadeStringUtil.isBlankOrZero(entityDeclaration.getDateRetourEff())) {
                        return entityDeclaration.getDateRetourEff();
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
        }

        return JACalendar.todayJJsMMsAAAA();
    }

    public boolean isDoCalculCotisation() {
        return doCalculCotisation;
    }

    public boolean isFirstCalculation() {
        return firstCalculation;
    }

    public boolean isMontantNegatif() {
        return montantNegatif;
    }

    /**
     * Determine le prochain JobId (IdPassage valide pour Facturation).
     * 
     * @return
     */
    public boolean retrieveIdPassage() {
        boolean result = true;
        if (getEtat().equals(CodeSystem.ETATS_RELEVE_SAISIE)) {
            try {
                // Recherche du prochain id passage à facturer
                IFAPassage passage = null;
                passage = ServicesFacturation.getProchainPassageFacturation(getSession(), null,
                        CodeSystem.TYPE_MODULE_RELEVE);
                if (passage != null) {
                    setIdPassage(passage.getIdPassage());
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
                result = false;
                setIdPassage("");
                // _addError(transaction, getSession().getLabel("1670"));
            }
        }
        return result;
    }

    /**
     * Recupére montants du Relevé dans la DB.
     * 
     * @throws Exception
     */
    public void retrieveReleveMontant() throws Exception {

        double masseGeneral = 0.0;
        FWCurrency totalCotisation = new FWCurrency(0.0);
        for (int i = 0; i < cotisationList.size(); i++) {
            AFApercuReleveLineFacturation releveLine = cotisationList.get(i);

            AFApercuReleveMontant montant = new AFApercuReleveMontant();
            montant.setSession(getSession());
            montant.setIdReleve(getIdReleve());
            montant.setAssuranceId(releveLine.getAssuranceId());
            montant.setDateDebut(releveLine.getDebutPeriode());
            montant.setCotisationId(releveLine.getCotisationId());
            montant.retrieve();
            if (montant.isNew()) {
                // temporaire après reprise sans id cotisation
                montant.setCotisationId("0");
                montant.retrieve();
            }
            if (!montant.isNew()) {
                releveLine.setMasse(montant.getMasse());

                AFAssurance assurance = new AFAssurance();
                assurance.setId(releveLine.getAssuranceId());
                assurance.setSession(getSession());
                assurance.retrieve();

                if (!assurance.isNew() && CodeSystem.TYPE_ASS_COTISATION_AVS_AI.equals(assurance.getTypeAssurance())) {
                    if (releveLine.getMasse() != 0.0) {
                        masseGeneral = releveLine.getMasse();
                    }
                }

                if (CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE.equals(releveLine.getTauxGenre())
                        && !CodeSystem.TYPE_TAUX_FORCE.equals(releveLine.getTauxType())) {
                    // mise à jour des taux dans le cas d'une taux variable non
                    // forcé
                    // getTauxVariable(releveLine);
                    if (!assurance.isNew() && CodeSystem.TYPE_ASS_FRAIS_ADMIN.equals(assurance.getTypeAssurance())) {
                        findAndSetTauxVariable(releveLine, String.valueOf(montant.getMasse()), masseGeneral);
                    } else {
                        findAndSetTauxVariable(releveLine, String.valueOf(montant.getMasse()), releveLine.getMasse());
                    }
                }

                releveLine.setMontantCalculer(montant.getMontantCalculer(), false);
            } else {
                cotisationList.remove(releveLine);
                i--;
            }
            totalCotisation.add(releveLine.getMontantCalculer());
        }
        setTotalCalculer(totalCotisation.toString());
    }

    /**
     * Recherche et incrémente l'idExterne facture si il existe un autre relevé pour l'affilié en cours de facturation
     * PO 4384
     * 
     * @param transaction
     * @return
     * @throws Exception
     * @throws NumberFormatException
     */
    protected int returndExterneFacture(BTransaction transaction) throws Exception, NumberFormatException {
        // PO 4384: Recherche et incrémente l'idExterne facture si il existe un autre relevé pour l'affilié en cours de
        // facturation
        FAEnteteFactureManager entete = new FAEnteteFactureManager();
        entete.setSession(getSession());
        entete.setUseManagerWhitPassage(Boolean.TRUE);
        entete.setInStatusPassage(FAPassage.CS_ETAT_OUVERT + ", " + FAPassage.CS_ETAT_TRAITEMENT); // PO 4384
        entete.setForIdExterneRole(getAffilieNumero());
        entete.setForIdTiers(getIdTiers());
        entete.find(transaction);
        int idFacturationExt = 0;
        // recherche sur les en-tête déjà existantes
        for (int iEntete = 0; iEntete < entete.size(); iEntete++) {
            FAEnteteFacture ef = (FAEnteteFacture) entete.getEntity(iEntete);
            // recherche et sauvegarde du dernier id externe facturation
            String idFactStart = ef.getIdExterneFacture().substring(0, 6);
            int idFact = Integer.parseInt(ef.getIdExterneFacture());
            if (getIdExterneFacture().substring(0, 6).equals(idFactStart) && (idFacturationExt < idFact)) {
                idFacturationExt = idFact;
            }
        }
        return idFacturationExt;
    }

    public void setAffiliationId(String affiliationId) {
        this.affiliationId = affiliationId;
    }

    public void setAffilieNumero(String string) {
        affilieNumero = string;
    }

    public void setAnneeReference(String anneeReference) {
        this.anneeReference = anneeReference;
    }

    public void setApercuReleveLineFacturationList(List<?> apercuReleveLineFacturationList) {
        this.apercuReleveLineFacturationList = apercuReleveLineFacturationList;
    }

    public void setCollaborateur(String string) {
        collaborateur = string;
    }

    public void setCotisationList(List<AFApercuReleveLineFacturation> cotList) {
        cotisationList = cotList;
    }

    public void setDateDebut(String string) throws JAException {

        if (!JadeStringUtil.isEmpty(string)) {
            try {

                if (string.matches("^[0-3][0-9].[0-1][0-9].[0-9]{4}$")) {
                    dateDebut = string;
                } else {

                    JADate date = new JADate(string);
                    dateDebut = AFUtil.getDateBeginingOfMonth(date.toStr("."));
                }
            } catch (JAException e) {
                JadeLogger.error(this, e);
                throw e;
            }
        }

    }

    public void setDateFin(String string) throws JAException {
        if (!JadeStringUtil.isEmpty(string)) {
            try {
                if (string.matches("^[0-3][0-9].[0-1][0-9].[0-9]{4}$")) {
                    dateFin = string;
                } else {

                    JADate date = new JADate(string);
                    dateFin = AFUtil.getDateEndOfMonth(date.toStr("."));
                }
            } catch (JAException e) {
                JadeLogger.error(this, e);
                throw e;
            }
        }
    }

    /**
     * @param string
     */
    public void setDateReception(String string) {
        dateReception = string;
    }

    public void setDoCalculCotisation(boolean doCalculCotisation) {
        this.doCalculCotisation = doCalculCotisation;
    }

    public void setEtat(String string) {
        etat = string;
    }

    public void setForcePeriode(Boolean forcePeriode) {
        this.forcePeriode = forcePeriode;
    }

    public void setIdEntete(String string) {
        idEntete = string;
    }

    public void setIdExterneFacture(String string) {
        idExterneFacture = string;
    }

    public void setIdPassage(String string) {
        idPassage = string;
    }

    public void setIdReleve(String string) {
        idReleve = string;
    }

    public void setIdSousTypeFacture(String string) {
        idSousTypeFacture = string;
    }

    public void setIdTiers(String string) {
        idTiers = string;
    }

    public void setInterets(String string) {
        interets = string;
    }

    public void setNewEtat(String string) {
        newEtat = string;
    }

    public void setPlanAffiliationId(String planAffiliationId) {
        this.planAffiliationId = planAffiliationId;
    }

    public void setPrevisionAcompteEBU(Boolean previsionAcompteEBU) {
        this.previsionAcompteEBU = previsionAcompteEBU;
    }

    public void setTotalCalculer(String string) {
        totalCalculer = JANumberFormatter.deQuote(string);
    }

    public void setTotalControl(String string) {
        totalControl = JANumberFormatter.deQuote(string);
    }

    public void setType(String string) {
        type = string;
    }

    public boolean isWantControleCotisation() {
        return wantControleCotisation;
    }

    public void setWantControleCotisation(boolean wantControleCotisation) {
        this.wantControleCotisation = wantControleCotisation;
    }

    /**
     * Mettre à jour la valeur des montants du Relevé dans la DB.
     * 
     * @param transaction
     * @throws Exception
     */
    private void updateReleveMontant(BTransaction transaction) throws Exception {
        for (int i = 0; i < cotisationList.size(); i++) {
            AFApercuReleveLineFacturation releveLine = cotisationList.get(i);

            AFApercuReleveMontant montant = new AFApercuReleveMontant();
            montant.setSession(getSession());
            montant.setIdReleve(getIdReleve());
            montant.setAssuranceId(releveLine.getAssuranceId());
            montant.setDateDebut(releveLine.getDebutPeriode());
            montant.setCotisationId(releveLine.getCotisationId());
            montant.retrieve(transaction);

            montant.setMasse(releveLine.getMasseString(false));
            montant.setMontantCalculer(releveLine.getMontantCalculerString());
            if (montant.isNew()) {
                montant.add(transaction);
            } else {
                montant.update(transaction);
            }
        }
    }

    /**
     * Contrôle si les champs obligatoires sont renseignés et valides.
     * 
     * @return Une String vide si les champs obligatoires sont renseignés et valides ou Une String contenant les erreurs
     * @throws Exception
     */
    public String validationMandatory() throws Exception {

        StringBuffer message = new StringBuffer();

        boolean numAffilieOK = true;
        boolean datesOK = true;

        // Test que les champs obligatoires soit renseignés
        if (JadeStringUtil.isBlankOrZero(getAffilieNumero())) {
            message.append(getSession().getLabel("150") + "\n");
            numAffilieOK = false;
        }
        try {
            BSessionUtil.checkRealDateGregorian(getSession(), getDateDebut());
        } catch (Exception e) {
            message.append(getSession().getLabel("20") + "\n");
            datesOK = false;
        }
        try {
            BSessionUtil.checkRealDateGregorian(getSession(), getDateFin());
        } catch (Exception e) {
            message.append(getSession().getLabel("30") + "\n");
            datesOK = false;
        }
        if (JadeStringUtil.isBlankOrZero(getType())) {
            message.append(getSession().getLabel("1750") + "\n");
        }

        // *******************************************************************
        // Numero Affilie
        // *******************************************************************

        // Formatage et Validité du numéro d'affilié.
        if (numAffilieOK && !checkAffilieNumero()) {
            message.append(getSession().getLabel("930") + "\n");
            numAffilieOK = false;
        }
        AFAffiliation aff = null;
        if (numAffilieOK) {
            // Control le Formatage du numéro d'affilié.
            AFAffiliationManager manager = new AFAffiliationManager();
            manager.setSession(getSession());
            manager.setForAffilieNumero(getAffilieNumero());
            manager.find(BManager.SIZE_USEDEFAULT);
            if (manager.size() > 0) {
                if (!idTiers.equals((aff = (AFAffiliation) manager.get(0)).getIdTiers())) {
                    message.append(getSession().getLabel("1630") + "\n");
                }
            } else {
                message.append(getSession().getLabel("1560") + "\n");
            }
        }

        // Controle Date début < date de Fin
        if (datesOK && !BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDebut(), getDateFin())) {
            message.append(getSession().getLabel("550") + "\n");
            datesOK = false;
        }

        // Controle de la période
        if (datesOK) {

            if (getType().equals(CodeSystem.TYPE_RELEVE_PERIODIQUE)
                    || getType().equals(CodeSystem.TYPE_RELEVE_COMPLEMENT)) {

                if (getDateDebut().length() > 6 && getDateFin().length() > 6
                        && !getDateDebut().substring(6).equals(getDateFin().substring(6))) {
                    message.append(getSession().getLabel("1640") + "\n");
                    datesOK = false;
                } else {
                    int nbMoisFacturer = AFUtil.nbMoisPeriode(getSession(), getDateDebut(), getDateFin());
                    String moisFacturation = getDateFin().substring(3, 5);

                    if ((nbMoisFacturer == 1) || (nbMoisFacturer == 3) || (nbMoisFacturer == 12)) {

                        if ((nbMoisFacturer == 3)
                                && !moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.MARS_TRIMESTRE_1)
                                && !moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.JUIN_TRIMESTRE_2)
                                && !moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.SEPTEMBRE_TRIMESTRE_3)
                                && !moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.ANNUEL)) {

                            message.append(getSession().getLabel("1650") + "\n");
                            datesOK = false;
                        }
                    } else {
                        message.append(getSession().getLabel("1660") + "\n");
                        datesOK = false;
                    }
                }
            } else if (getType().equals(CodeSystem.TYPE_RELEVE_DECOMP_FINAL)
                    || getType().equals(CodeSystem.TYPE_RELEVE_CONTROL_EMPL)
                    || getType().equals(CodeSystem.TYPE_RELEVE_RECTIF)) {

                if (getDateDebut().length() > 6 && getDateFin().length() > 6
                        && !getDateDebut().substring(6).equals(getDateFin().substring(6))) {
                    message.append(getSession().getLabel("1640") + "\n");
                    datesOK = false;
                }
            }

            if (datesOK) {
                // tester la période avec les relevés existants si différent de
                // rectificatif ou complément et qu'aucun plan d'affiliation
                // n'est sélectionné
                if (!CodeSystem.TYPE_RELEVE_COMPLEMENT.equals(getType())
                        && !CodeSystem.TYPE_RELEVE_RECTIF.equals(getType())
                        && JadeStringUtil.isEmpty(getPlanAffiliationId())) {
                    AFApercuReleveManager manager = new AFApercuReleveManager();
                    manager.setSession(getSession());
                    manager.setForIdTiers(getIdTiers());
                    manager.setForAffilieNumero(getAffilieNumero());
                    manager.setForType(getType());
                    manager.find();

                    for (int i = 0; i < manager.size(); i++) {

                        AFApercuReleve releve = (AFApercuReleve) manager.getEntity(i);

                        // Ne pas tester le relevé avec lui meme et ignorer les
                        // releves deja comptabilises
                        if (!CodeSystem.ETATS_RELEVE_COMPTABILISER.equals(releve.getEtat())
                                && !releve.getIdReleve().equalsIgnoreCase(getIdReleve())) {

                            if (BSessionUtil.compareDateBetweenOrEqual(getSession(), releve.getDateDebut(),
                                    releve.getDateFin(), getDateDebut())
                                    || BSessionUtil.compareDateBetweenOrEqual(getSession(), releve.getDateDebut(),
                                            releve.getDateFin(), getDateFin())) {

                                message.append(FWMessageFormat.format(getSession().getLabel("1740"), getDateDebut(),
                                        getDateFin(), releve.getDateDebut(), releve.getDateFin()) + "\n");
                                datesOK = false;
                            }
                        }
                    }
                }
            }
            if (datesOK && (aff != null)) {
                // contrôle avec affiliation
                if (BSessionUtil.compareDateFirstLower(getSession(), getDateDebut(), aff.getDateDebut())) {
                    // la date de début est antérieure à celle de l'affiliation
                    if (!forcePeriode.booleanValue()) {
                        message.append(FWMessageFormat.format(getSession().getLabel("1951"), getDateDebut(),
                                getDateFin(), aff.getDateDebut(), aff.getDateFin()) + "\n");
                    } else {
                        // on met à jour la date de début d'affiliation avec
                        // celle de début dur relevé
                        // et on mémorise l'ancienne date dans la particularité
                        // "période d'affiliation"
                        AFParticulariteAffiliation part = new AFParticulariteAffiliation();
                        part.setSession(getSession());
                        part.setAffiliationId(aff.getAffiliationId());
                        part.setDateDebut(aff.getDateDebut());
                        part.setDateFin(aff.getDateFin());
                        part.setParticularite(CodeSystem.PARTIC_AFFILIE_PERIODE_AFFILIATION);
                        part.add();
                        // maj affiliation
                        aff.setDateDebut(getDateDebut());
                        aff.update();
                    }

                }
            }
            // PO 3758 - Avertir si il n'y a pas de cotisation pour la période saisie
            if (isWantControleCotisation()
                    && AFUtil.returnNombreCotisationPourPeriode(getSession(), getPlanAffiliationId(), getDateDebut(),
                            getDateFin()) == 0) {
                message.append(getSession().getLabel("AUCUNECOTI_PLANPERIODE") + "\n");
            }
        }
        return message.toString();
    }

    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        super._afterUpdate(transaction);

        if (getNewEtat().equals(CodeSystem.ETATS_RELEVE_SAISIE)) {
            deleteReleveInFacturation(transaction);
        }
    }

}
