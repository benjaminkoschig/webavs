package globaz.hercule.service.declarationSalaire;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * <pre>
 * Manager permettant la récupération des masses AVS reprises et du nombre de CI repris
 * pour un controle et une période donnée.
 * </pre>
 * 
 * @author Sullivann Corneille
 * @since 21 févr. 2014
 */
public class CEDecSalInfosReprisesManager extends BManager {

    private static final long serialVersionUID = -4208010299363279068L;
    private int anneeDebut = 0;
    private int anneeFin = 0;
    private String idControle;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEDecSalInfosReprises();
    }

    @Override
    protected String _getSql(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT KBNANN AS ANNEE, SUM(masse) AS MASSE_AVS, SUM(nbr) AS NB_CI FROM ( ");
        sql.append("SELECT ECR.KBNANN,dsi.kaiind,SUM(CASE WHEN KBTEXT in (0,311002,311006,311008) THEN KBMMON else -KBMMON END) AS MASSE, COUNT(*) AS NBR ");
        sql.append("FROM ").append(_getCollection()).append("DSDECLP DS ");
        sql.append("INNER JOIN ").append(_getCollection()).append("DSINDP DSI ON (DS.TAIDDE = DSI.TAIDDE) ");
        sql.append("INNER JOIN ").append(_getCollection()).append("CIECRIP ECR ON (DSI.KBIECR = ECR.KBIECR) ");
        sql.append("WHERE DS.TAICTR = ").append(idControle).append(" ");
        sql.append("AND ECR.KBNANN BETWEEN ").append(anneeDebut).append(" AND ").append(anneeFin).append(" ");
        sql.append("GROUP BY KBNANN,DSI.KAIIND ");
        sql.append("HAVING SUM(CASE WHEN KBTEXT IN (0,311002,311006,311008) THEN KBMMON else -KBMMON END) <> 0 ");
        sql.append("ORDER BY KBNANN,DSI.KAIIND DESC) AS TEMP ");
        sql.append("GROUP BY KBNANN ");

        return sql.toString();
    }

    public void setAnneeDebut(int anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    public void setAnneeFin(int anneeFin) {
        this.anneeFin = anneeFin;
    }

    public void setIdControle(String idControle) {
        this.idControle = idControle;
    }

}
