package globaz.libra.db.groupes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * 
 * @author HPE
 * 
 */
public class LIGroupesManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDomaine = new String();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LIGroupesManager.
     */
    public LIGroupesManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getOrder(BStatement statement) {
        return LIGroupes.FIELDNAME_ID_GROUPE;
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forIdDomaine)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LIGroupes.FIELDNAME_ID_DOMAINE);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdDomaine));
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (LIGroupes)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LIGroupes();
    }

    // ~ Getter & Setter
    // -----------------------------------------------------------------------------------------------------

    public String getForIdDomaine() {
        return forIdDomaine;
    }

    public void setForIdDomaine(String forIdDomaine) {
        this.forIdDomaine = forIdDomaine;
    }

}