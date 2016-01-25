/*
 * Créé le 28 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.lots;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRAbstractManagerHierarchique;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APFactureJointCompensationManager extends PRAbstractManagerHierarchique {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String FIELDNAME_TI_IDTIERS = "HTITIE";
    private static final String TABLE_COMPENSATIONS_NAME = "APCOMPP";

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
        select.append(APCompensation.FIELDNAME_IDCOMPENSATION);
        select.append(",");
        select.append(APCompensation.FIELDNAME_IDLOT);
        select.append(",");
        select.append(APCompensation.FIELDNAME_IDTIERS);
        select.append(",");
        select.append(APCompensation.FIELDNAME_IDAFFILIE);
        select.append(",");
        select.append(APCompensation.FIELDNAME_MONTANTTOTAL);
        select.append(",");
        select.append(APCompensation.FIELDNAME_DETTES);
        select.append(",");
        select.append(APFactureJointCompensation.FIELDNAME_PRENOMTIERS);
        select.append(",");
        select.append(APFactureJointCompensation.FIELDNAME_NOMTIERS);

        // selection de valeurs nulles pour que les nombres de colonnes des deux
        // sous-requetes soient identiques
        select.append(", 0 AS ");
        select.append(APFactureACompenser.FIELDNAME_IDFACTACOMPENSER);
        select.append(", 0 AS ");
        select.append(APFactureACompenser.FIELDNAME_MONTANTACOMPENSER);
        select.append(", 0 AS ");
        select.append(APFactureACompenser.FIELDNAME_IDFACTURE);
        select.append(", 0 AS ");
        select.append(APFactureACompenser.FIELDNAME_IDCOMPENSATIONPARENTE);
        select.append(", ");
        select.append(BConstants.DB_BOOLEAN_FALSE_DELIMITED);
        select.append(" AS ");
        select.append(APFactureACompenser.FIELDNAME_ISCOMPENSER);
        select.append(", 0 AS ");
        select.append(APFactureACompenser.FIELDNAME_IDTIERS);
        select.append(", 0 AS ");
        select.append(APFactureACompenser.FIELDNAME_IDAFFILIE);
        select.append(", 0 AS ");
        select.append(APFactureACompenser.FIELDNAME_IDADRESSE);
        select.append(", 0 AS ");
        select.append(APFactureACompenser.FIELDNAME_DOMAINEADRESSE);
        select.append(", 0 AS ");
        select.append(APFactureACompenser.FIELDNAME_NO_FACTURE);

        // un marqueur en dur pour differencier les résultats des deux requetes
        select.append(", ");
        select.append(BConstants.DB_BOOLEAN_TRUE_DELIMITED);
        select.append(" AS ");
        select.append(APFactureJointCompensation.FIELDNAME_ISLIGNECOMPENSATION);

        select.append(" FROM ");
        select.append(schema);
        select.append(APFactureJointCompensationManager.TABLE_COMPENSATIONS_NAME);

        // jointure avec la table des tiers pour recuperer le nom du tiers
        select.append(" INNER JOIN ");
        select.append(schema);
        select.append(APFactureJointCompensationManager.TABLE_TIERS);
        select.append(" ON ");
        select.append(APCompensation.FIELDNAME_IDTIERS);
        select.append("=");
        select.append(APFactureJointCompensationManager.FIELDNAME_TI_IDTIERS);

        if (!JadeStringUtil.isEmpty(whereClause)) {
            select.append(" WHERE ");
            select.append(whereClause);
        }

        // deuxieme requete, union avec la table des factures a payer
        select.append(" UNION SELECT ");

        // selection de valeurs nulles pour que les nombres de colonnes des deux
        // sous-requetes soient identiques
        select.append(APCompensation.FIELDNAME_IDCOMPENSATION);
        select.append(",");
        select.append(APCompensation.FIELDNAME_IDLOT);
        select.append(",");
        select.append(APCompensation.FIELDNAME_IDTIERS);
        select.append(",");
        select.append(APCompensation.FIELDNAME_IDAFFILIE);
        select.append(",");
        select.append(APCompensation.FIELDNAME_MONTANTTOTAL);
        select.append(",");
        select.append(APCompensation.FIELDNAME_DETTES);

        // selection des champs de la table des factures à compenser
        select.append(",");
        select.append(APFactureJointCompensation.FIELDNAME_PRENOMTIERS);
        select.append(",");
        select.append(APFactureJointCompensation.FIELDNAME_NOMTIERS);
        select.append(",");
        select.append(APFactureACompenser.FIELDNAME_IDFACTACOMPENSER);
        select.append(",");
        select.append(APFactureACompenser.FIELDNAME_MONTANTACOMPENSER);
        select.append(",");
        select.append(APFactureACompenser.FIELDNAME_IDFACTURE);
        select.append(",");
        select.append(APFactureACompenser.FIELDNAME_IDCOMPENSATIONPARENTE);
        select.append(",");
        select.append(APFactureACompenser.FIELDNAME_ISCOMPENSER);
        select.append(",");
        select.append(APFactureACompenser.FIELDNAME_IDTIERS);
        select.append(",");
        select.append(APFactureACompenser.FIELDNAME_IDAFFILIE);
        select.append(",");
        select.append(APFactureACompenser.FIELDNAME_IDADRESSE);
        select.append(",");
        select.append(APFactureACompenser.FIELDNAME_DOMAINEADRESSE);
        select.append(",");
        select.append(APFactureACompenser.FIELDNAME_NO_FACTURE);

        // un marqueur en dur pour differencier les résultats des deux requetes
        select.append(", ");
        select.append(BConstants.DB_BOOLEAN_FALSE_DELIMITED);
        select.append(" AS ");
        select.append(APFactureJointCompensation.FIELDNAME_ISLIGNECOMPENSATION);

        select.append(" FROM ");
        select.append(schema);
        select.append(APFactureACompenser.TABLE_NAME);

        // jointure avec la table des compensations pour recuperer la
        // compensation
        select.append(" INNER JOIN ");
        select.append(schema);
        select.append(APCompensation.TABLE_NAME);
        select.append(" ON ");
        select.append(APFactureACompenser.FIELDNAME_IDCOMPENSATIONPARENTE);
        select.append("=");
        select.append(APCompensation.FIELDNAME_IDCOMPENSATION);

        // jointure avec la table des tiers pour recuperer le nom du tiers
        select.append(" INNER JOIN ");
        select.append(schema);
        select.append(APFactureJointCompensationManager.TABLE_TIERS);
        select.append(" ON ");
        select.append(APFactureACompenser.FIELDNAME_IDTIERS);
        select.append("=");
        select.append(APFactureJointCompensationManager.FIELDNAME_TI_IDTIERS);

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

        if (!JadeStringUtil.isEmpty(forIdCompensation)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(APCompensation.FIELDNAME_IDCOMPENSATION);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), forIdCompensation));
        }

        if (!JadeStringUtil.isEmpty(forIdLot)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(APCompensation.FIELDNAME_IDLOT);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), forIdLot));
        }

        return whereClause.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APFactureJointCompensation();
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
        return APCompensation.FIELDNAME_IDCOMPENSATION;
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
