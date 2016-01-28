package globaz.musca.db.facturation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APISection;

/**
 * @author MMO
 * @since 26 juin 2012
 */
public class FACompensationDecomptePeriodiqueManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Constantes sql
    public static final String INNER_JOIN = " INNER JOIN ";
    public static final String ON = " ON ";

    private String forIdPassage;

    /**
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer sqlFields = new StringBuffer("");

        sqlFields.append("ca.IDTIERS, ");
        sqlFields.append("ca.IDEXTERNEROLE, ");
        sqlFields.append("ca.IDROLE, ");
        sqlFields.append("ca.ASURVEILLER, ");
        sqlFields.append("ef.IDENTETEFACTURE, ");
        sqlFields.append("ef.TOTALFACTURE, ");
        sqlFields.append("se.IDSECTION, ");
        sqlFields.append("se.SOLDE, ");
        sqlFields.append("se.IDEXTERNE, ");
        sqlFields.append("se.IDTYPESECTION ");

        return sqlFields.toString();

    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + "FAENTFP AS ef");
        sqlFrom.append(FACompensationDecomptePeriodiqueManager.INNER_JOIN);
        sqlFrom.append(_getCollection() + "CACPTAP AS ca");
        sqlFrom.append(FACompensationDecomptePeriodiqueManager.ON);
        sqlFrom.append("(ef.IDTIERS=ca.IDTIERS and ef.IDROLE=ca.IDROLE and ef.IDEXTERNEROLE=ca.IDEXTERNEROLE)");
        sqlFrom.append(FACompensationDecomptePeriodiqueManager.INNER_JOIN);
        sqlFrom.append(_getCollection() + "CASECTP AS se");
        sqlFrom.append(FACompensationDecomptePeriodiqueManager.ON);
        sqlFrom.append("(ca.IDCOMPTEANNEXE=se.IDCOMPTEANNEXE)");

        return sqlFrom.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {

        // les sections récupérées par ce manager sont uniquement des sections dont le solde est négatif
        // avec ce tri les sections avec un grand montant disponible pour une compensation seront en premier
        // et ainsi les décomptes compensés avec un minimum de lignes de compensations
        return " se.SOLDE ASC ";
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        // décomptes périodiques positifs qui ne sont pas des décisions d'intérêts
        sqlWhere.append("ef.IDSOUSTYPE IN (227001, 227002, 227003, 227004, 227005, 227006, 227007, 227008, 227009, 227010, 227011, 227012, 227040, 227041, 227042, 227043, 227044, 227045, 227046, 227047, 227061, 227062)");
        sqlWhere.append(" AND ");

        sqlWhere.append("ef.TOTALFACTURE > " + this._dbWriteNumeric(statement.getTransaction(), "0"));
        sqlWhere.append(" AND ");

        sqlWhere.append("ef.IDEXTERNEFACTURE NOT LIKE '______9%' ");
        sqlWhere.append(" AND ");

        // sections négatives avec un mode de compensation : Compensation déc périodique
        sqlWhere.append("se.solde < " + this._dbWriteNumeric(statement.getTransaction(), "0"));
        sqlWhere.append(" AND ");

        sqlWhere.append("se.IDMODECOMPENS = "
                + this._dbWriteNumeric(statement.getTransaction(), APISection.MODE_COMP_DEC_PERIODIQUE));

        if (!JadeStringUtil.isEmpty(getForIdPassage())) {
            sqlWhere.append(" AND ");
            sqlWhere.append("ef.idpassage = " + this._dbWriteNumeric(statement.getTransaction(), getForIdPassage()));
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new FACompensationDecomptePeriodique();
    }

    public String getForIdPassage() {
        return forIdPassage;
    }

    public void setForIdPassage(String forIdPassage) {
        this.forIdPassage = forIdPassage;
    }

}
