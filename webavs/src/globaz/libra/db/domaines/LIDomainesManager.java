package globaz.libra.db.domaines;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class LIDomainesManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String forCsDomaine = new String();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LIDomainesManager.
     */
    public LIDomainesManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getOrder(BStatement statement) {
        return LIDomaines.FIELDNAME_ID_DOMAINE;
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forCsDomaine)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LIDomaines.FIELDNAME_CS_DOMAINE);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forCsDomaine));
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (LIDomaines)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LIDomaines();
    }

    public String getForCsDomaine() {
        return forCsDomaine;
    }

    public void setForCsDomaine(String forCsDomaine) {
        this.forCsDomaine = forCsDomaine;
    }

}
