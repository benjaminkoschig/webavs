/**
 * 
 */
package globaz.osiris.db.contentieux;

import globaz.aquila.api.ICOContentieuxConstante;
import globaz.aquila.api.ICOEtapeConstante;
import globaz.aquila.api.ICOHistoriqueConstante;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <pre>
 * select oedexe from webavs.cohistp h
 * inner join webavs.COCAVSP a on a.OAICON = h.OAICON
 * inner join webavs.COETAPP e on e.odieta = h.odieta
 * where oaisec=201108
 * and odteta=5200034 and h.ofiseq=1
 * and oebann='2';
 * </pre>
 * 
 * @author SEL
 */
public class CADateExecutionSommationAquilaManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String AND = " AND ";
    private static final String INNER_JOIN = " INNER JOIN ";
    private static final String ON = " ON ";

    private String forCsEtape = null;
    private String forIdSection = null;
    private String forSeqCon = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = new StringBuffer(_getCollection() + ICOHistoriqueConstante.TABLE_NAME + " h");

        from.append(CADateExecutionSommationAquilaManager.INNER_JOIN + _getCollection()
                + ICOContentieuxConstante.TABLE_NAME_AVS + " a");
        from.append(CADateExecutionSommationAquilaManager.ON + "a." + ICOContentieuxConstante.FNAME_ID_CONTENTIEUX
                + "=" + "h." + ICOHistoriqueConstante.FNAME_ID_CONTENTIEUX);

        from.append(CADateExecutionSommationAquilaManager.INNER_JOIN + _getCollection() + ICOEtapeConstante.TABLE_NAME
                + " e");
        from.append(CADateExecutionSommationAquilaManager.ON + "e." + ICOEtapeConstante.FNAME_ID_ETAPE + "=" + "h."
                + ICOHistoriqueConstante.FNAME_ID_ETAPE);

        return from.toString();
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        StringBuffer sqlWhere = new StringBuffer("");

        if (!JadeStringUtil.isBlank(getForIdSection())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_ID_SECTION + "="
                            + this._dbWriteNumeric(statement.getTransaction(), getForIdSection()));
        }

        if (!JadeStringUtil.isBlank(getForSeqCon())) {
            addCondition(
                    sqlWhere,
                    "h." + ICOHistoriqueConstante.FNAME_ID_SEQUENCE + "="
                            + this._dbWriteNumeric(statement.getTransaction(), getForSeqCon()));
        }

        if (!JadeStringUtil.isBlank(getForCsEtape())) {
            addCondition(
                    sqlWhere,
                    ICOEtapeConstante.FNAME_LIBETAPE + "="
                            + this._dbWriteNumeric(statement.getTransaction(), getForCsEtape()));
        }

        addCondition(sqlWhere, ICOHistoriqueConstante.FNAME_EST_ANNULE + "=" + BConstants.DB_BOOLEAN_FALSE_DELIMITED);

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CADateExecutionSommationAquila();
    }

    /**
     * Permet l'ajout d'une condition dans la clause WHERE. <br>
     * 
     * @param sqlWhere
     * @param condition
     *            à ajouter au where
     */
    protected void addCondition(StringBuffer sqlWhere, String condition) {
        if (sqlWhere.length() != 0) {
            sqlWhere.append(CADateExecutionSommationAquilaManager.AND);
        }
        sqlWhere.append(condition);
    }

    /**
     * @return the forCsEtape
     */
    public String getForCsEtape() {
        return forCsEtape;
    }

    /**
     * @return the forIdSection
     */
    public String getForIdSection() {
        return forIdSection;
    }

    /**
     * @return the forSeqCon
     */
    public String getForSeqCon() {
        return forSeqCon;
    }

    /**
     * @param forCsEtape
     *            the forCsEtape to set
     */
    public void setForCsEtape(String forCsEtape) {
        this.forCsEtape = forCsEtape;
    }

    /**
     * @param forIdSection
     *            the forIdSection to set
     */
    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

    /**
     * @param forSeqCon
     *            the forSeqCon to set
     */
    public void setForSeqCon(String forSeqCon) {
        this.forSeqCon = forSeqCon;
    }
}
