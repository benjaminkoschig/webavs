/*
 * Créé le 27 août 2010
 */
package globaz.cygnus.db.qds;

import globaz.cygnus.api.qds.IRFQd;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.sql.PRWhereStringBuffer;

/**
 * @author jje
 */
public class RFPeriodeValiditeQdPrincipaleManager extends BManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // private transient String fromClause = null;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String aliasFieldName = "idFamMax";
    private String aliasName = "a";
    private boolean forDerniereVersion = false;
    private boolean forIdFamilleMax = false;
    private String forIdFamilleModif = "";
    private String forIdPeriodeValidite = "";
    private String forIdQd = "";
    private String forTypeModif = "";

    private String idPeriodeValToIgnore = "";
    private boolean orderByDateDebutAsc = false;

    private boolean orderByDateDebutDesc = false;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFPeriodeValiditeQdPrincipaleManager.
     */
    public RFPeriodeValiditeQdPrincipaleManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFields(BStatement statement) {
        if (forIdFamilleMax) {
            return " max(" + RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_FAMILLE_MODIFICATION + ") as " + aliasFieldName;
        } else {
            return super._getFields(statement);
        }
    }

    /**
     * Retourne la clause from avec ou sans alias, selon le type de requête Retourne null si une exception est levée
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (forDerniereVersion) {
            try {
                return ((RFPeriodeValiditeQdPrincipale) _createNewEntity())._getFromAlias(aliasName);
            } catch (Exception e) {
                return null;
            }
        } else {
            return super._getFrom(statement);
        }
    }

    @Override
    protected String _getOrder(BStatement statement) {

        StringBuffer order = new StringBuffer(aliasName + "." + RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_DEBUT);

        if (orderByDateDebutAsc) {
            order.append(" ASC");
            return order.toString();
        } else if (orderByDateDebutDesc) {
            order.append(" DESC");
            return order.toString();
        } else {
            return super._getOrder(statement);
        }
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        String schema = _getCollection();
        PRWhereStringBuffer sqlWhere = new PRWhereStringBuffer(schema);

        String tblName;
        if (forDerniereVersion) {
            tblName = aliasName + ".";
        } else {
            tblName = schema + RFPeriodeValiditeQdPrincipale.TABLE_NAME + ".";
        }

        if (!JadeStringUtil.isBlankOrZero(getForIdQd())) {
            sqlWhere.newClause().append(tblName + RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE)
                    .appendEgal().append(getForIdQd());
        }

        if (!JadeStringUtil.isBlankOrZero(getForIdFamilleModif())) {
            sqlWhere.newClause().append(tblName + RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_FAMILLE_MODIFICATION)
                    .appendEgal().append(getForIdFamilleModif());
        }

        if (!JadeStringUtil.isBlankOrZero(getForIdPeriodeValidite())) {
            sqlWhere.newClause().append(tblName + RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE)
                    .appendEgal().append(getForIdPeriodeValidite());
        }

        if (!JadeStringUtil.isBlank(getForTypeModif())) {
            sqlWhere.newClause().append(tblName + RFPeriodeValiditeQdPrincipale.FIELDNAME_TYPE_MODIFICATION)
                    .appendEgal().append(getForTypeModif());
        }

        if (forDerniereVersion) {
            sqlWhere.newClause().append(tblName + RFPeriodeValiditeQdPrincipale.FIELDNAME_TYPE_MODIFICATION)
                    .append(" <> ").append(IRFQd.CS_SUPPRESSION);
            sqlWhere.newClause().append("not exists (select * from ")
                    .append(schema + RFPeriodeValiditeQdPrincipale.TABLE_NAME).append(" where ")
                    .append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_FAMILLE_MODIFICATION).appendEgal()
                    .append(aliasName + ".").append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_FAMILLE_MODIFICATION)
                    .append(" and ").append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE)
                    .append(" > " + aliasName + ".")
                    .append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE).append(" and ")
                    .append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE).appendEgal().append(getForIdQd())
                    .append(")");
        }

        if (forIdFamilleMax) {
            sqlWhere = new PRWhereStringBuffer(schema);
            /*
             * sqlWhere.newClause().append("not exists (select * from ") .append(schema +
             * RFPeriodeValiditeQdPrincipale.TABLE_NAME).append(" where ")
             * .append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_FAMILLE_MODIFICATION) .append(" > " + this.aliasName +
             * ".") .append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_FAMILLE_MODIFICATION).append(")");
             */
        }

        if (!JadeStringUtil.isEmpty(idPeriodeValToIgnore)) {
            sqlWhere.newClause().append(aliasName + ".")
                    .append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE).append(" <> ")
                    .append(idPeriodeValToIgnore);

        }

        return sqlWhere.toString();

    }

    /**
     * Définition de l'entité (RFPeriodeValiditeQdPrincipale)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        if (forIdFamilleMax) {
            return new RFPeriodeValiditeQdPrincipale() {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                protected void _readProperties(BStatement statement) throws Exception {
                    idFamilleModification = statement.dbReadNumeric(aliasFieldName);
                }
            };
        } else {
            return new RFPeriodeValiditeQdPrincipale();
        }
    }

    public String getAliasName() {
        return aliasName;
    }

    public String getForIdFamilleModif() {
        return forIdFamilleModif;
    }

    public String getForIdPeriodeValidite() {
        return forIdPeriodeValidite;
    }

    public String getForIdQd() {
        return forIdQd;
    }

    public String getForTypeModif() {
        return forTypeModif;
    }

    public String getIdPeriodeValToIgnore() {
        return idPeriodeValToIgnore;
    }

    public boolean isForDerniereVersion() {
        return forDerniereVersion;
    }

    public boolean isForIdFamilleMax() {
        return forIdFamilleMax;
    }

    public boolean isOrderByDateDebutAsc() {
        return orderByDateDebutAsc;
    }

    public boolean isOrderByDateDebutDesc() {
        return orderByDateDebutDesc;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public void setForDerniereVersion(boolean forDerniereVersion) {
        this.forDerniereVersion = forDerniereVersion;
    }

    public void setForIdFamilleMax(boolean forIdFamilleMax) {
        this.forIdFamilleMax = forIdFamilleMax;
    }

    public void setForIdFamilleModif(String forIdFamilleModif) {
        this.forIdFamilleModif = forIdFamilleModif;
    }

    public void setForIdPeriodeValidite(String forIdPeriodeValidite) {
        this.forIdPeriodeValidite = forIdPeriodeValidite;
    }

    public void setForIdQd(String forIdQd) {
        this.forIdQd = forIdQd;
    }

    public void setForTypeModif(String forTypeModif) {
        this.forTypeModif = forTypeModif;
    }

    public void setIdPeriodeValToIgnore(String idPeriodeValToIgnore) {
        this.idPeriodeValToIgnore = idPeriodeValToIgnore;
    }

    public void setOrderByDateDebutAsc(boolean orderByDateDebutAsc) {
        this.orderByDateDebutAsc = orderByDateDebutAsc;
    }

    public void setOrderByDateDebutDesc(boolean orderByDateDebutDesc) {
        this.orderByDateDebutDesc = orderByDateDebutDesc;
    }

}