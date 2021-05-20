package globaz.osiris.db.ebill;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class CATraitementEBillManager extends BManager {

    private static final String AND = " AND ";
    private String forIdFichier;
    private String forTransactionID;
    private String forEBillAccountID;
    private String forType;
    private String forNumAffilie;
    private String forStatutInterne;
    private String forEtat;
    private String forCodeErreur;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CATraitementEBill();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CATraitementEBill.TABLE_TRAITEMENT_EBILL;
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
            sqlWhere.append(CATraitementEBill.FIELD_ID_FICHIER).append("=").append(this._dbWriteNumeric(statement.getTransaction(), getForIdFichier()));
        }
        // TransactionID
        if (!JadeStringUtil.isEmpty(getForTransactionID())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(AND);
            }
            sqlWhere.append(CATraitementEBill.FIELD_TRANSACTION_ID).append("=").append(this._dbWriteString(statement.getTransaction(), getForTransactionID()));
        }
        // eBillAccountID
        if (!JadeStringUtil.isIntegerEmpty(getForEBillAccountID())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(AND);
            }
            sqlWhere.append(CATraitementEBill.FIELD_EBILL_ACCOUNT_ID).append("=").append(this._dbWriteString(statement.getTransaction(), getForEBillAccountID()));
        }
        // Numéro affilié
        if (!JadeStringUtil.isEmpty(getForNumAffilie())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(AND);
            }
            sqlWhere.append(CATraitementEBill.FIELD_NUMERO_AFFILIE).append(" like ").append(this._dbWriteString(statement.getTransaction(), "%" + getForNumAffilie() + "%"));
        }
        // Statut interne
        if (!JadeStringUtil.isIntegerEmpty(getForStatutInterne())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(AND);
            }
            sqlWhere.append(CATraitementEBill.FIELD_STATUT).append("=").append(this._dbWriteNumeric(statement.getTransaction(), getForStatutInterne()));
        }
        // Etat eBill
        if (!JadeStringUtil.isIntegerEmpty(getForEtat())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(AND);
            }
            sqlWhere.append(CATraitementEBill.FIELD_ETAT).append("=").append(this._dbWriteNumeric(statement.getTransaction(), getForEtat()));
        }
        // Code Erreur
        if (!JadeStringUtil.isIntegerEmpty(getForCodeErreur())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(AND);
            }
            sqlWhere.append(CATraitementEBill.FIELD_CODE_ERREUR).append("=").append(this._dbWriteString(statement.getTransaction(), getForCodeErreur()));
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

    public String getForTransactionID() {
        return forTransactionID;
    }

    public void setForTransactionID(String forTransactionID) {
        this.forTransactionID = forTransactionID;
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

    public String getForEtat() {
        return forEtat;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public String getForCodeErreur() {
        return forCodeErreur;
    }

    public void setForCodeErreur(String forCodeErreur) {
        this.forCodeErreur = forCodeErreur;
    }
}
