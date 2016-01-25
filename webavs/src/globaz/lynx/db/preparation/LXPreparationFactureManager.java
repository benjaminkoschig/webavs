package globaz.lynx.db.preparation;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.lynx.db.facture.LXFactureManager;
import globaz.lynx.db.fournisseur.LXFournisseur;
import globaz.lynx.db.operation.LXOperation;

public class LXPreparationFactureManager extends LXFactureManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean founissseurIsBloque = null;
    private Boolean isBloque = null;

    /**
     * Surcharge _getFields car dans le cas de la préparation nous voulons tout les champs pour remplir une opération.
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "*";
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXOperation();
    }

    /**
     * Construit les parites spécifiques du where pour la recherche des factures lors de la préparation d'un ordre
     * groupé.
     */
    @Override
    protected String _setWhereCommonPart(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer(super._setWhereCommonPart(statement));

        if (getFounissseurIsBloque() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection())
                    .append(LXFournisseur.TABLE_LXFOURP)
                    .append(".")
                    .append(LXFournisseur.FIELD_ESTBLOQUE)
                    .append(" = ")
                    .append(_dbWriteBoolean(statement.getTransaction(), getFounissseurIsBloque(),
                            BConstants.DB_TYPE_BOOLEAN_CHAR));
        }

        if (getIsBloque() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection())
                    .append(LXOperation.TABLE_LXOPERP)
                    .append(".")
                    .append(LXOperation.FIELD_ESTBLOQUE)
                    .append(" = ")
                    .append(_dbWriteBoolean(statement.getTransaction(), getIsBloque(), BConstants.DB_TYPE_BOOLEAN_CHAR));
        }

        return sqlWhere.toString();

    }

    // *******************************************************
    // Getter
    // *******************************************************

    public Boolean getFounissseurIsBloque() {
        return founissseurIsBloque;
    }

    public Boolean getIsBloque() {
        return isBloque;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setFounissseurIsBloque(Boolean founissseurIsBloque) {
        this.founissseurIsBloque = founissseurIsBloque;
    }

    public void setIsBloque(Boolean isBloque) {
        this.isBloque = isBloque;
    }
}
