/**
 * 
 */
package globaz.corvus.db.restitution;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;

/**
 * @author sel
 * 
 */
public class RERestitutionsSoldesManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateValeur = null;
    private String forRole = null;
    private String langue = null;

    @Override
    protected String _getSql(BStatement statement) {
        if (JadeStringUtil.isBlank(forRole)) {
            throw new NullPointerException("must have a role for this request");
        }
        if (JadeStringUtil.isBlank(forDateValeur)) {
            throw new NullPointerException("must have a date valeur for this request");
        }
        if (JadeDateUtil.isGlobazDate(forDateValeur)) {
            try {
                forDateValeur = (new JADate(forDateValeur)).toStrAMJ();
            } catch (JAException e) {
                JadeLogger.warn(this, "PROBLEM IN RERestitutionsSoldesManager._getSql() (" + e.toString() + ")");
                return "";
            }

        }

        // select idexternerole,description,se.idexterne,se.DATESECTION,cet.PCOLUT contentieux,sum(montant) solde
        // from webavsciam.cacptap ca

        // inner join webavsciam.casectp se on (ca.IDCOMPTEANNEXE = se.IDCOMPTEANNEXE)
        // inner join webavsciam.caoperp op on (se.IDSECTION = op.idsection)
        // inner join webavsciam.cajourp jo on (op.idjournal=jo.idjournal)
        // left join webavsciam.fwcoup cet on (se.IDLASTETATAQUILA = cet.pcosid and cet.plaide = 'F')
        // where idrole = 517038
        // and se.CATEGORIESECTION = 227026
        // and op.etat=205002
        // and jo.datevaleurcg <= 20111231
        // group by idexternerole,se.idexterne,description,se.DATESECTION,cet.pcolut
        // having sum(montant) <> 0
        // order by idexternerole,se.idexterne

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(CACompteAnnexe.TABLE_CACPTAP).append(".")
                .append(CACompteAnnexe.FIELD_IDEXTERNEROLE);
        sql.append(", ").append(CACompteAnnexe.TABLE_CACPTAP).append(".").append(CACompteAnnexe.FIELD_DESCRIPTION);
        sql.append(", ").append(CASection.TABLE_CASECTP).append(".").append(CASection.FIELD_IDSECTION);
        sql.append(", ").append(CASection.FIELD_IDEXTERNE);
        sql.append(", ").append(CASection.FIELD_DATESECTION);
        sql.append(", ").append("cet.PCOLUT ").append(RERestitutionsSoldes.FIELD_FICTIF_CONTENTIEUX);
        sql.append(", ").append("sum(montant) ").append(RERestitutionsSoldes.FIELD_FICTIF_SOLDE);

        // Compte annexe
        sql.append(" FROM ").append(_getCollection()).append(CACompteAnnexe.TABLE_CACPTAP).append(" ")
                .append(CACompteAnnexe.TABLE_CACPTAP);

        // Section
        sql.append(" INNER JOIN ").append(_getCollection()).append(CASection.TABLE_CASECTP).append(" ")
                .append(CASection.TABLE_CASECTP);
        sql.append(" ON (").append(CASection.TABLE_CASECTP).append(".").append(CASection.FIELD_IDCOMPTEANNEXE);
        sql.append("=").append(CACompteAnnexe.TABLE_CACPTAP).append(".").append(CACompteAnnexe.FIELD_IDCOMPTEANNEXE)
                .append(")");

        // Operation
        sql.append(" INNER JOIN ").append(_getCollection()).append(CAOperation.TABLE_CAOPERP).append(" ")
                .append(CAOperation.TABLE_CAOPERP);
        sql.append(" ON (").append(CAOperation.TABLE_CAOPERP).append(".").append(CAOperation.FIELD_IDSECTION);
        sql.append("=").append(CASection.TABLE_CASECTP).append(".").append(CASection.FIELD_IDSECTION).append(")");

        // journal
        sql.append(" INNER JOIN ").append(_getCollection()).append(CAJournal.TABLE_CAJOURP).append(" ")
                .append(CAJournal.TABLE_CAJOURP);
        sql.append(" ON (").append(CAJournal.TABLE_CAJOURP).append(".").append(CAJournal.FIELD_IDJOURNAL);
        sql.append("=").append(CAOperation.TABLE_CAOPERP).append(".").append(CAOperation.FIELD_IDJOURNAL).append(")");

        // Etape contentieux : left join webavsciam.fwcoup cet on (se.IDLASTETATAQUILA = cet.pcosid and cet.plaide =
        // 'F')
        sql.append(" LEFT JOIN ").append(_getCollection()).append("FWCOUP cet");
        sql.append(" ON (").append(CASection.TABLE_CASECTP).append(".").append(CASection.FIELD_IDLASTETATAQUILA);
        sql.append("=").append("cet.pcosid and cet.plaide='").append(getLangue()).append("')");

        // where idrole = 517038
        // and se.CATEGORIESECTION = 227026
        // and op.etat=205002
        // and jo.datevaleurcg <= 20111231
        // and op.idtypeoperation like 'E%'
        sql.append(" WHERE ").append("idrole=").append(getForRole());
        sql.append(" AND ").append(CASection.FIELD_CATEGORIESECTION).append("=")
                .append(APISection.ID_CATEGORIE_SECTION_DECISION_PCF_RESTITUTION);
        sql.append(" AND ").append(CAOperation.TABLE_CAOPERP).append(".").append(CAOperation.FIELD_ETAT).append("=")
                .append(APIOperation.ETAT_COMPTABILISE);
        sql.append(" AND ").append(CAJournal.FIELD_DATEVALEURCG).append("<=").append(getDateValeur());
        sql.append(" AND ").append(CAOperation.TABLE_CAOPERP).append(".").append(CAOperation.FIELD_IDTYPEOPERATION)
                .append(" LIKE 'E%'");

        // group by idexternerole,se.idexterne,description,se.DATESECTION,cet.pcolut
        sql.append(" GROUP BY ").append(CASection.TABLE_CASECTP).append(".").append(CASection.FIELD_IDSECTION);
        sql.append(", ").append(CACompteAnnexe.FIELD_IDEXTERNEROLE);
        sql.append(", ").append(CASection.FIELD_IDEXTERNE);
        sql.append(", ").append(CACompteAnnexe.FIELD_DESCRIPTION);
        sql.append(", ").append(CASection.FIELD_DATESECTION);
        sql.append(", ").append("cet.pcolut");

        // having sum(montant) <> 0
        sql.append(" HAVING SUM(montant) <> 0");

        // order by idexternerole,se.idexterne
        sql.append(" ORDER BY ").append(CACompteAnnexe.FIELD_IDEXTERNEROLE);
        sql.append(", ").append(CASection.FIELD_IDEXTERNE);

        return sql.toString();
    }

    @Override
    protected java.lang.String _getSqlCount(BStatement statement) {
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERestitutionsSoldes();
    }

    /**
     * @return the forDateValeur
     */
    public String getDateValeur() {
        return forDateValeur;
    }

    /**
     * @return the forRole
     */
    public String getForRole() {
        return forRole;
    }

    /**
     * @return the langue
     */
    public String getLangue() {
        return langue;
    }

    /**
     * @param forDateValeur
     *            the forDateValeur to set
     */
    public void setDateValeur(String forDateValeur) {
        this.forDateValeur = forDateValeur;
    }

    /**
     * @param forRole
     *            the forRole to set
     */
    public void setForRole(String forRole) {
        this.forRole = forRole;
    }

    /**
     * @param langue
     *            the langue to set
     */
    public void setLangue(String langue) {
        this.langue = langue;
    }

}
