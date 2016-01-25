package globaz.apg.db.annonces;

/**
 * Manager servant � r�cup�rer les donn�es pour la r�capitulation des annonces APG datant d'apr�s septembre 2012. En
 * effet, apr�s cette date, les annonces APG sont pass�es au format RAPG.
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
