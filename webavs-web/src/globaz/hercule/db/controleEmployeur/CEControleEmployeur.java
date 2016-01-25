package globaz.hercule.db.controleEmployeur;

import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JATime;
import globaz.hercule.db.reviseur.CEReviseur;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.service.CEAffiliationService;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.hercule.service.CETiersService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.annonceAffilie.AFAnnonceAffilie;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import globaz.webavs.common.ICommonConstantes;
import java.io.Serializable;
import java.text.SimpleDateFormat;

public class CEControleEmployeur extends BEntity implements Serializable {
    private static final long serialVersionUID = 5337694943238718466L;
    public static String CS_GENRE_CONTROLE_PERIODIQUE = "811009";
    public static final String FIELD_NUMERO_AFFILIE = "MALNAF";

    public static final String TABLE_CECONTP = "CECONTP";

    private AFAffiliation _affiliation = null;
    private TITiers _tiers = null;
    // Foreign Key
    private String affiliationId = new String();
    private String affiliationLaa = new String();
    private String affiliationLpp = new String();
    // Fields
    private String annee = new String();
    private String anneeActive = new String();
    private String brancheEco = new String();
    private String champConseil = new String();
    private String champConstate = new String();
    private String champRemarque = new String();
    private String comptaTenuPar = new String();
    private String controleurNom = "";
    private String controleurVisa = new String();
    private String dateBouclement = new String();
    private String dateDebutControle = new String();
    private String dateEffective = new String();
    private String dateFinControle = new String();
    private String dateImpression = "";
    private String datePrecedente = new String();
    private String datePrevue = new String();
    private String dateProchain = new String();
    private String debitCredit = new String();
    private String docAllocMiliComplet = new String();
    private String docAllocMiliSondage = new String();
    private String docAllocPerteComplet = new String();
    private String docAllocPerteSondage = new String();
    private String docBilanComplet = new String();
    private String docBilanSondage = new String();
    private String docComptaComplet = new String();
    private String docComptaSondage = new String();
    private String docDroitAllocComplet = new String();
    private String docDroitAllocSondage = new String();
    private Boolean eleAutre1 = new Boolean(false);
    private Boolean eleAutre2 = new Boolean(false);
    private Boolean eleCommission = new Boolean(false);
    private Boolean eleDomicile = new Boolean(false);
    private Boolean eleGratification = new Boolean(false);
    private Boolean eleHeure = new Boolean(false);
    private Boolean eleHonoraire = new Boolean(false);
    private Boolean eleIndemnite = new Boolean(false);
    private String eleLibelleAutre1 = new String();
    private String eleLibelleAutre2 = new String();
    private Boolean eleMensuel = new Boolean(false);
    private Boolean eleNature = new Boolean(false);
    private Boolean elePiece = new Boolean(false);
    private Boolean elePourboire = new Boolean(false);
    // private String planAssurance = new String();
    private Boolean erreur = new Boolean(false);
    private Boolean flagDernierRapport = new Boolean(false);
    private String formeJuri = new String();
    private String genreControle = new String();
    // DB
    // Primary Key
    private String idControleEmployeur = new String();
    private String idReviseur = new String();
    private String idTiers = new String();
    private String increment = "";
    private String infoTiers = "";
    private Boolean inscriptionRC = new Boolean(false);
    private String motif = new String();
    private String nombreSalariesFixes = new String();
    private String nouveauNumRapport = new String();
    private String numAffilie = new String();
    private String numeroExterne = new String();
    private String personneContact1 = new String();
    private String personneContact2 = new String();
    private String personneContact3 = new String();
    private Boolean rapportAFSepare = new Boolean(false);
    private String rapportNumero = new String();
    private Boolean succursales = new Boolean(false);
    private String tempsJour = new String();
    private String typeReviseur = "";
    private String tierDesignation1 = "";
    private String tierDesignation2 = "";
    private String codeSuva;

    /**
     * Constructeur de AFControleEmployeur.
     */
    public CEControleEmployeur() {
        super();
    }

    /**
     * @see globaz.globall.db.BEntity#_afterAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterAdd(final BTransaction transaction) throws Exception {
        JATime now = new JATime(JACalendar.now());
        AFAnnonceAffilie annonce = new AFAnnonceAffilie();
        annonce.setChampModifier(CodeSystem.CHAMPS_MOD_CREATION_CONTROL);
        annonce.setUtilisateur(getSession().getUserName());
        annonce.setAffiliationId(getAffiliationId());
        annonce.setChampAncienneDonnee(getSession().getLabel("960"));
        annonce.setDateEnregistrement(JACalendar.todayJJsMMsAAAA());
        annonce.setHeureEnregistrement(now.toStr(""));
        annonce.setSession(getSession());
        annonce.add(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(final BTransaction transaction) throws java.lang.Exception {
        if (!isLoadedFromManager()) {
            CEReviseur rev = new CEReviseur();
            rev.setSession(getSession());
            rev.setIdReviseur(getIdReviseur());
            rev.retrieve();
            if ((rev != null) && !rev.isNew()) {
                if (!JadeStringUtil.isEmpty(rev.getVisa())) {
                    setControleurVisa(rev.getVisa());
                }
                if (!JadeStringUtil.isEmpty(rev.getNomReviseur())) {
                    setControleurNom(rev.getNomReviseur());
                }
                if (!JadeStringUtil.isEmpty(rev.getTypeReviseur())) {
                    setTypeReviseur(rev.getTypeReviseur());
                }
            }

            CEAffilieManager affilieManager = new CEAffilieManager();
            affilieManager.setSession(getSession());
            affilieManager.setForIdAffiliation(getAffiliationId());
            affilieManager.find();

            if (affilieManager.size() != 1) {
                throw new HerculeException(CEControleEmployeur.class.getName() + " - "
                        + getSession().getLabel("AFFILIE_CONTROLE_PAS_TROUVE"));
            }

            CEAffilie affilie = (CEAffilie) affilieManager.getFirstEntity();

            if (affilie == null) {
                throw new HerculeException(CEControleEmployeur.class.getName() + " - "
                        + getSession().getLabel("AFFILIE_CONTROLE_PAS_TROUVE"));
            }

            setInfoTiers(affilie.getNom() + '\n' + affilie.getDateDebutAffiliation() + " - "
                    + affilie.getDateFinAffiliation());
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(final BTransaction transaction) throws Exception {
        // incrémente de +1 le numéro
        setIdControleEmployeur(this._incCounter(transaction, "0"));
        setIncrement(incCounter(transaction, "0", returnAnnee()));
        setFlagDernierRapport(new Boolean(true));

        AFAffiliation affilliation = CEAffiliationService.findAffilie(getSession(), getNumAffilie(),
                getDateDebutControle(), getDateFinControle());
        if ((affilliation != null) && !affilliation.isNew()) {
            setAffiliationId(affilliation.getAffiliationId());
            setIdTiers(affilliation.getIdTiers());
        } else {
            _addError(transaction, getSession().getLabel("NUMERO_AFFILIE_INEXISTANT_OU_HORS_PERIODE_AFFILIATION"));
        }

        // Mise a jour du numero de rapport
        majNumeroRapport(transaction, true);

        if (controleIfExisteControleNonEffectuePrevuAvecDateChevauchante(this, transaction)) {
            _addError(transaction, getSession().getLabel("PERIODE_CHEVAUCHANTE"));
        }

        // Mise a jour du tag actif ou inactif
        if (!transaction.hasErrors()) {
            // Mise a inactif des autres controles qui ont un numero de rapport
            // identique
            CEControleEmployeurService.majInactifControle(getSession(), transaction, getIdControleEmployeur(),
                    getNumAffilie(), getNouveauNumRapport());
        } else {
            setNouveauNumRapport("");
        }

        // Permet de cocher la case AF séparées
        if (CEControleEmployeurService.affilieHasCotisationAF(getSession(), transaction, getAffiliationId())) {
            setRapportAFSepare(true);
        }

    }

    @Override
    protected void _beforeDelete(final BTransaction transaction) throws Exception {

        if (CEControleEmployeurService.hasAttributionPts(getSession(), getNumAffilie(), getDateDebutControle(),
                getDateFinControle())) {
            _addError(transaction, getSession().getLabel("SUPP_CONTROLE_HAS_ATTRIBUTION"));
        }

    }

    /**
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(final BTransaction transaction) throws Exception {

        // On met a jour le numero de rapport
        majNumeroRapport(transaction, false);

        // Mise a jour du tag actif ou inactif
        if (!transaction.hasErrors()) {
            // Mise a inactif des autres controles qui ont un numero de rapport
            // identique
            CEControleEmployeurService.majInactifControle(getSession(), transaction, getIdControleEmployeur(),
                    getNumAffilie(), getNouveauNumRapport());
        } else {
            setNouveauNumRapport("");
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return CEControleEmployeur.TABLE_CECONTP;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(final BStatement statement) throws Exception {
        affiliationId = statement.dbReadNumeric("MAIAFF");
        idTiers = statement.dbReadNumeric("HTITIE");
        idControleEmployeur = statement.dbReadNumeric("MDICON");
        numAffilie = statement.dbReadString(CEControleEmployeur.FIELD_NUMERO_AFFILIE);
        datePrevue = statement.dbReadDateAMJ("MDDPRE");
        dateEffective = statement.dbReadDateAMJ("MDDEFF");
        genreControle = statement.dbReadNumeric("MDTGEN");
        rapportNumero = statement.dbReadString("MDLRAP");
        nouveauNumRapport = statement.dbReadString("MDLNRA");
        idReviseur = statement.dbReadNumeric("MDICTL");
        erreur = statement.dbReadBoolean("MDBERR");
        numeroExterne = statement.dbReadString("MDLNAF");
        debitCredit = statement.dbReadNumeric("MDTDCR");
        dateDebutControle = statement.dbReadDateAMJ("MDDCDE");
        dateFinControle = statement.dbReadDateAMJ("MDDCFI");
        nombreSalariesFixes = statement.dbReadNumeric("MDNSAL");
        controleurVisa = statement.dbReadString("MILVIS");
        tempsJour = statement.dbReadNumeric("MDNTJO");
        motif = statement.dbReadNumeric("MDTMOT");
        succursales = statement.dbReadBoolean("MDBSUC");
        inscriptionRC = statement.dbReadBoolean("MDBIRC");
        dateProchain = statement.dbReadDateAMJ("MDDPRO");
        dateBouclement = statement.dbReadDateAMJ("MDDBOU");
        personneContact1 = statement.dbReadString("MDNPC1");
        personneContact2 = statement.dbReadString("MDNPC2");
        personneContact3 = statement.dbReadString("MDNPC3");
        comptaTenuPar = statement.dbReadString("MDNCOM");
        docComptaComplet = statement.dbReadString("MDDCCO");
        docComptaSondage = statement.dbReadString("MDDCSO");
        docBilanComplet = statement.dbReadString("MDDBCO");
        docBilanSondage = statement.dbReadString("MDDBSO");
        docAllocPerteComplet = statement.dbReadString("MDDAGC");
        docAllocPerteSondage = statement.dbReadString("MDDAGS");
        docAllocMiliComplet = statement.dbReadString("MDDAMC");
        docAllocMiliSondage = statement.dbReadString("MDDAMS");
        docDroitAllocComplet = statement.dbReadString("MDDALC");
        docDroitAllocSondage = statement.dbReadString("MDDALS");
        rapportAFSepare = statement.dbReadBoolean("MDBRAF");
        elePourboire = statement.dbReadBoolean("MDBEPO");
        eleNature = statement.dbReadBoolean("MDBENA");
        eleHonoraire = statement.dbReadBoolean("MDBEHO");
        eleIndemnite = statement.dbReadBoolean("MDBEIN");
        eleMensuel = statement.dbReadBoolean("MDBEME");
        eleHeure = statement.dbReadBoolean("MDBEHE");
        elePiece = statement.dbReadBoolean("MDBEPI");
        eleDomicile = statement.dbReadBoolean("MDBEDO");
        eleCommission = statement.dbReadBoolean("MDBECO");
        eleGratification = statement.dbReadBoolean("MDBEGR");
        eleLibelleAutre1 = statement.dbReadString("MDLEA1");
        eleAutre1 = statement.dbReadBoolean("MDBEA1");
        eleLibelleAutre2 = statement.dbReadString("MDLEA2");
        eleAutre2 = statement.dbReadBoolean("MDBEA2");
        champConstate = statement.dbReadString("MDLCON");
        champConseil = statement.dbReadString("MDLREC");
        champRemarque = statement.dbReadString("MDLREM");
        flagDernierRapport = statement.dbReadBoolean("MDBFDR");
        controleurNom = statement.dbReadString("MILNOM");
        dateImpression = statement.dbReadDateAMJ("CEDIMP");
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(final BStatement statement) throws Exception {

        boolean validationOK = true;

        // Contrôle que les champs obligatoire soit renseigné
        validationOK &= _propertyMandatory(statement.getTransaction(), getDatePrevue(), getSession().getLabel("450"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getGenreControle(), getSession().getLabel("460"));

        if (validationOK) {
            validationOK &= _checkDate(statement.getTransaction(), getDatePrevue(), getSession().getLabel("1460"));
            validationOK &= _checkDate(statement.getTransaction(), getDateDebutControle(), getSession()
                    .getLabel("1470"));
            validationOK &= _checkDate(statement.getTransaction(), getDateFinControle(), getSession().getLabel("1480"));

            if (!BSessionUtil.compareDateFirstLower(getSession(), getDateDebutControle(), getDateFinControle())) {
                _addError(statement.getTransaction(), getSession().getLabel("550"));
                validationOK = false;
            }
        }

        try {
            if (validationOK) {
                // Contrôle des dates
                String dateLimiteInf = "01.01.1900";
                String dateInitiale = JACalendar.todayJJsMMsAAAA();
                String dateLimitSup = getSession().getApplication().getCalendar().addYears(dateInitiale, 10);

                // Date Prevue > 1.1.1948
                if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), datePrevue, dateLimiteInf)) {
                    // Date Prevue < Date du Jour + 6 ans
                    if (!BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), dateLimitSup, datePrevue)) {
                        _addError(statement.getTransaction(), getSession().getLabel("470"));
                        validationOK = false;
                    }
                } else {
                    _addError(statement.getTransaction(), getSession().getLabel("480"));
                    validationOK = false;
                }

                if (JadeStringUtil.isIntegerEmpty(getAffiliation().getDateFin())) {
                    // Si il n y a pas de date de Fin
                    // Date Debut Control >= Date debut Affiliation
                    if (!BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getDateDebutControle(),
                            getAffiliation().getDateDebut())) {

                        _addError(statement.getTransaction(), FWMessageFormat.format(getSession().getLabel("1940"),
                                getDateDebutControle(), getAffiliation().getDateDebut()));
                        validationOK = false;
                    }
                } else {
                    // Si il y a une date de Fin
                    // Date Debut Control >= Date Debut Affiliation
                    // Date Fin Control =< Date Fin Affiliation
                    if (!(BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getDateDebutControle(),
                            getAffiliation().getDateDebut()) && BSessionUtil.compareDateFirstLowerOrEqual(getSession(),
                            getDateFinControle(), getAffiliation().getDateFin()))) {

                        _addError(statement.getTransaction(), FWMessageFormat.format(getSession().getLabel("1950"),
                                getDateDebutControle(), getDateFinControle(), getAffiliation().getDateDebut(),
                                getAffiliation().getDateFin()));

                        validationOK = false;
                    }
                }

                if (validationOK) {

                    if (!JadeStringUtil.isEmpty(getDateEffective())) {
                        validationOK &= _checkDate(statement.getTransaction(), getDateEffective(), getSession()
                                .getLabel("1490"));

                        // Date de Fin du Contrôle <= Date effective
                        if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateFinControle(),
                                getDateEffective())) {
                            _addError(statement.getTransaction(), getSession().getLabel("1960"));
                            validationOK = false;
                        }
                        // Date effective <= Date du jour
                        if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateEffective(), dateInitiale)) {
                            _addError(statement.getTransaction(), getSession().getLabel("1970"));
                            validationOK = false;
                        }
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            validationOK = false;
        }

        if (!validationOK && (_getAction() == BEntity.ACTION_ADD)) {
            setNouveauNumRapport("");
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(final BStatement statement) throws Exception {
        statement.writeKey("MDICON", this._dbWriteNumeric(statement.getTransaction(), getIdControleEmployeur(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(final BStatement statement) throws Exception {
        statement.writeField("MAIAFF",
                this._dbWriteNumeric(statement.getTransaction(), getAffiliationId(), "affiliationId"));
        statement.writeField("HTITIE", this._dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers"));
        statement.writeField("MDICON",
                this._dbWriteNumeric(statement.getTransaction(), getIdControleEmployeur(), "controleEmployeurId"));
        statement.writeField(CEControleEmployeur.FIELD_NUMERO_AFFILIE,
                this._dbWriteString(statement.getTransaction(), getNumAffilie(), "numAffilie"));
        statement.writeField("MDDPRE", this._dbWriteDateAMJ(statement.getTransaction(), getDatePrevue(), "datePrevue"));
        statement.writeField("MDDCDE",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateDebutControle(), "dateDebutControle"));
        statement.writeField("MDDCFI",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateFinControle(), "dateFinControle"));
        statement.writeField("MDDEFF",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateEffective(), "dateEffective"));
        statement.writeField("MDTGEN",
                this._dbWriteNumeric(statement.getTransaction(), getGenreControle(), "genreControle"));
        statement.writeField("MDLRAP",
                this._dbWriteString(statement.getTransaction(), getRapportNumero(), "rapportNumero"));
        statement.writeField("MDLNRA",
                this._dbWriteString(statement.getTransaction(), getNouveauNumRapport(), "nouveauNumRapport"));
        statement.writeField("MDICTL", this._dbWriteNumeric(statement.getTransaction(), getIdReviseur(), "controleur"));
        statement
                .writeField("MDBERR", this._dbWriteBoolean(statement.getTransaction(), isErreur(),
                        BConstants.DB_TYPE_BOOLEAN_CHAR, "erreur"));
        statement.writeField("MDLNAF",
                this._dbWriteString(statement.getTransaction(), getNumeroExterne(), "numeroExterne"));
        statement.writeField("MDTDCR",
                this._dbWriteNumeric(statement.getTransaction(), getDebitCredit(), "debitCredit"));
        statement.writeField("MDNSAL",
                this._dbWriteNumeric(statement.getTransaction(), getNombreSalariesFixes(), "nombreSalariesFixes"));
        statement.writeField("MDNTJO", this._dbWriteNumeric(statement.getTransaction(), getTempsJour(), "tempsJour"));
        statement.writeField("MDTMOT", this._dbWriteNumeric(statement.getTransaction(), getMotif(), "motif"));
        statement.writeField("MDBSUC", this._dbWriteBoolean(statement.getTransaction(), getSuccursales(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "succursales"));
        statement.writeField("MDBIRC", this._dbWriteBoolean(statement.getTransaction(), getInscriptionRC(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "inscriptionRC"));
        statement.writeField("MDDPRO",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateProchain(), "dateProchain"));
        statement.writeField("MDDBOU",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateBouclement(), "dateBouclement"));
        statement.writeField("MDNPC1",
                this._dbWriteString(statement.getTransaction(), getPersonneContact1(), "personneContact1"));
        statement.writeField("MDNPC2",
                this._dbWriteString(statement.getTransaction(), getPersonneContact2(), "personneContact2"));
        statement.writeField("MDNPC3",
                this._dbWriteString(statement.getTransaction(), getPersonneContact3(), "personneContact3"));
        statement.writeField("MDNCOM",
                this._dbWriteString(statement.getTransaction(), getComptaTenuPar(), "comptaTenuPar"));
        statement.writeField("MDDCCO",
                this._dbWriteString(statement.getTransaction(), getDocComptaComplet(), "docComptaComplet"));
        statement.writeField("MDDCSO",
                this._dbWriteString(statement.getTransaction(), getDocComptaSondage(), "docComptaSondage"));
        statement.writeField("MDDBCO",
                this._dbWriteString(statement.getTransaction(), getDocBilanComplet(), "docBilanComplet"));
        statement.writeField("MDDBSO",
                this._dbWriteString(statement.getTransaction(), getDocBilanSondage(), "docBilanSondage"));
        statement.writeField("MDDAGC",
                this._dbWriteString(statement.getTransaction(), getDocAllocPerteComplet(), "docAllocPerteComplet"));
        statement.writeField("MDDAGS",
                this._dbWriteString(statement.getTransaction(), getDocAllocPerteSondage(), "docAllocPerteSondage"));
        statement.writeField("MDDAMC",
                this._dbWriteString(statement.getTransaction(), getDocAllocMiliComplet(), "docAllocMiliComplet"));
        statement.writeField("MDDAMS",
                this._dbWriteString(statement.getTransaction(), getDocAllocMiliSondage(), "docAllocMiliSondage"));
        statement.writeField("MDDALC",
                this._dbWriteString(statement.getTransaction(), getDocDroitAllocComplet(), "docDroitAllocComplet"));
        statement.writeField("MDDALS",
                this._dbWriteString(statement.getTransaction(), getDocDroitAllocSondage(), "docDroitAllocSondage"));
        statement.writeField("MDBRAF", this._dbWriteBoolean(statement.getTransaction(), getRapportAFSepare(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "rapportAFSepare"));
        statement.writeField("MDBEPO", this._dbWriteBoolean(statement.getTransaction(), getElePourboire(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "elePourboire"));
        statement.writeField("MDBENA", this._dbWriteBoolean(statement.getTransaction(), getEleNature(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "eleNature"));
        statement.writeField("MDBEHO", this._dbWriteBoolean(statement.getTransaction(), getEleHonoraire(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "eleHonoraire"));
        statement.writeField("MDBEIN", this._dbWriteBoolean(statement.getTransaction(), getEleIndemnite(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "eleIndemnite"));
        statement.writeField("MDBEME", this._dbWriteBoolean(statement.getTransaction(), getEleMensuel(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "eleMensuel"));
        statement.writeField("MDBEHE", this._dbWriteBoolean(statement.getTransaction(), getEleHeure(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "eleHeure"));
        statement.writeField("MDBEPI", this._dbWriteBoolean(statement.getTransaction(), getElePiece(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "elePiece"));
        statement.writeField("MDBEDO", this._dbWriteBoolean(statement.getTransaction(), getEleDomicile(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "eleDomicile"));
        statement.writeField("MDBECO", this._dbWriteBoolean(statement.getTransaction(), getEleCommission(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "eleCommission"));
        statement.writeField("MDBEGR", this._dbWriteBoolean(statement.getTransaction(), getEleGratification(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "eleGratification"));
        statement.writeField("MDLEA1",
                this._dbWriteString(statement.getTransaction(), getEleLibelleAutre1(), "eleLibelleAutre1"));
        statement.writeField("MDBEA1", this._dbWriteBoolean(statement.getTransaction(), getEleAutre1(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "eleAutre1"));
        statement.writeField("MDLEA2",
                this._dbWriteString(statement.getTransaction(), getEleLibelleAutre2(), "eleLibelleAutre2"));
        statement.writeField("MDBEA2", this._dbWriteBoolean(statement.getTransaction(), getEleAutre2(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "eleAutre2"));
        statement.writeField("MDLCON",
                this._dbWriteString(statement.getTransaction(), getChampConstate(), "champConstate"));
        statement.writeField("MDLREC",
                this._dbWriteString(statement.getTransaction(), getChampConseil(), "champConseil"));
        statement.writeField("MDLREM",
                this._dbWriteString(statement.getTransaction(), getChampRemarque(), "champRemarque"));
        statement.writeField("MDBFDR", this._dbWriteBoolean(statement.getTransaction(), getFlagDernierRapport(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "flagDernierRapport"));
        statement.writeField("CEDIMP",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateImpression(), "dateImpression"));
    }

    private boolean controleIfExisteControleNonEffectuePrevuAvecDateChevauchante(final CEControleEmployeur newControle,
            final BTransaction transaction) {

        CEControleEmployeurManager manager = new CEControleEmployeurManager();
        manager.setSession(getSession());
        manager.setForNumAffilie(newControle.getNumAffilie());
        manager.setForNotDateEffective(true);
        manager.setOrderBy(" MDDCFI DESC ");
        manager.setForActif(true);
        try {
            manager.find(BManager.SIZE_NOLIMIT);
            for (int i = 0; i < manager.size(); i++) {
                CEControleEmployeur controle = (CEControleEmployeur) manager.getEntity(i);

                if (!(controle.getDateDebutControle().equals(newControle.getDateDebutControle()) && controle
                        .getDateFinControle().equals(newControle.getDateFinControle()))) {

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

                    if (((dateFormat.parse(newControle.getDateFinControle())).getTime() > (dateFormat.parse(controle
                            .getDateDebutControle())).getTime())
                            && ((dateFormat.parse(newControle.getDateDebutControle())).getTime() < (dateFormat
                                    .parse(controle.getDateFinControle())).getTime())) {
                        // période chevauchante
                        return true;
                    }
                } else {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    /*
     * Retourne l'adresse de domicile du tiers Ex: Soit le n° avs, le n° affilié ou le n° de contribuable
     */
    public String getAdresseCourrier(final BTransaction trans, final String date) throws Exception {
        String courrier = "";
        String domaineCourrier = "";
        courrier = IConstantes.CS_AVOIR_ADRESSE_COURRIER;
        domaineCourrier = ICommonConstantes.CS_APPLICATION_COTISATION;

        // Récupérer le tiers
        TITiers tiers = null;
        try {
            tiers = getTiers();
        } catch (HerculeException e) {
            JadeLogger.warn(this, e);
        }

        if (tiers != null) {
            return tiers.getAdresseAsString(courrier, domaineCourrier, date, getNumAffilie());
        }

        return "";
    }

    /*
     * Retourne l'adresse de domicile du tiers Ex: Soit le n° avs, le n° affilié ou le n° de contribuable
     */
    public String getAdresseDomicile(final BTransaction trans, final String date) throws Exception {
        // Récupérer le tiers
        TITiers tiers = null;

        try {
            tiers = getTiers();
        } catch (HerculeException e) {
            JadeLogger.warn(this, e);
        }

        if (tiers != null) {
            return tiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, IConstantes.CS_APPLICATION_DEFAUT,
                    date, getNumAffilie());
        }

        return "";
    }

    /*
     * Retourne l'adresse de domicile du tiers Ex: Soit le n° avs, le n° affilié ou le n° de contribuable
     */
    public String getAdressePrincipale(final BTransaction trans, final String date) throws Exception {
        // l'adresse de paiement est l'adresse de courrier
        String result = getAdresseCourrier(trans, date);
        if (!JadeStringUtil.isBlank(result)) {
            return result;
        } else {
            return getAdresseDomicile(trans, date);
        }
    }

    /**
     * Rechercher l'affiliation du Control Employeur en fonction de son ID.
     * 
     * @return l'affiliation
     */
    public AFAffiliation getAffiliation() throws Exception {

        if (_affiliation == null) {
            if (!JadeStringUtil.isEmpty(getAffiliationId())) {
                _affiliation = CEAffiliationService.retrieveAffilie(getSession(), getAffiliationId());
            }
        }

        return _affiliation;
    }

    public String getAffiliationId() {
        return affiliationId;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getAffiliationLaa() throws Exception {
        AFSuiviCaisseAffiliationManager manager = new AFSuiviCaisseAffiliationManager();
        manager.setSession(getSession());
        manager.setForAffiliationId(getAffiliationId());
        manager.setForAnneeActive(getAnneeActive());
        manager.setForGenreCaisse(CodeSystem.GENRE_CAISSE_LAA);
        try {
            manager.find();
        } catch (Exception e) {
            throw new HerculeException(
                    "Technical Exception, Unabled to find AFSuiviCaisseAffiliation (ForAffiliationId="
                            + getAffiliationId() + ",ForAnneeActive=" + getAnneeActive() + ",ForGenreCaisse="
                            + CodeSystem.GENRE_CAISSE_LAA + ")", e);
        }
        if (manager.size() > 0) {
            if (((AFSuiviCaisseAffiliation) manager.getFirstEntity()).getAdministration() != null) {
                affiliationLaa = ((AFSuiviCaisseAffiliation) manager.getFirstEntity()).getAdministration()
                        .getDesignation1();
            }
            if (JadeStringUtil.isEmpty(affiliationLaa)) {
                affiliationLaa = CodeSystem.getLibelle(getSession(),
                        ((AFSuiviCaisseAffiliation) manager.getFirstEntity()).getMotif());
            }
        }
        return affiliationLaa;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getAffiliationLpp() throws Exception {
        AFSuiviCaisseAffiliationManager manager = new AFSuiviCaisseAffiliationManager();
        manager.setSession(getSession());
        manager.setForAffiliationId(getAffiliationId());
        manager.setForAnneeActive(getAnneeActive());
        manager.setForGenreCaisse(CodeSystem.GENRE_CAISSE_LPP);
        try {
            manager.find();
        } catch (Exception e) {
            throw new HerculeException(
                    "Technical Exception, Unabled to find AFSuiviCaisseAffiliation (ForAffiliationId="
                            + getAffiliationId() + ",ForAnneeActive=" + getAnneeActive() + ",ForGenreCaisse="
                            + CodeSystem.GENRE_CAISSE_LPP + ")", e);
        }
        if (manager.size() > 0) {
            if (((AFSuiviCaisseAffiliation) manager.getFirstEntity()).getAdministration() != null) {
                affiliationLpp = ((AFSuiviCaisseAffiliation) manager.getFirstEntity()).getAdministration()
                        .getDesignation1();
            }
            if (JadeStringUtil.isEmpty(affiliationLpp)) {
                affiliationLpp = CodeSystem.getLibelle(getSession(),
                        ((AFSuiviCaisseAffiliation) manager.getFirstEntity()).getMotif());
            }
        }
        return affiliationLpp;
    }

    public String getAnneeActive() {
        return anneeActive;
    }

    /**
     * @return
     */
    public String getBrancheEco() {
        try {
            if (getAffiliation() != null) {
                setBrancheEco(getAffiliation().getBrancheEconomique());
            }
        } catch (Exception e) {
            JadeLogger.warn(this, e);
        }

        return brancheEco;
    }

    public String getChampConseil() {
        return champConseil;
    }

    public String getChampConstate() {
        return champConstate;
    }

    public String getChampRemarque() {
        return champRemarque;
    }

    public String getComptaTenuPar() {
        return comptaTenuPar;
    }

    public String getControleurNom() {
        return controleurNom;
    }

    public String getControleurVisa() {
        return controleurVisa;
    }

    public String getDateBouclement() {
        return dateBouclement;
    }

    public String getDateDebutControle() {
        return dateDebutControle;
    }

    public String getDateEffective() {
        return dateEffective;
    }

    public String getDateFinControle() {
        return dateFinControle;
    }

    public String getDateImpression() {
        return dateImpression;
    }

    /**
     * @return
     */
    public String getDatePrecedentControle() {
        if (!JadeStringUtil.isEmpty(getNumAffilie())) {
            CEControleEmployeurManager contManager = new CEControleEmployeurManager();
            contManager.setSession(getSession());
            contManager.setForNumAffilie(getNumAffilie());
            // actif
            contManager.setForActif(true);
            contManager.setOrderBy("MDDEFF DESC");
            try {
                contManager.find();

                if (contManager.size() < 2) {
                    return "";
                }

                for (int i = 0; i < contManager.size(); i++) {
                    CEControleEmployeur emp = (CEControleEmployeur) contManager.getEntity(i);
                    if (!emp.getIdControleEmployeur().equals(getIdControleEmployeur())) {

                        JADate date = new JADate(getDateEffective());
                        JADate dateControlePre = new JADate(emp.getDateEffective());

                        if (!JadeStringUtil.isEmpty(getDatePrecedente())) {
                            JADate datePrecedente = new JADate(getDatePrecedente());
                            if ((date.toLong() > dateControlePre.toLong())
                                    && (dateControlePre.toLong() > datePrecedente.toLong())) {
                                setDatePrecedente(emp.getDateEffective());
                            }
                        } else {
                            if (date.toLong() > dateControlePre.toLong()) {
                                setDatePrecedente(emp.getDateEffective());
                            }
                        }
                    }
                }

            } catch (Exception e) {
                JadeLogger.warn(this, e);
                setDatePrecedente("");
            }
        }

        return getDatePrecedente();
    }

    public String getDatePrecedente() {
        return datePrecedente;
    }

    public String getDatePrevue() {
        return datePrevue;
    }

    public String getDateProchain() {
        return dateProchain;
    }

    public String getDebitCredit() {
        return debitCredit;
    }

    public String getDocAllocMiliComplet() {
        return docAllocMiliComplet;
    }

    public String getDocAllocMiliSondage() {
        return docAllocMiliSondage;
    }

    public String getDocAllocPerteComplet() {
        return docAllocPerteComplet;
    }

    public String getDocAllocPerteSondage() {
        return docAllocPerteSondage;
    }

    public String getDocBilanComplet() {
        return docBilanComplet;
    }

    public String getDocBilanSondage() {
        return docBilanSondage;
    }

    public String getDocComptaComplet() {
        return docComptaComplet;
    }

    public String getDocComptaSondage() {
        return docComptaSondage;
    }

    public String getDocDroitAllocComplet() {
        return docDroitAllocComplet;
    }

    public String getDocDroitAllocSondage() {
        return docDroitAllocSondage;
    }

    public Boolean getEleAutre1() {
        return eleAutre1;
    }

    public Boolean getEleAutre2() {
        return eleAutre2;
    }

    public Boolean getEleCommission() {
        return eleCommission;
    }

    public Boolean getEleDomicile() {
        return eleDomicile;
    }

    public Boolean getEleGratification() {
        return eleGratification;
    }

    public Boolean getEleHeure() {
        return eleHeure;
    }

    public Boolean getEleHonoraire() {
        return eleHonoraire;
    }

    public Boolean getEleIndemnite() {
        return eleIndemnite;
    }

    public String getEleLibelleAutre1() {
        return eleLibelleAutre1;
    }

    public String getEleLibelleAutre2() {
        return eleLibelleAutre2;
    }

    public Boolean getEleMensuel() {
        return eleMensuel;
    }

    public Boolean getEleNature() {
        return eleNature;
    }

    public Boolean getElePiece() {
        return elePiece;
    }

    public Boolean getElePourboire() {
        return elePourboire;
    }

    public Boolean getFlagDernierRapport() {
        return flagDernierRapport;
    }

    /**
     * @return
     */
    public String getFormeJuri() {
        try {
            if (getAffiliation() != null) {
                setFormeJuri(getAffiliation().getPersonnaliteJuridique());
            }
        } catch (Exception e) {
            JadeLogger.warn(this, e);
        }
        return formeJuri;
    }

    /**
     * Retourne le code SUVA de l'affiliation
     * 
     * @return String représentant le code SUVA
     */
    public String getCodeSuva() {
        try {
            if (getAffiliation() != null) {
                setCodeSuva(getAffiliation().getCodeSUVA());
            }
        } catch (Exception e) {
            JadeLogger.warn(this, e);
        }
        return codeSuva;
    }

    public String getGenreControle() {
        return genreControle;
    }

    public String getIdControleEmployeur() {
        return idControleEmployeur;
    }

    public String getIdReviseur() {
        return idReviseur;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIncrement() {
        return increment;
    }

    public String getInfoTiers() {
        return infoTiers;
    }

    public String getInscriLibelle() throws Exception {
        String reponse = "";
        if (inscriptionRC.booleanValue()) {
            reponse = (getSession().getApplication().getLabel("LIBELLE_OUI", getLangueTiers()));
        } else {
            reponse = (getSession().getApplication().getLabel("LIBELLE_NON", getLangueTiers()));
        }
        return reponse;
    }

    public Boolean getInscriptionRC() {
        return inscriptionRC;
    }

    public Boolean getInscriptionRCParticularite() throws Exception {
        if (AFParticulariteAffiliation.existeParticulariteDateDonnee(getSession(), getAffiliationId(),
                CodeSystem.PARTIC_AFFILIE_INSCRIT_RC, JACalendar.todayJJsMMsAAAA())) {
            setInscriptionRC(new Boolean(true));
            return new Boolean(true);
        } else {
            return getInscriptionRC();
        }
    }

    public String getLangueTiers() {
        TITiers tiers = null;

        try {
            tiers = getTiers();
        } catch (Exception e) {
            JadeLogger.warn(this, e);
        }

        if (tiers != null) {
            return AFUtil.toLangueIso(tiers.getLangue());
        }

        return "fr";
    }

    public String getMotif() {
        return motif;
    }

    public String getNombreSalariesFixes() {
        return nombreSalariesFixes;
    }

    public String getNomTiers() {
        // Récupérer le tiers
        TITiers tiers = null;

        try {
            tiers = getTiers();
        } catch (Exception e) {
            JadeLogger.warn(this, e);
        }

        if (tiers != null) {
            tierDesignation1 = tiers.getDesignation1();
            tierDesignation2 = tiers.getDesignation2();
        }
        return getTierDesignation1() + " " + getTierDesignation2();
    }

    public String getNouveauNumRapport() {
        return nouveauNumRapport;
    }

    /**
     * @return
     */
    public String getNumAffilie() {
        if (numAffilie.equals("") || numAffilie.equals(null)) {
            try {
                if (getAffiliation() != null) {
                    setNumAffilie(getAffiliation().getAffilieNumero());
                }
            } catch (Exception e) {
                JadeLogger.warn(this, e);
            }
        }
        return numAffilie;
    }

    public String getNumeroExterne() {
        return numeroExterne;
    }

    public String getPersonneContact1() {
        return personneContact1;
    }

    public String getPersonneContact2() {
        return personneContact2;
    }

    public String getPersonneContact3() {
        return personneContact3;
    }

    public Boolean getRapportAFSepare() {
        return rapportAFSepare;
    }

    public String getRapportNumero() {
        return rapportNumero;
    }

    public String getSuccLibelle() throws Exception {
        String reponse = "";
        // succursales = new
        // Boolean(AFAffiliationUtil.hasSuccursale(getAffiliation(),
        // getDatePrevue()));
        if (succursales.booleanValue()) {
            reponse = (getSession().getApplication().getLabel("LIBELLE_OUI", getLangueTiers()));
        } else {
            reponse = (getSession().getApplication().getLabel("LIBELLE_NON", getLangueTiers()));
        }
        return reponse;
    }

    /**
     * @return
     */
    public Boolean getSuccursales() {
        try {
            succursales = new Boolean(AFAffiliationUtil.hasSuccursale(getAffiliation(), getDatePrevue()));
        } catch (Exception e) {
            JadeLogger.warn(this, e);
        }
        return succursales;
    }

    public String getTempsJour() {
        return tempsJour;
    }

    public String getTierDesignation1() {
        return tierDesignation1;
    }

    public String getTierDesignation2() {
        return tierDesignation2;
    }

    /**
     * Rechercher le tiers pour l'affiliation en fonction de son ID.
     * 
     * @return le tiers
     */
    public TITiers getTiers() throws Exception {

        // Si pas d'identifiant => pas d'objet
        if (_tiers == null) {
            if (_affiliation == null) {
                try {
                    getAffiliation();
                } catch (Exception e) {
                    throw e;
                }
                if (_affiliation == null) {
                    return null;
                }
            }

            _tiers = CETiersService.retrieveTiers(getSession(), _affiliation.getIdTiers());
        }

        return _tiers;
    }

    public String incCounter(final BTransaction transaction, final String increment, final String annee)
            throws Exception {
        String numeroIncrement = this._incCounter(transaction, increment, "NUMCONTEMP", "0", annee);
        if (numeroIncrement.length() == 1) {
            numeroIncrement = "0000" + numeroIncrement;
        } else if (numeroIncrement.length() == 2) {
            numeroIncrement = "000" + numeroIncrement;
        } else if (numeroIncrement.length() == 3) {
            numeroIncrement = "00" + numeroIncrement;
        } else if (numeroIncrement.length() == 4) {
            numeroIncrement = "0" + numeroIncrement;
        }
        return numeroIncrement;
    }

    public Boolean isErreur() {
        return erreur;
    }

    /**
     * @return
     */
    public boolean isInscrit() {
        try {
            return AFParticulariteAffiliation.existeParticulariteDateDonnee(getSession(), getAffiliationId(),
                    CodeSystem.PARTIC_AFFILIE_INSCRIT_RC, JACalendar.todayJJsMMsAAAA());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Permet la mise a jour du numero de rapport. return false si le numero existe pas
     * 
     * @param transaction
     */
    public boolean majNumeroRapport(final BTransaction transaction, final boolean isModeAdd) throws Exception {
        boolean retour = true;

        // Si numéro de rapport vide, on en crée un nouveau
        if (JadeStringUtil.isEmpty(getNouveauNumRapport())) {
            setNouveauNumRapport(returnAnnee() + "." + incCounter(transaction, "0", returnAnnee()) + ".1");
            setFlagDernierRapport(new Boolean(true));

            // Si numéro de rapport non vide mais non correct, on crée une
            // erreur
        } else if (getNouveauNumRapport().length() < 12) {
            _addError(transaction, getSession().getLabel("ERREUR_NBR_POSITION"));

            // Si numero non vide, on controle qu'il existe ou qu'il peut
            // exister
        } else {
            String racine = getNouveauNumRapport().substring(0, 10);

            CEControleEmployeurManager contMana = new CEControleEmployeurManager();
            contMana.setSession(getSession());
            contMana.setForNotId(getIdControleEmployeur());
            contMana.setForNumAffilie(getNumAffilie());
            contMana.setLikeNouveauNumRapport(racine);
            contMana.setOrderBy("MDLNRA DESC");
            contMana.find();

            // Si la base du numéro existe, on controle que le numéro en entier
            // existe (mise a jour d'un controle existant)
            // ou que
            if (contMana.size() > 0) {
                int incrementDoublon = JadeStringUtil.toInt(getNouveauNumRapport().substring(11, 12));
                boolean pasDeNumRapportPrecedent = true;
                for (int i = 0; i < contMana.size(); i++) {
                    CEControleEmployeur controle = (CEControleEmployeur) contMana.getEntity(i);
                    if (controle.getNouveauNumRapport().equals(racine + "." + "" + (incrementDoublon - 1))) {
                        pasDeNumRapportPrecedent = false;
                    }
                    if (controle.getNouveauNumRapport().equals(getNouveauNumRapport())) {
                        _addError(transaction, getSession().getLabel("ERREUR_CONTEMPL_NUM_RAPPORT_EXISTANT"));
                    }
                }

                if (pasDeNumRapportPrecedent) {
                    _addError(transaction, getSession().getLabel("ERREUR_NUM_PRECEDENT") + " " + racine + "." + ""
                            + (incrementDoublon - 1));
                }

            } else if (isModeAdd) {
                _addError(transaction, getSession().getLabel("ERREUR_CONTEMPL_NUM_RAPPORT"));
            }
        }

        return retour;
    }

    public String returnAnnee() {
        try {
            annee = getDatePrevue().substring(6);
            return annee;
        } catch (Exception e) {
            return null;
        }

    }

    public void setAffiliationId(final String newAffiliationId) {
        affiliationId = newAffiliationId;
    }

    public void setAnnee(final String string) {
        annee = string;
    }

    public void setAnneeActive(final String string) {
        anneeActive = string;
    }

    public void setBrancheEco(final String string) {
        brancheEco = string;
    }

    public void setChampConseil(final String string) {
        champConseil = string;
    }

    public void setChampConstate(final String string) {
        champConstate = string;
    }

    public void setChampRemarque(final String string) {
        champRemarque = string;
    }

    public void setComptaTenuPar(final String string) {
        comptaTenuPar = string;
    }

    public void setControleurNom(final String controleurNom) {
        this.controleurNom = controleurNom;
    }

    public void setControleurVisa(final String controleurVisa) {
        this.controleurVisa = controleurVisa;
    }

    public void setDateBouclement(final String string) {
        dateBouclement = string;
    }

    public void setDateDebutControle(final String newDateDebutControle) {
        dateDebutControle = newDateDebutControle;
    }

    public void setDateEffective(final String newDateEffective) {
        dateEffective = newDateEffective;
    }

    public void setDateFinControle(final String newDateFinControle) {
        dateFinControle = newDateFinControle;
    }

    public void setDateImpression(final String dateImpression) {
        this.dateImpression = dateImpression;
    }

    public void setDatePrecedente(final String string) {
        datePrecedente = string;
    }

    public void setDatePrevue(final String newDatePrevue) {
        datePrevue = newDatePrevue;
    }

    public void setDateProchain(final String string) {
        dateProchain = string;
    }

    public void setDebitCredit(final String newDebitCredit) {
        debitCredit = newDebitCredit;
    }

    public void setDocAllocMiliComplet(final String string) {
        docAllocMiliComplet = string;
    }

    public void setDocAllocMiliSondage(final String string) {
        docAllocMiliSondage = string;
    }

    public void setDocAllocPerteComplet(final String string) {
        docAllocPerteComplet = string;
    }

    public void setDocAllocPerteSondage(final String string) {
        docAllocPerteSondage = string;
    }

    public void setDocBilanComplet(final String string) {
        docBilanComplet = string;
    }

    public void setDocBilanSondage(final String string) {
        docBilanSondage = string;
    }

    public void setDocComptaComplet(final String string) {
        docComptaComplet = string;
    }

    public void setDocComptaSondage(final String string) {
        docComptaSondage = string;
    }

    public void setDocDroitAllocComplet(final String string) {
        docDroitAllocComplet = string;
    }

    public void setDocDroitAllocSondage(final String string) {
        docDroitAllocSondage = string;
    }

    public void setEleAutre1(final Boolean boolean1) {
        eleAutre1 = boolean1;
    }

    public void setEleAutre2(final Boolean boolean1) {
        eleAutre2 = boolean1;
    }

    public void setEleCommission(final Boolean boolean1) {
        eleCommission = boolean1;
    }

    public void setEleDomicile(final Boolean boolean1) {
        eleDomicile = boolean1;
    }

    public void setEleGratification(final Boolean boolean1) {
        eleGratification = boolean1;
    }

    public void setEleHeure(final Boolean boolean1) {
        eleHeure = boolean1;
    }

    public void setEleHonoraire(final Boolean boolean1) {
        eleHonoraire = boolean1;
    }

    public void setEleIndemnite(final Boolean boolean1) {
        eleIndemnite = boolean1;
    }

    public void setEleLibelleAutre1(final String string) {
        eleLibelleAutre1 = string;
    }

    public void setEleLibelleAutre2(final String string) {
        eleLibelleAutre2 = string;
    }

    public void setEleMensuel(final Boolean boolean1) {
        eleMensuel = boolean1;
    }

    public void setEleNature(final Boolean boolean1) {
        eleNature = boolean1;
    }

    public void setElePiece(final Boolean boolean1) {
        elePiece = boolean1;
    }

    public void setElePourboire(final Boolean boolean1) {
        elePourboire = boolean1;
    }

    public void setErreur(final Boolean newErreur) {
        erreur = newErreur;
    }

    public void setFlagDernierRapport(final java.lang.Boolean flagDernierRapport) {
        this.flagDernierRapport = flagDernierRapport;
    }

    public void setFormeJuri(final String string) {
        formeJuri = string;
    }

    public void setGenreControle(final String newGenreControle) {
        genreControle = newGenreControle;
    }

    public void setIdControleEmployeur(final String newIdControleEmployeur) {
        idControleEmployeur = newIdControleEmployeur;
    }

    public void setIdReviseur(final String _idReviseur) {
        idReviseur = _idReviseur;
    }

    public void setIdTiers(final String string) {
        idTiers = string;
    }

    public void setIncrement(final String increment) {
        this.increment = increment;
    }

    public void setInfoTiers(final String newInfoTiers) {
        infoTiers = newInfoTiers;
    }

    public void setInscriptionRC(final Boolean boolean1) {
        inscriptionRC = boolean1;
    }

    public void setMotif(final String string) {
        motif = string;
    }

    public void setNombreSalariesFixes(final String newNombreSalariesFixes) {
        nombreSalariesFixes = newNombreSalariesFixes;
    }

    public void setNouveauNumRapport(final String nouveauNumRapport) {
        this.nouveauNumRapport = nouveauNumRapport;
    }

    public void setNumAffilie(final String string) {
        numAffilie = string;
    }

    public void setNumeroExterne(final String newNumeroExterne) {
        numeroExterne = newNumeroExterne;
    }

    public void setPersonneContact1(final String string) {
        personneContact1 = string;
    }

    public void setPersonneContact2(final String string) {
        personneContact2 = string;
    }

    public void setPersonneContact3(final String string) {
        personneContact3 = string;
    }

    public void setRapportAFSepare(final Boolean boolean1) {
        rapportAFSepare = boolean1;
    }

    public void setRapportNumero(final String newRapportNumero) {
        rapportNumero = newRapportNumero;
    }

    public void setSuccursales(final Boolean boolean1) {
        succursales = boolean1;
    }

    public void setTempsJour(final String string) {
        tempsJour = string;
    }

    public void setTierDesignation1(final String string) {
        tierDesignation1 = string;
    }

    public void setTierDesignation2(final String string) {
        tierDesignation2 = string;
    }

    /**
     * Getter de typeReviseur
     * 
     * @return the typeReviseur
     */
    public String getTypeReviseur() {
        return typeReviseur;
    }

    /**
     * Setter de typeReviseur
     * 
     * @param typeReviseur the typeReviseur to set
     */
    public void setTypeReviseur(final String typeReviseur) {
        this.typeReviseur = typeReviseur;
    }

    /**
     * Setter de codeSuva
     * 
     * @param codeSuva the codeSuva to set
     */
    public void setCodeSuva(final String codeSuva) {
        this.codeSuva = codeSuva;
    }

    @Override
    protected void _afterUpdate(final BTransaction transaction) throws Exception {
        super._afterUpdate(transaction);
        try {
            if (getAffiliation() != null) {
                // Si le code suva a changé
                // et si le reviseur est de type SUVA => Modification du code suva
                if (!getAffiliation().getCodeSUVA().equals(codeSuva)
                        && (!JadeStringUtil.isEmpty(typeReviseur) && CEReviseur.TYPE_REV_EXT_SUVA.equals(typeReviseur))) {
                    _affiliation.setCodeSUVA(codeSuva);
                    _affiliation.wantCallValidate(false);
                    _affiliation.wantCallMethodBefore(false);
                    _affiliation.wantCallMethodAfter(false);
                    _affiliation.update(transaction);
                }
            }
        } catch (Exception e) {
            throw new HerculeException("Unabled to update code SUVA (numAffilie = " + numAffilie + " / idControle = "
                    + getIdControleEmployeur() + " / Code suva = " + codeSuva + ")", e);
        }
    }

}
