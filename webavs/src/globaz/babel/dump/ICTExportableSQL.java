package globaz.babel.dump;

import globaz.globall.db.BTransaction;

/**
 * implementer pour pouvoir exporter.
 * 
 * @author vre
 */
public interface ICTExportableSQL {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param query
     *            requete
     * @param transaction
     *            DOCUMENT ME!
     */
    void export(CTInsertQueryBuilder query, BTransaction transaction);
}
