/*
 * Créé le 21 janvier 2010
 */
package globaz.cygnus.db.qds;

import globaz.globall.db.BStatement;
import globaz.prestation.tools.sql.PRFromStringBuffer;

/**
 * @author JJE
 */
public class RFQdHistoriqueSoldeExcedentDeRevenuJointSelf extends RFQdSoldeExcedentDeRevenu {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ANCIEN_CONCERNE = "FTLCON_A";
    public static final String FIELDNAME_ANCIEN_DATE = "FTDMOD_A";
    public static final String FIELDNAME_ANCIEN_GESTIONNAIRE = "FTIGES_A";
    public static final String FIELDNAME_ANCIEN_MONTANT = "FTMSEX_A";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Génération de la clause from pour la requête > Jointure du solde excédent de revenu sur lui-même
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {

        PRFromStringBuffer fromClauseBuffer = new PRFromStringBuffer(schema);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdSoldeExcedentDeRevenu.TABLE_NAME + " n");

        // jointure entre la table des Qd et la table des QdPrinicpale

        fromClauseBuffer.leftJoin(RFQdSoldeExcedentDeRevenu.TABLE_NAME + " a").appendOn()
                .append("n." + RFQdSoldeExcedentDeRevenu.FIELDNAME_ID_SOLDE_EXCEDENT_MODIFIE);
        fromClauseBuffer.appendEgal().append("a." + RFQdSoldeExcedentDeRevenu.FIELDNAME_ID_SOLDE_EXCEDENT);

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
        return "n.*,a." + RFQdSoldeExcedentDeRevenu.FIELDNAME_DATE_MODIFICATION + " "
                + RFQdHistoriqueSoldeExcedentDeRevenuJointSelf.FIELDNAME_ANCIEN_DATE + ",a."
                + RFQdSoldeExcedentDeRevenu.FIELDNAME_CONCERNE + " "
                + RFQdHistoriqueSoldeExcedentDeRevenuJointSelf.FIELDNAME_ANCIEN_CONCERNE + ",a."
                + RFQdSoldeExcedentDeRevenu.FIELDNAME_MONTANT_SOLDE_EXCEDENT + " "
                + RFQdHistoriqueSoldeExcedentDeRevenuJointSelf.FIELDNAME_ANCIEN_MONTANT + ",a."
                + RFQdSoldeExcedentDeRevenu.FIELDNAME_VISA_GEST + " "
                + RFQdHistoriqueSoldeExcedentDeRevenuJointSelf.FIELDNAME_ANCIEN_GESTIONNAIRE;
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = RFQdHistoriqueSoldeExcedentDeRevenuJointSelf.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        ancienDate = statement.dbReadDateAMJ(RFQdHistoriqueSoldeExcedentDeRevenuJointSelf.FIELDNAME_ANCIEN_DATE);
        ancienConcerne = statement.dbReadString(RFQdHistoriqueSoldeExcedentDeRevenuJointSelf.FIELDNAME_ANCIEN_CONCERNE);
        ancienMontant = statement.dbReadNumeric(RFQdHistoriqueSoldeExcedentDeRevenuJointSelf.FIELDNAME_ANCIEN_MONTANT);
        ancienGestionnaire = statement
                .dbReadNumeric(RFQdHistoriqueSoldeExcedentDeRevenuJointSelf.FIELDNAME_ANCIEN_GESTIONNAIRE);

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
