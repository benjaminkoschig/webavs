/**
 *
 */
package globaz.helios.db.journaldebits;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.mapping.CAJournalDebit;

/**
 * @author sel
 * 
 */
public class CGExtractionOperationOsirisManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut = "";
    private String dateFin = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sqlFields = new StringBuffer("");

        sqlFields.append("SUBSTR(DIGITS(jo." + CAJournal.FIELD_DATEVALEURCG + "),1,6) ");
        sqlFields.append(CGExtractionOperationOsiris.FIELD_ANNEE);
        sqlFields.append(", ");
        sqlFields.append("cc.").append(CACompteCourant.FIELD_IDEXTERNE);
        sqlFields.append(" ");
        sqlFields.append(CGExtractionOperationOsiris.FIELD_COMPTECOURANTSRC);
        sqlFields.append(", ");
        sqlFields.append("ru.").append(CARubrique.FIELD_IDEXTERNE);
        sqlFields.append(" ");
        sqlFields.append(CGExtractionOperationOsiris.FIELD_RUBRIQUESRC);
        sqlFields.append(", ");
        sqlFields.append(CAJournalDebit.FIELD_COMPTECOURANTDEST);
        sqlFields.append(", ");
        sqlFields.append(CAJournalDebit.FIELD_CONTREPARTIEDEST);
        sqlFields.append(", ");
        sqlFields.append(CAJournalDebit.FIELD_IDMANDAT);
        sqlFields.append(", ");
        sqlFields.append("SUM(").append(CAOperation.FIELD_MONTANT).append(")");
        sqlFields.append(" ");
        sqlFields.append(CGExtractionOperationOsiris.FIELD_MONTANT);

        return sqlFields.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer("");

        sqlFrom.append(_getCollection()).append(CACompteCourant.TABLE_CACPTCP).append(" cc");
        sqlFrom.append(" INNER JOIN ");
        sqlFrom.append(_getCollection()).append(CAOperation.TABLE_CAOPERP).append(" op");
        sqlFrom.append(" ON ");
        sqlFrom.append("cc.").append(CACompteCourant.FIELD_IDCOMPTECOURANT);
        sqlFrom.append(" = ");
        sqlFrom.append("op.").append(CAOperation.FIELD_IDCOMPTECOURANT);

        sqlFrom.append(" INNER JOIN ");
        sqlFrom.append(_getCollection()).append(CARubrique.TABLE_CARUBRP).append(" ru");
        sqlFrom.append(" ON ");
        sqlFrom.append("ru.").append(CARubrique.FIELD_IDRUBRIQUE);
        sqlFrom.append(" = ");
        sqlFrom.append("op.").append(CAOperation.FIELD_IDCOMPTE);

        sqlFrom.append(" INNER JOIN ");
        sqlFrom.append(_getCollection()).append(CAJournal.TABLE_CAJOURP).append(" jo");
        sqlFrom.append(" ON ");
        sqlFrom.append("jo.").append(CAJournal.FIELD_IDJOURNAL);
        sqlFrom.append(" = ");
        sqlFrom.append("op.").append(CAOperation.FIELD_IDJOURNAL);

        sqlFrom.append(" LEFT JOIN ");
        sqlFrom.append(_getCollection()).append(CAJournalDebit.TABLE_NAME).append(" li");
        sqlFrom.append(" ON ");
        sqlFrom.append("(cc.").append(CACompteCourant.FIELD_IDEXTERNE);
        sqlFrom.append(" = ");
        sqlFrom.append("li.").append(CAJournalDebit.FIELD_COMPTECOURANTSRC);
        sqlFrom.append(" AND ");
        sqlFrom.append("ru.").append(CARubrique.FIELD_IDEXTERNE);
        sqlFrom.append(" = ");
        sqlFrom.append("li.").append(CAJournalDebit.FIELD_CONTREPARTIESRC).append(")");

        return sqlFrom.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        StringBuffer sqlOrder = new StringBuffer("");

        sqlOrder.append(CGExtractionOperationOsiris.FIELD_ANNEE);
        sqlOrder.append(", ");
        sqlOrder.append(CGExtractionOperationOsiris.FIELD_COMPTECOURANTSRC);
        sqlOrder.append(", ");
        sqlOrder.append(CGExtractionOperationOsiris.FIELD_RUBRIQUESRC);

        return sqlOrder.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isBlank(getDateDebut()) && !JadeStringUtil.isBlank(getDateFin())) {
            sqlWhere.append("jo.").append(CAJournal.FIELD_DATEVALEURCG);
            sqlWhere.append(" BETWEEN ");
            sqlWhere.append(getDateDebut());
            sqlWhere.append(" AND ");
            sqlWhere.append(getDateFin());

            sqlWhere.append(" AND ");
        }
        sqlWhere.append("cc.").append(CACompteCourant.FIELD_JOURNALDESDEBIT);
        sqlWhere.append("='1'");
        sqlWhere.append(" AND ");
        sqlWhere.append("op.").append(CAOperation.FIELD_ETAT);
        sqlWhere.append("=");
        sqlWhere.append(APIOperation.ETAT_COMPTABILISE);

        sqlWhere.append(" GROUP BY ");
        sqlWhere.append("SUBSTR(DIGITS(jo." + CAJournal.FIELD_DATEVALEURCG + "),1,6)");
        sqlWhere.append(", ");
        sqlWhere.append("cc.").append(CACompteCourant.FIELD_IDEXTERNE);
        sqlWhere.append(", ");
        sqlWhere.append("ru.").append(CARubrique.FIELD_IDEXTERNE);
        sqlWhere.append(", ");
        sqlWhere.append(CAJournalDebit.FIELD_COMPTECOURANTDEST);
        sqlWhere.append(", ");
        sqlWhere.append(CAJournalDebit.FIELD_CONTREPARTIEDEST);
        sqlWhere.append(", ");
        sqlWhere.append(CAJournalDebit.FIELD_IDMANDAT);

        return sqlWhere.toString();
    }

    // select
    // substr(digits(jo.datevaleurcg),1,6),cc.idexterne,ru.idexterne,comptecourantdest,contrepartiedest,idmandat,sum(montant)
    // from meroweb.cacptcp cc
    // inner join meroweb.caoperp op on (cc.idcomptecourant=op.idcomptecourant)
    // inner join meroweb.carubrp ru on (op.idcompte=ru.idrubrique)
    // inner join meroweb.cajourp jo on (op.idjournal=jo.idjournal)
    // left join meroweb.cglinkp li on (cc.idexterne=li.comptecourantsrc and ru.idexterne=contrepartiesrc)
    // where jo.datevaleurcg between 20110301 and 20110331
    // and cc.journaldebit=’1’
    // and op.etat=205002
    // group by substr(digits(jo.datevaleurcg),1,6),cc.idexterne, ru.idexterne, li.comptecourantdest,
    // li.contrepartiedest,li.idmandat
    // order by substr(digits(jo.datevaleurcg),1,6),cc.idexterne, ru.idexterne

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CGExtractionOperationOsiris();
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

}
