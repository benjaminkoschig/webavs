package globaz.corvus.db.demandes;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class REDemandeRenteJointBaseCalculManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsTypeDemandeRente = "";
    private String forIdDemandeRente = "";

    @Override
    protected String _getFields(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        sql.append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE).append(",");
        sql.append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);

        return sql.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        sql.append(_getCollection()).append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);

        sql.append(" INNER JOIN ");
        sql.append(_getCollection()).append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        sql.append(" ON ");
        sql.append(_getCollection()).append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE).append(".")
                .append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);
        sql.append("=");
        sql.append(_getCollection()).append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE).append(".")
                .append(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE);

        sql.append(" INNER JOIN ");
        sql.append(_getCollection()).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        sql.append(" ON ");
        sql.append(_getCollection()).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL).append(".")
                .append(REBasesCalcul.FIELDNAME_ID_RENTE_CALCULEE);
        sql.append("=");
        sql.append(_getCollection()).append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE).append(".")
                .append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);

        return sql.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        if (!JadeStringUtil.isBlank(forIdDemandeRente)) {
            sql.append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);
            sql.append("=");
            sql.append(forIdDemandeRente);
        }

        if (!JadeStringUtil.isBlank(forCsTypeDemandeRente)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(REDemandeRente.FIELDNAME_CS_TYPE_DEMANDE_RENTE);
            sql.append("=");
            sql.append(forCsTypeDemandeRente);
        }

        return sql.length() > 0 ? sql.toString() : null;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REDemandeRenteJointBaseCalcul();
    }

    public String getForCsTypeDemandeRente() {
        return forCsTypeDemandeRente;
    }

    public String getForIdDemandeRente() {
        return forIdDemandeRente;
    }

    public void setForCsTypeDemandeRente(String csTypeDemandeRente) {
        forCsTypeDemandeRente = csTypeDemandeRente;
    }

    public void setForIdDemandeRente(String idDemandeRente) {
        forIdDemandeRente = idDemandeRente;
    }
}
