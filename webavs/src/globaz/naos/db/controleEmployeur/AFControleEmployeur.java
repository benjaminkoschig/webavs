package globaz.naos.db.controleEmployeur;

import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JATime;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.annonceAffilie.AFAnnonceAffilie;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import globaz.webavs.common.ICommonConstantes;
import java.io.Serializable;

public class AFControleEmployeur extends BEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AFAffiliation _affiliation = null;
    private AFReviseur _reviseur = null;
    private TITiers _tiers = null;
    // Foreign Key
    private java.lang.String affiliationId = new String();
    private java.lang.String affiliationLaa = new String();
    private java.lang.String affiliationLpp = new String();
    // Fields
    private java.lang.String annee = new String();
    private java.lang.String anneeActive = new String();
    private java.lang.String brancheEco = new String();
    private java.lang.String champConseil = new String();
    private java.lang.String champConstate = new String();
    private java.lang.String champRemarque = new String();
    private java.lang.String comptaTenuPar = new String();
    // DB
    // Primary Key
    private java.lang.String controleEmployeurId = new String();
    private java.lang.String controleur = new String();
    private java.lang.String controleurNom = new String();
    private java.lang.String controleurVisa = new String();
    private java.lang.String dateBouclement = new String();
    private java.lang.String dateDebutControle = new String();
    private java.lang.String dateEffective = new String();
    private java.lang.String dateFinControle = new String();
    private java.lang.String datePrecedente = new String();
    private java.lang.String datePrevue = new String();
    private java.lang.String dateProchain = new String();
    private java.lang.String debitCredit = new String();
    private java.lang.String docAllocMiliComplet = new String();
    private java.lang.String docAllocMiliSondage = new String();
    private java.lang.String docAllocPerteComplet = new String();
    private java.lang.String docAllocPerteSondage = new String();
    private java.lang.String docBilanComplet = new String();
    private java.lang.String docBilanSondage = new String();
    private java.lang.String docComptaComplet = new String();
    private java.lang.String docComptaSondage = new String();
    private java.lang.String docDroitAllocComplet = new String();
    private java.lang.String docDroitAllocSondage = new String();
    private java.lang.Boolean eleAutre1 = new Boolean(false);
    private java.lang.Boolean eleAutre2 = new Boolean(false);
    private java.lang.Boolean eleCommission = new Boolean(false);
    private java.lang.Boolean eleDomicile = new Boolean(false);
    private java.lang.Boolean eleGratification = new Boolean(false);
    private java.lang.Boolean eleHeure = new Boolean(false);
    private java.lang.Boolean eleHonoraire = new Boolean(false);
    private java.lang.Boolean eleIndemnite = new Boolean(false);
    private java.lang.String eleLibelleAutre1 = new String();
    private java.lang.String eleLibelleAutre2 = new String();
    private java.lang.Boolean eleMensuel = new Boolean(false);
    private java.lang.Boolean eleNature = new Boolean(false);
    private java.lang.Boolean elePiece = new Boolean(false);
    private java.lang.Boolean elePourboire = new Boolean(false);
    // private java.lang.String planAssurance = new String();
    private java.lang.Boolean erreur = new Boolean(false);
    private java.lang.Boolean flagDernierRapport = new Boolean(false);
    private java.lang.String formeJuri = new String();
    private java.lang.String genreControle = new String();
    private java.lang.String idTiers = new String();
    private java.lang.String increment = "";
    private java.lang.Boolean inscriptionRC = new Boolean(false);
    private java.lang.String motif = new String();
    private java.lang.String nombreSalariesFixes = new String();
    private java.lang.String nouveauNumRapport = new String();
    private java.lang.String numAffilie = new String();
    private java.lang.String numeroExterne = new String();
    private java.lang.String personneContact1 = new String();
    private java.lang.String personneContact2 = new String();
    private java.lang.String personneContact3 = new String();
    private java.lang.Boolean rapportAFSepare = new Boolean(false);
    private java.lang.String rapportNumero = new String();
    private java.lang.Boolean succursales = new Boolean(false);

    /*
     * private FWParametersSystemCode csGenreControle = null; private FWParametersSystemCode csDebitCredit = null;
     */

    private java.lang.String tempsJour = new String();
    private java.lang.String tierDesignation1 = "";
    private java.lang.String tierDesignation2 = "";

    /**
     * Constructeur de AFControleEmployeur.
     */
    public AFControleEmployeur() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
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
     * Effectue des traitements avant un ajout dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // incrémente de +1 le numéro
        setControleEmployeurId(this._incCounter(transaction, "0"));
        setIncrement(incCounter(transaction, "0", getAnnee()));
        AFAffiliationManager aff = new AFAffiliationManager();
        aff.setSession(getSession());
        aff.setForAffilieNumero(getNumAffilie());
        aff.setFromDateFin(getDateFinControle());
        aff.setForDateDebutAffLowerOrEqualTo(getDateDebutControle());
        aff.setForTypesAffParitaires();
        aff.find();
        if (aff.size() > 0) {
            setAffiliationId(((AFAffiliation) aff.getFirstEntity()).getAffiliationId());
            setIdTiers(((AFAffiliation) aff.getFirstEntity()).getIdTiers());
        } else {
            _addError(transaction, "Le numéro d'affilié n'existe pas !");
        }

        AFControleEmployeur controle = new AFControleEmployeur();
        String racine = "";
        int incrementDoublon = 0;
        if (JadeStringUtil.isEmpty(getNouveauNumRapport())) {
            setNouveauNumRapport(getAnnee() + "." + getIncrement() + ".1");
            setFlagDernierRapport(new Boolean(true));
        } else {
            if (getNouveauNumRapport().length() < 10) {
                _addError(transaction, getSession().getLabel("ERREUR_NBR_POSITION_MINI"));
            } else {
                AFControleEmployeurManager contMana = new AFControleEmployeurManager();
                contMana.setSession(getSession());
                contMana.setForNumAffilie(getNumAffilie());
                contMana.setLikeNouveauNumRapport(getNouveauNumRapport());
                contMana.setOrderBy("MDLNRA DESC");
                contMana.find();
                if (contMana.size() > 0) {
                    controle = (AFControleEmployeur) contMana.getFirstEntity();
                    racine = controle.getNouveauNumRapport().substring(0, 10);
                    incrementDoublon = JadeStringUtil.toInt(controle.getNouveauNumRapport().substring(11, 12));
                    setNouveauNumRapport(racine + "." + "" + (incrementDoublon + 1));
                    setFlagDernierRapport(new Boolean(true));
                    if (controle.getFlagDernierRapport().booleanValue()) {
                        controle.setFlagDernierRapport(new Boolean(false));
                        controle.wantCallMethodBefore(false);
                        controle.update(transaction);
                    }
                } else {
                    _addError(transaction, getSession().getLabel("ERREUR_CONTEMPL_NUM_RAPPORT"));
                }
            }
        }
    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        AFControleEmployeur controle = new AFControleEmployeur();
        String racine = "";
        int incrementDoublon = 0;
        if (JadeStringUtil.isEmpty(getNouveauNumRapport())) {
            setNouveauNumRapport(getAnnee() + "." + incCounter(transaction, "0", getAnnee()) + ".1");
            setFlagDernierRapport(new Boolean(true));
        } else {
            if (getNouveauNumRapport().length() < 12) {
                _addError(transaction, getSession().getLabel("ERREUR_NBR_POSITION"));
            } else {
                AFControleEmployeurManager contMana = new AFControleEmployeurManager();
                contMana.setSession(getSession());
                contMana.setForNotId(getControleEmployeurId());
                contMana.setForNumAffilie(getNumAffilie());
                contMana.setLikeNouveauNumRapport(getNouveauNumRapport());
                contMana.setOrderBy("MDLNRA DESC");
                contMana.find();
                if (contMana.size() == 0) {
                    racine = getNouveauNumRapport().substring(0, 10);
                    incrementDoublon = JadeStringUtil.toInt(getNouveauNumRapport().substring(11, 12));
                    if (incrementDoublon != 1) {
                        AFControleEmployeurManager contMana2 = new AFControleEmployeurManager();
                        contMana2.setSession(getSession());
                        contMana2.setForNumAffilie(getNumAffilie());
                        contMana2.setLikeNouveauNumRapport(racine + "." + "" + (incrementDoublon - 1));
                        contMana2.setOrderBy("MDLNRA DESC");
                        contMana2.find();
                        if (contMana2.size() == 1) {
                            controle = (AFControleEmployeur) contMana2.getFirstEntity();
                            setFlagDernierRapport(new Boolean(true));
                            if (controle.getFlagDernierRapport().booleanValue()) {
                                controle.setFlagDernierRapport(new Boolean(false));
                                controle.update();
                            }
                        } else {
                            _addError(
                                    transaction,
                                    getSession().getLabel("ERREUR_NUM_PRECEDENT") + " "
                                            + contMana2.getLikeNouveauNumRapport());
                        }
                    }
                } else {
                    _addError(transaction, getSession().getLabel("ERREUR_CONTEMPL_NUM_RAPPORT"));
                }
            }
        }
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "AFCONTP";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        affiliationId = statement.dbReadNumeric("MAIAFF");
        idTiers = statement.dbReadNumeric("HTITIE");
        controleEmployeurId = statement.dbReadNumeric("MDICON");
        numAffilie = statement.dbReadString("MALNAF");
        datePrevue = statement.dbReadDateAMJ("MDDPRE");
        dateEffective = statement.dbReadDateAMJ("MDDEFF");
        genreControle = statement.dbReadNumeric("MDTGEN");
        rapportNumero = statement.dbReadString("MDLRAP");
        nouveauNumRapport = statement.dbReadString("MDLNRA");
        controleur = statement.dbReadNumeric("MDICTL");
        erreur = statement.dbReadBoolean("MDBERR");
        numeroExterne = statement.dbReadString("MDLNAF");
        debitCredit = statement.dbReadNumeric("MDTDCR");
        dateDebutControle = statement.dbReadDateAMJ("MDDCDE");
        dateFinControle = statement.dbReadDateAMJ("MDDCFI");
        nombreSalariesFixes = statement.dbReadNumeric("MDNSAL");
        controleurVisa = statement.dbReadString("MDLNOM");
        datePrecedente = statement.dbReadDateAMJ("MDDPRC");
        tempsJour = statement.dbReadNumeric("MDNTJO");
        motif = statement.dbReadNumeric("MDTMOT");
        brancheEco = statement.dbReadNumeric("MDTBEC");
        formeJuri = statement.dbReadNumeric("MDTFJU");
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
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        boolean validationOK = true;

        // Contrôle que les champs obligatoire soit renseigné
        validationOK &= _propertyMandatory(statement.getTransaction(), getDatePrevue(), getSession().getLabel("450"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getGenreControle(), getSession().getLabel("460"));
        // validationOK &= _propertyMandatory(statement.getTransaction(),
        // getDateDebutControle(),getSession().getLabel("1060"));
        // validationOK &= _propertyMandatory(statement.getTransaction(),
        // getDateFinControle(),getSession().getLabel("1070"));

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
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MDICON", this._dbWriteNumeric(statement.getTransaction(), getControleEmployeurId(), ""));
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
        statement.writeField("MDICON",
                this._dbWriteNumeric(statement.getTransaction(), getControleEmployeurId(), "controleEmployeurId"));
        statement.writeField("MALNAF", this._dbWriteString(statement.getTransaction(), getNumAffilie(), "numAffilie"));
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
        statement.writeField("MDICTL", this._dbWriteNumeric(statement.getTransaction(), getControleur(), "controleur"));
        statement
                .writeField("MDBERR", this._dbWriteBoolean(statement.getTransaction(), isErreur(),
                        BConstants.DB_TYPE_BOOLEAN_CHAR, "erreur"));
        statement.writeField("MDLNAF",
                this._dbWriteString(statement.getTransaction(), getNumeroExterne(), "numeroExterne"));
        statement.writeField("MDLNOM",
                this._dbWriteString(statement.getTransaction(), getControleurVisa(), "controleurVisa"));
        statement.writeField("MDTDCR",
                this._dbWriteNumeric(statement.getTransaction(), getDebitCredit(), "debitCredit"));
        statement.writeField("MDNSAL",
                this._dbWriteNumeric(statement.getTransaction(), getNombreSalariesFixes(), "nombreSalariesFixes"));
        statement.writeField("MDDPRC",
                this._dbWriteDateAMJ(statement.getTransaction(), getDatePrecedente(), "datePrecedente"));
        statement.writeField("MDNTJO", this._dbWriteNumeric(statement.getTransaction(), getTempsJour(), "tempsJour"));
        statement.writeField("MDTMOT", this._dbWriteNumeric(statement.getTransaction(), getMotif(), "motif"));
        statement.writeField("MDTBEC", this._dbWriteNumeric(statement.getTransaction(), getBrancheEco(), "brancheEco"));
        statement.writeField("MDTFJU", this._dbWriteNumeric(statement.getTransaction(), getFormeJuri(), "formeJuri"));
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

    }

    // *******************************************************
    // Getter
    // *******************************************************

    /*
     * Retourne l'adresse de domicile du tiers Ex: Soit le n° avs, le n° affilié ou le n° de contribuable
     */
    public java.lang.String getAdresseCourrier(BTransaction trans, String date) throws Exception {
        String courrier = "";
        String domaineCourrier = "";
        courrier = IConstantes.CS_AVOIR_ADRESSE_COURRIER;
        domaineCourrier = ICommonConstantes.CS_APPLICATION_COTISATION;

        // Récupérer le tiers
        TITiers tiers = getTiers();
        if (tiers == null) {
            return "";
        } else {
            return tiers.getAdresseAsString(courrier, domaineCourrier, date, getNumAffilie());
        }

    }

    /*
     * Retourne l'adresse de domicile du tiers Ex: Soit le n° avs, le n° affilié ou le n° de contribuable
     */
    public java.lang.String getAdresseDomicile(BTransaction trans, String date) throws Exception {
        // Récupérer le tiers
        TITiers tiers = getTiers();
        if (tiers == null) {
            return "";
        } else {
            return tiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, IConstantes.CS_APPLICATION_DEFAUT,
                    date, getNumAffilie());
        }
    }

    /*
     * Retourne l'adresse de domicile du tiers Ex: Soit le n° avs, le n° affilié ou le n° de contribuable
     */
    public java.lang.String getAdressePrincipale(BTransaction trans, String date) throws Exception {
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
    public AFAffiliation getAffiliation() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getAffiliationId())) {
            return null;
        }

        if (_affiliation == null) {

            _affiliation = new AFAffiliation();
            _affiliation.setSession(getSession());
            _affiliation.setAffiliationId(getAffiliationId());
            try {
                _affiliation.retrieve();
                /*
                 * if (_affiliation.hasErrors()) _affiliation = null;
                 */
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _affiliation = null;
            }
        }
        return _affiliation;
    }

    public java.lang.String getAffiliationId() {
        return affiliationId;
    }

    /**
     * @return
     * @throws Exception
     */
    public java.lang.String getAffiliationLaa() throws Exception {
        AFSuiviCaisseAffiliationManager manager = new AFSuiviCaisseAffiliationManager();
        manager.setSession(getSession());
        manager.setForAffiliationId(getAffiliationId());
        manager.setForAnneeActive(getAnneeActive());
        manager.setForGenreCaisse(CodeSystem.GENRE_CAISSE_LAA);
        try {
            manager.find();
        } catch (Exception e) {
            _addError(null, e.getMessage());
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
    public java.lang.String getAffiliationLpp() throws Exception {
        AFSuiviCaisseAffiliationManager manager = new AFSuiviCaisseAffiliationManager();
        manager.setSession(getSession());
        manager.setForAffiliationId(getAffiliationId());
        manager.setForAnneeActive(getAnneeActive());
        manager.setForGenreCaisse(CodeSystem.GENRE_CAISSE_LPP);
        try {
            manager.find();
        } catch (Exception e) {
            _addError(null, e.getMessage());
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

    /**
     * @return
     */
    public java.lang.String getAnnee() {
        annee = getDatePrevue().substring(6);
        return annee;
    }

    /**
     * @return
     */
    public java.lang.String getAnneeActive() {
        return anneeActive;
    }

    /**
     * @return
     */
    public java.lang.String getBrancheEco() {
        if (brancheEco.equals("0") || brancheEco.equals(null) || brancheEco.equals("")) {
            AFAffiliation aff = new AFAffiliation();
            aff.setSession(getSession());
            aff.setAffiliationId(getAffiliationId());
            try {
                aff.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
            setBrancheEco(aff.getBrancheEconomique());
        }

        return brancheEco;
    }

    /**
     * @return
     */
    public java.lang.String getChampConseil() {
        return champConseil;
    }

    /**
     * @return
     */
    public java.lang.String getChampConstate() {
        return champConstate;
    }

    /**
     * @return
     */
    public java.lang.String getChampRemarque() {
        return champRemarque;
    }

    /**
     * @return
     */
    public java.lang.String getComptaTenuPar() {
        return comptaTenuPar;
    }

    public java.lang.String getControleEmployeurId() {
        return controleEmployeurId;
    }

    public java.lang.String getControleur() {
        return controleur;
    }

    /**
     * @return
     */
    public java.lang.String getControleurNom() {
        if (!getControleurVisa().equals(null) && !getControleurVisa().equals("")) {
            AFReviseurManager manager = new AFReviseurManager();
            manager.setSession(getSession());
            manager.setForVisa(getControleurVisa());
            try {
                manager.find();
                if (manager.size() > 0) {
                    controleurNom = ((AFReviseur) manager.getFirstEntity()).getNomReviseur();
                    setControleur(((AFReviseur) manager.getFirstEntity()).getIdReviseur());
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
        }
        return controleurNom;
    }

    public java.lang.String getControleurVisa() {
        return controleurVisa;
    }

    /**
     * @return
     */
    public java.lang.String getDateBouclement() {
        return dateBouclement;
    }

    public java.lang.String getDateDebutControle() {
        return dateDebutControle;
    }

    public java.lang.String getDateEffective() {
        return dateEffective;
    }

    /*
     * public FWParametersSystemCode getCsGenreControle() { // enregistrement déjà chargé ? if (csGenreControle == null)
     * { // liste pas encore chargée, on la charge csGenreControle = new FWParametersSystemCode();
     * csGenreControle.getCode(getGenreControle()); } return csGenreControle; }
     */

    /*
     * public FWParametersSystemCode getCsDebitCredit() { // enregistrement déjà chargé ? if (csDebitCredit == null) {
     * // liste pas encore chargée, on la charge csDebitCredit = new FWParametersSystemCode();
     * csDebitCredit.getCode(getDebitCredit()); } return csDebitCredit; }
     */

    // *******************************************************
    // Setter
    // *******************************************************

    public java.lang.String getDateFinControle() {
        return dateFinControle;
    }

    /**
     * @return
     */
    public java.lang.String getDatePrecedentControle() {
        if (JadeStringUtil.isEmpty(getDatePrecedente()) && !JadeStringUtil.isEmpty(getNumAffilie())) {
            AFControleEmployeurManager contManager = new AFControleEmployeurManager();
            contManager.setSession(getSession());
            contManager.setForNumAffilie(getNumAffilie());
            contManager.setField("MALNAF, MDDEFF");
            contManager.setOrderBy("MDDEFF DESC");
            try {
                contManager.find();

                if (contManager.size() > 1) {
                    for (int i = 0; i < contManager.size(); i++) {
                        if (BSessionUtil.compareDateFirstGreater(getSession(), getDatePrevue(),
                                ((AFControleEmployeur) contManager.getEntity(i)).getDateEffective())) {
                            setDatePrecedente(((AFControleEmployeur) contManager.getEntity(i)).getDateEffective());
                        } else {
                            setDatePrecedente("");
                        }
                        if (!getDatePrecedente().equals(datePrevue)) {
                            i = contManager.size();
                        }
                    }
                } else {
                    setDatePrecedente("");
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
        }
        return getDatePrecedente();
    }

    public java.lang.String getDatePrecedente() {
        return datePrecedente;
    }

    public java.lang.String getDatePrevue() {
        return datePrevue;
    }

    /**
     * @return
     */
    public java.lang.String getDateProchain() {
        return dateProchain;
    }

    public java.lang.String getDebitCredit() {
        return debitCredit;
    }

    /**
     * @return
     */
    public java.lang.String getDocAllocMiliComplet() {
        return docAllocMiliComplet;
    }

    /**
     * @return
     */
    public java.lang.String getDocAllocMiliSondage() {
        return docAllocMiliSondage;
    }

    /**
     * @return
     */
    public java.lang.String getDocAllocPerteComplet() {
        return docAllocPerteComplet;
    }

    /**
     * @return
     */
    public java.lang.String getDocAllocPerteSondage() {
        return docAllocPerteSondage;
    }

    /**
     * @return
     */
    public java.lang.String getDocBilanComplet() {
        return docBilanComplet;
    }

    /**
     * @return
     */
    public java.lang.String getDocBilanSondage() {
        return docBilanSondage;
    }

    /**
     * @return
     */
    public java.lang.String getDocComptaComplet() {
        return docComptaComplet;
    }

    /**
     * @return
     */
    public java.lang.String getDocComptaSondage() {
        return docComptaSondage;
    }

    /**
     * @return
     */
    public java.lang.String getDocDroitAllocComplet() {
        return docDroitAllocComplet;
    }

    /**
     * @return
     */
    public java.lang.String getDocDroitAllocSondage() {
        return docDroitAllocSondage;
    }

    /**
     * @return
     */
    public java.lang.Boolean getEleAutre1() {
        return eleAutre1;
    }

    /**
     * @return
     */
    public java.lang.Boolean getEleAutre2() {
        return eleAutre2;
    }

    /**
     * @return
     */
    public java.lang.Boolean getEleCommission() {
        return eleCommission;
    }

    /**
     * @return
     */
    public java.lang.Boolean getEleDomicile() {
        return eleDomicile;
    }

    /**
     * @return
     */
    public java.lang.Boolean getEleGratification() {
        return eleGratification;
    }

    /**
     * @return
     */
    public java.lang.Boolean getEleHeure() {
        return eleHeure;
    }

    /**
     * @return
     */
    public java.lang.Boolean getEleHonoraire() {
        return eleHonoraire;
    }

    /**
     * @return
     */
    public java.lang.Boolean getEleIndemnite() {
        return eleIndemnite;
    }

    /**
     * @return
     */
    public java.lang.String getEleLibelleAutre1() {
        return eleLibelleAutre1;
    }

    /**
     * @return
     */
    public java.lang.String getEleLibelleAutre2() {
        return eleLibelleAutre2;
    }

    /**
     * @return
     */
    public java.lang.Boolean getEleMensuel() {
        return eleMensuel;
    }

    /**
     * @return
     */
    public java.lang.Boolean getEleNature() {
        return eleNature;
    }

    /**
     * @return
     */
    public java.lang.Boolean getElePiece() {
        return elePiece;
    }

    /**
     * @return
     */
    public java.lang.Boolean getElePourboire() {
        return elePourboire;
    }

    public java.lang.Boolean getFlagDernierRapport() {
        return flagDernierRapport;
    }

    /**
     * @return
     */
    public java.lang.String getFormeJuri() {
        if (formeJuri.equals("0") || formeJuri.equals(null) || formeJuri.equals("")) {
            AFAffiliation aff = new AFAffiliation();
            aff.setSession(getSession());
            aff.setAffiliationId(getAffiliationId());
            try {
                aff.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
            setFormeJuri(aff.getPersonnaliteJuridique());
        }
        return formeJuri;
    }

    public java.lang.String getGenreControle() {
        return genreControle;
    }

    /**
     * @return
     */
    public java.lang.String getIdTiers() {
        return idTiers;
    }

    public java.lang.String getIncrement() {
        return increment;
    }

    public java.lang.String getInscriLibelle() throws Exception {
        String reponse = "";
        if (inscriptionRC.booleanValue()) {
            reponse = (getSession().getApplication().getLabel("NAOS_LIBELLE_OUI", getLangueTiers()));
        } else {
            reponse = (getSession().getApplication().getLabel("NAOS_LIBELLE_NON", getLangueTiers()));
        }
        return reponse;
    }

    /**
     * @return
     */
    public java.lang.Boolean getInscriptionRC() {
        return inscriptionRC;
    }

    public java.lang.Boolean getInscriptionRCParticularite() throws Exception {
        if (AFParticulariteAffiliation.existeParticulariteDateDonnee(getSession(), getAffiliationId(),
                CodeSystem.PARTIC_AFFILIE_INSCRIT_RC, JACalendar.todayJJsMMsAAAA())) {
            setInscriptionRC(new Boolean(true));
            return new Boolean(true);
        } else {
            return getInscriptionRC();
        }
    }

    public String getLangueTiers() {
        TITiers tier = new TITiers();
        tier = getTiers();
        if (tier != null) {
            return tier.getLangueIso();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public java.lang.String getMotif() {
        return motif;
    }

    public java.lang.String getNombreSalariesFixes() {
        return nombreSalariesFixes;
    }

    public String getNomTiers() {
        // Récupérer le tiers
        TITiers tiers = getTiers();
        if (tiers != null) {
            tierDesignation1 = tiers.getDesignation1();
            tierDesignation2 = tiers.getDesignation2();
        }
        return getTierDesignation1() + " " + getTierDesignation2();
    }

    public java.lang.String getNouveauNumRapport() {
        return nouveauNumRapport;
    }

    /**
     * @return
     */
    public java.lang.String getNumAffilie() {
        if (numAffilie.equals("") || numAffilie.equals(null)) {
            AFAffiliation aff = new AFAffiliation();
            aff.setSession(getSession());
            aff.setAffiliationId(getAffiliationId());
            try {
                aff.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
            numAffilie = aff.getAffilieNumero();
        }
        return numAffilie;
    }

    public java.lang.String getNumeroExterne() {
        return numeroExterne;
    }

    /**
     * @return
     */
    public java.lang.String getPersonneContact1() {
        return personneContact1;
    }

    /**
     * @return
     */
    public java.lang.String getPersonneContact2() {
        return personneContact2;
    }

    /**
     * @return
     */
    public java.lang.String getPersonneContact3() {
        return personneContact3;
    }

    /**
     * @return
     */
    public java.lang.Boolean getRapportAFSepare() {
        return rapportAFSepare;
    }

    public java.lang.String getRapportNumero() {
        return rapportNumero;
    }

    /**
     * @return
     */
    public AFReviseur getReviseur() {
        if ((_reviseur == null) && !JadeStringUtil.isBlank(getControleurVisa())) {
            AFReviseurManager manager = new AFReviseurManager();
            manager.setSession(getSession());
            manager.setForVisa(getControleurVisa());
            try {
                manager.find();
                if (manager.size() > 0) {
                    _reviseur = (AFReviseur) manager.getFirstEntity();
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
        }
        return _reviseur;
    }

    public java.lang.String getSuccLibelle() throws Exception {
        String reponse = "";
        // succursales = new
        // Boolean(AFAffiliationUtil.hasSuccursale(getAffiliation(),
        // getDatePrevue()));
        if (succursales.booleanValue()) {
            reponse = (getSession().getApplication().getLabel("NAOS_LIBELLE_OUI", getLangueTiers()));
        } else {
            reponse = (getSession().getApplication().getLabel("NAOS_LIBELLE_NON", getLangueTiers()));
        }
        return reponse;
    }

    /**
     * @return
     */
    public java.lang.Boolean getSuccursales() {
        try {
            succursales = new Boolean(AFAffiliationUtil.hasSuccursale(getAffiliation(), getDatePrevue()));
        } catch (Exception e) {
            _addError(null, e.getMessage());
        }
        return succursales;
    }

    /**
     * @return
     */
    public java.lang.String getTempsJour() {
        return tempsJour;
    }

    /**
     * @return
     */
    public java.lang.String getTierDesignation1() {
        return tierDesignation1;
    }

    /**
     * @return
     */
    public java.lang.String getTierDesignation2() {
        return tierDesignation2;
    }

    /**
     * Rechercher le tiers pour l'affiliation en fonction de son ID.
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
                /*
                 * if (_tiers.getSession().hasErrors()) _tiers = null;
                 */
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _tiers = null;
            }
        }
        return _tiers;
    }

    public String incCounter(BTransaction transaction, String increment, String annee) throws Exception {
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

    public java.lang.Boolean isErreur() {
        return erreur;
    }

    public void setAffiliationId(java.lang.String newAffiliationId) {
        affiliationId = newAffiliationId;
    }

    /**
     * @param string
     */
    public void setAnnee(java.lang.String string) {
        annee = string;
    }

    /**
     * @param string
     */
    public void setAnneeActive(java.lang.String string) {
        anneeActive = string;
    }

    /**
     * @param string
     */
    public void setBrancheEco(java.lang.String string) {
        brancheEco = string;
    }

    /**
     * @param string
     */
    public void setChampConseil(java.lang.String string) {
        champConseil = string;
    }

    /**
     * @param string
     */
    public void setChampConstate(java.lang.String string) {
        champConstate = string;
    }

    /**
     * @param string
     */
    public void setChampRemarque(java.lang.String string) {
        champRemarque = string;
    }

    /**
     * @param string
     */
    public void setComptaTenuPar(java.lang.String string) {
        comptaTenuPar = string;
    }

    public void setControleEmployeurId(java.lang.String newControleEmployeurId) {
        controleEmployeurId = newControleEmployeurId;
    }

    public void setControleur(java.lang.String newControleur) {
        controleur = newControleur;
    }

    /**
     * @param string
     */
    public void setControleurNom(java.lang.String string) {
        controleurNom = string;
    }

    public void setControleurVisa(java.lang.String newControleurVisa) {
        controleurVisa = newControleurVisa;
    }

    /**
     * @param string
     */
    public void setDateBouclement(java.lang.String string) {
        dateBouclement = string;
    }

    public void setDateDebutControle(java.lang.String newDateDebutControle) {
        dateDebutControle = newDateDebutControle;
    }

    public void setDateEffective(java.lang.String newDateEffective) {
        dateEffective = newDateEffective;
    }

    public void setDateFinControle(java.lang.String newDateFinControle) {
        dateFinControle = newDateFinControle;
    }

    /**
     * @param string
     */
    public void setDatePrecedente(java.lang.String string) {
        datePrecedente = string;
    }

    public void setDatePrevue(java.lang.String newDatePrevue) {
        datePrevue = newDatePrevue;
    }

    /**
     * @param string
     */
    public void setDateProchain(java.lang.String string) {
        dateProchain = string;
    }

    public void setDebitCredit(java.lang.String newDebitCredit) {
        debitCredit = newDebitCredit;
    }

    /**
     * @param string
     */
    public void setDocAllocMiliComplet(java.lang.String string) {
        docAllocMiliComplet = string;
    }

    /**
     * @param string
     */
    public void setDocAllocMiliSondage(java.lang.String string) {
        docAllocMiliSondage = string;
    }

    /**
     * @param string
     */
    public void setDocAllocPerteComplet(java.lang.String string) {
        docAllocPerteComplet = string;
    }

    /**
     * @param string
     */
    public void setDocAllocPerteSondage(java.lang.String string) {
        docAllocPerteSondage = string;
    }

    /**
     * @param string
     */
    public void setDocBilanComplet(java.lang.String string) {
        docBilanComplet = string;
    }

    /**
     * @param string
     */
    public void setDocBilanSondage(java.lang.String string) {
        docBilanSondage = string;
    }

    /**
     * @param string
     */
    public void setDocComptaComplet(java.lang.String string) {
        docComptaComplet = string;
    }

    /**
     * @param string
     */
    public void setDocComptaSondage(java.lang.String string) {
        docComptaSondage = string;
    }

    /**
     * @param string
     */
    public void setDocDroitAllocComplet(java.lang.String string) {
        docDroitAllocComplet = string;
    }

    /**
     * @param string
     */
    public void setDocDroitAllocSondage(java.lang.String string) {
        docDroitAllocSondage = string;
    }

    /**
     * @param boolean1
     */
    public void setEleAutre1(java.lang.Boolean boolean1) {
        eleAutre1 = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setEleAutre2(java.lang.Boolean boolean1) {
        eleAutre2 = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setEleCommission(java.lang.Boolean boolean1) {
        eleCommission = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setEleDomicile(java.lang.Boolean boolean1) {
        eleDomicile = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setEleGratification(java.lang.Boolean boolean1) {
        eleGratification = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setEleHeure(java.lang.Boolean boolean1) {
        eleHeure = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setEleHonoraire(java.lang.Boolean boolean1) {
        eleHonoraire = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setEleIndemnite(java.lang.Boolean boolean1) {
        eleIndemnite = boolean1;
    }

    /**
     * @param string
     */
    public void setEleLibelleAutre1(java.lang.String string) {
        eleLibelleAutre1 = string;
    }

    /**
     * @param string
     */
    public void setEleLibelleAutre2(java.lang.String string) {
        eleLibelleAutre2 = string;
    }

    /**
     * @param boolean1
     */
    public void setEleMensuel(java.lang.Boolean boolean1) {
        eleMensuel = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setEleNature(java.lang.Boolean boolean1) {
        eleNature = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setElePiece(java.lang.Boolean boolean1) {
        elePiece = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setElePourboire(java.lang.Boolean boolean1) {
        elePourboire = boolean1;
    }

    public void setErreur(java.lang.Boolean newErreur) {
        erreur = newErreur;
    }

    public void setFlagDernierRapport(java.lang.Boolean flagDernierRapport) {
        this.flagDernierRapport = flagDernierRapport;
    }

    /**
     * @param string
     */
    public void setFormeJuri(java.lang.String string) {
        formeJuri = string;
    }

    public void setGenreControle(java.lang.String newGenreControle) {
        genreControle = newGenreControle;
    }

    /**
     * @param string
     */
    public void setIdTiers(java.lang.String string) {
        idTiers = string;
    }

    public void setIncrement(java.lang.String increment) {
        this.increment = increment;
    }

    /**
     * @param boolean1
     */
    public void setInscriptionRC(java.lang.Boolean boolean1) {
        inscriptionRC = boolean1;
    }

    /**
     * @param string
     */
    public void setMotif(java.lang.String string) {
        motif = string;
    }

    public void setNombreSalariesFixes(java.lang.String newNombreSalariesFixes) {
        nombreSalariesFixes = newNombreSalariesFixes;
    }

    public void setNouveauNumRapport(java.lang.String nouveauNumRapport) {
        this.nouveauNumRapport = nouveauNumRapport;
    }

    /**
     * @param string
     */
    public void setNumAffilie(java.lang.String string) {
        numAffilie = string;
    }

    public void setNumeroExterne(java.lang.String newNumeroExterne) {
        numeroExterne = newNumeroExterne;
    }

    /**
     * @param string
     */
    public void setPersonneContact1(java.lang.String string) {
        personneContact1 = string;
    }

    /**
     * @param string
     */
    public void setPersonneContact2(java.lang.String string) {
        personneContact2 = string;
    }

    /**
     * @param string
     */
    public void setPersonneContact3(java.lang.String string) {
        personneContact3 = string;
    }

    /**
     * @param boolean1
     */
    public void setRapportAFSepare(java.lang.Boolean boolean1) {
        rapportAFSepare = boolean1;
    }

    public void setRapportNumero(java.lang.String newRapportNumero) {
        rapportNumero = newRapportNumero;
    }

    /**
     * @param boolean1
     */
    public void setSuccursales(java.lang.Boolean boolean1) {
        succursales = boolean1;
    }

    /**
     * @param string
     */
    public void setTempsJour(java.lang.String string) {
        tempsJour = string;
    }

    /**
     * @param string
     */
    public void setTierDesignation1(java.lang.String string) {
        tierDesignation1 = string;
    }

    /**
     * @param string
     */
    public void setTierDesignation2(java.lang.String string) {
        tierDesignation2 = string;
    }

    /**
     * @return
     */
    // public java.lang.String getPlanAssurance() {
    // AFCotisationManager manager = new AFCotisationManager();
    // manager.setSession(getSession());
    // manager.setForAffiliationId(this.getAffiliationId());
    // manager.setForAnneeActive(getDateFinControle().substring(6));
    // AFCotisation coti = new AFCotisation();
    // try {
    // manager.find();
    // if(manager.size()>0){
    // for(int i = 0; i<manager.size(); i++){
    // String idAssurance = "";
    // coti = (AFCotisation)manager.getEntity(i);
    // if(!idAssurance.equals(coti.getAssuranceId())){
    // if(!planAssurance.equals("")&&!planAssurance.equals(null)){
    // planAssurance = planAssurance + "\n";
    // }
    // planAssurance = planAssurance +
    // coti.getAssurance().getAssuranceLibelle(this.getLangueTiers());
    // idAssurance = coti.getAssuranceId();
    // }
    // }
    // }
    //
    // } catch (Exception e) {
    // _addError(null, e.getMessage());
    // }
    // return planAssurance;
    // }

}
