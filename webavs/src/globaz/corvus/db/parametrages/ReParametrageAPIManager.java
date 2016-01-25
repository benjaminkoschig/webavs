/*
 * Créé le 6 sept. 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.corvus.db.parametrages;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.tools.PRDateFormater;

/**
 * 
 * @author JJE
 */
public class ReParametrageAPIManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String fromDateDebut;
    private String toDateFin;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(ReParametrageAPI.TABLE_NAME_MONTANTAPI);

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

        if (!JadeStringUtil.isIntegerEmpty(getFromDateDebut())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(ReParametrageAPI.FIELDNAME_DATE_DEBUT);
            whereClause.append(">=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(),
                    PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getFromDateDebut())));

        }

        if (!JadeStringUtil.isIntegerEmpty(getToDateFin())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(ReParametrageAPI.FIELDNAME_DATE_FIN);
            whereClause.append("<=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(),
                    PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getToDateFin())));
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
        return new ReParametrageAPI();
    }

    public String getFromDateDebut() {
        return fromDateDebut;
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
        return ReParametrageAPI.FIELDNAME_DATE_DEBUT + " DESC";
    }

    public String getToDateFin() {
        return toDateFin;
    }

    public void setFromDateDebut(String fromDateDebut) {
        this.fromDateDebut = fromDateDebut;
    }

    public void setToDateFin(String toDateFin) {
        this.toDateFin = toDateFin;
    }

}
