package globaz.lynx.db.impression;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.fournisseur.LXFournisseur;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.utils.LXUtils;
import java.util.ArrayList;

public class LXImpressionBalanceAgeeManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String SUBTABLE_FOURNISSEUR = "FOURNISSEUR";
    public static final String SUBTABLE_OPERATION = "OPERATION";
    public static final String SUBTABLE_SECTION = "SECTION";
    public static final String SUBTABLE_TIERS = "TIERS";

    private String forCsCategorie;
    private ArrayList<String> forCsEtatIn;
    private String forFournisseurIdBorneInf;
    private String forFournisseurIdBorneSup;
    private String forFournisseurNameBorneInf;
    private String forFournisseurNameBorneSup;

    private String forIdSociete;

    /**
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sqlFields = new StringBuffer();

        sqlFields.append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_IDOPERATION).append(", ");
        sqlFields.append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_IDSECTION).append(", ");
        sqlFields.append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_MONTANT).append(", ");
        sqlFields.append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_LIBELLE).append(", ");
        sqlFields.append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_ESTBLOQUE).append(", ");
        sqlFields.append(LXImpressionBalanceAgeeManager.SUBTABLE_SECTION).append(".").append(LXSection.FIELD_IDEXTERNE)
                .append(" AS ").append(LXImpressionBalanceAgee.FIELD_IDEXTERNESECTION).append(", ");
        sqlFields.append(LXImpressionBalanceAgeeManager.SUBTABLE_FOURNISSEUR).append(".")
                .append(LXFournisseur.FIELD_IDEXTERNE).append(" AS ")
                .append(LXImpressionBalanceAgee.FIELD_IDEXTERNEFOUR).append(", ");
        sqlFields.append(LXImpressionBalanceAgeeManager.SUBTABLE_SECTION).append(".").append(LXSection.FIELD_IDSOCIETE)
                .append(", ");
        sqlFields.append(LXImpressionBalanceAgeeManager.SUBTABLE_SECTION).append(".")
                .append(LXSection.FIELD_IDFOURNISSEUR).append(", ");
        sqlFields.append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_DATEOPERATION).append(", ");
        sqlFields.append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_DATEECHEANCE).append(", ");
        sqlFields.append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_CSETATOPERATION).append(", ");
        sqlFields.append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_REFERENCEEXTERNE).append(", ");
        sqlFields.append(LXImpressionBalanceAgeeManager.SUBTABLE_TIERS).append(".HTLDE1, ");
        sqlFields.append(LXImpressionBalanceAgeeManager.SUBTABLE_TIERS).append(".HTLDE2 ");

        return sqlFields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection()).append(LXSection.TABLE_LXSECTP).append(" ")
                .append(LXImpressionBalanceAgeeManager.SUBTABLE_SECTION).append(", ");
        sqlFrom.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(" ")
                .append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(", ");
        sqlFrom.append(_getCollection()).append(LXFournisseur.TABLE_LXFOURP).append(" ")
                .append(LXImpressionBalanceAgeeManager.SUBTABLE_FOURNISSEUR).append(", ");
        sqlFrom.append(_getCollection()).append("TITIERP ").append(LXImpressionBalanceAgeeManager.SUBTABLE_TIERS);

        return sqlFrom.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getGroupBy(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getGroupBy(BStatement statement) {
        StringBuffer sqlGroupBy = new StringBuffer();

        sqlGroupBy.append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_IDOPERATION).append(", ");
        sqlGroupBy.append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_IDSECTION).append(", ");
        sqlGroupBy.append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_MONTANT).append(", ");
        sqlGroupBy.append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_LIBELLE).append(", ");
        sqlGroupBy.append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_ESTBLOQUE).append(", ");
        sqlGroupBy.append(LXImpressionBalanceAgeeManager.SUBTABLE_SECTION).append(".")
                .append(LXSection.FIELD_IDFOURNISSEUR).append(", ");
        sqlGroupBy.append(LXImpressionBalanceAgeeManager.SUBTABLE_SECTION).append(".")
                .append(LXSection.FIELD_IDSOCIETE).append(", ");
        sqlGroupBy.append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_DATEOPERATION).append(", ");
        sqlGroupBy.append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_DATEECHEANCE).append(", ");
        sqlGroupBy.append(LXImpressionBalanceAgeeManager.SUBTABLE_SECTION).append(".")
                .append(LXSection.FIELD_IDEXTERNE).append(", ");
        sqlGroupBy.append(LXImpressionBalanceAgeeManager.SUBTABLE_FOURNISSEUR).append(".")
                .append(LXFournisseur.FIELD_IDEXTERNE).append(", ");
        sqlGroupBy.append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_CSETATOPERATION).append(", ");
        sqlGroupBy.append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_REFERENCEEXTERNE).append(", ");
        sqlGroupBy.append(LXImpressionBalanceAgeeManager.SUBTABLE_TIERS).append(".HTLDE1, ");
        sqlGroupBy.append(LXImpressionBalanceAgeeManager.SUBTABLE_TIERS).append(".HTLDE2 ");

        return sqlGroupBy.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        StringBuffer sqlOrder = new StringBuffer();

        sqlOrder.append(LXImpressionBalanceAgeeManager.SUBTABLE_SECTION).append(".")
                .append(LXSection.FIELD_IDFOURNISSEUR).append(" ASC,");
        sqlOrder.append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_DATEOPERATION).append(" ASC,");
        sqlOrder.append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_DATEECHEANCE).append(" ASC");

        return sqlOrder.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        sqlWhere.append(LXImpressionBalanceAgeeManager.SUBTABLE_SECTION).append(".").append(LXSection.FIELD_IDSECTION)
                .append(" = ").append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_IDSECTION);
        sqlWhere.append(" AND ").append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_CSTYPEOPERATION).append(" IN (")
                .append(LXOperation.CS_TYPE_FACTURE_BVR_ORANGE).append(", ")
                .append(LXOperation.CS_TYPE_FACTURE_BVR_ROUGE).append(", ").append(LXOperation.CS_TYPE_FACTURE_CAISSE)
                .append(", ").append(LXOperation.CS_TYPE_FACTURE_LSV).append(", ")
                .append(LXOperation.CS_TYPE_FACTURE_VIREMENT).append(") ");
        sqlWhere.append(" AND ").append(LXImpressionBalanceAgeeManager.SUBTABLE_OPERATION).append(".")
                .append(LXUtils.getWhereValueMultiple(LXOperation.FIELD_CSETATOPERATION, getForCsEtatIn()));
        sqlWhere.append(" AND ").append(LXImpressionBalanceAgeeManager.SUBTABLE_SECTION).append(".")
                .append(LXSection.FIELD_IDFOURNISSEUR).append(" = ")
                .append(LXImpressionBalanceAgeeManager.SUBTABLE_FOURNISSEUR).append(".")
                .append(LXFournisseur.FIELD_IDFOURNISSEUR);
        sqlWhere.append(" AND ").append(LXImpressionBalanceAgeeManager.SUBTABLE_FOURNISSEUR).append(".")
                .append(LXFournisseur.FIELD_IDTIERS).append(" = ")
                .append(LXImpressionBalanceAgeeManager.SUBTABLE_TIERS).append(".HTITIE");

        if (!JadeStringUtil.isEmpty(getForIdSociete())) {
            sqlWhere.append(" AND ").append(LXImpressionBalanceAgeeManager.SUBTABLE_SECTION).append(".")
                    .append(LXSection.FIELD_IDSOCIETE).append(" = ").append(getForIdSociete());
        }
        if (!JadeStringUtil.isEmpty(getForFournisseurIdBorneInf())) {
            sqlWhere.append(" AND ").append(LXImpressionBalanceAgeeManager.SUBTABLE_FOURNISSEUR).append(".")
                    .append(LXSection.FIELD_IDEXTERNE).append(" >= ").append(getForFournisseurIdBorneInf());
        }
        if (!JadeStringUtil.isEmpty(getForFournisseurIdBorneSup())) {
            sqlWhere.append(" AND ").append(LXImpressionBalanceAgeeManager.SUBTABLE_FOURNISSEUR).append(".")
                    .append(LXSection.FIELD_IDEXTERNE).append(" <= ").append(getForFournisseurIdBorneSup());
        }
        if (!JadeStringUtil.isEmpty(getForCsCategorie())) {
            sqlWhere.append(" AND ").append(LXImpressionBalanceAgeeManager.SUBTABLE_FOURNISSEUR).append(".")
                    .append(LXFournisseur.FIELD_CSCATEGORIE).append(" = ").append(getForCsCategorie());
        }
        if (!JadeStringUtil.isEmpty(getForFournisseurNameBorneInf())) {

            String nomFormatte = JadeStringUtil.convertSpecialChars(getForFournisseurNameBorneInf());
            nomFormatte = JadeStringUtil.toUpperCase(nomFormatte);

            sqlWhere.append(" AND ").append(LXImpressionBalanceAgeeManager.SUBTABLE_TIERS).append(".HTLDE1 >= ")
                    .append(this._dbWriteString(statement.getTransaction(), nomFormatte));
        }
        if (!JadeStringUtil.isEmpty(getForFournisseurNameBorneSup())) {

            String nomFormatte = JadeStringUtil.convertSpecialChars(getForFournisseurNameBorneSup());
            nomFormatte = JadeStringUtil.toUpperCase(nomFormatte);

            sqlWhere.append(" AND ").append(LXImpressionBalanceAgeeManager.SUBTABLE_TIERS).append(".HTLDE1 <= ")
                    .append(this._dbWriteString(statement.getTransaction(), nomFormatte));
        }

        // Bug 5319 Tenir compte des factures extrounées
        // SECTION.idsection not in (select idsection from ccvdqua.lxoperp where CSTYPEOPERATION = 7700015)
        sqlWhere.append(" AND ").append(LXImpressionBalanceAgeeManager.SUBTABLE_SECTION).append(".")
                .append(LXSection.FIELD_IDSECTION).append(" NOT IN (").append("SELECT ")
                .append(LXOperation.FIELD_IDSECTION).append(" FROM ").append(_getCollection())
                .append(LXOperation.TABLE_LXOPERP).append(" WHERE ").append(LXOperation.FIELD_CSTYPEOPERATION)
                .append("=").append(LXOperation.CS_TYPE_EXTOURNE).append(" AND ")
                .append(LXUtils.getWhereValueMultiple(LXOperation.FIELD_CSETATOPERATION, getForCsEtatIn())).append(")");

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXImpressionBalanceAgee();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForCsCategorie() {
        return forCsCategorie;
    }

    public ArrayList<String> getForCsEtatIn() {
        return forCsEtatIn;
    }

    public String getForFournisseurIdBorneInf() {
        return forFournisseurIdBorneInf;
    }

    public String getForFournisseurIdBorneSup() {
        return forFournisseurIdBorneSup;
    }

    public String getForFournisseurNameBorneInf() {
        return forFournisseurNameBorneInf;
    }

    public String getForFournisseurNameBorneSup() {
        return forFournisseurNameBorneSup;
    }

    public String getForIdSociete() {
        return forIdSociete;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForCsCategorie(String forCsCategorie) {
        this.forCsCategorie = forCsCategorie;
    }

    public void setForCsEtatIn(ArrayList<String> forCsEtatIn) {
        this.forCsEtatIn = forCsEtatIn;
    }

    public void setForFournisseurIdBorneInf(String forFournisseurIdBorneInf) {
        this.forFournisseurIdBorneInf = forFournisseurIdBorneInf;
    }

    public void setForFournisseurIdBorneSup(String forFournisseurIdBorneSup) {
        this.forFournisseurIdBorneSup = forFournisseurIdBorneSup;
    }

    public void setForFournisseurNameBorneInf(String forFournisseurNameBorneInf) {
        this.forFournisseurNameBorneInf = forFournisseurNameBorneInf;
    }

    public void setForFournisseurNameBorneSup(String forFournisseurNameBorneSup) {
        this.forFournisseurNameBorneSup = forFournisseurNameBorneSup;
    }

    public void setForIdSociete(String forIdSociete) {
        this.forIdSociete = forIdSociete;
    }
}
