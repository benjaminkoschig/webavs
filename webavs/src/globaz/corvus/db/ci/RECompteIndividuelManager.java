/*
 * Créé le 15 janv. 07
 */

package globaz.corvus.db.ci;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * @author hpe
 * 
 */

public class RECompteIndividuelManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdTiers = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Renvoie la clause WHERE de la requête SQL
     * 
     * @param (BStatement)statement!
     * 
     * @return la clause WHERE de la requête SQL!
     */

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isEmpty(getForIdTiers())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += RECompteIndividuel.FIELDNAME_ID_TIERS + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RECompteIndividuel();
    }

    /**
     * getter pour l'attribut forIdTiers
     * 
     * @return la valeur courante de l'attribut forIdDemandeRente
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return RECompteIndividuel.FIELDNAME_ID_TIERS;
    }

    /**
     * setter pour l'attribut forIdTiers.
     * 
     * @param forIdTiers
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdTiers(String string) {
        forIdTiers = string;
    }

}
