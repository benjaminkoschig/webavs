package globaz.helios.db.bouclement;

import globaz.globall.db.BConstants;
import globaz.globall.db.BManager;
import globaz.helios.db.interfaces.ITreeListable;

public class CGBouclement extends globaz.globall.db.BEntity implements ITreeListable, java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_BOUCLEMENT_ANNUEL = "710004"; // Bouclement
    public final static String CS_BOUCLEMENT_ANNUEL_AVS = "710003"; // Bouclement
    public final static String CS_BOUCLEMENT_MENSUEL_AVS = "710002"; // Bouclement
    public final static String CS_STANDARD = "710001"; // Bouclement

    /**
     * Returns the cS_BOUCLEMENT_ANNUEL.
     * 
     * @return String
     */
    public static String getCS_BOUCLEMENT_ANNUEL() {
        return CS_BOUCLEMENT_ANNUEL;
    }

    /**
     * Returns the cS_BOUCLEMENT_ANNUEL_AVS.
     * 
     * @return String
     */
    public static String getCS_BOUCLEMENT_ANNUEL_AVS() {
        return CS_BOUCLEMENT_ANNUEL_AVS;
    }

    /**
     * Returns the cS_BOUCLEMENT_MENSUEL_AVS.
     * 
     * @return String
     */
    public static String getCS_BOUCLEMENT_MENSUEL_AVS() {
        return CS_BOUCLEMENT_MENSUEL_AVS;
    }

    /**
     * Returns the cS_STANDARD.
     * 
     * @return String
     */
    public static String getCS_STANDARD() {
        return CS_STANDARD;
    }

    private java.lang.String idBouclement = new String();
    private java.lang.String idMandat = null;
    private java.lang.String idTypeBouclement = new String();

    private Boolean isImprimerBalance = new Boolean(false);
    private Boolean isImprimerBalanceMvt = new Boolean(false);
    private Boolean isImprimerBilan = new Boolean(false);
    private Boolean isImprimerGrandLivre = new Boolean(false);

    private Boolean isImprimerPP = new Boolean(false);

    // code systeme

    private Boolean isImprimerReleveAvs = new Boolean(false);

    private java.lang.String libelleDe = new String();

    private java.lang.String libelleFr = new String();

    private java.lang.String libelleIt = new String();

    /**
     * Commentaire relatif au constructeur CGBouclement
     */
    public CGBouclement() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdBouclement(_incCounter(transaction, "0"));

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CGBOUCP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idBouclement = statement.dbReadNumeric("IDBOUCLEMENT");
        libelleFr = statement.dbReadString("LIBELLEFR");
        libelleDe = statement.dbReadString("LIBELLEDE");
        libelleIt = statement.dbReadString("LIBELLEIT");
        idMandat = statement.dbReadNumeric("IDMANDAT");
        idTypeBouclement = statement.dbReadNumeric("IDTYPEBOUCLEMENT");

        isImprimerBalance = statement.dbReadBoolean("IMPRIMERBALANCE");
        isImprimerBalanceMvt = statement.dbReadBoolean("IMPRIMEBALANCEMOUV");
        isImprimerBilan = statement.dbReadBoolean("IMPRIMERBILAN");
        isImprimerGrandLivre = statement.dbReadBoolean("IMPRIMERGRANDLIVRE");
        isImprimerPP = statement.dbReadBoolean("IMPRIMERPP");
        isImprimerReleveAvs = statement.dbReadBoolean("IMPRIMERRELEVEAVS");

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
        statement.writeKey("IDBOUCLEMENT", _dbWriteNumeric(statement.getTransaction(), getIdBouclement(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDBOUCLEMENT",
                _dbWriteNumeric(statement.getTransaction(), getIdBouclement(), "idBouclement"));
        statement.writeField("LIBELLEFR", _dbWriteString(statement.getTransaction(), getLibelleFr(), "libelleFr"));
        statement.writeField("LIBELLEDE", _dbWriteString(statement.getTransaction(), getLibelleDe(), "libelleDe"));
        statement.writeField("LIBELLEIT", _dbWriteString(statement.getTransaction(), getLibelleIt(), "libelleIt"));
        statement.writeField("IDTYPEBOUCLEMENT",
                _dbWriteNumeric(statement.getTransaction(), getIdTypeBouclement(), "idTypeBouclement"));
        statement.writeField("IDMANDAT", _dbWriteNumeric(statement.getTransaction(), getIdMandat(), "idMandat"));

        statement.writeField(
                "IMPRIMEBALANCEMOUV",
                _dbWriteBoolean(statement.getTransaction(), isImprimerBalanceMvt(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "imprimerBalanceMvt"));
        statement.writeField(
                "IMPRIMERBILAN",
                _dbWriteBoolean(statement.getTransaction(), isImprimerBilan(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "imprimerBilan"));
        statement.writeField(
                "IMPRIMERGRANDLIVRE",
                _dbWriteBoolean(statement.getTransaction(), isImprimerGrandLivre(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "imprimerGrandLivre"));
        statement.writeField(
                "IMPRIMERPP",
                _dbWriteBoolean(statement.getTransaction(), isImprimerPP(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "imprimerPP"));
        statement.writeField(
                "IMPRIMERBALANCE",
                _dbWriteBoolean(statement.getTransaction(), isImprimerBalance(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "imprimerBalance"));
        statement.writeField(
                "IMPRIMERRELEVEAVS",
                _dbWriteBoolean(statement.getTransaction(), isImprimerReleveAvs(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "imprimerReleveAvs"));

    }

    @Override
    public BManager[] getChilds() {

        return null;
    }

    /**
     * Getter
     */
    public java.lang.String getIdBouclement() {
        return idBouclement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.12.2002 15:03:29)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdMandat() {
        return idMandat;
    }

    public java.lang.String getIdTypeBouclement() {
        return idTypeBouclement;
    }

    @Override
    public java.lang.String getLibelle() {
        // A FAIRE : retourner le libelle en fonction de la langue de
        // l'utilisateur
        return libelleFr;
    }

    public java.lang.String getLibelleDe() {
        return libelleDe;
    }

    public java.lang.String getLibelleFr() {
        return libelleFr;
    }

    public java.lang.String getLibelleIt() {
        return libelleIt;
    }

    public Boolean isBouclementAnnuel() {
        return new Boolean(getIdTypeBouclement().equals(CS_BOUCLEMENT_ANNUEL));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.04.2003 19:00:24)
     * 
     * @return java.lang.Boolean
     */
    public Boolean isBouclementAnnuelAVS() {
        return new Boolean(getIdTypeBouclement().equals(CS_BOUCLEMENT_ANNUEL_AVS));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.04.2003 19:00:43)
     * 
     * @return java.lang.Boolean
     */
    public Boolean isBouclementMensuelAVS() {
        return new Boolean(getIdTypeBouclement().equals(CS_BOUCLEMENT_MENSUEL_AVS));
    }

    /**
     * Returns the isImprimerBalance.
     * 
     * @return Boolean
     */
    public Boolean isImprimerBalance() {
        return isImprimerBalance;
    }

    /**
     * Returns the isImprimerBalanceMvt.
     * 
     * @return Boolean
     */
    public Boolean isImprimerBalanceMvt() {
        return isImprimerBalanceMvt;
    }

    /**
     * Returns the isImprimerBilan.
     * 
     * @return Boolean
     */
    public Boolean isImprimerBilan() {
        return isImprimerBilan;
    }

    /**
     * Returns the isImprimerGrandLivre.
     * 
     * @return Boolean
     */
    public Boolean isImprimerGrandLivre() {
        return isImprimerGrandLivre;
    }

    /**
     * Returns the isImprimerPP.
     * 
     * @return Boolean
     */
    public Boolean isImprimerPP() {
        return isImprimerPP;
    }

    /**
     * Returns the isImprimerReleveAvs.
     * 
     * @return Boolean
     */
    public Boolean isImprimerReleveAvs() {
        return isImprimerReleveAvs;
    }

    /**
     * Setter
     */
    public void setIdBouclement(java.lang.String newIdBouclement) {
        idBouclement = newIdBouclement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.12.2002 15:03:29)
     * 
     * @param newIdMandat
     *            java.lang.String
     */
    public void setIdMandat(java.lang.String newIdMandat) {
        idMandat = newIdMandat;
    }

    public void setIdTypeBouclement(java.lang.String newIdTypeBouclement) {
        idTypeBouclement = newIdTypeBouclement;
    }

    /**
     * Sets the isImprimerBalance.
     * 
     * @param isImprimerBalance
     *            The isImprimerBalance to set
     */
    public void setIsImprimerBalance(Boolean isImprimerBalance) {
        this.isImprimerBalance = isImprimerBalance;
    }

    /**
     * Sets the isImprimerBalanceMvt.
     * 
     * @param isImprimerBalanceMvt
     *            The isImprimerBalanceMvt to set
     */
    public void setIsImprimerBalanceMvt(Boolean isImprimerBalanceMvt) {
        this.isImprimerBalanceMvt = isImprimerBalanceMvt;
    }

    /**
     * Sets the isImprimerBilan.
     * 
     * @param isImprimerBilan
     *            The isImprimerBilan to set
     */
    public void setIsImprimerBilan(Boolean isImprimerBilan) {
        this.isImprimerBilan = isImprimerBilan;
    }

    /**
     * Sets the isImprimerGrandLivre.
     * 
     * @param isImprimerGrandLivre
     *            The isImprimerGrandLivre to set
     */
    public void setIsImprimerGrandLivre(Boolean isImprimerGrandLivre) {
        this.isImprimerGrandLivre = isImprimerGrandLivre;
    }

    /**
     * Sets the isImprimerPP.
     * 
     * @param isImprimerPP
     *            The isImprimerPP to set
     */
    public void setIsImprimerPP(Boolean isImprimerPP) {
        this.isImprimerPP = isImprimerPP;
    }

    /**
     * Sets the isImprimerReleveAvs.
     * 
     * @param isImprimerReleveAvs
     *            The isImprimerReleveAvs to set
     */
    public void setIsImprimerReleveAvs(Boolean isImprimerReleveAvs) {
        this.isImprimerReleveAvs = isImprimerReleveAvs;
    }

    public void setLibelleDe(java.lang.String newLibelleDe) {
        libelleDe = newLibelleDe;
    }

    public void setLibelleFr(java.lang.String newLibelleFr) {
        libelleFr = newLibelleFr;
    }

    public void setLibelleIt(java.lang.String newLibelleIt) {
        libelleIt = newLibelleIt;
    }

}
