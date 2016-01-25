/*
 * Créé le 2 mars 06
 */
package globaz.ij.db.prononces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.ij.api.basseindemnisation.IIJBaseIndemnisation;
import globaz.ij.api.basseindemnisation.IIJFormulaireIndemnisation;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.basesindemnisation.IJFormulaireIndemnisation;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * @author hpe
 * 
 *         Manager de IJListeFormulairesNonRecus qui définit l'orderby, les date de début et de fin et la clause Where
 */
public class IJListeFormulairesNonRecusManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String dateDebutBaseIndemn = "";

    private String dateDebutPrononce = "";

    private String dateFinBaseIndemn = "";
    private String dateFinPrononce = "";

    private String fields = null;
    private String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        if (fields == null) {
            fields = IJListeFormulairesNonRecus.createFields(_getCollection());
        }

        return fields;
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = IJListeFormulairesNonRecus.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * 
     * @return la clause Where
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        String schema = _getCollection();

        // point ouvert 00658
        // ajout de l'etat decide
        sqlWhere += "(" + schema + IJPrononce.TABLE_NAME_PRONONCE + "." + IJPrononce.FIELDNAME_CS_ETAT + "="
                + IIJPrononce.CS_DECIDE + " OR " + schema + IJPrononce.TABLE_NAME_PRONONCE + "."
                + IJPrononce.FIELDNAME_CS_ETAT + "=" + IIJPrononce.CS_COMMUNIQUE + ")";

        sqlWhere += " AND ";

        sqlWhere += "(" + schema + IJBaseIndemnisation.TABLE_NAME + "." + IJBaseIndemnisation.FIELDNAME_ETAT + "="
                + IIJBaseIndemnisation.CS_OUVERT + " OR " + schema + IJBaseIndemnisation.TABLE_NAME + "."
                + IJBaseIndemnisation.FIELDNAME_ETAT + "=" + IIJBaseIndemnisation.CS_VALIDE + ")";

        sqlWhere += " AND ";

        sqlWhere += schema + IJFormulaireIndemnisation.TABLE_NAME + "." + IJFormulaireIndemnisation.FIELDNAME_ETAT
                + "=" + IIJFormulaireIndemnisation.CS_ENVOYE;

        if (!JadeStringUtil.isEmpty(dateDebutBaseIndemn) && !JadeStringUtil.isEmpty(dateFinBaseIndemn)) {

            sqlWhere += " AND ";

            sqlWhere += "((";
            sqlWhere += schema + IJBaseIndemnisation.TABLE_NAME + "." + IJBaseIndemnisation.FIELDNAME_DATEDEBUTPERIODE
                    + "<=" + _dbWriteDateAMJ(statement.getTransaction(), dateDebutBaseIndemn);
            sqlWhere += " AND ";

            sqlWhere += schema + IJBaseIndemnisation.TABLE_NAME + "." + IJBaseIndemnisation.FIELDNAME_DATEFINPERIODE
                    + ">=" + _dbWriteDateAMJ(statement.getTransaction(), dateDebutBaseIndemn);

            sqlWhere += ") OR (";

            sqlWhere += schema + IJBaseIndemnisation.TABLE_NAME + "." + IJBaseIndemnisation.FIELDNAME_DATEDEBUTPERIODE
                    + "<=" + _dbWriteDateAMJ(statement.getTransaction(), dateFinBaseIndemn);
            sqlWhere += " AND ";

            sqlWhere += schema + IJBaseIndemnisation.TABLE_NAME + "." + IJBaseIndemnisation.FIELDNAME_DATEFINPERIODE
                    + ">=" + _dbWriteDateAMJ(statement.getTransaction(), dateDebutBaseIndemn);
            sqlWhere += "))";
        }

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJListeFormulairesNonRecus();
    }

    /**
     * @return
     */
    public String getDateDebutBaseIndemn() {
        return dateDebutBaseIndemn;
    }

    /**
     * @return
     */
    public String getDateDebutPrononce() {
        return dateDebutPrononce;
    }

    /**
     * @return
     */
    public String getDateFinBaseIndemn() {
        return dateFinBaseIndemn;
    }

    /**
     * @return
     */
    public String getDateFinPrononce() {
        return dateFinPrononce;
    }

    /**
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return IJPrononce.FIELDNAME_ID_PRONONCE;
    }

    /**
     * @param string
     */
    public void setDateDebutBaseIndemn(String string) {
        dateDebutBaseIndemn = string;
    }

    /**
     * @param string
     */
    public void setDateDebutPrononce(String string) {
        dateDebutPrononce = string;
    }

    /**
     * @param string
     */
    public void setDateFinBaseIndemn(String string) {
        dateFinBaseIndemn = string;
    }

    /**
     * @param string
     */
    public void setDateFinPrononce(String string) {
        dateFinPrononce = string;
    }

}
