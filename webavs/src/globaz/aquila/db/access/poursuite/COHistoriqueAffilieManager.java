package globaz.aquila.db.access.poursuite;

import globaz.aquila.api.ICOContentieuxConstante;
import globaz.aquila.api.ICOEtapeConstante;
import globaz.aquila.api.ICOHistoriqueConstante;
import globaz.aquila.common.COBManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.external.IntRole;

/**
 * Récupération des informations du contentieux pour un affilié donné et une période donnée
 * 
 * <pre>
 * select ca.idexternerole, ca.idrole, h.odieta, e.odteta from webavsciam.cacptap ca
 *  inner join webavsciam.cocavsp ctx on ctx.oaicoa=ca.idcompteannexe
 *  inner join webavsciam.cohistp h on h.oaicon=ctx.oaicon
 *  inner join webavsciam.COETAPP e on e.odieta=h.odieta
 *  where idexternerole='000.318-00' and idrole in (517002, 517039)
 *  and oedexe between 20100101 and 20101201
 * </pre>
 * 
 * <pre>
 *  5200005 Cdp
 *  5200031 Cdp avec opposition
 *  5200032 rappel
 * </pre>
 * 
 * @author SCO
 * @since SCO 11 juin 2010
 */
public class COHistoriqueAffilieManager extends COBManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDebut;
    private String forDateFin;
    private String forNumAffilie;

    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer sqlFields = new StringBuffer("");

        sqlFields.append("CA." + CACompteAnnexe.FIELD_IDEXTERNEROLE + ", ");
        sqlFields.append("CA." + CACompteAnnexe.FIELD_IDROLE + ", ");
        sqlFields.append("H." + ICOEtapeConstante.FNAME_ID_ETAPE + ", ");
        sqlFields.append("E." + ICOEtapeConstante.FNAME_LIBETAPE);

        return sqlFields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer sqlFrom = new StringBuffer("");

        sqlFrom.append(_getCollection() + CACompteAnnexe.TABLE_CACPTAP + " CA");
        sqlFrom.append(" INNER JOIN " + _getCollection() + ICOContentieuxConstante.TABLE_NAME_AVS + " CTX ON CTX."
                + ICOContentieuxConstante.FNAME_ID_COMPTE_ANNEXE + " = CA." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE);
        sqlFrom.append(" INNER JOIN " + _getCollection() + ICOHistoriqueConstante.TABLE_NAME + " H ON H."
                + ICOHistoriqueConstante.FNAME_ID_CONTENTIEUX + " = CTX."
                + ICOContentieuxConstante.FNAME_ID_CONTENTIEUX);
        sqlFrom.append(" INNER JOIN " + _getCollection() + ICOEtapeConstante.TABLE_NAME + " E ON E."
                + ICOEtapeConstante.FNAME_ID_ETAPE + " = H." + ICOHistoriqueConstante.FNAME_ID_ETAPE);

        return sqlFrom.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer("");

        addCondition(sqlWhere, CACompteAnnexe.FIELD_IDROLE + " IN ( " + IntRole.ROLE_AFFILIE + ", "
                + IntRole.ROLE_AFFILIE_PARITAIRE + " )");

        if (!JadeStringUtil.isEmpty(getForNumAffilie())) {
            addCondition(
                    sqlWhere,
                    CACompteAnnexe.FIELD_IDEXTERNEROLE + " = "
                            + this._dbWriteString(statement.getTransaction(), getForNumAffilie()));
        }

        if (!JadeStringUtil.isEmpty(getForDateDebut()) && !JadeStringUtil.isEmpty(getForDateFin())) {
            addCondition(
                    sqlWhere,
                    ICOHistoriqueConstante.FNAME_DATE_EXECUTION + " BETWEEN "
                            + this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebut()) + " AND "
                            + this._dbWriteDateAMJ(statement.getTransaction(), getForDateFin()));
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new COHistoriqueAffilie();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForNumAffilie() {
        return forNumAffilie;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

}
