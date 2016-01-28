package globaz.naos.db.facturationOrganeExterne;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class AFParticulariteRI_PCManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAffiliation;
    private String forNumAff;

    /**
     * Renvoie la liste des champs.
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return _getCollection() + "AFAFFIP.MAIAFF, " + _getCollection() + "AFAFFIP.MALNAF," + _getCollection()
                + "AFPARTP.MFTPAR";
    }

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFAFFIP INNER JOIN " + _getCollection() + "AFPARTP ON " + _getCollection()
                + "AFAFFIP.MAIAFF = " + _getCollection() + "AFPARTP.MAIAFF ";

    }

    /**
     * Renvoie la composante de tri de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "MAIAFF";
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {

        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        sqlWhere += _getCollection() + "AFPARTP.MFTPAR IN (818013,818014,818015)";

        if (!JadeStringUtil.isEmpty(getForNumAff())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + "AFAFFIP.MALNAF = "
                    + this._dbWriteString(statement.getTransaction(), getForNumAff());
        }
        if (!JadeStringUtil.isEmpty(getForIdAffiliation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + "AFAFFIP.MAIAFF = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdAffiliation());
        }

        sqlWhere += " GROUP BY " + _getFields(statement);

        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité.
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFParticulariteRI_PC();
    }

    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    public String getForNumAff() {
        return forNumAff;
    }

    public void setForIdAffiliation(String forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }

    public void setForNumAff(java.lang.String newforNumAff) {
        forNumAff = newforNumAff;
    }

}
