/*
 * Créé le 16 fevr. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * @author SCR
 * 
 */

public class RERentesAccordeesJoinDemRenteManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forNoDemandeRente = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFields(BStatement statement) {

        String fields = "";

        fields += RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE + ", ";
        fields += REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE + ", ";
        fields += REPrestationsAccordees.FIELDNAME_CODE_PRESTATION + ", ";
        fields += REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT + ", ";
        fields += REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + ", ";
        fields += REPrestationsAccordees.FIELDNAME_CS_ETAT + " ";

        return fields;
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        // String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";
        String schema = _getCollection();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);

        // jointure entre table des prestations accordées et rentes accordee
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);

        // jointure entre table des bases de calculs et rentes accordee
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL);

        // jointure entre table des rentes calculees et table des bases de
        // calculs
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REBasesCalcul.FIELDNAME_ID_RENTE_CALCULEE);

        // jointure entre table des demandes de rentes et table des rentes
        // calculees
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);

        return fromClauseBuffer.toString();

    }

    /**
     * Renvoie la clause WHERE de la requête SQL
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */

    @Override
    protected String _getWhere(BStatement statement) {

        String sqlWhere = "";
        String schema = _getCollection();

        if (!JadeStringUtil.isIntegerEmpty(forNoDemandeRente)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REDemandeRente.TABLE_NAME_DEMANDE_RENTE + "."
                    + REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forNoDemandeRente);
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
        return new RERentesAccordeesJoinDemRente();
    }

    /**
     * @return
     */
    public String getForNoDemandeRente() {
        return forNoDemandeRente;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {

        return RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE;

    }

    /**
     * @param string
     */
    public void setForNoDemandeRente(String string) {
        forNoDemandeRente = string;
    }

}
