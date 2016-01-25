package globaz.corvus.db.adaptation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.tools.PRDateFormater;

/**
 * 
 * @author HPE
 * 
 */
public class REPmtFictifManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsTypeDonnee = "";
    private String forMoisAnnee = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getWhere(BStatement statement) {

        String sqlWhere = "";

        if (!JadeStringUtil.isEmpty(forMoisAnnee)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection()
                    + REPmtFictif.TABLE_NAME_PMT_FICTIF
                    + "."
                    + REPmtFictif.FIELDNAME_MOIS_RAPPORT
                    + " = "
                    + _dbWriteNumeric(statement.getTransaction(),
                            PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forMoisAnnee));
        }

        if (!JadeStringUtil.isEmpty(forCsTypeDonnee)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + REPmtFictif.TABLE_NAME_PMT_FICTIF + "." + REPmtFictif.FIELDNAME_TYPE_DONNEES
                    + " = " + _dbWriteNumeric(statement.getTransaction(), forCsTypeDonnee);
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REPmtFictif();
    }

    public String getForCsTypeDonnee() {
        return forCsTypeDonnee;
    }

    public String getForMoisAnnee() {
        return forMoisAnnee;
    }

    @Override
    public String getOrderByDefaut() {
        return REPmtFictif.FIELDNAME_ID_PAIEMENT_FICTIF;
    }

    public void setForCsTypeDonnee(String forCsTypeDonnee) {
        this.forCsTypeDonnee = forCsTypeDonnee;
    }

    public void setForMoisAnnee(String forMoisAnnee) {
        this.forMoisAnnee = forMoisAnnee;
    }

}
