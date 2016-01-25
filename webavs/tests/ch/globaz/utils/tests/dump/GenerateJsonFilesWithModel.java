package ch.globaz.utils.tests.dump;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import ch.globaz.utils.tests.DBToJSONDumpConfig;
import ch.globaz.utils.tests.FileUtil;

/**
 * 
 * @author sce
 * 
 */
public class GenerateJsonFilesWithModel {

    private static final String NO_PARAMETER = ".";
    private BManager manager = null;
    private Class<?> persistenceRootClasse = null;
    private JadeAbstractSearchModel searchModel = null;

    /**
     * Point d'entrée de la génération du dump en fichier json.
     * 
     * @param modelClassName
     *            nom pleinement qualifié de la classe modèle
     * @param searchModelClassName
     *            nom pleinement qualifié de la classe modèle de recherche
     * @param searchSize
     *            taille des résultats voulus
     * @throws Exception
     *             les exception succeptibles d'être générées. Exception générique, dépendante de l'ancien framework
     *             (sic)
     */
    public GenerateJsonFilesWithModel(String modelClassName, String searchModelClassName, String searchSize)
            throws Exception {

        // detection de la persistence
        definePersistenceRootClasse(Class.forName(modelClassName));

        // récupération des classes modèle et de rechecherche
        Class<?> searchClass = resolveSearchClass(modelClassName, searchModelClassName);
        Class<?> modelClass = Class.forName(modelClassName);

        // création du répertoie cible du fichier json (ou utilisation)
        useOrCreateDestinationFolder();

        // container contenant les données et l'opération de jonification
        DataContainer dataContainer = null;

        // détermination de l'implémentation du container en fonction de la persistence
        if (persistenceRootClasse.equals(JadeAbstractModel.class)) {
            dataContainer = dealForNewPersistence(searchClass, searchSize);
        } else {
            dataContainer = dealForOldPersistence(searchClass, searchSize);
        }
        dataContainer.setModelClass(modelClass);
        // generation json
        dataContainer.createJson();

    }

    private DataContainer dealForNewPersistence(Class<?> searchClass, String searchSize) throws InstantiationException,
            IllegalAccessException, SecurityException, NoSuchMethodException, NumberFormatException,
            IllegalArgumentException, InvocationTargetException, JadePersistenceException {

        // instanciation du modele de recherche
        searchModel = (JadeAbstractSearchModel) searchClass.newInstance();

        // taille de la recherche
        Method definedSearchSize = JadeAbstractSearchModel.class
                .getDeclaredMethod("setDefinedSearchSize", Integer.TYPE);

        definedSearchSize.invoke(searchModel, Integer.parseInt(searchSize));

        searchModel = JadePersistenceManager.search(searchModel);
        List<JadeAbstractModel> entitees = Arrays.asList(searchModel.getSearchResults());

        NewPersistenceContainer ctn = new NewPersistenceContainer(entitees);

        return ctn;
    }

    private DataContainer dealForOldPersistence(Class<?> searchModelClass, String searchSize) throws Exception {
        manager = (BManager) searchModelClass.newInstance();

        // taille de la recherche
        Method definedSearchSize = BManager.class.getDeclaredMethod("changeManagerSize", Integer.TYPE);
        definedSearchSize.invoke(manager, Integer.parseInt(searchSize));// .changeManagerSize(Integer.parseInt(searchSize));
        manager.find();

        Iterator<?> iter = manager.iterator();

        List<BEntity> entites = new ArrayList<BEntity>();

        while (iter.hasNext()) {
            entites.add((BEntity) iter.next());
        }

        OldPersistenceContainer ctn = new OldPersistenceContainer(entites);

        return ctn;

    }

    private void definePersistenceRootClasse(Class<?> c) {
        if (JadeAbstractModel.class.isAssignableFrom(c)) {
            persistenceRootClasse = JadeAbstractModel.class;
        } else if (BEntity.class.isAssignableFrom(c)) {
            persistenceRootClasse = BEntity.class;
        } else {
            throw new ClassCastException("The model classe is not a child of BEntity or JadeAbstractModel");
        }

    }

    private Class<?> resolveSearchClass(String modelClassName, String searchModelClassName)
            throws ClassNotFoundException {

        Class<?> searchClass = null;

        // Si le modèle de recherche n'est pas fournit on en déduite le nom avec le modèle de base
        if (searchModelClassName.equals(GenerateJsonFilesWithModel.NO_PARAMETER)) {
            if (persistenceRootClasse.equals(JadeAbstractModel.class)) {
                searchClass = Class.forName(modelClassName
                        + DBToJSONDumpConfig.NEW_PERSISTENCE_SEARCH_CLASS_SUFFIX.value);
            } else {
                searchClass = Class.forName(modelClassName
                        + DBToJSONDumpConfig.OLD_PERSISTENCE_MANAGER_CLASS_SUFFIX.value);
            }

        } else {
            searchClass = Class.forName(searchModelClassName);
        }

        return searchClass;
    }

    private void useOrCreateDestinationFolder() throws IOException {

        File folder = new File(DBToJSONDumpConfig.OUTPUT_JSON_FILE.value);

        // Si le dossier n'existe pas, on le crée
        if (!folder.exists()) {
            FileUtil.createFolder(folder);
        }

    }

}
