/*
 * Créé le 21 janvier 2010
 */
package globaz.cygnus.db.qds;

import globaz.globall.db.BStatement;
import globaz.prestation.tools.sql.PRFromStringBuffer;

/**
 * @author eco
 */
public class RFQdHistoriqueSoldeChargeJointSelf extends RFQdSoldeCharge {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ANCIEN_CONCERNE = "ERLCON_A";
    public static final String FIELDNAME_ANCIEN_DATE = "ERDMOD_A";
    public static final String FIELDNAME_ANCIEN_GESTIONNAIRE = "ERIGES_A";
    public static final String FIELDNAME_ANCIEN_MONTANT = "ERMSOC_A";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Génération de la clause from pour la requête > Jointure du solde de charge sur lui-même
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {

        PRFromStringBuffer fromClauseBuffer = new PRFromStringBuffer(schema);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdSoldeCharge.TABLE_NAME + " n");

        // jointure entre la table des Qd et la table des QdPrinicpale

        fromClauseBuffer.leftJoin(RFQdSoldeCharge.TABLE_NAME + " a").appendOn()
                .append("n." + FIELDNAME_ID_SOLDE_MODIFIE);
        fromClauseBuffer.appendEgal().append("a." + FIELDNAME_ID_SOLDE_CHARGE);

        return fromClauseBuffer.toString();
    }

    private String ancienConcerne = "";
    private String ancienDate = "";
    private String ancienGestionnaire = "";
    private String ancienMontant = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private transient String fromClause = null;

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * Il est interdit d'effacer un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * Il est interdit de mettre un objet de ce type à jour.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "n.*,a." + FIELDNAME_DATE_MODIFICATION + " " + FIELDNAME_ANCIEN_DATE + ",a." + FIELDNAME_CONCERNE + " "
                + FIELDNAME_ANCIEN_CONCERNE + ",a." + FIELDNAME_MONTANT_SOLDE + " " + FIELDNAME_ANCIEN_MONTANT + ",a."
                + FIELDNAME_VISA_GEST + " " + FIELDNAME_ANCIEN_GESTIONNAIRE;
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        ancienDate = statement.dbReadDateAMJ(FIELDNAME_ANCIEN_DATE);
        ancienConcerne = statement.dbReadString(FIELDNAME_ANCIEN_CONCERNE);
        ancienMontant = statement.dbReadNumeric(FIELDNAME_ANCIEN_MONTANT);
        ancienGestionnaire = statement.dbReadNumeric(FIELDNAME_ANCIEN_GESTIONNAIRE);

    }

    /**
     * @return the ancienConcerne
     */
    public String getAncienConcerne() {
        return ancienConcerne;
    }

    /**
     * @return the ancienDate
     */
    public String getAncienDate() {
        return ancienDate;
    }

    /**
     * @return the ancienGestionnaire
     * @throws Exception
     */
    public String getAncienGestionnaire() throws Exception {
        return ancienGestionnaire;
    }

    /**
     * @return the ancienMontant
     */
    public String getAncienMontant() {
        return ancienMontant;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    /**
     * @param ancienConcerne
     *            the ancienConcerne to set
     */
    public void setAncienConcerne(String ancienConcerne) {
        this.ancienConcerne = ancienConcerne;
    }

    /**
     * @param ancienDate
     *            the ancienDate to set
     */
    public void setAncienDate(String ancienDate) {
        this.ancienDate = ancienDate;
    }

    /**
     * @param ancienGestionnaire
     *            the ancienGestionnaire to set
     */
    public void setAncienGestionnaire(String ancienGestionnaire) {
        this.ancienGestionnaire = ancienGestionnaire;
    }

    /**
     * @param ancienMontant
     *            the ancienMontant to set
     */
    public void setAncienMontant(String ancienMontant) {
        this.ancienMontant = ancienMontant;
    }

}
