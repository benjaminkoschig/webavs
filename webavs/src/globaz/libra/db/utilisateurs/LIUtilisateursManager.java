package globaz.libra.db.utilisateurs;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.libra.db.groupes.LIGroupes;

public class LIUtilisateursManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDomaine = new String();
    private String forIdGroupe = new String();
    private String forIdUserExterne = new String();
    private Boolean forIsDefault = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LIUtilisateursManager.
     */
    public LIUtilisateursManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String schema = _getCollection();

        fromClauseBuffer.append(super._getFrom(statement));

        if (!JadeStringUtil.isIntegerEmpty(forIdDomaine)) {
            // jointure entre table des users et table des groupes
            fromClauseBuffer.append(" INNER JOIN ");
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(LIGroupes.TABLE_NAME);
            fromClauseBuffer.append(" ON ");
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(LIGroupes.TABLE_NAME);
            fromClauseBuffer.append(".");
            fromClauseBuffer.append(LIGroupes.FIELDNAME_ID_GROUPE);
            fromClauseBuffer.append("=");
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(LIUtilisateurs.TABLE_NAME);
            fromClauseBuffer.append(".");
            fromClauseBuffer.append(LIUtilisateurs.FIELDNAME_ID_GROUPE);
        }

        return fromClauseBuffer.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return LIUtilisateurs.FIELDNAME_ID_UTILISATEUR;
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forIdGroupe)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LIUtilisateurs.FIELDNAME_ID_GROUPE);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdGroupe));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDomaine)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(LIGroupes.FIELDNAME_ID_DOMAINE);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdDomaine));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdUserExterne)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LIUtilisateurs.FIELDNAME_ID_UTILISATEUR_EXTERNE);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteString(statement.getTransaction(), forIdUserExterne));
        }

        if ((getForIsDefault() != Boolean.FALSE) && (getForIsDefault() != null)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            if (getForIsDefault().booleanValue()) {
                sqlWhere.append(LIUtilisateurs.FIELDNAME_IS_DEFAULT
                        + "="
                        + _dbWriteBoolean(statement.getTransaction(), getForIsDefault(),
                                BConstants.DB_TYPE_BOOLEAN_CHAR));

            }
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (LIUtilisateurs)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LIUtilisateurs();
    }

    // ~ Getter & Setter
    // -----------------------------------------------------------------------------------------------------

    public String getForIdDomaine() {
        return forIdDomaine;
    }

    public String getForIdGroupe() {
        return forIdGroupe;
    }

    public String getForIdUserExterne() {
        return forIdUserExterne;
    }

    public Boolean getForIsDefault() {
        return forIsDefault;
    }

    public void setForIdDomaine(String forIdDomaine) {
        this.forIdDomaine = forIdDomaine;
    }

    public void setForIdGroupe(String forIdGroupe) {
        this.forIdGroupe = forIdGroupe;
    }

    public void setForIdUserExterne(String forIdUserExterne) {
        this.forIdUserExterne = forIdUserExterne;
    }

    public void setForIsDefault(Boolean forIsDefault) {
        this.forIsDefault = forIsDefault;
    }

}