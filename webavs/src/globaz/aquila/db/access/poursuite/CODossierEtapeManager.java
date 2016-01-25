/**
 *
 */
package globaz.aquila.db.access.poursuite;

import globaz.aquila.api.ICOContentieuxConstante;
import globaz.aquila.api.ICOHistoriqueConstante;
import globaz.aquila.common.COBManager;
import globaz.aquila.db.access.batch.COTransition;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;

/**
 * Manager pour la liste des dossiers pour une étape contentieux.
 * 
 * @author SEL
 */
public class CODossierEtapeManager extends COContentieuxManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Cet attribut a été créé afin d'éviter d'utiliser l'attribut forIdEtape de COContentieuxManager dans le cadre de
     * la liste des dossiers pour une étape
     * 
     * En effet, COContentieuxManager utilise forIdEtape sans le préfixer d'une table dans sa clause where Par
     * conséquent, si forIdEtape est renseigné et que le from de CODossierEtapeManager inclut COHISTP le nom ODIETA est
     * ambigu
     */
    private String forIdEtapeForListDossierEtape = "";

    private String forIdEtapeSuivanteForListDossierEtape = "";

    /**
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer _fields = new StringBuffer();

        _fields.append(_getTableName() + "." + ICOContentieuxConstante.FNAME_DATE_DECLENCHEMENT);
        _fields.append("," + _getTableName() + "." + ICOContentieuxConstante.FNAME_DATE_EXECUTION);
        _fields.append("," + _getTableName() + "." + ICOContentieuxConstante.FNAME_ID_COMPTE_ANNEXE);
        _fields.append("," + _getTableName() + "." + ICOContentieuxConstante.FNAME_ID_CONTENTIEUX);
        _fields.append("," + _getTableName() + "." + ICOContentieuxConstante.FNAME_ID_ETAPE);
        _fields.append("," + _getTableName() + "." + ICOContentieuxConstante.FNAME_ID_SECTION);
        _fields.append("," + _getTableName() + "." + ICOContentieuxConstante.FNAME_ID_SEQUENCE);

        _fields.append("," + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + "."
                + CACompteAnnexe.FIELD_IDCOMPTEANNEXE);
        _fields.append("," + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + "." + CACompteAnnexe.FIELD_DESCRIPTION);
        _fields.append("," + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + "." + CACompteAnnexe.FIELD_IDEXTERNEROLE);
        _fields.append("," + _getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_SOLDE);
        _fields.append("," + _getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_NOPOURSUITE);
        _fields.append("," + _getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_IDSECTION);
        _fields.append("," + _getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_IDEXTERNE);

        return _fields.toString();
    }

    /**
     * <pre>
     * FROM (WEBAVS.CACPTAP INNER JOIN (WEBAVS.CASECTP
     *   INNER JOIN WEBAVS.COCAVSP ctx ON WEBAVS.CASECTP.IDSECTION = ctx.OAISEC)
     * ON WEBAVS.CACPTAP.IDCOMPTEANNEXE = ctx.OAICOA)
     * LEFT JOIN webavs.COHISTP on ctx.oaicon = webavs.COHISTP.oaicon
     * </pre>
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String prefix = _getCollection();
        String ctx = _getTableName() + ".";

        StringBuffer from = new StringBuffer("(");
        from.append(prefix + CACompteAnnexe.TABLE_CACPTAP);
        from.append(COBManager.INNER_JOIN + "(");
        from.append(prefix + CASection.TABLE_CASECTP);
        from.append(COBManager.INNER_JOIN);
        from.append(_getTableName());
        from.append(COBManager.ON + prefix + CASection.TABLE_CASECTP + "." + CASection.FIELD_IDSECTION);
        from.append(COBManager.EGAL + ctx + ICOContentieuxConstante.FNAME_ID_SECTION + ")");
        from.append(COBManager.ON + prefix + CACompteAnnexe.TABLE_CACPTAP + "." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE);
        from.append(COBManager.EGAL + ctx + ICOContentieuxConstante.FNAME_ID_COMPTE_ANNEXE + ")");
        if (getForNumPoursuite().length() != 0) {
            from.append(COBManager.LEFT_JOIN + prefix + ICOHistoriqueConstante.TABLE_NAME);
            from.append(COBManager.ON + ctx + ICOContentieuxConstante.FNAME_ID_CONTENTIEUX);
            from.append(COBManager.EGAL + prefix + ICOHistoriqueConstante.TABLE_NAME + "."
                    + ICOHistoriqueConstante.FNAME_ID_CONTENTIEUX);
        }
        return from.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.aquila.db.access.poursuite.COContentieuxManager#_getWhere(globaz .globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer(super._getWhere(statement));

        if (!JadeStringUtil.isBlankOrZero(getForIdEtapeForListDossierEtape())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(COBManager.AND);
            }
            sqlWhere.append(_getTableName() + "." + ICOContentieuxConstante.FNAME_ID_ETAPE + COBManager.EGAL
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdEtapeForListDossierEtape()));
        }

        if (!JadeStringUtil.isBlankOrZero(getForIdEtapeSuivanteForListDossierEtape())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(COBManager.AND);
            }
            sqlWhere.append(_getTableName() + "." + ICOContentieuxConstante.FNAME_ID_ETAPE);
            sqlWhere.append(" IN (");
            sqlWhere.append(COBManager.SELECT + _getCollection() + COTransition.TABLE_NAME + "."
                    + COTransition.FNAME_ID_ETAPE);
            sqlWhere.append(COBManager.FROM + _getCollection() + COTransition.TABLE_NAME);
            sqlWhere.append(COBManager.WHERE + _getCollection() + COTransition.TABLE_NAME + "."
                    + COTransition.FNAME_ID_ETAPE_SUIVANTE);
            sqlWhere.append(COBManager.EGAL
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdEtapeSuivanteForListDossierEtape()));
            sqlWhere.append(")");
        }

        return sqlWhere.toString();
    }

    public String getForIdEtapeForListDossierEtape() {
        return forIdEtapeForListDossierEtape;
    }

    public String getForIdEtapeSuivanteForListDossierEtape() {
        return forIdEtapeSuivanteForListDossierEtape;
    }

    public void setForIdEtapeForListDossierEtape(String forIdEtapeForListDossierEtape) {
        this.forIdEtapeForListDossierEtape = forIdEtapeForListDossierEtape;
    }

    public void setForIdEtapeSuivanteForListDossierEtape(String forIdEtapeSuivanteForListDossierEtape) {
        this.forIdEtapeSuivanteForListDossierEtape = forIdEtapeSuivanteForListDossierEtape;
    }

}
