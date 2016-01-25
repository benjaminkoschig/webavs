package ch.globaz.pegasus.businessimpl.utils;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.mapping.JadeModelMappingProvider;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.sql.JadeAbstractSqlModelDefinition;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author dma
 * 
 *         Permet de modifier un requ�te avant qu'elle soit ex�cut� parle FW.
 * 
 *         Execute le sql par les couche du FW et peuple le searchModel par le FW.
 * 
 *         Cette classe devrait �tre utilis� avec parcimonie et que dans des cas bien pr�cis et ma�tris�.
 */
public class QueryAlterManager {

    /**
     * Modifie la requ�te avant sont execution et execute la requ�te.
     * 
     * @param searchModel
     *            : SearchModel que l'on vas utiliser pour executer la requ�te
     * @param queryAlter
     *            : Modificateur de la requ�te
     * @throws JadePersistenceException
     */
    public static JadeAbstractSearchModel executSearch(JadeAbstractSearchModel searchModel, QueryAlter queryAlter)
            throws JadePersistenceException {

        JadeLogger.trace(JadePersistenceManager.class, "search(" + searchModel + ")");
        // Si le model est null, ne peut faire le traitement
        if (searchModel == null) {
            throw new JadePersistenceException("Unable to search the model, the model passed is null!");
        }
        // Contr�le que le thread context permette d'effectuer des modifications
        // en db
        if (JadeThread.currentContext() == null) {
            throw new JadePersistenceException(JadePersistenceManager.class.getName() + " - "
                    + "Unable to search the model(" + searchModel + ") without a thread context!");
        } else if (!JadeThread.currentContext().isJdbc() || (JadeThread.currentJdbcConnection() == null)) {
            throw new JadePersistenceException(JadePersistenceManager.class.getName() + " - "
                    + "Unable to search the model(" + searchModel
                    + ") without an opened connection in the current thread context!");
        }
        // R�cup�re le mapping pour ce mod�le
        JadeAbstractSqlModelDefinition modelDefinition = JadeModelMappingProvider.getInstance().getSqlModelDefinition(
                searchModel.whichModelClass().getName());
        // Contr�le qu'il soit d�fini
        if (modelDefinition == null) {
            throw new JadePersistenceException(JadePersistenceManager.class.getName() + " - "
                    + "Unable to search the model(" + searchModel + "), the model mapping is not defined!");
        }
        Statement stmt = null;
        ResultSet rs = null;
        long currentTime = System.currentTimeMillis();
        // Construit la requ�te de recherche
        String sql = modelDefinition.getSqlSelect(searchModel);

        sql = queryAlter.alterSql(sql, searchModel, modelDefinition);

        long sqlGenerationTime = System.currentTimeMillis() - currentTime;
        long dbExecTime = 0;
        try {
            // Cr�e un statement de type scrollable et non modifi� par la
            // concurrence
            stmt = JadeThread.currentJdbcConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            // Ex�cute la requ�te
            rs = stmt.executeQuery(sql);
            // D�fini la taille du tableau de r�sultat, dans le cas o� la
            // recherche se fait sans taille de r�sultat ou que le nombre de
            // r�sultat est inf�rieur au nombre de r�sultat, on d�fini la taille
            // du tableau au nombre de r�sultats, sinon � la taille du manager
            // size
            rs.last();
            int nbOfResult = rs.getRow();
            if ((searchModel.getDefinedSearchSize() == JadeAbstractSearchModel.SIZE_NOLIMIT)
                    || (searchModel.getDefinedSearchSize() > (nbOfResult - searchModel.getOffset()))) {
                searchModel.initSearchResult(nbOfResult - searchModel.getOffset());
            } else {
                searchModel.initSearchResult(searchModel.getDefinedSearchSize());
            }
            // Si le mod�le de recherche poss�de un offset, d�place le resultSet
            // � la position de l'offset
            if (searchModel.getOffset() != 0) {
                rs.absolute(searchModel.getOffset());
            } else {
                rs.beforeFirst();
            }
            // Il y a des �l�ments de recherches suppl�mentaire si le manager
            // size est diff�rents de la taille sans limite et que la taille du
            // manager additionn� � l'offset est inf�rieur au nombre de
            // r�sultats
            searchModel.setHasMoreElements((searchModel.getDefinedSearchSize() != JadeAbstractSearchModel.SIZE_NOLIMIT)
                    && ((searchModel.getDefinedSearchSize() + searchModel.getOffset()) < nbOfResult));
            int idx = 0;
            dbExecTime = System.currentTimeMillis() - currentTime - sqlGenerationTime;
            // It�re tant qu'il y a des r�sultats && soit le manager size est
            // sans limite ou que l'index est inf�rieur � la taille du r�sultat
            // de recherche souhait�
            while (rs.next()
                    && (((searchModel.getDefinedSearchSize() == JadeAbstractSearchModel.SIZE_NOLIMIT) || (idx < searchModel
                            .getDefinedSearchSize())))) {
                searchModel.getSearchResults()[idx] = (JadeAbstractModel) searchModel.whichModelClass().newInstance();
                modelDefinition.read(rs, searchModel.getSearchResults()[idx]);

                idx++;
            }
            // D�fini le nombre de r�sultat que lorsque tout s'est correctement d�roul�
            searchModel.setNbOfResultMatchingQuery(nbOfResult);
        } catch (Exception e) {
            throw new JadePersistenceException(JadePersistenceManager.class.getName() + " - "
                    + "Unable to search for model(" + searchModel + ")!", e);
        } finally {
            if (JadeModelMappingProvider.getInstance().isVerbose()) {
                JadeLogger.info(JadePersistenceManager.class,
                        "Exec. time (Total :" + (System.currentTimeMillis() - currentTime) + "ms - DB :" + dbExecTime
                                + "ms - SQL instanciation :" + sqlGenerationTime + "ms) - Perform search ("
                                + searchModel.whichModelClass() + ") - Query : " + sql);
            }
            try {
                rs.close();
            } catch (SQLException e) {
                JadeLogger.warn(JadePersistenceManager.class,
                        "Problem to close results set in persistence manager, reason : " + e.toString());
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    JadeLogger.warn(JadePersistenceManager.class,
                            "Problem to close statement in persistence manager, reason : " + e.toString());
                }
            }
        }
        return searchModel;
    }
}
