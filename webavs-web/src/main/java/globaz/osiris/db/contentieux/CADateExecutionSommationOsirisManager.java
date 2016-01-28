/**
 * 
 */
package globaz.osiris.db.contentieux;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <pre>
 * select DATEEXECUTION from webavs.CAEVCTP ev
 * inner join webavs.CAPECTP p on ev.IDPARAMETREETAPE=p.IDPARAMETREETAPE
 * inner join webavs.CAETCTP e on e.idEtape=p.idetape
 * where idsection=75 and idseqcon=2 and typeEtape=216002
 * </pre>
 * 
 * @author SEL
 * 
 */
public class CADateExecutionSommationOsirisManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String AND = " AND ";
    private static final String INNER_JOIN = " INNER JOIN ";
    private static final String ON = " ON ";

    private String forIdSection = null;
    private String forIdSeqCon = null;
    private String forTypeEtape = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = new StringBuffer(_getCollection() + CAEvenementContentieux.TABLE_CAEVCTP + " ev");

        from.append(CADateExecutionSommationOsirisManager.INNER_JOIN + _getCollection() + "CAPECTP" + " p");
        from.append(CADateExecutionSommationOsirisManager.ON + "ev." + "IDPARAMETREETAPE" + "=" + "p."
                + "IDPARAMETREETAPE");

        from.append(CADateExecutionSommationOsirisManager.INNER_JOIN + _getCollection() + "CAETCTP" + " e");
        from.append(CADateExecutionSommationOsirisManager.ON + "e." + "idEtape" + "=" + "p." + "idEtape");

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
            addCondition(sqlWhere, "idsection=" + this._dbWriteNumeric(statement.getTransaction(), getForIdSection()));
        }

        if (!JadeStringUtil.isBlank(getForIdSeqCon())) {
            addCondition(sqlWhere, "idseqcon=" + this._dbWriteNumeric(statement.getTransaction(), getForIdSeqCon()));
        }

        if (!JadeStringUtil.isBlank(getForTypeEtape())) {
            addCondition(sqlWhere, "typeEtape=" + this._dbWriteNumeric(statement.getTransaction(), getForTypeEtape()));
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
        return new CAEvenementContentieux();
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
            sqlWhere.append(CADateExecutionSommationOsirisManager.AND);
        }
        sqlWhere.append(condition);
    }

    /**
     * @return the forIdSection
     */
    public String getForIdSection() {
        return forIdSection;
    }

    /**
     * @return the forIdSeqCon
     */
    public String getForIdSeqCon() {
        return forIdSeqCon;
    }

    /**
     * @return the forTypeEtape
     */
    public String getForTypeEtape() {
        return forTypeEtape;
    }

    /**
     * @param forIdSection
     *            the forIdSection to set
     */
    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

    /**
     * @param forIdSeqCon
     *            the forIdSeqCon to set
     */
    public void setForIdSeqCon(String forIdSeqCon) {
        this.forIdSeqCon = forIdSeqCon;
    }

    /**
     * @param forTypeEtape
     *            the forTypeEtape to set
     */
    public void setForTypeEtape(String forTypeEtape) {
        this.forTypeEtape = forTypeEtape;
    }

}
