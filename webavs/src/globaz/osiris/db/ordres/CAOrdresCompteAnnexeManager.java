package globaz.osiris.db.ordres;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationOrdreVersement;
import java.util.ArrayList;

public class CAOrdresCompteAnnexeManager extends CAOrdreNonVerseManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCompteAnnexe;
    private Boolean forOrdresBloquesOnly = new Boolean(false);

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        String order = _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_DATE + " DESC";

        String superOrder = super._getOrder(statement);
        if (!JadeStringUtil.isBlank(superOrder)) {
            order += ", " + superOrder;
        }

        return order;
    }

    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        String sqlWhere = super._getWhere(statement);

        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ";
        }

        sqlWhere += _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDCOMPTEANNEXE + "="
                + this._dbWriteNumeric(statement.getTransaction(), getForIdCompteAnnexe());

        if (getForOrdresBloquesOnly().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection()
                    + CAOrdreVersement.TABLE_CAOPOVP
                    + "."
                    + CAOrdreVersement.FIELD_ESTBLOQUE
                    + "="
                    + this._dbWriteBoolean(statement.getTransaction(), getForOrdresBloquesOnly(),
                            BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        return sqlWhere;
    }

    @Override
    protected void _init() {
        super._init();

        if (!isForOrdresNonVerse()) {
            ArrayList etatIn = new ArrayList();
            etatIn.add(APIOperation.ETAT_COMPTABILISE);
            etatIn.add(APIOperation.ETAT_PROVISOIRE);
            etatIn.add(APIOperation.ETAT_OUVERT);
            etatIn.add(APIOperation.ETAT_VERSE);
            setForEtatIn(etatIn);
        }
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAOperationOrdreVersement();
    }

    @Override
    public String getForIdCompteAnnexe() {
        return forIdCompteAnnexe;
    }

    public Boolean getForOrdresBloquesOnly() {
        return forOrdresBloquesOnly;
    }

    @Override
    public void setForIdCompteAnnexe(String forIdCompteAnnexe) {
        this.forIdCompteAnnexe = forIdCompteAnnexe;
    }

    public void setForOrdresBloquesOnly(Boolean forOrdresBloquesOnly) {
        this.forOrdresBloquesOnly = forOrdresBloquesOnly;
    }

}
