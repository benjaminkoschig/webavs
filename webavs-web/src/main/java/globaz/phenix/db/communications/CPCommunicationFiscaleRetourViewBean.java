package globaz.phenix.db.communications;

import globaz.commons.nss.NSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.divers.CPPeriodeFiscale;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionManager;
import globaz.phenix.db.principale.CPDecisionViewBean;
import globaz.phenix.interfaces.ICommunicationRetour;
import globaz.phenix.translation.CodeSystem;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;

public class CPCommunicationFiscaleRetourViewBean extends globaz.globall.db.BEntity implements ICommunicationRetour,
        java.io.Serializable, FWViewBeanInterface {

    private static final long serialVersionUID = 1L;
    public final static String CS_A_CONTROLER = "612001";
    public final static String CS_ABANDONNE = "612005";
    // changement de genre
    public final static String CS_AFI_IND = "619001";

    public final static String CS_AVERTISSEMENT = "612008";

    public final static String CS_COMPTABILISE = "612007";

    public final static String CS_ENQUETE = "612009";

    public final static String CS_ERREUR = "612003";

    public final static String CS_IND_AFI = "619002";

    public final static String CS_RECEPTIONNE = "612002";

    // code systeme
    // EtatCommunicationsFiscalesRetour
    public final static String CS_SANS_ANOMALIE = "612006";

    public final static String CS_VALIDE = "612004";

    public final static String defaultMenu = "CommunicationFiscaleRetour-default";

    public final static String LOG_SOURCE_COMPTABILISATION = "1003";

    public final static String LOG_SOURCE_GENERATION = "1002";

    public final static String LOG_SOURCE_RECEPTION = "1000";

    public final static String LOG_SOURCE_VALIDATION = "1001";

    /**
     * Renvoie le nom du menu a appelé en fonction de l'état du journal
     * 
     * @return
     */
    private final static Hashtable<String, String> menuTable = new Hashtable<String, String>();

    static public String getMenuName(String statusCom) {
        if (CPCommunicationFiscaleRetourViewBean.menuTable.isEmpty()) {
            CPCommunicationFiscaleRetourViewBean.menuTable.put(CPCommunicationFiscaleRetourViewBean.CS_A_CONTROLER,
                    CPCommunicationFiscaleRetourViewBean.defaultMenu);
            CPCommunicationFiscaleRetourViewBean.menuTable.put(CPCommunicationFiscaleRetourViewBean.CS_RECEPTIONNE,
                    CPCommunicationFiscaleRetourViewBean.defaultMenu);
            // Si menu dynamique
            // menuTable.put(CPCommunicationFiscaleRetourViewBean.CS_ERREUR,
            // "Communications-communicationFiscaleRetour-Abandonner");
            CPCommunicationFiscaleRetourViewBean.menuTable.put(CPCommunicationFiscaleRetourViewBean.CS_ERREUR,
                    CPCommunicationFiscaleRetourViewBean.defaultMenu);
            CPCommunicationFiscaleRetourViewBean.menuTable.put(CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE,
                    CPCommunicationFiscaleRetourViewBean.defaultMenu);
            CPCommunicationFiscaleRetourViewBean.menuTable.put(CPCommunicationFiscaleRetourViewBean.CS_SANS_ANOMALIE,
                    CPCommunicationFiscaleRetourViewBean.defaultMenu);
            CPCommunicationFiscaleRetourViewBean.menuTable.put(CPCommunicationFiscaleRetourViewBean.CS_AVERTISSEMENT,
                    CPCommunicationFiscaleRetourViewBean.defaultMenu);
            CPCommunicationFiscaleRetourViewBean.menuTable.put(CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE,
                    CPCommunicationFiscaleRetourViewBean.defaultMenu);
        }
        String dynamicMenu = CPCommunicationFiscaleRetourViewBean.menuTable.get(statusCom);
        if (dynamicMenu == null) {
            return CPCommunicationFiscaleRetourViewBean.defaultMenu;
        } else {
            return dynamicMenu;
        }

    }

    private AFAffiliation affiliation = null;

    private AFAffiliation affiliationConjoint = null;

    // public String getNumIfd() {
    // String numIfd = "";
    // if (getAnnee1() != null||getAnnee1() != ""){
    // try {
    // CPPeriodeFiscaleManager periode = new CPPeriodeFiscaleManager();
    // periode.setSession(getSession());
    // periode.setForAnneeDecisionDebut(getAnnee1());
    // periode.find();
    // if(periode.size()>0){
    // numIfd = ((CPPeriodeFiscale)periode.getFirstEntity()).getNumIfd();
    // }
    // } catch (Exception e) {
    // JadeLogger.error(this, e);
    // }
    // }else{
    // if (getTiers() != null) {
    // return getPeriodeFiscale().getNumIfd();
    // }
    // }
    // return numIfd;
    // }

    private java.lang.String annee1 = "";

    private String annee2 = "";

    private String autreRevenu = "";

    private String canton = "";

    // Zone de travail
    private String cantonJournal = "";

    private String cantonJournalBk = "";

    private String capital = "";

    private String changementGenre = "";

    private String changementGenreConjoint = "";

    private String codeSexe = "";

    private ICommunicationRetour commFiscaleFils;

    private CPCommunicationFiscale communicationFiscale = null;

    private TITiersViewBean conjoint = null;

    private String cotisation1 = "";

    private String cotisation2 = "";

    private String dateFortune = "";

    private String dateRetour = "";
    private String debutExercice1 = "";
    private String debutExercice2 = "";
    private CPDecision decisionDeBase = null;
    private CPDecisionViewBean decisionGeneree = null;
    private String depensesTrainVie = "";
    private String description = "";
    private String EMailAddress = "";
    private String etatCivil = "";
    private String finExercice1 = "";
    private String finExercice2 = "";
    private java.lang.String fortune = "";
    private Boolean generation = new Boolean(false);
    private String geNom = "";
    private String genreAffilie = "";
    private String genreConjoint = "";
    private String genreTaxation = "";
    private String geNumAffilie = "";
    private String geNumContribuable = "";
    private String gePrenom = "";
    private String idAffiliation = "";
    private String idAffiliationConjoint = "";
    private String idCommunication = "";
    private String idConjoint = "";
    private java.lang.String idIfd = "";
    private String idJournalRetour = "";
    private String idLog = "";
    private String idParametrePlausi = "";
    private String idRetour = "";
    private java.lang.String idTiers = "";
    private String impression = "";
    private boolean isForBackup = false;
    private boolean isForRetourOriginale = false;
    private CPJournalRetour journalRetour = null;
    private String juNumContribuable = "";
    private String lastDate = "";
    private String lastTime = "";
    private String lastUser = "";
    private String[] listIdRetour = null;
    private String majNumContribuable = "";
    private String neNumContribuable = "";
    private String nom = "";
    private String numAffilie = "";
    private String numAffilieRecu = "";
    private String numAvs = "";
    private String numAvsFisc = "";
    private java.lang.String numContribuable = "";
    private java.lang.String numContribuableRecu = "";
    private java.lang.String numIfd = "";
    private CPPeriodeFiscale periodeFiscale = null;
    private String prenom = "";
    private String rachatLpp = "";
    private String rachatLppCjt = "";
    private String reportType = "";
    private String revenuAnnee1 = "";
    private String revenuAnnee2 = "";
    private String seNom = "";
    private String sePrenom = "";
    private String status = "";
    private TITiersViewBean tiers = null;
    private String tri = "";
    private String valeurChampRecherche = "";
    private String vdNomPrenom = "";
    private String vdNumAffilie = "";
    private String vdNumContribuable = "";
    private String vsNomPrenom = "";
    private String vsNomPrenomCjt = "";
    private String vsNumAffilie = "";
    private String vsNumContribuable = "";
    private boolean wantAfterRetrieve = false;
    private boolean wantControleSpy = true;
    private Boolean wantDetail = Boolean.FALSE;

    private boolean wantDonneeBase = false;

    private boolean wantDonneeContribuable = false;

    private boolean wantMajBackup = false;
    private boolean wantPlausibilite = true;
    private boolean wantUpdateJournal = false;

    /**
     * Commentaire relatif au constructeur CPDonneesBase
     */
    public CPCommunicationFiscaleRetourViewBean() {
        super();
    }

    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        // si sedex, il faut aussi supprimer les tables spécifiques à Sedex
        if (getJournalRetour().getTypeJournal().equals(CPJournalRetour.CS_TYPE_JOURNAL_SEDEX)) {
            CPSedexContribuable contribuable = new CPSedexContribuable();
            contribuable.setSession(getSession());
            contribuable.setIdRetour(idRetour);
            contribuable.retrieve();
            contribuable.delete();

            CPSedexConjoint conjoint = new CPSedexConjoint();
            conjoint.setSession(getSession());
            conjoint.setIdRetour(idRetour);
            conjoint.retrieve();
            conjoint.delete();

            CPSedexDonneesBase donneesBase = new CPSedexDonneesBase();
            donneesBase.setSession(getSession());
            donneesBase.setIdRetour(idRetour);
            donneesBase.retrieve();
            donneesBase.delete();

            CPSedexDonneesCommerciales donneesCom = new CPSedexDonneesCommerciales();
            donneesCom.setSession(getSession());
            donneesCom.setIdRetour(idRetour);
            donneesCom.retrieve();
            donneesCom.delete();

            CPSedexDonneesPrivees donneesPri = new CPSedexDonneesPrivees();
            donneesPri.setSession(getSession());
            donneesPri.setIdRetour(idRetour);
            donneesPri.retrieve();
            donneesPri.delete();
        }
        super._afterDelete(transaction);
    }

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        if (wantAfterRetrieve) {
            getTiers();
            if ((getTiers() != null) && !getTiers().isNew()) {
                canton = getTiers().getCantonDomicile();
                nom = getTiers().getDesignation1();
                prenom = getTiers().getDesignation2();
                numContribuable = getTiers().getNumContribuableActuel();
                numAvs = getTiers().getNumAvsActuel();
            }
            getPeriodeFiscaleAffichage();
            numAffilie = getAffiliation().getAffilieNumero();
        }
        // Si la demande est comptabilisée => recherche si une plus récente
        if (!JadeStringUtil.isBlankOrZero(getIdCommunication())) {
            CPCommunicationFiscale cf = new CPCommunicationFiscale();
            cf.setIdCommunication(getIdCommunication());
            cf.setSession(getSession());
            cf.retrieve();
            if (!JadeStringUtil.isBlankOrZero(cf.getDateComptabilisation())
                    || cf.getDemandeAnnulee().equals(Boolean.TRUE)) {
                setIdCommunication(cf.getCommunicationFiscaleEnvoyee(1).getIdCommunication());
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction) On efface les messages de logs
     * générés
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        if (CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE.equalsIgnoreCase(getStatus())) {
            _suppressionValidation(transaction);
        }
        if (wantUpdateJournal) {
            majJournal();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction) On efface les messages de logs
     * générés
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        _suppressionDecision(transaction, "");
        _suppressionCommentaire(transaction);
        _suppressionLiensPlausi(transaction);
    }

    @Override
    protected void _copyPointersFromEntity(BEntity entity) throws Exception {
        // TODO checker si entity est du bon type avant de caster
        setForBackup(((CPCommunicationFiscaleRetourViewBean) entity).isForBackup());
        setForRetourOriginale(((CPCommunicationFiscaleRetourViewBean) entity).isForRetourOriginale());
        super._copyPointersFromEntity(entity);
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        if (!isForBackup()) {
            return "CPCRETP";
        } else {
            // isForBackup = false;
            return "CPCRETB";
        }
    }

    /**
     * Enregistre un message dans la communication
     * 
     * @param message
     * @param type
     *            : provenant de FWMessage (INFORMATION, AVERTISSEMENT, ERREUR)
     * @throws Exception
     */
    @Override
    public void _logMessage(BTransaction transaction, String idLog, String message, String type) throws Exception {
        // On ajoute l'idPlausibilite et l'idCommunication dans Le fichier Lien
        CPLienCommunicationsPlausi lien = new CPLienCommunicationsPlausi();
        lien.setSession(getSession());
        lien.setIdCommunication(getIdCommunication());
        lien.setIdPlausibilite(getIdParametrePlausi());
        lien.add(transaction);
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        lastUser = statement.dbReadString("IKLUSR");
        lastDate = statement.dbReadString("IKLDAT");
        lastTime = statement.dbReadString("IKLTIM");
        seNom = statement.dbReadString("SEOFNAM");
        sePrenom = statement.dbReadString("SEFINAM");
        vsNomPrenom = statement.dbReadString("IKNOMA");
        vsNomPrenomCjt = statement.dbReadString("IKNCON");
        vsNumAffilie = statement.dbReadString("IKNUMA");
        vsNumContribuable = statement.dbReadString("IKNUMC");
        geNom = statement.dbReadString("IKLNOM");
        gePrenom = statement.dbReadString("IKLPRE");
        geNumAffilie = statement.dbReadString("IKAFGE");
        geNumContribuable = statement.dbReadString("IKCOGE");
        neNumContribuable = statement.dbReadString("IKCONE");
        juNumContribuable = statement.dbReadString("IKNCJU");
        vdNumContribuable = statement.dbReadString("IKCOVD");
        vdNumAffilie = statement.dbReadString("IKAFVD");
        vdNomPrenom = statement.dbReadString("IKLNOM");
        idRetour = statement.dbReadNumeric("IKIRET");
        idTiers = statement.dbReadNumeric("HTITIE");
        idIfd = statement.dbReadNumeric("ICIIFD");
        numIfd = statement.dbReadNumeric("ICNIFD");
        genreAffilie = statement.dbReadNumeric("IKTGAF");
        idCommunication = statement.dbReadNumeric("IBIDCF");
        dateRetour = statement.dbReadDateAMJ("IKDRET");
        annee1 = statement.dbReadNumeric("IKANN1");
        revenuAnnee1 = statement.dbReadNumeric("IKREV1", 2);
        debutExercice1 = statement.dbReadDateAMJ("IKDDE1");
        finExercice1 = statement.dbReadDateAMJ("IKDFI1");
        revenuAnnee2 = statement.dbReadNumeric("IKREV2", 2);
        debutExercice2 = statement.dbReadDateAMJ("IKDDE2");
        finExercice2 = statement.dbReadDateAMJ("IKDFI2");
        cotisation1 = statement.dbReadNumeric("IKCOT1", 2);
        cotisation2 = statement.dbReadNumeric("IKCOT2", 2);
        capital = statement.dbReadNumeric("IKCAPI", 2);
        fortune = statement.dbReadNumeric("IKFORT", 2);
        dateFortune = statement.dbReadDateAMJ("IKDFOR");
        genreTaxation = statement.dbReadNumeric("IKTGTA");
        generation = statement.dbReadBoolean("IKBGEN");
        status = statement.dbReadNumeric("IKTSTA");
        idLog = statement.dbReadNumeric("IKILOG");
        idJournalRetour = statement.dbReadNumeric("IWRJOU");
        annee2 = statement.dbReadNumeric("IKANN2");
        idAffiliation = statement.dbReadNumeric("MAIAFF");
        tri = statement.dbReadString("IKLTRI");
        // Champ appartenant au managers joints
        nom = statement.dbReadString("HTLDE1");
        prenom = statement.dbReadString("HTLDE2");
        numAffilie = statement.dbReadString("MALNAF");
        canton = statement.dbReadString("HPTCAN");
        numAvs = statement.dbReadString("HXAAVS");
        numContribuable = statement.dbReadString("HXNCON");
        cantonJournal = statement.dbReadString("IWACAN");
        autreRevenu = statement.dbReadNumeric("IKAREV", 2);
        idConjoint = statement.dbReadNumeric("IKICON");
        idAffiliationConjoint = statement.dbReadNumeric("IKIACJ");
        idParametrePlausi = statement.dbReadNumeric("IXIDPA");
        changementGenre = statement.dbReadNumeric("IKTCHG");
        changementGenreConjoint = statement.dbReadNumeric("IKTCHC");
        genreConjoint = statement.dbReadNumeric("IKTGCJ");
        reportType = statement.dbReadNumeric("IKRETY");
        // Inforom 550 - Ajout champ pour conjoint
        /*
         * this.revenuAnnee1Cjt = statement.dbReadNumeric("IKRV1C", 2); this.debutExerciceCjt =
         * statement.dbReadDateAMJ("IKDEXC"); this.finExerciceCjt = statement.dbReadDateAMJ("IKFEXC");
         * this.revenuAnnee2Cjt = statement.dbReadNumeric("IKRV2C", 2); this.capitalCjt =
         * statement.dbReadNumeric("IKCAPC", 2); this.fortuneCjt = statement.dbReadNumeric("IKFORC", 2);
         */
    }

    public void _suppressionCommentaire(BTransaction transaction) throws Exception {
        // Suppression des décisions
        CPCommentaireCommunicationManager commentMng = new CPCommentaireCommunicationManager();
        commentMng.setSession(getSession());
        commentMng.setForIdCommentaireCf(getIdRetour());
        commentMng.find();
        for (int i = 0; i < commentMng.size(); i++) {
            CPCommentaireCommunication comment = ((CPCommentaireCommunication) commentMng.getEntity(i));
            comment.delete(transaction);
        }
    }

    @Override
    public void _suppressionDecision(BTransaction transaction, String idTiers) throws Exception {
        // Suppression des décisions
        CPDecisionManager decisionMng = new CPDecisionManager();
        decisionMng.setSession(getSession());
        decisionMng.setForIdCommunication(getIdRetour());
        decisionMng.setForIdTiers(idTiers);
        decisionMng.find();
        for (int i = 0; i < decisionMng.size(); i++) {
            CPDecision decision = ((CPDecision) decisionMng.getEntity(i));
            decision.setSuppressionExterne(true);
            decision.delete(transaction);
        }
    }

    public void _suppressionLiensPlausi(BTransaction transaction) throws Exception {
        // Suppression des décisions
        CPLienCommunicationsPlausiManager manager = new CPLienCommunicationsPlausiManager();
        manager.setSession(getSession());
        manager.setForIdCommunication(getIdRetour());
        manager.find();
        for (int i = 0; i < manager.size(); i++) {
            CPLienCommunicationsPlausi lien = ((CPLienCommunicationsPlausi) manager.getEntity(i));
            lien.delete(transaction);
        }
    }

    public void _suppressionValidation(BTransaction transaction) throws Exception {
        // Suppression du fichier de validation
        CPValidationCalculCommunicationManager validMng = new CPValidationCalculCommunicationManager();
        validMng.setSession(getSession());
        validMng.setForIdCommunicationRetour(getIdRetour());
        validMng.find();
        for (int i = 0; i < validMng.size(); i++) {
            CPValidationCalculCommunication val = ((CPValidationCalculCommunication) validMng.getEntity(i));
            val.delete(transaction);
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires) Les reception sont toujours chargée, elle
     * seront validées par les plausibilités
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(getIdCommunication())) {
            CPCommunicationFiscale comFis = null;
            comFis = getCommunicationFiscale(1);
            // Tester si l'on a pas délà lu l'enreg. et si c'est celui que l'on
            // traite
            if ((comFis != null) && comFis.getIdCommunication().equalsIgnoreCase(getIdCommunication())) {
                if (JadeStringUtil.isBlankOrZero(getDateRetour())) {
                    comFis.setDateRetour(getJournalRetour().getDateReception());
                } else {
                    comFis.setDateRetour(getDateRetour());
                }
                comFis.setAlternateKey(0);
                comFis.update();
            } else {
                comFis = new CPCommunicationFiscale();
                comFis.setIdCommunication(getIdCommunication());
                comFis.setSession(getSession());
                comFis.retrieve();
                if (!comFis.isNew()) {
                    if (JadeStringUtil.isBlankOrZero(getDateRetour())) {
                        comFis.setDateRetour(getJournalRetour().getDateReception());
                    } else {
                        comFis.setDateRetour(getDateRetour());
                    }
                    comFis.setAlternateKey(0);
                    comFis.update();
                }
            }
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IKIRET", this._dbWriteNumeric(statement.getTransaction(), getIdRetour(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IKLUSR", this._dbWriteString(statement.getTransaction(), getLastUser(), "lastUser"));
        statement.writeField("IKLDAT", this._dbWriteString(statement.getTransaction(), getLastDate(), "lastDate"));
        statement.writeField("IKLTIM", this._dbWriteString(statement.getTransaction(), getLastTime(), "lastTime"));
        statement.writeField("IKIRET", this._dbWriteNumeric(statement.getTransaction(), getIdRetour(), "idRetour"));
        statement.writeField("HTITIE", this._dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers"));
        statement.writeField("ICIIFD", this._dbWriteNumeric(statement.getTransaction(), getIdIfd(), "idIfd"));
        statement.writeField("ICNIFD", this._dbWriteNumeric(statement.getTransaction(), getNumIfd(), "numIfd"));
        statement.writeField("IKTGAF",
                this._dbWriteNumeric(statement.getTransaction(), getGenreAffilie(), "genreAffilie"));
        statement.writeField("IBIDCF",
                this._dbWriteNumeric(statement.getTransaction(), getIdCommunication(), "idCommunication"));
        statement.writeField("IKDRET", this._dbWriteDateAMJ(statement.getTransaction(), getDateRetour(), "dateRetour"));
        statement.writeField("IKANN1", this._dbWriteNumeric(statement.getTransaction(), getAnnee1(), "annee1"));
        statement.writeField("IKREV1", this._dbWriteNumeric(statement.getTransaction(), getRevenu1(), "revenu1"));
        statement.writeField("IKDDE1",
                this._dbWriteDateAMJ(statement.getTransaction(), getDebutExercice1(), "debutExercice1"));
        statement.writeField("IKDFI1",
                this._dbWriteDateAMJ(statement.getTransaction(), getFinExercice1(), "finExercice1"));
        statement.writeField("IKANN2", this._dbWriteNumeric(statement.getTransaction(), getAnnee2(), "annee2"));
        statement.writeField("IKREV2", this._dbWriteNumeric(statement.getTransaction(), getRevenu2(), "revenu2"));
        statement.writeField("IKDDE2",
                this._dbWriteDateAMJ(statement.getTransaction(), getDebutExercice2(), "debutExercice2"));
        statement.writeField("IKDFI2",
                this._dbWriteDateAMJ(statement.getTransaction(), getFinExercice2(), "finExercice2"));
        statement.writeField("IKCOT1",
                this._dbWriteNumeric(statement.getTransaction(), getCotisation1(), "cotisation1"));
        statement.writeField("IKCOT2",
                this._dbWriteNumeric(statement.getTransaction(), getCotisation2(), "cotisation2"));
        statement.writeField("IKCAPI", this._dbWriteNumeric(statement.getTransaction(), getCapital(), "capital"));
        statement.writeField("IKFORT", this._dbWriteNumeric(statement.getTransaction(), getFortune(), "fortuneTotale"));
        statement.writeField("IKDFOR",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateFortune(), "dateFortune"));
        statement.writeField("IKTGTA",
                this._dbWriteNumeric(statement.getTransaction(), getGenreTaxation(), "genreTaxation"));
        statement.writeField("IKBGEN", this._dbWriteBoolean(statement.getTransaction(), getGeneration(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "generation"));
        statement.writeField("IKTSTA", this._dbWriteNumeric(statement.getTransaction(), getStatus(), "status"));
        statement.writeField("IKILOG", this._dbWriteNumeric(statement.getTransaction(), getIdLog(), "idLog"));
        statement.writeField("IWRJOU",
                this._dbWriteNumeric(statement.getTransaction(), getIdJournalRetour(), "idJournalRetour"));
        statement.writeField("MAIAFF",
                this._dbWriteNumeric(statement.getTransaction(), getIdAffiliation(), "idAffiliation"));
        statement.writeField("IKAREV",
                this._dbWriteNumeric(statement.getTransaction(), getAutreRevenu(), "autreRevenu"));
        statement.writeField("IKICON", this._dbWriteNumeric(statement.getTransaction(), getIdConjoint(), "idConjoint"));
        statement.writeField("IKIACJ",
                this._dbWriteNumeric(statement.getTransaction(), getIdAffiliationConjoint(), "idAffiliationConjoint"));
        statement.writeField("IXIDPA",
                this._dbWriteNumeric(statement.getTransaction(), getIdParametrePlausi(), "idParametrePlausi"));
        statement.writeField("IKTCHG",
                this._dbWriteNumeric(statement.getTransaction(), getChangementGenre(), "changementGenre"));
        statement.writeField("IKTCHC", this._dbWriteNumeric(statement.getTransaction(), getChangementGenreConjoint(),
                "changementGenreConjoint"));
        statement.writeField("IKTGCJ",
                this._dbWriteNumeric(statement.getTransaction(), getGenreConjoint(), "genreConjoint"));
        statement.writeField("IKRETY", this._dbWriteNumeric(statement.getTransaction(), getReportType(), "reportType"));
        statement.writeField("IKLTRI", this._dbWriteString(statement.getTransaction(), getTri(), "tri"));
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /*
     * implementation de delete()
     */
    @Override
    public void deleteCas(BTransaction transaction) throws Exception {
        this.delete(transaction);
    }

    public TIAdresseDataSource getAdresse() {
        if (getTiers() != null) {
            try {
                return getTiers().getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005",
                        JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY), true);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.05.2003 11:37:07)
     * 
     * @return AFAffiliation
     */
    @Override
    public AFAffiliation getAffiliation() {
        // enregistrement déjà chargé ?
        if ((affiliation == null) || affiliation.isNew()) {
            // liste pas encore chargée, on la charge
            affiliation = new AFAffiliation();
            affiliation.setSession(getSession());
            if (!JadeStringUtil.isIntegerEmpty(getIdAffiliation())) {
                try {
                    affiliation.setAffiliationId(getIdAffiliation());
                    affiliation.retrieve();
                } catch (Exception e) {
                    getSession().addError(e.getMessage());
                }
            }
        }
        return affiliation;
    }

    @Override
    public AFAffiliation getAffiliationConjoint() {
        // enregistrement déjà chargé ?
        if ((affiliationConjoint == null) || affiliationConjoint.isNew()) {
            // liste pas encore chargée, on la charge
            affiliationConjoint = new AFAffiliation();
            affiliationConjoint.setSession(getSession());
            if (!JadeStringUtil.isIntegerEmpty(getIdAffiliationConjoint())) {
                try {
                    affiliationConjoint.setAffiliationId(getIdAffiliationConjoint());
                    affiliationConjoint.retrieve();
                } catch (Exception e) {
                    getSession().addError(e.getMessage());
                }
            }
        }
        return affiliationConjoint;
    }

    /**
     * Returns the annee.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getAnnee1() {
        return annee1;
    }

    @Override
    public java.lang.String getAnnee2() {
        return annee2;
    }

    @Override
    public String getAutreRevenu() {
        return autreRevenu;
    }

    @Override
    public String getAutreRevenuConjoint() {
        return "";
    }

    /**
     * @return
     */
    public String getCanton() {
        return canton;
    }

    /**
     * Retourne le tiers ou null si tiers invtrouvable
     * 
     * @return
     */
    public String getCantonJournal() {
        try {
            if (cantonJournal.equals("") || !cantonJournalBk.equals(cantonJournal)) {
                CPJournalRetour journal = new CPJournalRetour();
                journal.setSession(getSession());
                journal.setIdJournalRetour(getIdJournalRetour());
                journal.retrieve();
                if (!journal.isNew()) {
                    return journal.getCanton();
                }
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public java.lang.String getCapital() {
        if (!JadeStringUtil.isEmpty(capital)) {
            float capi = Float.valueOf(capital);
            return JANumberFormatter.fmt(String.valueOf(capi), true, false, true, 0);
        } else {
            return "";
        }
    }

    @Override
    public String getCapitalEntreprise() {
        return capital;
    }

    @Override
    public String getCapitalEntrepriseConjoint() {
        return "";
    }

    @Override
    public String getChangementGenre() {
        return changementGenre;
    }

    @Override
    public String getChangementGenreConjoint() {
        return changementGenreConjoint;
    }

    @Override
    public String getCodeCanton() {
        String canton = "";
        try {
            canton = CodeSystem.getCode(getSession(), getCantonJournal());
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return canton;
    }

    @Override
    public String getCodeSexe() {
        return codeSexe;
    }

    /**
     * @return
     */
    public ICommunicationRetour getCommFiscaleFils() {
        return commFiscaleFils;
    }

    /**
     * Retourne la communication fiscale, null si le tiers ou la période fiscale n'est pas renseigné
     */
    @Override
    public CPCommunicationFiscale getCommunicationFiscale(int modeRecherche) {
        try {
            if (communicationFiscale == null) {
                // On ne peut retrouver la communication sans la periode fiscale
                if (JadeStringUtil.isEmpty(getIdIfd())) {
                    return null;
                }
                if ((modeRecherche != 3) && JadeStringUtil.isIntegerEmpty(getIdTiers())) {
                    // On ne peut retrouver la communication sans le tiers
                    return null;
                }
                if ((modeRecherche == 3) && JadeStringUtil.isIntegerEmpty(getIdConjoint())) {
                    // On ne peut retrouver la communication sans le conjoint
                    return null;
                }
                communicationFiscale = new CPCommunicationFiscale();
                communicationFiscale.setSession(getSession());
                communicationFiscale.setIdTiers(getIdTiers());
                communicationFiscale.setIdIfd(getIdIfd());
                if (modeRecherche == 1) {
                    communicationFiscale.setAlternateKey(1);
                    communicationFiscale.setIdAffiliation(getIdAffiliation());
                    communicationFiscale.setDateRetour("0");
                    communicationFiscale.setDemandeAnnulee(Boolean.FALSE);
                    communicationFiscale.retrieve();
                    if (communicationFiscale.isNew()) {
                        communicationFiscale.setDemandeAnnulee(Boolean.TRUE);
                        communicationFiscale.retrieve();
                    }
                } else if (modeRecherche == 2) {
                    communicationFiscale.setNumAffilie(getNumAffilie());
                    return communicationFiscale.getCommunicationFiscaleEnvoyee(1);
                } else if (modeRecherche == 3) {
                    communicationFiscale.setIdTiers(getIdConjoint());
                    communicationFiscale.setAlternateKey(1);
                    communicationFiscale.setIdAffiliation(getIdAffiliationConjoint());
                    communicationFiscale.setDateRetour("0");
                    communicationFiscale.setDemandeAnnulee(Boolean.FALSE);
                    communicationFiscale.retrieve();
                    if (communicationFiscale.isNew()) {
                        communicationFiscale.setDemandeAnnulee(Boolean.TRUE);
                        communicationFiscale.retrieve();
                    }
                }
            }
            return communicationFiscale;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public TITiersViewBean getConjoint() {
        if (!JadeStringUtil.isBlankOrZero(getIdConjoint())
                && ((conjoint == null) || conjoint.isNew() || !conjoint.getIdTiers().equalsIgnoreCase(getIdConjoint()))) {
            try {
                if (!getIdConjoint().equals("") && !JadeStringUtil.isNull(getIdConjoint())) {
                    conjoint = new TITiersViewBean();
                    conjoint.setSession(getSession());

                    conjoint.setIdTiers(getIdConjoint());
                    conjoint.retrieve();
                }
            } catch (Exception e) {
                conjoint = null;
            }
        }
        return conjoint;
    }

    @Override
    public java.lang.String getCotisation1() {
        return JANumberFormatter.fmt(cotisation1, true, false, true, 0);
    }

    @Override
    public java.lang.String getCotisation2() {
        return JANumberFormatter.fmt(cotisation2, true, false, true, 0);
    }

    @Override
    public java.lang.String getDateFortune() {
        return dateFortune;
    }

    public String getDateNaissance() {
        if (getTiers() != null) {
            return getTiers().getDateNaissance();
        }
        return "";
    }

    /**
     * Returns the dateRetour.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDateRetour() {
        return dateRetour;
    }

    @Override
    public java.lang.String getDebutExercice1() {
        return debutExercice1;
    }

    @Override
    public java.lang.String getDebutExercice2() {
        return debutExercice2;
    }

    @Override
    public CPDecision getDecisionDeBase() {
        return decisionDeBase;
    }

    @Override
    public CPDecisionViewBean getDecisionGeneree() {
        return decisionGeneree;
    }

    public String getDepensesTrainVie() {
        return depensesTrainVie;
    }

    @Override
    public String getDescription(int cas) {
        return description;
    }

    public String getEMailAddress() {
        return EMailAddress;
    }

    public CPCommunicationPlausibiliteManager getErreursPlausi() {
        CPCommunicationPlausibiliteManager manager = new CPCommunicationPlausibiliteManager();
        manager.setSession(getSession());
        manager.setForIdCommunication(getIdRetour());
        try {
            manager.find();
        } catch (Exception e) {
            return new CPCommunicationPlausibiliteManager();
        }
        return manager;
    }

    @Override
    public String getEtatCivil() {
        return etatCivil;
    }

    @Override
    public java.lang.String getFinExercice1() {
        return finExercice1;
    }

    @Override
    public java.lang.String getFinExercice2() {
        return finExercice2;
    }

    @Override
    public java.lang.String getFortune() {
        try {
            if (!JadeStringUtil.isBlankOrZero(fortune)) {
                return JANumberFormatter.fmt(fortune, true, false, true, 0);
            } else {
                return "0";
            }
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * Returns the generation.
     * 
     * @return Boolean
     */
    @Override
    public Boolean getGeneration() {
        return generation;
    }

    public String getGeNom() {
        return geNom;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:25:35)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getGenreAffilie() {
        return genreAffilie;
    }

    @Override
    public String getGenreConjoint() {
        return genreConjoint;
    }

    @Override
    public java.lang.String getGenreTaxation() {
        return genreTaxation;
    }

    public String getGeNumAffilie() {
        return geNumAffilie;
    }

    public String getGeNumContribuable() {
        return geNumContribuable;
    }

    public String getGePrenom() {
        return gePrenom;
    }

    /**
     * @return
     */
    @Override
    public String getIdAffiliation() {
        return idAffiliation;
    }

    @Override
    public String getIdAffiliationConjoint() {
        return idAffiliationConjoint;
    }

    /**
     * Getter
     */
    @Override
    public java.lang.String getIdCommunication() {
        return idCommunication;
    }

    @Override
    public String getIdConjoint() {
        return idConjoint;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:22:04)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getIdIfd() {
        return idIfd;
    }

    /**
     * @return
     */
    @Override
    public java.lang.String getIdJournalRetour() {
        return idJournalRetour;
    }

    /**
     * Returns the idLog.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getIdLog() {
        return idLog;
    }

    @Override
    public String getIdParametrePlausi() {
        return idParametrePlausi;
    }

    /**
     * Returns the idRetour.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getIdRetour() {
        return idRetour;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:22:41)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getIdTiers() {
        return idTiers;
    }

    public String getImpression() {
        return impression;
    }

    @Override
    public CPJournalRetour getJournalRetour() {
        if (journalRetour == null) {
            journalRetour = new CPJournalRetourViewBean();
            journalRetour.setSession(getSession());
            journalRetour.setIdJournalRetour(getIdJournalRetour());
            try {
                journalRetour.retrieve();
            } catch (Exception e) {
            }
        }
        return journalRetour;

    }

    public String getJuNumContribuable() {
        return juNumContribuable;
    }

    public String getLastDate() {
        return lastDate;
    }

    public String getLastTime() {
        return lastTime;
    }

    public String getLastUser() {
        return lastUser;
    }

    public String[] getListIdRetour() {
        return listIdRetour;
    }

    public String getLocalite() {
        if (getTiers() != null) {
            return getTiers().getLocaliteLong();
        }
        return "";
    }

    /**
     * Affiche renvoe l'image
     */
    public String getLogImage(HttpServletRequest request, String idParam, BSession session) {
        try {
            CPParametrePlausibilite param = new CPParametrePlausibilite();
            param.setSession(session);
            param.setIdParametre(idParam);
            param.retrieve();
            if (param.getTypeMessage().equalsIgnoreCase(CPParametrePlausibilite.CS_MSG_AVERTISSEMENT)) {
                return request.getContextPath() + "/images/information.gif";
            } else {
                return request.getContextPath() + "/images/erreur.gif";
            }
        } catch (Exception e) {
            return request.getContextPath() + "/images/erreur.gif";
        }

    }

    @Override
    public String getMajNumContribuable() {
        return majNumContribuable;
    }

    public String getNeNumContribuable() {
        return neNumContribuable;
    }

    /**
     * @return
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return
     */
    public String getNomPrenom() {
        if ((JadeStringUtil.isEmpty(nom)) && (JadeStringUtil.isEmpty(prenom))) {
            if (!JadeStringUtil.isEmpty(getVdNomPrenom())) {
                return getVdNomPrenom();
            } else if ((!JadeStringUtil.isEmpty(getGeNom())) || (!JadeStringUtil.isEmpty(getGePrenom()))) {
                return getGeNom() + " " + getGePrenom();
            } else if (!JadeStringUtil.isEmpty(getVsNomPrenom())) {
                return getVsNomPrenom();
            } else if (!JadeStringUtil.isEmpty(getVsNomPrenomCjt())) {
                return getVsNomPrenomCjt();
            } else if ((!JadeStringUtil.isEmpty(getSeNom())) || (!JadeStringUtil.isEmpty(getSePrenom()))) {
                return getSeNom() + " " + getSePrenom();
            }
        } else {
            return nom + " " + prenom;
        }
        return nom + " " + prenom;
    }

    /**
     * @return
     */
    public String getNumAffilie() {
        if (JadeStringUtil.isEmpty(numAffilie)) {
            if (!JadeStringUtil.isEmpty(getVdNumAffilie())) {
                return getVdNumAffilie();
            } else if (!JadeStringUtil.isEmpty(getGeNumAffilie())) {
                return getGeNumAffilie();
            } else if (!JadeStringUtil.isEmpty(getVsNumAffilie())) {
                return getVsNumAffilie();
            } else {
                String idAff = idAffiliation;
                if (JadeStringUtil.isBlankOrZero(idAff)) {
                    // idAff = this.idAffiliationConjoint;
                }
                if (!JadeStringUtil.isBlankOrZero(idAff)) {
                    AFAffiliation aff = new AFAffiliation();
                    aff.setSession(getSession());
                    aff.setAffiliationId(idAff);
                    try {
                        aff.retrieve();
                    } catch (Exception e) {
                        return "";
                    }
                    return aff.getAffilieNumero();
                }
            }
        } else {
            return numAffilie;
        }
        return numAffilie;
    }

    @Override
    public String getNumAffilieRecu() {
        return numAffilieRecu;
    }

    // Nécessaire pour les tables de backup
    public String getNumAvs() {
        if (JadeStringUtil.isEmpty(numAvs)) {
            return numAvs;
        } else {
            return NSUtil.unFormatAVS(numAvs);
        }
    }

    /**
     * @return
     */
    @Override
    public String getNumAvs(int codeFormat) {
        try {
            if (codeFormat == 0) {
                if (JadeStringUtil.isEmpty(numAvs)) {
                    return numAvs;
                } else {
                    return NSUtil.unFormatAVS(numAvs);
                }
            } else {
                return NSUtil.formatAVSUnknown(numAvs);
            }
        } catch (Exception e) {
            return numAvs;
        }

    }

    /**
     * @return
     */
    @Override
    public String getNumAvsFisc(int codeFormat) {
        return numAvsFisc;
    }

    public java.lang.String getNumContribuable() {
        if (JadeStringUtil.isEmpty(numContribuable)) {
            if (!JadeStringUtil.isEmpty(getVdNumContribuable())) {
                return getVdNumContribuable();
            } else if (!JadeStringUtil.isEmpty(getJuNumContribuable())) {
                return getJuNumContribuable();
            } else if (!JadeStringUtil.isEmpty(getNeNumContribuable())) {
                return getNeNumContribuable();
            } else if (!JadeStringUtil.isEmpty(getGeNumContribuable())) {
                return getGeNumContribuable();
            } else if (!JadeStringUtil.isEmpty(getVsNumContribuable())) {
                return getVsNumContribuable();
            } else if ((tiers != null) && !tiers.isNew()) {
                return tiers.getNumContribuableActuel();
            } else if ((conjoint != null) && !conjoint.isNew()) {
                return conjoint.getNumContribuableActuel();
            }
        } else {
            return numContribuable;
        }
        return numContribuable;
    }

    @Override
    public java.lang.String getNumContribuableRecu() {
        return numContribuableRecu;
    }

    public String getNumeroAVS(int codeFormat) {
        try {
            if (codeFormat == 0) {
                return numAvs;
            } else {
                return NSUtil.formatAVSUnknown(numAvs);
            }
        } catch (Exception e) {
            return numAvs;
        }
    }

    @Override
    public java.lang.String getNumIfd() {
        return numIfd;
    }

    /**
     * Retourne la période fiscale, null si le l'année n'est pas renseignée
     */
    @Override
    public CPPeriodeFiscale getPeriodeFiscale() {
        try {
            if (periodeFiscale == null) {
                if (JadeStringUtil.isEmpty(getAnnee1())) {
                    return null;
                }
                periodeFiscale = new CPPeriodeFiscale();
                periodeFiscale.setSession(getSession());
                periodeFiscale.setAlternateKey(1);
                periodeFiscale.setAnneeRevenuDebut(getAnnee1());
                periodeFiscale.retrieve();
            }
            return periodeFiscale;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retourne la periode fiscale de la communication ou null si introuvable
     */
    public CPPeriodeFiscale getPeriodeFiscaleAffichage() {
        if (periodeFiscale == null) {
            try {
                periodeFiscale = new CPPeriodeFiscale();
                periodeFiscale.setSession(getSession());
                periodeFiscale.setIdIfd(getIdIfd());
                periodeFiscale.retrieve();
            } catch (Exception e) {
                periodeFiscale = null;
            }
        }
        return periodeFiscale;
    }

    /**
     * @return
     */
    public String getPrenom() {
        return prenom;
    }

    @Override
    public String getRachatLpp() {
        return rachatLpp;
    }

    @Override
    public String getRachatLppCjt() {
        return rachatLppCjt;
    }

    @Override
    public String getReportType() {
        return reportType;
    }

    public String getReportTypeValue() {
        return reportType;
    }

    @Override
    public java.lang.String getRevenu1() {
        if (!JadeStringUtil.isEmpty(revenuAnnee1)) {
            float revenu1 = Float.valueOf(revenuAnnee1);
            return JANumberFormatter.fmt(String.valueOf(revenu1), true, false, true, 0);
        } else {
            return "0";
        }

    }

    @Override
    public java.lang.String getRevenu2() {
        if (!JadeStringUtil.isEmpty(revenuAnnee2)) {
            float revenu2 = Float.valueOf(revenuAnnee2);
            return JANumberFormatter.fmt(String.valueOf(revenu2), true, false, true, 0);
        } else {
            return "0";
        }
    }

    @Override
    public String getRevenuA() {
        if (!JadeStringUtil.isBlankOrZero(autreRevenu)) {
            return JANumberFormatter.fmt(autreRevenu, true, false, false, 0);
        } else {
            return "0";
        }
    }

    @Override
    public String getRevenuAConjoint() {
        return "";
    }

    @Override
    public String getRevenuNA() {
        if (!JadeStringUtil.isBlankOrZero(revenuAnnee1)) {
            return JANumberFormatter.fmt(revenuAnnee1, true, false, false, 0);
        } else {
            return "0";
        }
    }

    @Override
    public String getRevenuNAConjoint() {
        return "";
    }

    @Override
    public String getRevenuR() {
        if (!JadeStringUtil.isBlankOrZero(revenuAnnee1)) {
            return JANumberFormatter.fmt(revenuAnnee1, true, false, false, 0);
        } else {
            return "0";
        }
    }

    @Override
    public String getSalaire() {
        return "";
    }

    @Override
    public String getRemarque() {
        return "";
    }

    @Override
    public String getSalaireConjoint() {
        return "";
    }

    public String getSeNom() {
        return seNom;
    }

    public String getSePrenom() {
        return sePrenom;
    }

    /**
     * Returns the status.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getStatus() {
        return status;
    }

    /**
     * @return
     */
    @Override
    public TITiersViewBean getTiers() {
        // Chargement tiers si idTiers <> vide
        // et si tiers pas déjà chargé avec cet id
        if (!JadeStringUtil.isBlankOrZero(getIdTiers())
                && ((tiers == null) || tiers.isNew() || !tiers.getIdTiers().equalsIgnoreCase(getIdTiers()))) {
            try {
                if (!getIdTiers().equals("") && !JadeStringUtil.isNull(getIdTiers())) {
                    tiers = new TITiersViewBean();
                    tiers.setSession(getSession());
                    tiers.setIdTiers(getIdTiers());
                    tiers.retrieve();
                }
            } catch (Exception e) {
                tiers = null;
            }
        }
        return tiers;
    }

    @Override
    public String getTri() {
        return tri;
    }

    /**
     * @return
     */
    @Override
    public String getValeurChampRecherche() {
        return valeurChampRecherche;
    }

    @Override
    public String getValeurRechercheBD(String zoneRecherche) {
        return "";
    }

    public String getVdNomPrenom() {
        return vdNomPrenom;
    }

    public String getVdNumAffilie() {
        return vdNumAffilie;
    }

    public String getVdNumContribuable() {
        return vdNumContribuable;
    }

    /**
     * Donne le libelle correspondant au status
     * 
     * @return
     */
    @Override
    public String getVisibleStatus() {
        try {
            return globaz.phenix.translation.CodeSystem.getLibelle(getSession(), getStatus());
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return "";
        }
    }

    public String getVsNomPrenom() {
        return vsNomPrenom;
    }

    public String getVsNomPrenomCjt() {
        return vsNomPrenomCjt;
    }

    public String getVsNumAffilie() {
        return vsNumAffilie;
    }

    public String getVsNumContribuable() {
        return vsNumContribuable;
    }

    public Boolean getWantDetail() {
        return wantDetail;
    }

    @Override
    public boolean hasSpy() {
        if (isForRetourOriginale() || !isWantControleSpy()) {
            return false;
        } else {
            return super.hasSpy();
        }
    }

    public boolean isForBackup() {
        return isForBackup;
    }

    public boolean isForRetourOriginale() {
        return isForRetourOriginale;
    }

    @Override
    public boolean isNonActif() {
        if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getGenreAffilie())
                || CPDecision.CS_ETUDIANT.equalsIgnoreCase(getGenreAffilie())
                || CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getGenreConjoint())
                || CPDecision.CS_ETUDIANT.equalsIgnoreCase(getGenreConjoint())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 07.09.2007: Cette méthode retourne si la décision est de genre non actif
     * 
     * @param myDecision
     * @return
     */
    @Override
    public boolean isNonActif(boolean traitementConjoint) {
        if (((CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getGenreAffilie()) || CPDecision.CS_ETUDIANT
                .equalsIgnoreCase(getGenreAffilie())) && !traitementConjoint)
                || ((CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getGenreConjoint()) || CPDecision.CS_ETUDIANT
                        .equalsIgnoreCase(getGenreConjoint())) && traitementConjoint)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isNonActif(String idTiersTraite) {
        if (getIdTiers().equalsIgnoreCase(idTiersTraite)) {
            if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getGenreAffilie())
                    || CPDecision.CS_ETUDIANT.equalsIgnoreCase(getGenreAffilie())) {
                return true;
            }
        }
        if (getIdConjoint().equalsIgnoreCase(idTiersTraite)) {
            if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getGenreConjoint())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return
     */
    public boolean isWantAfterRetrieve() {
        return wantAfterRetrieve;
    }

    public boolean isWantControleSpy() {
        return wantControleSpy;
    }

    public boolean isWantDonneeBase() {
        return wantDonneeBase;
    }

    public boolean isWantDonneeContribuable() {
        return wantDonneeContribuable;
    }

    public boolean isWantMajBackup() {
        return wantMajBackup;
    }

    public boolean isWantPlausibilite() {
        return wantPlausibilite;
    }

    private void majJournal() {
        if (!JadeStringUtil.isEmpty(getIdJournalRetour())) {
            CPJournalRetour jrn = new CPJournalRetour();
            jrn.setSession(getSession());
            jrn.setIdJournalRetour(getIdJournalRetour());
            try {
                jrn.retrieve();
                jrn.update();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Supprime les messages de log d'une communication d'une ceraine source
     * 
     * @param logSource
     *            : CPCommunicationFiscaleRetourViewBean.LOG_SOURCE_xxx
     * @throws IllegalArgumentException
     */
    @Override
    public void removeLogs(String logSource, BTransaction transaction) throws Exception {
        // On va effacer la table des liens !
        CPLienCommunicationsPlausi lien = new CPLienCommunicationsPlausi();
        lien.setSession(getSession());
        lien.setIdCommunication(commFiscaleFils.getIdRetour());
        lien.retrieve();
        if (!lien.isNew()) {
            lien.delete(transaction);
        }

        /*
         * FWLog log = getLog(null); if (log != null) { log.setIdLog(getIdLog()); Enumeration logEnum =
         * log.getMessagesToEnumeration(); boolean deleted = false; String highestLevel = ""; String idHighest = "";
         * while (logEnum.hasMoreElements()) { FWMessage msg = (FWMessage)logEnum.nextElement(); if
         * (msg.getMessageId().equals(logSource)) { msg.delete(transaction); deleted = true; } else { // garde le
         * message d'erreur de plus haut niveau if (msg.getTypeMessage().compareTo(highestLevel) > 0) { highestLevel =
         * msg.getTypeMessage(); idHighest = msg.getIdMessage(); } } } //Si un message a été supprimé il faut mettre à
         * jour le log (HighestMessage) if (deleted) { log.setIdHighestMessage(idHighest);
         * log.setErrorLevel(highestLevel); log.update(transaction); } }
         */
    }

    /**
     * Sets the annee.
     * 
     * @param annee
     *            The annee to set
     */
    @Override
    public void setAnnee1(java.lang.String annee) {
        annee1 = annee;
    }

    @Override
    public void setAnnee2(java.lang.String string) {
        annee2 = string;
    }

    public void setAutreRevenu(String autreRevenu) {
        this.autreRevenu = autreRevenu;
    }

    @Override
    public void setCapital(java.lang.String newCapital) {
        capital = JANumberFormatter.deQuote(newCapital);
    }

    @Override
    public void setChangementGenre(String changementGenre) {
        this.changementGenre = changementGenre;
    }

    @Override
    public void setChangementGenreConjoint(String changementGenreConjoint) {
        this.changementGenreConjoint = changementGenreConjoint;
    }

    /**
     * @param retour
     */
    public void setCommFiscaleFils(ICommunicationRetour retour) {
        commFiscaleFils = retour;
    }

    /**
     * @param fiscale
     */
    @Override
    public void setCommunicationFiscale(CPCommunicationFiscale fiscale) {
        communicationFiscale = fiscale;
    }

    public void setConjoint(TITiersViewBean conjoint) {
        this.conjoint = conjoint;
    }

    @Override
    public void setCotisation1(java.lang.String newCotisation1) {
        cotisation1 = JANumberFormatter.deQuote(newCotisation1);
    }

    public void setCotisation2(java.lang.String newCotisation2) {
        cotisation2 = JANumberFormatter.deQuote(newCotisation2);
    }

    @Override
    public void setDateFortune(java.lang.String newDateFortune) {
        dateFortune = newDateFortune;
    }

    /**
     * Sets the dateRetour.
     * 
     * @param dateRetour
     *            The dateRetour to set
     */
    @Override
    public void setDateRetour(java.lang.String dateRetour) {
        this.dateRetour = dateRetour;
    }

    @Override
    public void setDebutExercice1(java.lang.String newDebutExercice1) {
        debutExercice1 = newDebutExercice1;
    }

    @Override
    public void setDebutExercice2(java.lang.String newDebutExercice2) {
        debutExercice2 = newDebutExercice2;
    }

    @Override
    public void setDecisionDeBase(CPDecision decisionDeBase) {
        this.decisionDeBase = decisionDeBase;
    }

    @Override
    public void setDecisionGeneree(CPDecisionViewBean decisionGenere) {
        decisionGeneree = decisionGenere;
    }

    public void setDepensesTrainVie(String depensesTrainVie) {
        this.depensesTrainVie = depensesTrainVie;
    }

    public void setEMailAddress(String EMailAddress) {
        this.EMailAddress = EMailAddress;
    }

    @Override
    public void setFinExercice1(java.lang.String newFinExercice1) {
        finExercice1 = newFinExercice1;
    }

    @Override
    public void setFinExercice2(java.lang.String newFinExercice2) {
        finExercice2 = newFinExercice2;
    }

    @Override
    public void setForBackup(boolean isForBackup) {
        this.isForBackup = isForBackup;
    }

    public void setForRetourOriginale(boolean isForRetourOriginale) {
        this.isForRetourOriginale = isForRetourOriginale;
    }

    @Override
    public void setFortune(java.lang.String newFortuneTotale) {
        fortune = JANumberFormatter.deQuote(newFortuneTotale);
    }

    /**
     * Sets the generation.
     * 
     * @param generation
     *            The generation to set
     */
    @Override
    public void setGeneration(Boolean generation) {
        this.generation = generation;
    }

    public void setGeNom(String geNom) {
        this.geNom = geNom;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:25:35)
     * 
     * @param newGenreAffilie
     *            java.lang.String
     */
    @Override
    public void setGenreAffilie(java.lang.String newGenreAffilie) {
        genreAffilie = newGenreAffilie;
    }

    @Override
    public void setGenreConjoint(String genreConjoint) {
        this.genreConjoint = genreConjoint;
    }

    /**
     * Setter
     */
    @Override
    public void setGenreTaxation(java.lang.String newGenreTaxation) {
        genreTaxation = newGenreTaxation;
    }

    public void setGeNumAffilie(String geNumAffilie) {
        this.geNumAffilie = geNumAffilie;
    }

    public void setGeNumContribuable(String geNumContribuable) {
        this.geNumContribuable = geNumContribuable;
    }

    public void setGePrenom(String gePrenom) {
        this.gePrenom = gePrenom;
    }

    /**
     * @param string
     */
    @Override
    public void setIdAffiliation(String string) {
        idAffiliation = string;
    }

    @Override
    public void setIdAffiliationConjoint(String idAffiliationConjoint) {
        this.idAffiliationConjoint = idAffiliationConjoint;
    }

    /**
     * Setter
     */
    @Override
    public void setIdCommunication(java.lang.String newIdCommunication) {
        idCommunication = newIdCommunication;
    }

    @Override
    public void setIdConjoint(String idConjoint) {
        this.idConjoint = idConjoint;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:22:04)
     * 
     * @param newIdIfd
     *            java.lang.String
     */
    @Override
    public void setIdIfd(java.lang.String newIdIfd) {
        idIfd = newIdIfd;
    }

    /**
     * @param string
     */
    @Override
    public void setIdJournalRetour(java.lang.String string) {
        idJournalRetour = string;
    }

    /**
     * Sets the idLog.
     * 
     * @param idLog
     *            The idLog to set
     */
    @Override
    public void setIdLog(java.lang.String idLog) {
        this.idLog = idLog;
    }

    @Override
    public void setIdParametrePlausi(String idParametrePlausi) {
        this.idParametrePlausi = idParametrePlausi;
    }

    /**
     * Sets the idRetour.
     * 
     * @param idRetour
     *            The idRetour to set
     */
    @Override
    public void setIdRetour(java.lang.String idRetour) {
        this.idRetour = idRetour;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:22:41)
     * 
     * @param newIdTiers
     *            java.lang.String
     */
    @Override
    public void setIdTiers(java.lang.String newIdTiers) {
        idTiers = newIdTiers;
    }

    public void setImpression(String impression) {
        this.impression = impression;
    }

    @Override
    public void setJournalRetour(CPJournalRetour journalRetour) {
        this.journalRetour = journalRetour;
    }

    public void setJuNumContribuable(String juNumContribuable) {
        this.juNumContribuable = juNumContribuable;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public void setLastUser(String lastUser) {
        this.lastUser = lastUser;
    }

    public void setListIdRetour(String[] listIdRetour) {
        this.listIdRetour = listIdRetour;
    }

    @Override
    public void setMajNumContribuable(String majNumContribuale) {
        majNumContribuable = majNumContribuale;
    }

    public void setNeNumContribuable(String neNumContribuable) {
        this.neNumContribuable = neNumContribuable;
    }

    @Override
    public void setNumAffilie(java.lang.String newNumAffilie) {
        numAffilie = newNumAffilie;
    }

    public void setNumAffilieRecu(String numAffilieRecu) {
        this.numAffilieRecu = numAffilieRecu;
    }

    /**
     * @param string
     */
    public void setNumContribuable(java.lang.String string) {
        numContribuable = string;
    }

    public void setNumContribuableRecu(java.lang.String numContribuableRecu) {
        this.numContribuableRecu = numContribuableRecu;
    }

    @Override
    public void setNumIfd(java.lang.String string) {
        numIfd = string;
    }

    /**
     * @param fiscale
     */
    @Override
    public void setPeriodeFiscale(CPPeriodeFiscale fiscale) {
        periodeFiscale = fiscale;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    @Override
    public void setRevenu1(java.lang.String newRevenu1) {
        revenuAnnee1 = JANumberFormatter.deQuote(newRevenu1);
    }

    @Override
    public void setRevenu2(java.lang.String newRevenu2) {
        revenuAnnee2 = JANumberFormatter.deQuote(newRevenu2);
    }

    public void setRevenuAnnee1(java.lang.String string) {
        revenuAnnee1 = string;
    }

    public void setRevenuAnnee2(java.lang.String string) {
        revenuAnnee2 = string;
    }

    public void setSeNom(String seNom) {
        this.seNom = seNom;
    }

    public void setSePrenom(String sePrenom) {
        this.sePrenom = sePrenom;
    }

    /**
     * Sets the status.
     * 
     * @param status
     *            The status to set
     */
    @Override
    public void setStatus(java.lang.String codeErreur) {
        status = codeErreur;
    }

    /**
     * @param bean
     */
    @Override
    public void setTiers(TITiersViewBean bean) {
        tiers = bean;
    }

    @Override
    public void setTri(String tri) {
        this.tri = tri;
    }

    /**
     * @param string
     */
    @Override
    public void setValeurChampRecherche(String string) {
        valeurChampRecherche = string;
    }

    public void setVdNomPrenom(String vdNomPrenom) {
        this.vdNomPrenom = vdNomPrenom;
    }

    public void setVdNumAffilie(String vdNumAffilie) {
        this.vdNumAffilie = vdNumAffilie;
    }

    public void setVdNumContribuable(String vdNumContribuable) {
        this.vdNumContribuable = vdNumContribuable;
    }

    public void setVsNomPrenom(String vsNomPrenom) {
        this.vsNomPrenom = vsNomPrenom;
    }

    public void setVsNomPrenomCjt(String vsNomPrenomCjt) {
        this.vsNomPrenomCjt = vsNomPrenomCjt;
    }

    public void setVsNumAffilie(String vsNumAffilie) {
        this.vsNumAffilie = vsNumAffilie;
    }

    public void setVsNumContribuable(String vsNumContribuable) {
        this.vsNumContribuable = vsNumContribuable;
    }

    /**
     * @param b
     */
    @Override
    public void setWantAfterRetrieve(boolean b) {
        wantAfterRetrieve = b;
    }

    public void setWantControleSpy(boolean wantControleSpy) {
        this.wantControleSpy = wantControleSpy;
    }

    public void setWantDetail(Boolean wantDetail) {
        this.wantDetail = wantDetail;
    }

    @Override
    public void setWantDonneeBase(boolean wantDonneeBase) {
        this.wantDonneeBase = wantDonneeBase;
    }

    @Override
    public void setWantDonneeContribuable(boolean wantDonneeContribuable) {
        this.wantDonneeContribuable = wantDonneeContribuable;
    }

    public void setWantMajBackup(boolean wantMajBackup) {
        this.wantMajBackup = wantMajBackup;
    }

    public void setWantPlausibilite(boolean wantPlausibilite) {
        this.wantPlausibilite = wantPlausibilite;
    }

    /*
     * implementation de update()
     */
    @Override
    public void updateCas(BTransaction transaction) throws Exception {
        this.update(transaction);
    }

    /**
     * Permet de savoir si la caisse en question est la CCCVS
     * DDS : S141124_011
     * 
     * @return true si la caisse est la CCCVS, false sinon
     */
    public boolean isCaisseCCCVS() {

        try {
            CPApplication app = (CPApplication) GlobazServer.getCurrentSystem().getApplication(
                    CPApplication.DEFAULT_APPLICATION_PHENIX);

            return app.isCaisseCCCVS();
        } catch (Exception e) {
            JadeLogger.error(this, "Unabled to retrieve the propertie 'common.noCaisseFormate' " + e.getMessage());
        }

        return false;
    }
}
