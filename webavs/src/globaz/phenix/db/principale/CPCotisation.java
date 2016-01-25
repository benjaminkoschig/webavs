package globaz.phenix.db.principale;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.translation.CodeSystem;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class CPCotisation extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Retourne la décision pour une affiliation pour une année et un genre
     * 
     * @param BSession
     *            session, idAffiliation String, anneeDecision String, genreDecision String
     */
    static public CPCotisation _returnCotisation(BSession session, String idDecision, String genreCotisation)
            throws java.lang.Exception {
        CPCotisationManager cotiManager = new CPCotisationManager();
        cotiManager.setSession(session);
        cotiManager.setForIdDecision(idDecision);
        cotiManager.setForGenreCotisation(genreCotisation);
        cotiManager.find();
        if (cotiManager.size() > 0) {
            return (CPCotisation) cotiManager.getEntity(0);
        } else {
            return null;
        }
    }

    /**
     * Retourne le montant total pour la periodicité choisie des cotisations pour un genre d'assurance
     * 
     * @param BSession
     *            session
     * @param String
     *            idDecision
     * @param String
     *            periodicite (annuelle, trimestrielle...)
     * @return String montantTotal
     */
    public static String getTotalCotisation(BSession session, String idDecision, String periodicite) {
        float cumul = 0;
        String totalCoti = "";
        try {
            CPCotisationManager cotiManager = new CPCotisationManager();
            cotiManager.setSession(session);
            cotiManager.setForIdDecision(idDecision);
            cotiManager.find();
            for (int i = 0; i < cotiManager.getSize(); i++) {
                CPCotisation coti = (CPCotisation) cotiManager.getEntity(i);
                if (periodicite.equalsIgnoreCase(CodeSystem.PERIODICITE_ANNUELLE)) {
                    totalCoti = JANumberFormatter.deQuote(coti.getMontantAnnuel());
                } else if (periodicite.equalsIgnoreCase(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
                    totalCoti = JANumberFormatter.deQuote(coti.getMontantTrimestriel());
                } else {
                    totalCoti = JANumberFormatter.deQuote(coti.getMontantAnnuel());
                }
                if (!JadeStringUtil.isEmpty(totalCoti)) {
                    cumul = new FWCurrency(cumul + Float.parseFloat(totalCoti)).floatValue();
                }
            }
            return JANumberFormatter.fmt(Float.toString(cumul), true, false, false, 2);
        } catch (Exception e) {
            return "";
        }

    }

    private Boolean aForceAZero = new Boolean(false);
    private AFCotisation cotiAffiliation = null;
    /** (ISBPRO) */
    private Boolean cotisationMinimum = new Boolean(false);
    /** (ISMTAU) */
    private String debutCotisation = "";
    /** (ISBMIN) */
    //
    private CPDecision decision = null;
    /** (ISDDCO) */
    private String finCotisation = "";
    /** (IAIDEC) */
    private String genreCotisation = "";
    /** (ISTGCO) */
    private String idCotiAffiliation = "";
    /** Fichier CPCOTIP */
    private String idCotisation = "";
    /** (ISICOT) */
    private String idDecision = "";
    /** (ISMCSE) */
    private String montantAnnuel = "";
    /** (ISTPER) */
    private String montantFacture = "";
    /** (MEICOT) */
    private String montantMensuel = "";
    /** (ISMCTR) */
    private String montantSemestriel = "";
    /** (ISMCME) */
    private String montantTrimestriel = "";

    /** (ISDFCO) */
    private String periodicite = "";

    /** (ISMCFA) */
    private Boolean prorata = new Boolean(false);

    /** (ISMCAN) */
    private String taux = "";

    // code systeme
    /**
     * Commentaire relatif au constructeur CPCotisation
     */
    public CPCotisation() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdCotisation(this._incCounter(transaction, idCotisation));

    }

    public String _getGenreCotisation() {
        return genreCotisation;
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPCOTIP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idCotisation = statement.dbReadNumeric("ISICOT");
        idCotiAffiliation = statement.dbReadNumeric("MEICOT");
        idDecision = statement.dbReadNumeric("IAIDEC");
        genreCotisation = statement.dbReadNumeric("ISTGCO");
        montantMensuel = statement.dbReadNumeric("ISMCME", 2);
        montantTrimestriel = statement.dbReadNumeric("ISMCTR", 2);
        montantSemestriel = statement.dbReadNumeric("ISMCSE", 2);
        montantAnnuel = statement.dbReadNumeric("ISMCAN", 2);
        taux = statement.dbReadNumeric("ISMTAU", 5);
        debutCotisation = statement.dbReadDateAMJ("ISDDCO");
        finCotisation = statement.dbReadDateAMJ("ISDFCO");
        periodicite = statement.dbReadNumeric("ISTPER");
        montantFacture = statement.dbReadNumeric("ISMCFA", 2);
        prorata = statement.dbReadBoolean("ISIPRO");
        aForceAZero = statement.dbReadBoolean("ISBZER");
        cotisationMinimum = statement.dbReadBoolean("ISBMIN");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) {
        // La date de fin doit être supérieur ou égal à celle de début
        // date :
        try {
            if ((!JAUtil.isDateEmpty(getDebutCotisation())) && (!JAUtil.isDateEmpty(getFinCotisation()))) {
                if (!globaz.globall.db.BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDebutCotisation(),
                        getFinCotisation())) {
                    _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0087"));
                }
                // La période de la cotisation doit être comprise dans celle de
                // la décision
                // et dans celle de l'assurance
                // Lecture de la décision
                if (decision == null) {
                    CPDecision decis = new CPDecision();
                    decis.setSession(getSession());
                    decis.setIdDecision(getIdDecision());
                    decis.retrieve();
                    if (!decis.isNew()) {
                        setDecision(decis);
                    }
                }
                // La date de début de la cotisation ne doit pas être inférieure
                // à celle de début de la décision
                // PO 2036 - Test supprimé par rapport à la FER qui radie
                // uniquement une assurance => permet de rembourser
                /*
                 * if (globaz.globall.db.BSessionUtil.compareDateFirstLower(getSession (), getDebutCotisation(),
                 * decision.getDebutDecision())) { _addError(statement.getTransaction(),
                 * getSession().getLabel("CP_MSG_0088")); }
                 */
                // La date de fin de la cotisation ne doit pas être supérieure à
                // celle de fin de la décision
                if (BSessionUtil.compareDateFirstGreater(getSession(), getFinCotisation(), decision.getFinDecision())) {
                    _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0089"));
                }
                // Lecture de l'assurance de l'affiliation
                if (cotiAffiliation == null) {
                    AFCotisation cotiAf = new AFCotisation();
                    cotiAf.setSession(getSession());
                    cotiAf.setCotisationId(getIdCotiAffiliation());
                    cotiAf.retrieve(statement.getTransaction());
                    if (!cotiAf.isNew() && (cotiAf != null)) {
                        setCotiAffiliation(cotiAf);
                    }
                }
                if (cotiAffiliation != null) {
                    // La date de début de la cotisation ne doit pas être
                    // inférieure à
                    // celle de début de l'assurance
                    if (BSessionUtil.compareDateFirstLower(getSession(), getDebutCotisation(),
                            cotiAffiliation.getDateDebut())) {
                        _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0090"));
                    }
                    // La date de fin de la cotisation ne doit pas être
                    // supérieure à celle de l'assurance
                    /*
                     * Test supprimé => PO 2036 if(!JAUtil.isDateEmpty(cotiAffiliation.getDateFin())){ if
                     * (globaz.globall.db.BSessionUtil.compareDateFirstGreater( getSession(), getFinCotisation(),
                     * cotiAffiliation.getDateFin())) { _addError(statement.getTransaction(),
                     * getSession().getLabel("CP_MSG_0091")); } }
                     */
                } else {
                    _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0092"));
                }
            }
        } catch (Exception e) {
            _addError(statement.getTransaction(), e.getMessage());
        }
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité composant une clé alternée
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     * @param alternateKey
     *            int le numéro de la clé alternée à utiliser
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        // Traitement par défaut : pas de clé alternée
        // Recherche par décisoin et genre de cotisation
        if (alternateKey == 1) {
            statement.writeKey(_getBaseTable() + "IAIDEC",
                    this._dbWriteNumeric(statement.getTransaction(), getIdDecision(), ""));
            statement.writeKey(_getBaseTable() + "MEICOT",
                    this._dbWriteNumeric(statement.getTransaction(), getIdCotiAffiliation(), ""));
        } else if (alternateKey == 2) {
            statement.writeKey(_getBaseTable() + "IAIDEC",
                    this._dbWriteNumeric(statement.getTransaction(), getIdDecision(), ""));
            statement.writeKey(_getBaseTable() + "ISTGCO",
                    this._dbWriteNumeric(statement.getTransaction(), genreCotisation, ""));
        }
    }

    /**

 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("ISICOT", this._dbWriteNumeric(statement.getTransaction(), getIdCotisation(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField("ISICOT",
                this._dbWriteNumeric(statement.getTransaction(), getIdCotisation(), "idCotisation"));
        statement.writeField("IAIDEC", this._dbWriteNumeric(statement.getTransaction(), getIdDecision(), "idDecision"));
        statement.writeField("MEICOT",
                this._dbWriteNumeric(statement.getTransaction(), getIdCotiAffiliation(), "idCotiAffiliation"));
        statement.writeField("ISTGCO",
                this._dbWriteNumeric(statement.getTransaction(), getGenreCotisation(), "genreCotisation"));
        statement.writeField("ISMCME",
                this._dbWriteNumeric(statement.getTransaction(), getMontantMensuel(), "montantMensuel"));
        statement.writeField("ISMCTR",
                this._dbWriteNumeric(statement.getTransaction(), getMontantTrimestriel(), "montantTrimestriel"));
        statement.writeField("ISMCSE",
                this._dbWriteNumeric(statement.getTransaction(), getMontantSemestriel(), "montantSemestriel"));
        statement.writeField("ISMCAN",
                this._dbWriteNumeric(statement.getTransaction(), getMontantAnnuel(), "montantAnnuel"));
        statement.writeField("ISMTAU", this._dbWriteNumeric(statement.getTransaction(), getTaux(), "taux"));
        statement.writeField("ISDDCO",
                this._dbWriteDateAMJ(statement.getTransaction(), getDebutCotisation(), "debutCotisation"));
        statement.writeField("ISDFCO",
                this._dbWriteDateAMJ(statement.getTransaction(), getFinCotisation(), "finCotisation"));
        statement.writeField("ISTPER",
                this._dbWriteNumeric(statement.getTransaction(), getPeriodicite(), "periodicite"));
        statement.writeField("ISMCFA",
                this._dbWriteNumeric(statement.getTransaction(), getMontantFacture(), "montantFacture"));
        statement.writeField("ISIPRO", this._dbWriteBoolean(statement.getTransaction(), getProrata(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "prorata"));
        statement.writeField("ISBZER", this._dbWriteBoolean(statement.getTransaction(), getAForceAZero(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "aForceAZero"));
        statement.writeField("ISBMIN", this._dbWriteBoolean(statement.getTransaction(), getCotisationMinimum(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "cotisationMinimum"));
    }

    public Boolean getAForceAZero() {
        return aForceAZero;
    }

    /**
     * Returns the cotiAffiliation.
     * 
     * @return AFCotisation
     */
    public AFCotisation getCotiAffiliation() {
        return cotiAffiliation;
    }

    public Boolean getCotisationMinimum() {
        return cotisationMinimum;
    }

    /**
     * Returns the debutCotisation.
     * 
     * @return java.lang.String
     */
    public java.lang.String getDebutCotisation() {
        return debutCotisation;
    }

    /**
     * Returns the decision.
     * 
     * @return CPDecision
     */
    public CPDecision getDecision() {
        return decision;
    }

    /**
     * Returns the finCotisation.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFinCotisation() {
        return finCotisation;
    }

    public String getGenreCotisation() {
        String typeAssurance = "";
        AFCotisation coti = new AFCotisation();
        coti.setSession(getSession());
        coti.setCotisationId(getIdCotiAffiliation());
        try {
            coti.retrieve();
            if ((coti != null) && !coti.isNew()) {
                AFAssurance assurance = coti.getAssurance();
                if ((assurance != null) && !assurance.isNew()) {
                    typeAssurance = assurance.getTypeAssurance();
                }
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        return typeAssurance;
    }

    public String getIdCotiAffiliation() {
        return idCotiAffiliation;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getIdCotisation() {
        return idCotisation;
    }

    public String getIdDecision() {
        return idDecision;
    }

    /**
     * Retourne le libellé de la périodicité d'une cotisation
     * 
     * @return String
     */
    public String getLibellePeriodicite() {
        try {
            return globaz.phenix.translation.CodeSystem.getLibelle(getSession(), getPeriodicite());
        } catch (Exception e) {
            return "";
        }
    }

    public String getMontantAnnuel() {
        return JANumberFormatter.fmt(montantAnnuel, true, false, false, 2);
    }

    /**
     * @return
     */
    public java.lang.String getMontantFacture() {
        return montantFacture;
    }

    public String getMontantMensuel() {
        return JANumberFormatter.fmt(montantMensuel, true, false, false, 2);
    }

    public String getMontantSemestriel() {
        return JANumberFormatter.fmt(montantSemestriel, true, false, false, 2);
    }

    public String getMontantTrimestriel() {
        return JANumberFormatter.fmt(montantTrimestriel, true, false, false, 2);
    }

    /**
     * Returns the periodicite.
     * 
     * @return java.lang.String
     */
    public java.lang.String getPeriodicite() {
        return periodicite;
    }

    /**
     * @return
     */
    public Boolean getProrata() {
        return prorata;
    }

    /**
     * Returns the taux.
     * 
     * @return java.lang.String
     */
    public java.lang.String getTaux() {
        DecimalFormat myFormatter = new DecimalFormat("###.#####");
        DecimalFormatSymbols symbol = new DecimalFormatSymbols();
        symbol.setDecimalSeparator('.');
        myFormatter.setDecimalFormatSymbols(symbol);
        if (!"".equalsIgnoreCase(taux)) {
            return myFormatter.format(Double.parseDouble(taux));
        } else {
            return "";
        }

        // return JANumberFormatter.fmt(taux.toString(), true, false, true, 3);
    }

    public void setAForceAZero(Boolean forceAZero) {
        aForceAZero = forceAZero;
    }

    /**
     * Sets the cotiAffiliation.
     * 
     * @param cotiAffiliation
     *            The cotiAffiliation to set
     */
    public void setCotiAffiliation(AFCotisation cotiAffiliation) {
        this.cotiAffiliation = cotiAffiliation;
    }

    public void setCotisationMinimum(Boolean cotisationMinimum) {
        this.cotisationMinimum = cotisationMinimum;
    }

    /**
     * Sets the debutCotisation.
     * 
     * @param debutCotisation
     *            The debutCotisation to set
     */
    public void setDebutCotisation(java.lang.String debutCotisation) {
        this.debutCotisation = debutCotisation;
    }

    /**
     * Sets the decision.
     * 
     * @param decision
     *            The decision to set
     */
    public void setDecision(CPDecision decision) {
        this.decision = decision;
    }

    /**
     * Sets the finCotisation.
     * 
     * @param finCotisation
     *            The finCotisation to set
     */
    public void setFinCotisation(java.lang.String finCotisation) {
        this.finCotisation = finCotisation;
    }

    public void setGenreCotisation(String newGenreCotisation) {
        genreCotisation = newGenreCotisation;
    }

    public void setIdCotiAffiliation(String newIdCotiAffiliation) {
        idCotiAffiliation = newIdCotiAffiliation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    public void setIdCotisation(String newIdCotisation) {
        idCotisation = newIdCotisation;
    }

    public void setIdDecision(String newIdDecision) {
        idDecision = newIdDecision;
    }

    public void setMontantAnnuel(String newMontantAnnuel) {
        montantAnnuel = JANumberFormatter.deQuote(newMontantAnnuel);
    }

    /**
     * @param string
     */
    public void setMontantFacture(java.lang.String string) {
        montantFacture = string;
    }

    public void setMontantMensuel(String newMontantMensuel) {
        montantMensuel = JANumberFormatter.deQuote(newMontantMensuel);
    }

    public void setMontantSemestriel(String newMontantSemestriel) {
        montantSemestriel = JANumberFormatter.deQuote(newMontantSemestriel);
    }

    public void setMontantTrimestriel(String newMontantTrimestriel) {
        montantTrimestriel = JANumberFormatter.deQuote(newMontantTrimestriel);
    }

    /**
     * Sets the periodicite.
     * 
     * @param periodicite
     *            The periodicite to set
     */
    public void setPeriodicite(java.lang.String periodicite) {
        this.periodicite = periodicite;
    }

    /**
     * @param boolean1
     */
    public void setProrata(Boolean boolean1) {
        prorata = boolean1;
    }

    /**
     * Sets the taux.
     * 
     * @param taux
     *            The taux to set
     */
    public void setTaux(java.lang.String taux) {
        this.taux = JANumberFormatter.deQuote(taux);
    }

}
