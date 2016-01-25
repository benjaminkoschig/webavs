package globaz.osiris.db.interet.analytique;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.interets.CADetailInteretMoratoire;
import globaz.osiris.db.interets.CAGenreInteret;
import globaz.osiris.db.interets.CAInteretMoratoire;

/**
 * 
 //select sum(b.montantinteret) as MONTANTINTERET, a.idsection from webavsciam.caimdcp a, webavsciam.caimdep b,
 * WEBAVSCIAM.CAJOURP f where //a.idinteretmoratoire = b.idinteretmoratoire AND a.idsection > 0 and //a.MOTIFCALCUL =
 * 229002 and (a.idgenreinteret = 228001 or //(a.idgenreinteret = 228004 and a.idsection <> a.idsectionfacture)) //AND
 * a.IDJOURNALCALCUL = f.IDJOURNAL AND f.DATEVALEURCG >= 20090101 AND f.DATEVALEURCG <= 20090131 //group by
 * a.idinteretmoratoire, a.idsection
 * 
 * @author DDA
 * 
 */
public class CAInteretTardifEt25PourCentAnalytiqueManager extends CAInteretAnalytiqueManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see CAInteretAnalytiqueManager#getInteretSubTable(BStatement, StringBuffer)
     */
    @Override
    protected void getInteretSubTable(BStatement statement, StringBuffer sql) {
        sql.append("SELECT a.").append(CAInteretMoratoire.FIELD_IDSECTION).append(", sum(b.")
                .append(CADetailInteretMoratoire.FIELD_MONTANTINTERET).append(") as ")
                .append(CAInteretAnalytique.FIELD_MONTANTINTERET);
        sql.append(" FROM ").append(_getCollection()).append(CAInteretMoratoire.TABLE_CAIMDCP).append(" a, ")
                .append(_getCollection()).append(CADetailInteretMoratoire.TABLE_CAIMDEP).append(" b, ")
                .append(_getCollection()).append(CAJournal.TABLE_CAJOURP).append(" e ");
        sql.append(" WHERE ");
        sql.append("a.").append(CAInteretMoratoire.FIELD_IDINTERETMORATOIRE).append(" = b.")
                .append(CADetailInteretMoratoire.FIELD_IDINTERETMORATOIRE);
        sql.append(" AND ");
        sql.append("a.").append(CAInteretMoratoire.FIELD_IDSECTION).append(" > 0 AND (");
        sql.append("a.").append(CAInteretMoratoire.FIELD_IDGENREINTERET).append(" = ")
                .append(CAGenreInteret.CS_TYPE_TARDIF);
        sql.append(" OR (");
        sql.append("a.").append(CAInteretMoratoire.FIELD_IDGENREINTERET).append(" = ")
                .append(CAGenreInteret.CS_TYPE_COTISATIONS_PERSONNELLES);
        sql.append(" AND ");
        sql.append("a.").append(CAInteretMoratoire.FIELD_IDSECTION).append(" <> ").append("a.")
                .append(CAInteretMoratoire.FIELD_IDSECTIONFACTURE);
        sql.append(" )) ");
        sql.append(" AND ");
        sql.append("a.").append(CAInteretMoratoire.FIELD_MOTIFCALCUL).append(" = ")
                .append(CAInteretMoratoire.CS_SOUMIS);
        sql.append(" AND ");
        sql.append("a.").append(CAInteretMoratoire.FIELD_IDJOURNALCALCUL).append(" = e.")
                .append(CAJournal.FIELD_IDJOURNAL);

        if (!JadeStringUtil.isBlank(getForDateDebut())) {
            sql.append(" AND ");
            sql.append("e.").append(CAJournal.FIELD_DATEVALEURCG).append(" >= ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebut()));
        }

        if (!JadeStringUtil.isBlank(getForDateFin())) {
            sql.append(" AND ");
            sql.append("e.").append(CAJournal.FIELD_DATEVALEURCG).append(" <= ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateFin()));
        }

        sql.append(" GROUP BY a.").append(CAOperation.FIELD_IDSECTION).append(", a.")
                .append(CAInteretMoratoire.FIELD_IDINTERETMORATOIRE);
    }
}
