package globaz.apg.db.annonces;

/**
 * Manager servant à récupérer les données pour la récapitulation des annonces APG datant d'après septembre 2012. En
 * effet, après cette date, les annonces APG sont passées au format RAPG.
 * 
 * @author VRE
 * @author PBA
 * @see APRecapitulationAnnonceManagerHermes
 */
public class APRecapitulationAnnonceManager extends APAbstractListeRecapitulationAnnoncesManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected APAbstractRecapitulationAnnonce getNewEntity() {
        return new APRecapitulationAnnonce();
    }

    @Override
    protected String getSqlForGroupBy() {

        StringBuilder sql = new StringBuilder();

        sql.append(APAnnonceAPG.FIELDNAME_SUB_MESSAGE_TYPE);
        sql.append(", ");
        sql.append(APAnnonceAPG.FIELDNAME_GENRE);
        sql.append(", ");
        sql.append(APAnnonceAPG.FIELDNAME_MOISANNEECOMPTABLE);

        return sql.toString();
    }
}
