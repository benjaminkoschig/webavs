package globaz.tucana.transfert;

import globaz.globall.db.BEntity;

/**
 * Support d'une BEntity pour l'importation
 * 
 * @author fgo
 * 
 */
public class TUImportEntity {
    private BEntity entity = null;

    /**
     * Constructeur
     * 
     * @param _entity
     */
    public TUImportEntity(BEntity _entity) {
        entity = _entity;
    }

    /**
     * Retrourne l'attribut entity de la classe
     * 
     * @return
     */
    public BEntity getEntity() {
        return entity;
    }

}
