package globaz.aquila.db.access.poursuite;

import globaz.aquila.common.COBManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.extrait.CAExtraitCompte;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SEL <br>
 *         Date : 15 avr. 08
 */
public class COExtraitCompteManager extends COBManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String IDRUBRIQUE = " IDRUBRIQUE";
    private static final String SECTIONDATE = " SECTIONDATE";

    private boolean affichePaiementOP = false;
    private List<String> forEtatIn = new ArrayList<String>();
    private String forIdSection = "";
    private String forNotIdJournal = "";
    private String forNotIdTypeOperation = "";

    private String forSectionForPmtComp = "";
    private String forSectionForSoldeInitiale = "";
    private String fromDate;
    private String likeIdTypeOperation = "";
    private boolean useDate = false;

    public Boolean getUseDate() {
        return useDate;
    }

    public void setUseDate(Boolean useDate) {
        this.useDate = useDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {
        StringBuffer sqlBuffer = new StringBuffer("");

        if (!JadeStringUtil.isBlank(getForSectionForSoldeInitiale())) {
            getSqlSoldeInitial(statement, sqlBuffer);
        }
        if (!JadeStringUtil.isBlank(getForSectionForPmtComp())) {
            // -- Paiement et compensation groupé par journal et date
            sqlBuffer.append(COBManager.SELECT);
            sqlBuffer.append("0 ").append(CAOperation.FIELD_IDJOURNAL).append(", a.").append(CAOperation.FIELD_DATE)
                    .append(", a.").append(CAOperation.FIELD_MONTANT).append(", a.")
                    .append(CAOperation.FIELD_IDSECTION).append(", a.").append(CAOperation.FIELD_IDTYPEOPERATION)
                    .append(", a.").append(CASection.FIELD_IDEXTERNE).append(", a.")
                    .append(CASection.FIELD_IDTYPESECTION).append(", a.").append(COExtraitCompteManager.SECTIONDATE)
                    .append(", a.").append(CARubrique.FIELD_LIBELLEEXTRAIT).append(", a.")
                    .append(CAOperation.FIELD_PROVENANCEPMT).append(", a.").append(CAOperation.FIELD_IDCOMPTE)
                    .append(COExtraitCompteManager.IDRUBRIQUE);
            sqlBuffer.append(COBManager.FROM).append("(");

            getSqlOperationAvecIdRubrique(statement, sqlBuffer);

            sqlBuffer.append(COBManager.UNION_ALL);

            getSqlOperationSansIdRubrique(statement, sqlBuffer);

            sqlBuffer.append(COBManager.UNION_ALL);

            getSqlOperationTypeE(statement, sqlBuffer);

            sqlBuffer.append(") a ");

            if (getUseDate() == true) {
                sqlBuffer.append(" WHERE a.").append(CAOperation.FIELD_DATE).append(" >= ").append(getFromDateAMJ());
                sqlBuffer.append(" ");
            }
            sqlBuffer.append(COBManager.ORDER_BY).append("a.").append(CAOperation.FIELD_DATE);
        }

        return sqlBuffer.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAExtraitCompte();
    }

    /**
     * @param statement
     * @param sqlBuffer
     */
    private void getConditionTypeOperation(BStatement statement, StringBuffer sqlBuffer) {
        sqlBuffer.append(COBManager.AND);
        sqlBuffer.append("((");
        sqlBuffer.append(CAOperation.FIELD_IDTYPEOPERATION);
        sqlBuffer.append(" <> ");
        sqlBuffer.append(this._dbWriteString(statement.getTransaction(), APIOperation.CAECRITURE));
        sqlBuffer.append(COBManager.AND);
        sqlBuffer.append(CAOperation.FIELD_IDTYPEOPERATION);
        sqlBuffer.append(COBManager.LIKE);
        sqlBuffer.append(this._dbWriteString(statement.getTransaction(), APIOperation.CAECRITURE + "%"));
        sqlBuffer.append(")");
        sqlBuffer.append(COBManager.OR);
        sqlBuffer.append("(");
        sqlBuffer.append("idtypeoperation <> ");
        sqlBuffer.append(this._dbWriteString(statement.getTransaction(), APIOperation.CAAUXILIAIRE));
        sqlBuffer.append(COBManager.AND);
        sqlBuffer.append(CAOperation.FIELD_IDTYPEOPERATION);
        sqlBuffer.append(COBManager.LIKE);
        sqlBuffer.append(this._dbWriteString(statement.getTransaction(), APIOperation.CAAUXILIAIRE + "%"));
        sqlBuffer.append("))");
    }

    /**
     * @return the forEtatIn
     */
    public List<String> getForEtatIn() {
        return forEtatIn;
    }

    /**
     * @return the forIdSection
     */
    public String getForIdSection() {
        return forIdSection;
    }

    /**
     * @return the forNotIdJournal
     */
    public String getForNotIdJournal() {
        return forNotIdJournal;
    }

    /**
     * @return the forNotIdTypeOperation
     */
    public String getForNotIdTypeOperation() {
        return forNotIdTypeOperation;
    }

    /**
     * @return the forPmtComp
     */
    public String getForSectionForPmtComp() {
        return forSectionForPmtComp;
    }

    /**
     * @return the forSoldeInitiale
     */
    public String getForSectionForSoldeInitiale() {
        return forSectionForSoldeInitiale;
    }

    /**
     * <pre>
     * From CAOPERP o
     * Inner Join CASECTP s On s.idSection = o.idsection
     * Inner Join CARUBRP r On r.idRubrique = o.idCompte
     * </pre>
     * 
     * @param sqlBuffer
     */
    private void getFrom(StringBuffer sqlBuffer) {
        sqlBuffer.append(COBManager.FROM).append(_getCollection()).append(CAOperation.TABLE_CAOPERP).append(" o");
        sqlBuffer.append(COBManager.INNER_JOIN).append(_getCollection()).append(CASection.TABLE_CASECTP).append(" s")
                .append(COBManager.ON).append("s.").append(CASection.FIELD_IDSECTION).append(" = o.")
                .append(CAOperation.FIELD_IDSECTION);

        sqlBuffer.append(COBManager.INNER_JOIN).append(_getCollection()).append(CARubrique.TABLE_CARUBRP).append(" r")
                .append(COBManager.ON).append("r.").append(CARubrique.FIELD_IDRUBRIQUE).append(" = o.")
                .append(CAOperation.FIELD_IDCOMPTE);

        sqlBuffer.append(COBManager.INNER_JOIN).append(_getCollection()).append(CAJournal.TABLE_CAJOURP).append(" j")
                .append(COBManager.ON).append("j.").append(CAJournal.FIELD_IDJOURNAL).append("=").append("o.")
                .append(CAOperation.FIELD_IDJOURNAL);
    }

    /**
     * @return the fromDate
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * @return
     */
    public String getFromDateAMJ() {
        JADate myDate = null;
        try {
            myDate = new JADate(getFromDate());
        } catch (JAException e) {
        }
        return myDate.toAMJ().toString();
    }

    /**
     * @return
     */
    public String getLikeIdTypeOperation() {
        return likeIdTypeOperation;
    }

    /**
     * @return
     */
    private String getSqlConditionNotCumul() {
        StringBuilder sql = new StringBuilder();
        sql.append(COBManager.AND).append("((j.").append(CAJournal.FIELD_IDJOURNAL).append("<> 1")
                .append(COBManager.AND).append("j.").append(CAJournal.FIELD_DATEVALEURCG).append(" > s.")
                .append(CASection.FIELD_DATESECTION).append(")").append(COBManager.OR).append("(o.")
                .append(CAOperation.FIELD_IDJOURNAL).append(" = 1").append(COBManager.AND).append("o.")
                .append(CAOperation.FIELD_DATE).append(" > s.").append(CASection.FIELD_DATESECTION).append("))");
        return sql.toString();
    }

    /**
     * Retourne toutes les opérations de type E% ou A% mais différente de E ou A groupé par idjournal et date
     * 
     * @param sqlBuffer
     */
    private void getSqlOperationAvecIdRubrique(BStatement statement, StringBuffer sqlBuffer) {
        // Retourne toutes les opérations de type E% ou A% mais différente de E
        // ou A groupé par idjournal et date
        sqlBuffer.append(COBManager.SELECT);
        sqlBuffer.append("o.").append(CAOperation.FIELD_IDJOURNAL).append(", o.").append(CAOperation.FIELD_DATE)
                .append(", SUM(o.").append(CAOperation.FIELD_MONTANT).append(")").append(CAOperation.FIELD_MONTANT)
                .append(", o.").append(CAOperation.FIELD_IDSECTION).append(", o.")
                .append(CAOperation.FIELD_IDTYPEOPERATION).append(", s.").append(CASection.FIELD_IDEXTERNE)
                .append(", s.").append(CASection.FIELD_IDTYPESECTION).append(", s.")
                .append(CASection.FIELD_DATESECTION).append(COExtraitCompteManager.SECTIONDATE).append(", r.")
                .append(CARubrique.FIELD_LIBELLEEXTRAIT).append(", o.").append(CAOperation.FIELD_PROVENANCEPMT)
                .append(", o.").append(CAOperation.FIELD_IDCOMPTE);

        getFrom(sqlBuffer);

        sqlBuffer.append(COBManager.WHERE);
        sqlBuffer.append("o.").append(CAOperation.FIELD_IDSECTION).append(" = ").append(getForSectionForPmtComp());
        if (!isAffichePaiementOP()) {
            // Paiement ne provient pas de l'OP
            sqlBuffer.append(COBManager.AND).append("o.").append(CAOperation.FIELD_PROVENANCEPMT).append(" = 0");
        }

        sqlBuffer.append(COBManager.AND).append("o.").append(CAOperation.FIELD_ETAT).append(" = ")
                .append(APIOperation.ETAT_COMPTABILISE);
        getConditionTypeOperation(statement, sqlBuffer);

        sqlBuffer.append(getSqlConditionNotCumul());

        sqlBuffer.append(COBManager.GROUP_BY).append("o.").append(CAOperation.FIELD_IDJOURNAL).append(", o.")
                .append(CAOperation.FIELD_DATE).append(", o.").append(CAOperation.FIELD_IDSECTION).append(", o.")
                .append(CAOperation.FIELD_IDTYPEOPERATION).append(", s.").append(CASection.FIELD_IDEXTERNE)
                .append(", s.").append(CASection.FIELD_IDTYPESECTION).append(", s.")
                .append(CASection.FIELD_DATESECTION).append(", r.").append(CARubrique.FIELD_LIBELLEEXTRAIT)
                .append(", o.").append(CAOperation.FIELD_PROVENANCEPMT).append(", o.")
                .append(CAOperation.FIELD_IDCOMPTE);
    }

    /**
     * Retourne toutes les opérations de type E% ou A% mais différente de E ou A où IDCOMPTE = 0 groupé par idjournal et
     * date
     * 
     * @param sqlBuffer
     */
    private void getSqlOperationSansIdRubrique(BStatement statement, StringBuffer sqlBuffer) {
        // Retourne toutes les opérations de type E% ou A% mais différente de E
        // ou A où IDCOMPTE = 0 groupé par idjournal et date
        sqlBuffer.append(COBManager.SELECT);
        sqlBuffer.append("o.").append(CAOperation.FIELD_IDJOURNAL).append(", o.").append(CAOperation.FIELD_DATE)
                .append(", SUM(o.").append(CAOperation.FIELD_MONTANT).append(") ").append(CAOperation.FIELD_MONTANT)
                .append(", o.").append(CAOperation.FIELD_IDSECTION).append(", o.")
                .append(CAOperation.FIELD_IDTYPEOPERATION).append(", s.").append(CASection.FIELD_IDEXTERNE)
                .append(", s.").append(CASection.FIELD_IDTYPESECTION).append(", s.")
                .append(CASection.FIELD_DATESECTION).append(COExtraitCompteManager.SECTIONDATE).append(", 0")
                .append(COBManager.AS_FIELD).append(CARubrique.FIELD_LIBELLEEXTRAIT).append(", o.")
                .append(CAOperation.FIELD_PROVENANCEPMT).append(", 0").append(COBManager.AS_FIELD)
                .append(CAOperation.FIELD_IDCOMPTE);

        getFrom(sqlBuffer);

        sqlBuffer.append(COBManager.WHERE);
        sqlBuffer.append("o.").append(CAOperation.FIELD_IDSECTION).append(" = ").append(getForSectionForPmtComp());
        if (!isAffichePaiementOP()) {
            // Paiement ne provient pas de l'OP
            sqlBuffer.append(COBManager.AND).append("o.").append(CAOperation.FIELD_PROVENANCEPMT).append(" = 0");
        }

        sqlBuffer.append(COBManager.AND).append("o.").append(CAOperation.FIELD_ETAT).append(" = ")
                .append(APIOperation.ETAT_COMPTABILISE);
        getConditionTypeOperation(statement, sqlBuffer);
        sqlBuffer.append(COBManager.AND).append("(o.").append(CAOperation.FIELD_IDCOMPTE).append(" IS NULL")
                .append(COBManager.OR).append("o.").append(CAOperation.FIELD_IDCOMPTE).append(" = 0) ");

        sqlBuffer.append(getSqlConditionNotCumul());

        sqlBuffer.append(COBManager.GROUP_BY).append("o.").append(CAOperation.FIELD_IDJOURNAL).append(", o.")
                .append(CAOperation.FIELD_DATE).append(", o.").append(CAOperation.FIELD_IDSECTION).append(", o.")
                .append(CAOperation.FIELD_IDTYPEOPERATION).append(", s.").append(CASection.FIELD_IDEXTERNE)
                .append(", s.").append(CASection.FIELD_IDTYPESECTION).append(", s.")
                .append(CASection.FIELD_DATESECTION).append(", o.").append(CAOperation.FIELD_PROVENANCEPMT)
                .append(", o.").append(CAOperation.FIELD_IDCOMPTE);
    }

    /**
     * Retourne toutes les opérations de type E et A
     * 
     * @param statement
     * @param sqlBuffer
     */
    private void getSqlOperationTypeE(BStatement statement, StringBuffer sqlBuffer) {
        // Retourne toutes les opérations de type E
        sqlBuffer.append(COBManager.SELECT);
        sqlBuffer.append("o.").append(CAOperation.FIELD_IDJOURNAL).append(", o.").append(CAOperation.FIELD_DATE)
                .append(", SUM(o.").append(CAOperation.FIELD_MONTANT).append(") ").append(CAOperation.FIELD_MONTANT)
                .append(", o.").append(CAOperation.FIELD_IDSECTION).append(", o.")
                .append(CAOperation.FIELD_IDTYPEOPERATION).append(", s.").append(CASection.FIELD_IDEXTERNE)
                .append(", s.").append(CASection.FIELD_IDTYPESECTION).append(", s.")
                .append(CASection.FIELD_DATESECTION).append(COExtraitCompteManager.SECTIONDATE).append(", r.")
                .append(CARubrique.FIELD_LIBELLEEXTRAIT).append(", o.").append(CAOperation.FIELD_PROVENANCEPMT)
                .append(", o.").append(CAOperation.FIELD_IDCOMPTE);

        getFrom(sqlBuffer);

        sqlBuffer.append(COBManager.WHERE);
        sqlBuffer.append(" o.").append(CAOperation.FIELD_IDSECTION).append("  = ").append(getForSectionForPmtComp());
        sqlBuffer.append(COBManager.AND).append("o.").append(CAOperation.FIELD_ETAT).append("  = ")
                .append(APIOperation.ETAT_COMPTABILISE);
        sqlBuffer.append(COBManager.AND).append("o.").append(CAOperation.FIELD_IDTYPEOPERATION).append(" IN (")
                .append(this._dbWriteString(statement.getTransaction(), APIOperation.CAECRITURE)).append(",")
                .append(this._dbWriteString(statement.getTransaction(), APIOperation.CAAUXILIAIRE)).append(")");

        sqlBuffer.append(getSqlConditionNotCumul());

        sqlBuffer.append(COBManager.GROUP_BY).append("o.").append(CAOperation.FIELD_IDJOURNAL).append(", o.")
                .append(CAOperation.FIELD_DATE).append(", o.").append(CAOperation.FIELD_IDSECTION).append(", o.")
                .append(CAOperation.FIELD_IDTYPEOPERATION).append(", s.").append(CASection.FIELD_IDEXTERNE)
                .append(", s.").append(CASection.FIELD_IDTYPESECTION).append(", s.")
                .append(CASection.FIELD_DATESECTION).append(", r.").append(CARubrique.FIELD_LIBELLEEXTRAIT)
                .append(", o.").append(CAOperation.FIELD_PROVENANCEPMT).append(", o.")
                .append(CAOperation.FIELD_IDCOMPTE);
    }

    /**
     * @param sqlBuffer
     */
    private void getSqlSoldeInitial(BStatement statement, StringBuffer sqlBuffer) {
        sqlBuffer.append(COBManager.SELECT);
        sqlBuffer.append("s.").append(CASection.FIELD_IDJOURNAL).append(", s.").append(CASection.FIELD_DATESECTION)
                .append(" DATE").append(", sum(").append(CAOperation.FIELD_MONTANT).append(") ")
                .append(CAOperation.FIELD_MONTANT).append(", 0 ").append(CAOperation.FIELD_IDSECTION).append(", '' ")
                .append(CAOperation.FIELD_IDTYPEOPERATION).append(", '' ").append(CASection.FIELD_IDEXTERNE)
                .append(", 0 ").append(CASection.FIELD_IDTYPESECTION).append(", '0' ")
                .append(COExtraitCompteManager.SECTIONDATE).append(", '' ").append(CARubrique.FIELD_LIBELLEEXTRAIT)
                .append(", 0 ").append(CAOperation.FIELD_PROVENANCEPMT).append(", 0 ")
                .append(COExtraitCompteManager.IDRUBRIQUE);

        sqlBuffer.append(COBManager.FROM).append(_getCollection()).append(CAOperation.TABLE_CAOPERP).append(" o");

        sqlBuffer.append(COBManager.INNER_JOIN).append(_getCollection()).append(CAJournal.TABLE_CAJOURP).append(" j")
                .append(COBManager.ON).append("j.").append(CAJournal.FIELD_IDJOURNAL).append("=").append("o.")
                .append(CAOperation.FIELD_IDJOURNAL);

        sqlBuffer.append(COBManager.INNER_JOIN).append(_getCollection()).append(CASection.TABLE_CASECTP).append(" s")
                .append(COBManager.ON).append("s.").append(CASection.FIELD_IDSECTION).append("=").append("o.")
                .append(CASection.FIELD_IDSECTION);

        sqlBuffer.append(COBManager.WHERE).append("o.").append(CAOperation.FIELD_IDSECTION).append(" = ")
                .append(getForSectionForSoldeInitiale());
        sqlBuffer.append(COBManager.AND).append("o.").append(CAOperation.FIELD_ETAT).append(" = ")
                .append(APIOperation.ETAT_COMPTABILISE);

        sqlBuffer.append(COBManager.AND).append("((j.").append(CAJournal.FIELD_IDJOURNAL).append("<> 1 ")
                .append(COBManager.AND).append("j.").append(CAJournal.FIELD_DATEVALEURCG).append("<=").append("s.")
                .append(CASection.FIELD_DATESECTION).append(")").append(COBManager.OR).append("(").append("j.")
                .append(CAJournal.FIELD_IDJOURNAL).append(" = 1").append(COBManager.AND).append("o.")
                .append(CAOperation.FIELD_DATE).append("<=").append("s.").append(CASection.FIELD_DATESECTION)
                .append("))");

        // BZ 9378 - Incorporé toutes les types d'écriture et auxiliaires sauf les opérations (OR, OV, ...).
        sqlBuffer.append(COBManager.AND).append("(").append(CAOperation.FIELD_IDTYPEOPERATION).append(COBManager.LIKE)
                .append(this._dbWriteString(statement.getTransaction(), APIOperation.CAECRITURE + "%"));
        sqlBuffer.append(COBManager.OR).append(CAOperation.FIELD_IDTYPEOPERATION).append(COBManager.LIKE)
                .append(this._dbWriteString(statement.getTransaction(), APIOperation.CAAUXILIAIRE + "%")).append(")");

        sqlBuffer.append(COBManager.GROUP_BY).append("o.").append(CAOperation.FIELD_IDSECTION).append(",").append("s.")
                .append(CASection.FIELD_DATESECTION).append(", s.").append(CASection.FIELD_IDJOURNAL);
    }

    /**
     * @return the affichePaiementOP
     */
    public boolean isAffichePaiementOP() {
        return affichePaiementOP;
    }

    /**
     * @param affichePaiementOP the affichePaiementOP to set
     */
    public void setAffichePaiementOP(boolean affichePaiementOP) {
        this.affichePaiementOP = affichePaiementOP;
    }

    /**
     * @param forEtatIn the forEtatIn to set
     */
    public void setForEtatIn(List<String> forEtatIn) {
        this.forEtatIn = forEtatIn;
    }

    /**
     * @param forIdSection the forIdSection to set
     */
    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

    /**
     * @param forNotIdJournal the forNotIdJournal to set
     */
    public void setForNotIdJournal(String forNotIdJournal) {
        this.forNotIdJournal = forNotIdJournal;
    }

    /**
     * @param forNotIdTypeOperation the forNotIdTypeOperation to set
     */
    public void setForNotIdTypeOperation(String forNotIdTypeOperation) {
        this.forNotIdTypeOperation = forNotIdTypeOperation;
    }

    /**
     * @param forPmtComp the forPmtComp to set
     */
    public void setForSectionForPmtComp(String forPmtComp) {
        forSectionForPmtComp = forPmtComp;
    }

    /**
     * @param forSoldeInitiale the forSoldeInitiale to set
     */
    public void setForSectionForSoldeInitiale(String forSoldeInitiale) {
        forSectionForSoldeInitiale = forSoldeInitiale;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @param likeIdTypeOperation the likeIdTypeOperation to set
     */
    public void setLikeIdTypeOperation(String likeIdTypeOperation) {
        this.likeIdTypeOperation = likeIdTypeOperation;
    }

}
