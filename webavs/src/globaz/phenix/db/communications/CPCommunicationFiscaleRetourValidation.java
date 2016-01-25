package globaz.phenix.db.communications;

import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.principale.CPDecision;
import globaz.pyxis.db.tiers.TITiersViewBean;

public class CPCommunicationFiscaleRetourValidation extends CPCommunicationFiscaleRetourViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeDecision = "";
    private String capital = "";
    private String capitalEnCours = "";
    private String codeValidation = "";
    private String cotisation1 = "";
    private String cotisation1EnCours = "";
    private String dateCalcul = "";
    private String dateNaissance = "";
    // Conjoint
    private String dateNaissanceConjoint = "";
    // Affiliation conjoint
    private String debutAffiliationConjoint = "";
    // Affiliation tiers
    private String debutAffiliationTiers = "";
    private String debutDecision = "";
    private String debutDecisionEnCours = "";
    private Boolean div2 = Boolean.FALSE;
    private Boolean div2EnCours = Boolean.FALSE;
    private String etatCivil = "";
    private String etatCivilConjoint = "";
    private String finAffiliationConjoint = "";
    private String finAffiliationTiers = "";
    private String finDecision = "";
    private String finDecisionEnCours = "";
    private String fortuneTotale = "";
    private String fortuneTotaleEnCours = "";
    private String genreAffilieDecision = "";

    // Décision de base (dernière prise pour l'année de la communication)
    private String genreAffilieEnCours = "";

    private String groupeExtraction = "";

    private String groupeTaxation = "";
    private String idCjt = "";
    private String idCjtEnCours = "";
    private String idDecision = "";
    private String idPassage = "";
    private String idDecisionProvisoire = "";
    private String idJournal = "";
    private String idRetours = "";
    // Tiers
    private String idTiers = "";
    // Décision selon communication
    private String idTiersDecision = "";
    private String idValidation = "";
    private String numAffiliationConjoint = "";
    private String numAVSActuel = "";
    private String numAVSConjoint = "";
    private String numffiliationTiers = "";
    private String revenu1 = "";
    private String revenu1EnCours = "";
    private String revenuAutre1 = "";
    private String revenuAutre1EnCours = "";
    private String sexe = "";
    private String sexeConjoint = "";
    private String typeDecision = "";
    private String typeDecisionEnCours = "";

    /**
     * Commentaire relatif au constructeur CPDonneesBase
     */
    public CPCommunicationFiscaleRetourValidation() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdRetour(this._incCounter(transaction, "0", "CPCRETP", "0", "0"));
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String table1 = "CPVCCOP";
        String table2 = "CPCRETP";
        return _getCollection() + table1 + " INNER JOIN " + _getCollection() + table2 + " ON (" + _getCollection()
                + table1 + ".IKIRET=" + _getCollection() + table2 + ".IKIRET)";
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPVCCOP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idValidation = statement.dbReadString("ILITCO");
        idRetours = statement.dbReadString("IKIRET");
        codeValidation = statement.dbReadString("ILBVAL");
        dateCalcul = statement.dbReadDateAMJ("ILDCAL");
        groupeExtraction = statement.dbReadString("IXIDPA");
        groupeTaxation = statement.dbReadString("ILGPTA");
        // Tiers
        idTiers = statement.dbReadString("HTITIE");
        dateNaissance = statement.dbReadDateAMJ("HPDNAI");
        numAVSActuel = statement.dbReadString("HXNAVS");
        etatCivil = statement.dbReadString("HPTETC");
        sexe = statement.dbReadString("HPTSEX");
        // Affiliation tiers
        debutAffiliationTiers = statement.dbReadDateAMJ("MADDEB");
        finAffiliationTiers = statement.dbReadDateAMJ("MADFIN");
        numffiliationTiers = statement.dbReadString("MALNAF");
        // Conjoint
        dateNaissanceConjoint = statement.dbReadDateAMJ("NAICJT");
        numAVSConjoint = statement.dbReadString("AVSCJT");
        etatCivilConjoint = statement.dbReadString("ETCCJT");
        sexeConjoint = statement.dbReadString("SEXCJT");
        // Affiliation conjoint
        debutAffiliationConjoint = statement.dbReadDateAMJ("DEBCJT");
        finAffiliationConjoint = statement.dbReadDateAMJ("FINCJT");
        numAffiliationConjoint = statement.dbReadString("NAFCJT");
        // Décision selon communication
        idTiersDecision = statement.dbReadString("IDTDEC");
        idDecision = statement.dbReadString("IAIDEC");
        idPassage = statement.dbReadString("EBIPAS");
        anneeDecision = statement.dbReadString("ANNEC");
        genreAffilieDecision = statement.dbReadString("TGAFC");
        typeDecision = statement.dbReadString("TTDEC");
        debutDecision = statement.dbReadDateAMJ("DDEBC");
        finDecision = statement.dbReadDateAMJ("DFINC");
        // Données de la décision selon communication
        revenu1 = statement.dbReadString("REV1C");
        revenuAutre1 = statement.dbReadString("RAU1C");
        cotisation1 = statement.dbReadString("COT1C");
        capital = statement.dbReadString("CAPIC");
        fortuneTotale = statement.dbReadString("FORTC");
        // Décision de base (dernière prise pour l'année de la communication)
        idDecisionProvisoire = statement.dbReadString("ILIDEP");
        genreAffilieEnCours = statement.dbReadString("TGAFP");
        typeDecisionEnCours = statement.dbReadString("TTDEP");
        debutDecisionEnCours = statement.dbReadDateAMJ("DDEBP");
        finDecisionEnCours = statement.dbReadDateAMJ("DFINP");
        // Données selon décision de base (dernière prise pour l'année de la
        // communication)
        revenu1EnCours = statement.dbReadString("REV1P");
        revenuAutre1EnCours = statement.dbReadString("RAU1P");
        cotisation1EnCours = statement.dbReadString("COT1P");
        capitalEnCours = statement.dbReadString("CAPIP");
        fortuneTotaleEnCours = statement.dbReadString("FORTP");
        div2 = statement.dbReadBoolean("DIVP");
        div2EnCours = statement.dbReadBoolean("DIVC");
        idCjt = statement.dbReadString("CJTP");
        idCjtEnCours = statement.dbReadString("CJTC");
        if (statement.dbReadString("IWRJOU") != null) {
            idJournal = statement.dbReadString("IWRJOU");
        }
        super._readProperties(statement);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires) Les reception sont toujours chargée, elle
     * seront validées par les plausibilités
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(_getBaseTable() + "ILITCO",
                this._dbWriteNumeric(statement.getTransaction(), getIdValidation(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        if (_getAction() == BEntity.ACTION_COPY) {
            super._writeProperties(statement);
        }
        statement.writeField("ILITCO",
                this._dbWriteNumeric(statement.getTransaction(), getIdValidation(), "idValidation"));
    }

    public String getAnneeDecision() {
        return anneeDecision;
    }

    public String getAnneeDeLaDecision() {
        if ((idDecision.length() > 0) && !idDecision.equals("0")) {
            CPDecision decision = new CPDecision();
            decision.setSession(getSession());
            decision.setIdDecision(idDecision);
            try {
                decision.retrieve();
            } catch (Exception e) {
                return "";
            }
            return decision.getAnneeDecision();
        } else {
            return "";
        }
    }

    @Override
    public String getCapital() {
        return capital;
    }

    public String getCapitalEnCours() {
        return capitalEnCours;
    }

    public String getCodeValidation() {
        return codeValidation;
    }

    @Override
    public String getCotisation1() {
        return cotisation1;
    }

    public String getCotisation1EnCours() {
        return cotisation1EnCours;
    }

    public String getDateCalcul() {
        return dateCalcul;
    }

    @Override
    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDateNaissanceConjoint() {
        return dateNaissanceConjoint;
    }

    public String getDebutAffiliationConjoint() {
        return debutAffiliationConjoint;
    }

    public String getDebutAffiliationTiers() {
        return debutAffiliationTiers;
    }

    public String getDebutDecision() {
        return debutDecision;
    }

    public String getDebutDecisionEnCours() {
        return debutDecisionEnCours;
    }

    public Boolean getDiv2() {
        return div2;
    }

    public Boolean getDiv2EnCours() {
        return div2EnCours;
    }

    @Override
    public String getEtatCivil() {
        return etatCivil;
    }

    public String getEtatCivilConjoint() {
        return etatCivilConjoint;
    }

    public String getFinAffiliationConjoint() {
        return finAffiliationConjoint;
    }

    public String getFinAffiliationTiers() {
        return finAffiliationTiers;
    }

    public String getFinDecision() {
        return finDecision;
    }

    public String getFinDecisionEnCours() {
        return finDecisionEnCours;
    }

    public String getFortuneTotale() {
        return fortuneTotale;
    }

    public String getFortuneTotaleEnCours() {
        return fortuneTotaleEnCours;
    }

    public String getGenreAffilieDecision() {
        return genreAffilieDecision;
    }

    public String getGenreAffilieEnCours() {
        return genreAffilieEnCours;
    }

    public String getGroupeExtraction() {
        return groupeExtraction;
    }

    public String getGroupeTaxation() {
        return groupeTaxation;
    }

    public String getIdCjt() {
        return idCjt;
    }

    public String getIdCjtEnCours() {
        return idCjtEnCours;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdPassage() {
        return idPassage;
    }

    public String getIdDecisionProvisoire() {
        return idDecisionProvisoire;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdRetours() {
        return idRetours;
    }

    @Override
    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersDecision() {
        return idTiersDecision;
    }

    public java.lang.String getIdValidation() {
        return idValidation;
    }

    public String getLibelleTaxation() {
        return getSession().getCodeLibelle(groupeTaxation);
    }

    public String getNomDecision() {
        if ((idTiersDecision.length() > 0) && !idTiersDecision.equals("0")) {
            TITiersViewBean pers = new TITiersViewBean();
            pers.setSession(getSession());
            pers.setIdTiers(idTiersDecision);
            try {
                pers.retrieve();
                return pers.getPrenomNom();
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public String getNumAffiliationConjoint() {
        return numAffiliationConjoint;
    }

    public String getNumAVSActuel() {
        return numAVSActuel;
    }

    public String getNumAVSConjoint() {
        return numAVSConjoint;
    }

    public String getNumffiliationTiers() {
        return numffiliationTiers;
    }

    @Override
    public String getRevenu1() {
        return revenu1;
    }

    public String getRevenu1EnCours() {
        return revenu1EnCours;
    }

    public String getRevenuAutre1() {
        return revenuAutre1;
    }

    public String getRevenuAutre1EnCours() {
        return revenuAutre1EnCours;
    }

    public String getSexe() {
        return sexe;
    }

    public String getSexeConjoint() {
        return sexeConjoint;
    }

    public String getTypeDecision() {
        return typeDecision;
    }

    public String getTypeDecisionEnCours() {
        return typeDecisionEnCours;
    }

    /**
     * 07.09.2007: Cette méthode retourne si la décision est de genre non actif comme provisoire au point de vue métier
     * 
     * @param myDecision
     * @return
     */
    @Override
    public boolean isNonActif(boolean traitementConjoint) {
        if ((CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getGenreAffilie()) || CPDecision.CS_ETUDIANT
                .equalsIgnoreCase(getGenreAffilie())) && !traitementConjoint) {
            return true;
        }
        if (JadeStringUtil.isNull(getGenreAffilie()) || JadeStringUtil.isBlankOrZero(getGenreAffilie())) {
            if ((CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getGenreConjoint()) || CPDecision.CS_ETUDIANT
                    .equalsIgnoreCase(getGenreConjoint())) && traitementConjoint) {
                return true;
            }
        }
        if (!JadeStringUtil.isNull(getGenreAffilieDecision())) {
            if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getGenreAffilieDecision())
                    || CPDecision.CS_ETUDIANT.equalsIgnoreCase(getGenreAffilieDecision())) {
                return true;
            }
        }
        return false;
    }

    public void setAnneeDecision(String anneeDecision) {
        this.anneeDecision = anneeDecision;
    }

    @Override
    public void setCapital(String capital) {
        this.capital = capital;
    }

    public void setCapitalEnCours(String capitalEnCours) {
        this.capitalEnCours = capitalEnCours;
    }

    public void setCodeValidation(String codeValidation) {
        this.codeValidation = codeValidation;
    }

    @Override
    public void setCotisation1(String cotisation1) {
        this.cotisation1 = cotisation1;
    }

    public void setCotisation1EnCours(String cotisation1EnCours) {
        this.cotisation1EnCours = cotisation1EnCours;
    }

    public void setDateCalcul(String dateCalcul) {
        this.dateCalcul = dateCalcul;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDateNaissanceConjoint(String dateNaissanceConjoint) {
        this.dateNaissanceConjoint = dateNaissanceConjoint;
    }

    public void setDebutAffiliationConjoint(String debutAffiliationConjoint) {
        this.debutAffiliationConjoint = debutAffiliationConjoint;
    }

    public void setDebutAffiliationTiers(String debutAffiliationTiers) {
        this.debutAffiliationTiers = debutAffiliationTiers;
    }

    public void setDebutDecision(String debutDecision) {
        this.debutDecision = debutDecision;
    }

    public void setDebutDecisionEnCours(String debutDecisionEnCours) {
        this.debutDecisionEnCours = debutDecisionEnCours;
    }

    public void setDiv2(Boolean div2) {
        this.div2 = div2;
    }

    public void setDiv2EnCours(Boolean div2EnCours) {
        this.div2EnCours = div2EnCours;
    }

    public void setEtatCivil(String etatCivil) {
        this.etatCivil = etatCivil;
    }

    public void setEtatCivilConjoint(String etatCivilConjoint) {
        this.etatCivilConjoint = etatCivilConjoint;
    }

    public void setFinAffiliationConjoint(String finAffiliationConjoint) {
        this.finAffiliationConjoint = finAffiliationConjoint;
    }

    public void setFinAffiliationTiers(String finAffiliationTiers) {
        this.finAffiliationTiers = finAffiliationTiers;
    }

    public void setFinDecision(String finDecision) {
        this.finDecision = finDecision;
    }

    public void setFinDecisionEnCours(String finDecisionEnCours) {
        this.finDecisionEnCours = finDecisionEnCours;
    }

    public void setFortuneTotale(String fortuneTotale) {
        this.fortuneTotale = fortuneTotale;
    }

    public void setFortuneTotaleEnCours(String fortuneTotaleEnCours) {
        this.fortuneTotaleEnCours = fortuneTotaleEnCours;
    }

    public void setGenreAffilieDecision(String genreAffilieDecision) {
        this.genreAffilieDecision = genreAffilieDecision;
    }

    public void setGenreAffilieEnCours(String genreAffilieEnCours) {
        this.genreAffilieEnCours = genreAffilieEnCours;
    }

    public void setGroupeExtraction(String groupeExtraction) {
        this.groupeExtraction = groupeExtraction;
    }

    public void setGroupeTaxation(String groupeTaxation) {
        this.groupeTaxation = groupeTaxation;
    }

    public void setIdCjt(String idCjt) {
        this.idCjt = idCjt;
    }

    public void setIdCjtEnCours(String idCjtEnCours) {
        this.idCjtEnCours = idCjtEnCours;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    public void setIdDecisionProvisoire(String idDecisionProvisoire) {
        this.idDecisionProvisoire = idDecisionProvisoire;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setIdRetours(String idRetours) {
        this.idRetours = idRetours;
    }

    @Override
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersDecision(String idTiersDecision) {
        this.idTiersDecision = idTiersDecision;
    }

    public void setIdValidation(java.lang.String idValidation) {
        this.idValidation = idValidation;
    }

    public void setNumAffiliationConjoint(String numffiliationconjoint) {
        numAffiliationConjoint = numffiliationconjoint;
    }

    public void setNumAVSActuel(String numAVSActuel) {
        this.numAVSActuel = numAVSActuel;
    }

    public void setNumAVSConjoint(String numAVSConjoint) {
        this.numAVSConjoint = numAVSConjoint;
    }

    public void setNumffiliationTiers(String numffiliationTiers) {
        this.numffiliationTiers = numffiliationTiers;
    }

    @Override
    public void setRevenu1(String revenu1) {
        this.revenu1 = revenu1;
    }

    public void setRevenu1EnCours(String revenu1EnCours) {
        this.revenu1EnCours = revenu1EnCours;
    }

    public void setRevenuAutre1(String revenuAutre1) {
        this.revenuAutre1 = revenuAutre1;
    }

    public void setRevenuAutre1EnCours(String revenuAutre1EnCours) {
        this.revenuAutre1EnCours = revenuAutre1EnCours;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setSexeConjoint(String sexeConjoint) {
        this.sexeConjoint = sexeConjoint;
    }

    public void setTypeDecision(String typeDecision) {
        this.typeDecision = typeDecision;
    }

    public void setTypeDecisionEnCours(String typeDecisionEnCours) {
        this.typeDecisionEnCours = typeDecisionEnCours;
    }
}
