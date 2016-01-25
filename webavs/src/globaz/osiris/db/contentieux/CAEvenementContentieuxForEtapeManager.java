package globaz.osiris.db.contentieux;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;

public class CAEvenementContentieuxForEtapeManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdSection = new String();
    private String forIdSeqCon = new String();
    /**
     * Setter
     */
    private String forTypeEtape = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CAEVCTP INNER JOIN " + _getCollection() + "CAPECTP ON " + _getCollection()
                + "CAEVCTP.IDPARAMETREETAPE = " + _getCollection() + "CAPECTP.IDPARAMETREETAPE INNER JOIN "
                + _getCollection() + "CASECTP ON " + _getCollection() + "CAEVCTP.IDSECTION = " + _getCollection()
                + "CASECTP.IDSECTION";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement pour un idSection
        if (getForIdSection().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CASECTP.IDSECTION = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdSection());
        }

        // traitement du positionnement pour un typeEtape
        if (getForTypeEtape().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CAPECTP.IDPARAMETREETAPE = (SELECT IDPARAMETREETAPE FROM "
                    + _getCollection() + "CAETCTP INNER JOIN " + _getCollection() + "CAPECTP ON " + _getCollection()
                    + "CAETCTP.IDETAPE = " + _getCollection() + "CAPECTP.IDETAPE WHERE TYPEETAPE = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForTypeEtape()) + " AND " + _getCollection()
                    + "CAPECTP.IDSEQCON = " + this._dbWriteNumeric(statement.getTransaction(), getForIdSeqCon()) + ")";
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAEvenementContentieux();
    }

    public String getForIdSection() {
        return forIdSection;
    }

    public String getForIdSeqCon() {
        return forIdSeqCon;
    }

    public String getForTypeEtape() {
        return forTypeEtape;
    }

    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

    public void setForIdSeqCon(String forIdSeqCon) {
        this.forIdSeqCon = forIdSeqCon;
    }

    public void setForTypeEtape(String forTypeEtape) {
        this.forTypeEtape = forTypeEtape;
    }

}
