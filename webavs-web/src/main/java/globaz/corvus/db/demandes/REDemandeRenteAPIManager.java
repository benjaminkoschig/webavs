package globaz.corvus.db.demandes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class REDemandeRenteAPIManager extends REDemandeRenteManager {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return REDemandeRenteAPI.createFromClause(_getCollection());
    }

    /**
     * Création d'une nouvelle entité de type REDemandeRenteAPI
     * 
     * @return Une nouvelle entité de type REDemandeRenteAPI
     * 
     * @throws Exception
     * 
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REDemandeRenteAPI();
    }

}
