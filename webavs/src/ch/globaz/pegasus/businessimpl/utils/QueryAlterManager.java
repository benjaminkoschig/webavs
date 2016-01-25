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
 *         Permet de modifier un requête avant qu'elle soit exécuté parle FW.
 * 
 *         Execute le sql par les couche du FW et peuple le searchModel par le FW.
 * 
 *         Cette classe devrait être utilisé avec parcimonie et que dans des cas bien précis et maîtrisé.
 */
public class QueryAlterManager {

    /**
     * Modifie la requête avant sont execution et execute la requête.
     * 
     * @param searchModel
     *            : SearchModel que l'on vas utiliser pour executer la requête
     * @param queryAlter
     *            : Modificateur de la requête
     * @throws JadePersistenceException
     */
    public static JadeAbstractSearchModel executSearch(JadeAbstractSearchModel searchModel, QueryAlter queryAlter)
            throws JadePersistenceException {

        JadeLogger.trace(JadePersistenceManager.class, "search(" + searchModel + ")");
        // Si le model est null, ne peut faire le traitement
        if (searchModel == null) {
            throw new JadePersistenceException("Unable to search the model, the model passed is null!");
        }
        // Contrôle que le thread context permette d'effectuer des modifications
        // en db
        if (JadeThread.currentContext() == null) {
            throw new JadePersistenceException(JadePersistenceManager.class.getName() + " - "
                    + "Unable to search the model(" + searchModel + ") without a thread context!");
        } else if (!JadeThread.currentContext().isJdbc() || (JadeThread.currentJdbcConnection() == null)) {
            throw new JadePersistenceException(JadePersistenceManager.class.getName() + " - "
                    + "Unable to search the model(" + searchModel
                    + ") without an opened connection in the current thread context!");
        }
        // Récupère le mapping pour ce modèle
        JadeAbstractSqlModelDefinition modelDefinition = JadeModelMappingProvider.getInstance().getSqlModelDefinition(
                searchModel.whichModelClass().getName());
        // Contrôle qu'il soit défini
        if (modelDefinition == null) {
            throw new JadePersistenceException(JadePersistenceManager.class.getName() + " - "
                    + "Unable to search the model(" + searchModel + "), the model mapping is not defined!");
        }
        Statement stmt = null;
        ResultSet rs = null;
        long currentTime = System.currentTimeMillis();
        // Construit la requête de recherche
        String sql = modelDefinition.getSqlSelect(searchModel);

        sql = queryAlter.alterSql(sql, searchModel, modelDefinition);

        long sqlGenerationTime = System.currentTimeMillis() - currentTime;
        long dbExecTime = 0;
        try {
            // Crée un statement de type scrollable et non modifié par la
            // concurrence
            stmt = JadeThread.currentJdbcConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            // Exécute la requête
            rs = stmt.executeQuery(sql);
            // Défini la taille du tableau de résultat, dans le cas où la
            // recherche se fait sans taille de résultat ou que le nombre de
            // résultat est inférieur au nombre de résultat, on défini la taille
            // du tableau au nombre de résultats, sinon à la taille du manager
            // size
            rs.last();
            int nbOfResult = rs.getRow();
            if ((searchModel.getDefinedSearchSize() == JadeAbstractSearchModel.SIZE_NOLIMIT)
                    || (searchModel.getDefinedSearchSize() > (nbOfResult - searchModel.getOffset()))) {
                searchModel.initSearchResult(nbOfResult - searchModel.getOffset());
            } else {
                searchModel.initSearchResult(searchModel.getDefinedSearchSize());
            }
            // Si le modèle de recherche possède un offset, déplace le resultSet
            // à la position de l'offset
            if (searchModel.getOffset() != 0) {
                rs.absolute(searchModel.getOffset());
            } else {
                rs.beforeFirst();
            }
            // Il y a des éléments de recherches supplémentaire si le manager
            // size est différents de la taille sans limite et que la taille du
            // manager additionné à l'offset est inférieur au nombre de
            // résultats
            searchModel.setHasMoreElements((searchModel.getDefinedSearchSize() != JadeAbstractSearchModel.SIZE_NOLIMIT)
                    && ((searchModel.getDefinedSearchSize() + searchModel.getOffset()) < nbOfResult));
            int idx = 0;
            dbExecTime = System.currentTimeMillis() - currentTime - sqlGenerationTime;
            // Itère tant qu'il y a des résultats && soit le manager size est
            // sans limite ou que l'index est inférieur à la taille du résultat
            // de recherche souhaité
            while (rs.next()
                    && (((searchModel.getDefinedSearchSize() == JadeAbstractSearchModel.SIZE_NOLIMIT) || (idx < searchModel
                            .getDefinedSearchSize())))) {
                searchModel.getSearchResults()[idx] = (JadeAbstractModel) searchModel.whichModelClass().newInstance();
                modelDefinition.read(rs, searchModel.getSearchResults()[idx]);

                idx++;
            }
            // Défini le nombre de résultat que lorsque tout s'est correctement déroulé
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
