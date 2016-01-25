/**
 * 
 */
package globaz.naos.db.tauxAssurance;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author sel
 * 
 */
public class AFSynchroTauxAssuranceManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String AFASSUP = "AFASSUP";
    private static final String AFTAUXP = "AFTAUXP";
    private String forGenreAssurance = null;
    private String order = null;

    @Override
    protected String _getFields(BStatement statement) {
        return AFSynchroTauxAssuranceManager.AFTAUXP + ".*";
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer(_getCollection());
        sqlFrom.append("AFTAUXP ").append(AFSynchroTauxAssuranceManager.AFTAUXP);

        sqlFrom.append(" INNER JOIN ").append(_getCollection()).append("AFASSUP ")
                .append(AFSynchroTauxAssuranceManager.AFASSUP);
        sqlFrom.append(" ON ").append(AFSynchroTauxAssuranceManager.AFASSUP).append(".MBIASS");
        sqlFrom.append(" = ").append(AFSynchroTauxAssuranceManager.AFTAUXP).append(".MBIASS");

        return sqlFrom.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        StringBuffer sqlWhere = new StringBuffer("");

        if (!JadeStringUtil.isEmpty(getForGenreAssurance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("MBTGEN=").append(this._dbWriteNumeric(statement.getTransaction(), getForGenreAssurance()));
        }

        // ne pas prendre en compte les taux de type moyen
        if (sqlWhere.length() != 0) {
            sqlWhere.append(" AND ");
        }
        // sqlWhere.append("MCTTYP<>")
        // .append(this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_TAUX_MOYEN));
        sqlWhere.append("MCTTYP in (844001, 844005)");

        // ne pas prendre en compte les taux avec une date de fin
        // if (sqlWhere.length() != 0) {
        // sqlWhere.append(" AND ");
        // }
        // Ne prend que les taux actifs
        // sqlWhere.append("(mcdfin = 0 or mcdfin is null)");

        if (sqlWhere.length() != 0) {
            sqlWhere.append(" AND ");
        }
        sqlWhere.append("MCTGEN = 825001 and MCMVER <> 0");

        if (JadeStringUtil.isEmpty(order)) {
            setOrder("MCTGEN, MCDDEB DESC, MCNRAN");
        }

        return sqlWhere.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFTauxAssurance();
    }

    /**
     * @return the forGenreAssurance
     */
    public String getForGenreAssurance() {
        return forGenreAssurance;
    }

    /**
     * @return the order
     */
    public String getOrder() {
        return order;
    }

    /**
     * @param forGenreAssurance
     *            the forGenreAssurance to set
     */
    public void setForGenreAssurance(String forGenreAssurance) {
        this.forGenreAssurance = forGenreAssurance;
    }

    /**
     * @param order
     *            the order to set
     */
    public void setOrder(String order) {
        this.order = order;
    }

}
