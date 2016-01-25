/*
 * Créé le 16 fevr. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteVieillesse;
import globaz.corvus.db.demandes.RESituationFamiliale;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author SCR
 * 
 */

public class RERenteAccJoinTblTiersJoinDemRenteJoinAjourManager extends RERenteAccJoinTblTiersJoinDemRenteManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String forEnCoursPourMois = "";
    private boolean isDateDecesVide = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer fromClauseBuffer = new StringBuffer();
        // String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";
        String schema = _getCollection();

        fromClauseBuffer.append(super._getFrom(statement));

        // jointure avec la table demande de rente vieillesse
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDemandeRenteVieillesse.TABLE_NAME_DEMANDE_RENTE_VIEILLESSE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDemandeRenteVieillesse.TABLE_NAME_DEMANDE_RENTE_VIEILLESSE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REDemandeRenteVieillesse.FIELDNAME_ID_DEMANDE_RENTE_VIEILLESSE);

        // jointure avec la table membre famille
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RESituationFamiliale.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RESituationFamiliale.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RESituationFamiliale.FIELD_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteAccJoinTblTiersJoinDemandeRente.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteAccJoinTblTiersJoinDemandeRente.FIELDNAME_ID_TIERS_TI);

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
        // String schema = _getCollection();

        sqlWhere = super._getWhere(statement);

        if (!JadeStringUtil.isEmpty(forEnCoursPourMois)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT + " <= " + forEnCoursPourMois + " AND ( "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " IS NULL OR "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " >= " + forEnCoursPourMois + " OR "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " = 0 ) ";
        }

        if (isDateDecesVide) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "( " + IPRConstantesExternes.FIELDNAME_TABLE_PERSONNE_DATEDECES + " IS NULL OR "
                    + IPRConstantesExternes.FIELDNAME_TABLE_PERSONNE_DATEDECES + " = 0 )";
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
        return new RERenteAccJoinTblTiersJoinDemRenteJoinAjour();
    }

    public String getForEnCoursPourMois() {
        return forEnCoursPourMois;
    }

    public boolean isDateDecesVide() {
        return isDateDecesVide;
    }

    public void setDateDecesVide(boolean isDateDecesVide) {
        this.isDateDecesVide = isDateDecesVide;
    }

    public void setForEnCoursPourMois(String forEnCoursPourMois) {
        this.forEnCoursPourMois = forEnCoursPourMois;
    }

}
