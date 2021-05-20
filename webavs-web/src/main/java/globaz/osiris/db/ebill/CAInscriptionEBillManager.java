package globaz.osiris.db.ebill;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class CAInscriptionEBillManager extends BManager {

    private static final String AND = " AND ";
    private String forIdFichier;
    private String forEBillAccountID;
    private String forType;
    private String forNumAffilie;
    private String forStatutInterne;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAInscriptionEBill();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAInscriptionEBill.TABLE_INSCRIPTION_EBILL;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sqlWhere = new StringBuilder();
        // Id Fichier
        if (!JadeStringUtil.isEmpty(getForIdFichier())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(AND);
            }
            sqlWhere.append(CAInscriptionEBill.FIELD_ID_FICHIER).append("=").append(this._dbWriteNumeric(statement.getTransaction(), getForIdFichier()));
        }
        // eBillAccountID
        if (!JadeStringUtil.isIntegerEmpty(getForEBillAccountID())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(AND);
            }
            sqlWhere.append(CAInscriptionEBill.FIELD_EBILL_ACCOUNT_ID).append("=").append(this._dbWriteString(statement.getTransaction(), getForEBillAccountID()));
        }
        // Type
        if (!JadeStringUtil.isIntegerEmpty(getForType())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(AND);
            }
            sqlWhere.append(CAInscriptionEBill.FIELD_TYPE).append("=").append(this._dbWriteNumeric(statement.getTransaction(), getForType()));
        }
        // Numéro affilié
        if (!JadeStringUtil.isEmpty(getForNumAffilie())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(AND);
            }
            sqlWhere.append(CAInscriptionEBill.FIELD_NUMERO_AFFILIE).append(" like ").append(this._dbWriteString(statement.getTransaction(), "%" + getForNumAffilie() + "%"));
        }
        // Statut interne
        if (!JadeStringUtil.isIntegerEmpty(getForStatutInterne())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(AND);
            }
            sqlWhere.append(CAInscriptionEBill.FIELD_STATUT).append("=").append(this._dbWriteNumeric(statement.getTransaction(), getForStatutInterne()));
        }

        // Retour
        return sqlWhere.toString();
    }

    public String getForIdFichier() {
        return forIdFichier;
    }

    public void setForIdFichier(String forIdFichier) {
        this.forIdFichier = forIdFichier;
    }

    public String getForEBillAccountID() {
        return forEBillAccountID;
    }

    public void setForEBillAccountID(String forEBillAccountID) {
        this.forEBillAccountID = forEBillAccountID;
    }

    public String getForType() {
        return forType;
    }

    public void setForType(String forType) {
        this.forType = forType;
    }

    public String getForNumAffilie() {
        return forNumAffilie;
    }

    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

    public String getForStatutInterne() {
        return forStatutInterne;
    }

    public void setForStatutInterne(String forStatutInterne) {
        this.forStatutInterne = forStatutInterne;
    }
}
