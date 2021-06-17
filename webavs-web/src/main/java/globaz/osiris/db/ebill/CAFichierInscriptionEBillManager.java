package globaz.osiris.db.ebill;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;


/**
 * Manager des fichiers d'inscriptions EBill.
 */
public class CAFichierInscriptionEBillManager extends BManager {

    private static final String FICHIER = "fichier.";
    private static final String INSCRIPTION = "inscription.";
    private String forIdFichier;
    private String forDateLecture;
    private String forStatutFichier;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAFichierInscriptionEBill();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAFichierInscriptionEBill.TABLE_FICHIER_INSCRIPTION_EBILL + " fichier" +
                " LEFT JOIN " + _getCollection() + CAInscriptionEBill.TABLE_INSCRIPTION_EBILL + " inscription" +
                " ON " + FICHIER + CAFichierInscriptionEBill.FIELD_ID_FICHIER +
                " = " + INSCRIPTION +CAInscriptionEBill.FIELD_ID_FICHIER;
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
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(FICHIER).append(CAFichierInscriptionEBill.FIELD_ID_FICHIER).append("=").append(this._dbWriteNumeric(statement.getTransaction(), getForIdFichier()));
        }
        // Date de lecture
        if (!JadeStringUtil.isIntegerEmpty(getForDateLecture())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(CAFichierInscriptionEBill.FIELD_DATE_LECTURE).append("=").append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateLecture()));
        }
        // Statut fichier
        if (!JadeStringUtil.isIntegerEmpty(getForStatutFichier())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(CAFichierInscriptionEBill.FIELD_STATUT_FICHIER).append("=").append(this._dbWriteString(statement.getTransaction(), getForStatutFichier()));
        }

        // Retour
        return sqlWhere.toString();
    }

    @Override
    protected String _getSql(BStatement statement) {
        return super._getSql(statement) + _getGroupBy(statement);
    }

    @Override
    protected String _getGroupBy(BStatement statement) {
        StringBuilder groupBy = new StringBuilder(" group by ");
        groupBy.append(FICHIER).append(CAFichierInscriptionEBill.FIELD_ID_FICHIER).append(", ");
        groupBy.append(FICHIER).append(CAFichierInscriptionEBill.FIELD_NOM_FICHIER).append(", ");
        groupBy.append(FICHIER).append(CAFichierInscriptionEBill.FIELD_STATUT_FICHIER).append(", ");
        groupBy.append(FICHIER).append(CAFichierInscriptionEBill.FIELD_DATE_LECTURE);
        return groupBy.toString();
    }

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder fields = new StringBuilder();
        fields.append(FICHIER).append(CAFichierInscriptionEBill.FIELD_ID_FICHIER).append(", ");
        fields.append(FICHIER).append(CAFichierInscriptionEBill.FIELD_NOM_FICHIER).append(", ");
        fields.append(FICHIER).append(CAFichierInscriptionEBill.FIELD_STATUT_FICHIER).append(", ");
        fields.append(FICHIER).append(CAFichierInscriptionEBill.FIELD_DATE_LECTURE).append(", ");
        fields.append("COUNT(DISTINCT ").append(INSCRIPTION).append(CAInscriptionEBill.FIELD_ID_INSCRIPTION).append(") as ");
        fields.append(CAFichierInscriptionEBill.FIELD_NB_ELEMENTS);
        return fields.toString();
    }

    public String getForIdFichier() {
        return forIdFichier;
    }

    public void setForIdFichier(String forIdFichier) {
        this.forIdFichier = forIdFichier;
    }

    public String getForDateLecture() {
        return forDateLecture;
    }

    public void setForDateLecture(String forDateLecture) {
        this.forDateLecture = forDateLecture;
    }

    public String getForStatutFichier() {
        return forStatutFichier;
    }

    public void setForStatutFichier(String forStatutFichier) {
        this.forStatutFichier = forStatutFichier;
    }
}
