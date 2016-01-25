package globaz.corvus.db.prestations;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class REPrestationsManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtat = "";
    private String forIdDecision = "";
    private String forIdDemandeRente = "";
    private String forIdLot = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestations.TABLE_NAME_PRESTATION);

        return fromClauseBuffer.toString();
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForIdLot())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REPrestations.FIELDNAME_ID_LOT);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), getForIdLot()));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDemandeRente)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REPrestations.FIELDNAME_ID_DEMANDE_RENTE);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), forIdDemandeRente));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsEtat())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REPrestations.FIELDNAME_ETAT);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), getForCsEtat()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdDecision())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REPrestations.FIELDNAME_ID_DECISION);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), getForIdDecision()));
        }

        return whereClause.toString();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REPrestations();
    }

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForIdDemandeRente() {
        return forIdDemandeRente;
    }

    /**
     * @return
     */
    public String getForIdLot() {
        return forIdLot;
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut order by defaut
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return REPrestations.FIELDNAME_ID_PRESTATION;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForIdDemandeRente(String forIdDemandeRente) {
        this.forIdDemandeRente = forIdDemandeRente;
    }

    /**
     * @param string
     */
    public void setForIdLot(String string) {
        forIdLot = string;
    }

}
