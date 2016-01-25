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
 * Manager pour la méthode CASection.isSectionAuxPoursuites()
 * 
 * @author SEL <br>
 *         Date : 7 sept. 2010
 */
public class CASectionAuxPoursuitesManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String AND = " AND ";
    private static final String INNER_JOIN = " INNER JOIN ";
    private static final String ON = " ON ";

    private String forIdSection = "";
    private boolean onlySoldeOuvert = false;
    private boolean soldeDifferentZero = false;

    /**
     * <pre>
     * select * from ciciweb.casectp s inner join ciciweb.cocavsp c on c.oaisec=s.idsection inner join
     * ciciweb.cohistp h on h.oaicon=c.oaicon inner join ciciweb.coetapp e on e.odieta=h.odieta where s.idsection=17843
     * and h.oebann='2' and e.odteta=5200001
     * </pre>
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer fromBuffer = new StringBuffer("");
        fromBuffer.append(_getCollection() + CASection.TABLE_CASECTP + " " + CASection.TABLE_CASECTP);
        fromBuffer.append(CASectionAuxPoursuitesManager.INNER_JOIN);
        fromBuffer.append(_getCollection() + ICOContentieuxConstante.TABLE_NAME_AVS + " "
                + ICOContentieuxConstante.TABLE_NAME_AVS);
        fromBuffer.append(CASectionAuxPoursuitesManager.ON);
        fromBuffer.append(ICOContentieuxConstante.TABLE_NAME_AVS + "." + ICOContentieuxConstante.FNAME_ID_SECTION);
        fromBuffer.append("=");
        fromBuffer.append(CASection.TABLE_CASECTP + "." + CASection.FIELD_IDSECTION);
        fromBuffer.append(CASectionAuxPoursuitesManager.INNER_JOIN);
        fromBuffer.append(_getCollection() + ICOHistoriqueConstante.TABLE_NAME + " "
                + ICOHistoriqueConstante.TABLE_NAME);
        fromBuffer.append(CASectionAuxPoursuitesManager.ON);
        fromBuffer.append(ICOHistoriqueConstante.TABLE_NAME + "." + ICOHistoriqueConstante.FNAME_ID_CONTENTIEUX);
        fromBuffer.append("=");
        fromBuffer.append(ICOContentieuxConstante.TABLE_NAME_AVS + "." + ICOContentieuxConstante.FNAME_ID_CONTENTIEUX);
        fromBuffer.append(CASectionAuxPoursuitesManager.INNER_JOIN);
        fromBuffer.append(_getCollection() + ICOEtapeConstante.TABLE_NAME + " " + ICOEtapeConstante.TABLE_NAME);
        fromBuffer.append(CASectionAuxPoursuitesManager.ON);
        fromBuffer.append(ICOEtapeConstante.TABLE_NAME + "." + ICOEtapeConstante.FNAME_ID_ETAPE);
        fromBuffer.append("=");
        fromBuffer.append(ICOHistoriqueConstante.TABLE_NAME + "." + ICOHistoriqueConstante.FNAME_ID_ETAPE);

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

        sqlWhere.append(ICOHistoriqueConstante.TABLE_NAME + "." + ICOHistoriqueConstante.FNAME_EST_ANNULE + "="
                + BConstants.DB_BOOLEAN_FALSE_DELIMITED);
        sqlWhere.append(CASectionAuxPoursuitesManager.AND);
        sqlWhere.append(ICOEtapeConstante.TABLE_NAME + "." + ICOEtapeConstante.FNAME_LIBETAPE + "="
                + ICOEtape.CS_REQUISITION_DE_POURSUITE_ENVOYEE);

        if (!JadeStringUtil.isBlank(getForIdSection())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(CASectionAuxPoursuitesManager.AND);
            }
            sqlWhere.append(CASection.TABLE_CASECTP + "." + CASection.FIELD_IDSECTION + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdSection()));
        }

        if (isOnlySoldeOuvert()) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(CASectionAuxPoursuitesManager.AND);
            }

            sqlWhere.append(CASection.TABLE_CASECTP + "." + CASection.FIELD_SOLDE + " > 0");
        }

        if (isSoldeDifferentZero()) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(CASectionAuxPoursuitesManager.AND);
            }

            sqlWhere.append(CASection.TABLE_CASECTP + "." + CASection.FIELD_SOLDE + " <> 0");
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
        return new CASectionAuxPoursuites();
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
    public boolean isOnlySoldeOuvert() {
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
    public void setOnlySoldeOuvert(boolean onlySoldeOuvert) {
        this.onlySoldeOuvert = onlySoldeOuvert;
    }

    public boolean isSoldeDifferentZero() {
        return soldeDifferentZero;
    }

    public void setSoldeDifferentZero(boolean soldeDifferentZero) {
        this.soldeDifferentZero = soldeDifferentZero;
    }
}
