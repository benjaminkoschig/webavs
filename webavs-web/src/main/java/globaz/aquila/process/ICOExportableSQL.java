package globaz.aquila.process;

import globaz.globall.db.BTransaction;

/**
 * <h1>Description</h1>
 * <p>
 * Interface � implementer par les entit�s pour pouvoir �tre transform�es en requ�te SQL.
 * </p>
 * 
 * @author vre
 */
public interface ICOExportableSQL {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Renseigne dans le builder tous les noms et valeurs de propri�t�s de cette entit� exportable.
     * 
     * @param query
     *            builder de la requ�te.
     * @param transaction
     */
    public void export(COInsertQueryBuilder query, BTransaction transaction);

    /**
     * Retourne le nom de la table pour cette entit� exportable.
     * 
     * @return le nom de la table
     */
    public String getTableName();
}
