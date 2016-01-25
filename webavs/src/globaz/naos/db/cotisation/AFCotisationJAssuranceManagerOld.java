package globaz.naos.db.cotisation;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFCotisationJAssuranceManagerOld extends AFCotisationManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forGenreAssurance;
    private String forTypeAssurance;
    private String notForTypeAssurance;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return super._getFrom(statement) + " INNER JOIN " + _getCollection() + "AFASSUP ON " + _getCollection()
                + "AFCOTIP.MBIASS=" + _getCollection() + "AFASSUP.MBIASS";
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return _getCollection() + "AFCOTIP.MBIASS, MEDDEB";
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = super._getWhere(statement);

        if (!JadeStringUtil.isEmpty(forTypeAssurance)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MBTTYP=" + this._dbWriteNumeric(statement.getTransaction(), forTypeAssurance);
        }

        if (!JadeStringUtil.isEmpty(notForTypeAssurance)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MBTTYP<>" + this._dbWriteNumeric(statement.getTransaction(), notForTypeAssurance);
        }

        if (!JadeStringUtil.isEmpty(forGenreAssurance)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MBTGEN=" + this._dbWriteNumeric(statement.getTransaction(), forGenreAssurance);
        }

        JadeStringUtil.change(sqlWhere, "MBIASS", _getCollection() + "AFCOTIP.MBIASS");

        return sqlWhere;
    }

    /**
     * getter pour l'attribut for genre assurance.
     * 
     * @return la valeur courante de l'attribut for genre assurance
     */
    @Override
    public String getForGenreAssurance() {
        return forGenreAssurance;
    }

    /**
     * getter pour l'attribut for type assurance.
     * 
     * @return la valeur courante de l'attribut for type assurance
     */
    @Override
    public String getForTypeAssurance() {
        return forTypeAssurance;
    }

    /**
     * getter pour l'attribut not for type assurance.
     * 
     * @return la valeur courante de l'attribut not for type assurance
     */
    @Override
    public String getNotForTypeAssurance() {
        return notForTypeAssurance;
    }

    /**
     * setter pour l'attribut for genre assurance.
     * 
     * @param forGenreAssurance
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setForGenreAssurance(String forGenreAssurance) {
        this.forGenreAssurance = forGenreAssurance;
    }

    /**
     * setter pour l'attribut for type assurance.
     * 
     * @param forTypeAssurance
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setForTypeAssurance(String forTypeAssurance) {
        this.forTypeAssurance = forTypeAssurance;
    }

    /**
     * setter pour l'attribut not for type assurance.
     * 
     * @param notForTypeAssurance
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setNotForTypeAssurance(String notForTypeAssurance) {
        this.notForTypeAssurance = notForTypeAssurance;
    }
}
