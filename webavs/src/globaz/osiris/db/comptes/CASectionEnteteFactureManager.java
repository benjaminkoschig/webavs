package globaz.osiris.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAEnteteFacture;

/**
 * Basé sur la table de Section avec une jointure sur le CompteAnnexe et sur l'EnteteFacture (MUSCA)
 * 
 * @author MMU
 */
public class CASectionEnteteFactureManager extends CASectionManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ALIAS_ENTETE = "EN_TETE";
    public static final String ALIAS_MAX_ID_ENTETE_FACTURE = "ide";
    public static final String ALIAS_TABLE_COMPTE_ANNEXE = CACompteAnnexe.TABLE_CACPTAP;

    private String forIdPassage = "";
    private String fromSolde = "";
    private boolean sectionOuverte = false;
    private String untilSolde = "";

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder fields = new StringBuilder();

        fields.append(getSectionTableField()).append(", ");
        fields.append(FAEnteteFacture.TABLE_FAENTFP).append(".").append(FAEnteteFacture.FIELD_TOTALFACTURE)
                .append(", ");
        fields.append(FAEnteteFacture.TABLE_FAENTFP).append(".").append(FAEnteteFacture.FIELD_IDENTETEFACTURE);

        return fields.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder from = new StringBuilder();

        from.append(super._getFrom(statement));

        from.append(" INNER JOIN ");
        from.append(_getCollection()).append(CACompteAnnexe.TABLE_CACPTAP)
                .append(CASectionEnteteFactureManager.ALIAS_TABLE_COMPTE_ANNEXE);
        from.append(" ON ");
        from.append(CASectionEnteteFactureManager.ALIAS_TABLE_COMPTE_ANNEXE).append(".")
                .append(CACompteAnnexe.FIELD_IDCOMPTEANNEXE);
        from.append("=");
        from.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDCOMPTEANNEXE);

        from.append(" INNER JOIN ");

        from.append("(");
        from.append("SELECT ");
        from.append("MAX(").append(FAEnteteFacture.FIELD_IDENTETEFACTURE).append(") ")
                .append(CASectionEnteteFactureManager.ALIAS_MAX_ID_ENTETE_FACTURE).append(", ");
        from.append(FAEnteteFacture.FIELD_IDEXTERNEROLE).append(", ");
        from.append(FAEnteteFacture.FIELD_IDROLE);
        from.append(" FROM ");
        from.append(_getCollection()).append(FAEnteteFacture.TABLE_FAENTFP);
        from.append(" WHERE ");
        from.append(FAEnteteFacture.FIELD_IDPASSAGE).append("=").append(getForIdPassage());
        from.append(" GROUP BY ");
        from.append(FAEnteteFacture.FIELD_IDEXTERNEROLE).append(", ");
        from.append(FAEnteteFacture.FIELD_IDROLE);
        from.append(") AS ").append(CASectionEnteteFactureManager.ALIAS_ENTETE);

        from.append(" ON (");

        from.append(CASectionEnteteFactureManager.ALIAS_TABLE_COMPTE_ANNEXE).append(".")
                .append(CACompteAnnexe.FIELD_ASURVEILLER);
        from.append("=");
        from.append(CASectionEnteteFactureManager.ALIAS_ENTETE).append(".").append(FAEnteteFacture.FIELD_IDEXTERNEROLE);

        from.append(" AND ");

        from.append(CASectionEnteteFactureManager.ALIAS_TABLE_COMPTE_ANNEXE).append(".")
                .append(CACompteAnnexe.FIELD_IDROLE);
        from.append("=");
        from.append(CASectionEnteteFactureManager.ALIAS_ENTETE).append(".").append(FAEnteteFacture.FIELD_IDROLE);

        from.append(")");

        from.append(" INNER JOIN ");
        from.append(_getCollection()).append(FAEnteteFacture.TABLE_FAENTFP).append(" ")
                .append(FAEnteteFacture.TABLE_FAENTFP);
        from.append(" ON (");
        from.append(CASectionEnteteFactureManager.ALIAS_ENTETE).append(".")
                .append(CASectionEnteteFactureManager.ALIAS_MAX_ID_ENTETE_FACTURE);
        from.append("=");
        from.append(FAEnteteFacture.TABLE_FAENTFP).append(".").append(FAEnteteFacture.FIELD_IDENTETEFACTURE);
        from.append(")");

        return from.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        StringBuilder orderBy = new StringBuilder();
        orderBy.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDEXTERNE);
        return orderBy.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String superWhere = super._getWhere(statement);
        if (superWhere != null) {
            sql.append(superWhere);
        }

        if (!JadeStringUtil.isBlank(getFromSolde())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_SOLDE);
            sql.append(">");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getFromSolde()));
        }

        if (!JadeStringUtil.isBlank(getUntilSolde())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_SOLDE);
            sql.append("<");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getUntilSolde()));
        }

        if (isSectionOuverte()) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_SOLDE);
            sql.append("<>");
            sql.append("0");
        }

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CASectionEnteteFacture();
    }

    public String getForIdPassage() {
        return forIdPassage;
    }

    public String getFromSolde() {
        return fromSolde;
    }

    private String getSectionTableField() {
        StringBuilder sql = new StringBuilder();

        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDSECTIONPRINC).append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_NOPOURSUITE).append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_DATESUSPENDU).append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDLASTETAPECTX).append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDJOURNAL).append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_CATEGORIESECTION)
                .append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDMOTCONSUS).append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_DATEFINPERIODE).append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_DATEDEBUTPERIODE)
                .append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_CONTENTIEUXESTSUS)
                .append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_FRAIS).append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_TAXES).append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_INTERETS).append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_PMTCMP).append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_BASE).append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_SOLDE).append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_DATEECHEANCE).append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_DATESECTION).append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDEXTERNE).append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDSEQCON).append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDPOSJOU).append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDREMARQUE).append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDTYPESECTION).append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDCOMPTEANNEXE).append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDPLANRECOUVREMENT)
                .append(",");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDSECTION);

        return sql.toString();
    }

    public String getUntilSolde() {
        return untilSolde;
    }

    public boolean isSectionOuverte() {
        return sectionOuverte;
    }

    public void setForIdPassage(String string) {
        forIdPassage = string;
    }

    public void setFromSolde(String string) {
        fromSolde = string;
    }

    public void setSectionOuverte(boolean b) {
        sectionOuverte = b;
    }

    public void setUntilSolde(String string) {
        untilSolde = string;
    }
}
