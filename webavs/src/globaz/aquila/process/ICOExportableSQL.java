package globaz.aquila.process;

import globaz.globall.db.BTransaction;

/**
 * <h1>Description</h1>
 * <p>
 * Interface à implementer par les entités pour pouvoir être transformées en requête SQL.
 * </p>
 * 
 * @author vre
 */
public interface ICOExportableSQL {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Renseigne dans le builder tous les noms et valeurs de propriétés de cette entité exportable.
     * 
     * @param query
     *            builder de la requête.
     * @param transaction
     */
    public void export(COInsertQueryBuilder query, BTransaction transaction);

    /**
     * Retourne le nom de la table pour cette entité exportable.
     * 
     * @return le nom de la table
     */
    public String getTableName();
}
