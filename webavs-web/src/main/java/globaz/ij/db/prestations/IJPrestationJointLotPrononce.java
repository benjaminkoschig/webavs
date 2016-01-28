/*
 * Créé le 20 sept. 05
 */
package globaz.ij.db.prestations;

import globaz.globall.db.BStatement;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.lots.IJLot;
import globaz.ij.db.prononces.IJPrononce;
import globaz.prestation.db.demandes.PRDemande;

/**
 * <H1>Entity faisant une jointure entre les prestations, les lots, les bases d'indemnisation, les prononces et les
 * tiers</H1>
 * 
 * @author dvh
 */
public class IJPrestationJointLotPrononce extends IJPrestation {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idTiers = "";

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public String getIdTiers() {
        return idTiers;
    }

    /**
	 */
    public static final String FIELDNAME_DATE_NAISSANCE = "HPDNAI";

    /**
     */
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";

    /**
	 */
    public static final String FIELDNAME_NATIONALITE = "HNIPAY";

    /**
     */
    public static final String FIELDNAME_NOM = "HTLDE1";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";

    /**
     */
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";

    /**
     */
    public static final String FIELDNAME_PRENOM = "HTLDE2";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";

    /**
	 */

    public static final String FIELDNAME_SEXE = "HPTSEX";

    /**
     */
    public static final String TABLE_AVS = "TIPAVSP";

    /**
     */
    public static final String TABLE_TIERS = "TITIERP";

    /**
	 */
    public static final String TABLE_TIERS_DETAIL = "TIPERSP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrestation.TABLE_NAME);

        // jointure entre tables prestation et lot
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJLot.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrestation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrestation.FIELDNAME_IDLOT);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJLot.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJLot.FIELDNAME_IDLOT);

        // Jointure entre prestation et base indemnisation
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJBaseIndemnisation.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrestation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrestation.FIELDNAME_ID_BASEINDEMNISATION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJBaseIndemnisation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION);

        // jointure entre tables bases indemnisation et droits
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononce.TABLE_NAME_PRONONCE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJBaseIndemnisation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJBaseIndemnisation.FIELDNAME_IDPRONONCE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononce.TABLE_NAME_PRONONCE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononce.FIELDNAME_ID_PRONONCE);

        // jointure entre tables prononces et demande
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononce.TABLE_NAME_PRONONCE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononce.FIELDNAME_ID_DEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDDEMANDE);

        // jointure entre tables demandes et tiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre table des tiers et table détail des tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS_DETAIL);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS_DETAIL);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre tables tiers et avs
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        return fromClauseBuffer.toString();
    }

    private String csNationalite = "";
    private String csSexe = "";
    private String dateDebutPrononce = "";
    private String dateNaissance = "";
    private String datePaiement = "";
    private String datePrononce = "";
    private String descriptionLot = "";
    private String fromClause = null;
    private String idBaseIndemnisation = "";
    private String noAVS = "";
    private String noLot = "";
    private String nom = "";
    private String officeAI = "";
    private String prenom = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String typeIJ = "";

    /**
     * redefinie a false car les updates et suppressions se feront sur la table de son parent
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFields(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFields(BStatement statement) {
        // TODO prendre que ce qu'on a besoin
        return super._getFields(statement);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        // readString pour le numero AVS car il est deja formaté dans la base
        noAVS = statement.dbReadString(FIELDNAME_NUM_AVS);
        nom = statement.dbReadString(FIELDNAME_NOM);
        prenom = statement.dbReadString(FIELDNAME_PRENOM);
        dateDebutPrononce = statement.dbReadDateAMJ(IJPrononce.FIELDNAME_DATE_DEBUT_PRONONCE);
        descriptionLot = statement.dbReadString(IJLot.FIELDNAME_DESCRIPTION);
        noLot = statement.dbReadNumeric(IJLot.FIELDNAME_NOLOT);
        idBaseIndemnisation = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION);
        datePrononce = statement.dbReadDateAMJ(IJPrononce.FIELDNAME_DATE_PRONONCE);
        typeIJ = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_TYPE_IJ);
        officeAI = statement.dbReadNumeric(IJPrononce.FIELDNAME_OFFICE_AI);
        datePaiement = statement.dbReadDateAMJ(IJLot.FIELDNAME_DATECOMPTABLE);
        dateNaissance = statement.dbReadDateAMJ(FIELDNAME_DATE_NAISSANCE);
        csSexe = statement.dbReadNumeric(FIELDNAME_SEXE);
        csNationalite = statement.dbReadNumeric(FIELDNAME_NATIONALITE);
        idTiers = statement.dbReadNumeric(PRDemande.FIELDNAME_IDTIERS);
    }

    /**
     * @return
     */
    public String getCsNationalite() {
        return csNationalite;
    }

    /**
     * @return
     */
    public String getCsSexe() {
        return csSexe;
    }

    /**
     * getter pour l'attribut date debut droit
     * 
     * @return la valeur courante de l'attribut date debut droit
     */
    public String getDateDebutPrononce() {
        return dateDebutPrononce;
    }

    /**
     * @return
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * getter pour l'attribut datePaiement
     * 
     * @return la valeur courante pour l'attribut datePaiement
     */
    public String getDatePaiement() {
        return datePaiement;
    }

    /**
     * getter pour l'attribut date prononce
     * 
     * @return la valeur courante de l'attribut date prononce
     */
    public String getDatePrononce() {
        return datePrononce;
    }

    /**
     * getter pour l'attribut description lot
     * 
     * @return la valeur courante de l'attribut description lot
     */
    public String getDescriptionLot() {
        return descriptionLot;
    }

    /**
     * getter pour l'attribut from clause
     * 
     * @return la valeur courante de l'attribut from clause
     */
    public String getFromClause() {
        return fromClause;
    }

    /**
     * getter pour l'attribut id base indemnisation
     * 
     * @return la valeur courante de l'attribut id base indemnisation
     */
    @Override
    public String getIdBaseIndemnisation() {
        return idBaseIndemnisation;
    }

    /**
     * getter pour l'attribut no AVS
     * 
     * @return la valeur courante de l'attribut no AVS
     */
    public String getNoAVS() {
        return noAVS;
    }

    /**
     * getter pour l'attribut no lot
     * 
     * @return la valeur courante de l'attribut no lot
     */
    public String getNoLot() {
        return noLot;
    }

    /**
     * getter pour l'attribut nom
     * 
     * @return la valeur courante de l'attribut nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * getter pour l'attribut office AI
     * 
     * @return la valeur courrante pour l'attribut office AI
     */
    public String getOfficeAI() {
        return officeAI;
    }

    /**
     * getter pour l'attribut prenom
     * 
     * @return la valeur courante de l'attribut prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * getter pour l'attribut type IJ
     * 
     * @return la valeur courrante pour l'attribut type IJ
     */
    public String getTypeIJ() {
        return typeIJ;
    }

    /**
     * @param string
     */
    public void setCsNationalite(String string) {
        csNationalite = string;
    }

    /**
     * @param string
     */
    public void setCsSexe(String string) {
        csSexe = string;
    }

    /**
     * setter pour l'attribut date debut droit
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutPrononce(String string) {
        dateDebutPrononce = string;
    }

    /**
     * @param string
     */
    public void setDateNaissance(String string) {
        dateNaissance = string;
    }

    /**
     * setter pour l'attribut datePaiement
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDatePaiement(String string) {
        datePaiement = string;
    }

    /**
     * setter pour l'attribut date prononce
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDatePrononce(String string) {
        datePrononce = string;
    }

    /**
     * setter pour l'attribut description lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDescriptionLot(String string) {
        descriptionLot = string;
    }

    /**
     * setter pour l'attribut from clause
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromClause(String string) {
        fromClause = string;
    }

    /**
     * setter pour l'attribut id base indemnisation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setIdBaseIndemnisation(String string) {
        idBaseIndemnisation = string;
    }

    /**
     * setter pour l'attribut no AVS
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoAVS(String string) {
        noAVS = string;
    }

    /**
     * setter pour l'attribut no lot
     * 
     * @param noLot
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoLot(String noLot) {
        this.noLot = noLot;
    }

    /**
     * setter pour l'attribut nom
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNom(String string) {
        nom = string;
    }

    /**
     * setter pour l'attribut Office AI
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setOfficeAI(String string) {
        officeAI = string;
    }

    /**
     * setter pour l'attribut prenom
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setPrenom(String string) {
        prenom = string;
    }

    /**
     * setter pour l'attribut Type IJ
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setTypeIJ(String string) {
        typeIJ = string;
    }

}
