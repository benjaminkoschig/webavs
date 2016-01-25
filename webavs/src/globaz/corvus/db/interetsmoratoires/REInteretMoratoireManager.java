package globaz.corvus.db.interetsmoratoires;

import globaz.corvus.db.demandes.REDemandeRente;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class REInteretMoratoireManager extends RECalculInteretMoratoireManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtat = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        StringBuffer fromClauseBuffer = new StringBuffer(super._getFrom(statement));

        // jointure entre table des calcul interets moratoires et table des
        // interets moratoires
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REInteretMoratoire.TABLE_NAME_INTERET_MORATOIRE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RECalculInteretMoratoire.TABLE_NAME_CALCUL_INTERET_MORATOIRE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RECalculInteretMoratoire.FIELDNAME_ID_INTERET_MORATOIRE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REInteretMoratoire.TABLE_NAME_INTERET_MORATOIRE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REInteretMoratoire.FIELDNAME_ID_INTERET_MORATOIRE);

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

        if (!JadeStringUtil.isIntegerEmpty(getForIdDemandeRente())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), getForIdDemandeRente()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsEtat())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REDemandeRente.FIELDNAME_CS_ETAT);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), getForCsEtat()));
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
        return new REInteretMoratoire();
    }

    /**
     * @return
     */
    public String getForCsEtat() {
        return forCsEtat;
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
        return REInteretMoratoire.FIELDNAME_ID_INTERET_MORATOIRE;
    }

    /**
     * @param string
     */
    public void setForCsEtat(String string) {
        forCsEtat = string;
    }

}
