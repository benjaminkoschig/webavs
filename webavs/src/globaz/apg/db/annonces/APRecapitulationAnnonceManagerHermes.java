package globaz.apg.db.annonces;

/**
 * Manager servant à récupérer les données pour la récapitulation des annonces APG datant d'avant septembre 2012. En
 * effet, après cette date, les annonces APG sont passées au format RAPG et une nouvelle implementation a due être
 * faite.
 * 
 * @author VRE
 * @author PBA
 * @author LGA
 * @see APRecapitulationAnnonceManager
 */
public class APRecapitulationAnnonceManagerHermes extends APAbstractListeRecapitulationAnnoncesManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected APAbstractRecapitulationAnnonce getNewEntity() {
        return new APRecapitulationAnnonceHermes();
    }

    @Override
    protected String getSqlForGroupBy() {

        StringBuilder sql = new StringBuilder();

        sql.append(APAnnonceAPG.FIELDNAME_CONTENUANNONCE);
        sql.append(", ");
        sql.append(APAnnonceAPG.FIELDNAME_GENRE);
        sql.append(", ");
        sql.append(APAnnonceAPG.FIELDNAME_MOISANNEECOMPTABLE);

        return sql.toString();
    }
}
