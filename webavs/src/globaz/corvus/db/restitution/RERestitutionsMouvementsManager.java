/**
 * 
 */
package globaz.corvus.db.restitution;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CASection;

/**
 * @author sel
 * 
 */
public class RERestitutionsMouvementsManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateA = null;
    private String dateDe = null;
    private String forIdExterneRubrique = null;
    private String forRole = null;
    private String langue = null;

    @Override
    protected String _getSql(BStatement statement) {
        if (JadeStringUtil.isBlank(forRole)) {
            throw new NullPointerException("must have a role for this request");
        }
        if (JadeStringUtil.isBlank(dateDe)) {
            throw new NullPointerException("must have a date 'de' for this request");
        }
        if (JadeStringUtil.isBlank(dateA)) {
            throw new NullPointerException("must have a date 'à' for this request");
        }

        // select idexternerole,description,se.idexterne,ru.idexterne,tr.LIBELLE,op.date,op.libelle,op.montant
        // FROM webavsciam.cacptap ca
        // inner join webavsciam.casectp se on (ca.IDCOMPTEANNEXE = se.IDCOMPTEANNEXE)
        // inner join webavsciam.caoperp op on (se.idsection=op.idsection)
        // inner join webavsciam.cajourp jo on (op.idjournal=jo.idjournal)
        // inner join webavsciam.carubrp ru on (op.idcompte=ru.idrubrique)
        // inner join webavsciam.pmtradp tr on (ru.IDTRADUCTION = tr.IDTRADUCTION and tr.CODEISOLANGUE = 'FR')
        // where idrole = 517038
        // and op.etat=205002
        // and se.categoriesection = 227026
        // and ru.NATURERUBRIQUE not in (200004,200007)
        // and jo.datevaleurcg between 20110101 and 20111231
        // order by idexternerole,se.idexterne

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(CACompteAnnexe.TABLE_CACPTAP).append(".")
                .append(CACompteAnnexe.FIELD_IDEXTERNEROLE);
        sql.append(", ").append(CACompteAnnexe.TABLE_CACPTAP).append(".").append(CACompteAnnexe.FIELD_DESCRIPTION);
        sql.append(", ").append(CASection.TABLE_CASECTP).append(".").append(CASection.FIELD_IDEXTERNE);
        sql.append(", ").append(CARubrique.TABLE_CARUBRP).append(".").append(CARubrique.FIELD_IDEXTERNE).append(" ")
                .append(RERestitutionsMouvements.FIELD_NUM_RUBRIQUE);
        sql.append(", ").append("tr.libelle ").append(RERestitutionsMouvements.FIELD_DESCRIPTION);
        sql.append(", ").append(CAOperation.TABLE_CAOPERP).append(".").append(CAOperation.FIELD_DATE);
        sql.append(", ").append(CAOperation.TABLE_CAOPERP).append(".").append(CAOperation.FIELD_LIBELLE);
        sql.append(", ").append(CAOperation.FIELD_MONTANT);

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

        // rubrique
        sql.append(" INNER JOIN ").append(_getCollection()).append(CARubrique.TABLE_CARUBRP).append(" ")
                .append(CARubrique.TABLE_CARUBRP);
        sql.append(" ON (").append(CARubrique.TABLE_CARUBRP).append(".").append(CARubrique.FIELD_IDRUBRIQUE);
        sql.append("=").append(CAOperation.TABLE_CAOPERP).append(".").append(CAOperation.FIELD_IDCOMPTE).append(")");

        // traduction
        sql.append(" INNER JOIN ").append(_getCollection()).append("PMTRADP tr");
        sql.append(" ON (").append("tr.idtraduction=");
        sql.append(CARubrique.TABLE_CARUBRP).append(".").append(CARubrique.FIELD_IDTRADUCTION);
        sql.append(" AND tr.CODEISOLANGUE = '").append(langue.toUpperCase()).append("'").append(")");

        // where idrole = 517038
        // and op.etat=205002
        // and se.categoriesection = 227026
        // and ru.NATURERUBRIQUE not in (200004,200007)
        // and jo.datevaleurcg between 20110101 and 20111231
        sql.append(" WHERE ").append("idrole=").append(getForRole());
        sql.append(" AND ").append(CASection.FIELD_CATEGORIESECTION).append("=")
                .append(APISection.ID_CATEGORIE_SECTION_DECISION_PCF_RESTITUTION);
        sql.append(" AND ").append(CAOperation.TABLE_CAOPERP).append(".").append(CAOperation.FIELD_ETAT).append("=")
                .append(APIOperation.ETAT_COMPTABILISE);
        sql.append(" AND ").append(CAJournal.FIELD_DATEVALEURCG).append(" BETWEEN ").append(getDateDe())
                .append(" AND ").append(getDateA());

        if (!JadeStringUtil.isBlank(getForIdExterneRubrique())) {
            sql.append(" AND ").append(CARubrique.TABLE_CARUBRP).append(".").append(CARubrique.FIELD_IDEXTERNE)
                    .append("='").append(getForIdExterneRubrique()).append("'");
        }

        // order by idexternerole,se.idexterne
        sql.append(" ORDER BY ").append(CACompteAnnexe.FIELD_IDEXTERNEROLE);
        sql.append(", ").append(CASection.TABLE_CASECTP).append(".").append(CASection.FIELD_IDEXTERNE);

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
        return new RERestitutionsMouvements();
    }

    /**
     * @return the dateA
     */
    public String getDateA() {
        return dateA;
    }

    /**
     * @return the dateDe
     */
    public String getDateDe() {
        return dateDe;
    }

    /**
     * @return the forIdRubrique
     */
    public String getForIdExterneRubrique() {
        return forIdExterneRubrique;
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
     * @param dateA
     *            the dateA to set
     */
    public void setDateA(String dateA) {
        this.dateA = dateA;
    }

    /**
     * @param dateDe
     *            the dateDe to set
     */
    public void setDateDe(String dateDe) {
        this.dateDe = dateDe;
    }

    /**
     * @param forIdRubrique
     *            the forIdRubrique to set
     */
    public void setForIdExterneRubrique(String forIdRubrique) {
        forIdExterneRubrique = forIdRubrique;
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
