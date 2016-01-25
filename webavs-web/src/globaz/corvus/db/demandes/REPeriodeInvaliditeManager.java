/*
 * Créé le 18 janv. 07
 */
package globaz.corvus.db.demandes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * @author hpe
 * 
 */

public class REPeriodeInvaliditeManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDemandeRente = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Renvoie la clause WHERE de la requête SQL
     * 
     * @param (BStatement)statement
     * 
     * @return la clause WHERE de la requête SQL
     */

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isEmpty(getForIdDemandeRente())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPeriodeInvalidite.FIELDNAME_ID_DEMANDE_RENTE + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdDemandeRente());
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
        return new REPeriodeInvalidite();
    }

    /**
     * getter pour l'attribut forIdDemandeRente
     * 
     * @return la valeur courante de l'attribut forIdDemandeRente
     */
    public String getForIdDemandeRente() {
        return forIdDemandeRente;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return REPeriodeInvalidite.FIELDNAME_ID_PERIODE_INVALIDITE;
    }

    /**
     * setter pour l'attribut forIdDemandeRente.
     * 
     * @param forIdDemandeRente
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDemandeRente(String string) {
        forIdDemandeRente = string;
    }

}
