/*
 * Créé le 5 octobre 05
 */
package globaz.ij.db.lots;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRAbstractManagerHierarchique;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJFactureJointCompensationManager extends PRAbstractManagerHierarchique {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String FIELDNAME_TI_IDTIERS = "HTITIE";
    private static final String TABLE_COMPENSATIONS_NAME = "IJCOMPEN";

    private static final String TABLE_TIERS = "TITIERP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forIdCompensation = "";
    private String forIdLot = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {
        StringBuffer select = new StringBuffer();
        String whereClause = _getWhere(statement);
        String schema = _getCollection();

        // première requête, dans la table des compensations
        select.append("SELECT ");

        // selection des champs de la table des compensations
        select.append(IJCompensation.FIELDNAME_IDCOMPENSATION);
        select.append(",");
        select.append(IJCompensation.FIELDNAME_IDLOT);
        select.append(",");
        select.append(IJCompensation.FIELDNAME_IDTIERS);
        select.append(",");
        select.append(IJCompensation.FIELDNAME_IDAFFILIE);
        select.append(",");
        select.append(IJCompensation.FIELDNAME_MONTANTTOTAL);
        select.append(",");
        select.append(IJCompensation.FIELDNAME_DETTES);
        select.append(",");
        select.append(IJFactureJointCompensation.FIELDNAME_PRENOMTIERS);
        select.append(",");
        select.append(IJFactureJointCompensation.FIELDNAME_NOMTIERS);

        // selection de valeurs nulles pour que les nombres de colonnes des deux
        // sous-requetes soient identiques
        select.append(", 0 AS ");
        select.append(IJFactureACompenser.FIELDNAME_IDFACTUREACOMPENSER);
        select.append(", 0 AS ");
        select.append(IJFactureACompenser.FIELDNAME_MONTANT);
        select.append(", 0 AS ");
        select.append(IJFactureACompenser.FIELDNAME_IDFACTURECOMPTA);
        select.append(", 0 AS ");
        select.append(IJFactureACompenser.FIELDNAME_IDCOMPENSATIONPARENTE);
        select.append(", ");
        select.append(BConstants.DB_BOOLEAN_FALSE_DELIMITED);
        select.append(" AS ");
        select.append(IJFactureACompenser.FIELDNAME_ISCOMPENSE);
        select.append(", 0 AS ");
        select.append(IJFactureACompenser.FIELDNAME_IDTIERS);
        select.append(", 0 AS ");
        select.append(IJFactureACompenser.FIELDNAME_IDAFFILIE);
        select.append(", 0 AS ");
        select.append(IJFactureACompenser.FIELDNAME_IDADRESSEPAIEMENT);
        select.append(", 0 AS ");
        select.append(IJFactureACompenser.FIELDNAME_DOMAINEAPPLICATION);
        select.append(", 0 AS ");
        select.append(IJFactureACompenser.FIELDNAME_NOFACTURE);

        // un marqueur en dur pour differencier les résultats des deux requetes
        select.append(", ");
        select.append(BConstants.DB_BOOLEAN_TRUE_DELIMITED);
        select.append(" AS ");
        select.append(IJFactureJointCompensation.FIELDNAME_ISLIGNECOMPENSATION);

        select.append(" FROM ");
        select.append(schema);
        select.append(TABLE_COMPENSATIONS_NAME);

        // jointure avec la table des tiers pour recuperer le nom du tiers
        select.append(" INNER JOIN ");
        select.append(schema);
        select.append(TABLE_TIERS);
        select.append(" ON ");
        select.append(IJCompensation.FIELDNAME_IDTIERS);
        select.append("=");
        select.append(FIELDNAME_TI_IDTIERS);

        if (!JadeStringUtil.isEmpty(whereClause)) {
            select.append(" WHERE ");
            select.append(whereClause);
        }

        // deuxieme requete, union avec la table des factures a payer
        select.append(" UNION SELECT ");

        // selection de valeurs nulles pour que les nombres de colonnes des deux
        // sous-requetes soient identiques
        select.append(IJCompensation.FIELDNAME_IDCOMPENSATION);
        select.append(",");
        select.append(IJCompensation.FIELDNAME_IDLOT);
        select.append(",");
        select.append(IJCompensation.FIELDNAME_IDTIERS);
        select.append(",");
        select.append(IJCompensation.FIELDNAME_IDAFFILIE);
        select.append(",");
        select.append(IJCompensation.FIELDNAME_MONTANTTOTAL);
        select.append(",");
        select.append(IJCompensation.FIELDNAME_DETTES);

        // selection des champs de la table des factures à compenser
        select.append(",");
        select.append(IJFactureJointCompensation.FIELDNAME_PRENOMTIERS);
        select.append(",");
        select.append(IJFactureJointCompensation.FIELDNAME_NOMTIERS);
        select.append(",");
        select.append(IJFactureACompenser.FIELDNAME_IDFACTUREACOMPENSER);
        select.append(",");
        select.append(IJFactureACompenser.FIELDNAME_MONTANT);
        select.append(",");
        select.append(IJFactureACompenser.FIELDNAME_IDFACTURECOMPTA);
        select.append(",");
        select.append(IJFactureACompenser.FIELDNAME_IDCOMPENSATIONPARENTE);
        select.append(",");
        select.append(IJFactureACompenser.FIELDNAME_ISCOMPENSE);
        select.append(",");
        select.append(IJFactureACompenser.FIELDNAME_IDTIERS);
        select.append(",");
        select.append(IJFactureACompenser.FIELDNAME_IDAFFILIE);
        select.append(",");
        select.append(IJFactureACompenser.FIELDNAME_IDADRESSEPAIEMENT);
        select.append(",");
        select.append(IJFactureACompenser.FIELDNAME_DOMAINEAPPLICATION);
        select.append(",");
        select.append(IJFactureACompenser.FIELDNAME_NOFACTURE);

        // un marqueur en dur pour differencier les résultats des deux requetes
        select.append(", ");
        select.append(BConstants.DB_BOOLEAN_FALSE_DELIMITED);
        select.append(" AS ");
        select.append(IJFactureJointCompensation.FIELDNAME_ISLIGNECOMPENSATION);

        select.append(" FROM ");
        select.append(schema);
        select.append(IJFactureACompenser.TABLE_NAME);

        // jointure avec la table des compensations pour recuperer la
        // compensation
        select.append(" INNER JOIN ");
        select.append(schema);
        select.append(IJCompensation.TABLE_NAME);
        select.append(" ON ");
        select.append(IJFactureACompenser.FIELDNAME_IDCOMPENSATIONPARENTE);
        select.append("=");
        select.append(IJCompensation.FIELDNAME_IDCOMPENSATION);

        // jointure avec la table des tiers pour recuperer le nom du tiers
        select.append(" INNER JOIN ");
        select.append(schema);
        select.append(TABLE_TIERS);
        select.append(" ON ");
        select.append(IJFactureACompenser.FIELDNAME_IDTIERS);
        select.append("=");
        select.append(FIELDNAME_TI_IDTIERS);

        if (!JadeStringUtil.isEmpty(whereClause)) {
            select.append(" WHERE ");
            select.append(whereClause);
        }

        select.append(" ORDER BY ").append(getHierarchicalOrderBy());

        return select.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forIdCompensation)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJCompensation.FIELDNAME_IDCOMPENSATION);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), forIdCompensation));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdLot)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJCompensation.FIELDNAME_IDLOT);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), forIdLot));
        }

        return whereClause.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJFactureJointCompensation();
    }

    /**
     * getter pour l'attribut for id compensation
     * 
     * @return la valeur courante de l'attribut for id compensation
     */
    public String getForIdCompensation() {
        return forIdCompensation;
    }

    /**
     * getter pour l'attribut for id lot
     * 
     * @return la valeur courante de l'attribut for id lot
     */
    public String getForIdLot() {
        return forIdLot;
    }

    @Override
    public String getHierarchicalOrderBy() {
        return getOrderByDefaut();
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
        return IJCompensation.FIELDNAME_IDCOMPENSATION;
    }

    /**
     * setter pour l'attribut for id compensation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdCompensation(String string) {
        forIdCompensation = string;
    }

    /**
     * setter pour l'attribut for id lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdLot(String string) {
        forIdLot = string;
    }

}
