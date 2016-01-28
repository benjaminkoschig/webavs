/*
 * Créé le 20 janvier 2009
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
public class RFQdSoldeChargeManager extends BManager {

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
    private String forIdQd = "";
    private String forIdSoldeCharge = "";

    private String forTypeModif = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFQdSoldeChargeManager
     */
    public RFQdSoldeChargeManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFields(BStatement statement) {
        if (forIdFamilleMax) {
            return " max(" + RFQdSoldeCharge.FIELDNAME_ID_FAMILLE_MODIFICATION + ") as " + aliasFieldName;
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
                return ((RFQdSoldeCharge) _createNewEntity())._getFromAlias(aliasName);
            } catch (Exception e) {
                return null;
            }
        } else {
            return super._getFrom(statement);
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
            tblName = schema + RFQdSoldeCharge.TABLE_NAME + ".";
        }

        if (!JadeStringUtil.isBlankOrZero(getForIdQd())) {
            sqlWhere.newClause().append(tblName + RFQdSoldeCharge.FIELDNAME_ID_QD).appendEgal().append(getForIdQd());
        }

        if (!JadeStringUtil.isBlankOrZero(getForIdFamilleModif())) {
            sqlWhere.newClause().append(tblName + RFQdSoldeCharge.FIELDNAME_ID_FAMILLE_MODIFICATION).appendEgal()
                    .append(getForIdFamilleModif());
        }

        if (!JadeStringUtil.isBlankOrZero(getForIdSoldeCharge())) {
            sqlWhere.newClause().append(tblName + RFQdSoldeCharge.FIELDNAME_ID_SOLDE_CHARGE).appendEgal()
                    .append(getForIdSoldeCharge());
        }

        if (!JadeStringUtil.isBlank(getForTypeModif())) {
            sqlWhere.newClause().append(tblName + RFQdSoldeCharge.FIELDNAME_TYPE_MODIF).appendEgal()
                    .append(getForTypeModif());
        }

        if (forDerniereVersion) {
            sqlWhere.newClause().append(tblName + RFQdSoldeCharge.FIELDNAME_TYPE_MODIF).append(" <> ")
                    .append(IRFQd.CS_SUPPRESSION);
            sqlWhere.newClause().append("not exists (select * from ").append(schema + RFQdSoldeCharge.TABLE_NAME)
                    .append(" where ").append(RFQdSoldeCharge.FIELDNAME_ID_FAMILLE_MODIFICATION).appendEgal()
                    .append(aliasName + ".").append(RFQdSoldeCharge.FIELDNAME_ID_FAMILLE_MODIFICATION).append(" and ")
                    .append(RFQdSoldeCharge.FIELDNAME_ID_SOLDE_CHARGE).append(" > " + aliasName + ".")
                    .append(RFQdSoldeCharge.FIELDNAME_ID_SOLDE_CHARGE).append(" and ")
                    .append(RFQdSoldeCharge.FIELDNAME_ID_QD).appendEgal().append(getForIdQd()).append(")");
        }

        if (forIdFamilleMax) {
            sqlWhere = new PRWhereStringBuffer(schema);
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFQdSoldeCharge)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        if (forIdFamilleMax) {
            return new RFQdSoldeCharge() {

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
            return new RFQdSoldeCharge();
        }
    }

    /**
     * @return the forIdFamilleModif
     */
    public String getForIdFamilleModif() {
        return forIdFamilleModif;
    }

    /**
     * @return the forIdQd
     */
    public String getForIdQd() {
        return forIdQd;
    }

    /**
     * @return the forIdSoldeCharge
     */
    public String getForIdSoldeCharge() {
        return forIdSoldeCharge;
    }

    /**
     * @return the forTypeModif
     */
    public String getForTypeModif() {
        return forTypeModif;
    }

    /**
     * @return the forDerniereVersion
     */
    public String isForDerniereVersion() {
        return Boolean.toString(forDerniereVersion);
    }

    /**
     * @return the forIdFamilleMax
     */
    public boolean isForIdFamilleMax() {
        return forIdFamilleMax;
    }

    /**
     * @param forDerniereVersion
     *            the forDerniereVersion to set
     */
    public void setForDerniereVersion(String forDerniereVersion) {
        this.forDerniereVersion = JadeStringUtil.toBoolean(forDerniereVersion);
    }

    /**
     * @param forIdFamilleMax
     *            the forIdFamilleMax to set
     */
    public void setForIdFamilleMax(boolean forIdFamilleMax) {
        this.forIdFamilleMax = forIdFamilleMax;
    }

    /**
     * @param forIdFamilleModif
     *            the forIdFamilleModif to set
     */
    public void setForIdFamilleModif(String forIdFamilleModif) {
        this.forIdFamilleModif = forIdFamilleModif;
    }

    /**
     * @param forIdQd
     *            the forIdQd to set
     */
    public void setForIdQd(String forIdQd) {
        this.forIdQd = forIdQd;
    }

    /**
     * @param forIdSoldeCharge
     *            the forIdSoldeCharge to set
     */
    public void setForIdSoldeCharge(String forIdSoldeCharge) {
        this.forIdSoldeCharge = forIdSoldeCharge;
    }

    /**
     * @param forTypeModif
     *            the forTypeModif to set
     */
    public void setForTypeModif(String forTypeModif) {
        this.forTypeModif = forTypeModif;
    }

}
