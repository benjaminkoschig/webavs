package globaz.hercule.service.facturation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * </pre>Manager permettant la récupération des données de facturation (Total et date)
 * pour un affilié et un contrôle donnée</pre>
 * 
 * @author Sullivann Corneille
 * @since 21 févr. 2014
 */
public class CEEnteteFactureManager extends BManager {

    private static final long serialVersionUID = 1348696997725369928L;
    private String idControle = "";

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEEnteteFacture();
    }

    @Override
    protected String _getSql(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT TOTALFACTURE, PA.DATEFACTURATION ");
        sql.append("FROM ").append(_getCollection()).append("FAENTFP EF ");
        sql.append("INNER JOIN ").append(_getCollection()).append("FAPASSP PA ON (EF.IDPASSAGE = PA.IDPASSAGE) ");
        sql.append("WHERE EF.IDCONTROLE = " + idControle);

        return sql.toString();
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
