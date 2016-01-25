package globaz.osiris.db.contentieux;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;

/**
 * @author SEL
 * 
 */
public class CADossierEtapeManager extends CAContentieuxManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String AND = " AND ";
    private static final String DIFF_DE = "<>";
    private static final String EGAL = " = ";
    private static final String FROM = " FROM ";
    private static final String INNER_JOIN = " INNER JOIN ";
    private static final String ON = " ON ";
    private static final String SELECT = "SELECT ";
    private static final String WHERE = " WHERE ";
    private static final String ZERO = "0";

    private String csEtape = "";
    private String idSequence = "";

    /**
     * Renvoie la liste des champs.
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer _fields = new StringBuffer();

        _fields.append(_getCollection() + CACompteAnnexe.TABLE_CACPTAP + "." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE);
        _fields.append("," + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + "." + CACompteAnnexe.FIELD_DESCRIPTION);
        _fields.append("," + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + "." + CACompteAnnexe.FIELD_IDEXTERNEROLE);
        _fields.append("," + _getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_SOLDE);
        _fields.append("," + _getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_NOPOURSUITE);
        _fields.append("," + _getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_IDSECTION);
        _fields.append("," + _getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_IDEXTERNE);

        return _fields.toString();
    }

    /**
     * Retourne la clause FROM de la requete SQL (la table) <br>
     * Requete finale voulue :
     * 
     * <pre>
     * select idexternerole, idexterne, idlastetapectx
     * from (webavs.cacptap
     *   inner join (webavs.casectp
     *     inner join webavs.caevctp
     *     on WEBAVS.CASECTP.IDSECTION = webavs.caevctp.idsection
     *   ) on webavs.cacptap.idcompteannexe = WEBAVS.CASECTP.idcompteannexe
     *   inner join webavs.CAPECTP on webavs.CAPECTP.IDPARAMETREETAPE = webavs.caevctp.IDPARAMETREETAPE
     * where dateExecution = 0 and dateDeclenchement <> 0 and idetape = 2 and webavs.CAPECTP.idseqcon = 2
     * </pre>
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + CACompteAnnexe.TABLE_CACPTAP + CADossierEtapeManager.INNER_JOIN + "("
                + _getCollection() + CASection.TABLE_CASECTP + CADossierEtapeManager.INNER_JOIN + _getCollection()
                + CAEvenementContentieux.TABLE_CAEVCTP + CADossierEtapeManager.ON + _getCollection()
                + CASection.TABLE_CASECTP + "." + CASection.FIELD_IDSECTION + CADossierEtapeManager.EGAL
                + _getCollection() + CAEvenementContentieux.TABLE_CAEVCTP + "." + "idsection" + ")"
                + CADossierEtapeManager.ON + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + "."
                + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + CADossierEtapeManager.EGAL + _getCollection()
                + CASection.TABLE_CASECTP + "." + CASection.FIELD_IDCOMPTEANNEXE + CADossierEtapeManager.INNER_JOIN
                + _getCollection() + "CAPECTP" + CADossierEtapeManager.ON + _getCollection()
                + "CAPECTP.IDPARAMETREETAPE" + CADossierEtapeManager.EGAL + _getCollection()
                + CAEvenementContentieux.TABLE_CAEVCTP + "." + "IDPARAMETREETAPE";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.aquila.db.access.poursuite.COContentieuxManager#_getWhere(globaz .globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        // ctx.ODIETA IN (SELECT WEBAVS.COTRANP.ODIETA FROM WEBAVS.COTRANP WHERE
        // WEBAVS.COTRANP.OGIESU=40)
        if (sqlWhere.length() != 0) {
            sqlWhere.append(CADossierEtapeManager.AND);
        }
        sqlWhere.append(_getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_SOLDE + ">"
                + CADossierEtapeManager.ZERO);
        sqlWhere.append(CADossierEtapeManager.AND);
        sqlWhere.append(_getCollection() + CAEvenementContentieux.TABLE_CAEVCTP + "." + "dateExecution");
        sqlWhere.append(CADossierEtapeManager.EGAL + CADossierEtapeManager.ZERO);
        sqlWhere.append(CADossierEtapeManager.AND);
        sqlWhere.append(_getCollection() + CAEvenementContentieux.TABLE_CAEVCTP + "." + "dateDeclenchement");
        sqlWhere.append(CADossierEtapeManager.DIFF_DE + CADossierEtapeManager.ZERO);
        sqlWhere.append(CADossierEtapeManager.AND);
        sqlWhere.append("idetape");
        sqlWhere.append(CADossierEtapeManager.EGAL + getIdEtape());
        if (!JadeStringUtil.isBlank(getIdSequence()) && !getIdSequence().equals("-1")) {
            sqlWhere.append(CADossierEtapeManager.AND);
            sqlWhere.append(_getCollection() + "CAPECTP.idseqcon");
            sqlWhere.append(CADossierEtapeManager.EGAL + getIdSequence());
        }

        return sqlWhere.toString();
    }

    /**
     * @return the csEtape
     */
    public String getCsEtape() {
        return csEtape;
    }

    /**
     * Retourne le résultat de la requete suivante :
     * 
     * <pre>
     * SELECT idetape FROM WEBAVS.CAETCTP WHERE TYPEETAPE =
     * </pre>
     * 
     * @param csSequence
     * @return idEtape
     */
    private String getIdEtape() {
        StringBuffer sql = new StringBuffer("(");
        sql.append(CADossierEtapeManager.SELECT + "idetape");
        sql.append(CADossierEtapeManager.FROM + _getCollection() + "CAETCTP");
        sql.append(CADossierEtapeManager.WHERE + "TYPEETAPE" + CADossierEtapeManager.EGAL + getCsEtape());
        sql.append(")");
        return sql.toString();
    }

    /**
     * @return the idSequence
     */
    public String getIdSequence() {
        return idSequence;
    }

    /**
     * @param idEtape
     *            the idEtape to set
     */
    public void setCsEtape(String idEtape) {
        csEtape = idEtape;
    }

    /**
     * @param idSequence
     *            the idSequence to set
     */
    public void setIdSequence(String idSequence) {
        this.idSequence = idSequence;
    }

}
