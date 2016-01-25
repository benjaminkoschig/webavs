package ch.globaz.utils.tests.load;

import globaz.globall.db.BEntity;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.utils.tests.DBToJSONDumpConfig;
import ch.globaz.utils.tests.FileUtil;
import ch.globaz.utils.tests.Persistence;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

/**
 * Classe permettant de récupérer des dumps de base de données au format json.
 * 
 * 
 * @author sce
 * 
 */
public class JsonDumpFilesProvider {

    private static Gson gson = null;
    private static HashMap<String, List<?>> mapNewPersistence = null;
    private static HashMap<String, List<?>> mapOldPersistence = null;

    /**
     * Instanciatons des maps,
     */
    static {
        JsonDumpFilesProvider.mapNewPersistence = new HashMap<String, List<?>>();
        JsonDumpFilesProvider.mapOldPersistence = new HashMap<String, List<?>>();
        JsonDumpFilesProvider.gson = new Gson();

        try {
            File rootFolder = new File(DBToJSONDumpConfig.OUTPUT_JSON_FILE.value);

            if (rootFolder.exists()) {
                iterateFolderStructure(rootFolder, DBToJSONDumpConfig.OUTPUT_JSON_FILE.value);
            } else {
                JadeLogger.error(null, "The root folder for the json files: " + rootFolder.toString()
                        + " doesn't exist!");
                System.exit(1);
            }
        } catch (FileNotFoundException e) {
            JadeLogger.error(null, e.getMessage() + ", " + e.toString());
        } catch (ClassNotFoundException e) {
            JadeLogger.error(null, e.getMessage() + ", " + e.toString());
        }

    }

    private static <T> List<T> dealFile(File file, Class<T> classe) throws FileNotFoundException,
            ClassNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(file));

        // déserialisation
        TypeToken<List<T>> token = new TypeToken<List<T>>() {
            private static final long serialVersionUID = 1L;
        }.where(new TypeParameter<T>() {
        }, classe);

        // liste de smodèles déserialisés
        List<T> modeles = JsonDumpFilesProvider.gson.fromJson(br, token.getType());

        if (JadeAbstractModel.class.isAssignableFrom(classe)) {
            JsonDumpFilesProvider.mapNewPersistence.put(classe.getName(), modeles);
        } else if (BEntity.class.isAssignableFrom(classe)) {
            JsonDumpFilesProvider.mapOldPersistence.put(classe.getName(), modeles);
        }
        // hack, permet l'inférence de type T
        return null;

    }

    private static String extractJsonFileName(String completFileName) {
        return completFileName.substring(17, completFileName.length() - 5);
    }

    public static Map<Persistence, Map<String, List<?>>> getAllDatasFind() {
        Map<Persistence, Map<String, List<?>>> datasMap = new HashMap<Persistence, Map<String, List<?>>>();
        datasMap.put(Persistence.NEW, JsonDumpFilesProvider.mapNewPersistence);
        datasMap.put(Persistence.OLD, JsonDumpFilesProvider.mapOldPersistence);
        return datasMap;
    }

    public static List<?> getListeForModele(String modele) {
        if (JsonDumpFilesProvider.mapNewPersistence.containsKey(modele)) {
            return JsonDumpFilesProvider.mapNewPersistence.get(modele);
        }

        if (JsonDumpFilesProvider.mapOldPersistence.containsKey(modele)) {
            return JsonDumpFilesProvider.mapOldPersistence.get(modele);
        }

        throw new NullPointerException("##### The model [" + modele + "] don't exist in the map");

    }

    private static void iterateFolderStructure(File rootFolder, String folderName) throws FileNotFoundException,
            ClassNotFoundException {

        int size = rootFolder.list().length;

        // si le répertoire est vide
        if (rootFolder.list().length == 0) {
            JadeLogger.info(new FileUtil(), "##### The folder contains no files : " + rootFolder.getAbsolutePath());
        } else {
            // liste du contenu
            String files[] = rootFolder.list();

            for (String temp : files) {
                // Structure
                File internFile = new File(rootFolder, temp);
                String fileName = internFile.toString();

                String modeleName = extractJsonFileName(fileName);

                Class<?> classe = Class.forName(modeleName);
                dealFile(internFile, classe);

            }
        }

    }

}
