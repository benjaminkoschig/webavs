package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.hercule.service.CEAffiliationService;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

/**
 * @author
 * @revision SCO 3 sept. 2010
 */
public class CEAttributionPtsCumulMasseManager extends BManager implements Serializable {

    private static final long serialVersionUID = -7566136704736931972L;
    private String forAnnee;
    private String forNumAffilie;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {

        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection()).append("CACPTAP CA ");
        sqlFrom.append("INNER JOIN " + _getCollection() + "CACPTRP CR ON (CA.IDCOMPTEANNEXE=CR.IDCOMPTEANNEXE)");

        return sqlFrom.toString();

    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        String idRole = CEAffiliationService.getRoleForAffilieParitaire(getSession());
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isBlankOrZero(getForNumAffilie())) {
            CEUtils.sqlAddCondition(sqlWhere, "IDEXTERNEROLE = '" + getForNumAffilie() + "'");
        }
        if (!JadeStringUtil.isBlankOrZero(getForAnnee())) {
            CEUtils.sqlAddCondition(sqlWhere, "ANNEE = " + getForAnnee());
        }
        CEUtils.sqlAddCondition(sqlWhere, "CA.IDROLE = " + idRole);
        CEUtils.sqlAddCondition(sqlWhere, "IDRUBRIQUE IN (" + CEUtils.getIdRubrique(getSession()) + ") ");

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEAttributionPtsCumulMasse();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForNumAffilie() {
        return forNumAffilie;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }
}
