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
public class RFQdAugmentationManager extends BManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String aliasFieldName = "idFamMax";
    private String aliasName = "a";
    private boolean forDerniereVersion = false;
    private String forIdAugmentation = "";
    private boolean forIdFamilleMax = false;
    private String forIdFamilleModif = "";
    private String forIdQd = "";

    private String forTypeModif = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFQdAugmentationManager.
     */
    public RFQdAugmentationManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFields(BStatement statement) {
        if (forIdFamilleMax) {
            return " max(" + RFQdAugmentation.FIELDNAME_ID_FAMILLE_MODIFICATION + ") as " + aliasFieldName;
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
                return ((RFQdAugmentation) _createNewEntity())._getFromAlias(aliasName);
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
            tblName = schema + RFQdAugmentation.TABLE_NAME + ".";
        }

        if (!JadeStringUtil.isBlankOrZero(getForIdQd())) {
            sqlWhere.newClause().append(tblName + RFQdAugmentation.FIELDNAME_ID_QD).appendEgal().append(getForIdQd());
        }

        if (!JadeStringUtil.isBlankOrZero(getForIdFamilleModif())) {
            sqlWhere.newClause().append(tblName + RFQdAugmentation.FIELDNAME_ID_FAMILLE_MODIFICATION).appendEgal()
                    .append(getForIdFamilleModif());
        }

        if (!JadeStringUtil.isBlankOrZero(getForIdAugmentation())) {
            sqlWhere.newClause().append(tblName + RFQdAugmentation.FIELDNAME_ID_AUGMENTATION_QD).appendEgal()
                    .append(getForIdAugmentation());
        }

        if (!JadeStringUtil.isBlank(getForTypeModif())) {
            sqlWhere.newClause().append(tblName + RFQdAugmentation.FIELDNAME_TYPE_MODIF).appendEgal()
                    .append(getForTypeModif());
        }

        if (forDerniereVersion) {
            sqlWhere.newClause().append(tblName + RFQdAugmentation.FIELDNAME_TYPE_MODIF).append(" <> ")
                    .append(IRFQd.CS_SUPPRESSION);
            sqlWhere.newClause().append("not exists (select * from ").append(schema + RFQdAugmentation.TABLE_NAME)
                    .append(" where ").append(RFQdAugmentation.FIELDNAME_ID_FAMILLE_MODIFICATION).appendEgal()
                    .append(aliasName + ".").append(RFQdAugmentation.FIELDNAME_ID_FAMILLE_MODIFICATION).append(" and ")
                    .append(RFQdAugmentation.FIELDNAME_ID_AUGMENTATION_QD).append(" > " + aliasName + ".")
                    .append(RFQdAugmentation.FIELDNAME_ID_AUGMENTATION_QD).append(" and ")
                    .append(RFQdAugmentation.FIELDNAME_ID_QD).appendEgal().append(getForIdQd()).append(")");
        }

        if (forIdFamilleMax) {
            sqlWhere = new PRWhereStringBuffer(schema);
            /*
             * sqlWhere.newClause().append("not exists (select * from ").append(schema + RFQdAugmentation.TABLE_NAME)
             * .append(" where ").append(RFQdAugmentation.FIELDNAME_ID_FAMILLE_MODIFICATION).append( " > " + aliasName +
             * ".").append(RFQdAugmentation.FIELDNAME_ID_FAMILLE_MODIFICATION).append( ")");
             */
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFQdAugmentation)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        if (forIdFamilleMax) {
            return new RFQdAugmentation() {

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
            return new RFQdAugmentation();
        }
    }

    /**
     * @return the forIdAugmentation
     */
    public String getForIdAugmentation() {
        return forIdAugmentation;
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
     * @param forIdAugmentation
     *            the forIdAugmentation to set
     */
    public void setForIdAugmentation(String forIdAugmentation) {
        this.forIdAugmentation = forIdAugmentation;
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
     * @param forTypeModif
     *            the forTypeModif to set
     */
    public void setForTypeModif(String forTypeModif) {
        this.forTypeModif = forTypeModif;
    }

}
