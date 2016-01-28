/*
 * Créé le 27 juil. 07
 */
package globaz.corvus.db.ordresversements;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * @author HPE
 * 
 */
public class REOrdresVersementsManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPrestation = "";
    private String forIdRAD = "";
    private String forIdTiersBeneficiaire = "";
    private Long forIdRenteVerseeATort = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forIdTiersBeneficiaire)) {
            // if (whereClause.length() > 0) {
            // whereClause.append(" AND ");
            // }
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdPrestation)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REOrdresVersements.FIELDNAME_ID_PRESTATION);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), getForIdPrestation()));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdRAD)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REOrdresVersements.FIELDNAME_ID_RENTE_ACCORDEE_DIMINUEE);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), getForIdRAD()));
        }

        if (forIdRenteVerseeATort != null) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REOrdresVersements.FIELDNAME_ID_RENTE_VERSEE_A_TORT).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), forIdRenteVerseeATort.toString()));
        }

        return whereClause.toString();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REOrdresVersements();
    }

    /**
     * @return
     */
    public String getForIdPrestation() {
        return forIdPrestation;
    }

    public String getForIdRAD() {
        return forIdRAD;
    }

    /**
     * @return
     */
    public String getForIdTiersBeneficiaire() {
        return forIdTiersBeneficiaire;
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut order by defaut
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return REOrdresVersements.FIELDNAME_ID_ORDRE_VERSEMENT;
    }

    /**
     * @param string
     */
    public void setForIdPrestation(String string) {
        forIdPrestation = string;
    }

    public void setForIdRAD(String forIdRAD) {
        this.forIdRAD = forIdRAD;
    }

    /**
     * @param string
     */
    public void setForIdTiersBeneficiaire(String string) {
        forIdTiersBeneficiaire = string;
    }

    public void setForIdRenteVerseeATort(Long idRenteVerseeATort) {
        forIdRenteVerseeATort = idRenteVerseeATort;
    }

    public Long getForIdRenteVerseeATort() {
        return forIdRenteVerseeATort;
    }

}
