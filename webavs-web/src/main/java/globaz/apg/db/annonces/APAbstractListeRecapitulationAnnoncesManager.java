package globaz.apg.db.annonces;

import globaz.apg.api.annonces.IAPAnnonce;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

import java.util.ArrayList;
import java.util.List;

import static globaz.apg.api.annonces.IAPAnnonce.CS_PATERNITE;

/**
 * Manager regroupant les fonctionnalités communes à la recherche des données pour la récapitulation des anciennes et
 * nouvelles annonces APG (avant septembre 2012, ou après)
 * 
 * @author PBA
 * @see APRecapitulationAnnonceManager
 * @see APRecapitulationAnnonceManagerHermes
 */
public abstract class APAbstractListeRecapitulationAnnoncesManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forEtatDifferentDe;
    private Boolean forIsExclureAnnonceEnfant;
    private String forMoisAnneeComptable;
    private String forTypeAPG;

    public APAbstractListeRecapitulationAnnoncesManager() {
        super();

        forEtatDifferentDe = null;
        forIsExclureAnnonceEnfant = null;
        forMoisAnneeComptable = null;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        if (!JadeStringUtil.isIntegerEmpty(forEtatDifferentDe)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(APAnnonceAPG.FIELDNAME_ETAT);
            sql.append("<>");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), forEtatDifferentDe));
        }

        if (!JadeStringUtil.isEmpty(forMoisAnneeComptable)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(APAnnonceAPG.FIELDNAME_MOISANNEECOMPTABLE);
            sql.append(" =");
            sql.append(formatDateToDB(forMoisAnneeComptable));
        }

        if (!JadeStringUtil.isEmpty(forTypeAPG)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(APAnnonceAPG.FIELDNAME_GENRE);
            sql.append(" in ");
            switch (forTypeAPG){
                case IAPAnnonce.CS_PATERNITE:
                    sql.append("(91)");
                    break;
                default:
            }
        }
        if ((getForIsExclureAnnonceEnfant() != null) && getForIsExclureAnnonceEnfant().booleanValue()) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append("(").append(APAnnonceAPG.FIELDNAME_IDPARENT).append(" IS NULL OR ")
                    .append(APAnnonceAPG.FIELDNAME_IDPARENT).append(" = 0 ) ");
        }

        String groupBy = getSqlForGroupBy();
        if (!JadeStringUtil.isBlank(groupBy)) {
            sql.append(" GROUP BY ").append(groupBy);
        }

        return sql.toString();
    }

    @Override
    protected final APAbstractRecapitulationAnnonce _newEntity() throws Exception {
        return getNewEntity();
    }

    private String formatDateToDB(String date) {
        if (date.length() == 6) {
            return date.substring(2) + "0" + date.substring(0, 1);
        } else {
            return date.substring(3) + date.substring(0, 2);
        }
    }

    public final String getForEtatDifferentDe() {
        return forEtatDifferentDe;
    }

    public final Boolean getForIsExclureAnnonceEnfant() {
        return forIsExclureAnnonceEnfant;
    }

    public final String getForMoisAnneeComptable() {
        return forMoisAnneeComptable;
    }

    protected abstract APAbstractRecapitulationAnnonce getNewEntity();

    protected abstract String getSqlForGroupBy();

    public final void setForEtatDifferentDe(String string) {
        forEtatDifferentDe = string;
    }

    public final void setForIsExclureAnnonceEnfant(Boolean forIsExclureAnnonceEnfant) {
        this.forIsExclureAnnonceEnfant = forIsExclureAnnonceEnfant;
    }

    public final void setForMoisAnneeComptable(String string) {
        forMoisAnneeComptable = string;
    }
    public String getForTypeAPG() {
        return forTypeAPG;
    }

    public void setForTypeAPG(String forTypeAPG) {
        this.forTypeAPG = forTypeAPG;
    }

}
