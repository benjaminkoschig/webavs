/*
 * Créé le 23 nov. 05
 */
package globaz.apg.db.annonces;

import java.util.Hashtable;
import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BEntity;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRHierarchique;
import globaz.prestation.tools.PRSession;
import globaz.pyxis.api.ITIPays;
import globaz.pyxis.application.TIApplication;

/**
 * <H1>Description</H1>
 *
 * @author dvh
 */
public class APAnnonceAPG extends BEntity implements PRHierarchique {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_BREAK_RULES = "VKBRRU";
    public static final String FIELDNAME_BUSINESS_PROCESS_ID = "VKBPID";
    public static final String FIELDNAME_CANTONETAT = "VKACAE";
    public static final String FIELDNAME_CODEAPPLICATION = "VKACAP";
    public static final String FIELDNAME_CODEENREGISTREMENT = "VKACOE";
    public static final String FIELDNAME_CONTENUANNONCE = "VKACOA";
    public static final String FIELDNAME_DATEDEBUTDROIT = "VKDDDR";
    public static final String FIELDNAME_DATEENVOI = "VKDENV";
    public static final String FIELDNAME_DATEFINDROIT = "VKDFDR";
    public static final String FIELDNAME_ENVELOPEMESSAGEID = "VKEMID";
    public static final String FIELDNAME_ETAT = "VKTETA";
    public static final String FIELDNAME_ETATCIVIL = "VKAETC";
    public static final String FIELDNAME_EVENT_DATE = "VKEVDA";
    public static final String FIELDNAME_GARANTIEIJ = "VKBGIJ";
    public static final String FIELDNAME_GENRE = "VKAGEN";
    public static final String FIELDNAME_GENREACTIVITE = "VKAGEA";
    public static final String FIELDNAME_IDANNONCE = "VKIANN";
    public static final String FIELDNAME_IDPARENT = "VKIPAR";
    public static final String FIELDNAME_INSURANT_DOMICILE_COUNTRY = "VKINCO";
    public static final String FIELDNAME_ISALLOCATIONBASE = "VKBALB";
    public static final String FIELDNAME_ISALLOCATIONEXPLOITATION = "VKBALE";
    public static final String FIELDNAME_ISALLOCATIONFRAISGARDE = "VKBAFG";
    public static final String FIELDNAME_ISALLOCATIONISOLEEFRAISGARDE = "VKBAIF";
    public static final String FIELDNAME_ISALLOCATIONMENAGE = "VKBAME";
    public static final String FIELDNAME_ISALLOCATIONPERSONNESEULE = "VKBAPS";
    public static final String FIELDNAME_ISRECRUE = "VKBREC";
    public static final String FIELDNAME_MESSAGE_TYPE = "VKMTYP";
    public static final String FIELDNAME_MODEPAIEMENT = "VKAMOP";
    public static final String FIELDNAME_MOISANNEECOMPTABLE = "VKNMAC";
    public static final String FIELDNAME_MONTANTALLOCATIONASSISTANCE = "VKMMAA";
    public static final String FIELDNAME_MONTANTALLOCATIONEXPLOITATION = "VKMMFG";
    public static final String FIELDNAME_MONTANTALLOCATIONFRAISGARDE = "VKMMFG";
    public static final String FIELDNAME_NOMBREENFANTS = "VKNNBE";
    public static final String FIELDNAME_NOMBREJOURSSERVICE = "VKNNJS";
    public static final String FIELDNAME_NUMEROAGENCE = "VKANOA";
    public static final String FIELDNAME_NUMEROASSURE = "VKNNOA";
    public static final String FIELDNAME_NUMEROASSUREPEREENFANT = "VKNAPE";
    public static final String FIELDNAME_NUMEROCAISSE = "VKANOC";
    public static final String FIELDNAME_NUMEROCOMPTE = "VKNNCO";
    public static final String FIELDNAME_NUMEROCONTROLE = "VKNNOC";
    public static final String FIELDNAME_PERIODEA = "VKDPEA";
    public static final String FIELDNAME_PERIODEDE = "VKDPDE";
    public static final String FIELDNAME_REVENUMOYENDETERMINANT = "VKMRMD";
    public static final String FIELDNAME_SIGNEALLOCATIONFRAISGARDE = "VKBCVA";
    public static final String FIELDNAME_SIGNEMONTANTALLOCATION = "VKASMA";
    public static final String FIELDNAME_SUB_MESSAGE_TYPE = "VKMSTY";
    public static final String FIELDNAME_TAUXJOURNALIER = "VKMTAJ";
    public static final String FIELDNAME_TAUXJOURNALIERALLOCATIONBASE = "VKMTAB";
    public static final String FIELDNAME_TIME_STAMP = "VKTSTA";
    public static final String FIELDNAME_TOTALAPG = "VKMTOA";
    public static final String FIELDNAME_TYPEANNONCE = "VKTTYP";
    //Paternité
    public static final String FIELDNAME_PAYS_NAISSANCE_ENFANT = "VKCHILDCOUNTRYBORN";
    public static final String FIELDNAME_CANTON_NAISSANCE_ENFANT = "VKCHILDCANTONBORN";
    public static final String FIELDNAME_NSS_ENFANT_OLDEST = "VKCHILDNSS";
    public static final String FIELDNAME_DATE_NAISSANCE_ENFANT_PLUS_AGE = "VKCHILDBIRTHDAY";
    public static final String FIELDNAME_NB_JOURS_OUVRABLES = "VKNUMWORKDAY";
    public static final String FIELDNAME_TYPE_PATERNITE = "VKPATTYPE";

    public static final String TABLE_NAME = "APANNOP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String action = "";
    private String breakRules = "";
    private String businessProcessId = "";
    private String cantonEtat = "";
    private String codeApplication = "";
    private String codeEnregistrement = "";
    private String contenuAnnonce = "";
    private String dateDebutDroit = "";
    private String dateEnvoi = "";
    private String dateFinDroit = "";
    private String envelopeMessageId = "";
    private String etat = IAPAnnonce.CS_VALIDE;
    private String etatCivil = "";
    private String eventDate = "";
    private String garantieIJ = "";
    private String genre = "";
    private String genreActivite = "";
    private String idAnnonce = "";
    private String idParent = "";
    private String insurantDomicileCountry = "";
    private String isAllocationBase = "";
    private String isAllocationExploitation = "";
    private String isAllocationFraisGarde = "";
    private String isAllocationIsoleeFraisGarde = "";
    private String isAllocationMenage = "";
    private String isAllocationPersonneSeule = "";
    private String isRecrue = "";
    private String messageType = "";
    private String modePaiement = "";
    private String moisAnneeComptable = "";
    private String montantAllocationAssistance = "";
    private String montantAllocationFraisGarde = "";
    private String nombreEnfants = "";
    private String nombreJoursService = "";
    private String numeroAgence = "";
    private String numeroAssure = "";
    private String numeroAssurePereEnfant = "";
    private String numeroCaisse = "";
    private String numeroCompte = "";
    private String numeroControle = "";
    private String periodeA = "";
    private String periodeDe = "";
    private String revenuMoyenDeterminant = "";
    private String signeAllocationFraisGarde = "";
    private String signeMontantAllocation = "";
    private String subMessageType = "";
    private String tauxJournalier = "";
    private String tauxJournalierAllocationBase = "";
    private String timeStamp = "";
    private String totalAPG = "";
    private String typeAnnonce = "";
    private String idDroit = "";
    private String hasComplementCIAB = "";


    //Paternité
    private String paysNaissanceEnfant = "";
    private String cantonNaissanceEnfant = "";
    private String nssEnfantOldestDroit ="";
    private String dateNaissanceEnfant = "";
    private String nombreJoursOuvrable ="";
    private String typePaternite = "";

    /**
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     * @param transaction
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        APPrestationManager prestationManager = new APPrestationManager();
        prestationManager.setSession(getSession());
        prestationManager.setForIdAnnonce(idAnnonce);
        prestationManager.find(transaction);

        if (!prestationManager.isEmpty()) {
            APPrestation prestation = (APPrestation) prestationManager.getEntity(0);
            prestation.setIdAnnonce("");
            prestation.update(transaction);
        }
    }

    /**
     * @param transaction
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdAnnonce(this._incCounter(transaction, "0"));
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return APAnnonceAPG.TABLE_NAME;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idAnnonce = statement.dbReadNumeric(APAnnonceAPG.FIELDNAME_IDANNONCE);
        codeApplication = statement.dbReadString(APAnnonceAPG.FIELDNAME_CODEAPPLICATION);
        codeEnregistrement = statement.dbReadString(APAnnonceAPG.FIELDNAME_CODEENREGISTREMENT);
        numeroCaisse = statement.dbReadString(APAnnonceAPG.FIELDNAME_NUMEROCAISSE);
        numeroAgence = statement.dbReadString(APAnnonceAPG.FIELDNAME_NUMEROAGENCE);
        moisAnneeComptable = formatMoisAnneeComptableFromDB(
                statement.dbReadNumeric(APAnnonceAPG.FIELDNAME_MOISANNEECOMPTABLE));
        contenuAnnonce = statement.dbReadString(APAnnonceAPG.FIELDNAME_CONTENUANNONCE);
        genre = statement.dbReadString(APAnnonceAPG.FIELDNAME_GENRE);
        numeroCompte = statement.dbReadString(APAnnonceAPG.FIELDNAME_NUMEROCOMPTE);
        numeroAssure = statement.dbReadString(APAnnonceAPG.FIELDNAME_NUMEROASSURE);
        numeroControle = statement.dbReadString(APAnnonceAPG.FIELDNAME_NUMEROCONTROLE);
        cantonEtat = statement.dbReadString(APAnnonceAPG.FIELDNAME_CANTONETAT);
        etatCivil = statement.dbReadString(APAnnonceAPG.FIELDNAME_ETATCIVIL);
        genreActivite = statement.dbReadString(APAnnonceAPG.FIELDNAME_GENREACTIVITE);
        revenuMoyenDeterminant = statement.dbReadNumeric(APAnnonceAPG.FIELDNAME_REVENUMOYENDETERMINANT);
        nombreEnfants = statement.dbReadNumeric(APAnnonceAPG.FIELDNAME_NOMBREENFANTS);
        periodeDe = statement.dbReadDateAMJ(APAnnonceAPG.FIELDNAME_PERIODEDE);
        periodeA = statement.dbReadDateAMJ(APAnnonceAPG.FIELDNAME_PERIODEA);
        nombreJoursService = statement.dbReadNumeric(APAnnonceAPG.FIELDNAME_NOMBREJOURSSERVICE);
        tauxJournalier = statement.dbReadNumeric(APAnnonceAPG.FIELDNAME_TAUXJOURNALIER);
        garantieIJ = statement.dbReadString(APAnnonceAPG.FIELDNAME_GARANTIEIJ);
        isAllocationExploitation = statement.dbReadString(APAnnonceAPG.FIELDNAME_ISALLOCATIONEXPLOITATION);
        isAllocationFraisGarde = statement.dbReadString(APAnnonceAPG.FIELDNAME_ISALLOCATIONFRAISGARDE);
        montantAllocationFraisGarde = statement.dbReadNumeric(APAnnonceAPG.FIELDNAME_MONTANTALLOCATIONFRAISGARDE);
        totalAPG = statement.dbReadNumeric(APAnnonceAPG.FIELDNAME_TOTALAPG);
        modePaiement = statement.dbReadString(APAnnonceAPG.FIELDNAME_MODEPAIEMENT);
        numeroAssurePereEnfant = statement.dbReadString(APAnnonceAPG.FIELDNAME_NUMEROASSUREPEREENFANT);
        dateDebutDroit = statement.dbReadDateAMJ(APAnnonceAPG.FIELDNAME_DATEDEBUTDROIT);
        dateFinDroit = statement.dbReadDateAMJ(APAnnonceAPG.FIELDNAME_DATEFINDROIT);
        etat = statement.dbReadNumeric(APAnnonceAPG.FIELDNAME_ETAT);
        isRecrue = statement.dbReadString(APAnnonceAPG.FIELDNAME_ISRECRUE);
        isAllocationPersonneSeule = statement.dbReadString(APAnnonceAPG.FIELDNAME_ISALLOCATIONPERSONNESEULE);
        isAllocationMenage = statement.dbReadString(APAnnonceAPG.FIELDNAME_ISALLOCATIONMENAGE);
        montantAllocationAssistance = statement.dbReadNumeric(APAnnonceAPG.FIELDNAME_MONTANTALLOCATIONASSISTANCE);
        signeMontantAllocation = statement.dbReadString(APAnnonceAPG.FIELDNAME_SIGNEMONTANTALLOCATION);
        isAllocationBase = statement.dbReadString(APAnnonceAPG.FIELDNAME_ISALLOCATIONBASE);
        tauxJournalierAllocationBase = statement.dbReadNumeric(APAnnonceAPG.FIELDNAME_TAUXJOURNALIERALLOCATIONBASE);
        signeAllocationFraisGarde = statement.dbReadString(APAnnonceAPG.FIELDNAME_SIGNEALLOCATIONFRAISGARDE);
        isAllocationIsoleeFraisGarde = statement.dbReadString(APAnnonceAPG.FIELDNAME_ISALLOCATIONISOLEEFRAISGARDE);
        dateEnvoi = statement.dbReadDateAMJ(APAnnonceAPG.FIELDNAME_DATEENVOI);
        idParent = statement.dbReadNumeric(APAnnonceAPG.FIELDNAME_IDPARENT);
        typeAnnonce = statement.dbReadNumeric(APAnnonceAPG.FIELDNAME_TYPEANNONCE);
        breakRules = statement.dbReadString(APAnnonceAPG.FIELDNAME_BREAK_RULES);
        businessProcessId = statement.dbReadString(APAnnonceAPG.FIELDNAME_BUSINESS_PROCESS_ID);
        eventDate = statement.dbReadDateAMJ(APAnnonceAPG.FIELDNAME_EVENT_DATE);
        messageType = statement.dbReadNumeric(APAnnonceAPG.FIELDNAME_MESSAGE_TYPE);
        subMessageType = statement.dbReadString(APAnnonceAPG.FIELDNAME_SUB_MESSAGE_TYPE);
        insurantDomicileCountry = statement.dbReadNumeric(APAnnonceAPG.FIELDNAME_INSURANT_DOMICILE_COUNTRY);
        timeStamp = statement.dbReadNumeric(APAnnonceAPG.FIELDNAME_TIME_STAMP);
        envelopeMessageId = statement.dbReadString(APAnnonceAPG.FIELDNAME_ENVELOPEMESSAGEID);
        //Paternité
        paysNaissanceEnfant =  statement.dbReadString(APAnnonceAPG.FIELDNAME_PAYS_NAISSANCE_ENFANT);
        cantonNaissanceEnfant =  statement.dbReadString(APAnnonceAPG.FIELDNAME_CANTON_NAISSANCE_ENFANT);
        nssEnfantOldestDroit = statement.dbReadString(APAnnonceAPG.FIELDNAME_NSS_ENFANT_OLDEST);
        dateNaissanceEnfant =  statement.dbReadString(APAnnonceAPG.FIELDNAME_DATE_NAISSANCE_ENFANT_PLUS_AGE);
        nombreJoursOuvrable = statement.dbReadString(APAnnonceAPG.FIELDNAME_NB_JOURS_OUVRABLES);
        typePaternite =  statement.dbReadString(APAnnonceAPG.FIELDNAME_TYPE_PATERNITE);
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {

        if (IAPAnnonce.CS_APGSEDEX.equals(typeAnnonce)) {

            // Si le canton est rempli, on contrôle le code
            if (!JadeStringUtil.isBlankOrZero((getCantonEtat()))) {
                if (!PRTiersHelper.isCodeOFASCanton(getCantonEtat())) {
                    _addError(statement.getTransaction(),
                            FWMessageFormat.format(getSession().getLabel("ERROR_CODE_CENTRALE"), getNumeroAssure()));

                }
            }

            // On contrôle le pays si il est rempli
            if (!JadeStringUtil.isBlankOrZero(getInsurantDomicileCountry())) {

                ITIPays tiPays = (ITIPays) getSession().getAPIFor(ITIPays.class);

                tiPays.setISession(PRSession.connectSession(getSession(), TIApplication.DEFAULT_APPLICATION_PYXIS));

                Hashtable parameters = new Hashtable();
                parameters.put(ITIPays.FIND_FOR_IDPAYS, getInsurantDomicileCountry());

                Object[] result = tiPays.find(parameters);

                if (result.length == 0) {
                    _addError(statement.getTransaction(),
                            FWMessageFormat.format(getSession().getLabel("ERROR_CODE_CENTRALE"), getNumeroAssure()));

                }

            }

        }

    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(APAnnonceAPG.FIELDNAME_IDANNONCE,
                this._dbWriteNumeric(statement.getTransaction(), idAnnonce, "idAnnonce"));
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(APAnnonceAPG.FIELDNAME_IDANNONCE,
                this._dbWriteNumeric(statement.getTransaction(), idAnnonce, "idAnnonce"));
        statement.writeField(APAnnonceAPG.FIELDNAME_CODEAPPLICATION,
                this._dbWriteString(statement.getTransaction(), codeApplication, "codeApplication"));
        statement.writeField(APAnnonceAPG.FIELDNAME_CODEENREGISTREMENT,
                this._dbWriteString(statement.getTransaction(), codeEnregistrement, "codeEnregistrement"));
        statement.writeField(APAnnonceAPG.FIELDNAME_NUMEROCAISSE,
                this._dbWriteString(statement.getTransaction(), numeroCaisse, "numeroCaisse"));
        statement.writeField(APAnnonceAPG.FIELDNAME_NUMEROAGENCE,
                this._dbWriteString(statement.getTransaction(), numeroAgence, "numeroAgence"));

        if (!JadeStringUtil.isEmpty(moisAnneeComptable)) {
            statement.writeField(APAnnonceAPG.FIELDNAME_MOISANNEECOMPTABLE,
                    this._dbWriteNumeric(statement.getTransaction(), formatMoisAnneeComptableToDB(moisAnneeComptable),
                            "moisAnneeComptable"));
        } else {
            statement.writeField(APAnnonceAPG.FIELDNAME_MOISANNEECOMPTABLE, null);
        }

        statement.writeField(APAnnonceAPG.FIELDNAME_CONTENUANNONCE,
                this._dbWriteString(statement.getTransaction(), contenuAnnonce, "contenuAnnonce"));
        statement.writeField(APAnnonceAPG.FIELDNAME_GENRE,
                this._dbWriteString(statement.getTransaction(), genre, "genre"));
        statement.writeField(APAnnonceAPG.FIELDNAME_NUMEROCOMPTE,
                this._dbWriteString(statement.getTransaction(), numeroCompte, "numeroCompte"));
        statement.writeField(APAnnonceAPG.FIELDNAME_NUMEROASSURE,
                this._dbWriteString(statement.getTransaction(), numeroAssure, "numeroAssure"));
        statement.writeField(APAnnonceAPG.FIELDNAME_NUMEROCONTROLE,
                this._dbWriteString(statement.getTransaction(), numeroControle, "numeroControle"));
        statement.writeField(APAnnonceAPG.FIELDNAME_CANTONETAT,
                this._dbWriteString(statement.getTransaction(), cantonEtat, "cantonEtat"));
        statement.writeField(APAnnonceAPG.FIELDNAME_ETATCIVIL,
                this._dbWriteString(statement.getTransaction(), etatCivil, "etatCivil"));
        statement.writeField(APAnnonceAPG.FIELDNAME_GENREACTIVITE,
                this._dbWriteString(statement.getTransaction(), genreActivite, "genreActivite"));

        if (!JadeStringUtil.isEmpty(revenuMoyenDeterminant)) {
            statement.writeField(APAnnonceAPG.FIELDNAME_REVENUMOYENDETERMINANT,
                    this._dbWriteNumeric(statement.getTransaction(), revenuMoyenDeterminant, "revenuMoyenDeterminant"));
        } else {
            statement.writeField(APAnnonceAPG.FIELDNAME_REVENUMOYENDETERMINANT, null);
        }

        if (!JadeStringUtil.isEmpty(nombreEnfants)) {
            statement.writeField(APAnnonceAPG.FIELDNAME_NOMBREENFANTS,
                    this._dbWriteNumeric(statement.getTransaction(), nombreEnfants, "nombreEnfants"));
        } else {
            statement.writeField(APAnnonceAPG.FIELDNAME_NOMBREENFANTS, null);
        }

        statement.writeField(APAnnonceAPG.FIELDNAME_PERIODEDE,
                this._dbWriteDateAMJ(statement.getTransaction(), periodeDe, "periodeDe"));
        statement.writeField(APAnnonceAPG.FIELDNAME_PERIODEA,
                this._dbWriteDateAMJ(statement.getTransaction(), periodeA, "periodeA"));

        if (!JadeStringUtil.isEmpty(nombreJoursService)) {
            statement.writeField(APAnnonceAPG.FIELDNAME_NOMBREJOURSSERVICE,
                    this._dbWriteNumeric(statement.getTransaction(), nombreJoursService, "nombreJoursService"));
        } else {
            statement.writeField(APAnnonceAPG.FIELDNAME_NOMBREJOURSSERVICE, null);
        }

        if (!JadeStringUtil.isEmpty(tauxJournalier)) {
            statement.writeField(APAnnonceAPG.FIELDNAME_TAUXJOURNALIER,
                    this._dbWriteNumeric(statement.getTransaction(), tauxJournalier, "tauxJournalier"));
        } else {
            statement.writeField(APAnnonceAPG.FIELDNAME_TAUXJOURNALIER, null);
        }

        statement.writeField(APAnnonceAPG.FIELDNAME_GARANTIEIJ,
                this._dbWriteString(statement.getTransaction(), garantieIJ, "garantieIJ"));
        statement.writeField(APAnnonceAPG.FIELDNAME_ISALLOCATIONEXPLOITATION,
                this._dbWriteString(statement.getTransaction(), isAllocationExploitation, "isAllocationExploitation"));
        statement.writeField(APAnnonceAPG.FIELDNAME_ISALLOCATIONFRAISGARDE,
                this._dbWriteString(statement.getTransaction(), isAllocationFraisGarde, "isAllocationFraisGarde"));

        if (!JadeStringUtil.isEmpty(montantAllocationFraisGarde)) {
            statement.writeField(APAnnonceAPG.FIELDNAME_MONTANTALLOCATIONFRAISGARDE, this._dbWriteNumeric(
                    statement.getTransaction(), montantAllocationFraisGarde, "montantAllocationFraisGarde"));
        } else {
            statement.writeField(APAnnonceAPG.FIELDNAME_MONTANTALLOCATIONFRAISGARDE, null);
        }

        if (!JadeStringUtil.isEmpty(totalAPG)) {
            statement.writeField(APAnnonceAPG.FIELDNAME_TOTALAPG,
                    this._dbWriteNumeric(statement.getTransaction(), totalAPG, "totalAPG"));
        } else {
            statement.writeField(APAnnonceAPG.FIELDNAME_TOTALAPG, null);
        }

        statement.writeField(APAnnonceAPG.FIELDNAME_MODEPAIEMENT,
                this._dbWriteString(statement.getTransaction(), modePaiement, "modePaiement"));
        statement.writeField(APAnnonceAPG.FIELDNAME_NUMEROASSUREPEREENFANT,
                this._dbWriteString(statement.getTransaction(), numeroAssurePereEnfant, "numeroAssurePereEnfant"));
        statement.writeField(APAnnonceAPG.FIELDNAME_DATEDEBUTDROIT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDebutDroit, "dateDebutDroit"));
        statement.writeField(APAnnonceAPG.FIELDNAME_DATEFINDROIT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFinDroit, "dateFinDroit"));
        statement.writeField(APAnnonceAPG.FIELDNAME_ETAT,
                this._dbWriteNumeric(statement.getTransaction(), etat, "etat"));
        statement.writeField(APAnnonceAPG.FIELDNAME_ISRECRUE,
                this._dbWriteString(statement.getTransaction(), isRecrue, "isRecrue"));
        statement.writeField(APAnnonceAPG.FIELDNAME_ISALLOCATIONPERSONNESEULE, this
                ._dbWriteString(statement.getTransaction(), isAllocationPersonneSeule, "isAllocationPersonneSeule"));
        statement.writeField(APAnnonceAPG.FIELDNAME_ISALLOCATIONMENAGE,
                this._dbWriteString(statement.getTransaction(), isAllocationMenage, "isAllocationMenage"));

        if (!JadeStringUtil.isEmpty(montantAllocationAssistance)) {
            statement.writeField(APAnnonceAPG.FIELDNAME_MONTANTALLOCATIONASSISTANCE, this._dbWriteNumeric(
                    statement.getTransaction(), montantAllocationAssistance, "montantAllocationAssistance"));
        } else {
            statement.writeField(APAnnonceAPG.FIELDNAME_MONTANTALLOCATIONASSISTANCE, null);
        }

        statement.writeField(APAnnonceAPG.FIELDNAME_SIGNEMONTANTALLOCATION,
                this._dbWriteString(statement.getTransaction(), signeMontantAllocation, "signeMontantAllocation"));
        statement.writeField(APAnnonceAPG.FIELDNAME_ISALLOCATIONBASE,
                this._dbWriteString(statement.getTransaction(), isAllocationBase, "isAllocationBase"));

        if (!JadeStringUtil.isEmpty(tauxJournalierAllocationBase)) {
            statement.writeField(APAnnonceAPG.FIELDNAME_TAUXJOURNALIERALLOCATIONBASE, this._dbWriteNumeric(
                    statement.getTransaction(), tauxJournalierAllocationBase, "tauxJournalierAllocationBase"));
        } else {
            statement.writeField(APAnnonceAPG.FIELDNAME_TAUXJOURNALIERALLOCATIONBASE, null);
        }

        statement.writeField(APAnnonceAPG.FIELDNAME_SIGNEALLOCATIONFRAISGARDE, this
                ._dbWriteString(statement.getTransaction(), signeAllocationFraisGarde, "signeAllocationFraisGarde"));
        statement.writeField(APAnnonceAPG.FIELDNAME_ISALLOCATIONISOLEEFRAISGARDE, this._dbWriteString(
                statement.getTransaction(), isAllocationIsoleeFraisGarde, "isAllocationIsoleeFraisGarde"));
        statement.writeField(APAnnonceAPG.FIELDNAME_DATEENVOI,
                this._dbWriteDateAMJ(statement.getTransaction(), dateEnvoi, "dateEnvoi"));
        statement.writeField(APAnnonceAPG.FIELDNAME_IDPARENT,
                this._dbWriteNumeric(statement.getTransaction(), idParent, "idParent"));
        statement.writeField(APAnnonceAPG.FIELDNAME_TYPEANNONCE,
                this._dbWriteNumeric(statement.getTransaction(), typeAnnonce, "typeAnnonce"));

        statement.writeField(APAnnonceAPG.FIELDNAME_BREAK_RULES,
                this._dbWriteString(statement.getTransaction(), breakRules, "breakRules"));
        statement.writeField(APAnnonceAPG.FIELDNAME_BUSINESS_PROCESS_ID,
                this._dbWriteString(statement.getTransaction(), businessProcessId, "businessProcessId"));
        statement.writeField(APAnnonceAPG.FIELDNAME_EVENT_DATE,
                this._dbWriteDateAMJ(statement.getTransaction(), eventDate, "eventDate"));
        statement.writeField(APAnnonceAPG.FIELDNAME_MESSAGE_TYPE,
                this._dbWriteNumeric(statement.getTransaction(), messageType, "messageType"));
        statement.writeField(APAnnonceAPG.FIELDNAME_SUB_MESSAGE_TYPE,
                this._dbWriteString(statement.getTransaction(), subMessageType, "subMessageType"));
        statement.writeField(APAnnonceAPG.FIELDNAME_INSURANT_DOMICILE_COUNTRY,
                this._dbWriteNumeric(statement.getTransaction(), insurantDomicileCountry, "insurantDomicileCountry"));
        statement.writeField(APAnnonceAPG.FIELDNAME_TIME_STAMP,
                this._dbWriteNumeric(statement.getTransaction(), timeStamp, "timeStamp"));
        statement.writeField(APAnnonceAPG.FIELDNAME_ENVELOPEMESSAGEID,
                this._dbWriteString(statement.getTransaction(), envelopeMessageId, "envelopeMessageId"));
        //Paternité
        if (!JadeStringUtil.isEmpty(paysNaissanceEnfant)) {
            statement.writeField(APAnnonceAPG.FIELDNAME_PAYS_NAISSANCE_ENFANT, this._dbWriteString(
                    statement.getTransaction(), paysNaissanceEnfant, "paysNaissanceEnfant"));
        } else {
            statement.writeField(APAnnonceAPG.FIELDNAME_PAYS_NAISSANCE_ENFANT, null);
    }
        if (!JadeStringUtil.isEmpty(cantonNaissanceEnfant)) {
            statement.writeField(APAnnonceAPG.FIELDNAME_CANTON_NAISSANCE_ENFANT, this._dbWriteString(
                    statement.getTransaction(), cantonNaissanceEnfant, "cantonNaissanceEnfant"));
        } else {
            statement.writeField(APAnnonceAPG.FIELDNAME_CANTON_NAISSANCE_ENFANT, null);
        }
        if (!JadeStringUtil.isEmpty(nssEnfantOldestDroit)) {
            statement.writeField(APAnnonceAPG.FIELDNAME_NSS_ENFANT_OLDEST, this._dbWriteString(
                    statement.getTransaction(), nssEnfantOldestDroit, "nssEnfantOldestDroit"));
        } else {
            statement.writeField(APAnnonceAPG.FIELDNAME_NSS_ENFANT_OLDEST, null);
        }
        if (!JadeStringUtil.isEmpty(dateNaissanceEnfant)) {
            statement.writeField(APAnnonceAPG.FIELDNAME_DATE_NAISSANCE_ENFANT_PLUS_AGE, this._dbWriteString(
                    statement.getTransaction(), dateNaissanceEnfant, "dateNaissanceEnfant"));
        } else {
            statement.writeField(APAnnonceAPG.FIELDNAME_DATE_NAISSANCE_ENFANT_PLUS_AGE, null);
        }
        if (!JadeStringUtil.isEmpty(nombreJoursOuvrable)) {
            statement.writeField(APAnnonceAPG.FIELDNAME_NB_JOURS_OUVRABLES, this._dbWriteNumeric(
                    statement.getTransaction(), nombreJoursOuvrable, "nombreJoursOuvrable"));
        } else {
            statement.writeField(APAnnonceAPG.FIELDNAME_NB_JOURS_OUVRABLES, null);
        }
        if (!JadeStringUtil.isEmpty(typePaternite)) {
            statement.writeField(APAnnonceAPG.FIELDNAME_TYPE_PATERNITE, this._dbWriteString(
                    statement.getTransaction(), typePaternite, "typePaternite"));
        } else {
            statement.writeField(APAnnonceAPG.FIELDNAME_TYPE_PATERNITE, null);
        }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * copie tous les champs excepté la clef primaire dans un nouvel entity, et donne la session du parent
     *
     * @return DOCUMENT ME!
     */
    public APAnnonceAPG createClone() {
        APAnnonceAPG annonce = new APAnnonceAPG();
        annonce.setSession(getSession());
        annonce.setCodeApplication(codeApplication);
        annonce.setCodeEnregistrement(codeEnregistrement);
        annonce.setNumeroCaisse(numeroCaisse);
        annonce.setNumeroAgence(numeroAgence);
        annonce.setMoisAnneeComptable(moisAnneeComptable);
        annonce.setContenuAnnonce(contenuAnnonce);
        annonce.setGenre(genre);
        annonce.setNumeroCompte(numeroCompte);
        annonce.setNumeroAssure(numeroAssure);
        annonce.setNumeroControle(numeroControle);
        annonce.setCantonEtat(cantonEtat);
        annonce.setEtatCivil(etatCivil);
        annonce.setGenreActivite(genreActivite);
        annonce.setRevenuMoyenDeterminant(revenuMoyenDeterminant);
        annonce.setNombreEnfants(nombreEnfants);
        annonce.setPeriodeDe(periodeDe);
        annonce.setPeriodeA(periodeA);
        annonce.setNombreJoursService(nombreJoursService);
        annonce.setTauxJournalier(tauxJournalier);
        annonce.setGarantieIJ(garantieIJ);
        annonce.setIsAllocationExploitation(isAllocationExploitation);
        annonce.setIsAllocationFraisGarde(isAllocationFraisGarde);
        annonce.setMontantAllocationFraisGarde(montantAllocationFraisGarde);
        annonce.setTotalAPG(totalAPG);
        annonce.setModePaiement(modePaiement);
        annonce.setNumeroAssurePereEnfant(numeroAssurePereEnfant);
        annonce.setDateDebutDroit(dateDebutDroit);
        annonce.setDateFinDroit(dateFinDroit);
        annonce.setEtat(etat);
        annonce.setIsRecrue(isRecrue);
        annonce.setIsAllocationPersonneSeule(isAllocationPersonneSeule);
        annonce.setIsAllocationMenage(isAllocationMenage);
        annonce.setMontantAllocationAssistance(montantAllocationAssistance);
        annonce.setSigneMontantAllocation(signeMontantAllocation);
        annonce.setIsAllocationBase(isAllocationBase);
        annonce.setTauxJournalierAllocationBase(tauxJournalierAllocationBase);
        annonce.setSigneAllocationFraisGarde(signeAllocationFraisGarde);
        annonce.setIsAllocationIsoleeFraisGarde(isAllocationFraisGarde);
        annonce.setDateEnvoi(dateEnvoi);
        annonce.setIdParent(idParent);
        annonce.setTypeAnnonce(typeAnnonce);
        annonce.setBreakRules(breakRules);
        annonce.setBusinessProcessId(businessProcessId);
        annonce.setEventDate(eventDate);
        annonce.setMessageType(messageType);
        annonce.setSubMessageType(subMessageType);
        annonce.setInsurantDomicileCountry(insurantDomicileCountry);
        annonce.setTimeStamp(timeStamp);

        //Paternité
        annonce.setPaysNaissanceEnfant(paysNaissanceEnfant);
        annonce.setNssEnfantOldestDroit(nssEnfantOldestDroit);
        annonce.setDateNaissanceEnfant(dateNaissanceEnfant);
        annonce.setNombreJoursOuvrable(nombreJoursOuvrable);
        annonce.setTypePaternite(typePaternite);


        return annonce;
    }

    private String formatMoisAnneeComptableFromDB(String date) {
        try {
            String ret = JACalendar.format(date.substring(4, 6) + date.substring(0, 4), JACalendar.FORMAT_MMsYYYY);

            return ret;
        } catch (Exception e) {
            return date;
        }
    }

    private String formatMoisAnneeComptableToDB(String date) {
        try {
            if (date.length() == 6) {
                return date.substring(2) + "0" + date.substring(0, 1);
            } else {
                return date.substring(3) + date.substring(0, 2);
            }
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * Permet de passer d'une annonce RAPG à une annonce en base de donnée
     *
     * @param champsAnnonce
     */
    public void fromChampsAnnonce(APChampsAnnonce champsAnnonce) {

        setIdAnnonce(champsAnnonce.getMessageId());
        setNumeroCaisse(champsAnnonce.getDeliveryOfficeOfficeIdentifier());
        setNumeroAgence(champsAnnonce.getDeliveryOfficeBranch());
        setMoisAnneeComptable(champsAnnonce.getAccountingMonth());
        setGenre(champsAnnonce.getServiceType());
        setNumeroCompte(champsAnnonce.getReferenceNumber());
        setNumeroAssure(champsAnnonce.getInsurant());
        setNumeroControle(champsAnnonce.getControlNumber());
        setCantonEtat(champsAnnonce.getInsurantDomicileCanton());
        setEtatCivil(champsAnnonce.getInsurantMaritalStatus());
        setGenreActivite(champsAnnonce.getActivityBeforeService());
        setRevenuMoyenDeterminant(champsAnnonce.getAverageDailyIncome());
        setNombreEnfants(champsAnnonce.getNumberOfChildren());
        setPeriodeDe(champsAnnonce.getStartOfPeriod());
        setPeriodeA(champsAnnonce.getEndOfPeriod());
        setNombreJoursService(champsAnnonce.getNumberOfDays());
        setTauxJournalierAllocationBase(champsAnnonce.getBasicDailyAmount());
        setGarantieIJ((champsAnnonce.getDailyIndemnityGuaranteeAI()) ? "1" : "0");
        setIsAllocationExploitation((champsAnnonce.getAllowanceFarm()) ? "1" : "0");
        setMontantAllocationFraisGarde(champsAnnonce.getAllowanceCareExpenses());
        setTotalAPG(champsAnnonce.getTotalAPG());
        setModePaiement(champsAnnonce.getPaymentMethod());
        setDateEnvoi(champsAnnonce.getMessageDate());
        setBreakRules(champsAnnonce.getBreakRules());
        setBusinessProcessId(champsAnnonce.getBusinessProcessId());
        setContenuAnnonce(champsAnnonce.getAction());
        setEventDate(champsAnnonce.getEventDate());
        setMessageType(champsAnnonce.getMessageType());
        setSubMessageType(champsAnnonce.getSubMessageType());
        setInsurantDomicileCountry(champsAnnonce.getInsurantDomicileCountry());
        setEtat(champsAnnonce.getCsEtat());
        setTimeStamp(champsAnnonce.getTimeStamp());
        setEnvelopeMessageId(champsAnnonce.getEnvelopeMessageId());
        setPaysNaissanceEnfant(champsAnnonce.getChildDomicile());
        setCantonNaissanceEnfant(champsAnnonce.getChildCantonBorn());
        setNssEnfantOldestDroit(champsAnnonce.getChildInsurantVn());
        setDateNaissanceEnfant(champsAnnonce.getNewbornDateOfBirth());
        setNombreJoursOuvrable(champsAnnonce.getNumberOfWorkdays());
        setTypePaternite(champsAnnonce.getParternityLeaveType());
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @return the breakRules
     */
    public String getBreakRules() {
        return breakRules;
    }

    /**
     * @return the businessProcessId
     */
    public String getBusinessProcessId() {
        return businessProcessId;
    }

    /**
     * getter pour l'attribut canton etat
     *
     * @return la valeur courante de l'attribut canton etat
     */
    public String getCantonEtat() {
        return cantonEtat;
    }

    /**
     * getter pour l'attribut code application
     *
     * @return la valeur courante de l'attribut code application
     */
    public String getCodeApplication() {
        return codeApplication;
    }

    /**
     * getter pour l'attribut code enregistrement
     *
     * @return la valeur courante de l'attribut code enregistrement
     */
    public String getCodeEnregistrement() {
        return codeEnregistrement;
    }

    /**
     * getter pour l'attribut contenu annonce
     *
     * @return la valeur courante de l'attribut contenu annonce
     */
    public String getContenuAnnonce() {
        return contenuAnnonce;
    }

    /**
     * getter pour l'attribut date debut droit
     *
     * @return la valeur courante de l'attribut date debut droit
     */
    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    /**
     * getter pour l'attribut date envoi
     *
     * @return la valeur courante de l'attribut date envoi
     */
    public String getDateEnvoi() {
        return dateEnvoi;
    }

    /**
     * getter pour l'attribut date fin droit
     *
     * @return la valeur courante de l'attribut date fin droit
     */
    public String getDateFinDroit() {
        return dateFinDroit;
    }

    /**
     * @return the envelopeMessageId
     */
    public String getEnvelopeMessageId() {
        return envelopeMessageId;
    }

    /**
     * getter pour l'attribut etat
     *
     * @return la valeur courante de l'attribut etat
     */
    public String getEtat() {
        return etat;
    }

    /**
     * getter pour l'attribut etat civil
     *
     * @return la valeur courante de l'attribut etat civil
     */
    public String getEtatCivil() {
        return etatCivil;
    }

    /**
     * @return the eventDate
     */
    public String getEventDate() {
        return eventDate;
    }

    /**
     * getter pour l'attribut garantie IJ
     *
     * @return la valeur courante de l'attribut garantie IJ
     */
    public String getGarantieIJ() {
        return garantieIJ;
    }

    /**
     * getter pour l'attribut genre
     *
     * @return la valeur courante de l'attribut genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * getter pour l'attribut genre activite
     *
     * @return la valeur courante de l'attribut genre activite
     */
    public String getGenreActivite() {
        return genreActivite;
    }

    /**
     * getter pour l'attribut id annonce
     *
     * @return la valeur courante de l'attribut id annonce
     */
    public String getIdAnnonce() {
        return idAnnonce;
    }

    /**
     * (non-Javadoc)
     *
     * @see globaz.prestation.tools.PRHierarchique#getIdMajeur()
     * @return la valeur courante de l'attribut id majeur
     */
    @Override
    public String getIdMajeur() {
        return idAnnonce;
    }

    /**
     * getter pour l'attribut id parent
     *
     * @return la valeur courante de l'attribut id parent
     */
    @Override
    public String getIdParent() {
        return idParent;
    }

    /**
     * @return the insurantDomicileCountry
     */
    public String getInsurantDomicileCountry() {
        return insurantDomicileCountry;
    }

    /**
     * getter pour l'attribut is allocation base
     *
     * @return la valeur courante de l'attribut is allocation base
     */
    public String getIsAllocationBase() {
        return isAllocationBase;
    }

    /**
     * getter pour l'attribut is allocation exploitation
     *
     * @return la valeur courante de l'attribut is allocation exploitation
     */
    public String getIsAllocationExploitation() {
        return isAllocationExploitation;
    }

    /**
     * getter pour l'attribut is allocation frais garde
     *
     * @return la valeur courante de l'attribut is allocation frais garde
     */
    public String getIsAllocationFraisGarde() {
        return isAllocationFraisGarde;
    }

    /**
     * getter pour l'attribut is allocation isolee frais garde
     *
     * @return la valeur courante de l'attribut is allocation isolee frais garde
     */
    public String getIsAllocationIsoleeFraisGarde() {
        return isAllocationIsoleeFraisGarde;
    }

    /**
     * getter pour l'attribut is allocation menage
     *
     * @return la valeur courante de l'attribut is allocation menage
     */
    public String getIsAllocationMenage() {
        return isAllocationMenage;
    }

    /**
     * getter pour l'attribut is allocation personne seule
     *
     * @return la valeur courante de l'attribut is allocation personne seule
     */
    public String getIsAllocationPersonneSeule() {
        return isAllocationPersonneSeule;
    }

    /**
     * getter pour l'attribut is recrue
     *
     * @return la valeur courante de l'attribut is recrue
     */
    public String getIsRecrue() {
        return isRecrue;
    }

    /**
     * @return the messageType
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * getter pour l'attribut mode paiement
     *
     * @return la valeur courante de l'attribut mode paiement
     */
    public String getModePaiement() {
        return modePaiement;
    }

    /**
     * getter pour l'attribut mois annee comptable
     *
     * @return la valeur courante de l'attribut mois annee comptable
     */
    public String getMoisAnneeComptable() {
        return moisAnneeComptable;
    }

    /**
     * getter pour l'attribut montant allocation assistance
     *
     * @return la valeur courante de l'attribut montant allocation assistance
     */
    public String getMontantAllocationAssistance() {
        return montantAllocationAssistance;
    }

    /**
     * getter pour l'attribut montant allocation frais garde
     *
     * @return la valeur courante de l'attribut montant allocation frais garde
     */
    public String getMontantAllocationFraisGarde() {
        return montantAllocationFraisGarde;
    }

    /**
     * getter pour l'attribut nombre enfant
     *
     * @return la valeur courante de l'attribut nombre enfant
     */
    public String getNombreEnfants() {
        return nombreEnfants;
    }

    /**
     * getter pour l'attribut nombre jours service
     *
     * @return la valeur courante de l'attribut nombre jours service
     */
    public String getNombreJoursService() {
        return nombreJoursService;
    }

    /**
     * getter pour l'attribut numero agence
     *
     * @return la valeur courante de l'attribut numero agence
     */
    public String getNumeroAgence() {
        return numeroAgence;
    }

    /**
     * getter pour l'attribut numero assure
     *
     * @return la valeur courante de l'attribut numero assure
     */
    public String getNumeroAssure() {
        return numeroAssure;
    }

    /**
     * getter pour l'attribut numero assure pere enfant
     *
     * @return la valeur courante de l'attribut numero assure pere enfant
     */
    public String getNumeroAssurePereEnfant() {
        return numeroAssurePereEnfant;
    }

    /**
     * getter pour l'attribut numero caisse
     *
     * @return la valeur courante de l'attribut numero caisse
     */
    public String getNumeroCaisse() {
        return numeroCaisse;
    }

    /**
     * getter pour l'attribut numero compte
     *
     * @return la valeur courante de l'attribut numero compte
     */
    public String getNumeroCompte() {
        return numeroCompte;
    }

    /**
     * getter pour l'attribut numero controle
     *
     * @return la valeur courante de l'attribut numero controle
     */
    public String getNumeroControle() {
        return numeroControle;
    }

    /**
     * getter pour l'attribut periode a
     *
     * @return la valeur courante de l'attribut periode a
     */
    public String getPeriodeA() {
        return periodeA;
    }

    /**
     * getter pour l'attribut periode de
     *
     * @return la valeur courante de l'attribut periode de
     */
    public String getPeriodeDe() {
        return periodeDe;
    }

    /**
     * @return la période au format JJMM (foramt des les annonces HERMES)
     */
    public String getPeriodeDeFormatte() {
        JADate date = null;
        try {
            date = new JADate(periodeDe);
        } catch (JAException e) {
        }
        String dd = "";
        String mm = "";
        if (date.getDay() < 10) {
            dd = "0" + String.valueOf(date.getDay());
        } else {
            dd = String.valueOf(date.getDay());
        }

        if (date.getMonth() < 10) {
            mm = "0" + String.valueOf(date.getMonth());
        } else {
            mm = String.valueOf(date.getMonth());
        }

        return dd + mm;
    }

    /**
     * getter pour l'attribut revenu moyen determinant
     *
     * @return la valeur courante de l'attribut revenu moyen determinant
     */
    public String getRevenuMoyenDeterminant() {
        return revenuMoyenDeterminant;
    }

    /**
     * getter pour l'attribut signe allocation frais garde
     *
     * @return la valeur courante de l'attribut signe allocation frais garde
     */
    public String getSigneAllocationFraisGarde() {
        return signeAllocationFraisGarde;
    }

    /**
     * getter pour l'attribut signe montant allocation
     *
     * @return la valeur courante de l'attribut signe montant allocation
     */
    public String getSigneMontantAllocation() {
        return signeMontantAllocation;
    }

    /**
     * @return the subMessageType
     */
    public String getSubMessageType() {
        return subMessageType;
    }

    /**
     * getter pour l'attribut taux journalier
     *
     * @return la valeur courante de l'attribut taux journalier
     */
    public String getTauxJournalier() {
        return tauxJournalier;
    }

    /**
     * getter pour l'attribut taux journalier allocation base
     *
     * @return la valeur courante de l'attribut taux journalier allocation base
     */
    public String getTauxJournalierAllocationBase() {
        return tauxJournalierAllocationBase;
    }

    /**
     * @return the timeStamp
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * getter pour l'attribut total APG
     *
     * @return la valeur courante de l'attribut total APG
     */
    public String getTotalAPG() {
        return totalAPG;
    }

    /**
     * getter pour l'attribut type annonce
     *
     * @return la valeur courante de l'attribut type annonce
     */
    public String getTypeAnnonce() {
        return typeAnnonce;
    }

    /**
     * @param action
     *            the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @param breakRules
     *            the breakRules to set
     */
    public void setBreakRules(String breakRules) {
        this.breakRules = breakRules;
    }

    /**
     * @param businessProcessId
     *            the businessProcessId to set
     */
    public void setBusinessProcessId(String businessProcessId) {
        this.businessProcessId = businessProcessId;
    }

    /**
     * setter pour l'attribut canton etat
     *
     * @param cantonEtat
     *            une nouvelle valeur pour cet attribut
     */
    public void setCantonEtat(String cantonEtat) {
        this.cantonEtat = cantonEtat;
    }

    /**
     * setter pour l'attribut code application
     *
     * @param codeApplication
     *            une nouvelle valeur pour cet attribut
     */
    public void setCodeApplication(String codeApplication) {
        this.codeApplication = codeApplication;
    }

    /**
     * setter pour l'attribut code enregistrement
     *
     * @param codeEnregistrement
     *            une nouvelle valeur pour cet attribut
     */
    public void setCodeEnregistrement(String codeEnregistrement) {
        this.codeEnregistrement = codeEnregistrement;
    }

    /**
     * setter pour l'attribut contenu annonce
     *
     * @param contenuAnnonce
     *            une nouvelle valeur pour cet attribut
     */
    public void setContenuAnnonce(String contenuAnnonce) {
        this.contenuAnnonce = contenuAnnonce;
    }

    /**
     * setter pour l'attribut date debut droit
     *
     * @param dateDebutDroit
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    /**
     * setter pour l'attribut date envoi
     *
     * @param dateEnvoi
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    /**
     * setter pour l'attribut date fin droit
     *
     * @param dateFinDroit
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    /**
     * @param envelopeMessageId
     *            the envelopeMessageId to set
     */
    public void setEnvelopeMessageId(String envelopeMessageId) {
        this.envelopeMessageId = envelopeMessageId;
    }

    /**
     * setter pour l'attribut etat
     *
     * @param etat
     *            une nouvelle valeur pour cet attribut
     */
    public void setEtat(String etat) {
        this.etat = etat;
    }

    /**
     * setter pour l'attribut etat civil
     *
     * @param etatCivil
     *            une nouvelle valeur pour cet attribut
     */
    public void setEtatCivil(String etatCivil) {
        this.etatCivil = etatCivil;
    }

    /**
     * @param eventDate
     *            the eventDate to set
     */
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * setter pour l'attribut garantie IJ
     *
     * @param garantieIJ
     *            une nouvelle valeur pour cet attribut
     */
    public void setGarantieIJ(String garantieIJ) {
        this.garantieIJ = garantieIJ;
    }

    /**
     * setter pour l'attribut genre
     *
     * @param genre
     *            une nouvelle valeur pour cet attribut
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * setter pour l'attribut genre activite
     *
     * @param genreActivite
     *            une nouvelle valeur pour cet attribut
     */
    public void setGenreActivite(String genreActivite) {
        this.genreActivite = genreActivite;
    }

    /**
     * setter pour l'attribut id annonce
     *
     * @param idAnnonce
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

    /**
     * setter pour l'attribut id parent
     *
     * @param idParent
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdParent(String idParent) {
        this.idParent = idParent;
    }

    /**
     * @param insurantDomicileCountry
     *            the insurantDomicileCountry to set
     */
    public void setInsurantDomicileCountry(String insurantDomicileCountry) {
        this.insurantDomicileCountry = insurantDomicileCountry;
    }

    /**
     * setter pour l'attribut is allocation base
     *
     * @param isAllocationBase
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsAllocationBase(String isAllocationBase) {
        this.isAllocationBase = isAllocationBase;
    }

    /**
     * setter pour l'attribut is allocation exploitation
     *
     * @param isAllocationExploitation
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsAllocationExploitation(String isAllocationExploitation) {
        this.isAllocationExploitation = isAllocationExploitation;
    }

    /**
     * setter pour l'attribut is allocation frais garde
     *
     * @param isAllocationFraisGarde
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsAllocationFraisGarde(String isAllocationFraisGarde) {
        this.isAllocationFraisGarde = isAllocationFraisGarde;
    }

    /**
     * setter pour l'attribut is allocation isolee frais garde
     *
     * @param isAllocationIsoleeFraisGarde
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsAllocationIsoleeFraisGarde(String isAllocationIsoleeFraisGarde) {
        this.isAllocationIsoleeFraisGarde = isAllocationIsoleeFraisGarde;
    }

    /**
     * setter pour l'attribut is allocation menage
     *
     * @param isAllocationMenage
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsAllocationMenage(String isAllocationMenage) {
        this.isAllocationMenage = isAllocationMenage;
    }

    /**
     * setter pour l'attribut is allocation personne seule
     *
     * @param isAllocationPersonneSeule
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsAllocationPersonneSeule(String isAllocationPersonneSeule) {
        this.isAllocationPersonneSeule = isAllocationPersonneSeule;
    }

    /**
     * setter pour l'attribut is recrue
     *
     * @param isRecrue
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsRecrue(String isRecrue) {
        this.isRecrue = isRecrue;
    }

    /**
     * @param messageType
     *            the messageType to set
     */
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /**
     * setter pour l'attribut mode paiement
     *
     * @param modePaiement
     *            une nouvelle valeur pour cet attribut
     */
    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    /**
     * setter pour l'attribut mois annee comptable
     *
     * @param moisAnneeComptable
     *            une nouvelle valeur pour cet attribut
     */
    public void setMoisAnneeComptable(String moisAnneeComptable) {
        this.moisAnneeComptable = moisAnneeComptable;
    }

    /**
     * setter pour l'attribut montant allocation assistance
     *
     * @param montantAllocationAssistance
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantAllocationAssistance(String montantAllocationAssistance) {
        this.montantAllocationAssistance = montantAllocationAssistance;
    }

    /**
     * setter pour l'attribut montant allocation frais garde
     *
     * @param montantAllocationFraisGarde
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantAllocationFraisGarde(String montantAllocationFraisGarde) {
        this.montantAllocationFraisGarde = montantAllocationFraisGarde;
    }

    /**
     * setter pour l'attribut nombre enfant
     *
     * @param nombreEnfant
     *            une nouvelle valeur pour cet attribut
     */
    public void setNombreEnfants(String nombreEnfant) {
        nombreEnfants = nombreEnfant;
    }

    /**
     * setter pour l'attribut nombre jours service
     *
     * @param nombreJoursService
     *            une nouvelle valeur pour cet attribut
     */
    public void setNombreJoursService(String nombreJoursService) {
        this.nombreJoursService = nombreJoursService;
    }

    /**
     * setter pour l'attribut numero agence
     *
     * @param numeroAgence
     *            une nouvelle valeur pour cet attribut
     */
    public void setNumeroAgence(String numeroAgence) {
        this.numeroAgence = numeroAgence;
    }

    /**
     * setter pour l'attribut numero assure
     *
     * @param numeroAssure
     *            une nouvelle valeur pour cet attribut
     */
    public void setNumeroAssure(String numeroAssure) {
        this.numeroAssure = numeroAssure;
    }

    /**
     * setter pour l'attribut numero assure pere enfant
     *
     * @param numeroAssurePereEnfant
     *            une nouvelle valeur pour cet attribut
     */
    public void setNumeroAssurePereEnfant(String numeroAssurePereEnfant) {
        this.numeroAssurePereEnfant = numeroAssurePereEnfant;
    }

    /**
     * setter pour l'attribut numero caisse
     *
     * @param numeroCaisse
     *            une nouvelle valeur pour cet attribut
     */
    public void setNumeroCaisse(String numeroCaisse) {
        this.numeroCaisse = numeroCaisse;
    }

    /**
     * setter pour l'attribut numero compte
     *
     * @param numeroCompte
     *            une nouvelle valeur pour cet attribut
     */
    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    /**
     * setter pour l'attribut numero controle
     *
     * @param numeroControle
     *            une nouvelle valeur pour cet attribut
     */
    public void setNumeroControle(String numeroControle) {
        this.numeroControle = numeroControle;
    }

    /**
     * setter pour l'attribut periode a
     *
     * @param periodeA
     *            une nouvelle valeur pour cet attribut
     */
    public void setPeriodeA(String periodeA) {
        this.periodeA = periodeA;
    }

    /**
     * setter pour l'attribut periode de
     *
     * @param periodeDe
     *            une nouvelle valeur pour cet attribut
     */
    public void setPeriodeDe(String periodeDe) {
        this.periodeDe = periodeDe;
    }

    /**
     * setter pour l'attribut revenu moyen determinant
     *
     * @param revenuMoyenDeterminant
     *            une nouvelle valeur pour cet attribut
     */
    public void setRevenuMoyenDeterminant(String revenuMoyenDeterminant) {
        this.revenuMoyenDeterminant = revenuMoyenDeterminant;
    }

    /**
     * setter pour l'attribut signe allocation frais garde
     *
     * @param signeAllocationFraisGarde
     *            une nouvelle valeur pour cet attribut
     */
    public void setSigneAllocationFraisGarde(String signeAllocationFraisGarde) {
        this.signeAllocationFraisGarde = signeAllocationFraisGarde;
    }

    /**
     * setter pour l'attribut signe montant allocation
     *
     * @param signeMontantAllocation
     *            une nouvelle valeur pour cet attribut
     */
    public void setSigneMontantAllocation(String signeMontantAllocation) {
        this.signeMontantAllocation = signeMontantAllocation;
    }

    /**
     * @param subMessageType
     *            the subMessageType to set
     */
    public void setSubMessageType(String subMessageType) {
        this.subMessageType = subMessageType;
    }

    /**
     * setter pour l'attribut taux journalier
     *
     * @param tauxJournalier
     *            une nouvelle valeur pour cet attribut
     */
    public void setTauxJournalier(String tauxJournalier) {
        this.tauxJournalier = tauxJournalier;
    }

    /**
     * setter pour l'attribut taux journalier allocation base
     *
     * @param tauxJournalierAllocationBase
     *            une nouvelle valeur pour cet attribut
     */
    public void setTauxJournalierAllocationBase(String tauxJournalierAllocationBase) {
        this.tauxJournalierAllocationBase = tauxJournalierAllocationBase;
    }

    /**
     * @param timeStamp
     *            the timeStamp to set
     */
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * setter pour l'attribut total APG
     *
     * @param totalAPG
     *            une nouvelle valeur pour cet attribut
     */
    public void setTotalAPG(String totalAPG) {
        this.totalAPG = totalAPG;
    }

    /**
     * setter pour l'attribut type annonce
     *
     * @param typeAnnonce
     *            une nouvelle valeur pour cet attribut
     */
    public void setTypeAnnonce(String typeAnnonce) {
        this.typeAnnonce = typeAnnonce;
    }

    /**
     * setter pour l'attribut hasComplementCIAB
     *
     * @param hasComplementCIAB
     *            une nouvelle valeur pour cet attribut
     */
    public void setHasComplementCIAB(String hasComplementCIAB) {
        this.hasComplementCIAB = hasComplementCIAB;
    }

    /**
     * Permet de transformer l'annonce en BD en annonce RAPG
     *
     * @return
     */
    public APChampsAnnonce toChampsAnnonce() {
        APChampsAnnonce champsAnnonce = new APChampsAnnonce();
        champsAnnonce.setMessageId(getId());
        champsAnnonce.setDeliveryOfficeOfficeIdentifier(getNumeroCaisse());
        champsAnnonce.setDeliveryOfficeBranch(getNumeroAgence());
        champsAnnonce.setAccountingMonth(getMoisAnneeComptable());
        champsAnnonce.setServiceType(getGenre());
        champsAnnonce.setReferenceNumber(getNumeroCompte());
        champsAnnonce.setInsurant(getNumeroAssure());
        champsAnnonce.setControlNumber(getNumeroControle());
        champsAnnonce.setInsurantDomicileCanton(getCantonEtat());
        champsAnnonce.setInsurantMaritalStatus(getEtatCivil());
        champsAnnonce.setActivityBeforeService(getGenreActivite());
        champsAnnonce.setAverageDailyIncome(getRevenuMoyenDeterminant());
        champsAnnonce.setNumberOfChildren(getNombreEnfants());
        champsAnnonce.setStartOfPeriod(getPeriodeDe());
        champsAnnonce.setEndOfPeriod(getPeriodeA());
        champsAnnonce.setNumberOfDays(getNombreJoursService());
        champsAnnonce.setBasicDailyAmount(getTauxJournalierAllocationBase());
        champsAnnonce.setDailyIndemnityGuaranteeAI("1".equals(getGarantieIJ()));
        champsAnnonce.setAllowanceFarm("1".equals(getIsAllocationExploitation()));
        champsAnnonce.setAllowanceCareExpenses(getMontantAllocationFraisGarde());
        champsAnnonce.setTotalAPG(getTotalAPG());
        champsAnnonce.setPaymentMethod(getModePaiement());
        champsAnnonce.setMessageDate(getDateEnvoi());
        champsAnnonce.setBreakRules(getBreakRules());
        champsAnnonce.setBusinessProcessId(getBusinessProcessId());
        champsAnnonce.setAction(getContenuAnnonce());
        champsAnnonce.setEventDate(getEventDate());
        champsAnnonce.setMessageType(getMessageType());
        champsAnnonce.setSubMessageType(getSubMessageType());
        champsAnnonce.setInsurantDomicileCountry(getInsurantDomicileCountry());
        champsAnnonce.setCsEtat(getEtat());
        champsAnnonce.setTimeStamp(getTimeStamp());
        champsAnnonce.setEnvelopeMessageId(getEnvelopeMessageId());
        champsAnnonce.setIdDroit(getIdDroit());
        champsAnnonce.setHasComplementCIAB("1".equals(getHasComplementCIAB()));
        //Paternité
        champsAnnonce.setChildDomicile(getPaysNaissanceEnfant());
        champsAnnonce.setChildCantonBorn(getCantonNaissanceEnfant());
        champsAnnonce.setChildInsurantVn(getNssEnfantOldestDroit());
        champsAnnonce.setNewbornDateOfBirth(getDateNaissanceEnfant());
        champsAnnonce.setNumberOfWorkdays(getNombreJoursOuvrable());
        champsAnnonce.setParternityLeaveType(getTypePaternite());

        return champsAnnonce;
    }

    private String getIdDroit() {
        return idDroit;
    }

    // Ensemble de getter en anglais pour matcher selon les directives RAPG
    /**
     * return the accountingMonth (moisAnneeComptable)
     */
    public String getAccountingMonth() {
        return moisAnneeComptable;
    }

    /**
     * @return the activityBeforeService (genreActivite)
     */
    public String getActivityBeforeService() {
        return genreActivite;
    }

    /**
     * @return the allowanceCareExpenses (montantAllocationFraisGarde)
     */
    public String getAllowanceCareExpenses() {
        return montantAllocationFraisGarde;
    }

    /**
     * @return the allowanceFarm (isAllocationExploitation)
     */
    public Boolean getAllowanceFarm() {
        return "1".equals(isAllocationExploitation);
    }

    /**
     * @return the averageDailyIncome (revenuMoyenDeterminant)
     */
    public String getAverageDailyIncome() {
        return revenuMoyenDeterminant;
    }

    /**
     * @return the basicDailyAmount (tauxJournalier)
     */
    public String getBasicDailyAmount() {
        return tauxJournalier;
    }

    /**
     * @return the businessProcessId formatted
     */
    public String getBusinessProcessIdFormatted() {
        String bpId = getBusinessProcessId();
        String numCaisse = getDeliveryOfficeOfficeIdentifier();
        String numAgence = getDeliveryOfficeBranch();
        return JadeStringUtil.fillWithZeroes(numCaisse, 3) + "." + JadeStringUtil.fillWithZeroes(numAgence, 3) + "."
                + bpId;
    }

    /**
     * @return the dailyIndemnityGuaranteeAI (garantieIJ)
     */
    public Boolean getDailyIndemnityGuaranteeAI() {
        return "1".equals(garantieIJ);
    }

    /**
     * @return the deliveryOfficeBranch (numeroAgence)
     */
    public String getDeliveryOfficeBranch() {
        return numeroAgence;
    }

    /**
     * @return the deliveryOfficeOfficeIdentifier (numeroCaisse)
     */
    public String getDeliveryOfficeOfficeIdentifier() {
        return numeroCaisse;
    }

    /**
     * @return the endOfPeriod (periodeA) (date fin période)
     */
    public String getEndOfPeriod() {
        return periodeA;
    }

    /**
     * @return the insurant (numeroAssure)
     */
    public String getInsurant() {
        return numeroAssure;
    }

    /**
     * @return the insurantDomicileCanton (cantonEtat)
     */
    public String getInsurantDomicileCanton() {
        return cantonEtat;
    }

    /**
     * @return the numberOfChildren (nombreEnfants)
     */
    public String getNumberOfChildren() {
        return nombreEnfants;
    }

    /**
     * @return the numberOfDays (nombreJoursService)
     */
    public String getNumberOfDays() {
        return nombreJoursService;
    }

    /**
     * @return the paymentMethod (modePaiement)
     */
    public String getPaymentMethod() {
        return modePaiement;
    }

    /**
     * @return the referenceNumber (numeroCompte)
     */
    public String getReferenceNumber() {
        return numeroCompte;
    }

    /**
     * @return the serviceType (genre)
     */
    public String getServiceType() {
        return genre;
    }

    /**
     * @return the startOfPeriod (periodeDe) (dateDeDébut)
     */
    public String getStartOfPeriod() {
        return periodeDe;
    }

    /**
     * @return the insurantMaritalStatus (etatCivil)
     */
    public String getInsurantMaritalStatus() {
        return etatCivil;
    }

    /**
     * @return the controlNumber (numeroControle)
     */
    public String getControlNumber() {
        return numeroControle;
    }

    /**
     * return the hasComplementCIAB (possèdeComplementCIAB)
     */
    public String getHasComplementCIAB() {
        return hasComplementCIAB;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;

    }

    public String getPaysNaissanceEnfant() {
        return paysNaissanceEnfant;
}

    public void setPaysNaissanceEnfant(String paysNaissanceEnfant) {
        this.paysNaissanceEnfant = paysNaissanceEnfant;
    }

    public String getNssEnfantOldestDroit() {
        return nssEnfantOldestDroit;
    }

    public void setNssEnfantOldestDroit(String nssEnfantOldestDroit) {
        this.nssEnfantOldestDroit = nssEnfantOldestDroit;
    }

    public String getDateNaissanceEnfant() {
        return dateNaissanceEnfant;
    }

    public void setDateNaissanceEnfant(String dateNaissanceEnfant) {
        this.dateNaissanceEnfant = dateNaissanceEnfant;
    }

    public String getNombreJoursOuvrable() {
        return nombreJoursOuvrable;
    }

    public void setNombreJoursOuvrable(String nombreJoursOuvrable) {
        this.nombreJoursOuvrable = nombreJoursOuvrable;
    }

    public String getTypePaternite() {
        return typePaternite;
    }

    public void setTypePaternite(String typePaternite) {
        this.typePaternite = typePaternite;
    }

    public String getCantonNaissanceEnfant() {
        return cantonNaissanceEnfant;
    }

    public void setCantonNaissanceEnfant(String cantonNaissanceEnfant) {
        this.cantonNaissanceEnfant = cantonNaissanceEnfant;
    }
}
