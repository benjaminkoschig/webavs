/*
 * Créé le 3 juin 05
 */
package globaz.apg.db.prestation;

import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Manager liant droit APG et prestation</H1>
 * 
 * @author dvh
 */
public class APDroitAPGJointLAPGJointPrestationManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        // jointure entre table des demandes et les tables des droits
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APPrestation.TABLE_NAME);
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPG.TABLE_NAME_LAPG);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPG.TABLE_NAME_LAPG);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APPrestation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APPrestation.FIELDNAME_IDDROIT);
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitAPG.TABLE_NAME_DROIT_APG);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitAPG.TABLE_NAME_DROIT_APG);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APDroitAPG.FIELDNAME_IDDROIT_APG);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APPrestation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APPrestation.FIELDNAME_IDDROIT);

        return fromClauseBuffer.toString();
    }

    private String forEtatDroitDifferentDe = "";
    private String forEtatPrestation = "";
    private String notForGenre = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String fromClause = null;

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
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(getForEtatPrestation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_ETAT + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForEtatPrestation());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForEtatDroitDifferentDe())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APDroitLAPG.TABLE_NAME_LAPG + "." + APDroitLAPG.FIELDNAME_ETAT + "<>"
                    + _dbWriteNumeric(statement.getTransaction(), getForEtatDroitDifferentDe());
        }

        if (!JadeStringUtil.isIntegerEmpty(notForGenre)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_GENRE_PRESTATION
                    + "<>" + this._dbWriteNumeric(statement.getTransaction(), notForGenre);
        }

        return sqlWhere;
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APPrestation();
    }

    /**
     * getter pour l'attribut for etat droit different de
     * 
     * @return la valeur courante de l'attribut for etat droit different de
     */
    public String getForEtatDroitDifferentDe() {
        return forEtatDroitDifferentDe;
    }

    /**
     * getter pour l'attribut for etat prestation
     * 
     * @return la valeur courante de l'attribut for etat prestation
     */
    public String getForEtatPrestation() {
        return forEtatPrestation;
    }

    /**
     * setter pour l'attribut for etat droit different de
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForEtatDroitDifferentDe(String string) {
        forEtatDroitDifferentDe = string;
    }

    /**
     * setter pour l'attribut for etat prestation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForEtatPrestation(String string) {
        forEtatPrestation = string;
    }

    public String getNotForGenre() {
        return notForGenre;
    }

    public void setNotForGenre(String notForGenre) {
        this.notForGenre = notForGenre;
    }
}
