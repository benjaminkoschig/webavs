package ch.globaz.utils.tests.dump;

import globaz.jade.log.JadeLogger;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import ch.globaz.utils.tests.DBToJSONDumpConfig;
import ch.globaz.utils.tests.FileUtil;
import com.google.gson.Gson;

/**
 * Classe abstraite mere permattant de d�finir le traitement minimal des container de donn�es afin de g�n�rer des dumps
 * json
 * 
 * @author sce
 * 
 */
abstract class DataContainer {

    Gson gson = null;
    private Class<?> modelClass = null;
    PrintWriter out = null;
    String path = null;

    public void closeJson() {
        out.flush();
        out.close();

        JadeLogger.info(new FileUtil(), "########### Fichier cr�� : " + path);
    }

    public abstract void createJson();

    public void prepareJson() {
        gson = new Gson();
        out = null;

        // chemin du fichier
        path = DBToJSONDumpConfig.OUTPUT_JSON_FILE.value + modelClass.getName() + ".json";
        try {
            out = new PrintWriter(path);
        } catch (FileNotFoundException e) {
            JadeLogger.error(this, "##### " + e.getMessage() + ", " + e.toString());
        }
    };

    public void setModelClass(Class<?> modelClass) {
        this.modelClass = modelClass;
    }

}
