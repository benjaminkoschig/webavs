package globaz.osiris.db.ebill;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;


/**
 * Manager des fichiers d'traitements EBill.
 */
public class CAFichierTraitementEBillManager extends BManager {

    private static final String ALIAS_FICHIER = "fichier.";
    private static final String ALIAS_TRAITEMENT = "traitement.";
    private String forIdFichier;
    private String forDateTraitement;
    private String forStatutFichier;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAFichierTraitementEBill();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAFichierTraitementEBill.TABLE_FICHIER_TRAITEMENT_EBILL + " fichier" +
                " LEFT JOIN " + _getCollection() + CATraitementEBill.TABLE_TRAITEMENT_EBILL + " traitement" +
                " ON " + ALIAS_FICHIER + CAFichierTraitementEBill.FIELD_ID_FICHIER +
                " = " + ALIAS_TRAITEMENT +CATraitementEBill.FIELD_ID_FICHIER;
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
            sqlWhere.append(ALIAS_FICHIER).append(CAFichierTraitementEBill.FIELD_ID_FICHIER).append("=").append(this._dbWriteNumeric(statement.getTransaction(), getForIdFichier()));
        }
        // Date de lecture
        if (!JadeStringUtil.isIntegerEmpty(getForDateTraitement())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(CAFichierTraitementEBill.FIELD_DATE_LECTURE).append("=").append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateTraitement()));
        }
        // Statut fichier
        if (!JadeStringUtil.isIntegerEmpty(getForStatutFichier())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(CAFichierTraitementEBill.FIELD_STATUT_FICIHER).append("=").append(this._dbWriteString(statement.getTransaction(), getForStatutFichier()));
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
        groupBy.append(ALIAS_FICHIER).append(CAFichierTraitementEBill.FIELD_ID_FICHIER).append(", ");
        groupBy.append(ALIAS_FICHIER).append(CAFichierTraitementEBill.FIELD_NOM_FICHIER).append(", ");
        groupBy.append(ALIAS_FICHIER).append(CAFichierTraitementEBill.FIELD_STATUT_FICIHER).append(", ");
        groupBy.append(ALIAS_FICHIER).append(CAFichierTraitementEBill.FIELD_DATE_LECTURE).append(", ");
        groupBy.append(ALIAS_FICHIER).append(CAFichierTraitementEBill.FIELD_NB_ELEMENTS).append(", ");
        groupBy.append(ALIAS_FICHIER).append(CAFichierTraitementEBill.FIELD_NB_ELEMENTS_TRAITES).append(", ");
        groupBy.append(ALIAS_FICHIER).append(CAFichierTraitementEBill.FIELD_NB_ELEMENTS_EN_ERREURS).append(", ");
        groupBy.append(ALIAS_FICHIER).append(CAFichierTraitementEBill.FIELD_NB_ELEMENTS_REJETES);
        return groupBy.toString();
    }

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder fields = new StringBuilder();
        fields.append(ALIAS_FICHIER).append(CAFichierTraitementEBill.FIELD_ID_FICHIER).append(", ");
        fields.append(ALIAS_FICHIER).append(CAFichierTraitementEBill.FIELD_NOM_FICHIER).append(", ");
        fields.append(ALIAS_FICHIER).append(CAFichierTraitementEBill.FIELD_STATUT_FICIHER).append(", ");
        fields.append(ALIAS_FICHIER).append(CAFichierTraitementEBill.FIELD_DATE_LECTURE).append(", ");
        fields.append(ALIAS_FICHIER).append(CAFichierTraitementEBill.FIELD_NB_ELEMENTS).append(", ");
        fields.append(ALIAS_FICHIER).append(CAFichierTraitementEBill.FIELD_NB_ELEMENTS_TRAITES).append(", ");
        fields.append(ALIAS_FICHIER).append(CAFichierTraitementEBill.FIELD_NB_ELEMENTS_EN_ERREURS).append(", ");
        fields.append(ALIAS_FICHIER).append(CAFichierTraitementEBill.FIELD_NB_ELEMENTS_REJETES);
        return fields.toString();
    }

    public String getForIdFichier() {
        return forIdFichier;
    }

    public void setForIdFichier(String forIdFichier) {
        this.forIdFichier = forIdFichier;
    }

    public String getForDateTraitement() {
        return forDateTraitement;
    }

    public void setForDateTraitement(String forDateTraitement) {
        this.forDateTraitement = forDateTraitement;
    }

    public String getForStatutFichier() {
        return forStatutFichier;
    }

    public void setForStatutFichier(String forStatutFichier) {
        this.forStatutFichier = forStatutFichier;
    }
}
