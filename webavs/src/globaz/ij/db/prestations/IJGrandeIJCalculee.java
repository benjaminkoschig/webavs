package globaz.ij.db.prestations;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.tools.PRCalcul;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJGrandeIJCalculee extends IJIJCalculee {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    public static final String FIELDNAME_ID_GRANDE_IJ_CALCULEE = "XUIIJC";

    /**
     */
    public static final String FIELDNAME_MONTANT_INDEMNITE_ASSISTANCE = "XUMMIA";

    /**
     */
    public static final String FIELDNAME_MONTANT_INDEMNITE_ENFANTS = "XUMMIE";

    /**
     */
    public static final String FIELDNAME_MONTANT_INDEMNITE_EXPLOITATION = "XUMMEX";

    /**
     */
    public static final String FIELDNAME_NB_ENFANTS = "XUNNBE";

    /**
     */
    public static final String TABLE_NAME_GRANDE_IJ_CALCULEE = "IJGIJCAL";

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
        StringBuffer fromClause = new StringBuffer();

        fromClause.append(schema);
        fromClause.append(TABLE_NAME_GRANDE_IJ_CALCULEE);

        // jointure avec la table des ij calculees
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(TABLE_NAME_IJ_CALCULEE);
        fromClause.append(" ON ");
        fromClause.append(FIELDNAME_ID_GRANDE_IJ_CALCULEE);
        fromClause.append("=");
        fromClause.append(FIELDNAME_ID_IJ_CALCULEE);

        return fromClause.toString();
    }

    private String montantIndemniteAssistance = "";
    private String montantIndemniteEnfant = "";
    private String montantIndemniteExploitation = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String nbEnfants = "";

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
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
        return createFromClause(_getCollection());
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_GRANDE_IJ_CALCULEE;
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
        montantIndemniteEnfant = statement.dbReadNumeric(FIELDNAME_MONTANT_INDEMNITE_ENFANTS, 2);
        nbEnfants = statement.dbReadNumeric(FIELDNAME_NB_ENFANTS);
        montantIndemniteAssistance = statement.dbReadNumeric(FIELDNAME_MONTANT_INDEMNITE_ASSISTANCE, 2);
        montantIndemniteExploitation = statement.dbReadNumeric(FIELDNAME_MONTANT_INDEMNITE_EXPLOITATION, 2);
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_GRANDE_IJ_CALCULEE,
                _dbWriteNumeric(statement.getTransaction(), getIdIJCalculee()));
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        if (_getAction() == ACTION_COPY) {
            super._writeProperties(statement);
        } else {
            statement.writeField(FIELDNAME_ID_GRANDE_IJ_CALCULEE,
                    _dbWriteNumeric(statement.getTransaction(), getIdIJCalculee(), "idIJCalculee"));
        }

        statement.writeField(FIELDNAME_MONTANT_INDEMNITE_ENFANTS,
                _dbWriteNumeric(statement.getTransaction(), montantIndemniteEnfant, "montantIndemniteEnfant"));
        statement.writeField(FIELDNAME_NB_ENFANTS, _dbWriteNumeric(statement.getTransaction(), nbEnfants, "nbEnfants"));
        statement.writeField(FIELDNAME_MONTANT_INDEMNITE_ASSISTANCE,
                _dbWriteNumeric(statement.getTransaction(), montantIndemniteAssistance, "montantIndemniteAssistance"));
        statement.writeField(
                FIELDNAME_MONTANT_INDEMNITE_EXPLOITATION,
                _dbWriteNumeric(statement.getTransaction(), montantIndemniteExploitation,
                        "montantIndeminteExploitation"));
    }

    /**
     * getter pour l'attribut montant indemnite assistance
     * 
     * @return la valeur courante de l'attribut montant indemnite assistance
     */
    public String getMontantIndemniteAssistance() {
        return montantIndemniteAssistance;
    }

    /**
     * getter pour l'attribut montant indemnite enfant
     * 
     * @return la valeur courante de l'attribut montant indemnite enfant
     */
    public String getMontantIndemniteEnfant() {
        return montantIndemniteEnfant;
    }

    /**
     * getter pour l'attribut montant indemnite exploitation
     * 
     * @return la valeur courante de l'attribut montant indemnite exploitation
     */
    public String getMontantIndemniteExploitation() {
        return montantIndemniteExploitation;
    }

    /**
     * getter pour l'attribut nb enfants
     * 
     * @return la valeur courante de l'attribut nb enfants
     */
    public String getNbEnfants() {
        return nbEnfants;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return true;
    }

    /**
     * getter pour l'attribut egal pour calcul
     * 
     * @param ij
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut egal pour calcul
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public boolean isEgalPourCalcul(IJIJCalculee ij) throws Exception {
        IJGrandeIJCalculee gij = (IJGrandeIJCalculee) ij;

        if (PRCalcul.isIEgaux(nbEnfants, gij.nbEnfants)
                && PRCalcul.isDEgaux(montantIndemniteEnfant, gij.montantIndemniteEnfant)
                && PRCalcul.isDEgaux(montantIndemniteAssistance, gij.montantIndemniteAssistance)
                && PRCalcul.isDEgaux(montantIndemniteExploitation, gij.montantIndemniteExploitation)) {
            return super.isEgalPourCalcul(ij);
        }

        return false;
    }

    /**
     * setter pour l'attribut montant indemnite assistance
     * 
     * @param montantIndemniteAssistance
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantIndemniteAssistance(String montantIndemniteAssistance) {
        this.montantIndemniteAssistance = montantIndemniteAssistance;
    }

    /**
     * setter pour l'attribut montant indemnite enfant
     * 
     * @param montantIndemniteEnfant
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantIndemniteEnfant(String montantIndemniteEnfant) {
        this.montantIndemniteEnfant = montantIndemniteEnfant;
    }

    /**
     * setter pour l'attribut montant indemnite exploitation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantIndemniteExploitation(String string) {
        montantIndemniteExploitation = string;
    }

    /**
     * setter pour l'attribut nb enfants
     * 
     * @param nbEnfants
     *            une nouvelle valeur pour cet attribut
     */
    public void setNbEnfants(String nbEnfants) {
        this.nbEnfants = nbEnfants;
    }
}
