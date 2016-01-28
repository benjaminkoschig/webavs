package globaz.corvus.db.ci;

import globaz.corvus.db.annonces.REAnnonceInscriptionCI;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.pyxis.db.tiers.ITITiersDefTable;

/**
 * N'est utilisé que pour la génération de la liste des CI additionnels
 * 
 * @author BSC
 */
public class REListeCiAdditionnelsManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String ALIAS_MONTANT_TOTAL = "MONT_TOT";

    private String forDateDebut = "";
    private String forDateFin = "";
    private TypeListeCiAdditionnels forGenreCiAdd = TypeListeCiAdditionnels.RECEPTIONNES;

    @Override
    protected String _getGroupBy(BStatement statement) {
        StringBuilder sql = new StringBuilder(" GROUP BY ");
        sql.append(REListeCiAdditionnels.generateFields(_getCollection(), false));

        return sql.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        // ici on ne veut que des CI add.
        sql.append(_getCollection()).append(RERassemblementCI.TABLE_NAME_RCI).append(".")
                .append(RERassemblementCI.FIELDNAME_ID_PARENT);
        sql.append(">");
        sql.append(this._dbWriteNumeric(statement.getTransaction(), "0"));

        StringBuilder whereForType = getSqlWhereForType(getForGenreCiAdd(), statement);

        whereForType.append(_getGroupBy(statement));

        return whereForType.toString();
    }

    @Override
    protected REListeCiAdditionnels _newEntity() throws Exception {
        return new REListeCiAdditionnels();
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public TypeListeCiAdditionnels getForGenreCiAdd() {
        return forGenreCiAdd;
    }

    @Override
    public String getOrderBy() {
        return getOrderByDefaut();
    }

    @Override
    public String getOrderByDefaut() {
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;

        StringBuilder sql = new StringBuilder();

        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_1).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_2);

        return sql.toString();
    }

    private StringBuilder getSqlWhereForType(TypeListeCiAdditionnels typeListeCiAdditionnels, BStatement statement) {
        String tableRassemblementCI = _getCollection() + RERassemblementCI.TABLE_NAME_RCI;
        String tableAnnonceInscriptionCI = _getCollection() + REAnnonceInscriptionCI.TABLE_NAME_ANNONCE_INSCRIPTION_CI;

        StringBuilder sql = new StringBuilder();
        switch (typeListeCiAdditionnels) {
            case TRAITES:
                sql.append(_getCollection()).append(RERassemblementCI.TABLE_NAME_RCI).append(".");
                sql.append(RERassemblementCI.FIELDNAME_ID_PARENT);
                sql.append(">");
                sql.append(this._dbWriteNumeric(statement.getTransaction(), "0"));

                if (!JadeStringUtil.isEmpty(getForDateDebut())) {
                    if (sql.length() > 0) {
                        sql.append(" AND ");
                    }
                    sql.append(RERassemblementCI.FIELDNAME_DATE_TRAITEMENT);
                    sql.append(">=");
                    sql.append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebut()));
                }

                if (!JadeStringUtil.isEmpty(getForDateFin())) {
                    if (sql.length() > 0) {
                        sql.append(" AND ");
                    }
                    sql.append(tableRassemblementCI).append(".").append(RERassemblementCI.FIELDNAME_DATE_TRAITEMENT);
                    sql.append("<=");
                    sql.append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateFin()));
                }

                if (sql.length() > 0) {
                    sql.append(" AND ");
                }
                sql.append("(");
                sql.append("NOT ").append(tableRassemblementCI).append(".")
                        .append(RERassemblementCI.FIELDNAME_DATE_TRAITEMENT).append(" IS NULL");
                sql.append(" AND ");
                sql.append("NOT ").append(tableRassemblementCI).append(".")
                        .append(RERassemblementCI.FIELDNAME_DATE_TRAITEMENT).append("=0");
                sql.append(")");
                break;
            case NON_TRAITES:
                // sql.append(this.getSqlWhereForType(TypeListeCiAdditionnels.RECEPTIONNES, statement));
                sql.append(_getCollection()).append(RERassemblementCI.TABLE_NAME_RCI).append(".");
                sql.append(RERassemblementCI.FIELDNAME_ID_PARENT);
                sql.append(">");
                sql.append(this._dbWriteNumeric(statement.getTransaction(), "0"));
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }
                sql.append("(");
                sql.append(tableRassemblementCI).append(".").append(RERassemblementCI.FIELDNAME_DATE_TRAITEMENT)
                        .append(" IS NULL");
                sql.append(" OR ");
                sql.append(tableRassemblementCI).append(".").append(RERassemblementCI.FIELDNAME_DATE_TRAITEMENT)
                        .append("=0");
                sql.append(")");

                if (!JadeStringUtil.isEmpty(getForDateDebut())) {
                    if (sql.length() > 0) {
                        sql.append(" AND ");
                    }
                    sql.append(tableRassemblementCI).append(".").append(RERassemblementCI.FIELDNAME_DATE_RASSEMBLEMENT);
                    sql.append(">=");
                    sql.append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebut()));
                }

                if (!JadeStringUtil.isEmpty(getForDateFin())) {
                    if (sql.length() > 0) {
                        sql.append(" AND ");
                    }
                    sql.append(tableRassemblementCI).append(".").append(RERassemblementCI.FIELDNAME_DATE_RASSEMBLEMENT);
                    sql.append("<=");
                    sql.append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateFin()));
                }

                break;
            case RECEPTIONNES:
                sql.append(_getCollection()).append(RERassemblementCI.TABLE_NAME_RCI).append(".");
                sql.append(RERassemblementCI.FIELDNAME_ID_PARENT);
                sql.append(">");
                sql.append(this._dbWriteNumeric(statement.getTransaction(), "0"));
                if (!JadeStringUtil.isEmpty(getForDateDebut())) {
                    if (sql.length() > 0) {
                        sql.append(" AND ");
                    }
                    sql.append(tableRassemblementCI).append(".").append(RERassemblementCI.FIELDNAME_DATE_RASSEMBLEMENT);
                    sql.append(">=");
                    sql.append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebut()));
                }

                if (!JadeStringUtil.isEmpty(getForDateFin())) {
                    if (sql.length() > 0) {
                        sql.append(" AND ");
                    }
                    sql.append(tableRassemblementCI).append(".").append(RERassemblementCI.FIELDNAME_DATE_RASSEMBLEMENT);
                    sql.append("<=");
                    sql.append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateFin()));
                }
                break;

            case ATTENTE_CI_ADD_TOUS:
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }
                sql.append(tableAnnonceInscriptionCI).append(".")
                        .append(REAnnonceInscriptionCI.FIELDNAME_ATTENTE_CI_ADDITIONNEL);
                sql.append(" is not null");
                sql.append(" AND ");
                sql.append(tableAnnonceInscriptionCI).append(".")
                        .append(REAnnonceInscriptionCI.FIELDNAME_ATTENTE_CI_ADDITIONNEL);
                sql.append(" <> 0");

                if (!JadeStringUtil.isEmpty(getForDateDebut())) {
                    if (sql.length() > 0) {
                        sql.append(" AND ");
                    }
                    sql.append("CAST(SUBSTR(");
                    sql.append(tableAnnonceInscriptionCI).append(".CSPY");
                    sql.append(",1,8) AS INTEGER)");
                    sql.append(">=");
                    sql.append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebut()));
                }

                if (!JadeStringUtil.isEmpty(getForDateFin())) {
                    if (sql.length() > 0) {
                        sql.append(" AND ");
                    }
                    sql.append("CAST(SUBSTR(");
                    sql.append(tableAnnonceInscriptionCI).append(".CSPY");
                    sql.append(",1,8) AS INTEGER)");
                    sql.append("<=");
                    sql.append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateFin()));
                }

                break;

            case ATTENTE_CI_ADD_PROVISOIRE:
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }
                sql.append(tableAnnonceInscriptionCI).append(".")
                        .append(REAnnonceInscriptionCI.FIELDNAME_ATTENTE_CI_ADDITIONNEL);
                sql.append(" = ");
                sql.append(REAnnonceInscriptionCI.CS_ATTENTE_CI_ADDITIONNEL_PROVISOIRE);

                if (!JadeStringUtil.isEmpty(getForDateDebut())) {
                    if (sql.length() > 0) {
                        sql.append(" AND ");
                    }
                    sql.append("CAST(SUBSTR(");
                    sql.append(tableAnnonceInscriptionCI).append(".CSPY");
                    sql.append(",1,8) AS INTEGER)");
                    sql.append(">=");
                    sql.append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebut()));
                }

                if (!JadeStringUtil.isEmpty(getForDateFin())) {
                    if (sql.length() > 0) {
                        sql.append(" AND ");
                    }
                    sql.append("CAST(SUBSTR(");
                    sql.append(tableAnnonceInscriptionCI).append(".CSPY");
                    sql.append(",1,8) AS INTEGER)");
                    sql.append("<=");
                    sql.append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateFin()));
                }
                break;

            case ATTENTE_CI_ADD_TRAITE:
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }
                sql.append(tableAnnonceInscriptionCI).append(".")
                        .append(REAnnonceInscriptionCI.FIELDNAME_ATTENTE_CI_ADDITIONNEL);
                sql.append(" = ");
                sql.append(REAnnonceInscriptionCI.CS_ATTENTE_CI_ADDITIONNEL_TRAITE);

                if (!JadeStringUtil.isEmpty(getForDateDebut())) {
                    if (sql.length() > 0) {
                        sql.append(" AND ");
                    }
                    sql.append("CAST(SUBSTR(");
                    sql.append(tableAnnonceInscriptionCI).append(".CSPY");
                    sql.append(",1,8) AS INTEGER)");
                    sql.append(">=");
                    sql.append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebut()));
                }

                if (!JadeStringUtil.isEmpty(getForDateFin())) {
                    if (sql.length() > 0) {
                        sql.append(" AND ");
                    }
                    sql.append("CAST(SUBSTR(");
                    sql.append(tableAnnonceInscriptionCI).append(".CSPY");
                    sql.append(",1,8) AS INTEGER)");
                    sql.append("<=");
                    sql.append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateFin()));
                }
                break;
        }
        return sql;
    }

    public void setForDateDebut(String dateDebut) {
        forDateDebut = dateDebut;
    }

    public void setForDateFin(String dateFin) {
        forDateFin = dateFin;
    }

    public void setForGenreCiAdd(TypeListeCiAdditionnels genreCiAdd) {
        forGenreCiAdd = genreCiAdd;
    }

}
