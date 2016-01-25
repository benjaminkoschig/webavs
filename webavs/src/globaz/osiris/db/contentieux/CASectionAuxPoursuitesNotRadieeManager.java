package globaz.osiris.db.contentieux;

import globaz.aquila.api.ICOContentieuxConstante;
import globaz.aquila.api.ICOEtape;
import globaz.aquila.api.ICOEtapeConstante;
import globaz.aquila.api.ICOHistoriqueConstante;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CASection;

/**
 * Manager pour la méthode CASection.isSectionAuxPoursuitesNotRadiee()
 * 
 * @author sel <br/>
 *         Date : 1 juin 2012
 */
public class CASectionAuxPoursuitesNotRadieeManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String AND = " AND ";
    private static final String INNER_JOIN = " INNER JOIN ";
    private static final String ON = " ON ";

    private String forIdSection = "";
    private Boolean onlySoldeOuvert = false;

    /**
     * <pre>
     * select * from ccjuweb.CASECTP s
     * INNER JOIN CCJUWEB.COCAVSP COCAVSP ON COCAVSP.OAISEC=s.IDSECTION 
     * where s.IDSECTION=434079 AND s.SOLDE > 0
     * and 5200001 IN (
     * select e.ODTETA from CCJUWEB.COCAVSP c
     * INNER JOIN CCJUWEB.COHISTP h ON h.OAICON=c.OAICON 
     * INNER JOIN CCJUWEB.COETAPP e ON e.ODIETA=h.ODIETA 
     * WHERE h.OEBANN='2' AND c.oaisec=434079
     * )
     * and 5200027 not in (
     * select e.ODTETA from CCJUWEB.COCAVSP c
     * INNER JOIN CCJUWEB.COHISTP h ON h.OAICON=c.OAICON 
     * INNER JOIN CCJUWEB.COETAPP e ON e.ODIETA=h.ODIETA 
     * WHERE h.OEBANN='2' AND c.oaisec=434079
     * )
     * </pre>
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer fromBuffer = new StringBuffer("");

        fromBuffer.append(_getCollection() + CASection.TABLE_CASECTP + " " + CASection.TABLE_CASECTP);
        fromBuffer.append(CASectionAuxPoursuitesNotRadieeManager.INNER_JOIN);
        fromBuffer.append(_getCollection() + ICOContentieuxConstante.TABLE_NAME_AVS + " "
                + ICOContentieuxConstante.TABLE_NAME_AVS);
        fromBuffer.append(CASectionAuxPoursuitesNotRadieeManager.ON);
        fromBuffer.append(ICOContentieuxConstante.TABLE_NAME_AVS + "." + ICOContentieuxConstante.FNAME_ID_SECTION);
        fromBuffer.append("=");
        fromBuffer.append(CASection.TABLE_CASECTP + "." + CASection.FIELD_IDSECTION);

        return fromBuffer.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer("");

        if (JadeStringUtil.isBlank(getForIdSection())) {
            return "";
        }

        sqlWhere.append(ICOEtape.CS_REQUISITION_DE_POURSUITE_ENVOYEE).append(" IN (");
        sqlWhere.append(subQuery(getForIdSection()));
        sqlWhere.append(")");

        sqlWhere.append(CASectionAuxPoursuitesNotRadieeManager.AND);

        sqlWhere.append(ICOEtape.CS_POURSUITE_RADIEE).append(" NOT IN (");
        sqlWhere.append(subQuery(getForIdSection()));
        sqlWhere.append(")");

        sqlWhere.append(CASectionAuxPoursuitesNotRadieeManager.AND);
        sqlWhere.append(CASection.TABLE_CASECTP).append(".").append(CASection.FIELD_IDSECTION);
        sqlWhere.append("=").append(this._dbWriteNumeric(statement.getTransaction(), getForIdSection()));

        if (isOnlySoldeOuvert()) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(CASectionAuxPoursuitesNotRadieeManager.AND);
            }

            sqlWhere.append(CASection.TABLE_CASECTP).append(".").append(CASection.FIELD_SOLDE).append(" > 0");
        }

        return sqlWhere.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return null;
    }

    /**
     * @return the forIdSection
     */
    public String getForIdSection() {
        return forIdSection;
    }

    /**
     * @return the onlySoldeOuvert
     */
    public Boolean isOnlySoldeOuvert() {
        return onlySoldeOuvert;
    }

    /**
     * @param forIdSection
     *            the forIdSection to set
     */
    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

    /**
     * @param onlySoldeOuvert
     *            the onlySoldeOuvert to set
     */
    public void setOnlySoldeOuvert(Boolean onlySoldeOuvert) {
        this.onlySoldeOuvert = onlySoldeOuvert;
    }

    /**
     * <pre>
     * select e.ODTETA from CCJUWEB.COCAVSP c
     * INNER JOIN CCJUWEB.COHISTP h ON h.OAICON=c.OAICON 
     * INNER JOIN CCJUWEB.COETAPP e ON e.ODIETA=h.ODIETA 
     * WHERE h.OEBANN='2' AND c.oaisec=434079
     * </pre>
     * 
     * @return
     */
    private String subQuery(String idSection) {
        StringBuffer query = new StringBuffer();

        query.append("SELECT ").append(ICOEtapeConstante.TABLE_NAME).append(".")
                .append(ICOEtapeConstante.FNAME_LIBETAPE);

        // COCAVSP Contentieux
        query.append(" FROM ").append(_getCollection()).append(ICOContentieuxConstante.TABLE_NAME_AVS).append(" ")
                .append(ICOContentieuxConstante.TABLE_NAME_AVS);

        // COHISTP historique
        query.append(CASectionAuxPoursuitesNotRadieeManager.INNER_JOIN);
        query.append(_getCollection()).append(ICOHistoriqueConstante.TABLE_NAME).append(" ")
                .append(ICOHistoriqueConstante.TABLE_NAME);
        query.append(CASectionAuxPoursuitesNotRadieeManager.ON);
        query.append(ICOHistoriqueConstante.TABLE_NAME).append(".").append(ICOHistoriqueConstante.FNAME_ID_CONTENTIEUX);
        query.append("=");
        query.append(ICOContentieuxConstante.TABLE_NAME_AVS).append(".")
                .append(ICOContentieuxConstante.FNAME_ID_CONTENTIEUX);

        // COETAPP etape
        query.append(CASectionAuxPoursuitesNotRadieeManager.INNER_JOIN);
        query.append(_getCollection()).append(ICOEtapeConstante.TABLE_NAME).append(" ")
                .append(ICOEtapeConstante.TABLE_NAME);
        query.append(CASectionAuxPoursuitesNotRadieeManager.ON);
        query.append(ICOEtapeConstante.TABLE_NAME).append(".").append(ICOEtapeConstante.FNAME_ID_ETAPE);
        query.append("=");
        query.append(ICOHistoriqueConstante.TABLE_NAME).append(".").append(ICOHistoriqueConstante.FNAME_ID_ETAPE);

        query.append(" WHERE ");
        query.append(ICOHistoriqueConstante.TABLE_NAME).append(".").append(ICOHistoriqueConstante.FNAME_EST_ANNULE);
        query.append("=").append(BConstants.DB_BOOLEAN_FALSE_DELIMITED);
        query.append(CASectionAuxPoursuitesNotRadieeManager.AND);
        query.append(ICOContentieuxConstante.TABLE_NAME_AVS).append(".")
                .append(ICOContentieuxConstante.FNAME_ID_SECTION);
        query.append("=").append(idSection);

        return query.toString();
    }
}
