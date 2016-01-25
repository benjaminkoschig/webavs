package globaz.hercule.service.declarationSalaire;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Manager permettant la récupération des masses reprises lors d'un contrôle d'employeur
 * 
 * @author Sullivann Corneille
 * @since 21 févr. 2014
 */
public class CEDecSalMassesReprisesManager extends BManager {

    private static final long serialVersionUID = 1674356024115082741L;
    private String idControle = "";

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEDecSalMassesReprises();
    }

    @Override
    protected String _getSql(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        sql.append("select sum(MASSE_AVS) AS MASSE_AVS,sum(MASSE_AC1) AS MASSE_AC1, sum(MASSE_AC2) AS MASSE_AC2, sum(MASSE_AF) AS MASSE_AF, sum(NB_REPRISE) AS NB_REPRISE from (");
        sql.append("SELECT SUM(CASE WHEN KBTEXT in (0,311002,311006,311008) THEN KBMMON ELSE -KBMMON END) AS MASSE_AVS, SUM(DSI.TEMAI) AS MASSE_AC1, SUM(DSI.TEMAII) AS MASSE_AC2, SUM(DSI.TEMAF)  AS MASSE_AF, count(*) AS NB_REPRISE ");
        sql.append("FROM ").append(_getCollection()).append("DSDECLP DS ");
        sql.append("INNER JOIN ").append(_getCollection()).append("DSINDP DSI ON (DS.TAIDDE = DSI.TAIDDE) ");
        sql.append("LEFT JOIN ").append(_getCollection()).append("CIECRIP ECR ON (DSI.KBIECR = ECR.KBIECR) ");
        sql.append("where TAICTR = " + idControle + " ");
        sql.append("group by dsi.kaiind ");
        sql.append("having sum(case when kbtext in (0,311002,311006,311008) then kbmmon else -kbmmon end) <> 0) as TEMP");

        return sql.toString();
    }

    /**
     * Getter de idControle
     * 
     * @return the idControle
     */
    public String getIdControle() {
        return idControle;
    }

    /**
     * Setter de idControle
     * 
     * @param idControle the idControle to set
     */
    public void setIdControle(String idControle) {
        this.idControle = idControle;
    }

}
