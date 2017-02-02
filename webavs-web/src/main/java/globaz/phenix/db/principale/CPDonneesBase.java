package globaz.phenix.db.principale;

import globaz.globall.db.BConstants;
import globaz.globall.util.JANumberFormatter;

public class CPDonneesBase extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */

    public static final String FIELDNAME_MONTANT_TOTAL_RENTE_AVS = "IDMAVS";

    private static final long serialVersionUID = 1L;
    public final static String CS_BENEFICE_CAP = "613006";
    public final static String CS_FISC = "613004";
    // code systeme - sourceInformation
    public final static String CS_NOTRE_ESTIMATION = "613001";
    public final static String CS_REPRISE_IMPOT = "613007";
    public final static String CS_TAX_OFFICE = "613005";
    public final static String CS_VOS_COMPTE = "613002";
    public final static String CS_VOTRE_ESTIMATION = "613003";
    private java.lang.String capital = "";
    private java.lang.String cotisation1 = "";
    private java.lang.String cotisation2 = "";
    private java.lang.String cotisationSalarie = "";
    private java.lang.String dateFortune = "";
    private java.lang.String debutExercice1 = "";
    private java.lang.String debutExercice2 = "";
    private java.lang.String finExercice1 = "";
    private java.lang.String finExercice2 = "";
    private java.lang.String fortuneTotale = "";
    private java.lang.String idDecision = "";
    private java.lang.String nbMoisExercice1 = "";
    private java.lang.String nbMoisExercice2;
    private java.lang.String nbMoisRevenuAutre1 = "";
    private java.lang.String nbMoisRevenuAutre2 = "";
    private java.lang.String revenu1 = "";
    private java.lang.String revenu2 = "";
    private java.lang.String revenuAutre1 = "";
    private java.lang.String revenuAutre2 = "";
    private java.lang.String revenuCiForce = "";
    private String rachatLPP = "";
    private Boolean revenuCiForce0 = new Boolean(false);
    private java.lang.String sourceInformation = "";
    private String montantTotalRenteAVS = "";

    public String getMontantTotalRenteAVS() {

        return JANumberFormatter.fmt(montantTotalRenteAVS, true, false, true, 2);

    }

    public void setMontantTotalRenteAVS(String montantTotalRenteAVS) {

        this.montantTotalRenteAVS = JANumberFormatter.deQuote(montantTotalRenteAVS);
    }

    /**
     * Commentaire relatif au constructeur CPDonneesBase
     */
    public CPDonneesBase() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPDOENP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idDecision = statement.dbReadNumeric("IAIDEC");
        revenu1 = statement.dbReadNumeric("IDREV1", 2);
        montantTotalRenteAVS = statement.dbReadNumeric(FIELDNAME_MONTANT_TOTAL_RENTE_AVS, 2);
        debutExercice1 = statement.dbReadDateAMJ("IDDDE1");
        finExercice1 = statement.dbReadDateAMJ("IDDFI1");
        revenu2 = statement.dbReadNumeric("IDREV2", 2);
        debutExercice2 = statement.dbReadDateAMJ("IDDDE2");
        finExercice2 = statement.dbReadDateAMJ("IDDFI2");
        nbMoisExercice1 = statement.dbReadNumeric("IDMEX1");
        nbMoisExercice2 = statement.dbReadNumeric("IDMEX2");
        cotisation1 = statement.dbReadNumeric("IDCOT1", 2);
        cotisation2 = statement.dbReadNumeric("IDCOT2", 2);
        capital = statement.dbReadNumeric("IDCAPI", 2);
        revenuCiForce = statement.dbReadNumeric("IDRCIF", 2);
        revenuCiForce0 = statement.dbReadBoolean("IDBCI0");
        fortuneTotale = statement.dbReadNumeric("IDFORT", 2);
        dateFortune = statement.dbReadDateAMJ("IDDFOR");
        cotisationSalarie = statement.dbReadNumeric("IDMSAL", 2);
        revenuAutre1 = statement.dbReadNumeric("IDRAU1", 2);
        nbMoisRevenuAutre1 = statement.dbReadNumeric("IDMAU1");
        nbMoisRevenuAutre2 = statement.dbReadNumeric("IDMAU2");
        revenuAutre2 = statement.dbReadNumeric("IDRAU2", 2);
        nbMoisRevenuAutre2 = statement.dbReadNumeric("IDMAU2");
        sourceInformation = statement.dbReadNumeric("IDTSRC");

        rachatLPP = statement.dbReadNumeric("IDRLPP");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IAIDEC", this._dbWriteNumeric(statement.getTransaction(), getIdDecision(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IAIDEC", this._dbWriteNumeric(statement.getTransaction(), getIdDecision(), "idDecision"));
        statement.writeField("IDREV1", this._dbWriteNumeric(statement.getTransaction(), getRevenu1(), "revenu1"));
        statement.writeField(FIELDNAME_MONTANT_TOTAL_RENTE_AVS,
                this._dbWriteNumeric(statement.getTransaction(), getMontantTotalRenteAVS(), "montantTotalRenteAVS"));
        statement.writeField("IDDDE1",
                this._dbWriteDateAMJ(statement.getTransaction(), getDebutExercice1(), "debutExercice1"));
        statement.writeField("IDDFI1",
                this._dbWriteDateAMJ(statement.getTransaction(), getFinExercice1(), "finExercice1"));
        statement.writeField("IDREV2", this._dbWriteNumeric(statement.getTransaction(), getRevenu2(), "revenu2"));
        statement.writeField("IDDDE2",
                this._dbWriteDateAMJ(statement.getTransaction(), getDebutExercice2(), "debutExercice2"));
        statement.writeField("IDDFI2",
                this._dbWriteDateAMJ(statement.getTransaction(), getFinExercice2(), "finExercice2"));
        statement.writeField("IDMEX1",
                this._dbWriteNumeric(statement.getTransaction(), getNbMoisExercice1(), "nbMoisExercice1"));
        statement.writeField("IDMEX2",
                this._dbWriteNumeric(statement.getTransaction(), getNbMoisExercice2(), "nbMoisExercice2"));
        statement.writeField("IDCOT1",
                this._dbWriteNumeric(statement.getTransaction(), getCotisation1(), "cotisation1"));
        statement.writeField("IDCOT2",
                this._dbWriteNumeric(statement.getTransaction(), getCotisation2(), "cotisation2"));
        statement.writeField("IDCAPI", this._dbWriteNumeric(statement.getTransaction(), getCapital(), "capital"));
        statement.writeField("IDRCIF",
                this._dbWriteNumeric(statement.getTransaction(), getRevenuCiForce(), "reveuCiForce"));
        statement.writeField("IDBCI0", this._dbWriteBoolean(statement.getTransaction(), isRevenuCiForce0(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "revenuCiForce0"));
        statement
                .writeField("IDFORT", this._dbWriteNumeric(statement.getTransaction(), fortuneTotale, "fortuneTotale"));
        statement.writeField("IDDFOR",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateFortune(), "dateFortune"));
        statement.writeField("IDMSAL",
                this._dbWriteNumeric(statement.getTransaction(), getCotisationSalarie(), "cotisationSalarie"));
        statement.writeField("IDRAU1",
                this._dbWriteNumeric(statement.getTransaction(), getRevenuAutre1(), "revenuAutre1"));
        statement.writeField("IDRAU2",
                this._dbWriteNumeric(statement.getTransaction(), getRevenuAutre2(), "revenuAutre2"));
        statement.writeField("IDMAU1",
                this._dbWriteNumeric(statement.getTransaction(), getNbMoisRevenuAutre1(), "nbMoisRevenuAutre1"));
        statement.writeField("IDMAU2",
                this._dbWriteNumeric(statement.getTransaction(), getNbMoisRevenuAutre2(), "nbMoisRevenuAutre2"));
        statement.writeField("IDTSRC",
                this._dbWriteNumeric(statement.getTransaction(), getSourceInformation(), "sourceInformation"));

        statement.writeField("IDRLPP", this._dbWriteNumeric(statement.getTransaction(), getRachatLPP(), "rachatLPP"));
    }

    /**
     * Insert the method's description here. Creation date: (18.06.2003 09:01:14)
     * 
     * @return java.lang.Object
     * @exception java.lang.CloneNotSupportedException
     *                The exception description.
     */
    @Override
    public Object clone() throws java.lang.CloneNotSupportedException {
        return super.clone();
    }

    public java.lang.String getCapital() {
        return JANumberFormatter.fmt(capital, true, false, true, 0);
    }

    public java.lang.String getCotisation1() {
        return cotisation1;
    }

    public java.lang.String getCotisation2() {
        return cotisation2;
    }

    public java.lang.String getCotisationSalarie() {
        return JANumberFormatter.fmt(cotisationSalarie, true, false, true, 2);
    }

    public java.lang.String getDateFortune() {
        return dateFortune;
    }

    public java.lang.String getDebutExercice1() {
        return debutExercice1;
    }

    public java.lang.String getDebutExercice2() {
        return debutExercice2;
    }

    public java.lang.String getFinExercice1() {
        return finExercice1;
    }

    public java.lang.String getFinExercice2() {
        return finExercice2;
    }

    public java.lang.String getFortuneTotale(int codeFormat) {
        if (codeFormat == 1) {
            return JANumberFormatter.fmt(fortuneTotale, true, false, true, 0);
        } else {
            return fortuneTotale;
        }
    }

    /**
     * Getter
     */
    public java.lang.String getIdDecision() {
        return idDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 12:16:08)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNbMoisExercice1() {
        return nbMoisExercice1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 12:16:52)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNbMoisExercice2() {
        return nbMoisExercice2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 12:16:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNbMoisRevenuAutre1() {
        return nbMoisRevenuAutre1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 12:17:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNbMoisRevenuAutre2() {
        return nbMoisRevenuAutre2;
    }

    public java.lang.String getRevenu1() {
        return JANumberFormatter.fmt(revenu1, true, false, true, 2);
    }

    public java.lang.String getRevenu2() {
        return JANumberFormatter.fmt(revenu2, true, false, true, 2);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 08:43:14)
     * 
     * @return java.lang.String
     */
    public java.lang.String getRevenuAutre1() {
        return JANumberFormatter.fmt(revenuAutre1, true, false, true, 2);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 09:10:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getRevenuAutre2() {
        return JANumberFormatter.fmt(revenuAutre2, true, false, true, 2);
    }

    public java.lang.String getRevenuCiForce() {
        return JANumberFormatter.fmt(revenuCiForce, true, false, true, 0);
    }

    public String getRachatLPP() {
        return JANumberFormatter.fmt(rachatLPP, true, false, true, 2);
    }

    /**
     * @return
     */
    public java.lang.String getSourceInformation() {
        return sourceInformation;
    }

    public Boolean isRevenuCiForce0() {
        return revenuCiForce0;
    }

    public void setCapital(java.lang.String newCapital) {
        capital = JANumberFormatter.deQuote(newCapital);
    }

    public void setCotisation1(java.lang.String newCotisation1) {
        cotisation1 = JANumberFormatter.deQuote(newCotisation1);
    }

    public void setCotisation2(java.lang.String newCotisation2) {
        cotisation2 = JANumberFormatter.deQuote(newCotisation2);
    }

    public void setCotisationSalarie(java.lang.String newCotisationSalarie) {
        cotisationSalarie = JANumberFormatter.deQuote(newCotisationSalarie);
    }

    public void setDateFortune(java.lang.String newDateFortune) {
        dateFortune = newDateFortune;
    }

    public void setDebutExercice1(java.lang.String newDebutExercice1) {
        debutExercice1 = newDebutExercice1;
    }

    public void setDebutExercice2(java.lang.String newDebutExercice2) {
        debutExercice2 = newDebutExercice2;
    }

    public void setFinExercice1(java.lang.String newFinExercice1) {
        finExercice1 = newFinExercice1;
    }

    public void setFinExercice2(java.lang.String newFinExercice2) {
        finExercice2 = newFinExercice2;
    }

    public void setFortuneTotale(java.lang.String newFortuneTotale) {
        fortuneTotale = JANumberFormatter.deQuote(newFortuneTotale);
    }

    /**
     * Setter
     */
    public void setIdDecision(java.lang.String newIdDecision) {
        idDecision = newIdDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 12:16:08)
     * 
     * @param newNbMoisExercice1
     *            java.lang.String
     */
    public void setNbMoisExercice1(java.lang.String newNbMoisExercice1) {
        nbMoisExercice1 = newNbMoisExercice1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 12:16:52)
     * 
     * @param newNbMoisExercice2
     *            java.lang.String
     */
    public void setNbMoisExercice2(java.lang.String newNbMoisExercice2) {
        nbMoisExercice2 = newNbMoisExercice2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 12:16:34)
     * 
     * @param newNbMoisRevenuAutre1
     *            java.lang.String
     */
    public void setNbMoisRevenuAutre1(java.lang.String newNbMoisRevenuAutre1) {
        nbMoisRevenuAutre1 = newNbMoisRevenuAutre1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 12:17:26)
     * 
     * @param newNbMoisRevenuAutre2
     *            java.lang.String
     */
    public void setNbMoisRevenuAutre2(java.lang.String newNbMoisRevenuAutre2) {
        nbMoisRevenuAutre2 = newNbMoisRevenuAutre2;
    }

    public void setRevenu1(java.lang.String newRevenu1) {
        revenu1 = JANumberFormatter.deQuote(newRevenu1);
    }

    public void setRevenu2(java.lang.String newRevenu2) {
        revenu2 = JANumberFormatter.deQuote(newRevenu2);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 08:43:14)
     * 
     * @param newRevenuAutre1
     *            java.lang.String
     */
    public void setRevenuAutre1(java.lang.String newRevenuAutre1) {
        revenuAutre1 = JANumberFormatter.deQuote(newRevenuAutre1);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 09:10:26)
     * 
     * @param newRevenuAutre2
     *            java.lang.String
     */
    public void setRevenuAutre2(java.lang.String newRevenuAutre2) {
        revenuAutre2 = JANumberFormatter.deQuote(newRevenuAutre2);
    }

    public void setRachatLPP(String _rachatLPP) {
        rachatLPP = JANumberFormatter.deQuote(_rachatLPP);
    }

    public void setRevenuCiForce(java.lang.String newRevenuCiForce) {
        revenuCiForce = JANumberFormatter.deQuote(newRevenuCiForce);
    }

    public void setRevenuCiForce0(Boolean newRevenuCiForce0) {
        revenuCiForce0 = newRevenuCiForce0;
    }

    /**
     * @param string
     */
    public void setSourceInformation(java.lang.String string) {
        sourceInformation = string;
    }

}
