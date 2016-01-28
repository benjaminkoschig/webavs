package globaz.phenix.db.communications;

import globaz.globall.db.BConstants;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.phenix.process.communications.CPProcessValiderPlausibilite;

public class CPCommunicationFiscaleRetourVDViewBean extends CPCommunicationFiscaleRetourViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String SPONTANEE = "sp";

    public final static String TAXATION_OFFICE = "to";

    private String description = "";

    /*
     * Traitement avant ajout
     */

    private String vdAddChez = "";
    private String vdAddRue = "";
    private String vdAssujCtb = "";
    private Boolean vdBourses = new Boolean(false);
    private String vdCapInvesti = "";
    private String vdCodeNatureRente = "";
    private String vdCommentaire = "";
    private String vdDatDetCapInvesti = "";
    private String vdDateDemande = "";
    private String vdDateDetFortune = "";
    private String vdDatNaissance = "";
    private String vdDebAssuj = "";
    private String vdDebPeriode = "";
    private Boolean vdDivers = new Boolean(false);
    private String vdDroitHabitation = "";
    private String vdExcesLiquidite = "";
    private String vdFinAssuj = "";
    private String vdFinPeriode = "";
    private String vdFortuneImposable = "";

    private java.lang.String vdGenreAffilie = "";
    private String vdGIprof = "";
    private String vdImpositionDepense = "";
    private Boolean vdIndemniteJournaliere = new Boolean(false);
    private String vdMontantActLuc = "";
    private String vdMontantRente = "";
    private String vdNatureComm = "";
    private java.lang.String vdNomPrenom = "";
    private java.lang.String vdNumAffilie = "";
    private java.lang.String vdNumAvs = "";
    private java.lang.String vdNumCaisse = "";
    private java.lang.String vdNumContribuable = "";
    private java.lang.String vdNumDemande = "";
    private String vdNumLocalite = "";
    private String vdPenAliObtenue = "";
    private Boolean vdPension = new Boolean(false);
    private Boolean vdPensionAlimentaire = new Boolean(false);
    private Boolean vdRenteInvalidite = new Boolean(false);
    private Boolean vdRenteViagere = new Boolean(false);
    private Boolean vdRenteVieillesse = new Boolean(false);
    private Boolean vdRetraite = new Boolean(false);
    private String vdRevActInd = "";
    private String vdRevenuActivitesLucratives = "";
    private String vdRevenuNet = "";
    private String vdSalaireCotisant = "";
    private String vdSalVerseCjt = "";
    private String vdTypeTax = "";

    /**
     * Commentaire relatif au constructeur CPDonneesBase
     */
    public CPCommunicationFiscaleRetourVDViewBean() {
        super();
    }

    @Override
    protected void _afterRetrieve(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        super._afterRetrieve(transaction);
        // setValeurChampRecherche(getValeurRechercheBD());
        setNumContribuableRecu(getVdNumContribuable());
        setNumAffilieRecu(getVdNumAffilie());
    }

    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        if (!isForBackup()) {
            setIdRetour(_incCounter(transaction, "0", "CPCRETP", "0", "0"));
        }
        setStatus(CS_RECEPTIONNE);
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String table1 = "CPCRVDP";
        String table2 = "CPCRETP";

        return _getCollection() + table1 + " INNER JOIN " + _getCollection() + table2 + " ON (" + _getCollection()
                + table1 + ".IKIRET=" + _getCollection() + table2 + ".IKIRET)";
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        if (!isForBackup()) {
            return "CPCRVDP";
        } else {
            return "CPCRVDB";
        }

    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        vdNumCaisse = statement.dbReadString("IKCAVD");
        vdNumDemande = statement.dbReadNumeric("IKNDEM");
        vdGenreAffilie = statement.dbReadString("IKGAVD");
        vdNumAffilie = statement.dbReadString("IKAFVD");
        vdNumAvs = statement.dbReadString("IKNAVD");
        vdNumContribuable = statement.dbReadString("IKCOVD");
        vdPension = statement.dbReadBoolean("IKBPEN");
        vdRetraite = statement.dbReadBoolean("IKBRET");
        vdRenteVieillesse = statement.dbReadBoolean("IKBRVI");
        vdRenteInvalidite = statement.dbReadBoolean("IKBRIN");
        vdPensionAlimentaire = statement.dbReadBoolean("IKBPAL");
        vdRenteViagere = statement.dbReadBoolean("IKBRVG");
        vdIndemniteJournaliere = statement.dbReadBoolean("IKBIJO");
        vdBourses = statement.dbReadBoolean("IKBBOU");
        vdDivers = statement.dbReadBoolean("IKBDIV");
        vdNomPrenom = statement.dbReadString("IKLNOM");

        vdDebPeriode = statement.dbReadString("IKDPRD");
        vdFinPeriode = statement.dbReadString("IKFPRD");
        vdAddChez = statement.dbReadString("IKADCH");
        vdAddRue = statement.dbReadString("IKADRU");
        vdNumLocalite = statement.dbReadString("IKNLOC");
        vdDatNaissance = statement.dbReadString("IKDATN");
        vdAssujCtb = statement.dbReadString("IKACTB");
        vdPenAliObtenue = statement.dbReadString("IKPAOB");
        vdDroitHabitation = statement.dbReadString("IKDHAB");
        vdTypeTax = statement.dbReadString("IKTTAX");
        vdDebAssuj = statement.dbReadString("IKDASS");
        vdFinAssuj = statement.dbReadString("IKFASS");
        vdCapInvesti = statement.dbReadNumeric("IKCINV");
        vdDatDetCapInvesti = statement.dbReadString("IKDATC");
        vdRevActInd = statement.dbReadNumeric("IKRAIN");
        vdGIprof = statement.dbReadString("IKGIPR");
        vdExcesLiquidite = statement.dbReadString("IKEXLI");
        vdSalVerseCjt = statement.dbReadNumeric("IKSVCJ");
        vdNatureComm = statement.dbReadString("IKNACO");
        vdSalaireCotisant = statement.dbReadNumeric("IKSACO");
        vdFortuneImposable = statement.dbReadNumeric("IKFOIM");
        vdDateDetFortune = statement.dbReadString("IKDDFO");
        vdMontantRente = statement.dbReadNumeric("IKMORE");
        vdMontantActLuc = statement.dbReadString("IKMALU");
        vdCodeNatureRente = statement.dbReadString("IKCORE");
        vdRevenuNet = statement.dbReadNumeric("IKRENE");
        vdCommentaire = statement.dbReadString("IKCOMM");
        vdRevenuActivitesLucratives = statement.dbReadNumeric("IKRALU");
        vdImpositionDepense = statement.dbReadNumeric("IKIMDE");
        vdDateDemande = statement.dbReadDateAMJ("IKDDEM");

        super._readProperties(statement);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires) Les reception sont toujours chargée, elle
     * seront validées par les plausibilités
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        setValeurChampRecherche(getValeurRechercheBD());
        setNumContribuableRecu(getVdNumContribuable());
        CPCommentaireCommunicationManager mng = new CPCommentaireCommunicationManager();
        mng.setSession(getSession());
        mng.setForIdCommunicationRetour(getIdRetour());
        mng.find();
        for (int i = 0; i < mng.size(); i++) {
            ((CPCommentaireCommunication) mng.getEntity(i)).delete();
        }
        if (getVdTypeTax().equalsIgnoreCase(TAXATION_OFFICE)) {
            CPCommentaireCommunication comment = new CPCommentaireCommunication();
            comment.setSession(getSession());
            comment.setIdCommentaire(CPCommentaireCommunication.CS_GENRE_TO);
            comment.setIdCommunicationRetour(getIdRetour());
            comment.add(statement.getTransaction());
        }
        if (CS_RECEPTIONNE.equalsIgnoreCase(getStatus()) || CS_AVERTISSEMENT.equalsIgnoreCase(getStatus())
                || CS_ERREUR.equalsIgnoreCase(getStatus())) {
            // Plausibilité
            CPProcessValiderPlausibilite process = new CPProcessValiderPlausibilite();
            process.setSession(getSession());
            process.setTransaction(statement.getTransaction());
            process.setCommunicationRetour(this);
            process.setSendCompletionMail(false);
            process.setDeclenchement(CPReglePlausibilite.CS_AVANT_GENERATION);
            process.executeProcess();
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(_getBaseTable() + "IKIRET", _dbWriteNumeric(statement.getTransaction(), getIdRetour(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        if (_getAction() == ACTION_COPY) {
            super._writeProperties(statement);
        }
        statement.writeField("IKIRET", _dbWriteNumeric(statement.getTransaction(), getIdRetour(), "idRetour"));
        statement.writeField("IKCAVD", _dbWriteString(statement.getTransaction(), getVdNumCaisse(), "vdNumCaisse"));
        statement.writeField("IKNDEM", _dbWriteNumeric(statement.getTransaction(), getVdNumDemande(), "vdNumDemande"));
        statement.writeField("IKGAVD",
                _dbWriteString(statement.getTransaction(), getVdGenreAffilie(), "vdGenreAffilie"));
        statement.writeField("IKAFVD", _dbWriteString(statement.getTransaction(), getVdNumAffilie(), "vdNumAffilie"));
        statement.writeField("IKNAVD", _dbWriteString(statement.getTransaction(), getVdNumAvs(), "vdNumAvs"));
        statement.writeField("IKCOVD",
                _dbWriteString(statement.getTransaction(), getVdNumContribuable(), "vdNumContribuable"));
        statement.writeField("IKLNOM", _dbWriteString(statement.getTransaction(), getVdNomPrenom(), "vdNomPrenom"));
        statement.writeField(
                "IKBPEN",
                _dbWriteBoolean(statement.getTransaction(), getVdPension(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "vdPension"));
        statement.writeField(
                "IKBRET",
                _dbWriteBoolean(statement.getTransaction(), getVdRetraite(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "vdRetraite"));
        statement.writeField(
                "IKBRVI",
                _dbWriteBoolean(statement.getTransaction(), getVdRenteVieillesse(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "vdRenteVieillesse"));
        statement.writeField(
                "IKBRIN",
                _dbWriteBoolean(statement.getTransaction(), getVdRenteInvalidite(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "vdRenteInvalidite"));
        statement.writeField(
                "IKBPAL",
                _dbWriteBoolean(statement.getTransaction(), getVdPensionAlimentaire(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "vdPensionAlimentaire"));
        statement.writeField(
                "IKBRVG",
                _dbWriteBoolean(statement.getTransaction(), getVdRenteViagere(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "vdRenteViagere"));
        statement.writeField(
                "IKBIJO",
                _dbWriteBoolean(statement.getTransaction(), getVdIndemniteJournaliere(),
                        BConstants.DB_TYPE_BOOLEAN_CHAR, "vdIndemniteJournaliere"));
        statement.writeField(
                "IKBBOU",
                _dbWriteBoolean(statement.getTransaction(), getVdBourses(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "vdBourses"));
        statement
                .writeField(
                        "IKBDIV",
                        _dbWriteBoolean(statement.getTransaction(), getVdDivers(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                                "vdDivers"));

        statement.writeField("IKDPRD", _dbWriteString(statement.getTransaction(), getVdDebPeriode(), "vdDebPeriode"));
        statement.writeField("IKFPRD", _dbWriteString(statement.getTransaction(), getVdFinPeriode(), "vdFinPeriode"));
        statement.writeField("IKADCH", _dbWriteString(statement.getTransaction(), getVdAddChez(), "vdAddChez"));
        statement.writeField("IKADRU", _dbWriteString(statement.getTransaction(), getVdAddRue(), "vdAddRue"));
        statement.writeField("IKNLOC", _dbWriteString(statement.getTransaction(), getVdNumLocalite(), "vdNumLocalite"));
        statement.writeField("IKDATN",
                _dbWriteString(statement.getTransaction(), getVdDatNaissance(), "vdDatNaissance"));
        statement.writeField("IKACTB", _dbWriteString(statement.getTransaction(), getVdAssujCtb(), "vdAssujCtb"));
        statement.writeField("IKPAOB", _dbWriteString(statement.getTransaction(), vdPenAliObtenue, "vdPenAliObtenue"));
        statement.writeField("IKDHAB",
                _dbWriteString(statement.getTransaction(), vdDroitHabitation, "vdDroitHabitation"));
        statement.writeField("IKTTAX", _dbWriteString(statement.getTransaction(), getVdTypeTax(), "vdTypeTax"));
        statement.writeField("IKDASS", _dbWriteString(statement.getTransaction(), getVdDebAssuj(), "vdDebAssuj"));
        statement.writeField("IKFASS", _dbWriteString(statement.getTransaction(), getVdFinAssuj(), "vdFinAssuj"));
        statement.writeField("IKCINV", _dbWriteNumeric(statement.getTransaction(), vdCapInvesti, "vdCapInvesti"));
        statement.writeField("IKDATC",
                _dbWriteString(statement.getTransaction(), vdDatDetCapInvesti, "vdDatDetCapInvesti"));
        statement.writeField("IKRAIN", _dbWriteNumeric(statement.getTransaction(), vdRevActInd, "vdRevActInd"));
        statement.writeField("IKGIPR", _dbWriteString(statement.getTransaction(), vdGIprof, "vdGIprof"));
        statement
                .writeField("IKEXLI", _dbWriteString(statement.getTransaction(), vdExcesLiquidite, "vdExcesLiquidite"));
        statement.writeField("IKSVCJ", _dbWriteNumeric(statement.getTransaction(), vdSalVerseCjt, "vdSalVerseCjt"));
        statement.writeField("IKNACO", _dbWriteString(statement.getTransaction(), getVdNatureComm(), "vdNatureComm"));
        statement.writeField("IKSACO",
                _dbWriteNumeric(statement.getTransaction(), vdSalaireCotisant, "vdSalaireCotisant"));
        statement.writeField("IKFOIM",
                _dbWriteNumeric(statement.getTransaction(), vdFortuneImposable, "vdFortuneImposable"));
        statement.writeField("IKDDFO",
                _dbWriteString(statement.getTransaction(), getVdDateDetFortune(), "vdDateDetFortune"));
        statement.writeField("IKMORE", _dbWriteNumeric(statement.getTransaction(), vdMontantRente, "vdMontantRente"));
        statement.writeField("IKMALU", _dbWriteString(statement.getTransaction(), vdMontantActLuc, "vdMontantActLuc"));
        statement.writeField("IKCORE",
                _dbWriteString(statement.getTransaction(), getVdCodeNatureRente(), "vdCodeNatureRente"));
        statement.writeField("IKRENE", _dbWriteNumeric(statement.getTransaction(), vdRevenuNet, "vdRevenuNet"));
        statement.writeField("IKCOMM", _dbWriteString(statement.getTransaction(), getVdCommentaire(), "vdCommentaire"));
        statement
                .writeField(
                        "IKRALU",
                        _dbWriteNumeric(statement.getTransaction(), vdRevenuActivitesLucratives,
                                "vdRevenuActivitesLucratives"));
        statement.writeField("IKIMDE",
                _dbWriteNumeric(statement.getTransaction(), getVdImpositionDepense(), "vdImpositionDepense"));
        statement
                .writeField("IKDDEM", _dbWriteDateAMJ(statement.getTransaction(), getVdDateDemande(), "vdDateDemande"));
    }

    @Override
    public String getDescription(int cas) {
        description = "";
        if (cas == 0) {
            if (getVdNumContribuable().length() > 0) {
                description += getVdNumContribuable() + "                            " + getVdDatNaissance() + "\n";
            }
            if (getVdNumAffilie().length() > 0) {
                description += getSession().getLabel("NUM_AFFILIE") + " : " + getVdNumAffilie() + "\n";
            }
            if (getVdNomPrenom().length() > 0) {
                description += getVdNomPrenom() + "\n";
            }
            if (getVdAddChez().length() > 0) {
                description += getVdAddChez() + "\n";
            }
            if (getVdAddRue().length() > 0) {
                description += getVdAddRue() + "\n";
            }
            if (getVdNumLocalite().length() > 0) {
                description += getVdNumLocalite() + "\n";
            }
        } else if (cas == 1) {
            description = getVdNumContribuable();
        } else if (cas == 2) {
            description = getVdNumAffilie();
        } else if (cas == 3) {
            description = getVdNumAvs();
        }
        return description;
    }

    @Override
    public String getNumAvs() {
        return getVdNumAvs();
    }

    public String getNumeroAvs() {
        return getVdNumAvs();
    }

    private String getValeurRechercheBD() {
        try {
            if (getJournalRetour().getZoneRecherche().equalsIgnoreCase("numAffilie")) {
                return getVdNumAffilie();
            } else if (getJournalRetour().getZoneRecherche().equalsIgnoreCase("numAvs")) {
                return getVdNumAvs();
            } else {
                return getVdNumContribuable();
            }
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String getValeurRechercheBD(String zoneRecherche) {
        try {
            if (zoneRecherche.equalsIgnoreCase("numAffilie")) {
                return getVdNumAffilie();
            } else if (zoneRecherche.equalsIgnoreCase("numAvs")) {
                return getVdNumAvs();
            } else {
                return getVdNumContribuable();
            }
        } catch (Exception e) {
            return "";
        }
    }

    public String getVdAddChez() {
        return vdAddChez;
    }

    public String getVdAddRue() {
        return vdAddRue;
    }

    public String getVdAssujCtb() {
        return vdAssujCtb;
    }

    /**
     * @return
     */
    public Boolean getVdBourses() {
        return vdBourses;
    }

    public String getVdCapInvesti() {
        return JANumberFormatter.fmt(vdCapInvesti, true, false, true, 2);
    }

    public String getVdCodeNatureRente() {
        return vdCodeNatureRente;
    }

    public String getVdCommentaire() {
        return vdCommentaire;
    }

    public String getVdDatDetCapInvesti() {
        return vdDatDetCapInvesti;
    }

    public String getVdDateDemande() {
        return vdDateDemande;
    }

    public String getVdDateDetFortune() {
        return vdDateDetFortune;
    }

    public String getVdDatNaissance() {
        return vdDatNaissance;
    }

    public String getVdDebAssuj() {
        return vdDebAssuj;
    }

    public String getVdDebPeriode() {
        return vdDebPeriode;
    }

    /**
     * @return
     */
    public Boolean getVdDivers() {
        return vdDivers;
    }

    public String getVdDroitHabitation() {
        return vdDroitHabitation;
    }

    public String getVdExcesLiquidite() {
        return vdExcesLiquidite;
    }

    public String getVdFinAssuj() {
        return vdFinAssuj;
    }

    public String getVdFinPeriode() {
        return vdFinPeriode;
    }

    public String getVdFortuneImposable() {
        return JANumberFormatter.fmt(vdFortuneImposable, true, false, true, 2);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:25:35)
     * 
     * @return java.lang.String
     */
    public java.lang.String getVdGenreAffilie() {
        return vdGenreAffilie;
    }

    public String getVdGIprof() {
        return vdGIprof;
    }

    public String getVdImpositionDepense() {
        return vdImpositionDepense;
    }

    /**
     * @return
     */
    public Boolean getVdIndemniteJournaliere() {
        return vdIndemniteJournaliere;
    }

    public String getVdMontantActLuc() {
        return vdMontantActLuc;
    }

    public String getVdMontantRente() {
        return JANumberFormatter.fmt(vdMontantRente, true, false, true, 2);
    }

    public String getVdNatureComm() {
        return vdNatureComm;
    }

    @Override
    public java.lang.String getVdNomPrenom() {
        return vdNomPrenom;
    }

    /**
     * @return
     */
    @Override
    public java.lang.String getVdNumAffilie() {
        return vdNumAffilie;
    }

    public java.lang.String getVdNumAvs() {
        return vdNumAvs;
    }

    /**
     * @return
     */
    public java.lang.String getVdNumCaisse() {
        return vdNumCaisse;
    }

    /**
     * @return
     */
    @Override
    public java.lang.String getVdNumContribuable() {
        return vdNumContribuable;
    }

    public java.lang.String getVdNumDemande() {
        return vdNumDemande;
    }

    public String getVdNumLocalite() {
        return vdNumLocalite;
    }

    public String getVdPenAliObtenue() {
        return JANumberFormatter.fmt(vdPenAliObtenue, true, false, true, 2);
    }

    /**
     * @return
     */
    public Boolean getVdPension() {
        return vdPension;
    }

    /**
     * @return
     */
    public Boolean getVdPensionAlimentaire() {
        return vdPensionAlimentaire;
    }

    /**
     * @return
     */
    public Boolean getVdRenteInvalidite() {
        return vdRenteInvalidite;
    }

    /**
     * @return
     */
    public Boolean getVdRenteViagere() {
        return vdRenteViagere;
    }

    /**
     * @return
     */
    public Boolean getVdRenteVieillesse() {
        return vdRenteVieillesse;
    }

    /**
     * @return
     */
    public Boolean getVdRetraite() {
        return vdRetraite;
    }

    public String getVdRevActInd() {
        return JANumberFormatter.fmt(vdRevActInd, true, false, true, 2);
    }

    public String getVdRevenuActivitesLucratives() {
        return JANumberFormatter.fmt(vdRevenuActivitesLucratives, true, false, true, 2);
    }

    public String getVdRevenuNet() {
        return JANumberFormatter.fmt(vdRevenuNet, true, false, true, 2);
    }

    public String getVdRevPCI() {
        return vdRevActInd;
    }

    public String getVdSalaireCotisant() {
        return JANumberFormatter.fmt(vdSalaireCotisant, true, false, true, 2);
    }

    public String getVdSalVerseCjt() {
        return JANumberFormatter.fmt(vdSalVerseCjt, true, false, true, 2);
    }

    public String getVdTypeTax() {
        return vdTypeTax;
    }

    @Override
    public boolean isForBackup() {
        return super.isForBackup();
    }

    public void setVdAddChez(String vdAddChez) {
        this.vdAddChez = vdAddChez;
    }

    public void setVdAddRue(String vdAddRue) {
        this.vdAddRue = vdAddRue;
    }

    public void setVdAssujCtb(String vdAssujCtb) {
        this.vdAssujCtb = vdAssujCtb;
    }

    /**
     * @param boolean1
     */
    public void setVdBourses(Boolean boolean1) {
        vdBourses = boolean1;
    }

    public void setVdCapInvesti(String vdCapInvesti) {
        this.vdCapInvesti = vdCapInvesti;
    }

    public void setVdCodeNatureRente(String vdCodeNatureRente) {
        this.vdCodeNatureRente = vdCodeNatureRente;
    }

    public void setVdCommentaire(String vdCommentaire) {
        this.vdCommentaire = vdCommentaire;
    }

    public void setVdDatDetCapInvesti(String vdDatDetCapInvesti) {
        this.vdDatDetCapInvesti = vdDatDetCapInvesti;
    }

    public void setVdDateDemande(String vdDateDemande) {
        this.vdDateDemande = vdDateDemande;
    }

    public void setVdDateDetFortune(String vdDateDetFortune) {
        this.vdDateDetFortune = vdDateDetFortune;
    }

    public void setVdDatNaissance(String vdDatNaissance) {
        this.vdDatNaissance = vdDatNaissance;
    }

    public void setVdDebAssuj(String vdDebAssuj) {
        this.vdDebAssuj = vdDebAssuj;
    }

    public void setVdDebPeriode(String vdDebPeriode) {
        this.vdDebPeriode = vdDebPeriode;
    }

    /**
     * @param boolean1
     */
    public void setVdDivers(Boolean boolean1) {
        vdDivers = boolean1;
    }

    public void setVdDroitHabitation(String vdDroitHabitation) {
        this.vdDroitHabitation = vdDroitHabitation;
    }

    public void setVdExcesLiquidite(String vdExcesLiquidite) {
        this.vdExcesLiquidite = vdExcesLiquidite;
    }

    public void setVdFinAssuj(String vdFinAssuj) {
        this.vdFinAssuj = vdFinAssuj;
    }

    public void setVdFinPeriode(String vdFinPeriode) {
        this.vdFinPeriode = vdFinPeriode;
    }

    public void setVdFortuneImposable(String vdFortuneImposable) {
        this.vdFortuneImposable = vdFortuneImposable;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:25:35)
     * 
     * @param newGenreAffilie
     *            java.lang.String
     */
    public void setVdGenreAffilie(java.lang.String newGenreAffilie) {
        vdGenreAffilie = newGenreAffilie;
    }

    public void setVdGIprof(String vdGIprof) {
        this.vdGIprof = vdGIprof;
    }

    public void setVdImpositionDepense(String vdImpositionDepense) {
        this.vdImpositionDepense = vdImpositionDepense;
    }

    /**
     * @param boolean1
     */
    public void setVdIndemniteJournaliere(Boolean boolean1) {
        vdIndemniteJournaliere = boolean1;
    }

    public void setVdMontantActLuc(String vdMontantActLuc) {
        this.vdMontantActLuc = vdMontantActLuc;
    }

    public void setVdMontantRente(String vdMontantRente) {
        this.vdMontantRente = vdMontantRente;
    }

    public void setVdNatureComm(String vdNatureComm) {
        this.vdNatureComm = vdNatureComm;
    }

    @Override
    public void setVdNomPrenom(java.lang.String vdNomPrenom) {
        this.vdNomPrenom = vdNomPrenom;
    }

    /**
     * @param string
     */
    @Override
    public void setVdNumAffilie(java.lang.String string) {
        vdNumAffilie = string;
    }

    public void setVdNumAvs(java.lang.String string) {
        vdNumAvs = string;
    }

    /**
     * @param string
     */
    public void setVdNumCaisse(java.lang.String string) {
        vdNumCaisse = string;
    }

    /**
     * @param string
     */
    @Override
    public void setVdNumContribuable(java.lang.String string) {
        vdNumContribuable = string;
    }

    public void setVdNumDemande(java.lang.String string) {
        vdNumDemande = string;
    }

    public void setVdNumLocalite(String vdNumLocalite) {
        this.vdNumLocalite = vdNumLocalite;
    }

    public void setVdPenAliObtenue(String vdPenAliObtenue) {
        this.vdPenAliObtenue = vdPenAliObtenue;
    }

    /**
     * @param boolean1
     */
    public void setVdPension(Boolean boolean1) {
        vdPension = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setVdPensionAlimentaire(Boolean boolean1) {
        vdPensionAlimentaire = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setVdRenteInvalidite(Boolean boolean1) {
        vdRenteInvalidite = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setVdRenteViagere(Boolean boolean1) {
        vdRenteViagere = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setVdRenteVieillesse(Boolean boolean1) {
        vdRenteVieillesse = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setVdRetraite(Boolean boolean1) {
        vdRetraite = boolean1;
    }

    public void setVdRevActInd(String vdRevActInd) {
        this.vdRevActInd = vdRevActInd;
    }

    public void setVdRevenuActivitesLucratives(String vdRevenuActivitesLucratives) {
        this.vdRevenuActivitesLucratives = vdRevenuActivitesLucratives;
    }

    public void setVdRevenuNet(String vdRevenuNet) {
        this.vdRevenuNet = vdRevenuNet;
    }

    public void setVdSalaireCotisant(String vdSalaireCotisant) {
        this.vdSalaireCotisant = vdSalaireCotisant;
    }

    public void setVdSalVerseCjt(String vdSalVerseCjt) {
        this.vdSalVerseCjt = vdSalVerseCjt;
    }

    public void setVdTypeTax(String vdTypeTax) {
        this.vdTypeTax = vdTypeTax;
    }
}
