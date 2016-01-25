/*
 * Créé le 1 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.lots;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APFactureACompenserManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String exceptForIdFactureACompenser = "";
    private String forIdCompensationParente = "";
    private Boolean forIsCompenser = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + APFactureACompenser.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forIdCompensationParente)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(APFactureACompenser.FIELDNAME_IDCOMPENSATIONPARENTE);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), forIdCompensationParente));
        }

        if (!JadeStringUtil.isIntegerEmpty(exceptForIdFactureACompenser)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(APFactureACompenser.FIELDNAME_IDFACTACOMPENSER);
            whereClause.append("<>");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), exceptForIdFactureACompenser));
        }

        if (forIsCompenser != null) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(APFactureACompenser.FIELDNAME_ISCOMPENSER);
            whereClause.append("=");
            whereClause.append(_dbWriteBoolean(statement.getTransaction(), forIsCompenser,
                    BConstants.DB_TYPE_BOOLEAN_CHAR));
        }

        return whereClause.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APFactureACompenser();
    }

    /**
     * getter pour l'attribut except for id facture ACompenser
     * 
     * @return la valeur courante de l'attribut except for id facture ACompenser
     */
    public String getExceptForIdFactureACompenser() {
        return exceptForIdFactureACompenser;
    }

    /**
     * getter pour l'attribut for id compensation parente
     * 
     * @return la valeur courante de l'attribut for id compensation parente
     */
    public String getForIdCompensationParente() {
        return forIdCompensationParente;
    }

    public Boolean getForIsCompenser() {
        return forIsCompenser;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return APFactureACompenser.FIELDNAME_IDFACTACOMPENSER;
    }

    /**
     * setter pour l'attribut except for id facture ACompenser
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setExceptForIdFactureACompenser(String string) {
        exceptForIdFactureACompenser = string;
    }

    /**
     * setter pour l'attribut for id compensation parente
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdCompensationParente(String string) {
        forIdCompensationParente = string;
    }

    public void setForIsCompenser(Boolean forIsCompenser) {
        this.forIsCompenser = forIsCompenser;
    }

}
