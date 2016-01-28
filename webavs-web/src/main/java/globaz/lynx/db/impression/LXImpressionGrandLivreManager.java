package globaz.lynx.db.impression;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.fournisseur.LXFournisseur;
import globaz.lynx.db.journal.LXJournal;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.section.LXSection;

public class LXImpressionGrandLivreManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String SUBTABLE_FOURNISSEUR = "FOURNISSEUR";
    public static final String SUBTABLE_JOURNAL = "JOURNAL";
    public static final String SUBTABLE_OPERATION = "OPERATION";
    public static final String SUBTABLE_SECTION = "SECTION";
    public static final String SUBTABLE_TIERS = "TIERS";

    private String forCsCategorie;
    private String forDateDebut;
    private String forDateFin;
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

        sqlFields.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_MONTANT).append(", ");
        sqlFields.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_LIBELLE).append(", ");
        sqlFields.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_ESTBLOQUE).append(", ");
        sqlFields.append(LXImpressionGrandLivreManager.SUBTABLE_FOURNISSEUR).append(".")
                .append(LXFournisseur.FIELD_ESTBLOQUE).append(" AS ")
                .append(LXImpressionGrandLivre.FIELD_FOURNISSEURESTBLOQUE).append(", ");
        sqlFields.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_CSMOTIFBLOCAGE).append(", ");
        sqlFields.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_TAUXESCOMPTE).append(", ");
        sqlFields.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_CSCODETVA).append(", ");
        sqlFields.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_REFERENCEEXTERNE).append(", ");
        sqlFields.append(LXImpressionGrandLivreManager.SUBTABLE_JOURNAL).append(".")
                .append(LXJournal.FIELD_DATEVALEURCG).append(", ");
        sqlFields.append(LXImpressionGrandLivreManager.SUBTABLE_SECTION).append(".").append(LXSection.FIELD_IDEXTERNE)
                .append(" AS ").append(LXImpressionGrandLivre.FIELD_IDEXTERNESECTION).append(", ");
        sqlFields.append(LXImpressionGrandLivreManager.SUBTABLE_FOURNISSEUR).append(".")
                .append(LXFournisseur.FIELD_IDEXTERNE).append(" AS ")
                .append(LXImpressionGrandLivre.FIELD_IDEXTERNEFOUR).append(", ");
        sqlFields.append(LXImpressionGrandLivreManager.SUBTABLE_SECTION).append(".").append(LXSection.FIELD_IDSOCIETE)
                .append(", ");
        sqlFields.append(LXImpressionGrandLivreManager.SUBTABLE_SECTION).append(".")
                .append(LXSection.FIELD_IDFOURNISSEUR).append(", ");
        sqlFields.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_DATEOPERATION).append(", ");
        sqlFields.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_DATEECHEANCE).append(", ");
        sqlFields.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_CSTYPEOPERATION).append(", ");
        sqlFields.append(LXImpressionGrandLivreManager.SUBTABLE_TIERS).append(".HTLDE1, ");
        sqlFields.append(LXImpressionGrandLivreManager.SUBTABLE_TIERS).append(".HTLDE2 ");

        return sqlFields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection()).append(LXSection.TABLE_LXSECTP).append(" ")
                .append(LXImpressionGrandLivreManager.SUBTABLE_SECTION).append(", ");
        sqlFrom.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(" ")
                .append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(", ");
        sqlFrom.append(_getCollection()).append(LXFournisseur.TABLE_LXFOURP).append(" ")
                .append(LXImpressionGrandLivreManager.SUBTABLE_FOURNISSEUR).append(", ");
        sqlFrom.append(_getCollection()).append(LXJournal.TABLE_LXJOURP).append(" ")
                .append(LXImpressionGrandLivreManager.SUBTABLE_JOURNAL).append(", ");
        sqlFrom.append(_getCollection()).append("TITIERP ").append(LXImpressionGrandLivreManager.SUBTABLE_TIERS);

        return sqlFrom.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getGroupBy(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getGroupBy(BStatement statement) {
        StringBuffer sqlGroupBy = new StringBuffer();

        sqlGroupBy.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_MONTANT).append(", ");
        sqlGroupBy.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_LIBELLE).append(", ");
        sqlGroupBy.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_ESTBLOQUE).append(", ");
        sqlGroupBy.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_CSMOTIFBLOCAGE).append(", ");
        sqlGroupBy.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_TAUXESCOMPTE).append(", ");
        sqlGroupBy.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_CSCODETVA).append(", ");
        sqlGroupBy.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_REFERENCEEXTERNE).append(", ");
        sqlGroupBy.append(LXImpressionGrandLivreManager.SUBTABLE_JOURNAL).append(".")
                .append(LXJournal.FIELD_DATEVALEURCG).append(", ");
        sqlGroupBy.append(LXImpressionGrandLivreManager.SUBTABLE_SECTION).append(".")
                .append(LXSection.FIELD_IDFOURNISSEUR).append(", ");
        sqlGroupBy.append(LXImpressionGrandLivreManager.SUBTABLE_SECTION).append(".").append(LXSection.FIELD_IDSOCIETE)
                .append(", ");
        sqlGroupBy.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_DATEOPERATION).append(", ");
        sqlGroupBy.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_DATEECHEANCE).append(", ");
        sqlGroupBy.append(LXImpressionGrandLivreManager.SUBTABLE_SECTION).append(".").append(LXSection.FIELD_IDEXTERNE)
                .append(", ");
        sqlGroupBy.append(LXImpressionGrandLivreManager.SUBTABLE_FOURNISSEUR).append(".")
                .append(LXFournisseur.FIELD_IDEXTERNE).append(", ");
        sqlGroupBy.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_CSTYPEOPERATION).append(", ");
        sqlGroupBy.append(LXImpressionGrandLivreManager.SUBTABLE_TIERS).append(".HTLDE1, ");
        sqlGroupBy.append(LXImpressionGrandLivreManager.SUBTABLE_TIERS).append(".HTLDE2 ");

        return sqlGroupBy.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        StringBuffer sqlOrder = new StringBuffer();

        sqlOrder.append(LXImpressionGrandLivreManager.SUBTABLE_SECTION).append(".")
                .append(LXSection.FIELD_IDFOURNISSEUR).append(" ASC,");
        sqlOrder.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_DATEOPERATION).append(" ASC,");
        sqlOrder.append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_DATEECHEANCE).append(" ASC");

        return sqlOrder.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        sqlWhere.append(LXImpressionGrandLivreManager.SUBTABLE_SECTION).append(".").append(LXSection.FIELD_IDSECTION)
                .append(" = ").append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_IDSECTION);
        sqlWhere.append(" AND ").append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_CSETATOPERATION).append(" IN (").append(LXOperation.CS_ETAT_COMPTABILISE)
                .append(", ").append(LXOperation.CS_ETAT_PREPARE).append(", ").append(LXOperation.CS_ETAT_SOLDE)
                .append(") ");
        sqlWhere.append(" AND ").append(LXImpressionGrandLivreManager.SUBTABLE_OPERATION).append(".")
                .append(LXOperation.FIELD_IDJOURNAL).append(" = ")
                .append(LXImpressionGrandLivreManager.SUBTABLE_JOURNAL).append(".").append(LXJournal.FIELD_IDJOURNAL);
        sqlWhere.append(" AND ").append(LXImpressionGrandLivreManager.SUBTABLE_SECTION).append(".")
                .append(LXSection.FIELD_IDFOURNISSEUR).append(" = ")
                .append(LXImpressionGrandLivreManager.SUBTABLE_FOURNISSEUR).append(".")
                .append(LXFournisseur.FIELD_IDFOURNISSEUR);
        sqlWhere.append(" AND ").append(LXImpressionGrandLivreManager.SUBTABLE_FOURNISSEUR).append(".")
                .append(LXFournisseur.FIELD_IDTIERS).append(" = ").append(LXImpressionGrandLivreManager.SUBTABLE_TIERS)
                .append(".HTITIE");

        if (!JadeStringUtil.isEmpty(getForIdSociete())) {
            sqlWhere.append(" AND ").append(LXImpressionGrandLivreManager.SUBTABLE_SECTION).append(".")
                    .append(LXSection.FIELD_IDSOCIETE).append(" = ").append(getForIdSociete());
        }
        if (!JadeStringUtil.isEmpty(getForFournisseurIdBorneInf())) {
            sqlWhere.append(" AND ").append(LXImpressionGrandLivreManager.SUBTABLE_FOURNISSEUR).append(".")
                    .append(LXSection.FIELD_IDEXTERNE).append(" >= ")
                    .append(this._dbWriteString(statement.getTransaction(), getForFournisseurIdBorneInf()));
        }
        if (!JadeStringUtil.isEmpty(getForFournisseurIdBorneSup())) {
            sqlWhere.append(" AND ").append(LXImpressionGrandLivreManager.SUBTABLE_FOURNISSEUR).append(".")
                    .append(LXSection.FIELD_IDEXTERNE).append(" <= ")
                    .append(this._dbWriteString(statement.getTransaction(), getForFournisseurIdBorneSup()));
        }
        if (!JadeStringUtil.isEmpty(getForCsCategorie())) {
            sqlWhere.append(" AND ").append(LXImpressionGrandLivreManager.SUBTABLE_FOURNISSEUR).append(".")
                    .append(LXFournisseur.FIELD_CSCATEGORIE).append(" = ").append(getForCsCategorie());
        }
        if (!JadeStringUtil.isEmpty(getForFournisseurNameBorneInf())) {

            String nomFormatte = JadeStringUtil.convertSpecialChars(getForFournisseurNameBorneInf());
            nomFormatte = JadeStringUtil.toUpperCase(nomFormatte);

            sqlWhere.append(" AND ").append(LXImpressionGrandLivreManager.SUBTABLE_TIERS).append(".HTLDE1 >= ")
                    .append(this._dbWriteString(statement.getTransaction(), nomFormatte));
        }
        if (!JadeStringUtil.isEmpty(getForFournisseurNameBorneSup())) {

            String nomFormatte = JadeStringUtil.convertSpecialChars(getForFournisseurNameBorneSup());
            nomFormatte = JadeStringUtil.toUpperCase(nomFormatte);

            sqlWhere.append(" AND ").append(LXImpressionGrandLivreManager.SUBTABLE_TIERS).append(".HTLDE1 <= ")
                    .append(this._dbWriteString(statement.getTransaction(), nomFormatte));
        }
        if (!JadeStringUtil.isIntegerEmpty(getForDateDebut())) {
            sqlWhere.append(" AND ").append(LXImpressionGrandLivreManager.SUBTABLE_JOURNAL).append(".")
                    .append(LXJournal.FIELD_DATEVALEURCG).append(" >= ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebut()));
        }
        if (!JadeStringUtil.isIntegerEmpty(getForDateFin())) {
            sqlWhere.append(" AND ").append(LXImpressionGrandLivreManager.SUBTABLE_JOURNAL).append(".")
                    .append(LXJournal.FIELD_DATEVALEURCG).append(" <= ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateFin()));
        }
        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXImpressionGrandLivre();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForCsCategorie() {
        return forCsCategorie;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
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

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
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
