package globaz.osiris.db.interet.analytique;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.interets.CAGenreInteret;
import globaz.osiris.db.interets.CAInteretMoratoire;

/**
 * 
 * //SELECT a.IDSECTION, a.MONTANT as MONTANTINTERET FROM WEBAVSCIAM.CAOPERP a, WEBAVSCIAM.CAIMGIP b, WEBAVSCIAM.CAJOURP
 * f //WHERE a.IDCOMPTE = b.IDRUBRIQUE AND a.ETAT IN (205002, 205004) AND b.IDTYPEINTERET <> 228005 //AND a.IDJOURNAL =
 * f.IDJOURNAL AND f.DATEVALEURCG >= 20090101 AND f.DATEVALEURCG <= 20090131 //AND a.IDSECTION NOT IN (select idsection
 * from webavsciam.caimdcp where motifcalcul = 229002 and idsection > 0 group by idsection) //GROUP BY a.IDSECTION,
 * a.MONTANT
 * 
 * 
 * @author DDA
 * 
 */
public class CAInteretManuelAnalytiqueManager extends CAInteretAnalytiqueManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see CAInteretAnalytiqueManager#getInteretSubTable(BStatement, StringBuffer)
     */
    @Override
    protected void getInteretSubTable(BStatement statement, StringBuffer sql) {
        sql.append("SELECT a.").append(CAOperation.FIELD_IDSECTION).append(", a.").append(CAOperation.FIELD_MONTANT)
                .append(" as ").append(CAInteretAnalytique.FIELD_MONTANTINTERET);
        sql.append(" FROM ").append(_getCollection()).append(CAOperation.TABLE_CAOPERP).append(" a, ")
                .append(_getCollection()).append(CAGenreInteret.TABLE_CAIMGIP).append(" b, ").append(_getCollection())
                .append(CAJournal.TABLE_CAJOURP).append(" e ");
        sql.append(" WHERE ");
        sql.append("a.").append(CAOperation.FIELD_IDCOMPTE).append(" = b.").append(CAGenreInteret.FIELD_IDRUBRIQUE);
        sql.append(" AND ");
        sql.append("a.").append(CAOperation.FIELD_ETAT).append(" IN (").append(APIOperation.ETAT_COMPTABILISE)
                .append(", ").append(APIOperation.ETAT_PROVISOIRE).append(")");
        sql.append(" AND ");
        // TODO dda : delete for new version
        sql.append("b.").append(CAGenreInteret.FIELD_IDTYPEINTERET).append(" <> ")
                .append(CAGenreInteret.CS_TYPE_REMUNERATOIRES);
        sql.append(" AND ");
        sql.append("a.").append(CAOperation.FIELD_IDJOURNAL).append(" = e.").append(CAJournal.FIELD_IDJOURNAL);

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

        sql.append(" AND a.").append(CAOperation.FIELD_IDSECTION).append(" NOT IN (");
        sql.append("SELECT ").append(CAOperation.FIELD_IDSECTION);
        sql.append(" FROM ").append(_getCollection()).append(CAInteretMoratoire.TABLE_CAIMDCP);
        sql.append(" WHERE ");
        sql.append(CAInteretMoratoire.FIELD_MOTIFCALCUL).append(" = ").append(CAInteretMoratoire.CS_SOUMIS);
        // sql.append(" AND ");
        // sql.append(CAInteretMoratoire.FIELD_IDSECTION).append(" > 0 AND (");
        // sql.append(CAInteretMoratoire.FIELD_IDGENREINTERET).append(" = ").append(CAGenreInteret.CS_TYPE_TARDIF);
        // sql.append(" OR (");
        // sql.append(CAInteretMoratoire.FIELD_IDGENREINTERET).append(" = ").append(CAGenreInteret.CS_TYPE_COTISATIONS_PERSONNELLES);
        // sql.append(" AND ");
        // sql.append(CAInteretMoratoire.FIELD_IDSECTION).append(" <> ").append(CAInteretMoratoire.FIELD_IDSECTIONFACTURE);
        // sql.append(" )) ");
        sql.append(" GROUP BY ").append(CAOperation.FIELD_IDSECTION);
        sql.append(" ) ");

        sql.append(" GROUP BY a.").append(CAOperation.FIELD_IDSECTION).append(", a.").append(CAOperation.FIELD_MONTANT);
    }
}
