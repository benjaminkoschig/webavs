package globaz.draco.db.declaration;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.draco.application.DSApplication;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuelles;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuellesManager;
import globaz.draco.properties.DSProperties;
import globaz.draco.util.DSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEControleEmployeurManager;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.process.handler.LEJournalHandler;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.lupus.db.journalisation.LUJournalViewBean;
import globaz.musca.db.facturation.FAPassage;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFCalculAssurance;
import globaz.naos.db.controleEmployeur.AFControleEmployeur;
import globaz.naos.db.controleEmployeur.AFControleEmployeurManager;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliationManager;
import globaz.naos.db.releve.AFReleve1314Checker;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.services.AFAssuranceServices;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pyxis.db.tiers.TIRole;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DSDeclarationViewBean extends BEntity implements FWViewBeanInterface {
    public final static String CS_AFACTURER = "121002";
    public final static String CS_AUTOMATIQUE = "123002";
    public final static String CS_BOUCLEMENT_ACOMPTE = "122003";
    public final static String CS_COMPLEMENTAIRE = "122002";
    public final static String CS_COMPTABILISE = "121003";
    public final static String CS_CONTROLE_EMPLOYEUR = "122004";
    public final static String CS_DIVIDENDE = "122009";
    public final static String CS_ICI = "122008";
    // réduction AF à ignorer
    public final static String CS_ID_ASSURANCE_A_IGNORER = "12000007";
    public final static String CS_LTN = "122005";

    public final static String CS_LTN_COMPLEMENTAIRE = "122006";
    // Code Suspendu
    public final static String CS_MANUELLE = "123001";
    // codes systeme
    // Etat declaration
    public final static String CS_OUVERT = "121001";
    public final static String CS_PLAFOND_LTN_AFFILIE = "12000008";
    public final static String CS_PLAFOND_LTN_ASSURE = "12000009";

    // Type declaration
    public final static String CS_PRINCIPALE = "122001";

    public final static String CS_SALAIRE_DIFFERES = "122007";
    private static final String ELEMENT_NOT_INTERET_CHOIX = "except.draco.declaration.notInteretChoix";
    public final static String PROVENANCE_PUCS_CCJU = "3";
    public final static String PROVENANCE_SWISSDEC = "4";
    private static final long serialVersionUID = 1L;
    private AFAffiliation _affiliation = null;
    private CACompteAnnexe _compteAnnexe = null;
    /** (MAIAFF) */
    private String affiliationId = new String();
    private String affilieDesEcran = new String();
    private String affilieNumero = "";
    private String affilieRadieEcran = new String();
    /** (TAANNE) */
    private String annee = new String();
    /** TAANTX */
    private String anneeTaux = "";
    private String codeCantonAF = "";
    /** (TATCDS) */
    private String codeSuspendu = new String();
    private String dateEnvoiLettre = "";
    private String dateEnvoiRappel = "";
    /** (TADFIS) */
    private String dateFinSuspendu = new String();
    /** TADATT */
    private String dateImpressionAttestations = new String();
    /** TADANT */
    private String dateImpressionDecompteImpots = new String();
    /** (TADRDE) */
    private String dateRetourDes = new String();
    /** (TADREF) */
    private String dateRetourEff = new String();
    private String descriptionTiers = new String();
    private String designation1 = "";

    // Ce flat indique si la déclaration est en cours de facturation
    private boolean enFacturation = false;

    // ce flag indique si la déclaration est en validation ou non
    private boolean enValidation = false;

    /** (TATETA) */
    private String etat = new String();
    private Boolean forceUpdate = false;
    private String forControleEmployeurId = "";
    private String fordateEffective = "";
    private String fordatePrevue = "";
    /** Fichier DSDECLP */
    /** (TAIDDE) */
    private String idDeclaration = new String();
    /** TAIDIS */
    private String idDeclarationDistante = new String();
    /** (KCID) */
    private String idJournal = new String();
    /** (EAID) */
    private String idPassageFac = new String();

    /** Id du ichier pucs qui a créé la déclaration */
    private String idPucsFile = null;

    /** (HTITIE) */
    private String idTiers = new String();
    /** TAMAAT */
    private String masseAC2Total = new String();
    /** (TAMACT) */
    private String masseACTotal = new String();
    /** TANPER */
    private String massePeriode = new String();

    /** (TAMMAS) */
    private String masseSalTotal = new String();

    // ce champ permet de tester si la masse salariale totate à été modifiée
    private String masseSalTotalEcran = new String();
    // Indique que la déclaration à été introduite avec un montant 0
    private boolean masseZero = false;
    private FWCurrency montantFacture;
    /** (TALMOT) */
    private String motifSuspendu = new String();
    /** TANNBR */
    private String nbPersonnel = new String();
    private String nbRappel = "";
    /** (TANDEC) */
    private String noDecompte = new String();

    // Le nom du tiers
    private String nomTiers = new String();
    private Boolean notImpressionDecFinalAZero = new Boolean(false);
    // Le numéro d'affilié du tiers
    private String numeroAffilie = new String();
    // Le prénom du tiers
    private String prenomTiers = new String();

    private String provenance = new String(); // Vient de la DAN ou du PUCS

    /** TARFAC */
    private String referenceFacture = new String();

    private boolean saisieEcran = false;

    /** TAMCOD */
    private String soumisInteret = new String();
    /** TAMCAC */
    private String totalControleAc = new String();
    /** TAMCAF */
    private String totalControleAf = new String();
    /** TAMCON */
    private String totalControleDS = new String();

    private String tri = "";

    private String triEcran = "";
    private String typeAffiliationEcran = new String();
    /** (TATTYP) */
    private String typeDeclaration = new String();
    /** V_DATE_SPY */
    private String validationDateSpy = new String();
    /** V_SPY */
    private String validationSpy = new String();

    private String warningMessage = new String();

    /**
     * Commentaire relatif au constructeur DSDeclaration
     */
    public DSDeclarationViewBean() {
        super();
    }

    @Override
    protected void _afterAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        if (isSaisieEcran()
                && "022".equals(DSUtil.getNoCaisse(getSession()))
                && (DSDeclarationViewBean.CS_COMPLEMENTAIRE.equals(typeDeclaration)
                        || DSDeclarationViewBean.CS_BOUCLEMENT_ACOMPTE.equals(typeDeclaration) || DSDeclarationViewBean.CS_PRINCIPALE
                            .equals(typeDeclaration))) {
            // chercher toutes les assurances de l'affilié pour l'année de la
            // déclaration
            AFCotisationManager cotisations = new AFCotisationManager();
            cotisations.setSession(getSession());
            cotisations.setForAffiliationId(getAffiliationId());
            cotisations.setForAnneeDeclaration(getAnnee());
            cotisations.setForNotMotifFin(CodeSystem.MOTIF_FIN_EXCEPTION);
            try {
                cotisations.find();
                // Contrôler s'il existe déjà des lignes de déclaration
                DSLigneDeclarationListViewBean ligne = new DSLigneDeclarationListViewBean();
                ligne.setSession(getSession());
                ligne.setForIdDeclaration(getIdDeclaration());
                int count = ligne.getCount();
                // Si le montant est différent de zéro on génère les lignes
                if (isMasseZero()
                        || (!JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(getMasseSalTotal())) && (count == 0))) {
                    String oldId = "0";
                    for (int i = 0; i < cotisations.size(); i++) {

                        AFCotisation cotisation = (AFCotisation) cotisations.getEntity(i);
                        if (oldId.equals(cotisation.getAssuranceId())) {
                            continue;
                        }
                        oldId = cotisation.getAssuranceId();
                        // List tauxList = cotisation.getTauxList("31.12." +
                        // annee);
                        AFTauxAssurance tauxAssurance = cotisation.findTaux("31.12." + annee, getMasseSalTotal(),
                                getTypeDeclaration(),
                                !getTypeDeclaration().equals(DSDeclarationViewBean.CS_COMPLEMENTAIRE), false);

                        // On ne prend que les assurances paritaires
                        if (cotisation.getAssurance().getAssuranceGenre().equalsIgnoreCase("801001")
                                && cotisation.getAssurance().isAssurance13().booleanValue()
                                // && tauxList.size() > 0) {
                                && (tauxAssurance != null)) {
                            // On crée pour chaque assurance une ligne de
                            // déclaration
                            DSLigneDeclarationViewBean ligneDec = new DSLigneDeclarationViewBean();
                            ligneDec.setIdDeclaration(getIdDeclaration());
                            // S'il s'agit de l'assurance chômage on prend
                            // masseACTotal
                            if (cotisation.getAssurance().getTypeAssurance().equals(CodeSystem.TYPE_ASS_COTISATION_AC)
                                    || cotisation.getAssurance().getTypeAssurance().equals(CodeSystem.TYPE_ASS_LAA)) {
                                ligneDec.setMontantDeclaration(getMasseACTotal());
                            } else if (cotisation.getAssurance().getTypeAssurance()
                                    .equals(CodeSystem.TYPE_ASS_COTISATION_AC2)) {
                                ligneDec.setMontantDeclaration(getMasseAC2Total());
                            } else {
                                ligneDec.setMontantDeclaration(getMasseSalTotal());
                            }
                            if (cotisation.getAssurance().getAssuranceReference() != null) {
                                String dateDebut = "01.01." + annee;
                                String dateFin = "31.12." + annee;
                                double montant = 0;
                                montant = AFCalculAssurance.calculResultatAssurance(dateDebut, dateFin, cotisation
                                        .getAssurance().getAssuranceReference().getTaux(dateFin), new Double(
                                        getMasseSalTotalWhitoutFormat()).doubleValue(), getSession());
                                BigDecimal montantArr = new BigDecimal(montant);
                                montantArr = JANumberFormatter.round(montantArr, 0.05, 2, JANumberFormatter.NEAR);
                                ligneDec.setMontantDeclaration(montantArr.toString());
                            }
                            ligneDec.setAssuranceId(cotisation.getAssuranceId());
                            ligneDec.setAnneCotisation(getAnnee());
                            ligneDec.add(transaction);
                        }
                    }
                    // Activer un calcul spécial de bonus pour les décomptes 13
                    // (Seulement pour certaines caisses)
                    if ("true"
                            .equalsIgnoreCase(((DSApplication) getSession().getApplication()).getProperty("bonusPFA"))) {
                        getBonus(transaction, getAnnee());
                    }
                }
            } catch (Exception ex) {
                JadeLogger.error(this, ex);
                // retourne liste vide
            }
        }
        // Lorsqu'on reçoit la déclaration, on va transferer la date de
        // réception de la déclaration dans LEO afin de cloturer l'envoi

        if (!JadeStringUtil.isEmpty(getDateRetourEff())
                && (DSDeclarationViewBean.CS_PRINCIPALE.equals(getTypeDeclaration()) || DSDeclarationViewBean.CS_LTN
                        .equals(getTypeDeclaration()))) {
            AFAffiliation affiliation = getAffiliation();
            // On recherche dans la gestion des envois s'il y a un envoi qui
            // concerne le relevé qu'on est en train d'ajouter
            LUJournalListViewBean viewBean = new LUJournalListViewBean();
            // On sette les données nécessaires à la recherche de l'envoi
            LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, affiliation.getIdTiers());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, affiliation.getAffilieNumero());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                    DSApplication.DEFAULT_APPLICATION_DRACO);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, affiliation.getIdTiers());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, annee);
            // On va regarder s'il y a déjà un envoi correspondant aux critères
            // qui nous intéressent.
            viewBean.setSession(getSession());
            viewBean.setProvenance(provenanceCriteres);
            viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_CATEGORIE_GROUPE);
            if (CodeSystem.TYPE_AFFILI_LTN.equals(affiliation.getTypeAffiliation())) {
                viewBean.setForValeurCodeSysteme(ILEConstantes.CS_CATEGORIE_SUIVI_DS_LTN);

            } else {
                viewBean.setForValeurCodeSysteme(ILEConstantes.CS_CATEGORIE_SUIVI_DS);
            }

            viewBean.find();
            // Dans le cas où il existe un envoi, on va mettre la date de
            // réception dans LEO
            if (viewBean.size() > 0) {

                LUJournalViewBean vBean = new LUJournalViewBean();
                // Comme les critères entrés assurent l'unicité de l'envoi, on
                // est sûrs qu'une seule ligne est retournée par la requete
                vBean = (LUJournalViewBean) viewBean.getEntity(0);
                // On génère la reception du document dans LEO avec la date du
                // jour
                // Création d'une session LEO
                BIApplication remoteApplication = GlobazServer.getCurrentSystem().getApplication("LEO");
                BSession sessionLeo = (BSession) remoteApplication.newSession(getSession());
                LEJournalHandler journalHandler = new LEJournalHandler();
                journalHandler.genererJournalisationReception(vBean.getIdJournalisation(), getDateRetourEff(),
                        sessionLeo, transaction);
            }
        }

    }

    @Override
    protected void _afterRetrieve(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // recherche le canton des coti AF dans le plan de l'employeur
        if (!DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(getTypeDeclaration())) {
            codeCantonAF = AFAffiliationUtil.getCantonAFCSForDS(getAffiliation(), "31.12." + getAnnee());
        } else {
            codeCantonAF = AFAffiliationUtil.getCantonAFCSForDS(getAffiliation(),
                    "31.12." + new JADate(JACalendar.todayJJsMMsAAAA()).getYear());
        }

        if (JadeStringUtil.isEmpty(codeCantonAF)) {
            // si pas trouvé, utiliser la valeur par défaut
            DSApplication application = (DSApplication) globaz.globall.db.GlobazServer.getCurrentSystem()
                    .getApplication(DSApplication.DEFAULT_APPLICATION_DRACO);
            codeCantonAF = application.getCodeCantonAF();

        }
    }

    @Override
    protected void _afterUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // Incrémente de +1 le numéro
        setIdDeclaration(this._incCounter(transaction, "0"));
        // Lors d'un ajout on force l'état à ouvert
        setEtat(DSDeclarationViewBean.CS_OUVERT);
        if (isSaisieEcran()) {
            // Lors de saisie à l'écran
            setCodeSuspendu(DSDeclarationViewBean.CS_MANUELLE);
        } else {
            // Lors d'une saisie par préimpression
            setCodeSuspendu(DSDeclarationViewBean.CS_AUTOMATIQUE);
        }
        // Contrôle que l'affilié existe vraiment
        if (JadeStringUtil.isBlank(getAffiliationId())) {
            // throw new
            // Exception(getSession().getLabel("DECL_AFFILIE_NON_EXIST"));
            _addError(transaction, getSession().getLabel("DECL_AFFILIE_NON_EXIST"));
            return;
        }
        // Contôler qu'il n'y a pas deux déclarations principales identiques
        // ACR: Idem pour les LTN
        if (getTypeDeclaration().equalsIgnoreCase(DSDeclarationViewBean.CS_PRINCIPALE)
                || DSDeclarationViewBean.CS_LTN.equalsIgnoreCase(getTypeDeclaration())) {
            DSDeclarationListViewBean declaration = new DSDeclarationListViewBean();
            declaration.setSession(getSession());
            declaration.setForAnnee(getAnnee());
            declaration.setForTypeDeclaration(getTypeDeclaration());
            declaration.setForAffiliationId(getAffiliationId());
            if (declaration.getCount(transaction) != 0) {
                _addError(transaction, getSession().getLabel("DECL_EXISTE_DEJA"));
            }
        }
        /*
         * Contrôler qu'il n'existe pas de déclaration pour cet affilié et cette année encore ouverte
         */
        DSDeclarationListViewBean declaration = new DSDeclarationListViewBean();
        declaration.setSession(getSession());
        //
        if (!DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(getTypeDeclaration())) {
            declaration.setForAnnee(getAnnee());
        } else {
            declaration.setForTypeDeclaration(DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR);
        }
        declaration.setForAffiliationId(getAffiliationId());
        declaration.setForEtat(DSDeclarationViewBean.CS_OUVERT);
        declaration.setForTypeDeclaration(getTypeDeclaration());
        if (declaration.getCount(transaction) != 0) {
            _addError(transaction, getSession().getLabel("DECL_OUVERTE_DEJA_EXISTANTE"));
        }
        // Contrôler que l'année de la déclaration soit dans la période
        // d'affiliation
        // Contrôle d'employeur : on ne connait pas l'année => pas de plausi
        if (!DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(getTypeDeclaration())) {
            AFAffiliation affiliation = getAffiliation();
            String sDebut = affiliation.getDateDebut();
            String sFin = affiliation.getDateFin();
            Integer iDebut = null;
            Integer iFin = null;
            Integer iAnnee = null;
            if (!JadeStringUtil.isBlank(sDebut) && !sDebut.equals("0")) {
                sDebut = sDebut.substring(6);
                iDebut = new Integer(sDebut);
            } else {
                iDebut = new Integer(0);
            }
            if (!JadeStringUtil.isBlank(sFin) && !sFin.equals("0")) {
                sFin = sFin.substring(6);
                iFin = new Integer(sFin);
            } else {
                iFin = new Integer(0);
            }
            if (!JadeStringUtil.isBlank(getAnnee()) && !getAnnee().equals("0")) {
                iAnnee = new Integer(getAnnee());
            } else {
                iAnnee = new Integer(0);
            }
            if (JadeStringUtil.isBlank(sFin) || sFin.equals("0")) {
                if (iAnnee.intValue() < iDebut.intValue()) {
                    _addError(transaction, getSession().getLabel("DECL_PERIODE_NON_VALIDE"));
                }
            } else {
                if ((iAnnee.intValue() < iDebut.intValue()) || (iAnnee.intValue() > iFin.intValue())) {
                    _addError(transaction, getSession().getLabel("DECL_PERIODE_NON_VALIDE"));
                }
            }
        } else {
            if (!JadeStringUtil.isBlankOrZero(getAnnee())) {
                _addError(transaction, getSession().getLabel("ANNEE_CTRL_EMPL"));
            }
            if (JadeStringUtil.isBlankOrZero(getNoDecompte())) {
                _addError(transaction, getSession().getLabel("NO_DECOMPTE_CTRL_EMPL"));
            }
        }
        // Ajout plausi : empêcher principale si contenu dans la liste de
        // propriétés
        if (DSDeclarationViewBean.CS_PRINCIPALE.equals(getTypeDeclaration())) {
            if (!autoriseDecompte13(getSession())) {
                _addError(transaction, getSession().getLabel("BLOQUAGE_13"));
            }
        }
        // S'il existe une particularité "fiche partielle" on affiche une erreur
        if (!JadeStringUtil.isBlankOrZero(getAffiliationId())) {
            AFParticulariteAffiliationManager particulariteMana = new AFParticulariteAffiliationManager();
            particulariteMana.setSession(getSession());
            particulariteMana.setForAffiliationId(getAffiliationId());
            particulariteMana.setForParticularite(CodeSystem.PARTIC_AFFILIE_FICHE_PARTIELLE);
            particulariteMana.find();
            if (particulariteMana.size() > 0) {
                String numAffilie = getNumeroAffilie();
                if (JadeStringUtil.isBlankOrZero(numAffilie)) {
                    numAffilie = getAffilieNumero();
                }
                _addError(
                        transaction,
                        getSession().getLabel("PLAUSI_PARTICULARITE")
                                + getSession().getCodeLibelle(CodeSystem.PARTIC_AFFILIE_FICHE_PARTIELLE)
                                + getSession().getLabel("PLAUSI_PARTICULARITE2") + " " + numAffilie);
            }
        }
        // Lors de la validation ou de la facturation d'un décompte de type 13
        // ou 14
        // On met un message d'erreur si l'affilié a une particularité
        // "Code blocage - Décompte final"
        if (!JadeStringUtil.isBlankOrZero(getTypeDeclaration())) {
            if (DSDeclarationViewBean.CS_PRINCIPALE.equals(getTypeDeclaration())
                    || DSDeclarationViewBean.CS_BOUCLEMENT_ACOMPTE.equals(getTypeDeclaration())) {
                AFParticulariteAffiliationManager particulariteMana = new AFParticulariteAffiliationManager();
                particulariteMana.setSession(getSession());
                particulariteMana.setForAffiliationId(getAffiliationId());
                particulariteMana.setForParticularite(CodeSystem.PARTIC_AFFILIE_CODE_BLOCAGE_DECFINAL);
                particulariteMana.find();
                if (particulariteMana.size() > 0) {
                    String numAffilie = getNumeroAffilie();
                    if (JadeStringUtil.isBlankOrZero(numAffilie)) {
                        numAffilie = getAffilieNumero();
                    }
                    _addError(
                            transaction,
                            getSession().getLabel("PLAUSI_PARTICULARITE")
                                    + getSession().getCodeLibelle(CodeSystem.PARTIC_AFFILIE_CODE_BLOCAGE_DECFINAL)
                                    + getSession().getLabel("PLAUSI_PARTICULARITE2") + " " + numAffilie);
                }
            }
        }
        if (!JadeStringUtil.isBlank(dateRetourEff)) {
            if (BSessionUtil.compareDateFirstLower(getSession(), dateRetourEff, "01.01.1948")) {
                _addError(transaction, getSession().getLabel("MSG_DATE_TROP_PETITE"));
            }
        }

    }

    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws Exception {
        // Contrôler que la déclaration soit dans un état ouvert pour la
        // supprimer
        if (!getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_OUVERT)) {
            _addError(transaction, getSession().getLabel("DECL_SUPPRESSION_ETAT_NON_OUVERT"));
        }
        // Contrôler qu'il n'ait plus de ligne de déclaration pour cette
        // déclaration
        if (!transaction.hasErrors() && !JadeStringUtil.isIntegerEmpty(idDeclaration)) {
            DSLigneDeclarationListViewBean ligne = new DSLigneDeclarationListViewBean();
            ligne.setSession(getSession());
            ligne.setForIdDeclaration(getIdDeclaration());
            ligne.find(transaction);
            for (int i = 0; i < ligne.size(); i++) {
                DSLigneDeclarationViewBean ligneDec = (DSLigneDeclarationViewBean) ligne.getEntity(i);
                ligneDec.delete(transaction);
            }
        }

        if (!transaction.hasErrors() && !JadeStringUtil.isIntegerEmpty(idDeclaration)) {
            DSInscriptionsIndividuellesManager inscMgr = new DSInscriptionsIndividuellesManager();
            inscMgr.setSession(getSession());
            inscMgr.setForIdDeclaration(idDeclaration);
            inscMgr.changeManagerSize(BManager.SIZE_NOLIMIT);
            inscMgr.find(transaction);
            for (int i = 0; i < inscMgr.size(); i++) {
                DSInscriptionsIndividuelles inscInd = (DSInscriptionsIndividuelles) inscMgr.get(i);
                inscInd.delete(transaction);
            }
            // suppression du journal
            if (!transaction.hasErrors() && !JadeStringUtil.isBlankOrZero(idJournal)) {
                CIJournal journalASupp = new CIJournal();
                journalASupp.setIdJournal(idJournal);
                journalASupp.setSession(getSession());
                journalASupp.retrieve(transaction);
                if (!journalASupp.isNew() && CIJournal.CS_OUVERT.equals(journalASupp.getIdEtat())) {
                    journalASupp.wantCallMethodBefore(false);
                    journalASupp.delete(transaction);
                }

            }
        }
    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws java.lang.Exception {

        // On force l'update. Condition ajoutée pour lettre NSS
        if (isForceUpdate()) {
            return;
        }

        // Aucune mise à jour si état validé ou comptabilisé
        if (isEnValidation()) {
            if (!getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_AFACTURER)) {
                _addError(transaction, getSession().getLabel("DECL_EST_TRAITEE"));
            }
        } else if (!isEnFacturation()) {
            if (getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_AFACTURER)
                    || getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_COMPTABILISE)) {
                _addError(transaction, getSession().getLabel("DECL_EST_TRAITEE"));
            }
        }
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "DSDECLP INNER JOIN " + _getCollection() + "AFAFFIP ON (" + _getCollection()
                + "DSDECLP.MAIAFF=" + _getCollection() + "AFAFFIP.MAIAFF)" + " INNER JOIN " + _getCollection()
                + "TITIERP ON (" + _getCollection() + "AFAFFIP.HTITIE=" + _getCollection() + "TITIERP.HTITIE)";

    }

    @Override
    protected String _getTableName() {
        return "DSDECLP";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDeclaration = statement.dbReadNumeric("TAIDDE");
        affiliationId = statement.dbReadNumeric("MAIAFF");
        idPassageFac = statement.dbReadNumeric("EAID");
        noDecompte = statement.dbReadNumeric("TANDEC");
        typeDeclaration = statement.dbReadNumeric("TATTYP");
        etat = statement.dbReadNumeric("TATETA");
        masseSalTotal = statement.dbReadNumeric("TAMMAS", 2);
        masseACTotal = statement.dbReadNumeric("TAMACT", 2);
        dateRetourDes = statement.dbReadDateAMJ("TADRDE");
        dateRetourEff = statement.dbReadDateAMJ("TADREF");
        annee = statement.dbReadNumeric("TAANNE");
        codeSuspendu = statement.dbReadNumeric("TATCDS");
        dateFinSuspendu = statement.dbReadDateAMJ("TADFIS");
        motifSuspendu = statement.dbReadString("TALMOT");
        idJournal = statement.dbReadNumeric("KCID");
        masseAC2Total = statement.dbReadNumeric("TAMAAT", 2);
        totalControleDS = statement.dbReadNumeric("TAMCON", 2);
        soumisInteret = statement.dbReadNumeric("TATCOD");
        numeroAffilie = statement.dbReadString("MALNAF");
        designation1 = statement.dbReadString("HTLDE1");
        nbPersonnel = statement.dbReadNumeric("TANNBR");
        totalControleAc = statement.dbReadNumeric("TAMCAC", 2);
        totalControleAf = statement.dbReadNumeric("TAMCAF", 2);
        idDeclarationDistante = statement.dbReadNumeric("TAIDIS");
        affilieNumero = numeroAffilie;
        forControleEmployeurId = statement.dbReadNumeric("TAICTR");
        dateImpressionAttestations = statement.dbReadDateAMJ("TADATT");
        dateImpressionDecompteImpots = statement.dbReadDateAMJ("TADANT");
        triEcran = statement.dbReadString("TELTRI");
        validationSpy = statement.dbReadString("TALSPY");
        /** V_DATE_SPY */
        validationDateSpy = statement.dbReadString("TACSP");
        massePeriode = statement.dbReadNumeric("TANPER");
        // Si le tri n'a pas été changé à l'écran, on prend le tri en db
        if (JadeStringUtil.isBlankOrZero(tri)) {
            tri = triEcran;
        }

        provenance = statement.dbReadString("TAPROV");
        idPucsFile = statement.dbReadString("TAIDPU");
        notImpressionDecFinalAZero = statement.dbReadBoolean("NOIDFO");

        // Champs pour lettre reclamation nss
        dateEnvoiLettre = statement.dbReadDateAMJ("TADAEL");
        dateEnvoiRappel = statement.dbReadDateAMJ("TADAER");
        nbRappel = statement.dbReadNumeric("TANBRP");

        // Année de référence pour le taux
        anneeTaux = statement.dbReadNumeric("TAANTX");
        // Référence facture - inForom 282
        referenceFacture = statement.dbReadString("TARFAC");
        idTiers = statement.dbReadString("HTITIE");
    }

    /**
     * Methode permettant de rechercher des messages de warnings qui seront affichées à l'écran
     */
    public void fillWarningMessage() {

        StringBuilder messageBuilder = new StringBuilder();

        if (JadeStringUtil.isEmpty(getAnnee())) {
            return;
        }

        try {

            // Si DS 13, on recherche si releve 13 ou 14 à l'état facturer
            // ou si DS de type 14 a l'état facturé pour afficher un warning a l'écran
            if (DSDeclarationViewBean.CS_PRINCIPALE.equals(getTypeDeclaration())) {

                int annee = Integer.parseInt(getAnnee());

                if (AFReleve1314Checker.hasReleveAFacturerOuValide(CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE, annee,
                        getAffilieNumero(), "", getSession())) {
                    messageBuilder.append(getSession().getLabel("RELEVE_AVERTISSEMENT_TYPE_14_EXISTE_DEJA")).append(
                            "<br />");
                }

                if (AFReleve1314Checker.hasReleveAFacturerOuValide(CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA, annee,
                        getAffilieNumero(), "", getSession())) {
                    messageBuilder.append(getSession().getLabel("RELEVE_AVERTISSEMENT_TYPE_13_EXISTE_DEJA")).append(
                            "<br />");
                }

                if (AFReleve1314Checker.hasDeclarationSalaireAFacturer(DSDeclarationViewBean.CS_BOUCLEMENT_ACOMPTE,
                        annee, getAffiliationId(), getSession(), getIdDeclaration())) {
                    messageBuilder.append(getSession().getLabel("DECLARATION_AVERTISSEMENT_TYPE_14_EXISTE_DEJA"))
                            .append("<br />");
                }

            } else if (DSDeclarationViewBean.CS_BOUCLEMENT_ACOMPTE.equals(getTypeDeclaration())) {

                int annee = Integer.parseInt(getAnnee());

                if (AFReleve1314Checker.hasReleveAFacturerOuValide(CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE, annee,
                        getAffilieNumero(), getSession())) {
                    messageBuilder.append(getSession().getLabel("RELEVE_AVERTISSEMENT_TYPE_14_EXISTE_DEJA")).append(
                            "<br />");
                }

                if (AFReleve1314Checker.hasReleveAFacturerOuValide(CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA, annee,
                        getAffilieNumero(), getSession())) {
                    messageBuilder.append(getSession().getLabel("RELEVE_AVERTISSEMENT_TYPE_13_EXISTE_DEJA")).append(
                            "<br />");
                }
            }
        } catch (Exception e) {
            // On fait rien car on doit pas bloquer l'affichage
            JadeLogger.warn("Unabled to find releve or declaration 13/14 for warning message", e);
        }
        setWarningMessage(messageBuilder.toString());

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // S'il ne s'agit pas d'une saisie écran on effectue
        // les contrôles suivants
        if (!isSaisieEcran()) {
            // Contrôle de l'année à traiter
            if (JadeStringUtil.isBlank(getAnnee())
                    && !DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(typeDeclaration)) {
                _propertyMandatory(statement.getTransaction(), getAnnee(), getSession()
                        .getLabel("DECL_ANNEE_A_TRAITER"));
            }

            if (!isForceUpdate()) {
                if (isEnValidation()) {
                    if (!getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_AFACTURER)) {
                        _addError(statement.getTransaction(), getSession().getLabel("DECL_EST_TRAITEE"));
                    }
                } else if (!isEnFacturation()) {
                    if (getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_AFACTURER)
                            || getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_COMPTABILISE)) {
                        _addError(statement.getTransaction(), getSession().getLabel("DECL_EST_TRAITEE"));
                    }
                }
            }
        } else {
            // En cas de saisie écran
            // Contrôle qu'un numéro d'affilié soit introduit
            if (JadeStringUtil.isIntegerEmpty(getAffiliationId())) {
                _propertyMandatory(statement.getTransaction(), getAffiliationId(),
                        getSession().getLabel("DECL_AFFILIE_NON_RENS"));
            }
            // Contrôle de l'année
            int annee = 0;
            if (!JadeStringUtil.isBlank(getAnnee())) {
                annee = new Integer(getAnnee().trim()).intValue();
            } else {
                if (!DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(typeDeclaration)) {
                    _propertyMandatory(statement.getTransaction(), getAnnee(),
                            getSession().getLabel("DECL_ANNEE_A_TRAITER"));
                }
            }
            if (JAUtil.isDateEmpty(getDateRetourEff())) {
                _addError(statement.getTransaction(), getSession().getLabel("DECL_DATE_RETOUR_NON_RENS"));
            }
            if (getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_AFACTURER)
                    || getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_COMPTABILISE)) {
                _addError(statement.getTransaction(), getSession().getLabel("DECL_EST_TRAITEE"));
            }

            // Test de l'année de référence qui ne doit pas être plus grande que l'année de la déclaration
            if (DSDeclarationViewBean.CS_SALAIRE_DIFFERES.equals(typeDeclaration)) {
                if (JadeStringUtil.isBlankOrZero(getAnneeTaux())) {
                    _addError(statement.getTransaction(), getSession().getLabel("DECL_ANNEE_TAUX_NON_RENS"));
                } else {
                    int anneeTaux = new Integer(getAnneeTaux().trim()).intValue();
                    if (anneeTaux < annee) {
                        _addError(statement.getTransaction(), getSession().getLabel("DECL_ANNEE_TAUX_PLUS_PETIT"));
                    }
                    int anneeCourante = JACalendar.getYear(JACalendar.todayJJsMMsAAAA());
                    if (anneeTaux > anneeCourante) {
                        _addError(statement.getTransaction(),
                                getSession().getLabel("DECL_ANNEE_TAUX_SUP_ANNEE_COURANTE"));
                    }
                }
            }

            if (AFReleve1314Checker.hasDeclarationSalaireAFacturer(DSDeclarationViewBean.CS_BOUCLEMENT_ACOMPTE, annee,
                    getAffilieNumero(), getSession(), getIdDeclaration())) {

                setWarningMessage(getSession().getLabel("DECLARATION_AVERTISSEMENT_TYPE_14_EXISTE_DEJA"));
            }

        }
        if (!JadeStringUtil.isBlankOrZero(noDecompte)) {
            int longDecompte;
            longDecompte = noDecompte.length();
            if (longDecompte < 9) {
                _addError(statement.getTransaction(), getSession().getLabel("PLAUSI_NO_DECOMPTE_NON_VALIDE"));
            }
        }
        // Dans le cas où le montant AVS n'est pas vide et que le montant AC est
        // vide, le montant AVS est égal au montant AC
        if (JadeStringUtil.isBlank(masseACTotal) && !JadeStringUtil.isBlank(masseSalTotal)) {
            setMasseACTotal(masseSalTotal);
        }
        if (!JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            CIJournal journal = new CIJournal();
            journal.setSession((BSession) getSessionCI(getSession()));
            journal.setIdJournal(getIdJournal());
            journal.retrieve(statement.getTransaction());
            if (!journal.isNew()) {
                journal.setTotalControle(getTotalControleDS());
                journal.update(statement.getTransaction());
            }

        }
        // Lors de la validation ou de la facturation d'un décompte de type 13
        // ou 14
        // On met un message d'erreur si l'affilié a une particularité
        // "Code blocage - Décompte final"
        if (!JadeStringUtil.isBlankOrZero(getTypeDeclaration())) {
            if (DSDeclarationViewBean.CS_PRINCIPALE.equals(getTypeDeclaration())
                    || DSDeclarationViewBean.CS_BOUCLEMENT_ACOMPTE.equals(getTypeDeclaration())) {
                AFParticulariteAffiliationManager particulariteMana = new AFParticulariteAffiliationManager();
                particulariteMana.setSession(getSession());
                particulariteMana.setForAffiliationId(getAffiliationId());
                particulariteMana.setForParticularite(CodeSystem.PARTIC_AFFILIE_CODE_BLOCAGE_DECFINAL);
                particulariteMana.find();
                if (particulariteMana.size() > 0) {
                    _addError(statement.getTransaction(), getSession().getLabel("PLAUSI_PARTICULARITE")
                            + getSession().getCodeLibelle(CodeSystem.PARTIC_AFFILIE_CODE_BLOCAGE_DECFINAL)
                            + getSession().getLabel("PLAUSI_PARTICULARITE2") + " " + getNumeroAffilie());
                }
            }
        }
        AFAffiliation affiliation = getAffiliation();
        if ((getTypeDeclaration().equalsIgnoreCase(DSDeclarationViewBean.CS_LTN) || getTypeDeclaration()
                .equalsIgnoreCase(DSDeclarationViewBean.CS_LTN_COMPLEMENTAIRE))
                && !affiliation.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_LTN)) {
            _addError(statement.getTransaction(), getSession().getLabel("PLAUSI_AFFILIE_LTN"));
        }

        if (affiliation.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_LTN)
                && (!getTypeDeclaration().equalsIgnoreCase(DSDeclarationViewBean.CS_LTN))
                && !getTypeDeclaration().equalsIgnoreCase(DSDeclarationViewBean.CS_LTN_COMPLEMENTAIRE)) {
            _addError(statement.getTransaction(), getSession().getLabel("PLAUSI_DECL_LTN"));
        }

        // Si LTN(33) => vérification que le montant de controle ne dépasse pas le seuil LTN (pour l'affilié)
        if (getTypeDeclaration().equalsIgnoreCase(DSDeclarationViewBean.CS_LTN)
                && !JadeStringUtil.isEmpty(getTotalControleDS())) {
            // Recherche du seuil LTN pour l'affilié
            FWFindParameterManager param = new FWFindParameterManager();
            param.setSession(getSession());
            param.setIdApplParametre(getSession().getApplicationId());
            param.setIdCodeSysteme(DSDeclarationViewBean.CS_PLAFOND_LTN_AFFILIE);
            param.setIdCleDiffere("PLALTNAFF");
            param.setIdActeurParametre("0");
            param.setPlageValDeParametre("0");
            param.setDateDebutValidite("01.01." + getAnnee());
            param.find();
            if (param.size() > 0) {
                String plafondLtnAffilie = ((FWFindParameter) param.getFirstEntity()).getValeurNumParametre();
                if (Double.valueOf(getTotalControleDS()).doubleValue() > Double.valueOf(plafondLtnAffilie)
                        .doubleValue()) {
                    _addError(statement.getTransaction(), getSession().getLabel("PLAFOND_LTN_AFFILIE") + " ("
                            + plafondLtnAffilie + ")");
                }
            }
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("TAIDDE", this._dbWriteNumeric(statement.getTransaction(), getIdDeclaration(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("TAIDDE",
                this._dbWriteNumeric(statement.getTransaction(), getIdDeclaration(), "idDeclaration"));
        statement.writeField("MAIAFF",
                this._dbWriteNumeric(statement.getTransaction(), getAffiliationId(), "affiliationId"));
        statement.writeField("EAID",
                this._dbWriteNumeric(statement.getTransaction(), getIdPassageFac(), "idPassageFac"));
        statement.writeField("TANDEC", this._dbWriteNumeric(statement.getTransaction(), getNoDecompte(), "noDecompte"));
        statement.writeField("TATTYP",
                this._dbWriteNumeric(statement.getTransaction(), getTypeDeclaration(), "typeDeclaration"));
        statement.writeField("TATETA", this._dbWriteNumeric(statement.getTransaction(), getEtat(), "etat"));
        statement.writeField("TAMMAS",
                this._dbWriteNumeric(statement.getTransaction(), getMasseSalTotal(), "masseSalTotal"));
        statement.writeField("TAMACT",
                this._dbWriteNumeric(statement.getTransaction(), getMasseACTotal(), "masseACTotal"));
        statement.writeField("TADRDE",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateRetourDes(), "dateRetourDes"));
        statement.writeField("TADREF",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateRetourEff(), "dateRetourEff"));
        statement.writeField("TAANNE", this._dbWriteNumeric(statement.getTransaction(), getAnnee(), "annee"));
        statement.writeField("TATCDS",
                this._dbWriteNumeric(statement.getTransaction(), getCodeSuspendu(), "codeSuspendu"));
        statement.writeField("TADFIS",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateFinSuspendu(), "dateFinSuspendu"));
        statement.writeField("TALMOT",
                this._dbWriteString(statement.getTransaction(), getMotifSuspendu(), "motifSuspendu"));
        statement.writeField("KCID", this._dbWriteNumeric(statement.getTransaction(), getIdJournal(), "idJournal"));
        statement.writeField("TAMAAT",
                this._dbWriteNumeric(statement.getTransaction(), getMasseAC2Total(), "masseACII"));
        statement.writeField("TAMCON",
                this._dbWriteNumeric(statement.getTransaction(), getTotalControleDS(), "totalControle"));
        statement.writeField("TATCOD",
                this._dbWriteNumeric(statement.getTransaction(), getSoumisInteret(), "codeSoumis"));
        statement.writeField("TANNBR",
                this._dbWriteNumeric(statement.getTransaction(), getNbPersonnel(), "nombrePersonnel"));
        statement.writeField("TAMCAC",
                this._dbWriteNumeric(statement.getTransaction(), getTotalControleAc(), "totalControleAc"));
        statement.writeField("TAMCAF",
                this._dbWriteNumeric(statement.getTransaction(), getTotalControleAf(), "totalControleAf"));
        statement.writeField("TAIDIS",
                this._dbWriteNumeric(statement.getTransaction(), getIdDeclarationDistante(), "idDeclDistante"));
        statement
                .writeField("TAICTR", this._dbWriteNumeric(statement.getTransaction(), getForControleEmployeurId(),
                        "forControleEmployeurId"));
        statement.writeField("TADATT", this._dbWriteDateAMJ(statement.getTransaction(),
                getDateImpressionAttestations(), "dateImpressionAttestations"));
        statement.writeField("TADANT", this._dbWriteDateAMJ(statement.getTransaction(),
                getDateImpressionDecompteImpots(), "dateImpressionDecompteImpots"));
        statement.writeField("TACSP",
                this._dbWriteString(statement.getTransaction(), getValidationDateSpy(), "validationDateSpy"));
        // PO 9084
        String varString = getValidationSpy();
        if (getValidationSpy().length() > 24) {
            varString = getValidationSpy().substring(0, 23);
        }
        statement.writeField("TALSPY", this._dbWriteString(statement.getTransaction(), varString, "validationSpy"));
        statement.writeField("TELTRI", this._dbWriteString(statement.getTransaction(), getTri(), "tri"));
        statement.writeField("TAPROV", this._dbWriteString(statement.getTransaction(), getProvenance(), "provenance"));
        statement.writeField("TAIDPU", this._dbWriteString(statement.getTransaction(), getIdPucsFile(), "idPucsFile"));
        statement.writeField("NOIDFO", this._dbWriteBoolean(statement.getTransaction(),
                getNotImpressionDecFinalAZero(), BConstants.DB_TYPE_BOOLEAN_CHAR, "notImpressionDecFinalAZero"));
        statement.writeField("TANPER",
                this._dbWriteNumeric(statement.getTransaction(), getMassePeriode(), "massePeriode"));

        // Champs pour lettre reclamation nss
        statement.writeField("TADAEL",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateEnvoiLettre(), dateEnvoiLettre));
        statement.writeField("TADAER",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateEnvoiRappel(), dateEnvoiRappel));
        statement.writeField("TANBRP", this._dbWriteNumeric(statement.getTransaction(), getNbRappel(), "nbRappel"));

        statement.writeField("TAANTX", this._dbWriteNumeric(statement.getTransaction(), getAnneeTaux(), "anneeTaux"));
        statement.writeField("TARFAC",
                this._dbWriteString(statement.getTransaction(), getReferenceFacture(), "referenceFacture"));
    }

    private void addLigneReduction(BTransaction transaction, BigDecimal totalCotisation, String typeAssurance,
            boolean cotisationPresente, String anneeDeclaration) throws Exception {

        DSLigneDeclarationViewBean ligneReduction = new DSLigneDeclarationViewBean();
        ligneReduction.setSession(getSession());
        ligneReduction.setIdDeclaration(getIdDeclaration());
        ligneReduction.setAnneCotisation(anneeDeclaration);
        if (cotisationPresente) {
            ligneReduction.setMontantDeclaration(totalCotisation.toString());
        }
        AFAssurance assurance = AFAssuranceServices.findFirstAssuranceSelonType(getSession(), typeAssurance);
        if (assurance != null) {
            ligneReduction.setAssuranceId(assurance.getAssuranceId());
            if (!ligneReduction.getCotisationDue().equals("0.00")) {
                ligneReduction.add(transaction);
            }
        }
    }

    /**
     * @param session
     * @return false si l'utilisateur n'a pas les droits de modifier les interets.
     */
    public boolean afficheInteret(BSession session) {
        return !session.hasRight(DSDeclarationViewBean.ELEMENT_NOT_INTERET_CHOIX, FWSecureConstants.ADD);
    }

    public boolean autoriseDecompte13(BSession session) {
        ArrayList<?> array = DSUtil.getCSBloquage13(getSession());
        // Si array null, on autorise car propriété absente
        if (array == null) {
            return true;
        }
        return !array.contains(getAffiliation().getDeclarationSalaire());
    }

    /**
     * Méthode qui définit s'il existe une coti de type cotisation période pour une année
     * 
     * @return
     * @throws Exception
     */
    public boolean existeCotisationPeriode() throws Exception {
        // FIXME Uniquement pour 2011
        AFCotisationManager cotiMgr = new AFCotisationManager();
        cotiMgr.setSession(getSession());
        cotiMgr.setForAffiliationId(getAffiliationId());
        cotiMgr.setForAnneeActive("2011");
        cotiMgr.setForTypeAssurance(CodeSystem.TYPE_ASS_PC_FAMILLE);
        return (cotiMgr.getCount() > 0)
                && ("2011".equals(annee) || DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(getTypeDeclaration()));
    }

    private CEControleEmployeurManager findCEControleEmployeur() throws Exception {
        CEControleEmployeurManager controleManager = new CEControleEmployeurManager();
        controleManager.setSession(getSession());
        controleManager.setForControleEmployeurId(getForControleEmployeurId());
        controleManager.find();

        return controleManager;
    }

    /**
     * @return la date de comptabilisation contenue dans le passage
     * @throws Exception
     */
    public String findDateCompta() throws Exception {
        String dateCompta = "";

        if (!JadeStringUtil.isBlank(getIdPassageFac())) {
            FAPassage passage = new FAPassage();
            passage.setSession(getSession());
            passage.setId(getIdPassageFac());
            try {
                passage.retrieve();
            } catch (Exception e) {
                throw new Exception("Unable to findDateCompta for the DS!!!");
            }

            if (!JadeStringUtil.isBlank(passage.getDateFacturation())) {
                dateCompta = passage.getDateFacturation();
            }
        }

        return dateCompta;
    }

    /**
     * @return la date d'envoi de la gestion du suivi (l'impression de l'attestation fait foi)
     * @throws Exception
     */
    public String findDateEnvoi() throws Exception {
        String dateEnvoi = "";

        if (DSDeclarationViewBean.CS_PRINCIPALE.equals(getTypeDeclaration())
                || DSDeclarationViewBean.CS_LTN.equals(getTypeDeclaration())) {
            AFAffiliation affiliation = getAffiliation();
            LUJournalListViewBean viewBean = new LUJournalListViewBean();

            // On renseigne les données de provenance afin de retrouver l'étape voulue
            LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, affiliation.getIdTiers());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, affiliation.getAffilieNumero());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                    DSApplication.DEFAULT_APPLICATION_DRACO);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, affiliation.getIdTiers());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, annee);

            viewBean.setSession(getSession());
            viewBean.setProvenance(provenanceCriteres);
            viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_DEF_FORMULE_GROUPE);
            if (CodeSystem.TYPE_AFFILI_LTN.equals(affiliation.getTypeAffiliation())) {
                viewBean.setForValeurCodeSysteme(ILEConstantes.CS_DEF_FORMULE_ATT_DS_LTN);
            } else {
                viewBean.setForValeurCodeSysteme(ILEConstantes.CS_DEF_FORMULE_ATT_DS);
            }

            viewBean.find();
            if (viewBean.size() > 0) {
                LUJournalViewBean journalViewBean = (LUJournalViewBean) viewBean.getEntity(0);
                dateEnvoi = journalViewBean.getDate();
            }
        }
        return dateEnvoi;
    }

    /**
     * @return la date de sommation de la gestion du suivi
     * @throws Exception
     */
    public String findDateSommation() throws Exception {
        String dateSommation = "";

        if (DSDeclarationViewBean.CS_PRINCIPALE.equals(getTypeDeclaration())
                || DSDeclarationViewBean.CS_LTN.equals(getTypeDeclaration())) {
            AFAffiliation affiliation = getAffiliation();
            LUJournalListViewBean viewBean = new LUJournalListViewBean();

            // On renseigne les données de provenance afin de retrouver l'étape voulue
            LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, affiliation.getIdTiers());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, affiliation.getAffilieNumero());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                    DSApplication.DEFAULT_APPLICATION_DRACO);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, affiliation.getIdTiers());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, annee);

            viewBean.setSession(getSession());
            viewBean.setProvenance(provenanceCriteres);
            viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_DEF_FORMULE_GROUPE);
            if (CodeSystem.TYPE_AFFILI_LTN.equals(affiliation.getTypeAffiliation())) {
                viewBean.setForValeurCodeSysteme(ILEConstantes.CS_DEF_FORMULE_SOMMATION_DS_LTN);
            } else {
                viewBean.setForValeurCodeSysteme(ILEConstantes.CS_DEF_FORMULE_SOMMATION_DS);
            }

            viewBean.find();
            if (viewBean.size() > 0) {
                LUJournalViewBean journalViewBean = (LUJournalViewBean) viewBean.getEntity(0);
                dateSommation = journalViewBean.getDate();
            }
        }
        return dateSommation;
    }

    private List<String> findListIdAssuranceReduction() throws Exception {
        String param = "";
        try {
            param = FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(),
                    DSDeclarationViewBean.CS_ID_ASSURANCE_A_IGNORER, "ASSAF", "01.01." + String.valueOf(getAnnee()),
                    "0", 0);
        } catch (Exception e) {
            param = "";
        }

        List<String> listIdAssuranceIgnore = new ArrayList<String>();
        // Plusieurs
        if (!JadeStringUtil.isEmpty(param)) {
            StringTokenizer token = new StringTokenizer(param, ",");
            while (token.hasMoreElements()) {
                listIdAssuranceIgnore.add((String) token.nextElement());
            }
        }
        return listIdAssuranceIgnore;
    }

    /**
     * Permet de récupérer un affilié
     * 
     * @return Returns a AFAffiliation
     */
    public AFAffiliation getAffiliation() {
        // Si affiliation n'est pas déjà instanciée
        // if (_affiliation == null) {
        // Chargement du manager
        AFAffiliationManager manager = new AFAffiliationManager();
        manager.setSession(getSession());
        manager.setForAffiliationId(getAffiliationId());
        try {
            manager.find();
            if (!manager.isEmpty()) {
                _affiliation = (AFAffiliation) manager.getEntity(0);
            } else {
                _affiliation = null;
            }
        } catch (Exception e) {
            _addError(null, e.getMessage());
            _affiliation = null;
        }
        // }
        return _affiliation;
    }

    /**
     * Permet de récupérer un affilié (mais retourne vide si on a pas d'id affiliation)
     * 
     * @return Returns a AFAffiliation
     */
    public AFAffiliation getAffiliationAvecVides() {
        // Si affiliation n'est pas déjà instanciée
        // if (_affiliation == null) {
        // Chargement du manager
        if (JadeStringUtil.isBlank(getAffiliationId())) {
            return null;
        } else {
            AFAffiliationManager manager = new AFAffiliationManager();
            manager.setSession(getSession());
            manager.setForAffiliationId(getAffiliationId());
            try {
                manager.find();
                if (!manager.isEmpty()) {
                    _affiliation = (AFAffiliation) manager.getEntity(0);
                } else {
                    _affiliation = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _affiliation = null;
            }
            // }
            return _affiliation;
        }
    }

    public String getAffiliationId() {
        return affiliationId;
    }

    /**
     * Returns the affilieDesEcran.
     * 
     * @return String
     */
    public String getAffilieDesEcran() {
        return getAffiliation().getDateDebut();
    }

    public String getAffilieDesEcranFind() {
        try {
            if (!JadeStringUtil.isIntegerEmpty(affilieDesEcran)) {
                return affilieDesEcran;
            }

            if (JadeStringUtil.isIntegerEmpty(getAffiliationId())) {
                return "";
            } else {
                AFAffiliationManager affManager = new AFAffiliationManager();
                affManager.setSession(getSession());
                affManager.setForAffiliationId(getAffiliationId());
                affManager.find();
                if (affManager.size() > 0) {
                    return ((AFAffiliation) affManager.getFirstEntity()).getDateDebut();
                }
                return "";

            }
        } catch (Exception e) {
            return "";
        }

    }

    public String getAffilieForEcran() {
        try {
            if (!JadeStringUtil.isIntegerEmpty(affilieNumero) && !JadeStringUtil.isIntegerEmpty(getAffiliationId())) {
                return affilieNumero;
            }

            if (JadeStringUtil.isIntegerEmpty(getAffiliationId())) {
                return "";
            } else {
                AFAffiliationManager affManager = new AFAffiliationManager();
                affManager.setSession(getSession());
                affManager.setForAffiliationId(getAffiliationId());
                affManager.find();
                if (affManager.size() > 0) {
                    return ((AFAffiliation) affManager.getFirstEntity()).getAffilieNumero();
                }
                return "";

            }
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * @return
     */
    public String getAffilieNumero() {
        return affilieNumero;
    }

    public String getAffilieRadie() {
        if (getAffiliationAvecVides() != null) {
            return getAffiliationAvecVides().getDateFin();
        } else {
            return "";
        }
    }

    /**
     * Returns the affilieRadieEcran.
     * 
     * @return String
     */
    public String getAffilieRadieEcran() {
        return getAffiliation().getDateFin();
    }

    public String getAffilieRadieFind() {
        try {
            if (!JadeStringUtil.isIntegerEmpty(affilieRadieEcran)) {
                return affilieRadieEcran;
            }

            if (JadeStringUtil.isIntegerEmpty(getAffiliationId())) {
                return "";
            } else {
                AFAffiliationManager affManager = new AFAffiliationManager();
                affManager.setSession(getSession());
                affManager.setForAffiliationId(getAffiliationId());
                affManager.find();
                if (affManager.size() > 0) {
                    return ((AFAffiliation) affManager.getFirstEntity()).getDateFin();
                }
                return "";

            }
        } catch (Exception e) {
            return "";
        }

    }

    public String getAffilieTypeFind() {
        try {
            if (!JadeStringUtil.isIntegerEmpty(typeAffiliationEcran)) {
                return typeAffiliationEcran;
            }

            if (JadeStringUtil.isIntegerEmpty(getAffiliationId())) {
                return "";
            } else {
                AFAffiliationManager affManager = new AFAffiliationManager();
                affManager.setSession(getSession());
                affManager.setForAffiliationId(getAffiliationId());
                affManager.find();
                if (affManager.size() > 0) {
                    return globaz.draco.translation.CodeSystem.getLibelle(getSession(),
                            ((AFAffiliation) affManager.getFirstEntity()).getTypeAffiliation());
                }
                return "";

            }
        } catch (Exception e) {
            return "";
        }

    }

    public String getAnnee() {
        return annee;
    }

    public String getAnneeTaux() {
        return anneeTaux;
    }

    public void getBonus(BTransaction transaction, String anneeDeclaration) throws java.lang.Exception {
        boolean hasDemandeElec = false;
        boolean hasCotiAVS = false;
        boolean hasCotiAF = false;
        boolean hasCotiFA = false;
        String idAssuranceFA = "";
        String idAssuranceAF = "";
        BigDecimal totalCotiAF = BigDecimal.ZERO;
        BigDecimal totalCotiPfa = BigDecimal.ZERO;
        BigDecimal masseAVS = BigDecimal.ZERO;
        // Recherche des diffenrents Id des assurances de réduction à ignorer
        List<String> idAssuranceIgnore = findListIdAssuranceReduction();

        DSLigneDeclarationListViewBean ligneMana = new DSLigneDeclarationListViewBean();
        ligneMana.setSession(getSession());
        ligneMana.setForIdDeclaration(getIdDeclaration());
        ligneMana.find();
        for (int i = 0; i < ligneMana.size(); i++) {
            AFAssurance assurance = new AFAssurance();
            assurance = ((DSLigneDeclarationViewBean) ligneMana.getEntity(i)).getCotisation().getAssurance();
            if (assurance.getAssuranceGenre().equals(CodeSystem.GENRE_ASS_PARITAIRE)) {
                if (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_COTISATION_AVS_AI)) {
                    hasCotiAVS = true;
                    masseAVS = masseAVS.add(new BigDecimal(JANumberFormatter
                            .deQuote(((DSLigneDeclarationViewBean) ligneMana.getEntity(i)).getMontantDeclaration())));
                }
                // soustraire le montant de l'assurance à ingorer
                if (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_COTISATION_AF)) {
                    hasCotiAF = true;
                    idAssuranceAF = assurance.getAssuranceId();
                    if (!idAssuranceIgnore.contains(idAssuranceAF)) {
                        totalCotiAF = totalCotiAF.add(new BigDecimal(((DSLigneDeclarationViewBean) ligneMana
                                .getEntity(i)).getCotisationDue()));
                    }
                }
                if (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_FRAIS_ADMIN)) {
                    hasCotiFA = true;
                    idAssuranceFA = assurance.getAssuranceId();
                    if (!idAssuranceIgnore.contains(idAssuranceFA)) {
                        totalCotiPfa = totalCotiPfa.add(new BigDecimal(((DSLigneDeclarationViewBean) ligneMana
                                .getEntity(i)).getCotisationDue()));
                    }
                }
            }
        }
        AFAffiliation affiliation = new AFAffiliation();
        affiliation.setSession(getSession());
        affiliation.setAffiliationId(getAffiliationId());
        affiliation.retrieve();
        if (affiliation.getDeclarationSalaire().equals(CodeSystem.DS_ENVOIE_SELON_NORME)
                || affiliation.getDeclarationSalaire().equals(CodeSystem.DS_ENVOI_PUCS)
                || affiliation.getDeclarationSalaire().equals(CodeSystem.DS_SWISSDEC)) {
            hasDemandeElec = true;
        }

        // Pour les DAN, vérifier que la caisse veux la réduction (EX. FPV -> ok mias pas pour CCVD)
        if (affiliation.getDeclarationSalaire().equals(CodeSystem.DS_DAN)) {
            try {
                if ("true".equalsIgnoreCase(FWFindParameter.findParameter(transaction, "2", "CIREDDAN", "31.12."
                        + anneeDeclaration, "", 0))) {
                    hasDemandeElec = true;
                }
            } catch (Exception e) {
                hasDemandeElec = false;
            }
        }

        if (hasCotiAVS && hasCotiAF && (Integer.parseInt(anneeDeclaration) < 2009)) {

            addLigneReduction(transaction, totalCotiPfa, CodeSystem.TYPE_ASS_REDPFA_DOUBLE_AFF, hasCotiFA,
                    anneeDeclaration);

            addLigneReduction(transaction, totalCotiAF, CodeSystem.TYPE_ASS_REDCOTI_DOUBLE_AFF, hasCotiAF,
                    anneeDeclaration);

        }
        if (hasDemandeElec) {
            if (hasCotiAVS) {

                if (JadeStringUtil.isBlankOrZero(anneeDeclaration)
                        || JadeStringUtil.isBlankOrZero(DSProperties.PROPERTY_ANNEE_BONUSPFA_VARIABLE.getValue())
                        || Integer.valueOf(DSProperties.PROPERTY_ANNEE_BONUSPFA_VARIABLE.getValue()) > Integer
                                .valueOf(anneeDeclaration)) {

                    addLigneReduction(transaction, totalCotiPfa, CodeSystem.TYPE_ASS_REDPFA_DSE, hasCotiFA,
                            anneeDeclaration);

                } else {
                    addLigneReduction(transaction, masseAVS, CodeSystem.TYPE_ASS_REDPFA_DSE_VARIABLE, hasCotiFA,
                            anneeDeclaration);

                }

            }

        }
    }

    /**
     * @return
     */
    public String getCodeCantonAF() {
        return codeCantonAF;
    }

    public String getCodeSuspendu() {
        return codeSuspendu;
    }

    /**
     * Permet de récuperer un compte annexe Date de création : (22.05.2003 10:12:15)
     * 
     * @return globaz.osiris.db.comptes.CACompteAnnexe
     */
    public globaz.osiris.db.comptes.CACompteAnnexe getCompteAnnexe() {
        // Si _compteAnnexe n'est pas déjà instancié
        if (_compteAnnexe == null) {
            // Chargement du manager
            try {
                CACompteAnnexeManager manager = new CACompteAnnexeManager();
                manager.setSession(getSession());
                // manager.setForIdTiers(getAffiliation().getIdTiers());
                manager.setForIdRole(CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(
                        getSession().getApplication()));
                manager.setForIdExterneRole(getAffiliation().getAffilieNumero());
                manager.find();
                if (!manager.isEmpty()) {
                    _compteAnnexe = (CACompteAnnexe) manager.getEntity(0);
                    /*
                     * }else{ throw new Exception(getSession().getLabel("DECL_ERREUR_LECTURE_CA" )); }
                     */
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
                _addError(null, getSession().getLabel("DECL_ERREUR_LECTURE_CA") + e.getMessage());
                System.out.println("Erreur de lecture de compte annexe");
                _compteAnnexe = null;
            }
        }
        return _compteAnnexe;
    }

    public String getDateEnvoiLettre() {
        return dateEnvoiLettre;
    }

    public String getDateEnvoiRappel() {
        return dateEnvoiRappel;
    }

    public String getDateFinSuspendu() {
        return dateFinSuspendu;
    }

    public String getDateImpressionAttestations() {
        return dateImpressionAttestations;
    }

    public String getDateImpressionDecompteImpots() {
        return dateImpressionDecompteImpots;
    }

    public String getDateRetourDes() {
        return dateRetourDes;
    }

    public String getDateRetourEff() {
        return dateRetourEff;
    }

    public String getdescrionTiersForSaisieInd() {
        return getAffiliation().getAffilieNumero() + "\n" + getDescriptionTiers();
    }

    /**
     * Permet de retourner le label de la provenance suivant la lanque de la session
     * 
     * @return
     */
    public static String getLabelPourProvenance(BSession session, String provenance) {
        String labelReturn = "";

        if (JadeStringUtil.isEmpty(provenance)) {
            return labelReturn;
        }

        if ("1".equals(provenance)) {
            labelReturn = session.getLabel("PROVENANCE_PUCS");
        } else if ("2".equals(provenance)) {
            labelReturn = session.getLabel("PROVENANCE_DAN");
        } else if (DSDeclarationViewBean.PROVENANCE_PUCS_CCJU.equalsIgnoreCase(provenance)) {
            labelReturn = session.getLabel("PROVENANCE_PUCS_CCJU");
        } else if (DSDeclarationViewBean.PROVENANCE_SWISSDEC.equalsIgnoreCase(provenance)) {
            labelReturn = session.getLabel("PROVENANCE_SWISSDEC");
        }
        return labelReturn;
    }

    /**
     * Gets the descriptionTiers
     * 
     * @return Returns a String
     */
    public String getDescriptionTiers() {
        if (JadeStringUtil.isBlank(descriptionTiers) && !JadeStringUtil.isIntegerEmpty(affiliationId)) {
            return descriptionTiers = getAffiliation()._getDescriptionTiers();
        } else {
            return descriptionTiers;
        }
    }

    /**
     * Gets the descriptionTiers but allow blanks
     * 
     * @return Returns a String
     */
    public String getDescriptionTiersAvecVides() {
        if (JadeStringUtil.isBlank(descriptionTiers)) {
            if (getAffiliationAvecVides() == null) {
                return descriptionTiers = "";
            } else {
                return descriptionTiers = getAffiliationAvecVides()._getDescriptionTiers();
            }
        } else {
            return descriptionTiers;
        }
    }

    /**
     * @return
     */
    public String getDesignation1() {
        return designation1;
    }

    public String getEtat() {
        return etat;
    }

    public String getForControleEmployeurId() {
        return forControleEmployeurId;
    }

    public String getForDateEffective() {
        try {
            if (!JadeStringUtil.isIntegerEmpty(fordateEffective)) {
                return fordateEffective;
            }

            if (JadeStringUtil.isIntegerEmpty(getForControleEmployeurId())) {
                return "";
            }

            if (isNouveauControle()) {

                CEControleEmployeurManager controleManager = findCEControleEmployeur();
                if (controleManager.size() > 0) {
                    return ((CEControleEmployeur) controleManager.getFirstEntity()).getDateEffective();
                }

            } else {

                AFControleEmployeurManager AFControlManager = new AFControleEmployeurManager();
                AFControlManager.setSession(getSession());
                AFControlManager.setForControleEmployeurId(getForControleEmployeurId());
                AFControlManager.find();
                if (AFControlManager.size() > 0) {
                    return ((AFControleEmployeur) AFControlManager.getFirstEntity()).getDateEffective();
                }
            }

        } catch (Exception e) {
            return "";
        }

        return "";
    }

    public String getForDatePrevue() {
        try {
            if (!JadeStringUtil.isIntegerEmpty(fordatePrevue)) {
                return fordatePrevue;
            }

            if (JadeStringUtil.isIntegerEmpty(getForControleEmployeurId())) {
                return "";
            } else {

                if (isNouveauControle()) {

                    CEControleEmployeurManager controleManager = findCEControleEmployeur();
                    if (controleManager.size() > 0) {
                        return ((CEControleEmployeur) controleManager.getFirstEntity()).getDatePrevue();
                    }
                } else {

                    AFControleEmployeurManager AFControlManager = new AFControleEmployeurManager();
                    AFControlManager.setSession(getSession());
                    AFControlManager.setForControleEmployeurId(getForControleEmployeurId());
                    AFControlManager.find();
                    if (AFControlManager.size() > 0) {
                        return ((AFControleEmployeur) AFControlManager.getFirstEntity()).getDatePrevue();
                    }
                }
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getIdDeclaration() {
        return idDeclaration;
    }

    public String getIdDeclarationDistante() {
        return idDeclarationDistante;
    }

    /**
     * @return
     */
    public String getIdJournal() {
        return idJournal;
    }

    public String getIdPassageFac() {
        return idPassageFac;
    }

    public String getIdPucsFile() {
        return idPucsFile;
    }

    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return
     */
    public String getMasseAC2Total() {
        return JANumberFormatter.fmt(masseAC2Total.toString(), true, true, true, 2);
    }

    /**
     * Returns the masseACTotal.
     * 
     * @return String
     */
    public String getMasseACTotal() {
        return JANumberFormatter.fmt(masseACTotal.toString(), true, true, true, 2);
    }

    public String getMassePeriode() {
        return massePeriode;
    }

    public String getMassePeriodeFormate() {
        return JadeStringUtil.isDecimalEmpty(massePeriode) ? "" : new FWCurrency(massePeriode).toStringFormat();
    }

    public String getMasseSalTotal() {
        return JANumberFormatter.fmt(masseSalTotal.toString(), true, true, false, 2);
    }

    /**
     * Returns the masseSalTotalEcran.
     * 
     * @return String
     */
    public String getMasseSalTotalEcran() {
        return masseSalTotalEcran;
    }

    public String getMasseSalTotalWhitoutFormat() {
        return masseSalTotal.toString();
    }

    public FWCurrency getMontantFacture() {
        montantFacture = new FWCurrency(0);
        DSLigneDeclarationListViewBean ligneMana = new DSLigneDeclarationListViewBean();
        ligneMana.setSession(getSession());
        ligneMana.setForIdDeclaration(getIdDeclaration());
        try {
            ligneMana.find();
            if (ligneMana.size() > 0) {
                for (int i = 0; i < ligneMana.size(); i++) {
                    montantFacture.add(new FWCurrency(((DSLigneDeclarationViewBean) ligneMana.getEntity(i))
                            .getSoldeCotisation()));
                }
            }
        } catch (Exception e) {
            _addError(null, "Erreur lors de la récupération du montant de la déclaration"
                    + " DSDeclarationViewBean.getMontantFacture()" + getAffilieNumero() + e.getMessage());
            return null;
        }

        return montantFacture;
    }

    public String getMotifSuspendu() {
        return motifSuspendu;
    }

    /**
     * @return
     */
    public String getNbPersonnel() {
        return nbPersonnel;
    }

    public String getNbRappel() {
        return nbRappel;
    }

    public String getNoDecompte() {
        return noDecompte;
    }

    /**
     * Returns the nomTiers.
     * 
     * @return String
     */
    public String getNomTiers() {
        return nomTiers;
    }

    public Boolean getNotImpressionDecFinalAZero() {
        return notImpressionDecFinalAZero;
    }

    /**
     * Returns the numeroAffilie.
     * 
     * @return String
     */
    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    /**
     * Returns the prenomTiers.
     * 
     * @return String
     */
    public String getPrenomTiers() {
        return prenomTiers;
    }

    public String getProvenance() {
        return provenance;
    }

    public String getReferenceFacture() {
        return referenceFacture;
    }

    public BISession getSessionCI(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute("sessionPavo");
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = GlobazSystem.getApplication("PAVO").newSession(local);
            local.setAttribute("sessionPavo", remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    /**
     * @return
     */
    public String getSoumisInteret() {
        return soumisInteret;
    }

    /**
     * @return
     */
    public String getTotalControleAc() {
        return totalControleAc;
    }

    /**
     * @return
     */
    public String getTotalControleAcForDisplay() {
        return new FWCurrency(totalControleAc).toStringFormat();
    }

    /**
     * @return
     */
    public String getTotalControleAf() {
        return totalControleAf;
    }

    /**
     * @return
     */
    public String getTotalControleAfForDisplay() {
        return new FWCurrency(totalControleAf).toStringFormat();
    }

    /**
     * @return
     */
    public String getTotalControleDS() {
        return totalControleDS;
    }

    public String getTotalControleDSFormate() {
        return JadeStringUtil.isDecimalEmpty(totalControleDS) ? "" : new FWCurrency(totalControleDS).toStringFormat();
    }

    public String getTri() {
        return tri;
    }

    /**
     * Returns the typeAffiliationEcran.
     * 
     * @return String
     */
    public String getTypeAffiliationEcran() {
        return getAffiliation().getTypeAffiliation();
    }

    public String getTypeDeclaration() {
        return typeDeclaration;
    }

    public String getValidationDateSpy() {
        return validationDateSpy;
    }

    public String getValidationSpy() {
        return validationSpy;
    }

    public boolean isApresMiseEnProdEtCompl() {
        try {
            if (DSDeclarationViewBean.CS_COMPLEMENTAIRE.equals(getTypeDeclaration())
                    || DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(getTypeDeclaration())) {
                String anneeMiseEnProd = DSUtil.getDateMiseEnprod().substring(0, 4);
                BigDecimal dateDecl = new BigDecimal(getAnnee());
                BigDecimal anneeMiseProd = new BigDecimal(anneeMiseEnProd);
                if (dateDecl.compareTo(anneeMiseProd) <= 0) {
                    return false;
                }
                return true;
            } else {
                return true;
            }

        } catch (Exception e) {
            return true;
        }
    }

    public boolean isDSAZero() throws Exception {

        DSLigneDeclarationListViewBean theLineMgr = new DSLigneDeclarationListViewBean();
        theLineMgr.setSession(getSession());
        theLineMgr.setForIdDeclaration(getIdDeclaration());
        theLineMgr.find(BManager.SIZE_NOLIMIT);

        DSLigneDeclarationViewBean theLineEntity;
        for (int i = 0; i < theLineMgr.size(); i++) {
            theLineEntity = (DSLigneDeclarationViewBean) theLineMgr.getEntity(i);
            if (!JadeNumericUtil.isZeroValue(theLineEntity.getSoldeCotisation())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the enFacturation.
     * 
     * @return boolean
     */
    public boolean isEnFacturation() {
        return enFacturation;
    }

    /**
     * Retourne si la déclaration est en validation.
     * 
     * @return boolean
     */
    public boolean isEnValidation() {
        return enValidation;
    }

    public Boolean isForceUpdate() {
        return forceUpdate;
    }

    /**
     * Returns the masseZero.
     * 
     * @return boolean
     */
    public boolean isMasseZero() {
        return masseZero;
    }

    public boolean isNouveauControle() {
        return DSUtil.isNouveauControleEmployeur(getSession());
    }

    public boolean isSaisieEcran() {
        return saisieEcran;
    }

    public int nombreInscriptions() throws Exception {

        DSInscriptionsIndividuellesManager inscMgr = new DSInscriptionsIndividuellesManager();
        inscMgr.setSession(getSession());
        inscMgr.setForIdDeclaration(getIdDeclaration());
        inscMgr.setForMoisFin("12");
        return inscMgr.getCount();

    }

    public void setAffiliationId(String newAffiliationId) {
        affiliationId = newAffiliationId;
    }

    /**
     * Sets the affilieDesEcran.
     * 
     * @param affilieDesEcran
     *            The affilieDesEcran to set
     */
    public void setAffilieDesEcran(String affilieDesEcran) {
        this.affilieDesEcran = affilieDesEcran;
    }

    /**
     * @param string
     */
    public void setAffilieNumero(String string) {
        affilieNumero = string;
    }

    /**
     * Sets the affilieRadieEcran.
     * 
     * @param affilieRadieEcran
     *            The affilieRadieEcran to set
     */
    public void setAffilieRadieEcran(String affilieRadieEcran) {
        this.affilieRadieEcran = affilieRadieEcran;
    }

    public void setAnnee(String newAnnee) {
        annee = newAnnee;
    }

    public void setAnneeTaux(String anneeTaux) {
        this.anneeTaux = anneeTaux;
    }

    /**
     * @param string
     */
    public void setCodeCantonAF(String string) {
        codeCantonAF = string;
    }

    public void setCodeSuspendu(String newCodeSuspendu) {
        codeSuspendu = newCodeSuspendu;
    }

    public void setDateEnvoiLettre(String dateEnvoiLettre) {
        this.dateEnvoiLettre = dateEnvoiLettre;
    }

    public void setDateEnvoiRappel(String dateEnvoiRappel) {
        this.dateEnvoiRappel = dateEnvoiRappel;
    }

    public void setDateFinSuspendu(String newDateFinSuspendu) {
        dateFinSuspendu = newDateFinSuspendu;
    }

    public void setDateImpressionAttestations(String dateImpressionAttestations) {
        this.dateImpressionAttestations = dateImpressionAttestations;
    }

    public void setDateImpressionDecompteImpots(String dateImpressionDecompteImpots) {
        this.dateImpressionDecompteImpots = dateImpressionDecompteImpots;
    }

    public void setDateRetourDes(String newDateRetourDes) {
        dateRetourDes = newDateRetourDes;
    }

    public void setDateRetourEff(String newDateRetourEff) {
        dateRetourEff = newDateRetourEff;
    }

    /**
     * Sets the descriptionTiers.
     * 
     * @param descriptionTiers
     *            The descriptionTiers to set
     */
    public void setDescriptionTiers(String descriptionTiers) {
        this.descriptionTiers = descriptionTiers;
    }

    /**
     * @param string
     */
    public void setDesignation1(String string) {
        designation1 = string;
    }

    /**
     * Sets the enFacturation.
     * 
     * @param enFacturation
     *            The enFacturation to set
     */
    public void setEnFacturation(boolean enFacturation) {
        this.enFacturation = enFacturation;
    }

    /**
     * Sets the enValidation.
     * 
     * @param enValidation
     *            The enValidation to set
     */
    public void setEnValidation(boolean enValidation) {
        this.enValidation = enValidation;
    }

    public void setEtat(String newEtat) {
        etat = newEtat;
    }

    public void setForceUpdate(Boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public void setForControleEmployeurId(String controleEmployeurId) {
        forControleEmployeurId = controleEmployeurId;
    }

    public void setForDateEffective(String dateEffective) {
        fordateEffective = dateEffective;
    }

    public void setForDatePrevue(String datePrevue) {
        fordatePrevue = datePrevue;
    }

    public void setIdDeclaration(String newIdDeclaration) {
        idDeclaration = newIdDeclaration;
    }

    public void setIdDeclarationDistante(String idDeclarationDistante) {
        this.idDeclarationDistante = idDeclarationDistante;
    }

    /**
     * @param string
     */
    public void setIdJournal(String string) {
        idJournal = string;
    }

    public void setIdPassageFac(String newIdPassageFac) {
        idPassageFac = newIdPassageFac;
    }

    public void setIdPucsFile(String idPucsFile) {
        this.idPucsFile = idPucsFile;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * @param string
     */
    public void setMasseAC2Total(String string) {
        masseAC2Total = JANumberFormatter.deQuote(string);
    }

    /**
     * Sets the masseACTotal.
     * 
     * @param newMasseACTotal
     *            The masseACTotal to set
     */
    public void setMasseACTotal(String newMasseACTotal) {
        masseACTotal = JANumberFormatter.deQuote(newMasseACTotal);
    }

    public void setMassePeriode(String massePeriode) {
        this.massePeriode = JANumberFormatter.deQuote(massePeriode);
    }

    public void setMasseSalTotal(String newMasseSalTotal) {
        masseSalTotal = JANumberFormatter.deQuote(newMasseSalTotal);
    }

    /**
     * Sets the masseSalTotalEcran.
     * 
     * @param newMasseSalTotalEcran
     *            The masseSalTotalEcran to set
     */
    public void setMasseSalTotalEcran(String newMasseSalTotalEcran) {
        masseSalTotalEcran = JANumberFormatter.deQuote(newMasseSalTotalEcran);
    }

    /**
     * Sets the masseZero.
     * 
     * @param masseZero
     *            The masseZero to set
     */
    public void setMasseZero(boolean masseZero) {
        this.masseZero = masseZero;
    }

    public void setMontantFacture(FWCurrency montantFacture) {
        this.montantFacture = montantFacture;
    }

    public void setMotifSuspendu(String newMotifSuspendu) {
        motifSuspendu = newMotifSuspendu;
    }

    /**
     * @param string
     */
    public void setNbPersonnel(String string) {
        nbPersonnel = string;
    }

    public void setNbRappel(String nbRappel) {
        this.nbRappel = nbRappel;
    }

    public void setNoDecompte(String newNoDecompte) {
        noDecompte = newNoDecompte;
    }

    /**
     * Sets the nomTiers.
     * 
     * @param nomTiers
     *            The nomTiers to set
     */
    public void setNomTiers(String nomTiers) {
        this.nomTiers = nomTiers;
    }

    public void setNotImpressionDecFinalAZero(Boolean notImpressionDecFinalAZero) {
        this.notImpressionDecFinalAZero = notImpressionDecFinalAZero;
    }

    /**
     * Sets the numeroAffilie.
     * 
     * @param numeroAffilie
     *            The numeroAffilie to set
     */
    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    /**
     * Sets the prenomTiers.
     * 
     * @param prenomTiers
     *            The prenomTiers to set
     */
    public void setPrenomTiers(String prenomTiers) {
        this.prenomTiers = prenomTiers;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public void setReferenceFacture(String referenceFacture) {
        this.referenceFacture = referenceFacture;
    }

    /**
     * Permet de définir si une saisie de déclaration est faite manuellement ou automatiquement
     * 
     * @param newSaisieEcran
     *            java.lang.String
     */
    public void setSaisieEcran(String newSaisieEcran) {
        try {
            if (newSaisieEcran.equalsIgnoreCase("true")) {
                saisieEcran = true;
            } else {
                saisieEcran = false;
            }
        } catch (Exception ex) {
            saisieEcran = false;
        }
    }

    /**
     * @param string
     */
    public void setSoumisInteret(String string) {
        soumisInteret = string;
    }

    /**
     * @param string
     */
    public void setTotalControleAc(String string) {
        totalControleAc = string;
    }

    /**
     * @param string
     */
    public void setTotalControleAf(String string) {
        totalControleAf = string;
    }

    /**
     * @param string
     */
    public void setTotalControleDS(String string) {
        totalControleDS = JANumberFormatter.deQuote(string);
    }

    public void setTri(String tri) {
        this.tri = tri;
    }

    /**
     * Sets the typeAffiliationEcran.
     * 
     * @param typeAffiliationEcran
     *            The typeAffiliationEcran to set
     */
    public void setTypeAffiliationEcran(String typeAffiliationEcran) {
        this.typeAffiliationEcran = typeAffiliationEcran;
    }

    public void setTypeDeclaration(String newTypeDeclaration) {
        typeDeclaration = newTypeDeclaration;
    }

    public void setValidationDateSpy(String validationDateSpy) {
        this.validationDateSpy = validationDateSpy;
    }

    public void setValidationSpy(String validationSpy) {
        this.validationSpy = validationSpy;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

}
